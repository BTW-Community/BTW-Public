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

	//----------- Client Side Functionality -----------//

    @Override
    public Icon getIconFromDamage( int iItemDamage )
    {
        return FCBetterThanWolves.fcBlockBloodLeaves.getIcon( 0, iItemDamage );
    }

    @Override
    public int getColorFromItemStack( ItemStack itemStack, int iLayer )
    {
    	return 0xD81F1F;
    }
}
