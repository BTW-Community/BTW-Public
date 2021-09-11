// FCMOD

package net.minecraft.src;

public class FCItemBlockMillStone extends ItemBlock
{
    public FCItemBlockMillStone( int iItemID )
    {
    	super( iItemID );
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			player.playSound( "random.anvil_break", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F );
		}
		
    	super.onCreated( stack, world, player );
    }    
}
