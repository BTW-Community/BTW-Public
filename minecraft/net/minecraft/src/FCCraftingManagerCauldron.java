// FCMOD

package net.minecraft.src;

public class FCCraftingManagerCauldron  extends FCCraftingManagerBulk
{
    private static final FCCraftingManagerCauldron instance = new FCCraftingManagerCauldron();
    
    public static final FCCraftingManagerCauldron getInstance()
    {
        return instance;
    }

    private FCCraftingManagerCauldron()
    {
    	super();    	
    }
}

