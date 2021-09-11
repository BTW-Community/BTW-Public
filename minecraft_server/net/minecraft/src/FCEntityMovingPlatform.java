// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCEntityMovingPlatform extends Entity
	implements FCIEntityPacketHandler, FCIEntityIgnoreServerValidation
{
	// constants
	
	private static final int m_iYMotionDataWatcherID = 22;
	
    static final private int m_iVehicleSpawnPacketType = 103;
    
    // local vars

	// the following tracking variables are necessary
	private double m_AssociatedAnchorLastKnownXPos;
	private double m_AssociatedAnchorLastKnownYPos;
	private double m_AssociatedAnchorLastKnownZPos;
	
    public FCEntityMovingPlatform( World world )
    {
        super( world );
        
        preventEntitySpawning = true;
        
        setSize( 0.98F, 0.98F );        
        yOffset = height / 2.0F;
        
        motionX = 0.0D;        
    	motionY = 0.0D;
        motionZ = 0.0D;
        
    	m_AssociatedAnchorLastKnownXPos = 0.0D;
    	m_AssociatedAnchorLastKnownYPos = 0.0D;
    	m_AssociatedAnchorLastKnownZPos = 0.0D;    	
    }
    
    public FCEntityMovingPlatform( World world, double x, double y, double z,
    		FCEntityMovingAnchor entityMovingAnchor )
    {
        this( world );
        
        if ( entityMovingAnchor != null )
        {
	        m_AssociatedAnchorLastKnownXPos = entityMovingAnchor.posX;
	        m_AssociatedAnchorLastKnownYPos = entityMovingAnchor.posY;
	        m_AssociatedAnchorLastKnownZPos = entityMovingAnchor.posZ;
	        
	        motionY = entityMovingAnchor.motionY;
        }
        
        setPosition( x, y, z );
        
        lastTickPosX = prevPosX = x;
        lastTickPosY = prevPosY = y;
        lastTickPosZ = prevPosZ = z;        
    }   
    
	@Override
    protected void entityInit()
    {
        dataWatcher.addObject( m_iYMotionDataWatcherID, new Integer( 0 ) );		
    }
    
	@Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    	nbttagcompound.setDouble( "m_AssociatedAnchorLastKnownXPos", m_AssociatedAnchorLastKnownXPos );
    	nbttagcompound.setDouble( "m_AssociatedAnchorLastKnownYPos", m_AssociatedAnchorLastKnownYPos );
    	nbttagcompound.setDouble( "m_AssociatedAnchorLastKnownZPos", m_AssociatedAnchorLastKnownZPos );
    }

	@Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    	m_AssociatedAnchorLastKnownXPos = nbttagcompound.getDouble( "m_AssociatedAnchorLastKnownXPos" );
    	m_AssociatedAnchorLastKnownYPos = nbttagcompound.getDouble( "m_AssociatedAnchorLastKnownYPos" );
    	m_AssociatedAnchorLastKnownZPos = nbttagcompound.getDouble( "m_AssociatedAnchorLastKnownZPos" );
    }
    
	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }

	@Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

	@Override
    public boolean canBePushed()
    {
        return false;
    }

	@Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }
    
	@Override
    public void onUpdate()
    {
    	if ( isDead )
    	{
    		return;
    	}
    
    	if ( worldObj.isRemote )
    	{
        	motionY = GetCorseYMotion();        	
    	}
    	
		FCEntityMovingAnchor associatedMovingAnchor = null; 
		
		boolean bPauseMotion = false;
		
		int i = MathHelper.floor_double( posX );
    	int oldCenterJ = MathHelper.floor_double( posY );
		int k = MathHelper.floor_double( posZ );
		
    	if ( !worldObj.isRemote )
    	{
			// find our anchor
			
	        List list = worldObj.getEntitiesWithinAABB( FCEntityMovingAnchor.class, 
	    		AxisAlignedBB.getAABBPool().getAABB( 
				m_AssociatedAnchorLastKnownXPos - 0.25F, m_AssociatedAnchorLastKnownYPos - 0.25F, m_AssociatedAnchorLastKnownZPos - 0.25F,
				m_AssociatedAnchorLastKnownXPos + 0.25F, m_AssociatedAnchorLastKnownYPos + 0.25F, m_AssociatedAnchorLastKnownZPos + 0.25F ) );
				
	        if ( list != null && list.size() > 0 )
	        {        	
	        	associatedMovingAnchor = (FCEntityMovingAnchor)list.get( 0 );
	        	
	        	if ( !associatedMovingAnchor.isDead )
	        	{
			    	motionY =  associatedMovingAnchor.posY - m_AssociatedAnchorLastKnownYPos;//associatedMovingAnchor.motionY;
			    	
			    	if ( motionY < 0.01D && motionY > -0.01D )
			    	{
			    		// the anchor has stopped moving, likely due to chunk load, so pause our motion here.
			    		
			    		motionY = 0.0D;
			    		bPauseMotion = true;		    		
			    	}
			    	
			    	m_AssociatedAnchorLastKnownXPos = associatedMovingAnchor.posX;
			    	m_AssociatedAnchorLastKnownYPos = associatedMovingAnchor.posY;
			    	m_AssociatedAnchorLastKnownZPos = associatedMovingAnchor.posZ;
	        	}
	        	else
	        	{
	        		associatedMovingAnchor = null;
	        	}
	        }
	        
	        SetCorseYMotion( motionY );
    	}
    	
    	// move the platform    	
    	
    	double oldPosY = posY;
    	
    	MoveEntityInternal( motionX, motionY, motionZ );
    	
    	double newPosY = posY;
    	
        // handle collisions
        
    	List collisionList = worldObj.getEntitiesWithinAABBExcludingEntity( this, 
    		boundingBox.expand( 0.0D, 0.15D, 0.0D ) );
        
        if ( collisionList != null && collisionList.size() > 0 )
        {
            for(int j1 = 0; j1 < collisionList.size(); j1++)
            {
                Entity entity = (Entity)collisionList.get(j1);
   
                if( entity.canBePushed() || ( entity instanceof EntityItem ) || ( entity instanceof EntityXPOrb ) )
                {
                    PushEntity( entity );
                }
                else if ( !entity.isDead )
                {
	                if ( entity instanceof FCEntityMechPower )
	                {
	                	FCEntityMechPower entityDevice = (FCEntityMechPower)entity;
	                	
	                	entityDevice.DestroyWithDrop();
	                }
                }
            }
        }
        
    	if ( !worldObj.isRemote )
    	{
	    	if ( associatedMovingAnchor == null )
	    	{
	    		// the anchor has come to a stop or otherwise dissapeared and we should do the same
	    		
				ConvertToBlock( i, oldCenterJ, k, null, motionY > 0.0F  );
				
				return;
	    	}    	
	    	
	    	if ( !bPauseMotion )
	    	{
				if ( motionY > 0.0F )
				{
					// moving upwards
					
			    	int newTopJ = MathHelper.floor_double( newPosY + 0.49F );
			    	
		    		// we're entering a new block
		    		
					int iTargetBlockID = worldObj.getBlockId( i, newTopJ, k );
					
					if ( !FCUtilsWorld.IsReplaceableBlock( worldObj, i, newTopJ, k ) )
					{
				    	if ( !Block.blocksList[iTargetBlockID].blockMaterial.isSolid() || iTargetBlockID == Block.web.blockID ||
			        		iTargetBlockID == FCBetterThanWolves.fcBlockWeb.blockID )				    		
				    	{
				    		int iTargetMetadata = worldObj.getBlockMetadata( i, newTopJ, k );
				    		
				    		// we've collided with a non-solid block.  Destroy it.
				    		
				    		Block.blocksList[iTargetBlockID].dropBlockAsItem( 
								worldObj, i, newTopJ, k, iTargetMetadata, 0 );
				    		
				    		worldObj.setBlockWithNotify( i, newTopJ, k, 0 );
				    		
		    		        worldObj.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, 
		    		        	i, newTopJ, k, iTargetBlockID + ( iTargetMetadata << 12 ) );
						}
				    	else
				    	{
							// we've collided with something.  Stop movement of the entire platform
							
							ConvertToBlock( i, oldCenterJ, k, associatedMovingAnchor, true );
							
							associatedMovingAnchor.ForceStopByPlatform();
							
							return;
				    	}
					}
				}
				else
				{
					// moving downwards
					
			    	int newBottomJ = MathHelper.floor_double( newPosY - 0.49F );
			    	
					int iTargetBlockID = worldObj.getBlockId( i, newBottomJ, k );
						
					if ( !FCUtilsWorld.IsReplaceableBlock( worldObj, i, newBottomJ, k ) )
					{
				    	if ( !Block.blocksList[iTargetBlockID].blockMaterial.isSolid() || iTargetBlockID == Block.web.blockID ||
				    		iTargetBlockID == FCBetterThanWolves.fcBlockWeb.blockID )				    		
				    	{
				    		int iTargetMetadata = worldObj.getBlockMetadata( i, newBottomJ, k );
				    		
				    		// we've collided with a non-solid block.  Destroy it.
				    		
				    		Block.blocksList[iTargetBlockID].dropBlockAsItem( 
								worldObj, i, newBottomJ, k, iTargetMetadata, 0 );
				    		
				    		worldObj.setBlockWithNotify( i, newBottomJ, k, 0 );
				    		
		    		        worldObj.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, 
		    		        	i, newBottomJ, k, iTargetBlockID + ( iTargetMetadata << 12 ) );
						}
				    	else
				    	{
		    				// we've collided with something.  Stop movement of the entire platform    				
		    				
		    				ConvertToBlock( i, oldCenterJ, k, associatedMovingAnchor, false );
		    				
		    				associatedMovingAnchor.ForceStopByPlatform();
		    				
		    				return;
				    	}
			    	}
				}
	    	}
    	}
	}
    
	@Override
    public void moveEntity( double deltaX, double deltaY, double deltaZ )
    {
    	// this might be called by external sources (like the pistons), so we have to override it
    	
    	// since we are already dealing with a moving platform here, and since handling it any other way 
    	// would result in a ton of exception cases forming, just destroy the platform outright.
    	
    	DestroyPlatformWithDrop();
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
		return new Packet23VehicleSpawn( this, GetVehicleSpawnPacketType(), 0 );
    }
    
    @Override
    public int GetTrackerViewDistance()
    {
    	return 160;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 3;
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

    private double GetCorseYMotion()
    {
        return (double)( dataWatcher.getWatchableObjectInt( m_iYMotionDataWatcherID ) ) / 100F;
    }
    
    private void SetCorseYMotion( double yMotion )
    {
        dataWatcher.updateObject( m_iYMotionDataWatcherID, Integer.valueOf( (int)( yMotion * 100F ) ) );
    }
    
    static public int GetVehicleSpawnPacketType()
    {
    	return m_iVehicleSpawnPacketType;
    }
    
    public void DestroyPlatformWithDrop()
    {
		int i = MathHelper.floor_double( posX );
    	int j = MathHelper.floor_double( posY );
		int k = MathHelper.floor_double( posZ );
		
		ItemStack platformStack = new ItemStack( FCBetterThanWolves.fcPlatform );
		
		FCUtilsItem.EjectStackWithRandomOffset( worldObj, i, j, k, platformStack );
		
    	setDead();
    }
    
    private void MoveEntityInternal( double deltaX, double deltaY, double deltaZ )
    {
    	double newPosX = posX + deltaX;
    	double newPosY = posY + deltaY;
    	double newPosZ = posZ + deltaZ;
    	
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
        setPosition( newPosX, newPosY, newPosZ );     
        
        TestForBlockCollisions();
    }
    
    private void TestForBlockCollisions()
    {
    	// FCTODO: Code ripped out of Entity.java.  Clean up and rename variables appropriately
    	
        int i1 = MathHelper.floor_double(boundingBox.minX + 0.001D);
        int k1 = MathHelper.floor_double(boundingBox.minY + 0.001D);
        int i2 = MathHelper.floor_double(boundingBox.minZ + 0.001D);
        int k3 = MathHelper.floor_double(boundingBox.maxX - 0.001D);
        int l3 = MathHelper.floor_double(boundingBox.maxY - 0.001D);
        int i4 = MathHelper.floor_double(boundingBox.maxZ - 0.001D);
        
        if(worldObj.checkChunksExist(i1, k1, i2, k3, l3, i4))
        {
            for(int j4 = i1; j4 <= k3; j4++)
            {
                for(int k4 = k1; k4 <= l3; k4++)
                {
                    for(int l4 = i2; l4 <= i4; l4++)
                    {
                        int i5 = worldObj.getBlockId(j4, k4, l4);
                        if(i5 > 0)
                        {
                            Block.blocksList[i5].onEntityCollidedWithBlock(worldObj, j4, k4, l4, this);
                        }
                    }
                }
            }
        }
    }
    
    private void PushEntity( Entity entity )
    {
    	double testZoneMaxY = boundingBox.maxY + 0.075;
    	
    	double entityMinY = entity.boundingBox.minY;
    	
    	if ( entityMinY < testZoneMaxY )
    	{
    		if ( entityMinY > boundingBox.maxY - 0.5D )
    		{
    			// only update player pos on the client
    			
    			if ( entity instanceof EntityPlayer )
    			{
    			}    			
    			else
    			{
		    		double entityYOffset = ( boundingBox.maxY + 0.01D ) - entityMinY;
		    		
	    			entity.setPosition( entity.posX, entity.posY + entityYOffset, entity.posZ );
	    			
		    		if ( entity.riddenByEntity != null )
		    		{
			    		entity.riddenByEntity.setPosition( entity.riddenByEntity.posX, entity.riddenByEntity.posY + entityYOffset, entity.riddenByEntity.posZ );		    		
		    		}
    			}
    		}
    		else if ( entity instanceof EntityLiving )
    		{
    			if ( motionY < 0F )
    			{
        			double entityMaxY = entity.boundingBox.maxY;
        			
        			if ( boundingBox.minY < entityMaxY - 0.25D && testZoneMaxY > entityMaxY )
        			{
        				entity.attackEntityFrom( DamageSource.inWall, 1 );
        			}
    			}
    		}
    	}
    }

    // associatedAnchor can be null, should only be called on the server
    private void ConvertToBlock( int i, int j, int k, FCEntityMovingAnchor associatedAnchor, boolean bMovingUpwards )
    {
    	boolean moveEntities = true;
    	
    	int iTargetBlockID = worldObj.getBlockId( i, j, k );
    	
    	if ( FCUtilsWorld.IsReplaceableBlock( worldObj, i, j, k ) )
    	{
    		worldObj.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcPlatform.blockID );
    	}
    	else if ( !Block.blocksList[iTargetBlockID].blockMaterial.isSolid() || iTargetBlockID == Block.web.blockID ||
    		iTargetBlockID == FCBetterThanWolves.fcBlockWeb.blockID )				    		
    	{
    		int iTargetMetadata = worldObj.getBlockMetadata( i, j, k );
    		
    		Block.blocksList[iTargetBlockID].dropBlockAsItem( 
				worldObj, i, j, k, iTargetMetadata, 0 );
    		
	        worldObj.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, 
	        	i, j, k, iTargetBlockID + ( iTargetMetadata << 12 ) );
	        
    		worldObj.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcPlatform.blockID );
		}
    	else
    	{
    		// this shouldn't usually happen, but if the block is already occupied, eject the platform
    		// as an item
    		
			FCUtilsItem.EjectSingleItemWithRandomOffset( worldObj, i, j, k, 
				FCBetterThanWolves.fcPlatform.blockID, 0 );
			
			moveEntities = false;
    	}
    	
    	FCUtilsMisc.PositionAllNonPlayerMoveableEntitiesOutsideOfLocation( worldObj, i, j, k );
    	
		// FCTODO: hacky way of making sure players don't fall through platforms when they stop
		
    	if ( !bMovingUpwards )
    	{
    		FCUtilsMisc.ServerPositionAllPlayerEntitiesOutsideOfLocation( worldObj, i, j + 1, k );
    		FCUtilsMisc.ServerPositionAllPlayerEntitiesOutsideOfLocation( worldObj, i, j, k );
    	}
    	else
    	{
    		FCUtilsMisc.ServerPositionAllPlayerEntitiesOutsideOfLocation( worldObj, i, j - 1, k );
    		FCUtilsMisc.ServerPositionAllPlayerEntitiesOutsideOfLocation( worldObj, i, j, k );
    	}
    	
    	setDead();
    }
    
}