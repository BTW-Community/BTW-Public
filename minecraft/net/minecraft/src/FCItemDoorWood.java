// FCMOD

package net.minecraft.src;

public class FCItemDoorWood extends FCItemDoor
{
    public FCItemDoorWood( int iITemID )
    {
        super( iITemID );

        SetBuoyant();
        SetIncineratedInCrucible();
        
        setUnlocalizedName( "doorWood" );
    }

    @Override
    public Block GetDoorBlock()
    {
    	return FCBetterThanWolves.fcBlockDoorWood;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
