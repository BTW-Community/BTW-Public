// FCMOD

package net.minecraft.src;

public class FCBlockBoneSlab extends FCBlockSlab
{
    public FCBlockBoneSlab( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialMiscellaneous );
        
        setHardness( 2F ); 
        SetPicksEffectiveOn( true );       
        SetBuoyancy( 1.0F );
        
        setStepSound( soundGravelFootstep );
        
        setCreativeTab( CreativeTabs.tabBlock );
        
        setUnlocalizedName( "fcBlockBoneSlab" );        
    }
    
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
		return false;
    }
	
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
		return Item.bone.itemID;
    }
	
	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
		return 2; // 4 in full slab
    }
    
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcAestheticOpaque.blockID;
	}
	
	@Override
	public int GetCombinedMetadata( int iMetadata )
	{
		return FCBlockAestheticOpaque.m_iSubtypeBone;
	}
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_IconBoneSide;
	
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockBoneSlab_top" );
        m_IconBoneSide = register.registerIcon( "fcBlockBoneSlab_side" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iSide >= 2 )
		{
			return m_IconBoneSide;
		}
		
    	return blockIcon;
    }
}
