// FCMOD

package net.minecraft.src;

public class FCItemBlockSoulforgeDormant extends ItemBlock
{
    public FCItemBlockSoulforgeDormant( int iItemID )
    {
    	super( iItemID );
    	
        setUnlocalizedName( "fcBlockSoulforgeDormant" );
    }
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
		// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
    	
		if ( player.m_iTimesCraftedThisTick == 0 )
		{
			player.playSound( "random.anvil_land", 0.3F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F );
		}
    }
}
