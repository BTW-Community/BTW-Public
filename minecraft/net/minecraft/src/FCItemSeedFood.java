// FCMOD

package net.minecraft.src;

public class FCItemSeedFood extends ItemFood
{
    protected int m_iCropBlockID;
    
    public FCItemSeedFood( int iItemID, int iHealAmount, float fSaturationModifier, 
    	int iCropBlockID )
    {    	
	    // Note that seed foods are 1/3 as effective as others, so the HealAmount here is 
    	// should take this into account.  This is due to GetHungerRestored() below
    	
    	super( iItemID, iHealAmount, fSaturationModifier, false );
    	
    	m_iCropBlockID = iCropBlockID;
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
        if ( iFacing == 1 )
        {
	        if ( player == null || ( player.canPlayerEdit( i, j, k, iFacing, itemStack ) && 
	        	player.canPlayerEdit( i, j + 1, k, iFacing, itemStack ) ) )
	        {
	            Block cropBlock = Block.blocksList[m_iCropBlockID]; 
	
	            if ( cropBlock != null && cropBlock.canPlaceBlockAt( world, i, j + 1, k ) )
	            {
	                world.setBlockWithNotify( i, j + 1, k, m_iCropBlockID );
	                
	                world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, 
	                	Block.soundGrassFootstep.getStepSound(), 
	                	( Block.soundGrassFootstep.getVolume() + 1F ) / 2F, 
	                	Block.soundGrassFootstep.getPitch() * 0.8F );
	                
	                --itemStack.stackSize;
	                
	                return true;
	            }
	        }
        }
        
        return false;
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
        Block cropBlock = Block.blocksList[m_iCropBlockID]; 
    	
        if ( cropBlock != null && cropBlock.canPlaceBlockAt( world, 
        	targetPos.i, targetPos.j, targetPos.k ) )
        {
            world.setBlockWithNotify( targetPos.i, targetPos.j, targetPos.k, m_iCropBlockID );
            
            world.playSoundEffect( targetPos.i + 0.5D, targetPos.j + 0.5D, targetPos.k + 0.5D, 
            	Block.soundGrassFootstep.getStepSound(), 
            	( Block.soundGrassFootstep.getVolume() + 1F ) / 2F, 
            	Block.soundGrassFootstep.getPitch() * 0.8F );
            
            return true;
        }
        
        return false;
	}
    
    @Override
    public int GetHungerRestored()
    {
    	// do not multiply the heal amount so that seeds only restore a bit of hunger
    	
    	return getHealAmount();
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
