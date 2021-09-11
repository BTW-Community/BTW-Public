// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBucket extends FCBlockFalling
{
	protected FCModelBlockBucket m_model;
    
	protected FCModelBlock m_modelTransformed;
    	
    public FCBlockBucket( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialMiscellaneous );
        
        setHardness( 0F );
        setResistance( 0F );
        
    	InitBlockBounds( 
    		0.5D - FCModelBlockBucket.m_dBodyHalfWidth, FCModelBlockBucket.m_dBaseHeight, 
    		0.5D - FCModelBlockBucket.m_dBodyHalfWidth, 
    		0.5D + FCModelBlockBucket.m_dBodyHalfWidth, FCModelBlockBucket.m_dHeight, 
    		0.5D + FCModelBlockBucket.m_dBodyHalfWidth );
    	
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "bucket" );
        
        InitModels();
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		iMetadata = SetFacing( iMetadata, iFacing );
		
		return iMetadata;
    }
		
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneMod )
    {
		return Item.bucketEmpty.itemID;
    }
	
	@Override
    public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChance )
    {
		// always an empty bucket on bad break, regardless of contents
		
		DropItemsIndividualy( world, i, j, k, Item.bucketEmpty.itemID, 1, 0, fChance );		
		
    	return true;
	}
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
        return null;
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	m_modelTransformed = m_model.MakeTemporaryCopy();
    	
    	int iFacing = GetFacing( world, i, j, k );
    	
    	m_modelTransformed.TiltToFacingAlongJ( iFacing );
    	
    	FCModelBlockBucket.OffsetModelForFacing( m_modelTransformed, iFacing );
    	
    	return m_modelTransformed.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
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
    public int OnPreBlockPlacedByPiston( World world, int i, int j, int k, int iMetadata, int iDirectionMoved )
    {
		if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, i, j - 1, k, 
			1, true ) )
		{
			// handle bucket being pushed over ledge, and tilting in the appropriate direction
			
			if ( iDirectionMoved >= 2 )
			{ 
				int iFacing = GetFacing( iMetadata );
				
				if ( iFacing == 0 )
				{
					iFacing = Block.GetOppositeFacing( iDirectionMoved );
				}
				else if ( iFacing == 1 )
				{				
					iFacing = iDirectionMoved;
				}
				else if ( iFacing == iDirectionMoved )
				{
					iFacing = 0;
				}
				else if ( iFacing == Block.GetOppositeFacing( iDirectionMoved ) )
				{
					iFacing = 1;
				}
				else
				{
					// orientations perpendicular to direction of travel do not change facing
				}
				
				iMetadata = SetFacing( iMetadata, iFacing );
			}
		}
		
    	return iMetadata;
    }
	
	@Override
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
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
    
	//------------- Class Specific Methods ------------//
	
	protected void InitModels()
	{
		m_model = new FCModelBlockBucket();
	    
		// must initialize transformed model due to weird vanilla getIcon() calls that 
		// occur outside of regular rendering
		
		m_modelTransformed = m_model; 
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_iconOpenTop;    
    private Icon m_iconOpenSide;    
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockBucketEmpty" );
        
		m_iconOpenTop = register.registerIcon( "fcBlockBucketEmpty_top" );
		m_iconOpenSide = register.registerIcon( "fcBlockBucketEmpty_top_side" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( m_modelTransformed.GetActivePrimitiveID() == m_model.m_iAssemblyIDRim )
		{
			int iFacing = GetFacing( iMetadata );
			
			// backface must use texture with hole in it, as backfaces get rendered when pushed by 
			// pistons, causing them to flicker with the contents texture
			
			if ( iFacing == iSide || iFacing == Block.GetOppositeFacing( iSide ) )
			{
				if ( iFacing < 2 )
				{
					return m_iconOpenTop;
				}
				else
				{
					return m_iconOpenSide;
				}
			}
		}
		
		return super.getIcon( iSide, iMetadata );
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		FCUtilsBlockPos myPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, 
			Block.GetOppositeFacing( iSide ) );
		
		int iMetadata = blockAccess.getBlockMetadata( myPos.i, myPos.j, myPos.k );
		
		return ShouldSideBeRenderedOnFallingBlock( iSide, iMetadata );    
	}
	
	@Override
    public boolean ShouldSideBeRenderedOnFallingBlock( int iSide, int iMetadata )
    {
    	int iFacing = GetFacing( iMetadata );    	
		
		int iActiveID = m_modelTransformed.GetActivePrimitiveID();
		
		if ( iActiveID == FCModelBlockBucket.m_iAssemblyIDInterior )
		{
			return iSide != Block.GetOppositeFacing( iFacing );
		}
		else if ( iSide == iFacing )
		{
			return iActiveID == FCModelBlockBucket.m_iAssemblyIDRim;
		}
		
		return true;		
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	m_modelTransformed = m_model.MakeTemporaryCopy();
    	
    	int iFacing = GetFacing( renderBlocks.blockAccess, i, j, k );
    	
    	m_modelTransformed.TiltToFacingAlongJ( iFacing );

    	FCModelBlockBucket.OffsetModelForFacing( m_modelTransformed, iFacing );
		
    	return m_modelTransformed.RenderAsBlock( renderBlocks, this, i, j, k );
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	m_modelTransformed = m_model; 
    	
    	m_modelTransformed.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    }
    
    @Override
    public void RenderFallingBlock( RenderBlocks renderBlocks, int i, int j, int k, int iMetadata )
    {
    	m_modelTransformed = m_model.MakeTemporaryCopy();
    	
    	int iFacing = GetFacing( iMetadata );
    	
    	m_modelTransformed.TiltToFacingAlongJ( iFacing );

    	FCModelBlockBucket.OffsetModelForFacing( m_modelTransformed, iFacing );
    	
    	m_modelTransformed.RenderAsFallingBlock( renderBlocks, this, i, j, k, iMetadata );
    }
    
    @Override
    public void RenderBlockMovedByPiston( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	// override to bypass rendering of all faces normally done with blocks moved by pistons
    	// since the bucket model relies on certain faces being culled
    	
        RenderBlock( renderBlocks, i, j, k );
    }
    
    private static AxisAlignedBB m_selectionBox = new AxisAlignedBB( 
		0.5D - FCModelBlockBucket.m_dBodyHalfWidth, FCModelBlockBucket.m_dBaseHeight, 
		0.5D - FCModelBlockBucket.m_dBodyHalfWidth, 
		0.5D + FCModelBlockBucket.m_dBodyHalfWidth, FCModelBlockBucket.m_dHeight, 
		0.5D + FCModelBlockBucket.m_dBodyHalfWidth );
    	
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, MovingObjectPosition rayTraceHit )
    {
		int i = rayTraceHit.blockX;
		int j = rayTraceHit.blockY;
		int k = rayTraceHit.blockZ;
		
		int iFacing = GetFacing( world, i, j, k );
		
		AxisAlignedBB tempBox = m_selectionBox.MakeTemporaryCopy();
		
		if ( iFacing != 1 )
		{
	    	tempBox.TiltToFacingAlongJ( iFacing );
	    	
			Vec3 offset = FCModelBlockBucket.GetOffsetForFacing( iFacing );
			
			tempBox.Translate( offset.xCoord, offset.yCoord, offset.zCoord );
		}
		
		return tempBox.offset( i, j, k );
    }
}
