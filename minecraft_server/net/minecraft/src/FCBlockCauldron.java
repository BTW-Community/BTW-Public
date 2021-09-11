// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockCauldron extends FCBlockCookingVessel
{
    public FCBlockCauldron( int iBlockID )
    {
        super( iBlockID, Material.iron );
        
        setHardness( 3.5F );
        setResistance( 10F );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "fcBlockCauldron" );        
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityCauldron();
    }
    
    //------------- FCBlockCookingVessel -------------//

	@Override
	protected void ValidateFireUnderState( World world, int i, int j, int k )
	{
		// FCTODO: Move this to parent class
		
		if ( !world.isRemote )
		{
			TileEntity tileEnt = world.getBlockTileEntity( i, j, k );
			
			if ( tileEnt instanceof FCTileEntityCauldron )
			{
				FCTileEntityCauldron tileEntityCauldron = 
	            	(FCTileEntityCauldron)tileEnt;
	            
	            tileEntityCauldron.ValidateFireUnderType();            
			}
		}
	}
	
	@Override
	protected int GetContainerID()
	{
		return FCBetterThanWolves.fcCauldronContainerID;
	}
	
    //------------- Class Specific Methods -------------//
    
}