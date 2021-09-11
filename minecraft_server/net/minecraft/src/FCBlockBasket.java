// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockBasket extends BlockContainer
{
    protected FCBlockBasket( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialBasket );
        
        setHardness( 0.05F );        
		
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.WICKER );
		
        setStepSound( soundGrassFootstep );        
        
        setCreativeTab( CreativeTabs.tabDecorations );        
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        FCTileEntityBasket tileEntity = (FCTileEntityBasket)world.getBlockTileEntity( i, j, k );
        
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, iFacing ); 
    }
    
	@Override
    public int PreBlockPlacedBy( World world, int i, int j, int k, int iMetadata, EntityLiving entityBy ) 
	{
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed( entityBy );

		return SetFacing( iMetadata, iFacing );
	}
	
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
		{
			return false;
		}
		
        return super.canPlaceBlockAt( world, i, j, k );
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockToAir( i, j, k );
        }
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
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
        return false;
	}
	
	//------------- Class Specific Methods ------------//
	
	public void SetHasContents( World world, int i, int j, int k, boolean bHasContents )
	{
		int iMetadata = SetHasContents( world.getBlockMetadata( i, j, k ), bHasContents );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetHasContents( int iMetadata, boolean bHasContents )
	{
		if ( bHasContents )
		{
			iMetadata |= 4;
		}
		else
		{
			iMetadata &= (~4);
		}
		
		return iMetadata;
	}
	
	public boolean GetHasContents( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetHasContents( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public boolean GetHasContents( int iMetadata )
	{
		return ( iMetadata & 4 ) != 0;
	}
	
	public void SetIsOpen( World world, int i, int j, int k, boolean bOpen )
	{
		int iMetadata = SetIsOpen( world.getBlockMetadata( i, j, k ), bOpen );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetIsOpen( int iMetadata, boolean bOpen )
	{
		if ( bOpen )
		{
			iMetadata |= 8;
		}
		else
		{
			iMetadata &= (~8);
		}
		
		return iMetadata;
	}
	
	public boolean GetIsOpen( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsOpen( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public boolean GetIsOpen( int iMetadata )
	{
		return ( iMetadata & 8 ) != 0;
	}
	
	public abstract FCModelBlock GetLidModel( int iMetadata );
	
	public abstract Vec3 GetLidRotationPoint();
	
	//----------- Client Side Functionality -----------//
}
