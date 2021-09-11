// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockChunkOreGold extends FCBlockChunkOre
{
    protected FCBlockChunkOreGold( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockChunkOreGold" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		return FCBetterThanWolves.fcItemChunkGoldOre.itemID;
    }
	
	@Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
		return Item.goldNugget.itemID;
    }

}