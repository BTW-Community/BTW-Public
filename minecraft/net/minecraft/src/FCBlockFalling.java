// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFalling extends Block
{
	public static final int m_iFallingBlockTickRate = 2;
	public static final int m_iTackyFallingBlockTickRate = 40;
	
    public FCBlockFalling( int iBlockID, Material material )
    {
    	super( iBlockID, material );
    }
    
    @Override
    public boolean IsFallingBlock()
    {
    	return true;
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k ) 
    {
    	ScheduleCheckForFall( world, i, j, k );
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID ) 
    {    	
    	ScheduleCheckForFall( world, i, j, k );
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand ) 
    {    	
        CheckForFall( world, i, j, k );
    }
    
    @Override
    public int tickRate( World par1World )
    {
		return m_iFallingBlockTickRate;
    }
}
