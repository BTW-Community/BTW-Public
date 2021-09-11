// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCUtilsMisc
{
	public static final int m_iTicksPerSecond = 20;
	public static final int m_iTicksPerMinute = ( m_iTicksPerSecond * 60 );
	public static final int m_iTicksPerGameDay = ( m_iTicksPerMinute * 20 );
	
	static public int ConvertPlacingEntityOrientationToBlockFacingReversed( EntityLiving entityLiving )
    {
		float pitch = entityLiving.rotationPitch;
		
		if ( pitch > 60.0F )
		{
			// looking down
			
			return 1;			
		}
		else if ( pitch < -60.0F )
		{
			// looking up

			return 0;
		}
		else
		{
			return ConvertOrientationToFlatBlockFacingReversed( entityLiving );
		}		
    }
    
    static public int ConvertOrientationToFlatBlockFacingReversed( EntityLiving entityLiving )
    {
		int iFacing;
		
	    int l = MathHelper.floor_double( (double)( ( entityLiving.rotationYaw * 4F ) / 360F ) + 0.5D ) & 3;
	    
	    if(l == 0)
	    {
			iFacing = 2;			
	    }
	    else if(l == 1)
	    {
			iFacing = 5;			
	    }
	    else if(l == 2)
	    {
			iFacing = 3;			
	    }
	    else
	    {
			iFacing = 4;			
	    }
		
		return iFacing;
    }
    
    static public int ConvertOrientationToFlatBlockFacing( EntityLiving entityLiving )
    {
		int iFacing;
		
	    int l = MathHelper.floor_double( (double)( ( entityLiving.rotationYaw * 4F ) / 360F ) + 0.5D ) & 3;
	    
	    if(l == 0)
	    {
			iFacing = 3;			
	    }
	    else if(l == 1)
	    {
			iFacing = 4;			
	    }
	    else if(l == 2)
	    {
			iFacing = 2;			
	    }
	    else
	    {
			iFacing = 5;			
	    }
		
		return iFacing;
    }
    
    static boolean IsIKInColdBiome( World world, int i, int k )
    {
        BiomeGenBase biomegenbase = world.getBiomeGenForCoords( i, k);
        float f = biomegenbase.getFloatTemperature();

        if (f > 0.15F)
        {
            return false;
        }
        
        return true;
    }
    
    static public void PositionAllNonPlayerMoveableEntitiesOutsideOfLocation( World world, int i, int j, int k )
    {    	
        List list = world.getEntitiesWithinAABBExcludingEntity( null,
    		AxisAlignedBB.getAABBPool().getAABB( 
				(double)i, (double)j, (double)k, 
				(double)i + 1, (double)j + 1, (double)k + 1 ) );
        
        if ( list != null && list.size() > 0 )
        {
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
                Entity entity = (Entity)list.get( listIndex );
                
                if( ( entity.canBePushed() || ( entity instanceof EntityItem ) ) && !( entity instanceof EntityPlayer ) ) 
                {
                	PositionEntityOutsideOfLocation( world, entity, i, j, k );
                }
            }
        }                
    }
    
    static private void PositionEntityOutsideOfLocation( World world, Entity entity, int i, int j, int k )
    {
    	// this function moves the entity in question the minimum possible amount along one axis 
    	// to get it outside of the location specified.
    	// It assumes that the entity has already been successfully tested for actually being within
    	// said location.
    	// this function will also only move the entity if the required distance is a small amount
    	
    	double minPosX = (float)i; 
    	double minPosY = (float)j; 
    	double minPosZ = (float)k;
    	
    	double maxPosX = (float)( i + 1 ); 
    	double maxPosY = (float)( j + 1 ); 
    	double maxPosZ = (float)( k + 1 );
    	
    	boolean xOverlap = false;
    	boolean yOverlap = false;
    	boolean zOverlap = false;
    	
    	double xOffset = 0.0F;
    	double yOffset = 0.0F;
    	double zOffset = 0.0F;
    	
    	if ( entity.boundingBox.minX <= maxPosX && entity.boundingBox.maxX >= minPosX )
    	{
    		xOverlap = true;
    		
    		if ( Math.abs( maxPosX - entity.boundingBox.minX ) < 
				Math.abs( minPosX - entity.boundingBox.maxX ) )
			{
    			xOffset = maxPosX - entity.boundingBox.minX + 0.01D;
			}
    		else
    		{
    			xOffset = minPosX - entity.boundingBox.maxX - 0.01D;
    		}
    	}
    	
    	if ( entity.boundingBox.minY <= maxPosY && entity.boundingBox.maxY >= minPosY )
    	{
    		yOverlap = true;
    		
    		if ( Math.abs( maxPosY - entity.boundingBox.minY ) < 
    				Math.abs( minPosY - entity.boundingBox.maxY ) )
			{
    			yOffset = maxPosY - entity.boundingBox.minY + 0.01D;
			}
    		else
    		{
    			yOffset = minPosY - entity.boundingBox.maxY - 0.01D;
    		}
    	}
    	
    	if ( entity.boundingBox.minZ <= maxPosZ && entity.boundingBox.maxZ >= minPosZ )
    	{
    		zOverlap = true;
    		
    		if ( Math.abs( maxPosZ - entity.boundingBox.minZ ) < 
    				Math.abs( minPosZ - entity.boundingBox.maxZ ) )
			{
    			zOffset = maxPosZ - entity.boundingBox.minZ + 0.01D;
			}
    		else
    		{
    			zOffset = minPosZ - entity.boundingBox.maxZ - 0.01D;
    		}
    	}
    	
		double entityX = entity.posX;
		double entityY = entity.posY;
		double entityZ = entity.posZ;
		
		if ( xOverlap && Math.abs( xOffset ) < 0.20D && 
			( !yOverlap || Math.abs( xOffset ) < Math.abs( yOffset ) ) && 
			( !zOverlap || Math.abs( xOffset ) < Math.abs( zOffset ) ) ) 
		{
			// xOffset is smallest
			
			entityX += xOffset;
		}
		else if ( yOverlap && Math.abs( yOffset ) < 0.20D &&
			( !zOverlap || Math.abs( yOffset ) < Math.abs( zOffset ) ) )
		{
			// yOffset is smallest
			
			entityY += yOffset;
		}
		else if ( zOverlap && Math.abs( zOffset ) < 0.20D )
		{
			// zOffset is smallest
			
			entityZ += zOffset;
		}
		
		entity.setPosition( entityX, entityY, entityZ );
		
		if ( entity instanceof EntityPlayerMP )
		{
			// this function gets called server-side, so we must make sure the changes are relayed to the client
			
			EntityPlayerMP player = (EntityPlayerMP)entity;
			
			FCUtilsWorld.SendPacketToPlayer( player.playerNetServerHandler,
        		new Packet13PlayerLookMove( entityX, entityY + 1.6200000047683716D, entityY, entityZ, 
        			player.rotationYaw, player.rotationPitch, false ) );
		}
	
    }
    
    static public void ServerPositionAllPlayerEntitiesOutsideOfLocation( World world, int i, int j, int k )
    {    	
        List list = world.getEntitiesWithinAABB( EntityPlayerMP.class,
    		AxisAlignedBB.getAABBPool().getAABB( 
				(double)i, (double)j, (double)k, 
				(double)i + 1, (double)j + 1, (double)k + 1 ) );
        
        if ( list != null && list.size() > 0 )
        {
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
                EntityPlayerMP player = (EntityPlayerMP)list.get( listIndex );
                
            	ServerPositionPlayerEntityOutsideOfLocation( world, player, i, j, k );
            }
        }                
    }
    
    static private void ServerPositionPlayerEntityOutsideOfLocation( World world, EntityPlayerMP player, int i, int j, int k )
    {
    	// this function moves the entity in question the minimum possible amount along one axis 
    	// to get it outside of the location specified.
    	// It assumes that the entity has already been successfully tested for actually being within
    	// said location.
    	// this function will also only move the entity if the required distance is a small amount
    	
    	double minPosX = (float)i; 
    	double minPosY = (float)j; 
    	double minPosZ = (float)k;
    	
    	double maxPosX = (float)( i + 1 ); 
    	double maxPosY = (float)( j + 1 ); 
    	double maxPosZ = (float)( k + 1 );
    	
    	boolean xOverlap = false;
    	boolean yOverlap = false;
    	boolean zOverlap = false;
    	
    	double xOffset = 0.0F;
    	double yOffset = 0.0F;
    	double zOffset = 0.0F;
    	
    	if ( player.boundingBox.minX <= maxPosX && player.boundingBox.maxX >= minPosX )
    	{
    		xOverlap = true;
    		
    		if ( Math.abs( maxPosX - player.boundingBox.minX ) < 
				Math.abs( minPosX - player.boundingBox.maxX ) )
			{
    			xOffset = maxPosX - player.boundingBox.minX + 0.01D;
			}
    		else
    		{
    			xOffset = minPosX - player.boundingBox.maxX - 0.01D;
    		}
    	}
    	
    	if ( player.boundingBox.minY <= maxPosY && player.boundingBox.maxY >= minPosY )
    	{
    		yOverlap = true;
    		
    		if ( Math.abs( maxPosY - player.boundingBox.minY ) < 
    				Math.abs( minPosY - player.boundingBox.maxY ) )
			{
    			yOffset = maxPosY - player.boundingBox.minY + 0.01D;
			}
    		else
    		{
    			yOffset = minPosY - player.boundingBox.maxY - 0.01D;
    		}
    	}
    	
    	if ( player.boundingBox.minZ <= maxPosZ && player.boundingBox.maxZ >= minPosZ )
    	{
    		zOverlap = true;
    		
    		if ( Math.abs( maxPosZ - player.boundingBox.minZ ) < 
    				Math.abs( minPosZ - player.boundingBox.maxZ ) )
			{
    			zOffset = maxPosZ - player.boundingBox.minZ + 0.01D;
			}
    		else
    		{
    			zOffset = minPosZ - player.boundingBox.maxZ - 0.01D;
    		}
    	}
    	
		double entityX = player.posX;
		double entityY = player.posY;
		double entityZ = player.posZ;
		
		if ( xOverlap && Math.abs( xOffset ) < 0.20D && 
			( !yOverlap || Math.abs( xOffset ) < Math.abs( yOffset ) ) && 
			( !zOverlap || Math.abs( xOffset ) < Math.abs( zOffset ) ) ) 
		{
			// xOffset is smallest
			
			entityX += xOffset;
		}
		else if ( yOverlap && Math.abs( yOffset ) < 0.20D &&
			( !zOverlap || Math.abs( yOffset ) < Math.abs( zOffset ) ) )
		{
			// yOffset is smallest
			
			entityY += yOffset;
		}
		else if ( zOverlap && Math.abs( zOffset ) < 0.20D )
		{
			// zOffset is smallest
			
			entityZ += zOffset;
		}
		
		player.setPosition( entityX, entityY, entityZ );
		
		FCUtilsWorld.SendPacketToPlayer( player.playerNetServerHandler,
    		new Packet13PlayerLookMove( entityX, entityY + 1.6200000047683716D, entityY, entityZ, 
    			player.rotationYaw, player.rotationPitch, false ) );	
    }
    
    static public void PlayPlaceSoundForBlock( World world, int i, int j, int k )
    {
    	int iTargetBlockID = world.getBlockId( i, j, k );
    	Block targetBlock = Block.blocksList[iTargetBlockID];
    	
    	if ( targetBlock != null )
    	{
            world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 
        		targetBlock.stepSound.getStepSound(), 
        		(targetBlock.stepSound.getVolume() + 1.0F) / 2.0F, 
        		targetBlock.stepSound.getPitch() * 0.8F );                        
    	}
    }
    
    static public boolean IsCreatureWearingBreedingHarness( EntityCreature creature )
    {
    	if ( creature instanceof EntityAnimal )
    	{
    		EntityAnimal animal = (EntityAnimal)creature;
    		
    		return animal.getWearingBreedingHarness();
    	}
    	
    	return false;
    }
    
    // returns true if the orientation has changed
    static public boolean StandardRotateAroundJ( Block block, World world, int i, int j, int k, boolean bReverse )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		int iNewMetadata = StandardRotateMetadataAroundJ( block, iMetadata, bReverse );
		
		if ( iNewMetadata != iMetadata )
		{
	        world.setBlockMetadataWithNotify( i, j, k, iNewMetadata );
			
	        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
	        
	        return true;
    	}
		
		return false;
    }
    
	static public int StandardRotateMetadataAroundJ( Block block, int iMetadata, boolean bReverse )
	{
		int iFacing = block.GetFacing( iMetadata );
		
		int iNewFacing = Block.RotateFacingAroundJ( iFacing, bReverse );
		
		iMetadata = block.SetFacing( iMetadata, iNewFacing );
		
		return iMetadata;
	}

	/* 
	 * returns normalized vector in the direction of the block facing
	 */
	static public Vec3 ConvertBlockFacingToVector( int iFacing )
	{
		Vec3 vector = Vec3.createVectorHelper( 0.0D, 0.0D, 0.0D );
		
    	switch ( iFacing )
    	{
    		case 0:
    			
            	vector.yCoord += -1D;
            	
    			break;
    			
    		case 1:
    			
    			vector.yCoord += 1D;
            	
    			break;
    			
    		case 2:
    			
    			vector.zCoord -= 1D;
        		
    			break;
    			
    		case 3:
    			
    			vector.zCoord += 1D;
        		
    			break;
    			
    		case 4:
    			
    			vector.xCoord -= 1D;
        		
    			break;
    			
			default:
				
				vector.xCoord += 1D;
	    		
				break;    			
    	}
    	
    	return vector;
	}
	
	/* places a max-height non-source water block at the specified location, and then max-height - 1 blocks
	 * in the surrounding horizontal neghbors.  Note that the central block is not tested for validity of placement
	 * but that the surrounding ones will only appear if those blocks are open to water flow.
	 * WARNING: Be very careful calling this from within block code, as it can wind up flowing back into the block itself,
	 * destroying it during an update (for example).
	 */ 
	static public void PlaceNonPersistantWater( World world, int i, int j, int k )
	{
		world.setBlockAndMetadataWithNotify( i, j, k, Block.waterMoving.blockID, 1 );
		
		// spread the water around a bit so that it doesn't dissipate immediately
		
		FlowWaterIntoBlockIfPossible( world, i + 1, j, k, 2 );
		FlowWaterIntoBlockIfPossible( world, i - 1, j, k, 2 );
		FlowWaterIntoBlockIfPossible( world, i, j, k + 1, 2 );
		FlowWaterIntoBlockIfPossible( world, i, j, k - 1, 2 );
	}
	
	static public void PlaceNonPersistantWaterMinorSpread( World world, int i, int j, int k )
	{
		int iSpread = 5;
		
		world.setBlockAndMetadataWithNotify( i, j, k, Block.waterMoving.blockID, iSpread );
		
		// spread the water around a bit so that it doesn't dissipate immediately
		// FCTODO: This is a terrible way to do this.  Should place a single block that updates later
		// to avoid potentially flowing into (and destroying) the block which is causing the water to be placed (like melting snow)
		// during its own update, which lead to a crash with campfires.  Safe methods temporary bandaid.
		
		FlowWaterIntoBlockSafe( world, i + 1, j, k, iSpread + 1 );
		FlowWaterIntoBlockSafe( world, i - 1, j, k, iSpread + 1 );
		FlowWaterIntoBlockSafe( world, i, j, k + 1, iSpread + 1 );
		FlowWaterIntoBlockSafe( world, i, j, k - 1, iSpread + 1 );		
	}
	
	static public void FlowWaterIntoBlockSafe( World world, int i, int j, int k, int iDecayLevel )
	{
		if ( world.isAirBlock( i, j, k ) )
		{
			FlowWaterIntoBlockIfPossible( world, i, j, k, iDecayLevel );
		}
	}
	
	/* largely copied from BlockFlowing.flowIntoBlock() to bypass private access
	 */
	static public void FlowWaterIntoBlockIfPossible( World world, int i, int j, int k, int iDecayLevel )
	{
        if ( CanWaterDisplaceBlock( world, i, j, k ) )
        {
            int iTargetBlockID = world.getBlockId( i, j, k );

            if ( iTargetBlockID > 0 )
            {
            	Block.blocksList[iTargetBlockID].OnFluidFlowIntoBlock( world, i, j, k, Block.waterMoving );
            }

            world.setBlockAndMetadataWithNotify( i, j, k, Block.waterMoving.blockID, iDecayLevel );
        }
	}
        
	/* largely copied from BlockFlowing.liquidCanDisplaceBlock() to bypass private access
	 */
    static public boolean CanWaterDisplaceBlock( World world, int i, int j, int k )
    {
        Material material = world.getBlockMaterial( i, j, k );

        if ( material == Block.waterMoving.blockMaterial )
        {
            return false;
        }

        if ( material == Material.lava )
        {
            return false;
        }
        else
        {
            Block block = Block.blocksList[world.getBlockId(i, j, k)];
            
            return block == null || !block.GetPreventsFluidFlow( world, i, j, k, Block.waterMoving );
        }
    }
    
    /*
     * slightly modified version of getMovingObjectPositionFromPlayer() from Item.java so that non-source water and lava blocks are hit as well
     */
    static public MovingObjectPosition GetMovingObjectPositionFromPlayerHitWaterAndLava(World par1World, EntityPlayer par2EntityPlayer, boolean par3 )
    {
        float f = 1.0F;
        float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
        float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
        double d = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
        double d1 = (par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + 1.6200000000000001D) - (double)par2EntityPlayer.yOffset;
        double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
        Vec3 vec3 = par1World.getWorldVec3Pool().getVecFromPool(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5D;
        Vec3 vec3_1 = vec3.addVector((double)f7 * d3, (double)f8 * d3, (double)f9 * d3);
        return FCUtilsWorld.RayTraceBlocksAlwaysHitWaterAndLava(par1World, vec3, vec3_1, par3, !par3);
    }
    
    // FCTODO: Too much duplicate functionality with above
    static public MovingObjectPosition GetMovingObjectPositionFromPlayerHitWaterAndLavaAndFire(World par1World, EntityPlayer par2EntityPlayer, boolean par3 )
    {
        float f = 1.0F;
        float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
        float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
        double d = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
        double d1 = (par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + 1.6200000000000001D) - (double)par2EntityPlayer.yOffset;
        double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
        Vec3 vec3 = par1World.getWorldVec3Pool().getVecFromPool(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5D;
        Vec3 vec3_1 = vec3.addVector((double)f7 * d3, (double)f8 * d3, (double)f9 * d3);
        return FCUtilsWorld.RayTraceBlocksAlwaysHitWaterAndLavaAndFire(par1World, vec3, vec3_1, par3, !par3);
    }
    
    /*
     * Adapted from Block.collisionRayTrace() so as to not necessitate modifying the actual block bounds to perform the ray trace
     */
    static public MovingObjectPosition RayTraceWithBox( World world, int i, int j, int k, Vec3 boxMin, Vec3 boxMax, Vec3 startRay, Vec3 endRay )
    {
        startRay = startRay.addVector( -i, -j, -k );
        endRay = endRay.addVector( -i, -j, -k );
        
        Vec3 vec3 = startRay.getIntermediateWithXValue( endRay, boxMin.xCoord );
        Vec3 vec3_1 = startRay.getIntermediateWithXValue( endRay, boxMax.xCoord );
        Vec3 vec3_2 = startRay.getIntermediateWithYValue( endRay, boxMin.yCoord );
        Vec3 vec3_3 = startRay.getIntermediateWithYValue( endRay, boxMax.yCoord );
        Vec3 vec3_4 = startRay.getIntermediateWithZValue( endRay, boxMin.zCoord );
        Vec3 vec3_5 = startRay.getIntermediateWithZValue( endRay, boxMax.zCoord );

        if (!isVecInsideYZBounds(vec3, boxMin, boxMax))
        {
            vec3 = null;
        }

        if (!isVecInsideYZBounds(vec3_1, boxMin, boxMax))
        {
            vec3_1 = null;
        }

        if (!isVecInsideXZBounds(vec3_2, boxMin, boxMax))
        {
            vec3_2 = null;
        }

        if (!isVecInsideXZBounds(vec3_3, boxMin, boxMax))
        {
            vec3_3 = null;
        }

        if (!isVecInsideXYBounds(vec3_4, boxMin, boxMax))
        {
            vec3_4 = null;
        }

        if (!isVecInsideXYBounds(vec3_5, boxMin, boxMax))
        {
            vec3_5 = null;
        }

        Vec3 vec3_6 = null;

        if (vec3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3;
        }

        if (vec3_1 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_1) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_1;
        }

        if (vec3_2 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_2) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_2;
        }

        if (vec3_3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_3) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_3;
        }

        if (vec3_4 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_4) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_4;
        }

        if (vec3_5 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_5) < startRay.squareDistanceTo(vec3_6)))
        {
            vec3_6 = vec3_5;
        }

        if (vec3_6 == null)
        {
            return null;
        }

        byte byte0 = -1;

        if (vec3_6 == vec3)
        {
            byte0 = 4;
        }

        if (vec3_6 == vec3_1)
        {
            byte0 = 5;
        }

        if (vec3_6 == vec3_2)
        {
            byte0 = 0;
        }

        if (vec3_6 == vec3_3)
        {
            byte0 = 1;
        }

        if (vec3_6 == vec3_4)
        {
            byte0 = 2;
        }

        if (vec3_6 == vec3_5)
        {
            byte0 = 3;
        }

        return new MovingObjectPosition( i, j, k, byte0, vec3_6.addVector( i, j, k ) );
    }

    /*
     * Copied for Block.java to get around private declaration
     */
    static public boolean isVecInsideYZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max )
    {
        if (par1Vec3 == null)
        {
            return false;
        }
        else
        {
            return par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
        }
    }

    /*
     * Copied for Block.java to get around private declaration
     */
    static public boolean isVecInsideXZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max )
    {
        if (par1Vec3 == null)
        {
            return false;
        }
        else
        {
            return par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
        }
    }

    /*
     * Copied for Block.java to get around private declaration
     */
    static public boolean isVecInsideXYBounds(Vec3 par1Vec3, Vec3 min, Vec3 max )
    {
        if (par1Vec3 == null)
        {
            return false;
        }
        else
        {
            return par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord;
        }
    }
    
    static public boolean DoesWaterHaveValidSource( World world, int i, int j, int k, int iDistanceToCheck )
    {
    	return DoesWaterHaveValidSourceRecursive( world, i, j, k, i, j, k, iDistanceToCheck );
    }
    
    static private boolean DoesWaterHaveValidSourceRecursive( World world, int i, int j, int k, int startI, int startJ, int startK, int iDistanceToCheck )
    {
    	if ( iDistanceToCheck <= 0 )
    	{
    		// if we've run the full distance, assume that we have a valid source
    		
    		return true;
    	}
    	
    	if ( !world.checkChunksExist( i - 1, 64, k - 1, i + 1, 64, k + 1 ) )
    	{
    		// if we are up against the edge of the loaded world (ignoring y value), assume that the water has a valid source
    		
    		return true;
    	}
    	
		int iThisBlockHeight = world.getBlockMetadata( i, j, k );
		
		if ( iThisBlockHeight == 0 )
		{
			return true;
		}
		else if ( iThisBlockHeight >= 8 )
		{
			// only check upwards and for direct sources on blocks that have a source upwards to prevent recursion from getting stupid
			
			FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
			targetPos.AddFacingAsOffset( 1 );
			
			int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
			
			if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID ) 
			{
				if ( DoesWaterHaveValidSourceWithSourceCheck( world, targetPos.i, targetPos.j, targetPos.k, startI, startJ, startK, iDistanceToCheck - 1 ) )
				{
					return true;
				}
				
		    	for ( int iFacing = 2; iFacing < 6; iFacing++ )
		    	{
					targetPos = new FCUtilsBlockPos( i, j, k );
					
					targetPos.AddFacingAsOffset( iFacing );
					
					iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
					
					if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID )
					{
						int iTargetHeight = world.getBlockMetadata( targetPos.i, targetPos.j, targetPos.k );
						
						boolean bTargetIsHigher = false;
						
						if ( iTargetHeight == 0 )
						{
							// found a source block
							
							return true;
						}
					}
		    	}				
			}
			
			return false;
		}
		
		int iDownBlockID = world.getBlockId( i, j - 1, k );
		
		if ( FCBetterThanWolves.m_bBlocksPotentialFluidSources[iDownBlockID] )
		{
			FCIBlockFluidSource targetBlock = (FCIBlockFluidSource)(Block.blocksList[iDownBlockID]);
			
			if ( targetBlock.IsSourceToFluidBlockAtFacing( world, i, j - 1, k, 1 ) >= 0 )
			{			
				// FCOTOD: This really shouldn't be a hard-coded thing.  work it into the screw pump interface
				if ( iDownBlockID == FCBetterThanWolves.fcBlockScrewPump.blockID )
				{
					if ( j - 1 < startJ )
					{
						// if this pump is below the start point, assume it's a valid source.  If it isn't, it will be detected on this pump's own check later.
						
						return true;
					}
					
					// this is another pump.  Continue tracing at its source
					
					FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j - 1, k );
					
					int iTargetFacing = ( (FCBlockScrewPump)FCBetterThanWolves.fcBlockScrewPump ).GetFacing( world, targetPos.i, targetPos.j, targetPos.k );
					
					targetPos.AddFacingAsOffset( iTargetFacing );
					
					if ( DoesWaterHaveValidSourceWithSourceCheck( world, targetPos.i, targetPos.j, targetPos.k, startI, startJ, startK, iDistanceToCheck - 1 ) )
					{
						return true;
					}			
				}
				else
				{
					return true;
				}
			}
		}			

    	
    	for ( int iFacing = 2; iFacing < 6; iFacing++ )
    	{
			FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
			
			targetPos.AddFacingAsOffset( iFacing );
			
			int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
			
			if ( iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID )
			{
				int iTargetHeight = world.getBlockMetadata( targetPos.i, targetPos.j, targetPos.k );
				
				boolean bTargetIsHigher = false;
				
				if ( iTargetHeight >= 8 )
				{
					bTargetIsHigher = true;
				}
				else if ( iTargetHeight < iThisBlockHeight )
				{
					bTargetIsHigher = true;
				}				
				
				if ( bTargetIsHigher )
				{
					if ( DoesWaterHaveValidSourceWithSourceCheck( world, targetPos.i, targetPos.j, targetPos.k, startI, startJ, startK, iDistanceToCheck - 1 ) )
					{
						return true;
					}
				}
			}
			else if ( FCBetterThanWolves.m_bBlocksPotentialFluidSources[iTargetBlockID] )
			{
				FCIBlockFluidSource targetBlock = (FCIBlockFluidSource)(Block.blocksList[iTargetBlockID]);
				
				if ( targetBlock.IsSourceToFluidBlockAtFacing( world, targetPos.i, targetPos.j, targetPos.k, Block.GetOppositeFacing( iFacing ) ) >= 0 )
				
				// FCOTOD: This really shouldn't be a hard-coded thing.  work it into the interface, as these blocks may have their own source
				
				return true;
			}			
    	}
    	
    	return false;
    }
    
    static private boolean DoesWaterHaveValidSourceWithSourceCheck( World world, int i, int j, int k, int startI, int startJ, int startK, int iDistanceToCheck )
    {
    	if ( i == startI && j == startJ && k == startK )
    	{
    		return false;
    	}
    	else
    	{
    		return DoesWaterHaveValidSourceRecursive( world, i, j, k, startI, startJ, startK, iDistanceToCheck );
    	}
    }    
}