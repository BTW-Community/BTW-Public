// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCobblestoneMossy extends Block
{
    public FCBlockCobblestoneMossy( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 2F );
        setResistance( 10 );
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "stoneMoss" );
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 2; // iron or higher
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStone.itemID, 6, 0, fChanceOfDrop );
		
		return true;
	}
	
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
}
