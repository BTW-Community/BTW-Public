// FCMOD

package net.minecraft.src;

public class AaaFCItemTemplate extends Item
{
    public AaaFCItemTemplate( int iItemID )
    {
    	super( iItemID );
    	
    	SetNonBuoyant();
		SetBellowsBlowDistance( 0 );		
    	SetNotIncineratedInCrucible();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.NONE );
    	SetFilterableProperties( m_iFilterable_NoProperties );
    	
    	setUnlocalizedName( "fcItemTemplate" );
    	
    	setCreativeTab( CreativeTabs.tabMisc );    	
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
