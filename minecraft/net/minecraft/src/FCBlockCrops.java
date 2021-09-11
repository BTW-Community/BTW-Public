// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockCrops extends FCBlockPlants
{
	static protected final double m_dBoundsWidth = ( 1D - ( 4D / 16D ) );
	static protected final double m_dBoundsHalfWidth = ( m_dBoundsWidth / 2D );
	
    protected FCBlockCrops( int iBlockID )
    {
        super( iBlockID, Material.plants );
        
        setHardness( 0F );
        SetBuoyant();
		SetFireProperties( FCEnumFlammability.CROPS );
        
        InitBlockBounds( 0.5D - m_dBoundsHalfWidth, 0D, 0.5D - m_dBoundsHalfWidth, 
        	0.5D + m_dBoundsHalfWidth, 1D, 0.5D + m_dBoundsHalfWidth );
        
        setStepSound( soundGrassFootstep );
        
        setTickRandomly( true );
        
        disableStats();
    }

    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        super.onNeighborBlockChange( world, i, j, k, iNeighborBlockID );
        
        UpdateIfBlockStays( world, i, j, k );
    }
    
    @Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, 
    	float fChance, int iFortuneModifier )
    {
        if ( !world.isRemote && IsFullyGrown( iMetadata ) )
        {
        	super.dropBlockAsItemWithChance( world, i, j, k, iMetadata, fChance, 0 );

        	DropSeeds( world, i, j, k, iMetadata );
        }
    }

    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
    	if ( IsFullyGrown( iMetadata ) )
    	{
    		return GetCropItemID();
    	}
    	
        return 0;
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        float dVerticalOffset = 0;
        Block blockBelow = Block.blocksList[blockAccess.getBlockId( i, j - 1, k )];
        
        if ( blockBelow != null )
        {
        	dVerticalOffset = blockBelow.GroundCoverRestingOnVisualOffset( 
        		blockAccess, i, j - 1, k );
        }
        
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
        if ( IsFullyGrown( iMetadata ) )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
                0.5D - m_dBoundsHalfWidth, 0D + dVerticalOffset, 0.5D - m_dBoundsHalfWidth, 
            	0.5D + m_dBoundsHalfWidth, 1D + dVerticalOffset, 0.5D + m_dBoundsHalfWidth );
        }
        else
        {
        	int iGrowthLevel = GetGrowthLevel( iMetadata );
        	double dBoundsHeight = ( 1 + iGrowthLevel ) / 8D;
    		
        	int iWeedsGrowthLevel = GetWeedsGrowthLevel( blockAccess, i, j, k );
        	
        	if ( iWeedsGrowthLevel > 0 )
        	{
        		dBoundsHeight = Math.max( dBoundsHeight, 
        			FCBlockWeeds.GetWeedsBoundsHeight( iWeedsGrowthLevel ) );
        	}
        	
        	return AxisAlignedBB.getAABBPool().getAABB(         	
                0.5D - m_dBoundsHalfWidth, 0F + dVerticalOffset, 0.5D - m_dBoundsHalfWidth, 
        		0.5D + m_dBoundsHalfWidth, dBoundsHeight + dVerticalOffset, 
        		0.5D + m_dBoundsHalfWidth );
        }            
    }
	
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
		return true;
    }
    
    @Override
    public void OnGrazed( World world, int i, int j, int k, EntityAnimal animal )
    {
    	// drop the block as an item so that animals can get the base graze value + eat
    	// any tasties that drop
    	
        dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
        
        super.OnGrazed( world, i, j, k, animal );    
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        if ( UpdateIfBlockStays( world, i, j, k ) )
        {
        	// no plants can grow in the end
        	
	        if ( world.provider.dimensionId != 1 && !IsFullyGrown( world, i, j, k ) )
	        {
	        	AttemptToGrow( world, i, j, k, rand );
	        }
        }
    }

	@Override
	public boolean CanWeedsGrowInBlock( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}	
	
	@Override
	public boolean GetConvertsLegacySoil( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;		
	}
	
    @Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	Block blockOn = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	return blockOn != null && blockOn.CanDomesticatedCropsGrowOnBlock( world, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
    protected abstract int GetCropItemID();
    
    protected abstract int GetSeedItemID();

    protected boolean UpdateIfBlockStays( World world, int i, int j, int k ) 
    {
        if ( !canBlockStay( world, i, j, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockToAir( i, j, k );
            
            return false;
        }
        
        return true;
    }
    
    protected void AttemptToGrow( World world, int i, int j, int k, Random rand )
    {
    	if ( GetWeedsGrowthLevel( world, i, j, k ) == 0 &&  
    		world.GetBlockNaturalLightValue( i, j + 1, k ) >= GetLightLevelForGrowth() )
	    {
	        Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
	        
	        if ( blockBelow != null && 
	        	blockBelow.IsBlockHydratedForPlantGrowthOn( world, i, j - 1, k ) )
	        {
	    		float fGrowthChance = GetBaseGrowthChance( world, i, j, k ) *
	    			blockBelow.GetPlantGrowthOnMultiplier( world, i, j - 1, k, this );
	    		
	            if ( rand.nextFloat() <= fGrowthChance )
	            {
	            	IncrementGrowthLevel( world, i, j, k );
	            }
	        }
	    }
    }
    
    public void DropSeeds( World world, int i, int j, int k, int iMetadata )
    {
    	int iSeedItemID = GetSeedItemID();
    	
    	if ( iSeedItemID > 0 )
    	{
	        dropBlockAsItem_do( world, i, j, k, new ItemStack( iSeedItemID, 1, 0 ) );
    	}
    }
    
    public float GetBaseGrowthChance( World world, int i, int j, int k )
    {
    	return 0.05F;
    }
    
    protected void IncrementGrowthLevel( World world, int i, int j, int k )
    {
    	int iGrowthLevel = GetGrowthLevel( world, i, j, k ) + 1;
    	
        SetGrowthLevel( world, i, j, k, iGrowthLevel );
        
        if ( IsFullyGrown( world, i, j, k ) )
        {
        	Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
        	
        	if ( blockBelow != null )
        	{
        		blockBelow.NotifyOfFullStagePlantGrowthOn( world, i, j - 1, k, this );
        	}
        }
    }
    
    protected int GetGrowthLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetGrowthLevel( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected int GetGrowthLevel( int iMetadata )
    {
    	return iMetadata & 7;
    }
    
    protected void SetGrowthLevel( World world, int i, int j, int k, int iLevel )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~7); // filter out old level   	
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata | iLevel );
    }
    
    protected void SetGrowthLevelNoNotify( World world, int i, int j, int k, int iLevel )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~7); // filter out old level   	
    	
    	world.setBlockMetadata( i, j, k, iMetadata | iLevel );
    }
    
    protected boolean IsFullyGrown( World world, int i, int j, int k )
    {
    	return IsFullyGrown( world.getBlockMetadata( i, j, k ) );
    }
    
    protected boolean IsFullyGrown( int iMetadata )
    {
    	return GetGrowthLevel( iMetadata ) >= 7;
    }
    
    protected int GetLightLevelForGrowth()
    {
    	return 11;
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	RenderCrops( renderer, i, j, k );
    	
    	FCBetterThanWolves.fcBlockWeeds.RenderWeeds( this, renderer, i, j, k );
		
    	return true;
    }
    
    protected void RenderCrops( RenderBlocks renderer, int i, int j, int k )
    {
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.setBrightness( getMixedBrightnessForBlock( renderer.blockAccess, i, j, k ) );
        tessellator.setColorOpaque_F( 1F, 1F, 1F );
        
        double dVerticalOffset = 0D;
        Block blockBelow = Block.blocksList[renderer.blockAccess.getBlockId( i, j - 1, k )];
        
        if ( blockBelow != null )
        {
        	dVerticalOffset = blockBelow.GroundCoverRestingOnVisualOffset( 
        		renderer.blockAccess, i, j - 1, k );
        }
        
        RenderCrossHatch( renderer, i, j, k, getBlockTexture( renderer.blockAccess, i, j, k, 0 ), 
        	4D / 16D, dVerticalOffset );
    }
    
    @Override
    public int idPicked( World world, int i, int j, int k )
    {
        return GetSeedItemID();
    }
}
