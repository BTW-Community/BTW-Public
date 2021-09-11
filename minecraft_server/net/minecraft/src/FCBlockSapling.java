// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSapling extends BlockSapling
{
    private static final double m_dWidth = 0.8D;
    private static final double m_dHalfWidth = ( m_dWidth / 2D );
    
    protected FCBlockSapling( int iBlockID )
    {
    	super( iBlockID );
    	
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.KINDLING );
		SetFilterableProperties( Item.m_iFilterable_NoProperties );
    	
        InitBlockBounds( 0.5D - m_dHalfWidth, 0D, 0.5D - m_dHalfWidth, 
        	0.5D + m_dHalfWidth, m_dHalfWidth * 2D, 0.5D + m_dHalfWidth);
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
        checkFlowerChange(world, i, j, k); // replaces call to the super method two levels up in the hierarchy
        
        if ( world.provider.dimensionId != 1 && world.getBlockId( i, j, k ) == blockID ) // necessary because checkFlowerChange() may destroy the sapling
        {
            if ( world.getBlockLightValue( i, j + 1, k ) >= 9 && random.nextInt( 64 ) == 0 )
            {
                int iMetadata = world.getBlockMetadata( i, j, k );
                int iGrowthStage = ( iMetadata & (~3) ) >> 2;

                if ( iGrowthStage < 3 )
                {
                	iGrowthStage++;
                	iMetadata = ( iMetadata & 3 ) | ( iGrowthStage << 2 );
                	
                    world.setBlockMetadataWithNotify( i, j, k, iMetadata );
                }
                else
                {
                    growTree( world, i, j, k, random );
                }
            }
        }
    }
    
    @Override
    public void growTree( World world, int i, int j, int k, Random random )
    {
        int iTreeType = world.getBlockMetadata(i, j, k) & 3;
        boolean bSuccess = false;
        
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	
    	if ( iBlockBelowID == FCBetterThanWolves.fcBlockAestheticOpaqueEarth.blockID )
    	{
    		int iBlockBelowMetadata = world.getBlockMetadata( i, j - 1, k );
    		
    		if ( ((FCBlockAestheticOpaqueEarth)FCBetterThanWolves.fcBlockAestheticOpaqueEarth).IsBlightFromMetadata( iBlockBelowMetadata ) )
    		{
    			// FCTODO
    			//bSuccess = GrowBlightTree();
    			
    			//return;
    		}
    	}
    	
        int iIOffset = 0;
        int iKOffset = 0;
        
        boolean bGeneratedHuge = false;

        if ( iTreeType != 3 )
        {
            world.setBlock( i, j, k, 0 );            
        }
        
        if ( iTreeType == 1 )
        {
        	bSuccess = FCUtilsTrees.GenerateTaiga2( world, random, i, j, k );
        } 
		else if ( iTreeType == 2 )
        {
        	bSuccess = FCUtilsTrees.GenerateForest( world, random, i, j, k );
        } 
        else if ( iTreeType == 3 )
        {
            do
            {
                if ( iIOffset < -1 )
                {
                    break;
                }

                iKOffset = 0;

                do
                {
                    if ( iKOffset < -1 )
                    {
                        break;
                    }

                    if ( isSameSapling(world, i + iIOffset, j, k + iKOffset, 3) && 
                		isSameSapling(world, i + iIOffset + 1, j, k + iKOffset, 3) && 
                		isSameSapling(world, i + iIOffset, j, k + iKOffset + 1, 3) && 
                		isSameSapling(world, i + iIOffset + 1, j, k + iKOffset + 1, 3))
                    {
                        if ( GetSaplingGrowthStage(world, i + iIOffset, j, k + iKOffset) == 3 && 
                        	GetSaplingGrowthStage(world, i + iIOffset + 1, j, k + iKOffset ) == 3 && 
                        	GetSaplingGrowthStage(world, i + iIOffset, j, k + iKOffset + 1 ) == 3 && 
                        	GetSaplingGrowthStage(world, i + iIOffset + 1, j, k + iKOffset + 1 ) == 3 )
                        {
	                    	// clear all 4 saplings that make up the huge tree
	                    	
	                        world.setBlock( i + iIOffset, j, k + iKOffset, 0);
	                        world.setBlock( i + iIOffset + 1, j, k + iKOffset, 0);
	                        world.setBlock( i + iIOffset, j, k + iKOffset + 1, 0);
	                        world.setBlock( i + iIOffset + 1, j, k + iKOffset + 1, 0);
	                        
	                        FCUtilsGenHugeTree hugeTree = new FCUtilsGenHugeTree( true, 10 + random.nextInt(20), 3, 3 );
	                    	bSuccess = hugeTree.generate( world, random, i + iIOffset, j, k + iKOffset );
	                    	
	                        bGeneratedHuge = true;
	                        break;
                        }
                        else
                        {
                        	// we have 4 saplings, but they aren't all fully grown.  Bomb out entirely
                        	
                        	return;
                        }
                    }

                    iKOffset--;
                }
                while (true);

                if ( bGeneratedHuge )
                {
                    break;
                }

                iIOffset--;
            }
            while (true);

            if ( !bGeneratedHuge )
            {
            	iIOffset = iKOffset = 0;
                world.setBlock( i, j, k, 0 );
                
                bSuccess = FCUtilsTrees.GenerateTrees( world, random, i, j, k, 4 + random.nextInt(7), 3, 3, false);
            }
        }
		else	// metadata is 0
        {
            if ( random.nextInt(10) == 0 )
            {
            	FCUtilsGenBigTree bigTree = new FCUtilsGenBigTree( true );
            	
            	bSuccess = bigTree.generate( world, random, i, j, k );
            }
            else
            {
            	bSuccess = FCUtilsTrees.GenerateTrees( world, random, i, j, k );
            }
        }
        
        if ( !bSuccess )
        {
        	// restore saplings at full growth
        	
    		int iSaplingMetadata = iTreeType + ( 3 << 2 );
    		
        	if ( bGeneratedHuge )
        	{
	        	// replace all the saplings if a huge tree was grown
        		
                world.setBlockAndMetadata( i + iIOffset, j, k + iKOffset, blockID, iSaplingMetadata );
                world.setBlockAndMetadata(i + iIOffset + 1, j, k + iKOffset, blockID, iSaplingMetadata );
                world.setBlockAndMetadata(i + iIOffset, j, k + iKOffset + 1, blockID, iSaplingMetadata );
                world.setBlockAndMetadata(i + iIOffset + 1, j, k + iKOffset + 1, blockID, iSaplingMetadata );
        	}
        	else
        	{
	            world.setBlockAndMetadata( i, j, k, blockID, iSaplingMetadata );
        	}
        }
    }
    
	@Override
    public boolean OnBlockSawed( World world, int i, int j, int k )
    {
		return false;
    }
	
    @Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	Block blockOn = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	return blockOn != null && blockOn.CanSaplingsGrowOnBlock( world, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
	public int GetSaplingGrowthStage( World world, int i, int j, int k )
	{
		int iMetadata = world.getBlockMetadata( i, j, k );
		
        int iGrowthStage = ( iMetadata & (~3) ) >> 2;
    		
		return iGrowthStage;
	}
	
	//----------- Client Side Functionality -----------//
}
