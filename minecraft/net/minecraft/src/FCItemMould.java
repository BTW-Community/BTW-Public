// FCMOD

package net.minecraft.src;

public class FCItemMould extends Item
{
    protected FCItemMould( int iItemID )
    {
    	super( iItemID );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public boolean IsConsumedInCrafting()
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
