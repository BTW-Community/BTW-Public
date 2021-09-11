// FCMOD

package net.minecraft.src;

public class FCBlockEndStone extends FCBlockFullBlock
{
    public FCBlockEndStone( int iBlockID, Material material )
    {
    	super( iBlockID, material );
    	
		SetCanBeCookedByKiln( true );
    }

    @Override
    public void OnCookedByKiln( World world, int i, int j, int k )
    {
    	world.setBlockWithNotify( i, j, k, 0 );

		FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, FCBetterThanWolves.fcItemEnderSlag.itemID, 0 );
		
		FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, FCBetterThanWolves.fcAestheticOpaque.blockID, FCBlockAestheticOpaque.m_iSubtypeWhiteCobble );
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 1.0F;
    }
    
    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 8;
    }
    
	//----------- Client Side Functionality -----------//
}
