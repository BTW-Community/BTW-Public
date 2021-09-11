// FCMOD

package net.minecraft.src;

public class FCItemRedstone extends FCItemPlacesAsBlock
{
    public FCItemRedstone( int iItemID )
    {
    	super( iItemID, Block.redstoneWire.blockID );
    	
    	SetBellowsBlowDistance( 3 );
    	SetFilterableProperties( m_iFilterable_Fine );
    	
    	setUnlocalizedName( "redstone" );
    	
    	setCreativeTab( CreativeTabs.tabRedstone );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
