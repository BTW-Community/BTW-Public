// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockInfernalEnchanter extends BlockContainer
{
    public static final float m_fBlockHeight = 0.50F;
    public static final float m_fCandleHeight = 0.25F;

	private static final float m_fBlockHardness = 100F;
	private static final float m_fBlockExplosionResistance = 2000F;
	
	private static final int m_iHorizontalBookShelfCheckDistance = 8;
	private static final int m_iVerticalPositiveBookShelfCheckDistance = 8;
	private static final int m_iVerticalNegativeBookShelfCheckDistance = 8;
	
    protected FCBlockInfernalEnchanter( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialSoulforgedSteel );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, m_fBlockHeight, 1F );
        
        setLightOpacity( 0 );
        
        setHardness( m_fBlockHardness );
        setResistance( m_fBlockExplosionResistance );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "fcBlockInfernalEnchanter" );
        
        setCreativeTab( CreativeTabs.tabDecorations );
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityInfernalEnchanter();
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote )
        {
        	if ( player instanceof EntityPlayerMP ) // should always be true
        	{
        		FCContainerInfernalEnchanter container = new FCContainerInfernalEnchanter( player.inventory, world, i, j, k );
        		
        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcInfernalEnchanterContainerID );        		
        	}
        }
        
        return true;
    }
    
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    //------------- Class Specific Methods ------------//

	private boolean IsValidBookshelf( World world, int i, int j, int k )
	{
		int iBlockID = world.getBlockId( i, j, k );
		
		if ( iBlockID == Block.bookShelf.blockID )
		{
			// check around the bookshelf for an empty block to provide access
			
			if ( world.isAirBlock( i + 1, j, k ) ||
				world.isAirBlock( i - 1, j, k ) ||
				world.isAirBlock( i, j, k + 1 ) ||
				world.isAirBlock( i, j, k - 1 ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconBySideArray = new Icon[6];
    
    private Icon m_IconCandle;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        Icon bottomIcon = register.registerIcon( "fcBlockInfernalEnchanter_bottom" );
        
        blockIcon = bottomIcon; // for hit effects
        
        m_IconBySideArray[0] = bottomIcon;
        m_IconBySideArray[1] = register.registerIcon( "fcBlockInfernalEnchanter_top" );
        
        Icon sideIcon = register.registerIcon( "fcBlockInfernalEnchanter_side" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
        
        m_IconCandle = register.registerIcon( "fcBlockCandle_c00" );        
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_IconBySideArray[iSide];
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
        super.randomDisplayTick( world, i, j, k, random );
        
        DisplayMagicLetters( world, i, j, k, random );    	
    }
	
    private void DisplayMagicLetters( World world, int i, int j, int k, Random rand )
    {
    	TileEntity tileEntity = world.getBlockTileEntity( i, j, k );
    	
    	if ( tileEntity != null && tileEntity instanceof FCTileEntityInfernalEnchanter )
    	{
    		FCTileEntityInfernalEnchanter enchanterEntity = (FCTileEntityInfernalEnchanter)tileEntity;
    		
    		if ( enchanterEntity.m_bPlayerNear )
    		{
		        for ( int iTempCount = 0; iTempCount < 64; iTempCount++ )
		        {
		        	int iTargetI = rand.nextInt( m_iHorizontalBookShelfCheckDistance * 2 + 1 ) - m_iHorizontalBookShelfCheckDistance + i;
		        	
		        	int iTargetJ= rand.nextInt( m_iVerticalPositiveBookShelfCheckDistance + m_iVerticalNegativeBookShelfCheckDistance + 1 ) - 
		        		m_iVerticalNegativeBookShelfCheckDistance + j;
		        	
		        	int iTargetK = rand.nextInt( m_iHorizontalBookShelfCheckDistance * 2 + 1 ) - m_iHorizontalBookShelfCheckDistance + k; 
		        	
		            if ( IsValidBookshelf( world, iTargetI, iTargetJ, iTargetK ) )
		            {
		            	Vec3 velocity = Vec3.createVectorHelper( (double)( iTargetI - i  ), (double)( iTargetJ - j ), (double)( iTargetK - k ) );
		            	
		            	// oddly, the following specifies the dest of the particles, not the origins
		            	
			            world.spawnParticle( "enchantmenttable", (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		            		velocity.xCoord, velocity.yCoord, velocity.zCoord );
		            }
		        }
    		}
    	}
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
    	// render base
    	
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
        renderer.renderStandardBlock( this, i, j, k );

        // render candles
        
        renderer.setRenderBounds( ( 1.0F / 16.0F ), m_fBlockHeight, ( 1.0F / 16.0F ), ( 3.0F / 16.0F ), m_fBlockHeight + m_fCandleHeight, ( 3.0F / 16.0F ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, m_IconCandle );        
        
        renderer.setRenderBounds( ( 13.0F / 16.0F ), m_fBlockHeight, ( 1.0F / 16.0F ), ( 15.0F / 16.0F ), m_fBlockHeight + m_fCandleHeight, ( 3.0F / 16.0F ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, m_IconCandle );
        
        renderer.setRenderBounds( ( 1.0F / 16.0F ), m_fBlockHeight, ( 13.0F / 16.0F ), ( 3.0F / 16.0F ), m_fBlockHeight + m_fCandleHeight, ( 15.0F / 16.0F ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, m_IconCandle );
        
        renderer.setRenderBounds( ( 13.0F / 16.0F ), m_fBlockHeight, ( 13.0F / 16.0F ), ( 15.0F / 16.0F ), m_fBlockHeight + m_fCandleHeight, ( 15.0F / 16.0F ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, m_IconCandle );        
        
    	return true;
    }    
}