// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCContainerHamper extends FCContainerWithInventory
{
	private static final int m_iInvetoryRows = 2;
	private static final int m_iInvetoryColumns = 2;
	
	private static final int m_iContainerInventoryDisplayX = 71;
	private static final int m_iContainerInventoryDisplayY = 17;
	
	private static final int m_iPlayerInventoryDisplayX = 8;
	private static final int m_iPlayerInventoryDisplayY = 67;
	
    public FCContainerHamper( IInventory playerInventory, IInventory hamperInventory )
    {
    	super( playerInventory, hamperInventory, m_iInvetoryRows, m_iInvetoryColumns, 
    		m_iContainerInventoryDisplayX, m_iContainerInventoryDisplayY, 
    		m_iPlayerInventoryDisplayX, m_iPlayerInventoryDisplayY );
    	
		hamperInventory.openChest();
    }
    
    @Override
    public void onCraftGuiClosed( EntityPlayer player )
    {
        super.onCraftGuiClosed( player );
        
		m_containerInventory.closeChest();
    }
}