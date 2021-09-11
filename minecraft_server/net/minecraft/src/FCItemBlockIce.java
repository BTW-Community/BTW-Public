// FCMOD

package net.minecraft.src;

public class FCItemBlockIce extends ItemBlock
{
    public FCItemBlockIce( int iItemID )
    {
        super( iItemID );
    }
    
    @Override
    public int getMetadata( int iDamage )
    {
    	// flag any placed ice as being non-source
    	
        return 8;
    }
}
