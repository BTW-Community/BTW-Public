// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFurnaceBrick extends FCBlockFurnace
{
    protected final FCModelBlock m_modelBlockInterior = new FCModelBlockFurnaceBrick();
    
    protected final float m_fClickYTopPortion = ( 6F / 16F ); 
    protected final float m_fClickYBottomPortion = ( 6F / 16F ); 
    	
    protected FCBlockFurnaceBrick( int iBlockID, boolean bIsLit )
    {
        super( iBlockID, bIsLit );
        
        SetPicksEffectiveOn();
        
        setHardness( 2F );
        setResistance( 3.33F ); // need to override resistance set in parent
        
        setUnlocalizedName( "fcBlockFurnaceBrick" );        
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityFurnaceBrick();
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        int iMetadata = world.getBlockMetadata( i, j, k );
		int iBlockFacing = iMetadata & 7;
		
		if ( iBlockFacing != iFacing )
		{
			// block is only accessible from front
			
			return false;
		}

    	ItemStack heldStack = player.getCurrentEquippedItem();
        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)world.getBlockTileEntity( i, j, k );        
        ItemStack cookStack = tileEntity.GetCookStack();        
    	
        if ( fYClick > m_fClickYTopPortion )
        {
        	if ( cookStack != null )
        	{
				tileEntity.GivePlayerCookStack( player, iFacing );
				
				return true;
        	}
        	else
        	{
				if ( heldStack != null && IsValidCookItem( heldStack ) )
				{
					if ( !world.isRemote )
					{
						tileEntity.AddCookStack( new ItemStack( heldStack.itemID, 1, heldStack.getItemDamage() ) );
					}
    				
	    			heldStack.stackSize--;
	    			
	    			return true;
				}
        	}
        }
        else if ( fYClick < m_fClickYBottomPortion && heldStack != null )
        {
        	// handle fuel here
        	
    		Item item = heldStack.getItem();    		
			int iItemDamage = heldStack.getItemDamage();
			
    		if ( item.GetCanBeFedDirectlyIntoBrickOven( iItemDamage ) ) 
    		{
    			if ( !world.isRemote )
    			{
    				int iItemsConsumed = tileEntity.AttemptToAddFuel( heldStack );
    				
	                if ( iItemsConsumed > 0 )
	                {
	                	if ( isActive )
	                	{
			                world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
			                	"mob.ghast.fireball", 0.2F + world.rand.nextFloat() * 0.1F, world.rand.nextFloat() * 0.25F + 1.25F );
	                	}
	                	else
	                	{
	        	            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	                    		"random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
	                	}
		                
	        			heldStack.stackSize -= iItemsConsumed;	        			
	                }
    			}
                
    			return true;
    		}
        }
        
		return false;
    }
	
    @Override
    public int quantityDropped( Random rand )
    {
        return 4 + rand.nextInt( 6 );
    }

    @Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return Item.brick.itemID;
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
		if ( !FCUtilsWorld.DoesBlockHaveSolidTopSurface( world, i, j - 1, k ) )
		{
			return false;
		}
		
        return super.canPlaceBlockAt( world, i, j, k );
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !FCUtilsWorld.DoesBlockHaveSolidTopSurface( world, i, j - 1, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iBlockFacing = blockAccess.getBlockMetadata( i, j, k ) & 7;
    	
    	return iBlockFacing != iFacing;
	}

    @Override
    public void updateFurnaceBlockState( boolean bBurning, World world, int i, int j, int k, boolean bHasContents )
    {
        int iMetadata = world.getBlockMetadata( i, j, k );
        TileEntity tileEntity = world.getBlockTileEntity( i, j, k );
        
        keepFurnaceInventory = true;

        if ( bBurning )
        {
            world.setBlock( i, j, k, FCBetterThanWolves.fcBlockFurnaceBrickBurning.blockID );
        }
        else
        {
            world.setBlock( i, j, k, FCBetterThanWolves.fcBlockFurnaceBrickIdle.blockID );
        }

        keepFurnaceInventory = false;
        
        if ( !bHasContents )
        {
        	iMetadata = iMetadata & 7;
        }
        else
        {
        	iMetadata = iMetadata | 8;
        }
        
        world.SetBlockMetadataWithNotify( i, j, k, iMetadata, 2 );

        if ( tileEntity != null )
        {
            tileEntity.validate();
            world.setBlockTileEntity( i, j, k, tileEntity );
        }
    }

	@Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( !isActive )
		{
	        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)blockAccess.getBlockTileEntity( i, j, k );
	        
	        // uses the visual fuel level rather than the actualy fuel level so this will work on the client
	        
	        if ( tileEntity.GetVisualFuelLevel() > 0 )
	        {
	        	return true;
	        }
		}
		
    	return false;
    }
    
	@Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		if ( !isActive )
		{
	        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)world.getBlockTileEntity( i, j, k );
	        
	        if ( tileEntity.AttemptToLight() )
	        {
	            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	            	"mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F );
	            
	            return true;
	        }	            
		}
		
		return false;
    }
    
	@Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( !isActive )
		{
	        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)blockAccess.getBlockTileEntity( i, j, k );
	        
	        if ( tileEntity.HasValidFuel() )
	        {
				return 60; // same chance as leaves and other highly flammable objects
	        }
		}
		
		return 0;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
    @Override
	protected int IDDroppedSilkTouch()
	{
		return FCBetterThanWolves.fcBlockFurnaceBrickIdle.blockID;
	}
	
    @Override
    public boolean GetIsBlockWarm( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return isActive;
    }
    
    @Override
    public boolean DoesBlockHopperInsert( World world, int i, int j, int k )
    {
    	return true;
    }
    
	//------------- Class Specific Methods ------------//

	public boolean IsValidCookItem( ItemStack stack )
	{
		if ( FurnaceRecipes.smelting().getSmeltingResult( stack.getItem().itemID ) != null )
		{
			return true;
		}
		
		return false;
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_fuelOverlays;
	private Icon m_currentFuelOverlay = null;
    private Icon m_blankOverlay;
    
    private boolean m_bIsRenderingInterior = false;
    private int m_iInteriorBrightness = 0;
	
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockFurnaceBrick_side" );
        furnaceIconTop = register.registerIcon( "fcBlockFurnaceBrick_top" );
        
        if ( isActive )
        {
        	furnaceIconFront = register.registerIcon( "fcBlockFurnaceBrick_front_lit" );
        }
        else
        {
        	furnaceIconFront = register.registerIcon( "fcBlockFurnaceBrick_front" );
        }               
        
        m_fuelOverlays = new Icon[9];

        for ( int iTempIndex = 0; iTempIndex < 9; ++iTempIndex )
        {
        	m_fuelOverlays[iTempIndex] = register.registerIcon( "fcOverlayFurnaceFuel_" + ( iTempIndex ) );
        }
        
        m_blankOverlay = register.registerIcon( "fcOverlayBlank" );
    }
	
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return FCBetterThanWolves.fcBlockFurnaceBrickIdle.blockID;
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		int iFacing = iMetadata & 7; 
		
		if ( iFacing < 2 || iFacing > 5 )
		{
			// have to assign default value due to item render having metadata of 0
			
			iFacing = 3;
		}
		
		if ( m_currentFuelOverlay == null )
		{
			if ( iFacing == iSide )
			{
				return furnaceIconFront;
			}
			else if ( iSide < 2 )
			{
				return furnaceIconTop;
			}
			
			return blockIcon;
		}
		else
		{
			if ( iFacing == iSide )
			{
				return m_currentFuelOverlay;
			}
			
			return m_blankOverlay;
		}
    }
	
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
    	if ( m_bIsRenderingInterior )
    	{
			FCUtilsBlockPos myPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, Block.GetOppositeFacing( iSide ) );
			
			int iFacing = blockAccess.getBlockMetadata( myPos.i, myPos.j, myPos.k ) & 7;
			
			return iSide != Block.GetOppositeFacing( iFacing );
    	}
    	
		return super.shouldSideBeRendered( blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	renderer.renderStandardBlock( this, i, j, k );
    	
    	int iFacing = renderer.blockAccess.getBlockMetadata( i, j, k ) & 7;
    	
    	FCModelBlock transformedModel = m_modelBlockInterior.MakeTemporaryCopy();
    	
    	transformedModel.RotateAroundJToFacing( iFacing );

    	FCUtilsBlockPos interiorFacesPos = new FCUtilsBlockPos( i, j, k, iFacing );
    	
    	m_iInteriorBrightness = getMixedBrightnessForBlock( renderer.blockAccess, 
    		interiorFacesPos.i, interiorFacesPos.j, interiorFacesPos.k );
    	
    	renderer.setOverrideBlockTexture( blockIcon );
    	m_bIsRenderingInterior = true;
    	
        boolean bReturnValue = transformedModel.RenderAsBlockWithColorMultiplier( renderer, this, i, j, k );
        
    	m_bIsRenderingInterior = false;
        renderer.clearOverrideBlockTexture();
        
    	return bReturnValue;     	
    }
    
    @Override
    public void RenderBlockSecondPass( RenderBlocks renderer, int i, int j, int k, boolean bFirstPassResult )
    {
    	if ( bFirstPassResult )
    	{
            FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)renderer.blockAccess.getBlockTileEntity( i, j, k );
            int iFuelLevel = tileEntity.GetVisualFuelLevel();

            if ( iFuelLevel > 0 )
            {
            	iFuelLevel = MathHelper.clamp_int( iFuelLevel - 2, 0, 8 );
		    	m_currentFuelOverlay = m_fuelOverlays[iFuelLevel];
		    	
		        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
		        	renderer.blockAccess, i, j, k ) );
		        
		    	renderer.renderStandardBlock( this, i, j, k );	    	
	
		    	m_currentFuelOverlay = null;
            }
    	}
    }
    
    @Override
    public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	if ( m_bIsRenderingInterior )
    	{
    		return m_iInteriorBrightness;
    	}
    	
    	return super.getMixedBrightnessForBlock( par1IBlockAccess, par2, par3, par4 );
    }
    
    @Override
    public boolean RenderBlockWithTexture( RenderBlocks renderer, int i, int j, int k, Icon texture )
    {
    	// necessary to render harvest cracks as if this is a regular block due to non-standard render above
    	
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	renderer.setOverrideBlockTexture( texture );
    	
        boolean bReturnValue = renderer.renderStandardBlock( this, i, j, k );
        
        renderer.clearOverrideBlockTexture();
        
        return bReturnValue;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	renderBlocks.renderBlockAsItemVanilla( this, iItemDamage, fBrightness );
    	
    	FCModelBlock transformedModel = m_modelBlockInterior.MakeTemporaryCopy();
    	
    	transformedModel.RotateAroundJToFacing( 3 );
    	
    	transformedModel.RenderAsItemBlock( renderBlocks, this, iItemDamage );    	
    }
    
    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        if ( isActive )
        {
            FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)world.getBlockTileEntity( i, j, k );
            int iFuelLevel = tileEntity.GetVisualFuelLevel();
            
            if ( iFuelLevel == 1 )
            {
	            int iFacing = world.getBlockMetadata( i, j, k ) & 7;
	            
	            float fX = (float)i + 0.5F;
	            float fY = (float)j + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
	            float fZ = (float)k + 0.5F;
	            
	            float fFacingOffset = 0.52F;
	            float fRandOffset = rand.nextFloat() * 0.6F - 0.3F;
	
	            if ( iFacing == 4 )
	            {
	                world.spawnParticle( "largesmoke", fX - fFacingOffset, fY, fZ + fRandOffset, 0.0D, 0.0D, 0.0D );
	            }
	            else if ( iFacing == 5 )
	            {
	                world.spawnParticle( "largesmoke", fX + fFacingOffset, fY, fZ + fRandOffset, 0.0D, 0.0D, 0.0D );
	            }
	            else if ( iFacing == 2 )
	            {
	                world.spawnParticle( "largesmoke", fX + fRandOffset, fY, fZ - fFacingOffset, 0.0D, 0.0D, 0.0D );
	            }
	            else if ( iFacing == 3 )
	            {
	                world.spawnParticle( "largesmoke", fX + fRandOffset, fY, fZ + fFacingOffset, 0.0D, 0.0D, 0.0D );
	            }
            }
            
            ItemStack cookStack = tileEntity.GetCookStack();
            
        	if ( cookStack != null && IsValidCookItem( cookStack ) )
	        {
	            for ( int iTempCount = 0; iTempCount < 1; ++iTempCount )
	            {
	                float fX = i + 0.375F + rand.nextFloat() * 0.25F;
	                float fY = j + 0.45F + rand.nextFloat() * 0.1F;
	                float fZ = k + 0.375F + rand.nextFloat() * 0.25F;
	                
	                world.spawnParticle( "fcwhitecloud", fX, fY, fZ, 0D, 0D, 0D );
	            }
	        }           
        }
        
        super.randomDisplayTick( world, i, j, k, rand );
    }
}
