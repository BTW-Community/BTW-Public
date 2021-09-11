// FCMOD

package net.minecraft.src;

public class FCItemWheatLegacy extends Item
{
    public FCItemWheatLegacy( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
        SetIncineratedInCrucible();
		SetBellowsBlowDistance( 1 );
    	SetFilterableProperties( m_iFilterable_Narrow );
        
    	SetAsBasicHerbivoreFood();
    	SetAsBasicPigFood();
    	
    	setUnlocalizedName( "wheat" );
	}
    
    //------------- Class Specific Methods ------------//	
    
	//----------- Client Side Functionality -----------//
}
