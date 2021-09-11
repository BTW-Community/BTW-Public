// FCMOD

package net.minecraft.src;

public class FCBiomeGenEnd extends BiomeGenEnd
{
    public FCBiomeGenEnd( int iBiomeID )
    {
        super( iBiomeID );
        
        spawnableMonsterList.clear();
        
        spawnableMonsterList.add( new SpawnListEntry( FCEntityEnderman.class, 10, 4, 4 ) );
    }
}
