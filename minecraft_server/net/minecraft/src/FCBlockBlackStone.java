// FCMOD

package net.minecraft.src;

public class FCBlockBlackStone extends BlockQuartz
{
    public FCBlockBlackStone( int iBlockID )
    {
        super( iBlockID );
        
        setHardness( 2F );
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "quartzBlock" );
    }
    
	//----------- Client Side Functionality -----------//
}
