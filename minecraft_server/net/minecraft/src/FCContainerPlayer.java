// FCMOD

package net.minecraft.src;

public class FCContainerPlayer extends ContainerPlayer
{
    public FCContainerPlayer( InventoryPlayer inventory, boolean bNotRemote, EntityPlayer player )
    {
    	super( inventory, bNotRemote, player );
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
            	
                if (!this.mergeItemStack( newStackInSlotClicked, 9, 45, true ) )
                {
                    return null;
                }

                slotClicked.onSlotChange( newStackInSlotClicked, oldStackInSlotClicked );
            }
            else if ( iSlotClicked >= 1 && iSlotClicked < 5 )
            {
            	// crafting grid
            	
                if ( !mergeItemStack( newStackInSlotClicked, 9, 45, true ) )
                {
                    return null;
                }
            }
            else if ( iSlotClicked >= 5 && iSlotClicked < 9 )
            {
            	// armor slots
            	
                if ( !mergeItemStack(newStackInSlotClicked, 9, 45, true ) )
                {
                    return null;
                }
            }
            else if ( oldStackInSlotClicked.getItem() instanceof ItemArmor && !((Slot)inventorySlots.get(5 + ((ItemArmor)oldStackInSlotClicked.getItem()).armorType)).getHasStack() )
            {
            	// armor items from main inventory and hotbar, transfered to armor slots
            	
                int iArmorSlot = 5 + ((ItemArmor)oldStackInSlotClicked.getItem()).armorType;

                if ( !mergeItemStack( newStackInSlotClicked, iArmorSlot, iArmorSlot + 1, false ) )
                {
                    return null;
                }
            }
            else if ( iSlotClicked >= 9 && iSlotClicked < 36 )
            {
            	// main player inventory slots
            	
                if ( !mergeItemStack( newStackInSlotClicked, 36, 45, false ) )
                {
                    return null;
                }
            }
            else if ( iSlotClicked >= 36 && iSlotClicked < 45 )
            {
            	// hotbar slots
            	
                if ( !mergeItemStack( newStackInSlotClicked, 9, 36, false ) )
                {
                    return null;
                }
            }
            else if ( !mergeItemStack( newStackInSlotClicked, 9, 45, true ) )
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

            slotClicked.onPickupFromSlot(player, newStackInSlotClicked);
        }

        return oldStackInSlotClicked;
    }
}
