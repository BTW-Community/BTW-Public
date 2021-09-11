// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockFarmlandLegacyBase extends FCBlockFarmlandBase
{
    protected FCBlockFarmlandLegacyBase( int iBlockID )
    {
    	super( iBlockID );    	
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        super.onNeighborBlockChange( world, i, j, k, iNeighborBlockID );
        
        Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
        
        Material material = world.getBlockMaterial( i, j + 1, k );

        if ( blockAbove != null )
        {
	        if ( blockAbove.blockMaterial.isSolid() )
	        {
	            world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
	        }
	        else if ( blockAbove.GetConvertsLegacySoil( world, i, j + 1, k ) )
	        {
	        	// the new mod crop types (like wheat) convert legacy soil when planted
	        	
	        	ConvertToNewSoil( world, i, j, k );
	        }
        }
    }

    @Override
    protected boolean IsHydrated( int iMetadata )
    {
    	// stores decreasing levels of hydration from 7 to 1
    	
    	return iMetadata > 0;
    }
    
    @Override
    protected int SetFullyHydrated( int iMetadata )
    {
    	return iMetadata | 7;
    }
    
	@Override
	protected void DryIncrementally( World world, int i, int j, int k )
	{
        int iMetadata = world.getBlockMetadata( i, j, k );

        world.setBlockMetadataWithNotify( i, j, k, iMetadata - 1 );
	}

    //------------- Class Specific Methods ------------//
	
	protected abstract void ConvertToNewSoil( World world, int i, int j, int k );
    
	//----------- Client Side Functionality -----------//
}