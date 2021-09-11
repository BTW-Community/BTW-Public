// FCMOD

package net.minecraft.src;

public class FCEntityGhast extends EntityGhast
{
    private static final long m_iPlayerSwitchDimensionsGracePeriod = 600; // 30 seconds
    
    private static final double m_dMaxFireballLaunchRange = 64D;
    private static final double m_dMaxFireballLaunchRangeSq = ( m_dMaxFireballLaunchRange * m_dMaxFireballLaunchRange );
    
    private static final double m_dFireballSpawnDistFromGhast = 4D;

    private static final int m_iFireballExplosionPower = 1;
    
    private Entity m_entityTargeted = null;
    private int m_iRetargetCountdown = 0;
	
    public FCEntityGhast( World world )
    {
        super( world );
    }

    @Override
    public int getMaxHealth()
    {
        return 20;
    }
    
    @Override
    protected void updateEntityActionState()
    {
        entityAge++;        
        despawnEntity();
        
        prevAttackCounter = attackCounter;
        
        double dWaypointDeltaX = waypointX - posX;
        double dWaypointDeltaY = waypointY - posY;
        double dWaypointDeltaZ = waypointZ - posZ;
        
        double dWaypointDistSq = dWaypointDeltaX * dWaypointDeltaX + dWaypointDeltaY * dWaypointDeltaY + dWaypointDeltaZ * dWaypointDeltaZ;

        if ( dWaypointDistSq < 1D || dWaypointDistSq > 3600D )
        {
            waypointX = posX + ( ( rand.nextDouble() * 2D - 1D ) * 16D );
            waypointY = posY + ( ( rand.nextDouble() * 2D - 1D ) * 16D );
            waypointZ = posZ + ( ( rand.nextDouble() * 2D - 1D ) * 16D );
        }

        if ( courseChangeCooldown-- <= 0 )
        {
            courseChangeCooldown += rand.nextInt( 5 ) + 2;
            
            double dWaypointDist = MathHelper.sqrt_double( dWaypointDistSq );

            if ( IsCourseTraversable( waypointX, waypointY, waypointZ, dWaypointDist ) )
            {
                motionX += dWaypointDeltaX / dWaypointDist * 0.1D;
                motionY += dWaypointDeltaY / dWaypointDist * 0.1D;
                motionZ += dWaypointDeltaZ / dWaypointDist * 0.1D;
            }
            else
            {
                waypointX = posX;
                waypointY = posY;
                waypointZ = posZ;
            }
        }

        if ( m_entityTargeted == null || !m_entityTargeted.isEntityAlive() || m_iRetargetCountdown-- <= 0 )
        {
            m_entityTargeted = worldObj.getClosestVulnerablePlayerToEntity( this, 100D );

            // Give the player a grace period when they first entering the nether
            
            if ( m_entityTargeted != null && m_entityTargeted instanceof EntityPlayer )
            {
            	EntityPlayer targetPlayer = (EntityPlayer)m_entityTargeted;
            	
                long lTargetChangedDimensionTime = targetPlayer.m_lTimeOfLastDimensionSwitch;
                long lWorldTime = worldObj.getWorldTime();
                
                if ( lWorldTime > lTargetChangedDimensionTime && 
                	lWorldTime - lTargetChangedDimensionTime <= m_iPlayerSwitchDimensionsGracePeriod && 
                	targetPlayer != entityLivingToAttack )
                {
                	m_entityTargeted = null;
                }
            }
            
            if ( m_entityTargeted != null )
            {
                m_iRetargetCountdown = 20;
            }
        }

        if ( m_entityTargeted != null && m_entityTargeted.getDistanceSqToEntity( this ) < m_dMaxFireballLaunchRangeSq )
        {
            double dTargetDeltaX = m_entityTargeted.posX - posX;
            double dTargetDeltaZ = m_entityTargeted.posZ - posZ;
            
            double dTargetDeltaY = ( m_entityTargeted.boundingBox.minY + (double)( m_entityTargeted.height / 2F ) ) - 
            	( posY + (double)( height / 2F ) );
            
            renderYawOffset = rotationYaw = -(float)( Math.atan2( dTargetDeltaX, dTargetDeltaZ ) * 180D / Math.PI );

            if ( canEntityBeSeen( m_entityTargeted ) )
            {
                if ( attackCounter == 10 )
                {
                    worldObj.playAuxSFXAtEntity( null, 1007, (int)posX, (int)posY, (int)posZ, 0 );
                }

                attackCounter++;

                if ( attackCounter == 20 )
                {
                	FireAtTarget();
                }
            }
            else if ( attackCounter > 0 )
            {
                attackCounter--;
            }
        }
        else
        {
            renderYawOffset = rotationYaw = -(float)( Math.atan2( motionX, motionZ) * 180D / Math.PI );

            if ( attackCounter > 0 )
            {
                attackCounter--;
            }
        }

        if ( !worldObj.isRemote )
        {
        	// this updates whether the ghast's mouth is rendered as open for attack
        	
            boolean bMouthOpen = dataWatcher.getWatchableObjectByte( 16 ) != 0;
            boolean bShouldMouthBeOpen = attackCounter > 10;

            if ( bMouthOpen != bShouldMouthBeOpen )
            {
            	SetMouthOpen( bShouldMouthBeOpen );
            }
        }
    }

    @Override
    protected double MinDistFromPlayerForDespawn()
    {
    	return 144D;
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 500 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, Enchantment.punch.effectId );
            
