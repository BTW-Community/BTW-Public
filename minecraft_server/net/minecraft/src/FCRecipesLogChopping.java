// FCMOD

package net.minecraft.src;

import java.util.ArrayList;

public class FCRecipesLogChopping implements IRecipe
{
    public boolean matches( InventoryCrafting craftingInventory, World world )
    {
        ItemStack axeStack = null;
        ItemStack logStack = null;

        for ( int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); ++iTempSlot )
        {
            ItemStack tempStack = craftingInventory.getStackInSlot( iTempSlot );

            if ( tempStack != null )
            {
                if ( IsAxe( tempStack ) )
                {
                	if ( axeStack == null )
                	{
                		axeStack = tempStack;
                	}
                	else
                	{
                		return false;
                	}
                }
                else if ( IsLog( tempStack ) )
                {
                    if ( logStack == null )
                    {
                        logStack = tempStack;
                    }
                    else
                    {
                    	return false;
                    }
                }
                else
                {
                	return false;
                }
            }
        }

        return axeStack != null && logStack != null;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult( InventoryCrafting craftingInventory )
    {
        ItemStack axeStack = null;
        ItemStack logStack = null;
        
        for ( int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); ++iTempSlot )
        {
            ItemStack tempStack = craftingInventory.getStackInSlot( iTempSlot );

            if ( tempStack != null )
            {
                if ( IsAxe( tempStack ) )
                {
                	if ( axeStack == null )
                	{
                		axeStack = tempStack;
                	}
                	else
                	{
                		return null;
                	}
                }
                else if ( IsLog( tempStack ) )
                {
                    if ( logStack == null )
                    {
                        logStack = tempStack;                        
                    }
                    else
                    {
                    	return null;
                    }
                }
                else
                {
                	return null;
                }
            }
        }

        if ( logStack != null && axeStack != null )
        {
            ItemStack resultStack = null;
            FCItemAxe axeItem = (FCItemAxe)axeStack.getItem();

            if ( axeItem.toolMaterial.getHarvestLevel() <= 1 ) // wood, stone & gold
            {
            	resultStack = new ItemStack( Item.stick, 2 );
            }
            else
            {
	            int iLogID = logStack.itemID;
	            
	            if ( iLogID == FCBetterThanWolves.fcBloodWood.blockID )
	            {
	            	resultStack = new ItemStack( Block.planks.blockID, 2, 4 );
	            }
	            else
	            {
	            	resultStack = new ItemStack( Block.planks.blockID, 2, logStack.getItemDamage() );
	            }
            }
            
        	return resultStack;
        }
        
    	return null;
    }

    @Override
    public int getRecipeSize()
    {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return null;
    }

    @Override
    public boolean matches( IRecipe recipe )
    {
    	return false;
    }
    
    @Override
    public boolean HasSecondaryOutput()
    {
    	return true;
    }
    
	//------------- Class Specific Methods ------------//
    
    private boolean IsAxe( ItemStack stack )
    {
    	int iItemID = stack.itemID;
    		
    	if ( stack.getItem() instanceof FCItemAxe )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean IsLog( ItemStack stack )
    {
    	int iItemID = stack.itemID;
    		
    	if ( iItemID == FCBetterThanWolves.fcBloodWood.blockID ||
    		iItemID == Block.wood.blockID )
    	{
    		return true;
    	}
    	
    	return false;
    }    
}
