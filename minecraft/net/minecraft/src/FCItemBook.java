// FCMOD

package net.minecraft.src;

public class FCItemBook extends ItemBook
{
    public FCItemBook( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();
        SetIncineratedInCrucible();
        
        setUnlocalizedName( "book" );
        
        setCreativeTab( CreativeTabs.tabMisc );
    }

    @Override
    public int getItemEnchantability()
    {
    	// override to remove being able to enchant books into ancient manuscripts
    	
        return 0;
    }
}
