// FCMOD

package net.minecraft.src;

public class FCBiomeGenHell extends BiomeGenHell
{
    public FCBiomeGenHell( int iBiomeID )
    {
        super( iBiomeID );
        
        spawnableMonsterList.clear();
        
        spawnableMonsterList.add( new SpawnListEntry( FCEntityGhast.class, 50, 4, 4 ) );
        spawnableMonsterList.add( new SpawnListEntry( FCEntityPigZombie.class, 100, 4, 4 ) );
        spawnableMonsterList.add( new SpawnListEntry( FCEntityMagmaCube.class, 1, 4, 4 ) );
    }
}
