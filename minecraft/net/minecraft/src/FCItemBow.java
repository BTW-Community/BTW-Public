// FCMOD

package net.minecraft.src;

public class FCItemBow extends ItemBow
{
    public FCItemBow( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();        
    	SetFurnaceBurnTime( 3 * FCEnumFurnaceBurnTime.SHAFT.m_iBurnTime );
        SetIncineratedInCrucible();

		SetInfernalMaxEnchantmentCost( 30 );
		SetInfernalMaxNumEnchants( 3 );
        
        setUnlocalizedName( "bow" );
    }

    @Override
    public void onPlayerStoppedUsing( ItemStack itemStack, World world, EntityPlayer player, int iTicksInUseRemaining )
    {
        ItemStack arrowStack = GetFirstArrowStackInHotbar( player );
        
        boolean bInfiniteArrows = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel( Enchantment.infinity.effectId, itemStack ) > 0;
        
        if ( arrowStack != null || bInfiniteArrows )
        {
            float fPullStrength = GetCurrentPullStrength( player, itemStack, iTicksInUseRemaining );
            
            if ( fPullStrength < 0.1F )
            {
                return;
            }
            
            EntityArrow entityArrow;
            
            if ( arrowStack != null )
            {
            	entityArrow = CreateArrowEntityForItem( world, player, arrowStack.itemID, fPullStrength ); 
            	
        		player.inventory.consumeInventoryItem( arrowStack.itemID );
            }
            else
            {
            	entityArrow = new FCEntityInfiniteArrow( world, player, fPullStrength * GetPullStrengthToArrowVelocityMultiplier() );            	
            }
  
            if ( entityArrow != null )
            {
	            if ( fPullStrength == 1.0F )
	            {
	                entityArrow.setIsCritical( true );
	            }
	            
	            ApplyBowEnchantmentsToArrow( itemStack, entityArrow );	
	            
	            if ( !world.isRemote )
	            {
	                world.spawnEntityInWorld( entityArrow );
	            }
            }
            
            itemStack.damageItem( 1, player );
            
            PlayerBowSound( world, player, fPullStrength );
            
            if ( itemStack.stackSize == 0 )
            {
                player.inventory.mainInventory[player.inventory.currentItem] = null;            	
            }            
        }        
    }

    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	// Function overriden to not require infinity bows to have a supply of arrows
    	
        if ( player.capabilities.isCreativeMode || 
        	GetFirstArrowStackInHotbar( player ) != null ||
    		EnchantmentHelper.getEnchantmentLevel( Enchantment.infinity.effectId, stack ) > 0 )
        {
            player.setItemInUse( stack, getMaxItemUseDuration( stack ) );
        }

        return stack;
    }
    
    @Override
    public int getItemEnchantability()
    {
        return 0;
    }
    
    @Override
    public boolean IsEnchantmentApplicable( Enchantment enchantment )
    {
    	if ( enchantment.type == EnumEnchantmentType.bow )
    	{
    		return true;
    	}
    	
    	return super.IsEnchantmentApplicable( enchantment );
    }
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
		if ( outputStack.itemID == Item.stick.itemID )
		{
	    	if ( !player.worldObj.isRemote )
	    	{
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, new ItemStack( Item.silk ) ); 
	    	}
	    	
	    	if ( player.m_iTimesCraftedThisTick == 0 )
			{
				player.playSound( "random.bow", 0.25F, player.worldObj.rand.nextFloat() * 0.25F + 1.5F );
			}
		}
    }
    
    //------------- Class Specific Methods ------------//

    protected float GetCurrentPullStrength( EntityPlayer player, ItemStack itemStack, int iTicksInUseRemaining )
    {
        int iTicksInUse = getMaxItemUseDuration( itemStack ) - iTicksInUseRemaining;
        
        float fPullStrength = (float)iTicksInUse / 20F;
        fPullStrength = ( fPullStrength * fPullStrength + fPullStrength * 2.0F ) / 3F;
        
        if ( fPullStrength > 1.0F )
        {
            fPullStrength = 1.0F;
        }
        
        fPullStrength *= player.GetBowPullStrengthModifier();        
        
        return fPullStrength;
    }
    
    public ItemStack GetFirstArrowStackInHotbar( EntityPlayer player )
    {
    	for ( int iTempSlot = 0; iTempSlot < 9; iTempSlot++ )
    	{
    		ItemStack tempStack = player.inventory.getStackInSlot( iTempSlot );
    		
    		if ( tempStack != null && CanItemBeFiredAsArrow( tempStack.itemID ) )
    		{
    			return tempStack;
    		}
    	}
    	
    	return null;
    }
    
    public boolean CanItemBeFiredAsArrow( int iItemID )
    {    	
    	return iItemID == Item.arrow.itemID || iItemID == FCBetterThanWolves.fcItemRottenArrow.itemID;
    }
    
    public float GetPullStrengthToArrowVelocityMultiplier()
    {
    	return 2.0F;
    }
    
	protected EntityArrow CreateArrowEntityForItem( World world, EntityPlayer player, int iItemID, float fPullStrength )
	{
		EntityArrow entityArrow = null;
		
		if ( iItemID == FCBetterThanWolves.fcItemRottenArrow.itemID )
		{
			entityArrow = new FCEntityRottenArrow( world, player, fPullStrength * 0.55F * GetPullStrengthToArrowVelocityMultiplier() );
		}
		else if ( iItemID == Item.arrow.itemID )
		{
			entityArrow = new EntityArrow( world, player, fPullStrength * GetPullStrengthToArrowVelocityMultiplier() );
		}
		
		return entityArrow;
	}
	
    protected void ApplyBowEnchantmentsToArrow( ItemStack bowStack, EntityArrow entityArrow )
    {
        int iPowerLevel = EnchantmentHelper.getEnchantmentLevel( Enchantment.power.effectId, bowStack );
    	
        if ( iPowerLevel > 0 )
        {
            entityArrow.setDamage( entityArrow.getDamage() + (double)iPowerLevel * 0.5D + 0.5D );
        }

        int iPunchLevel = EnchantmentHelper.getEnchantmentLevel( Enchantment.punch.effectId, bowStack );

        if (iPunchLevel > 0)
        {
            entityArrow.setKnockbackStrength(iPunchLevel);
        }

        if ( EnchantmentHelper.getEnchantmentLevel( Enchantment.flame.effectId, bowStack ) > 0 )
        {
            entityArrow.setFire(100);
        }
    }
    
    protected void PlayerBowSound( World world, EntityPlayer player, float fPullStrength )
    {
    	world.playSoundAtEntity( player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + fPullStrength * 0.5F );
    }
}