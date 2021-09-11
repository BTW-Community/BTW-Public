// FCMOD

package net.minecraft.src;

public class FCItemFoulFood extends FCItemFood
{
	static private final int m_iHealthHealed = 1;
	static private final float m_iSaturationModifier = 0F;

    public FCItemFoulFood( int iItemID )
    {
        super( iItemID, m_iHealthHealed, m_iSaturationModifier, false, "fcItemFoulFood" );
        
        SetIncreasedFoodPoisoningEffect();
    }    
}
