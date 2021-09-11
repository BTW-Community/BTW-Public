// FCMOD

package net.minecraft.src;

public class FCBlockMobSpawner extends BlockMobSpawner
{
    protected FCBlockMobSpawner( int iBlockID )
    {
        super( iBlockID );
        
        setHardness( 5F );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "mobSpawner" );
        
        disableStats();
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityMobSpawner();
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
