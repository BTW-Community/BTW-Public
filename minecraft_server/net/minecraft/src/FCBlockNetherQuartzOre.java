// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockNetherQuartzOre extends BlockOre
{
    public FCBlockNetherQuartzOre( int iBlockID )
    {
        super( iBlockID );
        
    	SetBlockMaterial( FCBetterThanWolves.fcMaterialNetherRock );
    	
    	setHardness( 1F );
    	setResistance( 5F );
    	
    	setStepSound( soundStoneFootstep );
    	
    	setUnlocalizedName("netherquartz");
    }    
}
