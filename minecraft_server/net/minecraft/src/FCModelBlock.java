// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class FCModelBlock extends FCUtilsPrimitiveGeometric
{
	private List<FCUtilsPrimitiveGeometric> m_primitiveList;
	
	private int m_iAssemblyID = -1; // used to identify models within an assembly
	private int m_iActivePrimitiveID = -1; // used when rendering to track which primitive is being processed 
	
	public FCModelBlock()
	{
		m_primitiveList = new LinkedList();
		
		InitModel();
	}
	
	public FCModelBlock( int iAssemblyID )
	{
		this();
		
		SetAssemblyID( iAssemblyID );
	}
	
    @Override
    public FCModelBlock MakeTemporaryCopy()
    {
    	FCModelBlock newModel = new FCModelBlock( GetAssemblyID() );
    	
    	MakeTemporaryCopyOfPrimitiveList( newModel );
    	
    	return newModel;
    }
    
	/**
	 * Yaws the model around the J axis. Assumes that the model's initial facing is along the negative K axis (facing 2)
	 */	
    @Override
	public void RotateAroundJToFacing( int iFacing )
	{
		if ( iFacing > 2 )
		{
	    	Iterator<FCUtilsPrimitiveGeometric> tempIterator = m_primitiveList.iterator();
	    	
	    	while ( tempIterator.hasNext() )
	    	{
	    		tempIterator.next().RotateAroundJToFacing( iFacing );
	    	}
		}
	}

	/**
	 * "Tilts" the model towards the desired facing.  Takes the up vector and either yaws or rolls it towards the specified axis.
	 */	
    @Override
	public void TiltToFacingAlongJ( int iFacing )
	{
		if ( iFacing != 1 )
		{
	    	Iterator<FCUtilsPrimitiveGeometric> tempIterator = m_primitiveList.iterator();
	    	
	    	while ( tempIterator.hasNext() )
	    	{
	    		tempIterator.next().TiltToFacingAlongJ( iFacing );
	    	}
		}
	}
    
    @Override
	public void Translate( double dDeltaX, double dDeltaY, double dDeltaZ )
    {
    	Iterator<FCUtilsPrimitiveGeometric> tempIterator = m_primitiveList.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		tempIterator.next().Translate( dDeltaX, dDeltaY, dDeltaZ );
    	}
    }
	
    @Override
    public void AddToRayTrace( FCUtilsRayTraceVsComplexBlock rayTrace )
    {
    	Iterator<FCUtilsPrimitiveGeometric> tempIterator = m_primitiveList.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		tempIterator.next().AddToRayTrace( rayTrace );
    	}
    }
    
    @Override
    public void AddIntersectingBoxesToCollisionList( World world, int i, int j, int k, AxisAlignedBB boxToIntersect, List collisionList )
    {
    	Iterator<FCUtilsPrimitiveGeometric> tempIterator = m_primitiveList.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		tempIterator.next().AddIntersectingBoxesToCollisionList( world, i, j, k, 
    			boxToIntersect, collisionList );
    	}
    }

    @Override
    public int GetAssemblyID()
    {
    	return m_iAssemblyID;
    }
    
	//------------- Class Specific Methods ------------//
	
    protected void InitModel()
    {
    }

    public void MakeTemporaryCopyOfPrimitiveList( FCModelBlock modelTo )
    {
    	Iterator<FCUtilsPrimitiveGeometric> tempIterator = m_primitiveList.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		modelTo.m_primitiveList.add( tempIterator.next().MakeTemporaryCopy() );
    	}
    }
    
	public void AddPrimitive( FCUtilsPrimitiveGeometric primitive )
	{
		m_primitiveList.add( primitive );
	}
	
	public void AddBox( double dMinX, double dMinY, double dMinZ, double dMaxX, double dMaxY, double dMaxZ )
	{
		m_primitiveList.add( new AxisAlignedBB( dMinX, dMinY, dMinZ, dMaxX, dMaxY, dMaxZ ) );
	}
	
    public MovingObjectPosition CollisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );

    	AddToRayTrace( rayTrace );
    	
        return rayTrace.GetFirstIntersection();
    }
    
    public int GetActivePrimitiveID()
    {
    	return m_iActivePrimitiveID;
    }
    
    public void SetAssemblyID( int iAssemblyID )
    {
    	m_iAssemblyID = iAssemblyID;
    }
    
	//----------- Client Side Functionality -----------//
}