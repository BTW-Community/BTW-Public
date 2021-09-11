// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCobblestone extends Block
{
    public FCBlockCobblestone( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        SetPicksEffectiveOn();
    }
	
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockCobblestoneLoose.blockID;
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