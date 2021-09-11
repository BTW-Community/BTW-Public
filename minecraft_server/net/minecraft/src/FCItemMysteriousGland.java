// FCMOD

package net.minecraft.src;

public class FCItemMysteriousGland extends Item
{
    public FCItemMysteriousGland( int iItemID )
    {
    	super( iItemID );

    	SetBuoyant();
		SetBellowsBlowDistance( 2 ); // same as dye powder, and thus the ink sack 
		SetFilterableProperties( m_iFilterable_Small );
    	
    	setPotionEffect( PotionHelper.speckledMelonEffect );
    	
    	setUnlocalizedName( "fcItemMysteriousGland" );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
