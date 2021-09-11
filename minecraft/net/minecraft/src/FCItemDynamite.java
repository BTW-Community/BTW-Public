// FCMOD

package net.minecraft.src;

public class FCItemDynamite extends Item
{
    public FCItemDynamite( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
    	
    	setUnlocalizedName( "fcItemDynamite" );
    	
    	setCreativeTab( CreativeTabs.tabCombat );    	
	}
    
    @Override
    public ItemStack onItemRightClick( ItemStack itemStack, World world, EntityPlayer entityPlayer )
    {
    	int iFlintAndSteelSlot = -1;
    	
        for( int j = 0; j < entityPlayer.inventory.mainInventory.length; j++ )
        {
            if( entityPlayer.inventory.mainInventory[j] != null && 
        		entityPlayer.inventory.mainInventory[j].itemID == Item.flintAndSteel.itemID )
            {
                iFlintAndSteelSlot =  j;
                
                break;
            }
        }
        
        if( !world.isRemote )
        {
        	boolean bLit = false;
        	
	        if ( iFlintAndSteelSlot >= 0 )
			{
	        	bLit = true;
	        	
	            // damage the flint & steel
	            
	            ItemStack flintAndSteelItemStack = entityPlayer.inventory.getStackInSlot( 
	        		iFlintAndSteelSlot );
	            
	            flintAndSteelItemStack.damageItem( 1, entityPlayer );	 
	            
	            if ( flintAndSteelItemStack.stackSize <= 0 )
	            {
	            	entityPlayer.inventory.mainInventory[iFlintAndSteelSlot] = null;
	            }
			}
	        
	        itemStack.stackSize--;
	        
        	FCEntityDynamite dynamiteEnt = new FCEntityDynamite( world, entityPlayer, itemID, bLit );
        	
            world.spawnEntityInWorld( dynamiteEnt );
            
            if ( bLit )
            {            	
            	world.playSoundAtEntity( dynamiteEnt, "random.fuse", 1.0F, 1.0F);
            }
            else
            {
                world.playSoundAtEntity( dynamiteEnt, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            }            
		}
        
        return itemStack;
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
    	
    	FCEntityDynamite entity = new FCEntityDynamite( world, dXPos, dYPos, dZPos, itemID );
    	
        entity.setThrowableHeading( offsetPos.i, dYHeading, offsetPos.k, 1.1F, 6F );
        
        world.spawnEntityInWorld( entity );
        
        world.playAuxSFX( 1002, i, j, k, 0 ); // bow sound
        
		return true;
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
