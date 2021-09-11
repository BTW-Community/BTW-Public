// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemCreeperOysters extends FCItemFood
{
    public FCItemCreeperOysters( int iItemID )
    {
    	super( iItemID, FCItemFood.m_iCreeperOystersHungerHealed, 
    		FCItemFood.m_fCreeperOystersSaturationModifier, false, 
			FCItemFood.m_sCreeperOystersItemName );
    	
    	SetBellowsBlowDistance( 1 );
		SetFilterableProperties( m_iFilterable_Small );
    	
    	setPotionEffect( Potion.poison.id, 5, 0, 1F ) ;
	}
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
		// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
    	
		if ( player.m_iTimesCraftedThisTick == 0 )
		{
			player.playSound( "mob.slime.attack", 0.5F, ( player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.1F + 0.7F );
		}
    }
    
    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 16;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockCreeperOysters.blockID;
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
