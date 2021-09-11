// FCMOD

package net.minecraft.src;

public class FCItemPileSoulSand extends Item
{
    public FCItemPileSoulSand( int iItemID )
    {
        super( iItemID );
        
        SetBellowsBlowDistance( 1 );
		SetFilterableProperties( m_iFilterable_Fine );
        
        setUnlocalizedName( "fcItemPileSoulSand" );
        
        setCreativeTab( CreativeTabs.tabMaterials );
    }

    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player)
    {
        if ( !world.isRemote )
        {
        	boolean bHasTarget = false;
        	
        	double dTargetXPos = player.posX;
        	double dTargetZPos = player.posZ;
        	
        	if ( world.provider.dimensionId == 0 )
        	{
        		FCSpawnLocationList spawnList = world.GetSpawnLocationList();
        		
        		FCSpawnLocation closestSpawnLoc = spawnList.GetClosestSpawnLocationForPosition( player.posX, player.posZ );
        		
                if ( closestSpawnLoc != null )
                {
                	dTargetXPos = (double)closestSpawnLoc.m_iIPos;
                	dTargetZPos = (double)closestSpawnLoc.m_iKPos;
                	
                	bHasTarget = true;
                }                
        	}
        	else if ( world.provider.dimensionId == -1 )
        	{
        		IChunkProvider provider = world.getChunkProvider();
        		
        		if ( provider != null && provider instanceof ChunkProviderServer )
        		{
        			ChunkProviderServer serverProvider = (ChunkProviderServer)provider;
        			
        			provider = serverProvider.GetCurrentProvider();
        			
        			if ( provider != null && provider instanceof ChunkProviderHell )
        			{
        				ChunkProviderHell hellProvider = (ChunkProviderHell)provider;
        				
        				StructureStart closestFortress = hellProvider.genNetherBridge.GetClosestStructureWithinRangeSq( player.posX, player.posZ, 90000 ); // 300 block range
        				
        				if ( closestFortress != null )
        				{
                        	dTargetXPos = (double)closestFortress.boundingBox.getCenterX();
                        	dTargetZPos = (double)closestFortress.boundingBox.getCenterZ();
                        	
                        	bHasTarget = true;
        				}        				
        			}
        		}
        	}
        	
            FCEntitySoulSand sandEntity = new FCEntitySoulSand( world, player.posX, player.posY + 2.0D - (double)player.yOffset, player.posZ );

        	sandEntity.MoveTowards( dTargetXPos, dTargetZPos );
                
            world.spawnEntityInWorld( sandEntity );

            if ( bHasTarget )
            {
	        	world.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID, 
	        		(int)Math.round( sandEntity.posX ), (int)Math.round( sandEntity.posY ), (int)Math.round( sandEntity.posZ), 0 );
            }
                
            if (!player.capabilities.isCreativeMode)
            {
                --stack.stackSize;
            }
        }

        return stack;
    }
    
    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 4;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return Block.slowSand.blockID;
    }
}
