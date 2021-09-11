// FCMOD

package net.minecraft.src;

public class FCRecipesWoolBlock extends ShapedRecipes
{
    public FCRecipesWoolBlock( int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack )
    {
    	super( par1, par2, par3ArrayOfItemStack, par4ItemStack );
    }
    
    @Override
    public ItemStack getCraftingResult( InventoryCrafting inventory )
    {
    	ItemStack resultStack = super.getCraftingResult( inventory );
    	
    	if ( resultStack != null  )
    	{
    		int iAverageColor = FCItemWool.AverageWoolColorsInGrid( inventory );
    		
    		int iClosestColorIndex = FCItemWool.GetClosestColorIndex( iAverageColor );
    		
    		resultStack.setItemDamage( BlockCloth.getBlockFromDye( iClosestColorIndex ) );    		
    	}
    	
    	return resultStack;
    }
    
}
