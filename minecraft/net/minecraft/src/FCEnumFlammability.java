// FCMOD

package net.minecraft.src;

public enum FCEnumFlammability
{
	NONE( 0, 0 ),
	MINIMAL( 5, 5 ),
	MEDIUM( 5, 20 ),
	HIGH( 30, 60 ),
	EXTREME( 60, 100 ),
	QUICKCATCHSLOWBURN( 15, 100 ),
	MEDIUMCATCHQUICKBURN( 30, 20 );
	
	public static final FCEnumFlammability 
		LOGS = MINIMAL,
		PLANKS = MEDIUM,
		LEAVES = HIGH,
		CLOTH = HIGH,
		CROPS = HIGH,
		GRASS = EXTREME,
		WICKER = EXTREME,
		EXPLOSIVES = QUICKCATCHSLOWBURN,
		VINES = QUICKCATCHSLOWBURN,
		BOOKSHELVES = MEDIUMCATCHQUICKBURN; 
	
	public final int m_iChanceToEncourageFire;
	public final int m_iAbilityToCatchFire;
	
    private FCEnumFlammability( int iChanceToEncourageFire, int iAbilityToCatchFire )
    {
    	m_iChanceToEncourageFire = iChanceToEncourageFire;
    	m_iAbilityToCatchFire = iAbilityToCatchFire;
    }
}
