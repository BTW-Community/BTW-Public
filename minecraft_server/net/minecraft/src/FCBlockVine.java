// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockVine extends BlockVine
{
    public FCBlockVine( int iBlockID )
    {
    	super( iBlockID );
        
        setHardness( 0.2F );
		
        SetAxesEffectiveOn( true );
        
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.VINES );
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );
        
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "vine" );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
        if ( world.provider.dimensionId != 1 && world.rand.nextInt( 4 ) == 0 )
        {
            byte var6 = 4;
            int var7 = 5;
            boolean var8 = false;
            int var9;
            int var10;
            int var11;
            label138:

            for (var9 = i - var6; var9 <= i + var6; ++var9)
            {
                for (var10 = k - var6; var10 <= k + var6; ++var10)
                {
                    for (var11 = j - 1; var11 <= j + 1; ++var11)
                    {
                        if (world.getBlockId(var9, var11, var10) == this.blockID)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                var8 = true;
                                break label138;
                            }
                        }
                    }
                }
            }

            var9 = world.getBlockMetadata(i, j, k);
            var10 = world.rand.nextInt(6);
            var11 = Direction.facingToDirection[var10];
            int var12;
            int var13;

            if (var10 == 1 && j < 255 && world.isAirBlock(i, j + 1, k))
            {
                if (var8)
                {
                    return;
                }

                var12 = world.rand.nextInt(16) & var9;

                if (var12 > 0)
                {
                    for (var13 = 0; var13 <= 3; ++var13)
                    {
                        if (!this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var13], j + 1, k + Direction.offsetZ[var13])))
                        {
                            var12 &= ~(1 << var13);
                        }
                    }

                    if (var12 > 0)
                    {
                        world.setBlockAndMetadataWithNotify(i, j + 1, k, this.blockID, var12);
                    }
                }
            }
            else
            {
                int var14;

                if (var10 >= 2 && var10 <= 5 && (var9 & 1 << var11) == 0)
                {
                    if (var8)
                    {
                        return;
                    }

                    var12 = world.getBlockId(i + Direction.offsetX[var11], j, k + Direction.offsetZ[var11]);

                    if ( !world.isAirBlock( i + Direction.offsetX[var11], j, k + Direction.offsetZ[var11] ) && Block.blocksList[var12] != null )
                    {
                        if (Block.blocksList[var12].blockMaterial.isOpaque() && Block.blocksList[var12].renderAsNormalBlock())
                        {
                            world.setBlockMetadataWithNotify(i, j, k, var9 | 1 << var11);
                        }
                    }
                    else
                    {
                        var13 = var11 + 1 & 3;
                        var14 = var11 + 3 & 3;

                        if ((var9 & 1 << var13) != 0 && this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var11] + Direction.offsetX[var13], j, k + Direction.offsetZ[var11] + Direction.offsetZ[var13])))
                        {
                            world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11], j, k + Direction.offsetZ[var11], this.blockID, 1 << var13);
                        }
                        else if ((var9 & 1 << var14) != 0 && this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var11] + Direction.offsetX[var14], j, k + Direction.offsetZ[var11] + Direction.offsetZ[var14])))
                        {
                            world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11], j, k + Direction.offsetZ[var11], this.blockID, 1 << var14);
                        }
                        else if ((var9 & 1 << var13) != 0 && world.isAirBlock(i + Direction.offsetX[var11] + Direction.offsetX[var13], j, k + Direction.offsetZ[var11] + Direction.offsetZ[var13]) && this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var13], j, k + Direction.offsetZ[var13])))
                        {
                            world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11] + Direction.offsetX[var13], j, k + Direction.offsetZ[var11] + Direction.offsetZ[var13], this.blockID, 1 << (var11 + 2 & 3));
                        }
                        else if ((var9 & 1 << var14) != 0 && world.isAirBlock(i + Direction.offsetX[var11] + Direction.offsetX[var14], j, k + Direction.offsetZ[var11] + Direction.offsetZ[var14]) && this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var14], j, k + Direction.offsetZ[var14])))
                        {
                            world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11] + Direction.offsetX[var14], j, k + Direction.offsetZ[var11] + Direction.offsetZ[var14], this.blockID, 1 << (var11 + 2 & 3));
                        }
                        else if (this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var11], j + 1, k + Direction.offsetZ[var11])))
                        {
                            world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11], j, k + Direction.offsetZ[var11], this.blockID, 0);
                        }
                    }
                }
                else if (j > 1)
                {
                    var12 = world.getBlockId(i, j - 1, k);

                    if ( world.isAirBlock(i, j - 1, k))
                    {
                        var13 = world.rand.nextInt(16) & var9;

                        if (var13 > 0)
                        {
                            world.setBlockAndMetadataWithNotify(i, j - 1, k, this.blockID, var13);
                        }
                    }
                    else if (var12 == this.blockID)
                    {
                        var13 = world.rand.nextInt(16) & var9;
                        var14 = world.getBlockMetadata(i, j - 1, k);

                        if (var14 != (var14 | var13))
                        {
                            world.setBlockMetadataWithNotify(i, j - 1, k, var14 | var13);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
    	return Block.vine.blockID;
    }
    
    @Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
    	return 1;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	if ( par5Entity.IsAffectedByMovementModifiers() && par5Entity.onGround )
    	{
    		boolean bIsOnLadder = false;
    		
    		if ( par5Entity instanceof EntityLiving )
    		{
    			bIsOnLadder = ((EntityLiving)par5Entity).isOnLadder();
    		}
    		
    		if ( !bIsOnLadder )
    		{
		        par5Entity.motionX *= 0.8D;
		        par5Entity.motionZ *= 0.8D;
    		}
    	}
    }
    
    @Override
    public boolean CanSpitWebReplaceBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean IsReplaceableVegetation( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
	public boolean IsBlockClimbable( World world, int i, int j, int k )
	{
		return true;
	}
	
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	return createStackedBlock( iMetadata );
    }
    
    @Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent method
    }
    
    @Override
    public void setBlockBoundsForItemRender()
    {
    	// override to deprecate parent method
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iMetadata = blockAccess.getBlockMetadata(i, j, k);
        
        float fMinX = 1.0F;
        float fMinY = 1.0F;
        float fMinZ = 1.0F;
        float fMaxX = 0.0F;
        float fMaxY = 0.0F;
        float fMaxZ = 0.0F;
        
        boolean var13 = iMetadata > 0;

        if ((iMetadata & 2) != 0)
        {
            fMaxX = Math.max(fMaxX, 0.0625F);
            fMinX = 0.0F;
            fMinY = 0.0F;
            fMaxY = 1.0F;
            fMinZ = 0.0F;
            fMaxZ = 1.0F;
            var13 = true;
        }

        if ((iMetadata & 8) != 0)
        {
            fMinX = Math.min(fMinX, 0.9375F);
            fMaxX = 1.0F;
            fMinY = 0.0F;
            fMaxY = 1.0F;
            fMinZ = 0.0F;
            fMaxZ = 1.0F;
            var13 = true;
        }

        if ((iMetadata & 4) != 0)
        {
            fMaxZ = Math.max(fMaxZ, 0.0625F);
            fMinZ = 0.0F;
            fMinX = 0.0F;
            fMaxX = 1.0F;
            fMinY = 0.0F;
            fMaxY = 1.0F;
            var13 = true;
        }

        if ((iMetadata & 1) != 0)
        {
            fMinZ = Math.min(fMinZ, 0.9375F);
            fMaxZ = 1.0F;
            fMinX = 0.0F;
            fMaxX = 1.0F;
            fMinY = 0.0F;
            fMaxY = 1.0F;
            var13 = true;
        }

        if (!var13 && this.canBePlacedOn(blockAccess.getBlockId(i, j + 1, k)))
        {
            fMinY = Math.min(fMinY, 0.9375F);
            fMaxY = 1.0F;
            fMinX = 0.0F;
            fMaxX = 1.0F;
            fMinZ = 0.0F;
            fMaxZ = 1.0F;
        }

    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		fMinX, fMinY, fMinZ, fMaxX, fMaxY, fMaxZ );
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean canBePlacedOn(int par1)
    {
    	// copied from parent due to private
    	
        if (par1 == 0)
        {
            return false;
        }
        else
        {
            Block var2 = Block.blocksList[par1];
            return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
        }
    }
    
	//----------- Client Side Functionality -----------//
}
