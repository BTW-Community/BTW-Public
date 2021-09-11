// FCMOD

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FCMagneticPointList
{
	public List m_MagneticPoints;
	
	public FCMagneticPointList()
	{
		m_MagneticPoints = new ArrayList();
	}
	
    public void loadFromNBT( NBTTagList tagList )
    {
    	m_MagneticPoints.clear();
    	
        for ( int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount )
        {
            NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt( iTempCount );
            
            FCMagneticPoint newPoint = new FCMagneticPoint( tempCompound );
            
            m_MagneticPoints.add( newPoint );
        }
    }
    
    public NBTTagList saveToNBT()
    {
        NBTTagList tagList = new NBTTagList( "MagneticPoints" );
        
    	Iterator tempIterator = m_MagneticPoints.iterator();

    	while( tempIterator.hasNext() )
    	{
            NBTTagCompound tempTagCompound = new NBTTagCompound();
            
    		FCMagneticPoint tempPoint = (FCMagneticPoint)tempIterator.next();
    		
            tempPoint.WriteToNBT( tempTagCompound );
            
            tagList.appendTag( tempTagCompound );
    	}
    	
        return tagList;
    }
    
    public void RemovePointAt( int iIPos, int iJPos, int iKPos )
    {
    	Iterator tempIterator = m_MagneticPoints.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCMagneticPoint tempPoint = (FCMagneticPoint)tempIterator.next();
    		
    		if ( tempPoint.m_iIPos == iIPos && tempPoint.m_iKPos == iKPos && tempPoint.m_iJPos == iJPos )
    		{
    			tempIterator.remove();
    			
    			return;
    		}
    	}
    }
    
    public void AddPoint( int iIPos, int iJPos, int iKPos, int iPowerLevel )
    {
        FCMagneticPoint newPoint = new FCMagneticPoint( iIPos, iJPos, iKPos, iPowerLevel );
        
        m_MagneticPoints.add( newPoint );
    }
    
    public void ChangePowerLevelOfPointAt( int iIPos, int iJPos, int iKPos, int iPowerLevel )
    {
		FCMagneticPoint point = GetMagneticPointAtLocation( iIPos, iJPos, iKPos );
		
		if ( point != null )
		{
			point.m_iFieldLevel = iPowerLevel;
		}    	
    }
    
    public FCMagneticPoint GetMagneticPointAtLocation( int iIPos, int iJPos, int iKPos )
    {    	
    	Iterator tempIterator = m_MagneticPoints.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCMagneticPoint tempPoint = (FCMagneticPoint)tempIterator.next();
    		
    		if ( tempPoint.m_iIPos == iIPos && tempPoint.m_iKPos == iKPos && tempPoint.m_iJPos == iJPos )
    		{
    			return tempPoint;
    		}
    	}
    	
    	return null;
    }
}
