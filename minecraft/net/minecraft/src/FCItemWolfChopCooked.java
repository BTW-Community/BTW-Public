// FCMOD

package net.minecraft.src;

public class FCItemWolfChopCooked extends ItemFood
{
	public FCItemWolfChopCooked( int iItemID )
	{
		super( iItemID, 5, 0.25F, false, false );
		
		setUnlocalizedName( "fcItemWolfChopCooked" );		
	}

	//----------- Client Side Functionality -----------//
	
	@Override
    public void registerIcons( IconRegister register )
    {
        itemIcon = register.registerIcon("porkchopCooked");
    }
}
