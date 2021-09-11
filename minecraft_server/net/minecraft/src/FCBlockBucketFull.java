// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockBucketFull extends FCBlockBucket
{
    public FCBlockBucketFull( int iBlockID )
    {
        super( iBlockID );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand ) 
    {
    	if ( !CheckForFall( world, i, j, k ) )
    	{    	
    		CheckForSpillContents( world, i, j, k );
    	}
    }
    
	@Override
	protected void InitModels()
	{
		m_model = new FCModelBlockBucketFull();
	    
		// must initialize transformed model due to weird vanilla getIcon() calls that 
		// occur outside of regular rendering
		
		m_modelTransformed = m_model; 
	}
	
	//------------- Class Specific Methods ------------//
	
    public void CheckForSpillContents( World world, int i, int j, int k ) 
    {
    	int iFacing = GetFacing( world, i, j, k );
    	
		// note that upside down isn't spilled as it's assumed the bucket is resting
    	// on a center hardpoint that would block it
    	
    	if ( iFacing >= 2 )
    	{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    		
    		if ( AttemptToSpillIntoBlock( world, targetPos.i, targetPos.j, targetPos.k ) )
    		{
    			// verify the block hasn't already been displaced by the fluid
    			
    	    	if ( world.getBlockId( i, j, k ) == blockID )
    	    	{
    	    		world.setBlockAndMetadataWithNotify( i, j, k, 
    	    			FCBetterThanWolves.fcBlockBucketEmpty.blockID, 
    	    			SetFacing( 0, iFacing ) );
    	    	}
    		}
    	}
    }
    
    public abstract boolean AttemptToSpillIntoBlock( World world, int i, int j, int k );
    
	//----------- Client Side Functionality -----------//
}
