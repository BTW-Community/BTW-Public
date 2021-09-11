// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSoulforgedSteel extends Block
{
	private static final float m_fBlockHardness = 100F;
	private static final float m_fBlockExplosionResistance = 2000F;
	
	private static final int m_iTickRate = 4;
	
	private static final float m_iStrongholdActivationDistance = 64F;
	private static final float m_iStrongholdActivationDistanceSq = ( m_iStrongholdActivationDistance * m_iStrongholdActivationDistance );
	
	protected FCBlockSoulforgedSteel( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialSoulforgedSteel );
        
        setHardness( m_fBlockHardness );
        setResistance( m_fBlockExplosionResistance );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "fcBlockSoulforgedSteel" );
        
        setCreativeTab( CreativeTabs.tabBlock );
	}

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }
	
	@Override
    public int getMobilityFlag()
    {
		// blocks pushing (like obsidian)
		
        return 2;
    }
	
	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
    }

	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iChangedBlockID )
    {
    	if ( IsRedstonePowerFlagOn( world, i, j, k ) != 
    		IsReceivingRedstonePower( world, i, j, k ) ||
    		IsActivatedByWaterFlagOn( world, i, j, k ) != 
			IsBlockNeighboringOnWater( world, i, j, k ) )
    	{
			if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
			{
				world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
			}
    	}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		boolean bPowered = IsReceivingRedstonePower( world, i, j, k );
		
    	if ( IsRedstonePowerFlagOn( world, i, j, k ) !=  bPowered )
    	{
    		SetRedstonePowerFlag( world, i, j, k, bPowered );
    		
    		if ( bPowered )
    		{
                world.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, i, j, k, 1 );            
    		}
    	}
    	
    	boolean bIsNeighboringOnWater = IsBlockNeighboringOnWater( world, i, j, k );
    	
    	if ( IsActivatedByWaterFlagOn( world, i, j, k ) != bIsNeighboringOnWater )
    	{
    		SetActivatedByWaterFlag( world, i, j, k, bIsNeighboringOnWater );
    		
    		if ( bIsNeighboringOnWater )
    		{
    	        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
    	        		"random.drink", 0.25F + world.rand.nextFloat() * 0.25F, world.rand.nextFloat() * 0.75F + 0.25F );
    	        
    	        if ( GetStrongholdIndexWithinActivationRange( world, i, j, k ) == 0 )
    	        {
    	            world.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID, i, j, k, 0 );            
    	        }
    		}
    	}
    }
	
    //------------- Class Specific Methods ------------//
    
	public boolean IsRedstonePowerFlagOn( IBlockAccess blockAccess, int i, int j, int k )
	{
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
        return ( ( iMetadata & 1 ) > 0 );
	}    
	
	private void SetRedstonePowerFlag( World world, int i, int j, int k, boolean bPowerFlag )
	{
        int iMetadata = world.getBlockMetadata( i, j, k ) & (~1); // filter out old state
        
        if ( bPowerFlag )
        {
        	iMetadata = iMetadata | 1;
        }
        
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );        
	}
	
	private boolean IsReceivingRedstonePower( IBlockAccess blockAccess, int i, int j, int k )
	{
		// this block requires a redstone wire running directly over it to be powered
		
		int iBlockAboveID = blockAccess.getBlockId( i, j + 1, k );
		
		if ( iBlockAboveID == Block.redstoneWire.blockID )
		{
			return ( blockAccess.getBlockMetadata( i, j+ 1, k ) > 0 );
		}
		
		return false;
	}
	
    private void EmitPoweredParticles( World world, int i, int j, int k, Random random )
    {
        for ( int counter = 0; counter < 10; counter++ )
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            
            world.spawnParticle( "largesmoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D );
        }
    }

	public boolean IsActivatedByWaterFlagOn( IBlockAccess blockAccess, int i, int j, int k )
	{
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
        return ( ( iMetadata & 2 ) > 0 );
	}
	
	private void SetActivatedByWaterFlag( World world, int i, int j, int k, boolean bActivatedByWaterFlag )
	{
        int iMetadata = world.getBlockMetadata( i, j, k ) & (~2); // filter out old state
        
        if ( bActivatedByWaterFlag )
        {
        	iMetadata = iMetadata | 2;
        }
        
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );        
	}
	
	public boolean IsRecentlyActivatedFlagOn( IBlockAccess blockAccess, int i, int j, int k )
	{
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
        return ( ( iMetadata & 4 ) > 0 );
	}
	
	private void SetRecentlyActivatedFlag( World world, int i, int j, int k, boolean bActivatedByWaterFlag )
	{
        int iMetadata = world.getBlockMetadata( i, j, k ) & (~4); // filter out old state
        
        if ( bActivatedByWaterFlag )
        {
        	iMetadata = iMetadata | 4;
        }
        
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );        
	}
	
	private boolean IsBlockNeighboringOnWater( World world, int i, int j, int k )
	{
		return IsWaterBlock( world, i + 1, j, k ) ||
			IsWaterBlock( world, i - 1, j, k ) ||
			IsWaterBlock( world, i, j + 1, k ) ||
			IsWaterBlock( world, i, j - 1, k ) ||
			IsWaterBlock( world, i, j, k + 1 ) ||
			IsWaterBlock( world, i, j, k - 1 );
	}
	
	private boolean IsWaterBlock( World world, int i, int j, int k )
	{
		int iBlockID = world.getBlockId( i, j, k );
		
		return iBlockID == Block.waterMoving.blockID || iBlockID == Block.waterStill.blockID;
	}
	
	/*
	 * Returns the index of the stronghold if within range, -1 otherwise
	 * FCTODO: Optimize this.  It's a mass of instanceof calls at present.
	 */
	public int GetStrongholdIndexWithinActivationRange( World world, int i, int j, int k )
	{
		/*
		if ( world.provider.worldType == 0 ) // verify overworld
		{
			// this isn't just a distance check.  This call is required to "prime" the stronghold locations
            ChunkPosition chunkposition = world.findClosestStructure( "Stronghold", i, j, k );

            if (chunkposition != null)
            {
    			float fDeltaX = (float)( chunkposition.x - i );
    			float fDeltaZ = (float)( chunkposition.z - k );
    			
    			float fDistSq = ( fDeltaX * fDeltaX ) + ( fDeltaZ * fDeltaZ );
    			
    			if ( fDistSq <= m_iStrongholdActivationDistanceSq )
    			{
		        	if ( world.getChunkProvider() instanceof ChunkProvider )
		        	{
		        		ChunkProvider provider = (ChunkProvider)world.getChunkProvider();
		        		
		            	if ( provider.chunkProvider instanceof ChunkProviderGenerate )
		            	{
			        		ChunkProviderGenerate providerGenerate = (ChunkProviderGenerate)provider.chunkProvider;
			        		
			        		for ( int iStrongholdIndex = 0; iStrongholdIndex < 3; iStrongholdIndex++ )
			        		{
			        			if ( providerGenerate.strongholdGenerator.structureCoords[iStrongholdIndex] != null )
			        			{
				        			int iTempX = providerGenerate.strongholdGenerator.structureCoords[iStrongholdIndex].getCenterXPos();
				        			int iTempZ = providerGenerate.strongholdGenerator.structureCoords[iStrongholdIndex].getCenterZPosition();
				        			
				        			float fTempDeltaX = (float)( iTempX - i );
				        			float fTempDeltaZ = (float)( iTempZ - k );
				        			
				        			float fTempDistSq = ( fTempDeltaX * fTempDeltaX ) + ( fTempDeltaZ * fTempDeltaZ );
				        			
				        			if ( fTempDistSq <= m_iStrongholdActivationDistanceSq )
				        			{
				        				return iStrongholdIndex;
				        			}
			        			}
			        		}
		                }
	            	}
    			}
        	}
		}
		*/
		
		return -1;
	}
	
	/*
    // RTHMOD
    private void EmitPortalParticles( World world, int i, int j, int k, Random random )
    {
        for ( int counter = 0; counter < 1; counter++ )
        {
	        double d = (float)i + random.nextFloat();
	        double d2 = (double)j + maxY;
	        double d4 = (float)k + random.nextFloat();
	        world.spawnParticle("lava", d, d2, d4, 0.0D, 0.0D, 0.0D);
        }
    }
    
    private void CheckForHomePortalCreation( World world, int i, int j, int k )
    {
    	// check for portal creation at each of the spots this block might be part of a portal at
    	// this is very brute force and may require optimization
    	
    	FCBlockHomePortal portalBlock = (FCBlockHomePortal)(mod_FCBetterThanWolves.fcHomePortal);
    	
    	for ( int tempI = i - 2; tempI <= i + 2; tempI++ )
    	{
        	for ( int tempK = k - 2; tempK <= k + 2; tempK++ )
        	{
        		if ( tempI == i + 2 || tempI == i - 2 || tempK == k + 2 || tempK == k - 2 )
        		{
        			for ( int tempJ = j - 2; tempJ <= j + 2; tempJ += 4 )
        			{
	        			if ( portalBlock.IsValidSpotForHomePortalCreation( world, tempI, tempJ, tempK ) )
						{
	        				portalBlock.CreateHomePortalCentredOn( world, tempI, tempJ, tempK );
	        				
	        				return;
						}
        			}
        		}
        	}
    	}    	
    }
    
    private void NotifyAttachedPortalsOfStateChange( World world, int i, int j, int k )
    {
    	FCBlockHomePortal portalBlock = (FCBlockHomePortal)(mod_FCBetterThanWolves.fcHomePortal);
    	
    	for ( int tempI = i - 2; tempI <= i + 2; tempI++ )
    	{
        	for ( int tempK = k - 2; tempK <= k + 2; tempK++ )
        	{
        		if ( tempI == i + 2 || tempI == i - 2 || tempK == k + 2 || tempK == k - 2 )
        		{
        			for ( int tempJ = j - 2; tempJ <= j + 2; tempJ += 4 )
        			{
	        			if ( world.getBlockId( tempI, tempJ, tempK ) == portalBlock.blockID )
						{
	        				if ( portalBlock.IsMaster( world, tempI, tempJ, tempK ) )
	        				{
	        					portalBlock.onNeighborBlockChange( world, tempI, tempJ, tempK, blockID );
	        				}
	        				
	        				return;
						}
        			}
        		}
        	}
    	}    	
    }
    
    public boolean IsValidBlockForPortalActivation( World world, int i, int j, int k )
    {
    	if ( world.getBlockId( i, j, k ) != blockID ||
			!IsRedstonePowerFlagOn( world, i, j, k ) ||
			IsPartOfHomePortalFlagOn( world, i, j, k ) )
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean IsValidPortalActivatedBlock( World world, int i, int j, int k )
    {
    	if ( world.getBlockId( i, j, k ) != blockID ||
			!IsRedstonePowerFlagOn( world, i, j, k ) ||
			!IsPartOfHomePortalFlagOn( world, i, j, k ) )
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    public void SetSteelStatusForPortalPlate( World world, int i, int j, int k, boolean bPartOfPortal )
    {
    	for ( int tempI = i - 2; tempI <= i + 2; tempI++ )
    	{
        	for ( int tempK = k - 2; tempK <= k + 2; tempK++ )
        	{
        		if ( tempI == i + 2 || tempI == i - 2 || tempK == k + 2 || tempK == k - 2 )
        		{
        	    	if ( world.getBlockId( tempI, j, tempK ) == blockID )
        	    	{
        	    		SetPartOfHomePortalFlag( world, tempI, j, tempK, bPartOfPortal );
        	    	}
        		}
        	}
    	}
	}
   	// END RTHMOD
   	*/
	
    //----------- Client Side Functionality -----------//
}