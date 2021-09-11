// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCItemPotion extends ItemPotion
{
    public FCItemPotion( int iItemID )
    {
        super( iItemID );
        
        SetNeutralBuoyant();
        
        setMaxStackSize( 8 );
        
        setUnlocalizedName( "potion" );
    }
    
    @Override
    public ItemStack onEaten( ItemStack itemStack, World world, EntityPlayer player )
    {
    	// function overriden to drop bottle into world if inventory is full
    	
        if ( !player.capabilities.isCreativeMode )
        {
            --itemStack.stackSize;
        }

        if ( !world.isRemote )
        {
            List effectsList = getEffects( itemStack );

            if ( effectsList != null )
            {
                Iterator effectIterator = effectsList.iterator();

                while ( effectIterator.hasNext() )
                {
                    PotionEffect tempEffect = (PotionEffect)effectIterator.next();
                    
                    player.addPotionEffect( new PotionEffect( tempEffect ) );
                }
            }
        }

        if ( !player.capabilities.isCreativeMode )
        {
        	ItemStack bottleStack = new ItemStack( Item.glassBottle );
        	
            if ( !player.inventory.addItemStackToInventory( bottleStack ) )
            {
                player.dropPlayerItem( bottleStack );
            }        	
        }

        return itemStack;
    }
 
    @Override
    public boolean IsMultiUsePerClick()
    {
    	return false;
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
    	if ( ItemPotion.isSplash( stack.getItemDamage() ) )
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
	    	
	    	EntityThrowable entity = new EntityPotion( world, dXPos, dYPos, dZPos, 
	    		new ItemStack( Item.potion, 1, stack.getItemDamage() ) );
	    	
	        entity.setThrowableHeading( offsetPos.i, dYHeading, offsetPos.k, 1.375F, 3F );
	        
	        world.spawnEntityInWorld( entity );
	        
	        world.playAuxSFX( 1002, i, j, k, 0 ); // bow sound
	        
			return true;
    	}
    	
    	return super.OnItemUsedByBlockDispenser( stack, world,i, j, k, iFacing );
	}
    
    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	if ( isSplash( stack.getItemDamage() ) || player.CanDrink() )
    	{
    		return super.onItemRightClick( stack, world, player );
    	}
    	else
    	{
    		player.OnCantConsume();
    		
    		return stack;
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}