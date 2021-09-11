// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockCompanionCube extends Block
{
    public final static int m_iNumSubtypes = 16;    
    
    public FCBlockCompanionCube( int iBlockID )
    {
        super( iBlockID, Material.cloth );  
        
        setHardness( 0.4F );
        
        SetBuoyancy( 1F );
        
		SetFireProperties( FCEnumFlammability.CLOTH );
		
        setStepSound( Block.soundClothFootstep );

        setUnlocalizedName( "fcBlockCompanionCube" );        
        
        setCreativeTab( CreativeTabs.tabBlock );
    }

	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		if ( !GetIsSlabFromMetadata( iMetadata ) )
		{
			return SetFacing( iMetadata, Block.GetOppositeFacing( iFacing ) );        
		}
		else
		{
	        if ( iFacing == 0 || iFacing != 1 && (double)fClickY > 0.5D )
	        {
	        	return SetFacing( iMetadata, 1 );
	        }
		}
		
		return iMetadata;
    }
	
	@Override
    public int damageDropped( int iMetaData )
    {
    	if ( ( iMetaData & 8 ) > 0 ) // is slab
    	{
    		return 1;
    	}
    	
        return 0; 
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
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( GetIsSlab( blockAccess, i, j, k ) )
    	{
    		if ( !GetIsUpsideDownSlab( blockAccess, i, j, k ) )
    		{
            	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 0F, 1F, 0.5F, 1F );
    		}
    		else
    		{
            	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0.5F, 0F, 1F, 1F, 1F );
    		}
    	}
    	else
    	{
        	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 0F, 1F, 1F, 1F );
    	}
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityliving, ItemStack stack )
	{
    	if ( !GetIsSlab( world, i, j, k ) )
		{
    		SpawnHearts( world, i, j, k );
    		
			int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacingReversed( entityliving );
			
	        SetFacing( world, i, j, k, iFacing );        
		}
	}
	
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	if ( !GetIsSlab( world, i, j, k ) )
		{
	        world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.wolf.whine", 
	        		0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}
    }
    
	@Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
    	if ( !GetIsSlab( world, i, j, k ) )
		{
	        world.playAuxSFX( FCBetterThanWolves.m_iCompanionCubeDeathAuxFXID, i, j, k, 0 );
		}
    	
		super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
    }
    
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
    	return false;
    }

	@Override
    public boolean OnBlockSawed( World world, int i, int j, int k, int iSawPosI, int iSawPosJ, int iSawPosK )
    {
		int iSawFacing = ((FCBlockSaw)FCBetterThanWolves.fcSaw).GetFacing( world, iSawPosI, iSawPosJ, iSawPosK );
		
		if ( !GetIsSlab( world, i, j, k ) )
		{
			if ( iSawFacing == 0 || iSawFacing == 1 )
			{
				// if the saw is facing up or down, eject two seperate halves as items
				
				for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
				{
					FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
						FCBetterThanWolves.fcCompanionCube.blockID, 1 );
				}
				
		    	world.setBlockWithNotify( i, j, k, 0 );
			}
			else
			{
				// if the saw ir oriented towards the sides, it only chops off the top
				// and leaves a half-cube in its place
				
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					FCBetterThanWolves.fcCompanionCube.blockID, 1 );
				
				SetIsSlab( world, i, j, k, true );
				SetFacing( world, i, j, k, 0 );
				
				world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );				
			}
			
			FCUtilsBlockPos bloodPos = new FCUtilsBlockPos( i, j, k );
			
			bloodPos.AddFacingAsOffset( Block.GetOppositeFacing( iSawFacing ) );

			// FCTODO: Replace these particles don't currently work
	        // EmitBloodParticles( world, bloodPos.i, bloodPos.j, bloodPos.k, world.rand );
			
            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
            		"mob.wolf.hurt", 5.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);            
		}
		else
		{
			if ( iSawFacing == 0 || iSawFacing == 1 )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
						FCBetterThanWolves.fcCompanionCube.blockID, 1 );
				
		    	world.setBlockWithNotify( i, j, k, 0 );
			}
			else
			{
				return false;
			}
		}
		
		return true;
    }
	
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		if ( GetIsSlab( blockAccess, i, j, k ) )
		{
			if ( GetIsUpsideDownSlab( blockAccess, i, j, k ) )
			{
				return iFacing == 1;
			}
			else
			{
				return iFacing == 0;
			}
		}
		
		return true;
	}
    
	@Override
	public int GetFacing( int iMetadata )
	{
		return iMetadata & ~8;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= 8; // filter out old state
		
		iMetadata |= iFacing;
		
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !GetIsSlab( blockAccess, i, j, k );
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !GetIsSlab( blockAccess, i, j, k );
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return !GetIsSlab( blockAccess, i, j, k );
	}
	
	@Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		if ( !GetIsSlab( world, i, j, k ) )
		{
			if ( super.RotateAroundJAxis( world, i, j, k, bReverse ) )
			{
		        if ( world.rand.nextInt( 12 ) == 0 )
		        {
			        world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.wolf.whine", 
			        		0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F );
		        }
				
				return true;
			}
		}
		
		return false;
	}

	@Override
    public boolean IsNormalCube( IBlockAccess blockAccess, int i, int j, int k )
    {
		return !GetIsSlab( blockAccess, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
    public boolean GetIsSlab( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsSlabFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public void SetIsSlab( World world, int i, int j, int k, boolean bState )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & ~8;	// filter out old state
    	
    	if ( bState )
    	{
    		iMetadata |= 8;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public boolean GetIsSlabFromMetadata( int iMetadata )
    {
    	return ( iMetadata & 8 ) > 0;
    }
    
    public boolean GetIsUpsideDownSlab( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsUpsideDownSlabFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public boolean GetIsUpsideDownSlabFromMetadata( int iMetadata )
    {
    	if ( GetIsSlabFromMetadata( iMetadata ) )
    	{
    		return GetFacing( iMetadata ) == 1;
    	}
    	
    	return false;    			
    }
    
    static void SpawnHearts( World world, int i, int j, int k )
    {
        String s = "heart";
        
        for( int tempCount = 0; tempCount < 7; tempCount++)
        {
            double d = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            
            world.spawnParticle( s, 
        		((double) i) + (double)(world.rand.nextFloat()), 
        		(double)( j + 1 ) + (double)(world.rand.nextFloat()), 
				((double) k) + (double)(world.rand.nextFloat()), 
				d, d1, d2);
        }
    }    
    
	//----------- Client Side Functionality -----------//
}