            entityDropItem(itemstack, 0.0F);
    	}
    }
    
    @Override
    public int getTalkInterval()
    {
        return 80 + rand.nextInt( 480 ); // default is 80
    }
    
	@Override
    public boolean canEntityBeSeen( Entity entity )
	{
		// override to compute line of sight from the center of the ghast
		
        return worldObj.rayTraceBlocks_do_do( worldObj.getWorldVec3Pool().getVecFromPool( posX, posY + (double)height / 2D, posZ), 
        	worldObj.getWorldVec3Pool().getVecFromPool( entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ ), false, true ) == null;
	}
	
    @Override
    public boolean DoesEntityApplyToSquidPossessionCap()
    {
    	return isEntityAlive() && GetIsPersistent();
    }

    @Override
    protected int getDropItemId()
    {
        return Item.fireballCharge.itemID;
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iFortuneModifier )
    {
        int iNumDrops = rand.nextInt( 2 ) + rand.nextInt( 1 + iFortuneModifier );

        for ( int iTempCount = 0; iTempCount < iNumDrops; ++iTempCount )
        {
            dropItem( Item.ghastTear.itemID, 1 );
        }

        iNumDrops = rand.nextInt( 3 ) + rand.nextInt( 1 + iFortuneModifier );

        for ( int iTempCount = 0; iTempCount < iNumDrops; ++iTempCount )
        {
            dropItem( Item.fireballCharge.itemID, 1 );
        }
    }

    @Override
    protected float getSoundVolume()
    {
		// note that fireball launching screams and woosh are auxFX, so they aren't affected by this
		
    	if ( worldObj.provider.dimensionId == -1 )
    	{
    		return 10F;
    	}
    	
        return 3F;
    }
    
    @Override
    public boolean getCanSpawnHere()
    {
        return worldObj.checkNoEntityCollision( boundingBox ) && 
        	worldObj.getCollidingBoundingBoxes( this, boundingBox ).isEmpty() && 
        	!worldObj.isAnyLiquid( boundingBox ) &&
        	worldObj.getClosestPlayer( posX, posY, posZ, 64D ) == null;    	
    }

    @Override
    public boolean AttractsLightning()
    {
    	return false;
    }
    
	//------------- Class Specific Methods ------------//
    
    private boolean IsCourseTraversable( double dDestX, double dDestY, double dDestZ, double dDistToDest )
    {
        if ( dDestY >= 0 && dDestY <= worldObj.getHeight() )
        {        
	        double dDeltaXNorm = ( waypointX - posX ) / dDistToDest;
	        double dDeltaYNorm = ( waypointY - posY ) / dDistToDest;
	        double dDeltaZNorm = ( waypointZ - posZ ) / dDistToDest;
	        
	        AxisAlignedBB tempBox = boundingBox.copy();
	
	        for ( double dTempDist = 1; dTempDist < dDistToDest; dTempDist += 1D )
	        {
	            tempBox.offset( dDeltaXNorm, dDeltaYNorm, dDeltaZNorm );
	
	            if ( !worldObj.getCollidingBoundingBoxes( this, tempBox ).isEmpty() )
	            {
	                return false;
	            }            
	        }
	
	        // avoid destinations containing water or lava
	        
	        if ( worldObj.isAnyLiquid( tempBox )  )
	        {
	        	return false;
	        }
	        
	        return true;
        }
        
    	return false;
    }
    
    public boolean GetCanSpawnHereNoPlayerDistanceRestrictions()
    {
        return worldObj.checkNoEntityCollision( boundingBox ) && 
    		worldObj.getCollidingBoundingBoxes( this, boundingBox ).isEmpty();
    }
    
    private void SetMouthOpen( boolean bOpen )
    {
    	Byte tempByte = 0;
    	
    	if ( bOpen )
    	{
    		tempByte = 1;
    	}
    	
        dataWatcher.updateObject( 16, Byte.valueOf( tempByte ) );
    }    
    
	private void FireAtTarget()
	{
	    worldObj.playAuxSFXAtEntity( (EntityPlayer)null, 1008, (int)posX, (int)posY, (int)posZ, 0 );
	    
	    Vec3 ghastLookVec = getLook( 1F );
	    
		double dFireballX = posX + ghastLookVec.xCoord;
		double dFireballY = posY + (double)( height / 2F );
		double dFireballZ = posZ + ghastLookVec.zCoord;

        double dDeltaX = m_entityTargeted.posX - dFireballX;
        double dDeltaY = ( m_entityTargeted.posY + m_entityTargeted.getEyeHeight() ) - dFireballY;
        double dDeltaZ = m_entityTargeted.posZ - dFireballZ;
        
	    EntityLargeFireball fireball = new EntityLargeFireball( worldObj, this, dDeltaX, dDeltaY, dDeltaZ );
	    
	    fireball.field_92057_e = m_iFireballExplosionPower;
	    
	    double dDeltaLength = MathHelper.sqrt_double( dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ );
	    
	    double dUnitDeltaX = dDeltaX / dDeltaLength; 
	    double dUnitDeltaY = dDeltaY / dDeltaLength; 
	    double dUnitDeltaZ = dDeltaZ / dDeltaLength; 
	    
	    fireball.posX = dFireballX + ( dUnitDeltaX * m_dFireballSpawnDistFromGhast );
	    fireball.posY = dFireballY + ( dUnitDeltaY * m_dFireballSpawnDistFromGhast ) - ( fireball.height / 2D );
	    fireball.posZ = dFireballZ + ( dUnitDeltaZ * m_dFireballSpawnDistFromGhast );
	    
	    worldObj.spawnEntityInWorld( fireball );
	    
	    attackCounter = -40;
	}

	//----------- Client Side Functionality -----------//    
}
