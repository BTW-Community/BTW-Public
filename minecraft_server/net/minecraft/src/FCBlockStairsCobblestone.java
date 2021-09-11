// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStairsCobblestone extends FCBlockStairs
{
    protected FCBlockStairsCobblestone( int iBlockID )
    {
        super( iBlockID, Block.cobblestone, 0 );
        
        SetPicksEffectiveOn();
        
        setUnlocalizedName( "stairsStone" );
    }
	
    @Override
    public int idDropped( int iMetaData, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockCobblestoneLooseStairs.blockID;
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