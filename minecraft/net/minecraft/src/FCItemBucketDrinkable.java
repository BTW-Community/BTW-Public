// FCMOD

package net.minecraft.src;

public abstract class FCItemBucketDrinkable extends FCItemBucket
{
	private int m_iHungerHealed;
	private float m_fSaturationModifier;
	
    public FCItemBucketDrinkable( int iItemID, int iHungerHealed, float fSaturationModifier )
    {
        super( iItemID );
        
    	m_iHungerHealed = iHungerHealed;
    	m_fSaturationModifier = fSaturationModifier;
    }
    
    @Override
    public int getMaxItemUseDuration( ItemStack stack )
    {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction( ItemStack stack )
    {
        return EnumAction.drink;
    }
    
    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	if ( player.CanDrink() )
    	{
    		player.setItemInUse( stack, getMaxItemUseDuration( stack ) );
    	}
    	else
    	{
    		player.OnCantConsume();
    	}
        
        return stack;
    }
    
    @Override
    public ItemStack onEaten( ItemStack itemStack, World world, EntityPlayer player )
    {
    	// override to add nutritional value to drinking milk buckets
    	
        if ( !player.capabilities.isCreativeMode )
        {
            --itemStack.stackSize;
        }

        if ( !world.isRemote )
        {
            player.getFoodStats().addStats( m_iHungerHealed, m_fSaturationModifier ); // food value adjusted for increased hunger meter resolution
        }

        return itemStack.stackSize <= 0 ? new ItemStack( Item.bucketEmpty ) : itemStack;
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
