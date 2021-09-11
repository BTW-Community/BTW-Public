// FCMOD

package net.minecraft.src;

public class FCTileEntityToolPlaced extends TileEntity
	implements FCITileEntityDataPacketHandler
{
	private ItemStack m_toolStack = null;
	
    public FCTileEntityToolPlaced()
    {
    	super();
    }
    
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        NBTTagCompound storageTag = tag.getCompoundTag( "fcToolStack" );

        if ( storageTag != null )
        {
            m_toolStack = ItemStack.loadItemStackFromNBT( storageTag );
        }
    }
    
    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
        super.writeToNBT( tag );
        
        if ( m_toolStack != null)
        {
            NBTTagCompound storageTag = new NBTTagCompound();
            
            m_toolStack.writeToNBT( storageTag );
            
            tag.setCompoundTag( "fcToolStack", storageTag );
        }
    }
        
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        
        if ( m_toolStack != null )
        {
            NBTTagCompound storageTag = new NBTTagCompound();
            
            m_toolStack.writeToNBT( storageTag );
            
            tag.setCompoundTag( "x", storageTag );
        }
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, tag );
    }
    
    //------------- FCITileEntityDataPacketHandler ------------//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound tag )
    {
        NBTTagCompound storageTag = tag.getCompoundTag( "x" );

        if ( storageTag != null )
        {
        	m_toolStack = ItemStack.loadItemStackFromNBT( storageTag );
        }        
        
        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }    
    
    //------------- Class Specific Methods ------------//
    
    public void SetToolStack( ItemStack stack )
    {
    	if ( stack != null )
    	{
	    	m_toolStack = stack.copy();
    	}
    	else
    	{
    		m_toolStack = null;
    	}    	
    }
    
    public ItemStack GetToolStack()
    {
    	return m_toolStack;
    }
    
    public void EjectContents()
    {
    	if ( m_toolStack != null )
    	{
    		FCUtilsItem.EjectStackWithRandomOffset( worldObj, this.xCoord, yCoord, zCoord, m_toolStack );
    		
    		m_toolStack = null;
    	}
    }    
    
	//----------- Client Side Functionality -----------//
}