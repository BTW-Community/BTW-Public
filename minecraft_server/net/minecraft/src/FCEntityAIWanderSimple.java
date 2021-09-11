// FCMOD: A simplified version of EntityAIWander that uses int destinations to avoid multiple
// typecasts, and which ignores the entities "home" position entirely, since most creatures
// don't even have one.

package net.minecraft.src;

public class FCEntityAIWanderSimple extends EntityAIBase
{
    private EntityCreature m_myEntity;
    
    private float m_fMoveSpeed;
    
    protected FCUtilsBlockPos m_destPos = new FCUtilsBlockPos();

    public FCEntityAIWanderSimple( EntityCreature entity, float fMoveSpeed )
    {
        m_myEntity = entity;
        m_fMoveSpeed = fMoveSpeed;
        
        setMutexBits( 1 );
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if ( m_myEntity.getRNG().nextInt( 120 ) == 0 &&
        	FCUtilsRandomPositionGenerator.FindSimpleRandomTargetBlock( m_myEntity, 
        	10, 7, m_destPos ) )
        {
            return true;
        }
        
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !m_myEntity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	m_myEntity.getNavigator().tryMoveToXYZ( m_destPos.i, m_destPos.j, m_destPos.k, 
    		m_fMoveSpeed );
    }
}
