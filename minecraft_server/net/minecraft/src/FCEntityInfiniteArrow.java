// FCMOD

package net.minecraft.src;

public class FCEntityInfiniteArrow extends EntityArrow
	implements FCIEntityPacketHandler
{
    static final private int m_iVehicleSpawnPacketType = 100;
    
    public FCEntityInfiniteArrow( World world )
    {
        super(world);
    }
	
    public FCEntityInfiniteArrow( World world, double d, double d1, double d2 )
    {
        super( world, d, d1, d2 );
   }
    
    public FCEntityInfiniteArrow( World world, EntityLiving entityliving, float f )
    {
    	super( world, entityliving, f );
    	
    	canBePickedUp = 2;
    }
    
    public FCEntityInfiniteArrow( World world, EntityLiving firingEntity, EntityLiving targetEntity, float par4, float par5 )
    {
        super( world, firingEntity, targetEntity, par4, par5 );
    }
    
    @Override
	public Item GetCorrespondingItem()
	{
		return null;
	}
    
    @Override
	public boolean CanHopperCollect()
	{
		return false;
	}
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        if ( !isDead && inGround )
        {
        	// infinite arrows are destroyed on impact
        	
            for (int i = 0; i < 32; i++)
            {
            	// spew gold particles
            	
                worldObj.spawnParticle( "iconcrack_266", posX, posY, posZ,
	        		(double)((float)(Math.random() * 2D - 1.0D) * 0.4F), 
	        		(double)((float)(Math.random() * 2D - 1.0D) * 0.4F), 
	        		(double)((float)(Math.random() * 2D - 1.0D) * 0.4F) );
            }
            
        	setDead();
        }
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