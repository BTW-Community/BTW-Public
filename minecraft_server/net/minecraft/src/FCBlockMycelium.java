// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockMycelium extends BlockMycelium
{
    public static final int m_iMyceliumSurviveMinimumLightLevel = 4;
    public static final int m_iMyceliumSpreadToMinimumLightLevel = 4;
	
    protected FCBlockMycelium( int iBlockID )
    {
        super( iBlockID );
        
        setHardness( 0.6F );
        SetShovelsEffectiveOn();
    	SetHoesEffectiveOn();
        
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "mycel" );        
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	int iBlockAboveID = world.getBlockId( i, j + 1, k );
    	Block blockAbove = Block.blocksList[iBlockAboveID];
    	
        if ( world.getBlockLightValue( i, j + 1, k ) < m_iMyceliumSurviveMinimumLightLevel || 
        	Block.lightOpacity[iBlockAboveID] > 2 ||
        	( blockAbove != null && blockAbove.HasLargeCenterHardPointToFacing( world, i, j + 1, k, 0 ) ) )
        	
        {
            world.setBlockWithNotify( i, j, k, Block.dirt.blockID );
        }
    	else
    	{
        	CheckForMyceliumSpreadFromLocation( world, i, j, k );
    	}
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
    	// only disrupt grass/mycelium when block below is dug out
    	
		if ( iToFacing == 0 )
		{
			world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
		}    		
    }
    
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
    	return animal.CanGrazeMycelium();
    }
    
    @Override
    public void OnGrazed( World world, int i, int j, int k, EntityAnimal animal )
    {
        if ( !animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, Block.dirt.blockID );
        }
        else
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
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
    public boolean GetCanBlightSpreadToBlock( World world, int i, int j, int k, int iBlightLevel )
    {
    	return iBlightLevel >= 2;
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
	
	public static void CheckForMyceliumSpreadFromLocation( World world, int i, int j, int k )
	{
    	if ( world.provider.dimensionId != 1 )
    	{
	        if ( world.getBlockLightValue( i, j + 1, k ) >= 9 &&
	        	!FCBlockGroundCover.IsGroundCoverRestingOnBlock( world, i, j, k ) )
	        {
	            for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
	            {
	            	CheckForMyceliumSpreadToRandomBlockAround( world, i, j, k );
	            }
	        }
    	}
	}
	
	public static void CheckForMyceliumSpreadToRandomBlockAround( World world, int i, int j, int k )
	{
        int iTargetI = i + world.rand.nextInt( 3 ) - 1;
        int iTargetJ = j + world.rand.nextInt( 5 ) - 3;
        int iTargetK = k + world.rand.nextInt( 3 ) - 1;

        Block targetBlock = Block.blocksList[world.getBlockId( 
        	iTargetI, iTargetJ, iTargetK )];
        
        if ( targetBlock != null )
        {
        	targetBlock.AttempToSpreadMyceliumToBlock( world, 
        		iTargetI, iTargetJ, iTargetK );
        }
	}
	
	//----------- Client Side Functionality -----------//
}