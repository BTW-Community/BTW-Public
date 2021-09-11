// FCMOD

package net.minecraft.src;

public class FCItemSlatsLegacy extends FCItemPlacesAsBlock
{
    public FCItemSlatsLegacy( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcAestheticNonOpaque.blockID, 
    		FCBlockAestheticNonOpaque.m_iSubtypeSlats, "fcItemSlats" );
    	
    	SetBuoyant();
    	SetIncineratedInCrucible();    	
    }
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
		
    	return ( iFilterableProperties & ( Item.m_iFilterable_Thin | 
    		Item.m_iFilterable_Fine ) ) != 0;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
	@Override
    public Icon GetHopperFilterIcon()
    {
		return FCBetterThanWolves.fcBlockSlats.GetHopperFilterIcon();
    }
}
