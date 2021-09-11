// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWheatLegacy extends BlockCrops
{
    protected FCBlockWheatLegacy( int iBlockID )
    {
    	super( iBlockID );    	
    }
    
	@Override
    public boolean DoesBlockDropAsItemOnSaw( World world, int i, int j, int k )
    {
		return true;
    }
	
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
		// only retrieve if fully grown
    	
		if ( world.getBlockMetadata( i, j, k ) >= 7 )
		{
			return super.GetStackRetrievedByBlockDispenser( world, i, j, k );			
		}
    	
    	return null;
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
