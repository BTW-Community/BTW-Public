// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchLegacy extends FCBlockTorchBaseBurning
{
    protected FCBlockTorchLegacy( int iBlockID )
    {
    	super( iBlockID );
    	
    	setLightValue( 0.9375F );
    	
    	setUnlocalizedName( "torch" );
    }

    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockTorchNetherBurning.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
    	// legacy torches don't display flame particles to help spot them
    	
    	Vec3 pos = GetParticalPos( world, i, j, k );
    	
        world.spawnParticle( "smoke", pos.xCoord, pos.yCoord, pos.zCoord, 0D, 0D, 0D );
    }    
}
