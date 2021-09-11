// FCMOD

package net.minecraft.src;

public class FCItemBucketEmpty extends FCItemBucket
{
    public FCItemBucketEmpty( int iItemID )
    {
    	super( iItemID );
    	
    	setMaxStackSize( 16 );
    	
    	setUnlocalizedName( "bucket" );
	}
    
    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockBucketEmpty.blockID;
    }

    @Override
    public ItemStack onItemRightClick( ItemStack itemStack, World world, EntityPlayer player )
    {
    	MovingObjectPosition posClicked = 
    		FCUtilsMisc.GetMovingObjectPositionFromPlayerHitWaterAndLava( world, player, true );
        
        if ( posClicked != null && posClicked.typeOfHit == EnumMovingObjectType.TILE )
        {
            int i = posClicked.blockX;
            int j = posClicked.blockY;
            int k = posClicked.blockZ;
            
            int iBlockID = world.getBlockId( i, j, k );
            
            if ( world.getBlockMaterial( i, j, k ) == Material.water )
            {
    			if ( FCUtilsMisc.DoesWaterHaveValidSource( world, i, j, k, 128 ) )
				{        			
                    if ( --itemStack.stackSize <= 0 )
                    {
                    	return new ItemStack(Item.bucketWater);
                    }                    
                    else if ( !player.inventory.addItemStackToInventory( 
                    	new ItemStack( Item.bucketWater ) ) )
                    {
                    	player.dropPlayerItem( new ItemStack( Item.bucketWater.itemID, 1, 0 ) );
                    }	
				}
    			
                return itemStack;
            }                
            else if ( world.getBlockMaterial( i, j, k ) == Material.lava )
            {
            	player.dealFireDamage( 1 );

                world.playSoundEffect( (double)i  + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
            		"random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                
            	if ( world.isRemote )
            	{
                    for(int l = 0; l < 8; l++)
                    {
                        world.spawnParticle("largesmoke", (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
            	}
                
                return itemStack;
            }
        } 
        
        return itemStack;
    }

	//------------- Class Specific Methods ------------//
	
    private boolean IsPlayerClickingOnWaterOrLava( ItemStack stack, World world, EntityPlayer player )
    {
        MovingObjectPosition pos = FCUtilsMisc.GetMovingObjectPositionFromPlayerHitWaterAndLava( world, player, true );

        if ( pos != null )
        {
            if ( pos.typeOfHit == EnumMovingObjectType.TILE )
            {
                Material material = world.getBlockMaterial( pos.blockX, pos.blockY, pos.blockZ );
                
                if ( material == Material.water || material == Material.lava )
                {
                	return true;
                }
            }
        }
        
    	return false;
    }
	
	//----------- Client Side Functionality -----------//
}