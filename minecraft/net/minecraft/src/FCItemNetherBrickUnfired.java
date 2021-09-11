// FCMOD

package net.minecraft.src;

public class FCItemNetherBrickUnfired extends FCItemPlacesAsBlock
{
    public FCItemNetherBrickUnfired( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcUnfiredPottery.blockID, 
    		FCBlockUnfiredPottery.m_iSubtypeNetherBrick );
    	
    	SetNeutralBuoyant();
    	
    	setUnlocalizedName( "fcItemBrickNetherUnfired" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			player.playSound( "mob.slime.attack", 0.25F, 
				(  world.rand.nextFloat() - world.rand.nextFloat() ) * 0.1F + 0.7F );
		}
		
		super.onCreated( stack, world, player );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
