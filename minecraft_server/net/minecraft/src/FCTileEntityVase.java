//FCMOD

package net.minecraft.src;

public class FCTileEntityVase extends TileEntity
    implements IInventory
{	
	private final int m_iVaseInventorySize = 1;
	private final int m_iVaseStackSizeLimit = 1;
	private final double m_dVaseMaxPlayerInteractionDist = 64D;
	
    private ItemStack m_VaseContents[];
    
    public FCTileEntityVase()
    {
    	m_VaseContents = new ItemStack[m_iVaseInventorySize];
    }
    
    @Override
    public int getSizeInventory()
    {
        return m_iVaseInventorySize;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return m_iVaseStackSizeLimit;
    }
    
    @Override
    public ItemStack getStackInSlot( int iSlot )
    {
        return m_VaseContents[iSlot];
    }

    @Override
    public ItemStack decrStackSize( int iSlot, int iAmount )
    {
    	return FCUtilsInventory.DecrStackSize( this, iSlot, iAmount );    	
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (m_VaseContents[par1] != null)
        {
            ItemStack itemstack = m_VaseContents[par1];
            m_VaseContents[par1] = null;
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
    	m_VaseContents[iSlot] = itemstack;
    	
        if( itemstack != null && itemstack.stackSize > getInventoryStackLimit() )
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Vase";
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if( worldObj.getBlockTileEntity( xCoord, yCoord, zCoord ) != this )
        {
            return false;
        }
        
        return ( entityplayer.getDistanceSq( (
    		double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D ) 
        	<= m_dVaseMaxPlayerInteractionDist );
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
        
        m_VaseContents = new ItemStack[getSizeInventory()];
        
        for ( int i = 0; i < nbttaglist.tagCount(); i++ )
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt( i );
            
            int j = nbttagcompound1.getByte( "Slot" ) & 0xff;
            
            if ( j >= 0 && j < m_VaseContents.length )
            {
            	m_VaseContents[j] = ItemStack.loadItemStackFromNBT( nbttagcompound1 );
            }
        }
    }
    
    @Override
    public void writeToNBT( NBTTagCompound nbttagcompound )
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        
        for ( int i = 0; i < m_VaseContents.length; i++ )
        {
            if ( m_VaseContents[i] != null )
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte( "Slot", (byte)i );
                
                m_VaseContents[i].writeToNBT( nbttagcompound1 );
                
                nbttaglist.appendTag( nbttagcompound1 );
            }
        }
        
        nbttagcompound.setTag( "Items", nbttaglist );
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
}
