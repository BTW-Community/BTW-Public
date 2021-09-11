// FCMOD

package net.minecraft.src;

public class FCEntityAIZombieSecondaryAttack extends EntityAIAttackOnCollide
{
	private EntityZombie m_zombie;
	
	public FCEntityAIZombieSecondaryAttack( EntityZombie zombie )
	{
		super( zombie, EntityCreature.class, zombie.moveSpeed, true );
		
		m_zombie = zombie;
	}
	
    public boolean continueExecuting()
    {
        EntityLiving var1 = this.attacker.getAttackTarget();
        
        if ( var1 == null || !var1.IsValidZombieSecondaryTarget( m_zombie ) )
        {
        	return false;
        }
        
        return super.continueExecuting();
    }
}
