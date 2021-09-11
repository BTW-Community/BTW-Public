// FCMOD

package net.minecraft.src;

import java.util.Random;

//variant of WeightedRandomChestContent
public class FCUtilsRandomItemStack extends WeightedRandomItem
{
    private ItemStack m_stack = null;
    private int m_iMinStackSize;
    private int m_iMaxStackSize;

    public FCUtilsRandomItemStack( int iItemID, int iItemDamage, int iMinStackSize, int iMaxStackSize, int iWeight )
    {
        super( iWeight );
        
        m_stack = new ItemStack( iItemID, 1, iItemDamage );
        m_iMinStackSize = iMinStackSize;
        m_iMaxStackSize = iMaxStackSize;
    }
    
    public static ItemStack GetRandomStack( Random rand, FCUtilsRandomItemStack[] itemStackArray )
    {
    	FCUtilsRandomItemStack randItemStack = (FCUtilsRandomItemStack)WeightedRandom.getRandomItem( rand, itemStackArray );
    	
        int iStackSize = randItemStack.m_iMinStackSize + rand.nextInt( randItemStack.m_iMinStackSize - randItemStack.m_iMinStackSize + 1 );

        ItemStack newStack = randItemStack.m_stack.copy();
        newStack.stackSize = iStackSize;
        
        return newStack;
    }
}