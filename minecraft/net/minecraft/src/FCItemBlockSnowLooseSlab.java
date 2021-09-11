// FCMOD

package net.minecraft.src;

public class FCItemBlockSnowLooseSlab extends FCItemBlockSlab
{
    public FCItemBlockSnowLooseSlab( int iItemID )
    {
        super( iItemID );        
    }
    
    @Override
    public boolean canCombineWithBlock( World world, int i, int j, int k, int iItemDamage )
    {
        int iBlockID = world.getBlockId( i, j, k );
        
        if ( iBlockID == getBlockID() )
        {
        	return true;
        }
        
    	return false;
    }
}
