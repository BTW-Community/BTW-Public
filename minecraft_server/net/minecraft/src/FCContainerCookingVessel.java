// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCContainerCookingVessel extends FCContainerWithInventory
{
    private FCTileEntityCookingVessel m_AssociatedTileEntity;

    private int m_iLastCookCounter;

    public FCContainerCookingVessel( IInventory playerinventory, FCTileEntityCookingVessel tileEntity )
    {
    	super( playerinventory, tileEntity, 3, 9, 8, 43, 8, 111 );
    	
    	m_AssociatedTileEntity = tileEntity;

    	m_iLastCookCounter = 0;
    }

	@Override
    public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer)
    {
    	// this is necessary as not all slot clicks properly generate onInventoryChanged events

    	ItemStack returnValue = super.slotClick( i, j, k, entityplayer );
    	
    	m_AssociatedTileEntity.onInventoryChanged();
    	
    	return returnValue; 
    }
    
	@Override
    //public void addCraftingToCrafters( ICrafting craftingInterface ) // client
    public void onCraftGuiOpened( ICrafting craftingInterface ) // server
    {
        //super.addCraftingToCrafters( craftingInterface ); // client
        super.onCraftGuiOpened( craftingInterface ); // server
        
        craftingInterface.sendProgressBarUpdate( this, 0, m_AssociatedTileEntity.m_iScaledCookCounter );
    }
	
	@Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        Iterator iterator = crafters.iterator();

        while ( iterator.hasNext() )
        {
            ICrafting icrafting = (ICrafting)iterator.next();

            if ( m_iLastCookCounter != m_AssociatedTileEntity.m_iScaledCookCounter )
            {
                icrafting.sendProgressBarUpdate( this, 0, m_AssociatedTileEntity.m_iScaledCookCounter );
            }
        }

        m_iLastCookCounter = m_AssociatedTileEntity.m_iScaledCookCounter;
    }

	//----------- Client Side Functionality -----------//
}