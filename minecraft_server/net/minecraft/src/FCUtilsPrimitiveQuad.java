// FCMOD

package net.minecraft.src;

//import org.lwjgl.opengl.GL11; // client only

public class FCUtilsPrimitiveQuad extends FCUtilsPrimitiveGeometric
{
	private Vec3[] m_vertices = new Vec3[4];
	
	private float m_fMinUFrac = 0F;
	private float m_fMinVFrac = 0F;
	private float m_fMaxUFrac = 1F;
	private float m_fMaxVFrac = 1F;
	
	private int m_iIconIndex = 0;
	
	public FCUtilsPrimitiveQuad( Vec3 vertex1, Vec3 vertex2, Vec3 vertex3, Vec3 vertex4 )
	{
		m_vertices[0] = vertex1;
		m_vertices[1] = vertex2;
		m_vertices[2] = vertex3;
		m_vertices[3] = vertex4;
	}

	@Override
	public void RotateAroundJToFacing( int iFacing )
	{
		m_vertices[0].RotateAsBlockPosAroundJToFacing( iFacing );
		m_vertices[1].RotateAsBlockPosAroundJToFacing( iFacing );
		m_vertices[2].RotateAsBlockPosAroundJToFacing( iFacing );
		m_vertices[3].RotateAsBlockPosAroundJToFacing( iFacing );
	}
	
	@Override
	public void TiltToFacingAlongJ( int iFacing )
	{
		m_vertices[0].TiltAsBlockPosToFacingAlongJ( iFacing );
		m_vertices[1].TiltAsBlockPosToFacingAlongJ( iFacing );
		m_vertices[2].TiltAsBlockPosToFacingAlongJ( iFacing );
		m_vertices[3].TiltAsBlockPosToFacingAlongJ( iFacing );
	}
	
    @Override
	public void Translate( double dDeltaX, double dDeltaY, double dDeltaZ )
    {
    	m_vertices[0].addVector( dDeltaX, dDeltaY, dDeltaZ );
    	m_vertices[1].addVector( dDeltaX, dDeltaY, dDeltaZ );
    	m_vertices[2].addVector( dDeltaX, dDeltaY, dDeltaZ );
    	m_vertices[3].addVector( dDeltaX, dDeltaY, dDeltaZ );
    }
    
	@Override
	public void AddToRayTrace( FCUtilsRayTraceVsComplexBlock rayTrace )
	{
    	rayTrace.AddQuadWithLocalCoordsToIntersectionList( this, m_vertices[0] );    	
	}
	
	@Override
	public FCUtilsPrimitiveQuad MakeTemporaryCopy()
	{
		FCUtilsPrimitiveQuad newQuad = new FCUtilsPrimitiveQuad( 
			Vec3.createVectorHelper( m_vertices[0] ),
			Vec3.createVectorHelper( m_vertices[1] ),
			Vec3.createVectorHelper( m_vertices[2] ),
			Vec3.createVectorHelper( m_vertices[3] ) );
		
		newQuad.SetUVFractions( m_fMinUFrac, m_fMinVFrac, m_fMaxUFrac, m_fMaxVFrac );
		
		newQuad.SetIconIndex( m_iIconIndex );
		
		return newQuad;
	}
	
    //------------- Class Specific Methods ------------//

	private static final double m_dMindTheGap = 0.0001D;
	
	public boolean IsPointOnPlaneWithinBounds( Vec3 point )
	{
		Vec3 minBounds = Vec3.createVectorHelper( m_vertices[0] );
		Vec3 maxBounds = Vec3.createVectorHelper( m_vertices[0] );
		
		ComputeBounds( minBounds, maxBounds );
		
		if ( 
			( maxBounds.xCoord - minBounds.xCoord < m_dMindTheGap || 
				( point.xCoord >= minBounds.xCoord && point.xCoord <= maxBounds.xCoord )
			) &&
			( maxBounds.yCoord - minBounds.yCoord < m_dMindTheGap || 
				( point.yCoord >= minBounds.yCoord && point.yCoord <= maxBounds.yCoord )
			) &&
			( maxBounds.zCoord - minBounds.zCoord < m_dMindTheGap || 
				( point.zCoord >= minBounds.zCoord && point.zCoord <= maxBounds.zCoord )
			)
		)
		{
			return true;
		}
		
		return false;
	}
	
	/*
	 * Assumes min and max are set to point 0 when called
	 */
	public void ComputeBounds( Vec3 min, Vec3 max )
	{
		for ( int iTempCount = 1; iTempCount <= 3; iTempCount++ )
		{
			Vec3 tempPoint = m_vertices[iTempCount];
			
			if ( tempPoint.xCoord < min.xCoord )
			{
				min.xCoord = tempPoint.xCoord;
			}
			else if ( tempPoint.xCoord > max.xCoord )
			{
				max.xCoord = tempPoint.xCoord;
			}			
			
			if ( tempPoint.yCoord < min.yCoord )
			{
				min.yCoord = tempPoint.yCoord;
			}
			else if ( tempPoint.yCoord > max.yCoord )
			{
				max.yCoord = tempPoint.yCoord;
			}			
			
			if ( tempPoint.zCoord < min.zCoord )
			{
				min.zCoord = tempPoint.zCoord;
			}
			else if ( tempPoint.zCoord > max.zCoord )
			{
				max.zCoord = tempPoint.zCoord;
			}			
		}		
	}

	public Vec3 ComputeNormal()
	{
		Vec3 vec1 = m_vertices[0].SubtractFrom( m_vertices[1] ); 
		Vec3 vec2 = m_vertices[0].SubtractFrom( m_vertices[3] );
		
		return vec1.crossProduct( vec2 ); 
	}
	
	public FCUtilsPrimitiveQuad SetUVFractions( float fMinUFrac, float fMinVFrac, float fMaxVFrac, float fMaxUFrac )
	{
		m_fMinUFrac = fMinUFrac;		
		m_fMinVFrac = fMinVFrac;
		m_fMaxVFrac = fMaxVFrac;
		m_fMaxUFrac = fMaxUFrac;
		
		return this;
	}
	
	public FCUtilsPrimitiveQuad SetIconIndex( int iIconIndex )
	{
		m_iIconIndex = iIconIndex;
		
		return this;
	}
    
	//----------- Client Side Functionality -----------//
}
