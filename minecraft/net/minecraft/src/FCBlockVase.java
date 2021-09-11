// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockVase extends BlockContainer
{
	public static final float m_fVaseBaseWidth = ( 8.0F / 16F );
	public static final float m_fVaseBaseHalfWidth = m_fVaseBaseWidth / 2.0F;
	public static final float m_fVaseBaseHeight = ( 1.0F / 16F );
	public static final float m_fVaseBodyWidth = ( 10.0F / 16F );
	public static final float m_fVaseBodyHalfWidth = m_fVaseBodyWidth / 2.0F;
	public static final float m_fVaseBodyHeight = ( 6.0F / 16F );
	public static final float m_fVaseNeckBaseWidth = ( 8.0F / 16F );
	public static final float m_fVaseNeckBaseHalfWidth = m_fVaseNeckBaseWidth / 2.0F;
	public static final float m_fVaseNeckBaseHeight = ( 1.0F / 16F );
	public static final float m_fVaseNeckWidth = ( 4.0F / 16F );
	public static final float m_fVaseNeckHalfWidth = m_fVaseNeckWidth / 2.0F;
	public static final float m_fVaseNeckHeight = ( 7.0F / 16F );
	public static final float m_fVaseTopWidth = ( 6.0F / 16F );
	public static final float m_fVaseTopHalfWidth = m_fVaseTopWidth / 2.0F;
	public static final float m_fVaseTopHeight = ( 1.0F / 16F );
	
    public FCBlockVase( int iBlockID )
    {
        super( iBlockID, Material.glass );  
        
        setHardness( 0F );
        
        SetBuoyancy( 1F );
        
    	InitBlockBounds( ( 0.5F - m_fVaseBodyHalfWidth ), 0F, ( 0.5F - m_fVaseBodyHalfWidth ), 
			( 0.5F + m_fVaseBodyHalfWidth ), 1F, ( 0.5F + m_fVaseBodyHalfWidth ) );        
        
        setStepSound( soundGlassFootstep );        
        
        setUnlocalizedName( "fcBlockVase" );
        
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
    public int quantityDropped(Random random)
    {
        return 0;
    }

	@Override
    public int damageDropped( int i )
    {
        return i;
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityVase();
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	ItemStack playerEquippedItem = player.getCurrentEquippedItem();
    	
        if ( world.isRemote )
        {
            return true;
        } 
        else
        {
        	if ( playerEquippedItem != null )
        	{
        		if ( playerEquippedItem.stackSize > 0 )
        		{
                    FCTileEntityVase tileEntityVase = (FCTileEntityVase)world.getBlockTileEntity( i, j, k );
                    
                    int iTempStackSize = playerEquippedItem.stackSize;
        		
                    if ( FCUtilsInventory.AddItemStackToInventory( tileEntityVase, playerEquippedItem ) )
                    {
                    	player.destroyCurrentEquippedItem();
                    	
			            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
			            		"random.pop", 0.25F, 
			            		((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			            
                    	return true;
                	}
                    else if ( playerEquippedItem.stackSize < iTempStackSize )
                    {
			            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
			            		"random.pop", 0.25F, 
			            		((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			            
                        return true;                    	
                    }                    
        		}
        	}
        }
        
    	return false;
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	FCTileEntityVase tileEntity = (FCTileEntityVase)( world.getBlockTileEntity(i, j, k) );
    	
    	if ( tileEntity != null )
    	{
    		FCUtilsInventory.EjectInventoryContents( world, i, j, k, (IInventory)tileEntity );    		
    	}

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }

	@Override
    protected boolean canSilkHarvest()
    {
		return true;
    }

	@Override
    public void onBlockHarvested( World world, int i, int j, int k, int iMetadata, EntityPlayer player )
    {
		if ( !world.isRemote && !EnchantmentHelper.getSilkTouchModifier( player ) )
		{
			CheckForExplosion( world, i, j, k );
		}    	
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	// base 
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( ( 0.5F - m_fVaseBaseHalfWidth ), 0.0F, ( 0.5F - m_fVaseBaseHalfWidth ), 
        		( 0.5F + m_fVaseBaseHalfWidth ), m_fVaseBaseHeight, ( 0.5F + m_fVaseBaseHalfWidth ) );
        
    	// body 
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( ( 0.5F - m_fVaseBodyHalfWidth ), m_fVaseBaseHeight, ( 0.5F - m_fVaseBodyHalfWidth ), 
        		( 0.5F + m_fVaseBodyHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight, ( 0.5F + m_fVaseBodyHalfWidth ) );
        
    	// neck base 
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( ( 0.5F - m_fVaseNeckBaseHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight, ( 0.5F - m_fVaseNeckBaseHalfWidth ), 
        		( 0.5F + m_fVaseNeckBaseHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight + m_fVaseNeckBaseHeight, ( 0.5F + m_fVaseNeckBaseHalfWidth ) );
        
        // neck 
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( ( 0.5F - m_fVaseNeckHalfWidth ), 1.0F - ( m_fVaseTopHeight + m_fVaseNeckHeight ), ( 0.5F - m_fVaseNeckHalfWidth ), 
        		( 0.5F + m_fVaseNeckHalfWidth ), 1.0F - m_fVaseTopHeight, ( 0.5F + m_fVaseNeckHalfWidth ) );
        
    	// top 
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( ( 0.5F - m_fVaseTopHalfWidth ), 1.0F - m_fVaseTopHeight, ( 0.5F - m_fVaseTopHalfWidth ), 
        		( 0.5F + m_fVaseTopHalfWidth ), 1.0F, ( 0.5F + m_fVaseTopHalfWidth ) );
        
    	return rayTrace.GetFirstIntersection();    	
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	return iFacing == 1;
	}
    
    @Override
    public void OnArrowImpact( World world, int i, int j, int k, EntityArrow arrow )
    {
    	if ( !world.isRemote )
    	{
    		BreakVase( world, i, j, k );
    	}
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
	
	public void BreakVase( World world, int i, int j, int k )
	{
        world.playAuxSFX( 2001, i, j, k, blockID );
        
        CheckForExplosion( world, i, j, k );
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	private boolean CheckForExplosion( World world, int i, int j, int k )
	{
		// returns true if an explosion occurs
		
    	FCTileEntityVase tileEntity = (FCTileEntityVase)( world.getBlockTileEntity(i, j, k) );
    	
    	if ( tileEntity != null )
    	{
    		IInventory inventory = (IInventory)tileEntity;
    		
    		if ( FCUtilsInventory.GetFirstOccupiedStackOfItem( inventory, FCBetterThanWolves.fcItemBlastingOil.itemID ) >= 0 )
    		{
    			FCUtilsInventory.ClearInventoryContents( inventory );
    			
    	        world.createExplosion( null, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 1.5F, true );
    	        
    	        return true;
    		}
    	}
    	
    	return false;
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconByColor = new Icon[16];
    
	@Override
    public void registerIcons( IconRegister register )
    {
        m_IconByColor[0] = register.registerIcon( "fcBlockVase_c00" );
        m_IconByColor[1] = register.registerIcon( "fcBlockVase_c01" );
        m_IconByColor[2] = register.registerIcon( "fcBlockVase_c02" );
        m_IconByColor[3] = register.registerIcon( "fcBlockVase_c03" );
        m_IconByColor[4] = register.registerIcon( "fcBlockVase_c04" );
        m_IconByColor[5] = register.registerIcon( "fcBlockVase_c05" );
        m_IconByColor[6] = register.registerIcon( "fcBlockVase_c06" );
        m_IconByColor[7] = register.registerIcon( "fcBlockVase_c07" );
        m_IconByColor[8] = register.registerIcon( "fcBlockVase_c08" );
        m_IconByColor[9] = register.registerIcon( "fcBlockVase_c09" );
        m_IconByColor[10] = register.registerIcon( "fcBlockVase_c10" );
        m_IconByColor[11] = register.registerIcon( "fcBlockVase_c11" );
        m_IconByColor[12] = register.registerIcon( "fcBlockVase_c12" );
        m_IconByColor[13] = register.registerIcon( "fcBlockVase_c13" );
        m_IconByColor[14] = register.registerIcon( "fcBlockVase_c14" );
        m_IconByColor[15] = register.registerIcon( "fcBlockVase_c15" );
        
        blockIcon = m_IconByColor[0]; // white for hit effects
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		// FCTODO: This is bad.  Clean it up.
    	// method largly copied from block cloth
		
    	if ( iMetadata != 0 )
    	{
    		iMetadata = 1 + ((~iMetadata) & 0xf);
    	}
    	
		return m_IconByColor[iMetadata];
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	return RenderVaseBlock( renderBlocks, renderBlocks.blockAccess, i, j, k, this );    	
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	RenderInvBlock( renderBlocks, this, iItemDamage );
    }
    
    static public boolean RenderVaseBlock
    (
    	RenderBlocks renderBlocks,
    	IBlockAccess blockAcces,
    	int i, int j, int k,
    	Block block
	)
    {
		// seperate static function so that this can be called externally by unfired pottery
		
    	// render base 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseBaseHalfWidth ), 0.0F, ( 0.5F - m_fVaseBaseHalfWidth ), 
        		( 0.5F + m_fVaseBaseHalfWidth ), m_fVaseBaseHeight, ( 0.5F + m_fVaseBaseHalfWidth ) );
        
        renderBlocks.renderStandardBlock( block, i, j, k );
        
    	// render body 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseBodyHalfWidth ), m_fVaseBaseHeight, ( 0.5F - m_fVaseBodyHalfWidth ), 
        		( 0.5F + m_fVaseBodyHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight, ( 0.5F + m_fVaseBodyHalfWidth ) );
        
        renderBlocks.renderStandardBlock( block, i, j, k );
        
    	// render neck base 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseNeckBaseHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight, ( 0.5F - m_fVaseNeckBaseHalfWidth ), 
        		( 0.5F + m_fVaseNeckBaseHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight + m_fVaseNeckBaseHeight, ( 0.5F + m_fVaseNeckBaseHalfWidth ) );
        
        renderBlocks.renderStandardBlock( block, i, j, k );
        
    	// render neck 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseNeckHalfWidth ), 1.0F - ( m_fVaseTopHeight + m_fVaseNeckHeight ), ( 0.5F - m_fVaseNeckHalfWidth ), 
        		( 0.5F + m_fVaseNeckHalfWidth ), 1.0F - m_fVaseTopHeight, ( 0.5F + m_fVaseNeckHalfWidth ) );
        
        renderBlocks.renderStandardBlock( block, i, j, k );
        
    	// render top 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseTopHalfWidth ), 1.0F - m_fVaseTopHeight, ( 0.5F - m_fVaseTopHalfWidth ), 
        		( 0.5F + m_fVaseTopHalfWidth ), 1.0F, ( 0.5F + m_fVaseTopHalfWidth ) );
        
        renderBlocks.renderStandardBlock( block, i, j, k );
        
    	return true;
    }
    
    static public void RenderInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block, 
		int iItemDamage 
	)
    {
		// seperate static function so that this can be called externally by unfired pottery
		
    	// render base 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseBaseHalfWidth ), 0.0F, ( 0.5F - m_fVaseBaseHalfWidth ), 
        		( 0.5F + m_fVaseBaseHalfWidth ), m_fVaseBaseHeight, ( 0.5F + m_fVaseBaseHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage );    
        
    	// render body 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseBodyHalfWidth ), m_fVaseBaseHeight, ( 0.5F - m_fVaseBodyHalfWidth ), 
        		( 0.5F + m_fVaseBodyHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight, ( 0.5F + m_fVaseBodyHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage );    
        
    	// render neck base 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseNeckBaseHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight, ( 0.5F - m_fVaseNeckBaseHalfWidth ), 
        		( 0.5F + m_fVaseNeckBaseHalfWidth ), m_fVaseBaseHeight + m_fVaseBodyHeight + m_fVaseNeckBaseHeight, ( 0.5F + m_fVaseNeckBaseHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage );    
        
        // render neck 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseNeckHalfWidth ), 1.0F - ( m_fVaseTopHeight + m_fVaseNeckHeight ), ( 0.5F - m_fVaseNeckHalfWidth ), 
        		( 0.5F + m_fVaseNeckHalfWidth ), 1.0F - m_fVaseTopHeight, ( 0.5F + m_fVaseNeckHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage );    
        
    	// render top 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fVaseTopHalfWidth ), 1.0F - m_fVaseTopHeight, ( 0.5F - m_fVaseTopHalfWidth ), 
        		( 0.5F + m_fVaseTopHalfWidth ), 1.0F, ( 0.5F + m_fVaseTopHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage );
   }
}