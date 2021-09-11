// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntityDynamite extends Entity
	implements IProjectile, FCIEntityPacketHandler
{
	public static final int m_iTicksToDetonate = 100;
	
    public int m_iFuse;
    
    public int m_iItemShiftedIndex;
    
    public FCEntityDynamite( World world )
    {
        super( world );
        
        setSize( 0.25F, 0.40F );
        
        m_iFuse = -1;
        
        yOffset = 0.07F;
        
        preventEntitySpawning = true;
        
        m_iItemShiftedIndex = 0;
        
        isImmuneToFire = true;
    }
    
    public FCEntityDynamite( World world, int iItemShiftedIndex )
    {
        this( world );
        
        m_iItemShiftedIndex = iItemShiftedIndex;
    }

    public FCEntityDynamite( World world, EntityLiving entityliving, int iItemShiftedIndex, boolean bLit )
    {
        this( world, iItemShiftedIndex );
        
        setLocationAndAngles( entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), 
    		entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch );
        
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        
        setPosition(posX, posY, posZ);
        
        float f = 0.4F;
        
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
        
        setThrowableHeading( motionX, motionY, motionZ, 0.75F, 1.0F );
        
        if ( bLit )
        {
        	m_iFuse = m_iTicksToDetonate;
        }
    }

    public FCEntityDynamite( World world, double d, double d1, double d2, int iItemShiftedIndex )
    {
        this( world, iItemShiftedIndex );        
        
        setPosition( d, d1, d2 );        
    }

	@Override
    protected void entityInit()
    {
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger( "m_iItemShiftedIndex", m_iItemShiftedIndex );
        nbttagcompound.setInteger( "m_iFuse", m_iFuse );
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        m_iItemShiftedIndex = nbttagcompound.getInteger( "m_iItemShiftedIndex" );
        m_iFuse = nbttagcompound.getInteger( "m_iFuse" );
    }

	@Override
    public void onUpdate()
    {
    	if ( isDead )
    	{
    		return;
    	}
    	
        int iPosI = MathHelper.floor_double( posX );
        int iPosJ = MathHelper.floor_double( posY );
        int iPosK = MathHelper.floor_double( posZ );
        
        int iInBlockID = worldObj.getBlockId( iPosI, iPosJ, iPosK );
        
        if ( iInBlockID == Block.lavaMoving.blockID || iInBlockID == Block.lavaStill.blockID )
        {
        	// set to explode immediately in lava
        	
        	m_iFuse = 1;
        }
        
        if ( m_iFuse > 0 )
        {
        	m_iFuse--;
        	
	        if ( m_iFuse == 0 )
	        {
	            setDead();
	            
	            if ( !worldObj.isRemote )
	            {
	                DynamiteExplode();
	                
	                return;
	            }
	            else
	            {
	            	m_iFuse = 1;
	            }
	        }
	        
            float f3 = 0.25F;
            
            worldObj.spawnParticle( "smoke", posX - motionX * (double)f3, posY + 0.5F - motionY * (double)f3, posZ - motionZ * (double)f3, 
        		motionX * 0.10F, motionY  * 0.10F, motionZ * 0.10F );
        }
        else
        {
            if ( iInBlockID == Block.fire.blockID || iInBlockID == FCBetterThanWolves.fcBlockFireStoked.blockID )
            {
            	m_iFuse = m_iTicksToDetonate;
            	
            	worldObj.playSoundAtEntity( this, "random.fuse", 1.0F, 1.0F);
            }
            else
            {
            	if ( onGround )
            	{
            		if ( Math.abs( motionX ) < 0.01D && Math.abs( motionY ) < 0.01D &&  Math.abs( motionZ ) < 0.01D )
            		{
            			// The dynamite has come to a stop.  Convert it to an item.
            			
            			if ( !worldObj.isRemote )
            			{            					
	            			ConvertToItem();
	            			
	            			return;
            			}
            		}
            	}
            }
        }
        
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.039999999105930328D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98000001907348633D;
        motionY *= 0.98000001907348633D;
        motionZ *= 0.98000001907348633D;
        
        if ( onGround )
        {
            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
            motionY *= -0.5D;
        }
        
        // don't display fire sprites on dynamite.
        
        extinguish();
    }
	
    @Override
    protected boolean ShouldSetPositionOnLoad()
    {
    	return false;
    }
    
    //------------- IProjectile ------------//
	
	@Override
    public void setThrowableHeading( double dVectorX, double dVectorY, double dVectorZ, float fSpeed, float fRandomFactorMultiplier )
    {
		// normalize the vector
		
        float fVectorLength = MathHelper.sqrt_double( dVectorX * dVectorX + dVectorY * dVectorY + dVectorZ * dVectorZ );
        
        dVectorX /= fVectorLength;
        dVectorY /= fVectorLength;
        dVectorZ /= fVectorLength;
        
        dVectorX += rand.nextGaussian() * 0.0074999998323619366D * (double)fRandomFactorMultiplier;
        dVectorY += rand.nextGaussian() * 0.0074999998323619366D * (double)fRandomFactorMultiplier;
        dVectorZ += rand.nextGaussian() * 0.0074999998323619366D * (double)fRandomFactorMultiplier;
        
        dVectorX *= fSpeed;
        dVectorY *= fSpeed;
        dVectorZ *= fSpeed;
        
        motionX = dVectorX;
        motionY = dVectorY;
        motionZ = dVectorZ;
        
        float fFlatVectorLength = MathHelper.sqrt_double( dVectorX * dVectorX + dVectorZ * dVectorZ );
        
        prevRotationYaw = rotationYaw = (float)((Math.atan2(dVectorX, dVectorZ) * 180D) / 3.1415927410125732D);
        
        prevRotationPitch = rotationPitch = (float)((Math.atan2(dVectorY, fFlatVectorLength) * 180D) / 3.1415927410125732D);
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
    	return 10;
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
    	// FCTODO: Move this up into a parent class with the Floating Item code
    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeDynamite );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeInt( m_iItemShiftedIndex );
	        dataStream.writeInt( m_iFuse );
	        
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
    
    private void DynamiteExplode()
    {
        float f = 1.5F;
        worldObj.createExplosion(null, posX, posY, posZ, f, true);

        int iExplosionI = MathHelper.floor_double( posX );
        int iExplosionJ = MathHelper.floor_double( posY );
        int iExplosionK = MathHelper.floor_double( posZ );
        
        int iTargetBlockID = worldObj.getBlockId( iExplosionI, iExplosionJ, iExplosionK );
        
        if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID )
        {
        	RedneckFishing( iExplosionI, iExplosionJ, iExplosionK );
        }
    }
    
    private void RedneckFishing( int i, int j, int k )
    {
    	for ( int tempI = i - 2; tempI <= i + 2; tempI++ )
    	{
    		// favor deep water
    		
        	for ( int tempJ = j - 2; tempJ <= j + 4; tempJ++ )
        	{
            	for ( int tempK = k - 2; tempK <= k + 2; tempK++ )
            	{
            		if ( IsValidBlockForRedneckFishing( tempI, tempJ, tempK ) )
    				{
            			// averages one per j layer
            			
            			if ( worldObj.rand.nextInt( 25 ) == 0 )
            			{
            				SpawnRedneckFish( tempI, tempJ, tempK );
            			}
    				}            			
            	}    		
        	}    		
    	}
    }
    
    private boolean IsValidBlockForRedneckFishing( int i, int j, int k )
    {
    	// block must be totally surrounded by water (except positive j) to be valid
    	
    	for ( int tempI = i - 1; tempI <= i + 1; tempI++ )
    	{
        	for ( int tempJ = j - 1; tempJ <= j; tempJ++ )
        	{
            	for ( int tempK = k - 1; tempK <= k + 1; tempK++ )
            	{            		
                    int iTargetBlockID = worldObj.getBlockId( tempI, tempJ, tempK );
                    
                    if ( iTargetBlockID != Block.waterMoving.blockID && iTargetBlockID != Block.waterStill.blockID )
                    {
                    	return false;
                    }
            	}    		
        	}    		
    	}
    	
    	return true;
    }
    
    private void SpawnRedneckFish( int i, int j, int k )
    {
    	ItemStack stack = new ItemStack( Item.fishRaw.itemID, 1, 0 );
    	
        EntityItem entityItem;
        
        if ( FCBetterThanWolves.IsHardcoreBuoyEnabled( worldObj ) )
        {
        	entityItem = new EntityItem( worldObj, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, stack );
        }
        else
        {
        	entityItem = new FCEntityItemFloating( worldObj, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, stack );
        }
        
        worldObj.spawnEntityInWorld( entityItem );
    }
    
    private void ConvertToItem()
    {
    	FCUtilsItem.EjectSingleItemWithRandomVelocity( worldObj, (float)posX, (float)posY, (float)posZ, FCBetterThanWolves.fcItemDynamite.itemID, 0 );
    	
        setDead();        
    }
}
