// FCMOD

package net.minecraft.src;

public class FCBlockRedstoneWire extends BlockRedstoneWire
{
    public FCBlockRedstoneWire( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F );
    }
    
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
	@Override
	public boolean TriggersBuddy()
	{
		return false;
	}
	
	//----------- Client Side Functionality -----------//
}
