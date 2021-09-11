// FCMOD

package net.minecraft.src;

public class FCItemSoulFlux extends Item
{
    public FCItemSoulFlux( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
        SetBellowsBlowDistance( 3 );        
		SetFilterableProperties( m_iFilterable_Fine );
    	
    	setPotionEffect( PotionHelper.glowstoneEffect );
        
    	setUnlocalizedName( "fcItemSoulFlux" );
        
        setCreativeTab( CreativeTabs.tabMaterials );    
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}