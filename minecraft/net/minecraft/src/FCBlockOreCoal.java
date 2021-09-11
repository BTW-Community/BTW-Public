// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockOreCoal extends FCBlockOreStaged
{
    public FCBlockOreCoal( int iBlockID )
    {
        super( iBlockID );
    }
    
    @Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
        return Item.coal.itemID;
    }
    
    @Override
    public int IdDroppedOnConversion( int iMetadata )
    {
        return FCBetterThanWolves.fcItemCoalDust.itemID;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}