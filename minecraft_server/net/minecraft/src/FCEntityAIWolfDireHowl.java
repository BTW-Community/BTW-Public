// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCEntityAIWolfDireHowl extends EntityAIBase
{
    protected FCEntityWolfDire m_AssociatedWolf;
    protected World m_World;
    
    protected int m_iHowlCounter;
    
    protected static final int m_iChanceOfHowling = 2400;  // chance is 1 over value per tick
    
    protected static final int m_iHowlDuration = 80;
    
    public FCEntityAIWolfDireHowl( FCEntityWolfDire wolf )
    {
        this.m_AssociatedWolf = wolf;
        this.m_World = wolf.worldObj;
        
        this.setMutexBits( 7 );
    }

    @Override
    public boolean shouldExecute()
    {
        int iTimeOfDay = (int)( m_World.worldInfo.getWorldTime() % 24000L );
        
        if ( iTimeOfDay > 13500 && iTimeOfDay < 22500 )
        {
	    	return m_AssociatedWolf.getRNG().nextInt( m_iChanceOfHowling ) == 0;
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
        
    	NotifyOtherWolvesInAreaOfHowl();
        	
        m_AssociatedWolf.m_iHeardHowlCountdown = 0;

        int iTargetI = MathHelper.floor_double( m_AssociatedWolf.posX );
        int iTargetJ = MathHelper.floor_double( m_AssociatedWolf.posY ) + 1;
        int iTargetK = MathHelper.floor_double( m_AssociatedWolf.posZ );
        
        m_World.func_82739_e( FCBetterThanWolves.m_iWolfHowlAuxFXID, iTargetI, iTargetJ, iTargetK, 1 ); // this function broadcasts FX at a much larger range than playAuxFX
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
