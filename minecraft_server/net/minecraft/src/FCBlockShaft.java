// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockShaft extends Block
{
	protected static final double m_dShaftWidth = 0.125D;
	protected static final double m_dShaftHalfWidth = ( m_dShaftWidth / 2D );
	protected static final double m_dShaftHeight = 0.75D;

	protected static final double m_dSelectionBoxWidth = ( m_dShaftWidth + ( 2D / 16D ) );
	protected static final double m_dSelectionBoxHalfWidth = ( m_dSelectionBoxWidth / 2D );
	protected static final double m_dSelectionBoxHeight = m_dShaftHeight + ( 1D / 16D );
	
    private static final AxisAlignedBB m_boxShaft = new AxisAlignedBB( 
		0.5F - m_dShaftHalfWidth, 1F - m_dShaftHeight, 0.5F - m_dShaftHalfWidth, 
		0.5F + m_dShaftHalfWidth, 1F, 0.5F + m_dShaftHalfWidth );
    
    private static final AxisAlignedBB m_boxShaftSupporting = new AxisAlignedBB( 
		0.5F - m_dShaftHalfWidth, 0F, 0.5F - m_dShaftHalfWidth, 
		0.5F + m_dShaftHalfWidth, 1F, 0.5F + m_dShaftHalfWidth );
	
    private static final AxisAlignedBB m_boxSelection = new AxisAlignedBB( 
		0.5F - m_dSelectionBoxHalfWidth, 1F - m_dSelectionBoxHeight, 0.5F - m_dSelectionBoxHalfWidth, 
		0.5F + m_dSelectionBoxHalfWidth, 1F, 0.5F + m_dSelectionBoxHalfWidth );
    
    private static final AxisAlignedBB m_boxSelectionSupporting = new AxisAlignedBB( 
		0.5F - m_dSelectionBoxHalfWidth, 0F, 0.5F - m_dSelectionBoxHalfWidth, 
		0.5F + m_dSelectionBoxHalfWidth, 1F, 0.5F + m_dSelectionBoxHalfWidth );
	
    public FCBlockShaft( int iBlockID )
    {
        super( iBlockID, Material.circuits );

        setHardness( 0F );
        setResistance( 0F );

        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockShaft" );
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
    public boolean canPlaceBlockOnSide( World world, int i, int j, int k, int iSide )
    {
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iSide ) );
		
		if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, iSide ) )
		{
			int iTargetID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
			
			if ( CanStickInBlockType( iTargetID ) )
			{
				return true;
			}
		}
    	
        return false;
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		iMetadata = SetFacing( iMetadata, Block.GetOppositeFacing( iFacing ) );
		
		FCUtilsBlockPos anchorPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );
		int iAnchorID = world.getBlockId( anchorPos.i, anchorPos.j, anchorPos.k );
		Block anchorBlock = Block.blocksList[iAnchorID];
		
		if ( anchorBlock != null )
		{		
			world.playSoundEffect( (float)i + 0.5F, (float)j - 0.5F, (float)k + 0.5F, anchorBlock.stepSound.getPlaceSound(), 
				anchorBlock.stepSound.getVolume() / 2.0F, anchorBlock.stepSound.getPitch() * 0.8F );
			
			if ( !world.isRemote )
			{
				anchorBlock.OnPlayerWalksOnBlock( world, anchorPos.i, anchorPos.j, anchorPos.k, null );
			}				
		}
	    
		return iMetadata;
    }
	
	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return Item.stick.itemID;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
        return null;
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	AxisAlignedBB transformedBox;
        
    	if ( IsSupportingOtherBlock( blockAccess, i, j, k ) )
    	{
    		transformedBox = m_boxShaftSupporting.MakeTemporaryCopy();
    	}
    	else
    	{
    		transformedBox = m_boxShaft.MakeTemporaryCopy();
    	}
    	
    	transformedBox.TiltToFacingAlongJ( GetFacing( blockAccess, i, j, k ) );
		
		return transformedBox;		
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
		int iFacing = GetFacing( world, i, j, k );
		
		if ( !canPlaceBlockOnSide( world, i, j, k, Block.GetOppositeFacing( iFacing  ) ) )
		{
	    	// pop the block off if it no longer has a valid anchor point
	    	
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
		}
    }
	
	@Override
	public boolean HasSmallCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		// only has upwards facing hardpoint for tiki torches
		
		return iFacing == 1 && GetFacing( blockAccess, i, j, k ) == 0;
	}
	
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	if ( !world.isRemote )
    	{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );    		
    	}
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{		
		// shaft facing is in the direction of attachment
		
    	return iMetadata & 7;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= ~7;
		
        return iMetadata | iFacing;
	}
	
	@Override
    public boolean CanRotateAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		return iFacing == GetFacing( world, i, j, k );
    }
    
	@Override
    public int GetNewMetadataRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing )
    {
		int iOldMetadata = world.getBlockMetadata( i, j, k );
		
		return SetFacing( iOldMetadata, iRotatedFacing );
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
	public void OnNeighborDisrupted( World world, int i, int j, int k, int iToFacing )
	{
		if ( iToFacing == GetFacing( world, i, j, k ) )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
		}
	}
		
    //------------- Class Specific Methods ------------//    
	
    public boolean CanStickInBlockType( int iBlockID )
    {
    	Block block = Block.blocksList[iBlockID];
    	
    	return block != null && ((FCItemTool)(Item.shovelWood)).IsToolTypeEfficientVsBlockType( block );
    }   
    
    public boolean IsSupportingOtherBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( GetFacing( blockAccess, i, j, k ) == 0 && FCUtilsWorld.IsBlockRestingOnThatBelow( blockAccess, i, j + 1, k ) )
    	{
			return true;
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
}