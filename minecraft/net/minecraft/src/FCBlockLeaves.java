// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockLeaves extends BlockLeaves
{
	protected static final int m_iAdjacentTreeBlockArrayWidth = 32;
	protected static final int iArrayWidthHalf = m_iAdjacentTreeBlockArrayWidth / 2;
    protected static final int m_iAdjacentTreeBlockSearchDist = 4;
    protected static final int m_iAdjacentTreeBlockChunkCheckDist = m_iAdjacentTreeBlockSearchDist + 1;    
	
    protected int[][][] m_iAdjacentTreeBlocks = new int[m_iAdjacentTreeBlockArrayWidth][m_iAdjacentTreeBlockArrayWidth][m_iAdjacentTreeBlockArrayWidth]; 
	
    protected FCBlockLeaves( int iBlockID )
    {
        super( iBlockID );    
        
        setHardness( 0.2F );        
        SetAxesEffectiveOn( true );
        
        SetBuoyant();
        
        setLightOpacity( 1 );
        
		SetFireProperties( FCEnumFlammability.LEAVES );
		
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "leaves" );
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 0.5F;
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
    	// override of parent function to get rid of apple drop on oak
    	
        if ( !world.isRemote )
        {
            int iType = iMetadata & 3;
            
            int iChanceOfSaplingDrop = 20;

            if ( iType == 3 ) // jungle
            {
                iChanceOfSaplingDrop = 40;
            }

            if ( world.rand.nextInt( iChanceOfSaplingDrop ) == 0 )
            {
                int iIdDropped = idDropped( iMetadata, world.rand, iFortuneModifier );
                
                dropBlockAsItem_do( world, i, j, k, new ItemStack( iIdDropped, 1, damageDropped( iMetadata ) ) );
            }            
        }
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        if ( !world.isRemote )
        {
            int iMetadata = world.getBlockMetadata( i, j, k );

            // 8 bit on metadata indicates that the block has been flagged by a neighbor change (in breakBlock() in BlockLeaves or BlockLog) 
            // 4 bit indicates that it was placed by a player and should never decay
            if ( ( iMetadata & 8 ) != 0 && ( iMetadata & 4 ) == 0 )
            {
                if (world.checkChunksExist(i - m_iAdjacentTreeBlockChunkCheckDist, j - m_iAdjacentTreeBlockChunkCheckDist, k - m_iAdjacentTreeBlockChunkCheckDist, i + m_iAdjacentTreeBlockChunkCheckDist, j + m_iAdjacentTreeBlockChunkCheckDist, k + m_iAdjacentTreeBlockChunkCheckDist))
                {
                	UpdateAdjacentTreeBlockArray( world, i, j, k );
                	
                    int var12 = m_iAdjacentTreeBlocks[iArrayWidthHalf][iArrayWidthHalf][iArrayWidthHalf];

                    if (var12 >= 0)
                    {
                    	int iNewMetadata = iMetadata & 7;
                    	
                        world.SetBlockMetadataWithNotify( i, j, k, iNewMetadata, 4 );
                    }
                    else
                    {
                        this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
                        world.setBlockToAir(i, j, k);
                    }
                }
            }
        }
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return bIgnoreTransparency;
	}
	
	@Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
		super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
		
		GenerateAshOnBurn( world, i, j, k );		
    }
	
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
    	return createStackedBlock( world.getBlockMetadata( i, j, k ) );
    }
    
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
        return world.provider.dimensionId != -1;
    }
    
    //------------- Class Specific Methods ------------//
	
	protected void GenerateAshOnBurn( World world, int i, int j, int k )
	{
		for ( int iTempJ = j; iTempJ > 0; iTempJ-- )
		{
			if ( FCBlockAshGroundCover.CanAshReplaceBlock( world, i, iTempJ, k ) )
			{
		    	int iBlockBelowID = world.getBlockId( i, iTempJ - 1, k );
		    	Block blockBelow = Block.blocksList[iBlockBelowID];
		    	
		    	if ( blockBelow != null && blockBelow.CanGroundCoverRestOnBlock( world, i, iTempJ - 1, k ) )
		    	{
		    		world.setBlockWithNotify( i, iTempJ, k, FCBetterThanWolves.fcBlockAshGroundCover.blockID );
		    		
		    		break;
		    	}
			}
			else if ( world.getBlockId( i, iTempJ, k ) != Block.fire.blockID )
			{
				break;
			}
		}
	}
    
    protected void UpdateAdjacentTreeBlockArray( World world, int i, int j, int k )
    {
        for ( int iTempIOffset = -m_iAdjacentTreeBlockSearchDist; iTempIOffset <= m_iAdjacentTreeBlockSearchDist; ++iTempIOffset )
        {
            for ( int iTempJOffset = -m_iAdjacentTreeBlockSearchDist; iTempJOffset <= m_iAdjacentTreeBlockSearchDist; ++iTempJOffset )
            {
                for ( int iTempKOffset = -m_iAdjacentTreeBlockSearchDist; iTempKOffset <= m_iAdjacentTreeBlockSearchDist; ++iTempKOffset )
                {
                    int iTempBlockID = world.getBlockId( i + iTempIOffset, j + iTempJOffset, k + iTempKOffset );

                    if ( iTempBlockID == Block.wood.blockID && !((FCBlockLog)(Block.wood)).IsDeadStump( world, i + iTempIOffset, j + iTempJOffset, k + iTempKOffset ) )
                    {
                    	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] = 0;
                    }
                    else if (iTempBlockID == Block.leaves.blockID)
                    {
                    	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] = -2;
                    }
                    else
                    {
                    	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] = -1;
                    }
                }
            }
        }

        for ( int iTempValue = 1; iTempValue <= 4; ++iTempValue)
        {
            for ( int iTempIOffset = -m_iAdjacentTreeBlockSearchDist; iTempIOffset <= m_iAdjacentTreeBlockSearchDist; ++iTempIOffset)
            {
                for ( int iTempJOffset = -m_iAdjacentTreeBlockSearchDist; iTempJOffset <= m_iAdjacentTreeBlockSearchDist; ++iTempJOffset)
                {
                    for ( int iTempKOffset = -m_iAdjacentTreeBlockSearchDist; iTempKOffset <= m_iAdjacentTreeBlockSearchDist; ++iTempKOffset)
                    {
                        if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] == iTempValue - 1 )
                        {
                            if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf - 1][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] == -2 )
                            {
                            	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf - 1][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] = iTempValue;
                            }

                            if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf + 1][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] == -2 )
                            {
                            	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf + 1][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf] = iTempValue;
                            }
                            
                            if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf - 1][iTempKOffset + iArrayWidthHalf] == -2 )
                            {
                            	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf - 1][iTempKOffset + iArrayWidthHalf] = iTempValue;
                            }

                            if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf + 1][iTempKOffset + iArrayWidthHalf] == -2 )
                            {
                            	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf + 1][iTempKOffset + iArrayWidthHalf] = iTempValue;
                            }

                            if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf - 1] == -2 )
                            {
                            	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf - 1] = iTempValue;
                            }

                            if ( m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf + 1] == -2 )
                            {
                            	m_iAdjacentTreeBlocks[iTempIOffset + iArrayWidthHalf][iTempJOffset + iArrayWidthHalf][iTempKOffset + iArrayWidthHalf + 1] = iTempValue;
                            }
                        }
                    }
                }
            }
        }
    }
    
	//----------- Client Side Functionality -----------//

    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        if ( world.IsRainingAtPos( i, j + 1, k ) && 
        	!world.doesBlockHaveSolidTopSurface( i, j - 1, k ) && 
        	rand.nextInt(15) == 1 )
        {
            world.spawnParticle( "dripWater",  
            	i + rand.nextDouble(), j - 0.05D, k + rand.nextDouble(), 
            	0D, 0D, 0D );
        }
    }
}