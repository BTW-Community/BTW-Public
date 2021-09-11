// FCMOD

package net.minecraft.src;

public class FCBlockLilyPad extends BlockLilyPad
{
    protected FCBlockLilyPad( int iBlockID )
    {
        super(iBlockID);
        
        SetBuoyant();
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 0.015625D, 1D );
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }
    
    @Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	return world.getBlockId( i, j, k ) == Block.waterStill.blockID;
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return false;
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
