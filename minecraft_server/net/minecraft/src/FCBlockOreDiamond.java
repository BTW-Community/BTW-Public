// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockOreDiamond extends FCBlockOreStaged
{
    public FCBlockOreDiamond( int iBlockID )
    {
        super( iBlockID );
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return Item.diamond.itemID;
    }
    
    @Override
    public int IdDroppedOnConversion( int iMetadata )
    {
        return Item.diamond.itemID;
    }
    
    @Override
    public int GetRequiredToolLevelForOre( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}

