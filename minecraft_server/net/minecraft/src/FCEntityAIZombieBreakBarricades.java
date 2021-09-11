// FCMOD

package net.minecraft.src;

public class FCEntityAIZombieBreakBarricades extends EntityAIBase
{
    private EntityLiving m_AssociatedEntity;
    
    private int m_iDoorPosI;
    private int m_iDoorPosJ;
    private int m_iDoorPosK;
    
    private Block m_TargetBlock;
    
    private int breakingTime;
    
    private int field_75358_j = -1;

    public FCEntityAIZombieBreakBarricades(EntityLiving par1EntityLiving)
    {
        m_AssociatedEntity = par1EntityLiving;
    }

    @Override
    public boolean shouldExecute()
    {
        if ( m_AssociatedEntity.isCollidedHorizontally )
        {
            PathNavigate pathNavigate = m_AssociatedEntity.getNavigator();
            PathEntity path = pathNavigate.getPath();

            if ( path != null && !path.isFinished() && pathNavigate.getCanBreakDoors() )
            {
                for ( int iTempPathIndex = 0; iTempPathIndex < Math.min( path.getCurrentPathIndex() + 2, path.getCurrentPathLength() ); ++iTempPathIndex )
                {
                    PathPoint tempPathPoint = path.getPathPointFromIndex( iTempPathIndex );
                    
                    if ( m_AssociatedEntity.getDistanceSq( tempPathPoint.xCoord, m_AssociatedEntity.posY, tempPathPoint.zCoord ) <= 2.25D )
                    {
                        m_iDoorPosI = tempPathPoint.xCoord;
                        m_iDoorPosJ = tempPathPoint.yCoord + 1;
                        m_iDoorPosK = tempPathPoint.zCoord;

                        m_TargetBlock = ShouldBreakBarricadeAtPos( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );

                        if ( m_TargetBlock == null )
                        {
                            m_iDoorPosJ = tempPathPoint.yCoord;
                            
                            m_TargetBlock = ShouldBreakBarricadeAtPos( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );

                            if ( m_TargetBlock == null )
                            {
                                m_iDoorPosJ = MathHelper.floor_double( m_AssociatedEntity.posY + 1 );
                                
                            	m_TargetBlock = ShouldBreakBarricadeAtPos( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );
                            	
                                if ( m_TargetBlock == null )
                                {
                                    m_iDoorPosJ = MathHelper.floor_double( m_AssociatedEntity.posY );
                                    
                                	m_TargetBlock = ShouldBreakBarricadeAtPos( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );
                                }
                            }
                        }
                        
                        
                        if ( m_TargetBlock != null )
                        {
                            return true;
                        }                        
                        
                    }
                    else
                    {
                    	break;
                    }
                }

                m_iDoorPosI = MathHelper.floor_double( m_AssociatedEntity.posX );
                m_iDoorPosJ = MathHelper.floor_double( m_AssociatedEntity.posY + 1.0D );
                m_iDoorPosK = MathHelper.floor_double( m_AssociatedEntity.posZ );
                
                m_TargetBlock = ShouldBreakBarricadeAtPos( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );
                
                if ( m_TargetBlock == null )
                {
                    m_iDoorPosJ = MathHelper.floor_double( m_AssociatedEntity.posY );
                    
                    m_TargetBlock = ShouldBreakBarricadeAtPos( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );                    
                }
                
                if ( m_TargetBlock != null )
                {
                	return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public void startExecuting()
    {
        breakingTime = 0;
    }

    @Override
    public boolean continueExecuting()
    {
    	if ( breakingTime > 240 || m_AssociatedEntity.worldObj.getBlockId( m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK ) != m_TargetBlock.blockID )
    	{
    		return false;
    	}
    	
        double dDistSqToDoor = m_AssociatedEntity.getDistanceSq( (double)m_iDoorPosI, (double)m_iDoorPosJ, (double)m_iDoorPosK );
        
        return dDistSqToDoor < 4.0D;
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        
        m_AssociatedEntity.worldObj.destroyBlockInWorldPartially( m_AssociatedEntity.entityId, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK, -1 );
    }

    @Override
    public void updateTask()
    {
        if ( m_AssociatedEntity.getRNG().nextInt( 20 ) == 0 )
        {
            m_AssociatedEntity.worldObj.playAuxSFX( 1010, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK, 0 );
        }

        ++breakingTime;
        
        int iModifiedBreakTime = (int)( (float)breakingTime / 240.0F * 10.0F );

        if ( iModifiedBreakTime != field_75358_j )
        {
            m_AssociatedEntity.worldObj.destroyBlockInWorldPartially( m_AssociatedEntity.entityId, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK, iModifiedBreakTime );
            field_75358_j = iModifiedBreakTime;
        }

        if ( breakingTime == 240 )
        {
        	int iMetadata = m_AssociatedEntity.worldObj.getBlockMetadata( m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );
        	
            m_AssociatedEntity.worldObj.setBlockToAir( m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK );
            
            if ( m_TargetBlock.blockID != Block.doorWood.blockID && m_TargetBlock.blockID != FCBetterThanWolves.fcBlockDoorWood.blockID )
            {
            	m_TargetBlock.dropBlockAsItem( m_AssociatedEntity.worldObj, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK, iMetadata, 0 );
            }
            
            m_AssociatedEntity.worldObj.playAuxSFX( 1012, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK, 0 );
            m_AssociatedEntity.worldObj.playAuxSFX( 2001, m_iDoorPosI, m_iDoorPosJ, m_iDoorPosK, m_TargetBlock.blockID );
        }
    }
    
    //------------- Class Specific Methods ------------//
    
    private Block ShouldBreakBarricadeAtPos( World world, int i, int j, int k )
    {
        int iBlockID = world.getBlockId( i, j, k );

        if ( iBlockID != 0 )
        {
        	Block block = Block.blocksList[iBlockID];
        	
        	if ( block.IsBreakableBarricade( world, i, j, k ) /*&& !block.IsBreakableBarricadeOpen( world, i, j, k )*/  )
        	{
        		return block;
        	}        	
        }
        
        return null;
    }
}
