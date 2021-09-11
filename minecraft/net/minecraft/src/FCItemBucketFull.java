// FCMOD

package net.minecraft.src;

public abstract class FCItemBucketFull extends FCItemBucket
{
    public FCItemBucketFull( int iItemID )
    {
    	super( iItemID );
	}
    
    @Override
    public ItemStack onItemRightClick( ItemStack itemStack, World world, EntityPlayer player )
    {
    	MovingObjectPosition posClicked = getMovingObjectPositionFromPlayer( world, player, false );
        
        if ( posClicked != null && posClicked.typeOfHit == EnumMovingObjectType.TILE &&
        	world.canMineBlock( player, posClicked.blockX, posClicked.blockY, posClicked.blockZ ) )
        {
        	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( posClicked.blockX,
	            posClicked.blockY, posClicked.blockZ, posClicked.sideHit );

        	if ( player.canPlayerEdit( targetPos.i, targetPos.j, targetPos.k, 
        		posClicked.sideHit, itemStack ) && 
            	AttemptPlaceContentsAtLocation( world, targetPos.i, targetPos.j, targetPos.k ) )
        	{
                if ( !player.capabilities.isCreativeMode )
                {
                    return new ItemStack( Item.bucketEmpty );
                }
        	}
        } 
        
        return itemStack;
    }
    
	//------------- Class Specific Methods ------------//

	protected abstract boolean AttemptPlaceContentsAtLocation( World world, int i, int j, int k );
	
	//----------- Client Side Functionality -----------//
}
