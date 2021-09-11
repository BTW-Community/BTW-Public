// FCMOD

package net.minecraft.src;

public class FCEntityAICreeperSwell extends EntityAICreeperSwell
{
    private FCEntityCreeper m_myCreeper;

    public FCEntityAICreeperSwell( FCEntityCreeper creeper )
    {
    	super( creeper );
    	
    	m_myCreeper = creeper;
    }

    @Override
    public boolean shouldExecute()
    {
    	if ( m_myCreeper.getCreeperState() <= 0 && m_myCreeper.GetNeuteredState() > 0 )
    	{
    		return false;
    	}
    	else if ( m_myCreeper.GetIsDeterminedToExplode() )
    	{
    		return true;
    	}
    	
    	return super.shouldExecute();
    }

    @Override
    public void updateTask()
    {
    	if ( m_myCreeper.GetNeuteredState() > 0 )
    	{
    		m_myCreeper.setCreeperState( -1 );
    	}
    	else if ( !m_myCreeper.GetIsDeterminedToExplode() &&
			( creeperAttackTarget == null || m_myCreeper.getDistanceSqToEntity(this.creeperAttackTarget) > 36D ||
    		!m_myCreeper.getEntitySenses().canSee(creeperAttackTarget) ) )
    	{
    		m_myCreeper.setCreeperState( -1 );
    	}    	
        else
        {
        	m_myCreeper.setCreeperState( 1 );
        }
    }
}
