// FCMOD: More generalized replacement for PlayerInstance

package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class FCChunkTrackerEntry
{
    private final FCChunkTracker m_chunkTracker;

    public final ChunkCoordIntPair m_coord;
    
    private final List<EntityPlayerMP> m_playersWatching = new ArrayList<EntityPlayerMP>();

    // Tracks blocks that need to be sent to players watching the chunk.
    // 16 bit encoded coordinates with top 4 bits x, mid 4 bits z, and bottom 8 bits y.       
    
    private short[] m_locationsRequiringClientUpdate = new short[64];
    private int m_iNumLocationsRequiringClientUpdate = 0; // a running count of active elements in above    
    
    // bitfield that flags each vertical chunk of 16 blocks that needs to be sent to players
    
    private int m_iVerticalChunksToUpdatePlayersBitfield;

    public FCChunkTrackerEntry( FCChunkTracker chunkTracker, int iChunkX, int iChunkZ )
    {
        m_chunkTracker = chunkTracker;
        
        m_coord = new ChunkCoordIntPair( iChunkX, iChunkZ );
        
        chunkTracker.m_worldServer.theChunkProviderServer.loadChunk( iChunkX, iChunkZ );
    }

    public void AddPlayerWatching( EntityPlayerMP player )
    {
        if ( m_playersWatching.contains( player ) )
        {
            throw new IllegalStateException( "Failed to add player. " + player + 
            	" already is in chunk " + m_coord.chunkXPos + ", " + m_coord.chunkZPos );
        }
        else
        {
            m_playersWatching.add( player );
            
            player.m_chunksToBeSentToClient.add( m_coord );
        }
    }
    
    public boolean RequiresClientUpdate()
    {
    	return m_iNumLocationsRequiringClientUpdate > 0;
    }

    public void RemovePlayerWatching( EntityPlayerMP player )
    {
        if ( m_playersWatching.contains( player ) )
        {
            player.playerNetServerHandler.sendPacket( new Packet51MapChunk( 
            	m_chunkTracker.m_worldServer.getChunkFromChunkCoords( 
        		m_coord.chunkXPos, m_coord.chunkZPos ), true, 0 ) );
            
            m_playersWatching.remove( player );            
            player.m_chunksToBeSentToClient.remove( m_coord );

        	// FCTODO: Broaden this to only unload chunks once all watchers are gone
            if ( m_playersWatching.isEmpty() )
            {
            	m_chunkTracker.RemoveChunkFromTracker( this );
            }
        }
    }

    private short GetBitEncodingForLocalPos( int iLocalX, int iLocalY, int iLocalZ )
    {
    	return (short)( iLocalX << 12 | iLocalZ << 8 | iLocalY );
    }
    
    public void FlagBlockForUpdate( int iLocalX, int iLocalY, int iLocalZ )
    {
        m_iVerticalChunksToUpdatePlayersBitfield |= 1 << ( iLocalY >> 4 );

        if ( m_iNumLocationsRequiringClientUpdate < 64 )
        {
            short sBitCode = GetBitEncodingForLocalPos( iLocalX, iLocalY, iLocalZ );

            for ( int iTempIndex = 0; iTempIndex < m_iNumLocationsRequiringClientUpdate; iTempIndex++ )
            {
                if ( m_locationsRequiringClientUpdate[iTempIndex] == sBitCode )
                {
                    return;
                }
            }

            m_locationsRequiringClientUpdate[m_iNumLocationsRequiringClientUpdate++] = sBitCode;
        }
    }

    private void SendToPlayersWatchingNotWaitingFullChunk( Packet packet )
    {
        for ( int iTempCount = 0; iTempCount < m_playersWatching.size(); iTempCount++ )
        {
            EntityPlayerMP tempPlayer = (EntityPlayerMP)m_playersWatching.get(iTempCount);

            // don't send if the whole chunk is already going to be sent to the player
            
            if ( !tempPlayer.m_chunksToBeSentToClient.contains( m_coord ) )
            {
                tempPlayer.playerNetServerHandler.sendPacket(packet);
            }
        }
    }

    public void SendUpdatesToWatchingPlayers()
    {
        if ( m_iNumLocationsRequiringClientUpdate != 0 )
        {
            int iOffsetX = m_coord.chunkXPos * 16;
            int iOffsetZ = m_coord.chunkZPos * 16;
            
            if ( m_iNumLocationsRequiringClientUpdate == 1 )
            {
                int iBlockX = iOffsetX + ( m_locationsRequiringClientUpdate[0] >> 12 & 15 );
                int iBlockY = m_locationsRequiringClientUpdate[0] & 255;
                int iBlockZ = iOffsetZ + ( m_locationsRequiringClientUpdate[0] >> 8 & 15 );
                
                SendToPlayersWatchingNotWaitingFullChunk( new Packet53BlockChange(
                	iBlockX, iBlockY, iBlockZ, m_chunkTracker.m_worldServer ) );

                if ( m_chunkTracker.m_worldServer.blockHasTileEntity( 
                	iBlockX, iBlockY, iBlockZ ) )
                {
                    SendTileEntityToPlayersWatchingChunk( 
                    	m_chunkTracker.m_worldServer.getBlockTileEntity( 
                		iBlockX, iBlockY, iBlockZ ) );
                }
            }
            else if ( m_iNumLocationsRequiringClientUpdate == 64 )
            {
            	// if the number of updates has hit maximum capacity, send the entire 
            	// modified vertical chunks to players in one combined packet instead of 
            	// individual blocks
            	
                SendToPlayersWatchingNotWaitingFullChunk( new Packet51MapChunk(
                	m_chunkTracker.m_worldServer.getChunkFromChunkCoords(
            		m_coord.chunkXPos, m_coord.chunkZPos ), false, 
            		m_iVerticalChunksToUpdatePlayersBitfield ) );

                for ( int iTempVerticalChunk = 0; iTempVerticalChunk < 16; iTempVerticalChunk++ )
                {
                    if ( ( m_iVerticalChunksToUpdatePlayersBitfield & 1 << iTempVerticalChunk ) != 
                    	0 )
                    {
                        int iTempY = iTempVerticalChunk << 4;
                        
                        List<TileEntity> tempTileEntities = 
                        	m_chunkTracker.m_worldServer.getAllTileEntityInBox(
                        	iOffsetX, iTempY, iOffsetZ, iOffsetX + 16, iTempY + 16, iOffsetZ + 16 );

                        for ( int iTempCount = 0; iTempCount < tempTileEntities.size(); 
                        	iTempCount++ )
                        {
                            SendTileEntityToPlayersWatchingChunk( 
                            	tempTileEntities.get( iTempCount ) );
                        }
                    }
                }
            }
            else
            {
                SendToPlayersWatchingNotWaitingFullChunk( new Packet52MultiBlockChange(
                	m_coord.chunkXPos, m_coord.chunkZPos, m_locationsRequiringClientUpdate, 
                	m_iNumLocationsRequiringClientUpdate, m_chunkTracker.m_worldServer ) );

                for ( int iTempIndex = 0; iTempIndex < m_iNumLocationsRequiringClientUpdate; iTempIndex++ )
                {
                    int iBlockX = iOffsetX + ( m_locationsRequiringClientUpdate[iTempIndex] >> 12 & 15 );
                    int iBlockY = m_locationsRequiringClientUpdate[iTempIndex] & 255;
                    int iBlockZ = iOffsetZ + ( m_locationsRequiringClientUpdate[iTempIndex] >> 8 & 15 );

                    if ( m_chunkTracker.m_worldServer.blockHasTileEntity( 
                    	iBlockX, iBlockY, iBlockZ ) )
                    {
                        SendTileEntityToPlayersWatchingChunk( 
                        	m_chunkTracker.m_worldServer.getBlockTileEntity( 
                        		iBlockX, iBlockY, iBlockZ ) );
                    }
                }
            }

            m_iNumLocationsRequiringClientUpdate = 0;
            m_iVerticalChunksToUpdatePlayersBitfield = 0;
        }
    }

    private void SendTileEntityToPlayersWatchingChunk( TileEntity tileEntity )
    {
        Packet packet = tileEntity.getDescriptionPacket();

        if ( packet != null )
        {
            SendToPlayersWatchingNotWaitingFullChunk( packet );
        }
    }

    public ChunkCoordIntPair getChunkLocation()
    {
        return m_coord;
    }

    public List getPlayersInChunk()
    {
        return m_playersWatching;
    }
}
