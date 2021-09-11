// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWheatCrop extends FCBlockCropsDailyGrowth
{
    protected FCBlockWheatCrop( int iBlockID )
    {
    	super( iBlockID );    	
        
        setUnlocalizedName( "fcBlockWheatCrop" );
    }
    
	@Override
    protected int GetSeedItemID()
    {
        return 0;
    }

	@Override
    protected int GetCropItemID()
    {
        return FCBetterThanWolves.fcItemStraw.itemID;
    }

	@Override
	public boolean IsBlockHydratedForPlantGrowthOn( World world, int i, int j, int k )
	{
		// relays hydration from soil to top block
		
    	Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
    	
    	return blockBelow != null && 
    		blockBelow.IsBlockHydratedForPlantGrowthOn( world, i, j - 1, k );
	}
	
	@Override
    protected void IncrementGrowthLevel( World world, int i, int j, int k )
    {
    	int iGrowthLevel = GetGrowthLevel( world, i, j, k );
    	
    	if ( iGrowthLevel == 6 )
    	{
    		if ( world.isAirBlock( i, j + 1, k ) )
    		{
    			// can only grow to last stage, and into top block if it is empty
    			// intentionally don't notify block below of full stage growth, as that only
    			// occurs on top block
    			
    	        SetGrowthLevel( world, i, j, k, iGrowthLevel + 1 );

    	    	int iTopMetadata = 0;    	    	
    	        Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
    	        
    	        if ( blockBelow == null || 
    	        	!blockBelow.GetIsFertilizedForPlantGrowth( world, i, j - 1, k ) )
    	        {
    	        	iTopMetadata = FCBetterThanWolves.fcBlockWheatCrop.SetHasGrownToday( 
    	        		iTopMetadata, true );
    	        }
    	    	
        		world.setBlockAndMetadataWithNotify( i, j + 1, k, 
        			FCBetterThanWolves.fcBlockWheatCropTop.blockID, iTopMetadata );
    		}
    	}
    	else
    	{
        	super.IncrementGrowthLevel( world, i, j, k );
    	}    	
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// relay offset to top block
    	
    	Block blockBelow = Block.blocksList[blockAccess.getBlockId( i, j - 1, k )];
    	
    	if ( blockBelow != null )
    	{
        	return blockBelow.GroundCoverRestingOnVisualOffset( blockAccess, i, j - 1, k );
    	}
    	
    	return 0F;
    }
    
	@Override
	public boolean GetIsFertilizedForPlantGrowth( World world, int i, int j, int k )
	{
		// relays to soil from top block
		
    	Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
    	
    	return blockBelow != null && blockBelow.GetIsFertilizedForPlantGrowth( world, i, j - 1, k );
	}
	
	@Override
	public void NotifyOfFullStagePlantGrowthOn( World world, int i, int j, int k, Block plantBlock )
	{
		// relays to soil from top block
		
    	Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];

    	if ( blockBelow != null )
    	{
    		blockBelow.NotifyOfFullStagePlantGrowthOn( world, i, j - 1, k, plantBlock );
    	}
	}
	
    //------------- Class Specific Methods ------------//
    
    public boolean HasTopBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return blockAccess.getBlockId( i, j + 1, k ) == 
    		FCBetterThanWolves.fcBlockWheatCropTop.blockID;
    }

    /**
     * Assumes the block above is a wheat top
     */
    public int GetTopBlockGrowthLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return FCBetterThanWolves.fcBlockWheatCropTop.GetGrowthLevel( blockAccess, i, j + 1, k );
    }
    
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_iconArray;
    private Icon[] m_connectToTopIconArray;

    @Override
    public void registerIcons( IconRegister register )
    {
        m_iconArray = new Icon[8];

        for ( int iTempIndex = 0; iTempIndex < m_iconArray.length; iTempIndex++ )
        {
            m_iconArray[iTempIndex] = register.registerIcon( "fcBlockWheatCrop_" + iTempIndex );
        }
        
        blockIcon = m_iconArray[7]; // for block hit effects and item render
        
        m_connectToTopIconArray = new Icon[4];

        for ( int iTempIndex = 0; iTempIndex < m_connectToTopIconArray.length; iTempIndex++ )
        {
        	m_connectToTopIconArray[iTempIndex] = register.registerIcon( "fcBlockWheatCrop_7_" + 
        		iTempIndex );
        }        
    }

	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	int iGrowthLevel = GetGrowthLevel( blockAccess, i, j, k );
    	
    	iGrowthLevel = MathHelper.clamp_int( iGrowthLevel, 0, 7 );
    	
        if ( iGrowthLevel == 7 && HasTopBlock( blockAccess, i, j, k ) )
        {
            int iTopGrowthLevel = GetTopBlockGrowthLevel( blockAccess, i, j, k );
            
            iTopGrowthLevel = MathHelper.clamp_int( iTopGrowthLevel, 0, 3 );
            
            return m_connectToTopIconArray[iTopGrowthLevel];            
        }
        else
        {
        	return m_iconArray[iGrowthLevel];
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	// combine bounds with top block unless fully grown
    	
        if ( IsFullyGrown( world, i, j, k ) && HasTopBlock( world, i, j, k ) )
        {
            int iTopGrowthLevel = GetTopBlockGrowthLevel( world, i, j, k );
            
            if ( iTopGrowthLevel < 3 )
            {
                float dVerticalOffset = 0;
                Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
                
                if ( blockBelow != null )
                {
                	dVerticalOffset = blockBelow.GroundCoverRestingOnVisualOffset( 
                		world, i, j - 1, k );
                }
                
                double dTopHeight = ( 1 + iTopGrowthLevel ) / 8D;
                
            	return AxisAlignedBB.getAABBPool().getAABB(         	
                    0.5D - m_dBoundsHalfWidth, 0D + dVerticalOffset, 
                    0.5D - m_dBoundsHalfWidth, 
                	0.5D + m_dBoundsHalfWidth, 1D + dVerticalOffset + dTopHeight, 
                	0.5D + m_dBoundsHalfWidth ).offset( i, j, k );
            }
        }
        
        return super.getSelectedBoundingBoxFromPool( world, i, j, k );
    }
}
