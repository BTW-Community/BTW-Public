// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.Random;

public class FCContainerInfernalEnchanter extends Container
{
    public IInventory m_tableInventory;
    
    private World m_localWorld;
    
    private int m_iBlockI;
    private int m_iBlockJ;
    private int m_iBlockK;
    
    private static final double m_dMaxInteractionDistance = 8.0D;
    private static final double m_dMaxInteractionDistanceSq = ( m_dMaxInteractionDistance * m_dMaxInteractionDistance );
    
    private static final int m_iSlotScreenWidth = 18;
    private static final int m_iSlotScreenHeight = 18;
    
    private static final int m_iScrollSlotScreenPosX = 17;
    private static final int m_iScrollSlotScreenPosY = 37;
    
    private static final int m_iItemSlotScreenPosX = 17;
    private static final int m_iItemSlotScreenPosY = 75;
    
    private static final int m_iPlayerInventoryScreenPosX = 8;
    private static final int m_iPlayerInventoryScreenPosY = 129;
    
    private static final int m_iPlayerHotbarScreenPosY = ( m_iPlayerInventoryScreenPosY + 58 );
    
	private static final int m_iHorizontalBookShelfCheckDistance = 8;
	private static final int m_iVerticalPositiveBookShelfCheckDistance = 8;
	private static final int m_iVerticalNegativeBookShelfCheckDistance = 8;
	
    public static final int m_iMaxEnchantmentPowerLevel = 5;
    
    public int m_CurrentEnchantmentLevels[];
    
    public int m_iMaxSurroundingBookshelfLevel;
    public int m_iLastMaxSurroundingBookshelfLevel;
    
    public long m_lNameSeed;
    
    Random rand;

    /*
     * world, i, j, & k are only relevant on the server
     */
    public FCContainerInfernalEnchanter( InventoryPlayer playerInventory, World world, int i, int j, int k )
    {
        m_localWorld = world;
        
        m_iBlockI = i;
        m_iBlockJ = j;
        m_iBlockK = k;
        
        rand = new Random();
        
        m_lNameSeed = rand.nextLong();
        
        m_tableInventory = new FCInventoryInfernalEnchanter( this, "fcInfernalEnchanterInv", 2 );
        
        m_CurrentEnchantmentLevels = new int[m_iMaxEnchantmentPowerLevel];
        
        ResetEnchantingLevels();      
        
        m_iMaxSurroundingBookshelfLevel = 0;
        m_iLastMaxSurroundingBookshelfLevel = 0;        
        
        // add scroll slot
        
        addSlotToContainer( new Slot( m_tableInventory, 0, m_iScrollSlotScreenPosX, m_iScrollSlotScreenPosY ) );

        addSlotToContainer( new Slot( m_tableInventory, 1, m_iItemSlotScreenPosX, m_iItemSlotScreenPosY ) );
        
        // add item slot
        
        // add slots for the player inventory
        
        for ( int tempSlotY = 0; tempSlotY < 3; tempSlotY++ )
        {
            for ( int tempSlotX = 0; tempSlotX < 9; tempSlotX++ )
            {
            	addSlotToContainer( new Slot( playerInventory, 
            		tempSlotX + tempSlotY * 9 + 9, 
            		m_iPlayerInventoryScreenPosX + tempSlotX * m_iSlotScreenWidth, 
            		m_iPlayerInventoryScreenPosY + tempSlotY * m_iSlotScreenHeight ) );
            }
        }

        // add slots for the player hot-bar
        
        for ( int tempSlotX = 0; tempSlotX < 9; tempSlotX++ )
        {
        	addSlotToContainer( new Slot( playerInventory, tempSlotX, 
        		m_iPlayerInventoryScreenPosX + tempSlotX * m_iSlotScreenWidth, 
        		m_iPlayerHotbarScreenPosY ) );
        }
        
        if ( world != null && !world.isRemote )
        {
        	CheckForSurroundingBookshelves();
        }
    }

	@Override
    public void onCraftMatrixChanged( IInventory inventory )
    {
        if ( inventory == m_tableInventory )
        {
            m_lNameSeed = rand.nextLong();
	
    		ResetEnchantingLevels();
    		
        	ComputeCurrentEnchantmentLevels();
        	
        	if ( m_localWorld != null && !m_localWorld.isRemote )
        	{	        	
	            detectAndSendChanges();
        	}
        }
    }

	@Override
    public void onCraftGuiClosed( EntityPlayer player )
    {
        super.onCraftGuiClosed( player );
        
        if ( m_localWorld == null || m_localWorld.isRemote )
        {
            return;
        }
        
        for ( int i = 0; i < m_tableInventory.getSizeInventory(); i++ )
        {
            ItemStack itemstack = m_tableInventory.getStackInSlot(i);
            
            if ( itemstack != null )
            {
                player.dropPlayerItem( itemstack );
            }
        }
    }
	
