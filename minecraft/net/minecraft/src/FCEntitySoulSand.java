// FCMOD

package net.minecraft.src;

//client only
import net.minecraft.client.Minecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntitySoulSand extends Entity
	implements FCIEntityPacketHandler
{
    private double targetX;
    private double targetY;
    private double targetZ;
    
    private int despawnTimer;

    public FCEntitySoulSand( World world )
    {
        super(world);
        
        setSize( 0.25F, 0.25F );
    }

    public FCEntitySoulSand( World world, double x, double y, double z )
    {
    	this( world );
    	
        setPosition(x, y, z);
        yOffset = 0.0F;
        
        despawnTimer = 0;
    }

    @Override
    protected void entityInit() {}

    public void setVelocity(double par1, double par3, double par5)
    {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var7) * 180.0D / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (!this.worldObj.isRemote)
        {
            double var2 = this.targetX - this.posX;
            double var4 = this.targetZ - this.posZ;
            float var6 = (float)Math.sqrt(var2 * var2 + var4 * var4);
            float var7 = (float)Math.atan2(var4, var2);
            double var8 = (double)var1 + (double)(var6 - var1) * 0.0006D;

            if (var6 < 1.0F)
            {
                var8 *= 0.8D;
                this.motionY *= 0.8D;
            }

            this.motionX = Math.cos((double)var7) * var8;
            this.motionZ = Math.sin((double)var7) * var8;

            if (this.posY < this.targetY)
            {
                this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
            }
            else
            {
                this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
            }
            
            this.setPosition(this.posX, this.posY, this.posZ);            
            ++this.despawnTimer;

            if (this.despawnTimer > 40 && !this.worldObj.isRemote)
            {
                this.setDead();

                if ( var6 >= 1.0F )
                {
	                worldObj.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID, 
	                	(int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 0 );
                }                
            }
        }
        else
        {
        	ClientUpdateParticles(); // client only
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        return 1.0F;
    }

    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }
    
    //------------- FCIEntityPacketHandler ------------//

    @Override
    public int GetTrackerViewDistance()
    {
    	return 64;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 4;
    }
    
    @Override
    public boolean GetTrackMotion()
    {
    	return true;
    }
    
    @Override
    public boolean ShouldServerTreatAsOversized()
    {
    	return false;
    }
    
    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeSoulSand );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeByte( (byte)(int)( motionX * 128D ) );
	        dataStream.writeByte( (byte)(int)( motionY * 128D ) );
	        dataStream.writeByte( (byte)(int)( motionZ * 128D ) );	        		
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    //------------- Class Specific Methods ------------//
    
    public void MoveTowards( double x, double z )
    {
        double deltaX = x - posX;
        double deltaY = z - posZ;
        
        float fDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY);

        if ( fDistance > 3F )
        {
	        targetX = posX + deltaX / (double)fDistance * 3.0D;
	        targetZ = posZ + deltaY / (double)fDistance * 3.0D;
        }
        else
        {
        	targetX = x;
        	targetZ = z;
        }

        targetY = posY + 0.1;
        
        despawnTimer = 0;
    }
    
	//----------- Client Side Functionality -----------//
    
    private void ClientUpdateParticles()
    {
        float var10 = 0.25F;

    	for ( int iTempCount = 0; iTempCount < 10; iTempCount++ )
    	{
            EntityFX particleEntity = new EntitySmokeFX( worldObj, 
    			posX - motionX * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, 
    			posY - motionY * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D - 0.1D, 
    			posZ - motionZ * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, 
    			motionX, motionY, motionZ, 0.33F );
            
            if ( rand.nextInt( 8 ) == 0 )
            {
            	particleEntity.setRBGColorF( 1F, 1F, 1F );
            }
            
            Minecraft.getMinecraft().effectRenderer.addEffect( (EntityFX)particleEntity );
            
            particleEntity = new EntityAuraFX( worldObj, 
    			posX - motionX * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, 
    			posY - motionY * (double)var10 - 0.5D, 
    			posZ - motionZ * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, 
    			motionX, motionY, motionZ );
            
            float fColorMultiplier = 0.1F + ( rand.nextFloat() * 0.9F );
            
            particleEntity.setRBGColorF( 
            	( 108F / 255F ) * fColorMultiplier, 
            	( 78F / 255F ) * fColorMultiplier, 
            	( 60F / 255F ) * fColorMultiplier );                
            
            particleEntity.particleScale *= 0.25F;
                           
            Minecraft.getMinecraft().effectRenderer.addEffect( (EntityFX)particleEntity );            
    	}
    }
}
