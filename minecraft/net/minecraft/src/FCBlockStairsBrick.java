// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStairsBrick extends FCBlockStairs
{
    protected FCBlockStairsBrick( int iBlockID )
    {
        super( iBlockID, Block.brick, 0 );
        
        SetPicksEffectiveOn();
    }
	
    @Override
    public int idDropped( int iMetaData, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockBrickLooseStairs.blockID;
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	@Override
	public boolean HasMortar( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}