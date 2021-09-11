// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchBase extends Block
{
    protected FCBlockTorchBase( int iBlockID )
    {
        super( iBlockID, Material.circuits );
    	
    	setHardness( 0F );
    	
    	SetBuoyant();
		SetFilterableProperties( Item.m_iFilterable_Narrow );
    	
    	setStepSound( soundWoodFootstep );
    	
        setTickRandomly( true );
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
	{
		return null;
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
        return 2;
    }

    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	for ( int iFacing = 2; iFacing < 6; iFacing++ )
    	{
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    		
    		if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, 
    			Block.GetOppositeFacing( iFacing ) ) )
    		{
    			return true;
    		}
    	}
    	
        return canPlaceTorchOn( world, i, j - 1, k );
    }
    
    @Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        if ( iFacing == 1  )
        {
        	if ( canPlaceTorchOn( world, i, j - 1, k ) )
    		{
        		iMetadata = SetOrientation( iMetadata, 5 );
    		}
        }
        else if ( iFacing != 0 )
        {
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );
    		
    		if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, iFacing ) )
    		{
    			iMetadata = SetOrientation( iMetadata, 6 - iFacing );
    		}
    	}
    	
    	return iMetadata;
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        super.updateTick(world, i, j, k, rand);

        if ( GetOrientation( world, i, j, k ) == 0 )
        {
            onBlockAdded( world, i, j, k );
        }
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        if ( GetOrientation( world, i, j, k ) == 0 )
        {
        	for ( int iFacing = 1; iFacing < 6; iFacing++ )
        	{
        		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );
        		
        		if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, 
        			iFacing ) )
        		{
                    SetOrientation( world, i, j, k, 6 - iFacing );
                    
                    return;
        		}
        	}
        	
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborID )
    {
        ValidateState( world, i, j, k, iNeighborID );
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iOrientation = GetOrientation( blockAccess, i, j, k );
        
        float fTorchWidth = 0.15F;

        if ( iOrientation == 1 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		0F, 0.2F, 0.5F - fTorchWidth, 
        		fTorchWidth * 2F, 0.8F, 0.5F + fTorchWidth );
        }
        else if ( iOrientation == 2 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		1F - fTorchWidth * 2F, 0.2F, 0.5F - fTorchWidth, 
        		1F, 0.8F, 0.5F + fTorchWidth );
        }
        else if (iOrientation == 3)
        {
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		0.5F - fTorchWidth, 0.2F, 0.0F, 
        		0.5F + fTorchWidth, 0.8F, fTorchWidth * 2.0F );
        }
        else if (iOrientation == 4)
        {
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		0.5F - fTorchWidth, 0.2F, 1.0F - fTorchWidth * 2.0F, 
        		0.5F + fTorchWidth, 0.8F, 1.0F );
        }
        else // 5
        {
            fTorchWidth = 0.1F;
            
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		0.5F - fTorchWidth, 0.0F, 0.5F - fTorchWidth, 
        		0.5F + fTorchWidth, 0.6F, 0.5F + fTorchWidth );
        }
    }
    
    @Override
	public boolean IsBlockRestingOnThatBelow( IBlockAccess blockAccess, int i, int j, int k )
	{
        return GetOrientation( blockAccess, i, j, k ) == 5;
	}

    @Override
    public int GetFacing( int iMetadata )
    {
    	// facing is opposite side attached to    	
    	
    	return MathHelper.clamp_int( 6 - GetOrientation( iMetadata ), 1, 5 );
    }
    
    @Override
    public int SetFacing( int iMetadata, int iFacing )
    {
    	iFacing = MathHelper.clamp_int( iFacing, 1, 5 );
    	
    	return SetOrientation( iMetadata, 6 - iFacing );
    }
    
	@Override
    public boolean CanRotateAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		return iFacing == Block.GetOppositeFacing( GetFacing( world, i, j, k ) );
    }
    
	@Override
    public int GetNewMetadataRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing )
    {
		int iOldMetadata = world.getBlockMetadata( i, j, k );
		
		return SetFacing( iOldMetadata, Block.GetOppositeFacing( iRotatedFacing ) );
    }
	
	@Override
	public void OnNeighborDisrupted( World world, int i, int j, int k, int iToFacing )
	{
		if ( iToFacing == Block.GetOppositeFacing( GetFacing( world, i, j, k ) ) )
		{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
		}
	}
	
    //------------- Class Specific Methods ------------//    
	
    protected boolean canPlaceTorchOn( World world, int i, int j, int k)
    {
    	return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing( world, i, j, k, 1, true );
    }
    
    protected boolean ValidateState( World world, int i, int j, int k, int iNeighborID )
    {
    	// checks for drop on a neighbor block change
    	
        int iOrientation = GetOrientation( world, i, j, k );
        int iFacing = 0;
        
        if ( iOrientation != 0 ) // only do further tests if the torch has already been initialized
        {
			iFacing = 6 - iOrientation;
			
	        boolean bShouldDrop = false;
	        
	        if ( iFacing == 1  )
	        {
	        	if ( !canPlaceTorchOn( world, i, j - 1, k ) )
	    		{
	        		bShouldDrop = true;
	    		}
	        }
	        else
	        {
	    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, Block.GetOppositeFacing( iFacing ) );
	    		
	    		if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, targetPos.i, targetPos.j, targetPos.k, iFacing ) )
	    		{
	    			bShouldDrop = true;
	    		}
	    	}
	            
	        if ( bShouldDrop )
	        {
	            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
	            world.setBlockWithNotify( i, j, k, 0 );
	            
	            return true;
	        }
        }      
        
        return false;
    }
    
    public int GetOrientation( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetOrientation( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    static public int GetOrientation( int iMetadata )
    {
    	return iMetadata & 7;
    }
    
    public void SetOrientation( World world, int i, int j, int k, int iOrientation )
    {
		int iMetadata = SetOrientation( world.getBlockMetadata( i, j, k ), iOrientation );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    static public int SetOrientation( int iMetadata, int iOrientation )
    {
    	iMetadata &= (~7);
    	
    	return iMetadata | iOrientation;
    }
    
    public boolean IsRainingOnTorch( World world, int i, int j, int k )
    {
    	return world.isRaining() && world.IsRainingAtPos( i, j, k );
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	return renderBlocks.renderBlockTorch( this, i, j, k );
    }
}
