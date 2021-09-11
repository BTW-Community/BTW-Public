// FCMOD

package net.minecraft.src;

public class FCItemWolfChopRaw extends FCItemFood
{
	public FCItemWolfChopRaw( int iItemID )
	{
		super( iItemID, 4, 0.25F, false, "fcItemWolfChopRaw", true );
		
		SetStandardFoodPoisoningEffect();
	}
}
