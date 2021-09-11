// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStairsFalling extends FCBlockStairs
{
    protected FCBlockStairsFalling( int iBlockID, Block referenceBlock, int iReferenceBlockMetadata )
    {
    	super( iBlockID, referenceBlock, iReferenceBlockMetadata );
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
    public int tickRate( World par1World )
    {
		return FCBlockFalling.m_iFallingBlockTickRate;
    }
    
    protected void onStartFalling( EntityFallingSand entity ) 
    {
    	entity.metadata = SetIsUpsideDown( entity.metadata, false );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public void RenderFallingBlock( RenderBlocks renderBlocks, int i, int j, int k, int iMetadata )
    {
    	renderBlocks.SetRenderAllFaces( true );    	
    	
        renderBlocks.setRenderBounds( GetBoundsFromPoolForBase( iMetadata ) );        
        renderBlocks.RenderStandardFallingBlock( this, i, j, k, iMetadata );
        
        renderBlocks.setRenderBounds( GetBoundsFromPoolForSecondaryPiece( iMetadata ) );        
        renderBlocks.RenderStandardFallingBlock( this, i, j, k, iMetadata );
        
    	renderBlocks.SetRenderAllFaces( false );
    }
}
