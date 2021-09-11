// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockClay extends Block
{
    public FCBlockClay( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialNaturalClay );
        
        SetShovelsEffectiveOn();
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setCreativeTab( null ); // weirdness due to use of item as legacy clay
    }

    @Override
    public int idDropped( int iMetaData, Random rand, int iFortuneModifier )
    {
        return Item.clay.itemID;
    }

	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetaData, float fChance, int iFortuneModifier )
    {
        super.dropBlockAsItemWithChance(world, i, j, k, iMetaData, fChance, iFortuneModifier );
        
        if ( !world.isRemote )
        {
    		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 6, 0, fChance );
        }
    }

	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChance )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileClay.itemID, 1, 0, fChance );
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 4, 0, fChance );
		
		return true;
	}
	
	@Override
    protected boolean canSilkHarvest()
    {
		// can't silk harvest due to conflicting drops and conversion recipes old clay blocks
		
        return false;
    }
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    //------------- Class Specific Methods ------------//    
	
	//----------- Client Side Functionality -----------//
	
    protected Icon m_IconNaturalClay;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_IconNaturalClay = register.registerIcon( "fcBlockClay" );
    }
	
	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	return m_IconNaturalClay;
    }
}
