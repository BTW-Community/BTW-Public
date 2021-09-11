// FCMOD

package net.minecraft.src;

public class FCItemBucketMilkChocolate extends FCItemBucketDrinkable
{
    public FCItemBucketMilkChocolate( int iItemID )
    {
    	super( iItemID, 9, 0.25F );
    	
        setUnlocalizedName( "fcItemBucketChocolateMilk" );
    }
    
    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockBucketMilkChocolate.blockID;
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
    
    @Override
    public boolean DoesConsumeContainerItemWhenCrafted( Item containerItem )
    {
    	if ( containerItem.itemID == Item.bucketEmpty.itemID )
    	{
    		return true;
    	}
    	
    	return false;
    }
}
