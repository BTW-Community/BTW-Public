// FCMOD

package net.minecraft.src;

public class FCItemArmor extends ItemArmor
{
	private final int m_iArmorWeight;
	
    public FCItemArmor( int iItemID, EnumArmorMaterial armorMaterial, int iRenderIndex, int iArmorType, int iWeight )
    {
    	super( iItemID, armorMaterial, iRenderIndex, iArmorType );
    	
    	m_iArmorWeight = iWeight;
    }
    
    @Override
    public int GetWeightWhenWorn()
    {
    	return m_iArmorWeight;
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
    	super.onCreated( stack, world, player );
    	
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			if ( getArmorMaterial() == EnumArmorMaterial.CLOTH )
			{
				player.playSound( "step.cloth", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F );
			}
			else
			{
				player.playSound( "random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F );
			}
		}		
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
