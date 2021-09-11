// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockHempCrop extends BlockCrops
{
	static private final double m_dCollisionBoxWidth = 0.4F;
	static private final double m_dCollisionBoxHalfWidth = ( m_dCollisionBoxWidth / 2F );
	
	static private final float m_fBaseGrowthChance = 0.1F;
	
    protected FCBlockHempCrop( int iBlockID )
    {
        super( iBlockID );

        setHardness( 0.2F );
        SetAxesEffectiveOn( true );        
        
        SetBuoyant();        
		SetFireProperties( FCEnumFlammability.CROPS );
		
		InitBlockBounds( 0.5D - m_dCollisionBoxHalfWidth, 0D, 0.5D - m_dCollisionBoxHalfWidth, 
			0.5D + m_dCollisionBoxHalfWidth, 1D, 0.5D + m_dCollisionBoxHalfWidth );
		
        setUnlocalizedName( "fcBlockHemp" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
        if ( iMetadata >= 7 )
        {
            return getCropItem();
        }
        
        return 0;
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iGrowthLevel = blockAccess.getBlockMetadata( i, j, k );
        iGrowthLevel = MathHelper.clamp_int( iGrowthLevel, 0, 7 );
        
        double dBoundsHeight = ( 1 + iGrowthLevel ) / 8D;        
        double dHalfWidth = m_dCollisionBoxHalfWidth;
        
    	int iWeedsGrowthLevel = GetWeedsGrowthLevel( blockAccess, i, j, k );
    	
    	if ( iWeedsGrowthLevel > 0 )
    	{
    		dBoundsHeight = Math.max( dBoundsHeight, 
    			FCBlockWeeds.GetWeedsBoundsHeight( iWeedsGrowthLevel ) );
    		
    		dHalfWidth = FCBlockWeeds.m_dWeedsBoundsHalfWidth;
    	}
    	
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0.5D - dHalfWidth, 0D, 0.5D - dHalfWidth, 
    		0.5D + dHalfWidth, dBoundsHeight, 0.5D + dHalfWidth );
    }
	
	@Override
    public boolean DoesBlockDropAsItemOnSaw( World world, int i, int j, int k )
    {
		return true;
    }
    
    @Override
    public boolean canBlockStay( World world, int i, int j, int k )
    {
        return super.canBlockStay( world, i, j, k ) || 
        	( world.getBlockId( i, j - 1, k ) == blockID && !GetIsTopBlock( world, i, j - 1, k ) );
    }
    
	@Override
    protected void AttemptToGrow( World world, int i, int j, int k, Random rand )
    {
        if( !GetIsTopBlock( world, i, j, k ) &&
        	GetWeedsGrowthLevel( world, i, j, k ) == 0 &&
        	( world.getBlockLightValue( i, j, k ) >= 15 ||
    		world.getBlockId( i, j + 1, k ) == FCBetterThanWolves.fcLightBulbOn.blockID || 
    		world.getBlockId( i, j + 2, k ) == FCBetterThanWolves.fcLightBulbOn.blockID ) )
        {
            Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];            
        	
            if ( blockBelow != null && 
            	blockBelow.IsBlockHydratedForPlantGrowthOn( world, i, j - 1, k ) )
            {
                // only the base of the plants grows, and only does if its on hydrated soil
        		
                int iMetadata = world.getBlockMetadata( i, j, k );
                
	            if ( GetGrowthLevel( world, i, j, k ) < 7 )
	            {
	            	float fChanceOfGrowth = GetBaseGrowthChance( world, i, j, k ) * 
	            		blockBelow.GetPlantGrowthOnMultiplier( world, i, j - 1, k, this );
	            	
	                if ( rand.nextFloat() <= fChanceOfGrowth )
	                {
		            	IncrementGrowthLevel( world, i, j, k );
	                }
	            }
	            else if ( world.isAirBlock( i, j + 1, k ) )
	            {
	            	// check for growth of top block
	            	
	            	float fChanceOfGrowth = ( GetBaseGrowthChance( world, i, j, k ) / 4F ) * 
	            		blockBelow.GetPlantGrowthOnMultiplier( world, i, j - 1, k, this );
	            	
	                if ( rand.nextFloat() <= fChanceOfGrowth )
                    {
                		// top of the plant
	                	
	                	int iNewMetadata = SetIsTopBlock( 0, true );
                		
                		world.setBlockAndMetadataWithNotify( i, j + 1, k, blockID, iNewMetadata );
                    	
                		blockBelow.NotifyOfFullStagePlantGrowthOn( world, i, j - 1, k, this );
                    }
            	}
            }
        }
    }

	@Override
    public void DropSeeds( World world, int i, int j, int k, int iMetadata, 
    	float fChance, int iFortuneModifier )
    {
        if ( GetIsTopBlock( iMetadata ) && world.rand.nextInt( 100 ) < 50 )
        {
        	FCUtilsItem.DropStackAsIfBlockHarvested( world, i, j, k, 
        		new ItemStack( getSeedItem(), 1, 0 ) );
        }
    }
    
    @Override
    protected int getSeedItem()
    {
        return FCBetterThanWolves.fcItemHempSeeds.itemID;
    }

    @Override
    protected int getCropItem()
    {
        return FCBetterThanWolves.fcItemHemp.itemID;
    }
    
	@Override
    public float GetBaseGrowthChance( World world, int i, int j, int k )
    {
    	return 0.1F;
    }
    
	@Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
		super.harvestBlock( world, player, i, j, k, iMetadata );
		
        if( !world.isRemote )
        {
        	// kill the plant below if not harvested by shears
        	
        	if ( player.getCurrentEquippedItem() == null ||
        		player.getCurrentEquippedItem().itemID != Item.shears.itemID )
        	{
        		if ( world.getBlockId( i, j - 1, k ) == blockID )
        		{
        			dropBlockAsItem( world, i, j - 1, k, 
        				world.getBlockMetadata( i, j - 1, k ), 0 );
        			
        			world.setBlockToAir( i, j - 1, k );
        		}
        	}
        }
    }
	
    //------------- Class Specific Methods ------------//

    protected boolean GetIsTopBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsTopBlock( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected boolean GetIsTopBlock( int iMetadata )
    {
    	return ( iMetadata & 8 ) != 0;
    }
    
    protected void SetIsTopBlock( World world, int i, int j, int k, boolean bTop )
    {
    	int iMetadata = SetIsTopBlock( world.getBlockMetadata( i, j, k ), bTop );
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    protected int SetIsTopBlock( int iMetadata, boolean bTop )
    {
    	if ( bTop )
    	{
    		iMetadata |= 8;
    	}
    	else
    	{
    		iMetadata &= (~8);
    	}
    	
    	return iMetadata;    	
    }
    
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_IconBottomByGrowthArray = new Icon[8];
    private Icon m_IconTop;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		m_IconTop = register.registerIcon( "fcBlockHemp_top" );
		
		blockIcon = m_IconTop; // for hit effects
		
		m_IconBottomByGrowthArray[0] = register.registerIcon( "fcBlockHemp_bottom_00" );
		m_IconBottomByGrowthArray[1] = register.registerIcon( "fcBlockHemp_bottom_01" );
		m_IconBottomByGrowthArray[2] = register.registerIcon( "fcBlockHemp_bottom_02" );
		m_IconBottomByGrowthArray[3] = register.registerIcon( "fcBlockHemp_bottom_03" );
		m_IconBottomByGrowthArray[4] = register.registerIcon( "fcBlockHemp_bottom_04" );
		m_IconBottomByGrowthArray[5] = register.registerIcon( "fcBlockHemp_bottom_05" );
		m_IconBottomByGrowthArray[6] = register.registerIcon( "fcBlockHemp_bottom_06" );
		m_IconBottomByGrowthArray[7] = register.registerIcon( "fcBlockHemp_bottom_07" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        if ( GetIsTopBlock( iMetadata ) )
        {
        	return m_IconTop;
        }
        
    	return m_IconBottomByGrowthArray[iMetadata];
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	renderer.renderCrossedSquares( this, i, j, k );
 
    	if ( !GetIsTopBlock( renderer.blockAccess, i, j, k ) )
		{
    		FCBetterThanWolves.fcBlockWeeds.RenderWeeds( this, renderer, i, j, k );
		}
		
    	return true;
    }    
}