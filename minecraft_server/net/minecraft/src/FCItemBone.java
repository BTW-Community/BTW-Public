// FCMOD

package net.minecraft.src;

public class FCItemBone extends Item
{
    public FCItemBone( int iItemID )
    {
        super( iItemID );
        
        maxStackSize = 16;
        
        SetBuoyant();
        SetIncineratedInCrucible();
        SetFilterableProperties( m_iFilterable_Narrow );

        setFull3D();
        
        setUnlocalizedName( "bone" );
        
        setCreativeTab( CreativeTabs.tabMisc );
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
    	return FCBlockAestheticOpaque.m_iSubtypeBone;
    }
}
