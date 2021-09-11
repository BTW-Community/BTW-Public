// FCMOD

package net.minecraft.src;

import java.util.List;

public abstract class FCTileEntityCookingVessel extends TileEntity
    implements IInventory, FCITileEntityDataPacketHandler
{
	// constants
	
	protected final int m_iInventorySize = 27;
	private final int m_iStackSizeLimit = 64;
	
	private final int m_iPrimaryFireFactor = 5;
	private final int m_iSecondaryFireFactor = 3;
	
	protected final int m_iStackSizeToDropWhenTipped = 8;
	
	private final double m_dMaxPlayerInteractionDist = 64D;
	
	private final int m_iStokedTicksToCooldown = 20;
	
	// the first number in this equation represents the minimum number of ticks to cook something (with max fire)
    private final int m_iTimeToCook = 150 * ( m_iPrimaryFireFactor + ( m_iSecondaryFireFactor * 8 ) );
    
    // local vars

    protected ItemStack m_Contents[];
    protected int m_iCookCounter;
    
    protected int m_iStokedCooldownCounter;
    
    protected boolean m_bContainsValidIngrediantsForState;
    private boolean m_bForceValidateOnUpdate;
    
    protected int m_iFireUnderType; // 0 = none, 1 = normal, 2 = stoked, -1 = requires validation
    
    public int m_iScaledCookCounter; // used to communicate cook progress from server to client
    
    // variable to support legacy vessels that had their facing set in a different manner
    public int m_iForceFacing;
    
    // state variables used to communicate basic inventory info to the client
    
    public short m_sStorageSlotsOccupied;
    
    public FCTileEntityCookingVessel()
    {
    	m_Contents = new ItemStack[m_iInventorySize];
    	
    	m_iCookCounter = 0;
    	
    	m_iStokedCooldownCounter = 0;
    	m_bContainsValidIngrediantsForState = false;
    	m_bForceValidateOnUpdate = true;
    	m_iScaledCookCounter = 0;
    	m_iFireUnderType = 0;
    	m_iForceFacing = -1;
    	
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
            	m_Contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);;
            }
        }
        
        if ( nbttagcompound.hasKey( "m_iFireUnderType" ) )
        {
        	m_iFireUnderType = nbttagcompound.getInteger( "m_iFireUnderType" );
        }
        else
        {
        	// legacy support to force compute of this state is it's not in the NBT
        	
            m_iFireUnderType = -1;

        }        
        
        if ( nbttagcompound.hasKey( "m_iFacing" ) )
        {
        	// legacy: this state is no longer written as it is set in the metadata for the block
        	
        	m_iForceFacing = nbttagcompound.getInteger( "m_iFacing" );        	
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
        
    	nbttagcompound.setBoolean( "m_bContainsValidIngrediantsForState", m_bContainsValidIngrediantsForState );
    	
    	nbttagcompound.setInteger( "m_iFireUnderType", m_iFireUnderType );
    }
        
    @Override
    public void updateEntity()
    {
    	if ( worldObj.isRemote )
    	{
    		return;
    	}
    	
		int iBlockID = worldObj.getBlockId( xCoord, yCoord, zCoord );
		
		Block block = Block.blocksList[iBlockID];
		
		if ( block == null || !( block instanceof FCBlockCookingVessel ) )
		{
			// shouldn't happen
			
			return;
		}
		
		FCBlockCookingVessel cookingBlock = (FCBlockCookingVessel)block; 
		
    	if ( m_iForceFacing >= 0 )
    	{
    		// legacy support 
    		
    		cookingBlock.SetTiltFacing( worldObj, xCoord, yCoord, zCoord, m_iForceFacing );
    		
    		m_iForceFacing = -1;
    	}
    	
    	if ( m_iFireUnderType == -1 )
    	{
    		// forced update of fire state for legacy blocks that didn't have this tracking variable
    		
    		ValidateFireUnderType();
    	}
    	
		if ( !cookingBlock.GetMechanicallyPoweredFlag( worldObj, xCoord, yCoord, zCoord ) )
		{
			// only cook if upright (not mechanically powered) and if there's a fire under
			
	    	if ( m_iFireUnderType > 0 )
	    	{
	        	if ( m_bForceValidateOnUpdate )
	        	{
	        		ValidateContentsForState();
	        		
	        		m_bForceValidateOnUpdate = false;
	        		
	        		// FCTODO: Variable content display currently disabled
	        		// force a rerender of block since the quantity of stuff in its inventory	        		
	            	// changes its appearance,
	            	
	                // worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );
	        	}
	        	
	        	if ( m_iFireUnderType == 2 )
	        	{
	    			if ( m_iStokedCooldownCounter <= 0 )
	    			{
	        			m_iCookCounter = 0;		    			
	    			}
	    			
	    			m_iStokedCooldownCounter = m_iStokedTicksToCooldown;
	
					PerformStokedFireUpdate( GetCurrentFireFactor() );
	        	}
	        	else if ( m_iStokedCooldownCounter > 0 )
	        	{
	    			// this prevents the vessel from going back into regular cook mode if the fire beneath it is 
	    			// momentarily not stoked
	    			
	        		m_iStokedCooldownCounter--;
	    			
	    			if ( m_iStokedCooldownCounter <= 0 )
	    			{
	    				// reset the cook counter so that time spent rendering does not translate into cook time
	    				
	        			m_iCookCounter = 0;
	    			}			
	        	}
	        	else
	        	{
					PerformNormalFireUpdate( GetCurrentFireFactor() );
	        	}
	    	}
	    	else
	    	{
	    		m_iCookCounter = 0;
	    	}
    	}
    	else
    	{
			m_iCookCounter = 0;
			
    		int iTiltFacing = cookingBlock.GetTiltFacing( worldObj, xCoord, yCoord, zCoord );
    		
			AttemptToEjectStackFromInv( iTiltFacing );    	
		}
		
		m_iScaledCookCounter = ( m_iCookCounter * 1000 ) / m_iTimeToCook; 
	}
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        nbttagcompound.setShort( "s", m_sStorageSlotsOccupied );
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, nbttagcompound );
    }
    
    //************* FCITileEntityDataPacketHandler ************//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound nbttagcompound )
    {
        m_sStorageSlotsOccupied = nbttagcompound.getShort( "s" );     
        
        // force a visual update for the block since the above variable affects it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }
    
    //************* IInventory ************//
    
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
        if ( m_Contents[par1] != null )
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
    public int getInventoryStackLimit()
    {
        return m_iStackSizeLimit;
    }

    @Override
    public void onInventoryChanged()
    {
    	super.onInventoryChanged();
    	
		m_bForceValidateOnUpdate = true;
		
    	if ( worldObj != null )
    	{
	        if ( ValidateInventoryStateVariables() )
	        {
		        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
	        }
    	}
    }

    @Override
    public boolean isUseableByPlayer( EntityPlayer entityPlayer )
    {
        if( worldObj.getBlockTileEntity( xCoord, yCoord, zCoord ) != this )
        {
            return false;
        }
        
        int iBlockID = worldObj.getBlockId( xCoord, yCoord, zCoord );
        Block block = Block.blocksList[iBlockID];
        
        if ( !( block instanceof FCBlockCookingVessel ) )
        {
        	return false;
        }
        
        if ( ((FCBlockCookingVessel)block).IsOpenSideBlocked( worldObj, xCoord, yCoord, zCoord ) )
        {
        	return false;
        }
        
        return ( entityPlayer.getDistanceSq( 
    		(double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D ) 
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

    //************* Class Specific Methods ************//
    
    public abstract void ValidateContentsForState();

    protected abstract FCCraftingManagerBulk GetCraftingManager( int iFireType );
    
    /*
     * FireUnderType is 0 for none, 1 for normal, 2 for stoked
     */
    public int GetCurrentFireFactor()
    {
    	int iFireFactor = 0;
    	
    	if ( m_iFireUnderType > 0 )
    	{
			iFireFactor = m_iPrimaryFireFactor;
    		
    		if ( m_iFireUnderType == 1 )
    		{
        		int tempY = yCoord - 1;
        		
        		for ( int tempX = xCoord - 1; tempX <= xCoord + 1; tempX++ )
        		{
        			for ( int tempZ = zCoord - 1; tempZ <= zCoord + 1; tempZ++ )
        			{
        				if ( tempX != xCoord || tempZ != zCoord )
        				{
        					int iTempBlockID = worldObj.getBlockId( tempX, tempY, tempZ );
        					
        		        	if ( iTempBlockID == Block.fire.blockID ||
        		        		iTempBlockID == FCBetterThanWolves.fcBlockCampfireMedium.blockID || 
        		        		iTempBlockID == FCBetterThanWolves.fcBlockCampfireLarge.blockID )
        		        	{
        		        		iFireFactor += m_iSecondaryFireFactor;
        		        	}
        				}
        			}
        		}  		
    		}
    		else
    		{
        		int tempY = yCoord - 1;
        		
        		for ( int tempX = xCoord - 1; tempX <= xCoord + 1; tempX++ )
        		{
        			for ( int tempZ = zCoord - 1; tempZ <= zCoord + 1; tempZ++ )
        			{
        				if ( tempX != xCoord || tempZ != zCoord )
        				{
        		        	if ( worldObj.getBlockId( tempX, tempY, tempZ ) == FCBetterThanWolves.fcBlockFireStoked.blockID )
        		        	{
        		        		iFireFactor += m_iSecondaryFireFactor;
        		        	}
        				}
        			}
        		}  		
    		}    		
    	}
    	
    	return iFireFactor;
    }
    
	public void ValidateFireUnderType()
	{
		int iNewType = 0;
		
		int iBlockUnderID = worldObj.getBlockId( xCoord, yCoord - 1, zCoord );
		
		if ( iBlockUnderID == Block.fire.blockID ||
			iBlockUnderID == FCBetterThanWolves.fcBlockCampfireMedium.blockID || 
			iBlockUnderID == FCBetterThanWolves.fcBlockCampfireLarge.blockID )
		{
			iNewType = 1;
		}
		else if ( iBlockUnderID == FCBetterThanWolves.fcBlockFireStoked.blockID )
		{
			iNewType = 2;
		}
		
		if ( iNewType != m_iFireUnderType )
		{
			m_iFireUnderType = iNewType;
			
	    	ValidateContentsForState();			
		}
	}
	
    private void PerformNormalFireUpdate( int iFireFactor )
    {
		if ( m_bContainsValidIngrediantsForState )
		{
    		m_iCookCounter += iFireFactor;
    		
    		if ( m_iCookCounter >= m_iTimeToCook )
    		{
    			AttemptToCookNormal();
    			
    	        // reset the cook counter to start the next item
    	        
    	        m_iCookCounter = 0;
    		}
		}
		else
		{
	        m_iCookCounter = 0;
		}
    }
    
    private void PerformStokedFireUpdate( int iFireFactor )
    {
		if ( m_bContainsValidIngrediantsForState )
		{
			m_iCookCounter += iFireFactor;
			
			if ( m_iCookCounter >= m_iTimeToCook )
			{
	    		if ( DoesContainExplosives() )
				{
	    			BlowUp();
				}
				else 
				{
					AttemptToCookStoked();
	    		}
				
				m_iCookCounter = 0;
			}
		}
		else
		{
			m_iCookCounter = 0;
		}
    }

    protected boolean AttemptToCookNormal()
    {
    	return AttemptToCookWithManager( GetCraftingManager( 1 ) );
    }
    
    protected boolean AttemptToCookStoked()
    {
    	return AttemptToCookWithManager( GetCraftingManager( 2 ) );   	
    }
    
    private boolean AttemptToCookWithManager( FCCraftingManagerBulk manager )
    {
    	if ( manager != null )
    	{
        	if ( manager.GetCraftingResult( this ) != null )
        	{        		
        		List<ItemStack> outputList = manager.ConsumeIngrediantsAndReturnResult( this );
        		
    			assert( outputList != null && outputList.size() > 0 );
    			
                for ( int listIndex = 0; listIndex < outputList.size(); listIndex++ )
                {
    	    		ItemStack cookedStack = ((ItemStack)outputList.get( listIndex )).copy();
    	    		
    	    		if ( cookedStack != null )
    	    		{
    	    	        if ( !FCUtilsInventory.AddItemStackToInventory( this, cookedStack ) )
    	    	        {    	        	
    	    	        	FCUtilsItem.EjectStackWithRandomOffset( worldObj, xCoord, yCoord + 1, zCoord, cookedStack );			    	        	
    	    	        }
    	    		}
                }
                
                return true;
    		}        	
    	}
    	
    	return false;
    }
    
    public int getCookProgressScaled( int iScale )
    {
        return ( m_iScaledCookCounter * iScale ) / 1000;
    }    
    
    public boolean IsCooking()
    {
    	return ( m_iScaledCookCounter > 0 );
    }
    
    protected boolean DoesContainExplosives()
    {
		if ( FCUtilsInventory.GetFirstOccupiedStackOfItem( this, FCBetterThanWolves.fcItemHellfireDust.itemID ) >= 0 ||
			FCUtilsInventory.GetFirstOccupiedStackOfItem( this, Block.tnt.blockID ) >= 0 ||
			FCUtilsInventory.GetFirstOccupiedStackOfItem( this, Item.gunpowder.itemID ) >= 0 ||
			FCUtilsInventory.GetFirstOccupiedStackOfItem( this, FCBetterThanWolves.fcItemBlastingOil.itemID ) >= 0
		)
		{
			return true;
		}
		
		return false;
    }
    
    private void BlowUp()
    {
    	int iHellfireCount = FCUtilsInventory.CountItemsInInventory( this, FCBetterThanWolves.fcItemHellfireDust.itemID, -1 );
    	
    	float fExplosionSize = ( iHellfireCount * 10.0F ) / 64.0F;
    	
    	fExplosionSize += ( FCUtilsInventory.CountItemsInInventory( this, Item.gunpowder.itemID, -1 ) * 10.0F ) / 64.0F;
    	
    	fExplosionSize += ( FCUtilsInventory.CountItemsInInventory( this, FCBetterThanWolves.fcItemBlastingOil.itemID, -1 ) * 10.0F ) / 64.0F;
    	
    	int iTNTCount = FCUtilsInventory.CountItemsInInventory( this, Block.tnt.blockID, -1 );
    	
    	if ( iTNTCount > 0 )
    	{
    		if ( fExplosionSize < 4.0F )
    		{
    			fExplosionSize = 4.0F;
    		}
    		
        	fExplosionSize += FCUtilsInventory.CountItemsInInventory( this, Block.tnt.blockID, -1 );
    	}
    	
    	if ( fExplosionSize < 2.0F )
    	{
    		fExplosionSize = 2.0F;
    	}
    	else if ( fExplosionSize > 10.0F )
    	{
    		fExplosionSize = 10.0F;
    	}
    	
    	FCUtilsInventory.ClearInventoryContents( this );
    	
    	worldObj.setBlockWithNotify( xCoord, yCoord, zCoord, 0 );
    	
        worldObj.createExplosion( null, xCoord, yCoord, zCoord, fExplosionSize, true );
    }
    
    private void AttemptToEjectStackFromInv( int iTiltFacing )
    {
 		int iStackIndex = FCUtilsInventory.GetFirstOccupiedStackNotOfItem( this, Item.brick.itemID );
		
		if ( iStackIndex >= 0 && iStackIndex < getSizeInventory() )
		{
			ItemStack invStack = getStackInSlot( iStackIndex );
			
			int iEjectStackSize;
			
			if ( m_iStackSizeToDropWhenTipped > invStack.stackSize )
			{
				iEjectStackSize = invStack.stackSize;
			}
			else
			{
				iEjectStackSize = m_iStackSizeToDropWhenTipped;
			}
			
			ItemStack ejectStack = new ItemStack( invStack.itemID, iEjectStackSize, invStack.getItemDamage() );
			
			FCUtilsInventory.CopyEnchantments( ejectStack, invStack );

			FCUtilsBlockPos targetPos = new FCUtilsBlockPos( xCoord, yCoord, zCoord );
			
			targetPos.AddFacingAsOffset( iTiltFacing );
			
			boolean bEjectIntoWorld = false;
			
			if ( worldObj.isAirBlock( targetPos.i, targetPos.j, targetPos.k ) )
			{
				bEjectIntoWorld = true;
			}
			else
			{
				if ( FCUtilsWorld.IsReplaceableBlock( worldObj, targetPos.i, targetPos.j, targetPos.k ) )
				{
					bEjectIntoWorld = true;
				}
				else
				{				
					int iTargetBlockID = worldObj.getBlockId( targetPos.i, targetPos.j, targetPos.k );
					
					Block targetBlock = Block.blocksList[iTargetBlockID];
					
					if ( !targetBlock.blockMaterial.isSolid() )
					{
						bEjectIntoWorld = true;
					}
				}				
			}
			
			if ( bEjectIntoWorld )
			{
				EjectStack( ejectStack, iTiltFacing );
				
				decrStackSize( iStackIndex, iEjectStackSize );
			}
		}		
    }
    
    private void EjectStack( ItemStack stack, int iFacing )
    {
		Vec3 itemPos = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );

		itemPos.xCoord *= 0.5F;
		itemPos.yCoord *= 0.5F;
		itemPos.zCoord *= 0.5F;
		
		itemPos.xCoord += ((float)xCoord ) + 0.5F;
		itemPos.yCoord += ((float)yCoord ) + 0.25F;
		itemPos.zCoord += ((float)zCoord ) + 0.5F;		
    	
        EntityItem entityItem = new EntityItem( worldObj, itemPos.xCoord, itemPos.yCoord, itemPos.zCoord, stack );

		Vec3 itemVel = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );
		
		itemVel.xCoord *= 0.1F;
		itemVel.yCoord *= 0.1F;
		itemVel.zCoord *= 0.1F;
		
        entityItem.motionX = itemVel.xCoord;
        entityItem.motionY = itemVel.yCoord;
        entityItem.motionZ = itemVel.zCoord;
        
        entityItem.delayBeforeCanPickup = 10;
        
        worldObj.spawnEntityInWorld( entityItem );
    }
    
    private boolean ValidateInventoryStateVariables()
    {
    	boolean bStateChanged = false;
    	
    	short currentSlotsOccupied = (short)( FCUtilsInventory.GetNumOccupiedStacks( this ) ); 
    	
    	if ( currentSlotsOccupied != m_sStorageSlotsOccupied )
    	{
    		m_sStorageSlotsOccupied = currentSlotsOccupied;
    		
    		bStateChanged = true;
    	}
    	
    	return bStateChanged;
    }    
}