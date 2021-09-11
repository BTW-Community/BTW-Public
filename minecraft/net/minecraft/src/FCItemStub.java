//FCMOD

package net.minecraft.src;

public class FCItemStub extends Item
{
    public FCItemStub( int iItemID )
    {
        super( iItemID );
        
        setCreativeTab( null );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//

    public void registerIcons( IconRegister register )
    {
        itemIcon = register.registerIcon( "fcItemDung" );
    }
}
    
