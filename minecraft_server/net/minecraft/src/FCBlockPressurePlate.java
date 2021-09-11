// FCMOD

package net.minecraft.src;

public class FCBlockPressurePlate extends BlockPressurePlate
{
	protected static final double m_dHorizontalBorder = 0.0625D;
	protected static final double m_dHeightDepressed = 0.03125D;
	protected static final double m_dHeightResting = 0.0625D;
	
	protected static final double m_dHeightItem = 0.25D;
	protected static final double m_dHalfHeightItem = ( m_dHeightItem / 2D );

    protected FCBlockPressurePlate( int iBlockID, String iconName, Material material, EnumMobType mobType )
    {
    	super( iBlockID, iconName, material, mobType );
    	
        InitBlockBounds( m_dHorizontalBorder, 0D, m_dHorizontalBorder, 
        	1D - m_dHorizontalBorder, m_dHeightDepressed, 1D - m_dHorizontalBorder );
    }

    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	// override to prevent placing pressure plates on fences
    	
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );    
	}

	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
	@Override
    protected void func_94353_c_(int par1)
    {
    	// override to deprecate parent
    }
    
	@Override
    public void setBlockBoundsForItemRender()
    {
    	// override to deprecate parent
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        boolean bDepressed = getPowerSupply( blockAccess.getBlockMetadata( i, j, k ) ) > 0;

        if ( bDepressed )
        {
            return AxisAlignedBB.getAABBPool().getAABB( 
            	m_dHorizontalBorder, 0D, m_dHorizontalBorder, 
            	1D - m_dHorizontalBorder, m_dHeightDepressed, 1D - m_dHorizontalBorder );
        }
        else
        {
            return AxisAlignedBB.getAABBPool().getAABB( 
            	m_dHorizontalBorder, 0D, m_dHorizontalBorder, 
            	1D - m_dHorizontalBorder, m_dHeightResting, 1D - m_dHorizontalBorder );
        }
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
		// override to prevent pressure plates on fences and because this portion of the vanilla code seems to have the potential for item duplication
		
        boolean bOnInvalidSurface = false;

        if ( !world.doesBlockHaveSolidTopSurface( i, j - 1, k ) )
        {
            bOnInvalidSurface = true;
        }

        if ( bOnInvalidSurface )
        {
        	if ( world.getBlockId( i, j, k ) == blockID )
        	{
	            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
	            world.setBlockToAir( i, j, k );
        	}
        }
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean IsModFence( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		if ( block instanceof FCBlockSidingAndCornerAndDecorative )
    		{
    	    	int iSubtype = world.getBlockMetadata( i, j, k );
    	    	
    	    	return iSubtype == FCBlockSidingAndCornerAndDecorative.m_iSubtypeFence;    	    	
    		}
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
}