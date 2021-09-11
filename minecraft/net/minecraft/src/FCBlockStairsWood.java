// FCMOD

package net.minecraft.src;

public class FCBlockStairsWood extends FCBlockStairs
{
    protected FCBlockStairsWood( int iBlockID, Block referenceBlock, int iReferenceBlockMetadata )
    {
        super( iBlockID, referenceBlock, iReferenceBlockMetadata );
        
        SetAxesEffectiveOn();
        
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.PLANKS );		
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, 
			fChanceOfDrop );
		
		return true;
	}
		
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
    	return m_referenceBlock.GetFurnaceBurnTime( m_iReferenceBlockMetadata ) * 3 / 4;
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//    
}