// FCMOD

package net.minecraft.src;

public class FCItemGlassBottle extends ItemGlassBottle
{
    public FCItemGlassBottle(int par1)
    {
        super(par1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick( ItemStack itemStack, World world, EntityPlayer player )
    {
    	// this one line is the reason for the reflected base class, so bottles can be filled from non-source water blocks
    	MovingObjectPosition movingobjectposition = FCUtilsMisc.GetMovingObjectPositionFromPlayerHitWaterAndLava( world, player, true );    	

        if (movingobjectposition == null)
        {
            return itemStack;
        }

        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;

            if (!world.canMineBlock(player, i, j, k))
            {
                return itemStack;
            }

            if ( !player.canPlayerEdit( i, j, k, movingobjectposition.sideHit, itemStack ) )
            {
                return itemStack;
            }

            if (world.getBlockMaterial(i, j, k) == Material.water)
            {
                itemStack.stackSize--;

                if (itemStack.stackSize <= 0)
                {
                    return new ItemStack(Item.potion);
                }

                if (!player.inventory.addItemStackToInventory(new ItemStack(Item.potion)))
                {
                    player.dropPlayerItem(new ItemStack(Item.potion.itemID, 1, 0));
                }
            }
        }

        return itemStack;
    }
}