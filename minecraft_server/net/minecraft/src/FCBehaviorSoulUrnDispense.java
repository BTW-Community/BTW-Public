// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCBehaviorSoulUrnDispense extends BehaviorProjectileDispense
{
    public FCBehaviorSoulUrnDispense()
    {
    }

    protected IProjectile getProjectileEntity( World world, IPosition pos )
    {
        return new FCEntityUrn( world, pos.getX(), pos.getY(), pos.getZ(), FCBetterThanWolves.fcItemSoulUrn.itemID );
    }
}
