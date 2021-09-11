// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCEntityAIMoveToLooseFood extends EntityAIBase
{
    protected EntityAnimal m_myAnimal;
    
    protected float m_fMoveSpeed;
    
    private EntityItem temptingItem = null;

    private static final int m_iUpdatesBetweenChecks = 20; // updates are only every 3 ticks  
    	
    private int m_iDelayBetweenChecksCount = m_iUpdatesBetweenChecks;
    private boolean m_bFailedToPath = false;
    
    public FCEntityAIMoveToLooseFood( EntityAnimal animal, float fMoveSpeed )
    {
        m_myAnimal = animal;
        m_fMoveSpeed = fMoveSpeed;
        
        setMutexBits( 3 );
    }

    @Override
    public boolean shouldExecute()
    {
    	boolean bReturnValue = false;
    	    	
        if ( m_iDelayBetweenChecksCount <= 0 )
        {
        	// slight variance to avoid all animals syncing up
        	
        	m_iDelayBetweenChecksCount = m_iUpdatesBetweenChecks + 
        		m_myAnimal.rand.nextInt( 3 ) - 1; 
        	
        	if ( m_myAnimal.IsReadyToEatLooseFood() )
        	{
			    List<EntityItem> entityList = m_myAnimal.worldObj.getEntitiesWithinAABB( 
			    	EntityItem.class, AxisAlignedBB.getAABBPool().getAABB( 
					m_myAnimal.posX - 10F, m_myAnimal.posY - 7F, m_myAnimal.posZ - 10F, 
					m_myAnimal.posX + 10F, m_myAnimal.posY + 7F, m_myAnimal.posZ + 10F ) );
			    
			    if ( !entityList.isEmpty() )
			    {
	        		double dClosestDistSq = 0D;
	        		temptingItem = null;
	        		
			    	Iterator<EntityItem> entityIterator = entityList.iterator();

			    	while ( entityIterator.hasNext() )
		            {
			    		EntityItem tempEntity = entityIterator.next();
			    		
				        if ( tempEntity.isEntityAlive() &&
			        		m_myAnimal.IsReadyToEatLooseItem( tempEntity.getEntityItem() ) )
				        {
			        		double dTempDistSq = m_myAnimal.getDistanceSqToEntity( 
			        			tempEntity );
			        		
			        		if ( temptingItem == null || dTempDistSq < dClosestDistSq )
			        		{
			        			temptingItem = tempEntity;
			        			
			        			dClosestDistSq = dTempDistSq;
				        		
				        		bReturnValue = true;
			        		}
			    		}
		            }
		        }
        	}
        }
        else
        {
        	m_iDelayBetweenChecksCount--;
        }
        
        return bReturnValue;
    }

    @Override
    public boolean continueExecuting()
    {
    	return !m_bFailedToPath && temptingItem != null && temptingItem.isEntityAlive() &&
			m_myAnimal.IsReadyToEatLooseItem( temptingItem.getEntityItem() );
    }
    
    public void updateTask()
    {
        m_myAnimal.getLookHelper().setLookPositionWithEntity( temptingItem, 30F, 
        	m_myAnimal.getVerticalFaceSpeed() );
        
        if ( IsWithinEatBox() )
        {
        	// just hang out in the eat box
        	
            m_myAnimal.getNavigator().clearPathEntity();
        }
        else if ( !m_myAnimal.getNavigator().TryMoveToEntity( temptingItem, m_fMoveSpeed ) )
        {
        	m_bFailedToPath = true;
        }
    }

    @Override
    public void resetTask()
    {
        temptingItem = null;
        m_bFailedToPath = false;
        m_myAnimal.getNavigator().clearPathEntity();
    }
    
    public boolean IsWithinEatBox()
    {
    	// slightly smaller than the actual eat box to ensure we move within it
    	
    	AxisAlignedBB eatBox = AxisAlignedBB.getAABBPool().getAABB( 
    		m_myAnimal.boundingBox.minX - 1.45F, 
    		m_myAnimal.boundingBox.minY - 0.95F, 
    		m_myAnimal.boundingBox.minZ - 1.45F, 
    		m_myAnimal.boundingBox.maxX + 1.45F, 
    		m_myAnimal.boundingBox.maxY + 0.95F, 
    		m_myAnimal.boundingBox.maxZ + 1.45F );
    	
    	return eatBox.intersectsWith( temptingItem.boundingBox );
    }
}
