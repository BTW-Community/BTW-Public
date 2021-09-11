// FCMOD
// seperate AI task from regular howl due to different mutex bits being required to handle howls while a wolf is sitting

package net.minecraft.src;

public class FCEntityAIWolfHowlSitting extends FCEntityAIWolfHowl
{
    public FCEntityAIWolfHowlSitting( FCEntityWolf wolf )
    {
    	super( wolf );
        
        setMutexBits( 2 );
    }

    @Override
    public boolean shouldExecute()
    {
    	if ( m_AssociatedWolf.isSitting() && !m_AssociatedWolf.isChild()  )
    	{
	        int iTimeOfDay = (int)( m_World.worldInfo.getWorldTime() % 24000L );
	        
	        if ( iTimeOfDay > 13500 && iTimeOfDay < 22500 )
	        {
	        	if ( m_AssociatedWolf.m_iHeardHowlCountdown > 0 && m_AssociatedWolf.m_iHeardHowlCountdown <= m_iHeardHowlDuration - 15 )
	        	{
	        		m_iHowlingGroupInitiator = false;
	        		
			    	return m_AssociatedWolf.getRNG().nextInt( m_iChanceOfHowlingWhenOthersHowl ) == 0;
	        	}
	        }
    	}
    	
    	return false;
    }
}