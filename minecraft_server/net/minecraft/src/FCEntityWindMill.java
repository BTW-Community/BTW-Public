// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntityWindMill extends FCEntityMechPowerHorizontal
{
	// constants
	
    static final public float m_fHeight = 12.8F;
    static final public float m_fWidth = 12.8F;
    static final public float m_fDepth = 0.8F;
    
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
    
    private int m_iCurrentBladeColoringIndex;
    
    protected int m_iOverpowerTimer;
    
    private boolean m_bLegacyWindMill;    
    
    public FCEntityWindMill( World world )
    {
        super( world );
        
        m_iCurrentBladeColoringIndex = 0;
        
        m_bLegacyWindMill = false;
    }
    
    public FCEntityWindMill( World world, double x, double y, double z, boolean bIAligned  ) 
    {
    	super( world, x, y, z, bIAligned );
    }

    @Override
    protected void entityInit()
    {
    	super.entityInit();
    	
        dataWatcher.addObject( m_iBladeColor0DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor1DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor2DataWatcherID, new Byte( (byte) 0 ) );
        dataWatcher.addObject( m_iBladeColor3DataWatcherID, new Byte( (byte) 0 ) );
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
    	nbttagcompound.setBoolean( "bWindMillIAligned", m_bIAligned );
    	
    	nbttagcompound.setFloat( "fRotation", m_fRotation );
    	
    	nbttagcompound.setBoolean( "bProvidingPower", m_bProvidingPower );
    	
    	nbttagcompound.setInteger( "iOverpowerTimer", m_iOverpowerTimer );
    	
    	nbttagcompound.setInteger( "iBladeColors0", getBladeColor( 0 ) ); 
    	nbttagcompound.setInteger( "iBladeColors1", getBladeColor( 1 ) ); 
    	nbttagcompound.setInteger( "iBladeColors2", getBladeColor( 2 ) ); 
    	nbttagcompound.setInteger( "iBladeColors3", getBladeColor( 3 ) );
    	
    	nbttagcompound.setBoolean( "fcLegacy", m_bLegacyWindMill ); 
    }    	

	@Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
		m_bIAligned = nbttagcompound.getBoolean( "bWindMillIAligned" );
    	
		m_fRotation = nbttagcompound.getFloat( "fRotation" );
    	
		m_bProvidingPower = nbttagcompound.getBoolean( "bProvidingPower" );
    	
		m_iOverpowerTimer = nbttagcompound.getInteger( "iOverpowerTimer" );
    	
    	setBladeColor( 0, nbttagcompound.getInteger( "iBladeColors0" ) );
    	setBladeColor( 1, nbttagcompound.getInteger( "iBladeColors1" ) );
    	setBladeColor( 2, nbttagcompound.getInteger( "iBladeColors2" ) );
    	setBladeColor( 3, nbttagcompound.getInteger( "iBladeColors3" ) );
    	
        if ( nbttagcompound.hasKey( "fcLegacy" ) )
        {
        	m_bLegacyWindMill = nbttagcompound.getBoolean( "fcLegacy" );
        }
        else
        {
        	m_bLegacyWindMill = true;
        }
        
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
	        	
	        	if ( m_iCurrentBladeColoringIndex >= 4 )
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
    
    //************* FCEntityMechPower ************//
	
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
		return m_fDepth;
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
	    	dropItemWithOffset( FCBetterThanWolves.fcItemWindMill.itemID, 1, 0.0F );
	    	
			setDead();
    	}
    }
    
	@Override
    public boolean ValidateAreaAroundDevice()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	return WindMillValidateAreaAroundBlock( worldObj, iCenterI, iCenterJ, iCenterK, m_bIAligned );
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
    		if ( m_bLegacyWindMill )
    		{
	    		// Old Wind Mills always rotates in hell, but new ones do not
	    		
	    		fRotationAmount = m_fRotationPerTickInHell;	    		
    		}
    		
    		m_iOverpowerTimer = -1;
    	}
    	else if( worldObj.provider.dimensionId != 1 && worldObj.canBlockSeeTheSky( iCenterI, iCenterJ, iCenterK ) )
        {
	    	if ( worldObj.isThundering() && worldObj.IsPrecipitatingAtPos( iCenterI, iCenterK ) ) 
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
    
    //************* FCIEntityPacketHandler ************//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        byte bIAligned = 0;
	        
	        if ( m_bIAligned )
	        {
	        	bIAligned = 1;
	        }
	        
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeWindMill );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeByte( bIAligned );
	        dataStream.writeInt( getRotationSpeedScaled() );
	        
	        dataStream.writeByte( (byte)getBladeColor( 0 ) );
	        dataStream.writeByte( (byte)getBladeColor( 1 ) );
	        dataStream.writeByte( (byte)getBladeColor( 2 ) );
	        dataStream.writeByte( (byte)getBladeColor( 3 ) );
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    //************* Class Specific Methods ************//

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
    
    static public boolean WindMillValidateAreaAroundBlock( World world, int i, int j, int k, boolean bIAligned )
    {
    	int iOffset;
    	int kOffset;
    	
    	if ( bIAligned )
    	{
    		iOffset = 0;    		
    		kOffset = 1;
    	}
    	else
    	{
       		iOffset = 1;    		
    		kOffset = 0;
    	}
    	
    	for ( int iHeightOffset = -6; iHeightOffset <= 6; iHeightOffset++ )
    	{
    		for ( int iWidthOffset = -6; iWidthOffset <= 6; iWidthOffset++ )
    		{
    			if ( iHeightOffset != 0 || iWidthOffset != 0 )
    			{
    				int tempI = i + ( iOffset * iWidthOffset );
    				int tempJ = j + iHeightOffset;
    				int tempK = k + ( kOffset * iWidthOffset );
    				
    				if ( !IsValidBlockForWindMillToOccupy( world, tempI, tempJ, tempK ) )
    				{
    					return false;
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
}