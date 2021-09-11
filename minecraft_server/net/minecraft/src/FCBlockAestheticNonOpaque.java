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
}