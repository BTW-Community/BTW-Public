// FCMOD

package net.minecraft.src;

public class FCEntityAIWolfWildTargetIfStarvingOrHostile extends EntityAINearestAttackableTarget
{
    private FCEntityWolf m_AssociatedWolf;
    
    public FCEntityAIWolfWildTargetIfStarvingOrHostile( FCEntityWolf wolf, Class targetClass, float fTargetRange, int iChanceOfTargeting, boolean bCheckLineOfSight )
    {
        super( wolf, targetClass, fTargetRange, iChanceOfTargeting, bCheckLineOfSight );
        
        m_AssociatedWolf = wolf;
    }

    @Override
    public boolean continueExecuting()
    {
    	if ( !m_AssociatedWolf.IsWildAndHostile() )
    	{
    		return false;
    	}
    	
    	return super.continueExecuting();
    }
    
    @Override
    public boolean shouldExecute()
    {
    	if ( !m_AssociatedWolf.IsWildAndHostile() )
    	{
    		return false;
    	}
    	
        return super.shouldExecute();
    }
}
