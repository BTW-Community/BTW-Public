// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemBark extends Item
{
	public static final int m_iSubtypeOak = 0; 
	public static final int m_iSubtypeSpruce = 1; 
	public static final int m_iSubtypeBirch = 2; 
	public static final int m_iSubtypeJungle = 3; 
	public static final int m_iSubtypeBloodWood = 4;
	
	public static final int m_iNumSubtypes = 5;
	
    private String[] m_sNameExtensionsBySubtype = new String[] { "oak", "spruce", "birch", "jungle", "bloodwood" };
    
    public FCItemBark( int iItemID )
    {
        super( iItemID );
        
        setHasSubtypes( true );
        setMaxDamage( 0 );
        
        SetBuoyant();
        SetBellowsBlowDistance( 2 );
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.KINDLING );
		SetFilterableProperties( m_iFilterable_Small | m_iFilterable_Thin );

        setUnlocalizedName( "fcItemBark" );
        
        setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public String getUnlocalizedName( ItemStack stack )
    {
        int iSubtype = MathHelper.clamp_int( stack.getItemDamage(), 0, m_iNumSubtypes - 1);
        
        return super.getUnlocalizedName() + "." + m_sNameExtensionsBySubtype[iSubtype];
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
