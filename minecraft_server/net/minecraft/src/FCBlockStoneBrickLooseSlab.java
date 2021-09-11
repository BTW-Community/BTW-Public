// FCMOD

package net.minecraft.src;

public class FCBlockStoneBrickLooseSlab extends FCBlockMortarReceiverSlab
{
    public FCBlockStoneBrickLooseSlab( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 1F ); // setHardness( 2.25F ); regular stone brick
        setResistance( 5F ); // setResistance( 10F ); regular stone brick
        
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockStoneBrickLooseSlab" );
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockStoneBrickLoose.blockID;
	}

    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		int iNewMetadata = 5; // stone brick slab
		
		if ( GetIsUpsideDown( world, i, j, k ) )
		{
			iNewMetadata |= 8;
		}
		
		world.setBlockAndMetadataWithNotify( i, j, k, Block.stoneSingleSlab.blockID, iNewMetadata );
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
}