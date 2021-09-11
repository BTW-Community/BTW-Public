// FCMOD

package net.minecraft.src;

class FCInventoryInfernalEnchanter extends InventoryBasic
{
    final FCContainerInfernalEnchanter m_container;

    FCInventoryInfernalEnchanter( FCContainerInfernalEnchanter container, String name, int iNumSlots )
    {
        super( name, true, iNumSlots );
        
        m_container = container;
    }

    /*
    public int getInventoryStackLimit()
    {
        return 1;
    }
    */

    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        
        m_container.onCraftMatrixChanged( this );
    }
}
