// FCMOD

package net.minecraft.src;

public class FCBlockChunkOreStorageGold extends FCBlockChunkOreStorage
{
    protected FCBlockChunkOreStorageGold( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockChunkOreStorageGold" );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemChunkGoldOre.itemID, 
			9, 0, fChanceOfDrop );
		
		return true;
	}	
    
	@Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
		return Item.ingotGold.itemID;
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}