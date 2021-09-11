// FCMOD

package net.minecraft.src;

public class FCBlockChunkOreStorageIron extends FCBlockChunkOreStorage
{
    protected FCBlockChunkOreStorageIron( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockChunkOreStorageIron" );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemChunkIronOre.itemID, 
			9, 0, fChanceOfDrop );
		
		return true;
	}	
	
	@Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
		return Item.ingotIron.itemID;
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}