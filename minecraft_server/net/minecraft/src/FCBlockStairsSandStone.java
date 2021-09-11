// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStairsSandStone extends FCBlockStairs
{
    protected FCBlockStairsSandStone( int iBlockID )
    {
        super( iBlockID, Block.sandStone, 0 );
        
        SetPicksEffectiveOn();
    }
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 3; // diamonds or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSand.itemID, 12, 0, fChanceOfDrop );
		
		return true;
	}
	
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}