// FCMOD

package net.minecraft.src;

public class FCItemBlockSidingAndCorner extends ItemBlock
{
    public FCItemBlockSidingAndCorner( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( Block.blocksList[getBlockID()].getUnlocalizedName() );
    }
    
    @Override
    public int getMetadata( int iItemDamage )
    {
        return iItemDamage;
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	if( itemstack.getItemDamage() == FCBlockSidingAndCornerAndDecorative.m_iSubtypeBench )
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("bench").toString();
    	}
    	else if( itemstack.getItemDamage() == FCBlockSidingAndCornerAndDecorative.m_iSubtypeFence )
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("fence").toString();
    	}
    	else if( itemstack.getItemDamage() == 0 )
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("siding").toString();
    	}
    	else
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("corner").toString();
    	}
    }
}
