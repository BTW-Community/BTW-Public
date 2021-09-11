// FCMOD

package net.minecraft.src;

public class FCBlockBedrock extends FCBlockFullBlock
{
	public FCBlockBedrock( int iBlockID )
	{
		super( iBlockID, Material.rock );
		
		setBlockUnbreakable();
		setResistance( 6000000F );
		
		setStepSound( soundStoneFootstep );
		
		setUnlocalizedName( "bedrock" );
		
		disableStats();
		
		setCreativeTab( CreativeTabs.tabBlock );
	}

	@Override
    public int getMobilityFlag()
    {
        return 2; // cannot be pushed
    }
	
	@Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
    	return false;
    }
	
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
