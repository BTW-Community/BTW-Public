// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntityWindMillVertical extends FCEntityMechPower
{
	// constants
	
    static final public float m_fHeight = 6.8F;
    static final public float m_fWidth = 8.8F;
    
    static final private int m_iMaxDamage = 160;

    static final private float m_fRotationPerTick = -0.12F;
    static final private float m_fRotationPerTickInStorm = -2.0F;
    static final private float m_fRotationPerTickInHell = -0.07F;
    
    static final private int m_iTicksPerFullUpdate = 20;    
    static final private int m_iUpdatesToOverpower = 30;	// 30 seconds in a storm will overpower the mill
    
    // local vars 
    
	private static final int m_iBladeColor0DataWatcherID = 23;
	private static final int m_iBladeColor1DataWatcherID = 24;
	private static final int m_iBladeColor2DataWatcherID = 25;
	private static final int m_iBladeColor3DataWatcherID = 26;
	private static final int m_iBladeColor4DataWatcherID = 27;
	private static final int m_iBladeColor5DataWatcherID = 28;
	private static final int m_iBladeColor6DataWatcherID = 29;
	private static final int m_iBladeColor7DataWatcherID = 30;
    
    private int m_iCurrentBladeColoringIndex;
    
    protected int m_iOverpowerTimer;
    
    public FCEntityWindMillVertical( World world )
    {
        super( world );
        
        m_iCurrentBladeColoringIndex = 0;        
    }
    
    public FCEntityWindMillVertical( World world, double x, double y, double z  ) 
    {
    	super( world, x, y, z );
    }

    @Override
    protected void entityInit()
    {
    	super.entityInit();
    	
        dataWatcher.addObject( m_iBladeColor0DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor1DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor2DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor3DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor4DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor5DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor6DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor7DataWatcherID, new Byte( (byte) 0 ) );
    }
    
    public int getBladeColor( int iBladeIndex )
    {
        return (int)( dataWatcher.getWatchableObjectByte( m_iBladeColor0DataWatcherID + iBladeIndex ) );
    }
    
    public void setBladeColor( int iBladeIndex, int iColor )
    {
        dataWatcher.updateObject( m_iBladeColor0DataWatcherID + iBladeIndex, Byte.valueOf( (byte)iColor ) );
    }
    
	@Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    	nbttagcompound.setFloat( "fRotation", m_fRotation );
    	
    	nbttagcompound.setBoolean( "bProvidingPower", m_bProvidingPower );
    	
    	nbttagcompound.setInteger( "iOverpowerTimer", m_iOverpowerTimer );
    	
    	nbttagcompound.setInteger( "iBladeColors0", getBladeColor( 0 ) ); 
    	nbttagcompound.setInteger( "iBladeColors1", getBladeColor( 1 ) ); 
    	nbttagcompound.setInteger( "iBladeColors2", getBladeColor( 2 ) ); 
    	nbttagcompound.setInteger( "iBladeColors3", getBladeColor( 3 ) ); 
    	nbttagcompound.setInteger( "iBladeColors4", getBladeColor( 4 ) ); 
    	nbttagcompound.setInteger( "iBladeColors5", getBladeColor( 5 ) ); 
    	nbttagcompound.setInteger( "iBladeColors6", getBladeColor( 6 ) ); 
    	nbttagcompound.setInteger( "iBladeColors7", getBladeColor( 7 ) ); 
    }    	

	@Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
		m_fRotation = nbttagcompound.getFloat( "fRotation" );
    	
		m_bProvidingPower = nbttagcompound.getBoolean( "bProvidingPower" );
    	
		m_iOverpowerTimer = nbttagcompound.getInteger( "iOverpowerTimer" );
    	
    	setBladeColor( 0, nbttagcompound.getInteger( "iBladeColors0" ) );
    	setBladeColor( 1, nbttagcompound.getInteger( "iBladeColors1" ) );
    	setBladeColor( 2, nbttagcompound.getInteger( "iBladeColors2" ) );
    	setBladeColor( 3, nbttagcompound.getInteger( "iBladeColors3" ) );
    	setBladeColor( 4, nbttagcompound.getInteger( "iBladeColors4" ) );
    	setBladeColor( 5, nbttagcompound.getInteger( "iBladeColors5" ) );
    	setBladeColor( 6, nbttagcompound.getInteger( "iBladeColors6" ) );
    	setBladeColor( 7, nbttagcompound.getInteger( "iBladeColors7" ) );
    	
    	InitBoundingBox();
    }
    
	@Override
    public boolean interact( EntityPlayer player )
    {
        ItemStack itemstack = player.inventory.getCurrentItem();
        
        if ( itemstack != null && ( itemstack.itemID == Item.dyePowder.itemID ||
    		itemstack.itemID == FCBetterThanWolves.fcItemDung.itemID ) )
        {
        	if ( !worldObj.isRemote )
        	{
	        	int iColor = 0;
	        	
	        	if ( itemstack.itemID == Item.dyePowder.itemID )
	        	{
	        		iColor = BlockCloth.getBlockFromDye( itemstack.getItemDamage() );
	        	}
	        	else
	        	{
	        		iColor = 12; // brown smear for dung
	        	}
	            
	        	setBladeColor( m_iCurrentBladeColoringIndex, iColor );
	        	
	        	m_iCurrentBladeColoringIndex++;
	        	
	        	if ( m_iCurrentBladeColoringIndex >= 8 )
	        	{
	        		m_iCurrentBladeColoringIndex = 0;
	        	}
        	}
        	
        	itemstack.stackSize--;
        	
            if ( itemstack.stackSize == 0 )
            {
                player.inventory.setInventorySlotContents( player.inventory.currentItem, null);
            }
            
            return true;
        }
    	
        return super.interact( player );
    }
    
	@Override
    public void setDead()
    {
    	if ( m_bProvidingPower )
    	{   
    		boolean m_bAxlesPresent[] = new boolean[7];
    		
    		for ( int iTempIndex = 0; iTempIndex < 7; iTempIndex++ )
    		{
    			m_bAxlesPresent[iTempIndex] = false;
    		}
    		
        	int iCenterI = MathHelper.floor_double( posX );
        	int iCenterJ = MathHelper.floor_double( posY );
        	int iCenterK = MathHelper.floor_double( posZ );
        	
    		FCBlockAxle blockAxle = (FCBlockAxle)FCBetterThanWolves.fcBlockAxlePowerSource;
    		
    		// depower the center axles without neighbor notifications
        	
        	for ( int iTempJ = iCenterJ - 2; iTempJ <= iCenterJ + 2; iTempJ++ )
        	{
            	int iTempBlockID = worldObj.getBlockId( iCenterI, iTempJ, iCenterK );

            	if ( iTempBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
            	{
            		int iAxisAlignment = blockAxle.GetAxisAlignment( worldObj, iCenterI, iTempJ, iCenterK );
            		
            		if ( iAxisAlignment == 0 )
            		{
            			int iAxleIndex = iTempJ - iCenterJ + 3;
            			
            			m_bAxlesPresent[iAxleIndex] = true;
            			
                        worldObj.setBlock( iCenterI, iTempJ, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID, 0, 2 );
            		}            		
            	}
        	}
        	
        	// depower outlaying axles with notification so that power state change is propegated
        	
        	for ( int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ += 6 )
        	{
            	int iTempBlockID = worldObj.getBlockId( iCenterI, iTempJ, iCenterK );

            	if ( iTempBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
            	{
            		int iAxisAlignment = blockAxle.GetAxisAlignment( worldObj, iCenterI, iTempJ, iCenterK );
            		
            		if ( iAxisAlignment == 0 )
            		{
                        worldObj.setBlock( iCenterI, iTempJ, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID, 0, 3 );
            		}            		
            	}
        	}        	
    	}
    	
        super.setDead();
    }
    
    //------------- FCEntityMechPower ------------//
	
	@Override
    public float GetWidth()
	{
		return m_fWidth;
	}
    
	@Override
    public float GetHeight()
	{
		return m_fHeight;
	}
	
	@Override
    public float GetDepth()
	{
		return m_fWidth;
	}
	
	@Override
    public int GetMaxDamage()
	{
		return m_iMaxDamage;
	}
	
	@Override
    public int GetTicksPerFullUpdate()
	{
		return m_iTicksPerFullUpdate;
	}
	
	@Override
    protected void OnFullUpdateServer()
    {
		super.OnFullUpdateServer();
		
        // update overpower
        
        if ( m_iOverpowerTimer >= 0 )
        {
        	if ( m_iOverpowerTimer > 0 )
        	{
        		m_iOverpowerTimer--;
        	}
        	
        	if ( m_iOverpowerTimer <= 0 )
        	{
            	int iCenterI = MathHelper.floor_double( posX );
            	int iCenterJ = MathHelper.floor_double( posY );
            	int iCenterK = MathHelper.floor_double( posZ );
            	
        		// destroy any gearboxes attached to the windmill by overpowering the axle

	    		((FCBlockAxle)(FCBetterThanWolves.fcBlockAxle)).Overpower( 
    				worldObj, iCenterI, iCenterJ, iCenterK );	    		
        	}
        }
    }
    
	@Override
    public void DestroyWithDrop()
    {
    	if ( !isDead )
    	{
	    	dropItemWithOffset( FCBetterThanWolves.fcItemWindMillVertical.itemID, 1, 0.0F );
	    	
			setDead();
    	}
    }
    
	@Override
    public boolean ValidateAreaAroundDevice()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	return WindMillValidateAreaAroundBlock( worldObj, iCenterI, iCenterJ, iCenterK );
    }
    
    @Override
    protected float ComputeRotation()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	float fRotationAmount = 0.0F;
 
    	if ( worldObj.provider.dimensionId == -1 )
    	{
    		// Wind Mill always rotates in hell
    		
    		fRotationAmount = m_fRotationPerTickInHell;
    		
    		m_iOverpowerTimer = -1; 
    	}
    	else if( worldObj.provider.dimensionId != 1 && CanSeeSky() ) 
        {
	    	if ( worldObj.isThundering() && IsBeingPrecipitatedOn() ) 
	    	{
	    		fRotationAmount = m_fRotationPerTickInStorm;
	    		
	    		if ( m_iOverpowerTimer < 0 )
	    		{
	    			m_iOverpowerTimer = m_iUpdatesToOverpower; 
	    		}
	    	}
	    	else
	    	{
	    		fRotationAmount = m_fRotationPerTick;
	    		
	    		m_iOverpowerTimer = -1; 
	    	}
        }
    	else
    	{
    		m_iOverpowerTimer = -1; 
    	}
    	
		return fRotationAmount;
    }    
    
    @Override
    protected boolean ValidateConnectedAxles()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );

    	// check if we still have appropriately aligned axles in the center column
    	
    	for ( int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ++ )
    	{
	    	int iTempBlockID = worldObj.getBlockId( iCenterI, iTempJ, iCenterK );
	
	    	if ( FCUtilsMechPower.IsBlockIDAxle( iTempBlockID ) )
	    	{
	        	int iAxisAlignment = ((FCBlockAxle)(Block.blocksList[iTempBlockID])).GetAxisAlignment( worldObj, iCenterI, iTempJ, iCenterK );
	        	
	        	if ( iAxisAlignment != 0  )
				{
		    		return false;
				}            		
	    	}
	    	else
	    	{
	    		return false;
	    	}
    	}
    	
    	if ( !m_bProvidingPower )
    	{
	    	for ( int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ++ )
	    	{
		    	int iTempBlockID = worldObj.getBlockId( iCenterI, iTempJ, iCenterK );
		    	
	    		if ( ( (FCBlockAxle)Block.blocksList[iTempBlockID]).GetPowerLevel( worldObj, iCenterI, iTempJ, iCenterK ) > 0 )
	    		{
	    			// we have an unpowered device on a powered axle
	    			
	        		return false;
	    		}
	    	}
    	}
    	else
    	{    	
	    	for ( int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ++ )
	    	{
		    	int iTempBlockID = worldObj.getBlockId( iCenterI, iTempJ, iCenterK );
		    	
	    		if ( iTempBlockID != FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
	    		{
	    			// we have powered device on a unpowered axle.  Restore power (this is likely the result of a player-rotated axle or Gear Box).
	    			
	    			PowerAxleColumn();
	    			
	    			break;	    			
	    		}
	    	}
    	}
    	
    	return true;    	
    }
    
    @Override
    protected void TransferPowerStateToConnectedAxles()
    {
    	if ( m_bProvidingPower )
    	{
    		PowerAxleColumn();
    	}
    	else
    	{
    		DepowerAxleColumn();
    	}    	
    }
    
    @Override
	protected void OnClientRotationOctantChange()
	{
		float fSpeed = getRotationSpeed();
		
		if ( fSpeed < m_fRotationPerTick )
		{
	    	int iCenterI = MathHelper.floor_double( posX );
	    	int iCenterJ = MathHelper.floor_double( posY );
	    	int iCenterK = MathHelper.floor_double( posZ );
	    	
	    	int iCenterBlockID = worldObj.getBlockId( iCenterI, iCenterJ, iCenterK );
	    	
	    	if ( iCenterBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
	    	{
	    		int iAxleAlignment = ((FCBlockAxle)FCBetterThanWolves.fcBlockAxlePowerSource).GetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK );
	    		
	    		ClientNotifyGearboxOfOverpoweredOctantChangeInDirection( iCenterI, iCenterJ, iCenterK, iAxleAlignment << 1 );
	    		ClientNotifyGearboxOfOverpoweredOctantChangeInDirection( iCenterI, iCenterJ, iCenterK, ( iAxleAlignment << 1 ) + 1 );
	    	}
			
		}
	}
    
    //------------- FCIEntityPacketHandler ------------//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeWindMillVertical );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeInt( getRotationSpeedScaled() );
	        
	        dataStream.writeByte( (byte)getBladeColor( 0 ) );
	        dataStream.writeByte( (byte)getBladeColor( 1 ) );
	        dataStream.writeByte( (byte)getBladeColor( 2 ) );
	        dataStream.writeByte( (byte)getBladeColor( 3 ) );
	        dataStream.writeByte( (byte)getBladeColor( 4 ) );
	        dataStream.writeByte( (byte)getBladeColor( 5 ) );
	        dataStream.writeByte( (byte)getBladeColor( 6 ) );
	        dataStream.writeByte( (byte)getBladeColor( 7 ) );
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    //------------- Class Specific Methods ------------//
    
    private void PowerAxleColumn()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	// set the power level without notifying other axles on central blocks
    	
    	for ( int iTempJ = iCenterJ - 2; iTempJ <= iCenterJ + 2; iTempJ++ )
    	{
            worldObj.setBlock( iCenterI, iTempJ, iCenterK, FCBetterThanWolves.fcBlockAxlePowerSource.blockID, 0, 2 );
    	}
    	
    	// set power level on top and bottom blocks with notify to transfer power to other connected axles
    	
        worldObj.setBlock( iCenterI, iCenterJ + 3, iCenterK, FCBetterThanWolves.fcBlockAxlePowerSource.blockID, 0, 3 );
        worldObj.setBlock( iCenterI, iCenterJ + -3, iCenterK, FCBetterThanWolves.fcBlockAxlePowerSource.blockID, 0, 3 );        
    }
    
    private void DepowerAxleColumn()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	// set the power level without notifying other axles on central blocks
    	
    	for ( int iTempJ = iCenterJ - 2; iTempJ <= iCenterJ + 2; iTempJ++ )
    	{
            worldObj.setBlock( iCenterI, iTempJ, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID, 0, 2 );
    	}
    	
    	// set power level on top and bottom blocks with notify to transfer power to other connected axles
    	
        worldObj.setBlock( iCenterI, iCenterJ + 3, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID, 0, 3 );
        worldObj.setBlock( iCenterI, iCenterJ + -3, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID, 0, 3 );        
    }
    
    protected void ClientNotifyGearboxOfOverpoweredOctantChangeInDirection( int iCenterI, int iCenterJ, int iCenterK, int iFacing )    
    {
    	FCUtilsBlockPos tempPos = new FCUtilsBlockPos( iCenterI, iCenterJ, iCenterK );
    	
    	for ( int iTempCount = 0; iTempCount < 10; iTempCount++ ) // just to prevent running on indefinitely
    	{
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = worldObj.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == FCBetterThanWolves.fcBlockAxle.blockID || 
    			iTempBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
    		{
    			if ( ((FCBlockAxle)Block.blocksList[iTempBlockID]).IsAxleOrientedTowardsFacing( worldObj, tempPos.i, tempPos.j, tempPos.k, iFacing ) )
    			{
        			continue;
    			}
    		}
    		else if ( FCUtilsMechPower.IsPoweredGearBox( worldObj, 
    			tempPos.i, tempPos.j, tempPos.k ) )
    		{
				worldObj.playSound( tempPos.i + 0.5D, tempPos.j + 0.5D, tempPos.k + 0.5D, 
					"random.chestclosed", 1.5F, worldObj.rand.nextFloat() * 0.1F + 0.5F);
    		}
    		
    		break;
    	}
    }
    
    static public boolean WindMillValidateAreaAroundBlock( World world, int i, int j, int k )
    {
    	for ( int iTempI = i - 4; iTempI <= i + 4; iTempI++ )
    	{
        	for ( int iTempJ = j - 3; iTempJ <= j + 3; iTempJ++ )
        	{
            	for ( int iTempK = k - 4; iTempK <= k + 4; iTempK++ )
            	{
            		if ( iTempI != i || iTempK != k )
            		{
	    				if ( !IsValidBlockForWindMillToOccupy( world, iTempI, iTempJ, iTempK ) )
	    				{
	    					return false;
	    				}
            		}
            	}
        	}
    	}
    	
    	return true;
    }
    
    static public boolean IsValidBlockForWindMillToOccupy( World world, int i, int j, int k )
    {
    	if ( !world.isAirBlock( i, j, k ) )
    	{
			return false;
    	}
    	
    	return true;
    }    

    protected void InitBoundingBox()
    {
    	boundingBox.setBounds( posX - ( GetWidth() * 0.5F ), posY - ( GetHeight() * 0.5F ), posZ - ( GetWidth() * 0.5F ),
			posX + ( GetWidth() * 0.5F ), posY + ( GetHeight() * 0.5F ), posZ + ( GetWidth() * 0.5F ) );        			
    }
    
    private boolean CanSeeSky()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );

    	for ( int iTempI = iCenterI - 4; iTempI <= iCenterI + 4; iTempI++ )
    	{
        	for ( int iTempK = iCenterK - 4; iTempK <= iCenterK + 4; iTempK++ )
        	{
        		if ( worldObj.canBlockSeeTheSky( iTempI, iCenterJ, iTempK ) )
        		{
        			return true;
        		}
        	}
    	}
		
		return false;
    }
    
    private boolean IsBeingPrecipitatedOn()
    {
    	if ( worldObj.isRaining() )
    	{
        	int iCenterI = MathHelper.floor_double( posX );
        	int iCenterJ = MathHelper.floor_double( posY );
        	int iCenterK = MathHelper.floor_double( posZ );

        	for ( int iTempI = iCenterI - 4; iTempI <= iCenterI + 4; iTempI++ )
        	{
            	for ( int iTempK = iCenterK - 4; iTempK <= iCenterK + 4; iTempK++ )
            	{
            		if ( worldObj.IsPrecipitatingAtPos( iTempI, iTempK ) )
            		{
            			return true;
            		}
            	}
        	}
    	}
    	
    	return false;
    }
    
    protected AxisAlignedBB GetDeviceBounds()
    {
    	return AxisAlignedBB.getAABBPool().getAABB( 
    		posX - ( GetWidth() * 0.5F ), posY - ( GetHeight() * 0.5F ), posZ - ( GetWidth() * 0.5F ),
			posX + ( GetWidth() * 0.5F ), posY + ( GetHeight() * 0.5F ), posZ + ( GetWidth() * 0.5F ) );        			
    }
}