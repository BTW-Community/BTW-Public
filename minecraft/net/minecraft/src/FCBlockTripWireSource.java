// FCMOD

package net.minecraft.src;

public class FCBlockTripWireSource extends BlockTripWireSource
{
    public FCBlockTripWireSource( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName("tripWireSource");        
        
        setCreativeTab( null );        
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iDirection = blockAccess.getBlockMetadata(i, j, k) & 3;
        float fHalfWidth = 0.1875F;

        if ( iDirection == 3 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 0.2F, 0.5F - fHalfWidth, fHalfWidth * 2.0F, 0.8F, 0.5F + fHalfWidth );
        }
        else if ( iDirection == 1 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		1.0F - fHalfWidth * 2.0F, 0.2F, 0.5F - fHalfWidth, 1.0F, 0.8F, 0.5F + fHalfWidth );
        }
        else if ( iDirection == 0 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5F - fHalfWidth, 0.2F, 0.0F, 0.5F + fHalfWidth, 0.8F, fHalfWidth * 2.0F );
        }
        else // == 2
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5F - fHalfWidth, 0.2F, 1.0F - fHalfWidth * 2.0F, 0.5F + fHalfWidth, 0.8F, 1.0F );
        }
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderBlockTripWireSource( this, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }	
}