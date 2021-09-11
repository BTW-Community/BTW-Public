// FCMOD

package net.minecraft.src;

public class FCEntityAIPanicOnHeadCrab extends EntityAIBase
{
	private EntityCreature m_owningEntity;
	private float m_fMoveSpeed;
	
    private double m_fRandPosX;
    private double m_fRandPosY;
    private double m_fRandPosZ;
    
    public FCEntityAIPanicOnHeadCrab( EntityCreature entity, float fMoveSpeed )
    {
    	m_owningEntity = entity;
    	
        m_fMoveSpeed = fMoveSpeed;
        
        setMutexBits( 1 );
    }
    
    public boolean shouldExecute()
    {
    	if ( m_owningEntity.HasHeadCrabbedSquid() )
        {
            Vec3 randPos = RandomPositionGenerator.findRandomTarget( m_owningEntity, 5, 4 );

            if ( randPos != null )
            {
            	m_fRandPosX = randPos.xCoord;
            	m_fRandPosY = randPos.yCoord;
            	m_fRandPosZ = randPos.zCoord;
                
                return true;
            }
        }
    	
    	return false;
    }

    public void startExecuting()
    {
        m_owningEntity.getNavigator().tryMoveToXYZ( m_fRandPosX, m_fRandPosY, m_fRandPosZ, m_fMoveSpeed );
    }

    public boolean continueExecuting()
    {
        return !m_owningEntity.getNavigator().noPath() && m_owningEntity.HasHeadCrabbedSquid();
    }
}
