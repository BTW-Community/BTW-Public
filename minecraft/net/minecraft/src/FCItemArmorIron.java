// FCMOD

package net.minecraft.src;

public class FCItemArmorIron extends FCItemArmor
{
	private static final int m_iRenderIndex = 2;
	
    public FCItemArmorIron( int iItemID, int iArmorType, int iWeight )
    {
        super( iItemID, EnumArmorMaterial.IRON, m_iRenderIndex, iArmorType, iWeight );
     
		SetInfernalMaxEnchantmentCost( 25 );
		SetInfernalMaxNumEnchants( 2 );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
