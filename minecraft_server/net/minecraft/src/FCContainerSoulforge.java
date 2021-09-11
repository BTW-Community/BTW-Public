// FCMOD

package net.minecraft.src;

public class FCContainerSoulforge extends Container
{
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    
    private World m_localWorld;
    
    private int m_anvilI;
    private int m_anvilJ;
    private int m_anvilK;
    
    private static final double m_dMaxInteractionDistance = 8D;
    private static final double m_dMaxInteractionDistanceSq = 
    	( m_dMaxInteractionDistance * m_dMaxInteractionDistance );
    
    private static final int m_iCraftingGridWidth = 4;
    private static final int m_iCraftingGridHeight = 4;
    private static final int m_iCraftingGridSize = ( m_iCraftingGridHeight * m_iCraftingGridWidth );
    
    private static final int m_iSlotScreenWidth = 18;
    private static final int m_iSlotScreenHeight = 18;
    
    private static final int m_iGridScreenPosX = 23;
    private static final int m_iGridScreenPosY = 17;
    
    private static final int m_iPlayerInventoryScreenPosX = 8;
    private static final int m_iPlayerInventoryScreenPosY = 102;
    
    private static final int m_iPlayerHotbarScreenPosY = ( m_iPlayerInventoryScreenPosY + 58 );
    
    private static final int m_iPlayerInventoryMinSlot = ( m_iCraftingGridSize + 1 ); 
    private static final int m_iPlayerInventoryMaxSlot = ( m_iPlayerInventoryMinSlot + 27 - 1 );
    
    private static final int m_iPlayerHotbarMinSlot = ( m_iPlayerInventoryMaxSlot + 1 );
    private static final int m_iPlayerHotbarMaxSlot = ( m_iPlayerHotbarMinSlot + 9 - 1 );
    
    /*
     * world, i, j, & k are only relevant on the server
     */
    public FCContainerSoulforge( InventoryPlayer inventoryplayer, World world, int i, int j, int k )
    {
        craftMatrix = new InventoryCrafting( this, m_iCraftingGridWidth, m_iCraftingGridHeight );
        craftResult = new InventoryCraftResult();
        
        m_localWorld = world;
        
        m_anvilI = i;
        m_anvilJ = j;
        m_anvilK = k;
        
        // add crafting output slot
        
        addSlotToContainer( new SlotCrafting( inventoryplayer.player, craftMatrix, craftResult, 0, 135, 44 ) );

        // add slots for the crafting grid
        
        for ( int tempSlotY = 0; tempSlotY < m_iCraftingGridHeight; tempSlotY++ )
        {
            for ( int tempSlotX = 0; tempSlotX < m_iCraftingGridWidth; tempSlotX++ )
            {
            	addSlotToContainer( new Slot( craftMatrix, tempSlotX + tempSlotY * m_iCraftingGridWidth, 
            		m_iGridScreenPosX + tempSlotX * m_iSlotScreenWidth, 
            		m_iGridScreenPosY + tempSlotY * m_iSlotScreenHeight ) );
            }

        }

        // add slots for the player inventory
        
        for ( int tempSlotY = 0; tempSlotY < 3; tempSlotY++ )
        {
            for ( int tempSlotX = 0; tempSlotX < 9; tempSlotX++ )
            {
            	addSlotToContainer( new Slot( inventoryplayer, 
            		tempSlotX + tempSlotY * 9 + 9, 
            		m_iPlayerInventoryScreenPosX + tempSlotX * m_iSlotScreenWidth, 
            		m_iPlayerInventoryScreenPosY + tempSlotY * m_iSlotScreenHeight ) );
            }
        }        

        // add slots for the player hot-bar
        
        for ( int tempSlotX = 0; tempSlotX < 9; tempSlotX++ )
        {
        	addSlotToContainer( new Slot( inventoryplayer, tempSlotX, 
        		m_iPlayerInventoryScreenPosX + tempSlotX * m_iSlotScreenWidth, 
        		m_iPlayerHotbarScreenPosY ) );
        }

        if ( world != null && !world.isRemote )
        {
	        // transfer existing Mould items to the crafting grid
        	// legacy for old moulds hanging around
        	
	        FCTileEntityAnvil tileEntityAnvil = (FCTileEntityAnvil)world.getBlockTileEntity( i, j, k );
	        
	        if ( tileEntityAnvil != null )
	        {
		        for ( int tempSlotY = 0; tempSlotY < m_iCraftingGridHeight; tempSlotY++ )
		        {
		            for ( int tempSlotX = 0; tempSlotX < m_iCraftingGridWidth; tempSlotX++ )
		            {
		            	if ( tileEntityAnvil.DoesSlotContainMould( tempSlotX, tempSlotY ) )
		            	{
		                    ItemStack mouldStack = new ItemStack( FCBetterThanWolves.fcItemMould );
		
		                    Slot slot = (Slot)inventorySlots.get( tempSlotX + tempSlotY * m_iCraftingGridWidth + 1 );
		                    
		                    slot.putStack( mouldStack );
		            	}
		            }
		        }
		        
		        tileEntityAnvil.ClearMouldContents();
	        }
        }
        
        onCraftMatrixChanged( craftMatrix );
    }

