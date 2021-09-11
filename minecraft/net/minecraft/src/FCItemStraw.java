// FCMOD

package net.minecraft.src;

public class FCItemStraw extends Item
{
    public FCItemStraw( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
        SetIncineratedInCrucible();
		SetBellowsBlowDistance( 2 );
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.KINDLING );
    	SetFilterableProperties( m_iFilterable_Narrow );
        
    	SetHerbivoreFoodValue( EntityAnimal.m_iBaseGrazeFoodValue );
    	
    	setUnlocalizedName( "fcItemStraw" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );    	
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
