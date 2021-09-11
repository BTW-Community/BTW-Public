// FCMOD

package net.minecraft.src;

import java.util.List;

public abstract class FCUtilsPrimitiveGeometric
{
	public abstract FCUtilsPrimitiveGeometric MakeTemporaryCopy();
	
	public abstract void RotateAroundJToFacing( int iFacing );
	
	public abstract void TiltToFacingAlongJ( int iFacing );
	
	public abstract void AddToRayTrace( FCUtilsRayTraceVsComplexBlock rayTrace );
	
	public abstract void Translate( double dDeltaX, double dDeltaY, double dDeltaZ );
	
    public void AddIntersectingBoxesToCollisionList( World world, int i, int j, int k, AxisAlignedBB boxToIntersect, List collisionList )
    {
    	// not every primitive type will add boxes
    }
    
    public int GetAssemblyID()
    {
    	// not every primitive type will support assembly IDs
    	
    	return -1;
    }
	
	//----------- Client Side Functionality -----------//
}
