// FCMOD

package net.minecraft.src;

public class FCRecipesKnitting implements IRecipe
{
	// temporary variables used while processing recipes.  
	private ItemStack m_tempStackNeedles;
	private ItemStack m_tempStackWool;
	private ItemStack m_tempStackWool2;
	
    public boolean matches( InventoryCrafting inventory, World world )
    {
    	return CheckForIngredients( inventory );    	
    }

    public ItemStack getCraftingResult( InventoryCrafting inventory )
    {
    	if ( CheckForIngredients( inventory ) )
    	{    		
            ItemStack resultStack = new ItemStack( FCBetterThanWolves.fcItemKnitting.itemID, 1, 
            	FCItemKnitting.m_iDefaultMaxDamage - 1  );
            
            // messed up mixing in order to match color of final wool knit output that uses only 16 colors
    		int iWoolColor = FCItemWool.m_iWoolColors[FCItemWool.GetClosestColorIndex( FCItemWool.AverageWoolColorsInGrid( inventory ) )];

            FCItemKnitting.SetColor( resultStack, iWoolColor );
    	
        	return resultStack;
        }

    	return null;
    }

    @Override
    public int getRecipeSize()
    {
        return 3;
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
    
    private boolean CheckForIngredients( InventoryCrafting inventory )
    {
    	m_tempStackNeedles = null;
    	m_tempStackWool = null;
    	m_tempStackWool2 = null;
    	
        for ( int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++ )
        {
            ItemStack tempStack = inventory.getStackInSlot( iTempSlot );

            if (tempStack != null)
            {
                if ( tempStack.itemID == FCBetterThanWolves.fcItemKnittingNeedles.itemID )
                {
                	if ( m_tempStackNeedles == null )
                	{
                		m_tempStackNeedles = tempStack;
                	}
                	else
                	{
                		return false;
                	}
                }
                else if ( tempStack.itemID == FCBetterThanWolves.fcItemWool.itemID )
                {
                    if ( m_tempStackWool == null )
                    {
                    	m_tempStackWool = tempStack;
                    }
                    else if ( m_tempStackWool2 == null )
                    {
                    	m_tempStackWool2 = tempStack;
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

        return m_tempStackNeedles != null && m_tempStackWool != null && m_tempStackWool2 != null;
    }    
}
