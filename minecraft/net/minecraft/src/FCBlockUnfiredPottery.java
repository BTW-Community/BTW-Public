// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockUnfiredPottery extends Block
{
    public final static int m_iNumSubtypes = 14;
    public static final int m_iNumSpunPotteryTypes = 4;    
    
    public static final int m_iSubtypeCrucible = 0;
    public static final int m_iSubtypePlanter = 1;
    public static final int m_iSubtypeVase = 2;
    public static final int m_iSubtypeUrn = 3;
    public static final int m_iSubtypeMould = 4;
    public static final int m_iSubtypeClayBrick = 5;
    public static final int m_iSubtypeClayBrickIAligned = 6;
    public static final int m_iSubtypeNetherBrick = 7;
    public static final int m_iSubtypeNetherBrickIAligned = 8;
    public static final int m_iSubtypeUncookedCake = 9;
    public static final int m_iSubtypeUncookedCookies = 10;
    public static final int m_iSubtypeUncookedCookiesIAligned = 11;
    public static final int m_iSubtypeUncookedPumpkinPie = 12;
    public static final int m_iSubtypeUncookedBread = 13;
    public static final int m_iSubtypeUncookedBreadIAligned = 14;
    
    public final static int m_iRotationsOnTurntableToChangState = 8;
    
	public static final float m_fUnfiredPotteryUrnBaseWidth = ( 4.0F / 16F );
	public static final float m_fUnfiredPotteryUrnBaseHalfWidth = m_fUnfiredPotteryUrnBaseWidth / 2.0F;
	public static final float m_fUnfiredPotteryUrnBaseHeight = ( 1.0F / 16F );
	public static final float m_fUnfiredPotteryUrnBodyWidth = ( 6.0F / 16F );
	public static final float m_fUnfiredPotteryUrnBodyHalfWidth = m_fUnfiredPotteryUrnBodyWidth / 2.0F;
	public static final float m_fUnfiredPotteryUrnBodyHeight = ( 6.0F / 16F );
	public static final float m_fUnfiredPotteryUrnNeckWidth = ( 4.0F / 16F );
	public static final float m_fUnfiredPotteryUrnNeckHalfWidth = m_fUnfiredPotteryUrnNeckWidth / 2.0F;
	public static final float m_fUnfiredPotteryUrnNeckHeight = ( 1.0F / 16F );
	public static final float m_fUnfiredPotteryUrnTopWidth = ( 6.0F / 16F );
	public static final float m_fUnfiredPotteryUrnTopHalfWidth = m_fUnfiredPotteryUrnTopWidth / 2.0F;
	public static final float m_fUnfiredPotteryUrnTopHeight = ( 1.0F / 16F );
	public static final float m_fUnfiredPotteryUrnLidWidth = ( 4.0F / 16F );
	public static final float m_fUnfiredPotteryUrnLidHalfWidth = m_fUnfiredPotteryUrnLidWidth / 2.0F;
	public static final float m_fUnfiredPotteryUrnLidHeight = ( 1.0F / 16F );
	public static final float m_fUnfiredPotteryUrnHeight = ( m_fUnfiredPotteryUrnBaseHeight + 
		m_fUnfiredPotteryUrnBodyHeight + m_fUnfiredPotteryUrnNeckHeight + m_fUnfiredPotteryUrnTopHeight +
		m_fUnfiredPotteryUrnLidHeight );
	
	public static final float m_fUnfiredPotteryMouldHeight = ( 2F / 16F );
	public static final float m_fUnfiredPotteryMouldWidth = ( 6F / 16F );
	public static final float m_fUnfiredPotteryMouldHalfWidth = m_fUnfiredPotteryMouldWidth / 2F;
	
	public static final float m_fUnfiredPotteryBrickHeight = ( 4F / 16F );
	public static final float m_fUnfiredPotteryBrickWidth = ( 6F / 16F );
	public static final float m_fUnfiredPotteryBrickHalfWidth = ( m_fUnfiredPotteryBrickWidth / 2F );
	public static final float m_fUnfiredPotteryBrickLength = ( 12F / 16F );
	public static final float m_fUnfiredPotteryBrickHalfLength = ( m_fUnfiredPotteryBrickLength / 2F );
	
	public static final float m_fUnfiredPotteryUncookedCakeHeight = ( 8F / 16F );
	public static final float m_fUnfiredPotteryUncookedCakeWidth = ( 14F / 16F );
	public static final float m_fUnfiredPotteryUncookedCakeHalfWidth = ( m_fUnfiredPotteryUncookedCakeWidth / 2F );
	public static final float m_fUnfiredPotteryUncookedCakeLength = ( 14F / 16F );
	public static final float m_fUnfiredPotteryUncookedCakeHalfLength = ( m_fUnfiredPotteryUncookedCakeLength / 2F );
	
	public static final float m_fUnfiredPotteryUncookedCookiesHeight = ( 1F / 16F );
	public static final float m_fUnfiredPotteryUncookedCookiesWidth = ( 6F / 16F );
	public static final float m_fUnfiredPotteryUncookedCookiesHalfWidth = ( m_fUnfiredPotteryUncookedCookiesWidth / 2F );
	public static final float m_fUnfiredPotteryUncookedCookiesLength = ( 14F / 16F );
	public static final float m_fUnfiredPotteryUncookedCookiesHalfLength = ( m_fUnfiredPotteryUncookedCookiesLength / 2F );
	
	public static final float m_fUnfiredPotteryUncookedCookiesIndividualWidth = ( 2F / 16F );
	public static final float m_fUnfiredPotteryUncookedCookiesIndividualHalfWidth = ( m_fUnfiredPotteryUncookedCookiesIndividualWidth / 2F );
	
	public static final float m_fUnfiredPotteryUncookedPumpkinPieHeight = ( 4F / 16F );
	public static final float m_fUnfiredPotteryUncookedPumpkinPieWidth = ( 12F / 16F );
	public static final float m_fUnfiredPotteryUncookedPumpkinPieHalfWidth = ( m_fUnfiredPotteryUncookedPumpkinPieWidth / 2F );
	public static final float m_fUnfiredPotteryUncookedPumpkinPieLength = ( 12F / 16F );
	public static final float m_fUnfiredPotteryUncookedPumpkinPieHalfLength = ( m_fUnfiredPotteryUncookedPumpkinPieLength / 2F );	
	
    public FCBlockUnfiredPottery( int iBlockID )
    {
        super( iBlockID, Material.clay );  
        
        setHardness( 0.6F );
        SetShovelsEffectiveOn( true );
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );        
        
		SetCanBeCookedByKiln( true );
        
        setUnlocalizedName( "fcBlockUnfiredPottery" );
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
		// check beneath for valid block due to piston pushing not sending a notify
		if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
		{
	        dropBlockAsItem(world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
	        world.setBlockWithNotify(i, j, k, 0);
		}
    }
	
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		if ( iFacing == 4 || iFacing == 5 )
		{
			if ( iMetadata == m_iSubtypeClayBrick )
			{
				iMetadata = m_iSubtypeClayBrickIAligned;
			}
			else if ( iMetadata == m_iSubtypeNetherBrick )
			{
				iMetadata = m_iSubtypeNetherBrickIAligned;
			}
			else if ( iMetadata == m_iSubtypeUncookedCookies )
			{
				iMetadata = m_iSubtypeUncookedCookiesIAligned;
			}
			else if ( iMetadata == m_iSubtypeUncookedBread )
			{
				iMetadata = m_iSubtypeUncookedBreadIAligned;
			}
		}
		
		return iMetadata;
    }
	
	@Override
    public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving placingEntity, ItemStack stack )
    {
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed( placingEntity );

		if ( iFacing == 4 || iFacing == 5 )
		{
			int iMetadata = world.getBlockMetadata( i, j, k );
			
			if ( iMetadata == m_iSubtypeClayBrick )
			{
				world.setBlockMetadataWithNotify( i, j, k, m_iSubtypeClayBrickIAligned );
			}
			else if ( iMetadata == m_iSubtypeNetherBrick )
			{
				world.setBlockMetadataWithNotify( i, j, k, m_iSubtypeNetherBrickIAligned );
			}
			else if ( iMetadata == m_iSubtypeUncookedCookies )
			{
				world.setBlockMetadataWithNotify( i, j, k, m_iSubtypeUncookedCookiesIAligned );
			}
			else if ( iMetadata == m_iSubtypeUncookedBread )
			{
				world.setBlockMetadataWithNotify( i, j, k, m_iSubtypeUncookedBreadIAligned );
			}
		}			
    }
    
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		if ( iMetadata == m_iSubtypeClayBrick || iMetadata == m_iSubtypeClayBrickIAligned )
		{
			return Item.clay.itemID;
		}
		else if ( iMetadata == m_iSubtypeNetherBrick || iMetadata == m_iSubtypeNetherBrickIAligned )
		{
			return FCBetterThanWolves.fcItemNetherSludge.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedCake )
		{
			return FCBetterThanWolves.fcItemPastryUncookedCake.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedCookies || iMetadata == m_iSubtypeUncookedCookiesIAligned )
		{
			return FCBetterThanWolves.fcItemPastryUncookedCookies.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedPumpkinPie )
		{
			return FCBetterThanWolves.fcItemPastryUncookedPumpkinPie.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedBread || iMetadata == m_iSubtypeUncookedBreadIAligned )
		{
			return FCBetterThanWolves.fcItemBreadDough.itemID;
		}
	    
	    return blockID;
    }
	
	@Override
    public int damageDropped( int iMetadata )
    {
		if ( iMetadata < m_iNumSpunPotteryTypes )
		{
	        return iMetadata; 
		}
		
		return 0;
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
    	int iMetaData = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iMetaData )
    	{
			case m_iSubtypeCrucible:
			case m_iSubtypePlanter:
				
				break;
				
			case m_iSubtypeVase:
    			
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		( 0.5F - FCBlockVase.m_fVaseBodyHalfWidth ), 0.0F, 
	        		( 0.5F - FCBlockVase.m_fVaseBodyHalfWidth ), 
	    			( 0.5F + FCBlockVase.m_fVaseBodyHalfWidth ), 1.0F, 
	    			( 0.5F + FCBlockVase.m_fVaseBodyHalfWidth ) );
    			
			case m_iSubtypeUrn:
    			
	        	return AxisAlignedBB.getAABBPool().getAABB(         	
	        		( 0.5F - m_fUnfiredPotteryUrnBodyHalfWidth ), 0.0F, 
	        		( 0.5F - m_fUnfiredPotteryUrnBodyHalfWidth ), 
	        		( 0.5F + m_fUnfiredPotteryUrnBodyHalfWidth ), m_fUnfiredPotteryUrnHeight, 
	        		( 0.5F + m_fUnfiredPotteryUrnBodyHalfWidth ) );
    	    	
    		case m_iSubtypeMould:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryMouldHalfWidth ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryMouldHalfWidth ), 
	        		( 0.5F + m_fUnfiredPotteryMouldHalfWidth ), m_fUnfiredPotteryMouldHeight, 
	        		( 0.5F + m_fUnfiredPotteryMouldHalfWidth ) );
        	    	
    		case m_iSubtypeClayBrick:
    		case m_iSubtypeNetherBrick:
    		case m_iSubtypeUncookedBread:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryBrickHalfWidth ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryBrickHalfLength ), 
	        		( 0.5F + m_fUnfiredPotteryBrickHalfWidth ), m_fUnfiredPotteryBrickHeight, 
	        		( 0.5F + m_fUnfiredPotteryBrickHalfLength ) );
    	    	
    		case m_iSubtypeClayBrickIAligned:
    		case m_iSubtypeNetherBrickIAligned:
    		case m_iSubtypeUncookedBreadIAligned:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryBrickHalfLength ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryBrickHalfWidth ), 
	        		( 0.5F + m_fUnfiredPotteryBrickHalfLength ), m_fUnfiredPotteryBrickHeight, 
	        		( 0.5F + m_fUnfiredPotteryBrickHalfWidth ) );
    	    	
    		case m_iSubtypeUncookedCake:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryUncookedCakeHalfWidth ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryUncookedCakeHalfLength ), 
	        		( 0.5F + m_fUnfiredPotteryUncookedCakeHalfWidth ), 
	        		m_fUnfiredPotteryUncookedCakeHeight, 
	        		( 0.5F + m_fUnfiredPotteryUncookedCakeHalfLength ) );
    	    	
    		case m_iSubtypeUncookedCookies:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryUncookedCookiesHalfWidth ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryUncookedCookiesHalfLength ), 
	        		( 0.5F + m_fUnfiredPotteryUncookedCookiesHalfWidth ), 
	        		m_fUnfiredPotteryUncookedCookiesHeight, 
	        		( 0.5F + m_fUnfiredPotteryUncookedCookiesHalfLength ) );
    			
    		case m_iSubtypeUncookedCookiesIAligned:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryUncookedCookiesHalfLength ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryUncookedCookiesHalfWidth ), 
	        		( 0.5F + m_fUnfiredPotteryUncookedCookiesHalfLength ), 
	        		m_fUnfiredPotteryUncookedCookiesHeight, 
	        		( 0.5F + m_fUnfiredPotteryUncookedCookiesHalfWidth ) );
    	    	
    		case m_iSubtypeUncookedPumpkinPie:
    			
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		( 0.5F - m_fUnfiredPotteryUncookedPumpkinPieHalfWidth ), 0.0F, 
            		( 0.5F - m_fUnfiredPotteryUncookedPumpkinPieHalfLength ), 
	        		( 0.5F + m_fUnfiredPotteryUncookedPumpkinPieHalfWidth ), 
	        		m_fUnfiredPotteryUncookedPumpkinPieHeight, 
	        		( 0.5F + m_fUnfiredPotteryUncookedPumpkinPieHalfLength ) );
    	}
    	
    	return AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, 1D, 1D );
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
		return FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true );
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
    	{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockToAir( i, j, k );
    	}
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
    	{
    		// Unfired pottery can both be pushed by pistons and needs to rest on a block, which can create weird
    		// interactions if the block below is pushed at the same time as this one, 
    		// creating a ghost block on the client. Delay the popping of the block to next tick prevent this.

    		if( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
    		{
    			world.scheduleBlockUpdate( i, j, k, blockID, 1 );
    		}
    	}
    }
    
    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iMetadata )
    	{
    		case m_iSubtypeCrucible:
    		case m_iSubtypePlanter:
    		case m_iSubtypeVase:
    		case m_iSubtypeUrn:    			
    		case m_iSubtypeMould:    			
    		case m_iSubtypeClayBrick:
    		case m_iSubtypeClayBrickIAligned:    			
    		case m_iSubtypeNetherBrick:
    		case m_iSubtypeNetherBrickIAligned:
    			
    			return  4;    			
    	}    	
    	
    	return 1;
    }
    
	@Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iMetadata )
    	{
    		case m_iSubtypeCrucible:
    			
    			return FCBetterThanWolves.fcCrucible.blockID;
    			
    		case m_iSubtypePlanter:
    			
    			return FCBetterThanWolves.fcPlanter.blockID;
    			
    		case m_iSubtypeVase:
    			
    			return  FCBetterThanWolves.fcVase.blockID;
    			
    		case m_iSubtypeUrn:
    			
    			return  FCBetterThanWolves.fcItemUrn.itemID;
    			
    		case m_iSubtypeMould:
    			
    			return  FCBetterThanWolves.fcItemMould.itemID;
    			
    		case m_iSubtypeClayBrick:
    		case m_iSubtypeClayBrickIAligned:
    			
    			return Item.brick.itemID;
    			
    		case m_iSubtypeUncookedCake:
    			
    			return  Item.cake.itemID;
    			
    		case m_iSubtypeUncookedPumpkinPie:
    			
    			return  Item.pumpkinPie.itemID;
    			
    		case m_iSubtypeNetherBrick:
    		case m_iSubtypeNetherBrickIAligned:
    			
    			return  FCBetterThanWolves.fcItemNetherBrick.itemID;
    			
    		case m_iSubtypeUncookedBread:
    		case m_iSubtypeUncookedBreadIAligned:
    			
    			return  Item.bread.itemID;
    	}
    	
    	return -1;
    }

	@Override
    public void OnCookedByKiln( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( iMetadata == m_iSubtypeUncookedCookies || iMetadata == m_iSubtypeUncookedCookiesIAligned )
    	{
        	world.setBlockToAir( i, j, k );

        	for ( int iTempCount = 0; iTempCount < 8; iTempCount++ )
        	{
        		FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, Item.cookie.itemID, 0 );
        	}        	
    	}
    	else
    	{
    		super.OnCookedByKiln( world, i, j, k );
    	}    	
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( iMetadata == m_iSubtypeVase )
    	{
    		return FCBetterThanWolves.fcVase.collisionRayTrace( world, i, j, k, startRay, endRay );
    	}
    	else if ( iMetadata == m_iSubtypePlanter )
    	{
    		return FCBetterThanWolves.fcPlanter.collisionRayTrace( world, i, j, k, startRay, endRay );
    	}
    	
    	return super.collisionRayTrace( world, i, j, k, startRay, endRay );    	
    }
    
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		if ( iMetadata == m_iSubtypeClayBrick )
		{
			iMetadata = m_iSubtypeClayBrickIAligned;
		}
		else if ( iMetadata == m_iSubtypeClayBrickIAligned )
		{
			iMetadata = m_iSubtypeClayBrick; 
		}
		else if ( iMetadata == m_iSubtypeNetherBrick )
		{
			iMetadata = m_iSubtypeNetherBrickIAligned;
		}
		else if ( iMetadata == m_iSubtypeNetherBrickIAligned )
		{
			iMetadata = m_iSubtypeNetherBrick;
		}
		else if ( iMetadata == m_iSubtypeUncookedCookies )
		{
			iMetadata = m_iSubtypeUncookedCookiesIAligned;
		}
		else if ( iMetadata == m_iSubtypeUncookedCookiesIAligned )
		{
			iMetadata = m_iSubtypeUncookedCookies;
		}
		else if ( iMetadata == m_iSubtypeUncookedBread )
		{
			iMetadata = m_iSubtypeUncookedBreadIAligned;
		}
		else if ( iMetadata == m_iSubtypeUncookedBreadIAligned )
		{
			iMetadata = m_iSubtypeUncookedBread;
		}
		
		return iMetadata;			
	}

	@Override
	public void OnRotatedOnTurntable( World world, int i, int j, int k )
	{
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( iMetadata < m_iNumSpunPotteryTypes )
		{
			world.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, i, j, k, blockID );
		}
	}	
	
	@Override
	public int RotateOnTurntable( World world, int i, int j, int k, boolean bReverse, int iCraftingCounter )
	{
		iCraftingCounter = super.RotateOnTurntable( world, i, j, k, bReverse, iCraftingCounter );
		
		iCraftingCounter = TurntableCraftingRotation( world, i, j, k, bReverse, iCraftingCounter );
		
		return iCraftingCounter;
	}
	
	@Override
	public int GetRotationsToCraftOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return m_iRotationsOnTurntableToChangState;
	}
	
	@Override
	public int GetNewBlockIDCraftedOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iMetaData = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( iMetaData < m_iNumSpunPotteryTypes - 1 )
    	{
    		return blockID;
    	}
    	else
    	{
    		return 0;
    	}    	
	}
	
	@Override
	public int GetNewMetadataCraftedOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iMetaData = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( iMetaData < m_iNumSpunPotteryTypes - 1 )
    	{
    		return iMetaData + 1;
    	}
    	else
    	{
    		return 0;
    	}    	
	}
	
	@Override
	public int GetItemIDCraftedOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iMetadata = blockAccess.getBlockMetadata( i, j, k );
		
		if ( iMetadata == m_iSubtypeNetherBrick || iMetadata == m_iSubtypeNetherBrickIAligned )
		{
			return FCBetterThanWolves.fcItemNetherSludge.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedCake )
		{
			return FCBetterThanWolves.fcItemPastryUncookedCake.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedCookies || iMetadata == m_iSubtypeUncookedCookiesIAligned )
		{
			return FCBetterThanWolves.fcItemPastryUncookedCookies.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedPumpkinPie )
		{
			return FCBetterThanWolves.fcItemPastryUncookedPumpkinPie.itemID;
		}
		else if ( iMetadata == m_iSubtypeUncookedBread || iMetadata == m_iSubtypeUncookedBreadIAligned )
		{
			return FCBetterThanWolves.fcItemBreadDough.itemID;
		}
		
		return Item.clay.itemID;
	}
	
	@Override
	public int GetItemCountCraftedOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iMetaData = blockAccess.getBlockMetadata( i, j, k );
    	
		if ( iMetaData == m_iSubtypeCrucible ||
			iMetaData == m_iSubtypePlanter ||
			iMetaData == m_iSubtypeVase ||
			iMetaData == m_iSubtypeUrn )
		{
			return 2;
		}
		
		return 1;
	}

	@Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
	
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
		if ( iSubtype == m_iSubtypeVase || iSubtype == m_iSubtypeUrn )
		{
			return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
		}
		
		return super.CanGroundCoverRestOnBlock( world, i, j, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
		if ( iSubtype == m_iSubtypeVase || iSubtype == m_iSubtypeUrn )
		{
			return -1F;
		}
		
		return super.GroundCoverRestingOnVisualOffset( blockAccess, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
	
    private Icon m_IconNetherSludge;
    private Icon m_IconUncookedPastry;
    private Icon m_IconUncookedPumpkinPieTop;
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }	
	
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_IconNetherSludge = register.registerIcon( "fcBlockNetherSludge" );
        m_IconUncookedPastry = register.registerIcon( "fcBlockPastryUncooked" );
        m_IconUncookedPumpkinPieTop = register.registerIcon( "fcBlockUncookedPumpkinPie_top" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	if ( iMetadata == m_iSubtypeNetherBrick || iMetadata == m_iSubtypeNetherBrickIAligned )
    	{
    		return m_IconNetherSludge;
    	}
    	else if ( iMetadata >= m_iSubtypeUncookedCake )
    	{
    		if ( iSide == 1 )
    		{
	    		if ( iMetadata == m_iSubtypeUncookedPumpkinPie )
	    		{
	    			return m_IconUncookedPumpkinPieTop;
	    		}
    		}
    		
    		return m_IconUncookedPastry;
    	}
    	
    	return blockIcon;
    }
    	
	@Override
	public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeCrucible ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypePlanter ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeVase ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeUrn ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeMould ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeClayBrick ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeNetherBrick ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeUncookedCake ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeUncookedCookies ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeUncookedPumpkinPie ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeUncookedBread ) );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	int iMetaData = blockAccess.getBlockMetadata( i, j, k );
    	
    	switch ( iMetaData )
    	{
    		case m_iSubtypeCrucible:
    			
    			Icon crucibleTexture = getBlockTexture( blockAccess, i, j, k, 0 );
    			
    			RenderUnfiredCrucible( renderBlocks, blockAccess, i, j, k, this, crucibleTexture );
    			
    			break;
    			
    		case m_iSubtypePlanter:
    			
    			FCBlockPlanter.RenderEmptyPlanterBlock( renderBlocks, blockAccess, i, j, k, this );
    			
    			break;
    			
    		case m_iSubtypeVase:
    			
    			FCBlockVase.RenderVaseBlock( renderBlocks, blockAccess, i, j, k, this );
    			
    			break;
    			
    		case m_iSubtypeUrn:

    			Icon urnTexture = getBlockTexture( blockAccess, i, j, k, 0 );
    			
    			RenderUnfiredUrn( renderBlocks, blockAccess, i, j, k, this, urnTexture, 0.0F );
    			
    			break;    			
    			
			case m_iSubtypeUncookedCookies:
			case m_iSubtypeUncookedCookiesIAligned:
				
				RenderUncookedCookies( renderBlocks, blockAccess, i, j, k, this, ( iMetaData == m_iSubtypeUncookedCookiesIAligned ) );
				
    			break;    			
    			
    		//case m_iSubtypeMould:
    		//case m_iSubtypeClayBrick:
    		//case m_iSubtypeClayBrickIAligned:
    		//case m_iSubtypeNetherBrick:
        	//case m_iSubtypeNetherBrickIAligned:
			//case m_iSubtypeUncookedCake:
			//case m_iSubtypeUncookedPumpkinPie:
    		//case m_iSubtypeUncookedBread:
    		//case m_iSubtypeUncookedBreadIAligned:
    		    
			default:
				
				super.RenderBlock( renderBlocks, i, j, k );
		    	
		    	break;    		        
    	}
		
        return true;
    }
    
    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
    {
        RenderCookingByKilnOverlay( renderBlocks, i, j, k, bFirstPassResult );
    }
    
    static public boolean RenderUnfiredUrn
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block,
    	Icon texture,
    	float fVerticalOffset
    )
    {
    	// render base 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnBaseHalfWidth ), 0.0F + fVerticalOffset, ( 0.5F - m_fUnfiredPotteryUrnBaseHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnBaseHalfWidth ), m_fUnfiredPotteryUrnBaseHeight + fVerticalOffset, ( 0.5F + m_fUnfiredPotteryUrnBaseHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
    	// render body 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnBodyHalfWidth ), m_fUnfiredPotteryUrnBaseHeight + fVerticalOffset, ( 0.5F - m_fUnfiredPotteryUrnBodyHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnBodyHalfWidth ), m_fUnfiredPotteryUrnBaseHeight + m_fUnfiredPotteryUrnBodyHeight + fVerticalOffset, ( 0.5F + m_fUnfiredPotteryUrnBodyHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
    	// render neck 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnNeckHalfWidth ), m_fUnfiredPotteryUrnHeight - ( m_fUnfiredPotteryUrnTopHeight + m_fUnfiredPotteryUrnNeckHeight + m_fUnfiredPotteryUrnLidHeight) + fVerticalOffset, ( 0.5F - m_fUnfiredPotteryUrnNeckHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnNeckHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnTopHeight - m_fUnfiredPotteryUrnLidHeight + fVerticalOffset, ( 0.5F + m_fUnfiredPotteryUrnNeckHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
    	// render top 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnTopHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnTopHeight - m_fUnfiredPotteryUrnLidHeight  + fVerticalOffset, ( 0.5F - m_fUnfiredPotteryUrnTopHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnTopHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnLidHeight + fVerticalOffset, ( 0.5F + m_fUnfiredPotteryUrnTopHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
    	// render lid 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnLidHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnLidHeight + fVerticalOffset, ( 0.5F - m_fUnfiredPotteryUrnLidHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnLidHalfWidth ), m_fUnfiredPotteryUrnHeight + fVerticalOffset, ( 0.5F + m_fUnfiredPotteryUrnLidHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
    	return true;
    }
    
    static public boolean RenderUnfiredCrucible
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block,
    	Icon texture
    )
    {
    	// render sides
    	
        renderBlocks.setRenderBounds( 
    			( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ), 0.0F, ( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ), 
        		( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ) + 0.125F, FCBlockCookingVessel.m_fModelHeight, ( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) - 0.125F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        renderBlocks.setRenderBounds( 
    			( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ), 0.0F, ( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) - 0.125F, 
        		( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) - 0.125F, FCBlockCookingVessel.m_fModelHeight, ( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        renderBlocks.setRenderBounds( 
    			( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) - 0.125F, 0.0F, ( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ) + 0.125F, 
        		( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ), FCBlockCookingVessel.m_fModelHeight, ( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        renderBlocks.setRenderBounds( 
    			( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ) + 0.125F, 0.0F, ( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ), 
        		( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ), FCBlockCookingVessel.m_fModelHeight, ( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ) + 0.125F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        // render bottom
        
        renderBlocks.setRenderBounds( 
    			( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ) + 0.125F, 0.0F, ( 0.5F - FCBlockCookingVessel.m_fModelHalfWidth ) + 0.125F, 
        		( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) - 0.125F, 2.0F / 16F, ( 0.5F + FCBlockCookingVessel.m_fModelHalfWidth ) - 0.125F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
    	// render band around centre
    	
        renderBlocks.setRenderBounds( 
    			0.0F, 0.5F - FCBlockCookingVessel.m_fModelBandHalfHeight, 0.0F, 
	    		0.125F, 0.5F + FCBlockCookingVessel.m_fModelBandHalfHeight, 0.875F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        renderBlocks.setRenderBounds( 
    			0.0F, 0.5F - FCBlockCookingVessel.m_fModelBandHalfHeight, 0.875F, 
	    		0.875F, 0.5F + FCBlockCookingVessel.m_fModelBandHalfHeight, 1.0F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        renderBlocks.setRenderBounds( 
    			0.875F, 0.5F - FCBlockCookingVessel.m_fModelBandHalfHeight, 0.125F, 
	    		1.0F, 0.5F + FCBlockCookingVessel.m_fModelBandHalfHeight, 1.0F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
        
        renderBlocks.setRenderBounds( 
    			0.125F, 0.5F - FCBlockCookingVessel.m_fModelBandHalfHeight, 0.0F, 
	    		1.0F, 0.5F + FCBlockCookingVessel.m_fModelBandHalfHeight, 0.125F );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, texture );
    	
    	return true;
    }
    
    static public boolean RenderUncookedCookies
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block,
    	boolean bIAligned
    )
    {
    	int iCookiesAlongI;
    	int iCookiesAlongK;
    	
    	float fStartCenterX;
    	float fStartCenterZ;
    	
    	if ( !bIAligned )
    	{
        	iCookiesAlongI = 2;
        	iCookiesAlongK = 4;
        	
        	fStartCenterX = 0.5F - m_fUnfiredPotteryUncookedCookiesIndividualWidth;
        	fStartCenterZ = 0.5F -  ( 3 * m_fUnfiredPotteryUncookedCookiesIndividualWidth );
    	}
    	else
    	{
    		iCookiesAlongI = 4;
    		iCookiesAlongK = 2;
    		
        	fStartCenterX = 0.5F -  ( 3 * m_fUnfiredPotteryUncookedCookiesIndividualWidth );
        	fStartCenterZ = 0.5F - m_fUnfiredPotteryUncookedCookiesIndividualWidth;
    	}

    	for ( int iCountAlongI = 0; iCountAlongI < iCookiesAlongI; iCountAlongI++ )
    	{
        	for ( int iCountAlongK = 0; iCountAlongK < iCookiesAlongK; iCountAlongK++ )
        	{
        		float fCenterCookieX = fStartCenterX + ( (float)iCountAlongI  * m_fUnfiredPotteryUncookedCookiesIndividualWidth * 2F );
        		float fCenterCookieZ = fStartCenterZ + ( (float)iCountAlongK  * m_fUnfiredPotteryUncookedCookiesIndividualWidth * 2F );
        		
                renderBlocks.setRenderBounds( 
                	fCenterCookieX - m_fUnfiredPotteryUncookedCookiesIndividualHalfWidth, 0.0F, fCenterCookieZ - m_fUnfiredPotteryUncookedCookiesIndividualHalfWidth, 
                	fCenterCookieX + m_fUnfiredPotteryUncookedCookiesIndividualHalfWidth, m_fUnfiredPotteryUncookedCookiesHeight, fCenterCookieZ + m_fUnfiredPotteryUncookedCookiesIndividualHalfWidth );
            
                renderBlocks.renderStandardBlock( block, i, j, k );                
        	}
    	}
    	
    	return true;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	switch ( iItemDamage )
    	{
			case m_iSubtypeCrucible:			

				RenderUnfiredCrucibleInvBlock( renderBlocks, this, iItemDamage, blockIcon );
    			
    			break;
    			
			case m_iSubtypePlanter:			
    			
				FCBlockPlanter.RenderEmptyPlanterInvBlock( renderBlocks, this, iItemDamage );
    			
    			break;
    			
			case m_iSubtypeVase:
    			
				FCBlockVase.RenderInvBlock( renderBlocks, this, iItemDamage );
    			
    			break;
    			
			case m_iSubtypeUrn:		
    			
				RenderUnfiredUrnInvBlock( renderBlocks, this, iItemDamage, blockIcon );
    			
    			break;
    			
			case m_iSubtypeMould:
				
    	    	renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryMouldHalfWidth ), 0.0F, ( 0.5F - m_fUnfiredPotteryMouldHalfWidth ), 
    	        		( 0.5F + m_fUnfiredPotteryMouldHalfWidth ), m_fUnfiredPotteryMouldHeight, ( 0.5F + m_fUnfiredPotteryMouldHalfWidth ) );
    	    	
    	    	// fall through
    	    	
			default:
				
		        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, this, -0.5F, -0.5F, -0.5F, iItemDamage );
		        
		    	break;    		        
    	}    	
    }
    
    static public void RenderUnfiredUrnInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block, 
		int iItemDamage, 
		Icon texture
	)
    {
    	// render base 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnBaseHalfWidth ), 0.0F, ( 0.5F - m_fUnfiredPotteryUrnBaseHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnBaseHalfWidth ), m_fUnfiredPotteryUrnBaseHeight, ( 0.5F + m_fUnfiredPotteryUrnBaseHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );    
        
    	// render body 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnBodyHalfWidth ), m_fUnfiredPotteryUrnBaseHeight, ( 0.5F - m_fUnfiredPotteryUrnBodyHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnBodyHalfWidth ), m_fUnfiredPotteryUrnBaseHeight + m_fUnfiredPotteryUrnBodyHeight, ( 0.5F + m_fUnfiredPotteryUrnBodyHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );    
        
    	// render neck 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnNeckHalfWidth ), m_fUnfiredPotteryUrnHeight - ( m_fUnfiredPotteryUrnTopHeight + m_fUnfiredPotteryUrnNeckHeight + m_fUnfiredPotteryUrnLidHeight), ( 0.5F - m_fUnfiredPotteryUrnNeckHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnNeckHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnTopHeight - m_fUnfiredPotteryUrnLidHeight, ( 0.5F + m_fUnfiredPotteryUrnNeckHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );    
        
    	// render top 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnTopHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnTopHeight - m_fUnfiredPotteryUrnLidHeight, ( 0.5F - m_fUnfiredPotteryUrnTopHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnTopHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnLidHeight, ( 0.5F + m_fUnfiredPotteryUrnTopHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );    
        
    	// render lid 
    	
        renderBlocks.setRenderBounds( ( 0.5F - m_fUnfiredPotteryUrnLidHalfWidth ), m_fUnfiredPotteryUrnHeight - m_fUnfiredPotteryUrnLidHeight, ( 0.5F - m_fUnfiredPotteryUrnLidHalfWidth ), 
        		( 0.5F + m_fUnfiredPotteryUrnLidHalfWidth ), m_fUnfiredPotteryUrnHeight, ( 0.5F + m_fUnfiredPotteryUrnLidHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, texture );
    }
    
    static public void RenderUnfiredCrucibleInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block, 
		int iItemDamage, 
		Icon texture
	)
    {
    	// render bin sides
    	
        renderBlocks.setRenderBounds( ( 0.5F - FCBlockVessel.m_fModelHalfWidth ), 0.0F, ( 0.5F - FCBlockVessel.m_fModelHalfWidth ), 
        		( 0.5F - FCBlockVessel.m_fModelHalfWidth ) + 0.125F, FCBlockVessel.m_fModelHeight, ( 0.5F + FCBlockVessel.m_fModelHalfWidth ) - 0.125F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        renderBlocks.setRenderBounds( ( 0.5F - FCBlockVessel.m_fModelHalfWidth ), 0.0F, ( 0.5F + FCBlockVessel.m_fModelHalfWidth ) - 0.125F, 
        		( 0.5F + FCBlockVessel.m_fModelHalfWidth ) - 0.125F, FCBlockVessel.m_fModelHeight, ( 0.5F + FCBlockVessel.m_fModelHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        renderBlocks.setRenderBounds( ( 0.5F + FCBlockVessel.m_fModelHalfWidth ) - 0.125F, 0.0F, ( 0.5F - FCBlockVessel.m_fModelHalfWidth ) + 0.125F, 
        		( 0.5F + FCBlockVessel.m_fModelHalfWidth ), FCBlockVessel.m_fModelHeight, ( 0.5F + FCBlockVessel.m_fModelHalfWidth ) );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        renderBlocks.setRenderBounds( ( 0.5F - FCBlockVessel.m_fModelHalfWidth ) + 0.125F, 0.0F, ( 0.5F - FCBlockVessel.m_fModelHalfWidth ), 
        		( 0.5F + FCBlockVessel.m_fModelHalfWidth ), FCBlockVessel.m_fModelHeight, ( 0.5F - FCBlockVessel.m_fModelHalfWidth ) + 0.125F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        // render bin bottom
        
        renderBlocks.setRenderBounds( ( 0.5F - FCBlockVessel.m_fModelHalfWidth ) + 0.125F, 0.0F, ( 0.5F - FCBlockVessel.m_fModelHalfWidth ) + 0.125F, 
        		( 0.5F + FCBlockVessel.m_fModelHalfWidth ) - 0.125F, 2.0F / 16F, ( 0.5F + FCBlockVessel.m_fModelHalfWidth ) - 0.125F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
    	// render band around centre
    	
        renderBlocks.setRenderBounds( 0.0F, 0.5F - FCBlockVessel.m_fModelBandHalfHeight, 0.0F, 
	    		0.125F, 0.5F + FCBlockVessel.m_fModelBandHalfHeight, 0.875F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        renderBlocks.setRenderBounds( 0.0F, 0.5F - FCBlockVessel.m_fModelBandHalfHeight, 0.875F, 
	    		0.875F, 0.5F + FCBlockVessel.m_fModelBandHalfHeight, 1.0F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        renderBlocks.setRenderBounds( 0.875F, 0.5F - FCBlockVessel.m_fModelBandHalfHeight, 0.125F, 
	    		1.0F, 0.5F + FCBlockVessel.m_fModelBandHalfHeight, 1.0F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );    
        
        renderBlocks.setRenderBounds( 0.125F, 0.5F - FCBlockVessel.m_fModelBandHalfHeight, 0.0F, 
	    		1.0F, 0.5F + FCBlockVessel.m_fModelBandHalfHeight, 0.125F );
        
        FCClientUtilsRender.RenderInvBlockWithMetadata( renderBlocks, block, -0.5F, -0.5F, -0.5F, 0 );
    }
}