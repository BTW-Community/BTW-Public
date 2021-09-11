// FCMOD

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FCSpawnLocationList
{
	public List m_SpawnLocations;
	
	public FCSpawnLocationList()
	{
		m_SpawnLocations = new ArrayList();
	}
	
    public void loadFromNBT( NBTTagList tagList )
    {
    	m_SpawnLocations.clear();
    	
        for ( int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount )
        {
            NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt( iTempCount );
            
            FCSpawnLocation newPoint = new FCSpawnLocation( tempCompound );
            
            m_SpawnLocations.add( newPoint );
        }
    }
    
    public NBTTagList saveToNBT()
    {
        NBTTagList tagList = new NBTTagList( "SpawnLocations" );
        
    	Iterator tempIterator = m_SpawnLocations.iterator();

    	while( tempIterator.hasNext() )
    	{
            NBTTagCompound tempTagCompound = new NBTTagCompound();
            
            FCSpawnLocation tempPoint = (FCSpawnLocation)tempIterator.next();
    		
            tempPoint.WriteToNBT( tempTagCompound );
            
            tagList.appendTag( tempTagCompound );
    	}
    	
        return tagList;
    }
    
    public void AddPoint( int iIPos, int iJPos, int iKPos, long lSpawnTime )
    {
    	FCSpawnLocation newPoint = new FCSpawnLocation( iIPos, iJPos, iKPos, lSpawnTime );
        
        m_SpawnLocations.add( newPoint );
    }
    
    public FCSpawnLocation GetClosestSpawnLocationForPosition( double dXPos, double dZPos )
    {
    	FCSpawnLocation closestLocation = null;
    	double dClosestDistSq = 0;
    	
    	Iterator tempIterator = m_SpawnLocations.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCSpawnLocation tempPoint = (FCSpawnLocation)tempIterator.next();
    		
    		double dDeltaI = (double)tempPoint.m_iIPos - dXPos;
    		double dDeltaK = (double)tempPoint.m_iKPos - dZPos;
    		
    		double dTempDistSq = ( dDeltaI * dDeltaI ) + ( dDeltaK * dDeltaK );
    		
    		if ( closestLocation == null || dTempDistSq < dClosestDistSq )
    		{
    			closestLocation = tempPoint;
    			dClosestDistSq = dTempDistSq;
    		}    		
    	}
    	
    	return closestLocation;
    }
    
    public FCSpawnLocation GetMostRecentSpawnLocation()
    {
    	FCSpawnLocation mostRecent = null;
    	
    	Iterator tempIterator = m_SpawnLocations.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCSpawnLocation tempPoint = (FCSpawnLocation)tempIterator.next();
    		
    		if ( mostRecent == null || tempPoint.m_lSpawnTime > mostRecent.m_lSpawnTime )
    		{
    			mostRecent = tempPoint;
    		}    		
    	}
    	
    	return mostRecent;
    }
    
    public boolean DoesListContainPoint( int iIPos, int iJPos, int iKPos, long lSpawnTime )
    {
    	Iterator tempIterator = m_SpawnLocations.iterator();
    	
    	while ( tempIterator.hasNext() )
    	{
    		FCSpawnLocation tempPoint = (FCSpawnLocation)tempIterator.next();
    		
    		if ( tempPoint.m_iIPos == iIPos && tempPoint.m_iKPos == iKPos && tempPoint.m_iJPos == iJPos && tempPoint.m_lSpawnTime == lSpawnTime )
    		{
    			return true;
    		}
    	}    	
    	
    	return false;
    }
    
    public void AddPointIfNotAlreadyPresent( int iIPos, int iJPos, int iKPos, long lSpawnTime )
    {
    	if ( !DoesListContainPoint( iIPos, iJPos, iKPos, lSpawnTime ) )
		{
    		AddPoint( iIPos, iJPos, iKPos, lSpawnTime );
		}    	
    }    
}