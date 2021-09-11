// FCMOD

package net.minecraft.src;

public class FCEntityAIAnimalFlee extends EntityAIBase
{
    private EntityAnimal m_theAnimal;
    
    private float m_fSpeed;
    
    private double m_dTargetPosX;
    private double m_dTargetPosY;
    private double m_dTargetPosZ;

    public FCEntityAIAnimalFlee( EntityAnimal animal, float fSpeed )
    {
        this.m_theAnimal = animal;
        this.m_fSpeed = fSpeed;
        
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	Vec3 targetVec = null;
    	
    	if ( m_theAnimal.isBurning() )
    	{
            targetVec = RandomPositionGenerator.findRandomTarget( m_theAnimal, 5, 4 );
    	}
    	else if ( m_theAnimal.getAITarget() != null )
        {
    		targetVec = RandomPositionGenerator.findRandomTargetBlockAwayFrom( m_theAnimal, 5, 4, 
    			m_theAnimal.worldObj.getWorldVec3Pool().getVecFromPool( m_theAnimal.getAITarget().posX, m_theAnimal.getAITarget().posY, m_theAnimal.getAITarget().posZ ) );
        }
    	
    	if ( targetVec != null )
    	{
            this.m_dTargetPosX = targetVec.xCoord;
            this.m_dTargetPosY = targetVec.yCoord;
            this.m_dTargetPosZ = targetVec.zCoord;
            
            return true;
    	}
        
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.m_theAnimal.getNavigator().tryMoveToXYZ(this.m_dTargetPosX, this.m_dTargetPosY, this.m_dTargetPosZ, this.m_fSpeed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	if ( !this.m_theAnimal.getNavigator().noPath() && m_theAnimal.getAITarget()!= null )
    	{
    		EntityLiving aiTarget = m_theAnimal.getAITarget();
    		
    		if ( aiTarget == null )
    		{    			
    			// this is the case if the animal is burning and just running about in panic
    			
    			return true;
    		}
    		else
    		{
    			double dDistanceSqToTarget = m_theAnimal.getDistanceSq( m_dTargetPosX, m_dTargetPosY, m_dTargetPosZ );

    			// choose another target if we're already close to our chosen one
    			
    			if ( dDistanceSqToTarget > 4D )
    			{
		    		// only continue running in this direction if the enemy is closer to the target destination than we are to the enemy
    				// (Are we on the other side of the target destination from the enemy?)
		    		// this prevents animals running in circles when they overshoot their target.
	    			
	    			double dDistanceSqToAttacker = m_theAnimal.getDistanceSqToEntity( aiTarget );
	    			double dDistanceSqAttackerToTarget = aiTarget.getDistanceSq( m_dTargetPosX, m_dTargetPosY, m_dTargetPosZ );
	    			
	    			if ( dDistanceSqToAttacker < dDistanceSqAttackerToTarget )
	    			{
	    				return true;
	    			}
    			}
    		}
    	}
    	
        return false;
    }
}
