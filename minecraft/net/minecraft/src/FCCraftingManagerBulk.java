// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.ArrayList;

public abstract class FCCraftingManagerBulk
{
    private List<FCCraftingManagerBulkRecipe> m_recipes;
    
    protected FCCraftingManagerBulk()
    {
        m_recipes = new ArrayList<FCCraftingManagerBulkRecipe>();
    }   

    public void AddRecipe( ItemStack outputStack, ItemStack inputStacks[] )
    {
    	AddRecipe( outputStack, inputStacks, false );
    }
    
    public void AddRecipe( ItemStack outputStack, ItemStack inputStack )
    {
    	AddRecipe( outputStack, inputStack, false );
    }
    
    public void AddRecipe( ItemStack outputStacks[], ItemStack inputStacks[] )
    {
    	AddRecipe( outputStacks, inputStacks, false );
    }
    
    public void AddRecipe( ItemStack outputStack, ItemStack inputStacks[], boolean bMetadataExclusive )
    {
        ItemStack outputStacks[] = new ItemStack[1];
        
        outputStacks[0] = outputStack.copy();
        
        AddRecipe( outputStacks, inputStacks, bMetadataExclusive );        
    }
    
    public void AddRecipe( ItemStack outputStack, ItemStack inputStack, boolean bMetadataExclusive )
    {
        ItemStack outputStacks[] = new ItemStack[1];
        
        outputStacks[0] = outputStack.copy();
        
        ItemStack inputStacks[] = new ItemStack[1];
        
        inputStacks[0] = inputStack.copy();
        
        AddRecipe( outputStacks, inputStacks, bMetadataExclusive );        
    }
    
    public void AddRecipe( ItemStack outputStacks[], ItemStack inputStacks[], boolean bMetadataExclusive )
    {
    	FCCraftingManagerBulkRecipe recipe = CreateRecipe( outputStacks, inputStacks, bMetadataExclusive );        
    	
        m_recipes.add( recipe );
    }
    
    public boolean RemoveRecipe( ItemStack outputStack, ItemStack inputStacks[] )
    {
    	return RemoveRecipe( outputStack, inputStacks, false );
    }
    
    public boolean RemoveRecipe( ItemStack outputStack, ItemStack inputStack )
    {
    	return RemoveRecipe( outputStack, inputStack, false );
    }
    
    public boolean RemoveRecipe( ItemStack outputStacks[], ItemStack inputStacks[] )
    {
    	return RemoveRecipe( outputStacks, inputStacks, false );
    }
    
    public boolean RemoveRecipe( ItemStack outputStack, ItemStack inputStacks[], boolean bMetadataExclusive )
    {
        ItemStack outputStacks[] = new ItemStack[1];
        
        outputStacks[0] = outputStack.copy();
        
        return RemoveRecipe( outputStacks, inputStacks, bMetadataExclusive );        
    }
    
    public boolean RemoveRecipe( ItemStack outputStack, ItemStack inputStack, boolean bMetadataExclusive )
    {
        ItemStack outputStacks[] = new ItemStack[1];
        
        outputStacks[0] = outputStack.copy();
        
        ItemStack inputStacks[] = new ItemStack[1];
        
        inputStacks[0] = inputStack.copy();
        
        return RemoveRecipe( outputStacks, inputStacks, bMetadataExclusive );        
    }
    
    /*
     * Returns true if the recipe was successfully removed
     */
    public boolean RemoveRecipe( ItemStack outputStacks[], ItemStack inputStacks[], boolean bMetadataExclusive )
    {
    	FCCraftingManagerBulkRecipe recipe = CreateRecipe( outputStacks, inputStacks, bMetadataExclusive );
    	
    	int iMatchingIndex = GetMatchingRecipeIndex( recipe );
    	
    	if ( iMatchingIndex >= 0 )
    	{
    		m_recipes.remove( iMatchingIndex );
    		
    		return true;
    	}
    	
    	return false;
    }
    
