// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockLegacyOmniSlab extends Block
{
    public static final int m_iNumSubtypes = 2;
    
	protected static final double m_dSlabHeight = 0.5D;
	
	protected FCBlockLegacyOmniSlab( int iBlockID )
	{
        super( iBlockID, Material.wood );

        setHardness( 2F );        
        SetAxesEffectiveOn();
        SetPicksEffectiveOn();
        
        SetBuoyancy( 1F );
        
    	InitBlockBounds( 0D, 0D, 0D, 1D, 1D, m_dSlabHeight );
    	
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockOmniSlab" );
	}
	
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
		if ( ( iMetadata & 1 ) > 0 ) // is wood
		{
			return FCBetterThanWolves.fcBlockWoodSidingItemStubID;
		}
		else
		{
			return FCBetterThanWolves.fcBlockSmoothStoneSidingAndCorner.blockID;
		}		
    }
	
	@Override
	public int damageDropped( int iMetadata )
    {
		return 0;
    }
	
	@Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        switch ( iFacing )
        {
	        case 0:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, m_dSlabHeight, 0D, 
            		1D, 1D, 1D );
	        	
	        case 1:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.0F, 0.0F, 0.0F, 
            		1.0F, m_dSlabHeight, 1.0F );
	        	
	        case 2:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.0F, 0.0F, m_dSlabHeight, 
            		1.0F, 1.0F, 1.0F );
	        	
	        case 3:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.0F, 0.0F, 0.0F, 
            		1.0F, 1.0F, m_dSlabHeight );
	        	
	        case 4:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		m_dSlabHeight, 0.0F, 0.0F, 
            		1.0F, 1.0F, 1.0F );
	        	
	        default: // 5
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0.0F, 0.0F, 0.0F, 
        			m_dSlabHeight, 1.0F, 1.0F );
        }    	
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, iFacing );
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, 
		int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iBlockFacing = GetFacing( blockAccess, i, j, k );
    	
    	return iFacing == Block.GetOppositeFacing( iBlockFacing );  
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

		return iFacing != 0;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iFacing = GetFacing( blockAccess, i, j, k );
		
		if ( iFacing > 1 )
		{
			// sideways facing blocks can transmit
			
			return true;
		}

		return false;
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
        return true;
	}

	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
		return !IsSlabWood( world, i, j, k );
    }
	
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
		if ( IsSlabWood( world, i, j, k ) )
		{
			return FCBetterThanWolves.fcBlockWoodMouldingItemStubID;
		}
		
		return super.GetItemIDDroppedOnSaw( world, i, j, k );
    }

	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
		if ( IsSlabWood( world, i, j, k ) )
		{
			return 2;
		}
		
		return super.GetItemIDDroppedOnSaw( world, i, j, k );
    }
    
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
    	if ( ( iItemDamage & 1 ) > 0 )
    	{
    		return FCBlockPlanks.GetFurnaceBurnTimeByWoodType( 0 ) / 2;
    	}
    	
    	return 0;
    }
    
    //------------- Class Specific Methods ------------//
    
	public boolean IsSlabWood( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( ( iBlockAccess.getBlockMetadata( i, j, k ) ) & 1 ) > 0;
    }
   
	//----------- Client Side Functionality -----------//
}