// FCMOD

package net.minecraft.src;

public class FCBiomeGenForest extends BiomeGenForest
{
    public FCBiomeGenForest( int iBiomeID )
    {
        super( iBiomeID );
        
        spawnableCreatureList.clear();
        
        spawnableCreatureList.add( new SpawnListEntry( FCEntitySheep.class, 12, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityPig.class, 10, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityChicken.class, 10, 4, 4 ) );
        spawnableCreatureList.add( new SpawnListEntry( FCEntityCow.class, 8, 4, 4 ) );
        
        spawnableCreatureList.add( new SpawnListEntry( FCEntityWolf.class, 5, 4, 4 ) );
    }
}
