// FCMOD

package net.minecraft.src;

public class FCItemArmorDiamond extends FCItemArmor
{
	private static final int m_iRenderIndex = 3;
	
    public FCItemArmorDiamond( int iItemID, int iArmorType, int iWeight )
    {
        super( iItemID, EnumArmorMaterial.DIAMOND, m_iRenderIndex, iArmorType, iWeight );
     
		SetInfernalMaxEnchantmentCost( 30 );
		SetInfernalMaxNumEnchants( 2 );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
