//FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockDetectorBlock extends Block
{
    private final static int iDetectorTickRate = 4;
    
    public FCBlockDetectorBlock( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 3.5F );
        
        setStepSound( Block.soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockDetectorBlock" );
        
        setTickRandomly( true );
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }

	@Override
    public int tickRate( World world )
    {
        return iDetectorTickRate;
    }    
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, Block.GetOppositeFacing( iFacing ) );        
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack )
	{
		int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacingReversed( entityLiving );
		
		SetFacing( world, i, j, k, iFacing );		
	}
	
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
        this.SetBlockOn( world, i, j, k, false );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int l )
    {
		if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bPlacedLogic = PlaceDetectorLogicIfNecessary( world, i, j, k );    	
    	boolean bDetected = CheckForDetection( world, i, j, k );
    	
        int iFacingDirection = GetFacing( world, i, j, k );
        
        if ( iFacingDirection == 1 )
        {
	    	if ( !bDetected )
	    	{
            	// facing upwards...check for rain or snow

		    	if ( world.IsPrecipitatingAtPos( i, j + 1, k ) )
		    	{
		    		bDetected = true;		    		
		    	}		    	
            }
	    	
    		// upward facing blocks have to periodically poll for weather changes
    		// or they risk missing them.
    		
	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );		    		
    	}
    	
    	if ( bDetected )
    	{
    		if ( !IsBlockOn( world, i, j, k ) )
    		{
        		SetBlockOn( world, i, j, k, true );
    		}
    	}
    	else
    	{
    		if ( IsBlockOn( world, i, j, k ) )
    		{
	    		if ( !bPlacedLogic )
	    		{
	    			SetBlockOn( world, i, j, k, false );
	    		}
	    		else
	    		{
	    			// if we just placed the logic block, then wait a tick until we turn off
	    			// to give it a chance to detect anything that might be there
	    			
	    	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	    		}
    		}
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
		{
	        int iFacing = GetFacing( world, i, j, k );
	        
	        if ( iFacing == 1 )
	        {			
	        	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	        }
	        else
	        {
	        	if ( CheckForDetection( world, i, j, k ) != IsBlockOn( world, i, j, k ) )
	        	{
		        	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	        	}
	        }
		}
    }
	
	@Override
    public int isProvidingWeakPower( IBlockAccess iblockaccess, int i, int j, int k, int l )
    {
		if ( IsBlockOn( iblockaccess, i, j, k ) )
		{
			return 15;
		}
		
    	return 0;
    }
    
	@Override
    public int isProvidingStrongPower( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	// at present, the detectors don't indirectly power anything...they have to be hooked up directly.
    	
    	return 0;
    }
    
	@Override
    public boolean canProvidePower()
    {
        return true;
    }
    
    @Override
    public void OnArrowCollide( World world, int i, int j, int k, EntityArrow arrow )
    {
		if ( !world.isRemote )
		{
	        int iFacingDirection = GetFacing( world, i, j, k );
	        
			FCUtilsBlockPos logicBlockPos = new FCUtilsBlockPos( i, j, k );
			
			logicBlockPos.AddFacingAsOffset( iFacingDirection );
			
			if ( world.getBlockId( logicBlockPos.i, logicBlockPos.j, logicBlockPos.k ) == FCBetterThanWolves.fcBlockDetectorLogic.blockID )
			{
				FCBetterThanWolves.fcBlockDetectorLogic.onEntityCollidedWithBlock( world, logicBlockPos.i, logicBlockPos.j, logicBlockPos.k, arrow );
			}
		}
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
    	return ( iMetadata & (~1) ) >> 1;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata = ( iMetadata & 1 ) | ( iFacing << 1 );
    	
		return iMetadata;
	}
	
	@Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		if ( super.RotateAroundJAxis( world, i, j, k, bReverse ) )
		{
	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	    	
	    	return true;
		}
		
		return false;
	}

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );
		
		SetFacing( world, i, j, k, iFacing );
		
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
    	world.notifyBlockChange( i, j, k, blockID );
    	
    	return true;
	}
	
    //------------- Class Specific Methods ------------//
    
    public boolean IsBlockOn( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return IsBlockOnFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public boolean IsBlockOnFromMetadata( int iMetadata )
    {
    	return ( iMetadata & 1 ) > 0;
    }
    
    public void SetBlockOn( World world, int i, int j, int k, boolean bOn )
    {
    	if ( bOn != IsBlockOn( world, i, j, k ) )
    	{
	    	int iMetaData = world.getBlockMetadata(i, j, k);
	    	
	    	if ( bOn )
	    	{
	    		iMetaData = iMetaData | 1;
	    		
		        world.playAuxSFX( FCBetterThanWolves.m_iRedstonePowerClickSoundAuxFXID, i, j, k, 0 );							        
	    	}
	    	else
	    	{
	    		iMetaData = iMetaData & (~1);
	    	}
	    	
	        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
	        
	        // we have to notify in a radius as some redstone blocks get their power state from up to two blocks away
	        
            world.notifyBlocksOfNeighborChange( i, j - 1, k, blockID );
            world.notifyBlocksOfNeighborChange( i, j + 1, k, blockID );
            world.notifyBlocksOfNeighborChange( i - 1, j, k, blockID );
            world.notifyBlocksOfNeighborChange( i + 1, j, k, blockID );
            world.notifyBlocksOfNeighborChange( i, j, k - 1, blockID );
            world.notifyBlocksOfNeighborChange( i, j, k + 1, blockID );
	        
	        // the following forces a re-render (for texture change)
	        
	        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );    	
        }
    }

    /* 
     * returns true if a new logic block needed to be placed
     */
    public boolean PlaceDetectorLogicIfNecessary( World world, int i, int j, int k )
    {
        int iFacing = GetFacing( world, i, j, k );
        
        FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
        
        targetPos.AddFacingAsOffset( iFacing );
        
        int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
        
        if ( iTargetBlockID == 0 )
        {
        	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
        	
        	world.setBlock( targetPos.i, targetPos.j, targetPos.k, logicBlock.blockID, 0, 0 );
        	
        	logicBlock.SetIsDetectorLogicFlag( world, targetPos.i, targetPos.j, targetPos.k, true );
        	
        	// fully validate the block as there may also be beams passing through it
        	
        	logicBlock.FullyValidateBlock( world, targetPos.i, targetPos.j, targetPos.k );
        	
        	return true;
        }
        else if ( iTargetBlockID == FCBetterThanWolves.fcBlockDetectorLogic.blockID ||  
    		iTargetBlockID == FCBetterThanWolves.fcBlockDetectorGlowingLogic.blockID )
        {
        	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
        	
        	if ( !logicBlock.IsDetectorLogicFlagOn( world, targetPos.i, targetPos.j, targetPos.k ) )
        	{
        		// this logic block was created by a lens.  Flag it as detector logic and as an intersection point
        		
            	logicBlock.SetIsDetectorLogicFlag( world, targetPos.i, targetPos.j, targetPos.k, true );
            	
            	// legacy test to support old pre-lens detector logic
            	
            	if ( logicBlock.HasValidLensSource( world, targetPos.i, targetPos.j, targetPos.k ) )
            	{            		
            		logicBlock.SetIsIntersectionPointFlag( world, targetPos.i, targetPos.j, targetPos.k, true );
            	}
        	}
        }
        
    	return false;
    }
	
	public boolean CheckForDetection( World world, int i, int j, int k )
	{
        int iFacing = this.GetFacing( world, i, j, k );
        
        FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
        
        targetPos.AddFacingAsOffset( iFacing );        
        
        int targetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
        
        if ( targetBlockID > 0 )
        {
	        if ( FCBlockDetectorLogic.IsLogicBlock( targetBlockID ) )
	        {
	        	// check if the logic block is on
	        	
	        	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
	        	
	        	if ( logicBlock.IsEntityCollidingFlagOn( world, targetPos.i, targetPos.j, targetPos.k ) ||  
        			logicBlock.IsLitFlagOn( world, targetPos.i, targetPos.j, targetPos.k ) )
	        	{
	        		return true;
	        	}
	        	else 
	        	{
	        		// check for fully grown wheat below the logic block
	        		
	        		int iBlockBelowID = world.getBlockId( targetPos.i, targetPos.j - 1, targetPos.k );
	        		
	            	if ( iBlockBelowID == Block.crops.blockID && 
	        			world.getBlockMetadata( targetPos.i, targetPos.j - 1, targetPos.k ) >= 7 )
	    			{
	                	return true;
	    			}	        		
	        	}
	        }        
	        else
	        {
	        	if ( targetBlockID == FCBetterThanWolves.fcLens.blockID )
	        	{
	        		// if we're pointing towards a Lens, we act as a light-level detecor
	        		
	        		FCBlockLens lensBlock = (FCBlockLens)(FCBetterThanWolves.fcLens);
	        		
	        		if ( lensBlock.GetFacing( world, targetPos.i, targetPos.j, targetPos.k ) == Block.GetOppositeFacing( iFacing ) )
	        		{
	        			return lensBlock.IsLit( world, targetPos.i, targetPos.j, targetPos.k );
	        		}
	        	}
	        	
	        	return true;
	        }
        }
        
		return false;
	}
	
	//----------- Client Side Functionality -----------//
}
