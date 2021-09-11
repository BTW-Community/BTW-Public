// FCMOD

package net.minecraft.src;

public class FCItemArmorPadded extends FCItemArmorMod
{
	static final int m_iRenderIndex = 1;
	private final int m_iEnchantability = 0; // can not be enchanted normally	
	
    public FCItemArmorPadded( int iItemID, int iArmorType )
    {
        super( iItemID, EnumArmorMaterial.CLOTH, m_iRenderIndex, iArmorType, 0 ); // we're overriding the material
     
        setMaxDamage( getMaxDamage() >> 1  ); // 1/2 that of leather        
        
        SetBuoyant();
        SetIncineratedInCrucible();
    }
    
    @Override
    public boolean HasCustomColors()
    {
    	return true;
    }
    
    @Override
    public int GetDefaultColor()
    {
    	return 0x998F7F;
    }
    
    @Override
	public String GetWornTexturePrefix()
    {
    	return "fcPadded";
    }    
    
    @Override
    public int getItemEnchantability()
    {
        return m_iEnchantability;
    }
}
