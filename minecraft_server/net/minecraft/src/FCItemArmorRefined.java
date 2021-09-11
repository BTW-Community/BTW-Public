// FCMOD

package net.minecraft.src;

public class FCItemArmorRefined extends FCItemArmorMod
{
	static final int m_iRenderIndex = 1;
	
	private final int m_iEnchantability = 0; // can not be enchanted normally	
	static final int m_iMaxDamage = 576; // diamond breastplate is 528 

    public FCItemArmorRefined( int iItemID, int iArmorType, int iWeight )
    {
        super( iItemID, EnumArmorMaterial.DIAMOND, m_iRenderIndex, iArmorType, iWeight );
     
        // max damage is uniform for refined armour over all types
        
        setMaxDamage( m_iMaxDamage );        
        
        SetInfernalMaxEnchantmentCost( 30 );
        SetInfernalMaxNumEnchants( 4 );
    }
    
    @Override
    public int getItemEnchantability()
    {
        return m_iEnchantability;
    }
    
    @Override
	public String GetWornTexturePrefix()
    {
    	return "plate";
    }    
}