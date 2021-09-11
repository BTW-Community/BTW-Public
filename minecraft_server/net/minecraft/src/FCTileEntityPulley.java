// FCMOD

package net.minecraft.src;

public class FCTileEntityPulley extends TileEntity
    implements IInventory
{
	private final int iPulleyInventorySize = 4;
	private final int iPulleyStackSizeLimit = 64;
	private final double dPulleyMaxPlayerInteractionDist = 64D;
	private final int iTicksToUpdateRopeState = 20;
	private boolean m_bHasAssociatedAnchorEntity;

    private ItemStack pulleyContents[];
    public int iUpdateRopeStateCounter;
    
    public int m_iMechanicalPowerIndicator; // used to communicate power status from server to client.  0 for off, 1 for on
    
    public FCTileEntityPulley()
    {
    	pulleyContents = new ItemStack[iPulleyInventorySize];
    	
    	m_bHasAssociatedAnchorEntity = false;
    	
    	iUpdateRopeStateCounter = iTicksToUpdateRopeState;
    	
    	m_iMechanicalPowerIndicator = 0;
    }
    
    @Override
    public String getInvName()
    {
        return "Pulley";
    }

    @Override
    public int getSizeInventory()
    {
        return iPulleyInventorySize;
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return iPulleyStackSizeLimit;
    }   

    @Override
    public ItemStack getStackInSlot( int iSlot )
    {
        return pulleyContents[iSlot];
    }
    
    @Override
    public void setInventorySlotContents( int iSlot, ItemStack itemstack )
    {
    	pulleyContents[iSlot] = itemstack;
    	
        if( itemstack != null && itemstack.stackSize > getInventoryStackLimit() )
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
        onInventoryChanged();
    }

    @Override
    public ItemStack decrStackSize( int iSlot, int iAmount )
    {
    	return FCUtilsInventory.DecrStackSize( this, iSlot, iAmount );    	
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if ( pulleyContents[par1] != null )
        {
            ItemStack itemstack = pulleyContents[par1];
            pulleyContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
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
    public void readFromNBT( NBTTagCompound nbttagcompound )
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        
        pulleyContents = new ItemStack[getSizeInventory()];
        
        for ( int i = 0; i < nbttaglist.tagCount(); i++ )
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt( i );
            
            int j = nbttagcompound1.getByte( "Slot" ) & 0xff;
            
            if ( j >= 0 && j < pulleyContents.length )
            {
            	pulleyContents[j] = ItemStack.loadItemStackFromNBT( nbttagcompound1 );
            }
        }
        
        if ( nbttagcompound.hasKey( "iUpdateRopeStateCounter" ) )
        {
        	iUpdateRopeStateCounter = nbttagcompound.getInteger( "iUpdateRopeStateCounter" );
        }
        
        if ( nbttagcompound.hasKey( "m_bHasAssociatedAnchorEntity" ) )
        {
        	m_bHasAssociatedAnchorEntity = nbttagcompound.getBoolean( "m_bHasAssociatedAnchorEntity" );
        }
    }

    @Override
    public void writeToNBT( NBTTagCompound nbttagcompound )
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        
        for ( int i = 0; i < pulleyContents.length; i++ )
        {
            if ( pulleyContents[i] != null )
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte( "Slot", (byte)i );
                
                pulleyContents[i].writeToNBT( nbttagcompound1 );
                
                nbttaglist.appendTag( nbttagcompound1 );
            }
        }     

        nbttagcompound.setTag( "Items", nbttaglist );
        
    	nbttagcompound.setInteger( "iUpdateRopeStateCounter", iUpdateRopeStateCounter );
    	
    	nbttagcompound.setBoolean( "m_bHasAssociatedAnchorEntity", m_bHasAssociatedAnchorEntity );        
    }
    
    @Override
    public boolean isUseableByPlayer( EntityPlayer entityplayer )    
    {
        if( worldObj.getBlockTileEntity( xCoord, yCoord, zCoord ) != this )
        {
            return false;
        }
        
        return ( entityplayer.getDistanceSq( (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D ) 
        	<= dPulleyMaxPlayerInteractionDist );
    }   
    
    @Override
    public void updateEntity()
    {
    	if ( worldObj.isRemote )
    	{
    		return;
    	}
    	
    	if ( IsMechanicallyPowered() )
    	{
			m_iMechanicalPowerIndicator = 1;
    	}
    	else
    	{
			m_iMechanicalPowerIndicator = 0;
    	}
    	
    	iUpdateRopeStateCounter--;
    	
    	if ( iUpdateRopeStateCounter <= 0 )
    	{
    		// if the pulley has an associated anchor, that's what controls the dispensing and retracting of rope
    		
    		if ( !m_bHasAssociatedAnchorEntity )
    		{
	    		boolean bIsRedstoneOn = ( (FCBlockPulley)FCBetterThanWolves.fcPulley ).
					IsRedstoneOn( worldObj, xCoord, yCoord, zCoord );
	
	    		// redstone prevents the pulley from doing anything
	    		
	    		if ( !bIsRedstoneOn )
	    		{
	        		boolean bIsOn = ( (FCBlockPulley)FCBetterThanWolves.fcPulley ).
						IsBlockOn( worldObj, xCoord, yCoord, zCoord );
	        		
	        		if ( bIsOn )
	        		{
	        			AttemptToRetractRope();
	        		}
	        		else
	        		{
	        			AttemptToDispenseRope();
	        		}
	    		}
    		}
    		
        	iUpdateRopeStateCounter = iTicksToUpdateRopeState;
    	}
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
    
    //************* Class Specific Methods ************//
    
    private boolean IsMechanicallyPowered()
    {
    	return ( (FCBlockPulley)FCBetterThanWolves.fcPulley ).
			IsBlockOn( worldObj, xCoord, yCoord, zCoord );    	
    }
    
    private boolean IsRedstonePowered()
    {
    	return ( (FCBlockPulley)FCBetterThanWolves.fcPulley ).
			IsRedstoneOn( worldObj, xCoord, yCoord, zCoord );    	
    }
    
    public boolean IsRaising()
    {
    	if ( !IsRedstonePowered() )
    	{
    		if ( IsMechanicallyPowered() )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean IsLowering()
    {
    	if ( !IsRedstonePowered() )
    	{
    		if ( !IsMechanicallyPowered() )
    		{
    			// we can only lower if we have rope in our inv
    			
				if ( FCUtilsInventory.GetFirstOccupiedStackOfItem( this, 
					FCBetterThanWolves.fcItemRope.itemID ) >= 0 )
				{
	    			return true;
				}
    		}
    	}
    	
    	return false;
    }
    
    public void NotifyPulleyEntityOfBlockStateChange()
    {
    	iUpdateRopeStateCounter = iTicksToUpdateRopeState;
    	
    	NotifyAttachedAnchorOfEntityStateChange();
    }

    private void NotifyAttachedAnchorOfEntityStateChange()
    {
    	// FCTODO: Notify attached anchor entities.
    	
    	// scan downward towards bottom of rope
    	
    	for ( int tempj = yCoord - 1; tempj >= 0; tempj-- )
    	{
    		int iTempBlockID = worldObj.getBlockId( xCoord, tempj, zCoord );
    		
    		if ( iTempBlockID == FCBetterThanWolves.fcAnchor.blockID )
    		{
    			if ( ( (FCBlockAnchor)( FCBetterThanWolves.fcAnchor ) ).
					GetFacing( worldObj, xCoord, tempj, zCoord ) == 1 )
    			{    					
	    			if ( ( (FCBlockAnchor)( FCBetterThanWolves.fcAnchor ) ).
	    				NotifyAnchorBlockOfAttachedPulleyStateChange( this, worldObj, xCoord, tempj, zCoord ) )
	    			{
	    				m_bHasAssociatedAnchorEntity = true;
	    			}
    			}
    			else
    			{
    				break;
    			}
    		}
    		else if ( iTempBlockID != FCBetterThanWolves.fcRopeBlock.blockID )
    		{
    			break;
    		}
    	}
    }
    
	boolean AttemptToRetractRope()
	{
    	// scan downward towards bottom of rope
    	
    	for ( int tempj = yCoord - 1; tempj >= 0; tempj-- )
    	{
    		int iTempBlockID = worldObj.getBlockId( xCoord, tempj, zCoord );
    		
    		if ( iTempBlockID == FCBetterThanWolves.fcRopeBlock.blockID )
    		{
        		if ( worldObj.getBlockId( xCoord, tempj - 1, zCoord ) != 
        			FCBetterThanWolves.fcRopeBlock.blockID )
        		{
        			// we've found the bottom of the rope
        			
                    AddRopeToInventory();
                    
                    // destroy the block
                    
                    Block targetBlock = FCBetterThanWolves.fcRopeBlock;
                    
    		        worldObj.playSoundEffect( 
    		        		(float)xCoord + 0.5F, (float)tempj + 0.5F, (float)zCoord + 0.5F, 
    		        		targetBlock.stepSound.getStepSound(), 
    		        		targetBlock.stepSound.getVolume() / 4.0F, 
    		        		targetBlock.stepSound.getPitch() * 0.8F);
    		        
                    worldObj.setBlockWithNotify( xCoord, tempj, zCoord, 0 );
                    
                    return true;
        		}
    		}
    		else
    		{
    			return false;
    		}
    	}
    	
    	return false;
	}
	
	public boolean AttemptToDispenseRope()
	{		
		int iRopeSlot = FCUtilsInventory.GetFirstOccupiedStackOfItem( this, 
				FCBetterThanWolves.fcItemRope.itemID );
		
		iUpdateRopeStateCounter = iTicksToUpdateRopeState;
		
		if ( iRopeSlot >= 0 )
		{
	    	// scan downward towards bottom of rope
	    	
	    	for ( int tempj = yCoord - 1; tempj >= 0; tempj-- )
	    	{
        		int iTempBlockID = worldObj.getBlockId( xCoord, tempj, zCoord );
        		
        		if ( FCUtilsWorld.IsReplaceableBlock( worldObj, xCoord, tempj, zCoord ) )
        		{
        			int iMetadata = FCBetterThanWolves.fcRopeBlock.onBlockPlaced( worldObj, xCoord, tempj, zCoord, 0, 0F, 0F, 0F, 0 );
        			
                    if( worldObj.setBlockAndMetadataWithNotify( xCoord, tempj, zCoord, FCBetterThanWolves.fcRopeBlock.blockID, iMetadata ) )
                    {	
	                    Block targetBlock = FCBetterThanWolves.fcRopeBlock;
	                    
	    		        worldObj.playSoundEffect( 
	    		        		(float)xCoord + 0.5F, (float)tempj + 0.5F, (float)zCoord + 0.5F, 
	    		        		targetBlock.stepSound.getStepSound(), 
	    		        		targetBlock.stepSound.getVolume() / 4.0F, 
	    		        		targetBlock.stepSound.getPitch() * 0.8F);
	    		        
	        			RemoveRopeFromInventory();

	        			// check for an upwards facing anchor below that we have just attached to
	        			
	        			int iBlockBelowTargetID = worldObj.getBlockId( xCoord, tempj - 1, zCoord );
	        			
	        			if ( iBlockBelowTargetID == FCBetterThanWolves.fcAnchor.blockID )
	        			{
	            			if ( ( (FCBlockAnchor)( FCBetterThanWolves.fcAnchor ) ).
	            					GetFacing( worldObj, xCoord, tempj - 1, zCoord ) == 1 )
	                			{    					
	            	    			( (FCBlockAnchor)( FCBetterThanWolves.fcAnchor ) ).
	            	    				NotifyAnchorBlockOfAttachedPulleyStateChange( this, worldObj, 
        	    						xCoord, tempj - 1, zCoord );
	                			}
	        			}
	                    
	                    return true;
                    }
                    
                    return false;
        		}
        		else if ( iTempBlockID != FCBetterThanWolves.fcRopeBlock.blockID )
        		{
        			return false;
        		}
	    	}
		}
		
		return false;
	}	
	
	public void AddRopeToInventory()
	{
		ItemStack ropeStack = new ItemStack( FCBetterThanWolves.fcItemRope );
		
		iUpdateRopeStateCounter = iTicksToUpdateRopeState;
		
		if ( FCUtilsInventory.AddItemStackToInventory( this, ropeStack ) )
		{
			worldObj.playSoundEffect( (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, 
				"random.pop", 0.05F, 
        		((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.0F) );
		}
		else
		{
			FCUtilsItem.EjectStackWithRandomOffset( worldObj, xCoord, yCoord, zCoord, ropeStack );
		}		
	}
	
	public int GetContainedRopeCount()
	{		
		return FCUtilsInventory.CountItemsInInventory( this, FCBetterThanWolves.fcItemRope.itemID, FCUtilsInventory.m_iIgnoreMetadata);
	}
	
	public void RemoveRopeFromInventory()
	{
		int iRopeSlot = FCUtilsInventory.GetFirstOccupiedStackOfItem( this, 
				FCBetterThanWolves.fcItemRope.itemID );
		
		if ( iRopeSlot >= 0 )
		{
			FCUtilsInventory.DecrStackSize( this, iRopeSlot, 1 );
		}
	}
	
	public void NotifyOfLossOfAnchorEntity()
	{
		m_bHasAssociatedAnchorEntity = false;
	}
}