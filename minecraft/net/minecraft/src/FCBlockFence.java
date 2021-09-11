// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockFence extends Block
{
	protected static final FCModelBlockFence m_model = new FCModelBlockFence();
	
    protected final String m_sIconName;

    public FCBlockFence( int iBlockID, String sIconName, Material material )
    {
        // NOTE: I used metadata to represent neigboring connections in a single 
        // release (4.ABCFAFBFC) for oak and netherbrick fences. 
        // Keep this in mind should you decide to use metadata again later
    	
        super( iBlockID, material );
        
        m_sIconName = sIconName;
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1.5D, 1D );
        
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
    public boolean getBlocksMovement( IBlockAccess blockAccess, int i, int j, int k )
    {
		// getBlocksMovement() is misnamed and returns true if the block *doesn't* block movement
    	
        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startRay, Vec3 endRay )
    {
    	FCModelBlock tempModel = AssembleTemporaryModel( world, i, j, k );    	
		
    	return tempModel.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
    }

    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB boundingBox, List list, Entity entity )
    {
    	AxisAlignedBB tempBox = m_model.m_boxCollisionCenter.MakeTemporaryCopy();
    	
    	tempBox.offset( i, j, k );
    	
    	if ( tempBox.intersectsWith( boundingBox ) )
    	{
    		list.add( tempBox );
    	}
    	
    	for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
    	{
    		if ( CanConnectToBlockToFacing( world, i, j, k, iTempFacing ) )
    		{
    			tempBox = m_model.m_boxCollisionStruts.MakeTemporaryCopy();
    			
    			tempBox.RotateAroundJToFacing( iTempFacing );
	    		
    	    	tempBox.offset( i, j, k );
    	    	
    	    	if ( tempBox.intersectsWith( boundingBox ) )
    	    	{
    	    		list.add( tempBox );
    	    	}
    		}
    	}
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	AxisAlignedBB bounds = m_model.m_boxBoundsCenter.MakeTemporaryCopy();
    	
    	if ( CanConnectToBlockToFacing( blockAccess, i, j, k, 2 ) )
    	{
    		bounds.minZ = 0D;
    	}
    	
    	if ( CanConnectToBlockToFacing( blockAccess, i, j, k, 3 ) )
    	{
    		bounds.maxZ = 1D;
    	}
    	
    	if ( CanConnectToBlockToFacing( blockAccess, i, j, k, 4 ) )
    	{
    		bounds.minX = 0D;
    	}
    	
    	if ( CanConnectToBlockToFacing( blockAccess, i, j, k, 5 ) )
    	{
    		bounds.maxX = 1D;
    	}
    	
    	return bounds;
    }
    
	@Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -3;
    }
	
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, 
		int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing == 0 || iFacing == 1;
	}
    
    @Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
    	// corresponds to the actual collision volume of the fence, which extends
    	// half a block above it
    	
    	return 0.5F;
    }
    
	//------------- Class Specific Methods ------------//
	
    protected boolean CanConnectToBlockToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		return CanConnectToBlockAt( blockAccess, targetPos.i, targetPos.j, targetPos.k );
    }
    
    protected boolean CanConnectToBlockAt( IBlockAccess blockAccess, int i, int j, int k )
    {
        int var5 = blockAccess.getBlockId( i, j, k );

        if ( var5 != this.blockID && var5 != Block.fenceGate.blockID )
        {
            Block var6 = Block.blocksList[var5];
            
            return var6 != null && var6.blockMaterial.isOpaque() && var6.renderAsNormalBlock() ? 
            	var6.blockMaterial != Material.pumpkin : false;
        }
        else
        {
            return true;
        }
    }
    
    protected FCModelBlock AssembleTemporaryModel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	FCModelBlock tempModel = m_model.MakeTemporaryCopy();
    	
    	for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
    	{
    		if ( CanConnectToBlockToFacing( blockAccess, i, j, k, iTempFacing ) )
    		{
    			FCModelBlock tempSupportsModel = m_model.m_modelStruts.MakeTemporaryCopy();
    			
    			tempSupportsModel.RotateAroundJToFacing( iTempFacing );
	    		
				tempSupportsModel.MakeTemporaryCopyOfPrimitiveList( tempModel );
    		}
    	}
    	
    	return tempModel;
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon( m_sIconName );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }

    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	FCModelBlock tempModel = AssembleTemporaryModel( renderBlocks.blockAccess, i, j, k );    	
		
    	return tempModel.RenderAsBlock( renderBlocks, this, i, j, k );
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
		m_model.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    }
    
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, 
    	MovingObjectPosition rayTraceHit )
    {
		AxisAlignedBB tempBox = m_model.m_boxBoundsCenter.MakeTemporaryCopy();
		
		return tempBox.offset( rayTraceHit.blockX, rayTraceHit.blockY, rayTraceHit.blockZ );		
    }
}