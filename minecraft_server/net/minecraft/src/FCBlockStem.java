// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStem extends BlockStem
{
	private final static int m_iStemTickRate = 2;
	
	private static final double m_dWidth = 0.25D;
	private static final double m_dHalfWidth = ( m_dWidth / 2D );
	
    protected FCBlockStem( int iBlockID, Block fruitBlock )
    {
    	super( iBlockID, fruitBlock );
    	
    	setHardness( 0F );
    	
    	SetBuoyant();
    	
        InitBlockBounds( 0.5F - m_dHalfWidth, 0F, 0.5F - m_dHalfWidth, 
        	0.5F + m_dHalfWidth, 0.25F, 0.5F + m_dHalfWidth );
        
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "pumpkinStem" );        
    }
    
	@Override
    public int tickRate( World world )
    {
    	return m_iStemTickRate;
    }
	
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {    	
        checkFlowerChange( world, i, j, k );  // replaces call to super function two levels up
        
        // necessary because checkFlowerChange() may destroy the stem
        if ( world.getBlockId( i, j, k ) == blockID ) 
        {
        	ValidateFruitState( world, i, j, k, rand );
        }
    }
    
    @Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
    	updateTick( world, i, j, k, rand );
    	
    	 // necessary to check blockID because udateTick() may destroy the stem
    	
        if ( world.getBlockId( i, j, k ) == blockID && world.provider.dimensionId != 1 )
        {
        	CheckForGrowth( world, i, j, k, rand );
        }        
    }
    
	@Override
    public boolean OnBlockSawed( World world, int i, int j, int k )
    {
		return false;
    }
	
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
		// override to prevent stems from dropping seeds
    }
	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
	{
		// override to deprecate parent method
	}
	
	@Override
    public void setBlockBoundsForItemRender()
    {
		// override to deprecate parent method
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
		int iMetadata = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iMetadata == 15 && !HasConnectedFruit( blockAccess, i, j, k ) )
		{
			// necessary to avoid brief visual glitch on pumpkin harvest
			
			iMetadata = 7;
		}
		
        double dMaxY = (double)( iMetadata + 1 ) / 16D;
        
        if ( dMaxY < 0.125 )
        {
        	dMaxY = 0.125;
        }
        
        double dHalfWidth = m_dHalfWidth;
    	int iWeedsGrowthLevel = GetWeedsGrowthLevel( blockAccess, i, j, k );
    	
    	if ( iWeedsGrowthLevel > 0 )
    	{
    		dMaxY = Math.max( dMaxY, 
    			FCBlockWeeds.GetWeedsBoundsHeight( iWeedsGrowthLevel ) );
    		
    		dHalfWidth = FCBlockWeeds.m_dWeedsBoundsHalfWidth;
    	}
    	
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0.5D - dHalfWidth, 0D, 0.5D - dHalfWidth, 
        	0.5D + dHalfWidth, dMaxY, 0.5D + dHalfWidth );        
    }

	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		super.onNeighborBlockChange( world, i, j, k, iBlockID );
		
		// necessary because super.onNeighborBlockChange() may destroy the stem
        if ( world.getBlockId( i, j, k ) == blockID ) 
        {
        	ValidateFruitState( world, i, j, k, world.rand );
        }
    }
	
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
		return true;
    }
    
    @Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	Block blockOn = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	return blockOn != null && blockOn.CanDomesticatedCropsGrowOnBlock( world, i, j, k );
    }
    
	@Override
	public boolean CanWeedsGrowInBlock( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}	
	
    //------------- Class Specific Methods ------------//
    
	public void SetFruitBlock( Block block )
	{
		fruitType = block;
	}
	
	private boolean HasConnectedFruit( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetConnectedFruitDirection( blockAccess, i, j, k ) > 0;
	}
	
	private int GetConnectedFruitDirection( IBlockAccess blockAccess, int i, int j, int k )
	{
		for ( int iTempFacing = 2; iTempFacing < 6; iTempFacing++ )
		{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    		
    		targetPos.AddFacingAsOffset( iTempFacing );
    		
    		int iTempBlockID = blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    		
    		if ( iTempBlockID == fruitType.blockID && 
    			fruitType.IsBlockAttachedToFacing( blockAccess, targetPos.i, targetPos.j, targetPos.k, Block.GetOppositeFacing( iTempFacing ) ) )
    		{
    			return iTempFacing;
    		}
		}
        
        return -1;
	}
	
	private void ValidateFruitState( World world, int i, int j, int k, Random rand )
	{
        int iMetadata = world.getBlockMetadata( i, j, k );
        
		if ( iMetadata == 15 && !HasConnectedFruit( world, i, j, k ) )
		{
			// reset to earlier growth stage
			
            world.setBlockMetadataWithNotify( i, j, k, 7 );
		}
	}
	
	private void CheckForGrowth( World world, int i, int j, int k, Random rand )
	{
        if ( GetWeedsGrowthLevel( world, i, j, k ) == 0 &&
        	world.getBlockLightValue( i, j + 1, k ) >= 9 )
        {
	        Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
	        
	        if ( blockBelow != null && 
	        	blockBelow.IsBlockHydratedForPlantGrowthOn( world, i, j - 1, k ) )
	        {
	    		float fGrowthChance = 0.2F * 
	    			blockBelow.GetPlantGrowthOnMultiplier( world, i, j - 1, k, this );
	    		
	            if ( rand.nextFloat() <= fGrowthChance )
	            {
	                int iMetadata = world.getBlockMetadata( i, j, k );
	
	                if ( iMetadata < 14 )
	                {
	                    ++iMetadata;
	                    
	                    world.setBlockMetadataWithNotify( i, j, k, iMetadata );                    
	                }
	                else if ( iMetadata == 14 )
	                {
	                    FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
	                    int iTargetFacing = 0;
	                    
	                    if ( HasSpaceToGrow( world, i, j, k ) )
	                    {
	                    	// if the plant doesn't have space around it to grow, 
	                    	// the fruit will crush its own stem
	                    	
		                    iTargetFacing = rand.nextInt( 4 ) + 2;
		                	
		                    targetPos.AddFacingAsOffset( iTargetFacing );
	                    }
	                    
	                    if ( CanGrowFruitAt( world, targetPos.i, targetPos.j, targetPos.k ) )
	                    {	
	                    	blockBelow.NotifyOfFullStagePlantGrowthOn( world, i, j - 1, k, this );
	                    	
	                    	world.setBlockWithNotify( targetPos.i, targetPos.j, targetPos.k, 
	                    		fruitType.blockID );
	                    	
	                    	if ( iTargetFacing != 0 )
	                    	{
	                    		fruitType.AttachToFacing( world, 
	                    			targetPos.i, targetPos.j, targetPos.k, 
	                    			Block.GetOppositeFacing( iTargetFacing ) );
	                    		
	                            world.setBlockMetadataWithNotify( i, j, k, 15 );
	                    	}   
	                    }
	                }
	            }
	        }
        }
	}
    
    protected boolean HasSpaceToGrow( World world, int i, int j, int k )
    {
        for ( int iTargetFacing = 2; iTargetFacing <= 5; iTargetFacing++ )
        {
        	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
        	
            targetPos.AddFacingAsOffset( iTargetFacing );
            
            if ( CanGrowFruitAt( world, targetPos.i, targetPos.j, targetPos.k ) )
            {
            	return true;
            }
        }
        
        return false;
    }
    
    protected boolean CanGrowFruitAt( World world, int i, int j, int k )
    {
		int iBlockID = world.getBlockId( i, j, k );		
		Block block = Block.blocksList[iBlockID];
		
        if ( FCUtilsWorld.IsReplaceableBlock( world, i, j, k ) ||
        	( block != null && block.blockMaterial == Material.plants && 
    		iBlockID != Block.cocoaPlant.blockID ) )
        {
			// CanGrowOnBlock() to allow fruit to grow on tilled earth and such
			if ( world.doesBlockHaveSolidTopSurface( i, j - 1, k ) ||
				CanGrowOnBlock( world, i, j - 1, k ) ) 
            {				
				return true;
            }
        }
        
        return false;
    }    
	
	//----------- Client Side Functionality -----------//
}