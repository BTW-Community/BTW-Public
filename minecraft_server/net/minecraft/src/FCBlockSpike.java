// FCMOD

package net.minecraft.src;

public class FCBlockSpike extends Block
{
	protected static FCModelBlockSpike m_model = new FCModelBlockSpike();
	
    public FCBlockSpike( int iBlockID )
    {
        super( iBlockID, Material.iron );
        
        setHardness( 2F );     
        SetPicksEffectiveOn();
        
		InitBlockBounds( m_model.m_boxCollisionCenter );
		
        setStepSound( soundMetalFootstep );        
        
        setUnlocalizedName( "fcBlockSpikeIron" );
        
        setCreativeTab( CreativeTabs.tabDecorations );
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
	{
		// computes the bounds strictly from metadata, since we can't rely on neighboring blocks
		// to determine shape during chunk load
		
		int iFacing = GetFacing( world, i, j, k );
		
		if ( iFacing >= 2 )
		{
			AxisAlignedBB tempBox = m_model.m_boxCollisionStrut.MakeTemporaryCopy();
			
			tempBox.RotateAroundJToFacing( iFacing );
			
			tempBox.offset( i, j, k );
			
			return tempBox;
		}
		
    	return m_model.m_boxCollisionCenter.MakeTemporaryCopy().offset( i, j, k );	
	}
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, 
    	int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCModelBlock m_modelTransformed = new FCModelBlock();

    	// use the center collision area instead of just the raw model to give the player
    	// a larger surface to click on the top and bottom
    	
    	m_modelTransformed.AddPrimitive( m_model.m_boxCollisionCenter.MakeTemporaryCopy() );
    	
    	FCModelBlock transformedSupportsModel = 
    		GetSideSupportsTemporaryModel( world, i, j, k );
    	
    	if ( transformedSupportsModel != null )
    	{
    		transformedSupportsModel.MakeTemporaryCopyOfPrimitiveList( m_modelTransformed );
    	}
    	
    	return m_modelTransformed.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
    }
    
    @Override
    public boolean canPlaceBlockOnSide( World world, int i, int j, int k, int iSide )
    {
    	return CanConnectToBlockToFacing( world, i, j, k, Block.GetOppositeFacing( iSide ) );    	
    }

	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		return SetFacing( iMetadata, Block.GetOppositeFacing( iFacing ) );
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        super.onNeighborBlockChange( world, i, j, k, iNeighborBlockID );
        
		if ( !CanConnectToBlockToFacing( world, i, j, k, GetFacing( world, i, j, k ) ) )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockToAir( i, j, k );
		}
    }
	
	@Override
	public int GetFacing( int iMetadata )
	{		
    	return iMetadata & 7;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= ~7;
		
        return iMetadata | iFacing;
	}
	
	@Override
	public boolean HasSmallCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, 
		int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing < 2;
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
    
	//------------- Class Specific Methods ------------//
	
	protected boolean CanConnectToBlockToFacing( IBlockAccess blockAccess, 
		int i, int j, int k, int iFacing )
	{
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( blockAccess, 
			targetPos.i, targetPos.j, targetPos.k, Block.GetOppositeFacing( iFacing ) ) || 
			blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == blockID;
	}
	
	protected boolean IsConnectedSpikeToFacing( IBlockAccess blockAccess, 
		int i, int j, int k, int iFacing )
	{
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		return blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == blockID &&
			GetFacing( blockAccess, targetPos.i, targetPos.j, targetPos.k ) ==
			Block.GetOppositeFacing( iFacing );
	}
	
	/**
	 * Relies on horizontally neighboring blocks, so may not be accurate during chunk load
	 * and thus should not be used for collision volume
	 */
	FCModelBlock GetSideSupportsTemporaryModel( IBlockAccess blockAccess, int i, int j, int k )
	{
    	FCModelBlock supportsModel = null;
    	int iBlockFacing = GetFacing( blockAccess, i, j, k );
    		
    	for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
    	{
    		if ( iTempFacing == iBlockFacing || 
    			IsConnectedSpikeToFacing( blockAccess, i, j, k, iTempFacing ) )
    		{
    			FCModelBlock tempSupportsModel = m_model.m_modelSideSupport.MakeTemporaryCopy();
    			
    			tempSupportsModel.RotateAroundJToFacing( iTempFacing );
	    		
    			if ( supportsModel == null )
    			{
    				supportsModel = tempSupportsModel;
    			}
    			else
    			{
    				tempSupportsModel.MakeTemporaryCopyOfPrimitiveList( supportsModel );
    			}
    		}
    	}
    	
		return supportsModel;
	}
	
	//----------- Client Side Functionality -----------//
}
