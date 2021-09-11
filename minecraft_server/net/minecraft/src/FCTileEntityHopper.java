// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCTileEntityHopper extends TileEntity
    implements IInventory, FCITileEntityDataPacketHandler
{
	private final int m_iInventorySize = 19;
	private final int m_iStackSizeLimit = 64;
	private final double m_dMaxPlayerInteractionDist = 64D;
	
	private final int m_iStackSizeToEject = 8;
    private final int m_iTimeToEject = 3; 

	private final int m_iOverloadSoulCount = 8;
	
	private final int m_iXPInventorySpace = 100;
	private final int m_iXPEjectUnitSize = 20;
	private final int m_iXPDelayBetweenDrops = 10; 
	
    private ItemStack m_Contents[];
    
    private int m_iEjectCounter;
    private int m_iContainedSoulCount;
    private int m_iContainedXPCount;
    private int m_iHopperXPDropDelayCount;
    
    public boolean m_bOutputBlocked;    
    
    public int m_iMechanicalPowerIndicator; // used to communicate power status from server to client.  0 for off, 1 for on
    
    // state variables used to communicate basic inventory info to the client
    
    protected int m_iFilterItemID;
    public short m_sStorageSlotsOccupied;
    
    public FCTileEntityHopper()
    {
    	m_Contents = new ItemStack[m_iInventorySize];
    	
    	m_iEjectCounter = 0;
    	m_iContainedSoulCount = 0;
    	
    	m_iContainedXPCount = 0;
    	
    	m_iHopperXPDropDelayCount = m_iXPDelayBetweenDrops;
    	
    	m_bOutputBlocked = false;
    	
    	m_iMechanicalPowerIndicator = 0;
    	m_iFilterItemID = 0;
    	m_sStorageSlotsOccupied = 0;
    }

    @Override
    public void readFromNBT( NBTTagCompound nbttagcompound )
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        
        m_Contents = new ItemStack[getSizeInventory()];
        
        for ( int i = 0; i < nbttaglist.tagCount(); i++ )
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt( i );
            
            int j = nbttagcompound1.getByte( "Slot" ) & 0xff;
            
            if ( j >= 0 && j < m_Contents.length )
            {
            	m_Contents[j] = ItemStack.loadItemStackFromNBT( nbttagcompound1 );
            }
        }
        
        // note, this is misnamed, but we've already release with it as is, so it will have to remain this way
        if( nbttagcompound.hasKey( "grindCounter" ) )
        {
        	m_iEjectCounter = nbttagcompound.getInteger( "grindCounter" );
        }
        
        if( nbttagcompound.hasKey( "iHoppeContainedSoulCount" ) )
        {
        	m_iContainedSoulCount = nbttagcompound.getInteger( "iHoppeContainedSoulCount" );
        }
        
        if( nbttagcompound.hasKey( "iHopperContainedXPCount" ) )
        {
        	m_iContainedXPCount = nbttagcompound.getInteger( "iHopperContainedXPCount" );
        }        
        
        ValidateInventoryStateVariables();
    }
    
    @Override
    public void writeToNBT( NBTTagCompound nbttagcompound )
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        
        for ( int i = 0; i < m_Contents.length; i++ )
        {
            if ( m_Contents[i] != null )
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte( "Slot", (byte)i );
                
                m_Contents[i].writeToNBT( nbttagcompound1 );
                
                nbttaglist.appendTag( nbttagcompound1 );
            }
        }     

        nbttagcompound.setTag( "Items", nbttaglist );
        
        // note, this is misnamed, but we've already release with it as is, so it will have to remain this way        
        nbttagcompound.setInteger( "grindCounter", m_iEjectCounter );
        
        nbttagcompound.setInteger( "iHoppeContainedSoulCount", m_iContainedSoulCount );
        
        nbttagcompound.setInteger( "iHopperContainedXPCount", m_iContainedXPCount );
    }

    @Override
    public void updateEntity()
    {
    	if ( worldObj.isRemote )
    	{
    		return;
    	}
    	
    	boolean bHopperOn = ( (FCBlockHopper)FCBetterThanWolves.fcHopper ).
			IsBlockOn( worldObj, xCoord, yCoord, zCoord );
    	
		if ( bHopperOn )
		{
			m_iMechanicalPowerIndicator = 1;
			
			AttemptToEjectXPFromInv();
			
			if ( !m_bOutputBlocked )
			{
				// the hopper is powered, eject items
				
	    		m_iEjectCounter += 1;
	    		
	    		if ( m_iEjectCounter >= m_iTimeToEject )
	    		{
					AttemptToEjectStackFromInv();
					
	    			m_iEjectCounter = 0;    			
	    		}
			}
			else
			{
				m_iEjectCounter = 0;
			}
		}
		else
		{
			m_iMechanicalPowerIndicator = 0;

			m_iEjectCounter = 0;
	    	m_iHopperXPDropDelayCount = 0;
		}

		if ( m_iContainedSoulCount > 0 )
		{		
			// souls can only be trapped if there's a soul sand filter on the hopper
			
			if ( m_iFilterItemID == Block.slowSand.blockID )
			{
		    	int iBlockBelowID = worldObj.getBlockId( xCoord, yCoord - 1, zCoord );
		    	int iBlockBelowMetaData = worldObj.getBlockMetadata( xCoord, yCoord - 1, zCoord );
		    	
				if ( bHopperOn )
				{
					if ( iBlockBelowID != FCBetterThanWolves.fcAestheticNonOpaque.blockID ||
						iBlockBelowMetaData != FCBlockAestheticNonOpaque.m_iSubtypeUrn )
					{
						m_iContainedSoulCount = 0;
					}
				}
			
				if ( m_iContainedSoulCount >= m_iOverloadSoulCount )
				{
					if ( bHopperOn && 
						iBlockBelowID == FCBetterThanWolves.fcAestheticNonOpaque.blockID &&
						iBlockBelowMetaData == FCBlockAestheticNonOpaque.m_iSubtypeUrn )
					{
						// convert the urn below to a soul urn
						
						worldObj.setBlockWithNotify( xCoord, yCoord - 1, zCoord, 0 );

	        			ItemStack newItemStack = new ItemStack( FCBetterThanWolves.fcItemSoulUrn.itemID, 
	        					1, 0 );
	        			
	    	        	FCUtilsItem.EjectStackWithRandomOffset( worldObj, 
    	        			xCoord, yCoord - 1, zCoord, newItemStack );
	    	        	
						// the rest of the souls escape (if any remain)
						
						m_iContainedSoulCount = 0;						
					}
					else
					{
						// the hopper has become overloaded with souls.  Destroy it and generate a ghast.
						
						HopperSoulOverload();						
					}
				}
			}
			else
			{
				m_iContainedSoulCount = 0;
			}
		}
	}
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        nbttagcompound.setInteger( "f", m_iFilterItemID );
        nbttagcompound.setShort( "s", m_sStorageSlotsOccupied );
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, nbttagcompound );
    }
    
    //------------- FCITileEntityDataPacketHandler ------------//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound nbttagcompound )
    {
    	m_iFilterItemID = nbttagcompound.getInteger( "f" );
        m_sStorageSlotsOccupied = nbttagcompound.getShort( "s" );     
        
        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }    
    
    //------------- IInventory implementation -------------//
    
    @Override
    public int getSizeInventory()
    {
        return m_iInventorySize;
    }

    @Override
    public ItemStack getStackInSlot( int iSlot )
    {
        return m_Contents[iSlot];
    }

    @Override
    public ItemStack decrStackSize( int iSlot, int iAmount )
    {
    	return FCUtilsInventory.DecrStackSize( this, iSlot, iAmount );    	
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (m_Contents[par1] != null)
        {
            ItemStack itemstack = m_Contents[par1];
            m_Contents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public void setInventorySlotContents( int iSlot, ItemStack itemstack )
    {
    	m_Contents[iSlot] = itemstack;
    	
        if( itemstack != null && itemstack.stackSize > getInventoryStackLimit() )
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Hopper";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return m_iStackSizeLimit;
    }

    @Override
    public void onInventoryChanged()
    {
    	super.onInventoryChanged();
    	
    	if ( worldObj != null )
    	{
	        m_bOutputBlocked = false;	// the change in inventory may allow the dispenser to eject again.
	        
	        if ( ValidateInventoryStateVariables() )
	        {
		        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
	        }
	        
	        int iOccupiedStacks = FCUtilsInventory.GetNumOccupiedStacksInRange( this, 0, 17 );
	        
	        ( (FCBlockHopper)(FCBetterThanWolves.fcHopper) ).SetHopperFull( worldObj, xCoord, yCoord, zCoord,
	        		m_sStorageSlotsOccupied == 18 );
	        
	        ( (FCBlockHopper)(FCBetterThanWolves.fcHopper) ).SetHasFilter( worldObj, 
	        	xCoord, yCoord, zCoord, m_iFilterItemID > 0 );
	        
    	}
    }
    
    @Override
    public boolean isUseableByPlayer( EntityPlayer entityplayer )
    {
        if( worldObj.getBlockTileEntity( xCoord, yCoord, zCoord ) != this )
        {
            return false;
        }
        
        return ( entityplayer.getDistanceSq( (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D ) 
        	<= m_dMaxPlayerInteractionDist );
    }
    
    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isStackValidForSlot( int iSlot, ItemStack stack )
    {
        return true;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//
 
    public Item GetCurrentFilterItem()
    {
		return Item.itemsList[m_iFilterItemID];
    }
    
    /*
     * Returns true if any changes are made, false otherwise
     */
    private boolean ValidateInventoryStateVariables()
    {
    	boolean bStateChanged = false;
    	
    	int currentFilterID = GetFilterIDBasedOnInventory();
    	
    	if ( currentFilterID != m_iFilterItemID )
    	{
    		m_iFilterItemID = currentFilterID;
    		
    		bStateChanged = true;
    	}
    	
    	short numSlotsOccupied = (short)( FCUtilsInventory.GetNumOccupiedStacksInRange( 
    		this, 0, 17 ) ); 
    	
    	if ( numSlotsOccupied != m_sStorageSlotsOccupied )
    	{
    		m_sStorageSlotsOccupied = numSlotsOccupied;
    		
    		bStateChanged = true;
    	}
    	
    	return bStateChanged;
    }
    
    public int GetFilterIDBasedOnInventory()
    {
    	ItemStack filterStack = getStackInSlot( 18 );
    	
    	if ( filterStack != null && filterStack.stackSize > 0 )
    	{
    		return filterStack.itemID;
    	}
    	
    	return 0;
    }
    
    public Item GetFilterItem()
    {
    	ItemStack filterStack = getStackInSlot( 18 );
    	
    	if ( filterStack != null )
    	{
    		return filterStack.getItem();
    	}
    	
    	return null;    	
    }
    
    public boolean CanCurrentFilterProcessItem( ItemStack itemStack )
    {
    	Item filterItem = GetFilterItem();
    	
    	if ( filterItem != null )
    	{
    		return filterItem.CanItemPassIfFilter( itemStack );
    	}
			
    	return true;
    }
    
    public boolean IsEjecting()
    {
    	return ( (FCBlockHopper)FCBetterThanWolves.fcHopper ).
			IsBlockOn( worldObj, xCoord, yCoord, zCoord );    	
    }
    
    private void AttemptToEjectStackFromInv()
    {
		int iStackIndex = FCUtilsInventory.GetRandomOccupiedStackInRange( this, worldObj.rand, 0, 17 );
		
		if ( iStackIndex >= 0 && iStackIndex <= 17 )
		{
			ItemStack invStack = getStackInSlot( iStackIndex );
			
			int iEjectStackSize;
			
			if ( m_iStackSizeToEject > invStack.stackSize )
			{
				iEjectStackSize = invStack.stackSize;
			}
			else
			{
				iEjectStackSize = m_iStackSizeToEject;
			}
			
			ItemStack ejectStack = new ItemStack( invStack.itemID, iEjectStackSize, invStack.getItemDamage() );
			
			FCUtilsInventory.CopyEnchantments( ejectStack, invStack );
			
			int iTargetI = xCoord;
			int iTargetJ = yCoord - 1;
			int iTargetK = zCoord;
			
			boolean bEjectIntoWorld = false;
			
			if ( worldObj.isAirBlock( iTargetI, iTargetJ, iTargetK ) )
			{
				bEjectIntoWorld = true;
			}
			else
			{
				if ( FCUtilsWorld.IsReplaceableBlock( worldObj, iTargetI, iTargetJ, iTargetK ) )
				{
					bEjectIntoWorld = true;
				}
				else
				{				
					int iTargetBlockID = worldObj.getBlockId( iTargetI, iTargetJ, iTargetK );
					
					Block targetBlock = Block.blocksList[iTargetBlockID];
					
					if ( targetBlock == null || !targetBlock.DoesBlockHopperEject( worldObj, iTargetI, iTargetJ, iTargetK ) )
					{
						bEjectIntoWorld = true;
					}
					else if ( targetBlock.DoesBlockHopperInsert( worldObj, iTargetI, iTargetJ, iTargetK ) )
					{
						m_bOutputBlocked = true;
					}
					else 
					{
						TileEntity targetTileEntity = 
							worldObj.getBlockTileEntity( iTargetI, iTargetJ, iTargetK );
						
						int iNumItemsStored = 0;
						
						if ( targetTileEntity != null && targetTileEntity instanceof IInventory ) 
						{
							int iMinSlotToAddTo = 0;
							int iMaxSlotToAddTo = ((IInventory)targetTileEntity).getSizeInventory() - 1;
							boolean canProcessStack = true;

							if ( iTargetBlockID == Block.furnaceIdle.blockID || 
								iTargetBlockID == Block.furnaceBurning.blockID )
							{
								iMaxSlotToAddTo = 0;
							}
							else if ( iTargetBlockID == FCBetterThanWolves.fcHopper.blockID )
							{
								iMaxSlotToAddTo = 17;
								
								int iTargetFilterID = ((FCTileEntityHopper)targetTileEntity).m_iFilterItemID;
								
								if ( iTargetFilterID > 0 )
								{
									// filters in the hopper below block ejection
									
									canProcessStack = false;
								}								
							}						
							
							if ( canProcessStack )
							{
								boolean bFullStackDeposited;
								
								if ( iTargetBlockID != Block.chest.blockID &&
									iTargetBlockID != FCBetterThanWolves.fcBlockChest.blockID )
								{
									bFullStackDeposited = FCUtilsInventory.AddItemStackToInventoryInSlotRange( (IInventory)targetTileEntity, ejectStack, iMinSlotToAddTo, iMaxSlotToAddTo );
								}
								else
								{
									bFullStackDeposited = FCUtilsInventory.AddItemStackToChest( (TileEntityChest)targetTileEntity, ejectStack );
								}
								
								if ( !bFullStackDeposited )
								{
									iNumItemsStored = iEjectStackSize - ejectStack.stackSize;
								}
								else
								{
									iNumItemsStored = iEjectStackSize;
								}
								
								if ( iNumItemsStored > 0 )
								{
									decrStackSize( iStackIndex, iNumItemsStored );
									
							        worldObj.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, xCoord, yCoord, zCoord, 0 );							        
								}
							}
							else
							{
								m_bOutputBlocked = true;
							}
						}
						else
						{
							m_bOutputBlocked = true;
						}
					}
				}				
			}
			
			if ( bEjectIntoWorld )
			{
				// test for a storage cart below the hopper
				
		        List list = worldObj.getEntitiesWithinAABB( 
		        		EntityMinecart.class, 
		        		AxisAlignedBB.getAABBPool().getAABB( 
		    				(float)xCoord + 0.4f, (float)yCoord - 0.5f, (float)zCoord + 0.4f, 
		    				(float)xCoord + 0.6f, yCoord, (float)zCoord + 0.6f ) 
		    			);
		        
		        if ( list != null && list.size() > 0 )
		        {        	
		            for( int listIndex = 0; listIndex < list.size(); listIndex++ )
		            {
		                EntityMinecart minecartEntity = (EntityMinecart)list.get( listIndex );
		                
		                if ( minecartEntity.getMinecartType() == 1 )	// storage cart
		                {
		                	// check if the cart is properly aligned with the nozzle
		                	
		                	if ( minecartEntity.boundingBox.intersectsWith( AxisAlignedBB.getAABBPool().getAABB( 
				    				(float)xCoord, (float)yCoord - 0.5f, (float)zCoord, 
				    				(float)xCoord + 0.25f, yCoord, (float)zCoord + 1.0f ) ) &&
			    				minecartEntity.boundingBox.intersectsWith( AxisAlignedBB.getAABBPool().getAABB( 
				    				(float)xCoord + 0.75f, (float)yCoord - 0.5f, (float)zCoord, 
				    				(float)xCoord + 1.0f, yCoord, (float)zCoord + 1.0f ) )  &&
			    				minecartEntity.boundingBox.intersectsWith( AxisAlignedBB.getAABBPool().getAABB( 
				    				(float)xCoord, (float)yCoord - 0.5f, (float)zCoord, 
				    				(float)xCoord + 1.0f, yCoord, (float)zCoord + 0.25f ) ) &&
			    				minecartEntity.boundingBox.intersectsWith( AxisAlignedBB.getAABBPool().getAABB( 
				    				(float)xCoord, (float)yCoord - 0.5f, (float)zCoord + 0.75f, 
				    				(float)xCoord + 1.0f, yCoord, (float)zCoord + 1.0f ) ) )
		    				{
								int iNumItemsStored = 0;
								
			                	if ( FCUtilsInventory.AddItemStackToInventory( (IInventory)minecartEntity, ejectStack ) )
			                	{
									iNumItemsStored = iEjectStackSize;
			                	}
			                	else
			                	{
			                		iNumItemsStored = iEjectStackSize - ejectStack.stackSize;
			                	}
								
								if ( iNumItemsStored > 0 )
								{
									decrStackSize( iStackIndex, iNumItemsStored );
									
							        worldObj.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, xCoord, yCoord, zCoord, 0 );							        
								}
								
			                	bEjectIntoWorld = false;
			                	
			                	break;
		    				}
	                	}
            		}
		        }
			}
			
			if ( bEjectIntoWorld )
			{
				EjectStack( ejectStack );
				
				decrStackSize( iStackIndex, iEjectStackSize );
			}
		}		
    }
    
    private void EjectStack( ItemStack stack )
    {
        float xOffset = worldObj.rand.nextFloat() * 0.1F + 0.45F;
        float yOffset = -0.35F;
        float zOffset = worldObj.rand.nextFloat() * 0.1F + 0.45F;
    	
        EntityItem entityitem = 
        	new EntityItem( worldObj, 
    			(float)xCoord + xOffset, (float)yCoord + yOffset, (float)zCoord + zOffset, 
    			stack );

        entityitem.motionX = 0.0F;
        entityitem.motionY = -0.01F;
        entityitem.motionZ = 0.0F;
        
        entityitem.delayBeforeCanPickup = 10;
        
        worldObj.spawnEntityInWorld(entityitem);
    }
    
    public void AttemptToEjectXPFromInv()
    {
    	boolean bShouldResetEjectCount = true;
    	
    	if ( m_iContainedXPCount >= m_iXPEjectUnitSize )
    	{
			int iTargetI = xCoord;
			int iTargetJ = yCoord - 1;
			int iTargetK = zCoord;
			
			boolean bCanEjectIntoWorld = false;
			
			if ( worldObj.isAirBlock( iTargetI, iTargetJ, iTargetK ) )
			{
				bCanEjectIntoWorld = true;
			}
			else
			{
				int iTargetBlockID = worldObj.getBlockId( iTargetI, iTargetJ, iTargetK );
		
				if ( iTargetBlockID == FCBetterThanWolves.fcHopper.blockID )
				{
					bShouldResetEjectCount = AttemptToEjectXPIntoHopper( iTargetI, iTargetJ, iTargetK );
				}
				else if ( iTargetBlockID == FCBetterThanWolves.fcBlockArcaneVessel.blockID )
				{
					bShouldResetEjectCount = AttemptToEjectXPIntoArcaneVessel( iTargetI, iTargetJ, iTargetK );
				}					
				else if ( FCUtilsWorld.IsReplaceableBlock( worldObj, iTargetI, iTargetJ, iTargetK ) )
				{
					bCanEjectIntoWorld = true;
				}
				else
				{				
					Block targetBlock = Block.blocksList[iTargetBlockID];
					
					if ( !targetBlock.blockMaterial.isSolid() )
					{
						bCanEjectIntoWorld = true;
					}
				}
			}
			
			if ( bCanEjectIntoWorld )
			{
				if ( m_iHopperXPDropDelayCount <= 0 )
				{
					EjectXPOrb( m_iXPEjectUnitSize );
					
					m_iContainedXPCount -= m_iXPEjectUnitSize;
				}
				else
				{
					bShouldResetEjectCount = false;
				}				
			}
    	}
    	
    	if ( bShouldResetEjectCount )
    	{
    		ResetXPEjectCount();
    	}
    	else
    	{
			m_iHopperXPDropDelayCount--;			
    	}
    }
    	
    
	private boolean AttemptToEjectXPIntoHopper( int iTargetI, int iTargetJ, int iTargetK )
	{
		// returns whether the hopper eject counter should be reset 
		
		FCBlockHopper hopperBlock = (FCBlockHopper)FCBetterThanWolves.fcHopper;
        FCTileEntityHopper targetTileEntity = (FCTileEntityHopper)worldObj.getBlockTileEntity( iTargetI, iTargetJ, iTargetK );
        
        if ( targetTileEntity != null )
        {						
        	if ( m_iFilterItemID == Block.slowSand.blockID ) // soul sand filter required
        	{
        		int iTargetSpaceRemaining = m_iXPInventorySpace - targetTileEntity.m_iContainedXPCount;
        		
        		if ( iTargetSpaceRemaining > 0 )
        		{
    				if ( m_iHopperXPDropDelayCount <= 0 )
    				{
            			int iXPEjected = m_iXPEjectUnitSize;
            			
            			if ( iTargetSpaceRemaining < iXPEjected )
            			{
            				iXPEjected = iTargetSpaceRemaining;
            			}
            			
        				targetTileEntity.m_iContainedXPCount += iXPEjected;
        				
        				m_iContainedXPCount -= iXPEjected;
        				
        		        worldObj.playAuxSFX( FCBetterThanWolves.m_iXPEjectPopSoundAuxFXID, xCoord, yCoord, zCoord, 0 );       		        
    				}
    				else
    				{
    					return false;
    				}
        		}
        	}
        }
        
		return true;
	}
    
	private boolean AttemptToEjectXPIntoArcaneVessel( int iTargetI, int iTargetJ, int iTargetK )
	{
		// returns whether the hopper eject counter should be reset 
		
		FCBlockArcaneVessel vesselBlock = (FCBlockArcaneVessel)FCBetterThanWolves.fcBlockArcaneVessel;
        FCTileEntityArcaneVessel targetTileEntity = (FCTileEntityArcaneVessel)worldObj.getBlockTileEntity( iTargetI, iTargetJ, iTargetK );
        
        if ( targetTileEntity != null )
        {						
            if ( !vesselBlock.GetMechanicallyPoweredFlag( worldObj, iTargetI, iTargetJ, iTargetK ) )
        	{
        		int iTargetSpaceRemaining = targetTileEntity.m_iMaxContainedExperience - targetTileEntity.GetContainedTotalExperience();
        		
        		if ( iTargetSpaceRemaining > 0 )
        		{
    				if ( m_iHopperXPDropDelayCount <= 0 )
    				{
            			int iXPEjected = m_iXPEjectUnitSize;
            			
            			if ( iTargetSpaceRemaining < iXPEjected )
            			{
            				iXPEjected = iTargetSpaceRemaining;
            			}
            			
        				targetTileEntity.SetContainedRegularExperience( targetTileEntity.GetContainedRegularExperience() + iXPEjected );
        				
        				m_iContainedXPCount -= iXPEjected;
        				
        		        worldObj.playAuxSFX( FCBetterThanWolves.m_iXPEjectPopSoundAuxFXID, xCoord, yCoord, zCoord, 0 );       		        
    				}
    				else
    				{
    					return false;
    				}
        		}
        	}
        }
        
		return true;
	}
	
    private void ResetXPEjectCount()
    {
		m_iHopperXPDropDelayCount = m_iXPDelayBetweenDrops + worldObj.rand.nextInt( 3 );
    }
    
    private void EjectXPOrb( int iXPValue )
    {
        double xOffset = worldObj.rand.nextDouble() * 0.1D + 0.45D;
        double yOffset = -0.20D;
        double zOffset = worldObj.rand.nextDouble() * 0.1D + 0.45D;
    	
        EntityXPOrb xpOrb = new EntityXPOrb( worldObj, 
    			xCoord + xOffset, yCoord + yOffset, zCoord + zOffset, 
        		iXPValue );

        xpOrb.motionX = 0D;
        xpOrb.motionY = 0D;
        xpOrb.motionZ = 0D;
        
        worldObj.spawnEntityInWorld( xpOrb );
        
        worldObj.playAuxSFX( FCBetterThanWolves.m_iHopperXPEjectAuxFXID, xCoord, yCoord, zCoord, 0 );        
    }
    
    public void ResetContainedSoulCount()
    {
    	m_iContainedSoulCount = 0;
    }
    
    public void IncrementContainedSoulCount( int iNumSouls )
    {
    	m_iContainedSoulCount += iNumSouls;    	
    }
    
    private void HopperSoulOverload()
    {
        worldObj.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, xCoord, yCoord, zCoord, 0 );            
    	
    	((FCBlockHopper)(FCBetterThanWolves.fcHopper)).BreakHopper( worldObj, xCoord, yCoord, zCoord );
    }

    /*
     * returns true if the *entire* XP orb is swallowed, false otherwise
     */
    public boolean AttemptToSwallowXPOrb( World world, int i, int j, int k, EntityXPOrb entityXPOrb )
    {
    	int iRemainingSpace = m_iXPInventorySpace - m_iContainedXPCount;
    	
    	if ( iRemainingSpace > 0 )
    	{
    		if ( entityXPOrb.xpValue <= iRemainingSpace )
    		{
    			m_iContainedXPCount += entityXPOrb.xpValue;
    			
    			entityXPOrb.setDead();
    			
    			return true;    			
    		}
    		else
    		{
    			entityXPOrb.xpValue -= iRemainingSpace;
    			
    			m_iContainedXPCount = m_iXPInventorySpace;    			
    		}
    	}    
        
    	return false;
    }    
}