// FCMOD

package net.minecraft.src;

public class FCItemArrowRotten extends FCItemArrow
{
    public FCItemArrowRotten( int iItemID )
    {
    	super( iItemID );
		
		setUnlocalizedName( "fcItemArrowRotten" );
    }

    @Override
    EntityArrow GetFiredArrowEntity( World world, double dXPos, double dYPos, double dZPos )
    {
        EntityArrow entity = new FCEntityRottenArrow( world, dXPos, dYPos, dZPos );
        
        entity.canBePickedUp = 2;
        
        return entity;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
