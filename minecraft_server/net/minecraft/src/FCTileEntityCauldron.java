// FCMOD

package net.minecraft.src;

public class FCTileEntityCauldron extends FCTileEntityCookingVessel
{
    public FCTileEntityCauldron()
    {
    }

    @Override
    public void readFromNBT( NBTTagCompound nbttagcompound )
    {
        super.readFromNBT(nbttagcompound);
        
        if ( nbttagcompound.hasKey( "m_iCauldronCookCounter" ) )
        {
        	m_iCookCounter = nbttagcompound.getInteger( "m_iCauldronCookCounter" );
        }
        
        if ( nbttagcompound.hasKey( "m_iRenderCooldownCounter" ) )
        {
        	m_iStokedCooldownCounter = nbttagcompound.getInteger( "m_iRenderCooldownCounter" );
        }
        
        if ( nbttagcompound.hasKey( "m_bContainsValidIngrediantsForState" ) )
        {
        	m_bContainsValidIngrediantsForState = nbttagcompound.getBoolean( "m_bContainsValidIngrediantsForState" );
        }
    }

    @Override
    public void writeToNBT( NBTTagCompound nbttagcompound )
    {
        super.writeToNBT(nbttagcompound);
        
    	nbttagcompound.setInteger( "m_iCauldronCookCounter", m_iCookCounter );
    	nbttagcompound.setInteger( "m_iRenderCooldownCounter", m_iStokedCooldownCounter );    	
    }

    //************* IInventory ************//
    
    @Override
    public String getInvName()
    {
        return "Cauldron";
    }

    @Override
    public boolean isStackValidForSlot( int iSlot, ItemStack stack )
    {
        return true;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
    	return true;
    }
    
    //************* FCTileEntityCookingVessel ************//
    
    @Override
    public void ValidateContentsForState()
    {
    	// FCTODO: Move most of this into parent class
    	
		m_bContainsValidIngrediantsForState = false;
		
    	if ( m_iFireUnderType == 1 )
    	{
    		// regular fire

    		if ( FCCraftingManagerCauldron.getInstance().GetCraftingResult( this ) != null )
    		{
    			m_bContainsValidIngrediantsForState = true;
    		}
    		else if ( GetUncookedItemInventoryIndex() >= 0 )
    		{
    			m_bContainsValidIngrediantsForState = true;
    		}
    		else if ( FCUtilsInventory.GetFirstOccupiedStackOfItem( this, FCBetterThanWolves.fcItemDung.itemID ) >= 0 )    		
    		{
    			if ( ContainsNonFoulFood() )
    			{
        			m_bContainsValidIngrediantsForState = true;
    			}
    		}
    	}
    	else if ( m_iFireUnderType == 2 )
    	{
    		// stoked fire
    		
    		if ( DoesContainExplosives() )
    		{
				m_bContainsValidIngrediantsForState = true;
    		}
			else if ( FCCraftingManagerCauldronStoked.getInstance().GetCraftingResult( this ) != null ) 
			{
	    		m_bContainsValidIngrediantsForState = true;
			}
    	}
    }
    
    protected FCCraftingManagerBulk GetCraftingManager( int iFireType )
    {
    	if ( iFireType == 1 )
    	{
    		return FCCraftingManagerCauldron.getInstance();
    	}
    	else if ( iFireType == 2 )
    	{
    		return FCCraftingManagerCauldronStoked.getInstance();
    	}    	
    	
    	return null;
    }
    
    protected boolean AttemptToCookNormal()
    {
		int iDungIndex = FCUtilsInventory.GetFirstOccupiedStackOfItem( this, FCBetterThanWolves.fcItemDung.itemID );
    	
    	if ( iDungIndex >= 0 )
    	{
    		if ( TaintAllNonFoulFoodInInventory() )
    		{
    	        return true;
    		}
    	}
    	
    	if ( super.AttemptToCookNormal() )
    	{
    		return true;
    	}
    	
		return AttemptToCookFood();    	
    }
    
