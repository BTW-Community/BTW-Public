// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCBehaviorBroadheadArrowDispense extends BehaviorProjectileDispense
{
    public FCBehaviorBroadheadArrowDispense()
    {
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity( World world, IPosition position)
    {
    	FCEntityBroadheadArrow arrow = new FCEntityBroadheadArrow( world, position.getX(), position.getY(), position.getZ() );
        
        arrow.canBePickedUp = 1;
        
        return arrow;        
    }
}
