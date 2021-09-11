// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

public class FCEntitySquid extends EntityWaterMob
{
    public static final float fBrightnessAgressionThreshold = 0.1F;
    
	private static final float m_fSafeAttackDepth = 0.5F;
	private static final int m_fSafeAttackDepthTestMaximum = ( (int)m_fSafeAttackDepth + 1 );
	
	private static final float m_fSafePassiveDepth = 3F;
	private static final int m_fSafePassiveDepthTestMaximum = ( (int)m_fSafePassiveDepth + 1 );
	
	private static final double m_dAgressionRange = 16D;
	private static final int m_iChanceOfLosingAttackTargetInLight = 400;
	
	private static final int m_iTentacleAttackTicksToCooldown = 100;    
	private static final double m_dTentacleAttackRange = 6D;
    public static final int m_iTentacleAttackDuration = 20;
	private static final double m_dTentacleAttackTipCollisionWidth = 0.2D;
	private static final double m_dTentacleAttackTipCollisionHalfWidth = ( m_dTentacleAttackTipCollisionWidth / 2D );
    
    private int m_iTentacleAttackCooldownTimer = m_iTentacleAttackTicksToCooldown;
    
    public int m_iTentacleAttackInProgressCounter = -1;
    private double m_dTentacleAttackTargetX = 0D;
    private double m_dTentacleAttackTargetY = 0D;
    private double m_dTentacleAttackTargetZ = 0D;
    
    private static final int m_iHeadCrabDamageInitialDelay = 40; 
    private static final int m_iHeadCrabDamagePeriod = 40;
    
    private int m_iHeadCrabDamageCounter = m_iHeadCrabDamageInitialDelay;
    
    public float m_fSquidPitch = 0F;
    public float m_fPrevSquidPitch = 0F;
    
    public float m_fSquidYaw = 0F;
    public float m_fPrevSquidYaw = 0F;
    private float m_fSquidYawSpeed = 0F;
    
    public float m_fTentacleAngle = 0F;
    public float m_fPrevTentacleAngle = 0F;    
    private float m_fTentacleAnimProgress = 0F;
    private float m_fPrevTentacleAnimProgress = 0F;
    private float m_fTentacleAnimSpeed = 0F;    
    
    private float m_fRandomMotionSpeed = 0F;
    private float m_fRandomMotionVecX = 0F;
    private float m_fRandomMotionVecY = 0F;
    private float m_fRandomMotionVecZ = 0F;
    
    private Entity m_entityToNotRecrab = null; // tracking variable to prevent squid immediately reattaching when knocked off
    private int m_iReCrabEntityCountdown = 0;
    private static final int m_iReCrabEntityTicks = 5;
    
	private static final float m_fPossessedLeapDepth = 0.5F;
	
    private static final int m_iPossessedLeapCountdownDuration = ( 10 * 20 );
    private static final int m_iPossessedLeapPropulsionDuration = 10;
    
    private int m_iPossessedLeapCountdown = 0;
    private int m_iPossessedLeapPropulsionCountdown = 0;
    
    private final float m_fPossessedLeapGhastConversionChance = 0.25F;
    private float m_fPossessedLeapGhastConversionDiceRoll = 1F;
    
    private static final int m_iSquidPossessionMaxCount = 50;
    
    public FCEntitySquid( World world )
    {
        super( world );
        
        texture = "/mob/squid.png";
        
        setSize( 0.95F, 0.95F );
        
        m_fTentacleAnimSpeed = 1F / ( rand.nextFloat() + 1F ) * 0.2F;
    }
    
    @Override
    public int getMaxHealth()
    {
    	return 20;
    }
    
    @Override
    protected String getLivingSound()
    {
        return null;
    }
    
    @Override
    protected String getHurtSound()
    {
        return null;
    }
    
    protected String getDeathSound()
    {
        return null;
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    @Override
    protected int getDropItemId()
    {
        return 0;
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iLootingModifier )
    {
        int iNumInkSacks = this.rand.nextInt( 3 + iLootingModifier ) + 1;

        for ( int iTempInkSack = 0; iTempInkSack < iNumInkSacks; ++iTempInkSack )
        {
            entityDropItem( new ItemStack( Item.dyePowder, 1, 0 ), 0F );
        }
        
        if ( rand.nextInt( 8 ) - iLootingModifier <= 0 )
        {
            dropItem( FCBetterThanWolves.fcItemMysteriousGland.itemID, 1 );
        }
    }
    
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        
        m_fPrevSquidPitch = m_fSquidPitch;
        m_fPrevSquidYaw = m_fSquidYaw;
        
        m_fPrevTentacleAnimProgress = m_fTentacleAnimProgress;
        m_fPrevTentacleAngle = m_fTentacleAngle;
        
        UpdateTentacleAttack();

        if ( !isEntityAlive() )
        {
        	// dead squids tell no tales 
        	
        	if ( !worldObj.isRemote )
        	{
                this.motionX = 0.0D;
                if (this.isInWater())
                {
                	this.motionY -= 0.02D;
                	this.motionY *= 0.8D;
                }
                else
                {
                	this.motionY -= 0.08D;
                	this.motionY *= 0.9800000190734863D;
                }
                this.motionZ = 0.0D;
        	}
            
        	return;
        }
        
        m_fTentacleAnimProgress += this.m_fTentacleAnimSpeed;
        
        if (this.m_fTentacleAnimProgress > ((float)Math.PI * 2F))
        {
            this.m_fTentacleAnimProgress -= ((float)Math.PI * 2F);

            if (this.rand.nextInt(10) == 0)
            {
                this.m_fTentacleAnimSpeed = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }
        }
        
    	if ( ridingEntity != null && !ridingEntity.isEntityAlive() )
		{
    		mountEntity( null );
    		
    		if ( !worldObj.isRemote )
    		{
	        	worldObj.playAuxSFX( FCBetterThanWolves.m_iBurpSoundAuxFXID, 
	        		MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ), 0 );
    		}
		}
        		
