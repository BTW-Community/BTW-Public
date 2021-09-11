// FCMOD

package net.minecraft.src;

public class FCItemPileGravel extends Item
{
    public FCItemPileGravel( int iItemID )
    {
        super( iItemID );
        
        SetBellowsBlowDistance( 1 );
		SetFilterableProperties( m_iFilterable_Small );
        
        setUnlocalizedName( "fcItemPileGravel" );
        
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
    	return Block.gravel.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
