// FCMOD

package net.minecraft.src;

public class FCEntityBlaze extends EntityBlaze
{
    public FCEntityBlaze( World world )
    {
        super( world );
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iLootingModifier )
    {
    	// treat as always killed by player to override vanilla behavior of only dropping rods
    	// when killed by player
    	
    	super.dropFewItems( true, iLootingModifier );
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 500 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, 
            	Enchantment.flame.effectId );
            
            entityDropItem( itemstack, 0F );
    	}
    }
}
