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
	
    private Icon m_IconDrying;
    
	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	if ( IsCementPartiallyDry( blockAccess, i, j, k ) )
		{
    		return m_IconDrying;
		}
    	else
    	{
    		return blockIcon;
    	}
    }
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockCement" );
        
        m_IconDrying = register.registerIcon( "fcBlockCement_drying" );        
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	return RenderCement( renderBlocks, renderBlocks.blockAccess, i, j, k, this );
    }
    
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
        Material material = blockAccess.getBlockMaterial( iNeighborI, iNeighborJ, iNeighborK );
        
        if ( material == blockMaterial )
        {
            return false;
        }
        
        if ( material == Material.ice )
        {
            return false;
        }
        
        if(iSide == 1)
        {
            return true;
        } 
        else
        {
            return super.shouldSideBeRendered( blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide );
        }
    }

	@Override
    public float getBlockBrightness(IBlockAccess iblockaccess, int i, int j, int k)
    {
    	// I think this causes a chain reaction so that fluids are always the same brightness
    	// in the same column
    	
        float f = iblockaccess.getLightBrightness( i, j, k );
        float f1 = iblockaccess.getLightBrightness( i, j + 1, k );
        
        return f <= f1 ? f1 : f;
    }
    
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		// the souls may not be so happy about being encased in cement

		if ( !IsCementPartiallyDry( world, i, j, k ) )
		{				
		    if ( random.nextInt( 250 ) == 0 )
		    {
		    	world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.ghast.moan", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		    }
		}
	}    
    
    public boolean RenderCement
    ( 
		RenderBlocks renderblocks, 
		IBlockAccess iblockaccess, 
		int i, int j, int k, 
		Block block 
	)
    {
    	// FCTODO: Access renderblocks.renderAllFaces through ModLoader (private value) and set it here
    	boolean bRenderAllFaces = false;
    	
    	// modified fluid renderer to avoid texture reorientation (that resulted in having to modify
    	// terrain.png)
    	
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 1F, 1F, 1F );
        
        boolean bTopFaceFlag = block.shouldSideBeRendered(iblockaccess, i, j + 1, k, 1);
        boolean bBottomFaceFlag = block.shouldSideBeRendered(iblockaccess, i, j - 1, k, 0);
        boolean bSideFaceFlags[] = new boolean[4];
        
        bSideFaceFlags[0] = block.shouldSideBeRendered(iblockaccess, i, j, k - 1, 2);
        bSideFaceFlags[1] = block.shouldSideBeRendered(iblockaccess, i, j, k + 1, 3);
        bSideFaceFlags[2] = block.shouldSideBeRendered(iblockaccess, i - 1, j, k, 4);
        bSideFaceFlags[3] = block.shouldSideBeRendered(iblockaccess, i + 1, j, k, 5);
        
        if(!bTopFaceFlag && !bBottomFaceFlag && !bSideFaceFlags[0] && 
    		!bSideFaceFlags[1] && !bSideFaceFlags[2] && !bSideFaceFlags[3])
        {
            return false;
        }
        
        boolean hasRendered = false;
        
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        
        float cornerHeight1 = RenderCementGetCornerHeightFromNeighbours( iblockaccess, i, j, k );
        float cornerHeight2 = RenderCementGetCornerHeightFromNeighbours( iblockaccess, i, j, k + 1 );
        float cornerHeight3 = RenderCementGetCornerHeightFromNeighbours( iblockaccess, i + 1, j, k + 1 );
        float cornerHeight4 = RenderCementGetCornerHeightFromNeighbours( iblockaccess, i + 1, j, k );
        
        if ( bRenderAllFaces || bTopFaceFlag )
        {
        	hasRendered = true;
            Icon i1 = block.getBlockTexture( iblockaccess, i, j, k, 1 );
            
            double x1 = i1.getMinU();
            double x2 = i1.getMaxU();
            double y1 = i1.getMinV();
            double y2 = i1.getMaxV();
            
            tessellator.setBrightness( block.getMixedBrightnessForBlock( iblockaccess, i, j + 1, k ) );            
            
            tessellator.addVertexWithUV( i + 0, (float)j + cornerHeight1, k + 0, x1, y1 );
            tessellator.addVertexWithUV( i + 0, (float)j + cornerHeight2, k + 1, x1, y2 );
            tessellator.addVertexWithUV( i + 1, (float)j + cornerHeight3, k + 1, x2, y2 );
            tessellator.addVertexWithUV( i + 1, (float)j + cornerHeight4, k + 0, x2, y1 );
        }
        
        if ( bRenderAllFaces || bBottomFaceFlag )
        {
            tessellator.setBrightness( block.getMixedBrightnessForBlock( iblockaccess, i, j - 1, k ) );
            
            renderblocks.renderFaceYNeg( block, i, j, k, block.getBlockTexture( iblockaccess, i, j, k, 0 ) );
            
            hasRendered = true;
        }
        
        for ( int iSide = 0; iSide < 4; iSide++ )
        {
            int k1 = i;
            int i2 = j;
            int k2 = k;
            
            if ( iSide == 0 )
            {
                k2--;
            }
            else if ( iSide == 1 )
            {
                k2++;
            }
            else if ( iSide == 2)
            {
                k1--;
            }
            else if ( iSide == 3)
            {
                k1++;
            }
            
            if ( bRenderAllFaces || bSideFaceFlags[iSide] )
            {
                float f10;
                float f12;
                float f14;
                float f16;
                float f17;
                float f18;
                
                if ( iSide == 0 )
                {
                    f10 = cornerHeight1;
                    f12 = cornerHeight4;
                    f14 = i;
                    f17 = i + 1;
                    f16 = k;
                    f18 = k;
                } 
                else if ( iSide == 1 )
                {
                    f10 = cornerHeight3;
                    f12 = cornerHeight2;
                    f14 = i + 1;
                    f17 = i;
                    f16 = k + 1;
                    f18 = k + 1;
                } 
                else if ( iSide == 2 )
                {
                    f10 = cornerHeight2;
                    f12 = cornerHeight1;
                    f14 = i;
                    f17 = i;
                    f16 = k + 1;
                    f18 = k;
                } 
                else
                {
                    f10 = cornerHeight4;
                    f12 = cornerHeight3;
                    f14 = i + 1;
                    f17 = i + 1;
                    f16 = k;
                    f18 = k + 1;
                }
                
                hasRendered = true;
                
                Icon l2 = block.getBlockTexture( iblockaccess, i, j, k, iSide + 2 );
                
                double d4 = l2.getMinU();
                double d5 = l2.getMaxU();
                
                double d6 = l2.getInterpolatedV( ( 1.0D - f10 ) * 16D );
                double d7 = l2.getInterpolatedV( (1.0F - f12 ) * 16D );
                
                double d8 = l2.getMaxV();
                
                tessellator.setBrightness( block.getMixedBrightnessForBlock( iblockaccess, k1, i2, k2 ) );            
                
                tessellator.addVertexWithUV( f14, (float)j + f10, f16, d4, d6 );
                tessellator.addVertexWithUV( f17, (float)j + f12, f18, d5, d7 );
                tessellator.addVertexWithUV( f17, j + 0, f18, d5, d8 );
                tessellator.addVertexWithUV( f14, j + 0, f16, d4, d8 );
            }
        }

        return hasRendered;
    }
    
    public float RenderCementGetCornerHeightFromNeighbours( IBlockAccess iblockaccess, int i, int j, int k )	
    {
        int numFactorsCount = 0;
        
        float f = 0.0F;
        
        for( int tempSide = 0; tempSide < 4; tempSide++ )
        {
            int tempi = i - (tempSide & 1);
            int tempj = j;
            int tempk = k - (tempSide >> 1 & 1);

            
            if( iblockaccess.getBlockMaterial( tempi, tempj + 1, tempk ) == 
            	FCBetterThanWolves.fcMaterialCement )
            {
            	// if the block above is also cement, then this is a full-height block
            	
                return 1.0F;
            }
            
            Material material1 = iblockaccess.getBlockMaterial( tempi, tempj, tempk );
            
            if ( material1 == FCBetterThanWolves.fcMaterialCement )
            {
                if( iblockaccess.isBlockOpaqueCube( tempi, tempj + 1, tempk) ) 

                {
                	// if the block is cement, and it has a solid above it, it's full height
                	
                    return 1.0F;
                } 
                
                if( IsCementSourceBlock( iblockaccess, tempi, tempj, tempk ) )
                {
                	// source blocks have a very heavy weight on the overall height of neighboring blocks
                	
                    f += GetRenderHeight( iblockaccess, tempi, tempj, tempk ) * 10F;
                    
                    numFactorsCount += 10;
                }
                
                f += GetRenderHeight( iblockaccess, tempi, tempj, tempk );
                
                numFactorsCount++;
            } 
            else if ( !material1.isSolid() )
            {
            	// this controls how much "weight" empty blocks have in affecting the cement's height

        		f += 0.60F;
            	
                numFactorsCount++;
            }
        }

        if ( numFactorsCount > 0 )
        {
        	return 1.0F - f / (float)numFactorsCount;
        }
        else
        {
        	return 1.0F;
        }
    }
}