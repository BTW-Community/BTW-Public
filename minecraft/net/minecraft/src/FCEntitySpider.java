// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCEntitySpider extends EntitySpider
{
    private static final double m_dSpiderAttackRange = 16.0D;
    private static final int m_iTimeBetweenWebs = ( 20 * 60 * 20 ); // a full day
    
    protected int m_iTimeToNextWeb = 0;

    public FCEntitySpider( World world )
    {
        super( world );
    }
    
    @Override
    public void writeEntityToNBT( NBTTagCompound tag )
    {
        super.writeEntityToNBT( tag );
        
        tag.setInteger( "timeToWeb", m_iTimeToNextWeb );
    }
    
    @Override
    public void readEntityFromNBT( NBTTagCompound tag )
    {
        super.readEntityFromNBT( tag );
        
        if ( tag.hasKey( "timeToWeb" ) )
        {
        	m_iTimeToNextWeb = tag.getInteger( "timeToWeb" );
        }
    }
    
    @Override
    public void initCreature()
    {
        if ( worldObj.rand.nextInt( 100 ) == 0 )
        {
            FCEntitySkeleton jockey = new FCEntitySkeleton( worldObj );            
            jockey.setLocationAndAngles( posX, posY, posZ, rotationYaw, 0F );
            jockey.initCreature();
            
            worldObj.spawnEntityInWorld( jockey );
            
            jockey.mountEntity( this );
        }
    }
    
    @Override
    public void SpawnerInitCreature()
    {
    	// does not call super or initCreature() to override spider jokey generation
    
        m_iTimeToNextWeb = m_iTimeBetweenWebs;    	
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        Entity targetEntity = null;
        
        if ( !DoesLightAffectAggessiveness() || getBrightness( 1F ) < 0.5F )
        {            
        	targetEntity = worldObj.getClosestVulnerablePlayerToEntity( this, m_dSpiderAttackRange );
        }
        
        if ( targetEntity == null )
        {
        	List chickenList = worldObj.getEntitiesWithinAABB( FCEntityChicken.class, 
        		boundingBox.expand( m_dSpiderAttackRange, 4D, m_dSpiderAttackRange ) );
        	
            Iterator chickenIterator = chickenList.iterator();
            
            double dClosestChickenDistSq = m_dSpiderAttackRange * m_dSpiderAttackRange + 1D;

            while ( chickenIterator.hasNext() )
            {
                FCEntityChicken chicken = (FCEntityChicken)chickenIterator.next();
                
                if ( !chicken.isLivingDead )
                {
                	double dDeltaX = posX - chicken.posX;
                	double dDeltaY = posY - chicken.posY;
                	double dDeltaZ = posZ - chicken.posZ;
                	
                	double dDistSq = ( dDeltaX * dDeltaX ) + ( dDeltaY * dDeltaY ) + ( dDeltaZ * dDeltaZ );
                	
                	if ( dDistSq < dClosestChickenDistSq )
                	{
                		targetEntity = chicken;
                		dClosestChickenDistSq = dDistSq;
                	}
                }
            }
        }
        
        return targetEntity;
    }

	@Override
    public void setRevengeTarget( EntityLiving target )
    {
		// override to lengthen revenge time
		
        entityLivingToAttack = target;
        
        if ( entityLivingToAttack != null )
        {
        	revengeTimer = 200; // 10 seconds
        }
        else
        {
        	revengeTimer = 0;
        }
    }
    
    @Override
    protected boolean ShouldContinueAttacking( float fDistanceToTarget )
    {
        if ( revengeTimer <= 0 && DoesLightAffectAggessiveness() && rand.nextInt( 600 ) == 0 && 
        	getBrightness( 1.0F ) > 0.5F && entityToAttack instanceof EntityPlayer && !canEntityBeSeen( entityToAttack )  )
        {
            return false;
        }
        
    	return true;
    }

    @Override
    protected void attackEntity( Entity targetEntity, float fDistanceToTarget )
    {
    	if ( fDistanceToTarget < 2F )
    	{
        	if ( targetEntity instanceof EntityAnimal )
        	{
            	// extend reach on zombies slightly to avoid problems attacking animals due to their elongated bodies
            	
                if ( attackTime <= 0 )
                {
                    attackTime = 20;
                    attackEntityAsMob(targetEntity);
                }
        	}
        	else
        	{
        		EntityMobAttackEntity( targetEntity, fDistanceToTarget );
        	}    	
    	}
    	else if ( fDistanceToTarget < 6F )
        {
            if ( onGround && rand.nextInt( 10 ) == 0 )
            {
            	// jump at the target
            	
                double var4 = targetEntity.posX - this.posX;
                double var6 = targetEntity.posZ - this.posZ;
                float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
                this.motionX = var4 / (double)var8 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                this.motionZ = var6 / (double)var8 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                this.motionY = 0.4000000059604645D;
            }
        }
        else if ( fDistanceToTarget < 10F )
    	{
        	if ( HasWeb() && !IsEntityInWeb( targetEntity ) && rand.nextInt( 10 ) == 0 && !( targetEntity instanceof FCEntitySpider) )
        	{
        		SpitWeb( targetEntity );
        	}
    	}            	
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iLootingModifier )
    {
        if ( HasWeb() )
        {
            EntityLivingDropFewItems( bKilledByPlayer, iLootingModifier );
        }

        if ( DropsSpiderEyes() && rand.nextInt( 16 ) - ( iLootingModifier << 1 ) <= 0 )
        {
            dropItem( Item.spiderEye.itemID, 1 );
        }
    }
    
    @Override
    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
    	return true;
    }
    
    @Override
    public void onLivingUpdate()
    {
    	CheckForLooseFood();
    	CheckForSpiderSkeletonMounting();
    	
    	if ( m_iTimeToNextWeb > 0 )
    	{
    		m_iTimeToNextWeb--;
    	}
    	
    	super.onLivingUpdate();
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 1000 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, Enchantment.baneOfArthropods.effectId );
            
            entityDropItem(itemstack, 0.0F);
    	}
    }
    
	@Override
    public boolean IsAffectedByMovementModifiers()
    {
    	return false;
    }
	
    //------------- Class Specific Methods ------------//
	
    protected boolean DropsSpiderEyes()
    {
    	return true;
    }
    
    public boolean HasWeb()
    {    	
    	return m_iTimeToNextWeb <= 0;
    }
    
    public boolean IsEntityInWeb( Entity targetEntity )
    {
        return worldObj.isMaterialInBB( targetEntity.boundingBox, Material.web );
    }
    
    private void SpitWeb( Entity targetEntity )
    {
    	if ( !worldObj.isRemote )
    	{
            worldObj.spawnEntityInWorld( new FCEntitySpiderWeb( worldObj, this, targetEntity ) );
            
            m_iTimeToNextWeb = m_iTimeBetweenWebs;
    	}
    }
    
	private void CheckForLooseFood()
	{
	    if ( !worldObj.isRemote && !isLivingDead )
	    {
	    	boolean bAte = false;
	    	
	        List itemList = worldObj.getEntitiesWithinAABB( EntityItem.class, boundingBox.expand( 2.5D, 1.0D, 2.5D ) );
	        
	        Iterator itemIterator = itemList.iterator();
	
	        while ( itemIterator.hasNext())
	        {
	    		EntityItem itemEntity = (EntityItem)itemIterator.next();
	    		
		        if ( itemEntity.delayBeforeCanPickup == 0 && itemEntity.isEntityAlive() )
		        {
		        	// client
		        	ItemStack itemStack = itemEntity.getEntityItem();
		        	// server
		        	//ItemStack itemStack = itemEntity.func_92059_d();
		        	
		        	Item item = itemStack.getItem();
		        	
		        	if ( item.itemID == Item.chickenRaw.itemID || item.itemID == FCBetterThanWolves.fcItemRawMysteryMeat.itemID )
		        	{
			            itemEntity.setDead();
			            
			            bAte = true;				            
		        	}
		        }		        
	        }
	        
	        if ( bAte )
	        {
	        	worldObj.playAuxSFX( FCBetterThanWolves.m_iBurpSoundAuxFXID, 
	        		MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ), 0 );
	        }
	    }
	}
	
	public boolean DoesLightAffectAggessiveness()
	{
		return true;
	}
	
	public boolean DoEyesGlow()
	{
		return true;
	}	
    
	protected void CheckForSpiderSkeletonMounting()
	{
	    if ( !worldObj.isRemote && isEntityAlive() && riddenByEntity == null )
	    {
	        List itemList = worldObj.getEntitiesWithinAABB( FCEntitySkeleton.class, GetSpiderJockeyCollisionBoxFromPool() );
	        
	        Iterator itemIterator = itemList.iterator();
	
	        while ( itemIterator.hasNext())
	        {
	        	FCEntitySkeleton tempSkeleton = (FCEntitySkeleton)itemIterator.next();
	        	
	        	if ( tempSkeleton != entityToAttack && tempSkeleton.entityToAttack != this &&  
	        		tempSkeleton.ridingEntity == null )
	        	{
	        		tempSkeleton.mountEntity( this );
	        	}
	        }		        
		}
	}

	private AxisAlignedBB GetSpiderJockeyCollisionBoxFromPool()
	{
		double dWidthOffset = width / 16F; 
			
        return AxisAlignedBB.getAABBPool().getAABB( 
        	boundingBox.minX + dWidthOffset, boundingBox.maxY, boundingBox.minZ + dWidthOffset, 
        	boundingBox.maxX - dWidthOffset, boundingBox.maxY + 0.1F, boundingBox.maxZ - dWidthOffset );
	}
	
	//----------- Client Side Functionality -----------//
}
