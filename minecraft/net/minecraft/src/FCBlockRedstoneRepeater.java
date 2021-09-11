// FCMOD

package net.minecraft.src;

public class FCBlockRedstoneRepeater extends BlockRedstoneRepeater
{
    protected FCBlockRedstoneRepeater( int iBlockID, boolean bIsLit )
    {
        super( iBlockID, bIsLit );
        
        setHardness( 0F );
        
        if ( bIsLit )
        {
        	setLightValue( 0.625F );
        }
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "diode" );
        
        disableStats();
    }
    
    @Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}	
	
    @Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		int iOldMetadata = world.getBlockMetadata( i, j, k );
		
		int iNewMetadata = RotateMetadataAroundJAxis( iOldMetadata, bReverse );
		
		if ( iNewMetadata != iOldMetadata )
		{
			world.setBlockMetadataWithNotify( i, j, k, iNewMetadata );
			
			// this forces the repeater to update with the appropriate delay based on its settings
			
			onNeighborBlockChange( world, i, j, k, 0 );
			
	        // the following is a copy of func_94483_i_() in block redstone logic, except it is applied to the block's previous orientation
			
	        int iDirection = BlockDirectional.getDirection( iOldMetadata );

	        if ( iDirection == 1 )
	        {
	        	world.notifyBlockOfNeighborChange( i + 1, j, k, blockID );
	        	world.notifyBlocksOfNeighborChange( i + 1, j, k, blockID, 4 );
	        }
	        else if ( iDirection == 3 )
	        {
	        	world.notifyBlockOfNeighborChange( i - 1, j, k, blockID );
	        	world.notifyBlocksOfNeighborChange( i - 1, j, k, blockID, 5 );
	        }
	        else if ( iDirection == 2 )
	        {
	        	world.notifyBlockOfNeighborChange( i, j, k + 1, blockID );
	        	world.notifyBlocksOfNeighborChange( i, j, k + 1, blockID, 2 );
	        }
	        else if ( iDirection == 0 )
	        {
	        	world.notifyBlockOfNeighborChange( i, j, k - 1, blockID );
	        	world.notifyBlocksOfNeighborChange( i, j, k - 1, blockID, 3 );
	        }
	        
			return true;
		}
		
		return false;
	}
	
    @Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		int iOldMetaData = iMetadata;
		int iDirection = iMetadata & 3;
		
		if ( bReverse )
		{
			iDirection++;
			
			if ( iDirection > 3 )
			{
				iDirection = 0;
			}
		}
		else
		{
			iDirection--;
			
			if ( iDirection < 0 )
			{
				iDirection = 3;
			}
		}
		
		return ( iMetadata & (~3) ) | iDirection;
	}
	
    @Override
    public void OnRemovedByBlockDispenser( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	super.OnRemovedByBlockDispenser( world, i, j, k );
    	
    	if ( isRepeaterPowered )
    	{
        	// repeaters don't have their status properly sent to neighbors by the usual 
    		// "breakBlock" and thus need to have the following function manually called
        	
        	onBlockDestroyedByPlayer( world, i, j, k, iMetadata );
    	}
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ, int iMetadata )
	{
		// handles BD placement, player handled in parent onBlockPlaceBy()
		
    	if ( iFacing == 4 )
    	{
    		iMetadata = 1;
    	}
    	else if ( iFacing == 2 )
    	{
    		iMetadata = 2;
    	}
    	else if ( iFacing == 5 )
    	{
    		iMetadata = 3;
    	}
    	else
    	{
    		iMetadata = 0;                                	
    	}

		return iMetadata;
	}

	@Override
    public void onPostBlockPlaced( World world, int i, int j, int k, int iMetadata ) 
    {
		if ( func_94478_d( world, i, j, k, iMetadata ) )
		{
			// The above tests if the repeater is receiving power.  Don't do this otherwise as the parent
			// update function automatically toggles an off repeater on regardless of whether it is powered or not.
			
			// Required so that the repeater updates its state from nearby redstone
	    	// repeaters normally do this onPlacedBy(), but attempting to schedule another
			// is harmless and this covers BD placement.
	    	
			world.scheduleBlockUpdate( i, j, k, blockID, 1 );
		}
    }
    
	@Override
	public boolean TriggersBuddy()
	{
		return false;
	}
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderBlockRepeater( this, i, j, k );
    }    
}
