// FCMOD

package net.minecraft.src;

public class FCItemChisel extends FCItemTool
{	
    protected FCItemChisel( int iItemID, EnumToolMaterial toolMaterial, int iNumUses )
    {
        super( iItemID, 1, toolMaterial );
        
        setMaxDamage( iNumUses );
        
        damageVsEntity = 1; // chisels don't do any more damage to mobs than any regular (non-tool) item
    }
    
    @Override
    public boolean hitEntity( ItemStack stack, EntityLiving defendingEntity, EntityLiving attackingEntity )
    {
    	// chisels don't lose durability when used to hit entities, as they don't do any extra damage
    	
        return false;
    }    
    
    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	// chisels can't normally harvest blocks whole unless the block is explicitly set for it (like with webs)
    	
    	if ( block.CanChiselsHarvest() )
    	{
	    	return toolMaterial.getHarvestLevel() >= block.GetHarvestToolLevel( world, i, j, k );
    	}
    	
    	return false;
    }
    
    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	if ( block.GetIsProblemToRemove( world,  i, j, k ) )
    	{
    		// stumps and such
    		
    		return false;
    	}

    	int iToolLevel = toolMaterial.getHarvestLevel();
    	int iBlockToolLevel = block.GetEfficientToolLevel( world, i, j, k ); 
    	
    	if ( iBlockToolLevel > iToolLevel )
    	{
        	return false;
    	}
    	
    	return block.AreChiselsEffectiveOn( world, i, j, k );
    }
    
    @Override
    public int getItemEnchantability()
    {
    	// prevent chisels being enchanted like other tools
    	
        return 0;
    }
    
    @Override
    protected boolean IsToolTypeEfficientVsBlockType( Block block )
    {
    	return block.AreChiselsEffectiveOn();
    }
    
    @Override
    protected boolean GetCanBePlacedAsBlock()
    {
    	return false;
    }
    
    @Override
    protected void PlayPlacementSound( ItemStack stack, Block blockStuckIn, World world, int i, int j, int k )
    {
    	if ( ((FCItemTool)pickaxeIron).IsToolTypeEfficientVsBlockType( blockStuckIn ) )
    	{
	    	world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F,
	    		"random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F );
    	}
    	else if ( ((FCItemTool)axeIron).IsToolTypeEfficientVsBlockType( blockStuckIn ) ||
    		blockStuckIn.blockMaterial == FCBetterThanWolves.fcMaterialLog ||
    		blockStuckIn.blockMaterial == FCBetterThanWolves.fcMaterialPlanks )
    	{
        	world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F,
        		"mob.zombie.woodbreak", 0.25F, 1.25F + ( world.rand.nextFloat() * 0.25F ) );
    	}
    	else
    	{
    		super.PlayPlacementSound( stack, blockStuckIn, world, i, j, k );
    	}
    }
    
    @Override
    protected float GetVisualVerticalOffsetAsBlock()
    {
    	return 0.45F;
    }
    
    @Override
    protected float GetBlockBoundingBoxHeight()
    {
    	return 0.3F;
    }
    
    protected float GetBlockBoundingBoxWidth()
    {
    	return 0.5F;
    }
}
