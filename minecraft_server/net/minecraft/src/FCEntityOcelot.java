// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCEntityOcelot extends EntityOcelot
{
	private static final int m_iTabbySkinID = 22;
	
    public FCEntityOcelot( World world )
    {
        super( world );
        
        tasks.RemoveAllTasksOfClass( EntityAIWander.class );
        
        targetTasks.RemoveAllTasksOfClass( EntityAITargetNonTamed.class );
        
        tasks.addTask( 10, new FCEntityAIWanderSimple( this, 0.23F ) );
        
        targetTasks.addTask( 1, new EntityAITargetNonTamed( this, 
        	FCEntityChicken.class, 14F, 750, false ) );        
    }
    
    @Override
    public boolean interact( EntityPlayer player )
    {
    	boolean bWasTamed = isTamed();
    	
    	boolean bReturnValue = super.interact( player );
    	
    	if ( !worldObj.isRemote && !bWasTamed && isTamed() )
    	{
            if ( worldObj.rand.nextInt( 4 ) == 0 )
            {
                setTameSkin( m_iTabbySkinID ); // tabby
            }
    	}
    	
    	return bReturnValue;
    }
    
    @Override
    protected void CheckForLooseFood()
    {    
	    if ( !worldObj.isRemote && isEntityAlive() )
	    {
	    	boolean bAte = false;
	    	
	        List itemList = worldObj.getEntitiesWithinAABB( EntityItem.class, 
	        	boundingBox.expand( 2.5D, 1D, 2.5D ) );
	        
	        Iterator itemIterator = itemList.iterator();
	
	        while ( itemIterator.hasNext() )
	        {
	    		EntityItem itemEntity = (EntityItem)itemIterator.next();
	    		
		        if ( itemEntity.delayBeforeCanPickup == 0 && !itemEntity.isDead )
		        {
		        	ItemStack itemStack = itemEntity.getEntityItem();
		        	
		        	Item item = itemStack.getItem();
		        	
		        	if ( item.itemID == Item.chickenRaw.itemID || 
		        		item.itemID == Item.fishRaw.itemID )
		        	{
			            itemEntity.setDead();
			            
			            bAte = true;				            
		        	}
		        }
	        }
	        
	        if ( bAte )
	        {
	        	worldObj.playAuxSFX( FCBetterThanWolves.m_iBurpSoundAuxFXID, 
	        		MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), 
	        		MathHelper.floor_double( posZ ), 0 );
	        }
	    }
    }
    
    @Override
	public void OnNearbyAnimalAttacked( EntityAnimal attackedAnimal, EntityLiving attackSource )
	{
    	// wolves and ocelots don't give a shit if a nearby animal is attacked
	}
    
    @Override
	public void OnNearbyPlayerStartles( EntityPlayer player )
	{
    	// ignore fire start and block place/remove attempts
	}
    
	@Override
    public boolean IsAffectedByMovementModifiers()
    {
    	return false;
    }	
    
	@Override
    public void initCreature()
    {
        if ( worldObj.rand.nextInt( 7 ) == 0 )
        {
        	// spawn a couple of kittens nearby, as parent class does
        	
            for ( int iTempCount = 0; iTempCount < 2; ++iTempCount )
            {
                FCEntityOcelot kitten = new FCEntityOcelot( worldObj );
                
                kitten.setLocationAndAngles( posX, posY, posZ, rotationYaw, 0F );
                kitten.setGrowingAge( -kitten.GetTicksForChildToGrow() );
                
                worldObj.spawnEntityInWorld( kitten );
            }
        }
    }
	
	@Override
    public FCEntityOcelot spawnBabyAnimal( EntityAgeable otherParent )
    {
        FCEntityOcelot kitten = new FCEntityOcelot( worldObj );

        if ( isTamed() )
        {
            kitten.setOwner( getOwnerName() );
            kitten.setTamed( true );
            kitten.setTameSkin( getTameSkin() );
        }

        return kitten;
    }
    
    @Override
    protected int GetItemFoodValue( ItemStack stack )
    {
    	// class processes its own food values, so this prevents EntityAnimal from treating
    	// it like a herbivore.
    	
    	return 0;
    }
    
    @Override
    protected boolean IsTooHungryToHeal()
    {
    	// handles own healing independent of hunger
    	
    	return true; 
    }
    
    @Override
    public int GetMeleeAttackStrength( Entity target )
    {
    	return 3;
    }
    
    @Override
    public boolean attackEntityAsMob( Entity target )
    {
        return MeleeAttack( target ); // skip over parent
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}