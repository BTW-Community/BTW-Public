// FCMOD

package net.minecraft.src;

public class FCBlockJukebox extends BlockJukeBox
{
    protected FCBlockJukebox( int iBlockID )
    {
    	super( iBlockID );

    	setHardness( 1.5F );
    	setResistance( 10F );
    	SetAxesEffectiveOn();
    	
    	SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOD_BASED_BLOCK );    	
    	
    	setStepSound( soundStoneFootstep );
    	
    	setUnlocalizedName( "jukebox" );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
