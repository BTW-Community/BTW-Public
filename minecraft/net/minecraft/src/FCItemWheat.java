// FCMOD

package net.minecraft.src;

public class FCItemWheat extends Item
{
    public FCItemWheat( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
        SetIncineratedInCrucible();
		SetBellowsBlowDistance( 1 );
    	SetFilterableProperties( m_iFilterable_Narrow );
        
    	SetAsBasicHerbivoreFood();
    	SetAsBasicPigFood();
    	
    	setUnlocalizedName( "fcItemWheat" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );    
	}
    
    //------------- Class Specific Methods ------------//	
    
	//----------- Client Side Functionality -----------//
}
