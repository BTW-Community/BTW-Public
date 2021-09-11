// FCMOD

package net.minecraft.src;

public class FCItemArmorSpecial extends FCItemArmorMod
{
	static final int m_iRenderIndex = 1;
	static final int m_iArmorLevel = 1; // we're overriding this variable's behavior
	
	static final int m_iMaxDamage = 12; // leather helm is 55 

    public FCItemArmorSpecial( int iItemID, int iArmorType )
    {
        super( iItemID, EnumArmorMaterial.IRON, m_iRenderIndex, iArmorType, 0 ); // we're overriding the material
     
        // max damage is uniform for refined armour over all types
        
        setMaxDamage( m_iMaxDamage );  
        
        // we can't use cloth material above because it trigger the colored display of leather armor, so manually assign damage reduction amount
        
        damageReduceAmount = EnumArmorMaterial.CLOTH.getDamageReductionAmount( iArmorType );
    	
    	setCreativeTab( CreativeTabs.tabCombat );
    }
    
    @Override
	public String GetWornTexturePrefix()
    {
    	return "special";
    }    
}
