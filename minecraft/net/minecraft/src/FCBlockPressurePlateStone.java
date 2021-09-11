// FCMOD

package net.minecraft.src;

public class FCBlockPressurePlateStone extends FCBlockPressurePlate
{
    protected FCBlockPressurePlateStone( int iBlockID )
    {
    	super( iBlockID, "stone", Material.rock, EnumMobType.mobs );
    	
    	setHardness( 1.5F );
    	
    	setStepSound( soundStoneFootstep );
    	
    	setUnlocalizedName( "pressurePlate" );
    }
}
