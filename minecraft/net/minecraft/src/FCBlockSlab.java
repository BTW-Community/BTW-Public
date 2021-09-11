// FCMOD

package net.minecraft.src;

import java.util.List;

public abstract class FCBlockSlab extends Block
{
    public FCBlockSlab( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 0.5F, 1F );
        
        setLightOpacity( 255 );
        
        Block.useNeighborBrightness[iBlockID] = true;
    }

	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        if ( iFacing == 0 || ( iFacing != 1 && fClickY > 0.5F ) )
        {
    		if ( CanBePlacedUpsideDownAtLocation( world, i, j, k ) )
    		{
    			iMetadata = SetIsUpsideDown( iMetadata, true );
    		}
        }
        
        return iMetadata;
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	boolean bIsUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
    	
    	if ( bIsUpsideDown )
    	{
    		return iFacing == 1;
    	}
    	else
    	{
    		return iFacing == 0;
    	}
	}
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
        if ( !GetIsUpsideDown( blockAccess, i, j, k ) )
        {
        	return -0.5F;
        }
        
    	return 0F;
    }
    
    @Override
    public boolean IsSnowCoveringTopSurface( IBlockAccess blockAccess, int i, int j, int k )
    {
        if ( !GetIsUpsideDown( blockAccess, i, j, k ) )
        {
        	// bottom half slabs are only covered by offset snow ground cover
        	
			return blockAccess.getBlockId( i, j + 1, k ) == Block.snow.blockID;
        }
        
    	return super.IsSnowCoveringTopSurface( blockAccess, i, j, k );
    }
    
    @Override
	public boolean HasContactPointToFullFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
    	if ( iFacing < 2 )
    	{
        	boolean bIsUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
        	
        	return bIsUpsideDown == ( iFacing == 1 );
    	}    	
    		
		return true;
	}
	
    @Override
	public boolean HasContactPointToSlabSideFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown )
	{
		return bIsSlabUpsideDown == GetIsUpsideDown( blockAccess, i, j, k );
	}
	
    @Override
    public boolean HasNeighborWithMortarInContact( World world, int i, int j, int k )
    {
    	boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
    	
    	return HasNeighborWithMortarInContact( world, i, j, k, bIsUpsideDown );
    }
    
    @Override
    public boolean HasStickySnowNeighborInContact( World world, int i, int j, int k )
    {
    	boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
    	
    	return HasStickySnowNeighborInContact( world, i, j, k, bIsUpsideDown );
    }
    
    @Override
    protected ItemStack createStackedBlock( int iMetadata )
    {
    	iMetadata = SetIsUpsideDown( iMetadata, false );
    	
    	return super.createStackedBlock( iMetadata );
    }
    
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
        return blockMaterial.GetMobsCanSpawnOn( world.provider.dimensionId );
    }

    @Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
        if ( !GetIsUpsideDown( world, i, j, k ) )
        {
        	return -0.5F;
        }
        
    	return 0F;
    }
    
    //------------- Class Specific Methods ------------//
    
    protected boolean HasNeighborWithMortarInContact( World world, int i, int j, int k, boolean bIsUpsideDown )
    {
    	if ( bIsUpsideDown )
    	{
    		if ( FCUtilsWorld.HasNeighborWithMortarInFullFaceContactToFacing( world, i, j, k, 1 ) )
			{
				return true;
			}
    	}
    	else
    	{
    		if ( FCUtilsWorld.HasNeighborWithMortarInFullFaceContactToFacing( world, i, j, k, 0 ) )
			{
				return true;
			}
    	}
    	
    	for ( int iTempFacing = 2; iTempFacing < 6; iTempFacing++ )
    	{
    		if ( FCUtilsWorld.HasNeighborWithMortarInSlabSideContactToFacing( world, i, j, k, iTempFacing, bIsUpsideDown ) )
			{
				return true;
			}
    	}
    	
    	return false;
    }
    
    protected boolean HasStickySnowNeighborInContact( World world, int i, int j, int k, boolean bIsUpsideDown )
    {
    	if ( bIsUpsideDown )
    	{
    		if ( FCUtilsWorld.HasStickySnowNeighborInFullFaceContactToFacing( world, i, j, k, 1 ) )
			{
				return true;
			}
    	}
    	else
    	{
    		if ( FCUtilsWorld.HasStickySnowNeighborInFullFaceContactToFacing( world, i, j, k, 0 ) )
			{
				return true;
			}
    	}
    	
    	for ( int iTempFacing = 2; iTempFacing < 6; iTempFacing++ )
    	{
    		if ( FCUtilsWorld.HasStickySnowNeighborInSlabSideContactToFacing( world, i, j, k, iTempFacing, bIsUpsideDown ) )
			{
				return true;
			}
    	}
    	
    	return false;
    }
    
    public boolean CanBePlacedUpsideDownAtLocation( World world, int i, int j, int k )
    {
    	return true;
    }
    
    public boolean GetIsUpsideDown( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsUpsideDown( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public boolean GetIsUpsideDown( int iMetadata )
    {
    	return ( iMetadata & 1 ) > 0;
    }
    
    public void SetIsUpsideDown( World world, int i, int j, int k, boolean bUpsideDown )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	world.setBlockMetadataWithNotify( i, j, k, SetIsUpsideDown( iMetadata, bUpsideDown ) );
    }
    
    public int SetIsUpsideDown( int iMetadata, boolean bUpsideDown )
	{
    	if ( bUpsideDown )
    	{
    		iMetadata |= 1;
    	}
    	else
    	{
    		iMetadata &= (~1);    		
    	}
    	
    	return iMetadata;    	
	}
        
    public boolean ConvertToFullBlock( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	return world.setBlockAndMetadataWithNotify( i, j, k, GetCombinedBlockID( iMetadata ), GetCombinedMetadata( iMetadata ) );
    }
    
	public abstract int GetCombinedBlockID( int iMetadata );
	
	public int GetCombinedMetadata( int iMetadata )
	{
		return 0;
	}
	
    protected AxisAlignedBB GetBlockBoundsFromPoolFromMetadata( int iMetadata )
    {
        if ( GetIsUpsideDown( iMetadata ) )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(             
        		0F, 0.5F, 0F, 1F, 1F, 1F );
        }
        else
        {
        	return AxisAlignedBB.getAABBPool().getAABB(             
        		0F, 0F, 0F, 1F, 0.5F, 1F );
        }
    }

	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	FCUtilsBlockPos myPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, 
    		GetOppositeFacing( iSide ) );
    	
    	boolean bUpsideDown = GetIsUpsideDown( blockAccess, myPos.i, myPos.j, myPos.k );
    	
    	if ( iSide < 2 )
    	{
	    	if ( iSide == 0 )
	    	{
	    		return bUpsideDown || !blockAccess.isBlockOpaqueCube( iNeighborI, iNeighborJ, iNeighborK );
	    	}
	    	else // iSide == 1
	    	{
	    		return !bUpsideDown || !blockAccess.isBlockOpaqueCube( iNeighborI, iNeighborJ, iNeighborK );
	    	}
    	}

        return FCClientUtilsRender.ShouldRenderNeighborHalfSlabSide( blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide, bUpsideDown );
    }
    
    @Override
    public boolean ShouldRenderNeighborHalfSlabSide( IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown )
    {
		return GetIsUpsideDown( blockAccess, i, j, k ) != bNeighborUpsideDown;
    }
    
    @Override
    public boolean ShouldRenderNeighborFullFaceSide( IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide )
    {
    	if ( iNeighborSide < 2 )
    	{
    		boolean bUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
    		
    		if ( iNeighborSide == 0 )
    		{
    			return !bUpsideDown;
    		}
    		else // iNeighborSide == 1
    		{
    			return bUpsideDown;
    		}    			
    	}
    	
		return true;
    }    
}
