// FCMOD

package net.minecraft.src;

public class FCItemArmorGold extends FCItemArmor
{
	private static final int m_iRenderIndex = 4;
	
    public FCItemArmorGold( int iItemID, int iArmorType, int iWeight )
    {
        super( iItemID, EnumArmorMaterial.GOLD, m_iRenderIndex, iArmorType, iWeight );
     
		SetInfernalMaxEnchantmentCost( 30 );
		SetInfernalMaxNumEnchants( 3 );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
