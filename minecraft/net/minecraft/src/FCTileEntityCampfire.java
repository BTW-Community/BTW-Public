// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCTileEntityCampfire extends TileEntity
	implements FCITileEntityDataPacketHandler
{
	private final int m_iCampfireBurnTimeMultiplier = 8;
	
    private final int m_iTimeToCook = ( TileEntityFurnace.m_iDefaultCookTime * 
    	m_iCampfireBurnTimeMultiplier * 
    	3 / 2 ); // this line represents efficiency relative to furnace cooking
    
	private final int m_iMaxBurnTime = ( 5 * FCUtilsMisc.m_iTicksPerMinute );
	
    private final int m_iInitialBurnTime = ( 50 * 4 * m_iCampfireBurnTimeMultiplier * 
    	TileEntityFurnace.m_iBaseBurnTimeMultiplier ); // 50 is the furnace burn time of a shaft
    
    private final int m_iWarmupTime = ( 10 * FCUtilsMisc.m_iTicksPerSecond );
    private final int m_iRevertToSmallTime = ( 20 * FCUtilsMisc.m_iTicksPerSecond );
    private final int m_iBlazeTime = ( m_iInitialBurnTime * 3 / 2 );
    
    private final int m_iSmoulderTime = ( 5 * FCUtilsMisc.m_iTicksPerMinute ); // used to be 2 minutes
    
    private final int m_iTimeToBurnFood = ( m_iTimeToCook / 2 );
    
    private final float m_fChanceOfFireSpread = 0.05F;
    private final float m_fChanceOfGoingOutFromRain = 0.01F;
    
	private ItemStack m_spitStack = null;
	private ItemStack m_cookStack = null;
	
	private int m_iBurnTimeCountdown = 0;
	private int m_iBurnTimeSinceLit = 0;
	private int m_iCookCounter = 0;
	private int m_iSmoulderCounter = 0;
	private int m_iCookBurningCounter = 0;
	
    public FCTileEntityCampfire()
    {
    	super();
    }
    
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        NBTTagCompound spitTag = tag.getCompoundTag( "fcSpitStack" );

        if ( spitTag != null )
        {
            m_spitStack = ItemStack.loadItemStackFromNBT( spitTag );
        }
        
        NBTTagCompound cookTag = tag.getCompoundTag( "fcCookStack" );

        if ( cookTag != null )
        {
            m_cookStack = ItemStack.loadItemStackFromNBT( cookTag );
        }
        
        if ( tag.hasKey( "fcBurnCounter" ) )
        {
        	m_iBurnTimeCountdown = tag.getInteger( "fcBurnCounter" );
        }
        
        if ( tag.hasKey( "fcBurnTime" ) )
        {
        	m_iBurnTimeSinceLit = tag.getInteger( "fcBurnTime" );
        }
        
        if ( tag.hasKey( "fcCookCounter" ) )
        {
        	m_iCookCounter = tag.getInteger( "fcCookCounter" );
        }
        
        if ( tag.hasKey( "fcSmoulderCounter" ) )
        {
        	m_iSmoulderCounter = tag.getInteger( "fcSmoulderCounter" );
        }
        
        if ( tag.hasKey( "fcCookBurning" ) )
        {
        	m_iCookBurningCounter = tag.getInteger( "fcCookBurning" );
        }        	
    }
    
    @Override
    public void writeToNBT( NBTTagCompound tag)
    {
        super.writeToNBT( tag );
        
        if ( m_spitStack != null)
        {
            NBTTagCompound spitTag = new NBTTagCompound();
            
            m_spitStack.writeToNBT( spitTag );
            
            tag.setCompoundTag( "fcSpitStack", spitTag );
        }
        
        if ( m_cookStack != null)
        {
            NBTTagCompound cookTag = new NBTTagCompound();
            
            m_cookStack.writeToNBT( cookTag );
            
            tag.setCompoundTag( "fcCookStack", cookTag );
        }
        
        tag.setInteger( "fcBurnCounter", m_iBurnTimeCountdown );
        tag.setInteger( "fcBurnTime", m_iBurnTimeSinceLit );
        tag.setInteger( "fcCookCounter", m_iCookCounter );
        tag.setInteger( "fcSmoulderCounter", m_iSmoulderCounter );
        tag.setInteger( "fcCookBurning", m_iCookBurningCounter );
    }
        
    @Override
    public void updateEntity()
    {
    	super.updateEntity();   

    	if ( !worldObj.isRemote )
    	{
    		int iCurrentFireLevel = GetCurrentFireLevel();
    		
	    	if ( iCurrentFireLevel > 0 )
	    	{
	    		if ( iCurrentFireLevel > 1 && worldObj.rand.nextFloat() <= m_fChanceOfFireSpread )
	    		{
    				FCBlockFire.CheckForFireSpreadFromLocation( worldObj, xCoord, yCoord, zCoord, worldObj.rand, 0 );
	    		}
	    		
	    		m_iBurnTimeSinceLit++;
	    		
	    		if ( m_iBurnTimeCountdown > 0 )
	    		{
	    			m_iBurnTimeCountdown--;
	    			
	    			if ( iCurrentFireLevel == 3 )
	    			{
	    				// blaze burns extra fast
	    				
		    			m_iBurnTimeCountdown--;
	    			}
	    		}
	    		
	    		iCurrentFireLevel = ValidateFireLevel();
	    		
	        	if ( iCurrentFireLevel > 0 )
	        	{
		    		UpdateCookState();
		    		
	        		if ( worldObj.rand.nextFloat() <= m_fChanceOfGoingOutFromRain && IsRainingOnCampfire() )
	        		{
	        			ExtinguishFire( false );
	        		}
	        	}
	    	}
	    	else if ( m_iSmoulderCounter > 0 )
    		{
	    		m_iSmoulderCounter--;
	    		
	    		if ( m_iSmoulderCounter == 0 || worldObj.rand.nextFloat() <= m_fChanceOfGoingOutFromRain && IsRainingOnCampfire() )
	    		{
	    			StopSmouldering();
	    		}
    		}
    	}
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        
        if ( m_cookStack != null)
        {
            NBTTagCompound cookTag = new NBTTagCompound();
            
            m_cookStack.writeToNBT( cookTag );
            
            tag.setCompoundTag( "x", cookTag );
        }
        
        if ( m_spitStack != null)
        {
            NBTTagCompound spitTag = new NBTTagCompound();
            
            m_spitStack.writeToNBT( spitTag );
            
            tag.setCompoundTag( "y", spitTag );
        }
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, tag );
    }
    
    //------------- FCITileEntityDataPacketHandler ------------//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound tag )
    {
        NBTTagCompound cookTag = tag.getCompoundTag( "x" );

        if ( cookTag != null )
        {
            m_cookStack = ItemStack.loadItemStackFromNBT( cookTag );
        }        
        
        NBTTagCompound spitTag = tag.getCompoundTag( "y" );

        if ( spitTag != null )
        {
            m_spitStack = ItemStack.loadItemStackFromNBT( spitTag );
        }        
        
        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }    
    
    //------------- Class Specific Methods ------------//
    
    public void SetSpitStack( ItemStack stack ) 
    {
    	if ( stack != null )
    	{
	    	m_spitStack = stack.copy();
	    	
	    	m_spitStack.stackSize = 1;
    	}
    	else
    	{
    		m_spitStack = null;
    	}
    	
        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }
    
    public ItemStack GetSpitStack()
    {
    	return m_spitStack;
    }
    
    public void SetCookStack( ItemStack stack )
    {
    	if ( stack != null )
    	{
	    	m_cookStack = stack.copy();
	    	
	    	m_cookStack.stackSize = 1;
    	}
    	else
    	{
    		m_cookStack = null;
    		
    		m_iCookBurningCounter = 0;
    	}
    	
		m_iCookCounter = 0;
		
        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }
    
    public ItemStack GetCookStack()
    {
    	return m_cookStack;
    }
    
    public void EjectContents()
    {
    	if ( m_spitStack != null )
    	{
    		FCUtilsItem.EjectStackWithRandomOffset( worldObj, this.xCoord, yCoord, zCoord, m_spitStack );
    		
    		m_spitStack = null;
    	}
    	
    	if ( m_cookStack != null )
    	{
    		FCUtilsItem.EjectStackWithRandomOffset( worldObj, this.xCoord, yCoord, zCoord, m_cookStack );
    		
    		m_cookStack = null;
    	}
    }    
    
    public void AddBurnTime( int iBurnTime )
    {
    	m_iBurnTimeCountdown += iBurnTime * m_iCampfireBurnTimeMultiplier * 
    		TileEntityFurnace.m_iBaseBurnTimeMultiplier;
    	
    	if ( m_iBurnTimeCountdown > m_iMaxBurnTime )
    	{
    		m_iBurnTimeCountdown = m_iMaxBurnTime;
    	}
    	
    	ValidateFireLevel();
    }
 
    public void OnFirstLit()
    {
    	m_iBurnTimeCountdown = m_iInitialBurnTime;
    	m_iBurnTimeSinceLit = 0;
    }
    
    public int ValidateFireLevel()
    {
    	int iCurrentFireLevel = GetCurrentFireLevel();
    	
    	if ( iCurrentFireLevel > 0 )
    	{
    		//int iFuelState = FCBetterThanWolves.fcBlockCampfireUnlit.GetFuelState( worldObj, xCoord, yCoord, zCoord );
    		
    		if ( m_iBurnTimeCountdown <= 0 )
    		{
				ExtinguishFire( true );
				
				return 0;
    		}
    		else
    		{
				int iDesiredFireLevel = 2;
				
	    		if ( m_iBurnTimeSinceLit < m_iWarmupTime || m_iBurnTimeCountdown < m_iRevertToSmallTime )
				{
					iDesiredFireLevel = 1;
				}
				else if ( m_iBurnTimeCountdown > m_iBlazeTime )
				{
					iDesiredFireLevel = 3;
				}
				
				if ( iDesiredFireLevel != iCurrentFireLevel )
				{
					ChangeFireLevel( iDesiredFireLevel );
					
					if ( iDesiredFireLevel == 1 && iCurrentFireLevel == 2 )
					{
						worldObj.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, xCoord, yCoord, zCoord, 1 );						
					}
	    			
	    			return iDesiredFireLevel;
				}
    		}
				
    	}
		else // iCurrenFireLevel == 0 
		{
			if ( m_iBurnTimeCountdown > 0 && 
				FCBetterThanWolves.fcBlockCampfireUnlit.GetFuelState( worldObj, xCoord, yCoord, zCoord ) == 
				FCBlockCampfire.m_iCampfireFuelStateSmouldering )
			{
				RelightSmouldering();
				
				return 1;
			}
		}
    	
    	return iCurrentFireLevel;
    }
    
    private void ExtinguishFire( boolean bSmoulder )
    {
    	if ( bSmoulder )
    	{
    		m_iSmoulderCounter = m_iSmoulderTime;
    	}
    	else
    	{
    		m_iSmoulderCounter = 0;
    	}
    	
    	m_iCookCounter = 0; // reset cook counter in case fire is relit later
    	m_iCookBurningCounter = 0;
    	
    	FCBlockCampfire block = (FCBlockCampfire)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	block.ExtinguishFire( worldObj, xCoord, yCoord, zCoord, bSmoulder );
    }
    
    private void ChangeFireLevel( int iNewLevel )
    {
    	FCBlockCampfire block = (FCBlockCampfire)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	block.ChangeFireLevel( worldObj, xCoord, yCoord, zCoord, iNewLevel, worldObj.getBlockMetadata( xCoord, yCoord, zCoord ) );
    }
    
    private int GetCurrentFireLevel()
    {
    	FCBlockCampfire block = (FCBlockCampfire)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	return block.m_iFireLevel;
    }
    
	private void UpdateCookState()
	{
		if ( m_cookStack != null )
		{
			int iFireLevel = GetCurrentFireLevel();
			
			if ( iFireLevel >= 2 )
			{				
				ItemStack cookResult = FCCraftingManagerCampfire.instance.GetRecipeResult( m_cookStack.getItem().itemID );
				
				if ( cookResult != null )
				{
					m_iCookCounter++;
					
					if ( m_iCookCounter >= m_iTimeToCook )
					{
						SetCookStack( cookResult );
						
						m_iCookCounter = 0;
						
						// don't reset burn counter here, as the food can still burn after cooking
					}
				}
				
				if ( iFireLevel >= 3 && m_cookStack.itemID != FCBetterThanWolves.fcItemMeatBurned.itemID )
				{
					m_iCookBurningCounter++;
					
					if ( m_iCookBurningCounter >= m_iTimeToBurnFood )
					{
						SetCookStack( new ItemStack( FCBetterThanWolves.fcItemMeatBurned ) );
						
						m_iCookCounter = 0;
						m_iCookBurningCounter = 0;
					}
				}
			}
		}
	}
	
	public boolean GetIsCooking()
	{
		if ( m_cookStack != null && GetCurrentFireLevel() >= 2 )
		{
			ItemStack cookResult = FCCraftingManagerCampfire.instance.GetRecipeResult( m_cookStack.getItem().itemID );
			
			if ( cookResult != null )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean GetIsFoodBurning()
	{
		if ( m_cookStack != null && GetCurrentFireLevel() >= 3 && m_cookStack.itemID != FCBetterThanWolves.fcItemMeatBurned.itemID )
		{
			return true;
		}
		
		return false;
	}
	
    public boolean IsRainingOnCampfire()
    {
    	FCBlockCampfire block = (FCBlockCampfire)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	return block.IsRainingOnCampfire( worldObj, xCoord, yCoord, zCoord );
    }
    
    private void StopSmouldering()
    {
    	m_iSmoulderCounter = 0;
    	
    	FCBlockCampfire block = (FCBlockCampfire)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	block.StopSmouldering( worldObj, xCoord, yCoord, zCoord );
    }
    
    private void RelightSmouldering()
    {
    	m_iBurnTimeSinceLit = 0;
    	
    	FCBlockCampfire block = (FCBlockCampfire)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	block.RelightFire( worldObj, xCoord, yCoord, zCoord );
    }
    
    //----------- Client Side Functionality -----------//
}