// FCMOD

package net.minecraft.src;

public class FCItemBrickUnfired extends FCItemPlacesAsBlock
{
    public FCItemBrickUnfired( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcBlockUnfiredBrick.blockID );
    	
    	m_bRequireNoEntitiesInTargetBlock = true; // so that it isn't immediately kicked away
    	
    	setUnlocalizedName( "fcItemBrickUnfired" );
    	
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
    
    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 9;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockUnfiredClay.blockID;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
