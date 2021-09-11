// FCMOD

package net.minecraft.src;

public class FCEntityAIGraze extends EntityAIBase
{
    private EntityAnimal m_myAnimal;
    
    // used by chickens to prevent OCD pecking    
    private int m_iGrazeCooldown = 0; 

    public FCEntityAIGraze( EntityAnimal entity )
    {
        m_myAnimal = entity;
        setMutexBits( 7 );
    }

    @Override
    public boolean shouldExecute()
    {
    	if ( m_iGrazeCooldown > 0 )
    	{
    		m_iGrazeCooldown--;
    		
    		return false;
    	}
    	
    	if ( m_myAnimal.IsSubjectToHunger() )
    	{
    		return m_myAnimal.IsHungryEnoughToGraze() && m_myAnimal.GetGrazeBlockForPos() != null;
    	}
    	else
    	{
            return m_myAnimal.getRNG().nextInt( m_myAnimal.isChild() ? 50 : 1000 ) == 0 &&
            	m_myAnimal.GetGrazeBlockForPos() != null;            
    	}
    }
    
    @Override
    public void startExecuting()
    {
    	m_iGrazeCooldown = 10;
    	
    	m_myAnimal.m_iGrazeProgressCounter = m_myAnimal.GetGrazeDuration();
        m_myAnimal.worldObj.setEntityState( m_myAnimal, (byte)10 );
        m_myAnimal.getNavigator().clearPathEntity();
    }

    @Override
    public void resetTask()
    {
    	m_myAnimal.m_iGrazeProgressCounter = 0;
    }

    @Override
    public boolean continueExecuting()
    {
        return m_myAnimal.m_iGrazeProgressCounter > 0;
    }

    @Override
    public void updateTask()
    {
    	m_myAnimal.m_iGrazeProgressCounter = Math.max( 0, 
    		m_myAnimal.m_iGrazeProgressCounter - 1 );

        if ( m_myAnimal.m_iGrazeProgressCounter == 4 )
        {
            FCUtilsBlockPos targetPos = m_myAnimal.GetGrazeBlockForPos();

            if ( targetPos != null )
            {
                m_myAnimal.OnGrazeBlock( targetPos.i, targetPos.j, targetPos.k );
                
            	FCBlockGroundCover.ClearAnyGroundCoverRestingOnBlock( m_myAnimal.worldObj, 
        			targetPos.i, targetPos.j, targetPos.k );
            	
            	if ( m_myAnimal.ShouldNotifyBlockOnGraze() )
            	{
            		int iTargetBlockID = m_myAnimal.worldObj.getBlockId( 
	            		targetPos.i, targetPos.j, targetPos.k );
            		
            		if ( iTargetBlockID != 0 )
            		{
		            	m_myAnimal.PlayGrazeFX( targetPos.i, targetPos.j, targetPos.k, 
		            		iTargetBlockID );
            			
		            	Block.blocksList[iTargetBlockID].OnGrazed( m_myAnimal.worldObj, 
	            			targetPos.i, targetPos.j, targetPos.k, m_myAnimal );
            		}
            	}
            }            
        }
    }
    
    //------------- Class Specific Methods ------------//	
}
