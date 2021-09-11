// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCandle extends Block
{
	private static final double m_dStickHeight = ( 6D / 16D ); 
	private static final double m_dStickWidth = ( 2D / 16D ); 
	private static final double m_dStickHalfWidth = ( m_dStickWidth / 2D );
	
	private static final double m_dWickHeight = ( 1D / 16D );
	private static final double m_dWickWidth = ( 0.5D / 16D );
	private static final double m_dWickHalfWidth = ( m_dWickWidth / 2D );
	
    public FCBlockCandle( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialCandle );
        
        setHardness( 0F );    	
    	SetPicksEffectiveOn( true );        
        SetAxesEffectiveOn( true );        
        
        setLightValue( 1F );
        
    	InitBlockBounds( 0.5D - m_dStickHalfWidth, 0D, 0.5D - m_dStickHalfWidth, 
    		0.5D + m_dStickHalfWidth, m_dStickHeight, 0.5D + m_dStickHalfWidth );
    	
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockCandle" );        
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
		int iBlockBelowMetadata = world.getBlockMetadata( i, j - 1, k ) ;
		
		if ( iBlockBelowID == FCBetterThanWolves.fcAestheticNonOpaque.blockID && iBlockBelowMetadata == FCBlockAestheticNonOpaque.m_iSubtypeLightningRod )
		{
			return true;
		}

		return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( world, i, j - 1, k, 1, true );
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
		return null;
    }
	
	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemCandle.itemID;
    }
	
	@Override
    public int damageDropped( int iMetadata )
    {
    	return iMetadata;
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	// pop the block off if it no longer has a valid anchor point
    	
		if ( !canPlaceBlockAt( world, i, j, k ) )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
		}
    }
	
    @Override
	public boolean IsBlockRestingOnThatBelow( IBlockAccess blockAccess, int i, int j, int k )
	{
        return true;
	}

    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
	public void OnNeighborDisrupted( World world, int i, int j, int k, int iToFacing )
	{
		if ( iToFacing == 0 )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
		}
	}
	
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//

    private boolean m_bRenderingWick = false;
    
    private Icon[] m_iconByColor = new Icon[16];
    private Icon m_iconWick;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        m_iconByColor[0] = register.registerIcon( "fcBlockCandle_c00" );
        m_iconByColor[1] = register.registerIcon( "fcBlockCandle_c01" );
        m_iconByColor[2] = register.registerIcon( "fcBlockCandle_c02" );
        m_iconByColor[3] = register.registerIcon( "fcBlockCandle_c03" );
        m_iconByColor[4] = register.registerIcon( "fcBlockCandle_c04" );
        m_iconByColor[5] = register.registerIcon( "fcBlockCandle_c05" );
        m_iconByColor[6] = register.registerIcon( "fcBlockCandle_c06" );
        m_iconByColor[7] = register.registerIcon( "fcBlockCandle_c07" );
        m_iconByColor[8] = register.registerIcon( "fcBlockCandle_c08" );
        m_iconByColor[9] = register.registerIcon( "fcBlockCandle_c09" );
        m_iconByColor[10] = register.registerIcon( "fcBlockCandle_c10" );
        m_iconByColor[11] = register.registerIcon( "fcBlockCandle_c11" );
        m_iconByColor[12] = register.registerIcon( "fcBlockCandle_c12" );
        m_iconByColor[13] = register.registerIcon( "fcBlockCandle_c13" );
        m_iconByColor[14] = register.registerIcon( "fcBlockCandle_c14" );
        m_iconByColor[15] = register.registerIcon( "fcBlockCandle_c15" );
        
        m_iconWick = register.registerIcon( "fcBlockCandleWick" );
        
        blockIcon = m_iconByColor[0]; // black for hit effects
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_iconByColor[iMetadata];
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		if ( iSide == 0 )
		{
			if ( m_bRenderingWick )
			{
				return false;
			}
			else
			{
				return FCClientUtilsRender.ShouldRenderNeighborFullFaceSide( blockAccess,
					iNeighborI, iNeighborJ, iNeighborK, iSide );
			}
		}
		
		return true;
    }
	
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }
	
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand)
    {
        double xPos = i + 0.5D;
        double yPos = j + 0.1D + m_dStickHeight;
        double zPos = k + 0.5D;

        // tripple flame so that it's a little more steady
        
        world.spawnParticle( "fcsmallflame", xPos, yPos, zPos, 0D, 0D, 0D );
        world.spawnParticle( "fcsmallflame", xPos, yPos, zPos, 0D, 0D, 0D );
        world.spawnParticle( "fcsmallflame", xPos, yPos, zPos, 0D, 0D, 0D );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderStandardBlock( this, i, j, k );
    }
    
    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, 
    	boolean bFirstPassResult )
    {
    	if ( bFirstPassResult )
    	{
    		m_bRenderingWick = true;
    		
            renderBlocks.setRenderBounds( 
            	0.5D - m_dWickHalfWidth, m_dStickHeight, 0.5D - m_dWickHalfWidth, 
            	0.5D + m_dWickHalfWidth, m_dStickHeight + m_dWickHeight, 0.5D + m_dWickHalfWidth );
        
            FCClientUtilsRender.RenderBlockFullBrightWithTexture( renderBlocks, 
            	renderBlocks.blockAccess, i, j, k, m_iconWick );
            
    		m_bRenderingWick = false;
    	}
    }
}