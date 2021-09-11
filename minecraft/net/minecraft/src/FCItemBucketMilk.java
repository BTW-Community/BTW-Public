// FCMOD

package net.minecraft.src;

public class FCItemBucketMilk extends FCItemBucketDrinkable
{
    public FCItemBucketMilk( int iItemID )
    {
        super( iItemID, 6, 0.25F );        
        
        setUnlocalizedName( "milk" );
    }
    
    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockBucketMilk.blockID;
    }

    @Override
    public ItemStack onEaten( ItemStack itemStack, World world, EntityPlayer player )
    {
        if ( !world.isRemote )
        {
            player.clearActivePotions();
        }
        
        return super.onEaten( itemStack, world, player );
    }
}
