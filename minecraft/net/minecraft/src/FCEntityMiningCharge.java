// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


public class FCEntityMiningCharge extends Entity
	implements FCIEntityPacketHandler
{
    public int m_iFuse;
    public int m_iFacing;
    public boolean m_bAttachedToBlock;
    
    public FCEntityMiningCharge( World world )
    {
        super( world );
        
        m_iFuse = 0;
        m_iFacing = 0;
        m_bAttachedToBlock = true;
        
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        yOffset = height / 2.0F;
    }

    public FCEntityMiningCharge( World world, int i, int j, int k, int iFacing )
    {
        this( world );
        
        m_iFuse = 80;
        m_iFacing = iFacing;
        
        setPosition( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F );        
        
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    public FCEntityMiningCharge( World world, double x, double y, double z, int iFacing, int iFuse, boolean bAttachedToBlock )
    {
        this( world );
        
        setPosition( x, y, z );        
        
        m_iFacing = iFacing;
        m_iFuse = iFuse;
        m_bAttachedToBlock = bAttachedToBlock;
        
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }
    
	@Override
    protected void entityInit()
    {
    }

	@Override
    public boolean canBePushed()
    {
        return false;
    }

	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

	@Override
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
    	if ( damagesource.isExplosion() )
    	{
    		if ( m_iFuse > 1 )
    		{
    			m_iFuse = 1;
    		}
    	}
    	
        setBeenAttacked();
        
        return false;
    }

	@Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
        if ( m_bAttachedToBlock )
        {
        	// make sure we're still attached
        	
        	boolean bStillAttached = false;
        	
        	FCUtilsBlockPos attachedBlockPos = new FCUtilsBlockPos( MathHelper.floor_double( posX ),
	            MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ) );
        	
        	attachedBlockPos.AddFacingAsOffset( m_iFacing );
        	
			if ( worldObj.isBlockNormalCube( attachedBlockPos.i, attachedBlockPos.j, attachedBlockPos.k ) || 
				( m_iFacing == 0 && worldObj.doesBlockHaveSolidTopSurface( attachedBlockPos.i, attachedBlockPos.j, attachedBlockPos.k ) ) )
			{
				bStillAttached = true;
			}
        	
    		m_bAttachedToBlock = bStillAttached;
        }

        if ( !m_bAttachedToBlock )
        {
        	if ( m_iFacing == 1 )
        	{
        		// flip upwards facing blocks downwards since that's the direction they'll be falling.
        		
        		m_iFacing = 0;
        	}
        	
	        motionY -= 0.039999999105930328D;
	        
	        moveEntity( motionX, motionY, motionZ );
	        
	        motionX *= 0.98000001907348633D;
	        motionY *= 0.98000001907348633D;
	        motionZ *= 0.98000001907348633D;
	        
	        if ( onGround )
	        {
	            motionX *= 0.69999998807907104D;
	            motionZ *= 0.69999998807907104D;
	            motionY *= -0.5D;
	        }
        }
        
        if ( m_iFuse-- <= 0 )
        {
            if ( !worldObj.isRemote )
            {
                setDead();
                explode();
            } 
            else
            {
            	m_iFuse = 0;
            }
        } 
        else
        {
            worldObj.spawnParticle( "smoke", posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D );
        }
    }

	@Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte( "m_iFuse", (byte)m_iFuse );
        nbttagcompound.setByte( "m_iFacing", (byte)m_iFacing );
        nbttagcompound.setBoolean( "m_bAttachedToBlock", m_bAttachedToBlock );
    }

	@Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    	m_iFuse = nbttagcompound.getByte( "m_iFuse" );
    	m_iFacing = nbttagcompound.getByte( "m_iFacing" );
    	m_bAttachedToBlock = nbttagcompound.getBoolean( "m_bAttachedToBlock" );
    }
    
    @Override
    protected boolean ShouldSetPositionOnLoad()
    {
    	return false;
    }
    
    //************* FCIEntityPacketHandler ************//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeMiningCharge );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( (int)( posX * 32D ) );
	        dataStream.writeInt( (int)( posY * 32D ) );
	        dataStream.writeInt( (int)( posZ * 32D ) );
	        
	        dataStream.writeByte( (byte)m_iFacing );
	        dataStream.writeByte( (byte)m_iFuse );
	        
	        dataStream.writeByte( ( m_bAttachedToBlock ? 1 : 0 ) );
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    @Override
    public int GetTrackerViewDistance()
    {
    	return 160;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 10;
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

    private void explode()
    {
        FCExplosionMining explosion = new FCExplosionMining( worldObj, posX, posY, posZ, m_iFacing );
        
        explosion.doExplosion();
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    	// empty override to prevent network entity teleport packets from fucking up position and bounding box
    }

	@Override
    public float getShadowSize()
    {
        return 0.0F;
    }
}