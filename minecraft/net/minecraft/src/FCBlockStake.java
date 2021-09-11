// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStake extends Block
{
	private final double m_dBlockHeight = ( 10D / 16D );
	private final double m_dBlockWidth = ( 4D / 16D );
	private final double m_dBlockHalfWidth = ( m_dBlockWidth / 2D );
	
    public FCBlockStake( int iBlockID )
    {
        super( iBlockID, Material.wood );

        setHardness( 2F );
        setResistance( 5F );    	
        SetAxesEffectiveOn( true );        

		SetFireProperties( FCEnumFlammability.PLANKS );
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockStake" );        
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
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	for ( int iFacing = 0; iFacing < 6; iFacing++ )
    	{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    		
    		if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, 
    			Block.GetOppositeFacing( iFacing ) ) )
    		{
    			return true;
    		}
    	}
    	
    	return false;    	
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );
		
		if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, iFacing ) )
		{
			iFacing = FindValidFacing( world, i, j, k );
		}		
		
        return SetFacing( iMetadata, iFacing );        
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(
        	0.5D - m_dBlockHalfWidth, 0D, 0.5D - m_dBlockHalfWidth,
        	0.5D + m_dBlockHalfWidth, m_dBlockHeight, 0.5D + m_dBlockHalfWidth );
        
        box.TiltToFacingAlongJ( GetFacing( blockAccess, i, j, k ) );
        
        return box;
    }
	
	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemStake.itemID;
    }

	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	// pop the block off if it no longer has a valid anchor point
    	
    	int iFacing = GetFacing( world, i, j, k );
    	
		FCUtilsBlockPos anchorPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );
		
		if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, anchorPos.i, anchorPos.j, anchorPos.k, iFacing ) )			
    	{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
    	}    		
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	ItemStack equippedItem = player.getCurrentEquippedItem();
    	
		int iTargetFacing = Block.GetOppositeFacing( FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacingReversed( player ) );
    	
		if ( HasConnectedStringToFacing( world, i, j, k, iTargetFacing ) )
		{
			// remove the connected string and drop it at the player's feet

			if ( !world.isRemote )
			{
				int iStringCount = ClearStringToFacingNoDrop( world, i, j, k, iTargetFacing );
				
				if ( iStringCount > 0 )
				{
					FCUtilsItem.DropStackAsIfBlockHarvested( world, i, j, k, new ItemStack( Item.silk.itemID, iStringCount, 0 ) );
				}
				
	            world.playSoundAtEntity( player, "random.bow", 0.25F, world.rand.nextFloat() * 0.4F + 1.2F );
			}
			
			return true;
		}
		else if ( equippedItem != null && equippedItem.getItem().itemID == Item.silk.itemID )
    	{
			int iStringStackSize = equippedItem.stackSize;
			
			if ( iStringStackSize > 0 )
			{
				int iStakeFacing = GetFacing( world, i, j, k );
				
				// scan in the chosen direction for another stake

				int iDistanceToOtherStake = CheckForValidConnectingStakeToFacing( world, i, j, k, iTargetFacing, iStringStackSize );
				
				if ( iDistanceToOtherStake <= 0 )
				{
					// check alternate facings as we didn't find anything in the primary
					
				    int iYawOctant = MathHelper.floor_double( (double)( ( player.rotationYaw * 8F ) / 360F ) ) & 7;
				    
				    if ( iYawOctant >= 0 && iYawOctant <= 3 )
				    {
				    	iTargetFacing = 4;
				    }
				    else
				    {
						iTargetFacing = 5;
				    }
				    
					iDistanceToOtherStake = CheckForValidConnectingStakeToFacing( world, i, j, k, iTargetFacing, iStringStackSize );
					
					if ( iDistanceToOtherStake <= 0 )
					{
					    if ( iYawOctant >= 2 && iYawOctant <= 5 )
					    {
					    	iTargetFacing = 2;
					    }
					    else
					    {
					    	iTargetFacing = 3;
					    }
					}
					
					iDistanceToOtherStake = CheckForValidConnectingStakeToFacing( world, i, j, k, iTargetFacing, iStringStackSize );
					
					// scan alternate facings
					
					if ( iDistanceToOtherStake <= 0 )
					{
						if ( player.rotationPitch > 0 )
						{
					    	iTargetFacing = 0;
						}
						else
						{
					    	iTargetFacing = 1;
						}
						
						iDistanceToOtherStake = CheckForValidConnectingStakeToFacing( world, i, j, k, iTargetFacing, iStringStackSize );
					}
				}
				
				if ( iDistanceToOtherStake > 0 )
				{
					if ( !world.isRemote )
					{
						// place the string blocks
						
						FCBlockStakeString stringBlock = (FCBlockStakeString)(FCBetterThanWolves.fcBlockStakeString);
						
						FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
						
						for ( int iTempDistance = 0; iTempDistance < iDistanceToOtherStake; iTempDistance++ )
						{
							tempPos.AddFacingAsOffset( iTargetFacing );
							
							int iTargetBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
	
							if ( iTargetBlockID != stringBlock.blockID )
							{
								// no notify here as it will disrupt the strings still being placed
								
								world.setBlock( tempPos.i, tempPos.j, tempPos.k, stringBlock.blockID, 0, 2 );
							}
							
							stringBlock.SetExtendsAlongFacing( world, tempPos.i, tempPos.j, tempPos.k, iTargetFacing, true, false );
							
			                if ( !player.capabilities.isCreativeMode )
			                {
			                	equippedItem.stackSize--;
			                }
						}
						
						// cycle back through and give block change notifications
						
						tempPos = new FCUtilsBlockPos( i, j, k );
						
						for ( int iTempDistance = 0; iTempDistance < iDistanceToOtherStake; iTempDistance++ )
						{
							tempPos.AddFacingAsOffset( iTargetFacing );
							
							world.notifyBlockChange( tempPos.i, tempPos.j, tempPos.k, FCBetterThanWolves.fcBlockStakeString.blockID );
						}
						
			            world.playSoundAtEntity( player, "random.bow", 0.25F, world.rand.nextFloat() * 0.2F + 0.8F );
					}
					else
					{
		                if ( !player.capabilities.isCreativeMode )
		                {
		                	equippedItem.stackSize -= iDistanceToOtherStake;
		                }
					}
				}
				
				return true;
			}
    	}
    	
    	return false;
    }
	
	@Override
	public int GetFacing( int iMetadata )
	{
		// stake facing is opposite attachment point
		
    	return ( iMetadata & 7 );
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata &= ~7; // filter out old facing
    	
    	iMetadata |= iFacing;
    	
        return iMetadata;
	}
	
	@Override
    public boolean CanRotateAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		return iFacing == Block.GetOppositeFacing( GetFacing( world, i, j, k ) );
    }
    
	@Override
    public int GetNewMetadataRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		return SetFacing( iMetadata, Block.GetOppositeFacing( iRotatedFacing ) );
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
	 * returns the distance to the valid stake in the direction, 0 otherwise
	 */
	private int CheckForValidConnectingStakeToFacing( World world, int i, int j, int k, int iFacing, int iMaxDistance )
	{
		FCBlockStakeString stringBlock = (FCBlockStakeString)(FCBetterThanWolves.fcBlockStakeString);		
		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
		boolean bFoundOtherStake = false;
		
		for ( int iDistanceToOtherStake = 0; iDistanceToOtherStake <= iMaxDistance; iDistanceToOtherStake++ )
		{
			tempPos.AddFacingAsOffset( iFacing );
			
			if ( !world.isAirBlock( tempPos.i, tempPos.j, tempPos.k ) )
			{
				int iTargetBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
				
				if ( iTargetBlockID == blockID )
				{
					return iDistanceToOtherStake;
				}
				else if ( iTargetBlockID == stringBlock.blockID )
				{
					if ( stringBlock.GetExtendsAlongFacing( world, tempPos.i, tempPos.j, tempPos.k, iFacing ) )
					{
						return 0;
					}    									
				}
				else
				{
					Block tempBlock = Block.blocksList[iTargetBlockID];
					
					if ( !tempBlock.blockMaterial.isReplaceable() || tempBlock.blockMaterial.isLiquid() )
					{
						return 0;
					}
				}
			}    						
		}
		
		return 0;
	}
    	
	private int FindValidFacing( World world, int i, int j, int k )
	{
    	for ( int iFacing = 0; iFacing < 6; iFacing++ )
    	{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    		
    		if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, 
    			Block.GetOppositeFacing( iFacing ) ) )
    		{
    			return Block.GetOppositeFacing( iFacing );
    		}
    	}
    	
    	return 0;    	
	}
	
	public boolean HasConnectedStringToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{		
        FCBlockStakeString stringBlock = (FCBlockStakeString)( FCBetterThanWolves.fcBlockStakeString );
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
    	targetPos.AddFacingAsOffset( iFacing );
    	
    	int iTargetBlockID = blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	
    	if ( iTargetBlockID == stringBlock.blockID )
    	{
    		return stringBlock.GetExtendsAlongFacing( blockAccess, targetPos.i, targetPos.j, targetPos.k, iFacing );
    	}
    	
		return false;
	}
	
	private int ClearStringToFacingNoDrop( World world, int i, int j, int k, int iTargetFacing )
	{
		int iStringCount = 0;
		
		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k ); 
		
		do
		{
			tempPos.AddFacingAsOffset( iTargetFacing );
			
			if ( world.getBlockId( tempPos.i, tempPos.j, tempPos.k ) == FCBetterThanWolves.fcBlockStakeString.blockID )
			{
				FCBlockStakeString stringBlock = (FCBlockStakeString)FCBetterThanWolves.fcBlockStakeString;
				
				if ( stringBlock.GetExtendsAlongFacing( world, tempPos.i, tempPos.j, tempPos.k, iTargetFacing ) )
				{
					if ( stringBlock.GetExtendsAlongOtherFacing( world, tempPos.i, tempPos.j, tempPos.k, iTargetFacing ) )
					{
						stringBlock.SetExtendsAlongFacing( world, tempPos.i, tempPos.j, tempPos.k, iTargetFacing, false, false );
					}
					else
					{
						world.setBlock( tempPos.i, tempPos.j, tempPos.k, 0, 0, 2 );
					}
				}
				else
				{					
					break;
				}
			}
			else
			{
				break;
			}
			
			iStringCount++;
		}
		while ( iStringCount < 64 );
		
		if ( iStringCount > 0 )
		{
			// cycle back through and provide notifications to surrounding blocks
			
			tempPos = new FCUtilsBlockPos( i, j, k );
			
			for ( int iTempCount = 0; iTempCount < iStringCount; iTempCount++ )
			{
				tempPos.AddFacingAsOffset( iTargetFacing );
				
				world.notifyBlockChange( tempPos.i, tempPos.j, tempPos.k, FCBetterThanWolves.fcBlockStakeString.blockID );				
			}
		}
		
		return iStringCount;
	}
	
	//----------- Client Side Functionality -----------//
	
	private final int m_iTopTextureIndex = 142;
	private final int m_iTopWithStringTextureIndex = 143;
	private final int m_iSideTextureIndex = 144;
	private final int m_iSideWithTopStringTextureIndex = 145;

	private Icon m_IconTop;
	private Icon m_IconTopWithString;
	private Icon m_IconSide;
	private Icon m_IconSideWithString;
	private Icon m_IconString;
	
	@Override
    public void registerIcons( IconRegister register )
    {
		Icon sideIcon = register.registerIcon( "fcBlockStake_side" );
		
		blockIcon = sideIcon;
		
		m_IconTop = register.registerIcon( "fcBlockStake_top" );
		m_IconTopWithString = register.registerIcon( "fcBlockStake_top_string" );
		m_IconSide = sideIcon;
		m_IconSideWithString = register.registerIcon( "fcBlockStake_side_string" );
		
		m_IconString = register.registerIcon( "fcBlockStakeString" );
    }
	
	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing = GetFacing( blockAccess, i, j, k);
		boolean bTopConnectedString = HasConnectedStringToFacing( blockAccess, i, j, k, iFacing );
        
        if ( iSide == iFacing || iSide == Block.GetOppositeFacing( iFacing ) )
        {
        	if ( !bTopConnectedString )
        	{
        		return m_IconTop;
        	}
        	else
        	{
        		return m_IconTopWithString;
        	}
        } 
        else
        {
        	if ( !bTopConnectedString )
        	{
        		return m_IconSide;
        	}
        	else
        	{
        		return m_IconSideWithString;
        	}
        }
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
	private AxisAlignedBB GetBoundsFromPoolForStringToFacing( int iFacing )
	{
		AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB( 
			0.5D - FCBlockStakeString.m_dHalfHeight, 0.5D, 0.5D - FCBlockStakeString.m_dHalfHeight, 
    		0.5D + FCBlockStakeString.m_dHalfHeight, 1D, 0.5F + FCBlockStakeString.m_dHalfHeight );
		
		box.TiltToFacingAlongJ( iFacing );
		
		return box;
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
    	
    	int iFacing = GetFacing( blockAccess, i, j, k );
    	
    	if ( iFacing == 0 )
    	{
    		renderer.SetUvRotateSouth( 3 );
    		renderer.SetUvRotateNorth( 3 );
    		renderer.SetUvRotateEast( 3 );
    		renderer.SetUvRotateWest( 3 );
    	}
    	else if ( iFacing == 2 )
    	{
    		renderer.SetUvRotateSouth( 1 );
    		renderer.SetUvRotateNorth( 2 );
    	}
    	else if ( iFacing == 3 )
    	{
    		renderer.SetUvRotateSouth( 2 );
    		renderer.SetUvRotateNorth( 1 );
    		renderer.SetUvRotateTop( 3 );
    		renderer.SetUvRotateBottom( 3 );
    	}
    	else if ( iFacing == 4 )
    	{    		
    		renderer.SetUvRotateEast( 1 );
    		renderer.SetUvRotateWest( 2 );
    		renderer.SetUvRotateTop( 2 );
    		renderer.SetUvRotateBottom( 1 );
    	}
    	else if (  iFacing == 5 )
    	{
    		renderer.SetUvRotateEast( 2 );
    		renderer.SetUvRotateWest( 1 );
    		renderer.SetUvRotateTop( 1 );
    		renderer.SetUvRotateBottom( 2 );
    	}
    	
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
        renderer.renderStandardBlock( this, i, j, k );

        renderer.ClearUvRotation();
        
        // render any attached string
        
        Icon stringTexture = m_IconString;
        
        for ( int iStringFacing = 0; iStringFacing < 6; iStringFacing++ )
        {
        	if ( HasConnectedStringToFacing( blockAccess, i, j, k, iStringFacing ) )
        	{
        		renderer.setRenderBounds( GetBoundsFromPoolForStringToFacing( 
        			iStringFacing ) );
    			
    			FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, stringTexture );
        	}
        }
        
    	return true;
    }
}
