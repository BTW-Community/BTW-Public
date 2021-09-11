// FCMOD

package net.minecraft.src;

public class FCItemSlimeball extends FCItemMortar
{
    public FCItemSlimeball( int iItemID )
    {
    	super( iItemID );
    	
    	SetNeutralBuoyant();
		SetFilterableProperties( m_iFilterable_Small );
    	
    	setUnlocalizedName( "slimeball" );
    	
    	setCreativeTab( CreativeTabs.tabMisc );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}