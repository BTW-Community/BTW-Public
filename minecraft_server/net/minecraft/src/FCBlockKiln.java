// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockKiln extends Block
{
	private static int m_iMinFireFactorBaseTickRate = 40;
	private static int m_iMaxFireFactorBaseTickRate = 160;
	
    public FCBlockKiln( int iBlockID )
    {
        super( iBlockID, Material.rock );  
        
        setHardness( 2F );
        setResistance( 10F );
        setStepSound( soundStoneFootstep );
        
        setTickRandomly( true );
        
        setUnlocalizedName( "fcBlockKiln" );        
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	if ( CanBlockBeCooked( world, i, j + 1, k ) )
    	{
    		ScheduleUpdateBasedOnCookState( world, i, j, k );
    	}
    }
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	int iOldCookCounter = GetCookCounter( world, i, j, k );
    	int iNewCookCounter = 0;
    	
    	if ( CanBlockBeCooked( world, i, j + 1, k ) )
    	{
    		if ( CheckKilnIntegrity( world, i, j, k ) )
    		{
	    		if ( iOldCookCounter >= 15 )
	    		{
	    			CookBlock( world, i, j + 1, k );
	    		}
	    		else
	    		{    		
		    		iNewCookCounter = iOldCookCounter + 1;
		    		
	    			ScheduleUpdateBasedOnCookState( world, i, j, k );
	    		}
    		}
    		else
    		{
        		// if we have a valid cook block above, we have to reschedule another tick
        		// regardless of other factors, because the shape of the kiln can change without
        		// an immediate neighbor changing, causing the cook process to restart
        		
    			ScheduleUpdateBasedOnCookState( world, i, j, k );
    		}
    	}
    	
    	if ( iOldCookCounter != iNewCookCounter )
    	{    		
			SetCookCounter( world, i, j, k, iNewCookCounter );
    	}
    }
    
	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return Block.brick.blockID;
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	if ( world.getBlockId( i, j - 1, k ) != FCBetterThanWolves.fcBlockFireStoked.blockID )
    	{
    		// we don't have a stoked fire beneath us, so revert to regular brick
    		
    		world.setBlockWithNotify( i, j, k, Block.brick.blockID );
    	}
    	else if ( CanBlockBeCooked( world, i, j + 1, k ) )
    	{
        	if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) &&            	
    			!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
        	{			
        		ScheduleUpdateBasedOnCookState( world, i, j, k );
        	}
    	}
    	else if ( GetCookCounter( world, i, j, k ) > 0 )
    	{        	
    		// reset the cook counter so it doesn't get passed to another block on piston push
    		
    		SetCookCounterNoNotify( world, i, j, k, 0 );
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		// verify we have a tick already scheduled to prevent jams on chunk load
		
    	if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) &&
    		CanBlockBeCooked( world, i, j + 1, k ) )
    	{			
    		ScheduleUpdateBasedOnCookState( world, i, j, k );
    	}
    }
	
    //------------- Class Specific Methods ------------//
    
	public int GetCookCounter( int iMetadata )
	{
		return iMetadata;
	}
	
    public int GetCookCounter( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetCookCounter( blockAccess.getBlockMetadata( i, j, k ) );    
	}
    
    public int SetCookCounter( int iMetadata, int iCounter )
    {
    	return iCounter;
    }
    
    public void SetCookCounter( World world, int i, int j, int k, int iCounter )
    {
    	int iMetadata = SetCookCounter( world.getBlockMetadata( i, j, k ), iCounter );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public void SetCookCounterNoNotify( World world, int i, int j, int k, int iCounter )
    {
    	int iMetadata = SetCookCounter( world.getBlockMetadata( i, j, k ), iCounter );
    	
        world.setBlockMetadataWithClient( i, j, k, iMetadata );
    }
    
	protected void ScheduleUpdateBasedOnCookState( World world, int i, int j, int k )
	{
		int iTickRate = ComputeTickRateBasedOnFireFactor( world, i, j, k );
		
		iTickRate *= GetBlockCookTimeMultiplier( world, i, j + 1, k );
		
    	world.scheduleBlockUpdate( i, j, k, blockID, iTickRate );
	}

    private boolean CanBlockBeCooked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
	    	return block.GetCanBeCookedByKiln( blockAccess, i, j, k );
    	}
    	
    	return false;
    }
    
    private void CookBlock( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		if ( block.GetCanBeCookedByKiln( world, i, j, k ) )
			{
    			block.OnCookedByKiln( world, i, j, k );
			}
    	}
    }
    
    private boolean CheckKilnIntegrity( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBrickCount = 0;
    	
    	for ( int iTempFacing = 1; iTempFacing <= 5; iTempFacing++ )
    	{
    		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j + 1, k );
    		
    		tempPos.AddFacingAsOffset( iTempFacing );
    		
    		int iTempBlockID = blockAccess.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == Block.brick.blockID || 
				iTempBlockID == FCBetterThanWolves.fcKiln.blockID )
    		{
    			iBrickCount++;
    			
    	    	if ( iBrickCount >= 3 )
    	    	{
    	    		return true;
    	    	}
    		}
    	}
    	
		return false;
    }
    
    private int ComputeTickRateBasedOnFireFactor( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSecondaryFireFactor = 0;
    	
    	for ( int iOffset = -1; iOffset <= 1; iOffset++ )
    	{
        	for ( int kOffset = -1; kOffset <= 1; kOffset++ )
        	{
        		if ( iOffset != 0 || kOffset != 0 )
        		{
        			if ( blockAccess.getBlockId( i + iOffset, j - 1, k + kOffset ) == 
        				FCBetterThanWolves.fcBlockFireStoked.blockID )
					{
        				iSecondaryFireFactor++;
					}        				
        		}
        	}
    	}
    	
    	int iTickRate =  ( ( m_iMaxFireFactorBaseTickRate - m_iMinFireFactorBaseTickRate ) * 
    		( 8 - iSecondaryFireFactor ) / 8 ) + m_iMinFireFactorBaseTickRate;
    	
    	return iTickRate;
    }
    
    private int GetBlockCookTimeMultiplier( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
	    	return block.GetCookTimeMultiplierInKiln( blockAccess, i, j, k );
    	}
    	
    	return 1;
    }
    
	//----------- Client Side Functionality -----------//
}