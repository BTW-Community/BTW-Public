// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchNetherBurning extends FCBlockTorchBaseBurning
{
    protected FCBlockTorchNetherBurning( int iBlockID )
    {
    	super( iBlockID );
    	
    	setLightValue( 0.9375F );
    	
    	setUnlocalizedName( "fcBlockTorchNetherBurning" );
    	
    	setTickRandomly( true );
    }
	
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        super.updateTick( world, i, j, k, rand );
        
        // last param provides increased chance of fire spread, over default of 100
        
		FCBlockFire.CheckForFireSpreadAndDestructionToOneBlockLocation( world, i, j + 1, k, rand, 0, 25 );
    }
    
	@Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//

	//----------- Client Side Functionality -----------//
}
