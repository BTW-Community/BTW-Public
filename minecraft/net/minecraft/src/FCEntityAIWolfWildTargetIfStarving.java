// FCMOD

package net.minecraft.src;

public class FCEntityAIWolfWildTargetIfStarving extends EntityAINearestAttackableTarget
{
    private FCEntityWolf m_AssociatedWolf;
    
    public FCEntityAIWolfWildTargetIfStarving( FCEntityWolf wolf, Class targetClass, float fTargetRange, int iChanceOfTargeting, boolean bCheckLineOfSight )
    {
        super( wolf, targetClass, fTargetRange, iChanceOfTargeting, bCheckLineOfSight );
        
        m_AssociatedWolf = wolf;
    }

    @Override
    public boolean continueExecuting()
    {
    	if ( !m_AssociatedWolf.IsWildAndStarving() )
    	{
    		return false;
    	}
    	
    	return super.continueExecuting();
    }
    
    @Override
    public boolean shouldExecute()
    {
    	if ( !m_AssociatedWolf.IsWildAndStarving() )
    	{
    		return false;
    	}
    	
        return super.shouldExecute();
    }
}