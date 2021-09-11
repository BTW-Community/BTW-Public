// FCMOD

package net.minecraft.src;

public class FCBlockCobblestoneLoose extends FCBlockLavaReceiver
{
    public FCBlockCobblestoneLoose( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 1F ); // setHardness( 2F ); regular cobble
        setResistance( 5F ); // setResistance( 10F ); regular cobble
        
        SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockCobblestoneLoose" );        
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockWithNotify( i, j, k, Block.cobblestone.blockID );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
    
    //------------ Client Side Functionality ----------//
}