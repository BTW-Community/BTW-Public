// FCMOD

package net.minecraft.src;

public class FCBlockGroundCover extends Block
{
	public static final float m_fVisualHeight = 0.125F;

    protected FCBlockGroundCover( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 0.125F, 1F);
        
        setHardness( 0.1F );
        SetShovelsEffectiveOn();
        
        SetBuoyant();
        
        setLightOpacity( 0 );        
        
        setCreativeTab( CreativeTabs.tabDecorations );
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	Block blockBelow = Block.blocksList[iBlockBelowID];
    	
    	if ( blockBelow != null )
    	{
    		return blockBelow.CanGroundCoverRestOnBlock( world, i, j - 1, k );
    	}
    	
    	return false;
    }
    
    @Override    
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
        if ( !canPlaceBlockAt( world, i, j, k ) )
        {
            world.setBlockToAir( i, j, k );
        }
    }

    @Override    
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	float fVisualOffset = 0F;
    	
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	Block blockBelow = Block.blocksList[iBlockBelowID];
    	
    	if ( blockBelow != null )
    	{
    		fVisualOffset = blockBelow.GroundCoverRestingOnVisualOffset( world, i, j - 1, k ); 
    	}
    	
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0D, fVisualOffset, 0D, 
    		1D, m_fVisualHeight + fVisualOffset, 1D );
    	
        return rayTrace.GetFirstIntersection();    	
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public boolean IsGroundCover( )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//
    
    public static void ClearAnyGroundCoverRestingOnBlock( World world, int i, int j, int k )
    {
    	Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
    	
    	if ( blockAbove != null )
    	{
    		if ( blockAbove.IsGroundCover() )
    		{
    			world.setBlockToAir( i, j + 1, k );
    		}
    		else if ( blockAbove.GroundCoverRestingOnVisualOffset( world, i, j + 1, k ) < -0.99F )
    		{
    			Block block2Above = blockAbove = Block.blocksList[world.getBlockId( i, j + 2, k )];
    	    	
    	    	if ( block2Above != null && block2Above.IsGroundCover() )
    	    	{
	    			world.setBlockToAir( i, j + 2, k );
    	    	}
    		}
    	}
    }
    
    public static boolean IsGroundCoverRestingOnBlock( World world, int i, int j, int k )
    {
    	Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
    	
    	if ( blockAbove != null )
    	{
    		if ( blockAbove.IsGroundCover() )
    		{
    			return true;
    		}
    		else if ( blockAbove.GroundCoverRestingOnVisualOffset( world, i, j + 1, k ) < -0.99F )
    		{
    			Block block2Above = blockAbove = Block.blocksList[world.getBlockId( i, j + 2, k )];
    	    	
    	    	if ( block2Above != null && block2Above.IsGroundCover() )
    	    	{
	    			return true;
    	    	}
    		}
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
}