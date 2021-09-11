// FCMOD

package net.minecraft.src;

public class FCItemRefinedPickAxe extends FCItemPickaxe
{
    protected FCItemRefinedPickAxe( int i )
    {
        super( i, EnumToolMaterial.SOULFORGED_STEEL );
        
        setUnlocalizedName( "fcItemPickAxeRefined" );
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 0x11940;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        
        return itemstack;
    }

    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	if ( block != null && block.blockMaterial == FCBetterThanWolves.fcMaterialSoulforgedSteel )
    	{
    		return true;
    	}
    	
    	return super.canHarvestBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public float getStrVsBlock( ItemStack toolItemStack, World world, Block block, int i, int j, int k ) 
    {
    	if ( block != null && block.blockMaterial == FCBetterThanWolves.fcMaterialSoulforgedSteel )
    	{
            return efficiencyOnProperMaterial;
    	}
    	
    	return super.getStrVsBlock( toolItemStack, world, block, i, j, k );
    }
    
    @Override
    protected boolean GetCanBePlacedAsBlock()
    {
    	return false;
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}