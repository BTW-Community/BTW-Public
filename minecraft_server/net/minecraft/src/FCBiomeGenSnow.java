// FCMOD

package net.minecraft.src;

public class FCBiomeGenSnow extends BiomeGenSnow
{
    public FCBiomeGenSnow(  int iBiomeID )
    {
        super( iBiomeID );

        spawnableCreatureList.clear();

        // no chickens in the snow
        
        spawnableCreatureList.add( new SpawnListEntry( FCEntitySheep.class, 12, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityPig.class, 10, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityCow.class, 8, 4, 4 ) );
    }
}
