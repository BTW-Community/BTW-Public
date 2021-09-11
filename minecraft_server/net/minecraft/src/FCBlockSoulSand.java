// FCMOD

package net.minecraft.src;

public class FCBlockSoulSand extends BlockSoulSand
{
    public FCBlockSoulSand( int iBlockID )
    {
        super( iBlockID );
        
        SetShovelsEffectiveOn();
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSoulSand.itemID, 
			3, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	return filteredItem.itemID == FCBetterThanWolves.fcItemGroundNetherrack.itemID || 
	    	filteredItem.itemID == FCBetterThanWolves.fcItemSoulDust.itemID ||
	    	filteredItem.itemID == Item.lightStoneDust.itemID;
    }
    
    @Override
    public boolean CanTransformItemIfFilter( ItemStack filteredItem )
    {
    	// anything that passes through soul sand is transformed
    	
    	return true;
    }
    
    @Override
    public boolean CanNetherWartGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
}