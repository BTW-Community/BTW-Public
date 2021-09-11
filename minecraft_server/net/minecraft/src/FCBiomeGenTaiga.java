// FCMOD

package net.minecraft.src;

public class FCBiomeGenTaiga extends BiomeGenTaiga
{
    public FCBiomeGenTaiga( int iBiomeID )
    {
        super( iBiomeID );
        
        spawnableCreatureList.clear();
        
        spawnableCreatureList.add( new SpawnListEntry( FCEntitySheep.class, 12, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityPig.class, 10, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityCow.class, 8, 4, 4 ) );
        
        spawnableCreatureList.add( new SpawnListEntry( FCEntityWolf.class, 8, 4, 4 ) );
    }
}
