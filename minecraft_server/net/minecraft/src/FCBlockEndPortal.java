// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockEndPortal extends BlockEndPortal
{
    protected FCBlockEndPortal( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 0.0625F, 1F );        
        
        setTickRandomly( true );
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
    	super.onBlockAdded( world, i, j, k );
    	
		FCUtilsWorld.GameProgressSetEndDimensionHasBeenAccessedServerOnly();
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	super.updateTick( world, i, j, k, rand );
    	
		FCUtilsWorld.GameProgressSetEndDimensionHasBeenAccessedServerOnly();
    }
    	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
