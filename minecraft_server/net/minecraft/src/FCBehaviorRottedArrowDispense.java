// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCBehaviorRottedArrowDispense extends BehaviorProjectileDispense
{
    public FCBehaviorRottedArrowDispense()
    {
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity( World world, IPosition position)
    {
    	FCEntityRottenArrow arrow = new FCEntityRottenArrow( world, position.getX(), position.getY(), position.getZ() );
        
        arrow.canBePickedUp = 2;
        
        return arrow;        
    }
}
