// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockLegacyCorner extends Block
{
    public final static int m_iNumSubtypes = 16;    
    
	public static final int m_iStoneTextureID = 1; 
	public static final int m_iWoodTextureID = 4; 
	
	private static final double m_dCornerWidth = 0.5D;
	private static final double m_dHalfCornerWidth = ( m_dCornerWidth  / 2D );
	
	private static final double fCornerWidthOffset = 1F - m_dCornerWidth;
	
	protected FCBlockLegacyCorner( int iBlockID )
	{
        super( iBlockID, Material.wood );

        setHardness( 1.5F );
        SetAxesEffectiveOn( true );        
        SetPicksEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
    	InitBlockBounds( 
    		0.5F - m_dHalfCornerWidth, 0.5F - m_dHalfCornerWidth, 0.5F - m_dHalfCornerWidth,  
    		0.5F + m_dHalfCornerWidth, 0.5F + m_dHalfCornerWidth, 0.5F + m_dHalfCornerWidth );
    	
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockCorner" );
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
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		if ( ( iMetadata & 8 ) == 0 ) // is wood
		{
			return FCBetterThanWolves.fcBlockWoodCornerItemStubID;
		}
		else
		{
			return FCBetterThanWolves.fcBlockSmoothStoneSidingAndCorner.blockID;
		}		
    }
	
	@Override
	public int damageDropped( int iMetadata )
    {
		if ( ( iMetadata & 8 ) != 0 ) // is stone
		{
			return 1;
		}
		
		return 0;		
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	double dMinX = 0D;
    	double dMaxX = dMinX + m_dCornerWidth;
    	
    	double dMinY = 0D;
    	double dMaxY = dMinY + m_dCornerWidth;
    	
    	double dMinZ = 0D;
    	double dMaxZ = dMinZ + m_dCornerWidth;
    	
    	if ( IsIOffset( blockAccess, i, j, k ) )
    	{
    		dMinX += fCornerWidthOffset;
    		dMaxX += fCornerWidthOffset;
    	}
    		
    	if ( IsJOffset( blockAccess, i, j, k ) )
    	{
    		dMinY += fCornerWidthOffset;
    		dMaxY += fCornerWidthOffset;
    	}
    	
    	if ( IsKOffset( blockAccess, i, j, k ) )
    	{
    		dMinZ += fCornerWidthOffset;
    		dMaxZ += fCornerWidthOffset;
    	}
    	
    	return AxisAlignedBB.getAABBPool().getAABB( dMinX, dMinY, dMinZ, dMaxX, dMaxY, dMaxZ );    	
	}
    
	private boolean IsPlayerClickOffsetOnAxis( float fPlayerClick )
	{
		if ( fPlayerClick > 0F ) // should always be true
		{
			if ( fPlayerClick >= 0.5F )
			{
				return true;
			}
		}
		
		return false;
	}
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	boolean bIOffset = false;
    	boolean bJOffset = false;
    	boolean bKOffset = false;
    	
    	if ( iFacing == 0 )
    	{
    		bJOffset = true;

    		bIOffset = IsPlayerClickOffsetOnAxis( fClickX );
    		bKOffset = IsPlayerClickOffsetOnAxis( fClickZ );;
    	}
    	else if ( iFacing == 1 )
    	{
    		bIOffset = IsPlayerClickOffsetOnAxis( fClickX );
    		bKOffset = IsPlayerClickOffsetOnAxis( fClickZ );;
    	}
    	else if ( iFacing == 2 )
    	{
    		bKOffset = true;

    		bIOffset = IsPlayerClickOffsetOnAxis( fClickX );
    		bJOffset = IsPlayerClickOffsetOnAxis( fClickY );;
    	}
    	else if ( iFacing == 3 )
    	{
    		bIOffset = IsPlayerClickOffsetOnAxis( fClickX );
    		bJOffset = IsPlayerClickOffsetOnAxis( fClickY );;
    	}
    	else if ( iFacing == 4 )
    	{
    		bIOffset = true;

    		bJOffset = IsPlayerClickOffsetOnAxis( fClickY );
    		bKOffset = IsPlayerClickOffsetOnAxis( fClickZ );;
    	}
    	else if ( iFacing == 5 )
    	{
    		bJOffset = IsPlayerClickOffsetOnAxis( fClickY );
    		bKOffset = IsPlayerClickOffsetOnAxis( fClickZ );;
    	}
    	
    	return SetCornerAlignmentInMetadata( iMetadata, bIOffset, bJOffset, bKOffset );
    }
    
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
		return GetIsStone( world, i, j, k );
    }
	
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
		if ( !GetIsStone( world, i, j, k ) )
		{
			return FCBetterThanWolves.fcItemGear.itemID;
		}
		
		return super.GetItemIDDroppedOnSaw( world, i, j, k );
    }

	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
		if ( !GetIsStone( world, i, j, k ) )
		{
			return 2;
		}
		
		return super.GetItemIDDroppedOnSaw( world, i, j, k );
    }
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !IsJOffset( blockAccess, i, j, k );
	}
	
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		boolean bIOffset = IsIOffset( iMetadata );
		boolean bJOffset = IsJOffset( iMetadata );
		boolean bKOffset = IsKOffset( iMetadata );
		
		if ( bReverse )
		{
			if ( bIOffset )
			{
				if ( bKOffset )
				{
					bIOffset = false;
				}
				else
				{
					bKOffset = true;
				}
			}
			else
			{
				if ( bKOffset )
				{
					bKOffset = false;
				}
				else
				{
					bIOffset = true;
				}
			}
		}
		else
		{
			if ( bIOffset )
			{
				if ( bKOffset )
				{
					bKOffset = false;
				}
				else
				{
					bIOffset = false;
				}
			}
			else
			{
				if ( bKOffset )
				{
					bIOffset = true;
				}
				else
				{
					bKOffset = true;
				}
			}
		}
		
		return SetCornerAlignmentInMetadata( iMetadata, bIOffset, bJOffset, bKOffset );		
	}

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{
		int iAlignment = GetCornerAlignment( world, i, j, k );
		
		if ( !bReverse )
		{
			iAlignment++;
			
			if ( iAlignment > 7 )
			{
				iAlignment = 0;
			}
		}
		else
		{
			iAlignment--;
			
			if ( iAlignment < 0 )
			{
				iAlignment = 7;				
			}
		}

		SetCornerAlignment( world, i, j, k, iAlignment );
		
        return true;
	}	
	
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
    	if ( iItemDamage == 0 )
    	{
    		return FCBlockPlanks.GetFurnaceBurnTimeByWoodType( 0 ) / 8;
    	}
    	
    	return 0;
    }
    
    //------------- Class Specific Methods ------------//
    
    // bit 2 indicates i offset.  bit 1 indicates j offset.  bit 0 indicates k offset
    public int GetCornerAlignment( IBlockAccess iBlockAccess, int i, int j, int k )
	{
    	return ( ( iBlockAccess.getBlockMetadata( i, j, k ) ) & 7 );
	}
    
    public void SetCornerAlignment( World world, int i, int j, int k, int iAlignment )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & 8; // filter out the old alignment
    	
    	iMetaData |= iAlignment;
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
    public void SetCornerAlignment( World world, int i, int j, int k, 
		boolean bIAligned, boolean bJAligned, boolean bKAligned )
    {
    	int iAlignment = 0;
    	
    	if ( bIAligned )
    	{
    		iAlignment |= 4;
    	}
    	
    	if ( bJAligned )
    	{
    		iAlignment |= 2;
    	}
    	
    	if ( bKAligned )
    	{
    		iAlignment |= 1;
    	}
    	
    	SetCornerAlignment( world, i, j, k, iAlignment );    	
    }
    
    public int SetCornerAlignmentInMetadata( int iMetadata, int iAlignment )
    {
    	iMetadata = iMetadata & 8; // filter out the old alignment
    	
    	iMetadata |= iAlignment;
    	
        return iMetadata;
    }
    
    public int SetCornerAlignmentInMetadata( int iMetadata, boolean bIAligned, boolean bJAligned, boolean bKAligned )
    {
    	int iAlignment = 0;
    	
    	if ( bIAligned )
    	{
    		iAlignment |= 4;
    	}
    	
    	if ( bJAligned )
    	{
    		iAlignment |= 2;
    	}
    	
    	if ( bKAligned )
    	{
    		iAlignment |= 1;
    	}
    	
    	return SetCornerAlignmentInMetadata( iMetadata, iAlignment );    	
    }
        
    public boolean IsIOffset( IBlockAccess iBlockAccess, int i, int j, int k )
	{
    	return IsIOffset( iBlockAccess.getBlockMetadata( i, j, k ) );
	}
    
    public boolean IsIOffset( int iMetadata )
	{
    	return ( iMetadata & 4 ) > 0;
	}
    
    public boolean IsJOffset( IBlockAccess iBlockAccess, int i, int j, int k )
	{
    	return IsJOffset( iBlockAccess.getBlockMetadata( i, j, k ) );
	}
    
    public boolean IsJOffset( int iMetadata )
	{
    	return ( iMetadata & 2 ) > 0;
	}
    
    public boolean IsKOffset( IBlockAccess iBlockAccess, int i, int j, int k )
	{
    	return IsKOffset( iBlockAccess.getBlockMetadata( i, j, k ) );
	}
    
    public boolean IsKOffset( int iMetadata )
	{
    	return ( iMetadata & 1 ) > 0;
	}
    
    public boolean GetIsStone( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( ( iBlockAccess.getBlockMetadata( i, j, k ) ) & 8 ) > 0;
    }
    
    public void SetIsStone( World world, int i, int j, int k, boolean bStone )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & 7; // filter out the old alignment
    	
    	if ( bStone )
    	{
    		iMetaData |= 8;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
	//----------- Client Side Functionality -----------//
	
    public Icon m_IconWood;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" );
        m_IconWood = register.registerIcon( "wood" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	if ( ( iMetadata & 8 ) > 0 )
    	{
    		return blockIcon;
    	}
    	else
    	{
    		return m_IconWood;
    	}
    }
    
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }
	
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
}