// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockPlanter extends FCBlockPlanterBase
{
	// non linear types due to legacy code that had an odd system of metadata usage
	
	public static final int m_iTypeEmpty = 0;
	public static final int m_iTypeSoil = 1; // deprecated
	public static final int m_iTypeSoilFertilized = 2; // deprecated
	public static final int m_iTypeSoulSand = 8;
	public static final int m_iTypeGrass0 = 9; // deprecated
	public static final int m_iTypeGrass1 = 11; // deprecated
	public static final int m_iTypeGrass2 = 13; // deprecated
	public static final int m_iTypeGrass3 = 15; // deprecated
	
    public FCBlockPlanter( int iBlockID )
    {
        super( iBlockID );  
        
        setUnlocalizedName( "fcBlockPlanter" );     
    }

	@Override
    public int damageDropped( int iMetadata )
    {
		if ( iMetadata == m_iTypeGrass1 || iMetadata == m_iTypeGrass2 ||
			iMetadata == m_iTypeGrass3 )
		{
			iMetadata = m_iTypeGrass0;
		}
		
    	return iMetadata;
    }
    
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	if ( iPlanterType == m_iTypeGrass0 || iPlanterType == m_iTypeGrass1 || 
			iPlanterType == m_iTypeGrass2 || iPlanterType == m_iTypeGrass3 )
    	{
        	int iOldGrowthState = GetGrassGrowthState( world, i, j, k );
        	int iNewGrowthState = 0;
        	
	    	if ( world.isAirBlock( i, j + 1, k ) )
	    	{
	        	// grass planters with nothing in them will eventually sprout flowers or tall grass
	    		  
	    		if ( world.getBlockLightValue(i, j + 1, k) >= 8 )
	    		{
		    		iNewGrowthState = iOldGrowthState;
		    		
		    		if ( true )//random.nextInt( 12 ) == 0 )
		    		{
			    		iNewGrowthState++;
			    		
			    		if ( iNewGrowthState > 3 )
			    		{
			    			iNewGrowthState = 0;
			    			
			    			int iPlantType = random.nextInt( 4 );
			    			
			    			if ( iPlantType == 0 )
			    			{
			    				world.setBlockWithNotify( i, j + 1, k, Block.plantRed.blockID );
			    			}
			    			else if ( iPlantType == 1 )
			    			{
			    				world.setBlockWithNotify( i, j + 1, k, Block.plantYellow.blockID );
			    			}
			    			else
			    			{
			    				world.setBlockAndMetadataWithNotify( i, j + 1, k, Block.tallGrass.blockID, 1 );
			    			}
			    		}
		    		}
	    		}
	    	}
	    	
	    	// Spread grass
	    	
	        if ( world.getBlockLightValue( i, j + 1, k ) >= 9 )
	        {
	            for ( int tempCount = 0; tempCount < 4; tempCount++ )
	            {
	                int tempi = ( i + random.nextInt(3)) - 1;
	                int tempj = ( j + random.nextInt(5)) - 3;
	                int tempk = ( k + random.nextInt(3)) - 1;
	                
	                int iTempBlockAboveID = world.getBlockId( tempi, tempj + 1, tempk );

	                if ( world.getBlockId( tempi, tempj, tempk ) == Block.dirt.blockID && 
                		world.getBlockLightValue( tempi, tempj + 1, tempk ) >= 4 && 
                		Block.lightOpacity[iTempBlockAboveID] <= 2 )
	                {
	                	world.setBlockWithNotify( tempi, tempj, tempk, Block.grass.blockID );
	                }
	            }
	        }
	    	
	    	if ( iNewGrowthState != iOldGrowthState )
	    	{
	    		SetGrassGrowthState( world, i, j, k, iNewGrowthState );
	    	}
    	}    	
    }
    
	@Override
	public boolean AttemptToApplyFertilizerTo( World world, int i, int j, int k )
	{
		int iPlanterType = GetPlanterType( world, i, j, k );

		if ( iPlanterType == m_iTypeSoil )
		{
			SetPlanterType( world, i, j, k, m_iTypeSoilFertilized );
			
			return true;
		}
		
		return false;
	}
	
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		if ( GetPlanterType( blockAccess, i, j, k ) == m_iTypeEmpty )
		{
			return false;
    	}
		
		return super.HasLargeCenterHardPointToFacing( blockAccess, i, j, k, iFacing, 
			bIgnoreTransparency );		
	}
    
	@Override
    public boolean CanDomesticatedCropsGrowOnBlock( World world, int i, int j, int k )
    {
		int iPlanterType = GetPlanterType( world, i, j, k );
    	
    	return iPlanterType == m_iTypeSoil || iPlanterType == m_iTypeSoilFertilized;
    }
    
	@Override
    public boolean CanReedsGrowOnBlock( World world, int i, int j, int k )
    {
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	return iPlanterType != m_iTypeEmpty && iPlanterType != m_iTypeSoulSand;
    }
    
	@Override
    public boolean CanSaplingsGrowOnBlock( World world, int i, int j, int k )
    {
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	return iPlanterType != m_iTypeEmpty && iPlanterType != m_iTypeSoulSand;
    }
    
	@Override
    public boolean CanWildVegetationGrowOnBlock( World world, int i, int j, int k )
    {
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	return iPlanterType != m_iTypeEmpty && iPlanterType != m_iTypeSoulSand;
    }
    
	@Override
    public boolean CanNetherWartGrowOnBlock( World world, int i, int j, int k )
    {
		return GetPlanterType( world, i, j, k ) == m_iTypeSoulSand;
    }
    
	@Override
    public boolean CanCactusGrowOnBlock( World world, int i, int j, int k )
    {
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	return iPlanterType != m_iTypeEmpty && iPlanterType != m_iTypeSoulSand;
    }
    
	@Override
	public boolean IsBlockHydratedForPlantGrowthOn( World world, int i, int j, int k )
	{
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	return iPlanterType == m_iTypeSoil || iPlanterType == m_iTypeSoilFertilized;
	}
	
	@Override
	public boolean IsConsideredNeighbouringWaterForReedGrowthOn( World world, int i, int j, int k )
	{
		int iPlanterType = GetPlanterType( world, i, j, k );
		
    	return iPlanterType == m_iTypeSoil || iPlanterType == m_iTypeSoilFertilized ||
    		super.IsConsideredNeighbouringWaterForReedGrowthOn( world, i, j, k );
	}
	
	@Override
	public boolean GetIsFertilizedForPlantGrowth( World world, int i, int j, int k )
	{
		return GetPlanterType( world, i, j, k ) == m_iTypeSoilFertilized;
	}
	
	@Override
	public void NotifyOfFullStagePlantGrowthOn( World world, int i, int j, int k, Block plantBlock )
	{
		int iPlanterType = GetPlanterType( world, i, j, k );
		
		if ( iPlanterType == m_iTypeSoilFertilized )
		{
			SetPlanterType( world, i, j, k, m_iTypeSoil );
		}
	}
	
    //------------- Class Specific Methods ------------//

	public int GetPlanterType( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetPlanterTypeFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public void SetPlanterType( World world, int i, int j, int k, int iType )
	{
        world.setBlockMetadataWithNotify( i, j, k, iType );
	}
	
	public int GetPlanterTypeFromMetadata( int iMetadata )
	{
		return iMetadata;
	}
	
	public int GetGrassGrowthState( IBlockAccess blockAccess, int i, int j, int k )
	{
        int iPlanterType = GetPlanterType( blockAccess, i, j, k );

        switch ( iPlanterType )
        {
        	case m_iTypeGrass0:
        		
        		return 0;
        		
        	case m_iTypeGrass1:
        		
        		return 1;
        		
        	case m_iTypeGrass2:
        		
        		return 2;
        		
        	case m_iTypeGrass3:
        		
        		return 3;
        		
    		default:
    			
    			return 0;       		
        }
	}
	
	public void SetGrassGrowthState( World world, int i, int j, int k, int iGrowthState )
	{
		int iPlanterType = m_iTypeGrass0;
		
		if ( iGrowthState == 1 )
		{
			iPlanterType = m_iTypeGrass1;
		}
		else if ( iGrowthState == 2 )
		{
			iPlanterType = m_iTypeGrass2;
		}
		else if ( iGrowthState == 3 )
		{
			iPlanterType = m_iTypeGrass3;
		}
		
		SetPlanterType( world, i, j, k, iPlanterType );
	}
	
	//----------- Client Side Functionality -----------//
}