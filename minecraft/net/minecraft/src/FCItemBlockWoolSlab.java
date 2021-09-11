// FCMOD 

package net.minecraft.src;

public class FCItemBlockWoolSlab extends FCItemBlockSlab
{
    public FCItemBlockWoolSlab( int i )
    {
        super( i );
        
        setHasSubtypes( true );
        
        setUnlocalizedName( "fcBlockWoolSlab" );
    }

    @Override
    public int getMetadata( int i )
    {
    	return i;
    }
    
    @Override
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
        if ( iFacing == 0 || iFacing != 1 && (double)fClickY > 0.5D )
        {
			return FCBetterThanWolves.fcBlockWoolSlabTop.blockID;
        }
        
		return FCBetterThanWolves.fcWoolSlab.blockID;
    }
    
    @Override
    public boolean canCombineWithBlock( World world, int i, int j, int k, int iItemDamage )
    {
        int iBlockID = world.getBlockId( i, j, k );
        int iBlockMetadata = world.getBlockMetadata( i, j, k );
        
        if ( ( iBlockID == FCBetterThanWolves.fcWoolSlab.blockID || iBlockID == FCBetterThanWolves.fcBlockWoolSlabTop.blockID ) && 
    		iBlockMetadata == iItemDamage )
        {
        	return true;
        }
        
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
}
