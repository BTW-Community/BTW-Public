// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockLavaReceiver extends FCBlockMortarReceiver
{
	public static final int m_iLavaFillTickRate = 20;
	public static final int m_iLavaHardenTickRate = 2;
	
    public FCBlockLavaReceiver( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        setTickRandomly( true );
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k ) 
    {
    	if ( !ScheduleUpdatesForLavaAndWaterContact( world, i, j, k ) )
    	{
    		super.onBlockAdded( world, i, j, k );
    	}    	
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand ) 
    {
    	if ( !CheckForFall( world, i, j, k ) )
    	{
	    	if ( GetHasLavaInCracks( world, i, j, k ) )
	    	{
	    		if ( HasWaterAbove( world, i, j, k ) )
	    		{
	                world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
	                
	    			world.setBlockWithNotify( i, j, k, Block.stone.blockID );
	    			
	    			return;
	    		}
	    	}
	    	else if ( HasLavaAbove( world, i, j, k ) )
	    	{
	            SetHasLavaInCracks( world, i, j, k, true );
	    	}
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
    	if ( GetHasLavaInCracks( world, i, j, k ) )
    	{
	        if ( world.IsRainingAtPos( i, j + 1, k ) )
	        {
	        	world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
            
	        	world.setBlockWithNotify( i, j, k, Block.stone.blockID );
	        }
    	}
    }
	
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID ) 
    {
    	if ( !ScheduleUpdatesForLavaAndWaterContact( world, i, j, k ) )
    	{    	
    		super.onNeighborBlockChange( world, i, j, k, iNeighborBlockID );
    	}
    }
    
    @Override
    protected boolean canSilkHarvest()
    {
    	// prevent havest of version with lava in cracks
    	
        return false;
    }
    
    @Override
    public boolean GetIsBlockWarm( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetHasLavaInCracks( blockAccess, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
    protected boolean GetHasLavaInCracks( int iMetadata )
    {
    	return ( iMetadata & 1 ) != 0;
    }
    
    protected boolean GetHasLavaInCracks( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetHasLavaInCracks( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected int SetHasLavaInCracks( int iMetadata, boolean bHasLava )
    {
    	if ( bHasLava )
    	{
    		iMetadata |= 1;
    	}
    	else
    	{
    		iMetadata &= (~1);
    	}
    	
    	return iMetadata;
    }
    
    protected void SetHasLavaInCracks( World world, int i, int j, int k, boolean bHasLava )
    {
    	int iMetadata =  SetHasLavaInCracks( world.getBlockMetadata( i, j, k ), bHasLava );
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }    	
    
    protected boolean HasLavaAbove( IBlockAccess blockAccess, int i, int j, int k )
    {
		Block blockAbove = Block.blocksList[blockAccess.getBlockId( i, j + 1, k )];
		
		return blockAbove != null && blockAbove.blockMaterial == Material.lava;
    }
    
    protected boolean HasWaterAbove( IBlockAccess blockAccess, int i, int j, int k )
    {
		Block blockAbove = Block.blocksList[blockAccess.getBlockId( i, j + 1, k )];
		
		return blockAbove != null && blockAbove.blockMaterial == Material.water;
    }
    
    protected boolean ScheduleUpdatesForLavaAndWaterContact( World world, int i, int j, int k )
    {
    	if ( GetHasLavaInCracks( world, i, j, k ) )
    	{
    		if ( HasWaterAbove( world, i, j, k ) )
    		{
    			if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
    			{
    				world.scheduleBlockUpdate( i, j, k, blockID, m_iLavaHardenTickRate );
    			}
                
                return true;
    		}
    	}
    	else if ( HasLavaAbove( world, i, j, k ) )
    	{
			if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
			{
				world.scheduleBlockUpdate( i, j, k, blockID, m_iLavaFillTickRate );
			}
            
            return true;
    	}
    	
    	return false;
    }
    
    //------------ Client Side Functionality ----------//
}