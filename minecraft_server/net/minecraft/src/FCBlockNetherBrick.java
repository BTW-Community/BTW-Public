// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockNetherBrick extends Block
{
    public FCBlockNetherBrick( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialNetherRock );        
        
        setHardness( 2F );
        setResistance( 10F );
        
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "netherBrick" );
        
        setCreativeTab( CreativeTabs.tabBlock );        
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockNetherBrickLoose.blockID;
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
    
	//------------ Client Side Functionality ----------//    
}