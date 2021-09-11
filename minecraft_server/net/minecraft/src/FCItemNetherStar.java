//FCMOD

package net.minecraft.src;

public class FCItemNetherStar extends ItemSimpleFoiled
{
    public FCItemNetherStar( int iItemID )
    {
        super( iItemID );

		SetFilterableProperties( m_iFilterable_Small );
		
        setUnlocalizedName( "netherStar" );
        
        setCreativeTab( CreativeTabs.tabMaterials );    
    }
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
		// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
    	
		if ( player.m_iTimesCraftedThisTick == 0 )
		{
			player.playSound( "ambient.cave.cave4", 0.5F, player.worldObj.rand.nextFloat() * 0.05F + 0.5F );
		}
    }
}
