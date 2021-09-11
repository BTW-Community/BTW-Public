// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockOreLapis extends FCBlockOreStaged
{
    public FCBlockOreLapis( int iBlockID )
    {
        super( iBlockID );
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return Item.dyePowder.itemID ;
    }
    
    @Override
    public int quantityDropped( Random rand )
    {
        return 4 + rand.nextInt( 5 );
    }
    
    @Override
    public int damageDropped( int iMetadata )
    {
        return 4; // blue dye
    }
    
    @Override
    public int IdDroppedOnConversion( int iMetadata )
    {
        return Item.dyePowder.itemID ;
    }
    
    @Override
    public int QuantityDroppedOnConversion( Random rand )
    {
        return 4 + rand.nextInt( 5 );
    }
    
    @Override
    public int DamageDroppedOnConversion( int iMetadata )
    {
        return 4; // blue dye
    }
    
    @Override
    public int GetRequiredToolLevelForOre( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 1;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}

