// FCMOD

package net.minecraft.src;

public class FCBlockHamper extends FCBlockBasket
{
	public static final FCModelBlockHamper m_model = new FCModelBlockHamper();
	
    protected FCBlockHamper( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockHamper" );
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityHamper();
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote)
        {
        	if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, i, j + 1, k, 0, true ) && 
        		!FCUtilsWorld.IsBlockRestingOnThatBelow( world, i, j + 1, k ) )
    		{
	            FCTileEntityHamper tileEntity = (FCTileEntityHamper)world.getBlockTileEntity( i, j, k );
	            
	        	if ( player instanceof EntityPlayerMP ) // should always be true
	        	{
	        		FCContainerHamper container = new FCContainerHamper( player.inventory, tileEntity );
	        		
	        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcHamperContainerID );        		
	        	}
    		}
        }
        
		return true;
    }
	
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing == 0 || ( iFacing == 1 && !GetIsOpen( blockAccess, i, j, k ) );
	}
    
    @Override
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	if ( !world.isRemote )
    	{
    		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemWickerPiece.itemID, 2, 0, 0.75F );
    	}
    }
    
    @Override
	public FCModelBlock GetLidModel( int iMetadata )
    {
		return m_model.m_lid;
    }
	
    @Override
	public Vec3 GetLidRotationPoint()
	{
    	return m_model.GetLidRotationPoint();
	}
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
	
    private boolean m_bRenderingBase = false;
    
    private Icon m_iconBaseOpenTop;
    private Icon m_iconFront;
    private Icon m_iconTop;
    private Icon m_iconBottom;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_iconBaseOpenTop = register.registerIcon( "fcBlockHamper_open_top" );
		m_iconFront = register.registerIcon( "fcBlockHamper_front" );
		m_iconTop = register.registerIcon( "fcBlockHamper_top" );
		m_iconBottom = register.registerIcon( "fcBlockHamper_bottom" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iSide == 1 )
		{
			if ( m_bRenderingBase )
			{
				return m_iconBaseOpenTop;
			}
			else
			{
				return m_iconTop;
			}
		}
		else if ( iSide == 0 )
		{
			return m_iconBottom;
		}
		else
		{				
			int iFacing = this.GetFacing( iMetadata );
			
			if ( iSide == iFacing )
			{
				return m_iconFront;
			}
		}
		
		return super.getIcon( iSide, iMetadata );
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {    	
    	int iMetadata = renderBlocks.blockAccess.getBlockMetadata( i, j, k );
    	
		int iFacing = GetFacing( iMetadata );
		
    	FCModelBlock transformedModel = m_model.MakeTemporaryCopy();
    	
		transformedModel.RotateAroundJToFacing( iFacing );
		
		renderBlocks.SetUvRotateTop( ConvertFacingToTopTextureRotation( iFacing ) );
		renderBlocks.SetUvRotateBottom( ConvertFacingToBottomTextureRotation( iFacing ) );

		m_bRenderingBase = true;
		
		boolean bReturnValue = transformedModel.RenderAsBlock( renderBlocks, this, i, j, k );
		
		m_bRenderingBase = false;
		
		if ( !GetIsOpen( iMetadata ) )
		{
			transformedModel = m_model.m_lid.MakeTemporaryCopy();
			
			transformedModel.RotateAroundJToFacing( iFacing );
			
			transformedModel.RenderAsBlock( renderBlocks, this, i, j, k );
		}
    	
		renderBlocks.ClearUvRotation();
		
		return false;
    }
		
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	m_model.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    	m_model.m_lid.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    }
    
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		AxisAlignedBB transformedBox;
		
		if ( GetIsOpen( iMetadata ) )
		{
			transformedBox = m_model.m_selectionBoxOpen.MakeTemporaryCopy();
		}
		else
		{
			transformedBox = m_model.m_selectionBox.MakeTemporaryCopy();
		}
		
		transformedBox.RotateAroundJToFacing( GetFacing( iMetadata ) );
		
		transformedBox.offset( i, j, k );
		
		return transformedBox;		
    }
}
