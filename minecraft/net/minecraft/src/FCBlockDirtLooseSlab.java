// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockDirtLooseSlab extends FCBlockSlabFalling
{
    public FCBlockDirtLooseSlab( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( 0.5F );
        SetShovelsEffectiveOn( true );        
        
        setStepSound( soundGravelFootstep );
        
        setUnlocalizedName( "fcBlockDirtLooseSlab" );
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 3, 0, fChanceOfDrop );
		
		return true;
	}
    
	@Override
    public boolean GetCanGrassSpreadToBlock( World world, int i, int j, int k )
    {
        Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
        
        if ( blockAbove == null || blockAbove.GetCanGrassGrowUnderBlock( world, i, j + 1, k, true ) )
        {
        	return true;
        }
    	
    	return false;
    }
    
	@Override
    public boolean SpreadGrassToBlock( World world, int i, int j, int k )
    {
		world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtSlab.blockID, ( FCBlockDirtSlab.m_iSubtypeGrass ) << 1 );
        
    	return true;
    }
    
	@Override
    public boolean GetCanMyceliumSpreadToBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean SpreadMyceliumToBlock( World world, int i, int j, int k )
    {
        world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockMyceliumSlab.blockID );
        
    	return true;
    }
    
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockDirtLoose.blockID;
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
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
	
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockDirtLoose" );
    }
}