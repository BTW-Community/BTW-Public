// FCMOD

package net.minecraft.src;

public class FCBlockAnchor extends Block
{
	public static double m_dAnchorBaseHeight = ( 6D / 16D );
	
	protected FCBlockAnchor( int iBlockID )
	{
        super( iBlockID, Material.rock );

        setHardness( 2F );
        
    	InitBlockBounds( 0D, 0D, 0D, 1D, m_dAnchorBaseHeight, 1D );
    	
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockAnchor" );        
        
        setCreativeTab( CreativeTabs.tabTransport );
	}
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
    	// this is necessary because the rendering code changes the block bounds
    	
        switch ( iFacing )
        {
	        case 0:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 1D - m_dAnchorBaseHeight, 0D, 
            		1D, 1D, 1D );
	        	
	        case 1:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, 
            		1D, m_dAnchorBaseHeight, 1D );
	        	
	        case 2:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 1D - m_dAnchorBaseHeight, 
            		1D, 1D, 1D );
	        	
	        case 3:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, 
            		1D, 1D, m_dAnchorBaseHeight );
	        	
	        case 4:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		1D - m_dAnchorBaseHeight, 0D, 0D, 
            		1D, 1D, 1D );
	        	
	        default: // 5
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, 
            		m_dAnchorBaseHeight, 1D, 1D );
        }    	
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
        int iAnchorFacing = iFacing;
        
        return SetFacing( iMetadata, iAnchorFacing );
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	ItemStack playerEquippedItem = player.getCurrentEquippedItem();
    	
    	if ( playerEquippedItem != null )
    	{
			return false;
    	}
    	
		RetractRope( world, i, j, k, player );
		
		return true;    	
    }    

	@Override
	public int GetFacing( int iMetadata )
	{
    	return iMetadata;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		return iFacing;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{		
		int iFacing = GetFacing( blockAccess, i, j, k );	

		return iFacing != 0;
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
        return true;
	}
	
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return Block.GetOppositeFacing( iFacing ) == GetFacing( blockAccess, i, j, k );		
	}
    
    //------------- Class Specific Methods ------------//
    
	void RetractRope( World world, int i, int j, int k, EntityPlayer entityPlayer )
	{
    	// scan downward towards bottom of rope
    	
    	for ( int tempj = j - 1; tempj >= 0; tempj-- )
    	{
    		int iTempBlockID = world.getBlockId( i, tempj, k );
    		
    		if ( iTempBlockID == FCBetterThanWolves.fcRopeBlock.blockID )
    		{
        		if ( world.getBlockId( i, tempj - 1, k ) != FCBetterThanWolves.fcRopeBlock.blockID )
        		{
        			// we've found the bottom of the rope
        			
                    AddRopeToPlayerInventory( world, i, j, k, entityPlayer );
                    
                    Block targetBlock = FCBetterThanWolves.fcRopeBlock;
                    
                    if ( !world.isRemote )
                    {    		        
	                    // destroy the block
	                    
	    		        world.playAuxSFX( 2001, i, j, k, iTempBlockID );
	    		        
	                    world.setBlockWithNotify( i, tempj, k, 0 );
                    }                    
                    
                    break;
        		}
    		}
    		else
    		{
    			break;
    		}
    	}        
	}
	
	private void AddRopeToPlayerInventory( World world, int i, int j, int k, EntityPlayer entityPlayer )
	{
		ItemStack ropeStack = new ItemStack( FCBetterThanWolves.fcItemRope );
		
		if ( entityPlayer.inventory.addItemStackToInventory( ropeStack ) )
		{
            world.playSoundAtEntity( entityPlayer, "random.pop", 0.2F, 
        		( ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.7F + 1F ) * 2F);
		}
		else
		{
			FCUtilsItem.EjectStackWithRandomOffset( world, i, j, k, ropeStack );
		}
	}
	
    /*
     * return true if the associated anchor is converted into an entity
     */
	public boolean NotifyAnchorBlockOfAttachedPulleyStateChange( FCTileEntityPulley tileEntityPulley, 
		World world, int i, int j, int k )
	{
		int iMovementDirection = 0;
		
		if ( tileEntityPulley.IsRaising() )
		{
			if ( world.getBlockId( i, j + 1, k ) == FCBetterThanWolves.fcRopeBlock.blockID )
			{
				iMovementDirection = 1;
			}
		}
		else if ( tileEntityPulley.IsLowering() )
		{
			if ( world.isAirBlock( i, j - 1, k )  || 
				world.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcPlatform.blockID )
			{
				iMovementDirection = -1;
			}
		}
		
		if ( iMovementDirection != 0 )
		{
			ConvertAnchorToEntity( world, i, j, k, tileEntityPulley, iMovementDirection );
			
			return true;
		}
		
		return false;
	}
	
	private void ConvertAnchorToEntity( World world, int i, int j, int k, FCTileEntityPulley attachedTileEntityPulley, int iMovementDirection )
	{
		FCUtilsBlockPos pulleyPos = new FCUtilsBlockPos( attachedTileEntityPulley.xCoord, 
				attachedTileEntityPulley.yCoord, attachedTileEntityPulley.zCoord );
		
		FCEntityMovingAnchor entityAnchor = new FCEntityMovingAnchor( world, 
	    		(float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 
	    		pulleyPos, iMovementDirection ); 
				
        world.spawnEntityInWorld( entityAnchor );
        
        ConvertConnectedPlatformsToEntities( world, i, j, k, entityAnchor );
        
        world.setBlockWithNotify( i, j, k, 0 );
	}
	
    private void ConvertConnectedPlatformsToEntities( World world, int i, int j, int k, FCEntityMovingAnchor associatedAnchorEntity )
    {
    	int iTargetJ = j - 1;
    	
    	int iTargetBlockID = world.getBlockId( i, iTargetJ, k );
    	
    	if ( iTargetBlockID == FCBetterThanWolves.fcPlatform.blockID )
    	{
    		( (FCBlockPlatform)FCBetterThanWolves.fcPlatform ).CovertToEntitiesFromThisPlatform( 
				world, i, iTargetJ, k, associatedAnchorEntity );
    	}
    }
    
	//----------- Client Side Functionality -----------//
	
    private Icon m_IconFront;
    public Icon m_IconNub;
    private Icon m_IconRope;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_IconFront = register.registerIcon( "fcBlockAnchor_front" );
        m_IconNub = register.registerIcon( "fcBlockAnchor_nub" );
        m_IconRope = register.registerIcon( "fcBlockRope" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	if ( iSide < 2 )
    	{
    		return m_IconFront;
    	}
    	
        return blockIcon;
    }

	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing = blockAccess.getBlockMetadata( i, j, k );
        
    	if ( iSide == iFacing || iSide == Block.GetOppositeFacing( iFacing ) )
    	{
    		return m_IconFront;
    	}
    	
        return blockIcon;
    }
	
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
        // render the base
        
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
        renderer.renderStandardBlock( this, i, j, k );
        
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        // render the loop

        double dHalfLength = 0.125F;
        double dHalfWidth = 0.125F;
        
        double dBlockHeight = 0.25F;
        
        renderer.setRenderBounds( 
        	0.5D - dHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight, 0.5D - dHalfLength, 
    		0.5D + dHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight + dBlockHeight, 0.5D + dHalfLength );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, m_IconNub );

        boolean bRenderRope = false;
        
        dHalfLength = FCBlockRope.m_dRopeWidth * 0.5F;
        dHalfWidth = FCBlockRope.m_dRopeWidth * 0.5F;
        dBlockHeight = m_dAnchorBaseHeight;
        
        if ( iFacing == 1 ) // facing up
        {
        	int iBlockAboveId = blockAccess.getBlockId( i, j + 1, k );
        	
	        if ( iBlockAboveId == FCBetterThanWolves.fcRopeBlock.blockID ||
        		iBlockAboveId == FCBetterThanWolves.fcPulley.blockID )
	        {
	        	renderer.setRenderBounds( 0.5F - dHalfWidth, dBlockHeight, 0.5F - dHalfLength, 
	        		0.5F + dHalfWidth, 1.0F, 0.5F + dHalfLength );
	            
	            bRenderRope = true;
	        }
        }
        else
        {
	        // if there is rope below, we need to render the connecting piece
	        
	        if ( blockAccess.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcRopeBlock.blockID )
	        {
	        	renderer.setRenderBounds( 0.5F - dHalfWidth, 0.0F, 0.5F - dHalfLength, 
	        		0.5F + dHalfWidth, dBlockHeight, 0.5F + dHalfLength );
	            
	            bRenderRope = true;	            
	        }
        }
        
        if ( bRenderRope )
        {
            FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, m_IconRope );        	
        }
        
        return true;        
    }

    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
        // draw base
        
    	renderBlocks.setRenderBounds( 0, 0, 0, 1, m_dAnchorBaseHeight, 1 );		        
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, -0.5F, -0.25F, -0.5F, 1 );         
        
        // draw anchor point
        
        float fHalfLength = 0.125F;
        float fHalfWidth = 0.125F;
        float fBlockHeight = 0.25F;
        
        renderBlocks.setRenderBounds( 0.5F - fHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight, 0.5F - fHalfLength, 
    		0.5F + fHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight + fBlockHeight, 0.5F + fHalfLength );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, this, -0.5F, -0.25F, -0.5F, m_IconNub );
    }
}
