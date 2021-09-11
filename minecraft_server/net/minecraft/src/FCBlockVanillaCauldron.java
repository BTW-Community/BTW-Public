// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockVanillaCauldron extends BlockCauldron
{
    public FCBlockVanillaCauldron( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 1F, 1F );
    }
    
    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
    	// parent method is super complicated for no apparent reason
    	
        AxisAlignedBB tempBox = getCollisionBoundingBoxFromPool( world, i, j, k );
    	
    	tempBox.AddToListIfIntersects( intersectingBox, list );    	
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
