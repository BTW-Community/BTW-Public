// FCMOD

package net.minecraft.src;

public class FCItemKnittingNeedles extends Item
{
    public FCItemKnittingNeedles( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT );
    	SetFilterableProperties( Item.m_iFilterable_Narrow );
    	
    	setUnlocalizedName( "fcItemKnittingNeedles" );
    	
    	setCreativeTab( CreativeTabs.tabTools );
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoCampfire( int iItemDamage )
    {
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
