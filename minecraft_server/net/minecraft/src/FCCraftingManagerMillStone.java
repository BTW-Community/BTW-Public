// FCMOD

package net.minecraft.src;

public class FCCraftingManagerMillStone  extends FCCraftingManagerBulk
{
    private static final FCCraftingManagerMillStone instance = new FCCraftingManagerMillStone();
    
    public static final FCCraftingManagerMillStone getInstance()
    {
        return instance;
    }

    private FCCraftingManagerMillStone()
    {
    	super();    	
    }
}