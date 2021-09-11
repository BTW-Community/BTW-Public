// FCMOD

package net.minecraft.src;

public class FCBlockRottenFleshSlab extends FCBlockSlab
{
    public FCBlockRottenFleshSlab( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( FCBlockRottenFlesh.m_fHardness );
        SetShovelsEffectiveOn( true );
        
        SetBuoyancy( 1F );        
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setUnlocalizedName( "fcBlockRottenFleshSlab" );
        
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
		return Item.rottenFlesh.itemID;
    }
	
	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
		return 2; // 4 in full slab
    }
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockRottenFlesh.blockID;
	}
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
