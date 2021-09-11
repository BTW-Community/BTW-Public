// FCMOD

package net.minecraft.src;

public class FCBlockBed extends BlockBed
{
    public FCBlockBed( int iBlockID )
    {
    	super( iBlockID );
    	
    	InitBlockBounds( 0D, 0D, 0D, 1D, 0.5625D, 1D );
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
	{
        if ( world.isRemote )
        {
            player.addChatMessage( "You are too troubled to rest.");            
        }
        
        return true;
	}
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.RenderBlockBed( this, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
}