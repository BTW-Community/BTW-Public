// FCMOD

package net.minecraft.src;

public class FCItemAxe extends FCItemTool
{
    protected FCItemAxe( int iItemID, EnumToolMaterial material )
    {    	
        super( iItemID, 3, material );        
    }

    @Override
    public float getStrVsBlock( ItemStack toolItemStack, World world, Block block, int i, int j, int k ) 
    {
    	int iToolLevel = toolMaterial.getHarvestLevel();
    	int iBlockToolLevel = block.GetEfficientToolLevel( world, i, j, k ); 
    	
    	if ( iBlockToolLevel > iToolLevel )
    	{
        	return 1.0F;
    	}
    	
    	if ( block.GetIsProblemToRemove( world,  i, j, k ) )
    	{
    		// stumps and such
    		
    		return 1.0F;
    	}

    	return super.getStrVsBlock( toolItemStack, world, block, i, j, k );
    }
    
    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	int iToolLevel = toolMaterial.getHarvestLevel();
    	int iBlockToolLevel = block.GetHarvestToolLevel( world, i, j, k ); 
    	
    	if ( iBlockToolLevel > iToolLevel )
    	{
        	return false;
    	}
    	else if ( block.GetIsProblemToRemove( world,  i, j, k ) ) // stumps and such
    	{
			return false;
    	}    	
    	else if ( IsToolTypeEfficientVsBlockType( block ) )
    	{
    		return true;
    	}
        
        return super.canHarvestBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	int iToolLevel = toolMaterial.getHarvestLevel();
    	int iBlockToolLevel = block.GetEfficientToolLevel( world, i, j, k ); 
    	
    	if ( iBlockToolLevel > iToolLevel )
    	{
        	return false;
    	}    	
    	
    	if ( block.GetIsProblemToRemove( world,  i, j, k ) )
    	{
    		// stumps and such
    		
			return false;
    	}
    	
    	return super.IsEfficientVsBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public boolean IsConsumedInCrafting()
    {
    	return toolMaterial.getHarvestLevel() <= 2; // wood, stone, gold & iron
    }
    
    @Override
    public boolean IsDamagedInCrafting()
    {
    	return toolMaterial.getHarvestLevel() <= 2; // wood, stone, gold & iron
    }
    
    @Override
    public void OnUsedInCrafting( EntityPlayer player, ItemStack outputStack )
    {
		PlayChopSoundOnPlayer( player );
    }
    
    @Override
    public void OnBrokenInCrafting( EntityPlayer player )
    {
    	PlayBreakSoundOnPlayer( player );
    }

    @Override
    public boolean onBlockDestroyed( ItemStack stack, World world, int iBlockID, int i, int j, int k, EntityLiving destroyingEntityLiving )
    {
    	if ( !GetIsDamagedByVegetation() )
    	{
        	// override to prevent damage to non-stone axes when harvesting leaves or other vegetation

	    	Block block = Block.blocksList[iBlockID];
	    	
	    	if ( block != null && block.blockMaterial.GetAxesTreatAsVegetation() )
	    	{
    			return true;
	    	}
    	}
    
    	return super.onBlockDestroyed( stack, world, iBlockID, i, j, k, destroyingEntityLiving );
    }

    @Override
    public float GetExhaustionOnUsedToHarvestBlock( int iBlockID, World world, int i, int j, int k, int iBlockMetadata )
    {
    	if ( !GetConsumesHungerOnZeroHardnessVegetation() )
    	{
	    	Block block = Block.blocksList[iBlockID];
	    	
	    	if ( block != null )
	    	{
	            if ( (double)block.getBlockHardness( world, i, j, k ) == 0.0D ) // same code used to test this elsewhere
	            {
		    		if ( block.blockMaterial.GetAxesTreatAsVegetation() ) 
		    		{
		    			return 0F;
		    		}
	            }
	    	}
    	}
    	
    	return super.GetExhaustionOnUsedToHarvestBlock( iBlockID, world, i, j, k, iBlockMetadata );
    }
    
    @Override
    public boolean IsToolTypeEfficientVsBlockType( Block block )
    {
    	return block.blockMaterial.GetAxesEfficientOn() || block.AreAxesEffectiveOn();
    }
    
    @Override
    protected boolean CanToolStickInBlock( ItemStack stack, Block block, World world, int i, int j, int k )
    {
		if ( block.blockMaterial == FCBetterThanWolves.fcMaterialLog ||
			block.blockMaterial == FCBetterThanWolves.fcMaterialPlanks )
		{
			// ensures axe will stick in stumps and workbenches, despite not being efficient vs. them
			
			return true;
		}
    	
		return super.CanToolStickInBlock( stack, block, world, i, j, k );
    }
    
    @Override
    protected void PlayPlacementSound( ItemStack stack, Block blockStuckIn, World world, int i, int j, int k )
    {
    	world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F,
    		"mob.zombie.woodbreak", 0.25F, 1.25F + ( world.rand.nextFloat() * 0.25F ) );
    }
    
    //------------- Class Specific Methods ------------//
	
    public boolean GetConsumesHungerOnZeroHardnessVegetation()
    {
    	if ( this.toolMaterial.getHarvestLevel() <= 1 ) // wood, stone & gold
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean GetIsDamagedByVegetation()
    {
    	if ( this.toolMaterial.getHarvestLevel() <= 2 ) // wood, stone, gold, & iron
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    static public void PlayChopSoundOnPlayer( EntityPlayer player )
    {
		if ( player.m_iTimesCraftedThisTick == 0 )
		{
			// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
			
			player.playSound( "mob.zombie.wood", 0.5F, 2.5F );
		}
    }
    
    static public void PlayBreakSoundOnPlayer( EntityPlayer player )
    {
		// note: the playSound function for player both plays the sound locally on the client, and plays it remotely on the server without it being sent again to the same player
    	
		player.playSound( "random.break", 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F );
    }
    
	//----------- Client Side Functionality -----------//
}
