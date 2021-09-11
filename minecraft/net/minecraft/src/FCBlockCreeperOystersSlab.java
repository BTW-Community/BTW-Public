// FCMOD

package net.minecraft.src;

public class FCBlockCreeperOystersSlab extends FCBlockSlab
{
    public FCBlockCreeperOystersSlab( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( FCBlockCreeperOysters.m_fHardness );
        SetShovelsEffectiveOn( true );
        
        SetBuoyancy( 1F );        
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setUnlocalizedName( "fcBlockCreeperOystersSlab" );
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
		return false;
    }

	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
		return FCBetterThanWolves.fcItemCreeperOysters.itemID;
    }
	
	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
		return 4; // 8 in slab
    }
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockCreeperOysters.blockID;
	}
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
