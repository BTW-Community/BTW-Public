// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCement extends BlockContainer
{
	private final int iCementTexture;
	private final int iCementPartiallyDryTexture;
	
	public static final int iMaxCementSpreadDist = 16;
	public static final int iCementTicksToDry = 12;
	public static final int iCementTicksToPartiallyDry = 8;
	
	// these array are used within CheckSideBlocksForPotentialSpread, and I assume are declared
	// here to avoid allocating every time the method is exectuted (they were ripped out of BlockFLuid)
	
    boolean tempSpreadToSideFlags[];
    int tempClosestDownslopeToSideDist[];	
	
    protected FCBlockCement( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialCement );
   
        InitBlockBounds( 0F, 0F, 0F, 1F, 1F, 1F );
        
        setHardness( 100F );
        setLightOpacity( 255 );
        setUnlocalizedName( "fcBlockCement" );
        
        setStepSound( Block.soundSandFootstep );
        
        iCementTexture = 15;
        iCementPartiallyDryTexture = 16;        
        
        tempSpreadToSideFlags = new boolean[4];
        tempClosestDownslopeToSideDist = new int[4];
        
        Block.useNeighborBrightness[iBlockID] = true;
        
        setTickRandomly( true );        
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityCement();
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
    public boolean canCollideCheck( int i, boolean flag )
    {
        return flag && i == 0;
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	if ( world.getBlockId( i, j + 1, k ) != blockID )
    	{
	        return AxisAlignedBB.getAABBPool().getAABB( i, j, k, 
	        		(float)(i + 1), (float)j + 0.5F, (float)(k + 1) );
    	}
    	else
    	{
	        return AxisAlignedBB.getAABBPool().getAABB( i, j, k, 
	        		(float)(i + 1), (float)( j + 1), (float)(k + 1) );
    	}
    }
    
	@Override
    public int idDropped( int i, Random random, int iFortuneModifier )
    {
        return 0;
    }

	@Override
    public int quantityDropped( Random random )
    {
        return 0;
    }
    
	@Override
    public int tickRate( World world )
    {
    	return 20;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
        if ( world.getBlockId( i, j, k ) == blockID )
        {
            world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
        }
    }
    
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        int cementDist = GetCementSpreadDist( world, i, j, k );

        if ( cementDist > 0 )
        {
            int newCementDist = -100;
            
            newCementDist = CheckForLesserSpreadDist( world, i - 1, j, k, newCementDist );
            newCementDist = CheckForLesserSpreadDist( world, i + 1, j, k, newCementDist );
            newCementDist = CheckForLesserSpreadDist( world, i, j, k - 1, newCementDist );
            newCementDist = CheckForLesserSpreadDist( world, i, j, k + 1, newCementDist );
            newCementDist = CheckForLesserSpreadDist( world, i, j + 1, k, newCementDist );
            
            if( newCementDist < 0 )
            {
            	newCementDist = -1;
            }
            else
            {
                newCementDist += 1;
            }
            
            int cementDistUp = GetCementSpreadDist(world, i, j + 1, k);
            
            if( cementDistUp >= 0 )
            {
            	if ( cementDistUp < newCementDist )
            	{
            		newCementDist = cementDistUp + 1;
            	}
            }
            
    		if ( newCementDist > 0 && newCementDist < cementDist )
        	{            
        		cementDist = newCementDist;
                
                SetCementSpreadDist( world, i, j, k, cementDist );
                
                // reset the dry time, as this block just got a fresh infusion
                
                SetCementDryTime( world, i, j, k, 0 );
        	}
        } 
        
        int iDryTime = GetCementDryTime( world, i, j, k );
        
        iDryTime++;
        
        int minDryTime = CheckNeighboursCloserToSourceForMinDryTime( world, i, j, k );
        
        if ( minDryTime <= iDryTime )
        {
        	if ( minDryTime <= 0 )
        	{
        		iDryTime = 0;
        	}
        	else
        	{
        		iDryTime = minDryTime - 1;
        	}
        }
        
        if ( iDryTime > iCementTicksToDry )
        {
        	// solidify cement after its timer expires
        	
            world.setBlockWithNotify( i, j, k, Block.stone.blockID );            
        }
        else
        {
            SetCementDryTime( world, i, j, k, iDryTime );
            
        	// if the block hasn't dried out, then schedule another update 
        	
            world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	        
	        if ( IsBlockOpenToSpread( world, i, j - 1, k ) )
	        {
	        	// propagate to block below
	        	
	        	int targetCementDist = cementDist + 1;
	        	
	        	if ( targetCementDist <= iMaxCementSpreadDist )
				{
	       			world.setBlockWithNotify( i, j - 1, k, blockID );
	       			
	       			SetCementSpreadDist( world, i, j - 1, k, targetCementDist );
				}
	        } 
	        else if ( cementDist >= 0 && ( cementDist == 0 || blockBlocksFlow( world, i, j - 1, k ) ) )
	        {
	        	// can also call CheckSideBlocksForDownslope() here for alternative spread pattern
	        	
	            boolean spreadToSideFlags[] = CheckSideBlocksForPotentialSpread(world, i, j, k); 
	            
	            int spreadDist = cementDist + 1;
	            
	            if( spreadDist <= iMaxCementSpreadDist )
	            {
		            if( spreadToSideFlags[0] )
		            {
		            	AttemptToSpreadToBlock( world, i - 1, j, k, spreadDist );
		            }
		            if( spreadToSideFlags[1] )
		            {
		            	AttemptToSpreadToBlock( world, i + 1, j, k, spreadDist );
		            }
		            if( spreadToSideFlags[2] )
		            {
		            	AttemptToSpreadToBlock( world, i, j, k - 1, spreadDist );
		            }
		            if( spreadToSideFlags[3] )
		            {
		            	AttemptToSpreadToBlock( world, i, j, k + 1, spreadDist );
		            }
	            }
	        }
        }
    }
    
    @Override
    public boolean GetCanBlockBeIncinerated( World world, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean IsPowered( IBlockAccess blockAccess, int i, int j, int k )
    {    	
    	// Deprecated.  Just left here as a reminder that old blocks may remain with this bit set
    	
        int iMetaData = blockAccess.getBlockMetadata(i, j, k);
        
    	return ( iMetaData & 1 ) > 0;    	
    }
    
    public float GetRenderHeight( IBlockAccess blockAccess, int i, int j, int k ) // corresponds to SetFluidHeight() in BlockFluods
    {
    	// the return value of this function is inverted.  0.0 is full block height, 1.0 zero height.
    	
    	float fRenderHeight = 1.0F;
    	
        if ( blockAccess.getBlockMaterial( i, j, k ) == blockMaterial )
        {
	    	int dist = GetCementSpreadDist( blockAccess, i, j, k );
	    	
	        fRenderHeight = ( (float)( dist + 1 ) ) / ( (float)( iMaxCementSpreadDist + 2 ) ); 

	        // the following seems to cause tears in the cement mesh for some reason
	        // I've yet to figure out why this happens.  It's like some blocks are updating
	        // their visual while the neighbours aren't
	        
	        if ( IsCementPartiallyDry( blockAccess, i, j, k ) )
	        {
	        	// Increase the height when the cement is almost dry to blend with the solid block
	        	// this set the min height to 75% of that of a full block
	        	
	        	fRenderHeight *= 0.10F;  
	        }
	        else
	        {
	        	// this effectively sets the minimum height at a half-block
	        	
	        	fRenderHeight *= 0.5F;  
	        }
        } 
        
        return fRenderHeight;
    }  
    
    public int GetCementSpreadDist( IBlockAccess blockAccess, int i, int j, int k ) // corresponds to fun_290_h in BlockFluids
    {
        if ( blockAccess.getBlockMaterial( i, j, k ) != blockMaterial )
        {
            return -1;
        } 
        else
        {
        	FCTileEntityCement tileEntity = (FCTileEntityCement)blockAccess.getBlockTileEntity( i, j, k );
        	
            return tileEntity.GetSpreadDist();
        }
    }
    
    public void SetCementSpreadDist( World world, int i, int j, int k, int iSpreadDist )
    {
    	FCTileEntityCement tileEntity = (FCTileEntityCement)world.getBlockTileEntity( i, j, k );
    	
        tileEntity.SetSpreadDist( iSpreadDist );
        
        world.notifyBlocksOfNeighborChange( i, j, k, blockID );
        world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
    }

    public boolean IsCementSourceBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return ( GetCementSpreadDist( blockAccess, i, j, k ) == 0 );
    }
    
    public int GetCementDryTime( IBlockAccess blockAccess, int i, int j, int k ) // corresponds to fun_290_h in BlockFluids
    {
        if ( blockAccess.getBlockMaterial( i, j, k ) == blockMaterial )
        {
        	TileEntity tileEntity = blockAccess.getBlockTileEntity( i, j, k );
        	
        	if ( tileEntity instanceof FCTileEntityCement )
        	{
	        	FCTileEntityCement cementTileEntity = (FCTileEntityCement)blockAccess.getBlockTileEntity( i, j, k );
	        	
	            return cementTileEntity.GetDryTime();
        	}
        }
        
        return 0;
    }
    
    public void SetCementDryTime( World world, int i, int j, int k, int iDryTime )
    {
    	FCTileEntityCement tileEntity = (FCTileEntityCement)world.getBlockTileEntity( i, j, k );
    	
        tileEntity.SetDryTime( iDryTime );
        
        world.notifyBlocksOfNeighborChange( i, j, k, blockID );
        world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
    }
    
    public boolean IsCementPartiallyDry( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return ( GetCementDryTime( blockAccess, i, j, k ) >= iCementTicksToPartiallyDry ); 
    }
    
    private int CheckNeighboursCloserToSourceForMinDryTime( World world, int i, int j, int k )
    {    	
    	int minDryTime = 1000;
    	int distToSource = GetCementSpreadDist( world, i, j, k );

    	minDryTime = GetLesserDryTimeIfCloserToSource( world, i, j + 1, k, distToSource, minDryTime );
    	
    	minDryTime = GetLesserDryTimeIfCloserToSource( world, i + 1, j, k, distToSource, minDryTime );
    	minDryTime = GetLesserDryTimeIfCloserToSource( world, i - 1, j, k, distToSource, minDryTime );
    	
    	minDryTime = GetLesserDryTimeIfCloserToSource( world, i, j, k + 1, distToSource, minDryTime );
    	minDryTime = GetLesserDryTimeIfCloserToSource( world, i, j, k - 1, distToSource, minDryTime );
    	
    	return minDryTime;
    }
    
    private int GetLesserDryTimeIfCloserToSource( World world, int i, int j, int k, int distToSource, int dryTime )
    {
        Material material = world.getBlockMaterial(i, j, k);
        
        if ( material == blockMaterial )
        {
	    	int targetDistToSource = GetCementSpreadDist( world, i, j, k );
	    	
	    	if ( targetDistToSource < distToSource )
	    	{
	    		int targetDryTime = GetCementDryTime( world, i, j, k );
	    		
	    		if ( targetDryTime < dryTime )
	    		{
	    			return targetDryTime; 
	    		}
	    	}
        }
    	
    	return dryTime;
    }
    
    private void AttemptToSpreadToBlock( World world, int i, int j, int k, int newSpreadDist )
    {
        if( IsBlockOpenToSpread( world, i, j, k) )
        {
            int i1 = world.getBlockId( i, j, k );
            
            if( i1 > 0 )
            {
                Block.blocksList[i1].dropBlockAsItem( world, i, j, k, world.getBlockMetadata(i, j, k), 0 );
            }
            
   			world.setBlockWithNotify( i, j, k, blockID );
   			
   			SetCementSpreadDist( world, i, j, k, newSpreadDist );
        }
    }

    private boolean[] CheckSideBlocksForPotentialSpread( World world, int i, int j, int k )
    {
    	// alternative spread method to CheckSideBlocksForDownslope.  Just spreads uniformly around
    	// source instead of checking for downslopes
    	
        for ( int sideNum = 0; sideNum < 4; sideNum++ )
        {
            int iSide = i;
            int jSide = j;
            int kSide = k;

            switch ( sideNum )
            {
            	case 0:
            		
                	iSide--;
                	
                	break;
                	
            	case 1:
            		
                	iSide++;
                	
            		break;
            		
            	case 2:
            		
                	kSide--;
                	
            		break;
            		
        		default:
        			
                	kSide++;
                	
        			break;            		
            }
            
            if ( blockBlocksFlow( world, iSide, jSide, kSide ) || 
            		( world.getBlockMaterial( iSide, jSide, kSide ) == blockMaterial && 
            		IsCementSourceBlock( world, iSide, jSide, kSide ) ) )
            {
            	tempSpreadToSideFlags[sideNum] = false;
            }
            else
            {
            	tempSpreadToSideFlags[sideNum] = true;
            }            
        }

    	return tempSpreadToSideFlags;
    }

    private boolean[] CheckSideBlocksForDownslope( World world, int i, int j, int k ) // corresponds to func_297_k in BlockFLuids
    {
        for ( int sideNum = 0; sideNum < 4; sideNum++ )
        {
        	tempClosestDownslopeToSideDist[sideNum] = 1000;
            
            int iSide = i;
            int jSide = j;
            int kSide = k;
            
            if( sideNum == 0 )
            {
            	iSide--;
            }
            else if( sideNum == 1 )
            {
            	iSide++;
            }            
            else if ( sideNum == 2 )
            {
            	kSide--;
            }
            else if ( sideNum == 3 )
            {
            	kSide++;
            }
            
            if ( blockBlocksFlow( world, iSide, jSide, kSide ) || 
        		( world.getBlockMaterial( iSide, jSide, kSide ) == blockMaterial && 
        		IsCementSourceBlock( world, iSide, jSide, kSide ) ) )
            {
                continue;
            }
            
            if ( !blockBlocksFlow( world, iSide, jSide - 1, kSide ) )
            {
            	tempClosestDownslopeToSideDist[sideNum] = 0;
            } 
            else
            {
            	tempClosestDownslopeToSideDist[sideNum] = 
            		RecursivelyCheckSideBlocksForDownSlope( world, iSide, jSide, kSide, 1, sideNum );
            }
        }

        int minDistanceToDownslope = tempClosestDownslopeToSideDist[0];
        
        for ( int tempSide = 1; tempSide < 4; tempSide++ )
        {
            if ( tempClosestDownslopeToSideDist[tempSide] < minDistanceToDownslope )
            {
            	minDistanceToDownslope = tempClosestDownslopeToSideDist[tempSide];
            }
        }

        for ( int tempSide = 0; tempSide < 4; tempSide++ )
        {
        	tempSpreadToSideFlags[tempSide] = tempClosestDownslopeToSideDist[tempSide] == minDistanceToDownslope;
        }

        return tempSpreadToSideFlags;
    }

    private int RecursivelyCheckSideBlocksForDownSlope( World world, int i, int j, int k, 
    		int recursionCount,	int originSideNum )
    {
        int closestDownslope = 1000;
        
        for ( int tempSideNum = 0; tempSideNum < 4; tempSideNum++ )
        {
            if ( tempSideNum == 0 && originSideNum == 1 || 
        		tempSideNum == 1 && originSideNum == 0 || 
        		tempSideNum == 2 && originSideNum == 3 || 
        		tempSideNum == 3 && originSideNum == 2 )
            {
                continue;
            }
            
            int tempi = i;
            int tempj = j;
            int tempk = k;
            
            if ( tempSideNum == 0 )
            {
            	tempi--;
            }
            else if ( tempSideNum == 1 )
            {
            	tempi++;
            }
            else if ( tempSideNum == 2 )
            {
            	tempk--;
            }
            else if ( tempSideNum == 3 )
            {
            	tempk++;
            }
            
            if ( blockBlocksFlow(world, tempi, tempj, tempk ) || 
        		GetCementSpreadDist( world, tempi, tempj, tempk ) == 0 )
            {
                continue;
            }
            
            if ( !blockBlocksFlow( world, tempi, tempj - 1, tempk ) )
            {
                return recursionCount;
            }
            
            if( recursionCount >= 4 )
            {
                continue;
            }
            
            int tempSideClosestDownslope = RecursivelyCheckSideBlocksForDownSlope( world, tempi, tempj, tempk, recursionCount + 1, tempSideNum );
            
            if ( tempSideClosestDownslope < closestDownslope )
            {
            	closestDownslope = tempSideClosestDownslope;
            }
        }

        return closestDownslope;
    }    
    
    private boolean blockBlocksFlow( World world, int i, int j, int k )
    {
        Block block = blocksList[world.getBlockId(i, j, k)];
        
        return block != null && block.blockMaterial != blockMaterial && block.GetPreventsFluidFlow( world, i, j, k, this );
    }

    protected int CheckForLesserSpreadDist( World world, int i, int j, int k, int sourceSpreadDist )
    {
        int targetSpreadDist = GetCementSpreadDist( world, i, j, k );
        
        if ( targetSpreadDist < 0 )
        {
            return sourceSpreadDist;
        }
        
        if ( sourceSpreadDist < 0 || targetSpreadDist < sourceSpreadDist )
        {
        	return targetSpreadDist;
        }
        else
        {
        	return sourceSpreadDist;
        }        
    }

    private boolean IsBlockOpenToSpread(World world, int i, int j, int k)
    {
        if ( j < 0 )
        {
        	return false;
        }
        
        Material material = world.getBlockMaterial(i, j, k);
        
        if ( material == blockMaterial )
        {
            return false;
        }
        
        return !blockBlocksFlow(world, i, j, k);
    }
    
	//----------- Client Side Functionality -----------//
}