// FCMOD

package net.minecraft.src;

public class FCBlockRedstoneWire extends BlockRedstoneWire
{
    public FCBlockRedstoneWire( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F );
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
	@Override
	public boolean TriggersBuddy()
	{
		return false;
	}
	
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderBlockRedstoneWire( this, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }	
}
