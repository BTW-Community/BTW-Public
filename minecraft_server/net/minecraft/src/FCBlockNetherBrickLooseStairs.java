// FCMOD

package net.minecraft.src;

public class FCBlockNetherBrickLooseStairs extends FCBlockMortarReceiverStairs
{
    protected FCBlockNetherBrickLooseStairs( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcBlockNetherBrickLoose, 0 );
        
        SetPicksEffectiveOn();
        
        setUnlocalizedName( "fcBlockNetherBrickLooseStairs" );        
    }
	
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockAndMetadataWithNotify( i, j, k, Block.stairsNetherBrick.blockID, 
			world.getBlockMetadata( i, j, k ) );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}