// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCobblestoneLooseStairs extends FCBlockMortarReceiverStairs
{
    protected FCBlockCobblestoneLooseStairs( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcBlockCobblestoneLoose, 0 );
        
        SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
        
        setUnlocalizedName( "fcBlockCobblestoneLooseStairs" );        
    }
	
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockAndMetadataWithNotify( i, j, k, Block.stairsCobblestone.blockID, 
			world.getBlockMetadata( i, j, k ) );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}