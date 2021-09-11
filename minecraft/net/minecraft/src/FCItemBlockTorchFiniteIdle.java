// FCMOD 

package net.minecraft.src;

public class FCItemBlockTorchFiniteIdle extends FCItemBlockTorchIdle
{	
    public FCItemBlockTorchFiniteIdle( int iItemID )
    {
        super( iItemID );
        
        setUnlocalizedName( "fcBlockTorchFiniteIdle" );
    }
    
    @Override
    protected ItemStack OnRightClickOnIgniter( ItemStack stack, World world, EntityPlayer player )
    {
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY);
        int k = MathHelper.floor_double(player.posZ);
        
        player.playSound( "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F );
        
        ItemStack newStack = new ItemStack( FCBetterThanWolves.fcBlockTorchFiniteBurning, 1, 0 );
        
        long iExpiryTime = FCUtilsWorld.GetOverworldTimeServerOnly() + (long)FCTileEntityTorchFinite.m_iMaxBurnTime;
        
        newStack.setTagCompound( new NBTTagCompound() );
        newStack.getTagCompound().setLong( "outTime", iExpiryTime);

		stack.stackSize--;
		
        if ( stack.stackSize <= 0 )
        {
        	return newStack; 
        }
        
		FCUtilsItem.GivePlayerStackOrEject( player, newStack, i, j, k );
		
		return stack;
    }    

	//------------- Class Specific Methods ------------//
}
