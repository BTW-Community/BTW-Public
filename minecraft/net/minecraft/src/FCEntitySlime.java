// FCMOD

package net.minecraft.src;

public class FCEntitySlime extends EntitySlime
{
    private int m_iJumpCountdown = 0; // duplicate of parent slimeJumpDelay due to it being private
    
    public FCEntitySlime( World world )
    {
        super( world );
        
        m_iJumpCountdown = getJumpDelay();
    }
    
    @Override
    protected void setSlimeSize( int iSize )
    {
    	// override to change how bounding box size is set, which setPostion() also relies on, 
    	// preventing a straight call to super method
    	
        dataWatcher.updateObject( 16, new Byte( (byte)iSize ) );
        
        if ( iSize == 1 )
        {
        	setSize( 0.6F, 0.4F ); // smaller horizontal size tends to result in small slimes slipping through diagonal cracks
        }
        else
        {
            setSize( 0.4F * (float)iSize, 0.4F * (float)iSize );
        }
        
        setPosition( posX, posY, posZ );
        setEntityHealth( getMaxHealth() );
        experienceValue = iSize;
    }
    
    @Override
    protected boolean makesSoundOnLand()
    {
    	// messy override of parent function to avoid having to copy entire 
    	// onUpdate() just for this change
    	
        if ( super.makesSoundOnLand() && !inWater )
        {
            playSound( getJumpSound(), getSoundVolume(), getSoundPitch() * 
            	( ( rand.nextFloat() - rand.nextFloat() ) * 0.2F + 1F ) );
        }
        
        return false; // always return false so onUpdate() doesn't play sound
    }
    
    @Override
    protected void updateEntityActionState()
    {
        entityAge++;        
        despawnEntity();
        
        EntityPlayer targetPlayer = worldObj.getClosestVulnerablePlayerToEntity( this, 16D );

        if ( targetPlayer != null )
        {
            faceEntity( targetPlayer, 10F, 20F );
        }

        isJumping = false;
        
        if ( onGround )
        {
        	m_iJumpCountdown--;
        	
        	if ( m_iJumpCountdown <= 0 )
        	{
	            PlayJumpSound();
	        	
	            isJumping = true;	        	
	            m_iJumpCountdown = getJumpDelay();	            
	            moveForward = getSlimeSize();
	            
	            if ( targetPlayer != null )
	            {
		            moveStrafing = 1F - ( rand.nextFloat() * 2F );
		            
	            	// jump more frequently when attacking, resulting in faster movement
	            	
	                m_iJumpCountdown /= 6;
	            }
	            else
	            {
		            moveStrafing = 0F;
		            
	            	// if the slime isn't attacking, then give it some random direction changes 
		            // and keep it oriented to the world grid as befits its cubic form
	            	
		            if ( rand.nextInt( 4 ) == 0 )
		            {
			            rotationYaw = MathHelper.wrapAngleTo180_float( 
			            	(float)rand.nextInt( 4 ) * 90F );
		            }
	            }
        	}
        	else
        	{
        		// don't move on ground
        		
                moveStrafing = moveForward = 0F;
        	}
        }
    }
    
    @Override
    protected double MinDistFromPlayerForDespawn()
    {
    	return 64D;
    }
    
    @Override
    public void onCollideWithPlayer( EntityPlayer player )
    {
        if ( canDamagePlayer() && canEntityBeSeen( player ) )
        {
            if ( player.attackEntityFrom( DamageSource.causeMobDamage( this ), 
            	getAttackStrength() ) )
            {
            	attackTime = 20;
            	
                playSound( "mob.slime.attack", 1F, getSoundPitch() * 
                	( ( rand.nextFloat() - rand.nextFloat() ) * 0.2F + 1F ) );
            }
        }
    }

    @Override
    protected boolean canDamagePlayer()
    {
    	// Changed from parent so slimes of all sizes do damage
    	
    	return isEntityAlive() && attackTime <= 0;
    }

    @Override
    public boolean getCanSpawnHere()
    {
    	if ( super.getCanSpawnHere() )
    	{
    		if ( posY < 40D )
    		{
    			return CanSpawnOnBlockInSlimeChunk( MathHelper.floor_double( posX ),
    				MathHelper.floor_double( boundingBox.minY ) - 1, 
    				MathHelper.floor_double( posZ ) );                
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    protected float getSoundVolume()
    {
        return 0.1F * (float)getSlimeSize();
    }
    

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    public void CheckForScrollDrop()
    {    	
        if ( getSlimeSize() == 1 && rand.nextInt( 1000 ) == 0 )
        {
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, 
            	Enchantment.protection.effectId );
            
            entityDropItem( itemstack, 0F );
        }
    }
    
	@Override
    public boolean IsAffectedByMovementModifiers()
    {
    	return false;
    }
	
    @Override
    protected void jump()
    {
    	// jump a bit higher than normal creatures to better clear terrain
    	
    	motionY = 0.41999998688697815D * 1.25D;

        isAirBorne = true;
    }
    
    @Override
    public boolean CanSwim()
    {
    	return false;
    }
    
	@Override
	public float GetDefaultSlipperinessOnGround()
    {
		// always slide, which looks cool and helps it get out of tight spaces
		
    	return 0.9F  * 0.91F;
    }
    
	@Override
	public float GetSlipperinessRelativeToBlock( int iBlockID )
	{
		return GetDefaultSlipperinessOnGround();
	}
	
	@Override
    protected void fall( float fFallDistance )
	{
		// ignore fall damage
	}
	
	@Override
	protected float getSoundPitch()
	{
		int iSize = getSlimeSize();
		
		if ( iSize == 4 )
		{
			return 0.75F;
		}
		else if ( iSize == 2 )
		{
			return 1F;
		}
		else
		{
			return 1.25F;
		}
	}
	
	/** 
	 * used to split off smaller slimes when a larger one dies
	 */
	@Override
    protected EntitySlime createInstance()
    {
        return new FCEntitySlime( worldObj );
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean CanSpawnOnBlockInSlimeChunk( int i, int j, int k )
    {
    	// FCTEST: this needs to be based on material, not specific block type.  Release as is.
        int iBlockID = worldObj.getBlockId( i, j, k );

        return iBlockID == Block.dirt.blockID || 
			iBlockID == Block.stone.blockID || 
			iBlockID == Block.grass.blockID || 
			iBlockID == Block.gravel.blockID || 
			iBlockID == Block.sand.blockID;
    }
    
    private void PlayJumpSound()
    {
        if ( makesSoundOnJump() )
        {
        	if ( !inWater )
        	{
                playSound( getJumpSound(), getSoundVolume(), getSoundPitch() * 
                	( ( rand.nextFloat() - rand.nextFloat() ) * 0.2F + 1F ) );
        	}
        	else
        	{
                playSound( "liquid.swim", 0.25F, getSoundPitch() * 
                	( 1F + ( rand.nextFloat() - rand.nextFloat()) * 0.4F ) );
        	}
        }
    }    
}