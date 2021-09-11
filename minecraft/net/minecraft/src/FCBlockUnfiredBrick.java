// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockUnfiredBrick extends BlockContainer
{
	public static final float m_fBrickHeight = ( 4F / 16F );
	public static final float m_fBrickWidth = ( 6F / 16F );
	public static final float m_fBrickHalfWidth = ( m_fBrickWidth / 2F );
	public static final float m_fBrickLength = ( 12F / 16F );
	public static final float m_fBrickHalfLength = ( m_fBrickLength / 2F );
	
    public FCBlockUnfiredBrick( int iBlockID )
    {
        super( iBlockID, Material.circuits );  
        
        setHardness( 0F );
        SetShovelsEffectiveOn( true );
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );        
        
		SetCanBeCookedByKiln( true );
        
        setUnlocalizedName( "fcBlockUnfiredBrick" );
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityUnfiredBrick();
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
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		return Item.clay.itemID;
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
		if ( GetIsIAligned( blockAccess, i, j, k ) )
		{
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		( 0.5F - m_fBrickHalfLength ), 0D, ( 0.5F - m_fBrickHalfWidth ), 
        		( 0.5F + m_fBrickHalfLength ), m_fBrickHeight, ( 0.5F + m_fBrickHalfWidth ) );
		}
		
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		( 0.5F - m_fBrickHalfWidth ), 0D, ( 0.5F - m_fBrickHalfLength ), 
    		( 0.5F + m_fBrickHalfWidth ), m_fBrickHeight, ( 0.5F + m_fBrickHalfLength ) );
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
		// don't allow drying bricks on leaves as it makes for really lame drying racks in trees 
		
		return FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) && 
			world.getBlockId( i, j - 1, k ) != Block.leaves.blockID;
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
    	if ( !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j - 1, k, 1, true ) )
    	{
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify(i, j, k, 0);
    	}
    }
    
	@Override
    public int GetItemIndexDroppedWhenCookedByKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
		return Item.brick.itemID;
    }

    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( !world.isRemote && !entity.isDead && entity instanceof EntityLiving && !( entity instanceof EntityAmbientCreature ) )
		{
			// note that this part can occasionally get slightly fooled by having both a bat and other living entity colliding with the larger block,
			// but only the bat (EntityAmbientCreature above) within the brick itself.  That's fine.
			
			List collisionList = world.getEntitiesWithinAABB( EntityLiving.class, GetVisualBB( world, i, j, k ) );
			
	    	if ( collisionList != null && collisionList.size() > 0 )
	    	{			
		        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		        	stepSound.getStepSound(), ( stepSound.getVolume() + 1.0F ) / 2.0F, stepSound.getPitch() * 0.8F );
		        
	            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
	            
	            world.setBlockWithNotify( i, j, k, 0 );
	    	}
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
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		return SetIAligned( iMetadata, !GetIsIAligned( iMetadata ) );
	}

    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 4;
    }
    
	//------------- Class Specific Methods ------------//
	
	public void OnFinishedCooking( World world, int i, int j, int k )
	{
		int iMetadata = world.getBlockMetadata( i, j, k ) & 1; // preserve orientation
		
		world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockCookedBrick.blockID, iMetadata ); 
	}
	
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
	
	public void SetCookLevel( World world, int i, int j, int k, int iCookLevel )
	{
		int iMetadata = SetCookLevel( world.getBlockMetadata( i, j, k ), iCookLevel );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetCookLevel( int iMetadata, int iCookLevel )
	{
		iMetadata &= 1; // filter out old state
		
		iMetadata |= iCookLevel << 1;
	
		return iMetadata;
	}
    
	public int GetCookLevel( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetCookLevel( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public int GetCookLevel( int iMetadata )
	{
		return iMetadata >> 1;
	}
	
    public AxisAlignedBB GetVisualBB( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( GetIsIAligned( blockAccess, i, j, k ) )
        {
	    	return AxisAlignedBB.getAABBPool().getAABB(
	    		i + ( 0.5F - m_fBrickHalfLength ), j, k + ( 0.5F - m_fBrickHalfWidth ), 
        		i + ( 0.5F + m_fBrickHalfLength ), j + m_fBrickHeight, k + ( 0.5F + m_fBrickHalfWidth ) );	    	
        }
        else
        {
	    	return AxisAlignedBB.getAABBPool().getAABB(
	    		i + ( 0.5F - m_fBrickHalfWidth ), j, k + ( 0.5F - m_fBrickHalfLength ), 
        		i + ( 0.5F + m_fBrickHalfWidth ), j + m_fBrickHeight, k + ( 0.5F + m_fBrickHalfLength ) );	    	
        }    	
    }
    
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_cookIcons;

	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_cookIcons = new Icon[7];

        for ( int iTempIndex = 0; iTempIndex < 7; iTempIndex++ )
        {
        	m_cookIcons[iTempIndex] = register.registerIcon( "fcOverlayUnfiredBrick_" + ( iTempIndex + 1 ) );
        }
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		if ( iSide == 0 )
		{
			return FCClientUtilsRender.ShouldRenderNeighborFullFaceSide( blockAccess,
				iNeighborI, iNeighborJ, iNeighborK, iSide );
		}
		
		return true;
    }
	
    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
    {
    	if ( bFirstPassResult )
    	{
        	IBlockAccess blockAccess = renderBlocks.blockAccess;
        	
	    	int iCookLevel = GetCookLevel( blockAccess, i, j, k );
	    	
    		int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
    		
    		if ( iBlockBelowID == FCBetterThanWolves.fcKiln.blockID )
    		{
    			int iKilnCookLevel = 
    				FCBetterThanWolves.fcKiln.GetCookCounter( blockAccess, i, j - 1, k ) / 2;
    			
    			if ( iKilnCookLevel > iCookLevel )
    			{
    				iCookLevel = iKilnCookLevel;
    			}
    		}
    		
    		if ( iCookLevel > 0 && iCookLevel <= 7 )
    		{
        		RenderBlockWithTexture( renderBlocks, i, j, k, m_cookIcons[iCookLevel - 1] );
    		}
    	}
    }
}