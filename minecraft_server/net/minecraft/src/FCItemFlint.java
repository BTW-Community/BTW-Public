// FCMOD

package net.minecraft.src;

public class FCItemFlint extends Item
{
    public FCItemFlint( int iItemID )
    {
        super( iItemID );
        
        SetFilterableProperties( m_iFilterable_Small );

        setUnlocalizedName( "flint" );
        
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
    	return FCBetterThanWolves.fcAestheticOpaque.blockID;
    }
    
    @Override
    public int GetResultingBlockMetadataOnPistonPack( ItemStack stack )
    {
    	return FCBlockAestheticOpaque.m_iSubtypeFlint;
    }
}
