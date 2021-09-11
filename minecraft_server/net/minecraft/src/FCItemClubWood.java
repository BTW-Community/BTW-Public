// FCMOD

package net.minecraft.src;

public class FCItemClubWood extends FCItemClub
{
    public static final int m_iWeaponDamageWood = 2;    
    public static final int m_iDurabilityWood = 10;
    
    public FCItemClubWood( int iItemID )
    {
        super( iItemID, m_iDurabilityWood, m_iWeaponDamageWood, "fcItemClub" );
        
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT );   	
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
