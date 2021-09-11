// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockDetectorLogic extends Block
{
    private final static int iDetectorLogicTickRate = 5;
    
    public final static boolean bLogicDebugDisplay = false;
    
    public FCBlockDetectorLogic( int iBlockID )
    {
        super( iBlockID, Material.air );  
        
        setTickRandomly( true );        
    }
    
	@Override
    public int tickRate( World world )
    {
        return iDetectorLogicTickRate;
    }    

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@Override
    public int getMobilityFlag()
    {
    	// disable the ability for piston to push this block
    	
        return 1;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
    }

	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		if ( !FCBetterThanWolves.bIsLensBeamBeingRemoved )
		{
			// this basically occurs if a logic block is removed by an external source, in which case it needs to
			// cut off any orphaned beams surrounding it
			
			if ( !IsDetectorLogicFlagOn( world, i, j, k ) || IsIntersectionPointFlagOn( world, i, j, k ) )
			{
				for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
				{
					int iRangeToSource = GetRangeToValidLensSourceToFacing( world, i, j, k, iTempFacing );
					
					if ( iRangeToSource > 0 )
					{
						// we have a valid source, kill the beam in the opposite direction, since it is now blocked
						
						int iBeamRangeRemaining = FCBlockLens.m_iLensMaxRange - iRangeToSource;
						
						if ( iBeamRangeRemaining > 0 )
						{
							RemoveLensBeamFromBlock( world, i, j, k, Block.GetOppositeFacing( iTempFacing ), iBeamRangeRemaining );
						}
					}
				}
			}
		}
    }
	
	@Override
    public int idDropped( int i, Random random, int iFortuneModifier )
    {
        return 0;
    }
    
	@Override
    public boolean canCollideCheck(int i, boolean flag)
    {
        return false;
    }    
    
	@Override
    public int quantityDropped( Random random )
    {
        return 0;
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	// this turns off entities not being able to pass through the block
    	
        return null;
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
		if ( IsDetectorLogicFlagOn( world, i, j, k ) )
		{
	    	if ( !CheckForNeighboringDetector( world, i, j, k ) )
			{
	    		// there is no longer an associated detector
	    		
	    		if ( IsIntersectionPointFlagOn( world, i, j, k ) )
	    		{
	    			SetIsDetectorLogicFlag( world, i, j, k, false );
	    			
	    			if ( !HasMultipleValidLensSources( world, i, j, k ) )
	    			{
	    				SetIsIntersectionPointFlag( world, i, j, k, false );	    				
	    			}
	    		}
	    		else
	    		{
	    			// no lens sources either...self destruct
	    			
	    			RemoveSelf( world, i, j, k );
	    		}	
			}
	    	else
	    	{
	    		// notify any neigboring detector blocks so that they can check for wheat growth
	    		
	    		NotifyNeighboringDetectorBlocksOfChange( world, i, j, k );	    	
    		}
		}
		
		// check if a beam is passing through this block
		
		if ( !IsDetectorLogicFlagOn( world, i, j, k ) || IsIntersectionPointFlagOn( world, i, j, k ) )
		{
			// a neigboring air block just appeared next to a beam.  
			// Schedule an immediate update to check for beam propagation
			
			if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
			{
				world.scheduleBlockUpdate( i, j, k, world.getBlockId( i, j, k ), 
					tickRate( world ) );
			}
		}
		
		if ( IsLitFlagOn( world, i, j, k ) && IsBlockGlowing( world, i, j, k ) )
		{
			// force re-render of glowing blocks in case lit facings have changed
			
        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
		}
    }
	
	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity)
    {
		if ( !world.isRemote )
		{
	    	if ( IsEntityWithinBounds( world, i, j, k ) )
	    	{
		    	boolean bIsOn = IsEntityCollidingFlagOn( world, i, j, k );
		    	
		    	if ( !bIsOn )
		    	{
		    		ChangeStateToRegisterEntityCollision( world, i, j, k );
		    		
		    		// schedule an update to check if the entity leaves.  Have to manually get the block ID as it may have changed above.
		    		
		        	world.scheduleBlockUpdate( i, j, k, world.getBlockId( i, j, k ), tickRate( world ) );
		    	}
	    	}
		}
    }
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bIsOn = IsEntityCollidingFlagOn( world, i, j, k );
    	
        boolean bShouldBeOn = IsEntityWithinBounds( world, i, j, k );
        
        if ( bShouldBeOn )
        {
        	if ( !bIsOn )
        	{
	    		ChangeStateToRegisterEntityCollision( world, i, j, k );
        	}

    		// schedule another update to check if the entity leaves.  Have to manually get the block ID as it may have changed above.
    		
        	world.scheduleBlockUpdate( i, j, k, world.getBlockId( i, j, k ), tickRate( world ) );
        }
        else 
        {
            if ( bIsOn )
            {        
	    		ChangeStateToClearEntityCollision( world, i, j, k );
            }
        }
        
        FullyValidateBlock( world, i, j, k );
    }
	
	@Override
    public boolean IsAirBlock()
    {
    	return true;
    }
    
	@Override
	public boolean TriggersBuddy()
	{
		return false;
	}
	
    //------------- Class Specific Methods ------------//
    
	protected void RemoveSelf( World world, int i, int j, int k )
	{
		// this function exists as the regular block shouldn't notify the client when it is removed, but the glowing variety should 
		
    	world.setBlock( i, j, k, 0, 0, 0 );        	
	}
    
	public boolean IsEntityCollidingFlagOn( IBlockAccess iBlockAccess, int i, int j, int k )
	{
        int iMetaData = iBlockAccess.getBlockMetadata( i, j, k );
        
		return ( iMetaData & 1 ) > 0;
	}
	
	public void SetEntityCollidingFlag( World world, int i, int j, int k, boolean bEntityColliding )
	{
        int iMetaData = ( world.getBlockMetadata( i, j, k ) ) & (~1); // filter out old state
        
        if ( bEntityColliding )
        {
        	iMetaData |= 1;
        }

        // need notify as this can affect the state of neighboring lenses and Detector Blocks
        
        world.setBlockMetadataWithNotifyNoClient( i, j, k, iMetaData );

        if ( bLogicDebugDisplay )
        {
        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        }
	}
	
	public boolean IsDetectorLogicFlagOn( IBlockAccess iBlockAccess, int i, int j, int k )
	{
        int iMetaData = iBlockAccess.getBlockMetadata( i, j, k );
        
		return ( iMetaData & 2 ) > 0;
	}
	
	public void SetIsDetectorLogicFlag( World world, int i, int j, int k, boolean bIsDetectorLogic )
	{
        int iMetaData = ( world.getBlockMetadata( i, j, k ) ) & (~2); // filter out old state
        
        if ( bIsDetectorLogic )
        {
        	iMetaData |= 2;
        }
        
        world.setBlockMetadata( i, j, k, iMetaData );
        
        if ( bLogicDebugDisplay )
        {
        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        }
	}
	
	/*
	 * blocks are considered intersection points if multiple lens beams pass through them or if both beams and detector logic are present.  
	 * They are NOT considered intersection points if only multiple detector blocks are facing them without a lens beam.
	 */
	public boolean IsIntersectionPointFlagOn( IBlockAccess iBlockAccess, int i, int j, int k )
	{
        int iMetaData = iBlockAccess.getBlockMetadata( i, j, k );
        
		return ( iMetaData & 4 ) > 0;
	}
	
	public void SetIsIntersectionPointFlag( World world, int i, int j, int k, boolean bIsIntersectionPoint )
	{
        int iMetaData = ( world.getBlockMetadata( i, j, k ) ) & (~4); // filter out old state
        
        if ( bIsIntersectionPoint )
        {
        	iMetaData |= 4;
        }
        
        world.setBlockMetadata( i, j, k, iMetaData );
        
        if ( bLogicDebugDisplay )
        {
        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        }
	}
	
	public boolean IsLitFlagOn( IBlockAccess iBlockAccess, int i, int j, int k )
	{
        int iMetaData = iBlockAccess.getBlockMetadata( i, j, k );
        
		return ( iMetaData & 8 ) > 0;
	}
	
	public void SetIsLitFlag( World world, int i, int j, int k, boolean bIsLitByLens )
	{
        int iMetaData = ( world.getBlockMetadata( i, j, k ) ) & (~8); // filter out old state
        
        if ( bIsLitByLens )
        {
        	iMetaData |= 8;
        }
        
        world.setBlockMetadata( i, j, k, iMetaData );
        
        if ( bLogicDebugDisplay )
        {
        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        }
	}
	
    public boolean IsBlockGlowing( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	
    	return iBlockID == FCBetterThanWolves.fcBlockDetectorGlowingLogic.blockID;
    }
    
    public void SetBlockAsGlowing( World world, int i, int j, int k )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k );
    	
    	FCBetterThanWolves.bIsLensBeamBeingRemoved = true;
    	
    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockDetectorGlowingLogic.blockID, iMetaData );
    	
    	FCBetterThanWolves.bIsLensBeamBeingRemoved = false;
    	
    	if ( IsEntityCollidingFlagOn( world, i, j, k ) )
    	{
    		// if the entity colliding flag is on, then we need to schedule an update for the new block ID
    		
    		world.scheduleBlockUpdate( i, j, k, world.getBlockId( i, j, k ), tickRate( world ) );
    	}
    }
    
    public void SetBlockAsNotGlowing( World world, int i, int j, int k )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k );
    	
    	FCBetterThanWolves.bIsLensBeamBeingRemoved = true;
    	
    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockDetectorLogic.blockID, iMetaData );
    	
    	FCBetterThanWolves.bIsLensBeamBeingRemoved = false;
    	
    	if ( IsEntityCollidingFlagOn( world, i, j, k ) )
    	{
    		// if the entity colliding flag is on, then we need to schedule an update for the new block ID
    		
    		world.scheduleBlockUpdate( i, j, k, world.getBlockId( i, j, k ), tickRate( world ) );
    	}
    }
    
    private boolean IsEntityWithinBounds( World world, int i, int j, int k )
    {    	
        List list = world.getEntitiesWithinAABB( Entity.class, 
        		AxisAlignedBB.getAABBPool().getAABB( (double)i, (double)j, (double)k, 
				(double)(i + 1), (double)(j + 1), (double)(k + 1) ) );
        
        if( list != null && list.size() > 0 )
        {
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
            	Entity targetEntity = (Entity)list.get( listIndex );
            	
            	// client
            	if ( !( targetEntity instanceof EntityFX ) && !( targetEntity instanceof FCEntityBlockLiftedByPlatform ) )
        		// server
            	//if ( !( targetEntity instanceof FCEntityBlockLiftedByPlatform ) )
            	{
            		return true;            		
            	}
            }
        }
        
        return false;
    }
    
    private boolean CheckForNeighboringDetector( World world, int i, int j, int k )
    {
    	for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
    	{
    		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    		
    		tempPos.AddFacingAsOffset( iTempFacing );
    		
    		if ( world.getBlockId( tempPos.i, tempPos.j, tempPos.k ) == 
    			FCBetterThanWolves.fcBlockDetector.blockID )
    		{    			
    			if ( ( (FCBlockDetectorBlock)( FCBetterThanWolves.fcBlockDetector ) ).
					GetFacing( world, tempPos.i, tempPos.j, tempPos.k ) == 
						Block.GetOppositeFacing( iTempFacing ) )
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public void NotifyNeighboringDetectorBlocksOfChange( World world, int i, int j, int k )
    {
    	// this function should be called instead of the normal "OnNotify" functions in world when setting the metadata
    	// to prevent weirdness with vanilla blocks.
    	
    	for ( int iFacing = 0; iFacing <= 5; iFacing++ )
    	{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    		
    		targetPos.AddFacingAsOffset( iFacing );
    		
    		int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    		
    		if ( iTargetBlockID == FCBetterThanWolves.fcBlockDetector.blockID )
    		{
    			Block.blocksList[iTargetBlockID].onNeighborBlockChange( world, 
    					targetPos.i, targetPos.j, targetPos.k, world.getBlockId( i, j, k ) );     		
			}
    	}
    }
    
    public boolean HasValidLensSource( World world, int i, int j, int k )
    {
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( HasValidLensSourceToFacing( world, i, j, k, iTempFacing ) )
    		{
    			return true;
    		}	
    	}
    	
    	return false;
    }
    
    public boolean HasValidLensSourceIgnoreFacing( World world, int i, int j, int k, int iIgnoreFacing )
    {
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( iTempFacing != iIgnoreFacing )
    		{
	    		if ( HasValidLensSourceToFacing( world, i, j, k, iTempFacing ) )
	    		{
	    			return true;
	    		}
    		}
    	}
    	
    	return false;
    }
    
    public boolean HasMultipleValidLensSources( World world, int i, int j, int k )
    {
    	int iLensCount = 0;
    	
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( HasValidLensSourceToFacing( world, i, j, k, iTempFacing ) )
    		{
    			iLensCount++;
    			
    			if ( iLensCount > 1 )
    			{
    				return true;
    			}
    		}	
    	}
    	
    	return false;
    }
    
    public boolean HasMultipleValidLensSourcesIgnoreFacing( World world, int i, int j, int k, int iIgnoreFacing )
    {
    	int iLensCount = 0;
    	
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( iTempFacing != iIgnoreFacing )
    		{
	    		if ( HasValidLensSourceToFacing( world, i, j, k, iTempFacing ) )
	    		{
	    			iLensCount++;
	    			
	    			if ( iLensCount > 1 )
	    			{
	    				return true;
	    			}
	    		}
    		}
    	}
    	
    	return false;
    }
    
    public int CountValidLensSources( World world, int i, int j, int k )
    {
    	int iLensCount = 0;
    	
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( HasValidLensSourceToFacing( world, i, j, k, iTempFacing ) )
    		{
    			iLensCount++;
    		}	
    	}
    	
    	return iLensCount;
    }
    
    public int CountValidLensSourcesIgnoreFacing( World world, int i, int j, int k, int iIgnoreFacing )
    {
    	int iLensCount = 0;
    	
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( iTempFacing != iIgnoreFacing )
    		{
	    		if ( HasValidLensSourceToFacing( world, i, j, k, iTempFacing ) )
	    		{
	    			iLensCount++;
	    		}
    		}
    	}
    	
    	return iLensCount;
    }
    
    public boolean HasValidLensSourceToFacing( World world, int i, int j, int k, int iFacing )
    {
    	return GetRangeToValidLensSourceToFacing( world, i, j, k, iFacing ) > 0;
    }
    
    /* 
     * returns 0 if no valid source within the maximum range of a lens
     */
    public int GetRangeToValidLensSourceToFacing( World world, int i, int j, int k, int iFacing )
    {
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    	
    	for ( int iDistance = 1; iDistance <= FCBlockLens.m_iLensMaxRange; iDistance++ )
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == FCBetterThanWolves.fcLens.blockID )
    		{
    			FCBlockLens lensBlock = (FCBlockLens)(FCBetterThanWolves.fcLens);
    			if ( lensBlock.GetFacing( world, tempPos.i, tempPos.j, tempPos.k ) ==
    				Block.GetOppositeFacing( iFacing ) )
    			{
					return iDistance;
    			}
    			
    			return 0;
    		}
    		else if ( !IsLogicBlock( iTempBlockID ) ) 
    		{
    			return 0;
    		}
    	}   	
    	
    	return 0;
    }
    
    public boolean VerifyLitByLens( World world, int i, int j, int k )
    {
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( HasValidLitLensSourceToFacing( world, i, j, k, iTempFacing ) )
    		{
    			return true;
    		}	
    	}
    	
    	return false;
    }
    
    public boolean VerifyLitByLensIgnoreFacing( World world, int i, int j, int k, int iIgnoreFacing )
    {
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( iTempFacing != iIgnoreFacing )
    		{
	    		if ( HasValidLitLensSourceToFacing( world, i, j, k, iTempFacing ) )
	    		{
	    			return true;
	    		}
    		}
    	}
    	
    	return false;
    }
    
    public boolean HasValidLitLensSourceToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	return GetRangeToValidLitLensSourceToFacing( blockAccess, i, j, k, iFacing ) > 0;
    }
    
    /* 
     * returns 0 if no valid source within the maximum range of a lens
     */
    public int GetRangeToValidLitLensSourceToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    	
    	for ( int iDistance = 1; iDistance <= FCBlockLens.m_iLensMaxRange; iDistance++ )
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = blockAccess.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == FCBetterThanWolves.fcLens.blockID )
    		{
    			FCBlockLens lensBlock = (FCBlockLens)(FCBetterThanWolves.fcLens);
    			if ( lensBlock.GetFacing( blockAccess, tempPos.i, tempPos.j, tempPos.k ) ==
    				Block.GetOppositeFacing( iFacing ) )
    			{
    				if ( lensBlock.IsLit( blockAccess, tempPos.i, tempPos.j, tempPos.k ) )
    				{
    					return iDistance;
    				}
    			}
    			
    			return 0;
    		}
    		else if ( IsLogicBlock( iTempBlockID ) ) 
    		{
    			if ( !IsLitFlagOn( blockAccess, tempPos.i, tempPos.j, tempPos.k ) || IsEntityCollidingFlagOn( blockAccess, tempPos.i, tempPos.j, tempPos.k ) )
    			{    					
    				return 0;
    			}
    		}
    		else
    		{
    			return 0;
    		}
    	}   	
    	
    	return 0;
    }
    
    /*
     * The beam created does not include the block specified 
     */
    public void CreateLensBeamFromBlock( World world, int i, int j, int k, int iFacing, int iMaxRange )
    {
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    	
    	for ( int iDistance = 1; iDistance <= iMaxRange; iDistance++ )
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == 0 )
    		{
    			if ( !world.setBlock( tempPos.i, tempPos.j, tempPos.k, FCBetterThanWolves.fcBlockDetectorLogic.blockID, 0, 0 ) )
    			{
    				// we couldn't set the block and have probably either hit the height limit or the edge of the loaded chunks.
    				// stop extending the beam beyond this point
    				
    				break;
    			}
    			
    	        if ( bLogicDebugDisplay )
    	        {
    	        	world.markBlockRangeForRenderUpdate( tempPos.i, tempPos.j, tempPos.k, tempPos.i, tempPos.j, tempPos.k );
    	        }
    		}
    		else if ( IsLogicBlock( iTempBlockID ) ) 
    		{
    			SetIsIntersectionPointFlag( world, tempPos.i, tempPos.j, tempPos.k, true );
    			
    		}
    		else
    		{
    			break;
    		}
    	}    	
    }
    
    /*
     * The beam created does not include the block specified 
     */
    public void RemoveLensBeamFromBlock( World world, int i, int j, int k, int iFacing, int iMaxRange  )
    {
    	FCBetterThanWolves.bIsLensBeamBeingRemoved = true;
    	
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    	int iOppositeFacing = Block.GetOppositeFacing( iFacing );
    	
    	for ( int iDistance = 1; iDistance <= FCBlockLens.m_iLensMaxRange; iDistance++ )
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		if ( IsLogicBlock( world, tempPos.i, tempPos.j, tempPos.k ) ) 
    		{
    			// Intersection points must be validated when a contributing beam is removed
    			
    			if ( IsIntersectionPointFlagOn( world, tempPos.i, tempPos.j, tempPos.k ) )
    			{	
    				if ( IsDetectorLogicFlagOn( world, tempPos.i, tempPos.j, tempPos.k ) )
    				{
    					if ( !HasValidLensSourceIgnoreFacing( world, tempPos.i, tempPos.j, tempPos.k, iOppositeFacing ) )
    					{
        					SetIsIntersectionPointFlag( world, tempPos.i, tempPos.j, tempPos.k, false );
    					}
    				}	
    				else if ( !HasMultipleValidLensSourcesIgnoreFacing( world, tempPos.i, tempPos.j, tempPos.k, iOppositeFacing ) )
    				{
    					SetIsIntersectionPointFlag( world, tempPos.i, tempPos.j, tempPos.k, false );
    				}
    				
    				if ( IsLitFlagOn( world, tempPos.i, tempPos.j, tempPos.k ) )
    				{
    					if ( !VerifyLitByLensIgnoreFacing( world, tempPos.i, tempPos.j, tempPos.k, iOppositeFacing ) )
    					{
    						UnlightBlock( world, tempPos.i, tempPos.j, tempPos.k );
    					}
    					else if ( IsBlockGlowing( world, tempPos.i, tempPos.j, tempPos.k ) )
        		    	{
        		    		if ( !ShouldBeGlowing( world, tempPos.i, tempPos.j, tempPos.k ) )
        		    		{
            		    		SetBlockAsNotGlowing( world, tempPos.i, tempPos.j, tempPos.k );
        		    		}
        		    	}						
    				}
    			}
    			else
    			{
    				if ( !IsBlockGlowing ( world, tempPos.i, tempPos.j, tempPos.k ) )
    				{
	    				if ( !world.setBlock( tempPos.i, tempPos.j, tempPos.k, 0, 0, 0 ) )
	    				{
	        				// we couldn't set the block and have probably either hit the height limit or the edge of the loaded chunks.
	        				// stop removing the beam beyond this point
	    					
	    					break;
	    				}
    				}
    				else
    				{
	    				if ( !world.setBlockWithNotify( tempPos.i, tempPos.j, tempPos.k, 0 ) )
	    				{
	        				// we couldn't set the block and have probably either hit the height limit or the edge of the loaded chunks.
	        				// stop removing the beam beyond this point
	    					
	    					break;
	    				}
    				}
    				
        	        if ( bLogicDebugDisplay )
        	        {
        	        	world.markBlockRangeForRenderUpdate( tempPos.i, tempPos.j, tempPos.k, tempPos.i, tempPos.j, tempPos.k );
        	        }
    			}    			
    		}
    		else
    		{
    			break;
    		}
    	}
    	
    	FCBetterThanWolves.bIsLensBeamBeingRemoved = false;
    }
    
    public void LightBlock( World world, int i, int j, int k )
    {
		SetIsLitFlag( world, i, j, k, true );
		
		if ( IsDetectorLogicFlagOn( world, i, j, k ) )
		{
    		NotifyNeighboringDetectorBlocksOfChange( world, i, j, k );
		}
    }
    
    public void UnlightBlock( World world, int i, int j, int k )
    {
		SetIsLitFlag( world, i, j, k, false );
		
    	if ( IsBlockGlowing( world, i, j, k ) )
    	{
    		SetBlockAsNotGlowing( world, i, j, k );
    	}
    	
		if ( IsDetectorLogicFlagOn( world, i, j, k ) )
		{
    		NotifyNeighboringDetectorBlocksOfChange( world, i, j, k );
		}
    }
    
	public void ChangeStateToRegisterEntityCollision( World world, int i, int j, int k )
	{
		SetEntityCollidingFlag( world, i, j, k, true );

		/*
		if ( IsDetectorLogicFlagOn( world, i, j, k ) )
		{
			NotifyNeighboringDetectorBlocksOfChange( world, i, j, k );
		}
		*/
		
		if ( IsLitFlagOn( world, i, j, k ) )
		{
			if ( !IsBlockGlowing( world, i, j, k ) )
	    	{
	    		SetBlockAsGlowing( world, i, j, k );
	    	}
			else
			{
				// mark the block as dirty due to entity collision blocking projection onto surfaces
				
	        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
			}
			
			// turn off any surrounding beams blocked by this one
			
			for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
			{
				int iRangeToSource = GetRangeToValidLitLensSourceToFacing( world, i, j, k, iTempFacing );
				
				if ( iRangeToSource > 0 )
				{
					int iBeamRangeRemaining = FCBlockLens.m_iLensMaxRange - iRangeToSource;
					
					if ( iBeamRangeRemaining > 0 )
					{
						TurnBeamOffFromBlock( world, i, j, k, Block.GetOppositeFacing( iTempFacing ), iBeamRangeRemaining );
					}
				}
			}
		}		
	}
	
	public void ChangeStateToClearEntityCollision( World world, int i, int j, int k )
	{
		SetEntityCollidingFlag( world, i, j, k, false );
		
		/*
		if ( IsDetectorLogicFlagOn( world, i, j, k ) )
		{
			NotifyNeighboringDetectorBlocksOfChange( world, i, j, k );			
		}
		*/
		
		// turn on any surrounding beams that were blocked by this one
		
		for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
		{
			int iRangeToSource = GetRangeToValidLitLensSourceToFacing( world, i, j, k, iTempFacing );
			
			if ( iRangeToSource > 0 )
			{
				int iBeamRangeRemaining = FCBlockLens.m_iLensMaxRange - iRangeToSource;
				
				if ( iBeamRangeRemaining > 0 )
				{
					TurnBeamOnFromBlock( world, i, j, k, Block.GetOppositeFacing( iTempFacing ), iBeamRangeRemaining );
				}
			}
		}
		
		if ( IsLitFlagOn( world, i, j, k ) )
		{
			if ( IsBlockGlowing( world, i, j, k ) )
	    	{
	    		if ( !ShouldBeGlowing( world, i, j, k ) )
	    		{
		    		SetBlockAsNotGlowing( world, i, j, k );
	    		}
	    		else
	    		{
					// mark the block as dirty due to entity collision blocking projection onto surfaces
					
		        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
	    		}
	    	}
		}
	}
    
    /*
     * The beam affected does not include the block specified 
     */
    public void TurnBeamOnFromBlock( World world, int i, int j, int k, int iFacing, int iMaxRange )
    {
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    	
    	for ( int iDistance = 1; iDistance <= iMaxRange; iDistance++ )
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( IsLogicBlock( iTempBlockID ) ) 
    		{
				LightBlock( world, tempPos.i, tempPos.j, tempPos.k );
				
    			if ( IsEntityCollidingFlagOn( world, tempPos.i, tempPos.j, tempPos.k ) ) 
    			{
    				// light can't propagate through entities
    				
    		    	if ( IsLogicBlock( world, tempPos.i, tempPos.j, tempPos.k ) && 
    		    			!IsBlockGlowing( world, tempPos.i, tempPos.j, tempPos.k ) )
    		    	{
    		    		SetBlockAsGlowing( world, tempPos.i, tempPos.j, tempPos.k );
    		    	}    		    	
    				
    				break;
    			}    			
    		}
    		else
    		{
    			if ( !world.isAirBlock( tempPos.i, tempPos.j, tempPos.k ) )
    			{
        			// create glowing block in previous
    				
    		    	FCUtilsBlockPos previousPos = new FCUtilsBlockPos( tempPos.i, tempPos.j, tempPos.k );
    		    	
    		    	previousPos.AddFacingAsOffset( Block.GetOppositeFacing( iFacing ) );
    		    	
    		    	if ( IsLogicBlock( world, previousPos.i, previousPos.j, previousPos.k ) && 
		    			!IsBlockGlowing( world, previousPos.i, previousPos.j, previousPos.k ) )
    		    	{
    		    		SetBlockAsGlowing( world, previousPos.i, previousPos.j, previousPos.k );
    		    	}    		    	
    			}    			
    			
    			break;
    		}
    	}    	
    }
    
    
    /*
     * The beam affected does not include the block specified 
     */
    public void TurnBeamOffFromBlock( World world, int i, int j, int k, int iFacing, int iMaxRange )
    {
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    	int iOppositeFacing = Block.GetOppositeFacing( iFacing );
    	
    	for ( int iDistance = 1; iDistance <= iMaxRange; iDistance++ )
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( IsLogicBlock( iTempBlockID ) ) 
    		{
    			if ( IsIntersectionPointFlagOn( world, tempPos.i, tempPos.j, tempPos.k ) )
    			{	
					if ( !VerifyLitByLensIgnoreFacing( world, tempPos.i, tempPos.j, tempPos.k, iOppositeFacing ) )
					{
						UnlightBlock( world, tempPos.i, tempPos.j, tempPos.k );						
					}
					else if ( IsBlockGlowing( world, tempPos.i, tempPos.j, tempPos.k ) )
    		    	{
    		    		if ( !ShouldBeGlowing( world, tempPos.i, tempPos.j, tempPos.k ) )
    		    		{
        		    		SetBlockAsNotGlowing( world, tempPos.i, tempPos.j, tempPos.k );
    		    		}
    		    	}						
    			}
    			else
    			{
					UnlightBlock( world, tempPos.i, tempPos.j, tempPos.k );					
    			}
    			
    			if ( IsEntityCollidingFlagOn( world, tempPos.i, tempPos.j, tempPos.k ) )
    			{
    				// light can't propagate through entities
    				
    				break;
    			}
    		}
    		else
    		{
    			break;
    		}
    	}    	
    }

    protected boolean ShouldBeProjectingToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	if ( IsEntityCollidingFlagOn( blockAccess, i, j, k ) )
    	{
    		return false;
    	}
    	
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    	
		if ( blockAccess.isBlockNormalCube( targetPos.i, targetPos.j, targetPos.k ) )
		{
			// we have a solid block neigboring that light can project onto.  Check if there's a beam hitting it.
			
			if ( HasValidLitLensSourceToFacing( blockAccess, i, j, k, Block.GetOppositeFacing( iFacing ) ) )
			{
				return true;
			}				
		}
		
    	return false;
    }
    
    protected boolean ShouldBeGlowingToFacing( World world, int i, int j, int k, int iFacing )
    {
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		targetPos.AddFacingAsOffset( iFacing );
    	
		if ( !world.isAirBlock( targetPos.i, targetPos.j, targetPos.k ) )
		{
			// we have a neigboring block that can interrupt a beam.  Check if there's a beam hitting it.
			
			if ( HasValidLitLensSourceToFacing( world, i, j, k, Block.GetOppositeFacing( iFacing ) ) )
			{
				return true;
			}				
		}
		
    	return false;
    }
    
    /*
     * Assumes the block is lit
     */
    private boolean ShouldBeGlowing( World world, int i, int j, int k )
    {
    	if ( this.IsEntityCollidingFlagOn( world, i, j, k ) )
		{
    		return true;
		}
    	
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( ShouldBeGlowingToFacing( world, i, j, k, iTempFacing ) )
    		{
    			return true;
    		}	
    	}
    	
    	return false;
    }
    
    static boolean IsLogicBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	
    	return IsLogicBlock( iBlockID );
    	
    }
    
    static boolean IsLogicBlock( int iBlockID )
    {
    	return ( iBlockID ==  FCBetterThanWolves.fcBlockDetectorLogic.blockID || 
			iBlockID ==  FCBetterThanWolves.fcBlockDetectorGlowingLogic.blockID ); 
    }
    
	void PropagateBeamsThroughBlock( World world, int i, int j, int k )
	{
		boolean bIsLit = false;
		int iSourceCount = 0;
		
		if ( !world.setBlock( i, j, k, FCBetterThanWolves.fcBlockDetectorLogic.blockID, 0, 0 ) )
		{
			// we're probably on a chunk boundary or at the height limit.  Don't try to propagate
			
			return;
		}
		
        if ( bLogicDebugDisplay )
        {
        	world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        }		
		
		for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
		{
			int iRangeToSource = GetRangeToValidLensSourceToFacing( world, i, j, k, iTempFacing );
			
			if ( iRangeToSource > 0 )
			{
				iSourceCount++;
				
				int iRangeRemaining = FCBlockLens.m_iLensMaxRange - iRangeToSource; 
				
				if ( iRangeRemaining > 0 )
				{
					int iOppositeFacing = Block.GetOppositeFacing( iTempFacing );
					
					CreateLensBeamFromBlock( world, i, j, k, iOppositeFacing, iRangeRemaining );
					
					if ( HasValidLitLensSourceToFacing( world, i, j, k, iTempFacing ) )
					{
						TurnBeamOnFromBlock( world, i, j, k, iOppositeFacing, iRangeRemaining );
						
						bIsLit = true;
					}
				}
				else
				{
					if ( HasValidLitLensSourceToFacing( world, i, j, k, iTempFacing ) )
					{
						bIsLit = true;
					}
				}
			}
		}
		
		if ( bIsLit )
		{
			LightBlock( world, i, j, k );
			
    		if ( ShouldBeGlowing( world, i, j, k ) )
    		{
	    		SetBlockAsGlowing( world, i, j, k );
    		}			
		}
		
		if ( iSourceCount > 1 )
		{
			SetIsIntersectionPointFlag( world, i, j, k, true );
		}
	}
    
	public void FullyValidateBlock( World world, int i, int j, int k )
	{
		boolean bHasDetector = CheckForNeighboringDetector( world, i, j, k );
		
		if ( bHasDetector != IsDetectorLogicFlagOn( world, i, j, k ) )
		{
			SetIsDetectorLogicFlag( world, i, j, k, bHasDetector );
		}
		
		boolean bShouldBeLit = false;
		int iNumLensSources = 0;
		
		// cycle through potential sources
		
		for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
		{
			int iRangeToSource = GetRangeToValidLensSourceToFacing( world, i, j, k, iTempFacing );
			
			if ( iRangeToSource > 0 )
			{
				iNumLensSources++;
				
				if ( !bShouldBeLit )
				{
					// FCTODO: This can be optimized so as not to require the 2nd scan along the beam
					if ( HasValidLitLensSourceToFacing( world, i, j, k, iTempFacing ) )
					{						
						bShouldBeLit = true;
					}
				}
				
				// check for beam propagation
				
				int iRangeRemaining = FCBlockLens.m_iLensMaxRange - iRangeToSource;
				
				if ( iRangeRemaining > 0 )
				{
					FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
					
					targetPos.AddFacingAsOffset( Block.GetOppositeFacing( iTempFacing ) );
					
					if ( world.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == 0 )
					{
						// this block should contain a logic block, but doesn't.
						
						PropagateBeamsThroughBlock( world, targetPos.i, targetPos.j, targetPos.k );
					}
				}
			}				
		}
		
		if ( iNumLensSources == 0 && !IsDetectorLogicFlagOn( world, i, j, k ) )
		{
			// this is an orphaned logic block.  Kill it.
			
	    	FCBetterThanWolves.bIsLensBeamBeingRemoved = true;
	    	
	    	RemoveSelf( world, i, j, k );
			
	    	FCBetterThanWolves.bIsLensBeamBeingRemoved = false;
		}
		else
		{
			boolean bShouldBeIntersectionPoint = false;
			
			if ( iNumLensSources > 1 || ( iNumLensSources == 1 && IsDetectorLogicFlagOn( world, i, j, k ) ) )
			{						
				bShouldBeIntersectionPoint = true;
			}
			
			if ( bShouldBeIntersectionPoint != IsIntersectionPointFlagOn( world, i, j, k ) )
			{
				SetIsIntersectionPointFlag( world, i, j, k, bShouldBeIntersectionPoint );
			}
			
			if ( bShouldBeLit != IsLitFlagOn( world, i, j, k ) )
			{
				if ( bShouldBeLit )
				{
					LightBlock( world, i, j, k );
				}
				else
				{
					UnlightBlock( world, i, j, k );
				}
			}
		}
		
		if ( IsLitFlagOn( world, i, j, k ) )
		{
			// we don't have to worry about turning glowing off if it's not lit as that should be handle by the unlight code above
			
			boolean bShouldGlow = ShouldBeGlowing( world, i, j, k );
			
			if ( bShouldGlow != this.IsBlockGlowing( world, i, j, k ) )
			{
				if ( bShouldGlow )
				{
		    		SetBlockAsGlowing( world, i, j, k );
				}
				else
				{
		    		SetBlockAsNotGlowing( world, i, j, k );
				}
			}			
		}
	}
	
	//----------- Client Side Functionality -----------//
	
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockLens_spotlight" );
    }	
    
    //------------- Custom Renderers ------------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	if ( bLogicDebugDisplay )
    	{
    		return RenderDetectorLogicDebug( renderBlocks, renderBlocks.blockAccess, i, j, k, this );
    	}
    	else
    	{
        	return false;
    	}
    }
    
    public boolean RenderDetectorLogicDebug
    ( 
		RenderBlocks renderBlocks, 
		IBlockAccess blockAccess, 
		int i, int j, int k, 
		Block block 
	)
    {
    	/*
    	int iTexture = blockIndexInTexture;
    	
    	if ( IsEntityCollidingFlagOn( blockAccess, i, j, k ) )
    	{
    		iTexture = m_iDebugTextureCollision;
    	}
    	else if ( IsLitFlagOn( blockAccess, i, j, k ) )
    	{
    		iTexture = m_iDebugTextureOn;
    	}
    	else
    	{
    		iTexture = m_iDebugTextureOff;
    	}

    	if ( !IsIntersectionPointFlagOn( blockAccess, i, j, k ) )
    	{
	        block.setBlockBounds( 0.5F - ( 2.0F / 16.0F), 0.5F - ( 2.0F / 16.0F), 0.5F - ( 2.0F / 16.0F), 
	        		0.5F + ( 2.0F / 16.0F), 0.5F + ( 2.0F / 16.0F), 0.5F + ( 2.0F / 16.0F) );
	        
	        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, iTexture );
    	}
    	else
    	{
	        block.setBlockBounds( 0.5F - ( 6.0F / 16.0F), 0.5F - ( 2.0F / 16.0F), 0.5F - ( 2.0F / 16.0F), 
	        		0.5F + ( 6.0F / 16.0F), 0.5F + ( 2.0F / 16.0F), 0.5F + ( 2.0F / 16.0F) );
	        
	        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, iTexture );
	        
	        block.setBlockBounds( 0.5F - ( 2.0F / 16.0F), 0.5F - ( 6.0F / 16.0F), 0.5F - ( 2.0F / 16.0F), 
	        		0.5F + ( 2.0F / 16.0F), 0.5F + ( 6.0F / 16.0F), 0.5F + ( 2.0F / 16.0F) );
	        
	        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, iTexture );
	        
	        block.setBlockBounds( 0.5F - ( 2.0F / 16.0F), 0.5F - ( 2.0F / 16.0F), 0.5F - ( 6.0F / 16.0F), 
	        		0.5F + ( 2.0F / 16.0F), 0.5F + ( 2.0F / 16.0F), 0.5F + ( 6.0F / 16.0F) );
	        
	        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, iTexture );
    	}
    	
    	if ( IsDetectorLogicFlagOn( blockAccess, i, j, k ) )
    	{
	        block.setBlockBounds( 0, 0, 0, 
	        		( 2.0F / 16.0F), ( 2.0F / 16.0F), ( 2.0F / 16.0F) );
	        
	        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, iTexture );	        
    	}
        
        // restore block bounds
        
    	setBlockBounds( 0.0F, 0.0F, 0.0F, 
    			1.0F, 1.0F, 1.0F );
		*/
    	
    	return true;
    }
    
    public void RenderDetectorLogicDebugInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block, 
		int iItemDamage, 
		int iRenderType 
	)
    {
    	// no inv render
    }
}
