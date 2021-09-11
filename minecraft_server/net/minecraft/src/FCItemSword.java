// FCMOD

package net.minecraft.src;

public class FCItemSword extends ItemSword
{
    private final EnumToolMaterial m_material;

    public FCItemSword( int iItemID, EnumToolMaterial material )
    {
    	super( iItemID, material );
    	
    	m_material = material;
    	
    	if ( m_material == EnumToolMaterial.WOOD )
    	{
        	SetBuoyant();
        	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOD_TOOLS );        	
        	SetIncineratedInCrucible();
    	}
    	
    	SetInfernalMaxEnchantmentCost( m_material.GetInfernalMaxEnchantmentCost() );
    	SetInfernalMaxNumEnchants( m_material.GetInfernalMaxNumEnchants() );    	
    }
    
    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	return IsEfficientVsBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public float getStrVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
        if ( IsEfficientVsBlock( stack, world, block, i, j, k ) )
        {
            return 15.0F;
        }
        else
        {
            Material material = block.blockMaterial;
            
            if ( material == Material.plants || material == Material.vine || material == Material.coral || material != Material.leaves || material != Material.pumpkin )
            {
            	return 1.5F;
            }            
        }
    	
    	return super.getStrVsBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
        return block.blockID == Block.web.blockID || block.blockID == FCBetterThanWolves.fcBlockWeb.blockID;
    }
    
    @Override
    public boolean IsEnchantmentApplicable( Enchantment enchantment )
    {
    	if ( enchantment.type == EnumEnchantmentType.weapon )
    	{
    		return true;
    	}
    	
    	return super.IsEnchantmentApplicable( enchantment );
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			if ( m_material == EnumToolMaterial.WOOD )
			{
				player.playSound( "mob.zombie.woodbreak", 0.1F, 1.25F + ( world.rand.nextFloat() * 0.25F ) );
			}
			else if ( m_material == EnumToolMaterial.STONE )
			{
				player.playSound( "random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F );
			}
			else
			{
				player.playSound( "random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F );
			}			
		}
		
    	super.onCreated( stack, world, player );
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
