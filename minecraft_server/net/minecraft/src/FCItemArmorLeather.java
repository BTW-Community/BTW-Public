// FCMOD

package net.minecraft.src;

public class FCItemArmorLeather extends FCItemArmor
{
	private static final int m_iRenderIndex = 0;
	private static final int m_iWornWeight = 0;
	
    public FCItemArmorLeather( int iItemID, int iArmorType )
    {
        super( iItemID, EnumArmorMaterial.CLOTH, m_iRenderIndex, iArmorType, m_iWornWeight );
     
		SetInfernalMaxEnchantmentCost( 10 );
		SetInfernalMaxNumEnchants( 2 );
        
        SetBuoyant();
        SetIncineratedInCrucible();
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
