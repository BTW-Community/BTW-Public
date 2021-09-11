// FCMOD

package net.minecraft.src;

public class FCClosestEntitySelectionCriteria
{
	public static FCClosestEntitySelectionCriteria m_secondarySquidTarget = new FCClosestEntitySelectionCriteriaSquidSecondaryTarget();
	
    public void ProcessEntity( FCClosestEntityInfo closestEntityInfo, Entity entity ) 
    {
    	double dDeltaX = entity.posX - closestEntityInfo.m_dSourcePosX;
    	double dDeltaY = entity.posY - closestEntityInfo.m_dSourcePosY;
    	double dDeltaZ = entity.posZ - closestEntityInfo.m_dSourcePosZ;
    	
    	double dDistSq = ( dDeltaX * dDeltaX ) + ( dDeltaY * dDeltaY ) + ( dDeltaZ * dDeltaZ );
    	
    	if ( dDistSq < closestEntityInfo.m_dClosestDistanceSq )
    	{
    		closestEntityInfo.m_closestEntity = entity;
    		closestEntityInfo.m_dClosestDistanceSq = dDistSq;
    	}
	}
}
