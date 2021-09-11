// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCContainerBlockDispenser extends Container
{
    private FCTileEntityBlockDispenser m_LocalTileEntity;

	private final int m_iNumSlots = 16;
	
    private int m_iLastNextSlotIndexToDispense;
	
    public FCContainerBlockDispenser( IInventory iinventory, FCTileEntityBlockDispenser tileEntityBlockDispenser )
    {
    	m_LocalTileEntity = tileEntityBlockDispenser;
    	
    	m_LocalTileEntity.openChest();
        
        for(int i = 0; i < 4; i++)
        {
            for(int l = 0; l < 4; l++)
            {
            	addSlotToContainer(new Slot(tileEntityBlockDispenser, l + i * 4, 53 + l * 18, 17 + i * 18));
            }
        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
            	addSlotToContainer(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 102 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
        	addSlotToContainer(new Slot(iinventory, k, 8 + k * 18, 160));
        }

    }

	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return m_LocalTileEntity.isUseableByPlayer(entityplayer);
    }
    
	@Override
    public ItemStack transferStackInSlot( EntityPlayer player, int iSlotIndex )
    {
        // this function is performed when the gui is shift-clicked on
    	
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get( iSlotIndex );
        
        if ( slot != null && slot.getHasStack() )
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if ( iSlotIndex < m_iNumSlots )
            {
                if ( !mergeItemStack( itemstack1, m_iNumSlots, inventorySlots.size(), true ) )
                {
                    return null;
                }
            } 
            else if ( !mergeItemStack(itemstack1, 0, m_iNumSlots, false )  )
            {
                return null;
            }
            
            if ( itemstack1.stackSize == 0 )
            {
                slot.putStack(null);
            } 
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
	@Override
    public ItemStack slotClick( int i, int j, int k, EntityPlayer entityplayer )
    {
    	// manual changes to the dispensers inventory cause the order to reset
    	
    	if ( i < m_iNumSlots )
		{
    		m_LocalTileEntity.iNextSlotIndexToDispense = 0;
		}
    	
    	return super.slotClick( i, j, k, entityplayer);    	
    }
    
	@Override
    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        super.onCraftGuiClosed(entityplayer);
        
        m_LocalTileEntity.closeChest();
    }
	
	@Override
    public void addCraftingToCrafters( ICrafting craftingInterface )
    //public void onCraftGuiOpened( ICrafting craftingInterface )
    {
        super.addCraftingToCrafters( craftingInterface );
        //super.onCraftGuiOpened( craftingInterface );
        
        craftingInterface.sendProgressBarUpdate( this, 0, m_LocalTileEntity.iNextSlotIndexToDispense );
    }
	
	@Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        Iterator iterator = crafters.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            ICrafting icrafting = (ICrafting)iterator.next();

            if ( m_iLastNextSlotIndexToDispense != m_LocalTileEntity.iNextSlotIndexToDispense )
            {
                icrafting.sendProgressBarUpdate( this, 0, m_LocalTileEntity.iNextSlotIndexToDispense );
            }
        }
        while (true);

        m_iLastNextSlotIndexToDispense = m_LocalTileEntity.iNextSlotIndexToDispense;
    }

	//----------- Client Side Functionality -----------//
	
	@Override
    public void updateProgressBar( int iVariableIndex, int iValue )
    {
        if ( iVariableIndex == 0 )
        {
        	m_LocalTileEntity.iNextSlotIndexToDispense = iValue;
        }
    }
}