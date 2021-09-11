// FCMOD

package net.minecraft.src;

public class FCItemFishingRod extends ItemFishingRod
{
    public FCItemFishingRod( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 32 );
        
        SetBuoyant();
        SetFilterableProperties( m_iFilterable_Narrow );
        
        setUnlocalizedName( "fishingRod" );
    }
}
