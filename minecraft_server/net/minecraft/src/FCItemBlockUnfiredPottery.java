//FCMOD 

package net.minecraft.src;

public class FCItemBlockUnfiredPottery extends ItemBlock
{
    public FCItemBlockUnfiredPottery( int i )
    {
        super( i );
        
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        setUnlocalizedName( "fcBlockUnfiredPottery" );
    }

    @Override
    public int getMetadata( int i )
    {
        return i;
    }
}