	@Override
    public boolean canInteractWith( EntityPlayer entityplayer )
    {
		if ( m_localWorld == null || m_localWorld.isRemote )
		{
			return true;
		}
		
        if ( m_localWorld.getBlockId( m_iBlockI, m_iBlockJ, m_iBlockK ) != 
        	FCBetterThanWolves.fcInfernalEnchanter.blockID )
        {
            return false;
        }
        
        return entityplayer.getDistanceSq( (double)m_iBlockI + 0.5D, (double)m_iBlockJ + 0.5D, 
    		(double)m_iBlockK + 0.5D ) <= m_dMaxInteractionDistanceSq;
    }

	@Override
    public ItemStack transferStackInSlot( EntityPlayer player, int iSlotIndex )
    {
        ItemStack clickedStack = null;
        
        Slot slot = (Slot)inventorySlots.get( iSlotIndex );

        if ( slot != null && slot.getHasStack() )
        {
            ItemStack processedStack = slot.getStack();
            clickedStack = processedStack.copy();

            if ( iSlotIndex <= 1 )
            {
            	// scroll or item stack: transfer to player inv
            	
                if ( !mergeItemStack( processedStack, 2, 38, true ) )
                {
                    return null;
                }
            }
            else if ( processedStack.getItem().itemID == FCBetterThanWolves.fcItemArcaneScroll.itemID )
            {
            	// scroll item in player inv
            	
                if ( !mergeItemStack( processedStack, 0, 1, false ) )
                {
                    return null;
                }
            }
            else if ( GetMaximumEnchantmentCost( processedStack ) > 0 )
            {
            	// enchantable item 
            	
                if ( !mergeItemStack( processedStack, 1, 2, false ) )
                {
                    return null;
                }
            }
            else if ( iSlotIndex >= 2 && iSlotIndex < 29 )
            {
                if ( !mergeItemStack( processedStack, 29, 38, false ) )
                {
                    return null;
                }
            }
            else if ( iSlotIndex >= 29 && iSlotIndex < 38 && !mergeItemStack( processedStack, 2, 29, false ) )
            {
                return null;
            }

            if ( processedStack.stackSize == 0 )
            {
                slot.putStack( null );
            }
            else
            {
                slot.onSlotChanged();
            }

            if ( processedStack.stackSize != clickedStack.stackSize )
            {
                slot.onPickupFromSlot( player, processedStack );

            	if ( m_localWorld != null && !m_localWorld.isRemote )
                {
                	detectAndSendChanges();
                }

            }
            else
            {
                return null;
            }
        }

        return clickedStack;
    }
	
	private void CheckForSurroundingBookshelves()
	{
		int iBookshelfCount = 0;
		
		for ( int iTempI = m_iBlockI -  m_iHorizontalBookShelfCheckDistance; iTempI <= m_iBlockI + m_iHorizontalBookShelfCheckDistance; iTempI++ )
		{
			for ( int iTempJ = m_iBlockJ -  m_iVerticalNegativeBookShelfCheckDistance; iTempJ <= m_iBlockJ + m_iVerticalPositiveBookShelfCheckDistance; iTempJ++ )
			{
				for ( int iTempK = m_iBlockK -  m_iHorizontalBookShelfCheckDistance; iTempK <= m_iBlockK + m_iHorizontalBookShelfCheckDistance; iTempK++ )
				{
					if ( IsValidBookshelf( iTempI, iTempJ, iTempK ) )
					{
						iBookshelfCount++;
					}
				}
			}
		}
		
		m_iMaxSurroundingBookshelfLevel = iBookshelfCount; 
	}
	
