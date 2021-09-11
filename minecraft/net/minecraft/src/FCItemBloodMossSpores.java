// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemBloodMossSpores extends Item
{
    public FCItemBloodMossSpores( int iItemID )
    {
    	super( iItemID );
    	
        setMaxDamage( 0 );
        setHasSubtypes( false );
        
        SetBuoyant();
        
    	setUnlocalizedName( "fcItemSporesNetherGroth" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
		// tests copied over from itemReed to be on the safe side
        if ( player != null && !player.canPlayerEdit( i, j, k, iFacing, itemStack ) )
        {
            return false;
        }
        
        if ( itemStack.stackSize == 0 )
        {
            return false;
        }
        
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		targetPos.AddFacingAsOffset( iFacing );

        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
        
        if(worldchunkmanager != null)
        {
            BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(i, k);
            
            if( biomegenbase instanceof BiomeGenHell )
            {
				int iBlockID = FCBetterThanWolves.fcBlockBloodMoss.blockID;
				int iMetadata = 0;
				
		        if ( world.canPlaceEntityOnSide( iBlockID, targetPos.i, targetPos.j, targetPos.k, 
		    		false, iFacing, player, itemStack ) )
		        {
		        	if ( !world.isRemote )
		        	{
		                iMetadata = Block.blocksList[iBlockID].onBlockPlaced( world, targetPos.i, targetPos.j, targetPos.k, iFacing, fClickX, fClickY, fClickZ, iMetadata );
		                
		            	iMetadata = Block.blocksList[iBlockID].PreBlockPlacedBy( world, targetPos.i, targetPos.j, targetPos.k, iMetadata, player );
		            	
			            if ( world.setBlockAndMetadataWithNotify( targetPos.i, targetPos.j, 
			        		targetPos.k, iBlockID, iMetadata ) )
			            {
			                Block block = Block.blocksList[iBlockID];
			                
			                if ( world.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == iBlockID )
			                {
			                    Block.blocksList[iBlockID].onBlockPlacedBy( world, targetPos.i, targetPos.j, 
			                		targetPos.k, player, itemStack );
			                    
			                    Block.blocksList[iBlockID].onPostBlockPlaced( world, targetPos.i, targetPos.j, 
			                		targetPos.k, iMetadata );
			                }
			                
			                world.playSoundEffect((float)targetPos.i + 0.5F, (float)targetPos.j + 0.5F, 
			            		(float)targetPos.k + 0.5F, block.stepSound.getStepSound(), 
			            		(block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			                
	    		            AngerPigmen( world, player );
	    		            
			            }
		        	}
		            
	                itemStack.stackSize--;	                
		            
		        	return true;        	
		        }
            }
		}
            
    	return false;
    }
    
    //************* Class Specific Methods ************//
	
    private void AngerPigmen( World world, EntityPlayer entityPlayer )
    {
        List list = world.getEntitiesWithinAABB( FCEntityPigZombie.class, 
        	entityPlayer.boundingBox.expand( 32D, 32D, 32D ) );
        
        for( int tempIndex = 0; tempIndex < list.size(); tempIndex++ )
        {
            Entity targetEntity = (Entity)list.get( tempIndex );
            
            targetEntity.attackEntityFrom( DamageSource.causePlayerDamage( entityPlayer ), 0 );
            
        }
    }
}
