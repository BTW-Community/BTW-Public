//FCMOD

package net.minecraft.src;

public class FCItemRope extends Item
{
    public FCItemRope( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();
        
        setUnlocalizedName( "fcItemRope" );
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        if ( itemStack.stackSize == 0 )
        {
            return false;
        }
        
        int iTargetBlockID = world.getBlockId( i, j, k ); 
        
        // rope can only be attached to anchors or other ropes
        
        if ( ( iTargetBlockID == FCBetterThanWolves.fcAnchor.blockID && ((FCBlockAnchor)FCBetterThanWolves.fcAnchor).GetFacing( world, i, j, k ) != 1 ) ||
    		iTargetBlockID == FCBetterThanWolves.fcRopeBlock.blockID )
        {
        	// scan downward towards bottom of rope
        	
        	for ( int tempj = j - 1; tempj >= 0; tempj-- )
        	{
        		int iTempBlockID = world.getBlockId( i, tempj, k );
        		
        		if ( FCUtilsWorld.IsReplaceableBlock( world, i, tempj, k ) )
        		{
        			int iMetadata = FCBetterThanWolves.fcRopeBlock.onBlockPlaced( world, i, tempj, k, iFacing, 0F, 0F, 0F, 0 );
        			
                    iMetadata = FCBetterThanWolves.fcRopeBlock.PreBlockPlacedBy( world, i, tempj, k, iMetadata, player );            
        			
                    if( world.setBlockAndMetadataWithNotify( i, tempj, k, FCBetterThanWolves.fcRopeBlock.blockID, iMetadata ) )
                    {	        			
	        			FCBetterThanWolves.fcRopeBlock.onBlockPlacedBy( world, i, tempj, k, player, itemStack );
	        			
	        			FCBetterThanWolves.fcRopeBlock.onPostBlockPlaced( world, i, tempj, k, iMetadata );
	
	                    world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 
                    		FCBetterThanWolves.fcRopeBlock.stepSound.getStepSound(), 
                    		(FCBetterThanWolves.fcRopeBlock.stepSound.getVolume() + 1.0F) / 2.0F, 
                    		FCBetterThanWolves.fcRopeBlock.stepSound.getPitch() * 0.8F);
	
	                    itemStack.stackSize--;
	                    
	                    return true;
                    }
                    
                    return false;
        		}
        		else if ( iTempBlockID != FCBetterThanWolves.fcRopeBlock.blockID )
        		{
        			return false;
        		}
        	}        
        }
    	
    	return false;
    }    
}