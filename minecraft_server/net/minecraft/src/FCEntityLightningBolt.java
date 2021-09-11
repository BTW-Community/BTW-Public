// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Iterator;

public class FCEntityLightningBolt extends EntityWeatherEffect
{
	private final int m_iMaxTrunkDetectionPenetration = 6;
	private final int m_iMaxTrunkPenetration = 6;

	private int m_iLightningState;
	private int m_iDurationCountdown;

	public long m_lRenderSeed = 0L;

	public FCEntityLightningBolt( World world, double dPosX, double dPosY, double dPosZ )
	{
		super( world );

		setLocationAndAngles( dPosX, dPosY, dPosZ, 0F, 0F );

		m_iLightningState = 2;
		m_iDurationCountdown = rand.nextInt(3) + 1;

		m_lRenderSeed = rand.nextLong();

		if ( !world.isRemote && !IsStrikingLightningRod() && world.doChunksNearChunkExist(
				MathHelper.floor_double( dPosX ), MathHelper.floor_double( dPosY ), 
				MathHelper.floor_double( dPosZ ), 10 ) )
		{
			int iStrikeI = MathHelper.floor_double(dPosX);
			int iStrikeJ = (int)dPosY;
			int iStrikeK = MathHelper.floor_double(dPosZ);

			OnStrikeBlock( world, iStrikeI, iStrikeJ, iStrikeK ); 

			if ( !IsStrikingLightningRod() )
			{
				for ( int iTempCount = 0; iTempCount < 4; ++iTempCount )
				{
					int iTempI = iStrikeI + rand.nextInt( 3 ) - 1;
					int iTempJ = iStrikeJ + rand.nextInt( 3 ) - 1;
					int iTempK = iStrikeK + rand.nextInt( 3 ) - 1;

					if ( FCBlockFire.CanFireReplaceBlock( world, iTempI, iTempJ, iTempK ) && 
							Block.fire.canPlaceBlockAt( world, iTempI, iTempJ, iTempK ) )
					{
						if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
							world.setBlock( iTempI, iTempJ, iTempK, Block.fire.blockID );
						}
					}
				}
			}
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		m_iLightningState--;

		if ( m_iLightningState == 1 )
		{
			worldObj.func_82739_e( FCBetterThanWolves.m_iLightningStrikeAuxFXID, 
					MathHelper.floor_double( posX ), (int)posY - 1, MathHelper.floor_double( posZ ), 0 );

			if ( !IsStrikingLightningRod() )
			{
				double dRange = 3D;

				List<Entity> entityList = worldObj.getEntitiesWithinAABBExcludingEntity( this, 
						AxisAlignedBB.getAABBPool().getAABB( 
								posX - dRange, posY, posZ - dRange, 
								posX + dRange, posY + 6.0D + dRange, posZ + dRange ) );

				Iterator<Entity> entityIterator = entityList.iterator();

				while ( entityIterator.hasNext() )
				{
					Entity tempEntity = entityIterator.next();

					tempEntity.OnStruckByLightning( this );
				}	            
			}
		}
		else if ( m_iLightningState < 0 )
		{
			if ( m_iDurationCountdown == 0 )
			{
				setDead();
			}
			else if ( m_iLightningState < -rand.nextInt( 10 ) )
			{
				m_iDurationCountdown--;

				m_iLightningState = 1;
				m_lRenderSeed = rand.nextLong();

				if ( !worldObj.isRemote && !IsStrikingLightningRod() && 
						worldObj.doChunksNearChunkExist( MathHelper.floor_double( posX ), 
								MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ), 10 ) )
				{
					int iTempI = MathHelper.floor_double( posX );
					int iTempJ = (int)posY;
					int iTempK = MathHelper.floor_double( posZ );

					if ( FCBlockFire.CanFireReplaceBlock( worldObj, iTempI, iTempJ, iTempK ) && 
							Block.fire.canPlaceBlockAt( worldObj, iTempI, iTempJ, iTempK ) )
					{
						if (worldObj.getGameRules().getGameRuleBooleanValue("doFireTick")) {
							worldObj.setBlock( iTempI, iTempJ, iTempK, Block.fire.blockID );
						}
					}
				}
			}
		}

