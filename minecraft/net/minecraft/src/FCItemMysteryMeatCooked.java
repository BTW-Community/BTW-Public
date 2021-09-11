// FCMOD

package net.minecraft.src;

public class FCItemMysteryMeatCooked extends ItemFood
{
	public FCItemMysteryMeatCooked( int iItemID )
	{
		super( iItemID, 5, 0.25F, true, false );
		
		setUnlocalizedName( "fcItemMysteryMeatCooked" );		
	}

	//----------- Client Side Functionality -----------//
	
	@Override
    public void registerIcons( IconRegister register )
    {
        itemIcon = register.registerIcon("beefCooked");
    }
}
