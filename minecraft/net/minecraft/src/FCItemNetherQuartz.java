// FCMOD

package net.minecraft.src;

public class FCItemNetherQuartz extends Item
{
    protected FCItemNetherQuartz( int iItemID )
    {
    	super( iItemID );
    }
    
	//----------- Client Side Functionality -----------//
	
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon( "fcItemNetherQuartz" );
    }
}
