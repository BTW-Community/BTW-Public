// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCTileEntityHamper extends FCTileEntityBasket
    implements IInventory
{
	public static final int m_iInventorySize = 4;
	public static final String m_sUnlocalizedName = "container.fcHamper"; 
	
	private static final int m_iStackSizeLimit = 64; // standard
	private static final double m_dMaxPlayerInteractionDistSq = 64D; // standard
	
	private static final int m_iFullUpdateInterval = 200;
	
    private ItemStack m_Contents[];
    
    private int m_iNumUsingPlayers;
    private int m_iFullUpdateCounter = 0;
    
    public FCTileEntityHamper()
    {
    	super( FCBetterThanWolves.fcBlockHamper );
    	
    	m_Contents = new ItemStack[m_iInventorySize];    	
    }

    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        NBTTagList tagList = tag.getTagList( "Items" );
        
        m_Contents = new ItemStack[getSizeInventory()];
        
        for ( int iTempIndex = 0; iTempIndex < tagList.tagCount(); iTempIndex++ )
        {
            NBTTagCompound tempTag = (NBTTagCompound)tagList.tagAt( iTempIndex );
            
            int tempSlot = tempTag.getByte( "Slot" ) & 0xff;
            
            if ( tempSlot >= 0 && tempSlot < m_Contents.length )
            {
            	m_Contents[tempSlot] = ItemStack.loadItemStackFromNBT( tempTag );
            }
        }
    }
    
    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
        super.writeToNBT(tag);
        
        NBTTagList tagList = new NBTTagList();
        
        for ( int iTempIndex = 0; iTempIndex < m_Contents.length; iTempIndex++ )
        {
            if ( m_Contents[iTempIndex] != null )
            {
                NBTTagCompound tempTag = new NBTTagCompound();
                
                tempTag.setByte( "Slot", (byte)iTempIndex );
                
                m_Contents[iTempIndex].writeToNBT( tempTag );
                
                tagList.appendTag( tempTag );
            }
        }     

        tag.setTag( "Items", tagList );
    }

    @Override
    public void updateEntity()
    {
    	super.updateEntity();
    	
    	if ( !worldObj.isRemote )
    	{
    		if ( m_iNumUsingPlayers > 0 && !m_blockBasket.GetIsOpen( worldObj, xCoord, yCoord, zCoord ) )
    		{
				worldObj.playSoundEffect( xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
					"step.gravel", 0.25F + ( worldObj.rand.nextFloat() * 0.1F ), 
		    		0.5F + ( worldObj.rand.nextFloat() * 0.1F ) );

				m_blockBasket.SetIsOpen( worldObj, xCoord, yCoord, zCoord, true );
				
				if ( m_bClosing )
				{
					m_bClosing = false;
					
			        worldObj.addBlockEvent( xCoord, yCoord, zCoord, getBlockType().blockID, 1, 0 );
				}
    		}
    		
        	m_iFullUpdateCounter++;
        	
    		if ( ( m_iFullUpdateCounter + xCoord + yCoord + zCoord ) % m_iFullUpdateInterval == 0 )
    		{
        		if ( m_iNumUsingPlayers != 0 )
        		{
        			int iOldNumUsing = m_iNumUsingPlayers;
        			m_iNumUsingPlayers = 0;
        			
                    Iterator<EntityPlayer> playerIterator = ( worldObj.getEntitiesWithinAABB( EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB( 
                		xCoord - m_fMaxKeepOpenRange, yCoord - m_fMaxKeepOpenRange, zCoord - m_fMaxKeepOpenRange, 
                		xCoord + 1 + m_fMaxKeepOpenRange, yCoord + 1 + m_fMaxKeepOpenRange, zCoord + 1 + m_fMaxKeepOpenRange ) ) ).
                		iterator();

                    while ( playerIterator.hasNext() )
                    {
                        EntityPlayer tempPlayer = playerIterator.next();

                        if ( tempPlayer.openContainer instanceof FCContainerHamper )
                        {
                            if ( ((FCContainerHamper)tempPlayer.openContainer).m_containerInventory == this )
                            {
                                m_iNumUsingPlayers++;
                            }
                        }
                    }
        		}
    		}    			
    	}
	}
    
    @Override
    public void EjectContents()
    {
        FCUtilsInventory.EjectInventoryContents( worldObj, xCoord, yCoord, zCoord, this );
    }    
    
    //------------- IInventory implementation -------------//
    
    @Override
    public int getSizeInventory()
    {
        return m_iInventorySize;
    }

    @Override
    public ItemStack getStackInSlot( int iSlot )
    {
        return m_Contents[iSlot];
    }

    @Override
    public ItemStack decrStackSize( int iSlot, int iAmount )
    {
    	return FCUtilsInventory.DecrStackSize( this, iSlot, iAmount );    	
    }

    @Override
    public ItemStack getStackInSlotOnClosing( int iSlot )
    {
    	// I do not believe this is necessary other than to implement interface
    	
        return null;
    }
    
    @Override
    public void setInventorySlotContents( int iSlot, ItemStack itemstack )
    {
    	m_Contents[iSlot] = itemstack;
    	
        if ( itemstack != null && itemstack.stackSize > getInventoryStackLimit() )
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return m_sUnlocalizedName;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return m_iStackSizeLimit;
    }

    @Override
    public boolean isUseableByPlayer( EntityPlayer entityplayer )
    {
        return worldObj.getBlockTileEntity( xCoord, yCoord, zCoord ) == this && 
	    	entityplayer.getDistanceSq( (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D ) 
	    	<= m_dMaxPlayerInteractionDistSq;
    }
    
    @Override
    public void openChest()
    {
        if ( m_iNumUsingPlayers < 0 )
        {
        	m_iNumUsingPlayers = 0;
        }

        m_iNumUsingPlayers++;
    }

    @Override
    public void closeChest()
    {
        m_iNumUsingPlayers--;            
    }

    @Override
    public boolean isStackValidForSlot( int iSlot, ItemStack stack )
    {
        return true;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
    	return false;
    }
    
    @Override
    public boolean ShouldStartClosingServerSide()
    {
    	return !worldObj.isRemote && m_iNumUsingPlayers <= 0;
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}