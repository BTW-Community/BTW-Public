// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockPistonBase extends BlockPistonBase
{
    public FCBlockPistonBase( int iBlockID, boolean bIsSticky )
    {
        super( iBlockID, bIsSticky );
        
        SetPicksEffectiveOn( true );
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );
    }
    
    @Override
    public boolean CanContainPistonPackingToFacing( World world, int i, int j, int k, int iFacing )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( isExtended( iMetadata ) )
    	{
    		if ( Block.GetOppositeFacing( getOrientation( iMetadata ) ) != iFacing )
    		{
    			return false;
    		}
    	}
    	
    	return true;    	
    }
    
    @Override
	public int GetFacing( int iMetadata )
	{
    	// piston facing is in the direction of the push
    	
		return iMetadata & 7;
	}
	
    @Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata &= ~7;
    	
		return iMetadata | iFacing;
	}
	
    @Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !isExtended( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
    @Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !isExtended( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
    @Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !isExtended( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
    @Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		if ( !isExtended( iMetadata ) )
		{
			return super.RotateMetadataAroundJAxis( iMetadata, bReverse );
		}
		
		return iMetadata;
	}
	
    @Override
    public boolean CanBlockBePushedByPiston( World world, int i, int j, int k, int iToFacing )
    {
    	return !isExtended( world.getBlockMetadata( i, j, k ) );
    }
    
    @Override
    protected void updatePistonState( World world, int i, int j, int k )
    {
    	ValidatePistonState( world, i, j, k );
    	
    	super.updatePistonState( world, i, j, k );
    }
    
    @Override
    protected boolean canExtend( World world, int i, int j, int k, int iToFacing )
    {
        int iOffsetI = i + Facing.offsetsXForSide[iToFacing];
        int iOffsetJ = j + Facing.offsetsYForSide[iToFacing];
        int iOffsetK = k + Facing.offsetsZForSide[iToFacing];
        
        int iDist = 0;

        while ( iDist < 13 )
        {
            if ( iOffsetJ <= 0 || iOffsetJ >= 255 )
            {
                return false;
            }

            Block tempBlock = blocksList[world.getBlockId(iOffsetI, iOffsetJ, iOffsetK)];

            if ( tempBlock != null )
            {
                if ( !tempBlock.CanBlockBePushedByPiston( world, iOffsetI, iOffsetJ, iOffsetK, iToFacing ) )
                {
                    return false;
                }
                else
                {
                	int iMobility = tempBlock.getMobilityFlag();
                	
                	int iShovelEjectDirection = GetPistonShovelEjectionDirection( world, iOffsetI, iOffsetJ, iOffsetK, iToFacing );
                	
	                if ( iMobility != 1 && iShovelEjectDirection < 0 )
	                {
	                    if ( iDist == 12 )
	                    {
	                        return false;
	                    }
	
	                    iOffsetI += Facing.offsetsXForSide[iToFacing];
	                    iOffsetJ += Facing.offsetsYForSide[iToFacing];
	                    iOffsetK += Facing.offsetsZForSide[iToFacing];
	                    
	                    ++iDist;
	                    
	                    continue;
	                }
                }
            }
            
        	return true;
        }
        
        return true;
    }
    
    @Override
    protected boolean tryExtend( World world, int i, int j, int k, int iToFacing )
    {
        int iOffsetI = i + Facing.offsetsXForSide[iToFacing];
        int iOffsetJ = j + Facing.offsetsYForSide[iToFacing];
        int iOffsetK = k + Facing.offsetsZForSide[iToFacing];
        
        int iDist = 0;

        while (true)
        {
            if ( iDist < 13 )
            {
                if ( iOffsetJ <= 0 || iOffsetJ >= 255 )
                {
                    return false;
                }

                int iTempBlockID = world.getBlockId( iOffsetI, iOffsetJ, iOffsetK );
                Block tempBlock = blocksList[iTempBlockID];
                
                if ( tempBlock != null )
                {
                    if ( !tempBlock.CanBlockBePushedByPiston( world, iOffsetI, iOffsetJ, iOffsetK, iToFacing ) )
                    {
                        return false;
                    }
                    else
                    {
                    	int iMobility = tempBlock.getMobilityFlag();
                    	
                    	int iShovelEjectDirection = GetPistonShovelEjectionDirection( world, iOffsetI, iOffsetJ, iOffsetK, iToFacing );
                    	
    	                if ( iMobility != 1 && iShovelEjectDirection < 0 )
	                    {
	                        if (iDist == 12)
	                        {
	                            return false;
	                        }
	
	                        iOffsetI += Facing.offsetsXForSide[iToFacing];
	                        iOffsetJ += Facing.offsetsYForSide[iToFacing];
	                        iOffsetK += Facing.offsetsZForSide[iToFacing];
	                        ++iDist;
	                        
	                        continue;
	                    }

    	                int iTempMetadata = world.getBlockMetadata( iOffsetI, iOffsetJ, iOffsetK );
    	                
    	                if ( iShovelEjectDirection >= 0  )
    	                {
    	                	iTempMetadata =  tempBlock.AdjustMetadataForPistonMove( iTempMetadata );

    	                	int iDropI = iOffsetI + Facing.offsetsXForSide[iShovelEjectDirection];
    	                	int iDropJ = iOffsetJ + Facing.offsetsYForSide[iShovelEjectDirection];
    	                	int iDropK = iOffsetK + Facing.offsetsZForSide[iShovelEjectDirection];
    	                	
    	                	OnShovelEjectIntoBlock( world, iDropI, iDropJ, iDropK );
    	                	
    	                    world.setBlock( iDropI, iDropJ, iDropK, Block.pistonMoving.blockID, iTempMetadata, 4 );
    	                    
    	                    world.setBlockTileEntity( iDropI, iDropJ, iDropK, 
    	                    	FCBlockPistonMoving.GetShoveledTileEntity( iTempBlockID, iTempMetadata, iShovelEjectDirection ) );
    	                }
    	                else
    	                {
    	                	tempBlock.OnBrokenByPistonPush( world, iOffsetI, iOffsetJ, iOffsetK, iTempMetadata );
    	                }	                    
	                	
	                    world.setBlockToAir( iOffsetI, iOffsetJ, iOffsetK );    	                    
                    }
                }
            }

            int iPrevOffsetI = iOffsetI;
            int iPrevOffsetJ = iOffsetJ;
            int iPrevOffsetK = iOffsetK;
            
            int var12 = 0;
            int[] var13;
            int var14;
            int var15;
            int var16;

            for (var13 = new int[13]; iOffsetI != i || iOffsetJ != j || iOffsetK != k; iOffsetK = var16)
            {
                var14 = iOffsetI - Facing.offsetsXForSide[iToFacing];
                var15 = iOffsetJ - Facing.offsetsYForSide[iToFacing];
                var16 = iOffsetK - Facing.offsetsZForSide[iToFacing];
                int var17 = world.getBlockId(var14, var15, var16);
                int var18 = world.getBlockMetadata(var14, var15, var16);

                if (var17 == this.blockID && var14 == i && var15 == j && var16 == k)
                {
                    world.setBlock(iOffsetI, iOffsetJ, iOffsetK, Block.pistonMoving.blockID, iToFacing | (this.isSticky ? 8 : 0), 4);
                    world.setBlockTileEntity(iOffsetI, iOffsetJ, iOffsetK, BlockPistonMoving.getTileEntity(Block.pistonExtension.blockID, iToFacing | (this.isSticky ? 8 : 0), iToFacing, true, false));
                }
                else
                {
                	if ( Block.blocksList[var17] != null )
                	{
                		var18 =  Block.blocksList[var17].AdjustMetadataForPistonMove( var18 );
                	}

                    world.setBlock(iOffsetI, iOffsetJ, iOffsetK, Block.pistonMoving.blockID, var18, 4);
                    world.setBlockTileEntity(iOffsetI, iOffsetJ, iOffsetK, BlockPistonMoving.getTileEntity(var17, var18, iToFacing, true, false));
                }

                var13[var12++] = var17;
                iOffsetI = var14;
                iOffsetJ = var15;
            }

            iOffsetI = iPrevOffsetI;
            iOffsetJ = iPrevOffsetJ;
            iOffsetK = iPrevOffsetK;

            for (var12 = 0; iOffsetI != i || iOffsetJ != j || iOffsetK != k; iOffsetK = var16)
            {
                var14 = iOffsetI - Facing.offsetsXForSide[iToFacing];
                var15 = iOffsetJ - Facing.offsetsYForSide[iToFacing];
                var16 = iOffsetK - Facing.offsetsZForSide[iToFacing];
                world.notifyBlocksOfNeighborChange(var14, var15, var16, var13[var12++]);
                iOffsetI = var14;
                iOffsetJ = var15;
            }

            return true;
        }
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
	@Override
    public void setBlockBoundsForItemRender() 
    {
    	// override to deprecate parent
    }	
    
	@Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );

        if ( isExtended( iMetadata ) )
        {
            switch ( getOrientation( iMetadata ) )
            {
                case 0:
                	
                	return AxisAlignedBB.getAABBPool().getAABB(         	
                		0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F );

                case 1:
                	
                	return AxisAlignedBB.getAABBPool().getAABB(         	
                		0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F );

                case 2:
                	
                	return AxisAlignedBB.getAABBPool().getAABB(         	
                		0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F );

                case 3:
                	
                	return AxisAlignedBB.getAABBPool().getAABB(         	
                		0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);

                case 4:
                	
                	return AxisAlignedBB.getAABBPool().getAABB(         	
                		0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F );

                case 5:
                	
                	return AxisAlignedBB.getAABBPool().getAABB(         	
                		0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F );
            }
        }
        
    	return super.GetBlockBoundsFromPoolBasedOnState( blockAccess, i, j, k );
    }
    
	@Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
		getCollisionBoundingBoxFromPool( world, i, j, k ).AddToListIfIntersects( 
			intersectingBox, list );    	
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }

    @Override
    public boolean CanSupportFallingBlocks( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// NOTE: The piston base transforms into a PistonMoving on retraction making
    	// implementing regular hardpoints more difficult than usual, so just support
    	// falling blocks instead.
    	
    	return true;    	
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		// handles BD placement
		
        return GetOppositeFacing( iFacing );
    }
	
	@Override
    public int PreBlockPlacedBy( World world, int i, int j, int k, int iMetadata, 
    	EntityLiving entityBy ) 
    {
    	return determineOrientation( world, i, j, k, entityBy );    	
    }

	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, 
		EntityLiving entityBy, ItemStack stack )
    {
		// override vanilla orientation and update code
    }
	
	@Override
    public void onPostBlockPlaced( World world, int i, int j, int k, int iMetadata ) 
    {    	
        if ( !world.isRemote )
        {
            updatePistonState( world, i, j, k );
        }
    }
	
    //------------- Class Specific Methods ------------//

    protected void ValidatePistonState( World world, int i, int j, int k )
    {
    	// checks for jams that sometimes occur on chunk load, and corrects them
    	
        int iMetadata = world.getBlockMetadata( i, j, k );

        if ( !isExtended( iMetadata ) )
        {
        	int iFacing = getOrientation( iMetadata );
        	
        	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
        	int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
        	
        	if ( iTargetBlockID == Block.pistonExtension.blockID )
        	{
        		int iTargetMetadata = world.getBlockMetadata( targetPos.i, targetPos.j, targetPos.k );
        		
        		if ( BlockPistonExtension.getDirectionMeta( iTargetMetadata ) == iFacing )
        		{
        			// we've got an unextended piston base facing a piston arm coming out of the 
        			// facing the same direction, indicating a jam
        			
        			// don't perform a neighbor notify, as this may occur on a neighbor block change.
                    world.SetBlockMetadataWithNotify( i, j, k, iMetadata | 8, 2 );
        		}
        	}        	
        }
    }
    
    protected int GetPistonShovelEjectionDirection( World world, int i, int j, int k, int iToFacing )
    {
        Block block = Block.blocksList[world.getBlockId( i, j, k )];
        
    	if ( block!= null && block.CanBePistonShoveled( world, i, j, k ) )
    	{
        	int iOppFacing = Block.GetOppositeFacing( iToFacing );
        	
            int iShovelI = i + Facing.offsetsXForSide[iOppFacing];
            int iShovelJ = j + Facing.offsetsYForSide[iOppFacing];
            int iShovelK = k + Facing.offsetsZForSide[iOppFacing];
            
            Block shovelBlock = Block.blocksList[world.getBlockId( iShovelI, iShovelJ, iShovelK )];
            
            if ( shovelBlock != null )
            {
            	int iShovelEjectDirection = shovelBlock.GetPistonShovelEjectDirection( world, iShovelI, iShovelJ, iShovelK, iToFacing );
            	
        		if ( iShovelEjectDirection >= 0 && CanShovelEjectToFacing( world, i, j, k, iShovelEjectDirection ))
        		{
    				return iShovelEjectDirection;
        		}
            }            
    	}
    	
        return -1;
    }
    
    protected boolean CanShovelEjectToFacing( World world, int i, int j, int k, int iFacing )
    {
    	int iDestI = i + Facing.offsetsXForSide[iFacing];
    	int iDestJ = j + Facing.offsetsYForSide[iFacing];
    	int iDestK = k + Facing.offsetsZForSide[iFacing];
    	
    	Block destBlock = Block.blocksList[world.getBlockId( iDestI, iDestJ, iDestK )];
    	
    	if ( destBlock != null )
    	{
    		return destBlock.getMobilityFlag() == 1;
    	}
    	
    	return true;
    }
    
	protected void OnShovelEjectIntoBlock( World world, int i, int j, int k )
	{
    	Block block = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	if ( block != null && block.getMobilityFlag() == 1 )
    	{
        	block.dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
        	
            world.setBlockToAir( i, j, k );    	                    
    	}
	}
    
	//----------- Client Side Functionality -----------//
    
	public static boolean m_bIsRenderingExtendedBase = false;
	
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        int iFacing = getOrientation( iMetadata ); 
        
        if ( iFacing > 5 )
        {
            return topIcon;
        }
        else if ( iSide == iFacing )
    	{
    		if ( !m_bIsRenderingExtendedBase )        		
        	{
        		return topIcon;
        	}
        	else
        	{
        		return innerTopIcon;
        	}        	
    	}
    	else if ( iSide == this.GetOppositeFacing( iFacing ) )
		{
    		return  bottomIcon;
		}
    	
    	return blockIcon;
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderPistonBase( this, i, j, k, false );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
}
