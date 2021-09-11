// FCMOD

package net.minecraft.src;

public class FCItemRefinedShovel extends FCItemShovel
{
    protected FCItemRefinedShovel( int iItemID )
    {
        super( iItemID, EnumToolMaterial.SOULFORGED_STEEL );
        
        setUnlocalizedName( "fcItemShovelRefined" );        
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
    protected boolean GetCanBePlacedAsBlock()
    {
    	return false;
    }    

    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}