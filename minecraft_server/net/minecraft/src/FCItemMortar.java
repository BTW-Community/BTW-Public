// FCMOD

package net.minecraft.src;

public class FCItemMortar extends Item
{
    public FCItemMortar( int iItemID )
    {
    	super( iItemID );
    }
    
    @Override
    public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, 
    	int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
        if ( player != null && player.canPlayerEdit( i, j, k, iFacing, stack ) )
        {
        	Block targetBlock = Block.blocksList[world.getBlockId( i, j, k )];
        	
        	if ( targetBlock != null && targetBlock.OnMortarApplied( world, i, j, k ) )
        	{            	
    			if ( !world.isRemote )
    			{
    		        world.playAuxSFX( FCBetterThanWolves.m_iMortarAppliedAuxFXID, i, j, k, 0 ); 
    			}
    	        
    			stack.stackSize--;
    			
            	return true;
        	}
        }
        
        return false;
    }
    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
