// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockMyceliumSlab extends FCBlockSlabAttached
{
    protected FCBlockMyceliumSlab( int iBlockID )
    {
        super( iBlockID, Material.grass );
        
        setHardness( 0.6F );
        SetShovelsEffectiveOn();
        
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "fcBlockMyceliumSlab" );
        
        setTickRandomly( true );
        
        setCreativeTab( CreativeTabs.tabBlock );        
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	int iBlockAboveID = world.getBlockId( i, j + 1, k );
    	Block blockAbove = Block.blocksList[iBlockAboveID];
        boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
    	
        if ( ( bIsUpsideDown && blockAbove != null && blockAbove.HasLargeCenterHardPointToFacing( world, i, j + 1, k, 0 ) ) || 
        	world.getBlockLightValue( i, j + 1, k ) < FCBlockMycelium.m_iMyceliumSurviveMinimumLightLevel || 
        	Block.lightOpacity[iBlockAboveID] > 2 )        	
        {
        	int iNewMetadata = SetIsUpsideDown( FCBlockDirtSlab.m_iSubtypeDirt, bIsUpsideDown );
        	
            world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtSlab.blockID, iNewMetadata );
        }
    	else
    	{
        	FCBlockMycelium.CheckForMyceliumSpreadFromLocation( world, i, j, k );
    	}
    }    
    
    @Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockDirtLooseSlab.blockID;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 3, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public boolean AttemptToCombineWithFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
	{
		if ( entity.blockID == FCBetterThanWolves.fcBlockDirtLooseSlab.blockID )
		{
			if ( !GetIsUpsideDown( world, i, j, k ) )
			{			
				world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );					
						
				return true;
			}
		}
		
		return super.AttemptToCombineWithFallingEntity( world, i, j, k, entity );
	}

	@Override
	protected void OnAnchorBlockLost( World world, int i, int j, int k )
	{
		world.setBlock(i, j, k, FCBetterThanWolves.fcBlockDirtLooseSlab.blockID, world.getBlockMetadata(i, j, k) & 8, 2);
	}
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return Block.mycelium.blockID;
	}
	
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
    	return animal.CanGrazeMycelium();
    }
    
    @Override
    public void OnGrazed( World world, int i, int j, int k, EntityAnimal animal )
    {
        if ( !animal.GetDisruptsEarthOnGraze() )
        {
	    	int iNewMetadata = SetIsUpsideDown( FCBlockDirtSlab.m_iSubtypeDirt, 
	    		GetIsUpsideDown( world, i, j, k ) );
	    	
	        world.setBlockAndMetadataWithNotify( i, j, k, 
	        	FCBetterThanWolves.fcBlockDirtSlab.blockID, iNewMetadata );
        }
        else
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLooseSlab.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
    }
    
    @Override
	public void OnVegetationAboveGrazed( World world, int i, int j, int k, EntityAnimal animal )
	{
        if ( animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLooseSlab.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
	}
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	super.OnBlockDestroyedWithImproperTool( world, player, i, j, k, iMetadata );
    	
		OnDirtSlabDugWithImproperTool( world, i, j, k, GetIsUpsideDown( iMetadata ) );
    }
    
	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		super.onBlockDestroyedByExplosion( world, i, j, k, explosion );
    	
		OnDirtSlabDugWithImproperTool( world, i, j, k, GetIsUpsideDown( world, i, j, k ) );
    }
	
	@Override
    protected void OnNeighborDirtDugWithImproperTool( World world, int i, int j, int k, 
    	int iToFacing )
    {
		// only disrupt grass/mycelium when block below is dug out
	
		if ( iToFacing == 0 )
		{			
	        boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );

	        // only disrupt the block if it's touching the dug neighbor
	        
	        if ( !( bIsUpsideDown && iToFacing == 0 ) && !( !bIsUpsideDown && iToFacing == 1 ) )
	        {
	        	world.setBlockWithNotify( i, j, k, 
	        		FCBetterThanWolves.fcBlockDirtLooseSlab.blockID );
        	}
		}
    }
    
    //------------- Class Specific Methods ------------//    
}
