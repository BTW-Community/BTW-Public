// FCMOD

package net.minecraft.src;

public class FCRecipesArmorWool extends ShapedRecipes
{
    public FCRecipesArmorWool( int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack )
    {
    	super( par1, par2, par3ArrayOfItemStack, par4ItemStack );
    }
    
    @Override
    public ItemStack getCraftingResult( InventoryCrafting inventory )
    {
    	ItemStack resultStack = super.getCraftingResult( inventory );
    	
    	if ( resultStack != null && resultStack.getItem() instanceof FCItemArmorWool )
    	{
    		int iAverageColor = FCItemWool.AverageWoolColorsInGrid( inventory );
    		
    		FCItemArmorWool woolArmorItem = (FCItemArmorWool)resultStack.getItem();
    		
            woolArmorItem.func_82813_b( resultStack, iAverageColor );
    	}
    	
    	return resultStack;
    }    
}
