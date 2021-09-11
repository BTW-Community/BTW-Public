// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCItemEnchantedBook extends ItemEnchantedBook
{
    public FCItemEnchantedBook( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();        
    }
    
    @Override
    public ItemStack onItemRightClick( ItemStack itemStack, World world, EntityPlayer player )
    {
        if ( world.isRemote )
        {
        	player.addChatMessage( "The language of this text is unfamiliar to you" );
        }
        
    	return itemStack;
    }
    
    @Override
    public void InitializeStackOnGiveCommand( Random rand, ItemStack stack )
    {
    	// duplicates enchantment assignment as if this came from a dungeon chest (test code)
    	/*
        Enchantment assignedEnchantment = Enchantment.field_92090_c[rand.nextInt(Enchantment.field_92090_c.length)];
        
        int iEnchantLevel = MathHelper.getRandomIntegerInRange( rand, assignedEnchantment.getMinLevel(), assignedEnchantment.getMaxLevel() );
        
        func_92115_a( stack, new EnchantmentData( assignedEnchantment, iEnchantLevel ) );
        */
    }
}
