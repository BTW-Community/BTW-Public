// FCMOD

package net.minecraft.src;

public class FCBlockLegacyLadder extends FCBlockLadderBase
{
    protected FCBlockLegacyLadder( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "ladder" );
        
        setCreativeTab( null );
    }
    
    @Override
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
    	return true;
	}
    
    @Override
    public int GetFacing( int iMetadata )
    {
    	return iMetadata;
    }
    
    @Override
    public int SetFacing( int iMetadata, int iFacing )
    {
    	return MathHelper.clamp_int( iFacing, 2, 5 );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
