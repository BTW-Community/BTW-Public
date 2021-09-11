// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockBellows extends Block
	implements FCIBlockMechanical
{
	private static final int m_iBellowsTickRate = 35;// 37 until 4.B00 b 
	
	public static final float m_fBellowsContractedHeight = 1F - ( 5F / 16F );
	
	private static final double m_dBlowItemStrength = 0.2D;
	private static final double m_dParticleSpeed = 0.1F;	
	
	protected FCBlockBellows( int iBlockID )
	{
        super( iBlockID, Material.wood );

        setHardness( 2F );
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
    	InitBlockBounds( 0F, 0F, 0F, 1F, m_fBellowsContractedHeight, 1F );
    	
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockBellows" );
        
        setTickRandomly( true );
        
        setCreativeTab( CreativeTabs.tabRedstone );
	}
	
	@Override
    public int tickRate( World world )
    {
    	return m_iBellowsTickRate;
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
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( IsBlockMechanicalOn( blockAccess, i, j, k ) )
    	{
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0F, 0F, 0F, 1F, m_fBellowsContractedHeight, 1F );
    	}
    	else
    	{
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0F, 0F, 0F, 1F, 1F, 1F );
    	}
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	boolean bUpdateAlreadyScheduled = world.IsUpdateScheduledForBlock( i, j, k, blockID );
    	
    	if ( !bUpdateAlreadyScheduled )
    	{
    		if ( !IsCurrentStateValid( world, i, j, k ) && 
    			!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
    		{
    			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    			SetIsContinuousMechanicalStateChange( world, i, j, k, true );
    		}
    	}
    	else
    	{
    		boolean bContinuousChange = IsContinuousMechanicalStateChange( world, i, j, k );
    		
    		if ( bContinuousChange )
    		{
    			if ( IsCurrentStateValid( world, i, j, k ) )
    			{
        			// mechanical power has again changed state when we were already scheduled for an update
        			
        			SetIsContinuousMechanicalStateChange( world, i, j, k, false );
        		}
    		}
    	}    	
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bReceivingMechanicalPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bMechanicalOn = IsBlockMechanicalOn( world, i, j, k );
    	
    	boolean bContinuousChange = IsContinuousMechanicalStateChange( world, i, j, k );
    	
    	if ( bMechanicalOn != bReceivingMechanicalPower )
    	{
    		if ( bContinuousChange )
    		{
	    		SetIsContinuousMechanicalStateChange( world, i, j, k, false );
	    		SetBlockMechanicalOn( world, i, j, k, bReceivingMechanicalPower );
	    		
		        if ( bReceivingMechanicalPower )
		        {
		        	Blow( world, i, j, k );		        	
		        }
		        else
		        {
		        	LiftCollidingEntities( world, i, j, k );		        	
		        }		        
    		}
    		else
    		{
    			// the Bellows is in the wrong state for the power it's receiving, but the power flow was interrupted during the transition
    			// schedule another block update to check again later
    			
    			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );    			
	    		SetIsContinuousMechanicalStateChange( world, i, j, k, true );    			
    		}
    	}
    	else
    	{
    		if ( bContinuousChange )
    		{
	    		SetIsContinuousMechanicalStateChange( world, i, j, k, false );
    		}
    	}
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
    			SetIsContinuousMechanicalStateChange( world, i, j, k, true );
			}
		}
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
    	return ( iMetadata & 3 ) + 2;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata &= (~3);	// filter out old facing
    	
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
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		if ( super.RotateAroundJAxis( world, i, j, k, bReverse ) )
		{
	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
	    	
	    	FCUtilsMechPower.DestroyHorizontallyAttachedAxles( world, i, j, k );
	    	
	        return true;
		}
		
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
    	return FCUtilsMechPower.IsBlockPoweredByAxle( world, i, j, k, this ) || 
    		FCUtilsMechPower.IsBlockPoweredByHandCrank( world, i, j, k );  	
    }
	
	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		int iBlockFacing = GetFacing( world, i, j, k );
		
		return iFacing != iBlockFacing && iFacing != 1;
	}

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakBellows( world, i, j, k );
	}
	
	//------------- Class Specific Methods ------------//
    
    public boolean IsBlockMechanicalOn( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsBlockMechanicalOnFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );    
	}
    
    public void SetBlockMechanicalOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~4); // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetadata |= 4;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public boolean GetIsBlockMechanicalOnFromMetadata( int iMetadata )
    {
    	return ( iMetadata & 4 ) > 0;
    }
    
    public boolean IsContinuousMechanicalStateChange( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return ( blockAccess.getBlockMetadata( i, j, k ) & 8 ) > 0;    
	}
    
    public void SetIsContinuousMechanicalStateChange( World world, int i, int j, int k, boolean bContinuous )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~8); // filter out old on state
    	
    	if ( bContinuous )
    	{
    		iMetadata |= 8;
    	}
    	
    	// no notify here as this is strictly internal state
    	
        world.setBlockMetadata( i, j, k, iMetadata );
    }
    
	public boolean IsCurrentStateValid( World world, int i, int j, int k )
	{
    	boolean bReceivingMechanicalPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bMechanicalOn = IsBlockMechanicalOn( world, i, j, k );
    	
		if ( bReceivingMechanicalPower != bMechanicalOn )
		{
			return false;
		}
		
		return true;
	}
	
	private void Blow( World world, int i, int j, int k )
	{
		//EmitBellowsParticles( world, i, j, k, world.rand );
		
		StokeFiresInFront( world, i, j, k );
		
		BlowLightItemsInFront( world, i, j, k );
	}
	
	private void StokeFiresInFront( World world, int i, int j, int k )
	{
		int iFacing = GetFacing( world, i, j, k );
		int iFacingSide1 = Block.RotateFacingAroundJ( iFacing, false );
		int iFacingSide2 = Block.RotateFacingAroundJ( iFacing, true );
		
		FCUtilsBlockPos tempTargetPos = new FCUtilsBlockPos( i, j, k );
		
		// stoke fire
		
		for ( int iTempCount = 0; iTempCount < 3; iTempCount++ )
		{
			tempTargetPos.AddFacingAsOffset( iFacing );
			
			int tempBlockID = world.getBlockId( tempTargetPos.i, tempTargetPos.j, tempTargetPos.k );
			
			if ( tempBlockID == Block.fire.blockID || tempBlockID == FCBetterThanWolves.fcBlockFireStoked.blockID )
			{
				StokeFire( world, tempTargetPos.i, tempTargetPos.j, tempTargetPos.k );
			}
			else if ( !world.isAirBlock( tempTargetPos.i, tempTargetPos.j, tempTargetPos.k ) )
			{
				// line of sight to the next fire is blocked so drop out
				
				break;
			}
			
			// stoke one side
				
			FCUtilsBlockPos tempSidePos1 =  new FCUtilsBlockPos( tempTargetPos.i, tempTargetPos.j, tempTargetPos.k );
			
			tempSidePos1.AddFacingAsOffset( iFacingSide1 );
			
			tempBlockID = world.getBlockId( tempSidePos1.i, tempSidePos1.j, tempSidePos1.k );
			
			if ( tempBlockID == Block.fire.blockID || tempBlockID == FCBetterThanWolves.fcBlockFireStoked.blockID )
			{
				StokeFire( world, tempSidePos1.i, tempSidePos1.j, tempSidePos1.k );
			}
			
			// then the other
			
			FCUtilsBlockPos tempSidePos2 =  new FCUtilsBlockPos( tempTargetPos.i, tempTargetPos.j, tempTargetPos.k );
			
			tempSidePos2.AddFacingAsOffset( iFacingSide2 );
			
			tempBlockID = world.getBlockId( tempSidePos2.i, tempSidePos2.j, tempSidePos2.k );
			
			if ( tempBlockID == Block.fire.blockID || tempBlockID == FCBetterThanWolves.fcBlockFireStoked.blockID )
			{
				StokeFire( world, tempSidePos2.i, tempSidePos2.j, tempSidePos2.k );
			}
		}		
	}
	
	private void BlowLightItemsInFront( World world, int i, int j, int k )
	{
		int iFacing = GetFacing( world, i, j, k );
		
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
		targetPos.AddFacingAsOffset( iFacing );
		
        List collisionList = null;
        
    	int iBlowRange = ComputeBlowRange( world, i, j, k );

    	if ( iBlowRange > 0 )
    	{
	        AxisAlignedBB blowBox = CreateBlowBoundingBox( world, i, j, k, iBlowRange );
	        
	        if ( blowBox != null )
	        {        
		        // check for items within the blow zone       
		        
		        collisionList = world.getEntitiesWithinAABB( EntityItem.class, blowBox );
		
		    	if ( collisionList != null && collisionList.size() > 0 )
		    	{
		    		Vec3 blowVector = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );
		    		
		    		blowVector.xCoord *= m_dBlowItemStrength;
		    		blowVector.yCoord *= m_dBlowItemStrength;
		    		blowVector.zCoord *= m_dBlowItemStrength;
		    		
		            for ( int listIndex = 0; listIndex < collisionList.size(); listIndex++ )
		            {
			    		EntityItem targetEntityItem = (EntityItem)collisionList.get( listIndex );
			    		
				        if ( !targetEntityItem.isDead )
				        {
				        	ItemStack stack = targetEntityItem.getEntityItem();
				        	int iItemBlowDistance = stack.getItem().GetBellowsBlowDistance( stack.getItemDamage() ); 
				        	
				        	if ( iItemBlowDistance > 0 && ( iItemBlowDistance >= iBlowRange || 
				        		IsEntityWithinBlowRange( world, i, j, k, iItemBlowDistance, targetEntityItem ) ) )
		        			{
				        		targetEntityItem.motionX += blowVector.xCoord;
				        		targetEntityItem.motionY += blowVector.yCoord;
				        		targetEntityItem.motionZ += blowVector.zCoord;
		        			}		        
				        }		        
		            }
		    	}
	        }
    	}
	}
	
	private boolean IsEntityWithinBlowRange( World world, int i, int j, int k, int iBlowRange, Entity entity )
	{
		// FCTODO: Optimize this, it's brute force right now
		
        AxisAlignedBB blowBox = CreateBlowBoundingBox( world, i, j, k, iBlowRange );
        
        return blowBox.intersectsWith( entity.boundingBox );        
	}
	
    private AxisAlignedBB CreateBlowBoundingBox( World world, int i, int j, int k, int iBlowRange )
    {
    	AxisAlignedBB blowBox = null;
    	
    	if ( iBlowRange > 0 )
    	{
    		int iFacing = GetFacing( world, i, j, k );
    		
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
    		targetPos.AddFacingAsOffset( iFacing );
    		
    		blowBox = AxisAlignedBB.getAABBPool().getAABB( (float)targetPos.i, (float)targetPos.j, (float)targetPos.k, 
				(float)(targetPos.i + 1), (float)(targetPos.j + 1), (float)(targetPos.k + 1) );
    		
    		if ( iBlowRange > 1 )
    		{
	    		Vec3 blowVector = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );
	    		double dMultiplier = (double)( iBlowRange - 1 );
	    		
    			blowVector.xCoord *= dMultiplier;
    			blowVector.yCoord *= dMultiplier;
    			blowVector.zCoord *= dMultiplier;
	    		
    			blowBox = blowBox.addCoord( blowVector.xCoord, blowVector.yCoord, blowVector.zCoord );
    		}    		
    	}
    	
    	return blowBox;
    }

	private int ComputeBlowRange( World world, int i, int j, int k )
	{
		int iBlowRange = 0;
		int iFacing = GetFacing( world, i, j, k );
		
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		
		for ( int iTempRange = 0; iTempRange < 3; iTempRange++ )
		{
			targetPos.AddFacingAsOffset( iFacing );
			
			if ( CanBlowThroughBlock( world, targetPos.i, targetPos.j, targetPos.k ) )
			{
				iBlowRange++;
			}
			else
			{
				break;
			}
		}
		
		return iBlowRange;
	}
	
	private boolean CanBlowThroughBlock( World world, int i, int j, int k )
	{
		if ( !world.isAirBlock( i, j, k ) )
		{
			int iBlockID = world.getBlockId( i, j, k );
			
			if ( iBlockID != Block.fire.blockID && iBlockID != FCBetterThanWolves.fcBlockFireStoked.blockID && iBlockID != Block.trapdoor.blockID )
			{
				if ( Block.blocksList[iBlockID].getCollisionBoundingBoxFromPool( world, i, j, k ) != null )
				{
					return false;
				}
			}
		}
		
		return true;
	}	
	
	private void StokeFire( World world, int i, int j, int k )
	{
		if ( world.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcBBQ.blockID )
		{
			if ( world.getBlockId( i, j, k ) == FCBetterThanWolves.fcBlockFireStoked.blockID )
			{
				// reset the stoked fire's state to keep it going
				
				world.setBlockMetadata( i, j, k, 0 );
			}
			else
			{
				world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockFireStoked.blockID );
			}
			
			// put regular fire above the stoked
			
			if ( world.isAirBlock( i, j + 1, k ) )
			{
	            world.setBlockWithNotify( i, j + 1, k, fire.blockID );
			}
		}
		else
		{
			// we don't have a hibachi below, extinguish the fire
			
			world.setBlockWithNotify( i, j, k, 0 );
		}
	}
	
	private void LiftCollidingEntities( World world, int i, int j, int k )
	{
		List list = world.getEntitiesWithinAABBExcludingEntity( null, 
			AxisAlignedBB.getAABBPool().getAABB(
        		(float)i, (float)j + m_fBellowsContractedHeight, (float)k, 
        		(float)( i + 1), (float)(j + 1), (float)(k + 1) ) );
		
		float extendedMaxY = (float)(j + 1);
            
        if ( list != null && list.size() > 0 )
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity tempEntity = (Entity)list.get(j1);
   
                if( !tempEntity.isDead && ( tempEntity.canBePushed() || ( tempEntity instanceof EntityItem ) ) )
                {
                	double tempEntityMinY = tempEntity.boundingBox.minY;
                	
                	if ( tempEntityMinY < extendedMaxY )
                	{
        	    		double entityYOffset = extendedMaxY - tempEntityMinY;
        	    		
        	    		tempEntity.setPosition( tempEntity.posX, tempEntity.posY + entityYOffset, tempEntity.posZ );	    		
                	}
                }
            }
        }
	}
	
	public void BreakBellows( World world, int i, int j, int k )
	{
		// drop wood siding
		
		for ( int iTemp = 0; iTemp < 2; iTemp++ )
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, FCBetterThanWolves.fcBlockWoodSidingItemStubID, 0 );			
		}
		
		// drop gears
		
		for ( int iTemp = 0; iTemp < 1; iTemp++ )
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, FCBetterThanWolves.fcItemGear.itemID, 0 );
		}
		
		// drop leather straps
		
		for ( int iTemp = 0; iTemp < 2; iTemp++ )
		{
			ItemStack itemStack = new ItemStack( FCBetterThanWolves.fcItemTannedLeather.itemID, 4, 0 );
			
			FCUtilsItem.EjectStackWithRandomOffset( world, i, j, k, itemStack );
		}
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconBySideArray = new Icon[6];
    private Icon m_IconFront; 
    
	@Override
    public void registerIcons( IconRegister register )
    {
        Icon sideIcon = register.registerIcon( "fcBlockBellows_side" );
        
        blockIcon = sideIcon; // for hit effects
        
        m_IconBySideArray[0] = register.registerIcon( "fcBlockBellows_bottom" );
        m_IconBySideArray[1] = register.registerIcon( "fcBlockBellows_top" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
        
        m_IconFront = register.registerIcon( "fcBlockBellows_front" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		// item render
		
		if ( iSide == 3 )
		{
			return m_IconFront;			
		}
		
		return m_IconBySideArray[iSide];
    }
	
	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing = GetFacing( blockAccess, i, j, k);
        
        if ( iSide == iFacing )
        {
			return m_IconFront;			
        }
        
		return m_IconBySideArray[iSide];
    }
	
	@Override
	public void ClientNotificationOfMetadataChange( World world, int i, int j, int k, int iOldMetadata, int iNewMetadata )
	{
		if ( !GetIsBlockMechanicalOnFromMetadata( iOldMetadata ) && GetIsBlockMechanicalOnFromMetadata( iNewMetadata ) )
		{
			// bellows power turn on
			
			BlowLightItemsInFront( world, i, j, k );
			
            world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.cow.say4", 
            		0.25F, world.rand.nextFloat() * 0.4F + 2F );	            
	        
            int iFacing = GetFacing( iNewMetadata );
            
	        EmitBellowsParticles( world, i, j, k, iFacing, world.rand );
		}
		else if ( GetIsBlockMechanicalOnFromMetadata( iOldMetadata ) && !GetIsBlockMechanicalOnFromMetadata( iNewMetadata ) )
		{
			// bellows power turn off
			
        	LiftCollidingEntities( world, i, j, k );
        	
            world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.cow.say2", 
            		1.0F, world.rand.nextFloat() * 0.4F + 2F );
		}
	}
        
    private void EmitBellowsParticles( World world, int i, int j, int k, int iFacing, Random random )
    {
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
		targetPos.AddFacingAsOffset( iFacing );
		
		Vec3 blowVector = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );
		
		blowVector.xCoord *= m_dParticleSpeed;
		blowVector.yCoord *= m_dParticleSpeed;
		blowVector.zCoord *= m_dParticleSpeed;
		
        for ( int counter = 0; counter < 10; counter++ )
        {
            float smokeX = (float)targetPos.i + random.nextFloat();
            float smokeY = (float)targetPos.j + random.nextFloat() * 0.5F;
            float smokeZ = (float)targetPos.k + random.nextFloat();
            
            world.spawnParticle( "smoke", smokeX, smokeY, smokeZ, 
        		blowVector.xCoord + ( random.nextFloat() * 0.10F ) - 0.05F, 
        		blowVector.yCoord + ( random.nextFloat() * 0.10F ) - 0.05F, 
        		blowVector.zCoord + ( random.nextFloat() * 0.10F ) - 0.05F );
        }
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
}