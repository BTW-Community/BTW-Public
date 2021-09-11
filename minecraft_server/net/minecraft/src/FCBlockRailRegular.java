// FCMOD

package net.minecraft.src;

public class FCBlockRailRegular extends BlockRail
{
	public FCBlockRailRegular( int iBlockID )
	{
		super( iBlockID );
		
		setHardness( 0.7F );
		SetPicksEffectiveOn();
		
		setStepSound( soundMetalFootstep );
		
		setUnlocalizedName( "rail" );		
	}
}
