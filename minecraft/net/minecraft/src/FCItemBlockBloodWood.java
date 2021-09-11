// FCMOD 

package net.minecraft.src;

public class FCItemBlockBloodWood extends ItemBlock
{
    public FCItemBlockBloodWood( int iItemID )
    {
        super( iItemID );
    }
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
    	if ( !player.worldObj.isRemote )
    	{
    		if ( outputStack.itemID == Block.planks.blockID )
    		{
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemSawDust, 1, 0 ) );
    			
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemSoulDust, 1, 0 ) );
    			
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemBark, 1, 4 ) );
    		}
    		else if ( outputStack.itemID == Item.stick.itemID )
    		{
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemSawDust, 3, 0 ) );
    			
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemSoulDust, 1, 0 ) );
    			
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemBark, 1, 4 ) );
    		}
    	}
    }    
    
    @Override
    public int GetCampfireBurnTime( int iItemDamage )
    {
    	// logs can't be burned directly in a campfire without being split first
    	
    	return 0;
    }    
}
