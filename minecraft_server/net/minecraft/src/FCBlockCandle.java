// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCandle extends Block
{
	private static final double m_dStickHeight = ( 6D / 16D ); 
	private static final double m_dStickWidth = ( 2D / 16D ); 
	private static final double m_dStickHalfWidth = ( m_dStickWidth / 2D );
	
	private static final double m_dWickHeight = ( 1D / 16D );
	private static final double m_dWickWidth = ( 0.5D / 16D );
	private static final double m_dWickHalfWidth = ( m_dWickWidth / 2D );
	
    public FCBlockCandle( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialCandle );
        
        setHardness( 0F );    	
    	SetPicksEffectiveOn( true );        
        SetAxesEffectiveOn( true );        
        
        setLightValue( 1F );
        
    	InitBlockBounds( 0.5D - m_dStickHalfWidth, 0D, 0.5D - m_dStickHalfWidth, 
    		0.5D + m_dStickHalfWidth, m_dStickHeight, 0.5D + m_dStickHalfWidth );
    	
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockCandle" );        
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
		int iBlockBelowMetadata = world.getBlockMetadata( i, j - 1, k ) ;
		
		if ( iBlockBelowID == FCBetterThanWolves.fcAestheticNonOpaque.blockID && iBlockBelowMetadata == FCBlockAestheticNonOpaque.m_iSubtypeLightningRod )
		{
			return true;
		}

		return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( world, i, j - 1, k, 1, true );
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
		return null;
    }
	
	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemCandle.itemID;
    }
	
	@Override
    public int damageDropped( int iMetadata )
    {
    	return iMetadata;
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	// pop the block off if it no longer has a valid anchor point
    	
		if ( !canPlaceBlockAt( world, i, j, k ) )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
		}
    }
	
    @Override
	public boolean IsBlockRestingOnThatBelow( IBlockAccess blockAccess, int i, int j, int k )
	{
        return true;
	}

    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
	public void OnNeighborDisrupted( World world, int i, int j, int k, int iToFacing )
	{
		if ( iToFacing == 0 )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
		}
	}
	
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}