// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSnowSolidSlab extends FCBlockSlab
{
    public FCBlockSnowSolidSlab( int iBlockID )
    {
        super( iBlockID, Material.craftedSnow );
        
        setHardness( FCBlockSnowSolid.m_fHardness );
        SetShovelsEffectiveOn();
        
    	SetBuoyant();
        
    	setStepSound( soundSnowFootstep );
        
        setUnlocalizedName( "fcBlockSnowSolidSlab" );
        
        setTickRandomly( true );
		
        setLightOpacity( 2 );
        Block.useNeighborBrightness[iBlockID] = true;
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return Item.snowball.itemID;
    }

    public int quantityDropped( Random rand )
    {
        return 4;
    }

    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        if ( world.provider.isHellWorld )
        {
        	// melt instantly in the nether
        	
        	world.setBlockToAir( i, j, k );
        	
            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
            	"random.fizz", 0.5F, 2.6F + ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.8F );
            
            for ( int iTempCount = 0; iTempCount < 8; iTempCount++ )
            {
                world.spawnParticle( "largesmoke", (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 
                	0D, 0D, 0D );
            }
        }
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( !FCUtilsMisc.IsIKInColdBiome( world, i, k ) )
		{
			if ( rand.nextInt( FCBlockSnowSolid.m_iChanceOfMelting ) == 0 )
			{
				ConvertToLooseSnow( world, i, j, k );
			}
		}
    }
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return FCBetterThanWolves.fcBlockSnowSolid.blockID;
	}
	
    @Override
	public boolean IsStickyToSnow( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 1F;
    }
    
    @Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean GetCanBeSetOnFireDirectlyByItem( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		Melt( world, i, j, k );
		
    	return true;
    }
    
    @Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 60; // same chance as leaves and other highly flammable objects
    }
    
	@Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//    
    
	private void ConvertToLooseSnow( World world, int i, int j, int k )
	{
		int iNewMetadata = FCBetterThanWolves.fcBlockSnowLooseSlab.SetIsUpsideDown( 0, GetIsUpsideDown( world, i, j, k ) );

		iNewMetadata = FCBetterThanWolves.fcBlockSnowLooseSlab.SetHardeningLevel( iNewMetadata, 7 );
		
		world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockSnowLooseSlab.blockID, iNewMetadata );
		
		FCBetterThanWolves.fcBlockSnowLooseSlab.ScheduleCheckForFall( world, i, j, k );
	}
	
	private void Melt( World world, int i, int j, int k )
	{
		FCUtilsMisc.PlaceNonPersistantWaterMinorSpread( world, i, j, k );
	}
	
	//----------- Client Side Functionality -----------//
}
