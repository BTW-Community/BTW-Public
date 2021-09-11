// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11; // client only

public class FCBlockAestheticVegetation extends Block
{
    public final static int m_iSubtypeVineTrap = 0;
    public final static int m_iSubtypeVineTrapTriggeredByEntity = 1;
    public final static int m_iSubtypeBloodWoodSapling = 2;
    public final static int m_iSubtypeBloodLeaves = 3; // deprecated
    public final static int m_iSubtypeVineTrapUpsideDown = 4;
    public final static int m_iSubtypeVineTrapUpsideDownTriggeredByEntity = 5;
    
    public final static int m_iNumSubtypes = 6;
    
    private final static double m_dVineTrapHeight = ( 2D / 16D );

    private final static float m_fHardness = 0.2F;
    
	private final static int m_iTickRate = 10;
    
    public final static int m_iBloodWoodSaplingMinTrunkHeight = 4;
    
    public FCBlockAestheticVegetation( int iBlockID )
    {
        super( iBlockID, Material.leaves );
        
        setHardness( m_fHardness );
        SetAxesEffectiveOn();
        
        SetBuoyancy( 1F );
        
		SetFireProperties( FCEnumFlammability.EXTREME );
		
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "fcBlockAestheticVegetation" );
        
        setTickRandomly( true );           
		
		setCreativeTab( CreativeTabs.tabDecorations );		
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
		int iSubtype = iMetadata;
		
		if ( iSubtype == m_iSubtypeVineTrap )
		{
			if ( iFacing != 1 )
			{
				boolean bUpsideDown = true;
				
				if ( iFacing >= 2 )
				{
					if ( fClickY < 0.5F )
					{
						bUpsideDown = false;
					}
				}
				
				if ( bUpsideDown )
				{
					return m_iSubtypeVineTrapUpsideDown;
				}
			}
		}
		
