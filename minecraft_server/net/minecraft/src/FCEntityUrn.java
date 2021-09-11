// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

public class FCEntityUrn extends EntityThrowable
	implements FCIEntityPacketHandler
{
	static public final double m_dCubicRange = 4D;
	
    public int m_iItemShiftedIndex;
    
    public FCEntityUrn( World world )
    {
        super(world);
        
        m_iItemShiftedIndex = 0;
    }
    
    public FCEntityUrn( World world, int iItemShiftedIndex )
    {
    	this( world );
        
        m_iItemShiftedIndex = iItemShiftedIndex;
    }

    public FCEntityUrn( World world, EntityLiving throwingEntity, int iItemShiftedIndex )
    {
    	super( world, throwingEntity );
    	
        m_iItemShiftedIndex = iItemShiftedIndex;
    }

    public FCEntityUrn( World world, double d, double d1, double d2, int iItemShiftedIndex )
    {
    	super( world, d, d1, d2 );
        
        m_iItemShiftedIndex = iItemShiftedIndex;        
    }

	@Override
    public void writeEntityToNBT( NBTTagCompound nbttagcompound )
    {
		super.writeEntityToNBT( nbttagcompound );
		
        nbttagcompound.setInteger( "m_iItemShiftedIndex", m_iItemShiftedIndex );
    }

	@Override
    public void readEntityFromNBT( NBTTagCompound nbttagcompound )
    {
		super.readEntityFromNBT( nbttagcompound );
        
        m_iItemShiftedIndex = nbttagcompound.getInteger( "m_iItemShiftedIndex" );
    }

	@Override
    protected void onImpact( MovingObjectPosition impactPosition )
    {
        setDead();
        
        if ( !worldObj.isRemote )
        {
        	if ( m_iItemShiftedIndex == FCBetterThanWolves.fcItemSoulUrn.itemID )
        	{
        		boolean bLooseSoul = true;
        		
		        if (impactPosition.entityHit != null)
		        {
		            impactPosition.entityHit.attackEntityFrom( DamageSource.causeThrownDamage( this, getThrower() ), 0 );
		            
		            if ( impactPosition.entityHit instanceof FCEntityZombie )
		            {
		            	FCEntityZombie zombieHit = (FCEntityZombie)(impactPosition.entityHit);
		            	
		            	if ( zombieHit.AttemptToStartCure() )
		            	{
		            		bLooseSoul = false;
		            	}
		            }
		        }
		        else if ( m_iItemShiftedIndex == FCBetterThanWolves.fcItemSoulUrn.itemID  )
		        {
	        		if ( AttemptToCreateGolemOrWither( worldObj, impactPosition.blockX, impactPosition.blockY, impactPosition.blockZ ) )
	        		{
	        			bLooseSoul = false;
	        		}
		        }		

		        if ( bLooseSoul )
		        {
		    		AxisAlignedBB possessionBox = AxisAlignedBB.getAABBPool().getAABB( 
		    			 impactPosition.hitVec.xCoord - m_dCubicRange, impactPosition.hitVec.yCoord - m_dCubicRange, impactPosition.hitVec.zCoord - m_dCubicRange,
		    			 impactPosition.hitVec.xCoord + m_dCubicRange, impactPosition.hitVec.yCoord + m_dCubicRange, impactPosition.hitVec.zCoord + m_dCubicRange );
		    		
		            List nearbyCreatures = worldObj.getEntitiesWithinAABB( FCEntityZombie.class, possessionBox );
		            
		            Iterator creatureIterator = nearbyCreatures.iterator();
		        	
		            while ( creatureIterator.hasNext() && bLooseSoul )
		            {
		            	FCEntityZombie tempZombie = (FCEntityZombie)creatureIterator.next();
		        		
		            	if ( tempZombie.AttemptToStartCure() )
		            	{
		            		bLooseSoul = false;
		            		
				            tempZombie.attackEntityFrom( DamageSource.causeThrownDamage( this, getThrower() ), 0 );
		            	}
		            }    
		        }
		        
                worldObj.playAuxSFX( FCBetterThanWolves.m_iSoulUrnShatterAuxFXID, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 0 );                
        	}
        	else
        	{
		        if (impactPosition.entityHit != null)
		        {
		            impactPosition.entityHit.attackEntityFrom( DamageSource.causeThrownDamage( this, getThrower() ), 0 );
		        }
        	}
        }
    }
    
    //------------- FCIEntityPacketHandler ------------//

    @Override
    public int GetTrackerViewDistance()
    {
    	return 64;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 10;
    }
    
    @Override
    public boolean GetTrackMotion()
    {
    	return true;
    }
    
    @Override
    public boolean ShouldServerTreatAsOversized()
    {
    	return false;
    }
    
    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeUrn );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeInt( m_iItemShiftedIndex );
	        
	        dataStream.writeByte( (byte)(int)( motionX * 128D ) );
	        dataStream.writeByte( (byte)(int)( motionY * 128D ) );
	        dataStream.writeByte( (byte)(int)( motionZ * 128D ) );	        		
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    //------------- Class Specific Methods ------------//
    
    static public boolean AttemptToCreateGolemOrWither( World world, int i, int j, int k )
    {
    	// check if this is part of a column with a head on top
    	
    	for ( int iTempJ = j; iTempJ <= j + 2; iTempJ++ )
    	{
    		if ( IsGolemHeadBlock( world, i, iTempJ, k ) )
    		{
    			return AttemptToCreateSnowOrIronGolem( world, i, iTempJ, k );
    		}
    		else if ( IsWitherHeadBlock( world, i, iTempJ, k ) )
    		{
    			return AttemptToCreateWither( world, i, iTempJ, k );
    		}
    		else if ( !IsValidBodyBlockForSnowGolem( world, i, iTempJ, k ) &&
    			!IsValidBodyBlockForIronGolem( world, i, iTempJ, k ) &&
    			!IsWitherBodyBlock( world, i, iTempJ, k ) )
    		{
    			break;
    		}
    	}
    	
		// check if this is a possible "arm" of an iron golem
    	
    	if ( IsValidBodyBlockForIronGolem( world, i, j, k ) )
    	{
    		int iTempJ = j + 1;
    		
    		for ( int iTempI = i - 1; iTempI <= i + 1; iTempI++ )
    		{
        		for ( int iTempK = k - 1; iTempK <= k + 1; iTempK++ )
        		{
        			if ( IsGolemHeadBlock( world, iTempI, iTempJ, iTempK ) )
        			{
        				return AttemptToCreateSnowOrIronGolem( world, iTempI, iTempJ, iTempK );        				
        			}
        		}
    		}
    	}
    	
    	return false;
    }
    
    static private boolean IsGolemHeadBlock( World world, int i, int j, int k )
    {
		int iBlockID = world.getBlockId( i, j, k );
		
		return iBlockID == Block.pumpkin.blockID || 
			iBlockID == Block.pumpkinLantern.blockID;
    }
    
    static private boolean IsWitherHeadBlock( World world, int i, int j, int k )
    {
		int iBlockID = world.getBlockId( i, j, k );
		
		if ( iBlockID == Block.skull.blockID )
		{
            TileEntity tileEntity = world.getBlockTileEntity( i, j, k );
            
            if ( tileEntity != null && tileEntity instanceof TileEntitySkull )
            {            	
            	return ((TileEntitySkull)tileEntity).getSkullType() == 5; // infused skull
            }
		}
		
		return false;
    }
    
    static private boolean IsValidBodyBlockForSnowGolem( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	
    	return iBlockID == Block.blockSnow.blockID || 
    		iBlockID == FCBetterThanWolves.fcBlockSnowLoose.blockID ||
    		iBlockID == FCBetterThanWolves.fcBlockSnowSolid.blockID;
    }

    static private boolean IsValidBodyBlockForIronGolem( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	
    	return iBlockID == Block.blockIron.blockID;
    }

    static private boolean IsWitherBodyBlock( World world, int i, int j, int k )
    {
    	int iBlockID = world.getBlockId( i, j, k );
    	
    	if ( iBlockID == FCBetterThanWolves.fcAestheticOpaque.blockID )
    	{
    		int iSubtype = world.getBlockMetadata( i, j, k );
    		
    		if ( iSubtype == FCBlockAestheticOpaque.m_iSubtypeBone )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Assumes block at location has already been tested as a valid head block (pumpkin)
     */
    static private boolean AttemptToCreateSnowOrIronGolem( World world, int i, int j, int k )
    {
        if ( IsValidBodyBlockForSnowGolem( world, i, j - 1, k ) && 
        	IsValidBodyBlockForSnowGolem( world, i, j - 2, k ) )
        {
            world.setBlock( i, j, k, 0 );
            world.setBlock( i, j - 1, k, 0 );
            world.setBlock( i, j - 2, k, 0 );
            
            world.notifyBlockChange( i, j, k, 0 );
            world.notifyBlockChange( i, j - 1, k, 0 );
            world.notifyBlockChange( i, j - 2, k, 0 );
            
            FCEntitySnowman snowGolem = new FCEntitySnowman( world );
            
            snowGolem.setLocationAndAngles( (double)i + 0.5D, (double)j - 1.95D, (double)k + 0.5D, 0F, 0F);
            
            world.spawnEntityInWorld( snowGolem );
                
	        world.playAuxSFX( FCBetterThanWolves.m_iSnowGolemCreatedAuxFXID, i, j, k, 0 );

            return true;
        }
        else if ( IsValidBodyBlockForIronGolem( world, i, j - 1, k ) && 
        	IsValidBodyBlockForIronGolem( world, i, j - 2, k ) )
        {
            boolean bIronAlongIAxis = IsValidBodyBlockForIronGolem( world, i - 1, j - 1, k ) && 
            	IsValidBodyBlockForIronGolem( world, i + 1, j - 1, k );
            
            boolean bIronAlongKAxis = IsValidBodyBlockForIronGolem( world, i, j - 1, k - 1 ) && 
            	IsValidBodyBlockForIronGolem( world, i, j - 1, k + 1 );

            if ( bIronAlongIAxis || bIronAlongKAxis )
            {
                world.setBlock( i, j, k, 0 );
                world.setBlock( i, j - 1, k, 0 );
                world.setBlock( i, j - 2, k, 0 );

                if ( bIronAlongIAxis )
                {
                    world.setBlock( i - 1, j - 1, k, 0 );
                    world.setBlock( i + 1, j - 1, k, 0 );
                }
                else
                {
                    world.setBlock( i, j - 1, k - 1, 0 );
                    world.setBlock( i, j - 1, k + 1, 0 );
                }

                world.notifyBlockChange( i, j, k, 0 );
                world.notifyBlockChange( i, j - 1, k, 0 );
                world.notifyBlockChange( i, j - 2, k, 0 );

                if ( bIronAlongIAxis )
                {
                    world.notifyBlockChange( i - 1, j - 1, k, 0 );
                    world.notifyBlockChange( i + 1, j - 1, k, 0 );
                }
                else
                {
                    world.notifyBlockChange( i, j - 1, k - 1, 0 );
                    world.notifyBlockChange( i, j - 1, k + 1, 0 );
                }

                EntityIronGolem ironGolem = new EntityIronGolem( world );
                
                ironGolem.setPlayerCreated( true );
                ironGolem.setLocationAndAngles( (double)i + 0.5D, (double)j - 1.95D, (double)k + 0.5D, 0F, 0F);
                
                world.spawnEntityInWorld( ironGolem );

    	        world.playAuxSFX( FCBetterThanWolves.m_iIronGolemCreatedAuxFXID, i, j, k, 0 );    	        
            }
            
            return true;
        }
        
    	return false;
    }
    
    /**
     * Assumes block at location has already been tested as a valid head block (infused skull)
     */
    static private boolean AttemptToCreateWither( World world, int i, int j, int k )
    {
        if ( j >= 2 && world.provider.dimensionId == 0 )
        {
            for ( int iTempKOffset = -2; iTempKOffset <= 0; ++iTempKOffset )
            {
                if (
                	IsWitherBodyBlock( world, i, j - 1, k + iTempKOffset ) && 
                	IsWitherBodyBlock( world, i, j - 1, k + iTempKOffset + 1 ) && 
                	IsWitherBodyBlock( world, i, j - 2, k + iTempKOffset + 1 ) && 
                	IsWitherBodyBlock( world, i, j - 1, k + iTempKOffset + 2 ) && 
                	IsWitherHeadBlock( world, i, j, k + iTempKOffset ) && 
                	IsWitherHeadBlock( world, i, j, k + iTempKOffset + 1 ) && 
                	IsWitherHeadBlock( world, i, j, k + iTempKOffset + 2 ) )
                {
                	// This flags the skulls not to drop as an item when they're destroyed
                	
                    world.SetBlockMetadataWithNotify( i, j, k + iTempKOffset, 8, 2 );
                    world.SetBlockMetadataWithNotify( i, j, k + iTempKOffset + 1, 8, 2 );
                    world.SetBlockMetadataWithNotify( i, j, k + iTempKOffset + 2, 8, 2 );
                    
                    world.setBlock( i, j, k + iTempKOffset, 0, 0, 2 );
                    world.setBlock( i, j, k + iTempKOffset + 1, 0, 0, 2 );
                    world.setBlock( i, j, k + iTempKOffset + 2, 0, 0, 2 );
                                    
                    world.setBlock( i, j - 1, k + iTempKOffset, 0, 0, 2 );
                    world.setBlock( i, j - 1, k + iTempKOffset + 1, 0, 0, 2 );
                    world.setBlock( i, j - 1, k + iTempKOffset + 2, 0, 0, 2 );
                    world.setBlock( i, j - 2, k + iTempKOffset + 1, 0, 0, 2 );

                    FCEntityWitherPersistent.SummonWitherAtLocation( world, 
                    	i, j, k + iTempKOffset + 1 );
                    
                    world.notifyBlockChange( i, j, k + iTempKOffset, 0 );
                    world.notifyBlockChange( i, j, k + iTempKOffset + 1, 0 );
                    world.notifyBlockChange( i, j, k + iTempKOffset + 2, 0 );
                                             
                    world.notifyBlockChange( i, j - 1, k + iTempKOffset, 0 );
                    world.notifyBlockChange( i, j - 1, k + iTempKOffset + 1, 0 );
                    world.notifyBlockChange( i, j - 1, k + iTempKOffset + 2, 0 );
                    world.notifyBlockChange( i, j - 2, k + iTempKOffset + 1, 0 );
                    
                    return true;
                }
            }

            for ( int iTempIOffset = -2; iTempIOffset <= 0; ++iTempIOffset)
            {
                if (
                	IsWitherBodyBlock( world, i + iTempIOffset, j - 1, k ) && 
                	IsWitherBodyBlock( world, i + iTempIOffset + 1, j - 1, k ) && 
                	IsWitherBodyBlock( world, i + iTempIOffset + 1, j - 2, k ) && 
                	IsWitherBodyBlock( world, i + iTempIOffset + 2, j - 1, k ) && 
                	IsWitherHeadBlock( world, i + iTempIOffset, j, k ) && 
                	IsWitherHeadBlock( world, i + iTempIOffset + 1, j, k ) && 
                	IsWitherHeadBlock( world, i + iTempIOffset + 2, j, k ) )
                {
                	// This flags the skulls not to drop as an item when they're destroyed
                	
                    world.SetBlockMetadataWithNotify( i + iTempIOffset, j, k, 8, 2 );
                    world.SetBlockMetadataWithNotify( i + iTempIOffset + 1, j, k, 8, 2 );
                    world.SetBlockMetadataWithNotify( i + iTempIOffset + 2, j, k, 8, 2 );
                    
                    world.setBlock( i + iTempIOffset, j, k, 0, 0, 2 );
                    world.setBlock( i + iTempIOffset + 1, j, k, 0, 0, 2 );
                    world.setBlock( i + iTempIOffset + 2, j, k, 0, 0, 2 );
                    world.setBlock( i + iTempIOffset, j - 1, k, 0, 0, 2 );
                    world.setBlock( i + iTempIOffset + 1, j - 1, k, 0, 0, 2 );
                    world.setBlock( i + iTempIOffset + 2, j - 1, k, 0, 0, 2 );
                    world.setBlock( i + iTempIOffset + 1, j - 2, k, 0, 0, 2 );

                    FCEntityWitherPersistent.SummonWitherAtLocation( world, 
                    	i + iTempIOffset + 1, j, k );
                    
                    world.notifyBlockChange( i + iTempIOffset, j, k, 0 );
                    world.notifyBlockChange( i + iTempIOffset + 1, j, k, 0 );
                    world.notifyBlockChange( i + iTempIOffset + 2, j, k, 0 );
                    world.notifyBlockChange( i + iTempIOffset, j - 1, k, 0 );
                    world.notifyBlockChange( i + iTempIOffset + 1, j - 1, k, 0 );
                    world.notifyBlockChange( i + iTempIOffset + 2, j - 1, k, 0 );
                    world.notifyBlockChange( i + iTempIOffset + 1, j - 2, k, 0 );
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
	//----------- Client Side Functionality -----------//
}