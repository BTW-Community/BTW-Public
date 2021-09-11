// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCUtilsInventory
{
	public static final int m_iIgnoreMetadata = 32767;
	
    static public void ClearInventoryContents( IInventory inventory )
    {
        for ( int iSlot = 0; iSlot < inventory.getSizeInventory(); iSlot++ )
        {
            ItemStack itemstack = inventory.getStackInSlot( iSlot );
            
            if ( itemstack != null )
            {
                inventory.setInventorySlotContents( iSlot, null );
            }
        }
    }
    
    static public void EjectInventoryContents( World world, int i, int j, int k, IInventory inventory )
    {
    	// ripped from BlockChest (with small mods).  This function puts everything in the
    	// inventory into the world
    	
        for(int l = 0; l < inventory.getSizeInventory(); l++)
        {
            ItemStack itemstack = inventory.getStackInSlot(l);
            
            if(itemstack == null)
            {
                continue;
            }
            float f = world.rand.nextFloat() * 0.7F + 0.15F;
            float f1 = world.rand.nextFloat() * 0.7F + 0.15F;
            float f2 = world.rand.nextFloat() * 0.7F + 0.15F;
            
            while(itemstack.stackSize > 0) 
            {
                int i1 = world.rand.nextInt(21) + 10;
                
                if(i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                
                float f3 = 0.05F;
                
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;

                CopyEnchantments( entityitem.getEntityItem(), itemstack ); 
                
                world.spawnEntityInWorld(entityitem);
            }
        }
    }
    
    static public void CopyEnchantments( ItemStack destStack, ItemStack sourceStack )
    {
        if ( sourceStack.hasTagCompound())
        {
            destStack.setTagCompound( (NBTTagCompound)sourceStack.getTagCompound().copy() );
        }
    }
    
    static public ItemStack DecrStackSize( IInventory inventory, int iSlot, int iAmount )
    {
        if ( inventory.getStackInSlot( iSlot ) != null)
        {
            if ( inventory.getStackInSlot( iSlot ).stackSize <= iAmount )
            {
                ItemStack itemstack = inventory.getStackInSlot( iSlot );
                inventory.setInventorySlotContents( iSlot, null );
                
                return itemstack;
            }
            else
            {            
	            ItemStack splitStack = inventory.getStackInSlot( iSlot ).splitStack( iAmount );
	            
	            if ( inventory.getStackInSlot( iSlot ).stackSize == 0 ) 
	            {
	                inventory.setInventorySlotContents( iSlot, null );
	            }
	            else
	            {
	            	inventory.onInventoryChanged();
	            }
	            
	            return splitStack;
            }
        } 
        else
        {
            return null;
        }
    }

    static public int GetNumOccupiedStacks( IInventory inventory )
    {
    	return GetNumOccupiedStacksInRange( inventory, 0, inventory.getSizeInventory() - 1 );
    }
    
    static public int GetNumOccupiedStacksInRange( IInventory inventory, int iMinSlot, int iMaxSlot )
    {
    	int iCount = 0;
    	
        for ( int iTempSlot = iMinSlot; iTempSlot <= iMaxSlot; iTempSlot++ )
        {
            if( inventory.getStackInSlot( iTempSlot ) != null )
            {
                iCount++;
            }
        }

        return iCount;
    }
    
    static public int GetRandomOccupiedStackInRange( IInventory inventory, Random rand, int iMinSlot, int iMaxSlot )
    {
    	int iNumStacks = GetNumOccupiedStacksInRange( inventory, iMinSlot, iMaxSlot );    	
    	
    	if ( iNumStacks > 0 )
    	{
    		int iRandomStackNum = rand.nextInt( iNumStacks ) + 1;
    		
    		int iStackCount = 0;
    		
            for(int iTempSlot = iMinSlot; iTempSlot <= iMaxSlot; iTempSlot++)
            {
                if( inventory.getStackInSlot( iTempSlot ) != null )
                {
                	iStackCount++;
                	
                	if ( iStackCount >= iRandomStackNum )
                	{
                        return iTempSlot;
                	}                	
                }
            }

    	}
    	
		return -1;
    }
    
    static public int GetFirstOccupiedStack( IInventory inventory )
    {
        for ( int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++ )
        {
            if ( inventory.getStackInSlot( iTempSlot ) != null )
            {
                return iTempSlot;
            }
        }

        return -1;
    }    
    
    static public int GetFirstOccupiedStackOfItem( IInventory inventory, int iItemID )
    {
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if( inventory.getStackInSlot( i ) != null )
            {
            	if ( inventory.getStackInSlot(i ).getItem().itemID == iItemID )
            	{
            		return i;
            	}
            }
        }

        return -1;
    }    
    
    static public int GetFirstOccupiedStackNotOfItem( IInventory inventory, int iNotItemID )
    {
        for ( int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++ )
        {
            if ( inventory.getStackInSlot( iTempSlot ) != null )
            {
            	if ( inventory.getStackInSlot(iTempSlot ).getItem().itemID != iNotItemID )
            	{
            		return iTempSlot;
            	}
            }
        }

        return -1;
    }
    
    static public int GetFirstEmptyStack( IInventory inventory )
    {
    	return GetFirstEmptyStackInSlotRange( inventory, 0, inventory.getSizeInventory() - 1 );
    }
    
    static public int GetFirstEmptyStackInSlotRange( IInventory inventory, int iMinSlotIndex, int iMaxSlotIndex )
    {
        for( int iTempSlot = iMinSlotIndex; iTempSlot <= iMaxSlotIndex; iTempSlot++ )
        {
            if( inventory.getStackInSlot( iTempSlot ) == null )
            {
                return iTempSlot;
            }
        }

        return -1;
    }
    
    static public int CountItemsInInventory
    ( 
		IInventory inventory, 
		int iItemID, 
		int iItemDamage // m_iIgnoreMetadata to disregard 
	)
    {
    	return CountItemsInInventory( inventory, iItemID, iItemDamage, false );
    }
    
    static public int CountItemsInInventory
    ( 
		IInventory inventory, 
		int iItemID, 
		int iItemDamage, // m_iIgnoreMetadata to disregard
		boolean bMetaDataExclusive 
	)
    {
    	int itemCount = 0;
    	
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
        	ItemStack tempStack = inventory.getStackInSlot( i );
        	
            if( tempStack != null )
            {
            	if ( tempStack.getItem().itemID == iItemID )
            	{
            		if ( iItemDamage == m_iIgnoreMetadata || 
        				( (!bMetaDataExclusive) && tempStack.getItemDamage() == iItemDamage ) || 
        				( bMetaDataExclusive && tempStack.getItemDamage() != iItemDamage ) )
            		{
            			itemCount += inventory.getStackInSlot( i ).stackSize;
            		}
            	}
            }
        }

        return itemCount;
    }
    
    static public boolean ConsumeItemsInInventory
    ( 
		IInventory inventory, 
		int iShiftedItemIndex,
		int iItemDamage, // m_iIgnoreMetadata to disregard
		int iItemCount 
	)
    {
    	return ConsumeItemsInInventory( inventory, iShiftedItemIndex, iItemDamage, iItemCount, false );
    }
    
    static public boolean ConsumeItemsInInventory
    ( 
		IInventory inventory, 
		int iShiftedItemIndex,
		int iItemDamage, // m_iIgnoreMetadata to disregard
		int iItemCount,
		boolean bMetaDataExclusive
	)
    {
        for ( int iSlot = 0; iSlot < inventory.getSizeInventory(); iSlot++ )
        {
            ItemStack tempItemStack = inventory.getStackInSlot( iSlot );
            
            if ( tempItemStack != null )
            {
            	Item tempItem =  tempItemStack.getItem();
            	
            	if ( tempItem.itemID == iShiftedItemIndex )
            	{
            		if ( iItemDamage == m_iIgnoreMetadata || 
        				( (!bMetaDataExclusive) && tempItemStack.getItemDamage() == iItemDamage ) || 
        				( bMetaDataExclusive && tempItemStack.getItemDamage() != iItemDamage ) )
            		{
	            		if ( tempItemStack.stackSize >= iItemCount )
	            		{
	            			DecrStackSize( inventory, iSlot, iItemCount );
	            			
	            			return true;            			
	            		}
	            		else
	            		{
	            			iItemCount -= tempItemStack.stackSize;
	            			
	                        inventory.setInventorySlotContents( iSlot, null );
	            		}
            		}
            	}
            }
        }
        
        return false;
    }
    
    static public boolean AddSingleItemToInventory( IInventory inventory, int iItemShiftedIndex, int itemDamage )
    {
    	ItemStack itemStack = new ItemStack( iItemShiftedIndex, 1, itemDamage );
    	
    	return AddItemStackToInventory( inventory, itemStack );    	
    }
    
    static public boolean AddItemStackToInventory( IInventory inventory, ItemStack stack )
    {
    	return AddItemStackToInventoryInSlotRange( inventory, stack, 0, inventory.getSizeInventory() - 1 );
    }

    /*
     * returns true if the full stack has been stored
     */
    static public boolean AddItemStackToInventoryInSlotRange( IInventory inventory, ItemStack itemstack,
		int iMinSlotIndex, int iMaxSlotIndex )
	{
	    if ( !itemstack.isItemDamaged() )
	    {
	        if ( AttemptToMergeWithExistingStacksInSlotRange( inventory, itemstack, iMinSlotIndex, iMaxSlotIndex ) )	        
	        {
	            return true;
	        }
	    }
	    
        return AttemptToPlaceInEmptySlotInSlotRange( inventory, itemstack, iMinSlotIndex, iMaxSlotIndex );
	}

    /*
     * This function differs from a normal add in that it checks for double chests and deposits
     * first to the one that normally occupied the top GUI position
     */
    static public boolean AddItemStackToChest( TileEntityChest chest, ItemStack itemstack )
    {
    	World world = chest.worldObj;
    	
    	// check for a chest neigboring on the first
    	
    	FCUtilsBlockPos secondChestPos;
    	
		for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
		{
			secondChestPos = new FCUtilsBlockPos( chest.xCoord, chest.yCoord, chest.zCoord );
			
			secondChestPos.AddFacingAsOffset( iTempFacing );
			
			int iSecondBlockID = world.getBlockId( secondChestPos.i, secondChestPos.j, secondChestPos.k );
			
			if ( iSecondBlockID == Block.chest.blockID || iSecondBlockID == FCBetterThanWolves.fcBlockChest.blockID )
			{
				TileEntityChest secondChest = (TileEntityChest)world.getBlockTileEntity( secondChestPos.i, secondChestPos.j, secondChestPos.k );
				
				if ( secondChest != null )
				{
					// determine which chest is the "top" one when displayed in the GUI, and use it as the primary chest
					
					if ( chest.xCoord < secondChest.xCoord || chest.zCoord < secondChest.zCoord )
			    	{
						return AddItemStackToDoubleInventory( (IInventory)chest, (IInventory)secondChest, itemstack );
			    	}
			    	else
			    	{
						return AddItemStackToDoubleInventory( (IInventory)secondChest, (IInventory)chest, itemstack );
			    	}    	
				}
			}
		}

		// a second chest wasn't found, so just add the stack to the first
		
		return AddItemStackToInventory( (IInventory)chest, itemstack );
    }
    
	//------------- Private Methods ------------//
	
    static private boolean CanStacksMerge( ItemStack sourceStack, ItemStack destStack, int iInventoryStackSizeLimit )
    {
        return ( destStack != null && 
        	destStack.itemID == sourceStack.itemID && 
        	destStack.isStackable() && 
        	destStack.stackSize < destStack.getMaxStackSize() && 
        	destStack.stackSize < iInventoryStackSizeLimit && 
    		( !destStack.getHasSubtypes() || destStack.getItemDamage() == sourceStack.getItemDamage() ) &&
			ItemStack.areItemStackTagsEqual( destStack, sourceStack ) );
    }
    
    static private int FindSlotToMergeItemStackInSlotRange( IInventory inventory, ItemStack itemStack, int iMinSlotIndex, int iMaxSlotIndex )
    {
    	int iInventoryStackLimit = inventory.getInventoryStackLimit();
    	
        for ( int i = iMinSlotIndex; i <= iMaxSlotIndex; i++ )
        {
            ItemStack tempStack = inventory.getStackInSlot( i );
            
            if ( CanStacksMerge( itemStack, tempStack, iInventoryStackLimit ) )
            {
                return i;
            }
        }

        return -1;
    }
    
    /*
     * returns true if the full stack has been stored
     */
    static private boolean AttemptToMergeWithExistingStacks( IInventory inventory, ItemStack stack )
    {
    	return AttemptToMergeWithExistingStacksInSlotRange( inventory, stack, 0,  inventory.getSizeInventory() - 1 );
    }
    
    /*
     * returns true if the full stack has been stored
     */
    static private boolean AttemptToMergeWithExistingStacksInSlotRange( IInventory inventory, ItemStack stack, int iMinSlotIndex, int iMaxSlotIndex )
    {
        int iMergeSlot = FindSlotToMergeItemStackInSlotRange( inventory, stack, iMinSlotIndex, iMaxSlotIndex );
        
    	while ( iMergeSlot >= 0 )
        {
	        int iNumItemsToStore = stack.stackSize;
	        
	        ItemStack tempStack = inventory.getStackInSlot( iMergeSlot );        
	        
	        if ( iNumItemsToStore > tempStack.getMaxStackSize() - tempStack.stackSize )
	        {
	            iNumItemsToStore = tempStack.getMaxStackSize() - tempStack.stackSize;
	        }
	        
	        if ( iNumItemsToStore > inventory.getInventoryStackLimit() - tempStack.stackSize )
	        {
	            iNumItemsToStore = inventory.getInventoryStackLimit() - tempStack.stackSize;
	        }
	        
	        if ( iNumItemsToStore == 0 )
	        {
	            return false;
	        } 
	        else
	        {
	            stack.stackSize -= iNumItemsToStore;
	            
	            tempStack.stackSize += iNumItemsToStore;
	            inventory.setInventorySlotContents( iMergeSlot, tempStack );
	            
	            if ( stack.stackSize <= 0 )
	            {
	            	return true;
	            }	        	
	        }
	        
	        iMergeSlot = FindSlotToMergeItemStackInSlotRange( inventory, stack, iMinSlotIndex, iMaxSlotIndex );	        
        }
        	
    	return false;
    }
    
    /*
     * returns true if the full stack has been stored
     */
    static private boolean AttemptToPlaceInEmptySlot( IInventory inventory, ItemStack stack )
    {
    	return AttemptToPlaceInEmptySlotInSlotRange( inventory, stack, 0, inventory.getSizeInventory() - 1 );
    }
    
    /*
     * returns true if the full stack has been stored
     */
    static private boolean AttemptToPlaceInEmptySlotInSlotRange( IInventory inventory, ItemStack stack, int iMinSlotIndex, int iMaxSlotIndex )
    {
        int iItemID = stack.itemID;
        int iItemDamage = stack.getItemDamage();
        
	    int iEmptySlot = GetFirstEmptyStackInSlotRange( inventory, iMinSlotIndex, iMaxSlotIndex );
	  
	    while ( iEmptySlot >= 0 )
	    {
	        int iNumItemsToStore = stack.stackSize;
	        
	        if ( iNumItemsToStore > inventory.getInventoryStackLimit() )
	        {
	            iNumItemsToStore = inventory.getInventoryStackLimit();	            
	        }
	        
        	ItemStack newStack = new ItemStack( iItemID, iNumItemsToStore, iItemDamage );
        	
        	CopyEnchantments( newStack, stack );
        	
        	inventory.setInventorySlotContents( iEmptySlot, newStack );
        	
        	stack.stackSize -= iNumItemsToStore;
        	
            if ( stack.stackSize <= 0 )
            {
            	return true;
            }
        	
        	iEmptySlot = GetFirstEmptyStackInSlotRange( inventory, iMinSlotIndex, iMaxSlotIndex );
	    }
	    
        return false;
    }
    
    /*
     * returns true if the full stack has been stored
     */
    static private boolean AddItemStackToDoubleInventory( IInventory primaryInventory, IInventory secondaryInventory, ItemStack stack )
    {
        if ( !stack.isItemDamaged() )
        {
            if ( AttemptToMergeWithExistingStacks( primaryInventory, stack ) )
            {
            	return true;
            }
            
            if ( AttemptToMergeWithExistingStacks( secondaryInventory, stack ) )
            {
            	return true;
            }            
        }

        if ( AttemptToPlaceInEmptySlot( primaryInventory, stack ) )
        {
        	return true;
        }
        
        return AttemptToPlaceInEmptySlot( secondaryInventory, stack );
    }
}
