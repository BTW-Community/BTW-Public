// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBeacon extends BlockBeacon
{
	private static final long m_lRespawnCooldownDuration = 60;
	
    protected FCBlockBeacon( int iBlockID )
    {
    	super( iBlockID );
    	
    	SetPicksEffectiveOn( true );
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityBeacon();
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		FCTileEntityBeacon tileEntity = (FCTileEntityBeacon)world.getBlockTileEntity( i, j, k );
		
		if ( tileEntity != null )
		{
			FCUtilsInventory.EjectInventoryContents( world, i, j, k, tileEntity );
			
			if ( tileEntity.getLevels() > 0 )
			{
				// play power down sound
				
		        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		    		"mob.wither.death", 
		    		1.0F + ( world.rand.nextFloat() * 0.1F ),	// volume 
		    		1.0F + ( world.rand.nextFloat() * 0.1F ) );	// pitch
			}
			
			// update the block's power state so that any associated global effects destroyed
			
			tileEntity.SetPowerState( false, 0, 0 );
		}

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }
	
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
		// override to disable the beacon's interface since power is now determined based on material
		
    	FCTileEntityBeacon beaconEnt = (FCTileEntityBeacon)world.getBlockTileEntity( i, j, k );
    	
    	if ( beaconEnt != null )
    	{
    		int iBeaconEffect = beaconEnt.getPrimaryEffect();
    		
    		if ( iBeaconEffect == FCTileEntityBeacon.m_iEffectIDSpawnPoint )
    		{
    			int iBeaconPowerLevel = beaconEnt.getLevels();
    			
    			if ( iBeaconPowerLevel > 0 )
    			{
    				if ( !world.isRemote )
    				{
    					if ( world.getWorldTime() < player.m_lRespawnAssignmentCooldownTimer || 
    						world.getWorldTime() - player.m_lRespawnAssignmentCooldownTimer > m_lRespawnCooldownDuration )
    					{    					
	    					player.AddRawChatMessage( "You have been bound to this beacon" );
	    					
	    					ChunkCoordinates newSpawnPos = new ChunkCoordinates( i, j, k );
	    					
	    		            player.setSpawnChunk( newSpawnPos, false, world.provider.dimensionId );
	    		            
	    		            player.m_lRespawnAssignmentCooldownTimer = world.getWorldTime();
	    		            
			                world.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, i, j, k, 1 );
			                
					        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
					    		"mob.wither.spawn", 
					    		1.0F + ( world.rand.nextFloat() * 0.1F ),	// volume 
					    		1.0F + ( world.rand.nextFloat() * 0.1F ) );	// pitch		        
   					}
					}
    				
					return true;
    			}
    		}    			
    	}
    	
		return false;
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	if ( iSide != 0 )
    	{
    		return true;
    	}
    	
        return !blockAccess.isBlockOpaqueCube( i, j, k );
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.RenderBlockBeacon( this, i, j, k );
    }    
}