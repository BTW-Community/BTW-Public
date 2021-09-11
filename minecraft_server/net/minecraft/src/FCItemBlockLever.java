// FCMOD

package net.minecraft.src;

public class FCItemBlockLever extends ItemBlock
{
    public FCItemBlockLever( int iItemID )
    {
        super( iItemID );
    }
    
    @Override
	public int GetTargetFacingPlacedByBlockDispenser( int iDispenserFacing )
    {
    	// always place facing upwards
    	
    	return 1;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
