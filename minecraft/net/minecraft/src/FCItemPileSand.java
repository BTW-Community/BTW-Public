// FCMOD

package net.minecraft.src;

public class FCItemPileSand extends Item
{
    public FCItemPileSand( int iItemID )
    {
        super( iItemID );
        
        SetBellowsBlowDistance( 2 );
		SetFilterableProperties( m_iFilterable_Fine );
        
        setUnlocalizedName( "fcItemPileSand" );
        
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
    	return Block.sand.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
