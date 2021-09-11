// FCMOD

package net.minecraft.src;

public class FCBlockDoor extends BlockDoor
{
	private boolean m_bSuppressBottomDrop = false;
	
    protected FCBlockDoor( int iBlockID, Material material )
    {
        super( iBlockID, material );
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );        
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		// override to provide change notifications on open and close
		
        if (this.blockMaterial == Material.iron)
        {
            return true;
        }
        else
        {
            int var10 = this.getFullMetadata(par1World, par2, par3, par4);
            int var11 = var10 & 7;
            var11 ^= 4;

            if ((var10 & 8) == 0)
            {
                par1World.SetBlockMetadataWithNotify(par2, par3, par4, var11, 3);
                par1World.notifyBlockChange(par2, par3 + 1, par4, blockID ); // notify around other block that comprises the door.

                par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
            }
            else
            {
                par1World.SetBlockMetadataWithNotify(par2, par3 - 1, par4, var11, 3);
                par1World.notifyBlockChange(par2, par3, par4, blockID ); // notify around other block that comprises the door.
                
                par1World.markBlockRangeForRenderUpdate(par2, par3 - 1, par4, par2, par3, par4);
            }

            par1World.playAuxSFXAtEntity(par5EntityPlayer, 1003, par2, par3, par4, 0);
            return true;
        }
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborID )
    {
        int iMetadata = world.getBlockMetadata(i, j, k);

        if ( ( iMetadata & 8 ) == 0 ) // bottom block
        {
            boolean bIsDestroyed = false;

            int iBlockAboveID = world.getBlockId(i, j + 1, k);
            
            if ( iBlockAboveID != this.blockID )
            {
                world.setBlockToAir(i, j, k);
                bIsDestroyed = true;
            }
            else if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k))
            {
                world.setBlockToAir(i, j, k);
                bIsDestroyed = true;

                if (world.getBlockId(i, j + 1, k) == this.blockID)
                {
                    world.setBlockToAir(i, j + 1, k);
                }
            }

            if ( bIsDestroyed )
            {
            	if ( !m_bSuppressBottomDrop && !world.isRemote )
            	{
                    dropBlockAsItem(world, i, j, k, iMetadata, 0);
            	}
            	
        		m_bSuppressBottomDrop = false;
            }
            else
            {
                boolean var8 = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);

                if ((var8 || iNeighborID > 0 && Block.blocksList[iNeighborID].canProvidePower()) && iNeighborID != this.blockID)
                {
                    this.onPoweredBlockChange(world, i, j, k, var8);
                }
            }
        }
        else
        {
            if (world.getBlockId(i, j - 1, k) != this.blockID)
            {
                world.setBlockToAir(i, j, k);
            }

            if (iNeighborID > 0 && iNeighborID != this.blockID)
            {
                this.onNeighborBlockChange(world, i, j - 1, k, iNeighborID);
            }
        }
    }
    
    @Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
        if ( ( world.getBlockMetadata(i, j, k) & 8 ) != 0 ) // top block
        {
    		m_bSuppressBottomDrop = true;
        }
		
		super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
    }    
    
    @Override
    public void onPoweredBlockChange( World world, int i, int j, int k, boolean bOn )
    {
		// override to provide change notifications on open and close
		
        int var6 = this.getFullMetadata(world, i, j, k);
        boolean var7 = (var6 & 4) != 0;

        if (var7 != bOn)
        {
            int var8 = var6 & 7;
            var8 ^= 4;

            if ((var6 & 8) == 0)
            {
                world.SetBlockMetadataWithNotify(i, j, k, var8, 3);
                world.notifyBlockChange(i, j + 1, k, blockID ); // notify around other block that comprises the door.
                
                world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
            }
            else
            {
                world.SetBlockMetadataWithNotify(i, j - 1, k, var8, 3);
                world.notifyBlockChange(i, j, k, blockID ); // notify around other block that comprises the door.
                
                world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j, k);
            }

            world.playAuxSFXAtEntity((EntityPlayer)null, 1003, i, j, k, 0);
        }
    }
    
    @Override
    public boolean GetCanBlockBeIncinerated( World world, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
    	return true;
	}
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	// copied and cleaned up from super.setDoorRotation()
    	
    	int iMetadata = getFullMetadata( blockAccess, i, j, k );
    	
        float var2 = 0.1875F;
        
        int var3 = iMetadata & 3;
        boolean var4 = (iMetadata & 4) != 0;
        boolean var5 = (iMetadata & 16) != 0;

        if (var3 == 0)
        {
            if (var4)
            {
                if (!var5)
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
                else
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
            }
            else
            {
                return AxisAlignedBB.getAABBPool().getAABB( 
                	0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
        }
        else if (var3 == 1)
        {
            if (var4)
            {
                if (!var5)
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
                else
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
            }
            else
            {
                return AxisAlignedBB.getAABBPool().getAABB( 
                	0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }
        }
        else if (var3 == 2)
        {
            if (var4)
            {
                if (!var5)
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
                else
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
            }
            else
            {
                return AxisAlignedBB.getAABBPool().getAABB( 
                	1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        else if (var3 == 3)
        {
            if (var4)
            {
                if (!var5)
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
                else
                {
                    return AxisAlignedBB.getAABBPool().getAABB( 
                    	1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            }
            else
            {
                return AxisAlignedBB.getAABBPool().getAABB( 
                	0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }
        }

        return AxisAlignedBB.getAABBPool().getAABB( 0F, 0F, 0F, 1F, 2F, 1F );
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startRay, Vec3 endRay )
    {
    	// copy of function in Block parent class, to override super method
    	
    	AxisAlignedBB collisionBox = GetBlockBoundsFromPoolBasedOnState( 
    		world, i, j, k ).offset( i, j, k );
    	
    	MovingObjectPosition collisionPoint = collisionBox.calculateIntercept( startRay, endRay );
    	
    	if ( collisionPoint != null )
    	{
    		collisionPoint.blockX = i;
    		collisionPoint.blockY = j;
    		collisionPoint.blockZ = k;
    	}
    	
    	return collisionPoint;
    }
    
    @Override
    public boolean ShouldOffsetPositionIfPathingOutOf( IBlockAccess blockAccess, 
    	int i, int j, int k, Entity entity, PathFinder pathFinder )
    {
    	// given doors have their collision volume offset towards a block edge, regular
    	// methods of determing an offset won't work, so just rely on the vanilla pathing
    	// to try and get out of it.
   
    	return false;
    }
    
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
}
