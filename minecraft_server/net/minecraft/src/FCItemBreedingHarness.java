// FCMOD

package net.minecraft.src;

public class FCItemBreedingHarness extends Item
{
    public FCItemBreedingHarness( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
    	
    	setUnlocalizedName( "fcItemHarnessBreeding" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    //public boolean itemInteractionForEntity( ItemStack itemStack, EntityLiving targetEntity )
    public boolean useItemOnEntity( ItemStack itemStack, EntityLiving targetEntity )
    {
    	if ( targetEntity instanceof EntityAnimal )
    	{
	    	EntityAnimal animal = (EntityAnimal)targetEntity;
	
        	if ( !animal.isChild() && !animal.getWearingBreedingHarness() )
        	{
		        if ( targetEntity instanceof FCEntityCow )
		    	{
	            	animal.setDead();
	            	
		            if ( !animal.worldObj.isRemote )
		            {
		                FCEntityCow cow = new FCEntityCow(animal.worldObj);
		                cow.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, animal.rotationYaw, animal.rotationPitch);
		                cow.setEntityHealth(animal.getHealth());
		                cow.renderYawOffset = animal.renderYawOffset;
		                animal.worldObj.spawnEntityInWorld(cow);
		                
		                animal = cow;
		            }
		            else
		            {
		                animal.worldObj.spawnParticle("largeexplode", animal.posX, animal.posY + (double)(animal.height / 2.0F), animal.posZ, 0.0D, 0.0D, 0.0D);
		            }
		    	}
		        else if ( targetEntity instanceof FCEntitySheep )
		        {		        	
		            if ( !animal.worldObj.isRemote )
		            {
		            	FCEntitySheep sheep = (FCEntitySheep)animal;
		            	
		            	sheep.setSheared( true );
		            }
		        }
		        else if ( !( targetEntity instanceof FCEntityPig ) )
		        {
		        	return false;
		        }
		        
	            itemStack.stackSize--;

	            if ( !animal.worldObj.isRemote )
	            {
	            	animal.setWearingBreedingHarness( true );
	            }
	        	
	            return true;
        	}
    	}
        
        return false;
    }
}