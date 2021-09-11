//FCMOD

package net.minecraft.src;

public class FCItemStone extends Item
{
    public FCItemStone( int iItemID )
    {
        super( iItemID );
        
    	setUnlocalizedName( "fcItemStone" );
    	
    	SetFilterableProperties( Item.m_iFilterable_Small );
    	
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
    	return 8;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockCobblestoneLoose.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
    
