// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBucketWater extends FCBlockBucketFull
{
    public FCBlockBucketWater( int iBlockID )
    {
        super( iBlockID );
    	
    	setUnlocalizedName( "bucketWater" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneMod )
    {
		return Item.bucketWater.itemID;
    }
	
	//------------- Class Specific Methods ------------//
	
	@Override
    public boolean AttemptToSpillIntoBlock( World world, int i, int j, int k )
    {
        if ( ( world.isAirBlock( i, j, k ) || !world.getBlockMaterial( i, j, k ).isSolid() ) )
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
        		            world.setBlockAndMetadataWithNotify( i, j, k, 
        		            	Block.waterMoving.blockID, 6 );        					
        				}
        			}
        		}
            }
            
        	return true;
        }
        
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
	
    private Icon m_iconWater;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		// have to use non-transparent version of the water texture, as otherwise it'll render
		// with alpha when pushed by a piston, but not in block form
		
		m_iconWater = register.registerIcon( "fcBlockBucket_water" );
    }
	
	@Override
	protected Icon GetContentsIcon()
	{
		return m_iconWater;
	}
}
