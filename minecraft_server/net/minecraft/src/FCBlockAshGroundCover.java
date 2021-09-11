// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockAshGroundCover extends FCBlockGroundCover
{
    protected FCBlockAshGroundCover( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialAsh );
        
        setTickRandomly( true );
		
        setStepSound( soundSandFootstep );
        
        setUnlocalizedName( "fcBlockAshGroundCover" );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        if ( world.IsRainingAtPos( i, j, k ) )
        {
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return 0;
    }
	
    @Override
    public boolean GetCanGrassGrowUnderBlock( World world, int i, int j, int k, boolean bGrassOnHalfSlab )
    {
    	return false;
    }
    
    @Override
    public boolean GetCanBlockBeReplacedByFire( World world, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
	
    public static boolean CanAshReplaceBlock( World world, int i, int j, int k )
    {
    	Block block = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	return block == null || block.IsAirBlock() || ( block.IsGroundCover() && block != FCBetterThanWolves.fcBlockAshGroundCover );
    }
    
    public static boolean AttemptToPlaceAshAt( World world, int i, int j, int k )
    {
        if ( FCBlockAshGroundCover.CanAshReplaceBlock( world, i, j, k ) )
        {
	    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
	    	Block blockBelow = Block.blocksList[iBlockBelowID];
	    	
	    	if ( blockBelow != null && blockBelow.CanGroundCoverRestOnBlock( world, i, j - 1, k ) )
	    	{
	    		world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockAshGroundCover.blockID );
	    		
	    		return true;
	    	}
        }
        
        return false;
    }
    
	//----------- Client Side Functionality -----------//
}
        
