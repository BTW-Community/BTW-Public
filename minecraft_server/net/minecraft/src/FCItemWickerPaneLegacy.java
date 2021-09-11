// FCMOD

package net.minecraft.src;

public class FCItemWickerPaneLegacy extends FCItemPlacesAsBlock
{
    public FCItemWickerPaneLegacy( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcAestheticNonOpaque.blockID, 
    		FCBlockAestheticNonOpaque.m_iSubtypeWicker, "fcItemWicker" );
    	
    	SetBuoyant();
    	SetIncineratedInCrucible();    	
    }
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
		
    	return ( iFilterableProperties & Item.m_iFilterable_Fine ) != 0;
    }
    
    @Override
    public boolean CanTransformItemIfFilter( ItemStack filteredItem )
    {
    	return filteredItem.itemID == Block.gravel.blockID;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
