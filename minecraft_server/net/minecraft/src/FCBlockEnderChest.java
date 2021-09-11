// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCBlockEnderChest extends BlockEnderChest
{
    protected FCBlockEnderChest( int iBlockID )
    {
    	super( iBlockID );
    	
        InitBlockBounds( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityEnderChest();
    }
    
    @Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	if ( !world.isRemote )
    	{
        	if ( !world.isBlockNormalCube( i, j + 1, k ) )
        	{
                FCTileEntityEnderChest tileEntity = (FCTileEntityEnderChest)world.getBlockTileEntity( i, j, k );
                
                if ( tileEntity != null )
                {
	                InventoryEnderChest chestInventory = null;
	                
		    		int iAntennaLevel = ComputeLevelOfEnderChestsAntenna( world, i, j, k );
		    		
		    		if ( iAntennaLevel == 0 )
		    		{
		    			// local inventory
		    			
		    			chestInventory = tileEntity.GetLocalEnderChestInventory();
		    		}
		    		else if ( iAntennaLevel == 1 )
		    		{
		    			// dimension specific low power communal inventory
		    			
		    			chestInventory = world.GetLocalLowPowerEnderChestInventory();
		    		}
		    		else if ( iAntennaLevel == 2 )
		    		{
		    			// dimension specific communal inventory
		    			
		    			chestInventory = world.GetLocalEnderChestInventory();
		    		}
		    		else if ( iAntennaLevel == 3 )
		    		{
		    			// global communal inventory
		    			
                		chestInventory = MinecraftServer.getServer().worldServers[0].worldInfo.GetGlobalEnderChestInventory();                        
		    		}
		    		else
		    		{
		    			// global private inventory
		    			
		                chestInventory = player.getInventoryEnderChest();
		    		}
		    		
		    		if ( chestInventory != null )
		    		{
                        chestInventory.setAssociatedChest( tileEntity );
                        
                        player.displayGUIChest( chestInventory );
                        
                        if ( iAntennaLevel >= 1 )
                        {
            	            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.endermen.stare", 
            	            	0.25F * (float)iAntennaLevel, world.rand.nextFloat() * 0.4F + 1.2F );
                        }                        
		    		}
                }
        	}    		
    	}
        
        return true;        
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		if ( !world.isRemote )
		{
	        FCTileEntityEnderChest tileEntity = (FCTileEntityEnderChest)world.getBlockTileEntity( i, j, k );
	        
	        if ( tileEntity != null )
	        {
	        	InventoryEnderChest chestInventory = tileEntity.GetLocalEnderChestInventory();
	        	
	        	if ( chestInventory != null )
	        	{	        	
	        		FCUtilsInventory.EjectInventoryContents( world, i, j, k, chestInventory );
	        	}
	        }
		}

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }
	
    //------------- Class Specific Methods -------------//
    
    private int ComputeLevelOfEnderChestsAntenna( World world, int i, int j, int k )
    {
    	if ( world.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcAestheticOpaque.blockID &&
    		world.getBlockMetadata( i, j - 1, k ) == FCBlockAestheticOpaque.m_iSubtypeEnderBlock )
    	{
    		return GetLevelOfAntennaBeaconBlockIsPartOf( world, i, j - 1, k );
    	}
    	
    	return 0;
    }
    
    private int GetLevelOfAntennaBeaconBlockIsPartOf( World world, int i, int j, int k )
    {
    	// Note: This is pretty brute force.  There's probably a more efficient alogorithm for it, but it only occurs on player interaction
    	
    	// scan from the top down so that we can return on the first valid beacon found (since further ones will be lower power)
    	// the following creates an inverted pyramid with its point on the block in question, thus any active beacons of the appropriate level
    	// found *must* be associated with this block and be an antenna block pyramid 
    	
    	for ( int iTempLevel = 4; iTempLevel >= 1; iTempLevel-- )
    	{
    		int iTempJ = j + iTempLevel;
    		
	    	for ( int iTempI = i - iTempLevel; iTempI <= i + iTempLevel; iTempI++ )
	    	{
		    	for ( int iTempK = k - iTempLevel; iTempK <= k + iTempLevel; iTempK++ )
		    	{
		    		if ( world.getBlockId( iTempI, iTempJ, iTempK ) == Block.beacon.blockID )
		    		{
		                FCTileEntityBeacon tileEntity = (FCTileEntityBeacon)world.getBlockTileEntity( iTempI, iTempJ, iTempK );
		            	
		                if ( tileEntity != null )
		                {
		                	if ( tileEntity.getLevels() >= iTempLevel )
		                	{
		                		return iTempLevel;
		                	}
		                }
		    		}
		    	}
	    	}
    	}
    	
    	return 0;
    }
    
}
