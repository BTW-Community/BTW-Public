// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFireStoked extends FCBlockFire
{
	private final int m_iTickRate = 42;
	
	protected FCBlockFireStoked( int iBlockID )
	{
        super( iBlockID );

		setHardness( 0F );
		setLightValue( 1F );		
		
		SetFireProperties( 60, 0 );		
		
		setStepSound( soundWoodFootstep );		
		
		setUnlocalizedName( "fcBlockStokedFire" );
		
		disableStats();		
	}

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
		// override to make burn time more consistent
		
        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }
    
	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
    }

	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		if ( ValidateState( world, i, j, k ) )
		{
	        if ( world.getBlockId( i, j + 1, k ) == Block.brick.blockID )
	        {
	        	world.setBlockWithNotify( i, j + 1, k, FCBetterThanWolves.fcKiln.blockID );
	        }
	        
	        int iMetaData = world.getBlockMetadata( i, j, k );
	        
	        if( iMetaData < 15 )
	        {
	        	iMetaData += 1;
	        	
	            world.setBlockMetadata( i, j, k, iMetaData );
	        }
	        
	        TryToDestroyBlockWithFire( world, i + 1, j, k, 300, random, 0 );
	        TryToDestroyBlockWithFire( world, i - 1, j, k, 300, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j - 1, k, 250, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j + 1, k, 250, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j, k - 1, 300, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j, k + 1, 300, random, 0 );
	        
	        CheckForFireSpreadFromLocation( world, i, j, k, random, 0 );
	        
	        if ( iMetaData >= 3 )
	        {
	        	// revert to regular fire if we've exceeded our life-span
	        	
	            world.setBlockAndMetadataWithNotify( i, j, k, Block.fire.blockID, 0 );
	        }
	        else
	        {	
	        	world.scheduleBlockUpdate( i, j, k, blockID, 
	        		tickRate( world ) + world.rand.nextInt( 10 ) );
	        }
		}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		// verify we have a tick already scheduled to prevent jams on chunk load
		
		if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
		{
			// reset the countdown and schedule an extra long tick
			// to give any nearby bellows a chance to catch up too
			
			world.setBlockMetadata( i, j, k, 0 );  
			
	        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) * 4 );        
		}
    }
    
	@Override
    public boolean DoesInfiniteBurnToFacing( IBlockAccess blockAccess, int i, int j, int k, 
    	int iFacing )
    {
    	return iFacing == 1;
    }
	
    //------------- Class Specific Methods ------------//
    
    /**
     * Return true if the Stoked fire block still remains
     */
    public boolean ValidateState( World world, int i, int j, int k )
    {
        if ( !canPlaceBlockAt( world, i, j, k ) )
        {
            world.setBlockWithNotify( i, j, k, 0 );
            
            return false;
        }        
        else if ( world.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcBBQ.blockID )
        {
        	if ( !FCBetterThanWolves.fcBBQ.IsLit( world, i, j - 1, k ) )
        	{
        		// we are above an unlit BBQ, extinguish
        		
                world.setBlockWithNotify( i, j, k, 0 );
                
                return false;
        	}
        }
        else
        {
        	// stoked fire can only exist over a Hibachi.  Revert to regular fire if we're
        	// not over one
        	
            world.setBlockAndMetadataWithNotify( i, j, k, Block.fire.blockID, 0 );
            
            return false;
        }
        
    	return true;
    }
    
	//----------- Client Side Functionality -----------//
}