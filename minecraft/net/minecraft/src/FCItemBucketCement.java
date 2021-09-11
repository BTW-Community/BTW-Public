// FCMOD

package net.minecraft.src;

public class FCItemBucketCement extends FCItemBucketFull
{
    public FCItemBucketCement( int iBlockID )
    {
    	super( iBlockID );
    	
    	setUnlocalizedName( "fcItemBucketCement" );
	}

    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockBucketCement.blockID;
    }

    @Override
	protected boolean AttemptPlaceContentsAtLocation( World world, int i, int j, int k )
	{
        if ( ( world.isAirBlock( i, j, k ) || !world.getBlockMaterial( i, j, k ).isSolid() ) )
        {            
    		if ( !world.isRemote )
    		{        			
    	    	world.playSoundEffect( i, j, k, "mob.ghast.moan", 
    				0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
    	    	
    			world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcCement.blockID );
    		}
            
            return true;
        }
        
        return false;
	}
}