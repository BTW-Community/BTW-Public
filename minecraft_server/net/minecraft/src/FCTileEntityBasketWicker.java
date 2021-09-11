// FCMOD

package net.minecraft.src;

public class FCTileEntityBasketWicker extends FCTileEntityBasket
	implements FCITileEntityDataPacketHandler
{
	private ItemStack m_storageStack = null;
	
    public FCTileEntityBasketWicker()
    {
    	super( FCBetterThanWolves.fcBlockBasketWicker );
    }
    
    @Override
    public void updateEntity()
    {
    	super.updateEntity();   

		UpdateVisualContentsState();
    }
    
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        NBTTagCompound storageTag = tag.getCompoundTag( "fcStorageStack" );

        if ( storageTag != null )
        {
            m_storageStack = ItemStack.loadItemStackFromNBT( storageTag );
        }
    }
    
    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
        super.writeToNBT( tag );
        
        if ( m_storageStack != null)
        {
            NBTTagCompound storageTag = new NBTTagCompound();
            
            m_storageStack.writeToNBT( storageTag );
            
            tag.setCompoundTag( "fcStorageStack", storageTag );
        }
    }
        
    @Override
    public void EjectContents()
    {
    	if ( m_storageStack != null )
    	{
    		FCUtilsItem.EjectStackWithRandomOffset( worldObj, this.xCoord, yCoord, zCoord, m_storageStack );
    		
    		m_storageStack = null;
    	}
    }    
    
    //------------- FCITileEntityDataPacketHandler ------------//
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        
        if ( m_storageStack != null)
        {
            NBTTagCompound storageTag = new NBTTagCompound();
            
            m_storageStack.writeToNBT( storageTag );
            
            tag.setCompoundTag( "x", storageTag );
        }
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, tag );
    }

    @Override
    public void readNBTFromPacket( NBTTagCompound tag )
    {
        NBTTagCompound storageTag = tag.getCompoundTag( "x" );

        if ( storageTag != null )
        {
        	m_storageStack = ItemStack.loadItemStackFromNBT( storageTag );
        }        
    }    
    
    @Override
    public boolean ShouldStartClosingServerSide()
    {
    	return !worldObj.isRemote && worldObj.getClosestPlayer( xCoord, yCoord, zCoord, m_fMaxKeepOpenRange ) == null;
    }
    
    //------------- Class Specific Methods ------------//
    
    public void SetStorageStack( ItemStack stack )
    {
    	if ( stack != null )
    	{
	    	m_storageStack = stack.copy();
    	}
    	else
    	{
    		m_storageStack = null;
    	}
    	
		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }
    
    public ItemStack GetStorageStack()
    {
    	return m_storageStack;
    }
    
    private void UpdateVisualContentsState()
    {
    	if ( !worldObj.isRemote )
    	{
			// validate the block's indication of contents in metadata, since it wasn't included in the initial releases
			
			boolean bHasContents = FCBetterThanWolves.fcBlockBasketWicker.GetHasContents( worldObj, xCoord, yCoord, zCoord );
			
			if ( bHasContents != ( m_storageStack != null ) )
			{
				FCBetterThanWolves.fcBlockBasketWicker.SetHasContents( worldObj, xCoord, yCoord, zCoord, m_storageStack != null );
			}
    	}
    }
    
	//----------- Client Side Functionality -----------//
}