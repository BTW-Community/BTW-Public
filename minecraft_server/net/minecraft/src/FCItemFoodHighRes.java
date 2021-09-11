// FCMOD

package net.minecraft.src;

public class FCItemFoodHighRes extends FCItemFood
{
    public FCItemFoodHighRes( int iItemID, int iHungerHealed, float fSaturationModifier, boolean bWolfMeat, String sItemName )
    {
        super( iItemID, iHungerHealed, fSaturationModifier, bWolfMeat, sItemName );        
    }
    
    public FCItemFoodHighRes( int iItemID, int iHungerHealed, float fSaturationModifier, boolean bWolfMeat, String sItemName, boolean bZombiesConsume )
    {
        super( iItemID, iHungerHealed, fSaturationModifier, bWolfMeat, sItemName, bZombiesConsume );
    }
    
    @Override
    public int GetHungerRestored()
    {
    	// override of super function which multiplies the value
    	
    	return getHealAmount();
    }
}