		if ( m_iLightningState >= 0 && worldObj.isRemote )
		{
			// handles flashes lighting up world during lightning strike

			worldObj.lastLightningBolt = 2;
		}
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

	//------------- Class Specific Methods ------------//

	private boolean IsStrikingLightningRod() 
	{
		int i = MathHelper.floor_double( posX );
		int j = MathHelper.floor_double( posY );
		int k = MathHelper.floor_double( posZ );

		return worldObj.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcBlockLightningRod.blockID;
	}

	private void OnStrikeBlock( World world, int i, int j, int k )
	{
		if ( HasHitTreeTrunk( world, i, j, k ) )
		{
			BurnTreeTrunk( world, i, j, k );
		}
		else if (  world.getBlockId( i, j - 1, k ) == Block.leaves.blockID )
		{
			// we've likely hit the top of a tree, but missed the trunk.  Take a few more tries at it.

			for ( int iTempCount = 0; iTempCount < 5; iTempCount++ )
			{
				int iTempI = i + world.rand.nextInt( 5 ) - 2;
				int iTempK = k + world.rand.nextInt( 5 ) - 2;

				if ( FCBlockFire.CanFireReplaceBlock( world, iTempI, j, iTempK ) && HasHitTreeTrunk( world, iTempI, j, iTempK ) )
				{
					BurnTreeTrunk( world, iTempI, j, iTempK );

					break;
				}
			}
		}

		Block blockBelow = Block.blocksList[world.getBlockId( i, j - 1, k )];

		if ( blockBelow != null )
		{
			blockBelow.OnStruckByLightning( world, i, j - 1, k );
		}

		if ( FCBlockFire.CanFireReplaceBlock( world, i, j, k ) && 
				Block.fire.canPlaceBlockAt( world, i, j, k ) )
		{
			if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
				world.setBlock(i, j, k, Block.fire.blockID);
			}
		}
	}

	private boolean HasHitTreeTrunk( World world, int i, int j, int k )
	{
		int m_iMinJ = j - m_iMaxTrunkDetectionPenetration;

		for ( j--; j >= m_iMinJ; j-- )
		{
			int iTempBlockID = world.getBlockId( i, j, k );

			if ( iTempBlockID == Block.wood.blockID )
			{
				// test for jungle wood, which doesn't convert due to massive destruction it would cause

				return ( world.getBlockMetadata( i, j, k ) & 3 ) != 3;  
			}
			else if ( iTempBlockID != Block.leaves.blockID )
			{
				return false;
			}
		}

		return false;
	}

	private boolean BurnTreeTrunk( World world, int i, int j, int k )
	{
		int m_iMinJ = j - m_iMaxTrunkPenetration;

		FCBlockLog logBlock = (FCBlockLog)Block.wood;

		for ( j--; j >= m_iMinJ; j-- )
		{
			int iTempBlockID = world.getBlockId( i, j, k );

			if ( iTempBlockID == Block.wood.blockID )
			{
				// test for jungle wood, which doesn't convert due to massive destruction it would cause

				if ( ( world.getBlockMetadata( i, j, k ) & 3 ) != 3 )
				{
					if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
						logBlock.ConvertToSmouldering( world, i, j, k );
					}
				}
				else
				{
					break;
				}
			}
			else if ( iTempBlockID == Block.leaves.blockID )
			{
				if ( Block.fire.canPlaceBlockAt( world, i, j, k ) )
				{
					if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
						world.setBlock( i, j, k, Block.fire.blockID );
					}
				}
				else
				{
					world.setBlockToAir( i, j, k );
				}
			}
			else
			{
				break;
			}
		}

		return false;
	}

	//----------- Client Side Functionality -----------//
}
