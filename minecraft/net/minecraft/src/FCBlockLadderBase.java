// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockLadderBase extends Block
{
    protected static final float m_fLadderThickness = 0.125F;

    protected static final AxisAlignedBB m_boxCollision = new AxisAlignedBB( 0D, 0D, 1D - m_fLadderThickness, 1D, 1D, 1D );
    
    protected FCBlockLadderBase( int iBlockID )
    {
        super( iBlockID, Material.circuits );
        
        setHardness( 0.4F );        
        SetAxesEffectiveOn( true );
        
        SetBuoyant();
        
        setStepSound( soundLadderFootstep );
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockLadder.blockID;
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	AxisAlignedBB transformedBox = m_boxCollision.MakeTemporaryCopy();
    	
    	transformedBox.RotateAroundJToFacing( GetFacing( blockAccess, i, j, k ) );
    	
        return transformedBox;        
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
    	for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
    	{
    		if ( CanAttachToFacing( world, i, j, k, Block.GetOppositeFacing( iTempFacing ) ) )
    		{    			
    			return true;
    		}
    	}
    	
    	return false;
    }

    @Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	if ( CanAttachToFacing( world, i, j, k, Block.GetOppositeFacing( iFacing ) ) )
		{
    		iMetadata = SetFacing( iMetadata, iFacing );
		}
    	else
    	{    	
    		// specified facing isn't valid, search for another
    		
	    	for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
	    	{
	    		if ( CanAttachToFacing( world, i, j, k, iTempFacing ) )
    			{
	        		iMetadata = SetFacing( iMetadata, Block.GetOppositeFacing( iTempFacing ) );
	        		
	        		break;
    			}
	    	}
    	}
    	
        return iMetadata;
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iChangedBlockID )
    {
        int iMetadata = world.getBlockMetadata( i, j, k );
        
		if ( !CanAttachToFacing( world, i, j, k, Block.GetOppositeFacing( GetFacing( iMetadata ) ) ) )
		{
            dropBlockAsItem( world, i, j, k, iMetadata, 0 );
            world.setBlockToAir( i, j, k );
		}
			
        super.onNeighborBlockChange( world, i, j, k, iChangedBlockID );
    }
    
    @Override
    public int getRenderType()
    {
        return 8;
    }

	@Override
	public boolean IsBlockClimbable( World world, int i, int j, int k )
	{
		return true;
	}
	
	@Override
    public int GetFacing( int iMetadata )
    {
    	return ( iMetadata & 3 ) + 2;
    }
    
	@Override
	public int SetFacing( int iMetadata, int iFacing )
    {
    	int iFlatFacing = MathHelper.clamp_int( iFacing, 2, 5 ) - 2;
    	
    	iMetadata &= ~3;
    	
    	return iMetadata | iFlatFacing;
    }
    
	@Override
    public boolean CanRotateAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		return iFacing == Block.GetOppositeFacing( GetFacing( world, i, j, k ) );
    }
    
	@Override
    public int GetNewMetadataRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing )
    {
		int iOldMetadata = world.getBlockMetadata( i, j, k );
		
		return SetFacing( iOldMetadata, Block.GetOppositeFacing( iRotatedFacing ) );
    }
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
    		
    	return ( iFilterableProperties & Item.m_iFilterable_SolidBlock ) == 0;
    }
    
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
    protected boolean CanAttachToFacing( World world, int i, int j, int k, int iFacing )
    {
    	if ( iFacing >= 2 )
    	{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    		
    		return FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, 
    			Block.GetOppositeFacing( iFacing ) );
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//

    protected static final double m_dLadderHorizontalOffset = 0.05000000074505806D;
    
    private Icon m_filterIcon;
	
    @Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "ladder" );
		
		m_filterIcon = register.registerIcon( "fcBlockHopper_ladder" );
    }

	@Override
    public Icon GetHopperFilterIcon()
    {
    	return m_filterIcon;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return RenderLadder( renderer, i, j, k );
    }
    
    public boolean RenderLadder( RenderBlocks renderBlocks, int i, int j, int k )
    {
		IBlockAccess blockAccess = renderBlocks.blockAccess;
		int iFacing = GetFacing( blockAccess, i, j, k );
		
        Tessellator var5 = Tessellator.instance;

        var5.setBrightness( getMixedBrightnessForBlock( blockAccess, i, j, k));
        float var7 = 1.0F;
        var5.setColorOpaque_F(var7, var7, var7);
        
        Icon icon = blockIcon;
        
        if ( renderBlocks.hasOverrideBlockTexture() )
        {
        	icon = renderBlocks.GetOverrideTexture();
        }
        
        double dMinU = icon.getMinU();
        double dMinV = icon.getMinV();
        double dMaxU = icon.getMaxU();
        double dMaxV = icon.getMaxV();

        if ( iFacing == 5 )
        {
            var5.addVertexWithUV((double)i + m_dLadderHorizontalOffset, (double)(j + 1), (double)(k + 1), dMinU, dMinV);
            var5.addVertexWithUV((double)i + m_dLadderHorizontalOffset, (double)(j + 0), (double)(k + 1), dMinU, dMaxV);
            var5.addVertexWithUV((double)i + m_dLadderHorizontalOffset, (double)(j + 0), (double)(k + 0), dMaxU, dMaxV);
            var5.addVertexWithUV((double)i + m_dLadderHorizontalOffset, (double)(j + 1), (double)(k + 0), dMaxU, dMinV);
        }
        else if ( iFacing == 4 )
        {
            var5.addVertexWithUV((double)(i + 1) - m_dLadderHorizontalOffset, (double)(j + 0), (double)(k + 1), dMaxU, dMaxV);
            var5.addVertexWithUV((double)(i + 1) - m_dLadderHorizontalOffset, (double)(j + 1), (double)(k + 1), dMaxU, dMinV);
            var5.addVertexWithUV((double)(i + 1) - m_dLadderHorizontalOffset, (double)(j + 1), (double)(k + 0), dMinU, dMinV);
            var5.addVertexWithUV((double)(i + 1) - m_dLadderHorizontalOffset, (double)(j + 0), (double)(k + 0), dMinU, dMaxV);
        }
        else if ( iFacing == 3 )
        {
            var5.addVertexWithUV((double)(i + 1), (double)(j + 0), (double)k + m_dLadderHorizontalOffset, dMaxU, dMaxV);
            var5.addVertexWithUV((double)(i + 1), (double)(j + 1), (double)k + m_dLadderHorizontalOffset, dMaxU, dMinV);
            var5.addVertexWithUV((double)(i + 0), (double)(j + 1), (double)k + m_dLadderHorizontalOffset, dMinU, dMinV);
            var5.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)k + m_dLadderHorizontalOffset, dMinU, dMaxV);
        }
        else if ( iFacing == 2 )
        {
            var5.addVertexWithUV((double)(i + 1), (double)(j + 1), (double)(k + 1) - m_dLadderHorizontalOffset, dMinU, dMinV);
            var5.addVertexWithUV((double)(i + 1), (double)(j + 0), (double)(k + 1) - m_dLadderHorizontalOffset, dMinU, dMaxV);
            var5.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)(k + 1) - m_dLadderHorizontalOffset, dMaxU, dMaxV);
            var5.addVertexWithUV((double)(i + 0), (double)(j + 1), (double)(k + 1) - m_dLadderHorizontalOffset, dMaxU, dMinV);
        }

        return true;
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
}
