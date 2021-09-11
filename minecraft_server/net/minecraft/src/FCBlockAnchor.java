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
}
