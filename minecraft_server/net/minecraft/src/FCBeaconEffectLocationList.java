// FCMOD

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FCBeaconEffectLocationList
{
	public List m_EffectLocations;
	
	public FCBeaconEffectLocationList()
	{
		m_EffectLocations = new ArrayList();
	}
	
    public void loadFromNBT( NBTTagList tagList )
    {
    	m_EffectLocations.clear();
    	
        for ( int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount )
        {
            NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt( iTempCount );
            
            FCBeaconEffectLocation newPoint = new FCBeaconEffectLocation( tempCompound );
            
            m_EffectLocations.add( newPoint );
        }
    }
    
    public NBTTagList saveToNBT()
    {
        NBTTagList tagList = new NBTTagList( "EffectLocations" );
        
    	Iterator tempIterator = m_EffectLocations.iterator();

    	while( tempIterator.hasNext() )
    	{
            NBTTagCompound tempTagCompound = new NBTTagCompound();
            
            FCBeaconEffectLocation tempPoint = (FCBeaconEffectLocation)tempIterator.next();
    		
            tempPoint.WriteToNBT( tempTagCompound );
            
            tagList.appendTag( tempTagCompound );
    	}
    	
        return tagList;
    }
    
    public void RemovePointAt( int iIPos, int iJPos, int iKPos )
    {
    	Iterator tempIterator = m_EffectLocations.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCBeaconEffectLocation tempPoint = (FCBeaconEffectLocation)tempIterator.next();
    		
    		if ( tempPoint.m_iIPos == iIPos && tempPoint.m_iKPos == iKPos && tempPoint.m_iJPos == iJPos )
    		{
    			tempIterator.remove();
    			
    			return;
    		}
    	}
    }
    
    public void AddPoint( int iIPos, int iJPos, int iKPos, int iEffectLevel, int iRange )
    {
    	FCBeaconEffectLocation newPoint = new FCBeaconEffectLocation( iIPos, iJPos, iKPos, iEffectLevel, iRange );
        
        m_EffectLocations.add( newPoint );
    }
    
    public void ChangeEffectLevelOfPointAt( int iIPos, int iJPos, int iKPos, int iPowerLevel, int iRange )
    {
    	FCBeaconEffectLocation point = GetEffectAtLocation( iIPos, iJPos, iKPos );
		
		if ( point != null )
		{
			point.m_iEffectLevel = iPowerLevel;
			point.m_iRange = iRange;
		}    	
    }
    
    public FCBeaconEffectLocation GetEffectAtLocation( int iIPos, int iJPos, int iKPos )
    {    	
    	Iterator tempIterator = m_EffectLocations.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCBeaconEffectLocation tempPoint = (FCBeaconEffectLocation)tempIterator.next();
    		
    		if ( tempPoint.m_iIPos == iIPos && tempPoint.m_iKPos == iKPos && tempPoint.m_iJPos == iJPos )
    		{
    			return tempPoint;
    		}
    	}
    	
    	return null;
    }
    
    public int GetMostPowerfulBeaconEffectForLocation( int iIPos, int iKPos )
    {
    	int iMaxLevel = 0;
    	
    	Iterator tempIterator = m_EffectLocations.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCBeaconEffectLocation tempPoint = (FCBeaconEffectLocation)tempIterator.next();
    		
    		if ( iIPos >= tempPoint.m_iIPos - tempPoint.m_iRange && iIPos <= tempPoint.m_iIPos + tempPoint.m_iRange &&
    			iKPos >= tempPoint.m_iKPos - tempPoint.m_iRange && iKPos <= tempPoint.m_iKPos + tempPoint.m_iRange )
    		{
        		if ( tempPoint.m_iEffectLevel > iMaxLevel )
        		{
	    			return iMaxLevel = tempPoint.m_iEffectLevel;
	    		}
    		}
    	}
    	
    	return iMaxLevel;
    }
}
