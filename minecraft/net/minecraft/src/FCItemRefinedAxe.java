// FCMOD

package net.minecraft.src;

public class FCItemRefinedAxe extends FCItemAxe
{
    protected FCItemRefinedAxe( int i )
    {
        super( i, EnumToolMaterial.SOULFORGED_STEEL );
        
        setUnlocalizedName( "fcItemHatchetRefined" );
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        
        return itemstack;
    }

    @Override
    protected boolean GetCanBePlacedAsBlock()
    {
    	return false;
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}