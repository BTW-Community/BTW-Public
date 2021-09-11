// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBiomeGenHills extends BiomeGenBase
{
	private static final int m_iNumSilverfishClustersPerChunk = 7;
	private static final int m_iNumSilverfishBlocksPerCluster = 8;
	
    protected WorldGenerator m_generatorSilverfish;

    protected FCBiomeGenHills( int iBiomeID )
    {
        super( iBiomeID );
        
        m_generatorSilverfish = new WorldGenMinable( Block.silverfish.blockID, 
        	m_iNumSilverfishBlocksPerCluster );
    }

    public void decorate( World world, Random rand, int iChunkX, int iChunkZ )
    {
        super.decorate( world, rand, iChunkX, iChunkZ );
        
        AddEmeralds( world, rand, iChunkX, iChunkZ );

        AddSilverfishBlocks( world, rand, iChunkX, iChunkZ );        
    }
    
    public void AddEmeralds( World world, Random rand, int iChunkX, int iChunkZ )
    {
        int iNumEmeralds = 3 + rand.nextInt( 6 );

        for ( int iTempCount = 0; iTempCount < iNumEmeralds; iTempCount++ )
        {
            int iTempI = iChunkX + rand.nextInt( 16 );
            int iTempJ = rand.nextInt( 28 ) + 4;
            int iTempK = iChunkZ + rand.nextInt( 16 );
            
            if ( world.getBlockId( iTempI, iTempJ, iTempK ) == Block.stone.blockID )
            {
            	int iMetadata = 0;
            	
        		if ( iTempJ <= 48 + world.rand.nextInt( 2 ) )
        		{
        			int iStrataLevel = 1;
        			
        			if ( iTempJ <= 24 + world.rand.nextInt( 2 ) )
        			{
        				iStrataLevel = 2;                                				
        			}
        			
    				iMetadata = Block.oreEmerald.GetMetadataConversionForStrataLevel( 
    					iStrataLevel, 0 );
        		}
        		
                world.setBlock( iTempI, iTempJ, iTempK, Block.oreEmerald.blockID, iMetadata, 2 );
            }
        }
    }
    
    public void AddSilverfishBlocks( World world, Random rand, int iChunkX, int iChunkZ )
    {
        for ( int iTempCount = 0; iTempCount < m_iNumSilverfishClustersPerChunk; iTempCount++ )
        {
            int iTempI = iChunkX + rand.nextInt( 16 );
            int iTempJ = rand.nextInt( 64 );
            int iTempK = iChunkZ + rand.nextInt( 16 );
            
            m_generatorSilverfish.generate( world, rand, iTempI, iTempJ, iTempK );
        }
    }
}