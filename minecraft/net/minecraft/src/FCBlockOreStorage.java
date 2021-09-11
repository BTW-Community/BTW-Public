// FCMOD

package net.minecraft.src;

public class FCBlockOreStorage extends FCBlockFullBlock
{
    public FCBlockOreStorage( int iBlockID )
    {
        super( iBlockID, Material.iron );
        
        SetPicksEffectiveOn();
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
	//----------- Client Side Functionality -----------//
}
