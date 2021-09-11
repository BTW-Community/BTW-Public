// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCBehaviorDispenseDynamite extends BehaviorProjectileDispense
{
    public FCBehaviorDispenseDynamite()
    {
    }

    protected IProjectile getProjectileEntity( World world, IPosition pos )
    {
        return new FCEntityDynamite( world, pos.getX(), pos.getY(), pos.getZ(), FCBetterThanWolves.fcItemDynamite.itemID );
    }
}
