// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCEntityAIWolfHowl extends EntityAIBase
{
    protected FCEntityWolf m_AssociatedWolf;
    protected World m_World;
    
    protected int m_iHowlCounter;
    protected boolean m_iHowlingGroupInitiator;
    
    protected static final int m_iChanceOfHowling = 4800;  // chance is 1 over value per tick
    protected static final int m_iChanceOfHowlingDuringFullMoon = 240;
    protected static final int m_iChanceOfHowlingWhenOthersHowl = 240;
    
    protected static final int m_iHowlDuration = 80;
    protected static final int m_iHeardHowlDuration = 95;
    protected static final double m_dHearHowlDistance = 320D;
    protected static final double m_dHearHowlDistanceSQ = ( m_dHearHowlDistance * m_dHearHowlDistance );
    
    public FCEntityAIWolfHowl( FCEntityWolf wolf )
    {
        this.m_AssociatedWolf = wolf;
        this.m_World = wolf.worldObj;
        
        this.setMutexBits( 7 );
    }

    @Override
    public boolean shouldExecute()
    {
    	if ( !m_AssociatedWolf.isChild() )
    	{
	        int iTimeOfDay = (int)( m_World.worldInfo.getWorldTime() % 24000L );
	        
	        if ( iTimeOfDay > 13500 && iTimeOfDay < 22500 )
	        {
	        	int iMoonPhase = m_World.getMoonPhase();
	        	
	        	if ( iMoonPhase == 0 && m_World.worldInfo.getWorldTime() > 24000L ) // full moon, and not the first one of the game
	        	{
	            	if ( !m_AssociatedWolf.isTamed() )
	            	{
	            		m_iHowlingGroupInitiator = false;
	            		
	    		    	return m_AssociatedWolf.getRNG().nextInt( m_iChanceOfHowlingDuringFullMoon ) == 0;
	            	}
	        	}
	        	else if ( m_AssociatedWolf.m_iHeardHowlCountdown > 0 && m_AssociatedWolf.m_iHeardHowlCountdown <= m_iHeardHowlDuration - 15 )
	        	{
	        		m_iHowlingGroupInitiator = false;
	        		
			    	return m_AssociatedWolf.getRNG().nextInt( m_iChanceOfHowlingWhenOthersHowl ) == 0;
	        	}
	        	else
	        	{
	            	if ( !m_AssociatedWolf.isTamed() )
	            	{
	            		m_iHowlingGroupInitiator = true;
	            		
	    		    	return m_AssociatedWolf.getRNG().nextInt( m_iChanceOfHowling ) == 0;
	            	}
	        	}
	        }
    	}
    	
    	return false;
    }

    @Override
    public boolean continueExecuting()
    {
    	return m_iHowlCounter < m_iHowlDuration;
    }

    @Override
    public void startExecuting()
    {
    	m_iHowlCounter = 0;
    	
        m_World.setEntityState( m_AssociatedWolf, (byte)10 );
        
        m_AssociatedWolf.getNavigator().clearPathEntity();
        
        if ( m_iHowlingGroupInitiator )
        {
        	NotifyOtherWolvesInAreaOfHowl();
        	
        	m_iHowlingGroupInitiator = false;
        }
        
        m_AssociatedWolf.m_iHeardHowlCountdown = 0;

        int iTargetI = MathHelper.floor_double( m_AssociatedWolf.posX );
        int iTargetJ = MathHelper.floor_double( m_AssociatedWolf.posY ) + 1;
        int iTargetK = MathHelper.floor_double( m_AssociatedWolf.posZ );
        
        m_World.func_82739_e( FCBetterThanWolves.m_iWolfHowlAuxFXID, iTargetI, iTargetJ, iTargetK, 0 ); // this function broadcasts FX at a much larger range than playAuxFX
    }

    @Override
    public void resetTask()
    {
    	m_iHowlCounter = 0;
    }

    @Override
    public void updateTask()
    {
    	m_iHowlCounter++;    	
    }
    
    //------------- Class Specific Methods ------------//
    
    private void NotifyOtherWolvesInAreaOfHowl()
    {
        for (int l = 0; l < m_World.loadedEntityList.size(); l++)
        {
            Entity tempEntity = (Entity)m_World.loadedEntityList.get(l);
            
            tempEntity.NotifyOfWolfHowl( m_AssociatedWolf );
        }        
    }
}
