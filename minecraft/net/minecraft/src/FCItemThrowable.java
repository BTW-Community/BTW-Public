// FCMOD

package net.minecraft.src;

public abstract class FCItemThrowable extends Item
{
    public FCItemThrowable( int iItemID )
    {
    	super( iItemID );
    }
    
    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {        
        if ( !player.capabilities.isCreativeMode )
        {
        	stack.stackSize--;
        }
        
        world.playSoundAtEntity( player, "random.bow", 0.5F, 0.4F / 
        	( itemRand.nextFloat() * 0.4F + 0.8F ) );
        
        if( !world.isRemote )
        {
        	SpawnThrownEntity( stack, world, player );
        }
    	
        return stack;
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
    		
    		dYHeading = 0.1D;
    	}
    	else
    	{
    		dYHeading = offsetPos.j;
    	}
    	
    	EntityThrowable entity = GetEntityFiredByByBlockDispenser( world, dXPos, dYPos, dZPos );
    	
        entity.setThrowableHeading( offsetPos.i, dYHeading, offsetPos.k, 1.1F, 6F );
        
        world.spawnEntityInWorld( entity );
        
        world.playAuxSFX( 1002, i, j, k, 0 ); // bow sound
        
		return true;
	}
    
    //------------- Class Specific Methods ------------//
    
    protected abstract void SpawnThrownEntity( ItemStack stack, World world, 
    	EntityPlayer player );
    
    protected abstract EntityThrowable GetEntityFiredByByBlockDispenser( World world, 
    	double dXPos, double dYPos, double dZPos );
    
	//------------ Client Side Functionality ----------//
}
