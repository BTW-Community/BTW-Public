// FCMOD

package net.minecraft.src;

public class FCItemArrow extends Item
{
    public FCItemArrow( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
    	SetBellowsBlowDistance( 1 );
    	SetIncineratedInCrucible();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT );
    	SetFilterableProperties( m_iFilterable_Narrow );
    	
    	setUnlocalizedName( "arrow" );
    	
    	setCreativeTab( CreativeTabs.tabCombat );
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
        FCUtilsBlockPos offsetPos = new FCUtilsBlockPos( 0, 0, 0, iFacing );
        
        double dXPos = i + ( offsetPos.i * 0.6D ) + 0.5D;
        double dYPos = j + ( offsetPos.j * 0.6D ) + 0.5D;
        double dZPos = k + ( offsetPos.k * 0.6D ) + 0.5D;
    	
    	double dYHeading;
    	
    	if ( iFacing > 2 )
    	{
    		// slight upwards trajectory when fired sideways
    		
    		dYHeading = 0.10000000149011611F;
    	}
    	else
    	{
    		dYHeading = offsetPos.j;
    	}
    	
        EntityArrow entityarrow = GetFiredArrowEntity( world, dXPos, dYPos, dZPos );
        entityarrow.setThrowableHeading( offsetPos.i, dYHeading, offsetPos.k, 1.1F, 6F);
        world.spawnEntityInWorld( entityarrow );
        
        world.playAuxSFX( 1002, i, j, k, 0 ); // bow sound
        
		return true;
	}
	
    //------------- Class Specific Methods ------------//
    
    EntityArrow GetFiredArrowEntity( World world, double dXPos, double dYPos, double dZPos )
    {
        EntityArrow entity = new EntityArrow( world, dXPos, dYPos, dZPos );
        
        entity.canBePickedUp = 1;
        
        return entity;
    }
    
	//------------ Client Side Functionality ----------//
}
