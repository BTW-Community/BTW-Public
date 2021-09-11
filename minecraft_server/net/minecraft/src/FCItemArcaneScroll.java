// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemArcaneScroll extends Item
{
    public FCItemArcaneScroll( int iItemID )
    {
    	super( iItemID );
    	
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        SetBuoyant();
		SetBellowsBlowDistance( 3 );
		SetFilterableProperties( m_iFilterable_Small | m_iFilterable_Thin );

    	setUnlocalizedName( "fcItemScrollArcane" );
    	
        setCreativeTab( CreativeTabs.tabBrewing );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}