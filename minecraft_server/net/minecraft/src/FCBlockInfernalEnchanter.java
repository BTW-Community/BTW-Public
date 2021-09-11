// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockInfernalEnchanter extends BlockContainer
{
    public static final float m_fBlockHeight = 0.50F;
    public static final float m_fCandleHeight = 0.25F;

	private static final float m_fBlockHardness = 100F;
	private static final float m_fBlockExplosionResistance = 2000F;
	
	private static final int m_iHorizontalBookShelfCheckDistance = 8;
	private static final int m_iVerticalPositiveBookShelfCheckDistance = 8;
	private static final int m_iVerticalNegativeBookShelfCheckDistance = 8;
	
    protected FCBlockInfernalEnchanter( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialSoulforgedSteel );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, m_fBlockHeight, 1F );
        
        setLightOpacity( 0 );
        
        setHardness( m_fBlockHardness );
        setResistance( m_fBlockExplosionResistance );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "fcBlockInfernalEnchanter" );
        
        setCreativeTab( CreativeTabs.tabDecorations );
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityInfernalEnchanter();
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote )
        {
        	if ( player instanceof EntityPlayerMP ) // should always be true
        	{
        		FCContainerInfernalEnchanter container = new FCContainerInfernalEnchanter( player.inventory, world, i, j, k );
        		
        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcInfernalEnchanterContainerID );        		
        	}
        }
        
        return true;
    }
    
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    //------------- Class Specific Methods ------------//

	private boolean IsValidBookshelf( World world, int i, int j, int k )
	{
		int iBlockID = world.getBlockId( i, j, k );
		
		if ( iBlockID == Block.bookShelf.blockID )
		{
			// check around the bookshelf for an empty block to provide access
			
			if ( world.isAirBlock( i + 1, j, k ) ||
				world.isAirBlock( i - 1, j, k ) ||
				world.isAirBlock( i, j, k + 1 ) ||
				world.isAirBlock( i, j, k - 1 ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	//----------- Client Side Functionality -----------//
}