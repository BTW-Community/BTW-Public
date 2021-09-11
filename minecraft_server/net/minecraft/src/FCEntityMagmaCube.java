// FCMOD

package net.minecraft.src;

public class FCEntityMagmaCube extends EntityMagmaCube
{
    public FCEntityMagmaCube( World world )
    {
        super( world );
        
        landMovementFactor = 0.5F; // unifying client and server values in parent
    }
    
    @Override
    protected boolean canDamagePlayer()
    {
    	return isEntityAlive() && attackTime <= 0;
    }

    @Override
    public void CheckForScrollDrop()
    {    	
        if ( getSlimeSize() == 1 && rand.nextInt( 250 ) == 0 )
        {
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, 
            	Enchantment.fireAspect.effectId );
            
            entityDropItem( itemstack, 0F );
        }
    }
    
    @Override
    protected EntitySlime createInstance()
    {
        return new FCEntityMagmaCube( worldObj );
    }

    //------------- Class Specific Methods ------------//
}
