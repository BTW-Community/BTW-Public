// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockDragonEgg extends BlockDragonEgg
{
    public FCBlockDragonEgg( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F );
        
        setCreativeTab( CreativeTabs.tabDecorations );
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        CheckForFall( world, i, j, k );
    }

	@Override
    public void OnBlockDestroyedLandingFromFall( World world, int i, int j, int k, int iMetadata )
    {
		dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
