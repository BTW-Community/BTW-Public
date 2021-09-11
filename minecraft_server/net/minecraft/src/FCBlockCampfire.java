// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCampfire extends BlockContainer
{
	public final int m_iFireLevel;
	
	public static final int m_iCampfireFuelStateNormal = 0;
	public static final int m_iCampfireFuelStateBurnedOut = 1;
	public static final int m_iCampfireFuelStateSmouldering = 2;
	
    private FCModelBlockCampfire m_modelCampfire = new FCModelBlockCampfire();
    
    private FCModelBlock m_modelCollisionBase;
    private FCModelBlock m_modelCollisionWithSpit;
    
    public static FCBlockCampfire[] m_fireLevelBlockArray = new FCBlockCampfire[4];
    
    public static boolean m_bCampfireChangingState = false; // temporarily true when block is being changed from one block ID to another
	
    private static final float m_fSpitThickness = ( 1F / 16F );
    private static final float m_fHalfSpitThickness = ( m_fSpitThickness / 2F );
    private static final float m_fSpitHeight = ( 12F / 16F );
    private static final float m_fSpitMinY = ( m_fSpitHeight - m_fHalfSpitThickness );
    private static final float m_fSpitMaxY = ( m_fSpitMinY + m_fSpitThickness );
    
    private static final float m_fSpitSupportWidth= ( 1F / 16F );
    private static final float m_fHalfSpitSupportWidth = ( m_fSpitSupportWidth / 2F );
    private static final float m_fSpitSupportBorder = ( 0.5F / 16F );
    
    private static final float m_fSpitForkWidth = ( 1F / 16F );
    private static final float m_fSpitForkHeight = ( 3F / 16F );
    private static final float m_fSpitForkHeightOffset = ( 1F / 16F );
    private static final float m_fSpitForkMinY = ( m_fSpitMinY - m_fSpitForkHeightOffset );
    private static final float m_fSpitForkMaxY = ( m_fSpitForkMinY + m_fSpitForkHeight );
    
    private static final double m_dSpitCollisionHeight = ( m_fSpitHeight + 1.5D / 16D );
    private static final double m_dSpitCollisionWidth = ( 3D / 16D );
    private static final double m_dSpitCollisionHalfWidth = ( m_dSpitCollisionWidth / 2D );
    
    public FCBlockCampfire( int iBlockID, int iFireLevel )
    {
        super( iBlockID, Material.circuits );
        
        m_iFireLevel = iFireLevel;
        
        m_fireLevelBlockArray[iFireLevel] = this;
        
        setHardness( 0.1F );        
		
        SetBuoyant();
    	SetFurnaceBurnTime( 4 * FCEnumFurnaceBurnTime.SHAFT.m_iBurnTime );  	
        
        setStepSound( soundWoodFootstep );        
        
        setUnlocalizedName( "fcBlockCampfire" );
        
        InitModels();        
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityCampfire();
    }

	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		if ( !m_bCampfireChangingState )
		{
	        FCTileEntityCampfire tileEntity = (FCTileEntityCampfire)world.getBlockTileEntity( 
	        	i, j, k );
	        
	        tileEntity.EjectContents();
	        
	        // only called when not changing state as super kills the tile entity	        
	        super.breakBlock( world, i, j, k, iBlockID, iMetadata );	        
		}        
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
		return null;
	}
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( GetHasSpit( blockAccess, i, j, k ) )
		{
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0D, 0D, 0D, 1D, m_dSpitCollisionHeight, 1D );
		}
		else
		{
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0D, 0D, 0D, 1D, 0.5D, 1D );
		}
    }
	
	@Override
    protected boolean canSilkHarvest()
    {
        return false;
    }    

	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
		{
			return false;
		}
		
        return super.canPlaceBlockAt( world, i, j, k );
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetIAligned( iMetadata, IsFacingIAligned( iFacing ) );
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack )
	{
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed( entityLiving );
		
		SetIAligned( world, i, j, k, IsFacingIAligned( iFacing ) );
	}
	
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
		if ( m_iFireLevel != 0 || GetFuelState( iMetadata ) != m_iCampfireFuelStateNormal )
		{
			return 0;
		}
		
    	return super.idDropped( iMetadata, rand, iFortuneModifier );
    }
	
	@Override
    public int tickRate( World world )
    {
        return FCBlockFalling.m_iFallingBlockTickRate;
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
        {
        	// schedudle a block update to destroy rather than doing it immediately, as
        	// this can occur during the tileEntity's updateEntity() if, for example,
        	// and ice block under the campfire is melted

            world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
        }
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, 
        	i, j - 1, k, 1, true ) )
        {
        	if ( m_iFireLevel > 0 )
        	{
        		world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 1 );
        	}
			
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockToAir( i, j, k );
        }
    }
	
	@Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
		return m_iFireLevel == 0 && GetFuelState( blockAccess, i, j, k ) == 
			m_iCampfireFuelStateNormal;
    }
    
	@Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		if ( GetCanBeSetOnFireDirectly( world, i, j, k ) )
		{
			if ( !IsRainingOnCampfire( world, i, j, k ) )
			{
				ChangeFireLevel( world, i, j, k, 1, world.getBlockMetadata( i, j, k ) );
				
		        FCTileEntityCampfire tileEntity = (FCTileEntityCampfire)world.getBlockTileEntity( 
		        	i, j, k );
		        
		        tileEntity.OnFirstLit();
		        
	            world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, 
	            	"mob.ghast.fireball", 1F, world.rand.nextFloat() * 0.4F + 0.8F );
	            
	            if ( !Block.portal.tryToCreatePortal( world, i, j, k ) )
            	{
	            	// FCTODO: A bit hacky here.  Should probably be a general way to start a 
	            	// bigger fire atop flammable blocks
	            	
	            	int iBlockBelowID = world.getBlockId( i, j - 1, k );
	            	
	            	if ( iBlockBelowID == Block.netherrack.blockID ||
	            		iBlockBelowID ==  FCBetterThanWolves.fcBlockNetherrackFalling.blockID )
	            	{
	            		world.setBlockWithNotify( i, j, k, Block.fire.blockID );
	            	}
            	}
			}
			else
			{
	            world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );            
			}
            
            return true;
		}
		
		return false;
    }
    
	@Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( m_iFireLevel == 0 && GetFuelState( blockAccess, i, j, k ) == 
			m_iCampfireFuelStateNormal )
		{
			return 60; // same chance as leaves and other highly flammable objects
		}
		
		return 0;
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	ItemStack stack = player.getCurrentEquippedItem();
    	
    	if ( stack != null )
    	{
    		Item item = stack.getItem();
    		
    		if ( !GetHasSpit( world, i, j, k ) )
    		{
	    		if ( item == FCBetterThanWolves.fcItemChiselWood )
				{
	    			SetHasSpit( world, i, j, k, true );
	    			
	                FCTileEntityCampfire tileEntity = 
	                	(FCTileEntityCampfire)world.getBlockTileEntity( i, j, k );
	                
	                tileEntity.SetSpitStack( stack );
	                
	    			stack.stackSize--;
	    			
	    			return true;
				}
    		}
    		else
    		{
                FCTileEntityCampfire tileEntity = 
                	(FCTileEntityCampfire)world.getBlockTileEntity( i, j, k );
                
                ItemStack cookStack = tileEntity.GetCookStack();
                
    			if ( cookStack == null )
    			{
    				if ( IsValidCookItem( stack ) )
    				{
    	                ItemStack spitStack = tileEntity.GetSpitStack();
    	                
    	                if ( spitStack.getItemDamage() == 0 )
    	                {
    	                	tileEntity.SetCookStack( stack );
    	                }
    	                else
    	                {
    	                	// break the damaged spit when the player attempts to place an item on it
    	                	// this is to discourage early game exploits involving half damaged sticks.
    	                	
							tileEntity.SetSpitStack( null );
							
							SetHasSpit( world, i, j, k, false );
							
    		    			if ( !world.isRemote )
    		    			{
	    	                	ItemStack ejectStack = stack.copy();
	    	        	    	
	    	                	ejectStack.stackSize = 1;
	    	                	
	    	        			FCUtilsItem.EjectStackWithRandomOffset( world, i, j, k, 
	    	        				ejectStack );
	    	        			
	    	        			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, 
	    	        				FCBetterThanWolves.fcItemSawDust.itemID, 0 );	    	        			
	    	        			
    		        	        world.playAuxSFX( FCBetterThanWolves.m_iWoodBlockDestroyedAuxFXID, 
    		        	        	i, j, k, 0 );
    		    			}
    	                }
	    				
		    			stack.stackSize--;
		    			
		    			return true;
    				}
    			}
    			else if ( cookStack.itemID == stack.itemID && 
    				stack.stackSize < stack.getMaxStackSize() )
    			{
    	            player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F, 
    	        		( ( player.rand.nextFloat() - player.rand.nextFloat() ) * 0.7F + 1F ) * 2F );
    	            
    				stack.stackSize++;
    				
    				tileEntity.SetCookStack( null );
    				
    				return true;
    			}
    		}
    		
    		if ( m_iFireLevel > 0 || GetFuelState( world, i, j, k ) == 
    			m_iCampfireFuelStateSmouldering )
    		{	    		
    			int iItemDamage = stack.getItemDamage();
    			
	    		if ( item.GetCanBeFedDirectlyIntoCampfire( iItemDamage ) ) 
	    		{
	    			if ( !world.isRemote )
	    			{
		                FCTileEntityCampfire tileEntity = 
		                	(FCTileEntityCampfire)world.getBlockTileEntity( i, j, k );
		                
		                world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "mob.ghast.fireball", 
		                	0.2F + world.rand.nextFloat() * 0.1F, 
		                	world.rand.nextFloat() * 0.25F + 1.25F );
		                
		                tileEntity.AddBurnTime( item.GetCampfireBurnTime( iItemDamage ) );
	    			}
	                
	    			stack.stackSize--;
	    			
	    			return true;
	    		}
    		}
    	}
    	else // empty hand
    	{
            FCTileEntityCampfire tileEntity = 
            	(FCTileEntityCampfire)world.getBlockTileEntity( i, j, k );
            
            ItemStack cookStack = tileEntity.GetCookStack();
            
			if ( cookStack != null )
			{
				FCUtilsItem.GivePlayerStackOrEject( player, cookStack, i, j, k );
				
				tileEntity.SetCookStack( null );
    			
    			return true;
			}
			else
			{
	            ItemStack spitStack = tileEntity.GetSpitStack();
	            
	            if ( spitStack != null )
	            {
	            	FCUtilsItem.GivePlayerStackOrEject( player, spitStack, i, j, k );

					tileEntity.SetSpitStack( null );
					
					SetHasSpit( world, i, j, k, false );
	    			
	    			return true;
	            }
			}
    	}
		
		return false;
    }
	
	@Override
    public boolean ShouldDeleteTileEntityOnBlockChange( int iNewBlockID )
    {
    	for ( int iTempIndex = 0; iTempIndex < m_fireLevelBlockArray.length; iTempIndex++ )
    	{
    		if ( m_fireLevelBlockArray[iTempIndex].blockID == iNewBlockID )
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }

	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( !world.isRemote && entity.isEntityAlive() && ( m_iFireLevel > 0 || 
			GetFuelState( world, i, j, k ) == m_iCampfireFuelStateSmouldering ) )
		{
			if ( entity instanceof EntityItem )
			{
				EntityItem entityItem = (EntityItem)entity;
	        	ItemStack targetStack = entityItem.getEntityItem();
				Item item = targetStack.getItem();
				int iBurnTime = item.GetCampfireBurnTime( targetStack.getItemDamage() ); 
				
				if ( iBurnTime > 0 )
				{
					iBurnTime *= targetStack.stackSize;
					
	                FCTileEntityCampfire tileEntity = 
	                	(FCTileEntityCampfire)world.getBlockTileEntity( i, j, k );
	                
	                world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "mob.ghast.fireball", 
	                	world.rand.nextFloat() * 0.1F + 0.2F, 
	                	world.rand.nextFloat() * 0.25F + 1.25F );
	               
	                tileEntity.AddBurnTime( iBurnTime );				
	    			
	    			entity.setDead();
				}
			}
		}		
    }

	@Override
    public boolean GetDoesFireDamageToEntities( World world, int i, int j, int k, Entity entity )
    {
		return m_iFireLevel > 2 || ( m_iFireLevel == 2 && entity instanceof EntityLiving );
    }
	
    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return m_iFireLevel > 0;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	FCModelBlock collisionModel = m_modelCollisionBase;
    	
    	if ( GetHasSpit( iMetadata ) )
    	{
    		collisionModel = m_modelCollisionWithSpit;
    	}
    	
    	if ( GetIsIAligned( iMetadata ) )
    	{
    		collisionModel = collisionModel.MakeTemporaryCopy();
    		
    		collisionModel.RotateAroundJToFacing( 4 );
    	}
    	
    	return collisionModel.CollisionRayTrace( world, i, j, k, startRay, endRay );
    }
    
    @Override
	public void OnFluidFlowIntoBlock( World world, int i, int j, int k, BlockFluid newBlock )
	{
    	if ( m_iFireLevel > 0 )
    	{
    		world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
    	}
    	
    	super.OnFluidFlowIntoBlock( world, i, j, k, newBlock );
	}

    @Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, 
    	EntityFallingSand entity )
    {
    	return true;
    }
    
    @Override
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, 
    	EntityFallingSand entity )
    {
    	if ( !world.isRemote && m_iFireLevel > 0 )
    	{
    		world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
    	}
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
		if ( GetIsIAligned( iMetadata ) )
		{
			return 4;
		}
		
		return 2;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		return SetIAligned( iMetadata, IsFacingIAligned( iFacing ) );
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
		SetIAligned( world, i, j, k, !GetIsIAligned( world, i, j, k ) );
		
		return true;
	}

	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		return SetIAligned( iMetadata, !GetIsIAligned( iMetadata ) );
	}

    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return m_iFireLevel == 0 && world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
	//------------- Class Specific Methods ------------//
	
	public void SetIAligned( World world, int i, int j, int k, boolean bIAligned )
	{
		int iMetadata = SetIAligned( world.getBlockMetadata( i, j, k ), bIAligned );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetIAligned( int iMetadata, boolean bIAligned )
	{
		if ( bIAligned )
		{
			iMetadata |= 1;
		}
		else
		{
			iMetadata &= (~1);
		}
		
		return iMetadata;
	}
	
	public boolean GetIsIAligned( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsIAligned( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public boolean GetIsIAligned( int iMetadata )
	{
		return ( iMetadata & 1 ) != 0;
	}
	
	public boolean IsFacingIAligned( int iFacing )
	{
		return iFacing >= 4;
	}
	
	public void SetHasSpit( World world, int i, int j, int k, boolean bHasSpit )
	{
		int iMetadata = SetHasSpit( world.getBlockMetadata( i, j, k ), bHasSpit );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetHasSpit( int iMetadata, boolean bHasSpit )
	{
		if ( bHasSpit )
		{
			iMetadata |= 2;
		}
		else
		{
			iMetadata &= (~2);
		}
		
		return iMetadata;
	}
    
	public boolean GetHasSpit( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetHasSpit( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public boolean GetHasSpit( int iMetadata )
	{
		return ( iMetadata & 2 ) != 0;
	}
	
	public void SetFuelState( World world, int i, int j, int k, int iCampfireState )
	{
		int iMetadata = SetFuelState( world.getBlockMetadata( i, j, k ), iCampfireState );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetFuelState( int iMetadata, int iCampfireState )
	{
		iMetadata &= ~12; // filter out old state
		
		return iMetadata | ( iCampfireState << 2 );
	}
    
	public int GetFuelState( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetFuelState( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public int GetFuelState( int iMetadata )
	{
		return ( iMetadata & 12 ) >> 2;
	}
	
	public boolean IsValidCookItem( ItemStack stack )
	{
		if ( FCCraftingManagerCampfire.instance.GetRecipeResult( stack.getItem().itemID ) != null )
		{
			return true;
		}
		
		return false;
	}
	
	public void ExtinguishFire( World world, int i, int j, int k, boolean bSmoulder )
	{
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( bSmoulder )
		{
			iMetadata = SetFuelState( iMetadata, m_iCampfireFuelStateSmouldering );
		}
		else
		{
			iMetadata = SetFuelState( iMetadata, m_iCampfireFuelStateBurnedOut );
		}
		
		ChangeFireLevel( world, i, j, k, 0, iMetadata ); 

    	if ( !world.isRemote )
    	{
			world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 1 );
    	}
	}
	
	public void RelightFire( World world, int i, int j, int k )
	{
		ChangeFireLevel( world, i, j, k, 1, SetFuelState( world.getBlockMetadata( i, j, k ), 
			m_iCampfireFuelStateNormal ) ); 
	}
	
	public void StopSmouldering( World world, int i, int j, int k )
	{
		SetFuelState( world, i, j, k, m_iCampfireFuelStateBurnedOut );
	}
	
	public void ChangeFireLevel( World world, int i, int j, int k, int iFireLevel, int iMetadata )
	{
		FCBlockCampfire.m_bCampfireChangingState = true;
		
        world.setBlockAndMetadataWithNotify( i, j, k, 
        	FCBlockCampfire.m_fireLevelBlockArray[iFireLevel].blockID, 
        	iMetadata );
        
		FCBlockCampfire.m_bCampfireChangingState = false;		
	}
	
    public boolean IsRainingOnCampfire( World world, int i, int j, int k )
    {
    	return world.IsRainingAtPos( i, j, k );
    }
    
    private void InitModels()
    {
        m_modelCollisionBase = new FCModelBlock();
        m_modelCollisionWithSpit = new FCModelBlock();

        m_modelCollisionBase.AddBox( 0D, 0D, 0D, 1D, 0.5D, 1D );
        m_modelCollisionWithSpit.AddBox( 0D, 0D, 0D, 1D, 0.5D, 1D );
        
        m_modelCollisionWithSpit.AddBox( 0D, 0D, 0.5D - m_dSpitCollisionHalfWidth, 
        	1D, m_dSpitCollisionHeight, 0.5D + m_dSpitCollisionHalfWidth );        
    }
    
	//----------- Client Side Functionality -----------//
}