// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockLogDamaged extends Block
{
    public final static float m_fHardness = 2F;
    
    private FCModelBlock m_blockModels[];
    private FCModelBlock m_blockModelsNarrowOneSide[];
    private FCModelBlock m_blockModelsNarrowTwoSides[];
    
    private AxisAlignedBB m_boxSelectionArray[];
    
    public FCBlockLogDamaged( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );
        
        setHardness( m_fHardness );
		SetAxesEffectiveOn();
		SetChiselsEffectiveOn();
        
        SetBuoyant();
        
        SetFireProperties( 5, 5 );
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );

        InitModels();       
        
        Block.useNeighborBrightness[iBlockID] = true;        
        setLightOpacity( 8 );
        
        setStepSound( soundWoodFootstep );        

        setUnlocalizedName( "fcBlockLogDamaged" );
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
    public float getBlockHardness( World world, int i, int j, int k )
    {
    	float fHardness = super.getBlockHardness( world, i, j, k );
    	
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( GetIsStump( world, i, j, k ) )
    	{   
    		fHardness *= 3;    		
    	}
    	
        return fHardness; 
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		if ( iFacing <= 1 )
		{
			iMetadata = SetOrientation( iMetadata, 0 );
		}
		else if ( iFacing <= 3 )
		{
			iMetadata = SetOrientation( iMetadata, 2 );
		}
		else
		{
			iMetadata = SetOrientation( iMetadata, 1 );
		}
		
		return iMetadata;
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	CheckForReplaceWithSpike( world, i, j, k, iMetadata );
	}
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	int iFacing = SetCurrentModelForBlock( world, i, j, k );
    	
    	FCModelBlock transformedModel = m_tempCurrentModel.MakeTemporaryCopy();
    	
    	transformedModel.TiltToFacingAlongJ( iFacing );
    	
    	return transformedModel.CollisionRayTrace( world, i, j, k, startRay, endRay );
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iOrientation = GetOrientation( blockAccess, i, j, k );
    	
    	if ( iOrientation == 0 )
    	{
    		return iFacing <= 1;
    	}
    	else if ( iOrientation == 1 )
    	{
    		return iFacing >= 4;
    	}
    	else // 2
    	{
    		return iFacing == 2 || iFacing == 3;
    	}
	}
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	boolean bIsStump = GetIsStump( iMetadata );
    	
    	if ( bIsStump )
    	{
            dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust ) );
    	}
    	
		// last item dropped is always saw dust to encourage player to keep some chewed logs around decoratively 
    	
        dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust ) );
    }
		
    @Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	int iOldMetadata = world.getBlockMetadata( i, j, k );
    	int iDamageLevel = GetDamageLevel( iOldMetadata );
    	
    	if ( iDamageLevel < 3 )
    	{
    		iDamageLevel++;

    		SetDamageLevel( world, i, j, k, iDamageLevel );
    		
        	if ( !world.isRemote )
        	{
        		if ( GetIsStump( iOldMetadata ) )
        		{
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust, 1 ), iFromSide );
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust, 1 ), iFromSide );
        		}
        		else if ( iDamageLevel == 1 || iDamageLevel == 3 )
	            {
        	        world.playAuxSFX( FCBetterThanWolves.m_iShaftRippedOffLogAuxFXID, i, j, k, 0 );							        
        	        
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, new ItemStack( Item.stick, 1 ), iFromSide );
	            }
	            else
	            {
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust, 1 ), iFromSide );
	            }
        	}
            	
    		return true;
    	}
    	else if ( !world.isRemote )
    	{
    		if ( GetIsStump( iOldMetadata ) )
    		{
                FCUtilsItem.DropStackAsIfBlockHarvested( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust, 1 ) );
    		}
    		
            FCUtilsItem.DropStackAsIfBlockHarvested( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust, 1 ) );
    	}
        
    	return false;
    }
    
    @Override
    public boolean GetIsProblemToRemove( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsStump( blockAccess, i, j, k );
    }
    
    @Override
    public boolean GetDoesStumpRemoverWorkOnBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsStump( blockAccess, i, j, k );
    }
    
	@Override
    public boolean canDropFromExplosion( Explosion explosion )
    {
        return false;
    }
    
	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		float fChanceOfPileDrop = 1.0F;
		
		if ( explosion != null )
		{
			fChanceOfPileDrop = 1.0F / explosion.explosionSize;
		}
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 1, 0, fChanceOfPileDrop );
    }
	
    @Override
    public boolean GetCanBlockBeIncinerated( World world, int i, int j, int k )
    {
    	return !GetIsStump( world, i, j, k );
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 1000; // always convert, never harvest
    }
    
	@Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
		if ( GetIsStump( world, i, j, k ) )
		{
			int iNewMetadata = FCBetterThanWolves.fcBlockStumpCharred.SetDamageLevel( 0, GetDamageLevel( world, i, j, k ) );
			
			world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockStumpCharred.blockID, iNewMetadata );
		}
		else
		{
			super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
		}
    }
	
    //------------- Class Specific Methods ------------//
    
    private final static float m_fRimWidth = ( 1F / 16F );
    
    private final static float m_fLayerHeight = ( 2F / 16F );    
    private final static float m_fFirstLayerHeight = ( 3F / 16F );    
    private final static float m_fLayerWidthGap = ( 1F / 16F );
    
    private boolean m_bTempPosNarrow = false;
    private boolean m_bTempNegNarrow = false;
	
    private FCModelBlock m_tempCurrentModel;
    
    protected void InitModels()
    {
        m_blockModels = new FCModelBlock[4];
        m_blockModelsNarrowOneSide = new FCModelBlock[4];
        m_blockModelsNarrowTwoSides = new FCModelBlock[4];
        
        m_boxSelectionArray = new AxisAlignedBB[4];

        // center colum
        
        for ( int iTempIndex = 0; iTempIndex < 4; iTempIndex++ )
        {
        	FCModelBlock tempModel = m_blockModels[iTempIndex] = new FCModelBlock();
        	FCModelBlock tempNarrowOneSide = m_blockModelsNarrowOneSide[iTempIndex] = new FCModelBlock();
        	FCModelBlock tempNarrowTwoSides = m_blockModelsNarrowTwoSides[iTempIndex] = new FCModelBlock();
        	
            float fCenterColumnWidthGap = m_fRimWidth + ( m_fLayerWidthGap * iTempIndex );
            float fCenterColumnHeightGap = 0F;
            
            if ( iTempIndex > 0 )
            {
            	fCenterColumnHeightGap = m_fFirstLayerHeight + ( m_fLayerHeight * ( iTempIndex - 1 ) );
            }

            tempModel.AddBox( fCenterColumnWidthGap, fCenterColumnHeightGap, fCenterColumnWidthGap, 
            	1F - fCenterColumnWidthGap, 1F - fCenterColumnHeightGap, 1F - fCenterColumnWidthGap );

            tempNarrowOneSide.AddBox( fCenterColumnWidthGap, fCenterColumnHeightGap, fCenterColumnWidthGap, 
            	1F - fCenterColumnWidthGap, 1F, 1F - fCenterColumnWidthGap );
            
            tempNarrowTwoSides.AddBox( fCenterColumnWidthGap, 0, fCenterColumnWidthGap, 
            	1F - fCenterColumnWidthGap, 1F, 1F - fCenterColumnWidthGap );
            
        	AxisAlignedBB tempSelection = m_boxSelectionArray[iTempIndex] = new AxisAlignedBB( 
        		fCenterColumnWidthGap, 0, fCenterColumnWidthGap, 
            	1F - fCenterColumnWidthGap, 1F, 1F - fCenterColumnWidthGap );
        }
        
        // first layer
        
        for ( int iTempIndex = 1; iTempIndex < 4; iTempIndex++ )
        {
        	// bottom
        	
	        m_blockModels[iTempIndex].AddBox( m_fRimWidth, 0, m_fRimWidth, 1F - m_fRimWidth, m_fFirstLayerHeight, 1F - m_fRimWidth );
	        m_blockModelsNarrowOneSide[iTempIndex].AddBox( m_fRimWidth, 0, m_fRimWidth, 1F - m_fRimWidth, m_fFirstLayerHeight, 1F - m_fRimWidth );
	        
        	// top
	        
	        m_blockModels[iTempIndex].AddBox( m_fRimWidth, 1F - m_fFirstLayerHeight, m_fRimWidth, 1F - m_fRimWidth, 1F, 1F - m_fRimWidth );
	        
        }
        
        // second layer 
        
        float fWidthGap = m_fRimWidth + m_fLayerWidthGap;
        float fHeightGap = m_fFirstLayerHeight;
        
        for ( int iTempIndex = 2; iTempIndex < 4; iTempIndex++ )
        {
        	// second layer bottom
            
	        m_blockModels[iTempIndex].AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
	        m_blockModelsNarrowOneSide[iTempIndex].AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
	        
        	// second layer top
            
	        m_blockModels[iTempIndex].AddBox( fWidthGap, 1F - fHeightGap - m_fLayerHeight, fWidthGap, 1F - fWidthGap, 1F - fHeightGap, 1F - fWidthGap );
        }
        
    	// third layer bottom
        
        fWidthGap = m_fRimWidth + ( m_fLayerWidthGap * 2 );
        fHeightGap = m_fFirstLayerHeight + m_fLayerHeight;
        
        m_blockModels[3].AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
        m_blockModelsNarrowOneSide[3].AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
        
    	// third layer top
        
        m_blockModels[3].AddBox( fWidthGap, 1F - fHeightGap - m_fLayerHeight, fWidthGap, 1F - fWidthGap, 1F - m_fLayerHeight, 1F - fWidthGap );        
    }
    
    public void SetDamageLevel( World world, int i, int j, int k, int iDamageLevel )
    {
    	int iMetadata = ( world.getBlockMetadata( i, j, k ) ) & (~3);
    	
    	iMetadata |= iDamageLevel;
    	
    	if ( !CheckForReplaceWithSpike( world, i, j, k, iMetadata ) )
    	{
    		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    	}
    }
    
    private boolean CheckForReplaceWithSpike( World world, int i, int j, int k, int iMetadata )
    {
    	if ( GetDamageLevel( iMetadata ) == 3 && !GetIsStump( iMetadata ) )
    	{
    		int iFacing = SetConnectionFlagsForBlock( world, i, j, k, iMetadata );
    		
    		if ( m_bTempPosNarrow != m_bTempNegNarrow )
    		{
    	    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    	
    	    	targetPos.AddFacingAsOffset( iFacing );
    	    	
    	    	int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	    	Block targetBlock = Block.blocksList[iTargetBlockID];
    	    	
    	    	if ( iTargetBlockID != this.blockID || 
    	    		GetOrientation( iMetadata ) != GetOrientation( world, targetPos.i, targetPos.j, targetPos.k ) )
    	    	{
        			world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockLogSpike.blockID, iFacing );
        			
        			return true;
    	    	}
    		}
    	}
    	
    	return false;
    }
    
    public int GetDamageLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetDamageLevel( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetDamageLevel( int iMetadata )
    {
    	return iMetadata & 3;
    }
    
    /**
     * Uses same orientation as BlockLog.  0 is J aligned.  1 is I aligned.  2 is K aligned.
     */
    public int GetOrientation( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetOrientation( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetOrientation( int iMetadata )
    {
    	int iOrientation = ( iMetadata >> 2 ) & 3;
    	
    	if ( iOrientation == 3 ) // stump
    	{
    		iOrientation = 0;
    	}
    	
    	return iOrientation;
    }
    
    public int SetOrientation( int iMetadata, int iOrientation )
    {
    	if ( !GetIsStump( iMetadata ) )
    	{
    		iMetadata |=  ( iOrientation << 2 );
    	}
    	
    	return iMetadata; 
    }    
    
    static public boolean GetIsStump( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsStump( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    static public boolean GetIsStump( int iMetadata )
    {
    	return ( iMetadata & 12 ) == 12;
    }
    
    public int SetIsStump( int iMetadata )
    {
    	iMetadata |= 12;
    	
    	return iMetadata;
    }
    
    public int SetCurrentModelForBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iFacing = SetConnectionFlagsForBlock( blockAccess, i, j, k );
    	int iDamageLevel = GetDamageLevel( blockAccess, i, j, k );
    	
    	if ( m_bTempPosNarrow )
    	{
    		if ( m_bTempNegNarrow )
    		{
				m_tempCurrentModel = m_blockModelsNarrowTwoSides[iDamageLevel];
    		}
    		else
    		{
				m_tempCurrentModel = m_blockModelsNarrowOneSide[iDamageLevel];
    		}
    	}
    	else
    	{
        	m_tempCurrentModel = m_blockModels[iDamageLevel];
        	
    	}
    	
    	return iFacing;
    }

    public int SetConnectionFlagsForBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return SetConnectionFlagsForBlock( blockAccess, i, j, k, blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int SetConnectionFlagsForBlock( IBlockAccess blockAccess, int i, int j, int k, int iMetadata )
    {
    	int iOrientation = GetOrientation( iMetadata );
    	int iFacing = 1;
    	
    	if ( iOrientation == 1 )
    	{
    		iFacing = 5;
		}    	
    	else if ( iOrientation == 2 )
    	{
    		iFacing = 3;
    	}

    	m_bTempPosNarrow = true;
    	m_bTempNegNarrow = true;    	
    	
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    	
    	targetPos.AddFacingAsOffset( iFacing );
    	
    	int iTargetBlockID = blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	Block targetBlock = Block.blocksList[iTargetBlockID];

    	if ( DoesTargetBlockConnectToFacing( iOrientation, blockAccess, 
    		targetPos.i, targetPos.j, targetPos.k, Block.GetOppositeFacing( iFacing ) ) )
		{
    		m_bTempPosNarrow = false;
		}
    	
    	targetPos.Set( i, j, k );
    	targetPos.AddFacingAsOffset( Block.GetOppositeFacing( iFacing ) );
    	
    	iTargetBlockID = blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	targetBlock = Block.blocksList[iTargetBlockID];

    	if ( GetIsStump( iMetadata ) || DoesTargetBlockConnectToFacing( iOrientation, blockAccess, 
    		targetPos.i, targetPos.j, targetPos.k, iFacing ) )
		{
    		m_bTempNegNarrow = false;
    	}
    	
    	if ( m_bTempPosNarrow == false && m_bTempNegNarrow == true )
    	{
    		iFacing = Block.GetOppositeFacing( iFacing );
    		
    		m_bTempPosNarrow = true;
    		m_bTempNegNarrow = false;
    	}
    	
    	return iFacing;
    }
    
    public boolean DoesTargetBlockConnectToFacing( int iMyOrientation, IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {    	
    	int iTargetBlockID = blockAccess.getBlockId( i, j, k );
    	
		if ( iTargetBlockID == blockID )
		{
			return GetDamageLevel( blockAccess, i, j, k ) == 0 && iMyOrientation == GetOrientation( blockAccess, i, j, k );
		}
		else if ( iTargetBlockID == FCBetterThanWolves.fcBlockLogSpike.blockID )
		{
			return FCBetterThanWolves.fcBlockLogSpike.GetFacing( blockAccess, i, j, k ) == 
				Block.GetOppositeFacing( iFacing ); 
		}
		else if ( iTargetBlockID == Block.wood.blockID )
		{
			return true;
		}
    		
    	return false;
    }
    
    public boolean IsItemEffectiveConversionTool( ItemStack stack, World world, int i, int j, int k )
    {
    	if ( stack != null )
    	{
    		Item item = stack.getItem();
    		
	    	if ( item instanceof FCItemChisel || item instanceof FCItemAxe || item == FCBetterThanWolves.fcItemBattleAxe )
	    	{
	    		return true;
	    	}
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
}  
