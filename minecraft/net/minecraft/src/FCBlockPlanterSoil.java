// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockPlanterSoil extends FCBlockPlanterBase
{
    protected FCBlockPlanterSoil( int iBlockID )
    {
    	super( iBlockID );
    	
        setUnlocalizedName( "fcBlockPlanterSoil" );     
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
		boolean bHasIrrigation = HasIrrigatingBlocks( world, i, j, k ) || 
			world.IsRainingAtPos( i, j + 1, k );
		
		if ( bHasIrrigation != GetIsHydrated( world, i, j, k ) )
		{
			SetIsHydrated( world, i, j, k, bHasIrrigation );
		}
    }
	
	@Override
	public boolean AttemptToApplyFertilizerTo( World world, int i, int j, int k )
	{
		if ( !GetIsFertilized( world, i, j, k ) )
		{
			SetIsFertilized( world, i, j, k, true );
			
			return true;
		}
		
		return false;
	}
	
	@Override
    public boolean CanDomesticatedCropsGrowOnBlock( World world, int i, int j, int k )
    {
		return true;
    }
    
	@Override
    public boolean CanReedsGrowOnBlock( World world, int i, int j, int k )
    {
		return true;
    }
    
	@Override
    public boolean CanSaplingsGrowOnBlock( World world, int i, int j, int k )
    {
		return true;
    }
    
	@Override
    public boolean CanWildVegetationGrowOnBlock( World world, int i, int j, int k )
    {
		return true;
    }
	
	@Override
    public boolean CanCactusGrowOnBlock( World world, int i, int j, int k )
    {
		return true;
    }
	
	@Override
	public boolean IsBlockHydratedForPlantGrowthOn( World world, int i, int j, int k )
	{
		return GetIsHydrated( world, i, j, k );
	}
	
	@Override
	public boolean IsConsideredNeighbouringWaterForReedGrowthOn( World world, int i, int j, int k )
	{
		return GetIsHydrated( world, i, j, k );
	}
	
	@Override
	public boolean GetIsFertilizedForPlantGrowth( World world, int i, int j, int k )
	{
		return GetIsFertilized( world, i, j, k );
	}
	
	@Override
	public void NotifyOfFullStagePlantGrowthOn( World world, int i, int j, int k, Block plantBlock )
	{
		if ( GetIsFertilized( world, i, j, k ) )
		{
			SetIsFertilized( world, i, j, k, false );
		}
	}
	
    protected boolean HasIrrigatingBlocks( World world, int i, int j, int k )
    {
    	// planters can only be irrigated by direct neighbors

    	if ( world.getBlockMaterial( i, j - 1, k ) == Material.water || 
    		world.getBlockMaterial( i, j + 1, k ) == Material.water ||
    		world.getBlockMaterial( i, j, k - 1 ) == Material.water ||
    		world.getBlockMaterial( i, j, k + 1 ) == Material.water ||
    		world.getBlockMaterial( i - 1, j, k ) == Material.water ||
    		world.getBlockMaterial( i + 1, j, k ) == Material.water )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
	
	protected boolean GetIsHydrated( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsHydrated( blockAccess.getBlockMetadata( i, j, k ) );
	}
    
	protected boolean GetIsHydrated( int iMetadata )
	{
		return ( iMetadata & 1 ) != 0;
	}
	
	protected void SetIsHydrated( World world, int i, int j, int k, boolean bHydrated )
	{
		int iMetadata = SetIsHydrated( world.getBlockMetadata( i, j, k ), bHydrated );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	protected int SetIsHydrated( int iMetadata, boolean bHydrated )
	{
		if ( bHydrated)
		{
			iMetadata |= 1;
		}
		else
		{
			iMetadata &= (~1);
		}
		
		return iMetadata;
	}
	
	protected boolean GetIsFertilized( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsFertilized( blockAccess.getBlockMetadata( i, j, k ) );
	}
    
	protected boolean GetIsFertilized( int iMetadata )
	{
		return ( iMetadata & 2 ) != 0;
	}
	
	protected void SetIsFertilized( World world, int i, int j, int k, boolean bFertilized )
	{
		int iMetadata = SetIsFertilized( world.getBlockMetadata( i, j, k ), bFertilized );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	protected int SetIsFertilized( int iMetadata, boolean bFertilized )
	{
		if ( bFertilized)
		{
			iMetadata |= 2;
		}
		else
		{
			iMetadata &= (~2);
		}
		
		return iMetadata;
	}
	
	//------------ Client Side Functionality ----------//
	
    private Icon m_iconTopSoilDry;
    private Icon m_iconTopSoilDryFertilized;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_iconTopSoilDry = register.registerIcon( "fcBlockPlanter_top_dry" );
		m_iconTopSoilDryFertilized = register.registerIcon( "fcBlockPlanter_top_dry_fertilized" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iSide == 1  )
		{
			if ( GetIsFertilized( iMetadata ) )
	        {
				if ( GetIsHydrated( iMetadata ) )
				{
					return m_iconTopSoilWetFertilized;
				}
				else
				{
					return m_iconTopSoilDryFertilized;
				}
        	}
			else
			{
				if ( GetIsHydrated( iMetadata ) )
				{
					return m_iconTopSoilWet;
				}
				else
				{
					return m_iconTopSoilDry;
				}
			}
		}        
    	
        return blockIcon;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
		return RenderFilledPlanterBlock( renderer, i, j, k );
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderer, int iItemDamage, float fBrightness )
    {
		RenderFilledPlanterInvBlock( renderer, this, iItemDamage );
    }
}
