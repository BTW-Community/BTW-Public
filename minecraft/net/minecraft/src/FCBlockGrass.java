// FCMOD

package net.minecraft.src;

import java.util.Random;

import net.minecraft.client.Minecraft; // client only

public class FCBlockGrass extends BlockGrass
{
    // global constants
	
    public static final int m_iGrassSpreadFromLightLevel = 11;
    public static final int m_iGrassSpreadToLightLevel = 11; // 7 previously, 4 vanilla
    public static final int m_iGrassSurviveMinimumLightLevel = 9; // 4 previously
    
    protected FCBlockGrass( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 0.6F );
    	SetShovelsEffectiveOn();
    	SetHoesEffectiveOn();
    	
    	setStepSound(soundGrassFootstep);
    	
    	setUnlocalizedName("grass");    	
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	int iBlockAboveID = world.getBlockId( i, j + 1, k );
    	Block blockAbove = Block.blocksList[iBlockAboveID];
    	int iBlockAboveMaxNaturalLight = world.GetBlockNaturalLightValueMaximum( i, j + 1, k );
    	int iBlockAboveCurrentNaturalLight = iBlockAboveMaxNaturalLight - world.skylightSubtracted;
    	
        if ( iBlockAboveMaxNaturalLight < m_iGrassSurviveMinimumLightLevel || Block.lightOpacity[iBlockAboveID] > 2 ||
        	( blockAbove != null && !blockAbove.GetCanGrassGrowUnderBlock( world, i, j + 1, k, false ) ) )
        {
        	// convert back to dirt in low light
        	
            world.setBlockWithNotify( i, j, k, Block.dirt.blockID );
        }
        else if ( iBlockAboveCurrentNaturalLight >= m_iGrassSpreadFromLightLevel )
        {
        	CheckForGrassSpreadFromLocation( world, i, j, k );
        }
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockDirtLoose.blockID;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 6, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	super.OnBlockDestroyedWithImproperTool( world, player, i, j, k, iMetadata );
    	
    	OnDirtDugWithImproperTool( world, i, j, k );    	
    }
    
	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		super.onBlockDestroyedByExplosion( world, i, j, k, explosion );
		
		OnDirtDugWithImproperTool( world, i, j, k );    	
    }
	
    @Override
    protected void OnNeighborDirtDugWithImproperTool( World world, int i, int j, int k, 
    	int iToFacing )
    {
    	// only disrupt grass/mycelium when block below is dug out
    	
		if ( iToFacing == 0 )
		{
			world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
		}    		
    }
    
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
    	return true;
    }
    
    @Override
    public void OnGrazed( World world, int i, int j, int k, EntityAnimal animal )
    {
        if ( !animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, Block.dirt.blockID );
        }
        else
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
    }
    
    @Override
	public void OnVegetationAboveGrazed( World world, int i, int j, int k, EntityAnimal animal )
	{
        if ( animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
	}
    
	@Override
    public boolean CanReedsGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanSaplingsGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanWildVegetationGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean GetCanBlightSpreadToBlock( World world, int i, int j, int k, int iBlightLevel )
    {
		return true;
    }

	@Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return stack != null && stack.getItem() instanceof FCItemHoe;
    }
	
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );

    	if ( !world.isRemote )
		{
            world.playAuxSFX( 2001, i, j, k, blockID ); // block break FX
            
            if ( world.rand.nextInt( 25 ) == 0 )
            {
	            FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
	            	new ItemStack( FCBetterThanWolves.fcItemHempSeeds ), iFromSide );
            }
		}
    	
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
    public static void CheckForGrassSpreadFromLocation( World world, int i, int j, int k )
    {
    	if ( world.provider.dimensionId != 1 &&
    		!FCBlockGroundCover.IsGroundCoverRestingOnBlock( world, i, j, k ) )    		
    	{
        	// check for grass spread
        	
            int iTargetI = i + world.rand.nextInt(3) - 1;
            int iTargetJ = j + world.rand.nextInt(5) - 3;
            int iTargetK = k + world.rand.nextInt(3) - 1;
                            
            Block targetBlock = Block.blocksList[world.getBlockId( iTargetI, iTargetJ, iTargetK )];
            
            if ( targetBlock != null )
            {
            	targetBlock.AttempToSpreadGrassToBlock( world, iTargetI, iTargetJ, iTargetK );
            }
    	}
    }
    
	//----------- Client Side Functionality -----------//    
    
    private boolean m_bTempHasSnowOnTop; // temporary variable used by rendering
    
    // duplicate variables to parent class to avoid base class modification
    
    private Icon iconGrassTop;
    private Icon iconSnowSide;
    private Icon iconGrassSideOverlay;
    
    public void registerIcons(IconRegister par1IconRegister)
    {
    	super.registerIcons( par1IconRegister );
    	
        this.iconGrassTop = par1IconRegister.registerIcon("grass_top");
        this.iconSnowSide = par1IconRegister.registerIcon("snow_side");
        this.iconGrassSideOverlay = par1IconRegister.registerIcon("grass_side_overlay");
    }
    
    @Override
    public int colorMultiplier( IBlockAccess blockAccess, int i, int j, int k)
    {
    	if ( m_bTempHasSnowOnTop )
    	{
            return 16777215;
    	}
    	else
    	{
    		return super.colorMultiplier( blockAccess, i, j, k );
    	}
    }
    
    @Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        if ( iSide == 1 )
        {
            return iconGrassTop;
        }
        else if ( iSide == 0 )
        {
            return Block.dirt.getBlockTextureFromSide( iSide );
        }
        else if ( m_bTempHasSnowOnTop )
    	{
    		return iconSnowSide;
    	}
        
		return blockIcon;
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
        renderer.setRenderBounds( 0D, 0D, 0D, 1D, 1D, 1D );
        
        m_bTempHasSnowOnTop = IsSnowCoveringTopSurface( blockAccess, i, j, k ); 
        
        if ( m_bTempHasSnowOnTop )
        {
        	return renderer.renderStandardBlock( this, i, j, k );
        }
        else
        {
	        int var5 = colorMultiplier( blockAccess, i, j, k );
	        
	        float var6 = (float)(var5 >> 16 & 255) / 255.0F;
	        float var7 = (float)(var5 >> 8 & 255) / 255.0F;
	        float var8 = (float)(var5 & 255) / 255.0F;
	
	        if ( Minecraft.isAmbientOcclusionEnabled() )
	        {
	        	return renderer.renderGrassBlockWithAmbientOcclusion( this, i, j, k, var6, var7, var8, BlockGrass.getIconSideOverlay() );
	        }
	        else
	        {
	        	return renderer.renderGrassBlockWithColorMultiplier( this, i, j, k, var6, var7, var8, BlockGrass.getIconSideOverlay() );
	        }
        }
    }    
}