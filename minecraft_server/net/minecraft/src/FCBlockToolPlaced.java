// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockToolPlaced extends BlockContainer
{
	private static final double m_dBoundingThickness = ( 1D / 8D );
	private static final double m_dBoundingHalfThickness = ( m_dBoundingThickness / 2D );
	
    public FCBlockToolPlaced( int iBlockID )
    {
        super( iBlockID, Material.circuits );
        
        setHardness( 0.05F );        
		
        setStepSound( soundWoodFootstep );        
        
        setUnlocalizedName( "fcBlockToolPlaced" );
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityToolPlaced();
    }

	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		FCTileEntityToolPlaced tileEntity = (FCTileEntityToolPlaced)world.getBlockTileEntity( i, j, k );
        
        tileEntity.EjectContents();
        
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );	        
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
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
	{
		return 0;
	}
	
	@Override
    protected boolean canSilkHarvest()
    {
        return false;
    }    

	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, iFacing ); 
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		int iBlockFacing = GetAttachedToFacing( world, i, j, k ); 
		
		FCUtilsBlockPos attachedPos = new FCUtilsBlockPos( i, j, k, iBlockFacing );
		
        if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, attachedPos.i, attachedPos.j, attachedPos.k, 
        	Block.GetOppositeFacing( iBlockFacing ), true ) )
        {
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {    	
		FCTileEntityToolPlaced tileEntity = (FCTileEntityToolPlaced)world.getBlockTileEntity( i, j, k );
        
        ItemStack cookStack = tileEntity.GetToolStack();
        
		if ( cookStack != null )
		{
			FCUtilsItem.GivePlayerStackOrEjectFavorEmptyHand( player, cookStack, i, j, k );
			
			tileEntity.SetToolStack( null );
			
            world.setBlockWithNotify( i, j, k, 0 );
            
			return true;
		}
		
		return false;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
		return null;
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
		int iMetadata = blockAccess.getBlockMetadata( i, j,k );
		
		int iFacing = GetFacing( iMetadata );
		int iLevel = GetVerticalOrientation( iMetadata );
		
		float fHeight = 0.75F;
		float fWidth = 0.75F;
		
		FCTileEntityToolPlaced tileEntity = (FCTileEntityToolPlaced)blockAccess.getBlockTileEntity( i, j, k );
		
		if ( tileEntity != null )
		{
			ItemStack toolStack = tileEntity.GetToolStack();
			
			if ( toolStack != null )
			{
				fHeight = ((FCItemTool)toolStack.getItem()).GetBlockBoundingBoxHeight();
				fWidth = ((FCItemTool)toolStack.getItem()).GetBlockBoundingBoxWidth();
			}
		}
		
		double fXMin = 0.5F - ( fWidth / 2F );
		double fXMax = 0.5F + ( fWidth / 2F );
		
		double fZMin = fXMin;
		double fZMax = fXMax;
		
		double fYMin = fXMin;
		double fYMax = fXMax;
		
		if ( iFacing < 4 )
		{
			fXMin = 0.5F - m_dBoundingHalfThickness;
			fXMax = 0.5F + m_dBoundingHalfThickness;
		}
		else
		{
			fZMin = 0.5F - m_dBoundingHalfThickness;
			fZMax = 0.5F + m_dBoundingHalfThickness;
		}
		
		if ( iLevel == 0 )
		{
			fYMin = 0F;
			fYMax = fHeight;
		}
		else if ( iLevel == 1 )
		{
			fYMin = 1F - fHeight;
			fYMax = 1F;
		}
		else
		{
			if ( iFacing == 2 )
			{
				fZMin = 0;
				fZMax = fHeight;
			}
			else if ( iFacing == 3 )
			{
				fZMin = 1F - fHeight;
				fZMax = 1F;
			}
			else if ( iFacing == 4 )
			{
				fXMin = 0;
				fXMax = fHeight;
			}
			else if ( iFacing == 5 )
			{
				fXMin = 1F - fHeight;
				fXMax = 1F;
			}
		}
		
    	return AxisAlignedBB.getAABBPool().getAABB( fXMin, fYMin, fZMin, fXMax, fYMax, fZMax );
    }
	
	@Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
		return ( iMetadata & 3 ) + 2;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= ~3; // filter out old facing
		
		iMetadata |= MathHelper.clamp_int( iFacing, 2, 5 ) - 2; // convert to flat facing
		
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
    public boolean CanRotateAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( GetVerticalOrientation( iMetadata ) == 2 )
		{
			return iFacing == GetFacing( iMetadata );
		}
		
		return false;
    }
    
	@Override
    public boolean OnRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		// can't actually rotate around block due to tile entity, so pop the tool off
		
		world.setBlockToAir( i, j, k );
		
    	return false;
    }
	
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
	@Override
	public void OnNeighborDisrupted( World world, int i, int j, int k, int iToFacing )
	{
		if ( iToFacing == GetAttachedToFacing( world, i, j, k ) )
		{
            world.setBlockWithNotify( i, j, k, 0 );
		}
	}
	
	//------------- Class Specific Methods ------------//
	
	/**
	 * 0 = down, 1 = up, 2 = side
	 */
	public int GetVerticalOrientation( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetVerticalOrientation( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public int GetVerticalOrientation( int iMetadata )
	{
		return ( iMetadata & 12 ) >> 2;
	}
	
	public void SetVerticalOrientation( World world, int i, int j, int k, int iLevel )
	{
		int iMetadata = SetVerticalOrientation( world.getBlockMetadata( i, j, k ), iLevel );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetVerticalOrientation( int iMetadata, int iLevel )
	{
		iMetadata &= ~12; // filter out old facing
		
		iMetadata |= iLevel << 2;
		
		return iMetadata;
	}
	
	protected int GetAttachedToFacing( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iFacing = GetVerticalOrientation( blockAccess, i, j, k );
		
		if ( iFacing >= 2 )
		{
			iFacing = GetFacing( blockAccess, i, j, k );
		}
		
		return iFacing;
	}
	
	//----------- Client Side Functionality -----------//
}