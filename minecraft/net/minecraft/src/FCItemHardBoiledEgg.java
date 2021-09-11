// FCMOD

package net.minecraft.src;

public class FCItemHardBoiledEgg extends ItemFood
{
	static private final int iHardBoiledEggHealthHealed = 3;
	static private final float iHardBoiledEggSaturationModifier = 0.25F;

    public FCItemHardBoiledEgg( int iItemID )
    {
        super( iItemID, iHardBoiledEggHealthHealed, iHardBoiledEggSaturationModifier, false );
        
        SetNeutralBuoyant();
		SetFilterableProperties( m_iFilterable_Small );
        
        setUnlocalizedName( "fcItemEggPoached" );    
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