        if ( !inWater && getAir() % 100 == 0 )
        {
	        if ( IsPossessed() || IsHeadCrab() || IsBeingRainedOn() )
	        {
	    	    setAir( 300 ); // don't suffocate out of water
	        }
        }
        	
        if ( IsHeadCrab() )
        {
        	UpdateHeadCrab();
        	
            return;
        }
        
        if (this.isInWater()) // FCTODO: This test is likely what's screwing up client/server motion as it calls material acceleration
        {
            float var1;

            if (this.m_fTentacleAnimProgress < (float)Math.PI)
            {
                var1 = this.m_fTentacleAnimProgress / (float)Math.PI;
                this.m_fTentacleAngle = MathHelper.sin(var1 * var1 * (float)Math.PI) * (float)Math.PI * 0.25F;

                if ((double)var1 > 0.75D)
                {
                    this.m_fRandomMotionSpeed = 1.0F;
                    this.m_fSquidYawSpeed = 1.0F;
                }
                else
                {
                    this.m_fSquidYawSpeed *= 0.8F;
                }
            }
            else
            {
                this.m_fTentacleAngle = 0.0F;
                this.m_fRandomMotionSpeed *= 0.9F;
                this.m_fSquidYawSpeed *= 0.99F;
            }
        	
            if (!this.worldObj.isRemote)
            {
                this.motionX = (double)(this.m_fRandomMotionVecX * this.m_fRandomMotionSpeed);
                this.motionY = (double)(this.m_fRandomMotionVecY * this.m_fRandomMotionSpeed);
                this.motionZ = (double)(this.m_fRandomMotionVecZ * this.m_fRandomMotionSpeed);
                
                if ( m_iPossessedLeapPropulsionCountdown > 0 )
                {
                    motionY = 1F;
                }
            }            

        	if ( m_iPossessedLeapPropulsionCountdown > 0 )
        	{
        		m_iPossessedLeapPropulsionCountdown--;
        	}
        	
    		if ( m_iTentacleAttackInProgressCounter >= 0 )
    		{
    			OrientToTentacleAttackPoint();    			
    		}
    		else if ( entityToAttack != null )
    		{
    			OrientToEntity( entityToAttack );    			
    		}
    		else
    		{
    			OrientToMotion();
    		}
        }
        else
        {
        	// squid is out of water
        		
        	m_iPossessedLeapPropulsionCountdown = 0;
        	
            this.m_fTentacleAngle = MathHelper.abs(MathHelper.sin(this.m_fTentacleAnimProgress)) * (float)Math.PI * 0.25F;

            if (!this.worldObj.isRemote)
            {
                this.motionX = 0.0D;
                this.motionY -= 0.08D;
                this.motionY *= 0.9800000190734863D;
                this.motionZ = 0.0D;
            }

    		if ( m_iTentacleAttackInProgressCounter >= 0 )
    		{
    			OrientToTentacleAttackPoint();
    		}
    		else if ( motionY > 0.5F )
    		{
    			m_fSquidPitch = 0F;
    		}
    		else
    		{
    			m_fSquidPitch = (float)((double)m_fSquidPitch + (double)(-90.0F - m_fSquidPitch) * 0.02D);
    		}    		
        }        
    }
    
    @Override
    public void moveEntityWithHeading( float par1, float par2 )
    {
        moveEntity( motionX, motionY, motionZ );
    }
    
    @Override
    protected void updateEntityActionState()
    {
    	// only called on server
    	
        m_iTentacleAttackCooldownTimer--;
        
        CheckForHeadCrab();
        
        if ( IsHeadCrab() )
        {
        	UpdateHeadCrabActionState();
        	
        	return;
        }
        
        float fNaturalLightLevel = getBrightness( 1F );
        
        boolean bIsInDarkness = fNaturalLightLevel < fBrightnessAgressionThreshold;

        if ( !worldObj.isDaytime() )
        {
        	bIsInDarkness = true;
        }
        
    	if ( entityToAttack == null  )
    	{
    		if ( bIsInDarkness )
    		{
    			Entity targetEntity = FindClosestValidAttackTargetWithinRange( m_dAgressionRange );
	    		
	    		if ( targetEntity != null )
	    		{
	    			setTarget( targetEntity );
	    		}
    		}
    	}
    	else 
    	{
    		if ( bIsInDarkness || rand.nextInt( m_iChanceOfLosingAttackTargetInLight ) != 0 )
    		{
    			// FCOTOD: Clean up this test
	    		if ( !entityToAttack.IsValidOngoingAttackTargetForSquid() ||
	    			getDistanceToEntity( entityToAttack ) > m_dAgressionRange ||
	    			( 
	    				worldObj.isDaytime() && entityToAttack.getBrightness( 1F ) > fBrightnessAgressionThreshold && 
	    				rand.nextInt( m_iChanceOfLosingAttackTargetInLight ) == 0 
    				) 
				)
	    		{
	    			setTarget( null );
	    		}
    		}
    		else
    		{
    			setTarget( null );
    		}
    	}
    	
    	if ( entityToAttack != null )
    	{
    		double dDeltaX = entityToAttack.posX - posX; 
    		double dDeltaY = ( entityToAttack.posY + entityToAttack.getEyeHeight() ) - ( posY + ( height / 2 ) ); 
    		double dDeltaZ = entityToAttack.posZ - posZ;
    		
    		double dDistSqToTarget = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;

    		if ( dDistSqToTarget > ( 0.5D * 0.5D ) )
    		{
    			double dDistToTarget = MathHelper.sqrt_double( dDistSqToTarget );
    			
    			double dUnitVectorToTargetX = dDeltaX / dDistToTarget;
    			double dUnitVectorToTargetY = dDeltaY / dDistToTarget;
    			double dUnitVectorToTargetZ = dDeltaZ / dDistToTarget;
    			
	    		m_fRandomMotionVecX = (float)( dUnitVectorToTargetX * 0.4D );
	    		m_fRandomMotionVecY = (float)( dUnitVectorToTargetY * 0.4D );
	    		m_fRandomMotionVecZ = (float)( dUnitVectorToTargetZ * 0.4D );
	    		
	    		if ( !IsFullyPossessed() )
	    		{
		    		// prevent squid from leaping out of the water
		    		
	    			float fDepth = GetDepthBeneathSurface( m_fSafeAttackDepthTestMaximum );
	    			
	    			if ( fDepth < m_fSafeAttackDepth )
	    			{
	    				if ( m_fRandomMotionVecY > -0.1F )
	    				{
	    					m_fRandomMotionVecY = -0.1F;
	    				}
	    			}
	    			else if ( m_fRandomMotionVecY > 0F )
		    		{
		    			float fDeltaSafeDepth = fDepth - m_fSafeAttackDepth;
		    			
		    			if ( m_fRandomMotionVecY > fDeltaSafeDepth )
		    			{
		    				m_fRandomMotionVecY = fDeltaSafeDepth;
		    			}
		    		}
	    		}
	    		
	    		if ( inWater && ( !entityToAttack.inWater || entityToAttack.ridingEntity != null ) &&  
	    			m_iTentacleAttackInProgressCounter < 0 && m_iTentacleAttackCooldownTimer <= 0 && rand.nextInt( 20 ) == 0 )
	    		{
	    			AttemptTentacleAttackOnTarget();
	    		}	    			
    		}
    		else
    		{
        		m_fRandomMotionVecX = m_fRandomMotionVecY = m_fRandomMotionVecZ = 0.0F;
    		}    		
    	}        
    	else if ( rand.nextInt( 50 ) == 0 || !inWater || ( m_fRandomMotionVecX == 0.0F && m_fRandomMotionVecY == 0.0F && m_fRandomMotionVecZ == 0.0F ) )
        {
            float fFlatHeading = rand.nextFloat() * (float)Math.PI * 2.0F;
            
            m_fRandomMotionVecZ = MathHelper.sin( fFlatHeading ) * 0.2F;
            m_fRandomMotionVecX = MathHelper.cos( fFlatHeading ) * 0.2F;
            
			float fDepth = GetDepthBeneathSurface( m_fSafePassiveDepthTestMaximum );

			if ( IsFullyPossessed() && inWater )
			{
				// possessed squid always move towards the surface
				
				m_fRandomMotionVecY = 0.1F;
				
		    	if ( fDepth < m_fPossessedLeapDepth && m_iPossessedLeapCountdown <= 0)
		    	{	
		    		PossessedLeap();	    		
		    	}
			}				
			else if ( fDepth >= m_fSafePassiveDepth )
			{
	    		if ( fNaturalLightLevel < fBrightnessAgressionThreshold )
	    		{    		
	    			int iSkylightSubtracted = worldObj.skylightSubtracted;
	    			
	    			if ( !worldObj.isDaytime() )
	    			{
		    			// go up in darkness if the surface is dark
		    			
	    				m_fRandomMotionVecY = 0.1F;
	    			}
	    			else
	    			{
	    				// random motion tending towards downwards if the surface is light
	    				
	    				m_fRandomMotionVecY = ( rand.nextFloat() * 0.15F ) - 0.1F;
	    				
	    			}
	    		}
	    		else
	    		{
	    			// move down in light
	    			
	    			m_fRandomMotionVecY = -0.1F;
	    		}
			}
			else
			{
				// move down if not at sufficient depth
				
    			m_fRandomMotionVecY = -0.1F;
			}
        }

        entityAge++;        
        despawnEntity();
    }
    
    @Override
    protected double MinDistFromPlayerForDespawn()
    {
    	return 144D;
    }
    
    @Override
    protected boolean canDespawn()
    {
    	return !IsHeadCrab();
    }
    
    @Override
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double( posX );
        int j = MathHelper.floor_double( posY );
        int k = MathHelper.floor_double( posZ );

        if ( !IsBlockSurroundedByWater( i, j, k )
    		&& !IsBlockSurroundedByWater( i + 1, j, k )
    		&& !IsBlockSurroundedByWater( i - 1, j, k )
    		&& !IsBlockSurroundedByWater( i, j + 1, k )
    		&& !IsBlockSurroundedByWater( i, j - 1, k )
    		&& !IsBlockSurroundedByWater( i, j, k + 1 )
    		&& !IsBlockSurroundedByWater( i, j, k - 1 ) )
		{
        	return false;
        }
        
        int iLightLevel = this.worldObj.getBlockLightValue( i, j, k );
        
        if ( iLightLevel > 1 )
        {
        	return false;
        }

        return super.getCanSpawnHere();
    }
    
    @Override
    public boolean attackEntityFrom( DamageSource damageSource, int iDamageAmount )
    {
    	if ( this.IsHeadCrab() )
    	{
            if ( damageSource == DamageSource.inWall )
            {
            	return false;
            }
    	}
    	else
    	{
    		if ( IsPossessed() && damageSource == DamageSource.fall )
    		{
	        	return false;
    		}
    		
    		if ( super.attackEntityFrom( damageSource, iDamageAmount ) )
    		{
    			if ( !worldObj.isRemote )
    			{
	                Entity attackingEntity = damageSource.getEntity();
	
	                if ( attackingEntity != null && attackingEntity != this )
	                {
                		setTarget( attackingEntity );
	                }
    			}
                
    			return true;
    		}
    		
    		return false;
    	}
    	
    	return super.attackEntityFrom( damageSource, iDamageAmount );
    }
    
    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
    	// override to prevent squids playing sounds when on top of blocks.
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 250 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, Enchantment.respiration.effectId );
            
            entityDropItem(itemstack, 0.0F);
    	}
    }
    
    @Override
    public AxisAlignedBB GetVisualBoundingBox()
    {
    	if ( m_iTentacleAttackInProgressCounter >= 0 )
    	{
    		double dExpandByAmount = m_dTentacleAttackRange + 0.25D;
    		
    		return boundingBox.expand( dExpandByAmount, dExpandByAmount, dExpandByAmount );
    	}
    	
		return boundingBox;
    }
    
    @Override
    public void setTarget(Entity targetEntity )
    {
    	if ( !worldObj.isRemote && targetEntity != entityToAttack )
    	{
            entityToAttack = targetEntity;
            
            TransmitAttackTargetToClients();
    	}
    	else
    	{
            entityToAttack = targetEntity;
    	}    	
    }

    @Override
    protected boolean GetCanCreatureTypeBePossessed()
    {
    	return true;
    }
    
    @Override
    protected boolean GetCanCreatureBePossessedFromDistance( boolean bPersistentSpirit )
    {
    	// limit number squid that can be possessed from non-persistent sources (like  portals) to prevent potential performace problems
    	// in the case of persistent sources, a persistent entity has died to create the spirit, so there shouldn't be a problem
    	// with the number of entities created
    	
    	return bPersistentSpirit || worldObj.GetNumEntitiesThatApplyToSquidPossessionCap() < m_iSquidPossessionMaxCount;
    }
    
    @Override
	public boolean OnPossesedRidingEntityDeath()
	{
    	if ( isEntityAlive() && !IsPossessed() )
    	{
    		InitiatePossession();
    		
    		return true;
    	}
    	
		return false;
	}
	
    @Override
    public void InitiatePossession()
    {
    	super.InitiatePossession();
    	
    	SetPersistent( true );
    }
    
    @Override
    protected void HandlePossession()
    {
        super.HandlePossession();
        
    	// test code for immediate full possession
        /*
        if ( !worldObj.isRemote )
        {
	    	if ( IsPossessed() )
	    	{
	    		if ( GetPossessionLevel() == 1 )
	    		{
	    			m_iPossessionTimer = 0;
	    			
	    			SetPossessionLevel( 2 );
		    			
	    			OnFullPossession();	    			
	    		}
	    	}
        }
        */
    	
        if ( m_iPossessedLeapCountdown > 0 )
        {
        	m_iPossessedLeapCountdown--;
        }
        
        if ( !worldObj.isRemote && IsFullyPossessed() )
        {
			if ( ridingEntity == null && !inWater && !onGround )
			{
				if ( m_fPossessedLeapGhastConversionDiceRoll <= m_fPossessedLeapGhastConversionChance && 
					motionY <= 0F ) // negative motion to wait for apex of leap
				{					
			        FCEntityGhast ghast = new FCEntityGhast( worldObj );
			        ghast.setLocationAndAngles( posX, posY, posZ, rotationYaw, 0F );
			        
					if ( worldObj.checkNoEntityCollision( ghast.boundingBox, this ) && 
						worldObj.getCollidingBoundingBoxes( this, ghast.boundingBox ).isEmpty() && 
						!worldObj.isAnyLiquid( ghast.boundingBox ) )
					{
				        worldObj.playAuxSFX( FCBetterThanWolves.m_iPossessedSquidTransformToGhastAuxFXID, 
				    		MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ), 
				    		0 );
				        
				        this.setDead();
				        
				        ghast.SetPersistent( true );
				        
				        worldObj.spawnEntityInWorld( ghast );    			        
					}
				}
			}
			else
			{
				if ( !inWater || motionY <= 0F )
				{
					// reset the dice roll until the next leap occurs
					
					m_fPossessedLeapGhastConversionDiceRoll = 1F;
				}
			}
        }        
    }
    
    @Override
    public boolean DoesEntityApplyToSquidPossessionCap()
    {
    	return isEntityAlive() && GetIsPersistent();
    }
    
    @Override
    public boolean IsValidZombieSecondaryTarget( EntityZombie zombie )
    {
    	return !inWater && ridingEntity == null && zombie.riddenByEntity == null;
    }
    
    @Override
    public boolean AttractsLightning()
    {
    	return false;
    }

    @Override
    public float getEyeHeight()
    {
        return height * 0.5F;
    }

    //------------- Class Specific Methods ------------//
    
	private void UpdateHeadCrabActionState()
	{
		// only called on server
		
		Entity sharedTarget = ridingEntity.GetHeadCrabSharedAttackTarget();
		
		if ( sharedTarget == this )
		{
			sharedTarget = null;
		}
		
		setTarget( sharedTarget );
		
		if ( entityToAttack != null )
		{
    		if ( m_iTentacleAttackInProgressCounter < 0 && m_iTentacleAttackCooldownTimer <= 0 && rand.nextInt( 20 ) == 0 )
    		{
    			AttemptTentacleAttackOnTarget();
    		}	    			
		}		
    	
    	if ( IsFullyPossessed() && m_iPossessedLeapCountdown <= 0 && !inWater && rand.nextInt( 100 ) == 0  )
    	{	
    		mountEntity( null );
    		
    		PossessedLeap();	    		
    	}
	}
	
    private void OrientToMotion()
    {
        float fMotionVectorFlatLength = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        
        renderYawOffset = InterpolateAngle( renderYawOffset, -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI, 1F );
        rotationYaw = renderYawOffset;
        
        m_fSquidPitch += (-((float)Math.atan2((double)fMotionVectorFlatLength, motionY)) * 180.0F / (float)Math.PI - m_fSquidPitch) * 0.1F;
        
        m_fSquidYaw += (float)Math.PI * m_fSquidYawSpeed * 1.5F; // this is actually the squid's roll        
    }
    
    private void OrientToEntity( Entity entity )
    {
		double dDeltaX = entity.posX - posX; 
		double dDeltaY = entity.posY + entity.getEyeHeight() - ( posY + ( height / 2 ) ); 
		double dDeltaZ = entity.posZ - posZ;
		
        double dFlatDist = MathHelper.sqrt_double( dDeltaX * dDeltaX + dDeltaZ * dDeltaZ );
        
        renderYawOffset = InterpolateAngle( renderYawOffset, -((float)Math.atan2( dDeltaX, dDeltaZ )) * 180.0F / (float)Math.PI, 1F );
        rotationYaw = renderYawOffset;
        
        m_fSquidPitch = InterpolateAngle( m_fSquidPitch, -(float)( Math.atan2( dFlatDist, dDeltaY ) * 180.0F / Math.PI ), 10F );
        
        m_fSquidYaw += (float)Math.PI * m_fSquidYawSpeed * 1.5F; // this is actually the squid's roll
    }
    
    private void OrientToTentacleAttackPoint()
    {
		double dDeltaX = m_dTentacleAttackTargetX - posX; 
		double dDeltaY = m_dTentacleAttackTargetY - ( posY + ( height / 2 ) ); 
		double dDeltaZ = m_dTentacleAttackTargetZ - posZ;
		
        double dFlatDist = MathHelper.sqrt_double( dDeltaX * dDeltaX + dDeltaZ * dDeltaZ );
        
        renderYawOffset = InterpolateAngle( renderYawOffset, -((float)Math.atan2( dDeltaX, dDeltaZ )) * 180.0F / (float)Math.PI, 50F );
        rotationYaw = renderYawOffset;
        
        m_fSquidPitch = InterpolateAngle( m_fSquidPitch, -(float)(  Math.atan2( dFlatDist, dDeltaY ) * 180.0F / Math.PI - 150D ), 50F );
        
        m_fSquidYaw = InterpolateAngle( m_fSquidYaw, 0, 50F ); // this is actually the squid's roll
    }
    
    private Entity FindClosestValidAttackTargetWithinRange( double dRange )
    {
        Entity targetEntity = null;
        double dClosestDistSq = dRange * dRange; 

        for ( int iPlayerCount = 0; iPlayerCount < worldObj.playerEntities.size(); ++iPlayerCount )
        {
            EntityPlayer tempPlayer = (EntityPlayer)worldObj.playerEntities.get( iPlayerCount );

            if ( !tempPlayer.capabilities.disableDamage && tempPlayer.isEntityAlive() )
            {
        		double dDeltaX = tempPlayer.posX - posX; 
        		double dDeltaY = tempPlayer.posY - posY; 
        		double dDeltaZ = tempPlayer.posZ - posZ;
        		
        		double dDistSq  = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
        		
        		if ( dDistSq < dClosestDistSq && ( !worldObj.isDaytime() || tempPlayer.getBrightness( 1F ) < fBrightnessAgressionThreshold ) &&
        			( tempPlayer.inWater || canEntityBeSeen( tempPlayer ) ) )
        		{
        			targetEntity = tempPlayer;
        			dClosestDistSq = dDistSq; 
        		}
            }
        }
        
        // stagger secondary scans to only occur every 32 ticks for performance
        
        if ( ( ( worldObj.worldInfo.getWorldTime() + entityId ) & 31 ) == 0 && targetEntity == null  )
        {
        	targetEntity = worldObj.GetClosestEntityMatchingCriteriaWithinRange( 
        		posX, posY, posZ, dRange, FCClosestEntitySelectionCriteria.m_secondarySquidTarget );
        }
        
        return targetEntity;
    }
    
    private void CheckForHeadCrab()
    {
    	if ( isEntityAlive() )
    	{
			if ( ridingEntity == null )
			{		        
				if ( motionY < 0.5F )
				{
			    	if ( m_iReCrabEntityCountdown > 0 )
			    	{
			    		m_iReCrabEntityCountdown--;
			    	}
			    	else
			    	{
			    		m_entityToNotRecrab = null;
			    	}
			    	
					EntityLiving target = GetValidHeadCrabTargetInRange();
					
					if ( target != null )
					{
						mountEntity( target );
						
			            playSound( "mob.slime.attack", 1F, ( rand.nextFloat() - 
			            	rand.nextFloat() ) * 0.2F + 1F );
			            
			            m_iHeadCrabDamageCounter = m_iHeadCrabDamageInitialDelay;
			            
			    		target.OnHeadCrabbedBySquid( this );
					}
				}
			}
			else
			{
		    	m_entityToNotRecrab = ridingEntity; // tracking variable to prevent squid immediately reattaching when knocked off
		        m_iReCrabEntityCountdown = m_iReCrabEntityTicks;
			}
    	}
    }
    
    private EntityLiving GetValidHeadCrabTargetInRange()
    {
    	double dRange = 0.25D;
    	
    	if ( !isInWater() )
    	{
    		dRange = 0.5D;
    	}
    	
        List<EntityLiving> entityList = worldObj.getEntitiesWithinAABB( EntityLiving.class, 
        	boundingBox.expand( dRange, dRange, dRange ) );
        
        Iterator<EntityLiving> entityIterator = entityList.iterator();

        while ( entityIterator.hasNext() )
        {
    		EntityLiving tempEntity = entityIterator.next();
    		
            if ( tempEntity.GetCanBeHeadCrabbed( isInWater() ) &&
            	tempEntity != m_entityToNotRecrab && canEntityBeSeen( tempEntity ) )
	        {
            	return tempEntity;
	        }		        
        }
        
        return null;        
    }
    
	private void UpdateHeadCrab()
	{
	    m_fTentacleAnimSpeed = 0.2F;
	    
		m_fSquidPitch = 0;
		
	    // same tentacle movement as out of water
	    
	    float fSinTentacle = MathHelper.sin( m_fTentacleAnimProgress );
	    
	    this.m_fTentacleAngle = MathHelper.abs(MathHelper.sin( fSinTentacle ) ) * (float)Math.PI * 0.25F;
	    
	    if ( !worldObj.isRemote )
	    {
	    	m_iHeadCrabDamageCounter--;
	    	
	    	if ( m_iHeadCrabDamageCounter <= 0 )
	    	{
	    		if ( !ridingEntity.IsImmuneToHeadCrabDamage() )
	    		{
		    		DamageSource squidSource = DamageSource.causeMobDamage( this );
		    		
		    		squidSource.setDamageBypassesArmor();
		    
		    		ridingEntity.attackEntityFrom( squidSource, 1 );
	    		}
	    		
	    		m_iHeadCrabDamageCounter = m_iHeadCrabDamagePeriod;
	    	}
	    	
	    	// knock the player out of boats, minecarts, or whatever else he might be riding
	    	
	    	if ( ridingEntity.ridingEntity != null )
	    	{
	    		ridingEntity.mountEntity( ridingEntity.ridingEntity );
	    		
	            if ( ridingEntity.ridingEntity != null)
	            {
	                ridingEntity.ridingEntity.riddenByEntity = null;
	                ridingEntity.ridingEntity = null;
	            }
	    	}
	    }
	    else
	    {
	    	// check if the tentacles have reversed direction
	    	
	        float fPrevSinTentacle = MathHelper.sin( m_fPrevTentacleAnimProgress );
	        
	    	if ( ( fPrevSinTentacle <= 0 && fSinTentacle > 0 ) || ( fPrevSinTentacle > 0 && fSinTentacle <= 0 ) )
	    	{
	    		if ( !ridingEntity.IsImmuneToHeadCrabDamage() )
	    		{
		            worldObj.playSound( posX, posY, posZ, "random.eat", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F);
		            worldObj.playSound( posX, posY, posZ, "mob.slime.big", 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F);
	    		}
	    		else
	    		{
	    			worldObj.playSound( posX, posY, posZ, "mob.slime.big", 0.025F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F);
	    		}
	    		
	    	}
	    	
	    	if ( ridingEntity instanceof EntityLiving )
	    	{
	    		EntityLiving ridingCreature = (EntityLiving)ridingEntity;
	    		
	    		m_fSquidYaw = -ridingCreature.rotationYawHead;
	    		m_fPrevSquidYaw = -ridingCreature.prevRotationYawHead;
	    		
	            renderYawOffset = 0;
	            rotationYaw = 0;
	    	}		        	
	    }
	}
	
    private void AttemptTentacleAttackOnTarget()
    {
		double dDeltaX = entityToAttack.posX - posX; 
		double dDeltaY = ( entityToAttack.posY + ( entityToAttack.height / 2F ) ) - ( posY + ( height / 2F ) ); 
		double dDeltaZ = entityToAttack.posZ - posZ;
		
		double dDistSqToTarget = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
		
		if ( dDistSqToTarget < ( m_dTentacleAttackRange * m_dTentacleAttackRange ) )
		{
			double dDistToTarget;
			
			if ( !CanEntityCenterOfMassBeSeen( entityToAttack ) )
			{
				if ( canEntityBeSeen( entityToAttack ) )
				{
					dDeltaY = ( entityToAttack.posY + entityToAttack.getEyeHeight() ) - ( posY + ( height / 2F ) );
					
					dDistSqToTarget = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
				}
				else
				{
					return;
				}				
			}
			
			dDistToTarget = MathHelper.sqrt_double( dDistSqToTarget );
			
			double dUnitVectorToTargetX = dDeltaX / dDistToTarget;
			double dUnitVectorToTargetY = dDeltaY / dDistToTarget;
			double dUnitVectorToTargetZ = dDeltaZ / dDistToTarget;
			
			LaunchTentacleAttackInDirection( dUnitVectorToTargetX, dUnitVectorToTargetY, dUnitVectorToTargetZ );
		}
    }
	
    private void LaunchTentacleAttackInDirection( double dUnitVectorToTargetX, double dUnitVectorToTargetY, double dUnitVectorToTargetZ )
    {
    	m_iTentacleAttackInProgressCounter = 0;
		m_iTentacleAttackCooldownTimer = m_iTentacleAttackTicksToCooldown;
		
		m_dTentacleAttackTargetX = posX + ( dUnitVectorToTargetX * m_dTentacleAttackRange );
		m_dTentacleAttackTargetY = posY + ( height / 2F ) + ( dUnitVectorToTargetY * m_dTentacleAttackRange );
		m_dTentacleAttackTargetZ = posZ + ( dUnitVectorToTargetZ * m_dTentacleAttackRange );
		
		TransmitTentacleAttackToClients();
    }
    
    private void TransmitTentacleAttackToClients()
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( entityId );
	        dataStream.writeByte( (byte)FCBetterThanWolves.fcCustomEntityEventPacketTypeSquidTentacleAttack );
	        
	        dataStream.writeInt( MathHelper.floor_double( m_dTentacleAttackTargetX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( m_dTentacleAttackTargetY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( m_dTentacleAttackTargetZ * 32D ) );	        
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
        Packet250CustomPayload packet = new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelCustomEntityEvent, byteStream.toByteArray() );
        
        FCUtilsWorld.SendPacketToAllPlayersTrackingEntity( (WorldServer)worldObj, this, packet );
    }
    
    public void OnClientNotifiedOfTentacleAttack( double dTargetX, double dTargetY, double dTargetZ )
    {
    	m_iTentacleAttackInProgressCounter = 0;
    	
		m_dTentacleAttackTargetX = dTargetX;
		m_dTentacleAttackTargetY = dTargetY;
		m_dTentacleAttackTargetZ = dTargetZ;
		
        worldObj.playSound( posX, posY, posZ, "random.bow", 1F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
        worldObj.playSound( posX, posY, posZ, "mob.slime.big", 1F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
        
        if ( inWater )
        {
	        for ( int iParticleCount = 0; iParticleCount < 150; iParticleCount++ )
	        {
	            this.worldObj.spawnParticle( "bubble", 
	            	posX + ( rand.nextDouble() * 2F ) - 1F, 
	            	posY + rand.nextDouble(),  
	            	posZ + ( rand.nextDouble() * 2F ) - 1F, 
	            	0D, 0D, 0D);
	        }
	        
	        for ( int iParticleCount = 0; iParticleCount < 10; iParticleCount++ )
	        {
	            this.worldObj.spawnParticle( "splash", 
	            	posX + ( rand.nextDouble() * 2F ) - 1F, 
	            	posY + height,  
	            	posZ + ( rand.nextDouble() * 2F ) - 1F, 
	            	0D, 0D, 0D);
	        }
	        
	        worldObj.playSound( posX, posY, posZ, "liquid.splash", 1F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        }        
    }

    private void UpdateTentacleAttack()
    {
    	if ( m_iTentacleAttackInProgressCounter >= 0 )
    	{
    		m_iTentacleAttackInProgressCounter++;
    		
    		if ( m_iTentacleAttackInProgressCounter >= m_iTentacleAttackDuration )
    		{
    			m_iTentacleAttackInProgressCounter = -1;
    		}
    		else
    		{
    			// tip is only dangerous on forward motion
    			
    			if ( m_iTentacleAttackInProgressCounter <= ( m_iTentacleAttackDuration >> 1 ) )
    			{
    				Vec3 tentacleTip = ComputeTentacleAttackTip( (float)m_iTentacleAttackInProgressCounter );
    				
    				AxisAlignedBB tipBox = AxisAlignedBB.getAABBPool().getAABB( 
    					tentacleTip.xCoord - m_dTentacleAttackTipCollisionHalfWidth, 
    					tentacleTip.yCoord - m_dTentacleAttackTipCollisionHalfWidth, 
    					tentacleTip.zCoord - m_dTentacleAttackTipCollisionHalfWidth, 
    					tentacleTip.xCoord + m_dTentacleAttackTipCollisionHalfWidth, 
    					tentacleTip.yCoord + m_dTentacleAttackTipCollisionHalfWidth, 
    					tentacleTip.zCoord + m_dTentacleAttackTipCollisionHalfWidth );
    				
    		        List potentialCollisionList = worldObj.getEntitiesWithinAABB( EntityLiving.class, tipBox );
    		        
    		        if ( !potentialCollisionList.isEmpty() )
    		        {
	    		        Iterator collisionIterator = potentialCollisionList.iterator();
	    		        
	    		        while ( collisionIterator.hasNext() )
	    		        {
	    		        	EntityLiving tempEntity = (EntityLiving)collisionIterator.next();
	    		        	
	    		        	if ( !(tempEntity instanceof FCEntitySquid ) && tempEntity != ridingEntity )
	    		        	{
	    		        		RetractTentacleAttackOnCollision();
	    		        		
	    		        		if ( !worldObj.isRemote )
	    		        		{
	    		        			TentacleAttackFlingTarget( tempEntity, true );
	    		        		}
	    		        		
	    		        		break;
	    		        	}
	    		        }
    		        }
    			}
    		}
    	}
    }
    
    private void RetractTentacleAttackOnCollision()
    {
    	int iTurningPoint = ( m_iTentacleAttackDuration >> 1 );
    	
		if ( m_iTentacleAttackInProgressCounter < iTurningPoint )
		{
			m_iTentacleAttackInProgressCounter = iTurningPoint + ( iTurningPoint - m_iTentacleAttackInProgressCounter ); 
		}
    }
    
    public Vec3 ComputeTentacleAttackTip( float fAttackProgressTick )
    {
    	double dAttackProgressSin = GetAttackProgressSin( fAttackProgressTick );
    	
    	double dDeltaX = m_dTentacleAttackTargetX - posX;
    	double dDeltaY = m_dTentacleAttackTargetY - ( posY + ( height / 2F ) );
    	double dDeltaZ = m_dTentacleAttackTargetZ - posZ;
    	
    	double dTipOffsetX = dDeltaX * dAttackProgressSin;
    	double dTipOffsetY = dDeltaY * dAttackProgressSin;
    	double dTipOffsetZ = dDeltaZ * dAttackProgressSin;
    	
    	return Vec3.createVectorHelper( posX + dTipOffsetX, posY + ( height / 2F ) + dTipOffsetY, posZ + dTipOffsetZ );
    }
    
    public double GetAttackProgressSin( float fAttackProgressTick )
    {
    	double dAttackProgress = fAttackProgressTick / (float)m_iTentacleAttackDuration;
    	
    	return MathHelper.sin( (float)( dAttackProgress * Math.PI ) );
    }
    
    private void TentacleAttackFlingTarget( Entity targetEntity, boolean bPrimary )
    {
        Entity secondaryTargetEntity = null;
        
        if ( targetEntity.ridingEntity != null )
        {
        	secondaryTargetEntity = targetEntity.ridingEntity;
        	
        	targetEntity.mountEntity( null );
        }
        
		if ( bPrimary )
		{
            int iFXI = MathHelper.floor_double( targetEntity.posX );
            int iFXJ = MathHelper.floor_double( targetEntity.posY ) + 1;
            int iFXK = MathHelper.floor_double( targetEntity.posZ );
            
	        worldObj.playAuxSFX( FCBetterThanWolves.m_iSquidTentacleFlingAuxFXID, iFXI, iFXJ, iFXK, 0 );	        
		}
		
        double dVelocityX = targetEntity.motionX;
        double dVelocityZ = targetEntity.motionZ;
        
		double dDeltaX = targetEntity.posX - posX; 
		double dDeltaZ = targetEntity.posZ - posZ;
		
		double dFlatDistToTargetSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;

		if ( dFlatDistToTargetSq > 0.1D )
		{
			double dFlatDistToTarget = MathHelper.sqrt_double( dFlatDistToTargetSq );
			
			dVelocityX += (float)( -dDeltaX / dFlatDistToTarget ) * 1.0F;
			dVelocityZ += (float)( -dDeltaZ / dFlatDistToTarget ) * 1.0F;
		}
		
		if ( targetEntity instanceof EntityPlayer && ((EntityPlayer)targetEntity).isBlocking() )
		{
			EntityPlayer blockingPlayer = (EntityPlayer)targetEntity;
			
	        ItemStack blockItemStack = blockingPlayer.inventory.mainInventory[blockingPlayer.inventory.currentItem];
	        
	        if ( blockItemStack != null )
	        {
				ItemStack flingStack = new ItemStack( blockItemStack.itemID, blockItemStack.stackSize, blockItemStack.getItemDamage() );
				
				FCUtilsInventory.CopyEnchantments( flingStack, blockItemStack );
				
		        double dItemXPos = targetEntity.posX;
		        double dItemYPos = targetEntity.posY + 1D;
		        double dItemZPos = targetEntity.posZ;
		    	
		        EntityItem entityitem = new EntityItem( worldObj, dItemXPos, dItemYPos, dItemZPos, flingStack );

		        double dVelocityY = targetEntity.motionY + 0.5D;
		        
		        entityitem.motionX = dVelocityX;
		        entityitem.motionY = dVelocityY;
		        entityitem.motionZ = dVelocityZ;
		        
		        entityitem.delayBeforeCanPickup = 10;
		        
		        worldObj.spawnEntityInWorld(entityitem);
		        
		        blockItemStack.stackSize = 0;
	        }			
		}
		else
		{
			targetEntity.isAirBorne = true;

	        double dVelocityY = targetEntity.motionY + 0.75D;
	        
	        dVelocityX *= ( rand.nextDouble() * 0.2D ) + 0.9;
	        dVelocityZ *= ( rand.nextDouble() * 0.2D ) + 0.9;
	        
			targetEntity.motionX = dVelocityX;
			targetEntity.motionY = dVelocityY;
			targetEntity.motionZ = dVelocityZ;
			
			CapFlingMotionOfEntity( targetEntity );
	    	
			targetEntity.setBeenAttacked();
		}
		
		targetEntity.OnFlungBySquidTentacle( this );
		
		if ( secondaryTargetEntity != null )
		{
			TentacleAttackFlingTarget( secondaryTargetEntity, false );
		}		
    }
    
	private void CapFlingMotionOfEntity( Entity targetEntity )
	{
		if ( targetEntity.motionY > 0.75D )
		{
			targetEntity.motionY = 0.75D;
		}
		
		if ( targetEntity.motionX > 1D )
		{
			targetEntity.motionX = 1D;
		}
		else if ( targetEntity.motionX < -1D )
		{
			targetEntity.motionX = -1D;
		}
		
		if ( targetEntity.motionZ > 1D )
		{
			targetEntity.motionZ = 1D;
		}
		else if ( targetEntity.motionZ < -1D )
		{
			targetEntity.motionZ = -1D;
		}
	}
	
    public boolean IsHeadCrab()
    {
    	return ridingEntity != null && ridingEntity instanceof EntityLiving;
    }
    
    private boolean IsBlockSurroundedByWater( int i, int j, int k )
    {    	
        for ( int iTempJ = j - 1; iTempJ <= j + 1; iTempJ++ )
        {
	        for ( int iTempI = i - 1; iTempI <= i + 1; iTempI++ )
	        {
                for ( int iTempK = k - 1; iTempK <= k + 1; iTempK++ )
                {
			        int iTempBlockID = worldObj.getBlockId( iTempI, iTempJ, iTempK );
			        
			        if ( iTempBlockID != Block.waterMoving.blockID && iTempBlockID != Block.waterStill.blockID )
			        {
			        	return false;
			        }
                }
            }
        }
        
    	return true;
    }

    public float GetDepthBeneathSurface( float fMaxDepthToConsider )
    {
    	// returns -1 if not in water, and 0 if in water but not at center pos
    	
    	float fDepth = -1F;
    	
        int iPosI = MathHelper.floor_double( posX );
        int iPosJ = (int)posY;
        int iPosK = MathHelper.floor_double( posZ );
        
        int iTempBlockID = worldObj.getBlockId( iPosI, iPosJ, iPosK );
        int iTempBlockAboveID = worldObj.getBlockId( iPosI, iPosJ + 1, iPosK );
        
        if ( iTempBlockID == Block.waterStill.blockID || iTempBlockID == Block.waterMoving.blockID || 
        	iTempBlockAboveID == Block.waterStill.blockID || iTempBlockAboveID == Block.waterMoving.blockID )
        {
    		fDepth = 0F;
    		
        	fDepth += posY - (float)iPosJ;
        	
        	for ( int iJOffset = 1; fDepth < fMaxDepthToConsider; iJOffset++ )
        	{
        		iTempBlockID = worldObj.getBlockId( iPosI, iPosJ + iJOffset, iPosK );
        		
                if ( iTempBlockID == Block.waterStill.blockID || iTempBlockID == Block.waterMoving.blockID )
                {
                    fDepth += 1F;
                }
                else
                {
                	if ( fDepth == 0F && posY > 32 )
                	{
                		break;
                	}
                	
                	break;
                }                    
        	}
        }
    	
    	return fDepth;
    }
    
    private float InterpolateAngle( float fStartAngle, float fDestAngle, float fMaxIncrement )
    {
        float fDelta = MathHelper.wrapAngleTo180_float(fDestAngle - fStartAngle);

        if ( fDelta > fMaxIncrement )
        {
            fDelta = fMaxIncrement;
        }
        else if ( fDelta < -fMaxIncrement )
        {
            fDelta = -fMaxIncrement;
        }

        return fStartAngle + fDelta;
    }
    
    private void PossessedLeap()
    {
		motionY = 1F;
		
		isAirBorne = true;
		
		m_iPossessedLeapCountdown = m_iPossessedLeapCountdownDuration;
		
		m_fPossessedLeapGhastConversionDiceRoll = rand.nextFloat();
		
		if ( inWater )
		{
			m_iPossessedLeapPropulsionCountdown = m_iPossessedLeapPropulsionDuration;
			
            playSound( "liquid.splash", 1F, rand.nextFloat() * 0.1F + 0.5F );
		}
		else
		{
			m_iPossessedLeapPropulsionCountdown = 0;
			
			playSound( "mob.slime.big", 1F, rand.nextFloat() * 0.1F + 0.5F );
		}
    }

	private AxisAlignedBB GetGhastConversionCollisionBoxFromPool()
	{
		double dWidthOffset = width / 16F; 
			
        return AxisAlignedBB.getAABBPool().getAABB( 
        	boundingBox.minX + dWidthOffset, boundingBox.maxY, boundingBox.minZ + dWidthOffset, 
        	boundingBox.maxX - dWidthOffset, boundingBox.maxY + 0.1F, boundingBox.maxZ - dWidthOffset );
	}    
}
