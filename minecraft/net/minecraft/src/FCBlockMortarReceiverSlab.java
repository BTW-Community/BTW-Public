// FCMOD

package net.minecraft.src;

public abstract class FCBlockMortarReceiverSlab extends FCBlockSlabFalling
{
    public FCBlockMortarReceiverSlab( int iBlockID, Material material )
    {
    	super( iBlockID, material );
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k ) 
    {
    	if ( HasNeighborWithMortarInContact( world, i, j, k ) )
    	{
	        world.playAuxSFX( FCBetterThanWolves.m_iLooseBlockOnMortarAuxFXID, i, j, k, 0 ); 

            world.scheduleBlockUpdate( i, j, k, blockID, FCBlockFalling.m_iTackyFallingBlockTickRate );
    	}
    	else
    	{
    		ScheduleCheckForFall( world, i, j, k );
    	}
    }
    
    @Override
    public boolean CanBePlacedUpsideDownAtLocation( World world, int i, int j, int k )
    {
    	return HasNeighborWithMortarInContact( world, i, j, k, true );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//    
}