	@Override
    public void onCraftMatrixChanged(IInventory iinventory)
    {
    	ItemStack craftedStack = CraftingManager.getInstance().findMatchingRecipe( craftMatrix, m_localWorld );
    	
    	if ( craftedStack == null )
    	{
    		craftedStack = FCCraftingManagerAnvil.getInstance().findMatchingRecipe( craftMatrix, m_localWorld );
    	}
    	
        craftResult.setInventorySlotContents( 0, craftedStack );
    }

	@Override
    public void onCraftGuiClosed( EntityPlayer entityplayer )
    {
        super.onCraftGuiClosed( entityplayer );
        
        if ( m_localWorld != null && !m_localWorld.isRemote )
        {
	        for ( int i = 0; i < m_iCraftingGridSize; i++ )
	        {
	            ItemStack itemstack = craftMatrix.getStackInSlot( i );
	            
	            if ( itemstack != null )
	            {
	        		entityplayer.dropPlayerItem( itemstack );
	            }
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
		
        if ( m_localWorld.getBlockId( m_anvilI, m_anvilJ, m_anvilK ) != 
        	FCBetterThanWolves.fcAnvil.blockID )
        {
            return false;
        }
        
        return entityplayer.getDistanceSq( (double)m_anvilI + 0.5D, (double)m_anvilJ + 0.5D, 
    		(double)m_anvilK + 0.5D ) <= m_dMaxInteractionDistanceSq;
    }

	@Override
    public ItemStack transferStackInSlot( EntityPlayer player, int iSlotClicked )
    {
        ItemStack oldStackInSlotClicked = null;
        
        Slot slot = (Slot)inventorySlots.get( iSlotClicked );
        
        if ( slot != null && slot.getHasStack() )
        {
            ItemStack newStackInSlotClicked = slot.getStack();
            oldStackInSlotClicked = newStackInSlotClicked.copy();
            
            if ( iSlotClicked == 0 )
            {	
            	// crafting result slot
            	
                if ( !mergeItemStack( newStackInSlotClicked, 
                	m_iPlayerInventoryMinSlot, m_iPlayerHotbarMaxSlot + 1, true ) )
                {
                    return null;
                }
            } 
            else if ( iSlotClicked > m_iCraftingGridSize && iSlotClicked <= m_iCraftingGridSize + 27 + 9 )
            {
            	// player inventory & hot-bar
            	
            	if ( !mergeItemStack( newStackInSlotClicked, 1, m_iCraftingGridSize + 1, false ) )
            	{
            		return null;
            	}
            } 
            else
            {
            	// crafting grid
            	
                if ( !mergeItemStack( newStackInSlotClicked, 
                	m_iPlayerInventoryMinSlot, m_iPlayerHotbarMaxSlot + 1, true ) )
            	{
            		return null;
            	}
            }
            
            if(newStackInSlotClicked.stackSize == 0)
            {
                slot.putStack(null);
            } 
            else
            {
                slot.onSlotChanged();
            }
            
            if ( newStackInSlotClicked.stackSize != oldStackInSlotClicked.stackSize )
            {
                slot.onPickupFromSlot( player, newStackInSlotClicked);
            } 
            else
            {
                return null;
            }
        }
        
        return oldStackInSlotClicked;
    }
}