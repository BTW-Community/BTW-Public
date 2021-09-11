// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBookshelf extends Block
{
    public FCBlockBookshelf( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );
        
        setHardness( 1.5F );        
        SetAxesEffectiveOn();
        
        SetBuoyant();        
        SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOD_BASED_BLOCK );
		SetFireProperties( FCEnumFlammability.BOOKSHELVES );
		
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "bookshelf" );
        
        setCreativeTab( CreativeTabs.tabBlock );
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
		DropItemsIndividualy( world, i, j, k, Item.book.itemID, 3, 0, fChanceOfDrop );
		
		return true;
	}
	
	//----------- Client Side Functionality -----------//
}