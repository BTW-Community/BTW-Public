// FCMOD

package net.minecraft.src;

public class FCBlockNetherBrickLoose extends FCBlockMortarReceiver
{
    public FCBlockNetherBrickLoose( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialNetherRock );
        
        setHardness( 1F ); // setHardness( 2F ); regular nether brick
        setResistance( 5F ); // setResistance( 10F ); regular nether brick
        
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockNetherBrickLoose" );        
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockWithNotify( i, j, k, Block.netherBrick.blockID );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//    
}