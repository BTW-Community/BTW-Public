// FCMOD

package net.minecraft.src;

public class FCItemBucketWater extends FCItemBucketFull
{
    public FCItemBucketWater( int iItemID )
    {
    	super( iItemID );
    	
    	setUnlocalizedName( "bucketWater" );
	}
    
    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockBucketWater.blockID;
    }

    @Override
	protected boolean AttemptPlaceContentsAtLocation( World world, int i, int j, int k )
	{
        if ( ( world.isAirBlock( i, j, k ) || !world.getBlockMaterial( i, j, k ).isSolid() ) )
        {           
        	if ( !world.isRemote )
        	{
	            if( world.provider.isHellWorld )
	            {
	        		world.playAuxSFX( FCBetterThanWolves.m_iWaterEvaporateAuxFXID, i, j, k, 0 );
	            } 
	            else
	            {
	            	int iTargetBlockID = world.getBlockId( i, j, k );
	            	int iTargetMetadata = world.getBlockMetadata( i, j, k );
	            	
	        		if ( iTargetBlockID == Block.lavaMoving.blockID || 
	        				iTargetBlockID == Block.lavaStill.blockID )
	        		{
		        		world.playAuxSFX( FCBetterThanWolves.m_iWaterEvaporateAuxFXID, i, j, k, 0 );
	                    
            			if ( iTargetMetadata == 0 )
            			{
                			world.setBlockWithNotify( i, j, k, Block.obsidian.blockID );
            			}
            			else
            			{
                			world.setBlockWithNotify( i, j, k, 
                				FCBetterThanWolves.fcBlockLavaPillow.blockID );
            			}
	        		}
	        		else
	        		{        			
	        			// do not replace existing source blocks
	        			
	            		if ( ( iTargetBlockID != Block.waterMoving.blockID && 
	        				iTargetBlockID != Block.waterStill.blockID ) ||
	        				iTargetMetadata != 0 )
	        			{
	        				if ( world.provider.dimensionId == 1 )
	        				{
	        					// place water source block in the end dimension
	        					
	                			world.setBlockWithNotify( i, j, k, Block.waterMoving.blockID );
	        				}
	        				else
	        				{        					
	        					FCUtilsMisc.PlaceNonPersistantWater( world, i, j, k );
	        				}
	        			}
	        		}
	            }
        	}
            
            return true;
        }
        
        return false;
	}
	
	//------------- Class Specific Methods ------------//

	//----------- Client Side Functionality -----------//
}