// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11; // client only

public abstract class FCBlockVessel extends BlockContainer
	implements FCIBlockMechanical
{
	private static final int m_iTickRate = 1;	
	
	public static final double m_dCollisionBoxHeight = 1F;
	
	public static final float m_fModelHeight = 1F;
	public static final float m_fModelWidth = ( 1F - ( 2F / 16F ) );
	public static final float m_fModelHalfWidth = m_fModelWidth / 2F;	
	public static final float m_fModelBandHeight = ( 12F / 16F );
	public static final float m_fModelBandHalfHeight = m_fModelBandHeight / 2F;
	
    public FCBlockVessel( int iBlockID, Material material )
    {
        super( iBlockID, material );        
    }
    
	@Override
    public int tickRate( World world )
    {
    	return m_iTickRate;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
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
    	return AxisAlignedBB.getAABBPool().getAABB( (double)i, (double)j, (double)k,
    			(double)(i + 1), (double)j + m_dCollisionBoxHeight, (double)k + 1 );
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
		// update mechanical power state
		
		boolean bWasPowered = GetMechanicallyPoweredFlag( world, i, j, k );
		boolean bIsPowered = false;
		int iPoweredFromFacing = 0;
		
    	for ( int iFacing = 2; iFacing <= 5; iFacing++ )
    	{
			if ( FCUtilsMechPower.IsBlockPoweredByAxleToSide( world, i, j, k, iFacing ) || 
				FCUtilsMechPower.IsBlockPoweredByHandCrankToSide( world, i, j, k, iFacing ) )
			{
				bIsPowered = true;
				iPoweredFromFacing = iFacing;
				
				BreakPowerSourceThatOpposePoweredFacing( world, i, j, k, iPoweredFromFacing );
			}
    	}
    	
    	if ( bWasPowered != bIsPowered )
    	{
	        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	    		"step.gravel", 
	    		2.0F + ( rand.nextFloat() * 0.1F ),		// volume 
	    		0.5F + ( rand.nextFloat() * 0.1F ) );	// pitch
	        
    		SetMechanicallyPoweredFlag( world, i, j, k, bIsPowered );
    		
    		if ( !bIsPowered )
    		{
    			// unpowered blocks face upwards
    			
    			SetTiltFacing( world, i, j, k, 0 );
    		}
    		else
    		{
    			SetFacingBasedOnPoweredFromFacing( world, i, j, k, iPoweredFromFacing );
    		}
    	}    	
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }
    
	@Override
    public int GetFacing( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iFacing = 1;
    	
    	if ( GetMechanicallyPoweredFlag( blockAccess, i, j, k ) )
    	{
        	iFacing = GetTiltFacing( blockAccess, i, j, k );
    	}
    	
    	return iFacing;    	
    }
    
	@Override
    public void SetFacing( World world, int i, int j, int k, int iFacing )
    {
    }

	@Override
	public int GetFacing( int iMetadata )
	{
		return 0;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		return iMetadata;
	}
	
    //------------- FCIBlockMechanical -------------//
    
	@Override
    public boolean CanOutputMechanicalPower()
    {
    	return false;
    }

	@Override
    public boolean CanInputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean IsInputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return FCUtilsMechPower.IsBlockPoweredByAxle( world, i, j, k, this ); 
    }    

	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		return iFacing >= 2;
	}

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
	}
	
    //------------- Class Specific Methods -------------//

	public int GetTiltFacing( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return ( iBlockAccess.getBlockMetadata( i, j, k ) & 3 ) + 2;
	}
	
	public void SetTiltFacing( World world, int i, int j, int k, int iFacing )
	{
		int iFlatFacing = iFacing - 2;
		
		if ( iFlatFacing < 0 )
		{
			iFlatFacing = 0;
		}
		
		int iMetaData = world.getBlockMetadata( i, j, k ) & (~3); // filter out old on state
    	
		iMetaData |= ( iFlatFacing & 3 );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
        
		world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
	}

	public boolean GetMechanicallyPoweredFlag( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return ( iBlockAccess.getBlockMetadata( i, j, k ) & 4 ) > 0;
	}
    
	private void SetMechanicallyPoweredFlag( World world, int i, int j, int k, boolean bFlag )
	{
		int iMetaData = world.getBlockMetadata( i, j, k ) & (~4); // filter out old on state
    	
		if ( bFlag )
		{
			iMetaData |= 4;
		}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
	}
	
	private void SetFacingBasedOnPoweredFromFacing( World world, int i, int j, int k, int iPoweredFromFacing )
	{
		int iNewFacing = Block.RotateFacingAroundJ( iPoweredFromFacing, false );
		
		SetTiltFacing( world, i, j, k, iNewFacing );
	}
	
	private void BreakPowerSourceThatOpposePoweredFacing( World world, int i, int j, int k, int iPoweredFromFacing )
	{
		// break other axles that don't "fit" with the one that's primarily powering it
		
		int iOppositePoweredFromFacing = Block.GetOppositeFacing( iPoweredFromFacing );
		
    	for ( int iFacing = 2; iFacing <= 5; iFacing++ )
    	{
    		if ( iFacing != iPoweredFromFacing )
    		{
        		boolean bShouldBreak = false;
            	
	    		if ( iFacing == iOppositePoweredFromFacing  )
	    		{
	    			if ( FCUtilsMechPower.IsBlockPoweredByAxleToSide( world, i, j, k, iFacing ) )
	    			{
	    				bShouldBreak = true;
	    			}
	    		}
	    		else if ( FCUtilsMechPower.DoesBlockHaveFacingAxleToSide( world, i, j, k, iFacing ) )
	    		{
	    			bShouldBreak = true;
	    		}
	    		
	    		if ( bShouldBreak )
	    		{
					FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
					
					tempPos.AddFacingAsOffset( iFacing );
					
					((FCBlockAxle)FCBetterThanWolves.fcBlockAxle).BreakAxle( world, tempPos.i, tempPos.j, tempPos.k );
	    		}
	    		
	        	// break powered hand cranks
	        	
	    		if ( FCUtilsMechPower.IsBlockPoweredByHandCrankToSide( world, i, j, k, iFacing ) )
	    		{
					FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
					
					tempPos.AddFacingAsOffset( iFacing );
					
					((FCBlockHandCrank)FCBetterThanWolves.fcHandCrank).BreakCrankWithDrop( world, tempPos.i, tempPos.j, tempPos.k );
	    		}
    		}    		
    	}    	
	}
    
	protected boolean IsOpenSideBlocked( World world, int i, int j, int k )
	{
		int iFacing = GetFacing( world, i, j, k );
		
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		return world.isBlockNormalCube( targetPos.i, targetPos.j, targetPos.k );
	}

	//----------- Client Side Functionality -----------//
	
    protected Icon[] m_IconWideBandBySideArray = new Icon[6];
    protected Icon[] m_IconCenterColumnBySideArray = new Icon[6];
    protected Icon[] m_IconInteriorBySideArray = new Icon[6];
    
    private boolean m_bRenderingInterior = false; // temporary state variable used during rendering
    private boolean m_bRenderingWideBand = false; // temporary state variable used during rendering
    
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( m_bRenderingInterior )
		{
			return m_IconInteriorBySideArray[iSide];
		}
		else if ( m_bRenderingWideBand )
		{
			return m_IconWideBandBySideArray[iSide];
		}
		else
		{
			return m_IconCenterColumnBySideArray[iSide];
		}
    }

	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
		int iTextureSide = iSide;
		
		if ( GetMechanicallyPoweredFlag( blockAccess, i, j, k ) )
		{
			int iFacing = GetTiltFacing( blockAccess, i, j, k );			
			
			if ( iFacing == iSide )
			{
	    		iTextureSide = 1;
			}
			else if ( iSide == Block.GetOppositeFacing( iFacing ) )
			{
	    		iTextureSide = 0;
			}
			else
			{
	    		iTextureSide = 2;
			}
		}		
		
		return getIcon( iTextureSide, 0 );
    }
	
	@Override
    abstract public void registerIcons( IconRegister register );
    
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
		if ( !GetMechanicallyPoweredFlag( world, i, j, k ) )
		{
			int iBlockUnderID  = world.getBlockId( i, j - 1, k );
			
	    	if ( iBlockUnderID == Block.fire.blockID || iBlockUnderID == FCBetterThanWolves.fcBlockFireStoked.blockID  )
	        {
	            for ( int counter = 0; counter < 1; counter++ )
	            {
	                float smokeX = (float)i + random.nextFloat();
	                float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
	                float smokeZ = (float)k + random.nextFloat();
	                
	                world.spawnParticle( "fcwhitesmoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D );
	            }
	        }
		}
    }
    
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	int iFacing = GetFacing( blockAccess, i, j, k );
    	
    	m_bRenderingInterior = false;
    	
    	// render middle band
    	
    	m_bRenderingWideBand = true;
    	
    	FCClientUtilsRender.RenderBlockWithBoundsAndTextureRotation( renderBlocks, this, i, j, k, iFacing,
			0F, 0.5F - m_fModelBandHalfHeight, 0F, 
    		1F, 0.5F + m_fModelBandHalfHeight, 1F );

    	// render center column (includes top and bottom "lip")
    	
    	m_bRenderingWideBand = false;
    	
    	FCClientUtilsRender.RenderBlockWithBoundsAndTextureRotation( renderBlocks, this, i, j, k, iFacing,
			0.5F - m_fModelHalfWidth, 0.0F, 0.5F - m_fModelHalfWidth, 
    		0.5F + m_fModelHalfWidth, m_fModelHeight, 0.5F + m_fModelHalfWidth );
    	
        // render the interior of the bin
    	
    	m_bRenderingInterior = true;
        
        Tessellator tesselator = Tessellator.instance;

        FCClientUtilsRender.SetRenderBoundsToBlockFacing( renderBlocks, iFacing, 
        	0.5F - m_fModelHalfWidth, 0.0F, 0.5F - m_fModelHalfWidth, 
    		0.5F + m_fModelHalfWidth, m_fModelHeight, 0.5F + m_fModelHalfWidth );
        
        FCClientUtilsRender.SetTextureRotationBasedOnBlockFacing( renderBlocks, iFacing );
        
        tesselator.setBrightness( getMixedBrightnessForBlock( blockAccess, i, j, k ) );
        
        final float fInteriorBrightnessMultiplier = 0.66F;
        
        int iColorMultiplier = colorMultiplier( blockAccess, i, j, k );
        
        float iColorRed = (float)( iColorMultiplier >> 16 & 255 ) / 255.0F;
        float iColorGreen = (float)( iColorMultiplier >> 8 & 255 ) / 255.0F;
        float iColorBlue = (float)( iColorMultiplier & 255 ) / 255.0F;

        tesselator.setColorOpaque_F( fInteriorBrightnessMultiplier * iColorRed, fInteriorBrightnessMultiplier * iColorGreen, fInteriorBrightnessMultiplier * iColorBlue );
        
        final double dInteriorOffset = ( 4D / 16D ) - 0.001D; // slight extra bit to avoid texture glitches around edges
        
        renderBlocks.renderFaceXPos( this, (double)i - 1.0D + dInteriorOffset, (double)j, (double)k, getBlockTexture( blockAccess, i, j, k, 4 ) );        
        renderBlocks.renderFaceXNeg( this, (double)i + 1.0F - dInteriorOffset, (double)j, (double)k, getBlockTexture( blockAccess, i, j, k, 5 ) );
        renderBlocks.renderFaceZPos( this, (double)i, (double)j, (double)k - 1.0F + dInteriorOffset, getBlockTexture( blockAccess, i, j, k, 2 ) );
        renderBlocks.renderFaceZNeg( this, (double)i, (double)j, (double)k + 1.0F - dInteriorOffset, getBlockTexture( blockAccess, i, j, k, 3 ) );
        
        renderBlocks.renderFaceYPos( this, (double)i, (double)((float)j - 1.0F + dInteriorOffset ), (double)k, getBlockTexture( blockAccess, i, j, k, 0 ) );        
        renderBlocks.renderFaceYNeg( this, (double)i, (double)((float)j + 1.0F - dInteriorOffset ), (double)k, getBlockTexture( blockAccess, i, j, k, 1 ) );        
    	
		renderBlocks.ClearUvRotation();
		
    	return true;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	m_bRenderingInterior = false;
    	
    	// render middle band
    	
    	m_bRenderingWideBand = true;
    	
    	renderBlocks.setRenderBounds( 
			0F, 0.5F - m_fModelBandHalfHeight, 0F, 
    		1F, 0.5F + m_fModelBandHalfHeight, 1F );

        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, -0.5F, -0.5F, -0.5F, 0 );
        
    	// render center column (includes top and bottom "lip")
    	
    	m_bRenderingWideBand = false;
    	
    	renderBlocks.setRenderBounds( 
			0.5F - m_fModelHalfWidth, 0.0F, 0.5F - m_fModelHalfWidth, 
    		0.5F + m_fModelHalfWidth, m_fModelHeight, 0.5F + m_fModelHalfWidth );
    	
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, -0.5F, -0.5F, -0.5F, 0 );
        
    	// render interior
    	
    	m_bRenderingInterior = true;    	
    	
    	renderBlocks.setRenderBounds( 
        	0.5F - m_fModelHalfWidth, 0.0F, 0.5F - m_fModelHalfWidth, 
    		0.5F + m_fModelHalfWidth, m_fModelHeight, 0.5F + m_fModelHalfWidth );
        
        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef( -0.5F, -0.5F, -0.5F );
        
        final double dInteriorOffset = ( 4D / 16D ) - 0.001D; // slight extra bit to avoid texture glitches around edges
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(this, 0.0D, -1F + dInteriorOffset, 0.0D, getIcon( 0, 0 ) );
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderBlocks.renderFaceZNeg(this, 0.0D, 0.0D, 1.0F - dInteriorOffset, getIcon( 3, 0 ) );
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(this, 0.0D, 0.0D, -1.0F + dInteriorOffset, getIcon( 2, 0 ) );
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(this, 1.0F - dInteriorOffset, 0.0D, 0.0D, getIcon( 5, 0 ) );
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(this, -1.0D + dInteriorOffset, 0.0D, 0.0D, getIcon( 4, 0 ) );
        tessellator.draw();
        
        GL11.glTranslatef( 0.5F, 0.5F, 0.5F );        
    }    
}