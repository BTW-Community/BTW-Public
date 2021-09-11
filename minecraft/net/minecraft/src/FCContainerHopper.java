// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCContainerHopper extends Container
{
	private final int iNumHopperSlotRows = 2;
	private final int iNumHopperSlotColumns = 9;

	private final int iNumHopperSlots = ( iNumHopperSlotRows * iNumHopperSlotColumns );
	
    private FCTileEntityHopper localTileEntityHopper;

    private int m_iLastMechanicalPowerIndicator;
    
    public FCContainerHopper( IInventory playerinventory, FCTileEntityHopper tileentityHopper )
    {
    	localTileEntityHopper = tileentityHopper;
    	m_iLastMechanicalPowerIndicator = 0;
    	
    	// cauldron inventory slots
    	
        for ( int iRow = 0; iRow < iNumHopperSlotRows; iRow++ )
        {
        	for( int iColumn = 0; iColumn < iNumHopperSlotColumns; iColumn++ )
        	{
        		addSlotToContainer( new Slot( tileentityHopper, 
                		iColumn + iRow * iNumHopperSlotColumns, // slot index 
                		8 + iColumn * 18,  // bitmap x pos
                		60 + iRow * 18 ) ); // bitmap y pos
            }
        }

    	// draw filter slot
    	
        addSlotToContainer( new Slot( tileentityHopper, 
        		18, // slot index 
        		80,  // bitmap x pos
        		37 ) ); // bitmap y pos
        
        // player inventory slots
        
        for ( int iRow = 0; iRow < 3; iRow++ )
        {
            for ( int iColumn = 0; iColumn < 9; iColumn++ )
            {
            	addSlotToContainer( new Slot( playerinventory, 
                		iColumn + iRow * 9 + 9, // slot index (incremented by 9 due to 9 slots used below ) 
                		8 + iColumn * 18, // bitmap x pos
                		111 + iRow * 18 ) ); // bitmap y pos
            }

        }

        for ( int iColumn = 0; iColumn < 9; iColumn++ )
        {
        	addSlotToContainer( new Slot( playerinventory, iColumn, 8 + iColumn * 18, 169 ) );
        }

    }

	@Override
    public boolean canInteractWith( EntityPlayer entityplayer )
    {
        return localTileEntityHopper.isUseableByPlayer( entityplayer );
    }

	@Override
    public ItemStack transferStackInSlot( EntityPlayer player, int iSlotIndex )
    {
        // this function is performed when the gui is shift-clicked on
    	
        ItemStack clickedStack = null;
        Slot slot = (Slot)inventorySlots.get( iSlotIndex );
        
        if ( slot != null && slot.getHasStack() )
        {
            ItemStack processedStack = slot.getStack();
            clickedStack = processedStack.copy();
            
            if ( iSlotIndex < iNumHopperSlots + 1 )
            {
                if ( !mergeItemStack( processedStack, iNumHopperSlots + 1, inventorySlots.size(), true ) )
                {
                    return null;
                }
            } 
            else if ( !mergeItemStack(processedStack, 0, iNumHopperSlots, false )  )
            {
                return null;
            }
            
            if ( processedStack.stackSize == 0 )
            {
                slot.putStack( null );
            } 
            else
            {
                slot.onSlotChanged();
            }
        }
        return clickedStack;
    }
	
	@Override
    public void addCraftingToCrafters( ICrafting craftingInterface )
    //public void onCraftGuiOpened( ICrafting craftingInterface )
    {
        super.addCraftingToCrafters( craftingInterface );
        //super.onCraftGuiOpened( craftingInterface );
        
        craftingInterface.sendProgressBarUpdate( this, 0, localTileEntityHopper.m_iMechanicalPowerIndicator );
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

            if ( m_iLastMechanicalPowerIndicator != localTileEntityHopper.m_iMechanicalPowerIndicator )
            {
                icrafting.sendProgressBarUpdate( this, 0, localTileEntityHopper.m_iMechanicalPowerIndicator );
            }
        }
        while (true);

        m_iLastMechanicalPowerIndicator = localTileEntityHopper.m_iMechanicalPowerIndicator;
    }

	//----------- Client Side Functionality -----------//
	
	@Override
    public void updateProgressBar( int iVariableIndex, int iValue )
    {
        if ( iVariableIndex == 0 )
        {
        	localTileEntityHopper.m_iMechanicalPowerIndicator = iValue;
        }
    }
}