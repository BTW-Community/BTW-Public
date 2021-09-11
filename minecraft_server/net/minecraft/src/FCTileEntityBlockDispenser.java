// FCMOD

package net.minecraft.src;

public class FCTileEntityBlockDispenser extends TileEntity
    implements IInventory
{
    private ItemStack dispenserContents[];
    public int iNextSlotIndexToDispense;
    
    public FCTileEntityBlockDispenser()
    {
        dispenserContents = new ItemStack[16];
        iNextSlotIndexToDispense = 0;
    }

    @Override
    public int getSizeInventory()
    {
        return 16;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return dispenserContents[i];
    }

    @Override
    public ItemStack decrStackSize( int iSlot, int iAmount )
    {
    	return FCUtilsInventory.DecrStackSize( this, iSlot, iAmount );    	
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (dispenserContents[par1] != null)
        {
            ItemStack itemstack = dispenserContents[par1];
            dispenserContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public void setInventorySlotContents( int iSlot, ItemStack itemstack)
    {
    	super.onInventoryChanged();
    	
        dispenserContents[iSlot] = itemstack;
        
        if ( itemstack != null && itemstack.stackSize > getInventoryStackLimit() )
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "BlockDispenser";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        dispenserContents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < dispenserContents.length)
            {
                dispenserContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if( nbttagcompound.hasKey( "iNextSlotIndexToDispense" ) )
        {
        	iNextSlotIndexToDispense = nbttagcompound.getInteger( "iNextSlotIndexToDispense" );
        }        
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < dispenserContents.length; i++)
        {
            if(dispenserContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                dispenserContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
        
        nbttagcompound.setInteger( "iNextSlotIndexToDispense", iNextSlotIndexToDispense );        
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
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
    
    public ItemStack GetCurrentItemToDispense()
    {
    	if ( iNextSlotIndexToDispense >= dispenserContents.length ||
			dispenserContents[iNextSlotIndexToDispense] == null )
    	{
    		int iTempSlot = FindNextValidSlotIndex( iNextSlotIndexToDispense );
    		
    		if ( iTempSlot < 0 )
    		{
    			return null;
    		}
    		
    		iNextSlotIndexToDispense = iTempSlot; 
    	}    	
    	
    	ItemStack stack = getStackInSlot( iNextSlotIndexToDispense ).copy();
    	
    	stack.stackSize = 1;
    	
    	return stack;
    }
    
    public void OnDispenseCurrentSlot()
    {
    	decrStackSize( iNextSlotIndexToDispense, 1 );
    	
		int iTempSlot = FindNextValidSlotIndex( iNextSlotIndexToDispense );
		
		if ( iTempSlot < 0 )
		{
			iNextSlotIndexToDispense = 0;
		}
		else
		{
    		iNextSlotIndexToDispense = iTempSlot; 
		}
    }
    
    private int FindNextValidSlotIndex( int iCurrentSlot )
    {
    	for ( int iTempSlot = iCurrentSlot + 1; iTempSlot < dispenserContents.length; iTempSlot++ )
    	{
    		if ( dispenserContents[iTempSlot] != null )
    		{
    			return iTempSlot;
    		}
    	}
    	
    	for ( int iTempSlot = 0;  iTempSlot < iCurrentSlot; iTempSlot++ )
    	{
    		if ( dispenserContents[iTempSlot] != null )
    		{
    			return iTempSlot;
    		}
    	}
    	
    	if ( dispenserContents[iCurrentSlot] != null )
    	{
    		return iCurrentSlot;
    	}
    	
    	return -1;
    }    
}