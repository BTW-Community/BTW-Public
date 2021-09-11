// FCMOD

package net.minecraft.src;

public class FCSpawnLocation
{
	public int m_iIPos;
	public int m_iJPos;
	public int m_iKPos;
	public long m_lSpawnTime;
	
	public FCSpawnLocation()
	{
		m_iIPos = 0;
		m_iJPos = 0;
		m_iKPos = 0;
		m_lSpawnTime = 0;
	}
	
	public FCSpawnLocation( int iIPos, int iJPos, int iKPos, long iSpawnTime )
	{
		m_iIPos = iIPos;
		m_iJPos = iJPos;
		m_iKPos = iKPos;
		m_lSpawnTime = iSpawnTime;
	}
	
	public FCSpawnLocation( NBTTagCompound tagCompound )
	{
		LoadFromNBT( tagCompound );
	}
	
	public void LoadFromNBT( NBTTagCompound tagCompound )
	{
		m_iIPos = tagCompound.getInteger( "IPos" );
		m_iJPos = tagCompound.getShort( "JPos" );
		m_iKPos = tagCompound.getInteger( "KPos" );
		m_lSpawnTime = tagCompound.getLong( "SpawnTime" );
	}
	
    public NBTTagCompound WriteToNBT( NBTTagCompound tagCompound )
    {
    	tagCompound.setInteger( "IPos", m_iIPos );
    	tagCompound.setShort( "JPos", (short)m_iJPos );
    	tagCompound.setInteger( "KPos", m_iKPos );
    	tagCompound.setLong( "SpawnTime", m_lSpawnTime );
    	
        return tagCompound;
    }    
}