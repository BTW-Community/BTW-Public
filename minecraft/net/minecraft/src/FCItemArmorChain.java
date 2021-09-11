// FCMOD

package net.minecraft.src;

public class FCItemArmorChain extends FCItemArmorMod
{
	static final int m_iRenderIndex = 1;
	
    public FCItemArmorChain( int iItemID, int iArmorType, int iWeight )
    {
        super( iItemID, EnumArmorMaterial.CHAIN, m_iRenderIndex, iArmorType, iWeight );
        
        SetInfernalMaxEnchantmentCost( 30 );
        SetInfernalMaxNumEnchants( 2 );
        
        this.setCreativeTab(CreativeTabs.tabCombat);
    }
    
    @Override
	public String GetWornTexturePrefix()
    {
    	return "chain";
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}