// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBloodWood extends Block
{
    private final static float m_fHardness = 2F;
    
    public FCBlockBloodWood( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );
        
        setHardness( m_fHardness );        
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
    	SetFurnaceBurnTime( 4 * FCEnumFurnaceBurnTime.PLANKS_BLOOD.m_iBurnTime );
		SetFireProperties( FCEnumFlammability.EXTREME );
        
		SetCanBeCookedByKiln( true );
		SetItemIndexDroppedWhenCookedByKiln( Item.coal.itemID );
		SetItemDamageDroppedWhenCookedByKiln( 1 ); // charcoal
		
        setTickRandomly( true );
		
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setUnlocalizedName( "fcBlockBloodWood" );
        
		setCreativeTab( CreativeTabs.tabBlock );
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
	{
		iMetadata = SetFacing( iMetadata, iFacing );
		
		return iMetadata;
	}
	
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        world.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, i, j, k, 0 );            
        
        NotifySurroundingBloodLeavesOfBlockRemoval( world, i, j, k );
        
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }

	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		if ( GetCanGrow( world, i, j, k ) )
		{
			int iGrowthDirection = GetFacing( world, i, j, k );
			
			if ( iGrowthDirection != 0 )
			{
		        // verify if we're in the nether
		        
		        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
		        
		        if ( worldchunkmanager != null )
		        {
		            BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt( i, k );
		            
		            if ( biomegenbase instanceof BiomeGenHell )
		            {
		    			Grow( world, i, j, k, random );
		            }
		        }				
			}
			
			// A block of Blood Wood will only attempt to grow once, then stop
			
			SetCanGrow( world, i, j, k, false );
		}
    }

	@Override
    public boolean OnBlockSawed( World world, int i, int j, int k )
    {
		super.OnBlockSawed( world, i, j, k );
		
		for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcItemSoulDust.itemID, 0 );
		}
		
		FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
			FCBetterThanWolves.fcItemBark.itemID, 4 );
		
		return true;
    }
    
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
    	return Block.planks.blockID;
    }

	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
    	return 4;
    }
    
	@Override
    public int GetItemDamageDroppedOnSaw( World world, int i, int j, int k )
    {
    	return 4;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 4, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemBark.itemID, 1, 4, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSoulDust.itemID, 1, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
	public int GetFacing( int iMetadata )
	{
		return iMetadata & 7;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= (~7); // filter out old state
		
		iMetadata |= iFacing;
		
		return iMetadata;
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
		return true;
	}
	
    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 8;
    }
    
    //------------- Class Specific Methods ------------//
	
	public boolean GetCanGrow( IBlockAccess blockAccess, int i, int j, int k )
	{
    	return ( blockAccess.getBlockMetadata( i, j, k ) & 8 ) > 0;
	}
    
	public void SetCanGrow( World world, int i, int j, int k, boolean bCanGrow )
	{
    	int iMetaData = world.getBlockMetadata( i, j, k ) & (~8); // filter out old direction
    	
    	if ( bCanGrow )
    	{
    		iMetaData |= 8;
    	}
    	
    	world.setBlockMetadata( i, j, k, iMetaData );
	}
    
    public void Grow( World world, int i, int j, int k, Random random )
    {
    	if ( CountBloodWoodNeighboringOnBlockWithSoulSand( world, i, j, k ) >= 2 )
    	{
    		// too much neighboring wood to grow further
    		
    		return;
    	}
    	
    	int iGrowthDirection = GetFacing( world, i, j, k );
    	
    	if ( iGrowthDirection == 1 )
    	{
    		// trunk growth
    		
    		int iRandomFactor = random.nextInt( 100 );
    		
    		if ( iRandomFactor < 25 )
    		{
    			// just continue growing upwards
    			
    			AttemptToGrowIntoBlock( world, i, j + 1, k, 1 );
    		}
    		else if ( iRandomFactor < 90 )
    		{
    			// split and grow upwards
    			
				int iTargetFacing = random.nextInt( 4 ) + 2;
				
	    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
	    		
	    		targetPos.AddFacingAsOffset( iTargetFacing );
	    		
	    		AttemptToGrowIntoBlock( world, targetPos.i, targetPos.j, targetPos.k, iTargetFacing );
	    		
    			AttemptToGrowIntoBlock( world, i, j + 1, k, 1 );    			
    		}
    		else
    		{
    			// split
    			
    			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
    			{
    				int iTargetFacing = random.nextInt( 4 ) + 2;
    				
    	    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	    		
    	    		targetPos.AddFacingAsOffset( iTargetFacing );
    	    		
    	    		AttemptToGrowIntoBlock( world, targetPos.i, targetPos.j, targetPos.k, iTargetFacing );	    	    		
    			}
    		}
    	}
    	else
    	{
    		// branch growth
    		
    		int iRandomFactor = random.nextInt( 100 );
    		
    		if ( iRandomFactor < 40 )
    		{
    			// grow upwards
    			
    			AttemptToGrowIntoBlock( world, i, j + 1, k, iGrowthDirection );
    			
    			// reorient existing block so that it looks right
    			
    			SetFacing( world, i, j, k, 1 );
    		}
    		else if ( iRandomFactor < 65 )
    		{
    			// grow in the growth direction
    			
	    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
	    		
	    		targetPos.AddFacingAsOffset( iGrowthDirection );
	    		
	    		AttemptToGrowIntoBlock( world, targetPos.i, targetPos.j, targetPos.k, iGrowthDirection );	    	    		
    		}
    		else if ( iRandomFactor < 90 )
    		{
    			// split and keep going
    			
				int iTargetFacing = random.nextInt( 4 ) + 2;
				
				if ( iTargetFacing == iGrowthDirection )
				{
					iTargetFacing = 1;
				}
				
	    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
	    		
	    		targetPos.AddFacingAsOffset( iTargetFacing );
	    		
	    		int iTargetGrowthDirection = iGrowthDirection;
	    		
	    		if ( iTargetFacing >= 2 )
	    		{
	    			iTargetGrowthDirection = iTargetFacing;
	    		}
	    		
	    		AttemptToGrowIntoBlock( world, targetPos.i, targetPos.j, targetPos.k, iTargetGrowthDirection );
	    		
	    		targetPos = new FCUtilsBlockPos( i, j, k );
	    		
	    		targetPos.AddFacingAsOffset( iGrowthDirection );
	    		
	    		if ( !AttemptToGrowIntoBlock( world, targetPos.i, targetPos.j, targetPos.k, iGrowthDirection ) && iTargetFacing == 1 )
	    		{
	    			// reorient existing block so that it looks right
	    			
	    			SetFacing( world, i, j, k, 1 );
	    		}
    		}
    		else
    		{
    			// split
    			
    			int iGrowthDirections[] = new int[2];
    			
    			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
    			{
    				iGrowthDirections[iTempCount] = 0;
    				
    				int iTargetFacing = random.nextInt( 4 ) + 2;
    				
    				if ( iTargetFacing == iGrowthDirection )
    				{
    					iTargetFacing = 1;
    				}
    				
    	    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	    		
    	    		targetPos.AddFacingAsOffset( iTargetFacing );
    	    		
    	    		int iTargetGrowthDirection = iGrowthDirection;
    	    		
    	    		if ( iTargetFacing >= 2 )
    	    		{
    	    			iTargetGrowthDirection = iTargetFacing;
    	    		}
    	    		
    	    		if ( AttemptToGrowIntoBlock( world, targetPos.i, targetPos.j, targetPos.k, iTargetGrowthDirection ) )
    	    		{
    	    			iGrowthDirections[iTempCount] = iTargetFacing;
    	    		}
    			}
    			
    			if ( ( iGrowthDirections[0] == 1 && iGrowthDirections[1] <= 1 ) || ( iGrowthDirections[1] == 1 && iGrowthDirections[0] == 0 ) ) 
    			{
	    			// reorient existing block so that it looks right
	    			
	    			SetFacing( world, i, j, k, 1 );
    			}
    		}
    	}
    }
    
    public boolean AttemptToGrowIntoBlock( World world, int i, int j, int k, int iGrowthDirection )
    {	    	
    	if ( ( !( world.isAirBlock( i, j, k ) ) &&  !IsBloodLeafBlock( world, i, j, k ) ) || 
			CountBloodWoodNeighboringOnBlockWithSoulSand( world, i, j, k ) >= 2 )
    	{
    		// not empty, or too much neighboring wood to grow further
    		
    		return false;
    	}
    	
    	world.setBlockAndMetadataWithNotify( i, j, k, blockID, iGrowthDirection | 8 );
    	
		GrowLeaves( world, i, j, k );
		
    	return true;
    }
    
    public void GrowLeaves( World world, int i, int j, int k )
    {
    	for ( int tempI = i - 1; tempI <= i + 1; tempI++ )
    	{
	    	for ( int tempJ = j - 1; tempJ <= j + 1; tempJ++ )
	    	{
		    	for ( int tempK = k - 1; tempK <= k + 1; tempK++ )
		    	{
		    		if ( world.isAirBlock( tempI, tempJ, tempK ) )
		    		{
	    				world.setBlockAndMetadataWithNotify( tempI, tempJ, tempK, 
    						FCBetterThanWolves.fcBlockBloodLeaves.blockID, 0 );
		    		}
		    	}
	    	}
    	}
    }
    
    public boolean IsBloodLeafBlock( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	
    	if ( iBlockID == FCBetterThanWolves.fcBlockBloodLeaves.blockID )
    	{
    		return true;
    	}
    	else if ( iBlockID == FCBetterThanWolves.fcAestheticVegetation.blockID )
    	{
    		int iSubType = world.getBlockMetadata( i, j, k );
    		
    		if ( iSubType == FCBlockAestheticVegetation.m_iSubtypeBloodLeaves )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public int CountBloodWoodNeighboringOnBlockWithSoulSand( World world, int i, int j, int k )
    {
    	int iNeighborWoodCount = 0;
    	
    	for ( int iTempFacing = 0; iTempFacing < 6; iTempFacing++ )
    	{
    		FCUtilsBlockPos tempTargetPos = new FCUtilsBlockPos( i, j, k );
    		
    		tempTargetPos.AddFacingAsOffset( iTempFacing );
    		
    		if ( world.getBlockId( tempTargetPos.i, tempTargetPos.j, tempTargetPos.k ) == blockID )
    		{
    			iNeighborWoodCount++;
    		}
    	}
    	
    	if ( world.getBlockId( i, j - 1, k ) == Block.slowSand.blockID )
    	{
    		iNeighborWoodCount++;
    	}
    	
    	return iNeighborWoodCount;
    }
    
    public int CountBloodWoodNeighboringOnBlockIncludingDiagnals( World world, int i, int j, int k )
    {
    	int iNeighborWoodCount = 0;
    	
    	for ( int tempI = i - 1; tempI <= i + 1; tempI++ )
    	{
	    	for ( int tempJ = j - 1; tempJ <= j + 1; tempJ++ )
	    	{
		    	for ( int tempK = k - 1; tempK <= k + 1; tempK++ )
		    	{
		    		if ( world.getBlockId( tempI, tempJ, tempK ) == blockID )
		    		{
		    			if ( tempI != i || tempJ != j || tempK != k )
		    			{
		    				iNeighborWoodCount++;
		    			}
		    		}
		    	}
	    	}
    	}
    	
    	return iNeighborWoodCount;
    }
    
    public void NotifySurroundingBloodLeavesOfBlockRemoval( World world, int i, int j, int k )
    {
    	// copied over from BlockLog breakBlock() with minor mods for blood leaves
        byte byte0 = 4;
        int l = byte0 + 1;
        if(world.checkChunksExist(i - l, j - l, k - l, i + l, j + l, k + l))
        {
            for(int i1 = -byte0; i1 <= byte0; i1++)
            {
                for(int j1 = -byte0; j1 <= byte0; j1++)
                {
                    for(int k1 = -byte0; k1 <= byte0; k1++)
                    {
                        int l1 = world.getBlockId(i + i1, j + j1, k + k1);
                        if(l1 != FCBetterThanWolves.fcBlockBloodLeaves.blockID)
                        {
                            continue;
                        }
                        int i2 = world.getBlockMetadata(i + i1, j + j1, k + k1);
                        if((i2 & 8) == 0)
                        {
                            world.setBlockMetadata(i + i1, j + j1, k + k1, i2 | 8);
                        }
                    }
                }
            }
        }
    }
    
	//----------- Client Side Functionality -----------//
}