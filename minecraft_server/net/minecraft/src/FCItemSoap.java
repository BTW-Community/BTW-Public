// FCMOD

package net.minecraft.src;

public class FCItemSoap extends Item
{
    public FCItemSoap( int iItemID )
    {
        super(iItemID);
        
        SetBuoyant();
        
        setUnlocalizedName( "fcItemSoap" );        
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
    	return FCBlockAestheticOpaque.m_iSubtypeSoap;
    }
}
