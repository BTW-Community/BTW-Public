// FCMOD

package net.minecraft.src;

public class FCBlockGlowStone extends BlockGlowStone
{
    public FCBlockGlowStone( int iBlockID )
    {
    	super( iBlockID, Material.glass );
    	
	    setHardness( 0.6F );
	    setResistance( 0.5F ); // preserve vanilla resistance
	    
    	SetPicksEffectiveOn();
    	
    	setLightValue( 1F );
    	
    	setStepSound( soundGlassFootstep );
    	
    	setUnlocalizedName( "lightgem" );
    }
}