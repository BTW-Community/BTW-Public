// FCMOD

package net.minecraft.src;

public class FCItemCocoaBeans extends FCItemFood
{
	static public final int m_iHungerHealed = 1;
	static public final float m_fSaturationModifier = 0F;
	static public final String m_sItemName = "fcItemCocoaBeans";
	
    public FCItemCocoaBeans( int iItemID )
    {
    	super( iItemID, m_iHungerHealed, m_fSaturationModifier, false, m_sItemName );
    	
        SetBellowsBlowDistance( 1 );
		SetFilterableProperties( m_iFilterable_Small );
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
    	if ( AttemptPlaceOn( world, i, j, k, iFacing, 
        	fClickX, fClickY, fClickZ ) )
    	{
            if ( player == null || !player.capabilities.isCreativeMode)
            {
            	itemStack.stackSize--;
            }
            
            return true;
    	}
    	
    	return false;
    }
    
    protected boolean AttemptPlaceOn( World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ )
    {
        int iTargetBlockID = world.getBlockId( i, j, k );
        int iTargetMetadata = world.getBlockMetadata( i, j, k );

        if ( iFacing >= 2 && iTargetBlockID == Block.wood.blockID && 
        	BlockLog.limitToValidMetadata( iTargetMetadata ) == 3 )
        {
            FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
            
            if ( world.isAirBlock( targetPos.i, targetPos.j, targetPos.k ) )
            {
            	int iCocoaBlockID = Block.cocoaPlant.blockID;
            	
            	int iMetadata = Block.blocksList[iCocoaBlockID].onBlockPlaced( world, 
            		targetPos.i, targetPos.j, targetPos.k, iFacing, fClickX, fClickY, fClickZ, 0 );
            	
            	world.setBlockAndMetadataWithNotify( targetPos.i, 
            		targetPos.j, targetPos.k, iCocoaBlockID, iMetadata );
            	
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public int GetHungerRestored()
    {
    	// do not multiply the heal amount so that cocoa beans only restore a bit of hunger
    	
    	return getHealAmount();
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
        FCUtilsBlockPos block2AwayPos = new FCUtilsBlockPos( i, j, k, iFacing );
        
        block2AwayPos.AddFacingAsOffset( iFacing );
        
    	if ( AttemptPlaceOn( world, block2AwayPos.i, block2AwayPos.j, block2AwayPos.k, 
    		Block.GetOppositeFacing( iFacing ), 0F, 0F, 0F ) )
    	{
            world.playAuxSFX( FCBetterThanWolves.m_iBlockPlaceAuxFXID, i, j, k, 
            	Block.cocoaPlant.blockID );
            
			return true;
    	}
    	
    	return false;
	}
    
	//----------- Client Side Functionality -----------//
	
    @Override
    public void registerIcons( IconRegister register )
    {
        itemIcon = register.registerIcon( "dyePowder_brown" );
    }
}
