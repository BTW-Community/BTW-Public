// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockChunkOreIron extends FCBlockChunkOre
{
    protected FCBlockChunkOreIron( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockChunkOreIron" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		return FCBetterThanWolves.fcItemChunkIronOre.itemID;
    }
	
	@Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
		return FCBetterThanWolves.fcItemNuggetIron.itemID;
    }
}