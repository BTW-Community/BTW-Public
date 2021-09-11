// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockAestheticNonOpaque extends Block
{
    public final static int m_iSubtypeUrn = 0;
    public final static int m_iSubtypeColumn = 1; // deprecated
    public final static int m_iSubtypePedestalUp = 2;  // deprecated
    public final static int m_iSubtypePedestalDown = 3; // deprecated
    public final static int m_iSubtypeTable = 4; // deprecated
    public final static int m_iSubtypeWickerSlab = 5; // deprecated
    public final static int m_iSubtypeGrate = 6; // deprecated
    public final static int m_iSubtypeWicker = 7; // deprecated
    public final static int m_iSubtypeSlats = 8; // deprecated
    public final static int m_iSubtypeWickerSlabUpsideDown = 9; // deprecated
    public final static int m_iSubtypeWhiteCobbleSlab = 10;
    public final static int m_iSubtypeWhiteCobbleSlabUpsideDown = 11;
    public final static int m_iSubtypeLightningRod = 12; // deprecated
    
    public final static int m_iNumSubtypes = 13;    
    
    private final static float m_fDefaultHardness = 2F;
    
    private final static float m_fColumWidth = ( 10F / 16F );
    private final static float m_fColumHalfWidth = ( m_fColumWidth / 2F );
    
    private final static float m_fPedestalBaseHeight = ( 12F / 16F );
    private final static float m_fPedestalMiddleHeight = ( 2F / 16F );
    private final static float m_fPedestalMiddleWidth = ( 14F / 16F );
    private final static float m_fPedestalMiddleHalfWidth = ( m_fPedestalMiddleWidth / 2F );
    private final static float m_fPedestalTopHeight = ( 2F / 16F );
    private final static float m_fPedestalTopWidth = ( 12F / 16F );
    private final static float m_fPedestalTopHalfWidth = ( m_fPedestalTopWidth / 2F );
    
    private final static float m_fTableTopHeight = ( 2F / 16F );
    private final static float m_fTableLegHeight = ( 1F - m_fTableTopHeight );
    private final static float m_fTableLegWidth = ( 4F / 16F );
    private final static float m_fTableLegHalfWidth = ( m_fTableLegWidth / 2F );
    
    private final static float m_fLightningRodShaftWidth = ( 1F / 16F );
    private final static float m_fLightningRodShaftHalfWidth = ( m_fLightningRodShaftWidth / 2F );
    private final static float m_fLightningRodBaseWidth = ( 4F / 16F );
    private final static float m_fLightningRodBaseHalfWidth = ( m_fLightningRodBaseWidth / 2F );
    private final static float m_fLightningRodBaseHeight = ( 2F / 16F );
    private final static float m_fLightningRodBaseHalfHeight = ( m_fLightningRodBaseHeight / 2F );
    private final static float m_fLightningRodBallWidth = ( 3F / 16F );
    private final static float m_fLightningRodBallHalfWidth = ( m_fLightningRodBallWidth / 2F );
    private final static float m_fLightningRodBallVeticalOffset = ( 10F / 16F );
    private final static float m_fLightningRodCandleHolderWidth = ( 4F / 16F );
    private final static float m_fLightningRodCandleHolderHalfWidth = ( m_fLightningRodCandleHolderWidth / 2F );
    private final static float m_fLightningRodCandleHolderHeight = ( 1F / 16F );
    private final static float m_fLightningRodCandleHolderHalfHeight = ( m_fLightningRodCandleHolderHeight / 2F );
    private final static float m_fLightningRodCandleHolderVeticalOffset = ( ( 16F - m_fLightningRodCandleHolderHeight ) / 16F );
    
    public FCBlockAestheticNonOpaque( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialMiscellaneous );
        
        setHardness( m_fDefaultHardness ); 
        SetAxesEffectiveOn( true );        
        SetPicksEffectiveOn( true );       
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockAestheticNonOpaque" );        

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
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
    	if ( iMetadata == m_iSubtypeUrn )
    	{
    		return FCBetterThanWolves.fcItemUrn.itemID;
    	}
    	else if ( iMetadata == m_iSubtypeWickerSlab || 
    		iMetadata == m_iSubtypeWickerSlabUpsideDown )
    	{
    		return FCBetterThanWolves.fcBlockWickerSlab.blockID;
    	}
    	else if ( iMetadata == m_iSubtypeGrate )
    	{
    		return FCBetterThanWolves.fcBlockGrate.blockID;
    	}
    	else if ( iMetadata == m_iSubtypeWicker )
    	{
    		return FCBetterThanWolves.fcBlockWickerPane.blockID;
    	}
    	else if ( iMetadata == m_iSubtypeSlats )
    	{
    		return FCBetterThanWolves.fcBlockSlats.blockID;
    	}
    	else if ( iMetadata == m_iSubtypePedestalDown || iMetadata == m_iSubtypePedestalUp || 
    		iMetadata == m_iSubtypeColumn )
    	{
    		return FCBetterThanWolves.fcBlockSmoothStoneMouldingAndDecorative.blockID;
    	}
    	else if ( iMetadata == m_iSubtypeTable )
    	{
    		return FCBetterThanWolves.fcBlockWoodMouldingDecorativeItemStubID;
    	}
    	else
    	{
    		return blockID;
    	}
    }

	@Override
	public int damageDropped( int iMetadata )
    {
    	if ( iMetadata == m_iSubtypePedestalDown || iMetadata == m_iSubtypePedestalUp )
    	{
    		iMetadata = FCBlockMouldingAndDecorative.m_iSubtypePedestalUp;
    	}
    	else if ( iMetadata == m_iSubtypeColumn )
    	{
    		iMetadata = FCBlockMouldingAndDecorative.m_iSubtypeColumn;
    	}
    	else if ( iMetadata == m_iSubtypeWhiteCobbleSlabUpsideDown )
    	{
    		iMetadata = m_iSubtypeWhiteCobbleSlab;
    	}
    	else if ( iMetadata == m_iSubtypeTable )
    	{
    		return FCItemBlockWoodMouldingDecorativeStub.m_iTypeTable << 2;
    	}
    	else if ( iMetadata == m_iSubtypeUrn ||
    		iMetadata == m_iSubtypeWickerSlab ||  
    		iMetadata == m_iSubtypeWickerSlabUpsideDown ||        	 
        	iMetadata == m_iSubtypeGrate ||
            iMetadata == m_iSubtypeWicker || 
            iMetadata == m_iSubtypeSlats )
    	{
    		iMetadata = 0;
    	}
    	
        return iMetadata; 
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	int iSubType = iMetadata;
    	
    	if ( iSubType == m_iSubtypePedestalUp  )
    	{
            if ( iFacing == 0 || iFacing != 1 && (double)fClickY > 0.5D )
            {
    			return m_iSubtypePedestalDown;
            }            
    	}
    	else if ( iSubType == m_iSubtypeWickerSlab )
    	{
            if ( iFacing == 0 || iFacing != 1 && (double)fClickY > 0.5D )
            {
    			return m_iSubtypeWickerSlabUpsideDown;
            }
    	}
    	else if ( iSubType == m_iSubtypeWhiteCobbleSlab )
    	{
            if ( iFacing == 0 || iFacing != 1 && (double)fClickY > 0.5D )
            {
    			return m_iSubtypeWhiteCobbleSlabUpsideDown;
            }
    	}    	
    	
    	return iMetadata;
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSubType = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iSubType )
    	{
    		case m_iSubtypeTable:
    			
    			if ( !IsTableOnCorner( blockAccess, i, j, k ) )
    			{
    	        	return AxisAlignedBB.getAABBPool().getAABB(         	
    	        		0D, 1D - m_fTableTopHeight, 0D, 
    	        		1D, 1D, 1D );
    			}
    			else
    			{
    	        	return AxisAlignedBB.getAABBPool().getAABB(         	
    	        		0D, 0D, 0D, 1D, 1D, 1D);
    			}
    			
    		case m_iSubtypeGrate:
    		case m_iSubtypeWicker:
    		case m_iSubtypeSlats:
    			
    			return GetBlockBoundsFromPoolForPane( blockAccess, i, j, k, iSubType );
    			
			case m_iSubtypeUrn:

				AxisAlignedBB urnBox = AxisAlignedBB.getAABBPool().getAABB(         	
	        		( 0.5D - FCBlockUnfiredPottery.m_fUnfiredPotteryUrnBodyHalfWidth ), 
	        		0D, 
	        		( 0.5D - FCBlockUnfiredPottery.m_fUnfiredPotteryUrnBodyHalfWidth ), 
	        		( 0.5D + FCBlockUnfiredPottery.m_fUnfiredPotteryUrnBodyHalfWidth ), 
	        		FCBlockUnfiredPottery.m_fUnfiredPotteryUrnHeight, 
	        		( 0.5D + FCBlockUnfiredPottery.m_fUnfiredPotteryUrnBodyHalfWidth ) );

				if ( blockAccess.getBlockId( i, j + 1, k ) == FCBetterThanWolves.fcHopper.blockID )
				{
					urnBox.offset( 0D, 1D - FCBlockUnfiredPottery.m_fUnfiredPotteryUrnHeight, 0D );
				}
				
				return urnBox;
    	    	
			case m_iSubtypeColumn:
				
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		( 0.5F - m_fColumHalfWidth ), 0.0F, ( 0.5F - m_fColumHalfWidth ), 
	        		( 0.5F + m_fColumHalfWidth ), 1.0F, ( 0.5F + m_fColumHalfWidth ) );
    	    	
    		case m_iSubtypeWickerSlab:
    		case m_iSubtypeWhiteCobbleSlab:
    			
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.0F, 0.0F, 0.0F, 
	        		1.0F, 0.5F, 1.0F );
    	    	
    		case m_iSubtypeWickerSlabUpsideDown:
    		case m_iSubtypeWhiteCobbleSlabUpsideDown:
			
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.0F, 0.5F, 0.0F, 
	        		1.0F, 1.0F, 1.0F );
    	    	
    		case m_iSubtypeLightningRod:
    	    	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.5F - m_fLightningRodBaseHalfWidth, 0F, 0.5F - m_fLightningRodBaseHalfWidth, 
	        		0.5F + m_fLightningRodBaseHalfWidth, 1F, 0.5F + m_fLightningRodBaseHalfWidth );
    	}
    	
    	return super.GetBlockBoundsFromPoolBasedOnState( blockAccess, i, j, k );
    }
    
	@Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
		AxisAlignedBB intersectingBox, List list, Entity entity )
    {
    	int iSubType = world.getBlockMetadata( i, j, k );
    	
    	if ( iSubType == m_iSubtypeGrate || 
    		iSubType == m_iSubtypeWicker || 
    		iSubType == m_iSubtypeSlats )
    	{
	        boolean bKNeg = ShouldPaneConnectToBlock( world, i, j, k - 1, iSubType );
	        boolean bKPos = ShouldPaneConnectToBlock( world, i, j, k + 1, iSubType );
	        
	        boolean bINeg = ShouldPaneConnectToBlock( world, i - 1, j, k, iSubType );
	        boolean bIPos = ShouldPaneConnectToBlock( world, i + 1, j, k, iSubType );
	        
	        if ( ( !bINeg || !bIPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
	        {
	            if ( bINeg && !bIPos )
	            {
	            	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
	            		0F, 0F, 0.4375F, 0.5F, 1F, 0.5625F ).offset( i, j, k );

	            	tempBox.AddToListIfIntersects( intersectingBox, list );
	            }
	            else if ( !bINeg && bIPos )
	            {
	            	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
	                	0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F ).offset( i, j, k );

	            	tempBox.AddToListIfIntersects( intersectingBox, list );
	            }
	        }
	        else
	        {
	        	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
	            	0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F ).offset( i, j, k );
	            	
	        	tempBox.AddToListIfIntersects( intersectingBox, list );
	        }

	        if ( ( !bKNeg || !bKPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
	        {
	            if ( bKNeg && !bKPos )
	            {
	        		AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
	                	0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F ).offset( i, j, k );
	                	
	            	tempBox.AddToListIfIntersects( intersectingBox, list );
	            }
	            else if ( !bKNeg && bKPos )
	            {
	        		AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
	                	0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F ).offset( i, j, k );
	                	
	            	tempBox.AddToListIfIntersects( intersectingBox, list );
	            }
	        }
	        else
	        {
	    		AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
	            	0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F).offset( i, j, k );
	            	
	        	tempBox.AddToListIfIntersects( intersectingBox, list );
	        }
    	}
    	else
    	{
            super.addCollisionBoxesToList(world, i, j, k, intersectingBox, list, entity );
    	}
    }    
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iChangedBlockID )
    {
        super.onNeighborBlockChange( world, i, j, k, iChangedBlockID );
        
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeLightningRod )
    	{
    		if ( !CanLightningRodStay( world, i, j, k ) )
    		{
    			if ( world.getBlockId( i, j, k ) == blockID )
    			{
	                dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0);
	                world.setBlockWithNotify( i, j, k, 0 );
    			}
    		}
    		else
    		{
    			world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
    		}
    	}
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iSubtype )
    	{
    		case m_iSubtypeColumn:
    			
    			return iFacing == 0 || iFacing == 1;
    			
    		case m_iSubtypePedestalUp:
    		case m_iSubtypePedestalDown:
    			
    			return true;    			
    	}
    	
    	return super.HasCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );    	
	}
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iSubtype )
    	{
    		case m_iSubtypePedestalUp:
    			
    			return iFacing == 0;
    			
    		case m_iSubtypePedestalDown:
    			
    			return iFacing == 1;    			
    			
    		case m_iSubtypeWickerSlab:
    		case m_iSubtypeWhiteCobbleSlab:
    			
    			return iFacing == 0;
    			
    		case m_iSubtypeWickerSlabUpsideDown:
    		case m_iSubtypeWhiteCobbleSlabUpsideDown:
    			
    			return iFacing == 1;    			
    			
    		case m_iSubtypeTable:
    			
    			return iFacing == 1;    			
    	}
    	
    	return super.HasLargeCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );    	
	}
    
	@Override
	public boolean HasSmallCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( iSubtype == m_iSubtypeLightningRod )
    	{
    		return iFacing == 1;
    	}
    	
    	return super.HasSmallCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );    	
	}
	
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );

		if ( iSubtype == m_iSubtypeUrn ||
			iSubtype == m_iSubtypeTable ||
			iSubtype == m_iSubtypeWickerSlab ||
			iSubtype == m_iSubtypeWickerSlabUpsideDown ||
			iSubtype == m_iSubtypeGrate ||
			iSubtype == m_iSubtypeWicker || 
			iSubtype == m_iSubtypeSlats )
		{
			return false;
		}
		
		return true;
    }
	
	@Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
		return 1.2F;
    }
	
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );

    	if ( iSubtype == m_iSubtypeWickerSlab || iSubtype == m_iSubtypeWhiteCobbleSlab )
        {
        	return true;
        }
    	else if ( iSubtype == m_iSubtypeUrn )
    	{
    		return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    	}
        
    	return super.CanGroundCoverRestOnBlock( world, i, j, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );

    	if ( iSubtype == m_iSubtypeWickerSlab || iSubtype == m_iSubtypeWhiteCobbleSlab )
        {
        	return -0.5F;
        }
    	else if ( iSubtype == m_iSubtypeUrn )
    	{
    		return -1F;
    	}
        
    	return super.GroundCoverRestingOnVisualOffset( blockAccess, i, j, k );
    }
    
    @Override
    public boolean CanToolsStickInBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	return iSubtype != m_iSubtypeWickerSlab;
    }
    
    //------------- Class Specific Methods ------------//
    
    public int GetSubtype( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return blockAccess.getBlockMetadata( i, j, k );
    }
    
    public void SetSubtype( World world, int i, int j, int k, int iSubtype )
    {
    	world.setBlockMetadata( i, j, k, iSubtype );
    }
    
    public boolean IsBlockTable( IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( blockAccess.getBlockId( i, j, k ) == FCBetterThanWolves.fcAestheticNonOpaque.blockID )
    	{
    		if ( blockAccess.getBlockMetadata( i, j, k ) == m_iSubtypeTable )
    		{
    			return true;
    		}
    	}
    			
		return false;
    }
    
    public boolean IsTableOnCorner( IBlockAccess blockAccess, int i, int j, int k )
    {
    	boolean positiveITable = IsBlockTable( blockAccess, i + 1, j, k );
    	boolean negativeITable = IsBlockTable( blockAccess, i - 1, j, k );
    	boolean positiveKTable = IsBlockTable( blockAccess, i, j, k + 1 );
    	boolean negativeKTable = IsBlockTable( blockAccess, i, j, k - 1 );
    	
    	if ( ( !positiveITable && ( !positiveKTable || !negativeKTable ) ) ||
			( !negativeITable && ( !positiveKTable || !negativeKTable ) ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean ShouldPaneConnectToBlock( IBlockAccess blockAccess, int i, int j, int k, int iSubType )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	
        if ( Block.opaqueCubeLookup[iBlockID] || iBlockID == Block.glass.blockID )
        {
        	return true;
        }
        else if ( iBlockID == blockID )
        {
        	int iTargetSubType = blockAccess.getBlockMetadata( i, j, k );
        	
        	if ( iTargetSubType == iSubType )
        	{
        		return true;
        	}
        }

    	return false;
    }

    public AxisAlignedBB GetBlockBoundsFromPoolForPane( IBlockAccess blockAccess, 
    	int i, int j, int k, int iSubType )
    {
        float fXMin = 0.4375F;
        float fXMax = 0.5625F;
        
        float fZMin = 0.4375F;
        float fZMax = 0.5625F;
        
        boolean bKNeg = ShouldPaneConnectToBlock( blockAccess, i, j, k - 1, iSubType );
        boolean bKPos = ShouldPaneConnectToBlock( blockAccess, i, j, k + 1, iSubType );
        boolean bINeg = ShouldPaneConnectToBlock( blockAccess, i - 1, j, k, iSubType );
        boolean bIPos = ShouldPaneConnectToBlock( blockAccess, i + 1, j, k, iSubType );
        
        if ( ( !bINeg || !bIPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
        {
            if ( bINeg && !bIPos )
            {
                fXMin = 0.0F;
            }
            else if ( !bINeg && bIPos )
            {
                fXMax = 1.0F;
            }
        }
        else
        {
            fXMin = 0.0F;
            fXMax = 1.0F;
        }

        if ( ( !bKNeg || !bKPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
        {
            if ( bKNeg && !bKPos )
            {
                fZMin = 0.0F;
            }
            else if ( !bKNeg && bKPos )
            {
                fZMax = 1.0F;
            }
        }
        else
        {
            fZMin = 0.0F;
            fZMax = 1.0F;
        }

    	return AxisAlignedBB.getAABBPool().getAABB( fXMin, 0.0F, fZMin, fXMax, 1.0F, fZMax );
    }
    
	static public boolean CanLightningRodStay( World world, int i, int j, int k )
	{
		int iBlockBelowID = world.getBlockId( i, j - 1, k );
		
		if ( iBlockBelowID == FCBetterThanWolves.fcAestheticNonOpaque.blockID && world.getBlockMetadata( i, j - 1, k ) == m_iSubtypeLightningRod )
		{
			return true;
		}
		
		return FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, i, j - 1, k, 1, true );
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_IconUrn;
    private Icon m_IconColumnStoneTop;
    private Icon m_IconColumnStoneSide;
    private Icon m_IconPedestalStoneTop;
    private Icon m_IconPedestalStoneSide;
    private Icon m_IconSlabWicker;
    private Icon m_IconGrate;
    private Icon m_IconWicker;
    private Icon m_IconSlats;
    private Icon m_IconSlatsSide;
    private Icon m_IconWhiteCobble;
    private Icon m_IconLightningRod;
    
    public Icon m_IconTableWoodOakTop;
    public Icon m_IconTableWoodOakLeg;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" ); // hit effect
        
        m_IconUrn = register.registerIcon( "fcBlockUrn" );
        m_IconColumnStoneTop = register.registerIcon( "fcBlockColumnStone_top" );
        m_IconColumnStoneSide = register.registerIcon( "fcBlockColumnStone_side" );
        m_IconPedestalStoneTop = register.registerIcon( "fcBlockPedestalStone_top" );
        m_IconPedestalStoneSide = register.registerIcon( "fcBlockPedestalStone_side" );
        m_IconTableWoodOakTop = register.registerIcon( "fcBlockTableWoodOak_top" );
        m_IconTableWoodOakLeg = register.registerIcon( "fcBlockTableWoodOak_leg" );
        m_IconSlabWicker = register.registerIcon( "fcBlockSlabWicker" );
        m_IconGrate = register.registerIcon( "fcBlockGrate" );
        m_IconWicker = register.registerIcon( "fcBlockWicker" );
        m_IconSlats = register.registerIcon( "fcBlockSlats" );
        m_IconSlatsSide = register.registerIcon( "fcBlockSlats_side" );
        m_IconWhiteCobble = register.registerIcon( "fcBlockWhiteCobble" );
        m_IconLightningRod = register.registerIcon( "fcBlockLightningRodOld" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	int iSubType = iMetadata;
    	
    	switch ( iSubType )
    	{
	    	case m_iSubtypeColumn:
	    		
	    		if ( iSide < 2 )
	    		{
	    			return m_IconColumnStoneTop;
	    		}
	    		else
	    		{
	    			return m_IconColumnStoneSide;
	    		}
	    		
	    	case m_iSubtypePedestalUp:
	    	case m_iSubtypePedestalDown:

	    		if ( iSide < 2 )
	    		{
	    			return m_IconPedestalStoneTop;
	    		}
	    		else
	    		{
	    			return m_IconPedestalStoneSide;
	    		}
	    		
	    	case m_iSubtypeTable:
	    		
	    		return m_IconTableWoodOakTop;
	    		
	    	case m_iSubtypeWickerSlab:
	    	case m_iSubtypeWickerSlabUpsideDown:
	    		
	    		return m_IconSlabWicker;
	    		
	    	case m_iSubtypeGrate:
	    		
	    		return m_IconGrate;
	    		
	    	case m_iSubtypeWicker:
	    		
	    		return m_IconWicker;
	    		
	    	case m_iSubtypeSlats:
	    		
	    		return m_IconSlats;
	    		
	    	case m_iSubtypeWhiteCobbleSlab:
	    	case m_iSubtypeWhiteCobbleSlabUpsideDown:
	    		
	    		return m_IconWhiteCobble;
	    		
	    	case m_iSubtypeLightningRod:
	    		
	    		return m_IconLightningRod;
	    		
    		default:
    	
    	    	return blockIcon;
    	}    	
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }	
	
	@Override
    public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
		// none of the sub-blocks are in the creative inventory
    }
	
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }

    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
    	int iSubType = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iSubType )
    	{
    		case m_iSubtypeUrn:
    			
    			float fVerticalOffset = 0.0F;
    			
				if ( blockAccess.getBlockId( i, j + 1, k ) == FCBetterThanWolves.fcHopper.blockID )
				{
					fVerticalOffset = 1.0F - FCBlockUnfiredPottery.m_fUnfiredPotteryUrnHeight;
				}
				
    			return ((FCBlockUnfiredPottery)(FCBetterThanWolves.fcUnfiredPottery)).RenderUnfiredUrn( 
    				renderer, blockAccess, i, j, k, this, m_IconUrn, fVerticalOffset );
    			
    		case m_iSubtypePedestalUp:
    			
    			return RenderPedestalUp( renderer, blockAccess, i, j, k, this );
    			
    		case m_iSubtypePedestalDown:
    			
    			return RenderPedestalDown( renderer, blockAccess, i, j, k, this );
    			
    		case m_iSubtypeTable:
    			
    			return RenderTable( renderer, blockAccess, i, j, k, this );
    			
    		case m_iSubtypeGrate:
    		case m_iSubtypeWicker:
    		case m_iSubtypeSlats:
    			
    			return RenderPane( renderer, blockAccess, i, j, k, this, iSubType );
    			
    		case m_iSubtypeLightningRod:
    			
    			return RenderLightningRod( renderer, blockAccess, i, j, k, this );
    			
			default:
				
		        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
		        	renderer.blockAccess, i, j, k ) );
		        
				return renderer.renderStandardBlock( this, i, j, k );		    	
    	}    	
    }

    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	int iSubType = iItemDamage;
    	
    	switch ( iSubType )
    	{
    		case m_iSubtypeUrn:
    			
    			((FCBlockUnfiredPottery)(FCBetterThanWolves.fcUnfiredPottery)).RenderUnfiredUrnInvBlock( 
					renderBlocks, this, iItemDamage, m_IconUrn );
    			
    			return;
    			
    		case m_iSubtypePedestalUp:
    			
    			RenderPedestalUpInvBlock( renderBlocks, this );
    			
    			return;
    			
    		case m_iSubtypePedestalDown:
    			
    			RenderPedestalDownInvBlock( renderBlocks, this );
    			
    			return;
    			
    		case m_iSubtypeTable:
    			
    			RenderTableInvBlock( renderBlocks, this );
    			
    			return;
		        
    		case m_iSubtypeLightningRod:
    			
    			RenderLightningRodInvBlock( renderBlocks, this );
    			
    			return;		        
    			
			case m_iSubtypeColumn:
				
				renderBlocks.setRenderBounds( 
					( 0.5F - m_fColumHalfWidth ), 0.0F, ( 0.5F - m_fColumHalfWidth ), 
	        		( 0.5F + m_fColumHalfWidth ), 1.0F, ( 0.5F + m_fColumHalfWidth ) );
    	    	
    	    	break;
    	    	
    		case m_iSubtypeWickerSlab:
    		case m_iSubtypeWhiteCobbleSlab:
    			
    			renderBlocks.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	        		1.0F, 0.5F, 1.0F );
    	    	
    	    	break;
    	    	
    		case m_iSubtypeWickerSlabUpsideDown:
    		case m_iSubtypeWhiteCobbleSlabUpsideDown:
			
    			renderBlocks.setRenderBounds( 0.0F, 0.5F, 0.0F, 
	        		1.0F, 1.0F, 1.0F );
    	    	
    	    	break;
    	    	
	    	default:
    	    		
	    		renderBlocks.setRenderBounds( 0.0F, 0.0F, 0.0F, 
    	        		1.0F, 1.0F, 1.0F );
    	    	
    	    	break;
    	}
    	
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, 
        	-0.5F, -0.5F, -0.5F, iSubType );
    }
    
    //------------- Pedestal Renderers ------------//
    
    public boolean RenderPedestalUp
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   // render base
    	
	   renderBlocks.setRenderBounds( 0F, 0F, 0F,
			   1.0F, m_fPedestalBaseHeight, 1.0F );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render middle
	   
	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalMiddleHalfWidth, m_fPedestalBaseHeight, 0.5F - m_fPedestalMiddleHalfWidth,
			   0.5F + m_fPedestalMiddleHalfWidth, m_fPedestalBaseHeight + m_fPedestalMiddleHeight, 0.5F + m_fPedestalMiddleHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render top
       
	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalTopHalfWidth, 1.0F - m_fPedestalTopHeight, 0.5F - m_fPedestalTopHalfWidth,
			   0.5F + m_fPedestalTopHalfWidth, 1.0F, 0.5F + m_fPedestalTopHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   return true;
    }
    
    public void RenderPedestalUpInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
 	   // render base
     	
 	   renderBlocks.setRenderBounds( 0F, 0F, 0F,
 			   1.0F, m_fPedestalBaseHeight, 1.0F );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalUp );    
 	   
 	   // render middle
 	   
 	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalMiddleHalfWidth, m_fPedestalBaseHeight, 0.5F - m_fPedestalMiddleHalfWidth,
 			   0.5F + m_fPedestalMiddleHalfWidth, m_fPedestalBaseHeight + m_fPedestalMiddleHeight, 0.5F + m_fPedestalMiddleHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalUp );    
 	   
 	   // render top
        
 	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalTopHalfWidth, 1.0F - m_fPedestalTopHeight, 0.5F - m_fPedestalTopHalfWidth,
 			   0.5F + m_fPedestalTopHalfWidth, 1.0F, 0.5F + m_fPedestalTopHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalUp );    
    }
    
    public boolean RenderPedestalDown
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   // render base
    	
	   renderBlocks.setRenderBounds( 0F, 1.0F - m_fPedestalBaseHeight, 0F,
			   1.0F, 1.0F, 1.0F );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render middle
	   
	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalMiddleHalfWidth, 1.0F - m_fPedestalBaseHeight - m_fPedestalMiddleHeight, 0.5F - m_fPedestalMiddleHalfWidth,
			   0.5F + m_fPedestalMiddleHalfWidth, 1.0F - m_fPedestalBaseHeight, 0.5F + m_fPedestalMiddleHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render top
       
	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalTopHalfWidth, 00F, 0.5F - m_fPedestalTopHalfWidth,
			   0.5F + m_fPedestalTopHalfWidth, m_fPedestalTopHeight, 0.5F + m_fPedestalTopHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   return true;
    }
    
    public void RenderPedestalDownInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
 	   // render base
     	
 	   renderBlocks.setRenderBounds( 0F, 1.0F - m_fPedestalBaseHeight, 0F,
 			   1.0F, 1.0F, 1.0F );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalDown );    
 	   
 	   // render middle
 	   
 	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalMiddleHalfWidth, 1.0F - m_fPedestalBaseHeight - m_fPedestalMiddleHeight, 0.5F - m_fPedestalMiddleHalfWidth,
 			   0.5F + m_fPedestalMiddleHalfWidth, 1.0F - m_fPedestalBaseHeight, 0.5F + m_fPedestalMiddleHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalDown );    
 	   
 	   // render top
        
 	   renderBlocks.setRenderBounds( 0.5F - m_fPedestalTopHalfWidth, 00F, 0.5F - m_fPedestalTopHalfWidth,
 			   0.5F + m_fPedestalTopHalfWidth, m_fPedestalTopHeight, 0.5F + m_fPedestalTopHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalDown );    
    }
    
    //------------- Table Renderers ------------//
 
    public boolean RenderTable
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   // render top
       
	   renderBlocks.setRenderBounds( 0.0F, 1.0F - m_fTableTopHeight, 0.0F,
			   1.0F, 1.0F, 1.0F );
   
	   FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, m_IconTableWoodOakTop );
	   
	   if ( IsTableOnCorner( blockAccess, i, j, k ) )
	   {
		   // render leg
		   
		   renderBlocks.setRenderBounds( 0.5F - m_fTableLegHalfWidth, 0.0F, 0.5F - m_fTableLegHalfWidth,
			   0.5F + m_fTableLegHalfWidth, m_fTableLegHeight, 0.5F + m_fTableLegHalfWidth );
		   
		   FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, m_IconTableWoodOakLeg );		   
	   }	   
	   
	   return true;
    }
    
    public void RenderTableInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
 	   // render top
        
 	   renderBlocks.setRenderBounds( 0.0F, 1.0F - m_fTableTopHeight, 0.0F,
 			   1.0F, 1.0F, 1.0F );
    
       FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_IconTableWoodOakTop );
 	   
	   // render leg
	   
	   renderBlocks.setRenderBounds( 0.5F - m_fTableLegHalfWidth, 0.0F, 0.5F - m_fTableLegHalfWidth,
		   0.5F + m_fTableLegHalfWidth, m_fTableLegHeight, 0.5F + m_fTableLegHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_IconTableWoodOakLeg );
    }
    
    //------------- Pane Renderers ------------//

    public boolean RenderPane
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block,
    	int iSubType    	
    )
    {
    	// copied over from RenderBlocks BlockPane renderer with minor modifications
    	
        int iWorldHeight = 256; //blockAccess.getWorldHeight();
        
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.setBrightness( block.getMixedBrightnessForBlock( blockAccess, i, j, k ) );
        
        int iColor = block.colorMultiplier(blockAccess, i, j, k);
        
        float iColorRed = (float)(iColor >> 16 & 0xff) / 255F;
        float iColorGreen = (float)(iColor >> 8 & 0xff) / 255F;
        float iColorBlue = (float)(iColor & 0xff) / 255F;
        
        if ( EntityRenderer.anaglyphEnable )
        {
            iColorRed = (iColorRed * 30F + iColorGreen * 59F + iColorBlue * 11F) / 100F;
            iColorGreen = (iColorRed * 30F + iColorGreen * 70F) / 100F;
            iColorBlue = (iColorRed * 30F + iColorBlue * 70F) / 100F;            
        }
        
        tessellator.setColorOpaque_F( iColorRed, iColorGreen, iColorBlue);
        
        Icon paneTexture;
        Icon sideTexture;
        
        if ( renderBlocks.hasOverrideBlockTexture() )
        {
            paneTexture = renderBlocks.GetOverrideTexture();
            sideTexture = renderBlocks.GetOverrideTexture();
        } 
        else
        {
            int iMetadata = blockAccess.getBlockMetadata(i, j, k);
            
            paneTexture = block.getIcon( 0, iMetadata );
            sideTexture = block.getIcon( 0, iMetadata );
            
            if ( iSubType == m_iSubtypeSlats )
            {
            	sideTexture = this.m_IconSlatsSide;
            }
        }
        
        int iPaneTextureOriginX = paneTexture.getOriginX();
        int iPaneTextureOriginY = paneTexture.getOriginY();
        
        double dPaneTextureMinU = (double)paneTexture.getMinU();
        double dPaneTextureInterpolatedMidU = (double)paneTexture.getInterpolatedU(8.0D);        
        double dPaneTextureMaxU = (double)paneTexture.getMaxU();
        
        double dPaneTextureMinV = (double)paneTexture.getMinV();
        double dPaneTextureMaxV = (double)paneTexture.getMaxV();
        
        int iSideTextureOriginX = sideTexture.getOriginX();
        int iSideTextureOriginY = sideTexture.getOriginY();
        
        double dSideTextureInterpolatedMinU = (double)sideTexture.getInterpolatedU(7.0D);
        double dSideTextureInterpolatedMaxU = (double)sideTexture.getInterpolatedU(9.0D);
        
        double dSideTextureMinV = (double)sideTexture.getMinV();
        double dSideTextureInterpolatedMidV = (double)sideTexture.getInterpolatedV(8.0D);
        double dSideTextureMaxV = (double)sideTexture.getMaxV();
        
        double dPosXMin = (double)i;
        double dPosXMid = (double)i + 0.5D;
        double dPosXMax = (double)(i + 1);
        
        double dPosZMin = (double)k;
        double dPosZMid = (double)k + 0.5D;
        double dPosZMax = (double)(k + 1);
        
        double var50 = (double)i + 0.5D - 0.0625D;
        double var52 = (double)i + 0.5D + 0.0625D;
        
        double var54 = (double)k + 0.5D - 0.0625D;
        double var56 = (double)k + 0.5D + 0.0625D;
        
        boolean var58 = ShouldPaneConnectToBlock( blockAccess, i, j, k - 1, iSubType );
        boolean var59 = ShouldPaneConnectToBlock( blockAccess, i, j, k + 1, iSubType );
        boolean var60 = ShouldPaneConnectToBlock( blockAccess, i - 1, j, k, iSubType );
        boolean var61 = ShouldPaneConnectToBlock( blockAccess, i + 1, j, k, iSubType );
        
        boolean var62 = !ShouldPaneConnectToBlock( blockAccess, i, j + 1, k, iSubType );
        boolean var63 = !ShouldPaneConnectToBlock( blockAccess, i, j - 1, k, iSubType );
        
        if ((!var60 || !var61) && (var60 || var61 || var58 || var59))
        {
            if (var60 && !var61)
            {
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 1), dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 0), dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 0), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 1), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);

                if (!var59 && !var58)
                {
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var56, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var56, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var54, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var54, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                }

                if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i - 1, j + 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                }

                if (var63 || j > 1 && blockAccess.isAirBlock(i - 1, j - 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                }
            }
            else if (!var60 && var61)
            {
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 0), dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 1), dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 1), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 0), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);

                if (!var59 && !var58)
                {
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var56, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var56, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), var54, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), var54, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                }

                if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i + 1, j + 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }

                if (var63 || j > 1 && blockAccess.isAirBlock(i + 1, j - 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(dPosXMin, (double)(j + 1), dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMin, (double)(j + 0), dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, (double)(j + 0), dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, (double)(j + 1), dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, (double)(j + 1), dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, (double)(j + 0), dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMin, (double)(j + 0), dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMin, (double)(j + 1), dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);

            if (var62)
            {
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            }
            else
            {
                if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i - 1, j + 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                }

                if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i + 1, j + 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)(j + 1) + 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }
            }

            if (var63)
            {
                tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            }
            else
            {
                if (j > 1 && blockAccess.isAirBlock(i - 1, j - 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMin, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                }

                if (j > 1 && blockAccess.isAirBlock(i + 1, j - 1, k))
                {
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMid, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(dPosXMax, (double)j - 0.01D, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }
            }
        }

        if ((!var58 || !var59) && (var60 || var61 || var58 || var59))
        {
            if (var58 && !var59)
            {
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMin, dPaneTextureMinU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMin, dPaneTextureMinU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMin, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMin, dPaneTextureInterpolatedMidU, dPaneTextureMinV);

                if (!var61 && !var60)
                {
                    tessellator.addVertexWithUV(var50, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var52, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var50, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                }

                if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k - 1))
                {
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }

                if (var63 || j > 1 && blockAccess.isAirBlock(i, j - 1, k - 1))
                {
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }
            }
            else if (!var58 && var59)
            {
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMax, dPaneTextureMaxU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMax, dPaneTextureMaxU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMax, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMax, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
                tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);

                if (!var61 && !var60)
                {
                    tessellator.addVertexWithUV(var52, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var52, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var50, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 0), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1), dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                }

                if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k + 1))
                {
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                }

                if (var63 || j > 1 && blockAccess.isAirBlock(i, j - 1, k + 1))
                {
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMax, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMax, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMin, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMin, dPaneTextureMaxU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMin, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMin, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 0), dPosZMax, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, (double)(j + 1), dPosZMax, dPaneTextureMaxU, dPaneTextureMinV);

            if (var62)
            {
                tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            }
            else
            {
                if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k - 1))
                {
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }

                if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k + 1))
                {
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)(j + 1) + 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                }
            }

            if (var63)
            {
                tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMinV);
                tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            }
            else
            {
                if (j > 1 && blockAccess.isAirBlock(i, j - 1, k - 1))
                {
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
                }

                if (j > 1 && blockAccess.isAirBlock(i, j - 1, k + 1))
                {
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
                    tessellator.addVertexWithUV(var50, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
                    tessellator.addVertexWithUV(var52, (double)j - 0.005D, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
                }
            }
        }
        
        return true;
    }
    
    //------------- Lightning Rod Renderers ------------//
    
    public boolean RenderLightningRod
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
    	Icon texture = m_IconLightningRod;
    	
	   // render shaft
       
	   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodShaftHalfWidth, 0F, 0.5F - m_fLightningRodShaftHalfWidth,
		   0.5F + m_fLightningRodShaftHalfWidth, 1.0F, 0.5F + m_fLightningRodShaftHalfWidth );
   
	   FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, m_IconLightningRod );
	   
	   // render base
	   
	   if ( blockAccess.getBlockId( i, j - 1, k ) != FCBetterThanWolves.fcAestheticNonOpaque.blockID || 
		   blockAccess.getBlockMetadata( i, j - 1, k ) != FCBlockAestheticNonOpaque.m_iSubtypeLightningRod )
	   {
		   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodBaseHalfWidth, 0F, 0.5F - m_fLightningRodBaseHalfWidth,
			   0.5F + m_fLightningRodBaseHalfWidth, m_fLightningRodBaseHeight, 0.5F + m_fLightningRodBaseHalfWidth );
	   
		   FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, m_IconLightningRod );		   
	   }
	   
	   // render ball
	   
	   int iBlockAboveID = blockAccess.getBlockId( i, j + 1, k );
	   int iBlockAboveMetadata = blockAccess.getBlockMetadata( i, j + 1, k );
	   
	   if (  iBlockAboveID != FCBetterThanWolves.fcAestheticNonOpaque.blockID || 
		   iBlockAboveMetadata != FCBlockAestheticNonOpaque.m_iSubtypeLightningRod )
	   {
		   if ( iBlockAboveID == FCBetterThanWolves.fcBlockCandle.blockID )
		   {
			   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodCandleHolderHalfWidth, m_fLightningRodCandleHolderVeticalOffset, 0.5F - m_fLightningRodCandleHolderHalfWidth,
				   0.5F + m_fLightningRodCandleHolderHalfWidth, m_fLightningRodCandleHolderVeticalOffset + m_fLightningRodCandleHolderHeight, 0.5F + m_fLightningRodCandleHolderHalfWidth );
		   }
		   else
		   {
			   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodBallHalfWidth, m_fLightningRodBallVeticalOffset, 0.5F - m_fLightningRodBallHalfWidth,
				   0.5F + m_fLightningRodBallHalfWidth, m_fLightningRodBallVeticalOffset + m_fLightningRodBallWidth, 0.5F + m_fLightningRodBallHalfWidth );
		   }
		   
		   FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, m_IconLightningRod );
	   }
	   
	   return true;
    }
    
    public void RenderLightningRodInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
    	Icon texture = m_IconLightningRod;
    	
	   // render shaft
       
	   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodShaftHalfWidth, 0F, 0.5F - m_fLightningRodShaftHalfWidth,
		   0.5F + m_fLightningRodShaftHalfWidth, 1.0F, 0.5F + m_fLightningRodShaftHalfWidth );
   
	   FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );
	   
	   // render base
	   
	   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodBaseHalfWidth, 0F, 0.5F - m_fLightningRodBaseHalfWidth,
		   0.5F + m_fLightningRodBaseHalfWidth, m_fLightningRodBaseHeight, 0.5F + m_fLightningRodBaseHalfWidth );
   
	   FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );
	   
	   // render ball
	   
	   renderBlocks.setRenderBounds( 0.5F - m_fLightningRodBallHalfWidth, m_fLightningRodBallVeticalOffset, 0.5F - m_fLightningRodBallHalfWidth,
		   0.5F + m_fLightningRodBallHalfWidth, m_fLightningRodBallVeticalOffset + m_fLightningRodBallWidth, 0.5F + m_fLightningRodBallHalfWidth );
   
	   FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );
    }    
}