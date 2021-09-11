// FCMOD

package net.minecraft.src;

public class FCBlockObsidian extends BlockObsidian
{
    public FCBlockObsidian( int iBlockID )
    {
        super( iBlockID );
        
        setHardness( 50F );
        setResistance( 2000F );
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "obsidian" );
    }

	@Override
    public int getMobilityFlag()
    {
        return 2; // cannot be pushed
    }
}
