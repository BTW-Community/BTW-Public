// FCMOD

package net.minecraft.src;

public class FCItemArmorWool extends FCItemArmorMod
{
	static final int m_iRenderIndex = 1;
	private final int m_iEnchantability = 0; // can not be enchanted normally	
	
    public FCItemArmorWool( int iItemID, int iArmorType )
    {
        super( iItemID, EnumArmorMaterial.CLOTH, m_iRenderIndex, iArmorType, 0 ); // we're overriding the material
     
        setMaxDamage( getMaxDamage() >> 2  ); // 1/4 that of leather
        
        damageReduceAmount = 1;
        
        SetBuoyant();
        
    	SetFurnaceBurnTime( GetNumWoolKnitMadeOf() * 
    		FCEnumFurnaceBurnTime.WOOL_KNIT.m_iBurnTime / 2 );
    }
    
    @Override
    public boolean HasCustomColors()
    {
    	return true;
    }
    
    @Override
    public boolean HasSecondRenderLayerWhenWorn()
    {
    	return true;
    }
    
    @Override
    public int GetDefaultColor()
    {
    	return 0x808080;
    }
    
    @Override
	public String GetWornTexturePrefix()
    {
    	return "fcWool";
    }
    
    @Override
    public int getItemEnchantability()
    {
        return m_iEnchantability;
    }
    
    //------------- Class Specific Methods ------------//
    
    private int GetNumWoolKnitMadeOf()
    {
    	switch ( armorType )
    	{
    		case 0: // helmet
    			
    			return 2;
    			
    		case 1: // chest
    			
    			return 4;
    			
    		case 2: // leggings
    			
    			return 3;
    			
			default: // 3 == boots
				
    			return 2;
    	}
    }
    
	//----------- Client Side Functionality -----------//

    private Icon m_IconOverlay = null;
    
    @Override
    public void registerIcons( IconRegister register )
    {
    	super.registerIcons( register );

    	if ( armorType == 0 )
    	{
    		m_IconOverlay = register.registerIcon( "fcItemWoolHelm_overlay" );
    	}
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return m_IconOverlay != null;
    }
    
    @Override
    public Icon getIconFromDamageForRenderPass( int iDamage, int iRenderPass )
    {
    	if ( iRenderPass == 1 && m_IconOverlay != null )
    	{
    		return m_IconOverlay;
    	}
    	
        return getIconFromDamage( iDamage );
    }
}
