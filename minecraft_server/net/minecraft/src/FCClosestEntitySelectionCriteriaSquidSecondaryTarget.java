// FCMOD

package net.minecraft.src;

public class FCClosestEntitySelectionCriteriaSquidSecondaryTarget extends FCClosestEntitySelectionCriteria
{
	@Override
    public void ProcessEntity( FCClosestEntityInfo closestEntityInfo, Entity entity ) 
    {
		if ( entity.IsSecondaryTargetForSquid() && entity.isEntityAlive() )
		{
	    	double dDeltaX = entity.posX - closestEntityInfo.m_dSourcePosX;
	    	double dDeltaY = entity.posY - closestEntityInfo.m_dSourcePosY;
	    	double dDeltaZ = entity.posZ - closestEntityInfo.m_dSourcePosZ;
	    	
	    	double dDistSq = ( dDeltaX * dDeltaX ) + ( dDeltaY * dDeltaY ) + ( dDeltaZ * dDeltaZ );
	    	
	    	if ( dDistSq < closestEntityInfo.m_dClosestDistanceSq && 
	    		( !entity.worldObj.isDaytime() || entity.getBrightness( 1F ) < FCEntitySquid.fBrightnessAgressionThreshold ) &&
	    		( entity.inWater || CanEntityBeSeenBySource( closestEntityInfo, entity ) ) )
	    	{
	    		closestEntityInfo.m_closestEntity = entity;
	    		closestEntityInfo.m_dClosestDistanceSq = dDistSq;
	    	}
		}
	}
	
	private boolean CanEntityBeSeenBySource( FCClosestEntityInfo closestEntityInfo, Entity entity )
	{
        return entity.worldObj.rayTraceBlocks_do_do( 
        	entity.worldObj.getWorldVec3Pool().getVecFromPool( closestEntityInfo.m_dSourcePosX, closestEntityInfo.m_dSourcePosY, closestEntityInfo.m_dSourcePosZ ), 
        	entity.worldObj.getWorldVec3Pool().getVecFromPool( entity.posX, entity.posY + ( entity.height / 2F ), entity.posZ ), false, true ) == null;
	}
}
