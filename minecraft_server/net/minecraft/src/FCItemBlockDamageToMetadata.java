// FCMOD

package net.minecraft.src;

public class FCItemBlockDamageToMetadata extends ItemBlock
{
	protected FCItemBlockDamageToMetadata(  int iItemID  )
	{
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
	}
	
    @Override
    public int getMetadata( int iItemDamage )
    {
		return iItemDamage;    	
    }
}
