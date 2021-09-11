// FCMOD

package net.minecraft.src;

public class FCItemEgg extends FCItemThrowable
{
    public FCItemEgg( int iItemID )
    {
    	super( iItemID );

        maxStackSize = 16;
        
    	SetIncineratedInCrucible();
    	SetFilterableProperties( m_iFilterable_Small );
    	
    	setUnlocalizedName( "egg" );
    	
        setCreativeTab( CreativeTabs.tabFood );
    }
    
    @Override
    protected void SpawnThrownEntity( ItemStack stack, World world, 
    	EntityPlayer player )
    {
        world.spawnEntityInWorld( new EntityEgg( world, player ) );
    }
    
    @Override
    protected EntityThrowable GetEntityFiredByByBlockDispenser( World world, 
    	double dXPos, double dYPos, double dZPos )
    {
    	return new EntityEgg( world, dXPos, dYPos, dZPos );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
