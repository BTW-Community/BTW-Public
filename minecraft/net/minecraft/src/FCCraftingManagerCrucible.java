// FCMOD

package net.minecraft.src;

public class FCCraftingManagerCrucible extends FCCraftingManagerBulk
{
    private static final FCCraftingManagerCrucible instance = new FCCraftingManagerCrucible();
    
    public static final FCCraftingManagerCrucible getInstance()
    {
        return instance;
    }

    private FCCraftingManagerCrucible()
    {
    	super();    	
    }
}
