// FCMOD

package net.minecraft.src;

public class FCItemShaft extends ItemReed
{
    public FCItemShaft( int iItemID )
    {
    	// the shaft supplies its own blockID through method override to avoid initialization order
    	// problems
    	
    	super( iItemID, 0 );
    	
    	setFull3D();
    	
    	SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT );
    	SetIncineratedInCrucible();
    	SetFilterableProperties( m_iFilterable_Narrow );
    	
    	setUnlocalizedName( "stick" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );    	
    }

    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockShaft.blockID;
    }

    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
