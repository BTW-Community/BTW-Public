// FCMOD 

package net.minecraft.src;

public class FCItemBlockTorchIdle extends ItemBlock
{	
    public FCItemBlockTorchIdle( int iItemID )
    {
        super( iItemID );
        
        setUnlocalizedName( "fcBlockTorchIdle" );
    }
    
    @Override
    public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	if ( IsPlayerClickingOnIgniter( stack, world, player ) )
		{
    		// override normal block place behavior so it goes to onItemRightClick() below
    		
    		return false;
		}
        
        return super.onItemUse(  stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ );
    }
    
    @Override
    public boolean GetCanItemBeSetOnFireOnUse( int iItemDamage )
    {
    	return true;
    }
    
    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	// even though this is an ItemBlock, this function is called if the player doesn't click on a solid block
    	// which happens here if you click on a big lake of lava
    	
    	if ( IsPlayerClickingOnIgniter( stack, world, player ) )
    	{
    		return OnRightClickOnIgniter( stack, world, player );
    	}
    		
    	return super.onItemRightClick( stack, world, player );
    }    

	//------------- Class Specific Methods ------------//
	
    protected ItemStack OnRightClickOnIgniter( ItemStack stack, World world, EntityPlayer player )
    {
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY);
        int k = MathHelper.floor_double(player.posZ);
        
        player.playSound( "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F );
        
        ItemStack newStack = new ItemStack( FCBetterThanWolves.fcBlockTorchNetherBurning, stack.stackSize, 0 );
        
    	return newStack; 
    }    

    protected boolean IsPlayerClickingOnIgniter( ItemStack stack, World world, EntityPlayer player )
    {
    	return IsPlayerClickingOnSolidIgniter( stack, world, player ) || IsPlayerClickingOnLavaOrFire( stack, world, player );
    }
    
    private boolean IsPlayerClickingOnSolidIgniter( ItemStack stack, World world, EntityPlayer player )
    {
        MovingObjectPosition pos = getMovingObjectPositionFromPlayer( world, player, true );

        if ( pos != null )
        {
            if ( pos.typeOfHit == EnumMovingObjectType.TILE )
            {
            	Block targetBlock = Block.blocksList[world.getBlockId( pos.blockX, pos.blockY, pos.blockZ )];
            	
        		if ( targetBlock != null && targetBlock.GetCanBlockLightItemOnFire( world, pos.blockX, pos.blockY, pos.blockZ ) )
                {
                	return true;
                }
            }
        }
        
    	return false;
    }
    
    private boolean IsPlayerClickingOnLavaOrFire( ItemStack stack, World world, EntityPlayer player )
    {
        MovingObjectPosition pos = FCUtilsMisc.GetMovingObjectPositionFromPlayerHitWaterAndLavaAndFire( world, player, true );

        if ( pos != null )
        {
            if ( pos.typeOfHit == EnumMovingObjectType.TILE )
            {
                Material material = world.getBlockMaterial( pos.blockX, pos.blockY, pos.blockZ );
                
                if ( material == Material.lava || material == Material.fire )
                {
                	return true;
                }
            }
        }
        
    	return false;
    }
    
    //----------- Client Side Functionality -----------//
    
    @Override
    public boolean canPlaceItemBlockOnSide( World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack )
    {
    	if ( IsPlayerClickingOnIgniter( stack, world, player ) )
		{
    		return true;
		}
    	
        return super.canPlaceItemBlockOnSide( world, i, j, k, iFacing, player, stack );
    }
}
