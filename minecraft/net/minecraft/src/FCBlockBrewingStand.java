// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockBrewingStand extends BlockBrewingStand
{
	protected static final double m_dBaseHeight = 0.125D;	
	protected static final double m_dBaseWidth = ( 1D - ( 2D / 16D ) );
	protected static final double m_dBaseHalfWidth = ( m_dBaseWidth / 2D );
	
	protected static final double m_dCenterColumnWidth = ( 2D / 16D );
	protected static final double m_dCenterColumnHalfWidth = ( m_dCenterColumnWidth / 2D );
	
	protected static final double m_dCenterAssemblyWidth = ( 10D / 16D );
	protected static final double m_dCenterAssemblyHalfWidth = ( m_dCenterAssemblyWidth / 2D );
	
    public FCBlockBrewingStand( int iBlockID )
    {
    	super( iBlockID );
    }
    
    @Override
    public boolean DoesBlockHopperInsert( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB boundingBox, List list, Entity entity )
    {
    	// base
    	
    	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
    		0D, 0D, 0D, 1D, m_dBaseHeight, 1D ).
    		offset( i, j, k );
    	
    	tempBox.AddToListIfIntersects( boundingBox, list );

    	// center
    	
    	tempBox = AxisAlignedBB.getAABBPool().getAABB( 
    		0.5D - m_dCenterColumnHalfWidth, 0D, 0.5D - m_dCenterColumnHalfWidth, 
    		0.5D + m_dCenterColumnHalfWidth, 1D, 0.5D + m_dCenterColumnHalfWidth ).
    		offset( i, j, k );
    	
    	tempBox.AddToListIfIntersects( boundingBox, list );
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( 
    		world, i, j, k, startRay, endRay );
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( 
    		0.5D - m_dBaseHalfWidth, 0D, 0.5D - m_dBaseHalfWidth, 
    		0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfWidth );
        
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( 
    		0.5D - m_dCenterAssemblyHalfWidth, m_dBaseHeight, 0.5D - m_dCenterAssemblyHalfWidth, 
    		0.5D + m_dCenterAssemblyHalfWidth, 1D, 0.5D + m_dCenterAssemblyHalfWidth );
    	
    	return rayTrace.GetFirstIntersection();    	
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	return renderer.RenderBlockBrewingStand( this, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
    
    @Override    
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k)
    {
    	return AxisAlignedBB.getAABBPool().getAABB( 
    		0.5D - m_dBaseHalfWidth, 0, 0.5D - m_dBaseHalfWidth, 
    		0.5D + m_dBaseHalfWidth, 1D, 0.5D + m_dBaseHalfWidth ).
    		offset( i, j, k );
    }
}
