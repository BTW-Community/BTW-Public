// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBucketCement extends FCBlockBucketFull
{
    public FCBlockBucketCement( int iBlockID )
    {
        super( iBlockID );
    	
    	setUnlocalizedName( "fcItemBucketCement" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneMod )
    {
		return FCBetterThanWolves.fcItemBucketCement.itemID;
    }
	
	//------------- Class Specific Methods ------------//
	
	@Override
    public boolean AttemptToSpillIntoBlock( World world, int i, int j, int k )
    {
        if ( ( world.isAirBlock( i, j, k ) || !world.getBlockMaterial( i, j, k ).isSolid() ) )
        {            
	    	world.playSoundEffect( i, j, k, "mob.ghast.moan", 
				0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
	    	
	    	if ( world.isAirBlock( i, j - 1, k ) || !world.getBlockMaterial( i, j - 1, k ).isSolid() )
	    	{
	    		// if the block below the target is empty, place there instead so the player can
	    		// pour over ledges without destroying the bucket block
	    		
	    		j--;
	    	}
	    	
    		world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcCement.blockID );
            
            return true;
        }
        
        return false;
    }
	
	//----------- Client Side Functionality -----------//
}
