// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockScrewPump extends Block
	implements FCIBlockMechanical, FCIBlockFluidSource 
{
	static public final int m_iTickRate = 20;
	
    public FCBlockScrewPump( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );

        setHardness( 2F );
        setResistance( 5F );
    	
        SetAxesEffectiveOn( true );        
        SetBuoyancy( 1F );

		SetFireProperties( FCEnumFlammability.PLANKS );
        
        setStepSound( soundWoodFootstep );
        setUnlocalizedName( "fcBlockScrewPump" );
        
        setTickRandomly( true );
        
	    setCreativeTab( CreativeTabs.tabRedstone );
    }
    
	@Override
    public int tickRate( World world )
    {
    	return m_iTickRate;
    }

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	if ( iFacing < 2 )
    	{
    		iFacing = 2;
    	}
    	
        return SetFacing( iMetadata, iFacing );        
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack )
	{
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed( entityLiving );
		
		SetFacing( world, i, j, k, iFacing );
	}
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		boolean bIsJammed = IsJammed( world, i, j, k );
		
		if ( bIsJammed )
		{
    		FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( i, j, k );
    		
    		sourcePos.AddFacingAsOffset( GetFacing( world, i, j, k ) );
    		
    		int iSourceBlockID = world.getBlockId( sourcePos.i, sourcePos.j, sourcePos.k );
    		
    		if ( iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID )
    		{
    			// there is no longer any water at our input, so clear the jam
    			
    			SetIsJammed( world, i, j, k, false );
    		}
		}		
		
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = IsMechanicalOn( world, i, j, k );
    	
    	if ( bReceivingPower != bOn )
    	{
    		SetMechanicalOn( world, i, j, k, bReceivingPower );
    		
	        world.markBlockForUpdate( i, j, k );
	        
			if ( IsPumpingWater( world, i, j, k ) )
			{
    			// we just turned on, schedule another update to start pumping 
				// (to give the impression the water has time to travel up)
    			
    			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );    			
    		}
			
			if ( !bReceivingPower )
			{
				// clear any jams if we're turned off
				
				if ( IsJammed( world, i, j, k ) )
				{
	    			SetIsJammed( world, i, j, k, false );
				}				
			}
    	}
    	else
    	{
    		if ( bOn )
    		{
    			if ( IsPumpingWater( world, i, j, k ) )
    			{
    				boolean bSourceValidated = false;
    				
    	    		int iTargetBlockID = world.getBlockId( i, j + 1, k );
    	    		
    	    		if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID )
    	    		{
    	    			if ( OnNeighborChangeShortPumpSourceCheck( world, i, j, k ) )
    	    			{
	    	    			int iTargetHeight = world.getBlockMetadata( i, j + 1, k );
	    	    			
	    	    			if ( iTargetHeight > 1 && iTargetHeight < 8 )
	    	    			{
	    	    				// gradually increase the fluid height until it maxes at 1
	    	    				
	        	    			world.setBlockAndMetadataWithNotify( i, j + 1, k, Block.waterMoving.blockID, iTargetHeight - 1 );
	        	    			
	        	    			// schedule another update to increase it further
	        	    			
	        	    			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	    	    			}
    	    			}
    	    			else
    	    			{
	    					SetIsJammed( world, i, j, k, true );
    	    			}
    	    		}
    	    		else
    	    		{
    	    			// FCTODO: Break blocks here that water normally destroys
    	    			
    	    			if ( world.isAirBlock( i, j + 1, k ) )
    					{
    	    				if ( StartPumpSourceCheck( world, i, j, k ) )
    	    				{    	    				
		    	    			// start the water off at min height
		    	    			
		    	    			world.setBlockAndMetadataWithNotify( i, j + 1, k, Block.waterMoving.blockID, 7 );
		    	    			
		    	    			// schedule another update to increase it further
		    	    			
		    	    			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	    				}
    	    				else
    	    				{
    	    					SetIsJammed( world, i, j, k, true );
    	    				}
    					}
    	    		}
    			}
        		else
        		{
    	    		int iTargetBlockID = world.getBlockId( i, j + 1, k );
    	    		
    	    		if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID )
    	    		{
    	    			// if there is water above us, notify it that we are no longer pumping
    	    			
    	    			Block.blocksList[iTargetBlockID].onNeighborBlockChange( world, i, j + 1, k, blockID );
    	    		}
        		}
    		}
    	}
    }
	
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
    	boolean bWasJammed = IsJammed( world, i, j, k );
    	boolean bIsJammed = bWasJammed;
    	boolean bMechanicalOn = IsMechanicalOn( world, i, j, k );
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	
    	if ( bReceivingPower != bMechanicalOn )
    	{
			// verify we have a tick already scheduled to prevent jams on chunk load
			
			if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
			{
				world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
				
				return;
			}
    	}
    	
    	if ( bMechanicalOn )
    	{
			FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( i, j, k );
			
			sourcePos.AddFacingAsOffset( GetFacing( world, i, j, k ) );
			
			int iSourceBlockID = world.getBlockId( sourcePos.i, sourcePos.j, sourcePos.k );
			
			if ( iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID )
			{
				// there is no longer any water at our input, so clear any jams
		
				bIsJammed = false;
			}
			else
			{
				int iDistanceToCheck = GetRandomDistanceForSourceCheck( rand );
				
				bIsJammed = !FCUtilsMisc.DoesWaterHaveValidSource( world, sourcePos.i, sourcePos.j, sourcePos.k, iDistanceToCheck );
				
		    	if ( !bIsJammed && bWasJammed )
		    	{
					// schedule an update to start pumping again

	    			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		    	}		    		
			}
    	}
    	else
    	{
    		bIsJammed = false;
    	}
    	
    	if ( bWasJammed != bIsJammed )
    	{
			SetIsJammed( world, i, j, k, bIsJammed );			
    	}    	
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemScrew.itemID, 1, 0, fChanceOfDrop );		
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 4, 0, fChanceOfDrop );		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 4, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
	public int GetFacing( int iMetadata )
	{
    	return ( iMetadata & 3 ) + 2;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata &= ~3; // filter out old facing
    	
    	// this block only has 4 valid facings
    	
    	if ( iFacing >= 2 )
    	{
    		iFacing -= 2;
    	}
    	else
    	{
    		iFacing = 0;
    	}
    	
    	iMetadata |= iFacing;
    	
        return iMetadata;
	}
	
    @Override
    public boolean IsIncineratedInCrucible()
    {
    	return false;
    }
	
    //------------- FCIBlockMechanical -------------//
    
	@Override
    public boolean CanOutputMechanicalPower()
    {
    	return false;
    }

	@Override
    public boolean CanInputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean IsInputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return FCUtilsMechPower.IsBlockPoweredByAxleToSide( world, i, j, k, 0 );
    }    

	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		return iFacing == 0;
	}
	
	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakScrewPump( world, i, j, k );		
	}	
	
    //------------- FCIBlockFluidSource ------------//
	
	@Override
	public int IsSourceToFluidBlockAtFacing( World world, int i, int j, int k, int iFacing  )
	{
		if ( iFacing == 1 )
		{
			if ( IsPumpingWater( world, i, j, k ) )
			{
	    		int iTargetBlockID = world.getBlockId( i, j + 1, k );
	    		
	    		if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID )
	    		{
	    			int iSourceHeight = 0;
	    			
	    			int iTargetHeight = world.getBlockMetadata( i, j + 1, k );
	    			
	    			if ( iTargetHeight > 0 && iTargetHeight < 8 )
	    			{
	    				// return the fluid blocks height + 1 so that the pump can gradually increase the height of the water block above it
	    				
	    				iSourceHeight = iTargetHeight - 1; 
	    			}	    			
	    			
	    			return iSourceHeight;
	    		}	    		
			}
		}
		
		return -1;
	}
	
	//------------- Class Specific Methods ------------//
    
    public boolean IsMechanicalOn( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return ( blockAccess.getBlockMetadata( i, j, k ) & 4 ) > 0;    
	}
    
    public void SetMechanicalOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~4); // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetadata |= 4;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public boolean IsJammed( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return ( blockAccess.getBlockMetadata( i, j, k ) & 8 ) > 0;    
	}
    
    public void SetIsJammed( World world, int i, int j, int k, boolean bJammed )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~8); // filter out old on state
    	
    	if ( bJammed )
    	{
    		iMetadata |= 8;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public boolean IsPumpingWater( World world, int i, int j, int k )
    {
    	if ( IsMechanicalOn( world, i, j, k ) && !IsJammed( world, i, j, k ) )
    	{
    		FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( i, j, k );
    		
    		sourcePos.AddFacingAsOffset( GetFacing( world, i, j, k ) );
    		
    		int iSourceBlockID = world.getBlockId( sourcePos.i, sourcePos.j, sourcePos.k );
    		
    		if ( iSourceBlockID == Block.waterMoving.blockID || iSourceBlockID == Block.waterStill.blockID )
    		{
    			return true;
    		}    		
    	}
    	
    	return false;
    }
    
    private boolean StartPumpSourceCheck( World world, int i, int j, int k )
    {
    	// initial source check to prevent any dickery with getting pumps started with temporary water
    	
		FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( i, j, k );
		
		sourcePos.AddFacingAsOffset( GetFacing( world, i, j, k ) );
		
		int iSourceBlockID = world.getBlockId( sourcePos.i, sourcePos.j, sourcePos.k );
		
		if ( iSourceBlockID == Block.waterMoving.blockID || iSourceBlockID == Block.waterStill.blockID )
		{
			int iDistanceToCheck = 128;
			
			return FCUtilsMisc.DoesWaterHaveValidSource( world, sourcePos.i, sourcePos.j, sourcePos.k, iDistanceToCheck );			
		}
		
    	return false;
    }
    
    private boolean OnNeighborChangeShortPumpSourceCheck( World world, int i, int j, int k )
    {
    	// this test just checks for an immediate infinite loop with the pump itself
    	
		FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( i, j, k );
		
		sourcePos.AddFacingAsOffset( GetFacing( world, i, j, k ) );
		
		int iSourceBlockID = world.getBlockId( sourcePos.i, sourcePos.j, sourcePos.k );
		
		if ( iSourceBlockID == Block.waterMoving.blockID || iSourceBlockID == Block.waterStill.blockID )
		{
			int iDistanceToCheck = 4;
			
			return FCUtilsMisc.DoesWaterHaveValidSource( world, sourcePos.i, sourcePos.j, sourcePos.k, iDistanceToCheck );			
		}
		
    	return false;
    }
    
    private int GetRandomDistanceForSourceCheck( Random rand )
    {
		// Select random distance here, favoring the lower end to save on performance
		
		int iDistanceToCheck = 32;
		int iRandomFactor = rand.nextInt( 32 );
		
		if ( iRandomFactor == 0 )
		{
			// this is the maximum distance at which the user could conceivably construct 
			// an efficient infinite water loop (world height * 8 )
			
			iDistanceToCheck = 512;
		}
		else if ( iRandomFactor <= 2 )
		{
			iDistanceToCheck = 256;
		}
		else if ( iRandomFactor <= 6 )
		{
			iDistanceToCheck = 128;
		}
		else if ( iRandomFactor <= 14 )
		{
			iDistanceToCheck = 64;
		}
		
		return iDistanceToCheck;		
    }
    
	private void BreakScrewPump( World world, int i, int j, int k )
	{
		DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	//----------- Client Side Functionality -----------//
}