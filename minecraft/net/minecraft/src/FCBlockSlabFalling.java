// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockSlabFalling extends FCBlockSlab
{
    public FCBlockSlabFalling( int iBlockID, Material material )
    {
        super( iBlockID, material );
    }
    
	@Override
    public boolean AttemptToCombineWithFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
	{
		if ( entity.blockID == blockID && !GetIsUpsideDown( world, i, j, k ) )
		{
			ConvertToFullBlock( world, i, j, k );
					
			return true;
		}
		
		return false;
	}

    @Override
    public boolean IsFallingBlock()
    {
    	return true;
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k ) 
    {
    	ScheduleCheckForFall( world, i, j, k );
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID ) 
    {    	
    	ScheduleCheckForFall( world, i, j, k );
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand ) 
    {    	
        if ( !CheckForFall( world, i, j, k ) )
        {
        	if ( GetIsUpsideDown( world, i, j, k ) )
        	{
        		SetIsUpsideDown( world, i, j, k, false );
        	}
        }
    }
    
    @Override
    public int tickRate( World world )
    {
		return FCBlockFalling.m_iFallingBlockTickRate;
    }
    
    @Override
    protected void onStartFalling( EntityFallingSand entity ) 
    {
    	entity.metadata = SetIsUpsideDown( entity.metadata, false );
    }
    
    @Override
    public boolean CanBePlacedUpsideDownAtLocation( World world, int i, int j, int k )
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean ShouldRenderWhileFalling( World world, EntityFallingSand entity )
    {
        int iCurrentBlockI = MathHelper.floor_double( entity.posX );
        int iCurrentBlockJ = MathHelper.floor_double( entity.posY );
        int iCurrentBlockK = MathHelper.floor_double( entity.posZ );
        
        int iBlockIDAtLocation = world.getBlockId( iCurrentBlockI, iCurrentBlockJ, iCurrentBlockK );
        
        Block fallingBlock = Block.blocksList[entity.blockID];
        
		if ( iBlockIDAtLocation == entity.blockID )
		{
			if ( entity.posY - (double)iCurrentBlockJ < 0.4D )
			{
				return false;
			}
		}
		else
		{
        	FCBlockSlabFalling fallingSlab = (FCBlockSlabFalling)fallingBlock;
        	
        	if ( fallingSlab.GetCombinedBlockID( entity.metadata ) == iBlockIDAtLocation )
        	{
        		return false;
        	}
		}
		
		return true;
    }
    
    @Override
    public void RenderFallingBlock( RenderBlocks renderBlocks, int i, int j, int k, int iMetadata )
    {
        renderBlocks.setRenderBounds( GetBlockBoundsFromPoolFromMetadata( iMetadata ) );
        
        renderBlocks.RenderStandardFallingBlock( this, i, j, k, iMetadata );
    }
}