// FCMOD

package net.minecraft.src;

public class FCBlockSandStone extends BlockSandStone
{
    public FCBlockSandStone( int iBlockID )
    {
        super( iBlockID );
        
        SetPicksEffectiveOn();
        
        setHardness( 1.5F );
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "sandStone" );        
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 3; // diamond or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSand.itemID, 16, 0, fChanceOfDrop );
		
		return true;
	}
	
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
