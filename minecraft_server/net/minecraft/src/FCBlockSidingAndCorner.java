//FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockSidingAndCorner extends Block
{
	protected static final double m_dSidingHeight = 0.5D;
	
	protected static final double m_dCornerWidth = 0.5D;
	protected static final double m_dCornerWidthOffset = ( 1D - m_dCornerWidth );
	
	String m_sTextureName;
	
	protected FCBlockSidingAndCorner( int iBlockID, Material material, String sTextureName, float fHardness, float fResistance, StepSound stepSound, String name )
	{
        super( iBlockID, material );

        setHardness( fHardness );
        setResistance( fResistance );
        
        setStepSound( stepSound );
        setUnlocalizedName( name );
        
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
    public int damageDropped( int iMetadata )
    {
		return ( iMetadata & 1 );
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        if ( !GetIsCorner( blockAccess, i, j, k ) )
        {
        	AxisAlignedBB sidingBox = AxisAlignedBB.getAABBPool().getAABB( 
        		0D, 0D, 0D, 1D, m_dSidingHeight, 1D );
        	
        	sidingBox.TiltToFacingAlongJ( iFacing );
        	
        	return sidingBox;
        }
        else
        {        
        	AxisAlignedBB cornerBox = AxisAlignedBB.getAABBPool().getAABB( 
        		0D, 0D, 0D, m_dCornerWidth, m_dCornerWidth, m_dCornerWidth );
        	
	    	if ( IsCornerFacingIOffset( iFacing ) )
	    	{
	    		cornerBox.minX += m_dCornerWidthOffset;
	    		cornerBox.maxX += m_dCornerWidthOffset;
	    	}
	    		
	    	if ( IsCornerFacingJOffset( iFacing ) )
	    	{
	    		cornerBox.minY += m_dCornerWidthOffset;
	    		cornerBox.maxY += m_dCornerWidthOffset;
	    	}
	    	
	    	if ( IsCornerFacingKOffset( iFacing ) )
	    	{
	    		cornerBox.minZ += m_dCornerWidthOffset;
	    		cornerBox.maxZ += m_dCornerWidthOffset;
	    	}

	    	return cornerBox;
        }
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		if ( !GetIsCorner( iMetadata ) )
		{
	        int iSlabFacing = iFacing;
	        
	        return SetFacing( iMetadata, iSlabFacing );
		}
		else
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
	    	
	        return SetCornerFacingInMetadata( iMetadata, bIOffset, bJOffset, bKOffset );
		}
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	if ( !GetIsCorner( blockAccess, i, j, k ) )
    	{
	    	int iBlockFacing = GetFacing( blockAccess, i, j, k );
	    	
	    	return iFacing == Block.GetOppositeFacing( iBlockFacing );
    	}
    	
    	return false;
	}
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	if ( super.CanGroundCoverRestOnBlock( world, i, j, k ) )
    	{
    		return true;
    	}
    	else if ( !GetIsCorner( world, i, j, k ) )
    	{
            int iFacing = GetFacing( world, i, j, k );
            
            if ( iFacing == 1 )
            {
            	return true;
            }            
    	}
    	
    	return false;
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( !GetIsCorner( blockAccess, i, j, k ) )
    	{
            int iFacing = GetFacing( blockAccess, i, j, k );
            
            if ( iFacing == 1 )
            {
            	return -0.5F;
            }            
    	}
    	
    	return 0F;
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
    	return ( iMetadata >> 1 );
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata &= 1; // filter out the previous facing
    	
    	iMetadata |= ( iFacing << 1 );
    	
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{	
		int iFacing = GetFacing( blockAccess, i, j, k );	
		
		if ( !GetIsCorner( blockAccess, i, j, k ) )
		{
			return iFacing != 0;
		}
		else
		{		
			return !IsCornerFacingJOffset( iFacing );
		}
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		if ( !GetIsCorner( blockAccess, i, j, k ) )
		{
			int iFacing = GetFacing( blockAccess, i, j, k );
			
			if ( iFacing > 1 )
			{
				// sideways facing blocks can transmit
				
				return true;
			}
		}

		return false;
	}
	
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		int iFacing = GetFacing( iMetadata );
		
		if ( (iMetadata & 1 ) == 0 ) // corner test
		{
			return super.RotateMetadataAroundJAxis( iMetadata, bReverse );
		}
		else
		{
			boolean bIOffset = IsCornerFacingIOffset( iFacing );
			boolean bJOffset = IsCornerFacingJOffset( iFacing );
			boolean bKOffset = IsCornerFacingKOffset( iFacing );
			
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
			
			return SetCornerFacingInMetadata( iMetadata, bIOffset, bJOffset, bKOffset );		
		}
	}

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );	
		
		if ( !GetIsCorner( world, i, j, k ) )
		{		
			iFacing = Block.CycleFacing( iFacing, bReverse );
	
		}
		else
		{
			if ( !bReverse )
			{
				iFacing++;
				
				if ( iFacing > 7 )
				{
					iFacing = 0;
				}
			}
			else
			{
				iFacing--;
				
				if ( iFacing < 0 )
				{
					iFacing = 7;				
				}
			}
		}
		
		SetFacing( world, i, j, k, iFacing );
        
        return true;
	}
	
	@Override
    public float MobSpawnOnVerticalOffset( World world, int i, int j, int k )
    {
		int iFacing = GetFacing( world, i, j, k );	
		
		if ( GetIsCorner( world, i, j, k ) )
		{
			if ( !IsCornerFacingJOffset( iFacing ) )
        	{
				return -0.5F;			
        	}
		}
		else if ( iFacing == 1 )
		{
			return -0.5F;			
		}
		
		return 0F;
    }
	
    //------------- Class Specific Methods ------------//

	public boolean GetIsCorner( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return GetIsCorner( iBlockAccess.getBlockMetadata( i, j, k ) );
	}
   
	public boolean GetIsCorner( int iMetadata )
	{
		return ( iMetadata & 1 ) > 0;
	}
   
	public boolean IsCornerFacingIOffset( int iFacing )
	{
		return ( iFacing & 4 ) > 0;
	}
   
	public boolean IsCornerFacingJOffset( int iFacing )
	{
		return ( iFacing & 2 ) > 0;
	}
   
	public boolean IsCornerFacingKOffset( int iFacing )
	{
		return ( iFacing & 1 ) > 0;
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
    
    public void SetCornerFacing( World world, int i, int j, int k, 
    		boolean bIAligned, boolean bJAligned, boolean bKAligned )
    {
    	int iFacing = 0;
    	
    	if ( bIAligned )
    	{
    		iFacing |= 4;
    	}
    	
    	if ( bJAligned )
    	{
    		iFacing |= 2;
    	}
    	
    	if ( bKAligned )
    	{
    		iFacing |= 1;
    	}
    	
    	SetFacing( world, i, j, k, iFacing );    	
    }
    
    public int SetCornerFacingInMetadata( int iMetadata, boolean bIAligned, boolean bJAligned, boolean bKAligned )
    {
    	int iFacing = 0;
    	
    	if ( bIAligned )
    	{
    		iFacing |= 4;
    	}
    	
    	if ( bJAligned )
    	{
    		iFacing |= 2;
    	}
    	
    	if ( bKAligned )
    	{
    		iFacing |= 1;
    	}
    	
    	return SetFacing( iMetadata, iFacing );    	
    }
    
    /**
     * iAxis = 0 is i axis, 1 is j, 2 is k
     */
    public static int GetCornerAlignmentOffsetAlongAxis( int iCornerFacing, int iAxis )
    {
    	if ( ( iCornerFacing & ( 4 >> iAxis ) ) > 0 )
    	{
    		return 1;
    	}
    	
    	return -1;
    }

	//----------- Client Side Functionality -----------//
}