// FCMOD

package net.minecraft.src;

public class FCClientSoundTrackerEntry
{
	String m_sName;
	float m_fXPos;
	float m_fYPos;
	float m_fZPos;
	float m_fMaxRangeSq;
	
	public FCClientSoundTrackerEntry( String sName, float fXPos, float fYPos, float fZPos, float fMaxRange )
	{
		m_sName = sName;
		m_fXPos = fXPos;
		m_fYPos = fYPos;
		m_fZPos = fZPos;
		m_fMaxRangeSq = fMaxRange * fMaxRange;
	}
}