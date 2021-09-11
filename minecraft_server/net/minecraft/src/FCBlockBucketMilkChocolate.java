// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBucketMilkChocolate extends FCBlockBucketFull
{
    public FCBlockBucketMilkChocolate( int iBlockID )
    {
        super( iBlockID );
    	
    	setUnlocalizedName( "fcBlockBucketMilkChocolate" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneMod )
    {
		return FCBetterThanWolves.fcItemBucketMilkChocolate.itemID;
    }
	
	//------------- Class Specific Methods ------------//
	
	@Override
    public boolean AttemptToSpillIntoBlock( World world, int i, int j, int k )
    {
        if ( ( world.isAirBlock( i, j, k ) || !world.getBlockMaterial( i, j, k ).isSolid() ) )
        {     
    		world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockMilkChocolate.blockID );
            
            return true;
        }
        
        return false;
    }
	
	//----------- Client Side Functionality -----------//
}
