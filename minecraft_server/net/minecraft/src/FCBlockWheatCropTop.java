// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWheatCropTop extends FCBlockCropsDailyGrowth
{
    protected FCBlockWheatCropTop( int iBlockID )
    {
    	super( iBlockID );    	
        
        setUnlocalizedName( "fcBlockWheatCropTop" );
    }
    
	@Override
    public boolean DoesBlockDropAsItemOnSaw( World world, int i, int j, int k )
    {
		return true;
    }
	
	@Override
    protected int GetSeedItemID()
    {
        return 0;
    }

	@Override
    protected int GetCropItemID()
    {
        return FCBetterThanWolves.fcItemWheat.itemID;
    }

	@Override
    protected boolean IsFullyGrown( int iMetadata )
    {
    	return GetGrowthLevel( iMetadata ) >= 3;
    }
	
	@Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	return world.getBlockId( i, j, k ) == FCBetterThanWolves.fcBlockWheatCrop.blockID;
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	// doesn't take weed height into account
    	
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
            	0.5D + m_dBoundsHalfWidth, 0.5D + dVerticalOffset, 0.5D + m_dBoundsHalfWidth );
        }
        else
        {
        	int iGrowthLevel = GetGrowthLevel( iMetadata );
        	
        	return AxisAlignedBB.getAABBPool().getAABB(         	
                0.5D - m_dBoundsHalfWidth, 0F + dVerticalOffset, 0.5D - m_dBoundsHalfWidth, 
        		0.5D + m_dBoundsHalfWidth, ( 1 + iGrowthLevel ) / 8D + dVerticalOffset, 
        		0.5D + m_dBoundsHalfWidth );
        }            
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
        
        // kill the bottom block too if not fully grown
        
        if ( !IsFullyGrown( iMetadata ) )
        {
        	int iBlockBelowID = world.getBlockId( i, j - 1, k );
        	
        	if ( iBlockBelowID == FCBetterThanWolves.fcBlockWheatCrop.blockID )
        	{
        		world.setBlockToAir( i, j - 1, k );
        	}        	
        }
    }

	@Override
	protected void UpdateFlagForGrownToday( World world, int i, int j, int k )
	{
    	// fertilized crops can grow twice in a day
		
        Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
        
        if ( blockBelow != null )
        {
        	// inverted growth level staggering from parent to maintain overall count
        	
	    	if ( !blockBelow.GetIsFertilizedForPlantGrowth( world, i, j - 1, k ) ||
	    		GetGrowthLevel( world, i, j, k ) % 2 == 1 )
	    	{
	    		SetHasGrownToday( world, i, j, k, true );
	    	}
        }
	}
	
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
