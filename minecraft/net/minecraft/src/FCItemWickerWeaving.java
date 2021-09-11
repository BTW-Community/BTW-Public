// FCMOD

package net.minecraft.src;

public class FCItemWickerWeaving extends FCItemCraftingProgressive
{
	// takes half as long as other progressive crafting	
	static public final int m_iWickerWeavingMaxDamage = ( 60 * 20 / m_iProgressTimeInterval );
	
    public FCItemWickerWeaving( int iItemID )
    {
    	super( iItemID );
    	
        SetBuoyant();
        SetBellowsBlowDistance( 2 );
    	SetIncineratedInCrucible();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WICKER_PIECE );
        SetFilterableProperties( Item.m_iFilterable_Thin );
    	
        setUnlocalizedName( "fcItemWickerWeaving" );        
    }
    
    @Override
    protected void PlayCraftingFX( ItemStack stack, World world, EntityPlayer player )
    {
        player.playSound( "step.grass", 
        	0.25F + 0.25F * (float)world.rand.nextInt( 2 ), 
        	( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.25F + 1.75F );
    }
    
    @Override
    public ItemStack onEaten( ItemStack stack, World world, EntityPlayer player )
    {
        world.playSoundAtEntity( player, "step.grass", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F );
        
        return new ItemStack( FCBetterThanWolves.fcItemWickerPiece, 1, 0 );
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			player.playSound( "step.grass", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F );
		}
		
    	super.onCreated( stack, world, player );
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoCampfire( int iItemDamage )
    {
    	return false;
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoBrickOven( int iItemDamage )
    {
    	return false;
    }
    
    @Override
    protected int GetProgressiveCraftingMaxDamage()
    {
    	return m_iWickerWeavingMaxDamage;
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
