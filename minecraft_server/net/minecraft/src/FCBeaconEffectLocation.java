// FCMOD

package net.minecraft.src;

public class FCBeaconEffectLocation
{
	public int m_iIPos;
	public int m_iJPos;
	public int m_iKPos;
	public int m_iEffectLevel;
	public int m_iRange;
	
	public FCBeaconEffectLocation()
	{
		m_iIPos = 0;
		m_iJPos = 0;
		m_iKPos = 0;
		m_iEffectLevel = 0;
		m_iRange = 0;
	}
	
	public FCBeaconEffectLocation( int iIPos, int iJPos, int iKPos, int iEffectLevel, int iRange )
	{
		m_iIPos = iIPos;
		m_iJPos = iJPos;
		m_iKPos = iKPos;
		m_iEffectLevel = iEffectLevel;
		m_iRange = iRange;
	}
	
	public FCBeaconEffectLocation( NBTTagCompound tagCompound )
	{
		LoadFromNBT( tagCompound );
	}
	
	public void LoadFromNBT( NBTTagCompound tagCompound )
	{
		m_iIPos = tagCompound.getInteger( "IPos" );
		m_iJPos = tagCompound.getShort( "JPos" );
		m_iKPos = tagCompound.getInteger( "KPos" );
		m_iEffectLevel = tagCompound.getByte( "Lvl");		
		m_iRange = tagCompound.getInteger( "Rng");		
	}
	
    public NBTTagCompound WriteToNBT( NBTTagCompound tagCompound )
    {
    	tagCompound.setInteger( "IPos", m_iIPos );
    	tagCompound.setShort( "JPos", (short)m_iJPos );
    	tagCompound.setInteger( "KPos", m_iKPos );
		tagCompound.setByte( "Lvl", (byte)m_iEffectLevel );		
    	tagCompound.setInteger( "Rng", m_iRange );
    	
        return tagCompound;
    }    
}