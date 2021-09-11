// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockGearBox extends Block
	implements FCIBlockMechanical
{
	static public final int m_iTickRate = 10;
	
	static private final int m_iTurnOnTickRate = 10;
	static private final int m_iTurnOffTickRate = 9;
	
	protected FCBlockGearBox( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );

        setHardness( 2F );
        
        SetAxesEffectiveOn( true );
        
        SetBuoyant();
		SetFireProperties( FCEnumFlammability.PLANKS );
        
        setStepSound( soundWoodFootstep );
        
        setTickRandomly( true );

        setUnlocalizedName( "fcBlockGearBox" );
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }
	
	@Override
    public int tickRate( World world )
    {
    	return m_iTickRate;
    }	
	
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, Block.GetOppositeFacing( iFacing ) );        
    }    
  
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack )
	{
		int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacingReversed( entityLiving );
		
		SetFacing( world, i, j, k, iFacing );
	}
	
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	if ( player.getCurrentEquippedItem() == null && 
    		!FCUtilsMechPower.DoesBlockHaveAnyFacingAxles( world, i, j, k ) )
    	{
	        if ( !world.isRemote )
	        {
	        	ToggleFacing( world, i, j, k, false );
	        	
	            FCUtilsMisc.PlayPlaceSoundForBlock( world, i, j, k );        
	        }
	        
	        return true;
    	}
    	
		return false;
    }
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	boolean bMechPowered = IsInputtingMechanicalPower( world, i, j, k );
    	
    	UpdateMechPoweredState( world, i, j, k, bMechPowered );
    }
	
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( !IsCurrentStateValid( world, i, j, k ) )
		{
			// verify we have a tick already scheduled to prevent jams on chunk load
			
			if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
			{
		        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );        
			}
		}
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		// we have to verify that an update isn't pending as this may be called
		// by another block being updated, while an update is waiting to be processed
		// already this tick
		
		if ( !IsCurrentStateValid( world, i, j, k ) &&
			!world.IsUpdateScheduledForBlock( i, j, k, blockID ) &&
			!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
	    	if ( !FCBetterThanWolves.fcDisableGearBoxPowerDrain && 
	    		IsGearBoxOn( world, i, j, k ) )
			{
		    	// a Gear Box turns off slightly quicker than it turns on so that 
	    		// pulses of power bleed off with distance
		  
    			world.scheduleBlockUpdate( i, j, k, blockID, m_iTurnOffTickRate );
			}
	    	else
	    	{		
	    		world.scheduleBlockUpdate( i, j, k, blockID, m_iTurnOnTickRate );
	    	}
		}
    }
	
	@Override
    public int GetMechanicalPowerLevelProvidedToAxleAtFacing( World world, int i, int j, int k, int iFacing )
    {
		if ( IsGearBoxOn( world, i, j, k ) &&
			GetFacing( world, i, j, k ) != iFacing )
		{
			return 4;
		}
		
    	return 0;
    }
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 3, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemGear.itemID, 2, 0, fChanceOfDrop );
		
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
    	iMetadata &= 8; // filter out any old alignment
    	
    	iMetadata |= iFacing;
    	
		return iMetadata;
	}
	
	@Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		int iFacing = GetFacing( world, i, j, k );

		int iNewFacing = Block.RotateFacingAroundJ( iFacing, bReverse );
		
		if ( iNewFacing != iFacing )
		{
	    	if ( IsGearBoxOn( world, i, j, k ) )
	    	{
	    		SetGearBoxOn( world, i, j, k, false );
	    	}
	    	
			SetFacing( world, i, j, k, iNewFacing );
			
	        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
	        
	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	    	
	    	FCUtilsMechPower.DestroyHorizontallyAttachedAxles( world, i, j, k );
	    	
	    	return true;
    	}
		
		return false;
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
    	if ( IsGearBoxOn( world, i, j, k ) )
    	{
    		SetGearBoxOn( world, i, j, k, false );
    	}
    	
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	
    	world.notifyBlockChange( i, j, k, blockID );
    	
    	return true;    	
	}
	
    //------------- FCIBlockMechanical -------------//
    
	@Override
    public boolean CanOutputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean CanInputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean IsInputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return FCUtilsMechPower.IsBlockPoweredByAxleToSide( world, i, j, k, GetFacing( world, i, j, k) );
    }    

	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		int iBlockFacing = GetFacing( world, i, j, k );
		
		return iFacing == iBlockFacing;
	}

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return IsGearBoxOn( world, i, j, k );
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
    	if ( IsGearBoxOn( world, i, j, k ) )
    	{
    		// an overpowered gearbox that doesn't have its gears disengaged, is destroyed
    		
    		BreakGearBox( world, i, j, k );
    	}
	}
	
    //------------- Class Specific Methods ------------//
    
	protected void UpdateMechPoweredState( World world, int i, int j, int k, boolean bShouldBePowered )
	{
    	if ( IsGearBoxOn( world, i, j, k ) != bShouldBePowered )
    	{
    		SetGearBoxOn( world, i, j, k, bShouldBePowered );
    	}    	
	}
    
	protected boolean IsCurrentStateValid( World world, int i, int j, int k )
	{
    	return IsGearBoxOn( world, i, j, k ) == IsInputtingMechanicalPower( world, i, j, k );
	}
	
    public boolean IsGearBoxOn( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return IsGearBoxOn( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public boolean IsGearBoxOn( int iMetadata )
    {
    	return ( iMetadata & 8 ) > 0;
    }
    
    public int SetGearBoxOn( int iMetadata, boolean bOn )
    {
    	iMetadata &= 7; // filter out any old on state
    	
    	if ( bOn )
    	{
    		iMetadata |= 8;
    	}
    	
    	return iMetadata;
    
    }
    
    public void SetGearBoxOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetadata = SetGearBoxOn( world.getBlockMetadata( i, j, k ), bOn );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }    
	
	public void BreakGearBox( World world, int i, int j, int k )
	{
		DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon m_IconInput;
    private Icon m_IconOutput;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_IconInput = register.registerIcon( getUnlocalizedName2() + "_input" );
        m_IconOutput = register.registerIcon( getUnlocalizedName2() + "_output" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	// used by item render
		
        if ( iSide == 3 )
        {
        	return m_IconInput;
        }
        
        return blockIcon;    	
    }

	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing =  GetFacing( blockAccess, i, j, k);
        
        if ( iSide == iFacing )
        {
        	return m_IconInput;
        }
        else
        {
        	FCUtilsBlockPos sideBlockPos = new FCUtilsBlockPos( i, j, k );
        	
        	sideBlockPos.AddFacingAsOffset( iSide );
        	
        	if ( blockAccess.getBlockId( sideBlockPos.i, sideBlockPos.j, sideBlockPos.k ) == 
        		FCBetterThanWolves.fcBlockAxle.blockID )
        	{
        		if ( ( (FCBlockAxle)FCBetterThanWolves.fcBlockAxle ).IsAxleOrientedTowardsFacing( 
        				blockAccess, sideBlockPos.i, sideBlockPos.j, sideBlockPos.k, iSide ) )
        		{
        			return m_IconOutput;
        		}
        	}
        	
            if ( iSide == Block.GetOppositeFacing( iFacing ) )
            {
        		for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
        		{
        			if ( iTempFacing != iFacing && FCUtilsMechPower.DoesBlockHaveFacingAxleToSide( 
    					blockAccess, i, j, k, iTempFacing ) )
        			{
        				// if the box has no output axles connected, the
        				// side opposite the input displays the output texture 
        				// to help with orienting the box
        				
            			return blockIcon;
        			}
        		}
        		
    			return m_IconOutput;
            }
        }
        
        return blockIcon;
    }
	
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
    	if ( IsGearBoxOn( world, i, j, k ) )
    	{
    		EmitGearBoxParticles( world, i, j, k, random );	        
    	}
    }

	@Override
	public void ClientNotificationOfMetadataChange( World world, int i, int j, int k, int iOldMetadata, int iNewMetadata )
	{
		if ( !IsGearBoxOn( iOldMetadata ) && IsGearBoxOn( iNewMetadata ) )
		{
			// gear box mech power turn on
			
			world.playSound( i + 0.5D, j + 0.5D, k + 0.5D, 
	    		"random.chestopen", 0.25F, world.rand.nextFloat() * 0.25F + 0.25F);
	        
	        EmitGearBoxParticles( world, i, j, k, world.rand );
		}
		
		// Render update all blocks at a distance surrounding the gear box that could be powered 
		// by it so that axles change their visual state appropriately
		
        world.markBlockRangeForRenderUpdate( i - 3, j - 3, k - 3, i + 3, j + 3, k + 3 );
	}
	
	@Override
    public void ClientBreakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
	{
        world.markBlockRangeForRenderUpdate( i - 3, j - 3, k - 3, i + 3, j + 3, k + 3 );
	}
	
	@Override
    public void ClientBlockAdded( World world, int i, int j, int k )
	{
        world.markBlockRangeForRenderUpdate( i - 3, j - 3, k - 3, i + 3, j + 3, k + 3 );
	}	

    private void EmitGearBoxParticles( World world, int i, int j, int k, Random random )
    {
        for ( int iTempCount = 0; iTempCount < 5; iTempCount++ )
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            
            world.spawnParticle( "smoke", smokeX, smokeY, smokeZ, 0D, 0D, 0D );
        }
    }
}