// FCMOD

package net.minecraft.src;

public class FCItemBattleAxe extends FCItemAxe
{
    private final int m_iWeaponDamage;
    
    protected FCItemBattleAxe( int i )
    {
        super( i, EnumToolMaterial.SOULFORGED_STEEL );
        
        m_iWeaponDamage = 4 + toolMaterial.getDamageVsEntity();        
        
        setUnlocalizedName( "fcItemAxeBattle" );        
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse( itemstack, getMaxItemUseDuration( itemstack ) );
        
        return itemstack;
    }

    @Override
    public int getDamageVsEntity( Entity entity )
    {
        return m_iWeaponDamage;
    }

    @Override
    public boolean hitEntity( ItemStack stack, EntityLiving defendingEntity, EntityLiving attackingEntity )
    {
        stack.damageItem( 1, attackingEntity );
        
        return true;
    }

    @Override
    public float getStrVsBlock( ItemStack stack, World world, Block block, int i, int j, int k)
    {
        if ( block.blockID == Block.web.blockID || block.blockID == FCBetterThanWolves.fcBlockWeb.blockID )
    	{
    		return 15F;
    	}
    	
    	return super.getStrVsBlock( stack, world, block, i, j, k );
    }

    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
        if ( block.blockID == Block.web.blockID || block.blockID == FCBetterThanWolves.fcBlockWeb.blockID )
        {
        	return true;
        }
        
    	return super.canHarvestBlock( stack, world, block, i, j, k );
    }

    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
        if ( block.blockID == Block.web.blockID || block.blockID == FCBetterThanWolves.fcBlockWeb.blockID )
    	{
    		return true;
    	}
    	
    	return super.IsEfficientVsBlock( stack, world, block, i, j, k );
    }    
    
    @Override
    public boolean IsEnchantmentApplicable( Enchantment enchantment )
    {
    	if ( enchantment.type == EnumEnchantmentType.weapon )
    	{
    		return true;
    	}
    	
    	return super.IsEnchantmentApplicable( enchantment );
    }    
    
    @Override
    protected boolean GetCanBePlacedAsBlock()
    {
    	return false;
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}