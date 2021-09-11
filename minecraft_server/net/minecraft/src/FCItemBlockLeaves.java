// FCMOD

package net.minecraft.src;

public class FCItemBlockLeaves extends ItemBlock
{
    public FCItemBlockLeaves( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes( true );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
        return iItemDamage | 4;
    }

}
