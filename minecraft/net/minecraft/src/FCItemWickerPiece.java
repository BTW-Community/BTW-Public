// FCMOD

package net.minecraft.src;

public class FCItemWickerPiece extends Item
{
    public FCItemWickerPiece( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
        SetBellowsBlowDistance( 2 );
    	SetIncineratedInCrucible();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WICKER_PIECE );
    	SetFilterableProperties( Item.m_iFilterable_Thin );
    	
    	setUnlocalizedName( "fcItemWickerPiece" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );    	
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoCampfire( int iItemDamage )
    {
    	// so that it doesn't accidentally go into a fire after basket weaving
    	
    	return false;
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoBrickOven( int iItemDamage )
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
