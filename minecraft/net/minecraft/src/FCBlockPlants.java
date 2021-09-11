// FCMOD

package net.minecraft.src;

public abstract class FCBlockPlants extends Block
{
    protected FCBlockPlants( int iBlockID, Material material )
    {
        super( iBlockID, material );
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
	{
		return null;
	}
	
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
        
		Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
		
		if ( blockBelow != null )
		{
			blockBelow.NotifyOfPlantAboveRemoved( world, i, j - 1, k, this );
		}
    }
    
    @Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
    	// don't slow down movement in air as it affects the ability of entities to jump up blocks
    	
    	if ( entity.IsAffectedByMovementModifiers() && entity.onGround )
    	{
	        entity.motionX *= 0.8D;
	        entity.motionZ *= 0.8D;
    	}
    }
    
    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        return super.canPlaceBlockAt( world, i, j, k ) && CanGrowOnBlock( world, i, j - 1, k );
    }
    
    @Override
    public boolean canBlockStay( World world, int i, int j, int k )
    {
        return CanGrowOnBlock( world, i, j - 1, k ); 
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, 
    	int iFacing, float fXClick, float fYClick, float fZClick )
    {
		if ( GetWeedsGrowthLevel( world, i, j, k ) > 0 )
		{
            if ( !world.isRemote )
            {
            	RemoveWeeds( world, i, j, k );
            	
            	// block break FX            	
                world.playAuxSFX( 2001, i, j, k, Block.crops.blockID + ( 6 << 12 ) );
            }
            
        	return true;
		}
		
		return false;
    }
	
	@Override
	public int GetWeedsGrowthLevel( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
		Block blockBelow = Block.blocksList[iBlockBelowID];
		
		if ( blockBelow != null && iBlockBelowID != blockID )
		{
			return blockBelow.GetWeedsGrowthLevel( blockAccess, i, j - 1, k );
		}
		
		return 0;
	}
	
	@Override
	public void RemoveWeeds( World world, int i, int j, int k )
	{
		Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
		
		if ( blockBelow != null )
		{
			blockBelow.RemoveWeeds( world, i, j - 1, k );
		}
	}
	
	@Override
	public boolean AttemptToApplyFertilizerTo( World world, int i, int j, int k )
	{
		// relay to the block below so that applying fertilizer to the plant will
		// fertilize the soil
		
		Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
		
		if ( blockBelow != null )
		{
			return blockBelow.AttemptToApplyFertilizerTo( world, i, j - 1, k );
		}
		
		return false;
	}
	
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k ) && 
    		blockID != Block.waterlily.blockID;
    }
    
	@Override
    public void ClientBreakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		// destroy any ground cover on this block as well to prevent it momentarily "popping" and appearing above the block 
		
		FCUtilsWorld.ClearAnyGroundCoverOnBlock( world, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	Block blockOn = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	return blockOn != null && blockOn.CanWildVegetationGrowOnBlock( world, i, j, k );
    }

	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
    		renderer.blockAccess, i, j, k ) );
        
    	renderer.renderCrossedSquares( this, i, j, k );
    	
    	FCBetterThanWolves.fcBlockWeeds.RenderWeeds( this, renderer, i, j, k );
		
		return true;
    }    
}
