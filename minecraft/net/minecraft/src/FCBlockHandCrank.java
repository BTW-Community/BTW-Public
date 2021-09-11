// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockHandCrank extends Block
	implements FCIBlockMechanical
{
	private static int m_iHandCrankTickRate = 3; 
	private static int m_iHandCrankDelayBeforeReset = 15; 
	
	private static double m_dBaseHeight = ( 4D / 16D );
	
	private static double m_dShaftSelectionWidth = ( 4D / 16D );
	private static double m_dShaftSelectionHalfWidth = ( m_dShaftSelectionWidth / 2D );
	
	protected FCBlockHandCrank( int iBlockID )
	{
        super( iBlockID, Material.circuits );

        setHardness( 0.5F );
        SetPicksEffectiveOn();

        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );
        	
        setStepSound( soundWoodFootstep );
        setUnlocalizedName( "fcBlockHandCrank" );
        
        setTickRandomly( true );

        setCreativeTab( CreativeTabs.tabRedstone );
	}

	@Override
    public int tickRate( World world )
    {
    	return m_iHandCrankTickRate;
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
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        return CanRestAtPosition( world, i, j, k ) && super.canPlaceBlockAt( world, i, j, k );
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        int iMetaData = world.getBlockMetadata( i, j, k );

        if ( iMetaData == 0 )
        {
        	if ( player.getFoodStats().getFoodLevel() > 18 )
        	{
	        	player.addExhaustion( 2.0F ); // every two pulls results in a half pip of hunger
	        	
	            if ( !world.isRemote )
	            {	            
		        	if ( !CheckForOverpower( world, i, j, k ) )
		        	{
			        	world.setBlockMetadataWithNotify( i, j, k, 1 );
				        
				        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
				        
						world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
								"random.click", 1.0F, 2.0F );
				        
				        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
				        
				        world.markBlockForUpdate( i, j, k );
		        	}
		        	else
		        	{
		        		BreakCrankWithDrop( world, i, j, k );        	
		    		}
	            }
        	}
        	else
        	{
                if( world.isRemote )
                {
                	player.addChatMessage( "You're too exhausted for manual labor." );
                	
                	return false;
                }
        	}
        	
        	return true;
        }
	        
        return false;
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
        int iMetaData = world.getBlockMetadata( i, j, k );

    	if ( iMetaData > 0 )
    	{
    		if ( iMetaData < 7 )
    		{
    			if ( iMetaData <= 6 )
    			{
    				world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
    						"random.click", 1.0F, 2.0F );
    			}
    			
    			if ( iMetaData <= 5 )
    			{
    				world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) + iMetaData );
    			}
    			else
    			{
    				world.scheduleBlockUpdate( i, j, k, blockID, m_iHandCrankDelayBeforeReset );
    			}
    	        
    			// no notify here as it's not an actual state-change, just an internal timer update
            	world.setBlockMetadata( i, j, k, iMetaData + 1 );            	
    		}
    		else
    		{
            	world.setBlockMetadataWithNotify( i, j, k, 0 );
            	
		        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
		        
		        world.markBlockForUpdate( i, j, k ); // mutliplayer update
		        
    	        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
    		    		"random.click", 0.3F, 0.7F );    	        
    		}
    	}
    }    
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		// check if the hand crank is currently on
		
        int iMetaData = world.getBlockMetadata( i, j, k );

    	if ( iMetaData > 0 )
    	{
			// verify we have a tick already scheduled to prevent jams on chunk load
			
			if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
			{
		        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );        
			}
		}
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !CanRestAtPosition( world, i, j, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( 
    		world, i, j, k, startRay, endRay );
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( GetBaseBoundsFromPool() );
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( GetShaftSelectionBoundsFromPool() );
    	
    	return rayTrace.GetFirstIntersection();
    }
    
    //------------- FCIMechanicalDevice ------------//
    
	@Override
    public boolean CanOutputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean CanInputMechanicalPower()
    {
    	return false;
    }

	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		return false;
	}
	
	@Override
    public boolean IsInputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {	
    	return world.getBlockMetadata( i, j, k ) > 0;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
	}
	
    //------------- Class Specific Methods ------------//
    
	public boolean CanRestAtPosition( World world, int i, int j, int k )
	{
        return FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true );
	}
    
    public boolean CheckForOverpower( World world, int i, int j, int k )
    {
    	int iNumPotentialDevicesToPower = 0;
    	
    	for ( int iTempFacing = 0; iTempFacing <=5; iTempFacing++ )
    	{
    		if ( iTempFacing == 1 )
    		{
    			continue;
    		}
    		
    		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    		
    		tempPos.AddFacingAsOffset( iTempFacing );
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k ); 
    		
    		Block tempBlock = Block.blocksList[iTempBlockID];
    		
    		if ( tempBlock != null && ( tempBlock instanceof FCIBlockMechanical ) )
    		{
    			FCIBlockMechanical tempDevice = (FCIBlockMechanical)tempBlock;
    			
    			if ( tempDevice.CanInputMechanicalPower() )
    			{
    				iNumPotentialDevicesToPower++;
    			}
    		}
    	}
    	
    	return ( iNumPotentialDevicesToPower > 1 );
    }
    
    public void BreakCrankWithDrop( World world, int i, int j, int k )
    {
    	FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, Item.stick.itemID, 0 );    	
    	FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, FCBetterThanWolves.fcItemGear.itemID, 0 );
		
    	DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStone.itemID, 12, 0, 0.75F );
    	
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
    }
    
    protected AxisAlignedBB GetBaseBoundsFromPool()
    {
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0D, 0D, 0D, 1D, m_dBaseHeight, 1D );
    }
    
    protected AxisAlignedBB GetShaftSelectionBoundsFromPool()
    {
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0D, m_dBaseHeight, 0.5D - m_dShaftSelectionHalfWidth, 
    		1D, 1D, 0.5D + m_dShaftSelectionHalfWidth );
    }
    
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconBySideArray = new Icon[6];
    
    private Icon m_IconShaft;

	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" ); // for hit effects
        
        m_IconShaft = register.registerIcon( "fcBlockHandCrank_shaft" );
        
        m_IconBySideArray[0] = register.registerIcon( "fcBlockHandCrank_bottom" );
        m_IconBySideArray[1] = register.registerIcon( "fcBlockHandCrank_top" );
        
        Icon sideIcon = register.registerIcon( "fcBlockHandCrank_side" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
    }
    
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_IconBySideArray[iSide];
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
		if ( iSide != 1 )
		{
			return super.shouldSideBeRendered( blockAccess, i, j, k, iSide );
		}
		
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
        Tessellator tessellator = Tessellator.instance;
        
        // render the base
        
        renderBlocks.setRenderBounds( GetBaseBoundsFromPool() );
        
        renderBlocks.renderStandardBlock( this, i, j, k );
        
        // render shaft
 
        float fBlockBrightness = getBlockBrightness( blockAccess, i, j, k );
        
        if ( Block.lightValue[blockID] > 0 )
        {
        	fBlockBrightness = 1.0F;
        }
        
        tessellator.setColorOpaque_F( fBlockBrightness, fBlockBrightness, fBlockBrightness );
        
        Icon iTextureID = this.m_IconShaft;
        
        if ( renderBlocks.hasOverrideBlockTexture() )
        {
        	iTextureID = renderBlocks.GetOverrideTexture();
        }
        
        double f4 = (double)iTextureID.getMinU();
        double f5 = (double)iTextureID.getMaxU();
        double f6 = (double)iTextureID.getMinV();
        double f7 = (double)iTextureID.getMaxV();
        
        Vec3 avec3d[] = new Vec3[8];
        
        float f8 = 0.0625F;
        float f9 = 0.0625F;
        float f10 = 0.90F;
        
        avec3d[0] = Vec3.createVectorHelper(-f8, 0.0D, -f9);
        avec3d[1] = Vec3.createVectorHelper(f8, 0.0D, -f9);
        avec3d[2] = Vec3.createVectorHelper(f8, 0.0D, f9);
        avec3d[3] = Vec3.createVectorHelper(-f8, 0.0D, f9);
        avec3d[4] = Vec3.createVectorHelper(-f8, f10, -f9);
        avec3d[5] = Vec3.createVectorHelper(f8, f10, -f9);
        avec3d[6] = Vec3.createVectorHelper(f8, f10, f9);
        avec3d[7] = Vec3.createVectorHelper(-f8, f10, f9);
        
        boolean bIsOn = blockAccess.getBlockMetadata(i, j, k) > 0;
        
        for ( int i2 = 0; i2 < 8; i2++ )
        {
            if ( bIsOn )
            {
                avec3d[i2].zCoord -= 0.0625D;
                avec3d[i2].rotateAroundX(0.35F);
            } 
            else
            {
                avec3d[i2].zCoord += 0.0625D;
                avec3d[i2].rotateAroundX(-0.35F);
            }
            
            avec3d[i2].rotateAroundY(1.570796F);
            
            avec3d[i2].xCoord += (double)i + 0.5D;
            avec3d[i2].yCoord += (float)j + 0.125F;
            avec3d[i2].zCoord += (double)k + 0.5D;
        }

        Vec3 vec3d = null;
        Vec3 vec3d1 = null;
        Vec3 vec3d2 = null;
        Vec3 vec3d3 = null;
        
        for ( int j2 = 0; j2 < 6; j2++ )
        {
            if ( j2 == 0 )
            {
                f4 = (double)iTextureID.getInterpolatedU(7.0D);
                f5 = (double)iTextureID.getInterpolatedU(9.0D);
                f6 = (double)iTextureID.getMinV();
                f7 = iTextureID.getInterpolatedV(2.0D);
            } 
            else if ( j2 == 2 )
            {
                f4 = (double)iTextureID.getInterpolatedU(7.0D);
                f5 = (double)iTextureID.getInterpolatedU(9.0D);
                f6 = (double)iTextureID.getMinV();
                f7 = (double)iTextureID.getMaxV();
            }
            
            if ( j2 == 0 )
            {
                vec3d = avec3d[0];
                vec3d1 = avec3d[1];
                vec3d2 = avec3d[2];
                vec3d3 = avec3d[3];
            } 
            else if ( j2 == 1 )
            {
                vec3d = avec3d[7];
                vec3d1 = avec3d[6];
                vec3d2 = avec3d[5];
                vec3d3 = avec3d[4];
            } 
            else if ( j2 == 2 )
            {
                vec3d = avec3d[1];
                vec3d1 = avec3d[0];
                vec3d2 = avec3d[4];
                vec3d3 = avec3d[5];
            } 
            else if ( j2 == 3 )
            {
                vec3d = avec3d[2];
                vec3d1 = avec3d[1];
                vec3d2 = avec3d[5];
                vec3d3 = avec3d[6];
            } 
            else if ( j2 == 4 )
            {
                vec3d = avec3d[3];
                vec3d1 = avec3d[2];
                vec3d2 = avec3d[6];
                vec3d3 = avec3d[7];
            } 
            else if ( j2 == 5 )
            {
                vec3d = avec3d[0];
                vec3d1 = avec3d[3];
                vec3d2 = avec3d[7];
                vec3d3 = avec3d[4];
            }
            
            tessellator.addVertexWithUV(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, f4, f7);
            tessellator.addVertexWithUV(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, f5, f7);
            tessellator.addVertexWithUV(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, f5, f6);
            tessellator.addVertexWithUV(vec3d3.xCoord, vec3d3.yCoord, vec3d3.zCoord, f4, f6);
        }

        return true;
    }  
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
        // render the base
        
        renderBlocks.setRenderBounds( GetBaseBoundsFromPool() );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, -0.5F, -0.5F, -0.5F, 0 );

        // render the shaft
        
        float fHalfWidth = ( 1.0F / 16.0F );
        
        renderBlocks.setRenderBounds( 
        	0.5F - fHalfWidth, m_dBaseHeight, 0.5F - fHalfWidth, 
    		0.5F + fHalfWidth, 1F, 0.5F + fHalfWidth );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, this, -0.5F, -0.5F, -0.5F, m_IconShaft );        
    }
    
    @Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
	{
		return GetShaftSelectionBoundsFromPool().offset( i, j, k );
	}
}