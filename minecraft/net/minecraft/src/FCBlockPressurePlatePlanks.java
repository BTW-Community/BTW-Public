// FCMOD

package net.minecraft.src;

public class FCBlockPressurePlatePlanks extends FCBlockPressurePlate
{
    protected FCBlockPressurePlatePlanks( int iBlockID )
    {
    	super( iBlockID, "wood", FCBetterThanWolves.fcMaterialPlanks, EnumMobType.everything );
    	
    	setHardness( 0.5F );
    	
        SetAxesEffectiveOn();
        
    	SetBuoyant();
    	
    	setStepSound( soundWoodFootstep );
    	
    	setUnlocalizedName( "pressurePlate" );    	
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		
		return true;
	}
	
	//----------- Client Side Functionality -----------//
}