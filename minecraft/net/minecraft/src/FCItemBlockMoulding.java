// FCMOD

package net.minecraft.src;

public class FCItemBlockMoulding extends ItemBlock
{
    public FCItemBlockMoulding( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( Block.blocksList[getBlockID()].getUnlocalizedName() );
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
        return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("moulding").toString();
    }
}
