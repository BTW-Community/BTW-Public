// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockChunkOre extends Block
{
	protected static FCModelBlockChunkOre m_model = new FCModelBlockChunkOre();
	
    protected FCBlockChunkOre( int iBlockID )
    {
        super( iBlockID, Material.circuits  );
        
        setHardness( 0F );
        SetPicksEffectiveOn( true );
        
    	InitBlockBounds( 0.5D - m_model.m_dBoundingBoxHalfWidth, 
    		m_model.m_dBoundingBoxVerticalOffset, 
    		0.5D - m_model.m_dBoundingBoxHalfWidth, 
    		0.5D + m_model.m_dBoundingBoxHalfWidth, 
    		m_model.m_dBoundingBoxVerticalOffset + m_model.m_dBoundingBoxHeight, 
    		0.5D + m_model.m_dBoundingBoxHalfWidth );
    	
        setStepSound( soundStoneFootstep );
        
		SetCanBeCookedByKiln( true );
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		int iBlockFacing = world.rand.nextInt( 4 ) + 2;

        return SetFacing( iMetadata, iBlockFacing ); 
    }
	
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        if ( FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
		{
            return super.canPlaceBlockAt( world, i, j, k );
		}
        
		return false;		
    }
    
	@Override
	public boolean IsBlockRestingOnThatBelow( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockToAir( i, j, k );
        }
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
	{
		return null;
	}
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startRay, Vec3 endRay )
    {
    	FCModelBlock m_modelTransformed = m_model.MakeTemporaryCopy();
    	
    	int iFacing = GetFacing( world, i, j, k );
    	
    	m_modelTransformed.RotateAroundJToFacing( iFacing );
    	
    	return m_modelTransformed.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
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
	public int GetFacing( int iMetadata )
	{
		return ( iMetadata & 3 ) + 2;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= ~3; // filter out old facing
		
		iMetadata |= MathHelper.clamp_int( iFacing, 2, 5 ) - 2; // convert to flat facing
		
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 8;
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {    	
    	FCModelBlock transformedModel = m_model.MakeTemporaryCopy();    	
		
		transformedModel.RotateAroundJToFacing( GetFacing( renderBlocks.blockAccess, i, j, k ) );
		
		return transformedModel.RenderAsBlock( renderBlocks, this, i, j, k );
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	m_model.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    }
    
    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
    {
        RenderCookingByKilnOverlay( renderBlocks, i, j, k, bFirstPassResult );
    }
}