	private boolean IsValidBookshelf( int i, int j, int k )
	{
		int iBlockID = m_localWorld.getBlockId( i, j, k );
		
		if ( iBlockID == Block.bookShelf.blockID )
		{
			// check around the bookshelf for an empty block to provide access
			
			if ( m_localWorld.isAirBlock( i + 1, j, k ) ||
				m_localWorld.isAirBlock( i - 1, j, k ) ||
				m_localWorld.isAirBlock( i, j, k + 1 ) ||
				m_localWorld.isAirBlock( i, j, k - 1 ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void SetCurrentEnchantingLevels( int iMaxPowerLevel, int iCostMultiplier, int iMaxBaseCostForItem )
	{
		ResetEnchantingLevels();
		
		if ( iMaxPowerLevel == 1 )
		{
			m_CurrentEnchantmentLevels[0] = 30;
		}
		else if ( iMaxPowerLevel == 2 )
		{
			m_CurrentEnchantmentLevels[0] = 15;
			m_CurrentEnchantmentLevels[1] = 30;
		}
		else if ( iMaxPowerLevel == 3 )
		{
			m_CurrentEnchantmentLevels[0] = 10;
			m_CurrentEnchantmentLevels[1] = 20;
			m_CurrentEnchantmentLevels[2] = 30;
		}
		else if ( iMaxPowerLevel == 4 )
		{
			m_CurrentEnchantmentLevels[0] = 8;
			m_CurrentEnchantmentLevels[1] = 15;
			m_CurrentEnchantmentLevels[2] = 23;
			m_CurrentEnchantmentLevels[3] = 30;
		}
		else if ( iMaxPowerLevel == 5 )
		{
			m_CurrentEnchantmentLevels[0] = 6;
			m_CurrentEnchantmentLevels[1] = 12;
			m_CurrentEnchantmentLevels[2] = 18;
			m_CurrentEnchantmentLevels[3] = 24;
			m_CurrentEnchantmentLevels[4] = 30;
		}
		
		int iCostIncrement = ( ( iCostMultiplier - 1 ) * 30 );
		
        for ( int iTemp = 0; iTemp < m_iMaxEnchantmentPowerLevel; iTemp++ )
        {
        	if ( m_CurrentEnchantmentLevels[iTemp] > 0 )
        	{
        		if ( iMaxBaseCostForItem < m_CurrentEnchantmentLevels[iTemp] )
        		{
        			m_CurrentEnchantmentLevels[iTemp] = 0;
        		}
        		else
        		{
        			m_CurrentEnchantmentLevels[iTemp] += iCostIncrement;
        		}
        	}
        }        
	}
	
	private void ResetEnchantingLevels()
	{
		// clear the current levels
		
        for ( int iTemp = 0; iTemp < m_iMaxEnchantmentPowerLevel; iTemp++ )
        {
        	m_CurrentEnchantmentLevels[iTemp] = 0;
        }        
	}
	
	private void ComputeCurrentEnchantmentLevels()
	{
		ItemStack scrollStack = m_tableInventory.getStackInSlot( 0 );
		
		if ( scrollStack != null && scrollStack.getItem().itemID == FCBetterThanWolves.fcItemArcaneScroll.itemID )
		{
			ItemStack itemToEnchantStack = m_tableInventory.getStackInSlot( 1 );
			
			if ( itemToEnchantStack != null )
			{
				int iMaxEnchantmentCost = GetMaximumEnchantmentCost ( itemToEnchantStack );
				
				if ( iMaxEnchantmentCost > 0 )
				{
					int iEnchantmentIndex = scrollStack.getItemDamage();
					
					if ( iEnchantmentIndex >= Enchantment.enchantmentsList.length || Enchantment.enchantmentsList[iEnchantmentIndex] == null )
					{
						return;
					}
					
					if ( IsEnchantmentAppropriateForItem( iEnchantmentIndex, itemToEnchantStack ) )
					{
						if ( !DoesEnchantmentConflictWithExistingOnes( iEnchantmentIndex, itemToEnchantStack ) )
						{
							int iMaxNumberOfItemEnchants = GetMaximumNumberOfEnchantments( itemToEnchantStack ); 
							int iCurrentNumberOfItemEnchants = 0;
							
					        NBTTagList enchantmentTagList = itemToEnchantStack.getEnchantmentTagList();
					        
					        if ( enchantmentTagList != null )
					        {
					        	iCurrentNumberOfItemEnchants = itemToEnchantStack.getEnchantmentTagList().tagCount();			        	
					        }
					        
				        	if ( iCurrentNumberOfItemEnchants < iMaxNumberOfItemEnchants )
				        	{
				        		// the item may be enchanted
				        		
				        		SetCurrentEnchantingLevels( GetMaxEnchantmentPowerLevel( iEnchantmentIndex, itemToEnchantStack ), 
			        				iCurrentNumberOfItemEnchants + 1, GetMaximumEnchantmentCost( itemToEnchantStack ) );  
				        	}
						}
					}
				}
			}			
		}
	}
	
	private int GetMaximumEnchantmentCost( ItemStack itemStack )
	{
		return itemStack.getItem().GetInfernalMaxEnchantmentCost();		
	}
	
	private int GetMaximumNumberOfEnchantments( ItemStack itemStack )
	{
		return itemStack.getItem().GetInfernalMaxNumEnchants();		
	}
	
	private boolean IsEnchantmentAppropriateForItem( int iEnchantmentIndex, ItemStack itemStack )
	{
		// client
		//return Enchantment.enchantmentsList[iEnchantmentIndex].canApply( itemStack );
		// server
		return Enchantment.enchantmentsList[iEnchantmentIndex].func_92089_a( itemStack );		
	}
	
	private boolean DoesEnchantmentConflictWithExistingOnes( int iEnchantmentIndex, ItemStack itemStack )
	{
        NBTTagList enchantmentTagList = itemStack.getEnchantmentTagList();
        
        if ( enchantmentTagList != null )
        {
        	int iCurrentNumberOfItemEnchants = itemStack.getEnchantmentTagList().tagCount();
        	
            for (int iTemp = 0; iTemp < iCurrentNumberOfItemEnchants; iTemp++)
            {
                int iTempEnchantmentIndex = ((NBTTagCompound)enchantmentTagList.tagAt(iTemp)).getShort("id");

                if ( iTempEnchantmentIndex == iEnchantmentIndex )
                {
                	// enchantments always conflict with themselves
                	
                    return true;
                }
                else if ( ( iEnchantmentIndex == Enchantment.silkTouch.effectId && iTempEnchantmentIndex == Enchantment.fortune.effectId ) || 
            		( iEnchantmentIndex == Enchantment.fortune.effectId && iTempEnchantmentIndex == Enchantment.silkTouch.effectId ) )
                {
                	return true;
                }
            }
        }
        
		return false;
	}
	
	private int GetMaxEnchantmentPowerLevel( int iEnchantmentIndex, ItemStack itemStack )
	{
        if ( iEnchantmentIndex == Enchantment.respiration.effectId && itemStack.getItem().itemID == FCBetterThanWolves.fcItemPlateHelm.itemID )
        {
        	return 5;
        }
        
        return Enchantment.enchantmentsList[iEnchantmentIndex].getMaxLevel();
	}
	
	/*
	 * Return true if the item was successfully enchanted
	 */
	@Override
	public boolean enchantItem( EntityPlayer player, int iButtonIndex )
	{
		if ( m_localWorld == null || m_localWorld.isRemote )
		{
			return true;
		}
		
    	int iButtonEnchantmentLevel = m_CurrentEnchantmentLevels[iButtonIndex];
    	
    	if ( iButtonEnchantmentLevel > 0 )
    	{    	
    		boolean bPlayerCapable = iButtonEnchantmentLevel <= player.experienceLevel && iButtonEnchantmentLevel <= m_iMaxSurroundingBookshelfLevel;
    		
    		if ( bPlayerCapable )
    		{
    			ItemStack scrollStack = m_tableInventory.getStackInSlot( 0 );
    			
    			if ( scrollStack != null && scrollStack.getItem().itemID == FCBetterThanWolves.fcItemArcaneScroll.itemID )
    			{
    				ItemStack itemToEnchantStack = m_tableInventory.getStackInSlot( 1 );
    				
    				if ( itemToEnchantStack != null )
    				{
    					int iEnchantmentIndex = scrollStack.getItemDamage();
    					
    					if ( iEnchantmentIndex >= Enchantment.enchantmentsList.length || Enchantment.enchantmentsList[iEnchantmentIndex] == null )
    					{
    						return false;
    					}
    					
    					itemToEnchantStack.addEnchantment( Enchantment.enchantmentsList[iEnchantmentIndex], iButtonIndex + 1 );
    					
                        player.addExperienceLevel( -iButtonEnchantmentLevel );
                        
                		m_tableInventory.decrStackSize( 0, 1 );
    					
                        onCraftMatrixChanged( m_tableInventory );
                        
                        m_localWorld.playSoundAtEntity( player, "ambient.weather.thunder", 1.0F, m_localWorld.rand.nextFloat() * 0.4F + 0.8F );

                        return true;
					}			
    			}
    		}
    	}
    	
		return false;
	}
	
	@Override
    //public void addCraftingToCrafters( ICrafting craftingInterface )
    public void onCraftGuiOpened( ICrafting craftingInterface )
    {
        //super.addCraftingToCrafters( craftingInterface );
        super.onCraftGuiOpened( craftingInterface );
        
        craftingInterface.sendProgressBarUpdate( this, 0, m_iMaxSurroundingBookshelfLevel );
    }
	
	@Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        Iterator iterator = crafters.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            ICrafting icrafting = (ICrafting)iterator.next();

            if ( m_iLastMaxSurroundingBookshelfLevel != m_iMaxSurroundingBookshelfLevel )
            {
                icrafting.sendProgressBarUpdate( this, 0, m_iMaxSurroundingBookshelfLevel );
            }
        }
        while (true);

        m_iLastMaxSurroundingBookshelfLevel = m_iMaxSurroundingBookshelfLevel;
    }
}

