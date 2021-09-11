// FCMOD

package net.minecraft.src;

public class FCBlockGroundCover extends Block
{
	public static final float m_fVisualHeight = 0.125F;

    protected FCBlockGroundCover( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 0.125F, 1F);
        
        setHardness( 0.1F );
        SetShovelsEffectiveOn();
        
        SetBuoyant();
        
        setLightOpacity( 0 );        
        
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
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	Block blockBelow = Block.blocksList[iBlockBelowID];
    	
    	if ( blockBelow != null )
    	{
    		return blockBelow.CanGroundCoverRestOnBlock( world, i, j - 1, k );
    	}
    	
    	return false;
    }
    
    @Override    
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        if ( !canPlaceBlockAt( world, i, j, k ) )
        {
            world.setBlockToAir( i, j, k );
        }
    }

    @Override    
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	float fVisualOffset = 0F;
    	
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	Block blockBelow = Block.blocksList[iBlockBelowID];
    	
    	if ( blockBelow != null )
    	{
    		fVisualOffset = blockBelow.GroundCoverRestingOnVisualOffset( world, i, j - 1, k ); 
    	}
    	
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0D, fVisualOffset, 0D, 
    		1D, m_fVisualHeight + fVisualOffset, 1D );
    	
        return rayTrace.GetFirstIntersection();    	
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public boolean IsGroundCover( )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//
    
    public static void ClearAnyGroundCoverRestingOnBlock( World world, int i, int j, int k )
    {
    	Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
    	
    	if ( blockAbove != null )
    	{
    		if ( blockAbove.IsGroundCover() )
    		{
    			world.setBlockToAir( i, j + 1, k );
    		}
    		else if ( blockAbove.GroundCoverRestingOnVisualOffset( world, i, j + 1, k ) < -0.99F )
    		{
    			Block block2Above = blockAbove = Block.blocksList[world.getBlockId( i, j + 2, k )];
    	    	
    	    	if ( block2Above != null && block2Above.IsGroundCover() )
    	    	{
	    			world.setBlockToAir( i, j + 2, k );
    	    	}
    		}
    	}
    }
    
    public static boolean IsGroundCoverRestingOnBlock( World world, int i, int j, int k )
    {
    	Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
    	
    	if ( blockAbove != null )
    	{
    		if ( blockAbove.IsGroundCover() )
    		{
    			return true;
    		}
    		else if ( blockAbove.GroundCoverRestingOnVisualOffset( world, i, j + 1, k ) < -0.99F )
    		{
    			Block block2Above = blockAbove = Block.blocksList[world.getBlockId( i, j + 2, k )];
    	    	
    	    	if ( block2Above != null && block2Above.IsGroundCover() )
    	    	{
	    			return true;
    	    	}
    		}
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
	
	@Override
    public void ClientBreakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
	{
		// mark blocks up to 2 below for render update due to render offset
		
        world.markBlockRangeForRenderUpdate( i, j - 1, k, i, j - 2, k );
	}
	
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	float fVisualOffset = 0F;
    	
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	Block blockBelow = Block.blocksList[iBlockBelowID];
    	
    	if ( blockBelow != null )
    	{
    		fVisualOffset = blockBelow.GroundCoverRestingOnVisualOffset( world, i, j - 1, k ); 
    	}
    	
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( 
    		i, j + fVisualOffset, k );
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	// test to prevent momentary display above partial blocks that have just been destroyed
    	
    	if ( blockAccess.getBlockId( i, j - 1, k ) != 0 )
    	{
	    	float fVisualOffset = 0F;
	    	
	    	int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
	    	Block blockBelow = Block.blocksList[iBlockBelowID];
	    	
	    	int iBlockHeight = ( blockAccess.getBlockMetadata( i, j, k ) & 7 ) + 1;
	    	
	    	if ( blockBelow != null )
	    	{
	    		fVisualOffset = blockBelow.GroundCoverRestingOnVisualOffset( blockAccess, i, j - 1, k );
	    		
	    		if ( fVisualOffset < 0.0F )
	    		{
	    			j -= 1;
	    			
	    			fVisualOffset += 1F;
	    		}
	    	}
	    	
	    	float fHeight = m_fVisualHeight * iBlockHeight;
	    	
	    	renderBlocks.setRenderBounds( 0F, fVisualOffset, 0F, 
	    		1F, fHeight + fVisualOffset, 1F );
	    
	    	FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, blockIcon );
    	}
    
    	return true;
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	if ( iSide >= 2 )
    	{
    		// check for fully grown groth to side to eliminate a large number of faces
    		
    		if ( blockAccess.isBlockOpaqueCube( iNeighborI, iNeighborJ, iNeighborK ) )
    		{
    			return false;
    		}
    		else if ( blockAccess.getBlockId( iNeighborI, iNeighborJ, iNeighborK ) == blockID )
    		{
    			FCUtilsBlockPos thisBlockPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, Block.GetOppositeFacing( iSide ) );
    			
    			// veryify we aren't a block that is visually offset by testing if the block we think we're in actually contains snow
    			
    			if ( blockAccess.getBlockId( thisBlockPos.i, thisBlockPos.j, thisBlockPos.k ) == blockID )
    			{
	    			int iBlockBelowID = blockAccess.getBlockId( iNeighborI, iNeighborJ - 1, iNeighborK );
	    			
	    			if ( iBlockBelowID != 0 && blocksList[iBlockBelowID].GroundCoverRestingOnVisualOffset( blockAccess, iNeighborI, iNeighborJ - 1, iNeighborK ) > -0.01F )
	    			{
	    				return false;
	    			}
    			}
    		}
    		
    		return true;
    	}
    	else if ( iSide == 1 )
    	{
    		return true;
    	}
    	
        return super.shouldSideBeRendered( blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
    
    @Override
    public boolean ShouldRenderNeighborFullFaceSide( IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide )
    {
    	if ( iNeighborSide == 1 )
    	{
			int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
			
			if ( iBlockBelowID != 0 && blocksList[iBlockBelowID].GroundCoverRestingOnVisualOffset( 
				blockAccess, i, j - 1, k ) > -m_fVisualHeight )
			{
				return false;
			}
    	}
    	
		return true;
    }    
}