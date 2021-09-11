// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockCrucible extends FCBlockCookingVessel
{
    public FCBlockCrucible( int iBlockID )
    {
        super( iBlockID, Material.glass );  
        
        setHardness( 0.6F );
        setResistance( 3F );
        SetPicksEffectiveOn( true );       
        
        setStepSound( soundGlassFootstep );        
        
        setUnlocalizedName( "fcBlockCrucible" );
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityCrucible();
    }

    //------------- FCBlockCookingVessel -------------//

	@Override
	protected void ValidateFireUnderState( World world, int i, int j, int k )
	{
		// FCTODO: Move this to parent class
		
		if ( !world.isRemote )
		{
			TileEntity tileEnt = world.getBlockTileEntity( i, j, k );
			
			if ( tileEnt instanceof FCTileEntityCrucible )
			{
				FCTileEntityCrucible tileEntityCrucible = 
	            	(FCTileEntityCrucible)tileEnt;
	            
	            tileEntityCrucible.ValidateFireUnderType();            
			}
		}
	}
	
	@Override
	protected int GetContainerID()
	{
		return FCBetterThanWolves.fcCrucibleContainerID;
	}
	
    //------------- Class Specific Methods -------------//
    
}