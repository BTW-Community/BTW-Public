// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockMiningCharge extends Block
{
	public final static double m_dBoundingBoxHeight = 0.5D;
	
	private final static int m_iTickRate = 1;
	
    public FCBlockMiningCharge( int iBlockID )
    {
        super( iBlockID, Material.tnt );  
        
        setHardness( 0F );
        
		SetFireProperties( FCEnumFlammability.EXPLOSIVES );
		
    	InitBlockBounds( 0D, 0D, 0D, 1D, m_dBoundingBoxHeight, 1D );
    	
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "fcBlockMiningCharge" );
        
        setTickRandomly( true );        
		
		setCreativeTab( CreativeTabs.tabRedstone );		
    }
    
	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
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
    public int quantityDropped( Random random )
    {
    	// this is to override standard drop behavior since the block may or may not ignite
    	// depending on how it is destroyed
		
        return 0;
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
		for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
		{
			if ( this.IsValidAnchorToFacing( world, i, j, k, iTempFacing ) )
			{
				return true;
			}
		}
		
        return false;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
    	super.onBlockAdded( world, i, j, k );
    	
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) ); 
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        iFacing = Block.GetOppositeFacing( iFacing );

        if ( !IsValidAnchorToFacing( world, i, j, k, iFacing ) )
        {
    		iFacing = 0;
    	
    		for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
    		{
    			if ( this.IsValidAnchorToFacing( world, i, j, k, iTempFacing ) )
    			{
    				iFacing = iTempFacing;
    				
    				break;
    			}
    		}    		
        }
        
    	return SetFacing( iMetadata, iFacing );
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        switch ( iFacing )
        {
	        case 0:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, 1D, m_dBoundingBoxHeight, 1D );
	        	
	        case 1:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, m_dBoundingBoxHeight, 0D, 1D, 1D, 1D );
	        	
	        case 2:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, 1D, 1D, m_dBoundingBoxHeight );
	        	
	        case 3:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, m_dBoundingBoxHeight, 1D, 1D, 1D );
	        	
	        case 4:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, m_dBoundingBoxHeight, 1D, 1D );
	        	
	        default: // 5
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		m_dBoundingBoxHeight, 0D, 0D, 1D, 1D, 1D );
        }    	
    }
    
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID )
    {
    	if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
    	{
    		world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	}
    }

	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		if ( world.isRemote )
		{
			return;
		}
		
    	int iFacing = GetFacing( world, i, j, k );
    	
    	FCEntityMiningCharge entityMiningCharge = CreatePrimedEntity( world, i, j, k, iFacing );
    	
    	entityMiningCharge.m_iFuse = 1;    
    }

	@Override
    public void onBlockDestroyedByPlayer( World world, int i, int j, int k, int iMetaData )
    {
        if ( world.isRemote )
        {
            return;
        }
        
        dropBlockAsItem_do( world, i, j, k, new ItemStack( 
    		FCBetterThanWolves.fcBlockMiningCharge.blockID, 1, 0 ) );            
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	ItemStack playerEquippedItem = player.getCurrentEquippedItem();
    	
        if ( playerEquippedItem != null && 
    		playerEquippedItem.itemID == Item.flintAndSteel.itemID)
        {
        	if ( !world.isRemote )
        	{
	        	int iMetaData = world.getBlockMetadata( i, j, k );
	        	
	        	world.setBlockWithNotify( i, j, k, 0 );
	        	
	        	CreatePrimedEntity( world, i, j, k, iMetaData );
        	}
        	
        	playerEquippedItem.damageItem( 1, player );
            
            return true;
        }
        else
        {        
        	return super.onBlockActivated( world, i, j, k, player, iFacing, fXClick, fYClick, fZClick );
        }
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
        if( IsGettingRedstonePower( world, i, j, k ) )
        {
        	int iMetaData = world.getBlockMetadata( i, j, k );
        	
            world.setBlockWithNotify( i, j, k, 0 );
            
            CreatePrimedEntity( world, i, j, k, iMetaData );        
        }        
        else if ( !IsValidAnchorToFacing( world, i, j, k, GetFacing( world, i, j, k ) ) )
		{
            world.setBlockWithNotify( i, j, k, 0 );
            
            dropBlockAsItem_do( world, i, j, k, new ItemStack( 
            		FCBetterThanWolves.fcBlockMiningCharge.blockID, 1, 0 ) );            
		}
    }
    
	@Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
		CreatePrimedEntity( world, i, j, k, world.getBlockMetadata( i, j, k ) );
		
		super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
    }
	
    //------------- FCIBlock ------------//
    
	@Override
	public int GetFacing( int iMetadata )
	{
    	return iMetadata & 7;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata &= (~7);	// filter out old facing
    	
    	iMetadata |= iFacing;
    	
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		int iFacing = GetFacing( iBlockAccess, i, j, k );
		
		if ( iFacing != 1  )
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		if ( super.RotateAroundJAxis( world, i, j, k, bReverse ) )
		{
	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	    	
	    	return true;
		}
		
		return false;
	}

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	
    	return true;
	}
	
	//------------- Class Specific Methods ------------//
    
    public boolean IsGettingRedstonePower( World world, int i, int j, int k )
    {
    	return world.isBlockIndirectlyGettingPowered( i, j, k );
    }
    
    public boolean IsValidAnchorToFacing( World world, int i, int j, int k, int iFacing )
    {
        FCUtilsBlockPos anchorBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );

        return FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world,
        	anchorBlockPos.i, anchorBlockPos.j, anchorBlockPos.k, GetOppositeFacing( iFacing ), true );
    }
    
    static public FCEntityMiningCharge CreatePrimedEntity( World world, int i, int j, int k, int iMetaData )
    {
        FCEntityMiningCharge entityMiningCharge = new FCEntityMiningCharge( 
    		world, i, j, k, FCBetterThanWolves.fcBlockMiningCharge.GetFacing( iMetaData ) );
            
        world.spawnEntityInWorld( entityMiningCharge );
        world.playSoundAtEntity( entityMiningCharge, "random.fuse", 1.0F, 1.0F );
        
        return entityMiningCharge;
    }
    
	//----------- Client Side Functionality -----------//
    
    private Icon m_IconBottom;
    private Icon m_IconTop;
    private Icon m_IconSide;
    private Icon m_IconSideFlipped;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        m_IconBottom = register.registerIcon( "fcBlockMiningCharge_bottom" );
        m_IconTop = register.registerIcon( "fcBlockMiningCharge_top" );
        m_IconSide = register.registerIcon( "fcBlockMiningCharge_side" );
        m_IconSideFlipped = register.registerIcon( "fcBlockMiningCharge_side_vert" );
        
        blockIcon = m_IconSide; // for hit effects
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	// for inv render
    	
    	if ( iSide <= 3 )
    	{
    		return m_IconSideFlipped;    	
    	}
    	else if ( iSide == 4 )
    	{
    		return m_IconTop;    	
    	}
    	else // 5
    	{
    		return m_IconBottom;    	
    	}
    }
	
	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	int iFacing = GetFacing( blockAccess, i, j, k );
    	
    	if ( iFacing <= 1 )
    	{
        	if ( iSide <= 3 )
        	{
        		return m_IconSideFlipped;    	
        	}
        	else if ( iSide == 4 )
        	{
        		return m_IconTop;    	
        	}
        	else // 5
        	{
        		return m_IconBottom;    	
        	}        
    	}
    	else
    	{
        	if ( iSide == 0 )
        	{
        		return m_IconBottom;    	
    		}
        	else if ( iSide == 1 )
        	{
        		return m_IconTop;    	
        	}
        	else
        	{        	
        		return m_IconSide;
        	}
    	}    	
    }
	
    public Icon getBlockTextureFromMetadataCustom( int iSide, int iMetadata )
    {
    	// used by entity render
    	
    	int iFacing = GetFacing( iMetadata );
    	
    	if ( iFacing <= 1 )
    	{
        	if ( iSide <= 3 )
        	{
        		return m_IconSideFlipped;    	
        	}
        	else if ( iSide == 4 )
        	{
        		return m_IconTop;    	
        	}
        	else // 5
        	{
        		return m_IconBottom;    	
        	}        
    	}
    	else
    	{
        	if ( iSide == 0 )
        	{
        		return m_IconBottom;    	
    		}
        	else if ( iSide == 1 )
        	{
        		return m_IconTop;    	
        	}
        	else
        	{        	
        		return m_IconSide;
        	}
    	}    	
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
}