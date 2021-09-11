// FCMOD

package net.minecraft.src;

public class FCBlockIronBars extends FCBlockPane
{
    protected FCBlockIronBars( int iBlockID )
    {
        super( iBlockID, "fenceIron", "fenceIron", Material.iron, true );
        
        setHardness( 5F );
        setResistance( 10F );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "fenceIron" );
    }
    
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
		
    	return filteredItem.getMaxStackSize() > 1 && 
    		( iFilterableProperties & Item.m_iFilterable_SolidBlock ) == 0;
    }
    
    //------------- Class Specific Methods ------------//	
    
	//----------- Client Side Functionality -----------//
}