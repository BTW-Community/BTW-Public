// FCMOD

package net.minecraft.src;

public class FCBlockFenceGate extends BlockFenceGate
{
    public FCBlockFenceGate( int iBlockID )
    {
        super( iBlockID );
        
    	SetBlockMaterial( FCBetterThanWolves.fcMaterialPlanks );
    	
    	setHardness( 1.5F );
    	setResistance( 5F );
    	SetAxesEffectiveOn();
    	
        SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOD_BASED_BLOCK );    	
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fenceGate" );
    }
    
	@Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -3;
    }
    
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		// override to provide change notifications on open and close
		
        int var10 = par1World.getBlockMetadata(par2, par3, par4);

        if (isFenceGateOpen(var10))
        {
            par1World.SetBlockMetadataWithNotify(par2, par3, par4, var10 & -5, 3);
        }
        else
        {
            int var11 = (MathHelper.floor_double((double)(par5EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) % 4;
            int var12 = getDirection(var10);

            if (var12 == (var11 + 2) % 4)
            {
                var10 = var11;
            }

            par1World.SetBlockMetadataWithNotify(par2, par3, par4, var10 | 4, 3);
        }

        par1World.playAuxSFXAtEntity(par5EntityPlayer, 1003, par2, par3, par4, 0);
        return true;
    }
    
	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	// override to prevent trap doors from responding to redstone signal
    }
    
    @Override
    public boolean CanPathThroughBlock( IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder )
    {
    	if ( !pathFinder.CanPathThroughClosedWoodDoor() )
    	{
    		// note: getBlocksMovement() is misnamed and returns if the block *doesn't* block movement
    		
	    	if ( !pathFinder.CanPathThroughOpenWoodDoor() || !getBlocksMovement( blockAccess, i, j, k ) )
	    	{
	    		return false;
	    	}
    	}
    	
    	return true;
    }
    
    @Override
    public boolean IsBreakableBarricade( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }

    @Override
    public boolean IsBreakableBarricadeOpen( IBlockAccess blockAccess, int i, int j, int k )
    {
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
    	return isFenceGateOpen( iMetadata );
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iDirection = getDirection( blockAccess.getBlockMetadata( i, j, k ) );

        if (iDirection != 2 && iDirection != 0)
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.375F, 0F, 0F, 0.625F, 1F, 1F );
        }
        else
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0F, 0F, 0.375F, 1F, 1F, 0.625F );
        }
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderBlockFenceGate( this, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }	
}