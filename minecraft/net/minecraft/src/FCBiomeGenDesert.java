// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBiomeGenDesert extends BiomeGenDesert
{
    public FCBiomeGenDesert( int iBiomeID )
    {
        super( iBiomeID );
    }

    @Override
    public boolean CanLightningStrikeInBiome()
    {
    	// Can't rain in deserts, but lightning can still strike
    	
    	return true;
    }
}