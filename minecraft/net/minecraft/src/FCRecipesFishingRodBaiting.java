// FCMOD

package net.minecraft.src;

import java.util.ArrayList;

public class FCRecipesFishingRodBaiting implements IRecipe
{
    public boolean matches(InventoryCrafting craftingInventory, World world )
    {
        ItemStack rodStack = null;
        ItemStack baitStack = null;

        for ( int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); ++iTempSlot )
        {
            ItemStack tempStack = craftingInventory.getStackInSlot( iTempSlot );

            if (tempStack != null)
            {
                if ( tempStack.itemID == Item.fishingRod.itemID )
                {
                	if ( rodStack == null )
                	{
                		rodStack = tempStack;
                	}
                	else
                	{
                		return false;
                	}
                }
                else if ( IsFishingBait( tempStack ) )
                {
                    if ( baitStack == null )
                    {
                        baitStack = tempStack;
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

        return rodStack != null && baitStack != null;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult( InventoryCrafting craftingInventory )
    {
        ItemStack resultStack = null;
        
        ItemStack rodStack = null;
        ItemStack baitStack = null;
        
        for ( int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); ++iTempSlot )
        {
            ItemStack tempStack = craftingInventory.getStackInSlot( iTempSlot );

            if ( tempStack != null )
            {
                if ( tempStack.itemID == Item.fishingRod.itemID )
                {
                	if ( rodStack == null )
                	{
                		rodStack = tempStack;
                		
                        resultStack = tempStack.copy();
                        resultStack.stackSize = 1;
                        resultStack.itemID = FCBetterThanWolves.fcItemFishingRodBaited.itemID;
                	}
                	else
                	{
                		return null;
                	}
                }
                else if ( IsFishingBait( tempStack ) )
                {
                    if ( baitStack == null )
                    {
                        baitStack = tempStack;
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

        if ( baitStack != null && rodStack != null )
        {
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
    	return false;
    }
    
	//------------- Class Specific Methods ------------//
    
    private boolean IsFishingBait( ItemStack stack )
    {
    	int iItemID = stack.itemID;
    		
    	if ( iItemID == FCBetterThanWolves.fcItemCreeperOysters.itemID ||
    		iItemID == FCBetterThanWolves.fcItemBatWing.itemID ||
    		iItemID == FCBetterThanWolves.fcItemWitchWart.itemID ||
    		iItemID == Item.spiderEye.itemID || 
    		iItemID == Item.rottenFlesh.itemID )
    	{
    		return true;
    	}
    	
    	return false;
    }    
}
