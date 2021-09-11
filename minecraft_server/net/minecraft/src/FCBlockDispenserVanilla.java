// FCMOD

package net.minecraft.src;

public class FCBlockDispenserVanilla extends BlockDispenser
{
    protected FCBlockDispenserVanilla( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 3.5F );
    	
    	setStepSound( soundStoneFootstep );
    	
    	setUnlocalizedName( "dispenser" );
    }

    @Override
	public int GetFacing( int iMetadata )
	{
		return iMetadata & 7;
	}
	
    @Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		return ( iMetadata & (~7) ) | iFacing;
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}

