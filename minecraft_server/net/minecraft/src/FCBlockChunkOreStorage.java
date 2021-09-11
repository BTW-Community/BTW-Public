// FCMOD

package net.minecraft.src;

public abstract class FCBlockChunkOreStorage extends FCBlockFallingFullBlock
{
    protected FCBlockChunkOreStorage( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 1F );
        setResistance( 5F );
        SetPicksEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
		setCreativeTab( CreativeTabs.tabBlock );
        
		SetCanBeCookedByKiln( true );
    }
    
    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 8;
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
