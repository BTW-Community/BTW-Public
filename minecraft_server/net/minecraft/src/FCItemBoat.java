// FCMOD

package net.minecraft.src;

public class FCItemBoat extends ItemBoat
{
    public FCItemBoat( int iItemID )
    {
    	super( iItemID );

    	SetBuoyant();
    	SetIncineratedInCrucible();
    	
    	// same as 5 jungle sidings to be equivalent to crappiest recipe
    	SetFurnaceBurnTime( 5 * FCEnumFurnaceBurnTime.PLANKS_JUNGLE.m_iBurnTime / 2 );
    	
    	setUnlocalizedName( "boat" );
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
        FCUtilsBlockPos offsetPos = new FCUtilsBlockPos( 0, 0, 0, iFacing );
        
        double dXPos = i + ( offsetPos.i * 1.6D ) + 0.5D;
        double dYPos = j + offsetPos.j;
        double dZPos = k + ( offsetPos.k * 1.6D ) + 0.5D;
    	
    	double dBoatYPos = j + offsetPos.j;                	
    	
        Entity entity = new EntityBoat( world, dXPos, dYPos, dZPos );
        
        world.spawnEntityInWorld( entity );
        
        world.playAuxSFX( 1000, i, j, k, 0 ); // normal pitch click							        
        
		return true;
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
