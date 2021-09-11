// FCMOD

package net.minecraft.src;

public class FCItemArrowBroadhead extends FCItemArrow
{
    public FCItemArrowBroadhead( int iItemID )
    {
    	super( iItemID );
		
    	SetNeutralBuoyant();
    	
    	// neutralize parent properties
		SetBellowsBlowDistance( 0 );		
    	SetNotIncineratedInCrucible();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.NONE );
    	
    	setUnlocalizedName( "fcItemArrowBroadhead" );
    }

    @Override
    EntityArrow GetFiredArrowEntity( World world, double dXPos, double dYPos, double dZPos )
    {
        EntityArrow entity = new FCEntityBroadheadArrow( world, dXPos, dYPos, dZPos );
        
        entity.canBePickedUp = 1;
        
        return entity;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
