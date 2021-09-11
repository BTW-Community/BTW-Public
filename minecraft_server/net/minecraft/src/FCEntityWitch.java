// FCMOD

package net.minecraft.src;

public class FCEntityWitch extends EntityWitch
{
    private static final int[] m_itemDrops = new int[] {
    	FCBetterThanWolves.fcItemWitchWart.itemID, 
    	Item.glassBottle.itemID, 
    	Item.stick.itemID };
    
    public FCEntityWitch( World world )
    {
        super( world );
        
        tasks.RemoveAllTasksOfClass( EntityAIWander.class );
        
        tasks.addTask( 2, new FCEntityAIWanderSimple( this, moveSpeed ) );
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iLootingModifier )
    {
        int iNumDrops = rand.nextInt( 3 ) + 1;

        for ( int iTempCount = 0; iTempCount < iNumDrops; ++iTempCount )
        {
            int iItemID = m_itemDrops[rand.nextInt( m_itemDrops.length )];

            int iNumItems = rand.nextInt( 3 );
            
            if ( iLootingModifier > 0 )
            {
                iNumItems += rand.nextInt( iLootingModifier + 1 );
            }

            for ( ; iNumItems > 0; iNumItems-- )
            {
                dropItem( iItemID, 1 );
            }
        }
    }
    
    @Override
    public boolean getCanSpawnHere()
    {
    	// limit witch spawns to above and around sea level
    	
    	if ( (int)posY >= worldObj.provider.getAverageGroundLevel() - 5 )
    	{
    		return super.getCanSpawnHere();
    	}
    	
    	return false;
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 1000 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, Enchantment.aquaAffinity.effectId );
            
            entityDropItem(itemstack, 0.0F);
    	}
    }
 
    @Override
    public void playLivingSound()
    {
        String var1 = "mob.ghast.affectionate scream";

        if (var1 != null)
        {
            playSound(var1, this.getSoundVolume() * 0.25F, 0.5F + rand.nextFloat() * 0.25F );
        }
    }
    
    @Override
    public void attackEntityWithRangedAttack( EntityLiving target, float fDamageModifier )
    {
        if ( !IsConsumingPotion() ) 
        {
        	super.attackEntityWithRangedAttack( target, fDamageModifier );
        	
            worldObj.playSoundAtEntity( this, "mob.wither.shoot", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
        }
    }
    
    /**
     * This method is misnamed in that it actually sets whether the witch is in the process of 
     * consuming a potion, and has nothing to do with agression.
     */
    @Override
    public void setAggressive( boolean bIsConsumingPotion )
    {
    	super.setAggressive( bIsConsumingPotion );
    	
    	if ( bIsConsumingPotion )
    	{
    		// sound when witch starts drinking a potion
    		
            worldObj.playSoundAtEntity( this, "mob.wither.shoot", 0.5F, 
            	0.4F / ( rand.nextFloat() * 0.4F + 0.8F ) );
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
	/** 
	 * getAgressive() is misnamed in the parent.  This function for clarity
	 */    	
    public boolean IsConsumingPotion()
    {
    	return getAggressive();
    }
}