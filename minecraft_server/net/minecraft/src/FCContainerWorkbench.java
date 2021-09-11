// FCMOD

package net.minecraft.src;

public class FCContainerWorkbench extends ContainerWorkbench
{
	public World m_world;
	public int m_iBlockI, m_iBlockJ, m_iBlockK;
	
    public FCContainerWorkbench( InventoryPlayer inventory, World world, int i, int j, int k )    
    {
    	super( inventory, world, i, j, k );
    	
    	m_world = world;
    	
    	m_iBlockI = i;
    	m_iBlockJ = j;
    	m_iBlockK = k;
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
    	int iBlockID = m_world.getBlockId( m_iBlockI, m_iBlockJ, m_iBlockK );
    	
    	return ( iBlockID == FCBetterThanWolves.fcBlockWorkStump.blockID || 
    		iBlockID == FCBetterThanWolves.fcBlockWorkbench.blockID ||
    		iBlockID == Block.anvil.blockID ||
    		iBlockID == Block.workbench.blockID ) &&
    		par1EntityPlayer.getDistanceSq( m_iBlockI + 0.5D, m_iBlockJ + 0.5D, m_iBlockK + 0.5D ) <= 64D;
    }

    @Override
    public ItemStack transferStackInSlot( EntityPlayer player, int iSlotClicked )
    {
        ItemStack oldStackInSlotClicked = null;
        Slot slotClicked = (Slot)inventorySlots.get( iSlotClicked );

        if ( slotClicked != null && slotClicked.getHasStack() )
        {
            ItemStack newStackInSlotClicked = slotClicked.getStack();
            oldStackInSlotClicked = newStackInSlotClicked.copy();

            if ( iSlotClicked == 0 )  
            {
            	// crafting result slot
            	
                if (!this.mergeItemStack( newStackInSlotClicked, 10, 46, true ) )
                {
                    return null;
                }

                slotClicked.onSlotChange(newStackInSlotClicked, oldStackInSlotClicked);
            }
            else if (iSlotClicked >= 10 && iSlotClicked < 37)
            {
            	// Main inventory clicked, drop into the crafting grid
            	
                if ( !mergeItemStack( newStackInSlotClicked, 1, 10, false ) )
                {
                    return null;
                }
            }
            else if ( iSlotClicked >= 37 && iSlotClicked < 46 )
            {
            	// Hotbar clicked, drop into the crafting grid
            	
                if ( !mergeItemStack( newStackInSlotClicked, 1, 10, false ) )
                {
                    return null;
                }
            }
            else if ( !mergeItemStack( newStackInSlotClicked, 10, 46, true ) )
            {
                return null;
            }

            if ( newStackInSlotClicked.stackSize == 0 )
            {
                slotClicked.putStack( (ItemStack)null );
            }
            else
            {
                slotClicked.onSlotChanged();
            }

            if ( newStackInSlotClicked.stackSize == oldStackInSlotClicked.stackSize )
            {
                return null;
            }

            slotClicked.onPickupFromSlot( player, newStackInSlotClicked );
        }

        return oldStackInSlotClicked;
    }
}