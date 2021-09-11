// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBrickLooseSlab extends FCBlockMortarReceiverSlab
{
    public FCBlockBrickLooseSlab( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 1F ); // setHardness( 2F ); regular brick
        setResistance( 5F ); // setResistance( 10F ); regular brick
        
        SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockBrickLooseSlab" );
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockBrickLoose.blockID;
	}

    @Override
    public boolean OnMortarApplied( World world, int i, int j, int k )
    {
		int iNewMetadata = 4; // // metadata 4 is brick slab
		
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