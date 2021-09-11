// FCMOD

package net.minecraft.src;

public class FCBlockWaterStationary extends BlockStationary
{
    protected FCBlockWaterStationary( int iBlockID, Material material )
    {
        super( iBlockID, material );
    }
    
    @Override
    public boolean CanPathThroughBlock( IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder )
    {
    	return pathFinder.CanPathThroughWater();
    }
    
    @Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1;
    }

    @Override
    public int AdjustPathWeightOnNotBlocked( int iPreviousWeight )
    {
    	return 2;
    }
}  
