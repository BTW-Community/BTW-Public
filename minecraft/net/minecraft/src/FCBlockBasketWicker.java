// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBasketWicker extends FCBlockBasket
{
	private static final float m_fBasketOpenHeight = 0.75F;	
	
	private static final float m_fBasketHeight = 0.5F;
	private static final float m_fBasketRimWidth = ( 1F / 16F );
	private static final float m_fBasketWidthLip = ( 0F / 16F );
	private static final float m_fBasketDepthLip = ( 1F / 16F );
	
	private static final float m_fBasketLidHeight = ( 2F / 16F );
	private static final float m_fBasketLidLayerHeight = ( 1F / 16F );
	private static final float m_fBasketLidLayerWidthGap = ( 1F / 16F );
	
	private static final float m_fBasketHandleHeight = ( 1F / 16F );
	private static final float m_fBasketHandleWidth = ( 2F / 16F );
	private static final float m_fBasketHandleHalfWidth = ( m_fBasketHandleWidth / 2F );
	
	private static final float m_fBasketHandleLength = ( 4F / 16F );
	private static final float m_fBasketHandleHalfLength = ( m_fBasketHandleLength / 2F );
	
	private static final float m_fBasketInteriorWallThickness = ( 1F / 16F );
	private static final float m_fMindTheGap = 0.001F;
	
	private static final double m_dLidOpenLipHeight = ( 1D / 16D );
	private static final double m_dLidOpenLipYPos = ( 1D - m_dLidOpenLipHeight );
	private static final double m_dLidOpenLipWidth = ( 2D / 16D );
	private static final double m_dLidOpenLipLength = ( 1D );
	private static final double m_dLidOpenLipHalfLength = ( m_dLidOpenLipLength / 2D );
	private static final double m_dLidOpenLipHorizontalOffset = ( 5D / 16D );
	
    public FCModelBlock m_blockModelBase;
    public FCModelBlock m_blockModelBaseOpenCollision;
    public FCModelBlock m_blockModelLid;
    public FCModelBlock m_blockModelLidFull;
    public FCModelBlock m_blockModelInterior;
    
    private static AxisAlignedBB m_boxCollisionLidOpenLip = 
    	new AxisAlignedBB( 0.5D - m_dLidOpenLipHalfLength, m_dLidOpenLipYPos, m_dLidOpenLipHorizontalOffset,
    		0.5D + m_dLidOpenLipHalfLength, m_dLidOpenLipYPos + m_dLidOpenLipHeight, m_dLidOpenLipHorizontalOffset + m_dLidOpenLipWidth );
    
    private static final Vec3 m_lidRotationPoint = Vec3.createVectorHelper( 8F / 16F, 6F / 16F, 14F / 16F );
    
    public FCBlockBasketWicker( int iBlockID )
    {
        super( iBlockID );

    	InitBlockBounds( 0D, 0D, 0D, 1D, m_fBasketHeight, 1D );	    	
        
        InitModelBase();      
        InitModelBaseOpenCollison();      
        InitModelLid();       
        InitModelLidFull();      
        InitModelInterior();
        
        setUnlocalizedName( "fcBlockBasketWicker" );
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityBasketWicker();
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( !GetIsOpen( iMetadata ) )
		{
			if ( !world.isRemote )
			{
				SetIsOpen( world, i, j, k, true );
			}
			else
			{
				player.playSound( "step.gravel", 
		    		0.25F + ( world.rand.nextFloat() * 0.1F ), 
		    		0.5F + ( world.rand.nextFloat() * 0.1F ) );
			}
			
			return true;
		}
		else if ( IsClickingOnLid ( world, i, j, k, iFacing, fXClick, fYClick, fZClick ) ) 
		{
	        FCTileEntityBasketWicker tileEntity = (FCTileEntityBasketWicker)world.getBlockTileEntity( i, j, k );
	        
			if ( !tileEntity.m_bClosing )
			{
				if ( !world.isRemote )
				{
					tileEntity.StartClosingServerSide();
				}
				
				return true;
			}
		}
		else if ( GetHasContents( iMetadata ) )
		{
			if ( world.isRemote )
			{
				player.playSound( "step.gravel", 
		    		0.5F + ( world.rand.nextFloat() * 0.25F ), 
		    		1F + ( world.rand.nextFloat() * 0.25F ) );
			}
			else
			{
				EjectStorageStack( world, i, j, k );
			}
			
			SetHasContents( world, i, j, k, false );			
			
			return true;
		}
		else 
    	{
	    	ItemStack heldStack = player.getCurrentEquippedItem();
	    	
			if ( heldStack != null )
			{
				if ( world.isRemote )
				{
					player.playSound( "step.gravel", 
			    		0.5F + ( world.rand.nextFloat() * 0.25F ), 
			    		0.5F + ( world.rand.nextFloat() * 0.25F ) );
				}
				else
				{				
			        FCTileEntityBasketWicker tileEntity = (FCTileEntityBasketWicker)world.getBlockTileEntity( i, j, k );
			        
		        	tileEntity.SetStorageStack( heldStack );
				}
				
    			heldStack.stackSize = 0;
    			
    			SetHasContents( world, i, j, k, true );			
    			
    			return true;
			}    		
    	}
		
		return false;
    }
	
    private void EjectStorageStack( World world, int i, int j, int k )
    {    	
        FCTileEntityBasketWicker tileEntity = (FCTileEntityBasketWicker)world.getBlockTileEntity( i, j, k );
        
        ItemStack storageStack = tileEntity.GetStorageStack();

        if ( storageStack != null )
        {
	        float xOffset = 0.5F;
	        float yOffset = 0.4F;
	        float zOffset = 0.5F;
	        
	        double xPos = (float)i + xOffset;
	        double yPos = (float)j + yOffset;
	        double zPos = (float)k + zOffset;
	        
            EntityItem entityitem = 
            	new EntityItem( world, xPos, yPos, zPos, storageStack );

            entityitem.motionY = 0.2D;
            
            double fFacingFactor = 0.15D;
            double fRandomFactor = 0.05D;
            
            int iFacing = GetFacing( world, i, j, k );

            if ( iFacing <= 3 )
            {
                entityitem.motionX = ( world.rand.nextDouble() * 2D - 1D ) * fRandomFactor;
                
                if ( iFacing == 2 )
                {
                	entityitem.motionZ = -fFacingFactor;
                }
                else // 3
                {
                	entityitem.motionZ = fFacingFactor;
                }
            }
            else
            {
                entityitem.motionZ = ( world.rand.nextDouble() * 2D - 1D )  * fRandomFactor;
                
	        	if ( iFacing == 4 )
	            {
	            	entityitem.motionX = -fFacingFactor;
	            }
	            else // 5
	            {
	            	entityitem.motionX = fFacingFactor;
	            }
            }
            
            entityitem.delayBeforeCanPickup = 10;
            
            world.spawnEntityInWorld( entityitem );
            
			tileEntity.SetStorageStack( null );
        }
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetFixedBlockBoundsFromPool().offset( i, j, k );	    	
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( !GetIsOpen( blockAccess, i, j, k ) )
		{ 
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0F, 0F, 0F, 1F, m_fBasketHeight, 1F );
		}
    	else
    	{
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0F, 0F, 0F, 1F, m_fBasketOpenHeight, 1F );
    	}
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	int iFacing = GetFacing( iMetadata );
    	
    	FCModelBlock tempBaseModel;
    	
    	if ( !GetIsOpen( iMetadata ) )
		{ 
        	tempBaseModel = m_blockModelBase.MakeTemporaryCopy();
        	
	    	FCModelBlock tempLidModel;
	    	
	    	if ( GetHasContents( iMetadata ) )
	    	{
	    		tempLidModel = m_blockModelLidFull.MakeTemporaryCopy();
	    	}
	    	else
	    	{
	    		tempLidModel = m_blockModelLid.MakeTemporaryCopy();
	    	}
	    	
	    	tempLidModel.RotateAroundJToFacing( iFacing );
	    	
	    	tempLidModel.AddToRayTrace( rayTrace );
		}
    	else
    	{
        	tempBaseModel = m_blockModelBaseOpenCollision.MakeTemporaryCopy();
        	
            FCTileEntityBasketWicker tileEntity = (FCTileEntityBasketWicker)world.getBlockTileEntity( i, j, k );
            
            if ( tileEntity.m_fLidOpenRatio > 0.95F )
            {
    	    	AxisAlignedBB tempLidBox = m_boxCollisionLidOpenLip.MakeTemporaryCopy();
    	    	
    	    	tempLidBox.RotateAroundJToFacing( iFacing );
    	    	
    	    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( tempLidBox );
            }	            
    	}
		
    	tempBaseModel.RotateAroundJToFacing( iFacing );
    	
    	tempBaseModel.AddToRayTrace( rayTrace );
		
        return rayTrace.GetFirstIntersection();
    }
    
    @Override
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	if ( !world.isRemote )
    	{
    		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemWickerPiece.itemID, 1, 0, 0.75F );
    	}
    }
    
    @Override
	public FCModelBlock GetLidModel( int iMetadata )
    {
    	if ( GetHasContents( iMetadata ) )
    	{
    		return m_blockModelLidFull;
    	}
    	
		return m_blockModelLid;
    }
	
    @Override
	public Vec3 GetLidRotationPoint()
	{
    	return m_lidRotationPoint;
	}
    
    @Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
    	return -( 1F - m_fBasketHeight );
    }
    
	//------------- Class Specific Methods ------------//
	
	private void InitModelBase()
	{
		m_blockModelBase = new FCModelBlock();
		
		// base of basket
		
		m_blockModelBase.AddBox( 0D + m_fBasketRimWidth + m_fBasketWidthLip, 0D, 0D + m_fBasketRimWidth + m_fBasketDepthLip,
			1D - m_fBasketRimWidth - m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidHeight, 1D - m_fBasketRimWidth - m_fBasketDepthLip );
	}
	
    private void InitModelBaseOpenCollison()
    {
		m_blockModelBaseOpenCollision = new FCModelBlock();
		
		// base of basket
		
		m_blockModelBaseOpenCollision.AddBox( 0D + m_fBasketRimWidth + m_fBasketWidthLip, 0D, 0D + m_fBasketRimWidth + m_fBasketDepthLip,
			1D - m_fBasketRimWidth - m_fBasketWidthLip, m_fBasketOpenHeight, 1D - m_fBasketRimWidth - m_fBasketDepthLip );
    }
    
	private void InitModelLid()
	{
		m_blockModelLid = new FCModelBlock();
		
		// lid
		
		m_blockModelLid.AddBox( 0D + m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidHeight, 0D + m_fBasketDepthLip,
			1D - m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidHeight + m_fBasketLidLayerHeight, 1D - m_fBasketDepthLip );
		
		m_blockModelLid.AddBox( 0D + m_fBasketRimWidth + m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidLayerHeight, 0D + m_fBasketRimWidth + m_fBasketDepthLip,
			1D - m_fBasketRimWidth - m_fBasketWidthLip, m_fBasketHeight, 1D - m_fBasketRimWidth - m_fBasketDepthLip );
		
		// handle
		
		m_blockModelLid.AddBox( 0.5D - m_fBasketHandleHalfLength, m_fBasketHeight, 0.5D - m_fBasketHandleHalfWidth,
			0.5D + m_fBasketHandleHalfLength, m_fBasketHeight + m_fBasketHandleHeight, 0.5D + m_fBasketHandleHalfWidth );		
	}
	
	private void InitModelLidFull()
	{
		m_blockModelLidFull = new FCModelBlock();
		
		// lid
		
		m_blockModelLidFull.AddBox( 0D + m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidHeight, 0D + m_fBasketDepthLip,
			1D - m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidHeight + m_fBasketLidLayerHeight, 1D - m_fBasketDepthLip );
		
		m_blockModelLidFull.AddBox( 0D + m_fBasketRimWidth + m_fBasketWidthLip, m_fBasketHeight - m_fBasketLidLayerHeight, 0D + m_fBasketRimWidth + m_fBasketDepthLip,
			1D - m_fBasketRimWidth - m_fBasketWidthLip, m_fBasketHeight, 1D - m_fBasketRimWidth - m_fBasketDepthLip );
		
		// top layer that replaces handle
		
		m_blockModelLidFull.AddBox( 0D + m_fBasketRimWidth + m_fBasketWidthLip + m_fBasketLidLayerWidthGap, m_fBasketHeight, 0D + m_fBasketRimWidth + m_fBasketDepthLip + m_fBasketLidLayerWidthGap,
			1D - m_fBasketRimWidth - m_fBasketWidthLip - m_fBasketLidLayerWidthGap, m_fBasketHeight + m_fBasketLidLayerHeight, 1D - m_fBasketRimWidth - m_fBasketDepthLip - m_fBasketLidLayerWidthGap );	
	}

	private void InitModelInterior()
	{
		m_blockModelInterior = new FCModelBlock();
		
		// inverted interior
		
		m_blockModelInterior.AddBox( 
			1D - m_fBasketRimWidth - m_fBasketWidthLip - m_fBasketInteriorWallThickness + m_fMindTheGap, 
			m_fBasketHeight - m_fBasketLidHeight, 
			1D - m_fBasketRimWidth - m_fBasketDepthLip - m_fBasketInteriorWallThickness + m_fMindTheGap,			
			m_fBasketRimWidth + m_fBasketWidthLip + m_fBasketInteriorWallThickness - m_fMindTheGap, 
			m_fBasketInteriorWallThickness, 
			m_fBasketRimWidth + m_fBasketDepthLip + m_fBasketInteriorWallThickness - m_fMindTheGap );
	}
	
	private boolean IsClickingOnLid ( World world, int i, int j, int k, int iSideClicked, float fXClick, float fYClick, float fZClick )
	{
		if ( fYClick > m_fBasketOpenHeight )
		{
			// the only time this should be true is if the player clicks on the rim of the basket
			
			return true;
		}
        
		return false;
	}
	
	//----------- Client Side Functionality -----------//

    private Icon m_iconBaseOpenTop;
    private Icon m_iconFront;
    private Icon m_iconTop;
    private Icon m_iconBottom;
    
    private boolean m_bRenderingBase = false;
    private boolean m_bRenderingInterior = false;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_iconBaseOpenTop = register.registerIcon( "fcBlockBasketWicker_open_top" );
		m_iconFront = register.registerIcon( "fcBlockBasketWicker_front" );
		m_iconTop = register.registerIcon( "fcBlockBasketWicker_top" );
		m_iconBottom = register.registerIcon( "fcBlockBasketWicker_bottom" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iSide == 1 && m_bRenderingBase )
		{
			return m_iconBaseOpenTop;
		}		
		else if ( !m_bRenderingInterior )
		{
			if ( iSide == 1 )
			{
				return m_iconTop;
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
		}
		
		return super.getIcon( iSide, iMetadata );
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	if ( iSide == 0 )
    	{
    		if ( m_bRenderingInterior )
    		{
    			return false;
    		}
    		
    		return !m_bRenderingBase || super.shouldSideBeRendered( 
    			blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide );
    	}
    	
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {    	
    	FCModelBlock transformedModel;
    	int iMetadata = renderer.blockAccess.getBlockMetadata( i, j, k );
    	
		int iFacing = GetFacing( iMetadata );
		
		m_bRenderingBase = true;
		transformedModel = m_blockModelBase.MakeTemporaryCopy();
		
		transformedModel.RotateAroundJToFacing( GetFacing( renderer.blockAccess, i, j, k ) );

		renderer.SetUvRotateTop( ConvertFacingToTopTextureRotation( iFacing ) );
		renderer.SetUvRotateBottom( ConvertFacingToBottomTextureRotation( iFacing ) );
		
		boolean bReturnValue = transformedModel.RenderAsBlock( renderer, this, i, j, k );

		m_bRenderingBase = false;

    	if ( !GetIsOpen( iMetadata ) ) 
    	{
	    	if ( GetHasContents( iMetadata ) )
	    	{
	    		transformedModel = m_blockModelLidFull.MakeTemporaryCopy();
	    	}
	    	else
	    	{
	    		transformedModel = m_blockModelLid.MakeTemporaryCopy();
	    	}
	    	
			transformedModel.RotateAroundJToFacing( GetFacing( renderer.blockAccess, i, j, k ) );
	    	
			transformedModel.RenderAsBlockWithColorMultiplier( renderer, this, i, j, k );			
    	}
    	else
    	{
	    	transformedModel = m_blockModelInterior.MakeTemporaryCopy();
	    	
	    	transformedModel.RotateAroundJToFacing( iFacing );
	    	
	    	m_bRenderingInterior = true;
	    	
	        transformedModel.RenderAsBlockWithColorMultiplier( renderer, this, i, j, k );
	        
	    	m_bRenderingInterior = false;    		
    	}		
    	
		renderer.ClearUvRotation();
		
		return bReturnValue;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	m_blockModelLid.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    	m_blockModelBase.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    }
    
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, 
    	MovingObjectPosition rayTraceHit )
    {
		int i = rayTraceHit.blockX;
		int j = rayTraceHit.blockY;
		int k = rayTraceHit.blockZ;
		
		int iMetadata = world.getBlockMetadata( i, j, k );
		int iFacing = GetFacing( iMetadata );
		
		double minYBox = j;		
		double maxYBox = j + m_fBasketHeight;
		
		if ( GetIsOpen( iMetadata ) )
		{
            if ( rayTraceHit.hitVec.yCoord - minYBox >= 1D - m_fMindTheGap - m_dLidOpenLipHeight ) // check if the lid lip has been clicked
            {
            	AxisAlignedBB tempLidBox = m_boxCollisionLidOpenLip.MakeTemporaryCopy();
		    	
            	tempLidBox.RotateAroundJToFacing( iFacing );
		    	
		        return tempLidBox.offset( i, j, k );
            }
            
			maxYBox -= m_fBasketLidHeight;			
		}
		
		double minXBox, maxXBox, minZBox, maxZBox;
		
		if ( iFacing == 2 || iFacing == 3 )
		{
			minXBox = (double)i + + m_fBasketRimWidth + m_fBasketWidthLip;
			maxXBox = (double)i + 1D - m_fBasketRimWidth - m_fBasketWidthLip;
			
			minZBox = (double)k + + m_fBasketRimWidth + m_fBasketDepthLip;
			maxZBox = (double)k + 1D - m_fBasketRimWidth - m_fBasketDepthLip;
		}
		else
		{	
			minXBox = (double)i + m_fBasketRimWidth + m_fBasketDepthLip;
			maxXBox = (double)i + 1D - m_fBasketRimWidth - m_fBasketDepthLip;
			
			minZBox = (double)k + m_fBasketRimWidth + m_fBasketWidthLip;
			maxZBox = (double)k + 1D - m_fBasketRimWidth - m_fBasketWidthLip;			
		}
		
        return AxisAlignedBB.getAABBPool().getAABB( minXBox, minYBox, minZBox, maxXBox, maxYBox, maxZBox );
    }
}