// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockRope extends Block
{
	public static final double m_dRopeWidth = ( 2D / 16D );
	public static final double m_dRopeHalfWidth = ( m_dRopeWidth / 2D );
	
	protected FCBlockRope( int iBlockID )
	{
        super( iBlockID, Material.circuits );

        setHardness( 0.5F );        
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
    	InitBlockBounds( 0.5D - m_dRopeHalfWidth, 0D, 0.5D - m_dRopeHalfWidth, 
    		0.5D + m_dRopeHalfWidth, 1D, 0.5D + m_dRopeHalfWidth );
    	
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "fcBlockRope" );
    }
	
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemRope.itemID;
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	int iBlockAboveID = world.getBlockId( i, j + 1, k );
    	
    	boolean bSupported = true;
    	
    	if ( iBlockAboveID == FCBetterThanWolves.fcAnchor.blockID )
    	{
    		int iFacing = ((FCBlockAnchor)FCBetterThanWolves.fcAnchor).GetFacing( world, i, j + 1, k );
    		
    		if ( iFacing == 1 )
    		{
    			// upwards facing anchors can't support rope below
    			
    			bSupported = false;
    		}
    	}
    	else if ( iBlockAboveID != blockID &&
			iBlockAboveID != FCBetterThanWolves.fcPulley.blockID )
    	{
    		bSupported = false;
    	}
    	
    	if ( !bSupported )
    	{
    		// the block above is no longer a rope, valid anchor, or pulley.  Self Destruct.
    		
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
    	}
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	int iBlockAboveID = world.getBlockId( i, j + 1, k );
    	
    	if ( iBlockAboveID == blockID ||
    			iBlockAboveID == FCBetterThanWolves.fcAnchor.blockID )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
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
	public boolean IsBlockClimbable( World world, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean HasSmallCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, 
		int iFacing, boolean bIgnoreTransparency )
	{
		// so lightning rods can attach to the bottom of rope for chandeliers and such
		
		return iFacing == 0;
	}
	
    //------------- Class Specific Methods ------------//
    
    public void BreakRope( World world, int i, int j, int k )
    {
		FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcItemRope.itemID, 0 );
		
        world.playAuxSFX( 2001, i, j, k, blockID );
        
		world.setBlockWithNotify( i, j, k, 0 );		
    }
    
	//----------- Client Side Functionality -----------//
    
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }
	
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	if ( iSide < 2 )
    	{
    		int iNeighborBlockID = blockAccess.getBlockId( iNeighborI, iNeighborJ, iNeighborK );
    		
    		return iNeighborBlockID != blockID && super.shouldSideBeRendered( blockAccess, 
    			iNeighborI, iNeighborJ, iNeighborK, iSide ); 
		}
    	
        return true;
    }
}