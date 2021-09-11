// FCMOD

package net.minecraft.src;

public class FCMagneticPoint
{
	static private double[] m_dFieldStrengthMultipliersByLevel = new double[] { 0D, 1D, 8D, 27D, 64D, 125D, 216D, 343D, 4096D }; 
	static private double[] m_dMaxRangeSquaredForLevelWithNoise = new double[] { 0D, 100D, 400D, 1600D, 6400, 25600, 102400, 409600, Double.POSITIVE_INFINITY }; 
		
	public int m_iIPos;
	public int m_iJPos;
	public int m_iKPos;
	public int m_iFieldLevel;
	
	public FCMagneticPoint()
	{
		m_iIPos = 0;
		m_iJPos = 0;
		m_iKPos = 0;
		m_iFieldLevel = 0;
	}
	
	public FCMagneticPoint( int iIPos, int iJPos, int iKPos, int iFieldLevel )
	{
		m_iIPos = iIPos;
		m_iJPos = iJPos;
		m_iKPos = iKPos;
		m_iFieldLevel = iFieldLevel;
	}
	
	public FCMagneticPoint( NBTTagCompound tagCompound )
	{
		LoadFromNBT( tagCompound );
	}
	
	public void LoadFromNBT( NBTTagCompound tagCompound )
	{
		m_iIPos = tagCompound.getInteger( "IPos" );
		m_iJPos = tagCompound.getShort( "JPos" );
		m_iKPos = tagCompound.getInteger( "KPos" );
		m_iFieldLevel = tagCompound.getByte( "Lvl");		
	}
	
    public NBTTagCompound WriteToNBT( NBTTagCompound tagCompound )
    {
    	tagCompound.setInteger( "IPos", m_iIPos );
    	tagCompound.setShort( "JPos", (short)m_iJPos );
    	tagCompound.setInteger( "KPos", m_iKPos );
		tagCompound.setByte( "Lvl", (byte)m_iFieldLevel );		
    	
        return tagCompound;
    }
    
    public double GetFieldStrengthRelativeToPosition( double dRelativeX, double dRelativeZ )
    {
    	double dDeltaX = (double)m_iIPos - dRelativeX;
    	double dDeltaZ =  (double)m_iKPos - dRelativeZ;
    	
    	double dDistanceSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
    	
    	return m_dFieldStrengthMultipliersByLevel[m_iFieldLevel] / dDistanceSq;    	
    }
    
    public double GetFieldStrengthRelativeToPositionWithBackgroundNoise( double dRelativeX, double dRelativeZ )
    {
    	double dDeltaX = (double)m_iIPos - dRelativeX;
    	double dDeltaZ =  (double)m_iKPos - dRelativeZ;
    	
    	double dDistanceSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
    	
    	if ( dDistanceSq <= m_dMaxRangeSquaredForLevelWithNoise[m_iFieldLevel] )
    	{    	
    		return m_dFieldStrengthMultipliersByLevel[m_iFieldLevel] / dDistanceSq;
    	}
    	else
    	{
    		return -1D;
    	}
    }
}