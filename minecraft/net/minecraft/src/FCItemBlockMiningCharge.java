// FCMOD

package net.minecraft.src;

public class FCItemBlockMiningCharge extends ItemBlock
{
    public FCItemBlockMiningCharge( int iItemID )
    {
    	super( iItemID );
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    	
		FCBlockMiningCharge.CreatePrimedEntity( world, 
			targetPos.i, targetPos.j, targetPos.k, iFacing );
		
        world.playAuxSFX( FCBetterThanWolves.m_iBlockPlaceAuxFXID, i, j, k, 
        	FCBetterThanWolves.fcBlockMiningCharge.blockID );
        
		return true;
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
