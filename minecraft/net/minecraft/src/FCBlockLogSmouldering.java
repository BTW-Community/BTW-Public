// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockLogSmouldering extends FCBlockFalling
{
	private static final int m_iChanceOfDecay = 5;

	private static final int m_iChanceOfExtinguishInRain = 5;	

	private static final float m_fExplosionStrength = 1F;

	protected FCBlockLogSmouldering( int iBlockID )
	{
		super( iBlockID, FCBetterThanWolves.fcMaterialLog );

		setHardness( 2F );

		SetAxesEffectiveOn();
		SetChiselsEffectiveOn();

		SetBuoyant();

		setTickRandomly( true );

		setStepSound( soundWoodFootstep );

		setUnlocalizedName( "fcBlockLogSmouldering" );
	}

	@Override
	public float getBlockHardness( World world, int i, int j, int k )
	{
		float fHardness = super.getBlockHardness( world, i, j, k );

		int iMetadata = world.getBlockMetadata( i, j, k );

		if ( GetIsStump( world, i, j, k ) )
		{   
			fHardness *= 3F;    		
		}

		return fHardness; 
	}

	@Override
	public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
	{
		return 0;
	}

	@Override
	public boolean GetIsProblemToRemove( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsStump( blockAccess, i, j, k );
	}

	@Override
	public boolean GetCanBlockBeIncinerated( World world, int i, int j, int k )
	{
		return !GetIsStump( world, i, j, k );
	}

	@Override
	public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
	{
		return GetIsStump( world, i, j, k );
	}

	@Override
	public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
	{
		if ( GetIsStump( world.getBlockMetadata( i, j, k ) ) )
		{
			world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockStumpCharred.blockID );

			return true;
		}   

		return false;
	}

	@Override
	public void updateTick( World world, int i, int j, int k, Random rand ) 
	{
		if ( !HasWaterToSidesOrTop( world, i, j, k ) )
		{
			// prevent falling behavior for stumps and first level cinders

			int iMetadata = world.getBlockMetadata( i, j, k );

			if ( !GetIsStump( iMetadata ) && GetBurnLevel( iMetadata ) > 0 )
			{
				super.updateTick( world, i, j, k, rand );
			}
		}
		else
		{
			// extinguish due to neighboring water blocks

			ConvertToCinders( world, i, j, k );

			world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
		}
	}

	@Override
	public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
	{
		if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
			if ( !CheckForGoOutInRain( world, i, j, k ) )
			{			
				FCBlockFire.CheckForSmoulderingSpreadFromLocation( world, i, j, k );

				int iBurnLevel = GetBurnLevel( world, i, j, k );

				if ( iBurnLevel == 0 )
				{
					if ( !FCBlockFire.HasFlammableNeighborsWithinSmoulderRange( world, i, j, k ) )
					{
						int iMetadata = world.getBlockMetadata( i, j, k );

						iMetadata = SetBurnLevel( iMetadata, 1 );

						if ( IsSupportedBySolidBlocks( world, i, j, k ) )
						{
							// intentionally leaves the flag as true if it's already set

							iMetadata = SetShouldSuppressSnapOnFall( iMetadata, true );
						}

						world.setBlockMetadataWithNotify( i, j, k, iMetadata );

						ScheduleCheckForFall( world, i, j, k );
					}
				}
				else if ( rand.nextInt( m_iChanceOfDecay ) == 0 )
				{
					if ( iBurnLevel < 3 )
					{
						SetBurnLevel( world, i, j, k, iBurnLevel + 1 );
					}
					else
					{
						ConvertToCinders( world, i, j, k );
					}
				}
			}
		}
	}

	@Override
	public boolean GetIsBlockWarm( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}

	@Override
	public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}

	@Override
	protected void onStartFalling( EntityFallingSand entity ) 
	{	
		if ( !GetShouldSuppressSnapOnFall( entity.metadata ) )
		{
			entity.worldObj.playAuxSFX( FCBetterThanWolves.m_iLogSmoulderingFallAuxFXID,            
					MathHelper.floor_double( entity.posX ), 
					MathHelper.floor_double( entity.posY ), 
					MathHelper.floor_double( entity.posZ ), 0 );

			// only makes the fall sound once

			entity.metadata = SetShouldSuppressSnapOnFall( entity.metadata, true );
		}        
	}

	@Override
	protected void OnFallingUpdate( FCEntityFallingBlock entity ) 
	{
		if ( entity.worldObj.isRemote )
		{
			EmitSmokeParticles( entity.worldObj, entity.posX, entity.posY, entity.posZ, 
					entity.worldObj.rand, GetBurnLevel( entity.metadata ) );
		}
	}

	@Override
	public boolean OnFinishedFalling( EntityFallingSand entity, float fFallDistance )
	{
		if ( !entity.worldObj.isRemote )
		{
			int i = MathHelper.floor_double( entity.posX );
			int j = MathHelper.floor_double( entity.posY );
			int k = MathHelper.floor_double( entity.posZ );

			int iFallDistance = MathHelper.ceiling_float_int( fFallDistance - 5.0F );

			if ( iFallDistance >= 0 )
			{	  
				if ( !Material.water.equals( entity.worldObj.getBlockMaterial( i, j, k ) ) )
				{	    			
					if ( entity.rand.nextInt( 5 ) < iFallDistance )
					{
						Explode( entity.worldObj, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D );

						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
	{
		return 1000; // always convert, never harvest
	}

	@Override
	public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
	{
		Explode( world, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D );
	}

	//------------- Class Specific Methods ------------//

	public int GetBurnLevel( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iMetadata = blockAccess.getBlockMetadata( i, j, k );

		return GetBurnLevel( iMetadata );
	}

	public int GetBurnLevel( int iMetadata )
	{
		return iMetadata & 3;
	}

	public void SetBurnLevel( World world, int i, int j, int k, int iLevel )
	{
		int iMetadata = SetBurnLevel( world.getBlockMetadata( i, j, k ), iLevel );

		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}

	public int SetBurnLevel( int iMetadata, int iLevel )
	{
		iMetadata &= ~3;

		return iMetadata | iLevel;
	}

	public boolean GetShouldSuppressSnapOnFall( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iMetadata = blockAccess.getBlockMetadata( i, j, k );

		return GetShouldSuppressSnapOnFall( iMetadata );
	}

	public boolean GetShouldSuppressSnapOnFall( int iMetadata )
	{
		return ( iMetadata & 4 ) != 0;
	}

	public int SetShouldSuppressSnapOnFall( int iMetadata, boolean bSnap )
	{
		if ( bSnap )
		{
			iMetadata |= 4;
		}
		else
		{
			iMetadata &= ~4;
		}

		return iMetadata;
	}

	public boolean GetIsStump( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iMetadata = blockAccess.getBlockMetadata( i, j, k );

		return GetIsStump( iMetadata );
	}

	public boolean GetIsStump( int iMetadata )
	{
		return ( iMetadata & 8 ) != 0;
	}

	public int SetIsStump( int iMetadata, boolean bStump )
	{
		if ( bStump )
		{
			iMetadata |= 8;
		}
		else
		{
			iMetadata &= ~8;
		}

		return iMetadata;
	}

	private boolean CheckForGoOutInRain( World world, int i, int j, int k )
	{
		if ( world.rand.nextInt( m_iChanceOfExtinguishInRain ) == 0 )
		{
			if ( world.IsRainingAtPos( i, j + 1, k ) )
			{
				world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );

				ConvertToCinders( world, i, j, k );

				return true;
			}
		}

		return false;
	}

	private void ConvertToCinders( World world, int i, int j, int k )
	{
		if ( GetIsStump( world, i, j, k ) )
		{
			int iNewMetadata = FCBetterThanWolves.fcBlockWoodCinders.SetIsStump( 0, true );

			world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockWoodCinders.blockID, iNewMetadata );						
		}
		else
		{
			world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockWoodCinders.blockID );
		}
	}

	private void EmitSmokeParticles( World world, double dCenterX, double dCenterY, double dCenterZ, Random rand, int iBurnLevel )
	{
		for ( int iTempCount = 0; iTempCount < 5; ++iTempCount )
		{
			double xPos = dCenterX - 0.60D + rand.nextDouble() * 1.2D;
			double yPos = dCenterY + 0.25D + rand.nextDouble() * 0.25D;
			double zPos = dCenterZ - 0.60D + rand.nextDouble() * 1.2D;

			if ( iBurnLevel > 0 )
			{
				world.spawnParticle( "fcwhitesmoke", xPos, yPos, zPos, 0D, 0D, 0D );
			}
			else
			{
				world.spawnParticle( "largesmoke", xPos, yPos, zPos, 0D, 0D, 0D );
			}
		}
	}

	@Override
	public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
	{
		if ( !world.isRemote )
		{
			// explode without audio/visual effects to cut down on overhead

			explosion.AddSecondaryExplosionNoFX( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, m_fExplosionStrength, true, false );
		}
	}

	private void Explode( World world, double posX, double posY, double posZ )
	{
		world.NewExplosionNoFX( (Entity)null, posX, posY, posZ, m_fExplosionStrength, true, false );

		NotifyNearbyAnimalsFinishedFalling( world, MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ) );

		world.playAuxSFX( FCBetterThanWolves.m_iLogSmoulderingExplosionAuxFXID, 
				MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ), 
				0 );
	}

	protected boolean IsSupportedBySolidBlocks( World world, int i, int j, int k )
	{
		Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];

		return blockBelow != null && blockBelow.HasLargeCenterHardPointToFacing( world, i, j - 1, k, 1, false );
	}

	//----------- Client Side Functionality -----------//

	private Icon m_iconEmbers;

	@Override
	public void registerIcons( IconRegister register )
	{
		super.registerIcons( register );

		m_iconEmbers = register.registerIcon( "fcOverlayLogEmbers" );
	}

	@Override
	public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
	{
		renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
				renderer.blockAccess, i, j, k ) );

		return renderer.renderBlockLog( this, i, j, k );
	}


	@Override
	public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
	{
		if ( bFirstPassResult )
		{
			FCClientUtilsRender.RenderBlockFullBrightWithTexture( renderBlocks, renderBlocks.blockAccess, i, j, k, m_iconEmbers );
		}
	}

	@Override
	public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
	{
		renderBlocks.renderBlockAsItemVanilla( this, iItemDamage, fBrightness );

		FCClientUtilsRender.RenderInvBlockFullBrightWithTexture( renderBlocks, this, -0.5F, -0.5F, -0.5F, m_iconEmbers );        
	}

	@Override
	public void RenderFallingBlock( RenderBlocks renderBlocks, int i, int j, int k, int iMetadata )
	{
		renderBlocks.SetRenderAllFaces( true );

		renderBlocks.setRenderBounds( GetFixedBlockBoundsFromPool() );

		renderBlocks.renderStandardBlock( this, i, j, k );

		FCClientUtilsRender.RenderBlockFullBrightWithTexture( renderBlocks, renderBlocks.blockAccess, i, j, k, m_iconEmbers );

		renderBlocks.SetRenderAllFaces( false );
	}

	@Override
	public void randomDisplayTick( World world, int i, int j, int k, Random rand )
	{
		EmitSmokeParticles( world, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, rand, GetBurnLevel( world, i, j, k ) );    	

		if ( rand.nextInt( 24 ) == 0 )
		{
			float fVolume = 0.1F + rand.nextFloat() * 0.1F;

			world.playSound( i + 0.5D, j + 0.5D, k + 0.5D, "fire.fire", 
					fVolume, rand.nextFloat() * 0.7F + 0.3F, false );
		}	        
	}
}
