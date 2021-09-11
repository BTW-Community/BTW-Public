// FCMOD

package net.minecraft.src;

public class FCTileEntityCement extends TileEntity
	implements FCITileEntityDataPacketHandler
{
    private int m_iDryTime;
    private int m_iSpreadDist;
    
	public FCTileEntityCement()
	{
		m_iSpreadDist = 0; 
		m_iDryTime = 0;
	}
	
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        
        nbttagcompound.setInteger( "dryTime", m_iDryTime );
        nbttagcompound.setInteger( "spreadDist", m_iSpreadDist );
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        
        if( nbttagcompound.hasKey( "dryTime" ) )
        {
        	m_iDryTime = nbttagcompound.getInteger( "dryTime" );
        }
        else
        {
        	// if the cement doesn't have a drytime associated with it, set it to dry immediately
        	
        	m_iDryTime = FCBlockCement.iCementTicksToDry;
        }
        
        if( nbttagcompound.hasKey( "spreadDist" ) )
        {
        	m_iSpreadDist = nbttagcompound.getInteger( "spreadDist" );
        }
        else
        {
        	// if the cement doesn't have a spread associated with it, set it to not spread further
        	
        	m_iSpreadDist = FCBlockCement.iMaxCementSpreadDist;
        }
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        nbttagcompound.setShort( "d", (short)m_iDryTime );
        nbttagcompound.setShort( "s", (short)m_iSpreadDist );
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, nbttagcompound );
    }
    
    
    //------------- FCITileEntityDataPacketHandler ------------//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound nbttagcompound )
    {
    	m_iDryTime = nbttagcompound.getShort( "d" );
    	m_iSpreadDist = nbttagcompound.getShort( "s" );     
        
        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }    
    
    //------------- Class Specific Methods ------------//
    
    public int GetDryTime()
    {
    	return m_iDryTime;
    }
    
    public void SetDryTime( int iDryTime )
    {
    	m_iDryTime = iDryTime;
    	
    	// mark block to be sent to client
    	
        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }
    
    public int GetSpreadDist()
    {
    	return m_iSpreadDist;
    }
    
    public void SetSpreadDist( int iSpreadDist )
    {
    	m_iSpreadDist = iSpreadDist;
    	
    	// mark block to be sent to client
    	
        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }
    
    //----------- Client Side Functionality -----------//
}