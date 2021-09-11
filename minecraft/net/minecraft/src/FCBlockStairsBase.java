// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockStairsBase extends Block
{
    protected FCBlockStairsBase( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        setLightOpacity( 255 );
        
        Block.useNeighborBrightness[iBlockID] = true;        
        
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
    public int getRenderType()
    {
        return 10;
    }

    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
    	NotifyAllNearbyBlocksFlat( world, i, j, k );
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	NotifyAllNearbyBlocksFlat( world, i, j, k );
    }
    
    @Override
    public int PreBlockPlacedBy( World world, int i, int j, int k, int iMetadata, EntityLiving entityBy ) 
    {
        int iFlatFacing = MathHelper.floor_float( ( entityBy.rotationYaw * 4.0F / 360.0F ) + 0.5F ) & 3;

        if ( iFlatFacing == 0 )
        {
        	iMetadata = SetDirection( iMetadata, 2 );
        }
        else if ( iFlatFacing == 1 )
        {
        	iMetadata = SetDirection( iMetadata, 1 );
        }
        else if ( iFlatFacing == 2 )
        {
        	iMetadata = SetDirection( iMetadata, 3 );
        }
        else // iFlatFacing == 3
        {
        	iMetadata = SetDirection( iMetadata, 0 );
        }
        
        
        return ValidateMetadataForLocation( world, i, j, k, iMetadata );
    }

    @Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		if ( iFacing == 0 || ( iFacing != 1 && fClickY > 0.5D ) )
		{
    		iMetadata = SetUpsideDown( iMetadata );
		}
    	
        return iMetadata;
    }

    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
    	AxisAlignedBB baseBox = GetBoundsFromPoolForBase( world, i, j, k ).offset( i, j, k );
    	
    	baseBox.AddToListIfIntersects( intersectingBox, list );
        
    	AxisAlignedBB secondaryBox = AxisAlignedBB.getAABBPool().getAABB( 
    		0D, 0D, 0D, 1D, 1D, 1D );
    	
        boolean bIsFullStep = GetBoundsForSecondaryPiece( world, i, j, k, secondaryBox );
        
        secondaryBox.offset( i, j, k );
        
        secondaryBox.AddToListIfIntersects( intersectingBox, list );

        if ( bIsFullStep )
        {
        	AxisAlignedBB tertiaryBox = AxisAlignedBB.getAABBPool().getAABB( 
        		0D, 0D, 0D, 1D, 1D, 1D );
        	
        	int iTertiaryFacing = GetBoundsForTertiaryPiece( world, i, j, k, tertiaryBox );
        	
        	if ( iTertiaryFacing >= 0 )
        	{
        		tertiaryBox.offset( i, j, k );
                
        		tertiaryBox.AddToListIfIntersects( intersectingBox, list );
        	}
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startVec, Vec3 endVec )
    {    	
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startVec, endVec );
    	
    	AxisAlignedBB baseBox = GetBoundsFromPoolForBase( world, i, j, k );
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( baseBox ); 
        
    	AxisAlignedBB secondaryBox = AxisAlignedBB.getAABBPool().getAABB( 
    		0D, 0D, 0D, 1D, 1D, 1D );
    	
        boolean bIsFullStep = GetBoundsForSecondaryPiece( world, i, j, k, secondaryBox );
        
        rayTrace.AddBoxWithLocalCoordsToIntersectionList( secondaryBox );

        if ( bIsFullStep )
        {
        	AxisAlignedBB tertiaryBox = AxisAlignedBB.getAABBPool().getAABB( 
        		0D, 0D, 0D, 1D, 1D, 1D );
        	
        	int iTertiaryFacing = GetBoundsForTertiaryPiece( world, i, j, k, tertiaryBox );
        	
        	if ( iTertiaryFacing >= 0 )
        	{
        		rayTrace.AddBoxWithLocalCoordsToIntersectionList( tertiaryBox );
        	}
        }
    	
        return rayTrace.GetFirstIntersection();
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	if ( iFacing == 0 )
    	{
    		return !GetIsUpsideDown( blockAccess, i, j, k ); 
    	}
    	else if ( iFacing == 1 )
    	{
    		return GetIsUpsideDown( blockAccess, i, j, k ); 
    	}
    	else 
    	{
    		int iBlockFacing = ConvertDirectionToFacing( GetDirection( blockAccess, i, j, k ) );
    		
    		if ( iFacing == iBlockFacing )
    		{
    			// the backside will only be partial if another stair block sits next to it, so it's safe to say that it's always a hardpoint
    			
    			return true;
    		}
    		else if ( iFacing != Block.GetOppositeFacing( iBlockFacing ) )
    		{
    			return HasSecondaryFullSurfaceToFacing( blockAccess, i, j, k, iFacing );
    		}
    	}    	
    	
		return false;
	}
    
    @Override
    public boolean IsStairBlock()
    {
    	return true;
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return true;
    }
    
    @Override
	public boolean HasContactPointToFullFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
    	return true;
	}
	
    @Override
	public boolean HasContactPointToSlabSideFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown )
	{
		int iStairFacing = ConvertDirectionToFacing( GetDirection( blockAccess, i, j, k ) );
		
		if ( iFacing == Block.GetOppositeFacing( iStairFacing ) )
		{
			return GetIsUpsideDown( blockAccess, i, j, k ) == bIsSlabUpsideDown;
		}
		
		return true;
	}
    
    @Override
	public boolean HasContactPointToStairNarrowVerticalFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing, int iStairFacing )
	{
		boolean bIsUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
		
		if ( bIsUpsideDown == ( iFacing == 1 ) )
		{
			return true;
		}
			
		int iMyStairFacing = ConvertDirectionToFacing( GetDirection( blockAccess, i, j, k ) );
		
		return iMyStairFacing != Block.GetOppositeFacing( iStairFacing );
	}
	
    @Override
    public boolean HasNeighborWithMortarInContact( World world, int i, int j, int k )
    {
    	int iFacing = ConvertDirectionToFacing( GetDirection( world, i, j, k ) );
    	boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
    	
    	return HasNeighborWithMortarInContact( world, i, j, k, iFacing, bIsUpsideDown );
    	
    }
    
    @Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    @Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    @Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		int iDirection = ( iMetadata & 3 ) + 2;
		
		iDirection = Block.RotateFacingAroundJ( iDirection, !bReverse );
		
		return ( iMetadata & (~3) ) | ( iDirection - 2 );		
	}
	
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
        return blockMaterial.GetMobsCanSpawnOn( world.provider.dimensionId );
    }

    //------------- Class Specific Methods ------------//
    
    protected int ValidateMetadataForLocation( World world, int i, int j, int k, int iMetadata )
    {
    	return iMetadata;
    }
    
    public boolean HasNeighborWithMortarInContact( World world, int i, int j, int k, int iFacing, boolean bIsUpsideDown )
    {
    	if ( !bIsUpsideDown )
    	{
    		if ( FCUtilsWorld.HasNeighborWithMortarInFullFaceContactToFacing( world, i, j, k, 0 ) ||
    			FCUtilsWorld.HasNeighborWithMortarInStairNarrowVerticalContactToFacing( world, i, j, k, 1, iFacing ) )
			{
				return true;
			}
    	}
    	else
    	{
    		if ( FCUtilsWorld.HasNeighborWithMortarInFullFaceContactToFacing( world, i, j, k, 1 ) ||
    			FCUtilsWorld.HasNeighborWithMortarInStairNarrowVerticalContactToFacing( world, i, j, k, 0, iFacing ) )
			{
				return true;
			}
    	}
    	
		int iHalfBlockFacing = Block.GetOppositeFacing( iFacing );

    	for ( int iTempFacing = 2; iTempFacing < 6; iTempFacing++ )
    	{
    		if ( iTempFacing == iHalfBlockFacing )
    		{
	    		if ( FCUtilsWorld.HasNeighborWithMortarInSlabSideContactToFacing( world, i, j, k, iTempFacing, bIsUpsideDown ) )
				{
					return true;
				}
    		}
    		else
    		{
        		if ( FCUtilsWorld.HasNeighborWithMortarInStairShapedContactToFacing( world, i, j, k, iTempFacing ) )
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    protected int ConvertDirectionToFacing( int iDirection )
    {
    	int iFacing = 5 - iDirection;
    	
    	return iFacing;    	
    }

    protected AxisAlignedBB GetBoundsFromPoolForBase( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetBoundsFromPoolForBase( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected AxisAlignedBB GetBoundsFromPoolForBase( int iMetadata )
    {
    	if ( GetIsUpsideDown( iMetadata ) )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F );
        }
        else
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F );
        }
    }

    protected boolean GetBoundsForSecondaryPiece( IBlockAccess blockAccess, int i, int j, int k, AxisAlignedBB box )
    {
        int iMetadata = blockAccess.getBlockMetadata(i, j, k);
        
        int iBlockDir = GetDirection( iMetadata );
        boolean bUpsideDown = GetIsUpsideDown( iMetadata );
        
        box.minY = 0.5F;
        box.maxY = 1.0F;

        if ( bUpsideDown )
        {
        	box.minY = 0.0F;
        	box.maxY = 0.5F;
        }

        box.minX = 0.0F;
        box.minZ = 0.0F;
        
        box.maxX = 1.0F;
        box.maxZ = 0.5F;
        
        boolean bIsFullStep = true;
        
        if ( iBlockDir == 0 )
        {
        	box.minX = 0.5F;
        	box.maxZ = 1.0F;
            
            int iNeighborID = blockAccess.getBlockId(i + 1, j, k);
            int iNeighborMetadata = blockAccess.getBlockMetadata(i + 1, j, k);

            if (IsStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
                int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 3 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata))
                {
                	box.maxZ = 0.5F;
                    bIsFullStep = false;
                }
                else if (iNeighborDir == 2 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata))
                {
                	box.minZ = 0.5F;
                    bIsFullStep = false;
                }
            }
        }
        else if (iBlockDir == 1)
        {
        	box.maxX = 0.5F;
        	box.maxZ = 1.0F;
            
            int iNeighborID = blockAccess.getBlockId(i - 1, j, k);
            int iNeighborMetadata = blockAccess.getBlockMetadata(i - 1, j, k);

            if (IsStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
                int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 3 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata))
                {
                	box.maxZ = 0.5F;
                    bIsFullStep = false;
                }
                else if (iNeighborDir == 2 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata))
                {
                	box.minZ = 0.5F;
                    bIsFullStep = false;
                }
            }
        }
        else if (iBlockDir == 2)
        {
        	box.minZ = 0.5F;
        	box.maxZ = 1.0F;
            
            int iNeighborID = blockAccess.getBlockId(i, j, k + 1);
            int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k + 1);

            if (IsStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
                int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 1 && !this.IsStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata))
                {
                	box.maxX = 0.5F;
                    bIsFullStep = false;
                }
                else if (iNeighborDir == 0 && !this.IsStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata))
                {
                	box.minX = 0.5F;
                    bIsFullStep = false;
                }
            }
        }
        else if (iBlockDir == 3)
        {
            int iNeighborID = blockAccess.getBlockId(i, j, k - 1);
            int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k - 1);

            if (IsStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
                int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 1 && !this.IsStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata))
                {
                	box.maxX = 0.5F;
                    bIsFullStep = false;
                }
                else if (iNeighborDir == 0 && !this.IsStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata))
                {
                	box.minX = 0.5F;
                    bIsFullStep = false;
                }
            }
        }

        return bIsFullStep;
    }

    protected AxisAlignedBB GetBoundsFromPoolForSecondaryPiece( int iMetadata )
    {
    	AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D );
    	
        int iBlockDir = GetDirection( iMetadata );
        boolean bUpsideDown = GetIsUpsideDown( iMetadata );
        
        box.minY = 0.5F;
        box.maxY = 1.0F;

        if ( bUpsideDown )
        {
        	box.minY = 0.0F;
        	box.maxY = 0.5F;
        }

        box.minX = 0.0F;
        box.minZ = 0.0F;
        
        box.maxX = 1.0F;
        box.maxZ = 0.5F;
        
        if ( iBlockDir == 0 )
        {
        	box.minX = 0.5F;
        	box.maxZ = 1.0F;            
        }
        else if (iBlockDir == 1)
        {
        	box.maxX = 0.5F;
        	box.maxZ = 1.0F;
        }
        else if (iBlockDir == 2)
        {
        	box.minZ = 0.5F;
        	box.maxZ = 1.0F;
        }
    	
        return box;        
    }
    
    /**
     * Returns the facing of the piece if it exists, -1 otherwise
     */
    protected int GetBoundsForTertiaryPiece( IBlockAccess blockAccess, int i, int j, int k, AxisAlignedBB box )
    {
        int iMetadata = blockAccess.getBlockMetadata(i, j, k);
        
        int iBlockDir = GetDirection( iMetadata );
        boolean bUpsideDown = GetIsUpsideDown( iMetadata );
        int iFacing = -1;
        
        box.minY = 0.5F;
        box.maxY = 1.0F;

        if ( bUpsideDown )
        {
        	box.minY = 0.0F;
        	box.maxY = 0.5F;
        }

        box.minX = 0.0F;
        box.minZ = 0.5F;
        
        box.maxX = 0.5F;
        box.maxZ = 1.0F;
        
        if (iBlockDir == 0)
        {
            int iNeighborBlockID = blockAccess.getBlockId(i - 1, j, k);
            int iNeighborMetadata = blockAccess.getBlockMetadata(i - 1, j, k);

            if (IsStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
            	int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 3 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata))
                {
                	box.minZ = 0.0F;
                	box.maxZ = 0.5F;
                    iFacing = 2;
                }
                else if (iNeighborDir == 2 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata))
                {
                	box.minZ = 0.5F;
                	box.maxZ = 1.0F;
                    iFacing = 3;
                }
            }
        }
        else if (iBlockDir == 1)
        {
        	int iNeighborBlockID = blockAccess.getBlockId(i + 1, j, k);
        	int iNeighborMetadata = blockAccess.getBlockMetadata(i + 1, j, k);

            if (IsStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
            	box.minX = 0.5F;
            	box.maxX = 1.0F;
                
                int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 3 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata))
                {
                	box.minZ = 0.0F;
                	box.maxZ = 0.5F;
                    iFacing = 2;
                }
                else if (iNeighborDir == 2 && !this.IsStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata))
                {
                	box.minZ = 0.5F;
                	box.maxZ = 1.0F;
                    iFacing = 3;
                }
            }
        }
        else if (iBlockDir == 2)
        {
        	int iNeighborBlockID = blockAccess.getBlockId(i, j, k - 1);
        	int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k - 1);

            if (IsStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
            	box.minZ = 0.0F;
            	box.maxZ = 0.5F;
                
                int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 1 && !this.IsStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata))
                {
                    iFacing = 4;
                }
                else if (iNeighborDir == 0 && !this.IsStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata))
                {
                	box.minX = 0.5F;
                	box.maxX = 1.0F;
                    iFacing = 5;
                }
            }
        }
        else if (iBlockDir == 3)
        {
        	int iNeighborBlockID = blockAccess.getBlockId(i, j, k + 1);
        	int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k + 1);

            if (IsStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4))
            {
            	int iNeighborDir = iNeighborMetadata & 3;

                if (iNeighborDir == 1 && !this.IsStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata))
                {
                    iFacing = 4;
                }
                else if (iNeighborDir == 0 && !this.IsStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata))
                {
                	box.minX = 0.5F;
                	box.maxX = 1.0F;
                    iFacing = 5;
                }
            }
        }

        return iFacing;
    }

    private boolean HasSecondaryFullSurfaceToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D );
    	
    	boolean bHasFullStep = GetBoundsForSecondaryPiece( blockAccess, i, j, k, box );
    	
    	if ( bHasFullStep )
    	{
    		if ( iFacing == GetBoundsForTertiaryPiece( blockAccess, i, j, k, box ) )
    		{
    			return true;
    		}
    	}
    		
    	return false;
    }
    
    protected boolean IsStairBlock( int iBlockID )
    {
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		return block.IsStairBlock();
    	}
    	
    	return false;
    }

    protected boolean GetIsUpsideDown( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsUpsideDown( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected boolean GetIsUpsideDown( int iMetadata )
    {
        return ( iMetadata & 4 ) != 0;
    }
    
    protected int SetUpsideDown( int iMetadata )
    {
    	return iMetadata | 4;
    }
    
    protected int SetIsUpsideDown( int iMetadata, boolean bUpsideDown )
    {
    	if ( bUpsideDown )
    	{
        	iMetadata |= 4;
    	}
    	else
    	{
        	iMetadata &= ~4;
    	}
    	
    	return iMetadata;
    }
    
    protected void SetIsUpsideDown( World world, int i, int j, int k, boolean bUpsideDown )
	{
    	int iMetadata = SetIsUpsideDown( world.getBlockMetadata( i, j, k ), bUpsideDown );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
    
    protected void SetDirection( World world, int i, int j, int k, int iDirection )
	{
        int iMetadata = SetDirection( world.getBlockMetadata( i, j, k ), iDirection );

        world.SetBlockMetadataWithNotify( i, j, k, iMetadata, 2 );
	}
    
    protected int SetDirection( int iMetadata, int iDirection )
	{
        iMetadata &= ~3;
        
        return iMetadata | iDirection;	
    }
    
    protected int GetDirection( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetDirection( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected int GetDirection( int iMetadata )
	{
    	return iMetadata & 3;
	}
    
    protected boolean IsStairBlockWithMetadata( IBlockAccess blockAccess, int i, int j, int k, int iMetadata )
    {
        return FCUtilsWorld.IsStairBlock( blockAccess, i, j, k ) && blockAccess.getBlockMetadata( i, j, k ) == iMetadata;
    }

    protected void NotifyAllNearbyBlocksFlat( World world, int i, int j, int k )
    {
    	// notify to reshape neighboring stairs and anything that might be connected to them
    	
        world.notifyBlockOfNeighborChange( i - 1, j, k, blockID );
        world.notifyBlockOfNeighborChange( i - 2, j, k, blockID );
        world.notifyBlockOfNeighborChange( i + 1, j, k, blockID );
        world.notifyBlockOfNeighborChange( i + 2, j, k, blockID );
        
        world.notifyBlockOfNeighborChange( i, j, k - 1, blockID );
        world.notifyBlockOfNeighborChange( i, j, k - 2, blockID );
        world.notifyBlockOfNeighborChange( i, j, k + 1, blockID );
        world.notifyBlockOfNeighborChange( i, j, k + 2, blockID );
        
        world.notifyBlockOfNeighborChange( i - 1, j, k - 1, blockID );
        world.notifyBlockOfNeighborChange( i - 1, j, k + 1, blockID );
        world.notifyBlockOfNeighborChange( i + 1, j, k - 1, blockID );
        world.notifyBlockOfNeighborChange( i + 1, j, k + 1, blockID );
    }
    
	//----------- Client Side Functionality -----------//
    
    private boolean m_bRenderingBase = false;
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderBlockStairs( renderer, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	if ( !m_bRenderingBase && iSide < 2 )
    	{
    		FCUtilsBlockPos myPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, 
    			GetOppositeFacing( iSide ) );
    		
    		if ( GetIsUpsideDown( blockAccess, myPos.i, myPos.j, myPos.k ) )
    		{
    			if ( iSide == 1 )
    			{
    				return false;
    			}
    		}
    		else
    		{
    			if ( iSide == 0 )
    			{
    				return false;
    			}
    		}
    	}
    	
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
    
    @Override
    public boolean ShouldRenderNeighborHalfSlabSide( IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown )
    {
		boolean bUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
		
		if ( bUpsideDown == bNeighborUpsideDown )
		{
			return false;
		}
		
		int iBlockFacing = ConvertDirectionToFacing( GetDirection( blockAccess, i, j, k ) );
		
		return iNeighborSlabSide != Block.GetOppositeFacing( iBlockFacing );
    }
    
    @Override
    public boolean ShouldRenderNeighborFullFaceSide( IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide )
    {
    	if ( iNeighborSide < 2 )
    	{
    		boolean bUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
    		
    		if ( iNeighborSide == 0 )
    		{
    			return !bUpsideDown;
    		}
    		else // iNeighborSide == 1
    		{
    			return bUpsideDown;
    		}    			
    	}
    	
		int iBlockFacing = ConvertDirectionToFacing( GetDirection( blockAccess, i, j, k ) );
		
		return iNeighborSide != Block.GetOppositeFacing( iBlockFacing );
    }
    
    private boolean renderBlockStairs( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	m_bRenderingBase = true;
    	
    	renderBlocks.setRenderBounds( GetBoundsFromPoolForBase( 
    		renderBlocks.blockAccess, i, j, k ) );
        
        renderBlocks.renderStandardBlock( this, i, j, k );
        
    	m_bRenderingBase = false;
    	
    	AxisAlignedBB secondaryBox = AxisAlignedBB.getAABBPool().getAABB( 
    		0D, 0D, 0D, 1D, 1D, 1D );
    	
        boolean bIsFullStep = GetBoundsForSecondaryPiece( renderBlocks.blockAccess, 
        	i, j, k, secondaryBox );
        
        renderBlocks.setRenderBounds( secondaryBox );
        
        renderBlocks.renderStandardBlock( this, i, j, k );

    	AxisAlignedBB tertiaryBox = AxisAlignedBB.getAABBPool().getAABB( 
    		0D, 0D, 0D, 1D, 1D, 1D );
    	
    	int iTertiaryFacing = GetBoundsForTertiaryPiece( renderBlocks.blockAccess, 
    		i, j, k, tertiaryBox );
    	
    	if ( iTertiaryFacing >= 0 )
    	{
        	renderBlocks.setRenderBounds( tertiaryBox );
        	
        	renderBlocks.renderStandardBlock( this, i, j, k );
        }

        return true;
    }
}