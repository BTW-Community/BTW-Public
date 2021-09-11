// FCMOD

package net.minecraft.src;

public class FCEntityAISkeletonArrowAttack extends EntityAIBase
{
    private final EntityLiving m_EntityOwner;
    private final IRangedAttackMob m_EntityRangedAttackOwner;

    private EntityLiving m_EntityAttackTarget;

    private int m_AttackCooldownCounter;
    private float m_fEntityMoveSpeed;
    
    private int m_iCanSeeTargetCounter;
    private int m_iMinRangedAttackTime;

    private int m_iAttackInterval;
    
    private double m_dAttackRange;
    private double m_dAttackRangeSq;

	public FCEntityAISkeletonArrowAttack(IRangedAttackMob rangedAttackMob, float fMoveSpeed, int iAttackInterval, float fAttackRange)
	{
        m_iCanSeeTargetCounter = 0;

        m_EntityRangedAttackOwner = rangedAttackMob;
        m_EntityOwner = (EntityLiving)rangedAttackMob;
        m_fEntityMoveSpeed = fMoveSpeed;
        m_iAttackInterval = iAttackInterval;
        m_AttackCooldownCounter = iAttackInterval >> 1;
        m_dAttackRange = fAttackRange;
        m_dAttackRangeSq = fAttackRange * fAttackRange;
        
        setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        EntityLiving target = m_EntityOwner.getAttackTarget();

        if ( target == null )
        {
            return false;
        }
        else
        {
            m_EntityAttackTarget = target;
            
            return true;
        }
    }

    public boolean continueExecuting()
    {
        return this.shouldExecute() || !m_EntityOwner.getNavigator().noPath();
    }

    public void resetTask()
    {
        m_EntityAttackTarget = null;
        m_iCanSeeTargetCounter = 0;
        m_AttackCooldownCounter = m_iAttackInterval;
    }

    public void updateTask()
    {
        double dDistSqToTarget = m_EntityOwner.getDistanceSq( m_EntityAttackTarget.posX, m_EntityAttackTarget.boundingBox.minY, m_EntityAttackTarget.posZ );
        
        boolean bCanSeeTarget = m_EntityOwner.getEntitySenses().canSee( m_EntityAttackTarget );

        if ( bCanSeeTarget )
        {
            ++m_iCanSeeTargetCounter;
        }
        else
        {
            m_iCanSeeTargetCounter = 0;
        }

        if ( dDistSqToTarget <= (double)m_dAttackRangeSq && m_iCanSeeTargetCounter >= 20 )
        {
            m_EntityOwner.getNavigator().clearPathEntity();
        }
        else
        {
            m_EntityOwner.getNavigator().tryMoveToEntityLiving( m_EntityAttackTarget, m_fEntityMoveSpeed );
        }

        m_EntityOwner.getLookHelper().setLookPositionWithEntity( m_EntityAttackTarget, 30.0F, 30.0F );
        
        if ( m_AttackCooldownCounter > 1 )
        {
        	m_AttackCooldownCounter--;
        }
        else
        {
            if ( dDistSqToTarget <= (double)m_dAttackRangeSq && bCanSeeTarget )
            {
            	m_EntityRangedAttackOwner.attackEntityWithRangedAttack( m_EntityAttackTarget, 1F );
            	m_AttackCooldownCounter = m_iAttackInterval;
            }
        }   
    }
}
