// FCMOD

package net.minecraft.src;

public class FCBlockLever extends BlockLever
{
    protected FCBlockLever( int iBlockID )
    {
        super( iBlockID );
        
        SetPicksEffectiveOn( true );        
    }
    
	@Override
    public int GetFacing( int iMetadata )
    {
    	// facing is opposite side attached to
    	// note that the lever can have iMetadata & 7 of values 5 and 6, 
    	// which are different horizontal orientations of facing upwards, 
    	// and both will return a facing of 1 due to the clamp
    	
    	return MathHelper.clamp_int( 6 - ( iMetadata & 7 ), 1, 5 );
    }
    
	@Override
    public boolean CanRotateAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		return iFacing == Block.GetOppositeFacing( GetFacing( world, i, j, k ) );
    }
	
	@Override
    public boolean OnRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
		
		world.setBlockToAir( i, j, k );
		
    	return false;
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iDirection = blockAccess.getBlockMetadata(i, j, k) & 7;
        float fHalfWidth = 0.1875F;

        if ( iDirection == 1 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 0.2F, 0.5F - fHalfWidth, 
        		fHalfWidth * 2.0F, 0.8F, 0.5F + fHalfWidth );
        }
        else if ( iDirection == 2 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		1.0F - fHalfWidth * 2.0F, 0.2F, 0.5F - fHalfWidth, 
        		1.0F, 0.8F, 0.5F + fHalfWidth );
        }
        else if ( iDirection == 3 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5F - fHalfWidth, 0.2F, 0.0F, 
        		0.5F + fHalfWidth, 0.8F, fHalfWidth * 2.0F );
        }
        else if ( iDirection == 4 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5F - fHalfWidth, 0.2F, 1.0F - fHalfWidth * 2.0F, 
        		0.5F + fHalfWidth, 0.8F, 1.0F );
        }
        
        fHalfWidth = 0.25F;
        
        if ( iDirection == 0 || iDirection == 7 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5F - fHalfWidth, 0.4F, 0.5F - fHalfWidth, 
        		0.5F + fHalfWidth, 1.0F, 0.5F + fHalfWidth );
        }
        
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0.5F - fHalfWidth, 0.0F, 0.5F - fHalfWidth, 
    		0.5F + fHalfWidth, 0.6F, 0.5F + fHalfWidth );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}