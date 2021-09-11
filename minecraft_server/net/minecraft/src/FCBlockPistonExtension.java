// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockPistonExtension extends BlockPistonExtension
{
    public FCBlockPistonExtension( int iBlockID )
    {
        super( iBlockID );
        
        SetPicksEffectiveOn( true );
    }

    @Override
    public AxisAlignedBB GetAsPistonMovingBoundingBox( World world, int i, int j, int k )
    {
    	// override to return full block bounding box to fix problem with piston heads 
    	// moving through items

    	return GetFixedBlockBoundsFromPool().offset( i, j, k );
    }
    
    @Override
    public boolean CanContainPistonPackingToFacing( World world, int i, int j, int k, int iFacing )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		return BlockPistonExtension.getDirectionMeta( iMetadata ) == iFacing;
    }
    
    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
        int iFacing = getDirectionMeta( world.getBlockMetadata( i, j, k ) );

        AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 0D, 0.75D, 0D, 1D, 1D, 1D );        
        tempBox.TiltToFacingAlongJ( iFacing );
        tempBox.offset( i, j, k );
        tempBox.AddToListIfIntersects( intersectingBox, list );
    		
        tempBox = AxisAlignedBB.getAABBPool().getAABB( 0.375D, 0D, 0.375D, 0.625D, 0.75D, 0.625D );        
        tempBox.TiltToFacingAlongJ( iFacing );
        tempBox.offset( i, j, k );
        tempBox.AddToListIfIntersects( intersectingBox, list );
    }
    
	@Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = getDirectionMeta( blockAccess.getBlockMetadata( i, j, k ) );

        AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 0D, 0.75F, 0D, 1D, 1D, 1D );
        tempBox.TiltToFacingAlongJ( iFacing );
        
        return tempBox;
    }
    
    @Override
    public boolean CanSupportFallingBlocks( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return getDirectionMeta( blockAccess.getBlockMetadata( i, j, k ) ) == 1;    	
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
    //------------- Class Specific Methods ------------//

	//----------- Client Side Functionality -----------//
}
