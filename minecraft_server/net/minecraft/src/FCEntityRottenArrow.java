// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCEntityRottenArrow extends EntityArrow
	implements FCIEntityPacketHandler
{
	private static final float m_fDamageMultiplier = 1.0F;
	
    static final private int m_iVehicleSpawnPacketType = 104;
    
    public FCEntityRottenArrow( World world )
    {
        super( world );
    }

    public FCEntityRottenArrow( World world, double d, double d1, double d2 )
    {
        super( world, d, d1, d2 );
    }

    public FCEntityRottenArrow( World world, EntityLiving entityLiving, float f )
    {
        super( world, entityLiving, f );
    	
    	canBePickedUp = 2;
    }

    public FCEntityRottenArrow( World world, EntityLiving firingEntity, EntityLiving targetEntity, float par4, float par5)
    {
        super( world, firingEntity, targetEntity, par4, par5 );
    }
    
    @Override
    protected float GetDamageMultiplier()
    {
    	return m_fDamageMultiplier;
    }

    @Override
	protected boolean AddArrowToPlayerInv( EntityPlayer player )
	{
		return false;
	}
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        if ( !isDead && inGround )
        {
        	// rotten arrows are destroyed on impact
        	
            for (int i = 0; i < 32; i++)
            {
            	// spew boat particles
            	
                worldObj.spawnParticle("iconcrack_333", posX, posY, posZ,
	        		(double)((float)(Math.random() * 2D - 1.0D) * 0.4F), 
	        		(double)((float)(Math.random() * 2D - 1.0D) * 0.4F), 
	        		(double)((float)(Math.random() * 2D - 1.0D) * 0.4F) );
            }
            
        	setDead();
        }
    }

    @Override
	public Item GetCorrespondingItem()
	{
		return FCBetterThanWolves.fcItemRottenArrow;
	}
    
    @Override
	public boolean CanHopperCollect()
	{
		return false;
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