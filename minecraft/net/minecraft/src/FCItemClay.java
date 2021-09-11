// FCMOD

package net.minecraft.src;

public class FCItemClay extends FCItemMortar
{
    public FCItemClay( int iItemID )
    {
    	super( iItemID );
    	
    	SetFilterableProperties( m_iFilterable_Small );
    	
    	setUnlocalizedName( "clay" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 9;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockUnfiredClay.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
