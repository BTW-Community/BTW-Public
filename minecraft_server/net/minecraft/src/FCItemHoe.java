// FCMOD

package net.minecraft.src;

public class FCItemHoe extends FCItemTool
{
    protected EnumToolMaterial theToolMaterial;

    public FCItemHoe( int iItemID, EnumToolMaterial material )
    {
        super( iItemID, 1, material );        
        
        if ( material.getHarvestLevel() <= 2 ) // iron or worse
        {
        	efficiencyOnProperMaterial /= 8;
        }
        else
        {
        	efficiencyOnProperMaterial /= 4;
        }
    }

    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	// hoes don't harvest, they only convert
    	
    	return false;
    }
    
    @Override
    public boolean IsToolTypeEfficientVsBlockType( Block block )
    {
    	return block.AreHoesEffectiveOn();
    }

    @Override
    protected boolean CanToolStickInBlock( ItemStack stack, Block block, World world, int i, int j, int k )
    {
		return super.CanToolStickInBlock( stack, block, world, i, j, k ) ||
			block.AreShovelsEffectiveOn();			
    }
    
    @Override
    protected float GetVisualVerticalOffsetAsBlock()
    {
    	return 0.80F;
    }    
    
    @Override
    protected float GetBlockBoundingBoxHeight()
    {
    	return 0.79F;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
