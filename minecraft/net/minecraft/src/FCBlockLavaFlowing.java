// FCMOD

package net.minecraft.src;

public class FCBlockLavaFlowing extends BlockFlowing
{
    protected FCBlockLavaFlowing( int iBlockID, Material material)
    {
        super( iBlockID, material );
    }
    
    @Override
    public boolean CanPathThroughBlock( IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder )
    {
    	return entity.handleLavaMovement();
    }
    
    @Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -2;
    }
    
    @Override
    public boolean GetDoesFireDamageToEntities( World world, int i, int j, int k )
    {
    	return true;
    }    
    
    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }    
}
