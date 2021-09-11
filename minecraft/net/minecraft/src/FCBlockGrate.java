// FCMOD

package net.minecraft.src;

public class FCBlockGrate extends FCBlockPane
{
    protected FCBlockGrate( int iBlockID )
    {
        super( iBlockID, "fcBlockGrate", "fcBlockGrate", 
        	Material.wood, false );
        
        setHardness( 0.5F );        
        SetAxesEffectiveOn();
		
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.PLANKS );
		
        setLightOpacity( 2 );
        Block.useNeighborBrightness[iBlockID] = true;
        
        setStepSound( soundWoodFootstep );        
        
        setUnlocalizedName( "fcBlockGrate" );
    }
    
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
		return false;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, 
		int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 
			2, 0, fChanceOfDrop );
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 
			2, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
    		
    	return ( iFilterableProperties & ( Item.m_iFilterable_Small | 
    		Item.m_iFilterable_Fine ) ) != 0;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_filterIcon;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_filterIcon = register.registerIcon( "fcBlockHopper_grate" );
    }
	
	@Override
    public Icon GetHopperFilterIcon()
    {
    	return m_filterIcon;
    }
}
