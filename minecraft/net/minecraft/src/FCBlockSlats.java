// FCMOD

package net.minecraft.src;

public class FCBlockSlats extends FCBlockPane
{
    protected FCBlockSlats( int iBlockID )
    {
        super( iBlockID, "fcBlockSlats", "fcBlockSlats_side", 
        	Material.wood, false );
        
        setHardness( 0.5F );        
        SetAxesEffectiveOn();
		
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.PLANKS );
		
        setLightOpacity( 4 );
        Block.useNeighborBrightness[iBlockID] = true;
        
        setStepSound( soundWoodFootstep );        
        
        setUnlocalizedName( "fcBlockSlats" );
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
    		
    	return ( iFilterableProperties & ( Item.m_iFilterable_Thin | 
    		Item.m_iFilterable_Fine ) ) != 0;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_filterIcon;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_filterIcon = register.registerIcon( "fcBlockHopper_slats" );
    }
	
	@Override
    public Icon GetHopperFilterIcon()
    {
    	return m_filterIcon;
    }
}
