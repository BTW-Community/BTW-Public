// FCMOD

package net.minecraft.src;

public class FCBlockBrickLoose extends FCBlockMortarReceiver
{
    public FCBlockBrickLoose( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 1F ); // setHardness( 2F ); regular brick
        setResistance( 5F ); // setResistance( 10F ); regular brick        
        SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockBrickLoose" );        
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
	
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockWithNotify( i, j, k, Block.brick.blockID );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//    
}