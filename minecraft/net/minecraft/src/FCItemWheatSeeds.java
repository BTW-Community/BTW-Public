// FCMOD

package net.minecraft.src;

public class FCItemWheatSeeds extends FCItemSeeds
{
    public FCItemWheatSeeds( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcBlockWheatCrop.blockID );
    	
    	SetAsBasicChickenFood();
    	
    	setUnlocalizedName( "fcItemWheatSeeds" );
	}
    
    //------------- Class Specific Methods ------------//	
    
	//----------- Client Side Functionality -----------//
}
