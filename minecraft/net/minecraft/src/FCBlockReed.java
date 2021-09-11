// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockReed extends BlockReed
{
    private static final double m_dWidth = 0.75D;
    private static final double m_dHalfWidth = ( m_dWidth / 2D );
    
    protected FCBlockReed( int iBlockID )
    {
    	super( iBlockID );
    	
        InitBlockBounds( 0.5D - m_dHalfWidth, 0F, 0.5D - m_dHalfWidth, 
        	0.5D + m_dHalfWidth, 1F, 0.5D + m_dHalfWidth );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	// override to reduce growth rate and prevent growth in the end dimension
    	
    	if ( world.provider.dimensionId != 1 )
    	{
            if ( rand.nextInt( 2 ) == 0 && world.isAirBlock( i, j + 1, k ) )
            {
                int iReedHeight = 1;

                while ( world.getBlockId( i, j - iReedHeight, k ) == blockID )
                {
                	iReedHeight++;
                }

                if ( iReedHeight < 3 )
                {
                    int iMetadata = world.getBlockMetadata( i, j, k );

                    if ( iMetadata == 15 )
                    {
                        world.setBlock( i, j + 1, k, blockID );
                        
                        world.SetBlockMetadataWithNotify( i, j, k, 0, 4 );
                    }
                    else
                    {
                        world.SetBlockMetadataWithNotify( i, j, k, iMetadata + 1, 4 );
                    }
                }
            }
    	}
    }
    
    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        int iBlockBelowID = world.getBlockId( i, j - 1, k );
        Block blockBelow = Block.blocksList[iBlockBelowID];        

    	return iBlockBelowID == blockID || ( blockBelow != null && 
    		blockBelow.CanReedsGrowOnBlock( world, i, j - 1, k ) &&
    		blockBelow.IsConsideredNeighbouringWaterForReedGrowthOn( world, i, j - 1, k ) );
    }
    
    @Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
    	if ( entity.IsAffectedByMovementModifiers() && entity.onGround )
    	{    		
	        entity.motionX *= 0.8D;
	        entity.motionZ *= 0.8D;
    	}
    }    
    
	@Override
    public boolean DoesBlockDropAsItemOnSaw( World world, int i, int j, int k )
    {
		return true;
    }

    @Override
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
    	return true;
	}
    
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
		return animal.CanGrazeOnRoughVegetation();
    }

    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderCrossedSquares( this, i, j, k );
    }    
}
