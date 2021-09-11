//FCMOD

package net.minecraft.src;

public class FCBlockFarmlandFertilized extends FCBlockFarmland
{
    protected FCBlockFarmlandFertilized( int iBlockID )
    {
    	super( iBlockID );    	
    }
    
	@Override
	public float GetPlantGrowthOnMultiplier( World world, int i, int j, int k, Block plantBlock )
	{
		return 2F;
	}
	
	@Override
	public boolean GetIsFertilizedForPlantGrowth( World world, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public void NotifyOfFullStagePlantGrowthOn( World world, int i, int j, int k, Block plantBlock )
	{
		// revert back to unfertilized soil
		
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		world.setBlockAndMetadataWithNotify( i, j, k, 
			FCBetterThanWolves.fcBlockFarmland.blockID, iMetadata );
	}
	
	@Override
    protected boolean IsFertilized( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
