// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCContainerPulley extends Container
{
	private final int m_iNumSlotRows = 2;
	private final int m_iNumSlotColumns = 2;

	private final int m_iNumSlots = ( m_iNumSlotRows * m_iNumSlotColumns );
	
    private FCTileEntityPulley m_localTileEntity;

    private int m_iLastMechanicalPowerIndicator;
    
    public FCContainerPulley( IInventory playerinventory, FCTileEntityPulley tileEntityPulley )
    {
    	m_localTileEntity = tileEntityPulley;
    	
    	m_iLastMechanicalPowerIndicator = 0;
    	
    	// pulley inventory slots
    	
        for ( int iRow = 0; iRow < m_iNumSlotRows; iRow++ )
        {
        	for( int iColumn = 0; iColumn < m_iNumSlotColumns; iColumn++ )
        	{
        		addSlotToContainer( new Slot( tileEntityPulley, 
                		iColumn + iRow * m_iNumSlotColumns, // slot index 
                		71 + ( iColumn ) * 18,  // bitmap x pos
                		43 + iRow * 18 ) ); // bitmap y pos
            }
        }

        // player inventory slots
        
        for ( int iRow = 0; iRow < 3; iRow++ )
        {
            for ( int iColumn = 0; iColumn < 9; iColumn++ )
            {
            	addSlotToContainer( new Slot( playerinventory, 
                		iColumn + iRow * 9 + 9, // slot index (incremented by 9 due to 9 slots used below ) 
                		8 + iColumn * 18, // bitmap x pos
                		93 + iRow * 18 ) ); // bitmap y pos
            }
        }

        // player hot-bar
        
        for ( int iColumn = 0; iColumn < 9; iColumn++ )
        {
        	addSlotToContainer( new Slot( playerinventory, iColumn, 8 + iColumn * 18, 151 ) );
        }
    }

	@Override
    public boolean canInteractWith( EntityPlayer entityplayer )
    {
        return m_localTileEntity.isUseableByPlayer( entityplayer );
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
	
	@Override
    public void addCraftingToCrafters( ICrafting craftingInterface )
    //public void onCraftGuiOpened( ICrafting craftingInterface )
    {
        super.addCraftingToCrafters( craftingInterface );
        //super.onCraftGuiOpened( craftingInterface );
        
        craftingInterface.sendProgressBarUpdate( this, 0, m_localTileEntity.m_iMechanicalPowerIndicator );
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

            if ( m_iLastMechanicalPowerIndicator != m_localTileEntity.m_iMechanicalPowerIndicator )
            {
                icrafting.sendProgressBarUpdate( this, 0, m_localTileEntity.m_iMechanicalPowerIndicator );
            }
        }
        while (true);

        m_iLastMechanicalPowerIndicator = m_localTileEntity.m_iMechanicalPowerIndicator;
    }

	//----------- Client Side Functionality -----------//
	
	@Override
    public void updateProgressBar( int iVariableIndex, int iValue )
    {
        if ( iVariableIndex == 0 )
        {
        	m_localTileEntity.m_iMechanicalPowerIndicator = iValue;
        }
    }
}