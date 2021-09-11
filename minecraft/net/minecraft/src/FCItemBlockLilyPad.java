//FCMOD 

package net.minecraft.src;

public class FCItemBlockLilyPad extends ItemLilyPad
{
    public FCItemBlockLilyPad( int iItemID )
    {
        super( iItemID );
    }
    
    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	// override to prevent block placement
    	
        return stack;
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	// override to prevent block placement
    	
        return false;
    }    
}