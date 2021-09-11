// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockEndPortalFrame extends BlockEndPortalFrame
{
    public FCBlockEndPortalFrame( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 0.8125F, 1F );
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
        AxisAlignedBB tempBox = getCollisionBoundingBoxFromPool( world, i, j, k );

    	tempBox.AddToListIfIntersects( intersectingBox, list );
    	
        if ( isEnderEyeInserted( world.getBlockMetadata( i, j, k ) ) )
        {
        	tempBox = AxisAlignedBB.getAABBPool().getAABB( 
        		0.3125F, 0.8125F, 0.3125F, 
        		0.6875F, 1.0F, 0.6875F ).
        		offset( i, j, k );
            
        	tempBox.AddToListIfIntersects( intersectingBox, list );
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( 
    		world, i, j, k, startRay, endRay );
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( GetFixedBlockBoundsFromPool() );
        
        if ( isEnderEyeInserted( world.getBlockMetadata( i, j, k ) ) )
        {
	    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( 
	    		0.25F, 0.8125F, 0.25F, 
	    		0.75F, 1.0F, 0.75F );
        }
    	
    	return rayTrace.GetFirstIntersection();    	
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	return renderer.RenderBlockEndPortalFrame( this, i, j, k );
    }    
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	if ( iSide != 1 )
    	{
            return !blockAccess.isBlockOpaqueCube( i, j, k );    		
    	}
    	
    	return true;
    }
}
