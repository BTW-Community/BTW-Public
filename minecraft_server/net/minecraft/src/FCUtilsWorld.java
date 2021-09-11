// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

/**
 * Helper functions related to the world
 */
public class FCUtilsWorld
{
    public static boolean IsReplaceableBlock( World world, int i, int j, int k )
    {
		int iBlockID = world.getBlockId( i, j, k );
		
		Block block = Block.blocksList[iBlockID];
		
		return block == null || block.blockMaterial.isReplaceable();
    }
    
    public static MovingObjectPosition RayTraceBlocksAlwaysHitWaterAndLava( World world, Vec3 vec3d, Vec3 vec3d1, boolean flag, boolean flag1 )
    {
        if(Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
        {
            return null;
        }
        if(Double.isNaN(vec3d1.xCoord) || Double.isNaN(vec3d1.yCoord) || Double.isNaN(vec3d1.zCoord))
        {
            return null;
        }
        int i = MathHelper.floor_double(vec3d1.xCoord);
        int j = MathHelper.floor_double(vec3d1.yCoord);
        int k = MathHelper.floor_double(vec3d1.zCoord);
        int l = MathHelper.floor_double(vec3d.xCoord);
        int i1 = MathHelper.floor_double(vec3d.yCoord);
        int j1 = MathHelper.floor_double(vec3d.zCoord);
        int k1 = world.getBlockId(l, i1, j1);
        int i2 = world.getBlockMetadata(l, i1, j1);
        Block block = Block.blocksList[k1];
        if((!flag1 || block == null || block.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) && k1 > 0 && 
    		(block.canCollideCheck(i2, flag) || 
			k1 == Block.waterMoving.blockID || k1 == Block.waterStill.blockID || 
			k1 == Block.lavaMoving.blockID || k1 == Block.lavaStill.blockID ) )
        {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
            if(movingobjectposition != null)
            {
                return movingobjectposition;
            }
        }
        for(int l1 = 200; l1-- >= 0;)
        {
            if(Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
            {
                return null;
            }
            if(l == i && i1 == j && j1 == k)
            {
                return null;
            }
            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d = 999D;
            double d1 = 999D;
            double d2 = 999D;
            if(i > l)
            {
                d = (double)l + 1.0D;
            } else
            if(i < l)
            {
                d = (double)l + 0.0D;
            } else
            {
                flag2 = false;
            }
            if(j > i1)
            {
                d1 = (double)i1 + 1.0D;
            } else
            if(j < i1)
            {
                d1 = (double)i1 + 0.0D;
            } else
            {
                flag3 = false;
            }
            if(k > j1)
            {
                d2 = (double)j1 + 1.0D;
            } else
            if(k < j1)
            {
                d2 = (double)j1 + 0.0D;
            } else
            {
                flag4 = false;
            }
            double d3 = 999D;
            double d4 = 999D;
            double d5 = 999D;
            double d6 = vec3d1.xCoord - vec3d.xCoord;
            double d7 = vec3d1.yCoord - vec3d.yCoord;
            double d8 = vec3d1.zCoord - vec3d.zCoord;
            if(flag2)
            {
                d3 = (d - vec3d.xCoord) / d6;
            }
            if(flag3)
            {
                d4 = (d1 - vec3d.yCoord) / d7;
            }
            if(flag4)
            {
                d5 = (d2 - vec3d.zCoord) / d8;
            }
            byte byte0 = 0;
            if(d3 < d4 && d3 < d5)
            {
                if(i > l)
                {
                    byte0 = 4;
                } else
                {
                    byte0 = 5;
                }
                vec3d.xCoord = d;
                vec3d.yCoord += d7 * d3;
                vec3d.zCoord += d8 * d3;
            } else
            if(d4 < d5)
            {
                if(j > i1)
                {
                    byte0 = 0;
                } else
                {
                    byte0 = 1;
                }
                vec3d.xCoord += d6 * d4;
                vec3d.yCoord = d1;
                vec3d.zCoord += d8 * d4;
            } else
            {
                if(k > j1)
                {
                    byte0 = 2;
                } else
                {
                    byte0 = 3;
                }
                vec3d.xCoord += d6 * d5;
                vec3d.yCoord += d7 * d5;
                vec3d.zCoord = d2;
            }
            Vec3 vec3d2 = Vec3.createVectorHelper(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            l = (int)(vec3d2.xCoord = MathHelper.floor_double(vec3d.xCoord));
            if(byte0 == 5)
            {
                l--;
                vec3d2.xCoord++;
            }
            i1 = (int)(vec3d2.yCoord = MathHelper.floor_double(vec3d.yCoord));
            if(byte0 == 1)
            {
                i1--;
                vec3d2.yCoord++;
            }
            j1 = (int)(vec3d2.zCoord = MathHelper.floor_double(vec3d.zCoord));
            if(byte0 == 3)
            {
                j1--;
                vec3d2.zCoord++;
            }
            int j2 = world.getBlockId(l, i1, j1);
            int k2 = world.getBlockMetadata(l, i1, j1);
            Block block1 = Block.blocksList[j2];
            if((!flag1 || block1 == null || block1.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) && j2 > 0 && 
        		( block1.canCollideCheck(k2, flag) || 
				j2 == Block.waterMoving.blockID || j2 == Block.waterStill.blockID || 
				j2 == Block.lavaMoving.blockID || j2 == Block.lavaStill.blockID ) )
            {
                MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
                if(movingobjectposition1 != null)
                {
                    return movingobjectposition1;
                }
            }
        }

        return null;
    }
    
    // FCTODO: Too much duplicate functionality with above
    public static MovingObjectPosition RayTraceBlocksAlwaysHitWaterAndLavaAndFire( World world, Vec3 vec3d, Vec3 vec3d1, boolean flag, boolean flag1 )
    {
        if(Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
        {
            return null;
        }
        if(Double.isNaN(vec3d1.xCoord) || Double.isNaN(vec3d1.yCoord) || Double.isNaN(vec3d1.zCoord))
        {
            return null;
        }
        int i = MathHelper.floor_double(vec3d1.xCoord);
        int j = MathHelper.floor_double(vec3d1.yCoord);
        int k = MathHelper.floor_double(vec3d1.zCoord);
        int l = MathHelper.floor_double(vec3d.xCoord);
        int i1 = MathHelper.floor_double(vec3d.yCoord);
        int j1 = MathHelper.floor_double(vec3d.zCoord);
        int k1 = world.getBlockId(l, i1, j1);
        int i2 = world.getBlockMetadata(l, i1, j1);
        Block block = Block.blocksList[k1];
        if((!flag1 || block == null || block.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) && k1 > 0 && 
    		(block.canCollideCheck(i2, flag) || 
			k1 == Block.waterMoving.blockID || k1 == Block.waterStill.blockID || 
			k1 == Block.lavaMoving.blockID || k1 == Block.lavaStill.blockID ||
			k1 == Block.fire.blockID || k1 == FCBetterThanWolves.fcBlockFireStoked.blockID ) )
        {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
            if(movingobjectposition != null)
            {
                return movingobjectposition;
            }
        }
        for(int l1 = 200; l1-- >= 0;)
        {
            if(Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
            {
                return null;
            }
            if(l == i && i1 == j && j1 == k)
            {
                return null;
            }
            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d = 999D;
            double d1 = 999D;
            double d2 = 999D;
            if(i > l)
            {
                d = (double)l + 1.0D;
            } else
            if(i < l)
            {
                d = (double)l + 0.0D;
            } else
            {
                flag2 = false;
            }
            if(j > i1)
            {
                d1 = (double)i1 + 1.0D;
            } else
            if(j < i1)
            {
                d1 = (double)i1 + 0.0D;
            } else
            {
                flag3 = false;
            }
            if(k > j1)
            {
                d2 = (double)j1 + 1.0D;
            } else
            if(k < j1)
            {
                d2 = (double)j1 + 0.0D;
            } else
            {
                flag4 = false;
            }
            double d3 = 999D;
            double d4 = 999D;
            double d5 = 999D;
            double d6 = vec3d1.xCoord - vec3d.xCoord;
            double d7 = vec3d1.yCoord - vec3d.yCoord;
            double d8 = vec3d1.zCoord - vec3d.zCoord;
            if(flag2)
            {
                d3 = (d - vec3d.xCoord) / d6;
            }
            if(flag3)
            {
                d4 = (d1 - vec3d.yCoord) / d7;
            }
            if(flag4)
            {
                d5 = (d2 - vec3d.zCoord) / d8;
            }
            byte byte0 = 0;
            if(d3 < d4 && d3 < d5)
            {
                if(i > l)
                {
                    byte0 = 4;
                } else
                {
                    byte0 = 5;
                }
                vec3d.xCoord = d;
                vec3d.yCoord += d7 * d3;
                vec3d.zCoord += d8 * d3;
            } else
            if(d4 < d5)
            {
                if(j > i1)
                {
                    byte0 = 0;
                } else
                {
                    byte0 = 1;
                }
                vec3d.xCoord += d6 * d4;
                vec3d.yCoord = d1;
                vec3d.zCoord += d8 * d4;
            } else
            {
                if(k > j1)
                {
                    byte0 = 2;
                } else
                {
                    byte0 = 3;
                }
                vec3d.xCoord += d6 * d5;
                vec3d.yCoord += d7 * d5;
                vec3d.zCoord = d2;
            }
            Vec3 vec3d2 = Vec3.createVectorHelper(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            l = (int)(vec3d2.xCoord = MathHelper.floor_double(vec3d.xCoord));
            if(byte0 == 5)
            {
                l--;
                vec3d2.xCoord++;
            }
            i1 = (int)(vec3d2.yCoord = MathHelper.floor_double(vec3d.yCoord));
            if(byte0 == 1)
            {
                i1--;
                vec3d2.yCoord++;
            }
            j1 = (int)(vec3d2.zCoord = MathHelper.floor_double(vec3d.zCoord));
            if(byte0 == 3)
            {
                j1--;
                vec3d2.zCoord++;
            }
            int j2 = world.getBlockId(l, i1, j1);
            int k2 = world.getBlockMetadata(l, i1, j1);
            Block block1 = Block.blocksList[j2];
            if((!flag1 || block1 == null || block1.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) && j2 > 0 && 
        		( block1.canCollideCheck(k2, flag) || 
				j2 == Block.waterMoving.blockID || j2 == Block.waterStill.blockID || 
				j2 == Block.lavaMoving.blockID || j2 == Block.lavaStill.blockID ||
				j2 == Block.fire.blockID || j2 == FCBetterThanWolves.fcBlockFireStoked.blockID ) )
            {
                MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
                if(movingobjectposition1 != null)
                {
                    return movingobjectposition1;
                }
            }
        }

        return null;
    }
    
	/**
	 * Returns the height level of the source (0 to 8) if a valid source for the fluid block, -1 otherwise
	 */
	static public int IsValidSourceForFluidBlockToFacing( World world, int i, int j, int k, int iFacing  )
	{
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		
		targetPos.AddFacingAsOffset( iFacing );
		
		int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
		
		if ( FCBetterThanWolves.m_bBlocksPotentialFluidSources[iTargetBlockID] )
		{
			FCIBlockFluidSource targetBlock = (FCIBlockFluidSource)(Block.blocksList[iTargetBlockID]);
			
			return targetBlock.IsSourceToFluidBlockAtFacing( world, 
				targetPos.i, targetPos.j, targetPos.k, Block.GetOppositeFacing( iFacing ) );				
		}
		
		return -1;
	}
	
	static public boolean IsValidLightLevelForMobSpawning( World world, int i, int j, int k )
	{
        return world.provider.dimensionId == -1 || ( world.getBlockLightValue( i, j, k ) <= 7 && 
			world.getLightBrightness( i, j, k ) <= 0.5F );
	}
	
	static public boolean CanMobsSpawnHere( World world, int i, int j, int k )
	{
		// this function reproduces all the tests (mostly found in SpawnerAnimals) that determine 
		// if a hostile mob can spawn in a particular location, short of the mob-specific 
		// bounding box tests.  It does test however if the block itself is free of collisions.
		
    	if ( !world.isBlockNormalCube( i, j, k ) && 
    		!world.getBlockMaterial( i, j, k ).isLiquid() )
    	{
	    	Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];
	    	
			if ( blockBelow != null && blockBelow.CanMobsSpawnOn( world, i, j - 1, k ) && 
				blockBelow != Block.leaves )
			{
				if ( IsValidLightLevelForMobSpawning( world, i, j, k ) )		            
				{
	            	Block blockIn = Block.blocksList[world.getBlockId( i, j, k )];
	            	
	            	return blockIn == null || 
	            		blockIn.getCollisionBoundingBoxFromPool( world, i, j, k ) == null;
	            }
			}
    	}
    	
    	return false;
	}	
	
	static public boolean DoesBlockHaveSmallCenterHardpointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		return block.HasSmallCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );
    	}
    	
    	return false;
	}
	
	static public boolean DoesBlockHaveSmallCenterHardpointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )	
	{
		return DoesBlockHaveSmallCenterHardpointToFacing( blockAccess, i, j, k, iFacing, false );		
	}
	
	static public boolean DoesBlockHaveCenterHardpointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		return block.HasCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );
    	}
    	
    	return false;
	}

	static public boolean DoesBlockHaveCenterHardpointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
		return DoesBlockHaveCenterHardpointToFacing( blockAccess, i, j, k, iFacing, false );
	}
	
	static public boolean DoesBlockHaveLargeCenterHardpointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		return block.HasLargeCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );
    	}
    	
    	return false;
	}
	
	static public boolean DoesBlockHaveLargeCenterHardpointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
		return DoesBlockHaveLargeCenterHardpointToFacing( blockAccess, i, j, k, iFacing, false );
	}
	
	static public boolean IsBlockRestingOnThatBelow( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		return block.IsBlockRestingOnThatBelow( blockAccess, i, j, k );
    	}
    	
    	return false;
	}
	
	/**
	 * Functionality duplicated from world.java to allow use of blockAccess on server
	 */
    static public boolean DoesBlockHaveSolidTopSurface( IBlockAccess blockAccess, int i, int j, int k )
    {
        Block block = Block.blocksList[blockAccess.getBlockId( i, j, k )];
        
        return block != null && block.HasLargeCenterHardPointToFacing( blockAccess, i, j, k, 1 );
    }
    
    /**
     * Used to determine if this is a stair block for purposes of connecting visually to others
     */
    static public boolean IsStairBlock( IBlockAccess blockAccess, int i, int j, int k )
	{
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block != null )
    	{
    		return block.IsStairBlock();
    	}
    	
    	return false;
	}
    
    static public long GetOverworldTimeServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		return MinecraftServer.getServer().worldServers[0].getWorldTime();
    	}
    	
		return 0;
    }
    
    static public boolean GameProgressHasNetherBeenAccessedServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		return MinecraftServer.getServer().worldServers[0].worldInfo.HasNetherBeenAccessed();
    	}
    	
		return false;
    }
    
    static public void GameProgressSetNetherBeenAccessedServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		MinecraftServer.getServer().worldServers[0].worldInfo.SetNetherBeenAccessed();
    	}
    }
    
    static public boolean GameProgressHasWitherBeenSummonedServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		return MinecraftServer.getServer().worldServers[0].worldInfo.HasWitherBeenSummoned();
    	}
    	
    	return false;    
	}
    
    static public void GameProgressSetWitherHasBeenSummonedServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		MinecraftServer.getServer().worldServers[0].worldInfo.SetWitherHasBeenSummoned();
    	}
    }
    
    static public boolean GameProgressHasEndDimensionBeenAccessedServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		return MinecraftServer.getServer().worldServers[0].worldInfo.HasEndDimensionBeenAccessed();
    	}
    	
    	return false;
    }
    
    static public void GameProgressSetEndDimensionHasBeenAccessedServerOnly()
    {
    	if ( MinecraftServer.getServer() != null )
    	{
    		MinecraftServer.getServer().worldServers[0].worldInfo.SetEndDimensionHasBeenAccessed();
    	}
    }
    
    /**
     * Used during structure (temples, etc.) generation to rotate block facings relative to the orientation of the structure.
     * Largely copied out of a portion of StructureComponent.getMetadataWithOffset() 
     */
    static public int RotateFacingForCoordBaseMode( int iFacing, int iCoordBaseMode )
    {
        if ( iCoordBaseMode == 0 )
        {
            if ( iFacing == 2 )
            {
            	return 3;
            }
            else if ( iFacing == 3 )
            {
                return 2;
            }
        }
        else if ( iCoordBaseMode == 1 )
        {
            if ( iFacing == 2 )
            {
                return 4;
            }

            if ( iFacing == 3 )
            {
                return 5;
            }

            if ( iFacing == 4 )
            {
                return 2;
            }

            if ( iFacing == 5 )
            {
                return 3;
            }
        }
        else if ( iCoordBaseMode == 3 )
        {
            if ( iFacing == 2 )
            {
                return 5;
            }

            if ( iFacing == 3 )
            {
                return 4;
            }

            if ( iFacing == 4 )
            {
                return 2;
            }

            if ( iFacing == 5 )
            {
                return 3;
            }
        }
        
        return iFacing;
    }
    
	/**
	 * Naming wiggle to compensate for different deobfuscated function name on client and server
	 */
    static public void SendPacketToAllPlayersTrackingEntity( WorldServer world, Entity entity, Packet packet )
    {
        // client
        //world.getEntityTracker().sendPacketToAllPlayersTrackingEntity( entity, packet );
        // server
        world.getEntityTracker().sendPacketToTrackedPlayers( entity, packet );        
    }
    
    /**
     * Naming wiggle to compensate for different deobfuscated function name on client and server
     */
    static public void SendPacketToPlayer( NetServerHandler handler, Packet packet )
    {
    	// client
        //handler.sendPacketToPlayer( packet );
    	// server
        handler.sendPacket( packet );
    }
    
    static public boolean HasNeighborWithMortarInFullFaceContactToFacing( World world, int i, int j, int k, int iFacing )
    {
		FCUtilsBlockPos tempBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		int iTempBlockID = world.getBlockId( tempBlockPos.i, tempBlockPos.j, tempBlockPos.k );
		
		Block tempBlock = Block.blocksList[iTempBlockID];
		
		if ( tempBlock != null && tempBlock.HasMortar( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k ) )
		{
			if ( tempBlock.HasContactPointToFullFace( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k, 
				Block.GetOppositeFacing( iFacing ) ) )
			{
				return true;
			}
		}
		
		return false;
    }
    
    static public boolean HasNeighborWithMortarInSlabSideContactToFacing( World world, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown )
    {
		FCUtilsBlockPos tempBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		int iTempBlockID = world.getBlockId( tempBlockPos.i, tempBlockPos.j, tempBlockPos.k );
		
		Block tempBlock = Block.blocksList[iTempBlockID];
		
		if ( tempBlock != null && tempBlock.HasMortar( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k ) )
		{
			if ( tempBlock.HasContactPointToSlabSideFace( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k, 
				Block.GetOppositeFacing( iFacing ), bIsSlabUpsideDown ) )
			{
				return true;
			}
		}
		
		return false;
    }
    
    static public boolean HasNeighborWithMortarInStairShapedContactToFacing( World world, int i, int j, int k, int iFacing )
    {
		FCUtilsBlockPos tempBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		int iTempBlockID = world.getBlockId( tempBlockPos.i, tempBlockPos.j, tempBlockPos.k );
		
		Block tempBlock = Block.blocksList[iTempBlockID];
		
		if ( tempBlock != null && tempBlock.HasMortar( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k ) )
		{
			if ( tempBlock.HasContactPointToStairShapedFace( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k, 
				Block.GetOppositeFacing( iFacing ) ) )
			{
				return true;
			}
		}
		
		return false;
    }    
    
    static public boolean HasNeighborWithMortarInStairNarrowVerticalContactToFacing( World world, int i, int j, int k, int iFacing, 
    	int iStairFacing )
    {
		FCUtilsBlockPos tempBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		int iTempBlockID = world.getBlockId( tempBlockPos.i, tempBlockPos.j, tempBlockPos.k );
		
		Block tempBlock = Block.blocksList[iTempBlockID];
		
		if ( tempBlock != null && tempBlock.HasMortar( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k ) )
		{
			if ( tempBlock.HasContactPointToStairNarrowVerticalFace( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k, 
				Block.GetOppositeFacing( iFacing ), iStairFacing ) )
			{
				return true;
			}
		}
		
		return false;
    }
    
    static public boolean HasStickySnowNeighborInFullFaceContactToFacing( World world, int i, int j, int k, int iFacing )
    {
		FCUtilsBlockPos tempBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		int iTempBlockID = world.getBlockId( tempBlockPos.i, tempBlockPos.j, tempBlockPos.k );
		
		Block tempBlock = Block.blocksList[iTempBlockID];
		
		if ( tempBlock != null && tempBlock.IsStickyToSnow( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k ) )
		{
			if ( tempBlock.HasContactPointToFullFace( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k, 
				Block.GetOppositeFacing( iFacing ) ) )
			{
				return true;
			}
		}
		
		return false;
    }
    
    static public boolean HasStickySnowNeighborInSlabSideContactToFacing( World world, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown )
    {
		FCUtilsBlockPos tempBlockPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		int iTempBlockID = world.getBlockId( tempBlockPos.i, tempBlockPos.j, tempBlockPos.k );
		
		Block tempBlock = Block.blocksList[iTempBlockID];
		
		if ( tempBlock != null && tempBlock.IsStickyToSnow( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k ) )
		{
			if ( tempBlock.HasContactPointToSlabSideFace( world, tempBlockPos.i, tempBlockPos.j, tempBlockPos.k, 
				Block.GetOppositeFacing( iFacing ), bIsSlabUpsideDown ) )
			{
				return true;
			}
		}
		
		return false;
    }
    
    static public void ClearAnyGroundCoverOnBlock( World world, int i, int j, int k )
    {
		if ( IsGroundCoverOnBlock( world, i, j, k ) )
		{
			world.setBlockToAir( i, j + 1, k );
		}
    }
    
    static public boolean IsGroundCoverOnBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockAboveID = blockAccess.getBlockId( i, j + 1, k );
    	
    	if ( iBlockAboveID != 0 )
    	{
    		Block blockAbove = Block.blocksList[iBlockAboveID];
    		
    		if ( blockAbove.IsGroundCover( ) )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    static public boolean IsWaterSourceBlock( World world, int i, int j, int k )
    {
		int iBlockID = world.getBlockId( i, j, k );
		
		return ( iBlockID == Block.waterMoving.blockID || 
			iBlockID == Block.waterStill.blockID ) && world.getBlockMetadata( i, j, k ) == 0; 
    }
}