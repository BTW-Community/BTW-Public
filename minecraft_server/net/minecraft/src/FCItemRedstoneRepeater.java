// FCMOD

package net.minecraft.src;

public class FCItemRedstoneRepeater extends FCItemPlacesAsBlock
{
    public FCItemRedstoneRepeater( int iItemID )
    {
    	super( iItemID, Block.redstoneRepeaterIdle.blockID );    	
    	
    	setUnlocalizedName( "diode" );
    	
    	setCreativeTab( CreativeTabs.tabRedstone );    	
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
