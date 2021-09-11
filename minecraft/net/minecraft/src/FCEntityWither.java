// FCMOD

package net.minecraft.src;

public class FCEntityWither extends EntityWither
{
    public FCEntityWither( World world )
    {
        super( world );
        
        tasks.RemoveAllTasksOfClass( EntityAIWander.class );
        
        tasks.addTask( 5, new FCEntityAIWanderSimple( this, moveSpeed ) );
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
        ItemStack stack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, 
        	Enchantment.knockback.effectId );
        
        entityDropItem( stack, 0F );
    }
    
    @Override
    protected void ModSpecificOnLivingUpdate()
    {
    	super.ModSpecificOnLivingUpdate();
    	
    	if ( !worldObj.isRemote )
    	{            
            FCUtilsWorld.GameProgressSetWitherHasBeenSummonedServerOnly();
    	}
    }
    
    //------------- Class Specific Methods ------------//
}
