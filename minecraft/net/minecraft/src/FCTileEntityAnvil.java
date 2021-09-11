// FCMOD

package net.minecraft.src;

public class FCTileEntityAnvil extends TileEntity
{
    private int m_iMouldContentsBitField;
    
	public FCTileEntityAnvil()
	{
		m_iMouldContentsBitField = 0; 
	}
	
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        
        nbttagcompound.setInteger( "m_iMouldContentsBitField", m_iMouldContentsBitField );
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        
        if( nbttagcompound.hasKey( "m_iMouldContentsBitField" ) )
        {
        	m_iMouldContentsBitField = nbttagcompound.getInteger( "m_iMouldContentsBitField" );
        }
        else
        {
        	m_iMouldContentsBitField = 0;
        }        
    }    
    
    //************* Class Specific Methods ************//
    
    public void ClearMouldContents()
    {
    	m_iMouldContentsBitField = 0;
    }
    
    public boolean DoesSlotContainMould( int iSlotX, int iSlotY )
    {
    	int iSlotNum = ( iSlotX + iSlotY * 4 );
    	
    	return DoesSlotContainMould( iSlotNum );    	
    }
    
    public boolean DoesSlotContainMould( int iSlotNum )
    {
    	int iBitMask = 1 << iSlotNum;
    	
    	return ( m_iMouldContentsBitField & iBitMask ) > 0; 
    }
    
    public void SetSlotContainsMould( int iSlotNum )
    {
    	int iBitMask = 1 << iSlotNum;
    	
    	m_iMouldContentsBitField |= iBitMask;
    }
    
    public void SetSlotContainsMould( int iSlotX, int iSlotY )
    {
    	int iBitMask = 1 << ( iSlotX + iSlotY * 4 );
    	
    	m_iMouldContentsBitField |= iBitMask;
    }
    
    public void EjectMoulds()
    {
    	int iMouldCount = 0;
    	
    	for ( int iTemp = 0; iTemp < 16; iTemp++ )
    	{
    		if ( DoesSlotContainMould( iTemp ) )
    		{
    			FCUtilsItem.EjectSingleItemWithRandomOffset( worldObj, xCoord, yCoord, zCoord, FCBetterThanWolves.fcItemMould.itemID, 0 );
    		}    	
    	}
    	
    	ClearMouldContents();
    }
}