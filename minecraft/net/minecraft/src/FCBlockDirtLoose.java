// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockDirtLoose extends FCBlockFallingFullBlock
{
    public FCBlockDirtLoose( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( 0.5F );        
        SetShovelsEffectiveOn();
    	SetHoesEffectiveOn();
        
        setStepSound( soundGravelFootstep );
        
        setUnlocalizedName("fcBlockDirtLoose");        
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 6, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public boolean GetCanGrassSpreadToBlock( World world, int i, int j, int k )
    {
        Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
        
        if ( blockAbove == null || blockAbove.GetCanGrassGrowUnderBlock( world, i, j + 1, k, false ) ) 
        {            
        	return true;
        }
    	
    	return false;
    }
    
	@Override
    public boolean SpreadGrassToBlock( World world, int i, int j, int k )
    {
        world.setBlockWithNotify( i, j, k, Block.grass.blockID );
        
    	return true;
    }
    
	@Override
    public boolean GetCanMyceliumSpreadToBlock( World world, int i, int j, int k )
    {
		return !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j + 1, k, 0 );
    }
    
	@Override
    public boolean SpreadMyceliumToBlock( World world, int i, int j, int k )
    {
        world.setBlockWithNotify( i, j, k, Block.mycelium.blockID );
        
    	return true;
    }
    
	@Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
	@Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 2;
    }
    
	@Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockAestheticOpaqueEarth.blockID;
    }
    
	@Override
    public int GetResultingBlockMetadataOnPistonPack( ItemStack stack )
    {
    	return FCBlockAestheticOpaqueEarth.m_iSubtypePackedEarth;
    }
    
	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    @Override
	public void OnVegetationAboveGrazed( World world, int i, int j, int k, EntityAnimal animal )
	{
        if ( animal.GetDisruptsEarthOnGraze() )
        {
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
	}
    
	@Override
    public boolean CanReedsGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanSaplingsGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanWildVegetationGrowOnBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean GetCanBlightSpreadToBlock( World world, int i, int j, int k, int iBlightLevel )
    {
		return true;
    }
	
	@Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return stack != null && stack.getItem() instanceof FCItemHoe;
    }
	
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockFarmland.blockID );

    	if ( !world.isRemote )
		{
            world.playAuxSFX( 2001, i, j, k, blockID ); // block break FX
		}
    	
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//    
}