		return iMetadata;
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iChangedBlockID )
    {
        super.onNeighborBlockChange( world, i, j, k, iChangedBlockID );
        
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeBloodWoodSapling )
    	{
    		ValidateBloodWoodSapling( world, i, j, k );
    	}
    }
    
	@Override
    public boolean canBlockStay( World world, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeBloodWoodSapling )
    	{
    		return CanBloodwoodSaplingStayAtLocation( world, i, j, k );
    	}
    	else
    	{
    		return super.canBlockStay( world, i, j, k );
    	}
    }
    
	@Override
	public int damageDropped( int iMetadata )
    {
    	int iSubtype = iMetadata;

    	switch ( iSubtype )
    	{
			case m_iSubtypeVineTrap:
			case m_iSubtypeVineTrapTriggeredByEntity:
			case m_iSubtypeVineTrapUpsideDown:
			case m_iSubtypeVineTrapUpsideDownTriggeredByEntity:
				
	    		iSubtype = m_iSubtypeVineTrap;
	    		
	    		break;
	    		
			case m_iSubtypeBloodLeaves:

	    		iSubtype = m_iSubtypeBloodWoodSapling;
	    		
	    		break;
    	}
    	
        return iSubtype; 
    }
    
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetaData, float fChance, int iFortuneModifier )
    {
		if ( iMetaData == m_iSubtypeBloodLeaves )
		{
	        if ( world.isRemote )
	        {
	            return;
	        }
	        
	        int iNumDropped = world.rand.nextInt( 20 ) != 0 ? 0 : 1;
	        
	        for( int iTempCount = 0; iTempCount < iNumDropped; iTempCount++ )
	        {
	            if ( world.rand.nextFloat() > fChance )
	            {
	                continue;
	            }
	            
	            int iItemID = idDropped( iMetaData, world.rand, iFortuneModifier );
	            
	            if ( iItemID > 0 )
	            {
	                dropBlockAsItem_do( world, i, j, k, new ItemStack( iItemID, 1, damageDropped( iMetaData ) ) );
	            }
	        }
		}
		else
		{
			super.dropBlockAsItemWithChance( world, i, j, k, iMetaData, fChance, iFortuneModifier );
		}
    }
	
	@Override
    protected void dropBlockAsItem_do( World world, int i, int j, int k, ItemStack itemStack )
    {
		if ( itemStack.itemID == blockID && itemStack.getItemDamage() == m_iSubtypeBloodWoodSapling )
		{		
			// special case bloodwood saplings to generate a self-planting EntityItem
			
	        if ( world.isRemote)
	        {
	            return;
	        }
	        else
	        {
	            float f = 0.7F;
	            
	            double d = (double)( world.rand.nextFloat() * f) + (double)(1F - f) * 0.5D;
	            double d1 = (double)( world.rand.nextFloat() * f) + (double)(1F - f) * 0.5D;
	            double d2 = (double)( world.rand.nextFloat() * f) + (double)(1F - f) * 0.5D;
	            
	            EntityItem entityitem = new FCEntityItemBloodWoodSapling( world, (double)i + d, (double)j + d1, (double)k + d2, itemStack );
	            
	            entityitem.delayBeforeCanPickup = 10;
	            world.spawnEntityInWorld(entityitem);
	            
	            return;
	        }
		}
		else
		{
			super.dropBlockAsItem_do( world, i, j, k, itemStack );
		}
    }
    
	@Override
    public void harvestBlock( World world, EntityPlayer entityPlayer, int i, int j, int k, int iMetaData )
    {
        if ( 
    		!world.isRemote && 
    		entityPlayer.getCurrentEquippedItem() != null && 
    		entityPlayer.getCurrentEquippedItem().itemID == Item.shears.itemID &&
    		iMetaData == m_iSubtypeBloodLeaves
		)
        {
            dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcBlockBloodLeaves, 1, 0 ) );
            
            entityPlayer.getCurrentEquippedItem().damageItem( 1, entityPlayer );
        } 
        else
        {
            super.harvestBlock( world, entityPlayer, i, j, k, iMetaData );
        }
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeVineTrap ||
    		iSubtype == m_iSubtypeVineTrapTriggeredByEntity ||
    		iSubtype == m_iSubtypeVineTrapUpsideDownTriggeredByEntity ||
    		iSubtype == m_iSubtypeVineTrapUpsideDown ||
    		iSubtype == m_iSubtypeBloodWoodSapling )
    	{
			return null;
    	}
    	
    	return super.getCollisionBoundingBoxFromPool( world, i, j, k );
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( blockAccess, i, j, k );
    	
    	return GetBlockBoundsFromPoolBasedOnSubtype( iSubtype );    	
    }
    
	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
    }
    
	@Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
		return 0.8F;
    }
    
	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeVineTrap || 
    		iSubtype == m_iSubtypeVineTrapUpsideDown  ||
        	iSubtype == m_iSubtypeVineTrapTriggeredByEntity || 
        	iSubtype == m_iSubtypeVineTrapUpsideDownTriggeredByEntity ||
        	iSubtype == m_iSubtypeBloodWoodSapling )
    	{
        	if ( entity.IsAffectedByMovementModifiers() && entity.onGround )
        	{
	            entity.motionX *= 0.8D;
	            entity.motionZ *= 0.8D;
        	}            
		}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeVineTrapTriggeredByEntity || 
    		iSubtype == m_iSubtypeVineTrapUpsideDownTriggeredByEntity )
    	{
    		// these subtypes are no longer relevant...just revert back to normal
    		
    		SetSubtype( world, i, j, k, iSubtype - 1 );
    	}    			
    	else if ( iSubtype == m_iSubtypeBloodWoodSapling )
    	{
    		if ( ValidateBloodWoodSapling( world, i, j, k ) )
    		{    		
	    		if ( random.nextInt( 14 ) == 0 )
	    		{
	    	        // verify if we're in the nether
	    	        
	    	        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
	    	        
	    	        if ( worldchunkmanager != null )
	    	        {
	    	            BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt( i, k );
	    	            
	    	            if( biomegenbase instanceof BiomeGenHell )
	    	            {
	    	    			AttemptToGrowBloodwoodSapling( world, i, j, k, random );
	    	            }
	    	        }
	    	        
	    		}
    		}
    	}
    }
    
	@Override
    public boolean OnBlockSawed( World world, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( world, i, j, k );

		if (  iSubtype == m_iSubtypeBloodWoodSapling )
		{
			return false;
		}
		
		return super.OnBlockSawed( world, i, j, k );
    }
    
	@Override
    public boolean DoesBlockHopperEject( World world, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
		if ( iSubtype == m_iSubtypeVineTrap ||
			iSubtype == m_iSubtypeVineTrapTriggeredByEntity ||
			iSubtype == m_iSubtypeBloodWoodSapling ||
			iSubtype == m_iSubtypeVineTrapUpsideDown ||
			iSubtype == m_iSubtypeVineTrapUpsideDownTriggeredByEntity )
		{
			return false;
		}
		else
		{		
			return super.DoesBlockHopperEject( world, i, j, k );
		}
    }
    
	@Override
    public boolean canPlaceBlockOnSide( World world, int i, int j, int k, 
    	int iSide, ItemStack stack )
    {
		if ( stack != null && stack.getItemDamage() == 
			FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling )
		{
			if ( !CanBloodwoodSaplingStayAtLocation( world, i, j, k ) )
			{
				return false;
			}
		}
		
		return super.canPlaceBlockOnSide( world, i, j, k, iSide, stack );
    }
    
    //------------- Class Specific Methods ------------//
    
    public int GetSubtype( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return blockAccess.getBlockMetadata( i, j, k );
    }
    
    public void SetSubtype( World world, int i, int j, int k, int iSubtype )
    {
    	world.setBlockMetadata( i, j, k, iSubtype );
    }
    
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnSubtype( int iSubtype )
    {
    	switch ( iSubtype )
    	{
			case m_iSubtypeVineTrap:
			case m_iSubtypeVineTrapTriggeredByEntity:
				
		       	return AxisAlignedBB.getAABBPool().getAABB( 
		       		0D, 0D, 0D, 1D, m_dVineTrapHeight, 1D );
		    	
			case m_iSubtypeVineTrapUpsideDown:
			case m_iSubtypeVineTrapUpsideDownTriggeredByEntity:
				
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 1D - m_dVineTrapHeight, 0D, 1D, 1D, 1D );
		    	
			case m_iSubtypeBloodWoodSapling:				
			case m_iSubtypeBloodLeaves:				
	    	default:
	    		
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		0D, 0D, 0D, 1D, 1D, 1D );
    	}	
    }
    
	public boolean ValidateBloodWoodSapling( World world, int i, int j, int k )
	{
        if ( !canBlockStay( world, i, j, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata(i, j, k), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
            
            return false;
        }
        
        return true;
	}
	
	public void AttemptToGrowBloodwoodSapling( World world, int i, int j, int k, Random random )
	{
		// check to make sure there's enough room for a trunk
		
		for ( int iTempJ = j + 1; iTempJ < j + m_iBloodWoodSaplingMinTrunkHeight; iTempJ++ )
		{
			// test for air and against the height limit of the world
			
			if ( iTempJ >= 256 || !world.isAirBlock( i, iTempJ, k ) )
			{
				return;
			}
		}
		
		// create the basic trunk
		
		for ( int iTempJ = j; iTempJ < j + m_iBloodWoodSaplingMinTrunkHeight - 1; iTempJ++ )
		{
			world.setBlockAndMetadataWithNotify( i, iTempJ, k, FCBetterThanWolves.fcBloodWood.blockID, 0 );			
 		}
		
		// create the top of the trunk
		
		FCBlockBloodWood bloodWoodBlock = (FCBlockBloodWood)(FCBetterThanWolves.fcBloodWood);
		
		int iTrunkTopJ = j + m_iBloodWoodSaplingMinTrunkHeight - 1;
		
		world.setBlockAndMetadataWithNotify( i, iTrunkTopJ, k, FCBetterThanWolves.fcBloodWood.blockID, 1 );
		
		bloodWoodBlock.GrowLeaves( world, i, iTrunkTopJ, k );
		
		// grow the top of the trunk and any surrounding blocks it produces
		
		bloodWoodBlock.Grow( world, i, iTrunkTopJ, k, random );
		
    	for ( int tempI = i - 1; tempI <= i + 1; tempI++ )
    	{
	    	for ( int tempJ = iTrunkTopJ; tempJ <= iTrunkTopJ + 1; tempJ++ ) // only look at the same level and above the block
	    	{
		    	for ( int tempK = k - 1; tempK <= k + 1; tempK++ )
		    	{
		    		if ( world.getBlockId( tempI, tempJ, tempK ) == FCBetterThanWolves.fcBloodWood.blockID )
		    		{
		    			int iGrowthDirection = bloodWoodBlock.GetFacing( world, tempI, tempJ, tempK );
		    			
		    			if ( iGrowthDirection != 0 )
		    			{
			    			if ( tempI != i || tempJ != iTrunkTopJ || tempK != k )
			    			{
			    				bloodWoodBlock.Grow( world, tempI, tempJ, tempK, random );		    				
			    			}
		    			}
		    		}
		    	}
	    	}
    	}
    	
    	// whimper sound
    	
        world.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID, i, j, k, 0 );            
	}
	
	public boolean CanBloodwoodSaplingStayAtLocation( World world, int i, int j, int k )
	{
		int iBlockBelowID = world.getBlockId( i, j - 1, k );
		
		if ( iBlockBelowID == Block.slowSand.blockID )
		{
			return true;
		}
		else if ( iBlockBelowID == FCBetterThanWolves.fcPlanter.blockID )
		{
			if ( ((FCBlockPlanter)FCBetterThanWolves.fcPlanter).GetPlanterType( world, i, j - 1, k ) == FCBlockPlanter.m_iTypeSoulSand )
			{
				return true;
			}
		}
		
		return false;
	}
	
	//----------- Client Side Functionality -----------//
	
    private final static int m_iVineTrapTextureID = 105;
    private final static int m_iBloodWoodSaplingTextureID = 108;
    private final static int m_iBloodLeavesTextureID = 109;

    private Icon m_IconVineTrap;
    private Icon m_IconSaplingBloodWood;
    private Icon m_IconLeavesBloodWood;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		Icon vineIcon = register.registerIcon( "fcBlockVineTrap" );
		
		blockIcon = vineIcon; // for hit effects
		
		m_IconVineTrap = vineIcon;
		m_IconSaplingBloodWood = register.registerIcon( "fcBlockSaplingBloodWood" );
		m_IconLeavesBloodWood = register.registerIcon( "fcBlockLeavesBloodWood_old" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	int iSubtype = iMetadata;
    	
    	switch ( iSubtype )
    	{
    		case m_iSubtypeVineTrap:
    		case m_iSubtypeVineTrapTriggeredByEntity:
    		case m_iSubtypeVineTrapUpsideDown:
    		case m_iSubtypeVineTrapUpsideDownTriggeredByEntity:
    			
    			return m_IconVineTrap;
    			
    		case m_iSubtypeBloodWoodSapling:
    			
    			return m_IconSaplingBloodWood;
    			
    		case m_iSubtypeBloodLeaves:
    			
    			return m_IconLeavesBloodWood;
    	}
    	
    	return blockIcon;
    }

	@Override
    public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeVineTrap ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeBloodWoodSapling ) );
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {    	
    	if ( iSide >= 2 )
    	{
	    	if ( blockAccess.getBlockId( iNeighborI, iNeighborJ, iNeighborK ) == blockID )
	    	{
	    		int iTargetSubtype = blockAccess.getBlockMetadata( iNeighborI, iNeighborJ, iNeighborK );
	    		
	    		if ( iTargetSubtype == m_iSubtypeVineTrap ||
    				iTargetSubtype == m_iSubtypeVineTrapTriggeredByEntity )
	    		{
	    			FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK );
	    			
	    			sourcePos.AddFacingAsOffset( Block.GetOppositeFacing( iSide ) );
	    			
	    			int iSourceSubtype = blockAccess.getBlockMetadata( sourcePos.i, sourcePos.j, sourcePos.k );	    			
	    			
		    		if ( iSourceSubtype == m_iSubtypeVineTrap ||
	    				iSourceSubtype == m_iSubtypeVineTrapTriggeredByEntity )
		    		{
		    			return false;
		    		}
	    		}
	    		else if ( iTargetSubtype == m_iSubtypeVineTrapUpsideDown ||
	    				iTargetSubtype == m_iSubtypeVineTrapUpsideDownTriggeredByEntity )
	    		{
	    			FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK );
	    			
	    			sourcePos.AddFacingAsOffset( Block.GetOppositeFacing( iSide ) );
	    			
	    			int iSourceSubtype = blockAccess.getBlockMetadata( sourcePos.i, sourcePos.j, sourcePos.k );	    			
	    			
		    		if ( iSourceSubtype == m_iSubtypeVineTrapUpsideDown ||
	    				iSourceSubtype == m_iSubtypeVineTrapUpsideDownTriggeredByEntity )
		    		{
		    			return false;
		    		}
	    		}
	    	}
    	}
    	
    	return true;
    }
    
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( iMetadata == m_iSubtypeBloodLeaves )
		{
			return FCBetterThanWolves.fcBlockBloodLeaves.blockID;
		}
		
        return idDropped( iMetadata, world.rand, 0 );
    }

	@Override
    public int getDamageValue( World world, int i, int j, int k )
    {
		// used only by pick block
		
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( iMetadata == m_iSubtypeBloodLeaves )
		{
			return 0;
		}
		
		return super.getDamageValue( world, i, j, k );		
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	int iSubtype = GetSubtype( blockAccess, i, j, k );

        renderBlocks.setRenderBounds( GetBlockBoundsFromPoolBasedOnSubtype( iSubtype ) );
        
    	switch ( iSubtype )
    	{
			case m_iSubtypeBloodWoodSapling:
				
		    	return renderBlocks.renderCrossedSquares( this, i, j, k );    	
    			
			default:
				
				return renderBlocks.renderStandardBlock( this, i, j, k );
    	}				
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	renderBlocks.setRenderBounds( GetBlockBoundsFromPoolBasedOnSubtype( iItemDamage ) );
    	
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, 
        	-0.5F, -0.5F, -0.5F, iItemDamage );
    }
    
    @Override
    public boolean DoesItemRenderAsBlock( int iItemDamage )
    {
    	if ( iItemDamage == m_iSubtypeBloodWoodSapling )
    	{
    		// force bloodwood saplings to use the item renderer despite being a block
    		
    		return false;
    	}
    	else
    	{
    		return super.DoesItemRenderAsBlock( iItemDamage );
    	}
    }    
}