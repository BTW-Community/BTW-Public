//FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class FCEntityBlockLiftedByPlatform extends Entity
	implements FCIEntityPacketHandler, FCIEntityIgnoreServerValidation
{
    // local vars
    
    private int m_iBlockID;
    private int m_iBlockMetadata;

    public FCEntityBlockLiftedByPlatform( World world )
    {
        super( world );
        
        preventEntitySpawning = true;
        
        setSize( 0.98F, 0.98F );        
        yOffset = height / 2.0F;
        
        motionX = 0.0D;        
    	motionY = 0.0D;
        motionZ = 0.0D;        
    }
    
    public FCEntityBlockLiftedByPlatform( World world, int i, int j, int k )
    {
        this( world );
        
        int iBlockID = world.getBlockId( i, j, k );
        int iMetadata = world.getBlockMetadata( i, j, k );
        
        if ( iBlockID == Block.railPowered.blockID ||
    		iBlockID == Block.railDetector.blockID ||
    		iBlockID == FCBetterThanWolves.fcDetectorRailWood.blockID ||
    		iBlockID == FCBetterThanWolves.fcBlockDetectorRailSoulforgedSteel.blockID )
        {
        	iMetadata &= 7; // filter out power state
        }
        else if ( iBlockID == Block.redstoneWire.blockID )
        {
        	iMetadata = 0; // filter out power state
        }
        
        SetBlockID( iBlockID );
        SetBlockMetadata( iMetadata );
        
        setPosition( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F );
        
        lastTickPosX = prevPosX = posX;
        lastTickPosY = prevPosY = posY;
        lastTickPosZ = prevPosZ = posZ;   
        
        world.spawnEntityInWorld( this );
        
        world.setBlockWithNotify( i, j, k, 0 );
    }
    
    public FCEntityBlockLiftedByPlatform( World world, double x, double y, double z )
    {
        this( world );
        
        posX = x;
        posY = y;
        posZ = z;
        
        lastTickPosX = prevPosX = posX;
        lastTickPosY = prevPosY = posY;
        lastTickPosZ = prevPosZ = posZ;        
    }    
    
    public FCEntityBlockLiftedByPlatform( World world, double x, double y, double z, int iBlockID, int iMetadata )
    {
        this( world, x, y, z );
        
        m_iBlockID = iBlockID;
        m_iBlockMetadata = iMetadata;
    }    
    
	@Override
    protected void entityInit()
    {
    }
    
	@Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    	nbttagcompound.setInteger( "m_iBlockID", GetBlockID() );
    	nbttagcompound.setInteger( "m_iBlockMetaData", GetBlockMetadata() );
    }
    
	@Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    	SetBlockID( nbttagcompound.getInteger( "m_iBlockID" ) );
    	SetBlockMetadata( nbttagcompound.getInteger( "m_iBlockMetaData" ) );
    }
    
	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return null;
    }

	@Override
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

	@Override
    public boolean canBePushed()
    {
        return false;
    }

	@Override
    public boolean canBeCollidedWith()
    {
        return false;
    }
    
	@Override
    public void applyEntityCollision(Entity entity)
    {
    }
    
	@Override
    public void onUpdate()
    {
    	if ( isDead )
    	{
    		return;
    	}

    	// search for the associated platform
    
		FCEntityMovingPlatform associatedMovingPlatform = null;
		
        List collisionList = worldObj.getEntitiesWithinAABB( FCEntityMovingPlatform.class, 
        		AxisAlignedBB.getAABBPool().getAABB( 
    			posX - 0.25F, posY - 1.25F, posZ - 0.25F,
    			posX + 0.25F, posY - 0.75F, posZ + 0.25F ) );
        
        if ( collisionList != null && collisionList.size() > 0 )
        {        	
        	associatedMovingPlatform = (FCEntityMovingPlatform)collisionList.get( 0 );
        	
        	if ( !associatedMovingPlatform.isDead )
        	{
		    	double newPosX = associatedMovingPlatform.posX;
		    	double newPosY = associatedMovingPlatform.posY + 1.0D;
		    	double newPosZ = associatedMovingPlatform.posZ;
		    	
		        prevPosX = posX;
		        prevPosY = posY;
		        prevPosZ = posZ;
		        
		        setPosition( newPosX, newPosY, newPosZ );
        	}
        	else
        	{
        		associatedMovingPlatform = null;
        	}
        }
        
    	if ( !(worldObj.isRemote) )
    	{
	        if ( associatedMovingPlatform == null )
	        {
	        	// we've lost the platform we're on
	        	
	    		int i = MathHelper.floor_double( posX );
	        	int j = MathHelper.floor_double( posY );
	    		int k = MathHelper.floor_double( posZ );
	    		
				ConvertToBlock( i, j, k );
	        }
    	}
    }
    
	@Override
    public void moveEntity( double deltaX, double deltaY, double deltaZ )
    {
    	// this might be called by external sources (like the pistons), so we have to override it
    	
    	// since we are already dealing with a moving platform here, and since handling it any other way 
    	// would result in a ton of exception cases forming, just destroy the platform outright.
    	
    	DestroyBlockWithDrop();
    }
    
    @Override
    protected boolean ShouldSetPositionOnLoad()
    {
    	return false;
    }
    
    //------------- FCIEntityPacketHandler ------------//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeBlockLiftedByPlatform );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeInt( GetBlockID() );
	        dataStream.writeInt( GetBlockMetadata() );	        
	        
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    @Override
    public int GetTrackerViewDistance()
    {
    	return 160;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 3;
    }
    
    @Override
    public boolean GetTrackMotion()
    {
    	return false;
    }
    
    @Override
    public boolean ShouldServerTreatAsOversized()
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
    public int GetBlockID()
    {
        return m_iBlockID;
    }
    
    public void SetBlockID( int iBlockID )
    {
        m_iBlockID = iBlockID;
    }
    
    public int GetBlockMetadata()
    {
        return m_iBlockMetadata;
    }
    
    public void SetBlockMetadata( int iMetadata )
    {
        m_iBlockMetadata = iMetadata;
    }
    
    public void DestroyBlockWithDrop()
    {
		int i = MathHelper.floor_double( posX );
    	int j = MathHelper.floor_double( posY );
		int k = MathHelper.floor_double( posZ );
		
    	int idDropped = Block.blocksList[GetBlockID()].idDropped( 0, worldObj.rand, 0 );
    	
    	if ( idDropped > 0 )
    	{
			FCUtilsItem.EjectSingleItemWithRandomOffset( worldObj, i, j, k, idDropped, 0 );
    	}
		
    	setDead();
    }
    
    private void ConvertToBlock( int i, int j, int k )
    {
    	boolean bDestroyBlock = true;
    	
		if ( worldObj.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcPlatform.blockID )
		{
			// we have a platform beneath us 
			
	    	if ( FCUtilsWorld.IsReplaceableBlock( worldObj, i, j, k ) )
	    	{
	    		worldObj.setBlockAndMetadataWithNotify( i, j, k, GetBlockID(), GetBlockMetadata() );
	    		
	    		bDestroyBlock = false;
	    	}
		}
		
		if ( bDestroyBlock )
		{
			DestroyBlockWithDrop();
		}
		else
		{		
			setDead();
		}
    }
    
    static public boolean CanBlockBeConvertedToEntity( World world, int i, int j, int k )
    {
    	int iTargetBlockID = world.getBlockId( i, j, k );
    	
    	Block targetBlock = Block.blocksList[iTargetBlockID];
    	
    	if ( targetBlock != null )
    	{
    		if ( targetBlock instanceof BlockRailBase )
    		{
    			int iTargetMetaData = world.getBlockMetadata( i, j, k );
    			
    			if ( iTargetMetaData >=2 & iTargetMetaData <= 5 )
    			{
    				// rail is angled upwards.  It can not be lifted.
    				
    				return false;
    			}
    			else
    			{
    				return true;
    			}
    		}
    		else if ( iTargetBlockID == Block.redstoneWire.blockID )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
	
	@Override
    public float getShadowSize()
    {
        return 0.0F;
    }
    
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    	// empty override to prevent network entity teleport packets from fucking up position and bounding box
    }    
}
