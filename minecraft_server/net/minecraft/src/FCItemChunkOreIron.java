// FCMOD

package net.minecraft.src;

public class FCItemChunkOreIron extends FCItemPlacesAsBlock
{
    public FCItemChunkOreIron( int iItemID )
    {
        super( iItemID, FCBetterThanWolves.fcBlockChunkOreIron.blockID );
        
        SetFilterableProperties( Item.m_iFilterable_Small );
        
        setUnlocalizedName( "fcItemChunkIronOre" );
        
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
    	return FCBetterThanWolves.fcBlockChunkOreStorageIron.blockID;
    }
}
