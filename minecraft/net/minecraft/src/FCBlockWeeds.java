// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWeeds extends FCBlockPlants
{
	static public final double m_dWeedsBoundsWidth = ( 1D - ( 4D / 16D ) );
	static public final double m_dWeedsBoundsHalfWidth = ( m_dWeedsBoundsWidth / 2D );
	
    protected FCBlockWeeds( int iBlockID )
    {
        super( iBlockID, Material.plants );
        
        setHardness( 0F );
        SetBuoyant();
		SetFireProperties( FCEnumFlammability.CROPS );
        
        InitBlockBounds( 0.5D - m_dWeedsBoundsWidth, 0D, 0.5D - m_dWeedsBoundsWidth, 
        	0.5D + m_dWeedsBoundsWidth, 0.5D, 0.5D + m_dWeedsBoundsWidth );
        
        setStepSound( soundGrassFootstep );
        
        setTickRandomly( true );
        
        disableStats();
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return -1;
    }

	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		// override to prevent parent from disrupting soil as it felt weird
		// relative to being able to clear weeds around plants without disruption
    }
	
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        super.onNeighborBlockChange( world, i, j, k, iNeighborBlockID );
        
        if ( !canBlockStay( world, i, j, k ) )
        {
            world.setBlockToAir( i, j, k );
        }
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
        
    	int iGrowthLevel = GetWeedsGrowthLevel( blockAccess, i, j, k );
    	
		double dBoundsHeight = GetWeedsBoundsHeight( iGrowthLevel );
    	
    	return AxisAlignedBB.getAABBPool().getAABB(         	
            0.5D - m_dWeedsBoundsHalfWidth, 0F + dVerticalOffset, 
            0.5D - m_dWeedsBoundsHalfWidth, 
    		0.5D + m_dWeedsBoundsHalfWidth, dBoundsHeight + dVerticalOffset, 
    		0.5D + m_dWeedsBoundsHalfWidth );
    }
    
	@Override
	public void RemoveWeeds( World world, int i, int j, int k )
	{
		Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
		
		if ( blockBelow != null )
		{
			blockBelow.RemoveWeeds( world, i, j - 1, k );
		}
		
		world.setBlockToAir( i, j, k );
	}
    
	@Override
	public boolean CanWeedsGrowInBlock( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
		int iBlockOnID = world.getBlockId( i, j, k );
		
		return world.getBlockId( i, j, k ) == FCBetterThanWolves.fcBlockFarmland.blockID ||
			world.getBlockId( i, j, k ) == FCBetterThanWolves.fcBlockFarmlandFertilized.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
    public static double GetWeedsBoundsHeight( int iGrowthLevel )
    {
    	return ( ( iGrowthLevel >> 1 ) + 1 ) / 8D; 
    }
    
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_weedIconArray = null;

    @Override
    public void registerIcons( IconRegister register )
    {
    	m_weedIconArray = new Icon[8];
    	
    	m_weedIconArray[0] = m_weedIconArray[1] = register.registerIcon( "fcBlockWeeds_0" );
    	m_weedIconArray[2] = m_weedIconArray[3] = register.registerIcon( "fcBlockWeeds_1" );
    	m_weedIconArray[4] = m_weedIconArray[5] = register.registerIcon( "fcBlockWeeds_2" );
    	m_weedIconArray[6] = m_weedIconArray[7] = register.registerIcon( "fcBlockWeeds_3" );        	
    	
        blockIcon = m_weedIconArray[7]; // for block hit effects and item render
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
		RenderWeeds( this, renderer, i, j, k );
		
		return true;
    }
    
    /**
     * Note that this will be called by different block types that can support weeds,
     * like the various forms of crops, and thus the block parameter is not necessarily of type
     * FCBlockWeeds
     */
    public void RenderWeeds( Block block, RenderBlocks renderer, int i, int j, int k )
    {
		int iGrowthLevel = block.GetWeedsGrowthLevel( renderer.blockAccess, i, j, k );
		
		if ( iGrowthLevel > 0 )
		{
	        double dVerticalOffset = 0;
	        Block blockBelow = Block.blocksList[renderer.blockAccess.getBlockId( i, j - 1, k )];
	        
	        if ( blockBelow != null )
	        {
	        	dVerticalOffset = blockBelow.GroundCoverRestingOnVisualOffset( 
	        		renderer.blockAccess, i, j - 1, k );
	        }
	        
	        Tessellator tessellator = Tessellator.instance;
	        
	        tessellator.setBrightness( block.getMixedBrightnessForBlock( renderer.blockAccess, 
	        	i, j, k ) );
	        
	        tessellator.setColorOpaque_F( 1F, 1F, 1F );
	        
			iGrowthLevel = MathHelper.clamp_int( iGrowthLevel, 0, 7 );
			
	        block.RenderCrossHatch( renderer, i, j, k, m_weedIconArray[iGrowthLevel], 
	        	2D / 16D, dVerticalOffset );
		}
    }
}
