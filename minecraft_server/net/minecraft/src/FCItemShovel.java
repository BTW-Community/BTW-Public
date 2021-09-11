// FCMOD

package net.minecraft.src;

public class FCItemShovel extends FCItemTool
{
    public FCItemShovel( int iItemID, EnumToolMaterial material )
    {
        super( iItemID, 1, material );
    }

    protected FCItemShovel( int iItemID, EnumToolMaterial material, int iMaxUses )
    {
        super( iItemID, 1, material );
        
        setMaxDamage( iMaxUses );
    }

    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
        return IsToolTypeEfficientVsBlockType( block );
    }
    
    @Override
    public boolean IsToolTypeEfficientVsBlockType( Block block )
    {
    	return block.AreShovelsEffectiveOn();
    }
    
    @Override
    protected float GetVisualVerticalOffsetAsBlock()
    {
    	return 0.70F;
    }    
    
    @Override
    protected float GetVisualRollOffsetAsBlock()
    {
    	return -15F;
    }
}
