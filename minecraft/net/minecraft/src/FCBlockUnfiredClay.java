// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockUnfiredClay extends Block
{
    public FCBlockUnfiredClay( int iBlockID )
    {
        super( iBlockID, Material.clay );
        
        setHardness( 0.6F );
        SetShovelsEffectiveOn();
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setUnlocalizedName( "fcBlockUnfiredClay" );
        
        setCreativeTab( CreativeTabs.tabBlock );
    }

    @Override
    public int idDropped( int iMetaData, Random rand, int iFortuneModifier )
    {
        return Item.clay.itemID;
    }

    @Override
    public int quantityDropped( Random rand )
    {
        return 9;
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
		return FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true );
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
		// check beneath for valid block due to piston pushing not sending a notify
		
		if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
		{
	        dropBlockAsItem(world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
	        world.setBlockWithNotify(i, j, k, 0);
		}
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
    	{
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify(i, j, k, 0);
    	}
    }
    
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public void OnRotatedOnTurntable( World world, int i, int j, int k )
	{
		world.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, i, j, k, blockID );
	}	
	
	@Override
	public int RotateOnTurntable( World world, int i, int j, int k, boolean bReverse, int iCraftingCounter )
	{
		iCraftingCounter = super.RotateOnTurntable( world, i, j, k, bReverse, iCraftingCounter );
		
		iCraftingCounter = TurntableCraftingRotation( world, i, j, k, bReverse, iCraftingCounter );
		
		return iCraftingCounter;
	}
	
	@Override
	public int GetRotationsToCraftOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return FCBlockUnfiredPottery.m_iRotationsOnTurntableToChangState;
	}
	
	@Override
	public int GetNewBlockIDCraftedOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return FCBetterThanWolves.fcUnfiredPottery.blockID;
	}
	
	@Override
	public int GetNewMetadataCraftedOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return FCBlockUnfiredPottery.m_iSubtypeCrucible;
	}
	
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    //------------- Class Specific Methods ------------//    
	
	//----------- Client Side Functionality -----------//
}
