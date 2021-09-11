// FCMOD 
// Note that this file only applies to the sand blocks themselves, 
// not blocks that inherit from BlockSand

package net.minecraft.src;

public class FCBlockSand extends FCBlockFallingFullBlock
{
    public FCBlockSand( int iBlockID )
    {
        super( iBlockID, Material.sand );
        
        setHardness( 0.5F );
        SetShovelsEffectiveOn();
		SetFilterableProperties( Item.m_iFilterable_Fine );
        
        setStepSound( soundSandFootstep );
        
        setUnlocalizedName( "sand" );
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 0.8F;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSand.itemID, 6, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
	@Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 2;
    }
    
	@Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return Block.sandStone.blockID;
    }
    
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
	@Override
    public boolean CanReedsGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanCactusGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
}