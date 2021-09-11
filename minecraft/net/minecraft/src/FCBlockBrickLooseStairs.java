// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBrickLooseStairs extends FCBlockMortarReceiverStairs
{
    protected FCBlockBrickLooseStairs( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcBlockBrickLoose, 0 );
        
        SetPicksEffectiveOn();
        
        setUnlocalizedName( "fcBlockBrickLooseStairs" );        
    }
	
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockAndMetadataWithNotify( i, j, k, Block.stairsBrick.blockID, 
			world.getBlockMetadata( i, j, k ) );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}