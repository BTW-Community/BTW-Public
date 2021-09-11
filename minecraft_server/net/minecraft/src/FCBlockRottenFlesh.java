// FCMOD

package net.minecraft.src;

public class FCBlockRottenFlesh extends Block
{
    public final static float m_fHardness = 0.6F;
    
    public FCBlockRottenFlesh( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( m_fHardness );
        SetBuoyancy( 1F );
        
        SetShovelsEffectiveOn( true );
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setCreativeTab( CreativeTabs.tabBlock );
        
        setUnlocalizedName( "fcBlockRottenFlesh" );
    }
    
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
		return false;
    }
    
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
		return Item.rottenFlesh.itemID;
    }
	
	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
		return 5; // 9 in full block
    }
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}