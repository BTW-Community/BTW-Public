// FCMOD

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FCUtilsRayTraceVsComplexBlock
{
	private World m_World;
	
	private int m_iBlockI;
	private int m_iBlockJ;
	private int m_iBlockK;
	private Vec3 m_StartRay;
	private Vec3 m_EndRay;
	
	List<MovingObjectPosition> m_IntersectionPointList;
	
	public FCUtilsRayTraceVsComplexBlock( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
	{
		m_IntersectionPointList = new ArrayList<MovingObjectPosition>();
        
		m_World = world;
		
		m_iBlockI = i;
		m_iBlockJ = j;
		m_iBlockK = k;
		
		m_StartRay = Vec3.createVectorHelper( startRay.xCoord, startRay.yCoord, startRay.zCoord );
		m_EndRay = Vec3.createVectorHelper( endRay.xCoord, endRay.yCoord, endRay.zCoord );
	}
	
	public void AddBoxWithLocalCoordsToIntersectionList( AxisAlignedBB box )
	{		
		AddBoxWithLocalCoordsToIntersectionList( box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ ); 
	}
	
	public void AddBoxWithLocalCoordsToIntersectionList( double fMinX, double fMinY, double fMinZ, double fMaxX, double fMaxY, double fMaxZ )
	{
        Vec3 startRay = m_StartRay.addVector( -m_iBlockI, -m_iBlockJ, -m_iBlockK );
        Vec3 endRay = m_EndRay.addVector( -m_iBlockI, -m_iBlockJ, -m_iBlockK );
        
        Vec3 boxMin = Vec3.createVectorHelper( fMinX, fMinY, fMinZ );
        Vec3 boxMax = Vec3.createVectorHelper( fMaxX, fMaxY, fMaxZ );
        
        Vec3 vec3 = startRay.getIntermediateWithXValue( endRay, boxMin.xCoord );
        Vec3 vec3_1 = startRay.getIntermediateWithXValue( endRay, boxMax.xCoord );
        Vec3 vec3_2 = startRay.getIntermediateWithYValue( endRay, boxMin.yCoord );
        Vec3 vec3_3 = startRay.getIntermediateWithYValue( endRay, boxMax.yCoord );
        Vec3 vec3_4 = startRay.getIntermediateWithZValue( endRay, boxMin.zCoord );
        Vec3 vec3_5 = startRay.getIntermediateWithZValue( endRay, boxMax.zCoord );

        if (!isVecInsideYZBounds(vec3, boxMin, boxMax))
        {
            vec3 = null;
        }

        if (!isVecInsideYZBounds(vec3_1, boxMin, boxMax))
        {
            vec3_1 = null;
        }

        if (!isVecInsideXZBounds(vec3_2, boxMin, boxMax))
        {
            vec3_2 = null;
        }

        if (!isVecInsideXZBounds(vec3_3, boxMin, boxMax))
        {
            vec3_3 = null;
        }

        if (!isVecInsideXYBounds(vec3_4, boxMin, boxMax))
        {
            vec3_4 = null;
        }

        if (!isVecInsideXYBounds(vec3_5, boxMin, boxMax))
        {
            vec3_5 = null;
        }

        Vec3 vec3_6 = null;

        if (vec3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3;
        }

        if (vec3_1 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_1) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_1;
        }

        if (vec3_2 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_2) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_2;
        }

        if (vec3_3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_3) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_3;
        }

        if (vec3_4 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_4) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_4;
        }

        if (vec3_5 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_5) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_5;
        }

        if (vec3_6 != null)
        {
	        byte byte0 = -1;
	
	        if (vec3_6 == vec3)
	        {
	            byte0 = 4;
	        }
	
	        if (vec3_6 == vec3_1)
	        {
	            byte0 = 5;
	        }
	
	        if (vec3_6 == vec3_2)
	        {
	            byte0 = 0;
	        }
	
	        if (vec3_6 == vec3_3)
	        {
	            byte0 = 1;
	        }
	
	        if (vec3_6 == vec3_4)
	        {
	            byte0 = 2;
	        }
	
	        if (vec3_6 == vec3_5)
	        {
	            byte0 = 3;
	        }
	        
			m_IntersectionPointList.add( new MovingObjectPosition( m_iBlockI, m_iBlockJ, m_iBlockK, byte0, vec3_6.addVector( m_iBlockI, m_iBlockJ, m_iBlockK ) ) );
        }
	}
	
	public void AddQuadWithLocalCoordsToIntersectionList( FCUtilsPrimitiveQuad quad, Vec3 pointOnQuad )
	{
		// Uses formula from wikipedia.org/wiki/Line�plane_intersection
		// d = ( ( p0 - l0 ) dot n ) / ( l dot n )
		// where d is a scalar from the line equation. p0 and l0 represent known points on the line and plane
		// n is the normal of the plane, and l is a direction vector for the line.
		
		// NOTE: Assumes that the quad is rectangular and aligned along 2 axis.  Will not be accurate for an 
		// arbitrary quad due to use of FCUtilsPrimitiveQuad.IsPointOnPlaneWithinBounds()
		
        Vec3 lineDir = m_EndRay.SubtractFrom( m_StartRay );
        
        Vec3 pointOnLine = m_StartRay.addVector( -m_iBlockI, -m_iBlockJ, -m_iBlockK );
        
        Vec3 planeNormal = quad.ComputeNormal();
        
        double lDotn = planeNormal.dotProduct( lineDir );
        
        if ( lDotn != 0D ) // dot product of zero means ray and plane are parallel, with zero intersections
        {
        	Vec3 p0Minusl0 = pointOnLine.SubtractFrom( pointOnQuad );
        	
            double dNumeratorDot = planeNormal.dotProduct( p0Minusl0 );
            
            if ( dNumeratorDot != 0D ) // dot product of zero means line is within plane, with infinite intersections
            {
            	double dLineScalar = dNumeratorDot / lDotn;
            	
            	Vec3 intersection = Vec3.createVectorHelper( dLineScalar * lineDir.xCoord, 
            		dLineScalar * lineDir.yCoord, dLineScalar * lineDir.zCoord ).AddVector( pointOnLine );

            	if ( quad.IsPointOnPlaneWithinBounds( intersection ) )
            	{
            		int iFacingClicked = ConvertVectorToApproximateBlockFacing( planeNormal );
            		
            		if ( lDotn < 0 ) 
            		{
            			// angle greater than 90 degrees (negative dot product) indicates backface intersection
            			
            			iFacingClicked = Block.GetOppositeFacing( iFacingClicked );
            		}
            		
	    			m_IntersectionPointList.add( new MovingObjectPosition( m_iBlockI, m_iBlockJ, m_iBlockK, 
	    				iFacingClicked,  
	    				intersection.addVector( m_iBlockI, m_iBlockJ, m_iBlockK ) ) );
            	}
            }
        }
	}
	
	public int ConvertVectorToApproximateBlockFacing( Vec3 vec )
	{
		double dAbsX = FCUtilsMath.AbsDouble( vec.xCoord );
		double dAbsY = FCUtilsMath.AbsDouble( vec.yCoord );
		double dAbsZ = FCUtilsMath.AbsDouble( vec.zCoord );
		
		if ( dAbsX >= dAbsZ && dAbsX >= dAbsY )
		{
			if ( vec.xCoord < 0 )
			{
				return 4;
			}
			
			return 5;
		}
		else if ( dAbsZ >= dAbsY )
		{
			if ( vec.zCoord < 0 )
			{
				return 2;
			}
			
			return 3;
		}
		else // dAbsY greatest
		{
			if ( vec.yCoord < 0 )
			{
				return 0;
			}
			
			return 1;
		}
	}
	
	public MovingObjectPosition GetFirstIntersection()
	{
        // scan through the list of intersect points we have built, and check for the first intersection
		
        MovingObjectPosition firstIntersect = null;
        
        Iterator iterator = m_IntersectionPointList.iterator();

        double dMaxDistance = 0D;
        
        while ( iterator.hasNext() )
        {
        	MovingObjectPosition tempPos = (MovingObjectPosition)iterator.next();
	        
            double dCurrentIntersectDistance = tempPos.hitVec.squareDistanceTo( m_EndRay );

            if ( dCurrentIntersectDistance  > dMaxDistance )
            {
            	firstIntersect = tempPos;
            	dMaxDistance = dCurrentIntersectDistance ;
            }
        }
        
        return firstIntersect;    	
	}
	
	private boolean isVecInsideYZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max )
    {
        if (par1Vec3 == null)
        {
            return false;
        }
        else
        {
            return par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
        }
    }

    private boolean isVecInsideXZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max )
    {
        if (par1Vec3 == null)
        {
            return false;
        }
        else
        {
            return par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
        }
    }

    private boolean isVecInsideXYBounds(Vec3 par1Vec3, Vec3 min, Vec3 max )
    {
        if (par1Vec3 == null)
        {
            return false;
        }
        else
        {
            return par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord;
        }
    }
}