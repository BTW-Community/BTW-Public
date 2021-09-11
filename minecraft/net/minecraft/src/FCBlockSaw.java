// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;

public class FCBlockSaw extends Block
	implements FCIBlockMechanical
{
	private static final int m_iPowerChangeTickRate = 10;
	
	private static final int m_iSawTimeBaseTickRate = 20;
	private static final int m_iSawTimeTickRateVariance = 4;

	// This base height prevents chickens slipping through grinders, while allowing items to pass
	
	public static final float m_fBaseHeight = 1F - ( 4F / 16F );

	public static final float m_fBladeLength = ( 10F / 16F );
    public static final float m_fBladeHalfLength = m_fBladeLength * 0.5F;
    
    public static final float m_fBladeWidth = ( 0.25F / 16F );
    public static final float m_fBladeHalfWidth = m_fBladeWidth * 0.5F;    

    public static final float m_fBladeHeight = 1F - m_fBaseHeight;
    
	protected FCBlockSaw( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );

        setHardness( 2F );
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, m_fBaseHeight, 1F );
        
		SetFireProperties( 5, 20 );
		
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockSaw" );
        
        setTickRandomly( true );

        setCreativeTab( CreativeTabs.tabRedstone );
	}
	
	@Override
    public int tickRate( World world )
    {
    	return m_iPowerChangeTickRate;
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

        // note that we can't validate if the update is required here as the block will have
        // its facing set after being added
        
        world.scheduleBlockUpdate( i, j, k, blockID, m_iPowerChangeTickRate );
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
		// reduce the collison box slightly to avoid vanilla cardinal point weirdness
		// with floating items
				
		float fBaseHeight = m_fBaseHeight - ( 1.0F / 32.0F );
		
    	return GetBlockBoundsFromPoolForBaseHeight( world, i, j, k, fBaseHeight ).offset( i, j, k );
    }

    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolForBaseHeight( blockAccess, i, j, k, m_fBaseHeight );
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			ScheduleUpdateIfRequired( world, i, j, k );
		}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = IsBlockOn( world, i, j, k );
    	
    	if ( bOn != bReceivingPower )
    	{
	        EmitSawParticles( world, i, j, k, rand );
	        
    		SetBlockOn( world, i, j, k, bReceivingPower );
    		
    		if ( bReceivingPower )
    		{
		        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		    		"minecart.base", 
		    		1F + ( rand.nextFloat() * 0.1F ),		// volume 
		    		1.5F + ( rand.nextFloat() * 0.1F ) );	// pitch
		        
    			// the saw doesn't cut on the update in which it is powered, so check if another
    			// update is required
    			
		        ScheduleUpdateIfRequired( world, i, j, k );		        
    		}
    		else
    		{
		        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		    		"minecart.base", 
		    		1F + ( rand.nextFloat() * 0.1F ),		// volume 
		    		0.75F + ( rand.nextFloat() * 0.1F ) );	// pitch
    		}
    	}
    	else if ( bOn )
    	{
    		SawBlockToFront( world, i, j, k, rand );
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		// verify we have a tick already scheduled to prevent jams on chunk load
		
		if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
		{
			ScheduleUpdateIfRequired( world, i, j, k );
		}
    }

	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( world.isRemote )
		{
			return;
		}
		
    	if ( IsBlockOn( world, i, j, k ) )
    	{
	    	if ( entity instanceof EntityLiving )
	    	{
	    		int iFacing = GetFacing( world, i, j, k );
	    		
	    		// construct bounding box from saw
	    		
	            float fHalfLength = ( 10.0F / 16.0F ) * 0.5F;
	            float fHalfWidth = ( 0.25F / 16.0F ) * 0.5F;
	            float fBlockHeight = ( 4.0F / 16.0F );
	            
	            AxisAlignedBB sawBox;
	            
	            switch ( iFacing )
	            {	    	        
	            	case 0:
	    	        	
	    	        	sawBox = AxisAlignedBB.getAABBPool().getAABB( 0.5F - fHalfLength, 0.0F, 0.5F - fHalfWidth, 
    			    		0.5F + fHalfLength, fBlockHeight, 0.5F + fHalfWidth );		        
	    		        
	    	        	break;
	    	        	
	    	        case 1:        	
	    	        	
	    	        	sawBox = AxisAlignedBB.getAABBPool().getAABB( 0.5F - fHalfLength, 1.0F - fBlockHeight, 0.5F - fHalfWidth, 
    			    		0.5F + fHalfLength, 1.0F, 0.5F + fHalfWidth );
	    		        
	    		        break;
	    		        
	    	        case 2:
	    	        	
	    	        	sawBox = AxisAlignedBB.getAABBPool().getAABB(  0.5F - fHalfLength, 0.5F - fHalfWidth, 0.0F,   
    		        		0.5F + fHalfLength, 0.5F + fHalfWidth, fBlockHeight );
	    		        
	    	        	break;
	    	        	
	    	        case 3:
	    	        	
	    	        	sawBox = AxisAlignedBB.getAABBPool().getAABB( 0.5F - fHalfLength, 0.5F - fHalfWidth, 1.0F - fBlockHeight,  
    		        		0.5F + fHalfLength, 0.5F + fHalfWidth, 1.0F );
	    			        
	    	        	break;
	    	        	
	    	        case 4:
	    	        	
	    	        	sawBox = AxisAlignedBB.getAABBPool().getAABB( 0.0F, 0.5F - fHalfWidth, 0.5F - fHalfLength, 
    			    		fBlockHeight, 0.5F + fHalfWidth, 0.5F + fHalfLength );
	    		        
	    	        	break;
	    	        	
	    	        default: // 5
	    	        	
	    	        	sawBox = AxisAlignedBB.getAABBPool().getAABB( 1.0F - fBlockHeight, 0.5F - fHalfWidth,  0.5F - fHalfLength, 
    			    		1.0F, 0.5F + fHalfWidth, 0.5F + fHalfLength );
	    			        
	    	        	break;	        	
	            }
	            
	            sawBox = sawBox.getOffsetBoundingBox( (double)i, (double)j, (double)k );
	    	        	
	            List collisionList = null;
	            
	            collisionList = world.getEntitiesWithinAABB(net.minecraft.src.EntityLiving.class, sawBox );
	            
	            if ( collisionList != null && collisionList.size() > 0 )
	            {
	            	DamageSource source = FCDamageSourceCustom.m_DamageSourceSaw;
	            	int iDamage = 4;
	            	
	            	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
	            	
	            	targetPos.AddFacingAsOffset( iFacing );
	            	
	            	int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
	            	int iTargetMetadata = world.getBlockMetadata( targetPos.i, targetPos.j, targetPos.k );
	            	
	            	if ( iTargetBlockID == FCBetterThanWolves.fcAestheticOpaque.blockID &&
	            		( iTargetMetadata == FCBlockAestheticOpaque.m_iSubtypeChoppingBlockClean || iTargetMetadata == FCBlockAestheticOpaque.m_iSubtypeChoppingBlockDirty ) )
	            	{
	            		source = FCDamageSourceCustom.m_DamageSourceChoppingBlock;
	            		iDamage *= 3;
	            		
	            		if ( iTargetMetadata == FCBlockAestheticOpaque.m_iSubtypeChoppingBlockClean )
	            		{
	            			world.setBlockMetadataWithNotify( targetPos.i, targetPos.j, targetPos.k, FCBlockAestheticOpaque.m_iSubtypeChoppingBlockDirty );
	            		}
	            	}	            		
	            	
		            for( int iTempListIndex = 0; iTempListIndex < collisionList.size(); iTempListIndex++ )
		            {
		                EntityLiving tempTargetEntity = (EntityLiving)collisionList.get( iTempListIndex );
		                
			            if ( tempTargetEntity.attackEntityFrom( source, iDamage ) )
			    		{
			                world.playAuxSFX( FCBetterThanWolves.m_iSawDamageEntityAuxFXID, i, j, k, iFacing );            
			    		}
		            }
	            }
	    	}
    	}
    }
    
	@Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing != GetFacing( blockAccess, i, j, k );
	}
	
	@Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return Block.GetOppositeFacing( iFacing ) == GetFacing( blockAccess, i, j, k );
	}
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemGear.itemID, 1, 0, fChanceOfDrop );		
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 3, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, Item.ingotIron.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemNuggetIron.itemID, 4, 0, fChanceOfDrop );		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStrap.itemID, 3, 0, fChanceOfDrop );
		
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
    	iMetadata &= (~7);	// filter out old facing
    	
    	iMetadata |= iFacing;
    	
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		int iFacing = GetFacing( iBlockAccess, i, j, k );
		
		return iFacing != 0;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iFacing = GetFacing( blockAccess, i, j, k );
		
		return iFacing != 0 && iFacing!= 1;
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

	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    	
    	world.notifyBlockChange( i, j, k, blockID );
    	
    	return true;
	}
	
    @Override
    public boolean IsIncineratedInCrucible()
    {
    	return false;
    }
	
    //------------- Class Specific Methods ------------//    
	
	protected boolean IsCurrentPowerStateValid( World world, int i, int j, int k )
	{
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = IsBlockOn( world, i, j, k );
    	
    	return bOn == bReceivingPower;
	}
	
    public boolean IsBlockOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 8 ) > 0;    
	}
    
    public void SetBlockOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & 7; // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 8;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
	protected void ScheduleUpdateIfRequired( World world, int i, int j, int k )
	{
		if ( !IsCurrentPowerStateValid( world, i, j, k ) )
		{
	        world.scheduleBlockUpdate( i, j, k, blockID, m_iPowerChangeTickRate );        
		}
		else if ( IsBlockOn( world, i, j, k ) )
		{
			// check if we have something to cut in front of us
			
    		int iFacing = GetFacing( world, i, j, k );    		
    		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    		
			Block targetBlock = Block.blocksList[world.getBlockId( targetPos.i, targetPos.j, 
				targetPos.k )];
    		
    		if ( targetBlock != null && ( targetBlock.blockMaterial.isSolid() || 
    			targetBlock.GetItemIDDroppedOnSaw( world, targetPos.i, targetPos.j, 
				targetPos.k ) > 0 || targetBlock.DoesBlockDropAsItemOnSaw( world, targetPos.i, 
				targetPos.j, targetPos.k ) ) )
    		{
		        world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "minecart.base", 
		    		1.5F + ( world.rand.nextFloat() * 0.1F ),		// volume 
		    		1.9F + ( world.rand.nextFloat() * 0.1F ) );		// pitch
		        
    			world.scheduleBlockUpdate( i, j, k, blockID, m_iSawTimeBaseTickRate + 
    				world.rand.nextInt( m_iSawTimeTickRateVariance )  );
    		}	    		
		}
	}
	
    public AxisAlignedBB GetBlockBoundsFromPoolForBaseHeight( IBlockAccess blockAccess, int i, int j, int k,
    	float fBaseHeight )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        switch ( iFacing )
        {
	        case 0:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB(	0F, 1F - fBaseHeight, 0F, 1F, 1F, 1F );
	        	
	        case 1:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 0F, 1F, fBaseHeight, 1F );
	        	
	        case 2:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 1F - fBaseHeight, 
            		1F, 1F, 1F );
	        	
	        case 3:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 0F, 1F, 1F, fBaseHeight );
	        	
	        case 4:
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB( 1F - fBaseHeight, 0F, 0F, 1F, 1F, 1F );
	        	
	        default: // 5
	        	
	        	return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 0F, fBaseHeight, 1F, 1F );
        }
    }
	
    void EmitSawParticles( World world, int i, int j, int k, Random random )
    {
    	// FCTODO: I don't believe this is working as it's being called on the server
		int iFacing = GetFacing( world, i, j, k );
		
		// compute position of saw blade
		
        float fBladeXPos = (float)i;
        float fBladeYPos = (float)j;
        float fBladeZPos = (float)k;
        
        float fBladeXExtent = 0.0f;
        float fBladeZExtent = 0.0f;
        
        switch ( iFacing )
        {	    	        
        	case 0:
	        	
        		fBladeXPos += 0.5f;
        		fBladeZPos += 0.5f;
        		
                fBladeXExtent = 1.0f;
                
	        	break;
	        	
	        case 1:        	
	        	
        		fBladeXPos += 0.5f;
        		fBladeZPos += 0.5f;
        		
        		fBladeYPos += 1.0f;
        		
                fBladeXExtent = 1.0f;
                
		        break;
		        
	        case 2:
	        	
        		fBladeXPos += 0.5f;
        		fBladeYPos += 0.5f;
        		
                fBladeXExtent = 1.0f;
                
	        	break;
	        	
	        case 3:
	        	
        		fBladeXPos += 0.5f;
        		fBladeYPos += 0.5f;
        		
        		fBladeZPos += 1.0f;
        		
                fBladeXExtent = 1.0f;
                
	        	break;
	        	
	        case 4:
	        	
        		fBladeYPos += 0.5f;
        		fBladeZPos += 0.5f;
        		
                fBladeZExtent = 1.0f;
                
	        	break;
	        	
	        default: // 5
	        	
        		fBladeYPos += 0.5f;
        		fBladeZPos += 0.5f;
        		
        		fBladeXPos += 1.0f;
        		
                fBladeZExtent = 1.0f;
                
	        	break;	        	
        }
        
        for ( int counter = 0; counter < 5; counter++ )
        {
            float smokeX = fBladeXPos + ( ( random.nextFloat() - 0.5f ) * fBladeXExtent );
            float smokeY = fBladeYPos + ( random.nextFloat() * 0.10f );
            float smokeZ = fBladeZPos + ( ( random.nextFloat() - 0.5f ) * fBladeZExtent );
            
            world.spawnParticle( "smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D );
        }
    }
    
	protected void SawBlockToFront( World world, int i, int j, int k, Random random )
	{
		int iFacing = GetFacing( world, i, j, k );
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		if ( !world.isAirBlock( targetPos.i, targetPos.j, targetPos.k ) )
		{
			if ( !HandleSawingExceptionCases( world, targetPos.i, targetPos.j, targetPos.k, i, j, k, iFacing, random ) )
			{
	    		Block targetBlock = Block.blocksList[world.getBlockId( targetPos.i, targetPos.j, targetPos.k )];
	    		
	    		if ( targetBlock != null )
	    		{
	    			if ( targetBlock.DoesBlockBreakSaw( world, targetPos.i, targetPos.j, targetPos.k ) )
	    			{
	    				BreakSaw( world, i, j, k );
	    			}
	    			else if ( targetBlock.OnBlockSawed( world, targetPos.i, targetPos.j, targetPos.k, i, j, k ) )
	    			{    				
				        EmitSawParticles( world, targetPos.i, targetPos.j, targetPos.k, random );    			        
	    			}
	    		}
			}
		}
	}
	
	/*
	 * returns true if an exception case is processed, false otherwise
	 */
    private boolean HandleSawingExceptionCases( World world, int i, int j, int k, int iSawI, int iSawJ, int iSawK, int iSawFacing, Random random )
    {
		int iTargetBlockID = world.getBlockId( i, j, k );
		
		if ( iTargetBlockID == Block.pistonMoving.blockID )
		{
			return true;
		}
		
		int iTargetMetadata = world.getBlockMetadata( i, j, k );
		
		boolean bSawedBlock = false;
		
		if ( iTargetBlockID == Block.wood.blockID )
		{
			iTargetMetadata &= 3;
			
			for ( int iTempCount = 0; iTempCount < 4; iTempCount++ )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					Block.planks.blockID, iTargetMetadata );
			}
			
			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					FCBetterThanWolves.fcItemSawDust.itemID, 0 );
			}
			
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcItemBark.itemID, iTargetMetadata );
			
			bSawedBlock = true;				

		}
		else if ( iTargetBlockID == Block.stairsWoodOak.blockID )				
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodSidingItemStubID, 0 );
			
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodMouldingItemStubID, 0 );
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.stairsWoodSpruce.blockID )				
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodSidingItemStubID, 1 );
			
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodMouldingItemStubID, 1 );
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.stairsWoodBirch.blockID )				
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodSidingItemStubID, 2 );
			
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodMouldingItemStubID, 2 );
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.stairsWoodJungle.blockID )				
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodSidingItemStubID, 3 );
			
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
				FCBetterThanWolves.fcBlockWoodMouldingItemStubID, 3 );
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.woodDoubleSlab.blockID )
		{
			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					Block.woodSingleSlab.blockID, iTargetMetadata & 7 );
			}
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.woodSingleSlab.blockID ) // wood slab
		{
			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					FCBetterThanWolves.fcBlockWoodMouldingItemStubID, iTargetMetadata & 7 );
			}
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.stoneDoubleSlab.blockID && ( iTargetMetadata & 7 ) == 2 ) // legacy double wood slabs
		{
			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					Block.woodSingleSlab.blockID, 0 );
			}
			
			bSawedBlock = true;
		}
		else if ( iTargetBlockID == Block.stoneSingleSlab.blockID && ( iTargetMetadata & 7 ) == 2 ) // legacy wood slabs
		{
			for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
			{
				FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
					FCBetterThanWolves.fcBlockWoodMouldingItemStubID, 0 );
			}
			
			bSawedBlock = true;
		}			
		
		if ( bSawedBlock )
		{
	        EmitSawParticles( world, i, j, k, random );

	        world.setBlockToAir( i, j, k );
		}
		
		return bSawedBlock;
    }
    
	public void BreakSaw( World world, int i, int j, int k )
	{
		DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
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
    	return FCUtilsMechPower.IsBlockPoweredByAxle( world, i, j, k, this ); 
    }    

	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		int iBlockFacing = GetFacing( world, i, j, k );
		
		return iFacing != iBlockFacing;
	}

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakSaw( world, i, j, k );
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon m_IconFront;
    private Icon m_IconBladeOff;
    private Icon m_IconBladeOn;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_IconFront = register.registerIcon( "fcBlockSaw_front" );
        m_IconBladeOff = register.registerIcon( "fcBlockSawBlade_off" );
        m_IconBladeOn = register.registerIcon( "fcBlockSawBlade_on" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	// used by item render
    	
        if ( iSide == 1 )
        {
        	return m_IconFront;
        }
        
        return blockIcon;
    }

	@Override
    public Icon getBlockTexture( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
        int iFacing = GetFacing( blockAccess, i, j, k);
        
        if ( iSide == iFacing )
        {
            return m_IconFront;
        }
        
        return blockIcon;
    }
	
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
    	if ( IsBlockOn( world, i, j, k ) )
    	{
    		EmitSawParticles( world, i, j, k, random );	        
    	}
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
        float fHalfLength = 0.5F;
        float fHalfWidth = 0.5F;
        float fBlockHeight = m_fBaseHeight;
        
        int iFacing = GetFacing( blockAccess, i, j, k );
      
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
        renderer.renderStandardBlock( this, i, j, k );
        
        // render the blade

        fHalfLength = ( 10.0F / 16.0F ) * 0.5F;
        fHalfWidth = ( 0.25F / 16.0F ) * 0.5F;
        fBlockHeight = ( 4.0F / 16.0F );
        
        switch ( iFacing )
        {
        	// the weirdness in facing 0 and 1 is due to this weird texture inversion thing that the rendering code
        	// does.
        	
	        case 0:
	        	
		        renderer.setRenderBounds( 0.5F - fHalfLength, 0.0F, 0.5F - fHalfWidth, 
			    		0.5F + fHalfLength, 0.999F/*fBlockHeight*/, 0.5F + fHalfWidth );		        
		        
		    	renderer.SetUvRotateEast( 3 );
		    	renderer.SetUvRotateWest( 3 );
		    	
		    	renderer.SetUvRotateSouth( 1 );
		    	renderer.SetUvRotateNorth( 2 );
		    	
		    	renderer.SetUvRotateBottom( 3 );
		    	
	        	break;
	        	
	        case 1:        	
	        	
		        renderer.setRenderBounds( 0.5F - fHalfLength, 0.001F/*1.0F - fBlockHeight*/, 0.5F - fHalfWidth, 
			    		0.5F + fHalfLength, 1.0F, 0.5F + fHalfWidth );
		        
		    	renderer.SetUvRotateSouth( 2 );
		    	renderer.SetUvRotateNorth( 1 );
		    	
		        break;
		        
	        case 2:
	        	
		        renderer.setRenderBounds(  0.5F - fHalfLength, 0.5F - fHalfWidth, 0.0F,   
		        		0.5F + fHalfLength, 0.5F + fHalfWidth, fBlockHeight );
		        
		    	renderer.SetUvRotateSouth( 3 );
		    	renderer.SetUvRotateNorth( 4 );
		    	
		    	renderer.SetUvRotateEast( 3 ); // top
		    	renderer.SetUvRotateWest( 3 ); // bottom
		    	
	        	break;
	        	
	        case 3:
	        	
		        renderer.setRenderBounds( 0.5F - fHalfLength, 0.5F - fHalfWidth, 1.0F - fBlockHeight,  
		        		0.5F + fHalfLength, 0.5F + fHalfWidth, 1.0F );
			        
		    	renderer.SetUvRotateSouth( 4 );
		    	renderer.SetUvRotateNorth( 3 );
		    	
		    	renderer.SetUvRotateTop( 3 );
		    	renderer.SetUvRotateBottom( 3 );
		    	
	        	break;
	        	
	        case 4:
	        	
		        renderer.setRenderBounds( 0.0F, 0.5F - fHalfWidth, 0.5F - fHalfLength, 
			    		fBlockHeight, 0.5F + fHalfWidth, 0.5F + fHalfLength );
		        
		    	renderer.SetUvRotateEast( 4 );
		    	renderer.SetUvRotateWest( 3 );
		    	
		    	renderer.SetUvRotateTop( 2 );
		    	renderer.SetUvRotateBottom( 1 );
		    	
		    	renderer.SetUvRotateNorth( 3 );
		    	renderer.SetUvRotateSouth( 4 );
		    	
	        	break;
	        	
	        default: // 5
	        	
		        renderer.setRenderBounds( 1.0F - fBlockHeight, 0.5F - fHalfWidth,  0.5F - fHalfLength, 
			    		1.0F, 0.5F + fHalfWidth, 0.5F + fHalfLength );
			        
		    	renderer.SetUvRotateEast( 3 );
		    	renderer.SetUvRotateWest( 4 );
		    	
		    	renderer.SetUvRotateTop( 1 );
		    	renderer.SetUvRotateBottom( 2 );
		    	
		    	renderer.SetUvRotateSouth( 4 );
		    	renderer.SetUvRotateNorth( 3 );
		    	
	        	break;	        	
        }

        
        renderer.SetRenderAllFaces( true );
        
        Icon bladeIcon = m_IconBladeOff;
        
        if ( IsBlockOn( blockAccess, i, j, k ) )
        {
        	bladeIcon = m_IconBladeOn;
        }
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderer, this, i, j, k, bladeIcon );
        
        renderer.SetRenderAllFaces( false );

		renderer.ClearUvRotation();
		
        return true;        
    }      
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
        renderBlocks.setRenderBounds( 0.0F, 0.0F, 0.0F, 
    		1.0F, m_fBaseHeight, 1.0F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, -0.5F, -0.5F, -0.5F, 1 );
        
        // render blade
        
        renderBlocks.setRenderBounds( 0.5F - m_fBladeHalfLength, 0.001F, 0.5F - m_fBladeHalfWidth, 
	    		0.5F + m_fBladeHalfLength, 1.0F, 0.5F + m_fBladeHalfWidth );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, this, -0.5F, -0.5F, -0.5F, m_IconBladeOff );
   }    
}