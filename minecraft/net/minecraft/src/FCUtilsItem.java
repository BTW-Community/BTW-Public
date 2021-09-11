// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCUtilsItem
{
    static public void EjectStackWithRandomOffset( World world, int i, int j, int k, ItemStack stack )
    {
        float xOffset = world.rand.nextFloat() * 0.7F + 0.15F;
        float yOffset = world.rand.nextFloat() * 0.2F + 0.1F;
        float zOffset = world.rand.nextFloat() * 0.7F + 0.15F;
        
        EjectStackWithRandomVelocity( world, (float)i + xOffset, (float)j + yOffset, (float)k + zOffset, stack );
    }
    
    static public void EjectSingleItemWithRandomOffset( World world, int i, int j, int k, int iShiftedItemIndex, int iDamage )
    {
		ItemStack itemStack = new ItemStack( iShiftedItemIndex, 1, iDamage );
		
		EjectStackWithRandomOffset( world, i, j, k, itemStack );
    }
    
    static public void EjectStackWithRandomVelocity( World world, double xPos, double yPos, double zPos, ItemStack stack )
    {
        EntityItem entityitem = 
        	new EntityItem( world, xPos, yPos, zPos, 
    			stack );

        float velocityFactor = 0.05F;

        entityitem.motionX = (float)world.rand.nextGaussian() * velocityFactor;
        entityitem.motionY = (float)world.rand.nextGaussian() * velocityFactor + 0.2F;
        entityitem.motionZ = (float)world.rand.nextGaussian() * velocityFactor;
        
        entityitem.delayBeforeCanPickup = 10;
        
        world.spawnEntityInWorld( entityitem );
    }
    
    static public void EjectSingleItemWithRandomVelocity( World world, float xPos, float yPos, float zPos, int iShiftedItemIndex, int iDamage )
    {
		ItemStack itemStack = new ItemStack( iShiftedItemIndex, 1, iDamage );
		
		FCUtilsItem.EjectStackWithRandomVelocity( world, xPos, yPos, zPos, itemStack );
    }
    
    static public void DropStackAsIfBlockHarvested( World world, int i, int j, int k, ItemStack stack )
    {
    	// copied from dropBlockAsItemWithChance() in Block.java
    	
        float f1 = 0.7F;
        
        double d = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        
        EntityItem entityitem = new EntityItem( world, (double)i + d, (double)j + d1, (double)k + d2, stack );
        
        entityitem.delayBeforeCanPickup = 10;
        
        world.spawnEntityInWorld( entityitem );
    }
    
    static public void DropSingleItemAsIfBlockHarvested( World world, int i, int j, int k, int iShiftedItemIndex, int iDamage )
    {
		ItemStack itemStack = new ItemStack( iShiftedItemIndex, 1, iDamage );
		
		FCUtilsItem.DropStackAsIfBlockHarvested( world, i, j, k, itemStack );
    }
    
    static public void EjectStackAroundBlock( World world, int i, int j, int k, ItemStack stack )
    {
    	int iTempFacing = world.rand.nextInt( 6 );
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    	
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos();
    	
    	for ( int iTempFacingCount = 0; iTempFacingCount < 6; iTempFacingCount++ )
    	{    		
        	tempPos.Set( i, j, k );
        	tempPos.AddFacingAsOffset( iTempFacing );
        	
        	if ( FCUtilsWorld.IsReplaceableBlock( world, tempPos.i, tempPos.j, tempPos.k ) )
        	{
        		targetPos.Set( tempPos );
        		
        		break;
        	}
        	
    		iTempFacing++;
    		
    		if ( iTempFacing  >= 6 )
    		{    			
    			iTempFacing = 0;
    		}
    	}
    		
    	DropStackAsIfBlockHarvested( world, targetPos.i, targetPos.j, targetPos.k, stack );    
	}
    
    static public void EjectStackFromBlockTowardsFacing( World world, int i, int j, int k, ItemStack stack, int iFacing )
    {
    	Vec3 ejectPos = Vec3.createVectorHelper( 
    		world.rand.nextDouble() * 0.7D + 0.15D, 
    		1.2D + world.rand.nextDouble() * 0.1D, 
    		world.rand.nextDouble() * 0.7D + 0.15D );
    	
    	ejectPos.TiltAsBlockPosToFacingAlongJ( iFacing );
    	
        EntityItem entity = new EntityItem( world, i + ejectPos.xCoord, j + ejectPos.yCoord, k + ejectPos.zCoord, stack );

        if ( iFacing < 2 )
        {
            entity.motionX = world.rand.nextDouble() * 0.1D - 0.05D;
            entity.motionZ = world.rand.nextDouble() * 0.1D - 0.05D;

            if ( iFacing == 0 )
            {
            	entity.motionY = 0D;
            }
            else
            {
            	entity.motionY = 0.2D;
            }
        }
        else
        {
        	Vec3 ejectVel = Vec3.createVectorHelper( world.rand.nextDouble() * 0.1D - 0.05D, 
        		0.2D, world.rand.nextDouble() * -0.05D - 0.05D );
        	
        	ejectVel.RotateAsVectorAroundJToFacing( iFacing );
        	
            entity.motionX = ejectVel.xCoord;
            entity.motionY = ejectVel.yCoord;
            entity.motionZ = ejectVel.zCoord;
        }
        
        entity.delayBeforeCanPickup = 10;
        
        world.spawnEntityInWorld( entity );        
	}
    
	static public void GivePlayerStackOrEjectFromTowardsFacing( EntityPlayer player, ItemStack stack, int i, int j, int k, int iFacing )
	{
		if ( player.inventory.addItemStackToInventory( stack ) )
		{
            player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F, 
        		((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
		else if ( !player.worldObj.isRemote )			
		{			
			FCUtilsItem.EjectStackFromBlockTowardsFacing( player.worldObj, i, j, k, stack, iFacing );
		}
	}
	
	static public void GivePlayerStackOrEjectFavorEmptyHand( EntityPlayer player, ItemStack stack, int i, int j, int k )
	{
		if ( player.AddStackToCurrentHeldStackIfEmpty( stack ) )
		{
            player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F, 
        		((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
		else
		{
			GivePlayerStackOrEject( player, stack, i, j, k );
		}		
	}
	
	static public void GivePlayerStackOrEject( EntityPlayer player, ItemStack stack, int i, int j, int k )
	{
		if ( player.inventory.addItemStackToInventory( stack ) )
		{
            player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F, 
        		((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
		else if ( !player.worldObj.isRemote )			
		{			
			FCUtilsItem.EjectStackWithRandomOffset( player.worldObj, i, j, k, stack );
		}
	}
	
	static public void GivePlayerStackOrEject( EntityPlayer player, ItemStack stack )
	{
		if ( player.inventory.addItemStackToInventory( stack ) )
		{
            player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F, 
        		((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
		else if ( !player.worldObj.isRemote )			
		{			
			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, stack ); 
		}
	}	
}
