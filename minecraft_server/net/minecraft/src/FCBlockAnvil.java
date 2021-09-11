// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockAnvil extends Block
{
	FCModelBlockAnvil m_model = new FCModelBlockAnvil();
	
    protected FCBlockAnvil( int iBlockID )
    {
        super( iBlockID, Material.anvil );
    	
    	setHardness( 50F );
    	setResistance( 10F );
    	SetPicksEffectiveOn();
    	
        setLightOpacity( 0 );
        
    	setStepSound( soundAnvilFootstep );
    	
    	setUnlocalizedName( "anvil" );
        
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
    public int quantityDropped( Random rand )
    {
        return 7;
    }

    @Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemMetalFragment.itemID;
    }
    
	@Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        if ( !canSilkHarvest( iMetadata ) || !EnchantmentHelper.getSilkTouchModifier( player ) )
        {
            world.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyedWithImproperToolAuxFXID, i, j, k, blockID + ( iMetadata << 12 ) );
        }
        
        super.harvestBlock( world, player, i, j, k, iMetadata );
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	// regular harvest regardless of tool type
    	
		dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	@Override
    protected boolean canSilkHarvest( int iMetadata )
    {
    	return true;
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, iFacing ); 
    }
    
	@Override
    public int PreBlockPlacedBy( World world, int i, int j, int k, int iMetadata, EntityLiving entityBy ) 
	{
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacing( entityBy );

		return SetFacing( iMetadata, iFacing );
	}
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
        {
        	int iMetadata = world.getBlockMetadata( i, j, k );
        	
            world.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyedWithImproperToolAuxFXID, i, j, k, blockID + ( iMetadata << 12 ) );
            
            dropBlockAsItem( world, i, j, k, iMetadata, 0 );
            
            world.setBlockToAir( i, j, k );
        }
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
	{
		// prevent access if solid block above
		
        if ( !world.isRemote && !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j + 1, k, 0 ) )
        {
        	if ( player instanceof EntityPlayerMP ) // should always be true
        	{
        		FCContainerWorkbench container = new FCContainerWorkbench( player.inventory, world, i, j, k );
        		
        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcVanillaAnvilContainerID );        		
        	}
        }

        return true;
	}	

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	int iFacing = GetFacing( world, i, j, k );
    	
    	FCModelBlock transformedModel = m_model.MakeTemporaryCopy();
    	
    	transformedModel.RotateAroundJToFacing( iFacing );
    	
    	return transformedModel.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
		// fucked up metadata due to vanilla legacy block
		
		int iOrientation = iMetadata & 3;
		
		if ( ( iOrientation & 1 ) == 0 )
		{
			if ( iOrientation == 0 )
			{
				return 3;
			}
			
			return 2;
		}
		else
		{
			if ( iOrientation == 1 )
			{
				return 4;
			}
			
			return 5;
		}
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		int iOrientation;
		
		if ( iFacing == 2 )
		{
			iOrientation = 2;
		}
		else if ( iFacing == 3 )
		{
			iOrientation = 0;
		}
		else if ( iFacing == 4 )
		{
			iOrientation = 1;
		}
		else // iFacing == 5
		{
			iOrientation = 3;
		}
		
		iMetadata &= ~3; // filter out old facing
		
		return iMetadata | iOrientation;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	//----------- Client Side Functionality -----------//
}