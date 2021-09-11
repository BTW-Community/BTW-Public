// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockMoulding extends Block
{
	protected static final double m_dMouldingWidth = 0.5D;
	protected static final double m_dMouldingHalfWidth = ( m_dMouldingWidth / 2D );
	
	protected static final double m_dMouldingLength = 1D;
	
    private static final int[][] m_iFacingOfConnections = // indexed by axis and alignment
    {
    	{ -1, 4, -1, 5, 5, 4, 4, 5, -1, 4, -1, 5 }, // i Axis
    	{ 1, 1, 1, 1, -1, -1, -1, -1, 0, 0, 0, 0 }, // j Axis
    	{ 3, -1, 2, -1, 3, 3, 2, 2, 3, -1, 2, -1 }  // k Axis
    };

    private static final int[][] m_iAlignmentOffsetAlongAxis = // indexed by axis and alignment
    {
    	{ 0, 1, 0, -1, -1, 1, 1, -1, 0, 1, 0, -1 }, // i Axis
    	{ -1, -1, -1, -1, 0, 0, 0, 0, 1, 1, 1, 1 }, // j Axis
    	{ -1, 0, 1, 0, -1, -1, 1, 1, -1, 0, 1, 0 }  // k Axis
    };

	protected int m_iMatchingCornerBlockID;
	
	String m_sTextureName;
	
	protected FCBlockMoulding( int iBlockID, Material material, String sTextureName, 
		int iMatchingCornerBlockID, float fHardness, float fResistance, 
		StepSound stepSound, String name )
	{
        super( iBlockID, material );        
        
        setHardness( fHardness );
        setResistance( fResistance );
        
    	InitBlockBounds( 0.5D - m_dMouldingHalfWidth, 0.5D - m_dMouldingHalfWidth, 0D,  
    		0.5D + m_dMouldingHalfWidth, 0.5D + m_dMouldingHalfWidth, m_dMouldingLength  );
    	
        setStepSound( stepSound );
        
        setUnlocalizedName( name );
        
        m_iMatchingCornerBlockID = iMatchingCornerBlockID;
        
        m_sTextureName = sTextureName;
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
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startRay, Vec3 endRay )
    {
    	int iAlignment = GetMouldingAlignment( world, i, j, k );
    	
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( 
    		world, i, j, k, startRay, endRay );
    	
        // check for intersection with the base block
        
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( GetBlockBoundsFromPoolForAlignment( 
    		iAlignment ) );
    	
    	// check for intersections with extensions
    	
        for ( int iAxis = 0; iAxis <= 2; iAxis++ )
        {
        	AxisAlignedBB tempBox = GetBlockBoundsFromPoolForConnectingBlocksAlongAxis( 
        		world, i, j, k, iAxis );
        	
        	if ( tempBox != null )
        	{
        		rayTrace.AddBoxWithLocalCoordsToIntersectionList( tempBox );
        	}
        }
        
    	return rayTrace.GetFirstIntersection();
    }
    
	@Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
    	int iAlignment = GetMouldingAlignment( world, i, j, k );
    	
        // check for intersection with the base block
        
    	GetBlockBoundsFromPoolForAlignment( iAlignment ).offset( i, j, k ).
    		AddToListIfIntersects( intersectingBox, list );
    	
    	// check for intersections with extensions
    	
        for ( int iAxis = 0; iAxis <= 2; iAxis++ )
        {
        	AxisAlignedBB tempBox = GetBlockBoundsFromPoolForConnectingBlocksAlongAxis( 
        		world, i, j, k, iAxis );
        	
        	if ( tempBox != null )
        	{
        		tempBox.offset( i, j, k ).AddToListIfIntersects( intersectingBox, list );
        	}
        }
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	int iAlignment = 0;
    	
    	float fXOffsetFromCenter = Math.abs( fClickX - 0.5F );
    	float fYOffsetFromCenter = Math.abs( fClickY - 0.5F );
    	float fZOffsetFromCenter = Math.abs( fClickZ - 0.5F );
    	
    	switch ( iFacing )
    	{
    		case 0:
    			
    			if ( fXOffsetFromCenter > fZOffsetFromCenter )
    			{
    				if ( fClickX > 0.5F )
    				{    				
    					iAlignment = 9;
    				}
    				else
    				{
    					iAlignment = 11;
    				}
    			}
    			else
    			{
    				if ( fClickZ > 0.5F )
    				{    				
    					iAlignment = 10;
    				}
    				else
    				{
    					iAlignment = 8;
    				}
    			}
    			
    			break;
    			
    		case 1:
    			
    			if ( fXOffsetFromCenter > fZOffsetFromCenter )
    			{
    				if ( fClickX > 0.5F )
    				{    				
    					iAlignment = 1;
    				}
    				else
    				{
    					iAlignment = 3;
    				}
    			}
    			else
    			{
    				if ( fClickZ > 0.5F )
    				{    				
    					iAlignment = 2;
    				}
    				else
    				{
    					iAlignment = 0;
    				}
    			}
    			
    			break;
    			
    		case 2:
    			
    			if ( fXOffsetFromCenter > fYOffsetFromCenter )
    			{
    				if ( fClickX > 0.5F )
    				{    				
    					iAlignment = 6;
    				}
    				else
    				{
    					iAlignment = 7;
    				}
    			}
    			else
    			{
    				if ( fClickY > 0.5F )
    				{    				
    					iAlignment = 10;
    				}
    				else
    				{
    					iAlignment = 2;
    				}
    			}
    			
    			break;
    			
    		case 3:
    			
    			if ( fXOffsetFromCenter > fYOffsetFromCenter )
    			{
    				if ( fClickX > 0.5F )
    				{    				
    					iAlignment = 5;
    				}
    				else
    				{
    					iAlignment = 4;
    				}
    			}
    			else
    			{
    				if ( fClickY > 0.5F )
    				{    				
    					iAlignment = 8;
    				}
    				else
    				{
    					iAlignment = 0;
    				}
    			}
    			
    			break;
    			
    		case 4:
    			
    			if ( fZOffsetFromCenter > fYOffsetFromCenter )
    			{
    				if ( fClickZ > 0.5F )
    				{    				
    					iAlignment = 6;
    				}
    				else
    				{
    					iAlignment = 5;
    				}
    			}
    			else
    			{
    				if ( fClickY > 0.5F )
    				{    				
    					iAlignment = 9;
    				}
    				else
    				{
    					iAlignment = 1;
    				}
    			}
    			
    			break;
    			
    		default: // 5
    			
    			if ( fZOffsetFromCenter > fYOffsetFromCenter )
    			{
    				if ( fClickZ > 0.5F )
    				{    				
    					iAlignment = 7;
    				}
    				else
    				{
    					iAlignment = 4;
    				}
    			}
    			else
    			{
    				if ( fClickY > 0.5F )
    				{    				
    					iAlignment = 11;
    				}
    				else
    				{
    					iAlignment = 3;
    				}
    			}
    			
    			break;    			
    	}
    	
    	return SetMouldingAlignmentInMetadata( iMetadata, iAlignment );
    }
    
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iAlignment = GetMouldingAlignment( blockAccess, i, j, k );
    	
		return ( iAlignment < 8 );
	}
	
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
    	int iAlignment = GetMouldingAlignmentFromMetadata( iMetadata );
    	
		if ( bReverse )
		{
			iAlignment++;
			
			if ( iAlignment == 4 )
			{
				iAlignment = 0;
			}
			else if ( iAlignment == 8 )
			{
				iAlignment = 4;
			}
			else if ( iAlignment >= 12 )
			{
				iAlignment = 8;
			}			
		}
		else
		{
			iAlignment--;
			
			if ( iAlignment < 0 )
			{
				iAlignment = 3;
			}
			else if ( iAlignment == 3 )
			{
				iAlignment = 7;
			}
			else if ( iAlignment == 7 )
			{
				iAlignment = 11;
			}
		}
		
		iMetadata = SetMouldingAlignmentInMetadata( iMetadata, iAlignment );
		
		return iMetadata;
	}

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iAlignment = GetMouldingAlignment( world, i, j, k );
		
		if ( !bReverse )
		{
			iAlignment++;
			
			if ( iAlignment > 11 )
			{
				iAlignment = 0;
			}
		}
		else
		{
			iAlignment--;
			
			if ( iAlignment < 0 )
			{
				iAlignment = 11;				
			}
		}

		SetMouldingAlignment( world, i, j, k, iAlignment );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
        return true;
	}
	
	@Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
		int iAlignment = GetMouldingAlignment( world, i, j, k );
		
		if ( iAlignment < 4 )
		{
			return -0.5F;
		}
		
		return 0F;		
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolForAlignment( GetMouldingAlignment( blockAccess, i, j, k ) );
    }

    //------------- Class Specific Methods ------------//
    
    public AxisAlignedBB GetBlockBoundsFromPoolForAlignment( int iAlignment )
    {    
    	AxisAlignedBB bounds = AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D ); 
    	
    	if ( iAlignment == 0 )
    	{
    		bounds.maxY = bounds.minY + m_dMouldingWidth;
    		bounds.maxZ = bounds.minZ + m_dMouldingWidth;
    	}
    	else if ( iAlignment == 1 )
    	{    		
    		bounds.minX += 1D - m_dMouldingWidth;
    		bounds.maxY = bounds.minY + m_dMouldingWidth;
    	}
    	else if ( iAlignment == 2 )
    	{
    		bounds.maxY = bounds.minY + m_dMouldingWidth;
    		bounds.minZ += 1D - m_dMouldingWidth;
    	}
    	else if ( iAlignment == 3 )
    	{
    		bounds.maxX = bounds.minX + m_dMouldingWidth;
    		bounds.maxY = bounds.minY + m_dMouldingWidth;
    	}
    	else if ( iAlignment == 4 )
    	{
    		bounds.maxX = bounds.minX + m_dMouldingWidth;
    		bounds.maxZ = bounds.minZ + m_dMouldingWidth;
    	}
    	else if ( iAlignment == 5 )
    	{
    		bounds.minX += 1D - m_dMouldingWidth;
    		bounds.maxZ = bounds.minZ + m_dMouldingWidth;
    	}
    	else if ( iAlignment == 6 )
    	{
    		bounds.minX += 1D - m_dMouldingWidth;
    		bounds.minZ += 1D - m_dMouldingWidth;
    	}
    	else if ( iAlignment == 7 )
    	{
    		bounds.maxX = bounds.minX + m_dMouldingWidth;
    		bounds.minZ += 1D - m_dMouldingWidth;
    	}
    	else if ( iAlignment == 8 )
    	{
    		bounds.minY += 1D - m_dMouldingWidth;
    		bounds.maxZ = bounds.minZ + m_dMouldingWidth;
    	}
    	else if ( iAlignment == 9 )
    	{
    		bounds.minX += 1D - m_dMouldingWidth;
    		bounds.minY += 1D - m_dMouldingWidth;
    	}
    	else if ( iAlignment == 10 )
    	{
    		bounds.minY += 1D - m_dMouldingWidth;
    		bounds.minZ += 1D - m_dMouldingWidth;
    	}
    	else // 11
    	{
    		bounds.maxX = bounds.minX + m_dMouldingWidth;
    		bounds.minY += 1D - m_dMouldingWidth;
    	}
    	
    	return bounds;
    }
    
    protected boolean IsMouldingOfSameType( IBlockAccess blockAccess, int i, int j, int k )
    {    	
    	return blockAccess.getBlockId( i, j, k ) == blockID;
    }
    
    public int GetMouldingAlignment( IBlockAccess iBlockAccess, int i, int j, int k )
	{
    	return GetMouldingAlignmentFromMetadata( iBlockAccess.getBlockMetadata( i, j, k ) );
	}    
    
    public int GetMouldingAlignmentFromMetadata( int iMetadata )
	{
    	return iMetadata;
	}    
    
    public void SetMouldingAlignment( World world, int i, int j, int k, int iAlignment )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	iMetadata = SetMouldingAlignmentInMetadata( iMetadata, iAlignment );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public int SetMouldingAlignmentInMetadata( int iMetadata, int iAlignment )
    {
        return iAlignment;
    }    
    
	private float ClickOffsetFromCenter( float fClickPos )
	{
		return Math.abs( fClickPos - 0.5F );
	}
	
    static private void OffsetCornerBoundingBoxAlongAxis( int iAxis, int iOffset, Vec3 min, Vec3 max )
    {
    	if ( iOffset > 0 )
    	{
    		if ( iAxis == 0 )
    		{
    			min.xCoord += 0.5D;
    			max.xCoord += 0.5D;
    		}
    		else if ( iAxis == 1 )
    		{
    			min.yCoord += 0.5D;
    			max.yCoord += 0.5D;
    		}
    		else // 2
    		{
    			min.zCoord += 0.5D;
    			max.zCoord += 0.5D;
    		}
    	}
    }
    
    private boolean IsAlignedAlongAxis( IBlockAccess blockAccess, int i, int j, int k, int iAxis )
    {
		int iAlignment = GetMouldingAlignment( blockAccess, i, j, k );
		
		// if the target moulding runs along the axis of connection 
		// it could not itself connect along it
		
		return m_iFacingOfConnections[iAxis][iAlignment] < 0;
    }
    
    /*
     * returns -1 if no connecting moulding present
     */
    private int GetAlignmentOfConnectingMouldingAtLocation( IBlockAccess blockAccess, 
    	int i, int j, int k, int iAlignmentToConnectTo, int iAxisToConnectAlong )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	
    	if ( IsMouldingOfSameType( blockAccess, i, j, k ) &&
    		IsAlignedAlongAxis( blockAccess, i, j, k, iAxisToConnectAlong ) )
    	{
    		int iTargetAlignment = GetMouldingAlignment( blockAccess, i, j, k );

			for ( int iTempAxis = 0; iTempAxis <= 2; iTempAxis++ )
			{
				if ( iTempAxis != iAxisToConnectAlong )
				{
					if ( m_iFacingOfConnections[iTempAxis][iTargetAlignment] ==
						m_iFacingOfConnections[iTempAxis][iAlignmentToConnectTo] )
					{
						return iTargetAlignment;
					}
				}
			}
    	}
    	
    	return -1;
    }
    
    /**
     * returns -1 if no connecting corner present
     */
    private int GetConnectingCornerFacingAtLocation( IBlockAccess blockAccess, int i, int j, int k, int iAlignmentToConnectTo, int iAxisToConnectAlong )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );

    	if ( iBlockID == m_iMatchingCornerBlockID )
    	{
    		FCBlockSidingAndCorner corner = (FCBlockSidingAndCorner)Block.blocksList[iBlockID];
    		
        	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        	
    		if ( corner.GetIsCorner( iMetadata ) )
    		{    		
    	    	int iCornerFacing = corner.GetFacing( iMetadata );
    	    	
	    		// make sure the corner is flush with the side the moulding is on
	    		
	    		if ( m_iAlignmentOffsetAlongAxis[iAxisToConnectAlong][iAlignmentToConnectTo] == 
	    			corner.GetCornerAlignmentOffsetAlongAxis( iCornerFacing, iAxisToConnectAlong ) )
	    		{
					int iReturnValue = -1;
					
	    			for ( int iTempAxis = 0; iTempAxis <= 2; iTempAxis++ )
	    			{    				
	    				if ( iTempAxis != iAxisToConnectAlong )
	    				{
							if ( m_iAlignmentOffsetAlongAxis[iTempAxis][iAlignmentToConnectTo] == 0 )
							{
								iReturnValue = iCornerFacing;
							}
							else
							{
					    		if ( m_iAlignmentOffsetAlongAxis[iTempAxis][iAlignmentToConnectTo] != 
					    			corner.GetCornerAlignmentOffsetAlongAxis( iCornerFacing, 
				    				iTempAxis ) )
					    		{
					    			// the corner does not match up
					    			
					    			return -1;
					    		}
							}
	    				}
	    			}
	    			
	    			return iReturnValue;
	    		}
    		}
    	}
    	
    	return -1;
    }
    
    private AxisAlignedBB GetBlockBoundsFromPoolForConnectingBlocksAlongAxis( 
    	IBlockAccess blockAccess, int i, int j, int k, int iAxis )
    {
    	int iAlignment = GetMouldingAlignment( blockAccess, i, j, k );
    	
		int iConnectionToFacing = m_iFacingOfConnections[iAxis][iAlignment];
		
		if ( iConnectionToFacing >= 0 )
		{    		                              
    		FCUtilsBlockPos connectingPos = new FCUtilsBlockPos( i, j, k, iConnectionToFacing );
    		
    		int iConnectingAlignment = GetAlignmentOfConnectingMouldingAtLocation( blockAccess, 
    			connectingPos.i, connectingPos.j, connectingPos.k, iAlignment, iAxis );        		
    		
    		if ( iConnectingAlignment >= 0 )
    		{
    			return GetBlockBoundsFromPoolForConnectingMoulding( iConnectionToFacing, 
    				iConnectingAlignment );
			}
    		else
    		{
    			int iConnectingFacing = GetConnectingCornerFacingAtLocation( blockAccess, 
    				connectingPos.i, connectingPos.j, connectingPos.k, iAlignment, iAxis );
    			
    			if ( iConnectingFacing >= 0 )
    			{
    				return GetBlockBoundsFromPoolForConnectingCorner( iAxis,
    					m_iAlignmentOffsetAlongAxis[iAxis][iAlignment], iConnectingFacing );        			
				}
    		}
		}
    	
    	return null;
    }
    
    private AxisAlignedBB GetBlockBoundsFromPoolForConnectingMoulding( int iToFacing, int 
    	iToAlignment )
    {    	
    	AxisAlignedBB box = GetBlockBoundsFromPoolForAlignment( iToAlignment ); 

    	// clip the connecting molding's box to the direction of connection
    	
    	if ( iToFacing == 0 )
    	{
    		box.maxY = 0.5F;
    	}
    	else if ( iToFacing == 1 )
    	{
    		box.minY = 0.5F;
    	}
    	else if ( iToFacing == 2 )
    	{
    		box.maxZ = 0.5F;
    	}
    	else if ( iToFacing == 3 )
    	{
    		box.minZ = 0.5F;
    	}
    	else if ( iToFacing == 4 )
    	{
    		box.maxX = 0.5F;
    	}
    	else // if ( iToFacing == 5 )
    	{
    		box.minX = 0.5F;
    	}
    	
    	return box;
    }
    
    private AxisAlignedBB GetBlockBoundsFromPoolForConnectingCorner( int iConnectingAxis, 
    	int iOffsetAlongAxis, int iCornerFacing )
    {
    	Vec3 cornerMin = Vec3.createVectorHelper( 0, 0, 0 );
    	Vec3 cornerMax = Vec3.createVectorHelper( 0.5, 0.5, 0.5 );

    	OffsetBoundingBoxForConnectingCorner( iConnectingAxis, 
    		iOffsetAlongAxis, iCornerFacing, cornerMin, cornerMax );
    	
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		cornerMin.xCoord, cornerMin.yCoord, cornerMin.zCoord, 
			cornerMax.xCoord, cornerMax.yCoord, cornerMax.zCoord );    	
    }
    
    private void OffsetBoundingBoxForConnectingCorner( int iConnectingAxis, int iOffsetAlongAxis, int iCornerFacing, Vec3 boundingMin, Vec3 boundingMax )
    {
        for ( int iTempAxis = 0; iTempAxis <= 2; iTempAxis++ )
        {
        	if ( iTempAxis == iConnectingAxis )
        	{
        		OffsetCornerBoundingBoxAlongAxis( iTempAxis, -iOffsetAlongAxis, boundingMin, boundingMax );
        	}
        	else
        	{
        		OffsetCornerBoundingBoxAlongAxis( iTempAxis, 
        			FCBlockSidingAndCorner.GetCornerAlignmentOffsetAlongAxis( 
    				iCornerFacing, iTempAxis ), boundingMin, boundingMax );
        	}
        }
    }
    
	//----------- Client Side Functionality -----------//
}