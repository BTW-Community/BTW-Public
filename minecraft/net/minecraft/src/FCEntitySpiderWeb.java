// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class FCEntitySpiderWeb extends EntityThrowable
	implements FCIEntityPacketHandler
{
    public FCEntitySpiderWeb( World world )
    {
        super(world);
    }
    
    public FCEntitySpiderWeb( World world, int iItemShiftedIndex )
    {
    	this( world );
    }

    public FCEntitySpiderWeb( World world, EntityLiving throwingEntity )
    {
    	super( world, throwingEntity );
    }

    public FCEntitySpiderWeb( World world, double d, double d1, double d2 )
    {
    	super( world, d, d1, d2 );
    }

    public FCEntitySpiderWeb( World world, EntityLiving throwingEntity, Entity targetEntity )
    {
    	super( world );
    	
    	SetThrower( throwingEntity );
    	
        setSize(0.25F, 0.25F);
        
        setLocationAndAngles(throwingEntity.posX, throwingEntity.posY + (double)throwingEntity.getEyeHeight(), throwingEntity.posZ, 
        	throwingEntity.rotationYaw, throwingEntity.rotationPitch);
        
        posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        posY -= 0.2D;
        posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        
        setPosition( posX, posY, posZ );

        yOffset = 0.0F;
        
        // try to aim for the target's feet first
        
        double targetY = targetEntity.posY;
        
        if ( worldObj.rayTraceBlocks_do_do( worldObj.getWorldVec3Pool().getVecFromPool( throwingEntity.posX, throwingEntity.posY + throwingEntity.getEyeHeight(), throwingEntity.posZ), 
        	worldObj.getWorldVec3Pool().getVecFromPool( targetEntity.posX, targetY, throwingEntity.posZ ), false, true ) != null )
        {
        	// try for the center of mass
        	
            targetY = targetEntity.posY + ( targetEntity.getEyeHeight() / 2F );
            
            if ( worldObj.rayTraceBlocks_do_do( worldObj.getWorldVec3Pool().getVecFromPool( throwingEntity.posX, throwingEntity.posY + throwingEntity.getEyeHeight(), throwingEntity.posZ), 
            	worldObj.getWorldVec3Pool().getVecFromPool( targetEntity.posX, targetY, throwingEntity.posZ ), false, true ) != null )
            {
            	// eye to eye contact has already been established by the attack code, so just use that
            	
            	targetY = targetEntity.posY + targetEntity.getEyeHeight();
            }
        }
        
        double deltaX = targetEntity.posX - posX;
        double deltaY = targetY - posY;
        double deltaZ = targetEntity.posZ - posZ;
        
        setThrowableHeading( deltaX, deltaY, deltaZ, 1.5F, 1.0F );
        
        motionY += 0.1F; // slight vertical offset to compensate for drop
    }

	@Override
    protected void onImpact(MovingObjectPosition impactPos)
    {
		Entity entityHit = impactPos.entityHit;
		
        if ( entityHit != null )
        {
            entityHit.attackEntityFrom( DamageSource.causeThrownDamage( this, getThrower() ), 0 );
            
            if( !worldObj.isRemote )
            {
	        	int iWebI = MathHelper.floor_double( entityHit.posX );
	        	int iWebJ = MathHelper.floor_double( entityHit.posY );
	        	int iWebK = MathHelper.floor_double( entityHit.posZ );
	
	        	// attempt to place at feet of entity first
	        	
	        	if ( !AttemptToPlaceWebInBlock( iWebI, iWebJ - 1, iWebK ) )
	        	{
	        		AttemptToPlaceWebInBlock( iWebI, iWebJ, iWebK );
	        	}
            }
        }
        else
        {        	
            if( !worldObj.isRemote )
            {
	        	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( impactPos.blockX, impactPos.blockY, impactPos.blockZ, impactPos.sideHit );
	        	
	    		AttemptToPlaceWebInBlock( targetPos.i, targetPos.j, targetPos.k );
            }
        }

        setDead();
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
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeSpiderWeb );
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
    
    private boolean AttemptToPlaceWebInBlock( int i, int j, int k )
    {
    	if ( CanWebReplaceBlock( i, j, k ) )
    	{
    		worldObj.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockWeb.blockID );
        	
        	return true;
    	}
    	
    	return false;
    }
    
    private boolean CanWebReplaceBlock( int i, int j, int k )
    {
    	int iBlockID = worldObj.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	return block == null || block.CanSpitWebReplaceBlock( worldObj, i, j, k );
    }

	//----------- Client Side Functionality -----------//
}