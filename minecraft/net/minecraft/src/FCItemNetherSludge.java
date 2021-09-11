// FCMOD

package net.minecraft.src;

public class FCItemNetherSludge extends FCItemMortar
{
    public FCItemNetherSludge( int iItemID )
    {
    	super( iItemID );
    	
    	SetNeutralBuoyant();
    	
    	setUnlocalizedName( "fcItemNetherSludge" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
