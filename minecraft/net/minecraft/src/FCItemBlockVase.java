// FCMOD 

package net.minecraft.src;

public class FCItemBlockVase extends ItemBlock
{
    public FCItemBlockVase( int i )
    {
        super( i );
        
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        setUnlocalizedName( "fcBlockVase" );
    }

    @Override
    public int getMetadata( int i )
    {
    	return i;
    }    
}
