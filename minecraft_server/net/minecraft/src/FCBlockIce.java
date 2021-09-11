// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockIce extends BlockIce
{
    public FCBlockIce( int iBlockID )
    {
    	super ( iBlockID );
    	
    	SetPicksEffectiveOn();
    }
    
    @Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        if ( !world.provider.isHellWorld )
        {
	        if ( IsNonSourceIceFromMetadata( iMetadata) && ( !canSilkHarvest() || !EnchantmentHelper.getSilkTouchModifier( player ) ) )
	        {
	        	// HCB ice turns into non persistant water on harvest
	        	
	            player.addExhaustion( 0.025F );		            
	            player.addStat( StatList.mineBlockStatArray[blockID], 1 );

				FCUtilsMisc.PlaceNonPersistantWaterMinorSpread( world, i, j, k );
	        	
	        	return;
	        }
        }
        
        super.harvestBlock( world, player, i, j, k, iMetadata );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
        if (world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) > 11 - Block.lightOpacity[blockID])
        {
        	Melt( world, i, j, k );
        }
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        if ( world.provider.isHellWorld )
        {
        	// melt ice instantly in the nether
        	
        	world.setBlockWithNotify( i, j, k, 0 );
        	
            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
            	"random.fizz", 0.5F, 2.6F + ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.8F );
            
            for ( int l = 0; l < 8; l++ )
            {
                world.spawnParticle( "largesmoke", (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D );
            }
        }
        else 
        {            
        	int iBlockAboveID = world.getBlockId( i, j + 1, k );
        	
            if ( !FCUtilsMisc.IsIKInColdBiome( world, i, k ) ||
				( iBlockAboveID != blockID && !world.canBlockSeeTheSky( i, j + 1, k ) ) )
            {            	
            	world.setBlockWithNotify( i, j, k, 0 );
            	
    			FCUtilsMisc.PlaceNonPersistantWaterMinorSpread( world, i, j, k );
            }            
        }
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 1.0F;
    }
    
    @Override
    public int AdjustMetadataForPistonMove( int iMetadata )
    {
    	// flag pushed ice as non-source
    	
    	return iMetadata |= 8;
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return bIgnoreTransparency;
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
    
    //------------- Class Specific Methods ------------//    
    
    public boolean IsNonSourceIce( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return IsNonSourceIceFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );    
	}
    
    public void SetIsNonSourceIce( World world, int i, int j, int k, boolean bNonSource )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~8); // filter out old state
    	
    	if ( bNonSource )
    	{
            iMetadata |= 8;
    	}
    	
		world.setBlockMetadata( i, j, k, iMetadata );
    }
    
    public boolean IsNonSourceIceFromMetadata( int iMetadata )
    {
    	return ( iMetadata & 8 ) > 0;    
    }
    
	private void Melt( World world, int i, int j, int k )
	{
    	if ( IsNonSourceIce( world, i, j, k ) )
    	{
    		FCUtilsMisc.PlaceNonPersistantWaterMinorSpread( world, i, j, k );
    	}
    	else
    	{
            world.setBlockWithNotify( i, j, k, Block.waterMoving.blockID );
    	}
	}
	
	//----------- Client Side Functionality -----------//
}
