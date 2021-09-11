// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockFarmlandBase extends Block
{
    protected FCBlockFarmlandBase( int iBlockID )
    {
    	super( iBlockID, Material.ground );
    	
    	setHardness( 0.6F );
    	SetShovelsEffectiveOn( true );
    	
        InitBlockBounds( 0F, 0F, 0F, 1F, 0.9375F, 1F );
        
        setLightOpacity( 255 );
        useNeighborBrightness[iBlockID] = true;
    	
        setTickRandomly( true );
        
        setStepSound( soundGravelFootstep );
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        if ( HasIrrigatingBlocks( world, i, j, k ) || world.IsRainingAtPos( i, j + 1, k ) )
        {
        	SetFullyHydrated( world, i, j, k );
        }
        else if ( IsHydrated( world, i, j, k ) )
    	{
        	DryIncrementally( world, i, j, k );
    	}
        else
        {
        	CheckForSoilReversion( world, i, j, k );
        }
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
	{		
		// full block bounds for entity collisions
		
	    return AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D ).offset( i, j, k );
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
    public void onFallenUpon( World world, int i, int j, int k, Entity entity, float fFallDist )
    {
		// min fall dist of 0.75 (previously 0.5) so that the player can safely 
		// step off slabs without potentially ruining crops
		
        if ( !world.isRemote && world.rand.nextFloat() < fFallDist - 0.75F )
        {
            world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
        }
    }

    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 0.8F;
    }    
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockDirtLoose.blockID;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, 
		int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 
			6, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    @Override
	public void OnVegetationAboveGrazed( World world, int i, int j, int k, EntityAnimal animal )
	{
        if ( animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
	}
    
	@Override
    public boolean CanDomesticatedCropsGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanWildVegetationGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
	
	@Override
	public boolean IsBlockHydratedForPlantGrowthOn( World world, int i, int j, int k )
	{
    	return IsHydrated( world, i, j, k );
	}
	
	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( !world.isRemote && !IsFertilized( world, i, j, k ) &&
			entity.isEntityAlive() && entity instanceof EntityItem )
		{
			EntityItem entityItem = (EntityItem)entity;
			ItemStack stack = entityItem.getEntityItem();			
			
			if ( stack.itemID == Item.dyePowder.itemID && stack.getItemDamage() == 15 ) // bone meal
			{
				stack.stackSize--;
				
				if ( stack.stackSize <= 0 )
				{
					entityItem.setDead();
				}
				
				SetFertilized( world, i, j, k );
            	
	            world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "random.pop", 0.25F, 
            		( ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.7F + 1F ) * 2F );
			}
		}
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -( 1F / 16F );        
    }
    
	@Override
	public boolean AttemptToApplyFertilizerTo( World world, int i, int j, int k )
	{
		if ( !IsFertilized( world, i, j, k ) )
		{
			SetFertilized( world, i, j, k );
			
			return true;
		}
		
		return false;
	}
	
	@Override
    public boolean GetCanBlightSpreadToBlock( World world, int i, int j, int k, int iBlightLevel )
    {
    	return iBlightLevel >= 1;
    }
    
    //------------- Class Specific Methods ------------//
    
    protected boolean IsHydrated( World world, int i, int j, int k )
    {
    	return IsHydrated( world.getBlockMetadata( i, j, k ) );
    }
    
    protected abstract boolean IsHydrated( int iMetadata );
    
    protected void SetFullyHydrated( World world, int i, int j, int k )
    {
    	int iMetadata = SetFullyHydrated( world.getBlockMetadata( i, j, k ) );
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    protected abstract int SetFullyHydrated( int iMetadata );
    
	protected abstract void DryIncrementally( World world, int i, int j, int k );
	
    protected abstract boolean IsFertilized( IBlockAccess blockAccess, int i, int j, int k );
    
    protected void SetFertilized( World world, int i, int j, int k )
    {
    }
    
    protected int GetHorizontalHydrationRange( World world, int i, int j, int k )
    {
    	return 4;
    }
    
    protected boolean HasIrrigatingBlocks( World world, int i, int j, int k )
    {
    	int iHorizontalRange = GetHorizontalHydrationRange( world, i, j, k );
    	
        for ( int iTempI = i - iHorizontalRange; iTempI <= i + iHorizontalRange; iTempI++ )
        {
            for ( int iTempJ = j; iTempJ <= j + 1; iTempJ++ )
            {
                for ( int iTempK = k - iHorizontalRange; iTempK <= k + iHorizontalRange; iTempK++ )
                {
                    if ( world.getBlockMaterial( iTempI, iTempJ, iTempK ) == Material.water )
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    protected boolean DoesBlockAbovePreventSoilReversion( World world, int i, int j, int k )
    {
    	// this approach sucks, but given this is a deprecated functionality that won't
    	// apply to additional block types, just leave it alone.
    	
        int iBlockAboveID = world.getBlockId( i, j + 1, k );

        if ( iBlockAboveID == Block.crops.blockID || 
        	iBlockAboveID == Block.melonStem.blockID || 
        	iBlockAboveID == Block.pumpkinStem.blockID || 
        	iBlockAboveID == Block.potato.blockID || 
        	iBlockAboveID == Block.carrot.blockID )
        {
            return true;
        }
        
        return false;
    }

	protected void CheckForSoilReversion( World world, int i, int j, int k )
	{
		if ( !DoesBlockAbovePreventSoilReversion( world, i, j, k ) )
		{
			world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
		}
	}

	//----------- Client Side Functionality -----------//
}

