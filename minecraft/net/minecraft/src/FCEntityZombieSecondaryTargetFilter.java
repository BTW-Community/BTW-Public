// FCMOD

package net.minecraft.src;

public class FCEntityZombieSecondaryTargetFilter implements IEntitySelector
{
	EntityZombie m_zombie;
	
	public FCEntityZombieSecondaryTargetFilter( EntityZombie zombie )
	{
		m_zombie = zombie;
	}
	
    public boolean isEntityApplicable( Entity entity )
    {
        return entity.IsValidZombieSecondaryTarget( m_zombie );
    }
}
