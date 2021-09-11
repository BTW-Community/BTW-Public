// FCMOD

package net.minecraft.src;

public class FCItemFoodCured extends FCItemFood
{
    public FCItemFoodCured( int iItemID, int iHungerHealed, float fSaturationModifier, String sItemName )
    {
        super( iItemID, iHungerHealed, fSaturationModifier, false, sItemName );
        
        maxStackSize = 32;
        
        setUnlocalizedName( sItemName );    
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			player.playSound( "random.fizz",  0.25F, 2.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F );
		}
		
    	super.onCreated( stack, world, player );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}