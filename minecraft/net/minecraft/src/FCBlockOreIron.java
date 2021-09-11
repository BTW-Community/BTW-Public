// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockOreIron extends FCBlockOreStaged
{
    public FCBlockOreIron( int iBlockID )
    {
        super( iBlockID );
        
		SetCanBeCookedByKiln( true );
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemChunkIronOre.itemID;
    }
    
    @Override
    public int IdDroppedOnConversion( int iMetadata )
    {
        return FCBetterThanWolves.fcItemPileIronOre.itemID;
    }
    
    @Override
    public int GetRequiredToolLevelForOre( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 1;
    }
    
    @Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// have to do this here instead of through SetItemIndexDroppedWhenCookedByKiln() 
    	// because of vanilla instatiating this block before the corresponding item
    	
    	return FCBetterThanWolves.fcItemNuggetIron.itemID;
    }
    
    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 8;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
