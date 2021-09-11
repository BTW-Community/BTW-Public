// FCMOD

package net.minecraft.src;

import java.util.Random;

//import org.lwjgl.opengl.GL11; //client only

public class FCBlockHopper extends BlockContainer
	implements FCIBlockMechanical
{
	protected static final int m_iTickRate = 10;
	
	protected static final float m_fSpoutHeight = 0.25F;
	protected static final float m_fSpoutWidth = ( 6F / 16F );
	protected static final float m_fSpoutHalfWidth = ( m_fSpoutWidth / 2F );
	
	protected FCBlockHopper( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );
        
        setHardness( 2F );
        
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
		SetFireProperties( FCEnumFlammability.PLANKS );
		
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );

        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockHopper" );
        
        setTickRandomly( true );
        
        setCreativeTab( CreativeTabs.tabRedstone );
	}	
	
	@Override
    public int tickRate( World world )
    {
    	return m_iTickRate;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
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
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	
    	if ( IsBlockOn( world, i, j, k ) != bReceivingPower &&
			!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )    		
    	{    		
	        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	}
    	
    	// the change in state may unblock the hopper, and this forces a recheck
    	
        ((FCTileEntityHopper)world.getBlockTileEntity( i, j, k )).m_bOutputBlocked = false;;
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote )
        {
            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getBlockTileEntity( i, j, k );
            
        	if ( player instanceof EntityPlayerMP ) // should always be true
        	{
        		FCContainerHopper container = new FCContainerHopper( player.inventory, tileEntityHopper );
        		
        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcHopperContainerID );        		
        	}
        }
        
        return true;
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityHopper();
    }

	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = IsBlockOn( world, i, j, k );
    	boolean bFull = IsHopperFull( world, i, j, k );
    	boolean bRedstone = IsRedstoneOutputOn( world, i, j, k );
    	
    	if ( bOn != bReceivingPower )
    	{
	        world.playAuxSFX( FCBetterThanWolves.m_iHopperCloseSoundAuxFXID, i, j, k, 0 );							        
	        
    		SetBlockOn( world, i, j, k, bReceivingPower );
    	}
    	
    	if ( bFull != bRedstone )
    	{
	        world.playAuxSFX( FCBetterThanWolves.m_iRedstonePowerClickSoundAuxFXID, i, j, k, 0 );							        
            
            SetRedstoneOutputOn( world, i, j, k, bFull );
    	}
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        FCUtilsInventory.EjectInventoryContents( world, i, j, k, (IInventory)world.getBlockTileEntity(i, j, k) );

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }

	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		// don't collect items on the client, as it's dependent on the state of the inventory
		
		if ( !world.isRemote )
		{
			if ( entity instanceof EntityItem )
			{
				OnEntityItemCollidedWithBlock( world, i, j, k, (EntityItem)entity );
			}
			else if ( entity instanceof EntityXPOrb )
			{
				OnEntityXPOrbCollidedWithBlock( world, i, j, k, (EntityXPOrb)entity );			
			}
		}		
    }
	
	@Override
    public int isProvidingWeakPower( IBlockAccess iBlockAccess, int i, int j, int k, int l)
    {
		if ( IsRedstoneOutputOn( iBlockAccess, i, j, k) )
		{
			return 15;
		}
		
    	return 0;
    }
    
	@Override
    public int isProvidingStrongPower( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	// at present, Hoppers don't indirectly power anything...they have to be hooked up directly.
    	
    	return 0;
    }
    
	@Override
    public boolean canProvidePower()
    {
        return true;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	// spout
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.5D - m_fSpoutHalfWidth, 0D, 0.5D - m_fSpoutHalfWidth, 
			0.5D + m_fSpoutHalfWidth, m_fSpoutHeight, 0.5D + m_fSpoutHalfWidth );
		
		// body
		
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0D, m_fSpoutHeight, 0D, 1D, 1D, 1D );
    	
        return rayTrace.GetFirstIntersection();    	
    }
    
    @Override
    public void OnArrowCollide( World world, int i, int j, int k, EntityArrow arrow )
    {
    	if ( !world.isRemote && !arrow.isDead && arrow.CanHopperCollect() )
    	{    	
	        // check if arrow is within the collection zone       
	        
	        Vec3 arrowPos = Vec3.createVectorHelper( arrow.posX, arrow.posY, arrow.posZ );
	        
	    	AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB( (float)i, (float)j + 0.9F, (float)k, 
					(float)(i + 1), (float)j + 1.1F, (float)(k + 1) );
	    	
	    	if ( collectionZone.isVecInside( arrowPos ) )
	    	{
	            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getBlockTileEntity( i, j, k );
	            
    			ItemStack newItemStack = new ItemStack( arrow.GetCorrespondingItem(), 1, 0 );
    			
	        	if ( tileEntityHopper.CanCurrentFilterProcessItem( newItemStack ) )
	        	{
        			if ( FCUtilsInventory.AddItemStackToInventoryInSlotRange( tileEntityHopper, 
        				newItemStack, 0, 17 ) )
        			{        				
        				arrow.setDead();
        				
				        world.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, 
				        	i, j, k, 0 );							        
        			}        				
	        	}
	    	}
    	}
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemGear.itemID, 1, 0, fChanceOfDrop );
		
		return true;
	}
	
    //------------- FCIBlockMechanical -------------//
    
	@Override
    public boolean CanOutputMechanicalPower()
    {
    	return false;
    }

	@Override
    public boolean CanInputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean IsInputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return FCUtilsMechPower.IsBlockPoweredByAxle( world, i, j, k, this ) || 
			FCUtilsMechPower.IsBlockPoweredByHandCrank( world, i, j, k );  	
    }    

	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		return iFacing >= 2;
	}

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakHopper( world, i, j, k );
	}
	
    //------------- Class Specific Methods ------------//
    
    public boolean IsBlockOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 1 ) > 0;    
	}
    
    public void SetBlockOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ); // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 1;
    	}
    	else
    	{
    		iMetaData &= ~1;
    	}
    	
        world.setBlockMetadataWithNotifyNoClient( i, j, k, iMetaData );
    }
    
    public boolean IsHopperFull( IBlockAccess iBlockAccess, int i, int j, int k )
	{	
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 2 ) > 0;
	}
    
    public void SetHopperFull( World world, int i, int j, int k, boolean bOn )
    {
    	boolean bOldOn = IsHopperFull( world, i, j, k );
    	
    	if ( bOldOn != bOn )
    	{
    		int iMetaData = world.getBlockMetadata( i, j, k );
    		
    		if ( bOn )
    		{
    			iMetaData |= 2;
    		}
    		else
    		{
    			iMetaData &= ~2;
    		}
    		
            world.setBlockMetadataWithNotifyNoClient( i, j, k, iMetaData );
            
        	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	}    	
    }
    
    public boolean IsRedstoneOutputOn( IBlockAccess iBlockAccess, int i, int j, int k )
	{	
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 4 ) > 0;
	}
    
    public void SetRedstoneOutputOn( World world, int i, int j, int k, boolean bOn )
    {
    	if ( bOn != IsRedstoneOutputOn( world, i, j, k ) )
    	{    			
			int iMetaData = world.getBlockMetadata( i, j, k );
			
			if ( bOn )
			{
				iMetaData |= 4;
			}
			else
			{
				iMetaData &= ~4;
			}
			
	        world.setBlockMetadataWithNotifyNoClient( i, j, k, iMetaData );
	        
	        // we have to notify in a radius as some redstone blocks get their power state from up to two blocks away
	        
	        world.notifyBlocksOfNeighborChange( i, j - 1, k, blockID );
	        world.notifyBlocksOfNeighborChange( i, j + 1, k, blockID );
	        world.notifyBlocksOfNeighborChange( i - 1, j, k, blockID );
	        world.notifyBlocksOfNeighborChange( i + 1, j, k, blockID );
	        world.notifyBlocksOfNeighborChange( i, j, k - 1, blockID );
	        world.notifyBlocksOfNeighborChange( i, j, k + 1, blockID );
    	}
    }
    
    public boolean HasFilter( IBlockAccess iBlockAccess, int i, int j, int k )
	{	
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 8 ) > 0;
	}
    
    public void SetHasFilter( World world, int i, int j, int k, boolean bOn )
    {
		int iMetaData = world.getBlockMetadata( i, j, k );
		
		if ( bOn )
		{
			iMetaData |= 8;
		}
		else
		{
			iMetaData &= ~8;
		}
		
        world.setBlockMetadataWithNotifyNoClient( i, j, k, iMetaData );
    }
    
	public void BreakHopper( World world, int i, int j, int k )
	{
		DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
    
    
    private void OnEntityItemCollidedWithBlock( World world, int i, int j, int k, EntityItem entityItem )
    {
        // check if item is within the collection zone       
        
        float fHopperHeight = 1F;
        
    	AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB( 
    		(float)i, (float)j + fHopperHeight, (float)k, 
			(float)(i + 1), (float)j + fHopperHeight + 0.05F, (float)(k + 1) );
    	
    	if ( entityItem.boundingBox.intersectsWith( collectionZone ) )
    	{    	
	        if ( !entityItem.isDead )
	        {
	        	Item targetItem = Item.itemsList[entityItem.getEntityItem().itemID];
	        	ItemStack targetStack = entityItem.getEntityItem();
	        	
	            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getBlockTileEntity( i, j, k );
	            
	        	if ( tileEntityHopper.CanCurrentFilterProcessItem( targetStack ) )
	        	{
	            	Item filterItem = tileEntityHopper.GetFilterItem();
	            	
	            	if ( filterItem != null && filterItem.CanTransformItemIfFilter( targetStack ) )
	            	{
			        	int iTargetItemID = targetItem.itemID;
			        	
		        		if ( iTargetItemID == Block.gravel.blockID )
		        		{
		        			// convert the gravel to sand and flint
		        			
		        			ItemStack sandItemStack = new ItemStack( Block.sand.blockID, targetStack.stackSize, 0 );
		        			
		        			int iSandSwallowed = 0;
		        			
		        			// attempt to add the sand to the inventory
		        			
		        			if ( FCUtilsInventory.AddItemStackToInventoryInSlotRange( tileEntityHopper, 
		        				sandItemStack, 0, 17 ) )
		        			{
		        				// the whole stack has been swallowed
		        				
		        				iSandSwallowed = targetStack.stackSize;
		        				
		        				entityItem.setDead();
		        			}
		        			else
		        			{
		        				// the entire stack hasn't been swallowed
		        				
		        				iSandSwallowed = entityItem.getEntityItem().stackSize - sandItemStack.stackSize;
		        				
		        				entityItem.getEntityItem().stackSize -= iSandSwallowed;
		        			}
		        			
		        			
		        			if ( iSandSwallowed > 0 )
		        			{
						        world.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, i, j, k, 0 );							        
					            
			        			// convert any gravel that has been swallowed into flint at the original 
		        				// location
		        				
			        			ItemStack flintItemStack = new ItemStack( Item.flint.itemID, 
		        					iSandSwallowed, 0 );
			        			
			        	        EntityItem flintEntityitem = new EntityItem( world,
	        	        			entityItem.posX, entityItem.posY, entityItem.posZ,
			        	    		flintItemStack );
	
			        	        flintEntityitem.delayBeforeCanPickup = 10;
			        	        
			        	        world.spawnEntityInWorld( flintEntityitem );
		        			}
		        		}
		        		else if ( iTargetItemID == FCBetterThanWolves.fcItemGroundNetherrack.itemID )
		        		{
		        			ConvertItemAndIncrementSouls( world, i, j, k, entityItem, 
		        				FCBetterThanWolves.fcItemHellfireDust.itemID, 0 );
		        		}
		        		else if ( iTargetItemID == FCBetterThanWolves.fcItemSoulDust.itemID )
		        		{
		        			ConvertItemAndIncrementSouls( world, i, j, k, entityItem, 
		        				FCBetterThanWolves.fcItemSawDust.itemID, 0 );
		        		}
		        		else if ( iTargetItemID == Item.lightStoneDust.itemID )
		        		{
		        			ConvertItemAndIncrementSouls( world, i, j, k, entityItem, 
		        				FCBetterThanWolves.fcItemBrimstone.itemID, 0 );
		        		}
	            	}
	            	else if ( FCUtilsInventory.AddItemStackToInventoryInSlotRange( tileEntityHopper, 
        				entityItem.getEntityItem(), 0, 17 ) )
        			{
				        world.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, i, j, k, 0 );							        
			            
			            entityItem.setDead();
        			}
	        	}		        
            }
    	}
    }	
    
    private void ConvertItemAndIncrementSouls( World world, int i, int j, int k, EntityItem inputEntityItem, int iOutputItemID, int iOutputItemDamage )
    {
        FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getBlockTileEntity( i, j, k );
        
		ItemStack outputItemStack = new ItemStack(  iOutputItemID, 
			inputEntityItem.getEntityItem().stackSize, iOutputItemDamage );
		
        EntityItem outputEntityItem = new EntityItem( world,
        	inputEntityItem.posX, inputEntityItem.posY, inputEntityItem.posZ,
			outputItemStack );

        outputEntityItem.delayBeforeCanPickup = 10;
        
		tileEntityHopper.IncrementContainedSoulCount( outputItemStack.stackSize );
		
        world.spawnEntityInWorld( outputEntityItem );
        
        world.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID, i, j, k, 0 );
        
		inputEntityItem.setDead();
    }
    
    private void OnEntityXPOrbCollidedWithBlock( World world, int i, int j, int k, EntityXPOrb entityXPOrb )
    {
        if ( !entityXPOrb.isDead )
        {
	        // check if item is within the collection zone
	    	
	        float fHopperHeight = 1F;
	        
	    	AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB( (float)i, (float)j + fHopperHeight, (float)k, 
					(float)(i + 1), (float)j + fHopperHeight + 0.05F, (float)(k + 1) );
	    	
	    	if ( entityXPOrb.boundingBox.intersectsWith( collectionZone ) )
	    	{    	
	            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getBlockTileEntity( i, j, k );
	
	    		Item filterItem = tileEntityHopper.GetFilterItem();
	    		
	    		if ( filterItem != null && filterItem.itemID == Block.slowSand.blockID )
	    		{
	    			if ( tileEntityHopper.AttemptToSwallowXPOrb( world, i, j, k, entityXPOrb ) )
	    			{
				        world.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, i, j, k, 0 );							        
	    			}
	    		}	    		
	    	}
        }
    }	
    
	//----------- Client Side Functionality -----------//
}