    public List<ItemStack> GetCraftingResult( IInventory inventory )
    {
        for(int i = 0; i < m_recipes.size(); i++)
        {
        	FCCraftingManagerBulkRecipe tempRecipe = m_recipes.get( i );
        	
            if( tempRecipe.DoesInventoryContainIngredients( inventory ) )
            {
                return tempRecipe.getCraftingOutputList();
            }
        }
        
    	return null;
    }
    
    public List<ItemStack> GetCraftingResult( ItemStack inputStack )
    {
        for(int i = 0; i < m_recipes.size(); i++)
        {
        	FCCraftingManagerBulkRecipe tempRecipe = m_recipes.get( i );
        	
            if ( tempRecipe.DoesStackSatisfyIngredients( inputStack ) )
            {
                return tempRecipe.getCraftingOutputList();
            }
        }
        
    	return null;
    }
    
    public List<ItemStack> GetValidCraftingIngrediants( IInventory inventory )
    {
        for(int i = 0; i < m_recipes.size(); i++)
        {
        	FCCraftingManagerBulkRecipe tempRecipe = m_recipes.get( i );
        	
            if( tempRecipe.DoesInventoryContainIngredients( inventory ) )
            {
                return tempRecipe.getCraftingIngrediantList();
            }
        }
        
    	return null;
    }
    
    /**
     * Checks if any recipe is satisfied by the single input stack, and returns the required
     * ingredient stack if it does (null otherwise)
     */
    public ItemStack GetValidSingleIngredient( ItemStack inputStack )
    {    	
        for ( int i = 0; i < m_recipes.size(); i++ )
        {
        	FCCraftingManagerBulkRecipe tempRecipe = m_recipes.get( i );
        	
            if ( tempRecipe.DoesStackSatisfyIngredients( inputStack ) )
            {
                return tempRecipe.GetFirstIngredient();
            }
        }
        
    	return null;
    }
    
    public boolean HasRecipeForSingleIngredient( ItemStack inputStack )
    {
    	return GetValidSingleIngredient( inputStack ) != null;
    }
    
    public List<ItemStack> ConsumeIngrediantsAndReturnResult( IInventory inventory )
    {
        for(int i = 0; i < m_recipes.size(); i++)
        {
        	FCCraftingManagerBulkRecipe tempRecipe = m_recipes.get( i );
        	
            if( tempRecipe.DoesInventoryContainIngredients( inventory ) )
            {
            	tempRecipe.ConsumeInventoryIngrediants( inventory );
            	
                return tempRecipe.getCraftingOutputList();
            }
        }
        
    	return null;
    }
    
    private FCCraftingManagerBulkRecipe CreateRecipe( ItemStack outputStacks[], ItemStack inputStacks[], boolean bMetadataExclusive )
    {
        ArrayList<ItemStack> inputArrayList = new ArrayList<ItemStack>();
        
        int iInputStacksArrayLength = inputStacks.length;
        
        for ( int iTempIndex = 0; iTempIndex < iInputStacksArrayLength; iTempIndex++ )
        {
        	inputArrayList.add( inputStacks[iTempIndex].copy() );            
        }

        ArrayList<ItemStack> outputArrayList = new ArrayList<ItemStack>();
        
        int iOutputStacksArrayLength = outputStacks.length;
        
        for ( int iTempIndex = 0; iTempIndex < iOutputStacksArrayLength; iTempIndex++ )
        {
        	outputArrayList.add( outputStacks[iTempIndex].copy() );            
        }
        
        return new FCCraftingManagerBulkRecipe( outputArrayList, inputArrayList, bMetadataExclusive );
    }

    private int GetMatchingRecipeIndex( FCCraftingManagerBulkRecipe recipe )
    {
    	int iMatchingRecipeIndex = -1;
    	
        for ( int iIndex = 0; iIndex < m_recipes.size(); iIndex++ )
        {
            FCCraftingManagerBulkRecipe tempRecipe = m_recipes.get( iIndex );

            if ( tempRecipe.matches( recipe ) )
            {
                return iIndex;
            }            
        }

    	return -1;
    }
}