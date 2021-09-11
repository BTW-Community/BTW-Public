// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockAxle extends Block
{
	protected static final double m_dAxleWidth = 0.25D;
	protected static final double m_dAxleHalfWidth = ( m_dAxleWidth / 2D );
	
	static public final int iAxleTickRate = 1;
	
	protected static final int[][] m_AxleFacingsForAlignment = new int[][] { { 0, 1 }, { 2, 3 }, { 4, 5 } };

	protected FCBlockAxle( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );

        setHardness( 2F );        
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
    	InitBlockBounds( 0.5D - m_dAxleHalfWidth, 0.5D - m_dAxleHalfWidth, 0D,  
    		0.5D + m_dAxleHalfWidth, 0.5D + m_dAxleHalfWidth, 1D  );
    	
        setStepSound( soundWoodFootstep );
    	
        setUnlocalizedName( "fcBlockAxle" );        

        setCreativeTab( CreativeTabs.tabRedstone );
    }
	
	@Override
    public int tickRate( World world )
    {
    	return iAxleTickRate;
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
    	return SetAxisAlignmentInMetadataBasedOnFacing( iMetadata, iFacing );    	
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	// should only be called when the block is first added, to force it to validate
    	// its power level for the first time.  All following changes are instantaneous
    	
        SetPowerLevel( world, i, j, k, 0 );
        
    	ValidatePowerLevel( world, i, j, k );
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iAxis = GetAxisAlignment( blockAccess, i, j, k );
    	
    	switch ( iAxis )
    	{
    		case 0:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
		    		0.5D - m_dAxleHalfWidth, 0D, 0.5D - m_dAxleHalfWidth, 
		    		0.5D + m_dAxleHalfWidth, 1D, 0.5D + m_dAxleHalfWidth );
		    	
    		case 1:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
		    		0.5D - m_dAxleHalfWidth, 0.5D - m_dAxleHalfWidth, 0D,  
		    		0.5D + m_dAxleHalfWidth, 0.5D + m_dAxleHalfWidth, 1D  );
		    	
	    	default:
		    	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
		    		0D, 0.5D - m_dAxleHalfWidth, 0.5F - m_dAxleHalfWidth, 
		    		1D, 0.5D + m_dAxleHalfWidth, 0.5F + m_dAxleHalfWidth );
    	}
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	ValidatePowerLevel( world, i, j, k );
    }
    
	@Override
    public int getMobilityFlag()
    {
    	// set the block to be destroyed by pistons to avoid problems with the generation of infinite mechanical power loops
    	
        return 1;
    }
	
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	return IsAxleOrientedTowardsFacing( blockAccess, i, j, k, iFacing );
	}
    
	@Override
    public int GetMechanicalPowerLevelProvidedToAxleAtFacing( World world, int i, int j, int k, int iFacing )
    {
		int iAlignment = GetAxisAlignment( world, i, j, k );
		
		if ( ( iFacing >> 1 ) == iAlignment )
		{
			return GetPowerLevel( world, i, j, k );
		}
		
    	return 0;
    }
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemHempFibers.itemID, 4, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
	public int GetFacing( int iMetadata )
	{
    	return GetAxisAlignmentFromMetadata( iMetadata ) << 1;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		return SetAxisAlignmentInMetadataBasedOnFacing( iMetadata, iFacing );
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{	
		int iAxisAlignment = GetAxisAlignment( world, i, j, k );
		
		if ( !bReverse )
		{
			iAxisAlignment++;
			
			if ( iAxisAlignment > 2 )
			{
				iAxisAlignment = 0;
			}
		}
		else
		{
			iAxisAlignment--;
			
			if ( iAxisAlignment < 0 )
			{
				iAxisAlignment = 2;
			}
		}
		
		SetAxisAlignment( world, i, j, k, iAxisAlignment );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
        SetPowerLevel( world, i, j, k, 0 );
        
    	ValidatePowerLevel( world, i, j, k );
    	
        world.markBlockForUpdate( i, j, k );
        
    	return true;
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
    
    /* 
     * return value is 0 for y aligned (j), 1 for z (k), and 2 for x (i)
     */
    public int GetAxisAlignment( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) >> 2 );
    }
    
    /* 
     * return value is 0 for y aligned (j), 1 for z (k), and 2 for x (i)
     */
    public void SetAxisAlignment( World world, int i, int j, int k, int iAxisAlignment )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & 3; // filter out any old alignment
    	
    	iMetaData |= ( iAxisAlignment << 2 );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }

    /* 
     * return value is 0 for y aligned (j), 1 for z (k), and 2 for x (i)
     */
    public int GetAxisAlignmentFromMetadata( int iMetadata )
    {
    	return ( iMetadata >> 2 );
    }
    
    public void SetAxisAlignmentBasedOnFacing( World world, int i, int j, int k, int iFacing )
    {
    	int iAxis;
    	
    	switch ( iFacing )
    	{
    		case 0:
    		case 1:
    			
    			iAxis = 0;
    			
    			break;
    			
    		case 2:
    		case 3:
    			
    			iAxis = 1;
    			
    			break;
    			
    		default:
    			
    			iAxis = 2;
    			
    			break;    			
    	}
    	
    	int iMetaData = world.getBlockMetadata( i, j, k ) & 3; // filter out any old alignment
    	
    	iMetaData |= ( iAxis << 2 );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
    public int SetAxisAlignmentInMetadataBasedOnFacing( int iMetadata, int iFacing )
    {
    	int iAxis;
    	
    	switch ( iFacing )
    	{
    		case 0:
    		case 1:
    			
    			iAxis = 0;
    			
    			break;
    			
    		case 2:
    		case 3:
    			
    			iAxis = 1;
    			
    			break;
    			
    		default:
    			
    			iAxis = 2;
    			
    			break;    			
    	}
    	
    	iMetadata &= 3; // filter out any old alignment
    	
    	iMetadata |= ( iAxis << 2 );
    	
        return iMetadata;
    }
    
    public int GetPowerLevel( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return GetPowerLevelFromMetadata( iBlockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetPowerLevelFromMetadata( int iMetadata )
    {
    	return iMetadata & 3;
    }
    
    public void SetPowerLevel( World world, int i, int j, int k, int iPowerLevel )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	iMetadata = SetPowerLevelInMetadata( iMetadata, iPowerLevel );
    	
        world.setBlockMetadataWithNotifyNoClient( i, j, k, iMetadata );
    }
    
    public int SetPowerLevelInMetadata( int iMetadata, int iPowerLevel )
    {
    	iPowerLevel &= 3;
    	
    	iMetadata &= 12; // filter out any old power
    	
    	iMetadata |= iPowerLevel;
    	
    	return iMetadata;
    }
    
    public void SetPowerLevelWithoutNotify( World world, int i, int j, int k, int iPowerLevel )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	iMetadata = SetPowerLevelInMetadata( iMetadata, iPowerLevel );    	
    	
        world.setBlockMetadata( i, j, k, iMetadata );
    }
    
    public boolean IsAxleOrientedTowardsFacing( IBlockAccess iBlockAccess, int i, int j, int k, int iFacing )
    {
    	int iAxis = GetAxisAlignment( iBlockAccess, i, j, k );
    	
    	switch ( iAxis )
    	{
    		case 0:
    			
    			if ( iFacing == 0 || iFacing == 1 )
    			{
    				return true;
    			}
    			
    			break;
    			
    		case 1:
    			
    			if ( iFacing == 2 || iFacing == 3 )
    			{
    				return true;
    			}
    			
    			break;
    			
    		default: // 2
    			
    			if ( iFacing == 4 || iFacing == 5 )
    			{
    				return true;
    			}
    			
    			break;
    	}
    		
    	return false;
    }
    
	public void BreakAxle( World world, int i, int j, int k )
	{
		if ( world.getBlockId( i, j, k ) == blockID )
		{
			DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
			
	        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
	        
			world.setBlockWithNotify( i, j, k, 0 );
		}
	}

	protected void ValidatePowerLevel( World world, int i, int j, int k )
	{
		int iCurrentPower = GetPowerLevel( world, i, j, k );
		int iAxis = GetAxisAlignment( world, i, j, k );

		int iMaxNeighborPower = 0;
		int iGreaterPowerNeighbors = 0;
		
		for ( int iTempSourceIndex = 0; iTempSourceIndex < 2; iTempSourceIndex++ )
		{
			int iTempFacing = m_AxleFacingsForAlignment[iAxis][iTempSourceIndex];
			FCUtilsBlockPos tempSourcePos = new FCUtilsBlockPos( i, j, k, iTempFacing );
			                                 
			int iTempBlockID = world.getBlockId( tempSourcePos.i, tempSourcePos.j, tempSourcePos.k );
			
			if ( iTempBlockID != 0 )
			{
				Block tempBlock = Block.blocksList[iTempBlockID];
				
				int iTempPowerLevel = tempBlock.GetMechanicalPowerLevelProvidedToAxleAtFacing( 
					world, tempSourcePos.i, tempSourcePos.j, tempSourcePos.k, Block.GetOppositeFacing( iTempFacing ) );
				
				if ( iTempPowerLevel > iMaxNeighborPower )
				{
					iMaxNeighborPower = iTempPowerLevel;
				}
				
				if ( iTempPowerLevel > iCurrentPower )
				{
					iGreaterPowerNeighbors++;
				}
			}			
		}
		
		if ( iGreaterPowerNeighbors >= 2 )
		{
			// we're receiving power from more than one direction at once
			
			BreakAxle( world, i, j, k );
			
			return;
		}

		int iNewPower = iCurrentPower;
		
		if ( iMaxNeighborPower > iCurrentPower )
		{
			if ( iMaxNeighborPower == 1 )
			{
				// the power has overextended.  Break this axle.
				
				BreakAxle( world, i, j, k );
				
				return;
			}
			
			iNewPower = iMaxNeighborPower - 1;
		}
		else
		{
			iNewPower = 0;
		}

		if ( iNewPower != iCurrentPower )
		{
			SetPowerLevel( world, i, j, k, iNewPower );
		}
	}
	
    private void EmitAxleParticles( World world, int i, int j, int k, Random random )
    {
        for ( int counter = 0; counter < 2; counter++ )
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 0.625F;
            float smokeZ = (float)k + random.nextFloat();
            
            world.spawnParticle( "smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D );
        }
    }
    
    public void Overpower( World world, int i, int j, int k )
    {
		int iAxis = GetAxisAlignment( world, i, j, k );
		
		switch( iAxis )
		{
    		case 0:

    			OverpowerBlockToFacing( world, i, j, k, iAxis, 0 );
    			OverpowerBlockToFacing( world, i, j, k, iAxis, 1 );
    			
    			break;
    			
    		case 1:
    			
    			OverpowerBlockToFacing( world, i, j, k, iAxis, 2 );
    			OverpowerBlockToFacing( world, i, j, k, iAxis, 3 );
    			
    			break;
    			
    		default: // 2
    			
    			OverpowerBlockToFacing( world, i, j, k, iAxis, 4 );
    			OverpowerBlockToFacing( world, i, j, k, iAxis, 5 );
    			
    			break;
		}		
    }
    
    private void OverpowerBlockToFacing( World world, int i, int j, int k, int iSourceAxis, int iFacing )
    {
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
    	targetPos.AddFacingAsOffset( iFacing );
    	
		int iTempBlockID = world.getBlockId( targetPos.i, 
			targetPos.j, targetPos.k );
		
		if ( iTempBlockID == FCBetterThanWolves.fcBlockAxle.blockID || iTempBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
		{
			int iTempAxis = GetAxisAlignment( world, targetPos.i, 
				targetPos.j, targetPos.k );
			
			if ( iTempAxis == iSourceAxis )
			{
				OverpowerBlockToFacing( world, targetPos.i, targetPos.j, targetPos.k, iSourceAxis, iFacing );
			}
		}
		else if ( Block.blocksList[iTempBlockID] instanceof FCIBlockMechanical )
		{
			FCIBlockMechanical mechDevice = (FCIBlockMechanical)(Block.blocksList[iTempBlockID]);
			
			if ( mechDevice.CanInputAxlePowerToFacing( world, targetPos.i, 
				targetPos.j, targetPos.k, Block.GetOppositeFacing( iFacing ) ) )
			{				
				mechDevice.Overpower( world, targetPos.i, targetPos.j, targetPos.k );
			}
		}
    }
    
	//----------- Client Side Functionality -----------//
	
    public Icon m_IconSide;
    public Icon m_IconSideOn;
    public Icon m_IconSideOnOverpowered;
    
    public boolean m_bIsPowerOnForCurrentRender; // temporary state variable assigned as each block is rendered
    public boolean m_bIsOverpoweredForCurrentRender; // temporary state variable assigned as each block is rendered

	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockAxle_end" );
        
        m_IconSide = register.registerIcon( "fcBlockAxle_side" );
        m_IconSideOn = register.registerIcon( "fcBlockAxle_side_on" );
        m_IconSideOnOverpowered = register.registerIcon( "fcBlockAxle_side_on_fast" );
    }

	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	// used by item render
    	
		if ( iSide != 2 && iSide != 3 )
		{
			return m_IconSide;
		}
		
		return blockIcon;
    }
	
	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
	{
		int iAxisAlignment = GetAxisAlignment( blockAccess, i, j, k );
		
    	if ( iAxisAlignment == 0 )
    	{
    		if ( iSide >= 2 )
    		{
    			return GetAxleSideTextureForOnState( m_bIsPowerOnForCurrentRender );
    		}
    	}
    	else if ( iAxisAlignment == 1 )
    	{
    		if ( iSide != 2 && iSide != 3 )
    		{
    			return GetAxleSideTextureForOnState( m_bIsPowerOnForCurrentRender );
    		}
    	}
    	else if ( iSide < 4 )
		{
			return GetAxleSideTextureForOnState( m_bIsPowerOnForCurrentRender );
		}
    	
    	return blockIcon;
	}
	
    public Icon GetAxleSideTextureForOnState( boolean bIsOn )
    {
    	if ( bIsOn )
    	{
    		return m_IconSideOn;
    	}
    	
		return m_IconSide;
    }
    
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
    	if ( ClientCheckIfPowered( world, i, j, k ) )
    	{
    		EmitAxleParticles( world, i, j, k, random );
    		
	        if ( random.nextInt( 200 ) == 0 )
	        {
	            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	        		"random.chestopen", 0.075F, random.nextFloat() * 0.1F + 0.5F);
	        }
    	}
    }
	
	public boolean ClientCheckIfPowered( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iCurrentPower = GetPowerLevel( blockAccess, i, j, k );
		int iAxis = GetAxisAlignment( blockAccess, i, j, k );

		for ( int iTempFacingIndex = 0; iTempFacingIndex < 2; iTempFacingIndex++ )
		{
			FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
			int iFacingOfCheck = m_AxleFacingsForAlignment[iAxis][iTempFacingIndex];
			
			for ( int iTempDistance = 1; iTempDistance <= 3; iTempDistance++ )
			{
				targetPos.AddFacingAsOffset( iFacingOfCheck );
				
				int iTempBlockID = blockAccess.getBlockId( targetPos.i, 
					targetPos.j, targetPos.k );
				
				if ( iTempBlockID == blockID && GetAxisAlignment( blockAccess, targetPos.i, 
						targetPos.j, targetPos.k ) == iAxis )
				{
					// we've found another aligned axle, just continue on our merry way
				}
				else if ( iTempBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID && GetAxisAlignment( blockAccess, targetPos.i, 
					targetPos.j, targetPos.k ) == iAxis )
				{
					return true;
				}
				else
				{
					if ( FCUtilsMechPower.IsPoweredGearBox( blockAccess, 
						targetPos.i, targetPos.j, targetPos.k ) )
					{
						// we've found a powered Gear Box hooked up to this axle, 
						// so the axle should be considered to be on
						
						return true;
					}
					
					break;
				}
			}
		}
		
		return false;
	}
	
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	FCUtilsBlockPos myPos = new FCUtilsBlockPos( 
    		iNeighborI, iNeighborJ, iNeighborK, GetOppositeFacing( iSide ) );

    	if ( IsAxleOrientedTowardsFacing( blockAccess, myPos.i, myPos.j, myPos.k, iSide ) )
    	{
    		int iNeighborBlockID = blockAccess.getBlockId( iNeighborI, iNeighborJ, iNeighborK );
    		
    		if ( iNeighborBlockID == blockID )
    		{
    			if ( GetAxisAlignment( blockAccess, myPos.i, myPos.j, myPos.k ) ==
    				GetAxisAlignment( blockAccess, iNeighborI, iNeighborJ, iNeighborK ) )
    			{
    				return false;
    			}
    		}
    		else
    		{
    			return FCClientUtilsRender.ShouldRenderNeighborFullFaceSide( blockAccess,
    				iNeighborI, iNeighborJ, iNeighborK, iSide );
    		}
    	}
    	
    	return true;
    }
    
	@Override
    public void ClientBreakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
	{
        world.markBlockRangeForRenderUpdate( i - 3, j - 3, k - 3, i + 3, j + 3, k + 3);
	}
	
	@Override
    public void ClientBlockAdded( World world, int i, int j, int k )
	{
        world.markBlockRangeForRenderUpdate( i - 3, j - 3, k - 3, i + 3, j + 3, k + 3);
	}
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
    	int iAlignment = GetAxisAlignment( blockAccess, i, j, k );
    	
    	if ( iAlignment == 0 )
    	{
    		renderer.SetUvRotateEast( 1 );
    		renderer.SetUvRotateWest( 1 );
    		renderer.SetUvRotateSouth( 1 );
    		renderer.SetUvRotateNorth( 1 );
    		renderer.SetUvRotateTop( 0 );
    		renderer.SetUvRotateBottom( 0 );
    	}
    	else if ( iAlignment == 1 )
    	{
    		renderer.SetUvRotateEast( 0 );
    		renderer.SetUvRotateWest( 0 );
    		renderer.SetUvRotateSouth( 0 );
    		renderer.SetUvRotateNorth( 3 );
    		renderer.SetUvRotateTop( 2 );
    		renderer.SetUvRotateBottom( 2 );
    	}
    	else // 2
    	{
    		renderer.SetUvRotateEast( 0 );
    		renderer.SetUvRotateWest( 3 );
    		renderer.SetUvRotateSouth( 0 );
    		renderer.SetUvRotateNorth( 0 );
    		renderer.SetUvRotateTop( 3 );
    		renderer.SetUvRotateBottom( 0 );
    	}
    	
    	if ( ClientCheckIfPowered( blockAccess, i, j, k ) )
    	{
    		m_bIsPowerOnForCurrentRender = true;
    	}
    	else
    	{
    		m_bIsPowerOnForCurrentRender = false;
    	}

        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	renderer.renderStandardBlock( this, i, j, k );
    	
		renderer.ClearUvRotation();
		
    	return true;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderer, int iItemDamage, float fBrightness )
    {
    	renderer.setRenderBounds( GetBlockBoundsFromPoolForItemRender( iItemDamage ) );
    	
		renderer.SetUvRotateEast( 0 );
		renderer.SetUvRotateWest( 0 );
		renderer.SetUvRotateSouth( 0 );
		renderer.SetUvRotateNorth( 3 );
		renderer.SetUvRotateTop( 2 );
		renderer.SetUvRotateBottom( 2 );
		
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderer, this, 
        	-0.5F, -0.5F, -0.5F, iItemDamage );
        
        renderer.ClearUvRotation();
    }
}
