// FCMOD: More generalized replacement for PlayerManager that keeps track of all 
// chunks around any loaders (players, wither, etc.) to decide which should be 
// loaded and communicated to players

package net.minecraft.src;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class FCChunkTracker
{
    public final WorldServer m_worldServer;

    private final LinkedList<EntityPlayerMP> m_playersTracked = new LinkedList<EntityPlayerMP>();

    public final LongHashMap m_trackerEntriesMap = new LongHashMap();

    private final LinkedList<FCChunkTrackerEntry> m_entriesRequiringClientUpdate = 
    	new LinkedList<FCChunkTrackerEntry>();

    private final int m_iChunkViewDistance;

    /** x, z direction vectors: east, south, west, north */
    private final int[][] m_xzOffsets = new int[][] {
    	{ 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

    public FCChunkTracker( WorldServer world, int iChunkViewDistance )
    {
        if ( iChunkViewDistance > 15 )
        {
            throw new IllegalArgumentException( "Too big view radius!" );
        }
        else if ( iChunkViewDistance < 3 )
        {
            throw new IllegalArgumentException( "Too small view radius!" );
        }
        else
        {
            m_iChunkViewDistance = iChunkViewDistance;
            m_worldServer = world;
        }
    }

    public void Update()
    {
    	if ( !m_entriesRequiringClientUpdate.isEmpty() )
    	{
	    	Iterator<FCChunkTrackerEntry> updateIterator = 
	    		m_entriesRequiringClientUpdate.iterator();
	    	
	        while ( updateIterator.hasNext() )
	        {
	        	FCChunkTrackerEntry tempEntry = updateIterator.next();
	        	
	            tempEntry.SendUpdatesToWatchingPlayers();
	        }
	
	        m_entriesRequiringClientUpdate.clear();
        }

    	// FCTODO: Broaden this to only unload worlds once all tracked chunks are gone
    	// not just players
    	//if ( m_trackerEntriesMap.getNumHashElements() <= 0 )
        if ( m_playersTracked.isEmpty() )
        {
            WorldProvider provider = m_worldServer.provider;

            if ( !provider.canRespawnHere() )
            {
                this.m_worldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }

    public void FlagBlockForClientUpdate( int i, int j, int k )
    {
        int iChunkX = i >> 4;
        int iChunkZ = k >> 4;
        
        FCChunkTrackerEntry entry = GetTrackerEntry( iChunkX, iChunkZ );

        if ( entry != null )
        {
        	if ( !entry.RequiresClientUpdate() )
        	{
        		m_entriesRequiringClientUpdate.add( entry );
        	}
        		
    		entry.FlagBlockForUpdate( i & 15, j, k & 15 );
        }
    }

    public void AddPlayer( EntityPlayerMP player )
    {
        int iPlayerChunkX = MathHelper.floor_double( player.posX / 16D );
        int iPlayerChunkZ = MathHelper.floor_double( player.posZ / 16D );
        
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;

        for ( int iTempChunkX = iPlayerChunkX - m_iChunkViewDistance; 
        	iTempChunkX <= iPlayerChunkX + m_iChunkViewDistance; iTempChunkX++ )
        {
            for ( int iTempChunkZ = iPlayerChunkZ - m_iChunkViewDistance; 
            	iTempChunkZ <= iPlayerChunkZ + m_iChunkViewDistance; iTempChunkZ++ )
            {
                GetOrCreateTrackerEntry( iTempChunkX, iTempChunkZ ).AddPlayerWatching( player );
            }
        }

        m_playersTracked.add( player );
        FilterChunksToBeSentToClient( player );
    }

    public void UpdateMovingPlayer( EntityPlayerMP player )
    {
        double dDeltaManagedX = player.posX - player.managedPosX;
        double dDeltaManagedZ = player.posZ - player.managedPosZ;
        
        double dDistManagedSq = dDeltaManagedX * dDeltaManagedX + dDeltaManagedZ * dDeltaManagedZ;

    	// prevents jittery loading and unloading of chunks by requiring player to move
    	// a minimum distance before fully updating
    	
        if ( dDistManagedSq >= ( 8D * 8D ) )
    	{
	        int iPlayerChunkX = MathHelper.floor_double( player.posX / 16D );
	        int iPlayerChunkZ = MathHelper.floor_double( player.posZ / 16D );
	        
	        int iManagedChunkX = MathHelper.floor_double( player.managedPosX / 16D );
	        int iManagedChunkZ = MathHelper.floor_double( player.managedPosZ / 16D );
	            
	        if ( iManagedChunkX != iPlayerChunkX || iManagedChunkZ != iPlayerChunkZ )
	        {
	        	// cycle through all chunks around player's new location
	        	
	            for ( int iTempChunkX = iPlayerChunkX - m_iChunkViewDistance; 
	            	iTempChunkX <= iPlayerChunkX + m_iChunkViewDistance; iTempChunkX++ )
	            {
	                for ( int iTempChunkZ = iPlayerChunkZ - m_iChunkViewDistance; 
	                	iTempChunkZ <= iPlayerChunkZ + m_iChunkViewDistance; iTempChunkZ++ )
	                {
	                    if ( !AreWithinAxisDistance( iTempChunkX, iTempChunkZ, 
	                    	iManagedChunkX, iManagedChunkZ, m_iChunkViewDistance ) )
	                    {
	                    	// chunk isn't within previous managed zone
	                    	
	                    	FCChunkTrackerEntry tempEntry = GetOrCreateTrackerEntry( 
	                    		iTempChunkX, iTempChunkZ );
	                    	
	                    	tempEntry.AddPlayerWatching( player );
	                    }	                    
	                }
	            }
	            
	        	// cycle through all chunks around player's old managed location
	        	
	            for ( int iTempChunkX = iManagedChunkX - m_iChunkViewDistance; 
            		iTempChunkX <= iManagedChunkX + m_iChunkViewDistance; iTempChunkX++ )
	            {
	                for ( int iTempChunkZ = iManagedChunkZ - m_iChunkViewDistance; 
	                	iTempChunkZ <= iManagedChunkZ + m_iChunkViewDistance; iTempChunkZ++ )
                	{
	                    if ( !AreWithinAxisDistance( iTempChunkX, iTempChunkZ, 
	                    	iPlayerChunkX, iPlayerChunkZ, m_iChunkViewDistance ) )
	                    {
	                    	// chunk isn't within the player's new zone
	                    	
	                        FCChunkTrackerEntry tempEntry = GetTrackerEntry( 
	                        	iTempChunkX, iTempChunkZ );
	
	                        if ( tempEntry != null)
	                        {
	                        	tempEntry.RemovePlayerWatching( player );
	                        }
	                    }
                	}
	            }
	
	            FilterChunksToBeSentToClient(player);
	            
	            player.managedPosX = player.posX;
	            player.managedPosZ = player.posZ;
	        }
    	}
    }

    public void RemoveChunkFromTracker( FCChunkTrackerEntry entry )
    {
    	ChunkCoordIntPair coord = entry.m_coord;
    	
        long lKey = ComputeTrackerEntryKey( coord.chunkXPos, coord.chunkZPos );
        
        m_trackerEntriesMap.remove( lKey );
        
        if ( entry.RequiresClientUpdate() )
        {
        	m_entriesRequiringClientUpdate.remove( this );
        }

        m_worldServer.theChunkProviderServer.unloadChunksIfNotNearSpawn(
        	coord.chunkXPos, coord.chunkZPos );    	
    }

    // FCTODO: Old method, get rid of this once sure
    /*
    public void FilterChunksToBeSentToClient( EntityPlayerMP player )
    {
        LinkedList<ChunkCoordIntPair> oldChunksList = player.m_chunksToBeSentToClient;
        
        player.m_chunksToBeSentToClient = new LinkedList<ChunkCoordIntPair>();

        int iPlayerChunkX = MathHelper.floor_double( player.posX / 16D );
        int iPlayerChunkZ = MathHelper.floor_double( player.posZ / 16D );
        
        FCChunkTrackerEntry playerChunkEntry = GetOrCreateTrackerEntry( iPlayerChunkX, 
        	iPlayerChunkZ );        
        
        if ( oldChunksList.contains( playerChunkEntry.getChunkLocation() ) )
        {
            player.m_chunksToBeSentToClient.add( playerChunkEntry.getChunkLocation() );
        }

        int iRunningDir = 0;
        
        int iRunningXOffset = 0;
        int iRunningZOffset = 0;
        
        for ( int iTempDist = 1; iTempDist <= m_iChunkViewDistance * 2; iTempDist++ )
        {
            for ( int var11 = 0; var11 < 2; var11++ )
            {
                int[] iTempOffset = m_xzOffsets[iRunningDir % 4];
                
                iRunningDir++;

                for ( int var13 = 0; var13 < iTempDist; ++var13 )
                {
                    iRunningXOffset += iTempOffset[0];
                    iRunningZOffset += iTempOffset[1];
                    
                    FCChunkTrackerEntry tempEntry =
                    	GetOrCreateTrackerEntry( iPlayerChunkX + iRunningXOffset, 
                		iPlayerChunkZ + iRunningZOffset );

                    if ( oldChunksList.contains( tempEntry.getChunkLocation() ) )
                    {
                        player.m_chunksToBeSentToClient.add( tempEntry.getChunkLocation() );
                    }
                }
            }
        }

        iRunningDir %= 4;

        for ( int iTempDist = 0; iTempDist < m_iChunkViewDistance * 2; iTempDist++ )
        {
            iRunningXOffset += this.m_xzOffsets[iRunningDir][0];
            iRunningZOffset += this.m_xzOffsets[iRunningDir][1];
            
            FCChunkTrackerEntry tempEntry = 
            	GetOrCreateTrackerEntry( iPlayerChunkX + iRunningXOffset, iPlayerChunkZ + iRunningZOffset );

            if ( oldChunksList.contains( tempEntry.getChunkLocation() ) )
            {
                player.m_chunksToBeSentToClient.add( tempEntry.getChunkLocation() );
            }
        }
    }
    */
    // END FCTODO

    public void FilterChunksToBeSentToClient( EntityPlayerMP player )
    {
        LinkedList<ChunkCoordIntPair> oldChunksList = player.m_chunksToBeSentToClient;
        
        player.m_chunksToBeSentToClient = new LinkedList<ChunkCoordIntPair>();

        int iPlayerChunkX = MathHelper.floor_double( player.posX / 16D );
        int iPlayerChunkZ = MathHelper.floor_double( player.posZ / 16D );
        
        FCChunkTrackerEntry playerChunkEntry = GetOrCreateTrackerEntry( iPlayerChunkX, 
        	iPlayerChunkZ );        
        
        if ( oldChunksList.contains( playerChunkEntry.getChunkLocation() ) )
        {
            player.m_chunksToBeSentToClient.add( playerChunkEntry.getChunkLocation() );
        }

        // goes through all the chunks in a spiral outwards from the player 
        // check if the chunk is waiting to be sent to the client, and ordering
        // the chunks that are so that closest are sent first.
        
        for ( int iTempDist = 1; iTempDist <= m_iChunkViewDistance; iTempDist++ )
        {
            int iRunningChunkX = iPlayerChunkX - iTempDist;
            int iRunningChunkZ = iPlayerChunkZ - iTempDist;
            
            for ( int iRunningSide = 0; iRunningSide < 4; iRunningSide++ )
            {
            	int iSideWidth = ( iTempDist * 2 ) + 1;
            	
            	int iSideOffsetX = m_xzOffsets[iRunningSide][0];
            	int iSideOffsetZ = m_xzOffsets[iRunningSide][1];
            	
            	for ( int iTempCount = 0; iTempCount < iSideWidth - 1; iTempCount++ )
            	{
                    FCChunkTrackerEntry tempEntry = GetOrCreateTrackerEntry( 
                    	iRunningChunkX, iRunningChunkZ );

                    if ( oldChunksList.contains( tempEntry.getChunkLocation() ) )
                    {
                        player.m_chunksToBeSentToClient.add( tempEntry.getChunkLocation() );
                    }
                    
                    iRunningChunkX += iSideOffsetX;
                    iRunningChunkZ += iSideOffsetZ;
            	}
            }            
        }
    }
    
    /**
     * Removes an EntityPlayerMP from the PlayerManager.
     */
    public void RemovePlayer( EntityPlayerMP player )
    {
        int iPlayerChunkX = MathHelper.floor_double( player.managedPosX / 16D );
        int iPlayerChunkZ = MathHelper.floor_double( player.managedPosZ / 16D );

        for ( int iTempChunkX = iPlayerChunkX - m_iChunkViewDistance; 
        	iTempChunkX <= iPlayerChunkX + m_iChunkViewDistance; iTempChunkX++ )
        {
            for ( int iTempChunkZ = iPlayerChunkZ - m_iChunkViewDistance; 
            	iTempChunkZ <= iPlayerChunkZ + m_iChunkViewDistance; iTempChunkZ++ )
            {
                FCChunkTrackerEntry tempEntry = GetTrackerEntry( iTempChunkX, iTempChunkZ );

                if ( tempEntry != null )
                {
                    tempEntry.RemovePlayerWatching( player );
                }
            }
        }

        m_playersTracked.remove( player );
    }

    public boolean IsChunkWatchedByPlayerAndSentToClient( EntityPlayerMP player, 
    	int iChunkX, int iChunkZ )
    {
    	FCChunkTrackerEntry entry = GetTrackerEntry( iChunkX, iChunkZ );
    	
    	if ( entry != null )
    	{
    		if ( entry.getPlayersInChunk().contains( player ) && 
    			!player.m_chunksToBeSentToClient.contains( entry.getChunkLocation() ) )
			{
    			return true;
			}
    	}
    	
    	return false;
    }

    /**
     * Tests each axis to see if the two points are within specified distance on each axis.
     */
    private boolean AreWithinAxisDistance( int iX1, int iZ1, int iX2, int iZ2, int iAxisDist )
    {
        int iDeltaX = iX1 - iX2;
        int iDeltaZ = iZ1 - iZ2;
        
        if ( iDeltaX >= -iAxisDist && iDeltaX <= iAxisDist )
        {
        	return iDeltaZ >= -iAxisDist && iDeltaZ <= iAxisDist;
        }
        
        return false;
    }

    public static int GetFurthestViewableBlock( int iChunkViewDistance )
    {
        return iChunkViewDistance * 16 - 16;
    }
    
    public boolean IsChunkBeingWatched( int iChunkX, int iChunkZ  )
    {
    	return GetTrackerEntry( iChunkX, iChunkZ ) != null;
    }

    private long ComputeTrackerEntryKey( int iChunkX, int iChunkZ )
    {
        return (long)iChunkX + 2147483647L | (long)iChunkZ + 2147483647L << 32;        
    }
    
    private FCChunkTrackerEntry GetTrackerEntry( int iChunkX, int iChunkZ )
    {
        long lKey = ComputeTrackerEntryKey( iChunkX, iChunkZ );
        
        return (FCChunkTrackerEntry)m_trackerEntriesMap.getValueByKey( lKey );
    }
    
    private FCChunkTrackerEntry GetOrCreateTrackerEntry( int iChunkX, int iChunkZ )
    {
        FCChunkTrackerEntry entry = GetTrackerEntry( iChunkX, iChunkZ );

        if ( entry == null )
        {
            entry = new FCChunkTrackerEntry( this, iChunkX, iChunkZ );
            
            m_trackerEntriesMap.add( ComputeTrackerEntryKey( iChunkX, iChunkZ ), entry );
        }

        return entry;
    }
}
