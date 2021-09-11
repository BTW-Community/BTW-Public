// FCMOD

package net.minecraft.src;

public class FCItemChunkOreGold extends FCItemPlacesAsBlock
{
    public FCItemChunkOreGold( int iItemID )
    {
        super( iItemID, FCBetterThanWolves.fcBlockChunkOreGold.blockID );
        
        SetFilterableProperties( Item.m_iFilterable_Small );
        
        setUnlocalizedName( "fcItemChunkGoldOre" );
        
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
    	return FCBetterThanWolves.fcBlockChunkOreStorageGold.blockID;
    }
}
