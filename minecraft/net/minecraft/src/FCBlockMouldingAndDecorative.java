// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockMouldingAndDecorative extends FCBlockMoulding
{
    public static final int m_iSubtypeColumn = 12;
    public static final int m_iSubtypePedestalUp = 13;
    public static final int m_iSubtypePedestalDown = 14;
    public static final int m_iSubtypeTable = 15;
    
    protected static final double m_dColumWidth = ( 10D / 16D );
    protected static final double m_dColumHalfWidth = ( m_dColumWidth / 2D );
    
    protected static final double m_dPedestalBaseHeight = ( 12D / 16D);
    protected static final double m_dPedestalMiddleHeight = ( 2D / 16D );
    protected static final double m_dPedestalMiddleWidth = ( 14D / 16D );
    protected static final double m_dPedestalMiddleHalfWidth = ( m_dPedestalMiddleWidth / 2D );
    protected static final double m_dPedestalTopHeight = ( 2D / 16D );
    protected static final double m_dPedestalTopWidth = ( 12D / 16D );
    protected static final double m_dPedestalTopHalfWidth = ( m_dPedestalTopWidth / 2D );
    
    protected static final double m_dTableTopHeight = ( 2D / 16D );
    protected static final double m_dTableLegHeight = ( 1D - m_dTableTopHeight );
    protected static final double m_dTableLegWidth = ( 4D / 16D );
    protected static final double m_dTableLegHalfWidth = ( m_dTableLegWidth / 2D );
    
    String m_sColumnSideTextureName;
    String m_sColumnTopAndBottomTextureName; 
    String m_sPedestalSideTextureName;
    String m_sPedestalTopAndBottomTextureName;    
    
	protected FCBlockMouldingAndDecorative( int iBlockID, Material material, String sTextureName, String sColumnSideTextureName, int iMatchingCornerBlockID, 
		float fHardness, float fResistance, StepSound stepSound, String name )
	{
		super( iBlockID, material, sTextureName, iMatchingCornerBlockID, fHardness, fResistance, stepSound, name );
		
	    m_sColumnSideTextureName = sColumnSideTextureName;
	    m_sColumnTopAndBottomTextureName = sTextureName; 
	    m_sPedestalSideTextureName = sTextureName;
	    m_sPedestalTopAndBottomTextureName = sTextureName;
	}
	
	protected FCBlockMouldingAndDecorative( int iBlockID, Material material, 
		String sTextureName, String sColumnSideTextureName, String sColumnTopAndBottomTextureName, String sPedestalSideTextureName, String sPedestalTopAndBottomTextureName,
		int iMatchingCornerBlockID, float fHardness, float fResistance, StepSound stepSound, String name )
	{
		super( iBlockID, material, sTextureName, iMatchingCornerBlockID, fHardness, fResistance, stepSound, name );
		
	    m_sColumnSideTextureName = sColumnSideTextureName;
	    m_sColumnTopAndBottomTextureName = sColumnTopAndBottomTextureName; 
	    m_sPedestalSideTextureName = sPedestalSideTextureName;
	    m_sPedestalTopAndBottomTextureName = sPedestalTopAndBottomTextureName;
	}
	
	@Override
    public int damageDropped( int iMetadata )
    {
    	if ( !IsDecorative( iMetadata ) )
    	{
    		return super.damageDropped( iMetadata );
    	}
    	
    	if ( iMetadata == m_iSubtypePedestalDown )
    	{
    		iMetadata = m_iSubtypePedestalUp;
    	}
    	
        return iMetadata; 
    }

	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	int iSubtype = iMetadata;
    	
    	if ( iSubtype == m_iSubtypePedestalUp  )
    	{
            if ( iFacing == 0 || iFacing != 1 && (double)fClickY > 0.5D )
            {
    			return m_iSubtypePedestalDown;
            }            
    	}
    	else if ( !IsDecorative( iMetadata ) )
    	{
    		return super.onBlockPlaced( world, i, j, k, iFacing, fClickX, fClickY, fClickZ, iMetadata );
    	}
    	
    	return iMetadata;
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( IsDecorative( iMetadata ) )
    	{
	    	switch ( iMetadata )
	    	{
				case m_iSubtypeColumn:
					
		        	return AxisAlignedBB.getAABBPool().getAABB(         	
		        		0.5D - m_dColumHalfWidth, 0D, 0.5D - m_dColumHalfWidth, 
		        		0.5D + m_dColumHalfWidth, 1D, 0.5D + m_dColumHalfWidth );
			    	
	    		case m_iSubtypeTable:
	    			
	    			if ( !DoesTableHaveLeg( blockAccess, i, j, k ) )
	    			{
	    	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	    	        		0D, 1D - m_dTableTopHeight, 0D, 1D, 1D, 1D );
	    			}
	    			
    	        	return AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D );
	    			
				case  m_iSubtypePedestalUp:
				case  m_iSubtypePedestalDown:
				
    	        	return AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D );
	    	    	
    	    	default:
    	    		
    	        	return AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D );
	    	}
    	}
    	
    	return super.GetBlockBoundsFromPoolBasedOnState( blockAccess, i, j, k );
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	if ( IsDecorative( world, i, j, k ) )
    	{
	    	if ( IsBlockTable( world, i, j, k ) && DoesTableHaveLeg( world, i, j, k ) )
	    	{
		   		return CollisionRayTraceTableWithLeg( world, i, j, k, startRay, endRay );
	    	}
	    	else
	    	{
	    		return CollisionRayTraceVsBlockBounds( world, i, j, k, startRay, endRay );
	    	}
    	}
    	
    	return super.collisionRayTrace( world, i, j, k, startRay, endRay );
    }
    
	@Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
    	if ( IsDecorative( world, i, j, k ) )
    	{
	        getCollisionBoundingBoxFromPool( world, i, j, k ).AddToListIfIntersects( 
	        	intersectingBox, list );
    	}
    	else
    	{
    		super.addCollisionBoxesToList( world, i, j, k, intersectingBox, list, entity );
    	}
    }
	
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iSubtype )
    	{
    		case m_iSubtypeColumn:
    			
    			return iFacing == 0 || iFacing == 1;
    			
    		case m_iSubtypePedestalUp:
    		case m_iSubtypePedestalDown:
    			
    			return true;   			
    	}
    	
    	return super.HasCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );    	
	}
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iSubtype )
    	{
    		case m_iSubtypePedestalUp:
    			
    			return iFacing == 0;
    			
    		case m_iSubtypePedestalDown:
    			
    			return iFacing == 1;
    			
    		case m_iSubtypeTable:
    			
    			return iFacing == 1;    			
    	}
    	
    	return super.HasLargeCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );    	
	}
    
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
    	if ( !IsDecorative( iMetadata ) )
    	{
    		return super.RotateMetadataAroundJAxis( iMetadata, bReverse );
    	}    	
		
		return iMetadata;
	}

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
    	if ( !IsDecorative( world, i, j, k ) )
    	{
    		return super.ToggleFacing( world, i, j, k, bReverse );
    	}
    	
    	return false;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( IsDecorative( iMetadata ) )
    	{
	    	if ( iMetadata == m_iSubtypePedestalUp || iMetadata == m_iSubtypePedestalDown )
	    	{
	    		return true;
	    	}
	    	
	    	return false;
    	}
    	
		return super.CanTransmitRotationHorizontallyOnTurntable( blockAccess, i, j, k );
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( IsDecorative( iMetadata ) )
    	{
    		if ( iMetadata != m_iSubtypeTable )
    		{
    			return true;
    		}
    		
    		return false;
    	}
    	
		return super.CanTransmitRotationVerticallyOnTurntable( blockAccess, i, j, k );
	}
	
	@Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
    	if ( iSubtype < 12 )
    	{
    		return super.MobSpawnOnVerticalOffset( world, i, j, k );
    	}
    	
    	return 0F;
    }
	
    //------------- Class Specific Methods ------------//

	@Override
    protected boolean IsMouldingOfSameType( IBlockAccess blockAccess, int i, int j, int k )
    {    	
    	return blockAccess.getBlockId( i, j, k ) == blockID && !IsDecorative( blockAccess, i, j, k );
    }
    
    public boolean IsDecorative( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return IsDecorative( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public boolean IsDecorative( int iMetadata )
    {
    	return iMetadata >= 12;
    }
    
    public boolean IsBlockTable( IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( blockAccess.getBlockId( i, j, k ) == blockID )
    	{
    		if ( blockAccess.getBlockMetadata( i, j, k ) == m_iSubtypeTable )
    		{
    			return true;
    		}
    	}
    			
		return false;
    }
    
    public boolean DoesTableHaveLeg( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
    	
    	if ( blockID == FCBetterThanWolves.fcBlockNetherBrickMouldingAndDecorative.blockID )
    	{
    		if ( iBlockBelowID == Block.netherFence.blockID )
    		{
    			return true;
    		}
    	}
    	else if ( iBlockBelowID == m_iMatchingCornerBlockID )
    	{
    		int iBlockBelowMetadata = blockAccess.getBlockMetadata( i, j - 1, k );
    		
    		if ( iBlockBelowMetadata == FCBlockSidingAndCornerAndDecorative.m_iSubtypeFence )
    		{
    			return true;
    		}
    	}
    		
    	boolean positiveITable = IsBlockTable( blockAccess, i + 1, j, k );
    	boolean negativeITable = IsBlockTable( blockAccess, i - 1, j, k );
    	boolean positiveKTable = IsBlockTable( blockAccess, i, j, k + 1 );
    	boolean negativeKTable = IsBlockTable( blockAccess, i, j, k - 1 );
    	
    	if ( ( !positiveITable && ( !positiveKTable || !negativeKTable ) ) ||
			( !negativeITable && ( !positiveKTable || !negativeKTable ) ) )
    	{
    		return true;
    	}
    	
    	return false;
    }

    public MovingObjectPosition CollisionRayTraceTableWithLeg( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	// top
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.0F, 1.0F - m_dTableTopHeight, 0.0F, 1.0F, 1.0F, 1.0F );
		
		// leg
		
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.5F - m_dTableLegHalfWidth, 0.0F, 0.5F - m_dTableLegHalfWidth,
	   		0.5F + m_dTableLegHalfWidth, m_dTableLegHeight, 0.5F + m_dTableLegHalfWidth );
    	
        return rayTrace.GetFirstIntersection();        
    }
    
    //------------- Stonecutter related functionality ------------//
    // Only called by Automation+ addon
    // Can be used by other addons to interface
    
    private int cornerIDDropped = -1;
    
    public void setCornerIDDroppedOnStonecutter(int id) {
    	this.cornerIDDropped = id;
    }

    public int getItemIDDroppedOnStonecutter(World var1, int var2, int var3, int var4)
    {
        return this.IsDecorative(var1, var2, var3, var4) ? super.getItemIDDroppedOnStonecutter(var1, var2, var3, var4) : this.cornerIDDropped;
    }

    public int getItemCountDroppedOnStonecutter(World var1, int var2, int var3, int var4)
    {
        return this.IsDecorative(var1, var2, var3, var4) ? super.getItemCountDroppedOnStonecutter(var1, var2, var3, var4) : 2;
    }

    public int getItemDamageDroppedOnStonecutter(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        return this.IsDecorative(var5) ? super.getItemDamageDroppedOnStonecutter(var1, var2, var3, var4) : 1;
    }

	//----------- Client Side Functionality -----------//
    
    private Icon m_IconColumnSide;
    private Icon m_IconColumnTopAndBottom;
    private Icon m_IconPedestalSide;
    private Icon m_IconPedestalTopAndBottom;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_IconColumnSide = register.registerIcon( m_sColumnSideTextureName );
		m_IconColumnTopAndBottom = register.registerIcon( m_sColumnTopAndBottomTextureName ); 
		m_IconPedestalSide = register.registerIcon( m_sPedestalSideTextureName );
		m_IconPedestalTopAndBottom = register.registerIcon( m_sPedestalTopAndBottomTextureName );    
   }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iMetadata == m_iSubtypeColumn )
		{
			if ( iSide < 2 )
			{
				return m_IconColumnTopAndBottom;
			}
			else
			{
				return m_IconColumnSide;
			}
		}
		else if ( iMetadata == m_iSubtypePedestalUp || iMetadata == m_iSubtypePedestalDown )
		{
			if ( iSide < 2 )
			{
				return m_IconPedestalTopAndBottom;
			}
			else
			{
				return m_IconPedestalSide;
			}
		}
		
		return super.getIcon( iSide, iMetadata );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( !IsDecorative( iMetadata ) )
    	{
    		return super.RenderBlock( renderBlocks, i, j, k );
    	}
    	
    	switch ( iMetadata )
    	{
    		case m_iSubtypePedestalUp:
    			
    			return RenderPedestalUp( renderBlocks, blockAccess, i, j, k, this );
    			
    		case m_iSubtypePedestalDown:
    			
    			return RenderPedestalDown( renderBlocks, blockAccess, i, j, k, this );
    			
    		case m_iSubtypeTable:
    			
    			return RenderTable( renderBlocks, blockAccess, i, j, k, this );
    			
			default:
				
				renderBlocks.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
					renderBlocks.blockAccess, i, j, k ) );
		        
		    	return renderBlocks.renderStandardBlock( this, i, j, k );
		    	
    	}    	
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	if ( IsDecorative( iItemDamage ) )
    	{
    		RenderDecorativeInvBlock( renderBlocks, this, iItemDamage, fBrightness );
    	}
    	else
    	{
    		super.RenderBlockAsItem( renderBlocks, iItemDamage, fBrightness );
    	}
    }
    
    /**
     * The block param is necessary here as the wood item stubs use them to specify what block
     * type they represent.
     */
    protected void RenderDecorativeInvBlock( RenderBlocks renderBlocks, Block block, int iItemDamage, float fBrightness )
    {    	
    	switch ( iItemDamage )
    	{
    		case m_iSubtypePedestalUp:
    			
    			RenderPedestalUpInvBlock( renderBlocks, block );
    			
    			break;
    			
    		case m_iSubtypePedestalDown:
    			
    			RenderPedestalDownInvBlock( renderBlocks, block );
    			
    			break;
    			
    		case m_iSubtypeTable:
    			
    			RenderTableInvBlock( renderBlocks, block );
    			
    			break;    			
		        
			case m_iSubtypeColumn:
				
				renderBlocks.setRenderBounds( ( 0.5F - m_dColumHalfWidth ), 0.0F, ( 0.5F - m_dColumHalfWidth ), 
    	        		( 0.5F + m_dColumHalfWidth ), 1.0F, ( 0.5F + m_dColumHalfWidth ) );
    	    	
		        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, 
		        	-0.5F, -0.5F, -0.5F, iItemDamage );
    			
    			break;    			
		        
			default:
				
		    	renderBlocks.renderBlockAsItemVanilla( block, iItemDamage, fBrightness );
    			
    			break;    			
    	}
    }
    
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
	{
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( IsDecorative( iMetadata ) )
    	{
        	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    	}
    	
    	return super.getSelectedBoundingBoxFromPool( world, i, j, k );
	}
	
    static public boolean RenderPedestalUp
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   // render base
    	
	   renderBlocks.setRenderBounds( 0F, 0F, 0F,
			   1.0F, m_dPedestalBaseHeight, 1.0F );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render middle
	   
	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalMiddleHalfWidth, m_dPedestalBaseHeight, 0.5F - m_dPedestalMiddleHalfWidth,
			   0.5F + m_dPedestalMiddleHalfWidth, m_dPedestalBaseHeight + m_dPedestalMiddleHeight, 0.5F + m_dPedestalMiddleHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render top
       
	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalTopHalfWidth, 1.0F - m_dPedestalTopHeight, 0.5F - m_dPedestalTopHalfWidth,
			   0.5F + m_dPedestalTopHalfWidth, 1.0F, 0.5F + m_dPedestalTopHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   return true;
    }
    
    static public void RenderPedestalUpInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
 	   // render base
     	
 	   renderBlocks.setRenderBounds( 0F, 0F, 0F,
 			   1.0F, m_dPedestalBaseHeight, 1.0F );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalUp );    
 	   
 	   // render middle
 	   
 	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalMiddleHalfWidth, m_dPedestalBaseHeight, 0.5F - m_dPedestalMiddleHalfWidth,
 			   0.5F + m_dPedestalMiddleHalfWidth, m_dPedestalBaseHeight + m_dPedestalMiddleHeight, 0.5F + m_dPedestalMiddleHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalUp );    
 	   
 	   // render top
        
 	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalTopHalfWidth, 1.0F - m_dPedestalTopHeight, 0.5F - m_dPedestalTopHalfWidth,
 			   0.5F + m_dPedestalTopHalfWidth, 1.0F, 0.5F + m_dPedestalTopHalfWidth );
    
       FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalUp );    
    }
    
    static public boolean RenderPedestalDown
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   // render base
    	
	   renderBlocks.setRenderBounds( 0F, 1.0F - m_dPedestalBaseHeight, 0F,
			   1.0F, 1.0F, 1.0F );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render middle
	   
	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalMiddleHalfWidth, 1.0F - m_dPedestalBaseHeight - m_dPedestalMiddleHeight, 0.5F - m_dPedestalMiddleHalfWidth,
			   0.5F + m_dPedestalMiddleHalfWidth, 1.0F - m_dPedestalBaseHeight, 0.5F + m_dPedestalMiddleHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   // render top
       
	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalTopHalfWidth, 00F, 0.5F - m_dPedestalTopHalfWidth,
			   0.5F + m_dPedestalTopHalfWidth, m_dPedestalTopHeight, 0.5F + m_dPedestalTopHalfWidth );
   
	   renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   return true;
    }
    
    static public void RenderPedestalDownInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
	   // render base
	 	
	   renderBlocks.setRenderBounds( 0F, 1.0F - m_dPedestalBaseHeight, 0F,
			   1.0F, 1.0F, 1.0F );
	
	   FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalDown );    
	   
	   // render middle
	   
	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalMiddleHalfWidth, 1.0F - m_dPedestalBaseHeight - m_dPedestalMiddleHeight, 0.5F - m_dPedestalMiddleHalfWidth,
			   0.5F + m_dPedestalMiddleHalfWidth, 1.0F - m_dPedestalBaseHeight, 0.5F + m_dPedestalMiddleHalfWidth );
	
	   FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalDown );    
	   
	   // render top
	    
	   renderBlocks.setRenderBounds( 0.5F - m_dPedestalTopHalfWidth, 00F, 0.5F - m_dPedestalTopHalfWidth,
			   0.5F + m_dPedestalTopHalfWidth, m_dPedestalTopHeight, 0.5F + m_dPedestalTopHalfWidth );
	
	   FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypePedestalDown );    
    }
    
    static public boolean RenderTable
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   	FCBlockMouldingAndDecorative tableBlock = (FCBlockMouldingAndDecorative)block;
		
    	// render top
       
    	renderBlocks.setRenderBounds( 0.0F, 1.0F - m_dTableTopHeight, 0.0F,
    		1.0F, 1.0F, 1.0F );
   
	   	renderBlocks.renderStandardBlock( block, i, j, k );
	   
	   	if ( tableBlock.DoesTableHaveLeg( blockAccess, i, j, k ) )
	   	{
		   	// render leg
		   
		   	renderBlocks.setRenderBounds( 0.5F - m_dTableLegHalfWidth, 0.0F, 0.5F - m_dTableLegHalfWidth,
		   		0.5F + m_dTableLegHalfWidth, m_dTableLegHeight, 0.5F + m_dTableLegHalfWidth );
		   
		   	renderBlocks.renderStandardBlock( block, i, j, k );		   
	   	}
	   
	   	return true;
    }
    
    static public void RenderTableInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
    	// render top
        
 	   	renderBlocks.setRenderBounds( 0.0F, 1.0F - m_dTableTopHeight, 0.0F,
 	   		1.0F, 1.0F, 1.0F );
    
 	   	FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypeTable );
 	   
 	   	// render leg
	   
 	   	renderBlocks.setRenderBounds( 0.5F - m_dTableLegHalfWidth, 0.0F, 0.5F - m_dTableLegHalfWidth,
 	   		0.5F + m_dTableLegHalfWidth, m_dTableLegHeight, 0.5F + m_dTableLegHalfWidth );
    
 	   	FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, m_iSubtypeTable );
    }
}