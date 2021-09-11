// FCMOD

package net.minecraft.src;

public class FCItemShears extends ItemShears
{
    public FCItemShears( int iItemID )
    {
    	super( iItemID );
    	
		SetInfernalMaxEnchantmentCost( 25 );
		SetInfernalMaxNumEnchants( 2 );
    }
    
    @Override
    public boolean onBlockDestroyed( ItemStack stack, World world, int iBlockID, int i, int j, int k, EntityLiving usingEntity )
    {
        if ( iBlockID != Block.leaves.blockID && 
        	iBlockID != FCBetterThanWolves.fcBlockBloodLeaves.blockID &&
        	iBlockID != Block.web.blockID && 
        	iBlockID != FCBetterThanWolves.fcBlockWeb.blockID &&
        	iBlockID != Block.tallGrass.blockID && 
        	iBlockID != Block.vine.blockID && 
        	iBlockID != Block.tripWire.blockID &&
        	iBlockID != FCBetterThanWolves.fcBlockHempCrop.blockID )
        {
            return super.onBlockDestroyed(stack, world, iBlockID, i, j, k, usingEntity);
        }
        else
        {
            stack.damageItem( 1, usingEntity );
            return true;
        }
    }
    
    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
        return block.blockID == Block.web.blockID || 
        	block.blockID == FCBetterThanWolves.fcBlockWeb.blockID || 
        	block.blockID == Block.redstoneWire.blockID || 
        	block.blockID == Block.tripWire.blockID;
    }
    
    @Override
    public float getStrVsBlock( ItemStack stack, World world, Block block, int i, int j, int k ) 
    {
    	if ( IsEfficientVsBlock( stack, world, block, i, j, k ) )
		{
        	if ( block.blockID == FCBetterThanWolves.fcBlockBloodLeaves.blockID || 
        		block.blockID == Block.leaves.blockID ||
        		block.blockID == Block.web.blockID || 
        		block.blockID == FCBetterThanWolves.fcBlockWeb.blockID )
        	{
                return 15F;
        	}
        	else
        	{
        		return 5F;
        	}
		}
    	
    	return super.getStrVsBlock( stack, world, block, i, j, k );
    }    
    
    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	if ( !block.blockMaterial.isToolNotRequired() )
    	{
    		if ( canHarvestBlock( stack, world, block, i, j, k ) )
    		{
    			return true;
    		}
    	}
    	
    	if ( block == Block.cloth || 
    		block == Block.leaves || 
    		block == FCBetterThanWolves.fcBlockBloodLeaves || 
    		block == FCBetterThanWolves.fcWoolSlab || 
    		block == FCBetterThanWolves.fcBlockWoolSlabTop ||
    		block == Block.vine ||
    		block == FCBetterThanWolves.fcBlockHempCrop )
    	{
    		return true;
    	}
    	
        return false;
    }
  
    @Override
    public boolean IsDamagedInCrafting()
    {
    	return true;
    }
    
    @Override
    public void OnDamagedInCrafting( EntityPlayer player )
    {
		// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
    	
		if ( player.m_iTimesCraftedThisTick == 0 )
		{
			player.playSound("mob.sheep.shear", 0.8F, 1.0F);
		}
    }
    
    @Override
    public void OnBrokenInCrafting( EntityPlayer player )
    {
		// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
    	
		player.playSound("random.break", 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			player.playSound( "random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F );
		}
		
    	super.onCreated( stack, world, player );
    }    
}
