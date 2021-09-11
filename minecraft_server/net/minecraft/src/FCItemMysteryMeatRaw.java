// FCMOD

package net.minecraft.src;

public class FCItemMysteryMeatRaw extends FCItemFood
{
	public FCItemMysteryMeatRaw( int iItemID )
	{
		super( iItemID, 4, 0.25F, true, "fcItemMysteryMeatRaw", true );
		
		SetStandardFoodPoisoningEffect();
	}
}
