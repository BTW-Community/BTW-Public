// FCMOD

package net.minecraft.src;

public class FCTileEntityEnderChest extends TileEntityEnderChest
{
    private InventoryEnderChest m_localChestInventory = new InventoryEnderChest();
    
	@Override
    public void readFromNBT( NBTTagCompound tag)
    {
        super.readFromNBT( tag );
        
	    if ( tag.hasKey( "FCEnderItems" ) )
	    {
	        NBTTagList itemList = tag.getTagList( "FCEnderItems" );
	        
	    	m_localChestInventory.loadInventoryFromNBT( itemList );
	    }	    
    }

	@Override
    public void writeToNBT( NBTTagCompound tag )
    {
        super.writeToNBT( tag );
        
	    if ( m_localChestInventory != null )
	    {
	    	tag.setTag( "FCEnderItems", m_localChestInventory.saveInventoryToNBT() );
	    }	    
    }
	
    public InventoryEnderChest GetLocalEnderChestInventory()
    {
    	return m_localChestInventory;
    }	
}
