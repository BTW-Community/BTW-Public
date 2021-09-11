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
}
