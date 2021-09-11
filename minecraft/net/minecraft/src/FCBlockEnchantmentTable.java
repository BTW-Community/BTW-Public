// FCMOD

package net.minecraft.src;

public class FCBlockEnchantmentTable extends BlockEnchantmentTable
{
    protected FCBlockEnchantmentTable( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F );
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {	
    	if ( iSide != 1 )
    	{
    		return super.shouldSideBeRendered( blockAccess, iNeighborI, iNeighborJ, iNeighborK, 
    			iSide );
    	}
    	
    	return true;
    }
}
