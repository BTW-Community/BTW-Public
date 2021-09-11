// FCMOD

package net.minecraft.src;

public class FCItemChiselStone extends FCItemChisel
{	
    protected FCItemChiselStone( int iItemID )
    {
        super( iItemID, EnumToolMaterial.STONE, 8 );
        
        SetFilterableProperties( Item.m_iFilterable_Small );
        
        efficiencyOnProperMaterial /= 2;
        
        setUnlocalizedName( "fcItemChiselStone" );        
    }
    
    @Override
    public float getStrVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	float fStrength = super.getStrVsBlock( stack, world, block, i, j, k );
    	
    	if ( block.blockID == Block.web.blockID || block.blockID == FCBetterThanWolves.fcBlockWeb.blockID )
    	{
    		// boost stone chisels against webs so that it reads a little better
    		
    		fStrength *= 2;
    	}
    	
    	return fStrength;
    }
}
