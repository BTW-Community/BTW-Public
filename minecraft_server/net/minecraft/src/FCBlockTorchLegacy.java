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
}
