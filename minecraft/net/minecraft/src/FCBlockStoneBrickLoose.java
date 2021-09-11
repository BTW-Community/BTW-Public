// FCMOD

package net.minecraft.src;

public class FCBlockStoneBrickLoose extends FCBlockLavaReceiver
{
    public FCBlockStoneBrickLoose( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 1F ); // setHardness( 2.25F ); regular stone brick
        setResistance( 5F ); // setResistance( 10F ); regular stone brick
        
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockStoneBrickLoose" );        
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		world.setBlockWithNotify( i, j, k, Block.stoneBrick.blockID );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//    
    
    private Icon m_iconLavaCracks;
    
    @Override
    public void registerIcons( IconRegister register )
    {
        super.registerIcons( register );
        
        m_iconLavaCracks = register.registerIcon( "fcOverlayStoneBrickLava" );
    }
    
    @Override
    protected Icon GetLavaCracksOverlay()
    {
    	return m_iconLavaCracks;
    }    
}