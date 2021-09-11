// FCMOD

package net.minecraft.src;

public class FCItemMattock extends FCItemRefinedPickAxe
{
    protected FCItemMattock( int i )
    {
        super( i );
        
        setUnlocalizedName( "fcItemMattock" );
    }
    
    @Override
    public float getStrVsBlock( ItemStack itemStack, World world, Block block, int i, int j, int k ) 
    {
    	float pickStr = super.getStrVsBlock( itemStack, world, block, i, j, k );
    	float shovelStr = ((FCItemRefinedShovel)(FCBetterThanWolves.fcItemRefinedShovel)).getStrVsBlock( itemStack, world, block, i, j, k );
    	
    	if ( shovelStr > pickStr )
    	{
    		return shovelStr;
    	}
    	else
    	{
    		return pickStr;
    	}
    }

    @Override
    public boolean canHarvestBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	return super.canHarvestBlock( stack, world, block, i, j, k ) || 
    		((FCItemRefinedShovel)(FCBetterThanWolves.fcItemRefinedShovel)).canHarvestBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	return super.IsEfficientVsBlock( stack, world, block, i, j, k ) || 
    		((FCItemRefinedShovel)(FCBetterThanWolves.fcItemRefinedShovel)).IsEfficientVsBlock( stack, world, block, i, j, k );
    }    
}