// FCMOD

package net.minecraft.src;

public class FCItemGrateLegacy extends FCItemPlacesAsBlock
{
    public FCItemGrateLegacy( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcAestheticNonOpaque.blockID, 
    		FCBlockAestheticNonOpaque.m_iSubtypeGrate, "fcItemGrate" );
    	
    	SetBuoyant();
    	SetIncineratedInCrucible();    	
    }
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
    		
    	return ( iFilterableProperties & ( Item.m_iFilterable_Small | 
    		Item.m_iFilterable_Fine ) ) != 0;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
