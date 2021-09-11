// FCMOD

package net.minecraft.src;

public class FCBlockSoulSand extends BlockSoulSand
{
    public FCBlockSoulSand( int iBlockID )
    {
        super( iBlockID );
        
        SetShovelsEffectiveOn();
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSoulSand.itemID, 
			3, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	return filteredItem.itemID == FCBetterThanWolves.fcItemGroundNetherrack.itemID || 
	    	filteredItem.itemID == FCBetterThanWolves.fcItemSoulDust.itemID ||
	    	filteredItem.itemID == Item.lightStoneDust.itemID;
    }
    
    @Override
    public boolean CanTransformItemIfFilter( ItemStack filteredItem )
    {
    	// anything that passes through soul sand is transformed
    	
    	return true;
    }
    
    @Override
    public boolean CanNetherWartGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
    
    private Icon m_filterIcon;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_filterIcon = register.registerIcon( "fcBlockHopper_soulsand" );
    }
	
	@Override
    public Icon GetHopperFilterIcon()
    {
    	return m_filterIcon;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	return renderer.RenderStandardFullBlock( this, i, j, k );
    }
    
    @Override
    public boolean DoesItemRenderAsBlock( int iItemDamage )
    {
    	return true;
    }    
    
    @Override
    public void RenderBlockMovedByPiston( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	renderBlocks.RenderStandardFullBlockMovedByPiston( this, i, j, k );
    }    
}