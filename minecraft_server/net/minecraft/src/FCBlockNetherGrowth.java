// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockNetherGrowth extends Block
{
	private static final float m_fBlockHardness = 0.2F;
	
	protected FCBlockNetherGrowth( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialNetherGrowth );
        
        setHardness( m_fBlockHardness );
        SetAxesEffectiveOn( true );
        
        setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setUnlocalizedName( "fcBlockGroth" );
   
        setTickRandomly( true );
	}

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );

        int iBlockBelowID = world.getBlockId( i, j - 1, k );
        
        if ( iBlockBelowID == Block.netherrack.blockID )
        {
        	// no notify required as it's strictly an aesthetic change
        	world.setBlockAndMetadata( i, j - 1, k, FCBetterThanWolves.fcAestheticOpaque.blockID, FCBlockAestheticOpaque.m_iSubtypeNetherrackWithGrowth );        	
        }        
    }
	
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
		int iHeight = GetHeightLevel( world, i, j, k );
		
		if ( iHeight == 7 )
		{
			ReleaseSpores( world, i, j, k );
		}
		
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
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
		double dHeight = (double)( GetHeightLevel( blockAccess, i, j, k ) + 1 ) / 16D;
    	
    	return AxisAlignedBB.getAABBPool().getAABB( 0D, 0D, 0D, 1D, dHeight, 1D );
    }
    
	@Override
    public int getMobilityFlag()
    {
		// block breaks when pushed by piston
		
        return 1;
    }
	
	@Override
    public int quantityDropped( Random random )
    {
        return 0;
    }
    
	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return 0;
    }
	
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iChangedBlockID )
    {
		if ( !canPlaceBlockAt( world, i, j, k ) )
		{
			// play block destroy sound and particles
			
	        world.playAuxSFX( 2001, i, j, k, blockID + ( world.getBlockMetadata( i, j, k ) << 12 ) );

			// destroy the block if we no longer have a solid base
			
			world.setBlockWithNotify( i, j, k, 0 );
			
		}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		int iHeight = GetHeightLevel( world, i, j, k );
		int iBlockBelowID = world.getBlockId( i, j - 1, k );
		boolean bOnNetherrack = false;
				
        if ( iBlockBelowID == Block.netherrack.blockID )
        {
        	// convert regular netherrack to one with a growth texture
        	
        	// no notify required as it's strictly an aesthetic change
        	world.setBlockAndMetadata( i, j - 1, k, FCBetterThanWolves.fcAestheticOpaque.blockID, FCBlockAestheticOpaque.m_iSubtypeNetherrackWithGrowth );
        	
        	bOnNetherrack = true;
        }
        else if ( iBlockBelowID ==  FCBetterThanWolves.fcAestheticOpaque.blockID )
        {
        	int iSubtype = world.getBlockMetadata( i, j - 1, k );
        	
        	if ( iSubtype == FCBlockAestheticOpaque.m_iSubtypeNetherrackWithGrowth )
        	{
        		bOnNetherrack = true;
        	}
        }
        
		// attempt to grow
		
		if ( iHeight < 7 )		
		{
			boolean bGrow = false;
			
			if ( bOnNetherrack )
			{
				bGrow = true;
			}
			else
			{
				if ( GetMaxHeightOfNeighbors( world, i, j, k ) > iHeight + 1 )
				{
					bGrow = true;
				}
			}
			
			if ( bGrow )
			{
				iHeight++;
				
				SetHeightLevel( world, i, j, k, iHeight );
				
		        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
			}
		}
		
		// attempt to spread
		
		if ( iHeight >= 1 )
		{
			// select a random directional on horizontal plane to attempt to spread to
			
			int iFacing = random.nextInt( 4 ) + 2;
			
			FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
			targetPos.AddFacingAsOffset( iFacing );
			
			if ( IsBlockOpenToSpread( world, targetPos.i, targetPos.j, targetPos.k ) )
			{
				if ( world.doesBlockHaveSolidTopSurface( targetPos.i, targetPos.j - 1, targetPos.k ) )
				{
					SpreadToBlock( world, targetPos.i, targetPos.j, targetPos.k );
				}
				else
				{
					// the moss can only spread to downward neighbors if it is on netherrack
					
					if ( bOnNetherrack )
					{
						targetPos.j -= 1;
						
						if ( IsBlockOpenToSpread( world, targetPos.i, targetPos.j, targetPos.k ) )
						{
							if ( world.doesBlockHaveSolidTopSurface( targetPos.i, targetPos.j - 1, targetPos.k ) )
							{
								SpreadToBlock( world, targetPos.i, targetPos.j, targetPos.k );
							}
						}
					}
				}
			}
			else
			{
				// the moss can only spread upwards onto a netherrack block and if there is empty space above where it's currently at
				
				if ( world.isAirBlock( i, j + 1, k ) && world.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == Block.netherrack.blockID )
				{
					targetPos.j += 1;
					
					if ( IsBlockOpenToSpread( world, targetPos.i, targetPos.j, targetPos.k ) )
					{
						SpreadToBlock( world, targetPos.i, targetPos.j, targetPos.k );
					}
				}
			}
		}
    }
	
	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( !entity.isDead && !world.isRemote )
		{
			int iHeight = GetHeightLevel( world, i, j, k );
			
			// only mature blood moss does damage
			
			if ( iHeight >= 7 )
			{
				if ( entity instanceof EntityLiving )
				{					
					boolean bAttack = true;
					
					if ( entity instanceof EntityPlayer )
					{
						EntityPlayer player = (EntityPlayer)entity;
						
						if ( player.IsWearingSoulforgedBoots() )
						{
							bAttack = false;
						}
					}
					
					if ( bAttack )
					{
				        if ( entity.attackEntityFrom( FCDamageSourceCustom.m_DamageSourceGroth, 2 ) )
				        {
				        	entity.isAirBorne = true;
				            entity.motionY += 0.84D;
				            
			                world.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, i, j, k, 0 );            
				        }			        
					}
				}
				else if ( entity instanceof EntityItem )
				{
					// full grown growth eats food and mush
					
					EntityItem entityItem = (EntityItem)entity;
					
					if ( entityItem.delayBeforeCanPickup <= 0 )
					{					
						if ( entityItem.getEntityItem().getItem() instanceof ItemFood ||
							entityItem.getEntityItem().itemID == Block.mushroomRed.blockID || 
							entityItem.getEntityItem().itemID == Block.mushroomBrown.blockID )
						{
							entityItem.setDead();
							
			                world.playAuxSFX( FCBetterThanWolves.m_iBurpSoundAuxFXID, i, j, k, 0 );            
						}
					}
				}
			}
		}
    }
    
	@Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
		return 0.8F;        
    }
	
    //------------- Class Specific Methods ------------//
	
	public int GetHeightLevel( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetHeightLevelFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public int GetHeightLevelFromMetadata( int iMetdata )
	{
		return iMetdata & 7;
	}
	
	public void SetHeightLevel( World world, int i, int j, int k, int iHeight )
	{
		int iMetadata = world.getBlockMetadata( i, j, k ) & 8; // filter out old height
		
		iMetadata |= iHeight;
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	private int GetMaxHeightOfNeighbors( World world, int i, int j, int k )
	{
		int iMaxHeight = -1;
		
		for ( int iTempFacing = 2; iTempFacing <= 5; iTempFacing++ )
		{
			FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
			
			tempPos.AddFacingAsOffset( iTempFacing );
			
			if ( world.getBlockId( tempPos.i, tempPos.j, tempPos.k ) == blockID )
			{
				int iTempHeight = GetHeightLevel( world, tempPos.i, tempPos.j, tempPos.k );
				
				if ( iTempHeight > iMaxHeight )
				{
					iMaxHeight = iTempHeight;
				}
			}
		}
		
		return iMaxHeight;
	}
	
	private void SpreadToBlock( World world, int i, int j, int k )
	{
		if ( world.getBlockId( i, j, k ) == Block.fire.blockID )
		{
            world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );            
		}
		else if ( world.getBlockId( i, j, k ) == Block.mushroomBrown.blockID ||
			world.getBlockId( i, j, k ) == Block.mushroomRed.blockID )
		{				
            world.playAuxSFX( FCBetterThanWolves.m_iBurpSoundAuxFXID, i, j, k, 0 );            
		}
		
		if ( world.setBlockWithNotify( i, j, k, blockID ) )
		{
            world.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID, i, j, k, 0 );            
		}
	}
	
	private boolean IsBlockOpenToSpread( World world, int i, int j, int k )
	{
		if ( world.isAirBlock( i, j, k )  )
		{
			return true;
		}
		else
		{
			int iBlockID = world.getBlockId( i, j, k );
			
			if ( iBlockID == Block.fire.blockID || iBlockID == Block.mushroomRed.blockID ||
				iBlockID == Block.mushroomBrown.blockID )
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void ReleaseSpores( World world, int i, int j, int k )
	{
        world.playAuxSFX( FCBetterThanWolves.m_iNetherGrothSporesAuxFXID, i, j, k, 0 );            

        // spread growth to nearby blocks
        
        for ( int iTempI = i - 3; iTempI <= i + 3; iTempI++ )
        {
            for ( int iTempJ = j - 3; iTempJ <= j + 3; iTempJ++ )
            {
                for ( int iTempK = k - 3; iTempK <= k + 3; iTempK++ )
                {
                	if ( iTempI != i || iTempJ  != j || iTempK != k )
                	{
                		if ( IsBlockOpenToSpread( world, iTempI, iTempJ, iTempK ) )
                		{
                			if ( world.doesBlockHaveSolidTopSurface( iTempI, iTempJ - 1, iTempK ) )
                			{
                				if ( world.rand.nextInt( 2 ) == 0 )
                				{
                					world.setBlockWithNotify( iTempI, iTempJ, iTempK, blockID );
                				}
                			}
                		}
                	}
                }
            }
        }
        
        // damage living stuff in the vicinity
        
		double posX = (double)i + 0.5D;
		double posY = (double)j + 0.5D;
		double posZ = (double)k + 0.5D;
		
        List list = world.getEntitiesWithinAABB( EntityLiving.class, 
    		AxisAlignedBB.getAABBPool().getAABB( 
    			posX - 5.0D, posY - 5.0D, posZ - 5.0D,
    			posX + 5.0D, posY + 5.0D, posZ + 5.0D ) );
    			
        if ( list != null && list.size() > 0 )
        {
            for ( int listIndex = 0; listIndex < list.size(); listIndex++ )
            {
	    		EntityLiving targetEntity = (EntityLiving)list.get( listIndex );
	    		
	    		boolean bDamageEntity = true;
	    		
	    		if ( targetEntity instanceof EntityPlayer )
	    		{
	    			EntityPlayer player = (EntityPlayer)targetEntity;
	    			
	    			if ( player.IsWearingFullSuitSoulforgedArmor() )
	    			{
	    				bDamageEntity = false;
	    			}
	    		}
	    		
	    		if ( bDamageEntity )
	    		{
	    			targetEntity.attackEntityFrom( FCDamageSourceCustom.m_DamageSourceGrothSpores, 4 );
	    			
                    targetEntity.addPotionEffect(new PotionEffect(Potion.poison.id, 300, 0));
	    		}
            }
	    		
        }
	}
	
	//----------- Client Side Functionality -----------//
}