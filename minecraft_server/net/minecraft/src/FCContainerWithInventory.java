// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCContainerWithInventory extends Container
{
    protected IInventory m_containerInventory;

	private int m_iNumSlotRows;
	private int m_iNumSlotColumns;

	private int m_iNumSlots;
	
    public FCContainerWithInventory( IInventory playerInventory, IInventory containerInventory, int iNumSlotRows, int iNumSlotColumns,
    	int iContainerInventoryX, int iContainerInventoryY, int iPlayerInventoryX, int iPlayerInventoryY )
    {
    	m_containerInventory = containerInventory;
    	
    	m_iNumSlotRows = iNumSlotRows;
    	m_iNumSlotColumns = iNumSlotColumns;
    	
    	m_iNumSlots = m_iNumSlotRows * m_iNumSlotColumns; 
    	
    	// vessel inventory slots
    	
        for ( int iRow = 0; iRow < m_iNumSlotRows; iRow++ )
        {
        	for( int iColumn = 0; iColumn < m_iNumSlotColumns; iColumn++ )
        	{
        		addSlotToContainer( new Slot( containerInventory, 
            		iColumn + iRow * m_iNumSlotColumns, // slot index 
            		iContainerInventoryX + iColumn * 18,  // bitmap x pos
            		iContainerInventoryY + iRow * 18 ) ); // bitmap y pos
            }

        }

        // player inventory slots
        
        for ( int iRow = 0; iRow < 3; iRow++ )
        {
            for ( int iColumn = 0; iColumn < 9; iColumn++ )
            {
            	addSlotToContainer( new Slot( playerInventory, 
            		iColumn + iRow * 9 + 9, // slot index (incremented by 9 due to 9 slots used below ) 
            		iPlayerInventoryX + iColumn * 18, // bitmap x pos
            		iPlayerInventoryY + iRow * 18 ) ); // bitmap y pos
            }

        }

        for ( int iColumn = 0; iColumn < 9; iColumn++ )
        {
        	addSlotToContainer( new Slot( playerInventory, iColumn, 
        		iPlayerInventoryX + iColumn * 18, iPlayerInventoryY + 58 ) );
        }
    }
    
	@Override
    public boolean canInteractWith( EntityPlayer entityplayer )
    {
        return m_containerInventory.isUseableByPlayer( entityplayer );
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
                slot.putStack( null );
            } 
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//	
}