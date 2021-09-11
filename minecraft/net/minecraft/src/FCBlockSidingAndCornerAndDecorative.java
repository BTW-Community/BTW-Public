// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11; // client only

public class FCBlockSidingAndCornerAndDecorative extends FCBlockSidingAndCorner
{
	public static final int m_iSubtypeBench = 12;
	public static final int m_iSubtypeFence = 14;
	
    private final static float m_fBenchTopHeight = ( 2F / 16F );
    private final static float m_fBenchLegHeight = ( 0.5F - m_fBenchTopHeight );
    private final static float m_fBenchLegWidth = ( 4F / 16F );
    private final static float m_fBenchLegHalfWidth = ( m_fBenchLegWidth / 2F );
    
    public final static int m_iOakBenchTopTextureID = 93;
    public final static int m_iOakBenchLegTextureID = 94;
    
	protected FCBlockSidingAndCornerAndDecorative( int iBlockID, Material material, String sTextureName, float fHardness, float fResistance, StepSound stepSound, String name )
	{
		super( iBlockID, material, sTextureName, fHardness, fResistance, stepSound, name );
	}
	
	@Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
		AxisAlignedBB axisalignedbb, List list, Entity entity )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
		if ( iSubtype == m_iSubtypeFence )
		{
			AddCollisionBoxesToListForFence( world, i, j, k, axisalignedbb, list, entity );
		}
		else
		{
            super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, list, entity);
		}
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
		int iSubtype = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeBench )
		{
			return GetBlockBoundsFromPoolForBench( blockAccess, i, j, k );
		}
		else if ( iSubtype == m_iSubtypeFence )
		{
			return GetBlockBoundsFromPoolForFence( blockAccess, i, j, k );
		}
		
		return super.GetBlockBoundsFromPoolBasedOnState( blockAccess, i, j, k );
    }
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
        int iBlockID = world.getBlockId( i, j, k );

    	if ( IsBlockBench( world, i, j, k ) && DoesBenchHaveLeg( world, i, j, k ) )
    	{
	   		return CollisionRayTraceBenchWithLeg( world, i, j, k, startRay, endRay );
    	}
    	else if ( ( iBlockID == blockID && world.getBlockMetadata( i, j, k ) == m_iSubtypeFence ) || iBlockID == Block.fenceGate.blockID )
    	{
	   		return CollisionRayTraceFence( world, i, j, k, startRay, endRay );
    	}
    	
    	return super.collisionRayTrace( world, i, j, k, startRay, endRay );
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		int iSubtype = world.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeBench || iSubtype == m_iSubtypeFence )
		{
			return iMetadata;
		}
		
		return super.onBlockPlaced( world, i, j, k, iFacing, fClickX, fClickY, fClickZ, iMetadata );
    }
	
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		int iSubtype = blockAccess.getBlockMetadata( i, j, k );
		
    	if ( iSubtype == m_iSubtypeBench )
    	{
    		return iFacing == 0;
    	}
    	else if ( iSubtype == m_iSubtypeFence )
    	{
    		return iFacing == 0 || iFacing == 1;
    	}
    	
    	return super.HasCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );
	}

    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
    {
		int iSubtype = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeBench || iSubtype == m_iSubtypeFence )
		{
			return false;			
		}
		
    	return super.HasLargeCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );
    }
    
	@Override
    public int damageDropped( int iMetadata )
    {
		if ( IsDecorativeFromMetadata( iMetadata ) )
		{
			return iMetadata;
		}
		
		return super.damageDropped( iMetadata );
    }

	@Override
    public boolean getBlocksMovement( IBlockAccess blockAccess, int i, int j, int k )
    {
		int iSubtype = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeFence )
		{			
			return false;
		}
		
		return super.getBlocksMovement( blockAccess, i, j, k );        
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
		if ( iMetadata == m_iSubtypeBench )
		{
			return true;
		}		
		else if ( IsDecorativeFromMetadata( iMetadata ) )
		{
    		return world.doesBlockHaveSolidTopSurface( i, j, k );
		}
    	
    	return super.CanGroundCoverRestOnBlock( world, i, j, k );    		
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
		if ( iMetadata == m_iSubtypeBench )
		{
			return -0.5F;
		}		
		else if ( IsDecorativeFromMetadata( iMetadata ) )
		{
    		return 0F;
		}
    	
    	return super.GroundCoverRestingOnVisualOffset( blockAccess, i, j, k );
    }
    
	@Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
		if ( iMetadata == m_iSubtypeFence )
		{
			return -3;
		}
		else
		{
			return 0;
		}
    }	
   
	@Override
	public int GetFacing( int iMetadata )
	{
		if ( iMetadata == m_iSubtypeBench || iMetadata == m_iSubtypeFence )
		{
			return 0;
		}
		
		return super.GetFacing( iMetadata );
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		if ( iMetadata == m_iSubtypeBench || iMetadata == m_iSubtypeFence )
		{
			return iMetadata;
		}
		
		return super.SetFacing( iMetadata, iFacing );		
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iSubtype = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeBench || iSubtype == m_iSubtypeFence )
		{
			return true;
		}
		
		return super.CanRotateOnTurntable( blockAccess, i, j, k );
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iSubtype = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeFence )
		{
			return true;
		}
		else if ( iSubtype == m_iSubtypeBench )
		{
			return false;
		}
		
		return super.CanTransmitRotationVerticallyOnTurntable( blockAccess, i, j, k );
	}
	
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		if ( iMetadata == m_iSubtypeBench || iMetadata == m_iSubtypeFence )
		{
			return iMetadata;
		}
		
		return super.RotateMetadataAroundJAxis( iMetadata, bReverse );
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{
		int iSubtype = world.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeBench || iSubtype == m_iSubtypeFence )
		{
			return false;
		}
		
		return super.ToggleFacing( world, i, j, k, bReverse );
	}
	
	@Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
		int iSubtype = world.getBlockMetadata( i, j, k );
		
		if ( iSubtype == m_iSubtypeFence )
		{
	    	// corresponds to the actual collision volume of the fence, which extends
	    	// half a block above it
	    	
	    	return 0.5F;
		}
		else if ( iSubtype == m_iSubtypeBench )
		{
			return -0.5F;
		}
		
		return super.MobSpawnOnVerticalOffset( world, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
	
	public boolean IsDecorative( IBlockAccess blockAccess, int i, int j, int k )
	{
		return IsDecorativeFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	static public boolean IsDecorativeFromMetadata( int iMetadata )
	{
		return iMetadata == m_iSubtypeBench || iMetadata == m_iSubtypeFence;
	}
    
    public AxisAlignedBB GetBlockBoundsFromPoolForBench( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( !DoesBenchHaveLeg( blockAccess, i, j, k ) )
		{
			return AxisAlignedBB.getAABBPool().getAABB( 
				0D, 0.5D - m_fBenchTopHeight, 0D, 
        		1D, 0.5D, 1D );
		}
		else
		{
			return AxisAlignedBB.getAABBPool().getAABB( 
				0D, 0D, 0D, 1D, 0.5D, 1D );
		}
    }
    
    public AxisAlignedBB GetBlockBoundsFromPoolForFence( IBlockAccess blockAccess, int i, int j, int k )
    {
    	AxisAlignedBB fenceBox = AxisAlignedBB.getAABBPool().getAABB(
    		0.375D, 0D, 0.375D, 
    		0.625D, 1D, 0.625D );

        if ( DoesFenceConnectTo( blockAccess, i, j, k - 1 ) )
        {
        	fenceBox.minZ = 0D;
        }

        if ( DoesFenceConnectTo( blockAccess, i, j, k + 1 ) )
        {
        	fenceBox.maxZ = 1D;
        }

        if ( DoesFenceConnectTo( blockAccess, i - 1, j, k ) )
        {
        	fenceBox.minX = 0D;
        }

        if ( DoesFenceConnectTo( blockAccess, i + 1, j, k ) )
        {
        	fenceBox.maxX = 1D;
        }

		return fenceBox;
    }
    
    public void AddCollisionBoxesToListForFence( World world, int i, int j, int k, 
		AxisAlignedBB intersectingBox, List list, Entity entity )
    {
        boolean bConnectsNegativeI = DoesFenceConnectTo( world, i - 1, j, k );
        boolean bConnectsPositiveI = DoesFenceConnectTo( world, i + 1, j, k );
        boolean bConnectsNegativeK = DoesFenceConnectTo( world, i, j, k - 1 );
        boolean bConnectsPositiveK = DoesFenceConnectTo( world, i, j, k + 1 );
        
        float fXMin = 0.375F;
        float fXMax = 0.625F;
        float fZMin = 0.375F;
        float fZMax = 0.625F;

        if ( bConnectsNegativeK )
        {
            fZMin = 0.0F;
        }

        if ( bConnectsPositiveK )
        {
            fZMax = 1.0F;
        }

        if ( bConnectsNegativeK || bConnectsPositiveK )
        {
        	AxisAlignedBB.getAABBPool().getAABB( fXMin, 0.0F, fZMin, fXMax, 1.5F, fZMax ).
        		offset( i, j, k ).AddToListIfIntersects( intersectingBox, list );
        }

        if ( bConnectsNegativeI )
        {
            fXMin = 0.0F;
        }

        if ( bConnectsPositiveI )
        {
            fXMax = 1.0F;
        }

        if ( bConnectsNegativeI || bConnectsPositiveI || ( !bConnectsNegativeK && !bConnectsPositiveK ) )
        {
        	AxisAlignedBB.getAABBPool().getAABB( fXMin, 0.0F, 0.375F, fXMax, 1.5F, 0.625F ).
    			offset( i, j, k ).AddToListIfIntersects( intersectingBox, list );
        }
    }
    
    public boolean DoesBenchHaveLeg( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
    	
    	if ( blockID == FCBetterThanWolves.fcBlockNetherBrickSidingAndCorner.blockID )
    	{
    		if ( iBlockBelowID == Block.netherFence.blockID )
    		{
    			return true;
    		}
    	}
    	else if ( blockID == iBlockBelowID )
    	{
    		int iBlockBelowMetadata = blockAccess.getBlockMetadata( i, j - 1, k );
    		
    		if ( iBlockBelowMetadata == FCBlockSidingAndCornerAndDecorative.m_iSubtypeFence )
    		{
    			return true;
    		}
    	}
    		
    	boolean positiveIBench = IsBlockBench( blockAccess, i + 1, j, k );
    	boolean negativeIBench = IsBlockBench( blockAccess, i - 1, j, k );
    	boolean positiveKBench = IsBlockBench( blockAccess, i, j, k + 1 );
    	boolean negativeKBench = IsBlockBench( blockAccess, i, j, k - 1 );
    	
    	if ( ( !positiveIBench && ( !positiveKBench || !negativeKBench ) ) ||
			( !negativeIBench && ( !positiveKBench || !negativeKBench ) ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean IsBlockBench( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return blockAccess.getBlockId( i, j, k ) == blockID && blockAccess.getBlockMetadata( i, j, k ) == m_iSubtypeBench;  
    }
    
    public MovingObjectPosition CollisionRayTraceBenchWithLeg( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	// top
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.0F, 0.5F - m_fBenchTopHeight, 0.0F, 1.0F, 0.5F, 1.0F );
		
		// leg
		
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.5F - m_fBenchLegHalfWidth, 0.0F, 0.5F - m_fBenchLegHalfWidth,
	   		0.5F + m_fBenchLegHalfWidth, m_fBenchLegHeight, 0.5F + m_fBenchLegHalfWidth );
    	
        return rayTrace.GetFirstIntersection();        
    }
    
    public MovingObjectPosition CollisionRayTraceFence( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	// post
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D );
        
        // supports
        
        boolean bConnectsAlongI = false;

        boolean bConnectsNegativeI = DoesFenceConnectTo( world, i - 1, j, k );
        boolean bConnectsPositiveI = DoesFenceConnectTo( world, i + 1, j, k );
        boolean bConnectsNegativeK = DoesFenceConnectTo( world, i, j, k - 1 );
        boolean bConnectsPositiveK = DoesFenceConnectTo( world, i, j, k + 1 );
        
        if ( bConnectsNegativeI || bConnectsPositiveI )
        {
            bConnectsAlongI = true;
        }

        boolean bConnectsAlongK = false;
        
        if ( bConnectsNegativeK || bConnectsPositiveK )
        {
            bConnectsAlongK = true;
        }

        if ( !bConnectsAlongI && !bConnectsAlongK )
        {
            bConnectsAlongI = true;
        }

        float var6 = 0.4375F;
        float var7 = 0.5625F;
        float var14 = 0.75F;
        float var15 = 0.9375F;
        
        float var16 = bConnectsNegativeI ? 0.0F : var6;
        float var17 = bConnectsPositiveI ? 1.0F : var7;
        float var18 = bConnectsNegativeK ? 0.0F : var6;
        float var19 = bConnectsPositiveK ? 1.0F : var7;

        if (bConnectsAlongI)
        {
        	rayTrace.AddBoxWithLocalCoordsToIntersectionList((double)var16, (double)var14, (double)var6, (double)var17, (double)var15, (double)var7);
        }

        if (bConnectsAlongK)
        {
        	rayTrace.AddBoxWithLocalCoordsToIntersectionList((double)var6, (double)var14, (double)var18, (double)var7, (double)var15, (double)var19);
        }

        var14 = 0.375F;
        var15 = 0.5625F;

        if (bConnectsAlongI)
        {
        	rayTrace.AddBoxWithLocalCoordsToIntersectionList((double)var16, (double)var14, (double)var6, (double)var17, (double)var15, (double)var7);
        }

        if (bConnectsAlongK)
        {
        	rayTrace.AddBoxWithLocalCoordsToIntersectionList((double)var6, (double)var14, (double)var18, (double)var7, (double)var15, (double)var19);
        }

        return rayTrace.GetFirstIntersection();        
    }
    
    public boolean DoesFenceConnectTo( IBlockAccess blockAccess, int i, int j, int k )
    {
        int iBlockID = blockAccess.getBlockId( i, j, k );

        if ( ( iBlockID == blockID && blockAccess.getBlockMetadata( i, j, k ) == m_iSubtypeFence ) || iBlockID == Block.fenceGate.blockID )
        {
        	return true;
        }
        
        Block block = Block.blocksList[iBlockID];
        
        return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() && 
        	block.blockMaterial != Material.pumpkin;
    }
    
    //------------- Stonecutter related functionality ------------//
    // Only called by Automation+ addon
    // Can be used by other addons to interface

    private int mouldingIDDropped = -1;
    
    public void setMouldingIDDroppedOnStonecutter(int id) {
    	this.mouldingIDDropped = id;
    }
    
    public int getItemIDDroppedOnStonecutter(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        return var5 == 12 ? super.getItemIDDroppedOnStonecutter(var1, var2, var3, var4) : (var5 == 14 ? this.blockID : (this.GetIsCorner(var1, var2, var3, var4) ? super.getItemIDDroppedOnStonecutter(var1, var2, var3, var4) : this.mouldingIDDropped));
    }

    public int getItemCountDroppedOnStonecutter(World var1, int var2, int var3, int var4)
    {
        if (this.IsDecorative(var1, var2, var3, var4))
        {
            int var5 = var1.getBlockMetadata(var2, var3, var4);
            return var5 == 14 ? 2 : super.getItemCountDroppedOnStonecutter(var1, var2, var3, var4);
        }
        else
        {
            return 2;
        }
    }

    public int getItemDamageDroppedOnStonecutter(World var1, int var2, int var3, int var4)
    {
        if (this.IsDecorative(var1, var2, var3, var4))
        {
            int var5 = var1.getBlockMetadata(var2, var3, var4);
            return var5 == 14 ? 1 : super.getItemDamageDroppedOnStonecutter(var1, var2, var3, var4);
        }
        else
        {
            return 0;
        }
    }
    
	//----------- Client Side Functionality -----------//
	
	@Override
    public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
		super.getSubBlocks( iBlockID, creativeTabs, list );
		
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeBench ) );
        
        if ( iBlockID != FCBetterThanWolves.fcBlockNetherBrickSidingAndCorner.blockID )
        {	        	
        	list.add( new ItemStack( iBlockID, 1, m_iSubtypeFence ) );
        }
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		FCUtilsBlockPos thisPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, Block.GetOppositeFacing( iSide ) );
		
		int iSubtype = blockAccess.getBlockMetadata( thisPos.i, thisPos.j, thisPos.k );
		
		if ( iSubtype == m_iSubtypeFence || iSubtype == m_iSubtypeBench )
		{
			return true;
		}
		
		return super.shouldSideBeRendered( blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide );
	}
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
		if ( iSubtype == m_iSubtypeBench )
		{
			return RenderBench( renderBlocks, blockAccess, i, j, k, this );
		}
		else if ( iSubtype == m_iSubtypeFence )
		{
			return RenderFence( renderBlocks, blockAccess, i, j, k, this );
		}
   	
    	return super.RenderBlock( renderBlocks, i, j, k );
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	int iSubtype = iItemDamage;
    	
    	Block block = this;
    	
    	if ( blockID == FCBetterThanWolves.fcBlockWoodSidingDecorativeItemStubID )
    	{
    		
    		int iItemType = FCItemBlockWoodSidingDecorativeStub.GetBlockType( iItemDamage );
			int iWoodType = FCItemBlockWoodSidingDecorativeStub.GetWoodType( iItemDamage );
    		
    		if ( iItemType == FCItemBlockWoodSidingDecorativeStub.m_iTypeBench )
    		{
    			iSubtype = m_iSubtypeBench;
    		}
    		else // fence
    		{
    			iSubtype = m_iSubtypeFence;
    		}
    		
    		if ( iWoodType == 0 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodOakSidingAndCorner;
    		}
    		else if ( iWoodType == 1 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodSpruceSidingAndCorner;
    		}
    		else if ( iWoodType == 2 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodBirchSidingAndCorner;
    		}
    		else if ( iWoodType == 3 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodJungleSidingAndCorner;
    		}    			
    		else 
    		{
    			block = FCBetterThanWolves.fcBlockWoodBloodSidingAndCorner;
    		}    			
    	}
    	
		if ( iSubtype == m_iSubtypeBench )
		{
			RenderBenchInvBlock( renderBlocks, block, iSubtype );
		}
		else if ( iSubtype == m_iSubtypeFence )
		{
			RenderFenceInvBlock( renderBlocks, block, iSubtype );
		}
		else
		{		
			super.RenderBlockAsItem( renderBlocks, iItemDamage, fBrightness );		
		}
    }
    
    //------------- Bench Renderers ------------//

    static public boolean RenderBench
    ( 
		RenderBlocks renderBlocks, 
		IBlockAccess blockAccess, 
		int i, int j, int k, 
		Block block 
	)
    {    	
    	if ( block.blockID != FCBetterThanWolves.fcBlockWoodOakSidingAndCorner.blockID )
    	{
	    	// render top
	        
	    	renderBlocks.setRenderBounds( 0.0F, 0.5F - m_fBenchTopHeight, 0.0F,
	    		1.0F, 0.5F, 1.0F );
	   
		   	renderBlocks.renderStandardBlock( block, i, j, k );
		   
		   	FCBlockSidingAndCornerAndDecorative benchBlock = (FCBlockSidingAndCornerAndDecorative)block;
		
		   	if ( benchBlock.DoesBenchHaveLeg( blockAccess, i, j, k ) )
		   	{
			   	// render leg
			   
			   	renderBlocks.setRenderBounds( 0.5F - m_fBenchLegHalfWidth, 0.0F, 0.5F - m_fBenchLegHalfWidth,
			   		0.5F + m_fBenchLegHalfWidth, m_fBenchLegHeight, 0.5F + m_fBenchLegHalfWidth );
			   
			   	renderBlocks.renderStandardBlock( block, i, j, k );		   
		   	}
    	}
    	else
    	{
	    	// render top
	        
	    	renderBlocks.setRenderBounds( 0.0F, 0.5F - m_fBenchTopHeight, 0.0F,
	    		1.0F, 0.5F, 1.0F );
	   
	    	FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakTop );
	    	   		   
		   	FCBlockSidingAndCornerAndDecorative benchBlock = (FCBlockSidingAndCornerAndDecorative)block;
		
		   	if ( benchBlock.DoesBenchHaveLeg( blockAccess, i, j, k ) )
		   	{
			   	// render leg
			   
			   	renderBlocks.setRenderBounds( 0.5F - m_fBenchLegHalfWidth, 0.0F, 0.5F - m_fBenchLegHalfWidth,
			   		0.5F + m_fBenchLegHalfWidth, m_fBenchLegHeight, 0.5F + m_fBenchLegHalfWidth );
			   
		    	FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakLeg );
		   	}
    	}
	   
	   	return true;
    }
    
    static public void RenderBenchInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block, 
		int iItemDamage 
	)
    {    	
    	if ( block.blockID != FCBetterThanWolves.fcBlockWoodOakSidingAndCorner.blockID )
    	{
	    	// render top
	        
	 	   	renderBlocks.setRenderBounds( 0.0F, 0.5F - m_fBenchTopHeight, 0.0F,
	 	   		1.0F, 0.5F, 1.0F );
	    
	 	   	FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypeBench );
	 	   
	 	   	// render leg
		   
	 	   	renderBlocks.setRenderBounds( 0.5F - m_fBenchLegHalfWidth, 0.0F, 0.5F - m_fBenchLegHalfWidth,
	 	   		0.5F + m_fBenchLegHalfWidth, m_fBenchLegHeight, 0.5F + m_fBenchLegHalfWidth );
	    
	 	   	FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypeBench );
    	}
    	else
    	{
	    	// render top
	        
	 	   	renderBlocks.setRenderBounds( 0.0F, 0.5F - m_fBenchTopHeight, 0.0F,
	 	   		1.0F, 0.5F, 1.0F );
	    
	 	   	FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakTop );
	 	   
	 	   	// render leg
		   
	 	   	renderBlocks.setRenderBounds( 0.5F - m_fBenchLegHalfWidth, 0.0F, 0.5F - m_fBenchLegHalfWidth,
	 	   		0.5F + m_fBenchLegHalfWidth, m_fBenchLegHeight, 0.5F + m_fBenchLegHalfWidth );
	    
	 	   	FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakLeg );
    	}
    }
    
    //------------- Fence Renderers ------------//

    static public boolean RenderFence
    ( 
		RenderBlocks renderBlocks, 
		IBlockAccess blockAccess, 
		int i, int j, int k, 
		Block block 
	)
    {    	
    	// post
    	
        renderBlocks.setRenderBounds( 0.375D, 0.0D, (double)0.375D, 0.625D, 1.0D, 0.625D );
        
        renderBlocks.renderStandardBlock( block, i, j, k );
        
        // supports
        
    	FCBlockSidingAndCornerAndDecorative blockSiding = (FCBlockSidingAndCornerAndDecorative)block;
    	
        boolean bConnectsAlongI = false;

        boolean bConnectsNegativeI = blockSiding.DoesFenceConnectTo( renderBlocks.blockAccess, i - 1, j, k );
        boolean bConnectsPositiveI = blockSiding.DoesFenceConnectTo( renderBlocks.blockAccess, i + 1, j, k );
        boolean bConnectsNegativeK = blockSiding.DoesFenceConnectTo( renderBlocks.blockAccess, i, j, k - 1 );
        boolean bConnectsPositiveK = blockSiding.DoesFenceConnectTo( renderBlocks.blockAccess, i, j, k + 1 );
        
        if ( bConnectsNegativeI || bConnectsPositiveI )
        {
            bConnectsAlongI = true;
        }

        boolean bConnectsAlongK = false;
        
        if ( bConnectsNegativeK || bConnectsPositiveK )
        {
            bConnectsAlongK = true;
        }

        if ( !bConnectsAlongI && !bConnectsAlongK )
        {
            bConnectsAlongI = true;
        }

        float var6 = 0.4375F;
        float var7 = 0.5625F;
        float var14 = 0.75F;
        float var15 = 0.9375F;
        
        float var16 = bConnectsNegativeI ? 0.0F : var6;
        float var17 = bConnectsPositiveI ? 1.0F : var7;
        float var18 = bConnectsNegativeK ? 0.0F : var6;
        float var19 = bConnectsPositiveK ? 1.0F : var7;

        if (bConnectsAlongI)
        {
            renderBlocks.setRenderBounds((double)var16, (double)var14, (double)var6, (double)var17, (double)var15, (double)var7);
            renderBlocks.renderStandardBlock(blockSiding, i, j, k);
        }

        if (bConnectsAlongK)
        {
            renderBlocks.setRenderBounds((double)var6, (double)var14, (double)var18, (double)var7, (double)var15, (double)var19);
            renderBlocks.renderStandardBlock(blockSiding, i, j, k);
        }

        var14 = 0.375F;
        var15 = 0.5625F;

        if (bConnectsAlongI)
        {
            renderBlocks.setRenderBounds((double)var16, (double)var14, (double)var6, (double)var17, (double)var15, (double)var7);
            renderBlocks.renderStandardBlock(blockSiding, i, j, k);
        }

        if (bConnectsAlongK)
        {
            renderBlocks.setRenderBounds((double)var6, (double)var14, (double)var18, (double)var7, (double)var15, (double)var19);
            renderBlocks.renderStandardBlock(blockSiding, i, j, k);
        }

        return true;
    }
    
    static public void RenderFenceInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block par1Block, 
		int iItemDamage 
	)
    {
        Tessellator tesselator = Tessellator.instance;

        for ( int iTempCount = 0; iTempCount < 4; ++iTempCount )
        {
            float fThickness = 0.125F;

            if (iTempCount == 0)
            {
                renderBlocks.setRenderBounds((double)(0.5F - fThickness), 0.0D, 0.0D, (double)(0.5F + fThickness), 1.0D, (double)(fThickness * 2.0F));
            }

            if (iTempCount == 1)
            {
                renderBlocks.setRenderBounds((double)(0.5F - fThickness), 0.0D, (double)(1.0F - fThickness * 2.0F), (double)(0.5F + fThickness), 1.0D, 1.0D);
            }

            fThickness = 0.0625F;

            if (iTempCount == 2)
            {
                renderBlocks.setRenderBounds((double)(0.5F - fThickness), (double)(1.0F - fThickness * 3.0F), (double)(-fThickness * 2.0F), (double)(0.5F + fThickness), (double)(1.0F - fThickness), (double)(1.0F + fThickness * 2.0F));
            }

            if (iTempCount == 3)
            {
                renderBlocks.setRenderBounds((double)(0.5F - fThickness), (double)(0.5F - fThickness * 3.0F), (double)(-fThickness * 2.0F), (double)(0.5F + fThickness), (double)(0.5F - fThickness), (double)(1.0F + fThickness * 2.0F));
            }

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, -1.0F, 0.0F);
            renderBlocks.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 1.0F, 0.0F);
            renderBlocks.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 0.0F, -1.0F);
            renderBlocks.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 0.0F, 1.0F);
            renderBlocks.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(-1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
            tesselator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    }
}