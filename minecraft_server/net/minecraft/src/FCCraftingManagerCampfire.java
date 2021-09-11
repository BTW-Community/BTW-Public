// FCMOD

package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class FCCraftingManagerCampfire
{
	public static FCCraftingManagerCampfire instance = new FCCraftingManagerCampfire();
	
    private Map m_recipeMap = new HashMap();

    private FCCraftingManagerCampfire()
    {
    }

    public ItemStack GetRecipeResult( int iInputItemID )
    {
        return (ItemStack)m_recipeMap.get( iInputItemID );
    }

    public void AddRecipe( int iInputItemID, ItemStack outputStack )
    {
    	m_recipeMap.put( iInputItemID, outputStack );
    }
}