    //************* Class Specific Methods ************//
    
    private boolean AttemptToCookFood()
    {
    	int iUncookedFoodIndex = GetUncookedItemInventoryIndex();
    	
		if ( iUncookedFoodIndex >= 0 )
		{
        	ItemStack tempStack = FurnaceRecipes.smelting().getSmeltingResult(
	        		m_Contents[iUncookedFoodIndex].getItem().itemID );
        	
        	// we have to copy the furnace recipe stack so we don't end up with a pointer to the
        	// actual smelting result ItemStack in our inventory
        	ItemStack cookedStack = tempStack.copy();
	        
	        decrStackSize( iUncookedFoodIndex, 1 );
	        
	        if ( !FCUtilsInventory.AddItemStackToInventory( this, cookedStack ) )
	        {    	        	
	        	FCUtilsItem.EjectStackWithRandomOffset( worldObj, xCoord, yCoord + 1, zCoord, cookedStack );			    	        	
	        }
	        
	        return true;
		}
		
		return false;
    }
    
    public int GetUncookedItemInventoryIndex()
    {
    	for ( int tempIndex = 0;  tempIndex < m_iInventorySize; tempIndex++ )
    	{
    		if ( m_Contents[tempIndex] != null )
    		{
	    		Item tempItem = m_Contents[tempIndex].getItem();
	    		
	    		if ( tempItem != null )
	    		{
		    		if ( ( tempItem instanceof ItemFood ) &&
						FurnaceRecipes.smelting().getSmeltingResult( tempItem.itemID ) != null )
		    		{
		    			// this is raw food that has a cooked state
		    			
		    			return tempIndex;
		    		}
	    		}
    		}
    	}
    	
    	return -1;
    }
    
    private boolean ContainsNonFoulFood()
    {
    	for ( int tempIndex = 0;  tempIndex < m_iInventorySize; tempIndex++ )
    	{
    		if ( m_Contents[tempIndex] != null )
    		{
	    		Item tempItem = m_Contents[tempIndex].getItem();
	    		
	    		if ( tempItem != null )
	    		{
	    			int iTempItemID = tempItem.itemID;
	    			
		    		if( tempItem.itemID != FCBetterThanWolves.fcItemFoulFood.itemID &&
		    			iTempItemID != FCBetterThanWolves.fcItemMushroomBrown.itemID &&
		    			iTempItemID != FCBetterThanWolves.fcItemMushroomRed.itemID &&
		    			( tempItem instanceof ItemFood ) )
	    			{
		    			return true;
		    		}
	    		}
    		}
    	}
    	
    	return false;
    }
    
    private boolean TaintAllNonFoulFoodInInventory()
    {
    	boolean bFoodDestroyed = false;
    	
    	for ( int tempIndex = 0;  tempIndex < m_iInventorySize; tempIndex++ )
    	{
    		if ( m_Contents[tempIndex] != null )
    		{
	    		Item tempItem = m_Contents[tempIndex].getItem();
	    		
	    		if ( tempItem != null )
	    		{
	    			int iTempItemID = tempItem.itemID;
	    			
		    		if ( tempItem.itemID != FCBetterThanWolves.fcItemFoulFood.itemID && iTempItemID != FCBetterThanWolves.fcItemMushroomBrown.itemID &&
		    			iTempItemID != FCBetterThanWolves.fcItemMushroomRed.itemID && ( tempItem instanceof ItemFood ) )
		    		{
		    			int stackSize = m_Contents[tempIndex].stackSize;
		    			
		                m_Contents[tempIndex] = null;
		                
		                ItemStack spoiledStack = new ItemStack( FCBetterThanWolves.fcItemFoulFood, stackSize );
		                
		                setInventorySlotContents( tempIndex, spoiledStack );
		                
		                bFoodDestroyed = true;
		    		}
	    		}
    		}
    	}
    	
    	if ( bFoodDestroyed )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
}