// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCEntityMovingAnchor extends Entity
	implements FCIEntityPacketHandler, FCIEntityIgnoreServerValidation
{
	// constants
	
    static final public float fMovementSpeed = 1.0F / 20.0F;
    
	private static final int m_iYMotionDataWatcherID = 22;	
    
    static final private int m_iVehicleSpawnPacketType = 102;
    
    // local vars
    
    private FCUtilsBlockPos associatedPulleyPos = new FCUtilsBlockPos();
    private int m_iAssociatedPulleyRopeStateCounter;
    private int m_iOldBottomJ;
    
    public FCEntityMovingAnchor( World world )
    {
        super( world );
        
        preventEntitySpawning = true;
        
        setSize( 0.98F, 1.98F );        
        yOffset = 0.5F;
        
        motionX = 0.0D;        
    	motionY = 0.0D;
        motionZ = 0.0D;
        
        m_iAssociatedPulleyRopeStateCounter = -1;
        
        m_iOldBottomJ = 0;
    }
    
    /*
     * Used client-side for basic init
     */ 
    public FCEntityMovingAnchor( World world, double x, double y, double z )
    {
        this( world );
        
        setPosition( x, y, z );
        
        lastTickPosX = prevPosX = x;
        lastTickPosY = prevPosY = y;
        lastTickPosZ = prevPosZ = z;
        
    	m_iOldBottomJ = MathHelper.floor_double( posY -  yOffset );    	
    }
    
    public FCEntityMovingAnchor( World world, double x, double y, double z,
    		FCUtilsBlockPos pulleyPos, int iMovementDirection )
    {
        this( world );
        
        associatedPulleyPos.i = pulleyPos.i;
        associatedPulleyPos.j = pulleyPos.j;
        associatedPulleyPos.k = pulleyPos.k;
        
        if ( iMovementDirection > 0 )
        {
        	motionY = fMovementSpeed;
        }
        else
        {
        	motionY = -fMovementSpeed;
        }
        
        setPosition( x, y, z );
        
        lastTickPosX = prevPosX = x;
        lastTickPosY = prevPosY = y;
        lastTickPosZ = prevPosZ = z;
        
    	int associatedPulleyBlockID = worldObj.getBlockId( associatedPulleyPos.i, 
    			associatedPulleyPos.j, associatedPulleyPos.k );
    	
    	if ( associatedPulleyBlockID == FCBetterThanWolves.fcPulley.blockID )
    	{   
        	FCTileEntityPulley tileEntityPulley = (FCTileEntityPulley)worldObj.getBlockTileEntity( associatedPulleyPos.i, 
					associatedPulleyPos.j, associatedPulleyPos.k );
        	
        	if ( tileEntityPulley != null )
        	{
        		m_iAssociatedPulleyRopeStateCounter = tileEntityPulley.iUpdateRopeStateCounter;
        	}
    	}
    	
    	m_iOldBottomJ = MathHelper.floor_double( posY -  yOffset );    	
    }
    
	@Override
    protected void entityInit()
    {
        dataWatcher.addObject( m_iYMotionDataWatcherID, new Integer( 0 ) );		
    }
    
	@Override
    protected void writeEntityToNBT( NBTTagCompound nbttagcompound )
    {
    	nbttagcompound.setInteger( "associatedPulleyPosI", associatedPulleyPos.i );
    	nbttagcompound.setInteger( "associatedPulleyPosJ", associatedPulleyPos.j );
    	nbttagcompound.setInteger( "associatedPulleyPosK", associatedPulleyPos.k );
    	
    	nbttagcompound.setInteger( "m_iAssociatedPulleyRopeStateCounter", m_iAssociatedPulleyRopeStateCounter );
    	
    	nbttagcompound.setInteger( "m_iOldBottomJ", m_iOldBottomJ );   	
    	
    }    	

	@Override
    protected void readEntityFromNBT( NBTTagCompound nbttagcompound )
    {
    	associatedPulleyPos.i = nbttagcompound.getInteger( "associatedPulleyPosI" );    	
    	associatedPulleyPos.j = nbttagcompound.getInteger( "associatedPulleyPosJ" );    	
    	associatedPulleyPos.k = nbttagcompound.getInteger( "associatedPulleyPosK" );
    	
        if ( nbttagcompound.hasKey( "m_iAssociatedPulleyRopeStateCounter" ) )
        {
        	m_iAssociatedPulleyRopeStateCounter = nbttagcompound.getInteger( "m_iAssociatedPulleyRopeStateCounter" );
        }
        
        if ( nbttagcompound.hasKey( "m_iOldBottomJ" ) )
        {
        	m_iOldBottomJ = nbttagcompound.getInteger( "m_iOldBottomJ" );
        }
        else
        {
        	m_iOldBottomJ = MathHelper.floor_double( posY -  yOffset );
        }
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
    	// this determines the actual collision box around the object
    	
    	return AxisAlignedBB.getBoundingBox( boundingBox.minX, boundingBox.minY, boundingBox.minZ,
			boundingBox.maxX, boundingBox.minY + FCBlockAnchor.m_dAnchorBaseHeight, boundingBox.maxZ );
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
    	
		int i = MathHelper.floor_double( posX );
		int k = MathHelper.floor_double( posZ );

		// The following is an increased radius version of the test to see whether an entity should be updated in world.java.
		// By extending the range, it ensures that an anchor won't update unless all the platforms that may be attached to it
		// are capable of updating as well.  This prevents Anchor/Platform assemblies from being broken apart during chunk save/load
		
        byte checkChunksRange = 35;

        if ( !worldObj.checkChunksExist( i - checkChunksRange, 0, k - checkChunksRange, i + checkChunksRange, 0, k + checkChunksRange ) )
        {
            return;
        }
	        
    	FCTileEntityPulley tileEntityPulley = null;
		int iBlockAboveID = worldObj.getBlockId( i, m_iOldBottomJ + 1, k );
		boolean bForceValidation = false;
    	
		if ( !worldObj.isRemote )
		{
	    	int associatedPulleyBlockID = worldObj.getBlockId( associatedPulleyPos.i, 
	    			associatedPulleyPos.j, associatedPulleyPos.k );
	        	
			int i2BlockAboveID = worldObj.getBlockId( i, m_iOldBottomJ + 2, k );
			
			boolean bPauseMotion = false;
			
	    	if ( associatedPulleyBlockID == FCBetterThanWolves.fcPulley.blockID )
	    	{   
	    		// check for broken rope
	    		
	    		if ( iBlockAboveID == FCBetterThanWolves.fcPulley.blockID ||
					iBlockAboveID == FCBetterThanWolves.fcRopeBlock.blockID ||
					i2BlockAboveID == FCBetterThanWolves.fcPulley.blockID ||
					i2BlockAboveID == FCBetterThanWolves.fcRopeBlock.blockID )
	    		{
		    		tileEntityPulley = (FCTileEntityPulley)worldObj.getBlockTileEntity( associatedPulleyPos.i, 
						associatedPulleyPos.j, associatedPulleyPos.k );
		    		
		    		if ( m_iAssociatedPulleyRopeStateCounter != tileEntityPulley.iUpdateRopeStateCounter )
		    		{
			    		if ( motionY > 0.0F )
			    		{
			    			// moving upwards
			    			
			        		if ( tileEntityPulley.IsLowering() )
			        		{
			        			// if the pulley has switched direction, change motion to match immediately
			        			
			        			motionY = -motionY;
			        			
			        			bForceValidation = true;
			        		}
			    		}
			    		else
			    		{
			    			// moving downwards
			    			
			        		if ( tileEntityPulley.IsRaising() )
			        		{
			        			// if the pulley has switched direction, change motion to match immediately
			        			
			        			motionY = -motionY;
			        			
			        			bForceValidation = true;
			        		}
			    		}
			    		
			    		m_iAssociatedPulleyRopeStateCounter = tileEntityPulley.iUpdateRopeStateCounter;
		    		}
		    		else
		    		{
		    			// the Pulley hasn't updated, perhaps due to chunk load.  Pause the anchor's motion until it updates again.
		    			
		    			return;	    			
		    		}		    		
	    		}
	    		
		        SetCorseYMotion( motionY );
	    	}
	    	
	        if ( motionY <= 0.01 && motionY >= -0.01 )
	        {
	        	// we've stopped for some reason.  Convert
	        	
				ConvertToBlock( i, m_iOldBottomJ, k );
				
		        return;	        
	        }
		}
        
    	MoveEntityInternal( motionX, motionY, motionZ );
    	
    	double newPosY = posY;
    	
    	int newBottomJ = MathHelper.floor_double( newPosY - yOffset );
    	
        // handle collisions with other entities
        
        List list = 
        	worldObj.getEntitiesWithinAABBExcludingEntity( this, getBoundingBox().expand( 0.0D, 0.15D, 0.0D ) );
        
        if ( list != null && list.size() > 0 )
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);
                
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
	        if ( m_iOldBottomJ != newBottomJ || bForceValidation )
	        {
				if ( motionY > 0.0F )
				{
					// moving upwards
					
					if ( worldObj.getBlockId( i, newBottomJ, k ) == FCBetterThanWolves.fcRopeBlock.blockID )
					{
						tileEntityPulley.AttemptToRetractRope();
					}
					
					int iTargetBlockID = worldObj.getBlockId( i, newBottomJ + 1, k );
	
					if ( iTargetBlockID != FCBetterThanWolves.fcRopeBlock.blockID ||
						tileEntityPulley == null ||
						!tileEntityPulley.IsRaising() ||
						newBottomJ + 1 >= associatedPulleyPos.j )
					{
						// we've reached the top of our rope or the pulley is no longer raising.  
						// Stop our movement.
						
						ConvertToBlock( i, newBottomJ, k );
						
						return;
					}
				}
				else
				{
					// moving downwards
					
					boolean bEnoughRope = false;
					
					if ( tileEntityPulley != null )
					{
						int iRopeRequiredToDescend = 2;
						
			    		if ( iBlockAboveID == FCBetterThanWolves.fcPulley.blockID ||
		    				iBlockAboveID == FCBetterThanWolves.fcRopeBlock.blockID )
			    		{
		    				iRopeRequiredToDescend = 1;
		    				
		    				int iOldBlockID = worldObj.getBlockId( i, m_iOldBottomJ, k );
		    				
		    	    		if ( iOldBlockID == FCBetterThanWolves.fcPulley.blockID ||
			    				iOldBlockID == FCBetterThanWolves.fcRopeBlock.blockID )
		    	    		{
		        				iRopeRequiredToDescend = 0;
		    	    		}
			    		}
			    		
			    		if ( tileEntityPulley.GetContainedRopeCount() >= iRopeRequiredToDescend )
			    		{
			    			bEnoughRope = true;
			    		}
			    		else
			    		{
			    			bEnoughRope = false;
			    		}
					}
					
					int iTargetBlockID = worldObj.getBlockId( i, newBottomJ, k );
					
					boolean bStop = false;
					
					if ( tileEntityPulley == null ||
						!tileEntityPulley.IsLowering() ||
						!bEnoughRope ) 
					{
						bStop = true;
					}
					else if ( !FCUtilsWorld.IsReplaceableBlock( worldObj, i, newBottomJ, k ) )
					{
				    	if ( !Block.blocksList[iTargetBlockID].blockMaterial.isSolid() || iTargetBlockID == Block.web.blockID ||
			        		iTargetBlockID == FCBetterThanWolves.fcBlockWeb.blockID )				    		
				    	{
				    		// we've collided with a non-solid block.  Destroy it.
				    		
				    		int iTargetMetadata = worldObj.getBlockMetadata( i, newBottomJ, k );
				    		
				    		if ( iTargetBlockID == FCBetterThanWolves.fcRopeBlock.blockID )
				    		{
				    			if ( !ReturnRopeToPulley() )
				    			{
			    		    		Block.blocksList[iTargetBlockID].dropBlockAsItem( 
			    						worldObj, i, newBottomJ, k, iTargetMetadata, 0 );
				    			}
				    		}
				    		else
				    		{
			    		        worldObj.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, 
			    		        	i, newBottomJ, k, iTargetBlockID + ( iTargetMetadata << 12 ) );
					    		
					    		Block.blocksList[iTargetBlockID].dropBlockAsItem( 
									worldObj, i, newBottomJ, k, iTargetMetadata, 0 );			    		        
				    		}				    		
				    		
				    		worldObj.setBlockWithNotify( i, newBottomJ, k, 0 );
						}
				    	else
				    	{
				    		bStop = true;
				    	}
					}
		
					if ( bStop )
					{
						ConvertToBlock( i, m_iOldBottomJ, k );
						
						return;
					}
					
					if ( tileEntityPulley != null && worldObj.getBlockId( i, newBottomJ + 1, k ) != FCBetterThanWolves.fcRopeBlock.blockID 
							&& worldObj.getBlockId( i, newBottomJ + 1, k ) != FCBetterThanWolves.fcPulley.blockID )
					{
						// make sure the pulley fills in the last block above us with rope
						
						tileEntityPulley.AttemptToDispenseRope();
					}					
				}			
				
		    	m_iOldBottomJ = newBottomJ;
	        }
		}
    }
    
	@Override
    public void moveEntity( double deltaX, double deltaY, double deltaZ )
    {
    	// this might be called by external sources (like the pistons), so we have to override it
    	// FCTODO: We may want to react to this kind of event
    	
    	NotifyAssociatedPulleyOfLossOfAnchorEntity();
    	
    	DestroyAnchorWithDrop();
    }
    
    @Override
    protected boolean ShouldSetPositionOnLoad()
    {
    	return false;
    }
    
    //------------- FCIEntityPacketHandler ------------//

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
    
    //------------- Class Specific Methods ------------//
	
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
    
    public void DestroyAnchorWithDrop()
    {
		int i = MathHelper.floor_double( posX );
    	int j = MathHelper.floor_double( posY );
		int k = MathHelper.floor_double( posZ );
		
		ItemStack anchorStack = new ItemStack( FCBetterThanWolves.fcAnchor );
		
		FCUtilsItem.EjectStackWithRandomOffset( worldObj, i, j, k, anchorStack );
		
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
    	
        int i1 = MathHelper.floor_double(getBoundingBox().minX + 0.001D);
        int k1 = MathHelper.floor_double(getBoundingBox().minY + 0.001D);
        int i2 = MathHelper.floor_double(getBoundingBox().minZ + 0.001D);
        int k3 = MathHelper.floor_double(getBoundingBox().maxX - 0.001D);
        int l3 = MathHelper.floor_double(getBoundingBox().maxY - 0.001D);
        int i4 = MathHelper.floor_double(getBoundingBox().maxZ - 0.001D);
        
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
    	AxisAlignedBB collisionBox = getBoundingBox();
    	
    	double testZoneMaxY = collisionBox.maxY + 0.075;
    	
    	double entityMinY = entity.boundingBox.minY;
    	
    	if ( entityMinY < testZoneMaxY )
    	{
    		if ( entityMinY > collisionBox.maxY - 0.25D )
    		{
    			if ( entity instanceof EntityPlayer )
    			{
    				if ( worldObj.isRemote )
    				{
    					// client only
    					ClientPushPlayer( entity );
    				}
    			}
    			else
    			{
		    		double entityYOffset = ( collisionBox.maxY + 0.01D ) - entityMinY;
		    		
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
        			
        			if ( collisionBox.minY < entityMaxY - 0.25D && testZoneMaxY > entityMaxY )
        			{
        				entity.attackEntityFrom( DamageSource.inWall, 1 );
        			}
    			}
    		}
    	}
    }
    
    public void ForceStopByPlatform()
    {
    	if ( isDead )
    	{
    		return;
    	}
    	
    	if ( motionY > 0.0F )
    	{
    		// the pulley is ascending.  Break any rope attached above
    		
    		int i = MathHelper.floor_double( posX );
    		int jAbove = MathHelper.floor_double( posY ) + 1;
    		int k = MathHelper.floor_double( posZ );
    		
    		int iBlockAboveID  = worldObj.getBlockId( i, jAbove, k ); 
    		
			if ( iBlockAboveID == FCBetterThanWolves.fcRopeBlock.blockID )
			{
				( (FCBlockRope)(FCBetterThanWolves.fcRopeBlock) ).BreakRope( worldObj, i, jAbove, k );
			}   		
    	}
    	
		int i = MathHelper.floor_double( posX );
		int j = MathHelper.floor_double( posY );
		int k = MathHelper.floor_double( posZ );
		
		ConvertToBlock( i, j, k );
    }
    
    private void ConvertToBlock( int i, int j, int k )
    {
    	boolean bCanPlace = true;
    	
    	int iTargetBlockID = worldObj.getBlockId( i, j, k );
    	
    	if ( !FCUtilsWorld.IsReplaceableBlock( worldObj, i, j, k ) )
    	{
    		if ( iTargetBlockID == FCBetterThanWolves.fcRopeBlock.blockID )
    		{
    			// this shouldn't happen, but if there is a rope at the destination, 
    			// send it back to the pulley above
    			
    			if ( !ReturnRopeToPulley() )
    			{
	    			FCUtilsItem.EjectSingleItemWithRandomOffset( worldObj, i, j, k, 
	    					FCBetterThanWolves.fcItemRope.itemID, 0 );
    			}
    		}
        	else if ( !Block.blocksList[iTargetBlockID].blockMaterial.isSolid() || iTargetBlockID == Block.web.blockID ||
        		iTargetBlockID == FCBetterThanWolves.fcBlockWeb.blockID )
	    	{
	    		int iTargetMetadata = worldObj.getBlockMetadata( i, j, k );
	    		
	    		Block.blocksList[iTargetBlockID].dropBlockAsItem( 
					worldObj, i, j, k, iTargetMetadata, 0 );
	    		
		        worldObj.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, 
		        	i, j, k, iTargetBlockID + ( iTargetMetadata << 12 ) );
			}
	    	else
	    	{
	    		bCanPlace = false;
	    	}
    	}
    	
    	if ( bCanPlace )
    	{
    		worldObj.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcAnchor.blockID );
    		
    		( (FCBlockAnchor)( FCBetterThanWolves.fcAnchor ) ).SetFacing( worldObj, i, j, k, 1 );    	
		}
    	else
    	{
    		// this shouldn't usually happen, but if the block is already occupied, eject the anchor
    		// as an item
    		
			FCUtilsItem.EjectSingleItemWithRandomOffset( worldObj, i, j, k, 
				FCBetterThanWolves.fcAnchor.blockID, 0 );
    	}
    	
    	NotifyAssociatedPulleyOfLossOfAnchorEntity();
    	
    	setDead();
    }
    
    public boolean ReturnRopeToPulley()
    {
    	int associatedPulleyBlockID = worldObj.getBlockId( associatedPulleyPos.i, 
			associatedPulleyPos.j, associatedPulleyPos.k );
    	
    	if ( associatedPulleyBlockID == FCBetterThanWolves.fcPulley.blockID )
    	{    		
    		// FCTODO: Check for the continuity of the rope here
    		
    		FCTileEntityPulley tileEntityPulley = 
    			(FCTileEntityPulley)worldObj.getBlockTileEntity( associatedPulleyPos.i, 
    					associatedPulleyPos.j, associatedPulleyPos.k );
    		
    		if ( tileEntityPulley != null )
    		{
    			tileEntityPulley.AddRopeToInventory();
    			
    			return true;
    		}    		
    	}
    	
    	return false;
    }
    
    private void NotifyAssociatedPulleyOfLossOfAnchorEntity()
    {
    	int associatedPulleyBlockID = worldObj.getBlockId( associatedPulleyPos.i, 
    			associatedPulleyPos.j, associatedPulleyPos.k );
        	
    	if ( associatedPulleyBlockID == FCBetterThanWolves.fcPulley.blockID )
    	{    		
    		FCTileEntityPulley tileEntityPulley = 
    			(FCTileEntityPulley)worldObj.getBlockTileEntity( associatedPulleyPos.i, 
    					associatedPulleyPos.j, associatedPulleyPos.k );
    		
    		tileEntityPulley.NotifyOfLossOfAnchorEntity();
    	}    	
    }
    
	//----------- Client Side Functionality -----------//
	
	@Override
    public float getShadowSize()
    {
        return 0.0F;
    }
    
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    	// empty override to prevent network entity teleport packets from fucking up position and bounding box
    	setPosition( par1, par3, par5 );
    }
    
	private void ClientPushPlayer( Entity entity )
	{
    	double entityMinY = entity.boundingBox.minY;
    	AxisAlignedBB collisionBox = getBoundingBox();
    	
		double entityYOffset = ( collisionBox.maxY + 0.01D ) - entityMinY;
		
		entity.setPosition( entity.posX, entity.posY + entityYOffset, entity.posZ );
		
		entity.serverPosX = (int)( entity.posX * 32D );
		entity.serverPosY = (int)( entity.posY * 32D );
		entity.serverPosZ = (int)( entity.posZ * 32D );
		
		if ( entity.riddenByEntity != null )
		{
			entity.riddenByEntity.setPosition( entity.riddenByEntity.posX, entity.riddenByEntity.posY + entityYOffset, entity.riddenByEntity.posZ );		    		
		}
		
		entity.onGround = true;
	}
}