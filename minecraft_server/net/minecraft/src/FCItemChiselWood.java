// FCMOD

package net.minecraft.src;

public class FCItemChiselWood extends FCItemChisel
{	
    protected FCItemChiselWood( int iItemID )
    {
        super( iItemID, EnumToolMaterial.WOOD, 2 );
        
        SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT.m_iBurnTime / 2 );    	
		SetFilterableProperties( m_iFilterable_Narrow );
        
        efficiencyOnProperMaterial /= 4;
        
        setUnlocalizedName( "fcItemChiselWood" );        
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
