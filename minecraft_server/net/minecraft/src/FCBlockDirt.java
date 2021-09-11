// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockDirt extends FCBlockFullBlock
{
    protected FCBlockDirt( int iBlockID )
    {
        super( iBlockID, Material.ground );
        
        setHardness( 0.5F );
        SetShovelsEffectiveOn();
    	SetHoesEffectiveOn();
    	
    	setStepSound( soundGravelFootstep );
    	
    	setUnlocalizedName( "dirt" );
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockDirtLoose.blockID;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 6, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	super.OnBlockDestroyedWithImproperTool( world, player, i, j, k, iMetadata );
    	
    	OnDirtDugWithImproperTool( world, i, j, k );    	
    }
    
	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		super.onBlockDestroyedByExplosion( world, i, j, k, explosion );
		
    	OnDirtDugWithImproperTool( world, i, j, k );    	
    }
	
	@Override
    protected void OnNeighborDirtDugWithImproperTool( World world, int i, int j, int k, 
    	int iToFacing )
    {
		world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
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
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
	public void OnVegetationAboveGrazed( World world, int i, int j, int k, EntityAnimal animal )
	{
        if ( animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
        	
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
    	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );

    	if ( !world.isRemote )
		{
            world.playAuxSFX( 2001, i, j, k, blockID ); // block break FX
		}
    	
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
	
	//----------- Client Side Functionality -----------//
}