// FCMOD

package net.minecraft.src;

public class FCItemArmorGimp extends FCItemArmorMod
{
	static final int m_iRenderIndex = 1;
	static final int m_iWornWeight = 0;
	
    public FCItemArmorGimp( int iItemID, int iArmorType )
    {
        super( iItemID, EnumArmorMaterial.CLOTH, m_iRenderIndex, iArmorType, m_iWornWeight ); // we're overriding the material
     
        setMaxDamage( getMaxDamage() << 1 ); // 2X durability as normal leather
        
		SetInfernalMaxEnchantmentCost( 10 );
		SetInfernalMaxNumEnchants( 2 );
		
		SetBuoyant();
    }
    
    @Override
	public String GetWornTexturePrefix()
    {
    	return "fcGimp";
    }    
}
