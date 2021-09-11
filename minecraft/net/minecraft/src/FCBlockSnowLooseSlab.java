// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSnowLooseSlab extends FCBlockSlabFalling
{
    public FCBlockSnowLooseSlab( int iBlockID )
    {
        super( iBlockID, Material.craftedSnow );
        
        setHardness( FCBlockSnowLoose.m_fHardness );
        SetShovelsEffectiveOn();
        
    	SetBuoyant();
        
    	setStepSound( soundSnowFootstep );
        
        setUnlocalizedName( "fcBlockSnowLooseSlab" );
        
        setTickRandomly( true );
		
        setLightOpacity( 2 );
        Block.useNeighborBrightness[iBlockID] = true;
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return Item.snowball.itemID;
    }

    public int quantityDropped( Random rand )
    {
        return 4;
    }

    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        if ( world.provider.isHellWorld )
        {
        	// melt instantly in the nether
        	
        	world.setBlockToAir( i, j, k );
        	
            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
            	"random.fizz", 0.5F, 2.6F + ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.8F );
            
            for ( int iTempCount = 0; iTempCount < 8; iTempCount++ )
            {
                world.spawnParticle( "largesmoke", (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 
                	0D, 0D, 0D );
            }
        }
        else
        {
        	ScheduleCheckForFall( world, i, j, k );
        }
    }
    
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockSnowLoose.blockID;
	}

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand ) 
    {
    	if ( !HasStickySnowNeighborInContact( world, i, j, k ) || 
    		( GetIsUpsideDown( world, i, j, k ) && HasFallingBlockRestingOn( world, i, j, k ) ) )
    	{
    		if ( !CheckForFall( world, i, j, k ) )
    		{
            	if ( GetIsUpsideDown( world, i, j, k ) )
            	{
            		SetIsUpsideDown( world, i, j, k, false );
            	}
    		}
    	}
    }
	
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( FCUtilsMisc.IsIKInColdBiome( world, i, k ) )
		{
			if ( rand.nextInt( FCBlockSnowLoose.m_iChanceOfHardeningIncrement ) == 0 )
			{
				int iHardeningLevel = GetHardeningLevel( world, i, j, k );
				
				if ( iHardeningLevel < 7 )
				{
					SetHardeningLevel( world, i, j, k, iHardeningLevel + 1 );
				}
				else
				{
					ConvertToSolidSnow( world, i, j, k );
				}
			}
		}
		else
		{
			if ( rand.nextInt( FCBlockSnowLoose.m_iChanceOfMeltingIncrement ) == 0 )
			{
				int iHardeningLevel = GetHardeningLevel( world, i, j, k );
				
				if ( iHardeningLevel > 0 )
				{
					SetHardeningLevel( world, i, j, k, iHardeningLevel - 1 );
				}
				else
				{
					Melt( world, i, j, k );
				}
			}
		}
    }
	
    @Override
    public boolean CanBePlacedUpsideDownAtLocation( World world, int i, int j, int k )
    {
    	return HasStickySnowNeighborInContact( world, i, j, k, true );
    }
    
	@Override
    public void OnPlayerWalksOnBlock( World world, int i, int j, int k, EntityPlayer player )
    {
		if ( !CheckForFall( world, i, j, k ) )
		{
        	if ( GetIsUpsideDown( world, i, j, k ) )
        	{
        		SetIsUpsideDown( world, i, j, k, false );
        	}
		}
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 0.8F;
    }
    
    @Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean GetCanBeSetOnFireDirectlyByItem( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		Melt( world, i, j, k );
		
    	return true;
    }
    
    @Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 60; // same chance as leaves and other highly flammable objects
    }
    
	@Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    protected ItemStack createStackedBlock( int iMetadata )
    {
        return new ItemStack( blockID, 1, 0 );
    }
    
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//   
    
    public int GetHardeningLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetHardeningLevel( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetHardeningLevel( int iMetadata )
    {
    	return ( iMetadata & 14 ) >> 1; 
    }

    public void SetHardeningLevel( World world, int i, int j, int k, int iLevel )
    {
    	int iMetadata = SetHardeningLevel( world.getBlockMetadata( i, j, k ), iLevel );
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    	
    public int SetHardeningLevel( int iMetadata, int iLevel )
    {
    	iMetadata &= ~14;
    	
    	return iMetadata | ( iLevel << 1 );
    }
    
	private void ConvertToSolidSnow( World world, int i, int j, int k )
	{
		int iNewMetadata = FCBetterThanWolves.fcBlockSnowSolidSlab.SetIsUpsideDown( 0, GetIsUpsideDown( world, i, j, k ) );
		
		world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockSnowSolidSlab.blockID, iNewMetadata );
	}
	
	private void Melt( World world, int i, int j, int k )
	{
		FCUtilsMisc.PlaceNonPersistantWaterMinorSpread( world, i, j, k );
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_iconsHardening;

	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_iconsHardening = new Icon[8];

        for ( int iTempIndex = 0; iTempIndex < 8; iTempIndex++ )
        {
        	m_iconsHardening[iTempIndex] = register.registerIcon( "fcOverlaySnowLoose_" + iTempIndex );
        }
    }
	
    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
    {
    	if ( bFirstPassResult )
    	{
	    	int iHardeningLevel = GetHardeningLevel( renderBlocks.blockAccess, i, j, k );
	    	
    		if ( iHardeningLevel >= 0 && iHardeningLevel <= 7 )
    		{
        		RenderBlockWithTexture( renderBlocks, i, j, k, m_iconsHardening[iHardeningLevel] );
    		}
    	}
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	renderBlocks.renderBlockAsItemVanilla( this, iItemDamage, fBrightness );
    	
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, this, -0.5F, -0.5F, -0.5F, m_iconsHardening[0] );        
    }    
    
    @Override
    public void RenderFallingBlock( RenderBlocks renderBlocks, int i, int j, int k, int iMetadata )
    {
    	renderBlocks.SetRenderAllFaces( true );
    	
        renderBlocks.setRenderBounds( GetBlockBoundsFromPoolFromMetadata( iMetadata ) );
        
        renderBlocks.renderStandardBlock( this, i, j, k );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, m_iconsHardening[GetHardeningLevel( iMetadata )] );
        
    	renderBlocks.SetRenderAllFaces( false );
    }
    
    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
    	EmitHardeningParticles( world, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, rand );    	
    }

    private void EmitHardeningParticles( World world, double dCenterX, double dCenterY, double dCenterZ, Random rand )
    {
        for ( int iTempCount = 0; iTempCount < 1; ++iTempCount )
        {
            double xPos = dCenterX - 0.60D + rand.nextDouble() * 1.2D;
            double yPos = dCenterY - 0.60D + rand.nextDouble() * 1.2D;
            double zPos = dCenterZ - 0.60D + rand.nextDouble() * 1.2D;
        
        	world.spawnParticle( "fcwhitecloud", xPos, yPos, zPos, 0D, 0D, 0D );
        }
    }
}
