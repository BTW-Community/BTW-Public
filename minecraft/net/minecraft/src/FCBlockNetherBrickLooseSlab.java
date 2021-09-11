// FCMOD

package net.minecraft.src;

public class FCBlockNetherBrickLooseSlab extends FCBlockMortarReceiverSlab
{
    public FCBlockNetherBrickLooseSlab( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialNetherRock );
        
        setHardness( 1F ); // setHardness( 2F ); regular nether brick
        setResistance( 5F ); // setResistance( 10F ); regular nether brick
        
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockNetherBrickLooseSlab" );
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockNetherBrickLoose.blockID;
	}

    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		int iNewMetadata = 6; // nether brick slab
		
		if ( GetIsUpsideDown( world, i, j, k ) )
		{
			iNewMetadata |= 8;
		}
		
		world.setBlockAndMetadataWithNotify( i, j, k, Block.stoneSingleSlab.blockID, iNewMetadata );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
	
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockNetherBrickLoose" );
    }
}