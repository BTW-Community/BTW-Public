// FCMOD

package net.minecraft.src;

public class FCItemBlockLog extends ItemMultiTextureTile
{
    public FCItemBlockLog( int iBlockID, Block block, String[] sTypeNames )
    {
        super( iBlockID, block, sTypeNames );
    }
    
    @Override
    public void OnUsedInCrafting( int iItemDamage, EntityPlayer player, ItemStack outputStack )
    {
    	if ( !player.worldObj.isRemote )
    	{
    		if ( outputStack.itemID == Block.planks.blockID )
    		{
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemSawDust, 2, 0 ) );
    			
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemBark, 1, outputStack.getItemDamage() ) );
    		}
    		else if ( outputStack.itemID == Item.stick.itemID )
    		{
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemSawDust, 4, 0 ) );
    			
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( FCBetterThanWolves.fcItemBark, 1, iItemDamage ) );
    		}
    	}
    }
    
    @Override
    public int GetCampfireBurnTime( int iItemDamage )
    {
    	// logs can't be burned directly in a campfire without being split first
    	
    	return 0;
    }    
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
