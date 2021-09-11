// FCMOD

package net.minecraft.src;

public class FCItemPileDirt extends Item
{
    public FCItemPileDirt( int iItemID )
    {
        super( iItemID );
        
        SetBellowsBlowDistance( 1 );
		SetFilterableProperties( m_iFilterable_Fine );
        
        setUnlocalizedName( "fcItemPileDirt" );
        
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
    	return Block.dirt.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
