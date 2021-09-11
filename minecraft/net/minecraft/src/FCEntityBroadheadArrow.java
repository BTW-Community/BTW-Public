// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCEntityBroadheadArrow extends EntityArrow
	implements FCIEntityPacketHandler
{
	private static final float m_fBroadheadDamageMultiplier = 1.5F;
	
    static final private int m_iVehicleSpawnPacketType = 101;
    
    public FCEntityBroadheadArrow( World world )
    {
        super( world );
    }

    public FCEntityBroadheadArrow( World world, double d, double d1, double d2 )
    {
        super( world, d, d1, d2 );
    }

    public FCEntityBroadheadArrow( World world, EntityLiving entityLiving, float f )
    {
        super( world, entityLiving, f );
    }

    @Override
    protected float GetDamageMultiplier()
    {
    	return m_fBroadheadDamageMultiplier;
    }

    @Override
	public Item GetCorrespondingItem()
	{
		return FCBetterThanWolves.fcItemBroadheadArrow;
	}
    
    //************* FCIEntityPacketHandler ************//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {
		return new Packet23VehicleSpawn( this, GetVehicleSpawnPacketType(), shootingEntity == null ?  entityId : shootingEntity.entityId );
    }
    
    @Override
    public int GetTrackerViewDistance()
    {
    	return 64;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 20;
    }
    
    @Override
    public boolean GetTrackMotion()
    {
    	return false;
    }
    
    @Override
    public boolean ShouldServerTreatAsOversized()
    {
    	return false;
    }
    
    //************* Class Specific Methods ************//

    static public int GetVehicleSpawnPacketType()
    {
    	return m_iVehicleSpawnPacketType;
    }
}