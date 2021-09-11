// FCMOD

package net.minecraft.src;

public class FCBlockCreeperOysters extends Block
{
    public final static float m_fHardness = 0.6F;
    
    public FCBlockCreeperOysters( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( m_fHardness );
        SetShovelsEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setCreativeTab( CreativeTabs.tabBlock );
        
        setUnlocalizedName( "fcBlockCreeperOysters" );
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
		return 8; // 16 in full block
    }
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}