// FCMOD

package net.minecraft.src;

public class FCItemBlockPumpkinFresh extends ItemBlock
{
    public FCItemBlockPumpkinFresh( int iBlockID )
    {
        super( iBlockID );
        
		setMaxStackSize( 16 );
    }
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
		if ( outputStack.itemID == Block.pumpkin.blockID )
		{
	    	if ( !player.worldObj.isRemote )
	    	{
    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
    				new ItemStack( Item.pumpkinSeeds, 4, 0 ) );
	    	}
	    	
	    	if ( player.m_iTimesCraftedThisTick == 0 )
			{
				// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
		    	
				player.playSound( "mob.slime.attack", 0.5F, ( player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.1F + 0.7F );
			}
		}
    }
}
