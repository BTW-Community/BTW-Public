// FCMOD

package net.minecraft.src;

public class FCTileEntityFurnaceBrick extends TileEntityFurnace
	implements FCITileEntityDataPacketHandler
{
    static private final float m_fChanceOfFireSpread = 0.01F;
    
	private boolean m_bLightOnNextUpdate = false;
	
	private ItemStack m_cookStack = null;
	
	private int m_iUnlitFuelBurnTime = 0;
	private int m_iVisualFuelLevel = 0;

	private final int m_iBrickBurnTimeMultiplier = 4; // applied on top of base multiplier of standard furnace
	private final int m_iCookTimeMultiplier = 4;
	
	// the following is not the actual maximum time, but rather the point above which additional fuel can no longer be added 
	//private final int m_iMaxFuelBurnTime = ( 1600 * 2 * 2 ); // 1600 oak log burn time, 2x base furnace multiplier, 2x brick furnace multiplier
	// the following is an actual max
	private final int m_iMaxFuelBurnTime = ( ( 64 + 7 ) * 25 * 2 * m_iBrickBurnTimeMultiplier ); // 64 + 7 buffer, 25x saw dust, 2x base furnace multiplier
	
	private final int m_iVisualFuelLevelIncrement = ( 200 * 2 * m_iBrickBurnTimeMultiplier );
	private final int m_iVisualSputterFuelLevel = ( m_iVisualFuelLevelIncrement / 4 );
	
	@Override
    public void updateEntity()
    {
        boolean bWasBurning = furnaceBurnTime > 0;
        boolean bInventoryChanged = false;

        if ( furnaceBurnTime > 0 )
        {
            --furnaceBurnTime;
        }

        if ( !worldObj.isRemote )
        {        	
            if ( bWasBurning || m_bLightOnNextUpdate )
            {
            	furnaceBurnTime += m_iUnlitFuelBurnTime;
            	m_iUnlitFuelBurnTime = 0;
                
                m_bLightOnNextUpdate = false;
            }

            if ( isBurning() && canSmelt() )
            {
                ++furnaceCookTime;

                if ( furnaceCookTime >= GetCookTimeForCurrentItem() )
                {
                    furnaceCookTime = 0;
                    smeltItem();
                    bInventoryChanged = true;
                }
            }
            else
            {
                furnaceCookTime = 0;
            }
            
    		if ( isBurning() && worldObj.rand.nextFloat() <= m_fChanceOfFireSpread )
    		{
    			FCUtilsBlockPos frontPos = new FCUtilsBlockPos( xCoord, yCoord, zCoord );
    			int iFacing = worldObj.getBlockMetadata( xCoord, yCoord, zCoord ) & 7;

    			frontPos.AddFacingAsOffset( iFacing );
    			
				FCBlockFire.CheckForFireSpreadAndDestructionToOneBlockLocation( worldObj, frontPos.i, frontPos.j, frontPos.k );
    		}

            FCBlockFurnace furnaceBlock = (FCBlockFurnace)Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )];            
            
            if ( bWasBurning != isBurning() )
            {
                bInventoryChanged = true;
                
                furnaceBlock.updateFurnaceBlockState( furnaceBurnTime > 0, worldObj, xCoord, yCoord, zCoord, false );
            }
            
            UpdateCookStack();            
            UpdateVisualFuelLevel();            
        }

        if ( bInventoryChanged )
        {
            onInventoryChanged();
        }
    }

	@Override
    public String getInvName()
    {
        return "container.fcFurnaceBrick";
    }
	
	@Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        if ( tag.hasKey( "fcUnlitFuel" ) )
        {
        	m_iUnlitFuelBurnTime = tag.getInteger( "fcUnlitFuel" );
        }
        
        if ( tag.hasKey( "fcVisualFuel" ) )
        {
        	m_iVisualFuelLevel = tag.getByte( "fcVisualFuel" );
        }
    }
	
	@Override
    public void writeToNBT( NBTTagCompound tag )
    {
        super.writeToNBT( tag );
        
        tag.setInteger( "fcUnlitFuel", m_iUnlitFuelBurnTime );
        tag.setByte( "fcVisualFuel", (byte)m_iVisualFuelLevel );
    }	
    
	@Override
    public int getItemBurnTime( ItemStack stack )
    {
    	return super.getItemBurnTime( stack ) * m_iBrickBurnTimeMultiplier;
    }

	@Override
    protected int GetCookTimeForCurrentItem()
    {
		return super.GetCookTimeForCurrentItem() * m_iCookTimeMultiplier;
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
        
        tag.setByte( "y", (byte)m_iVisualFuelLevel );

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

        m_iVisualFuelLevel = tag.getByte( "y" );
        
        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }    
    
    //------------- Class Specific Methods ------------//
    
	public boolean AttemptToLight()
	{
		if ( m_iUnlitFuelBurnTime > 0 )
		{
			// lighting has to be done on update to prevent funkiness with tile entity removal on block being set
			
			m_bLightOnNextUpdate = true;
		
			return true;
		}
		
		return false;
	}
	
	public boolean HasValidFuel()
	{
		return m_iUnlitFuelBurnTime > 0;
	}
	
    private void UpdateCookStack()
    {
    	ItemStack newCookStack = furnaceItemStacks[0];
		
    	if ( newCookStack == null )
    	{
        	newCookStack = furnaceItemStacks[2];
        	
        	if ( newCookStack == null )
        	{
        		newCookStack = furnaceItemStacks[1];
        	}
    	}
    	
    	if ( !ItemStack.areItemStacksEqual( newCookStack, m_cookStack ) )
    	{
            SetCookStack( newCookStack );
    	}
    }
    
    public void SetCookStack( ItemStack stack )
    {
    	if ( stack != null )
    	{
	    	m_cookStack = stack.copy();
    	}
    	else
    	{
    		m_cookStack = null;
    	}
    	
        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }
    
    public ItemStack GetCookStack()
    {
    	return m_cookStack;
    }
    
    public void GivePlayerCookStack( EntityPlayer player, int iFacing )
    {    	
		if ( !worldObj.isRemote )
		{
			// this is legacy support to clear all inventory items that may have been added through the GUI
			
			EjectAllNotCookStacksToFacing( player, iFacing );
		}
		
		FCUtilsItem.GivePlayerStackOrEjectFromTowardsFacing( player, m_cookStack, xCoord, yCoord, zCoord, iFacing );
		
		furnaceItemStacks[0] = null;
		furnaceItemStacks[1] = null;
		furnaceItemStacks[2] = null;
		
		SetCookStack( null );
    }
    
    private void EjectAllNotCookStacksToFacing( EntityPlayer player, int iFacing )
    {
    	if ( furnaceItemStacks[0] != null && !ItemStack.areItemStacksEqual( furnaceItemStacks[0], m_cookStack ) )
    	{
    		FCUtilsItem.EjectStackFromBlockTowardsFacing( worldObj, xCoord, yCoord, zCoord, furnaceItemStacks[0], iFacing );
    		
    		furnaceItemStacks[0] = null;
    	}
    	
    	if ( furnaceItemStacks[1] != null && !ItemStack.areItemStacksEqual( furnaceItemStacks[1], m_cookStack ) )
    	{
    		FCUtilsItem.EjectStackFromBlockTowardsFacing( worldObj, xCoord, yCoord, zCoord, furnaceItemStacks[1], iFacing );
    		
    		furnaceItemStacks[1] = null;
    	}
    	
    	if ( furnaceItemStacks[2] != null && !ItemStack.areItemStacksEqual( furnaceItemStacks[2], m_cookStack ) )
    	{
    		FCUtilsItem.EjectStackFromBlockTowardsFacing( worldObj, xCoord, yCoord, zCoord, furnaceItemStacks[2], iFacing );
    		
    		furnaceItemStacks[2] = null;
    	}		
		
		onInventoryChanged();
    }
    
    public void AddCookStack( ItemStack stack )
    {
    	furnaceItemStacks[0] = stack;
		
		onInventoryChanged();
    }
    
    public int AttemptToAddFuel( ItemStack stack )
    {
    	int iTotalBurnTime = m_iUnlitFuelBurnTime + furnaceBurnTime;
    	int iDeltaBurnTime = m_iMaxFuelBurnTime - iTotalBurnTime;
    	int iNumItemsBurned = 0;
    	
    	if ( iDeltaBurnTime > 0 )
    	{
    		iNumItemsBurned = iDeltaBurnTime / getItemBurnTime( stack );

    		if ( iNumItemsBurned == 0 && GetVisualFuelLevel() <= 2 )
    		{
    			// once the fuel level hits the bottom visual stage, you can jam anything in
    			
    			iNumItemsBurned = 1;
    		}
    		
    		if ( iNumItemsBurned > 0 )
    		{
    			if ( iNumItemsBurned > stack.stackSize )
    			{
    				iNumItemsBurned = stack.stackSize;
    			}
    			
        		m_iUnlitFuelBurnTime += getItemBurnTime( stack ) * iNumItemsBurned;
        		
        		onInventoryChanged();
    		}    		
    	}
    	
    	return iNumItemsBurned;
    }
    
    private void UpdateVisualFuelLevel()
    {
    	int iTotalBurnTime = m_iUnlitFuelBurnTime + furnaceBurnTime;
    	int iNewFuelLevel = 0;
    	
    	if ( iTotalBurnTime > 0 )
    	{
    		if ( iTotalBurnTime < m_iVisualSputterFuelLevel )
    		{
    			iNewFuelLevel = 1;
    		}
    		else
    		{
    			iNewFuelLevel = ( iTotalBurnTime / m_iVisualFuelLevelIncrement ) + 2;
    		}
    	}
    	
    	SetVisualFuelLevel( iNewFuelLevel );
    }
    
    public int GetVisualFuelLevel()
    {
    	return m_iVisualFuelLevel;
    }
    
    public void SetVisualFuelLevel( int iLevel )
    {
    	if ( m_iVisualFuelLevel != iLevel )
    	{
	    	m_iVisualFuelLevel = iLevel;
	    	
	        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    	}
    }    
}
