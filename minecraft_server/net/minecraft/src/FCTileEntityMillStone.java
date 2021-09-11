//FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCTileEntityMillStone extends TileEntity
	implements IInventory
{
	public ItemStack m_stackMilling = null;
	
	public static final String m_sUnlocalizedName = "container.fcMillStone";
	
	private final double m_dMaxPlayerInteractionDistSq = 64D;
	
    private final int m_iTimeToGrind = 200;
    
    private boolean m_bValidateContentsOnUpdate;
    private boolean m_bContainsIngrediantsToGrind;

    public int m_iGrindCounter;        
    
	private final int m_iLegacyInventorySize = 3;	
    private ItemStack m_legacyInventory[] = null;
    
    public FCTileEntityMillStone()
    {
    	m_iGrindCounter = 0;
    	
    	m_bValidateContentsOnUpdate = true;
    }

    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        if ( tag.hasKey( "Items" ) )
        {
	        NBTTagList tagList = tag.getTagList( "Items" );
	        
	        m_legacyInventory = new ItemStack[m_iLegacyInventorySize];
	        
	        for ( int iTempTag = 0; iTempTag < tagList.tagCount(); iTempTag++ )
	        {
	            NBTTagCompound tempSlotTag = (NBTTagCompound)tagList.tagAt( iTempTag );
	            
	            int iTempSlot = tempSlotTag.getByte( "Slot" ) & 0xff;
	            
	            if ( iTempSlot >= 0 && iTempSlot < m_legacyInventory.length )
	            {
	            	m_legacyInventory[iTempSlot] = ItemStack.loadItemStackFromNBT( tempSlotTag );;
	            }
	        }
        }
        
        NBTTagCompound millingTag = tag.getCompoundTag( "stackMilling" );

        if ( millingTag != null )
        {
        	m_stackMilling = ItemStack.loadItemStackFromNBT( millingTag );
        }
        
        if ( tag.hasKey( "grindCounter" ) )
        {
        	m_iGrindCounter = tag.getInteger( "grindCounter" );
        }
    }

    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
        super.writeToNBT(tag);
        
        if ( !IsLegacyInventoryEmpty() )
        {
	        NBTTagList tagList = new NBTTagList();
	        
	        for ( int iTempSlot = 0; iTempSlot < m_legacyInventory.length; iTempSlot++ )
	        {
	            if ( m_legacyInventory[iTempSlot] != null )
	            {
	                NBTTagCompound tempSlotTag = new NBTTagCompound();
	                
	                tempSlotTag.setByte( "Slot", (byte)iTempSlot );
	                
	                m_legacyInventory[iTempSlot].writeToNBT( tempSlotTag );
	                
	                tagList.appendTag( tempSlotTag );
	            }
	        }     

	        tag.setTag( "Items", tagList );
        }
        
        if ( m_stackMilling != null)
        {
            NBTTagCompound millingTag = new NBTTagCompound();
            
            m_stackMilling.writeToNBT( millingTag );
            
            tag.setCompoundTag( "stackMilling", millingTag );
        }
        
        tag.setInteger( "grindCounter", m_iGrindCounter );        
    }

    @Override
    public void updateEntity()
    {
    	super.updateEntity();
    	
    	if ( !worldObj.isRemote )
    	{
			int iBlockID = worldObj.getBlockId( xCoord, yCoord, zCoord );
			
			FCBlockMillStone millStoneBlock = (FCBlockMillStone)Block.blocksList[iBlockID];
			
			if ( m_bValidateContentsOnUpdate )
			{
				ValidateContentsForGrinding( millStoneBlock );
			}
			
			if ( m_bContainsIngrediantsToGrind && millStoneBlock.GetIsMechanicalOn( worldObj, xCoord, yCoord, zCoord ) )
			{
				m_iGrindCounter++;
				
	    		if ( m_iGrindCounter >= m_iTimeToGrind )
	    		{
	    			GrindContents( millStoneBlock );
	    			
	    			m_iGrindCounter = 0;
	    			m_bValidateContentsOnUpdate = true;
	    		}
	    		
	    		CheckForNauseateNearbyPlayers( millStoneBlock );    		
	    	}
    	}		
    }
    
    //------------- IInventory implementation -------------//
    
    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot( int iSlot )
    {
    	if ( iSlot == 0 )
    	{
    		return m_stackMilling;
    	}
    	
        return null;
    }

    @Override
    public ItemStack decrStackSize( int iSlot, int iAmount )
    {
    	return FCUtilsInventory.DecrStackSize( this, iSlot, iAmount );    	
    }

    @Override
    public ItemStack getStackInSlotOnClosing( int iSlot )
    {
    	if ( iSlot == 0 && m_stackMilling != null )
    	{
            ItemStack itemstack = m_stackMilling;
            
            m_stackMilling = null;
            
            return itemstack;
        }
    	
        return null;
    }
    
    @Override
    public void setInventorySlotContents( int iSlot, ItemStack stack )
    {
    	if ( iSlot == 0 )
    	{
    		m_stackMilling = stack;
	    	
	        if( stack != null && stack.stackSize > getInventoryStackLimit() )
	        {
	            stack.stackSize = getInventoryStackLimit();
	        }
	        
	        onInventoryChanged();
    	}
    }

    @Override
    public String getInvName()
    {
        return m_sUnlocalizedName;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1; // only single stack items can fit in the millstone
    }

    @Override
    public void onInventoryChanged()
    {
    	super.onInventoryChanged();
    	
    	if ( worldObj != null && !worldObj.isRemote )
    	{
	    	if ( ContainsWholeCompanionCube() )
	    	{
	            worldObj.playSoundEffect( (float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, 
	        		"mob.wolf.whine", 
	        		0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
	    	}
	    	
	    	m_bValidateContentsOnUpdate = true;
    	}
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if ( worldObj.getBlockTileEntity( xCoord, yCoord, zCoord ) == this )
        {
            return ( entityplayer.getDistanceSq( (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D ) 
            	<= m_dMaxPlayerInteractionDistSq );
        }
        
        return false;
    }
    
    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isStackValidForSlot( int iSlot, ItemStack stack )
    {
        return true;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
    public void EjectStackOnMilled( ItemStack stack )
    {
    	int iFacing = 2 + worldObj.rand.nextInt( 4 ); // random direction to the sides
    	
    	Vec3 ejectPos = Vec3.createVectorHelper( worldObj.rand.nextDouble() * 1.25F - 0.125F, 
    		worldObj.rand.nextFloat() * ( 1F / 16F ) + ( 7F / 16F ), 
    		-0.2F );
    	
    	ejectPos.RotateAsBlockPosAroundJToFacing( iFacing );
    	
        EntityItem entity = new EntityItem( worldObj, xCoord + ejectPos.xCoord, 
        		yCoord + ejectPos.yCoord, zCoord + ejectPos.zCoord, stack );

    	Vec3 ejectVel = Vec3.createVectorHelper( worldObj.rand.nextGaussian() * 0.025D, 
    		worldObj.rand.nextGaussian() * 0.025D + 0.1F, 
    		-0.06D + worldObj.rand.nextGaussian() * 0.04D );
    	
    	ejectVel.RotateAsVectorAroundJToFacing( iFacing );
    	
        entity.motionX = ejectVel.xCoord;
        entity.motionY = ejectVel.yCoord;
        entity.motionZ = ejectVel.zCoord;
        
        entity.delayBeforeCanPickup = 10;
        
        worldObj.spawnEntityInWorld( entity );
    }
    
    public int getGrindProgressScaled( int iScale )
    {
        return ( m_iGrindCounter * iScale ) / m_iTimeToGrind;
    }    
    
    public boolean IsGrinding()
    {
    	return m_iGrindCounter > 0;
    }
    
    public boolean ContainsWholeCompanionCube()
    {    	
		return m_stackMilling != null && m_stackMilling.itemID == FCBetterThanWolves.fcCompanionCube.blockID &&
			m_stackMilling.getItemDamage() == 0;
    }
    
    private boolean GrindContents( FCBlockMillStone millStoneBlock )
    {
		if ( m_stackMilling != null && FCCraftingManagerMillStone.getInstance().HasRecipeForSingleIngredient( m_stackMilling ) )		
		{
			List<ItemStack> outputList = FCCraftingManagerMillStone.getInstance().GetCraftingResult( m_stackMilling );

			if ( outputList != null )
			{
				if ( m_stackMilling.itemID == FCBetterThanWolves.fcCompanionCube.blockID && m_stackMilling.getItemDamage() == 0 )
				{
	    	        worldObj.playAuxSFX( FCBetterThanWolves.m_iCompanionCubeDeathAuxFXID, xCoord, yCoord, zCoord, 0 );
				}
	            
	            // eject the product stacks
	            
	            for ( int listIndex = 0; listIndex < outputList.size(); listIndex++ )
	            {
		    		ItemStack groundStack = ((ItemStack)outputList.get( listIndex )).copy();
		    		
		    		if ( groundStack != null )
		    		{
		    			EjectStackOnMilled( groundStack );
		    		}
	            }
	            
	            m_stackMilling = null;
	            
	            return true;
			}
		}        	
    	
    	return false;
    }
    
    private void ValidateContentsForGrinding( FCBlockMillStone millStoneBlock )
    {
    	int iOldGrindingType = millStoneBlock.GetCurrentGrindingType( worldObj, xCoord, yCoord, zCoord );
    	int iNewGrindingType = FCBlockMillStone.m_iContentsNothing;
    	
    	MigrateLegacyInventory();
    	
		if ( m_stackMilling != null )		
		{
			if ( FCCraftingManagerMillStone.getInstance().HasRecipeForSingleIngredient( m_stackMilling ) )
			{			
				m_bContainsIngrediantsToGrind = true;
				
				int iItemIndex = m_stackMilling.getItem().itemID;
				
				if ( iItemIndex == FCBetterThanWolves.fcCompanionCube.blockID && m_stackMilling.getItemDamage() == 0 )
				{
					iNewGrindingType = FCBlockMillStone.m_iContentsCompanionCube;
				}
				else if ( iItemIndex == Block.netherrack.blockID )
				{
					iNewGrindingType = FCBlockMillStone.m_iContentsNetherrack;
				}
				else
				{
					iNewGrindingType = FCBlockMillStone.m_iContentsNormalGrinding;
				}
			}
			else
			{
				iNewGrindingType = FCBlockMillStone.m_iContentsJammed;
				
				m_iGrindCounter = 0;
				
				m_bContainsIngrediantsToGrind = false;
			}
		}
		else
		{
			m_iGrindCounter = 0;
			
			m_bContainsIngrediantsToGrind = false;
		}
		
		m_bValidateContentsOnUpdate = false;
		
    	if ( iOldGrindingType != iNewGrindingType )
    	{
    		millStoneBlock.SetCurrentGrindingType( worldObj, xCoord, yCoord, zCoord, iNewGrindingType );
    	}
    }
    
    private void CheckForNauseateNearbyPlayers( FCBlockMillStone block )
    {
    	int iGrindType = block.GetCurrentGrindingType( worldObj, xCoord, yCoord, zCoord );
		
    	if ( iGrindType == FCBlockMillStone.m_iContentsNetherrack &&
    		worldObj.getTotalWorldTime() % 40L == 0L )
    	{
    		ApplyPotionEffectToPlayersInRange( Potion.confusion.getId(), 120, 0, 10D );
    	}
    }
    
    private void ApplyPotionEffectToPlayersInRange( int iEffectID, int iDuration, int iEffectLevel, double dRange )
    {
        Iterator playerIterator = worldObj.playerEntities.iterator();
        
        while ( playerIterator.hasNext() )
        {
        	EntityPlayer player = (EntityPlayer)playerIterator.next();
        	
        	if ( !player.isDead && !player.capabilities.isCreativeMode && 
        		Math.abs( xCoord - player.posX ) <= dRange &&
        		Math.abs( yCoord - player.posY ) <= dRange &&
        		Math.abs( zCoord - player.posZ ) <= dRange )
        	{        	
                player.addPotionEffect( new PotionEffect( iEffectID, iDuration, iEffectLevel, true ) );                            
        	}
        }
    }
    
    private void MigrateLegacyInventory()
    {
    	if ( m_stackMilling == null && m_legacyInventory != null )
    	{
	        for ( int iTempSlot = 0; iTempSlot < m_legacyInventory.length; iTempSlot++ )
	        {
	            if ( m_legacyInventory[iTempSlot] != null )
	            {
            		ItemStack legacyStack = m_legacyInventory[iTempSlot];
            			
            		m_stackMilling = legacyStack.copy();
            		m_stackMilling.stackSize = 1;
            		
            		m_legacyInventory[iTempSlot].stackSize--;
            		
            		if ( legacyStack.stackSize <= 0 )
            		{
            			m_legacyInventory[iTempSlot] = null;
            			
            			if ( IsLegacyInventoryEmpty() )
            			{
            				m_legacyInventory = null;
            				
            				break;
            			}
            		}	            		
	            }
	        }
    	}
    }
    
    private boolean IsLegacyInventoryEmpty()
    {
    	if ( m_legacyInventory != null )
    	{
	        for ( int iTempSlot = 0; iTempSlot < m_legacyInventory.length; iTempSlot++ )
	        {
	        	if ( m_legacyInventory[iTempSlot] != null && m_legacyInventory[iTempSlot].stackSize > 0 )
	        	{
	        		return false;
	        	}
	        }
    	}
    	
    	return true;
    }
    
    public void EjectContents( int iFacing )
    {
    	if ( iFacing < 2 )
    	{
    		// always eject towards the sides
    		
    		iFacing = worldObj.rand.nextInt( 4 ) + 2;
    	}
        
    	if ( m_legacyInventory != null )
    	{
	        for ( int iTempSlot = 0; iTempSlot < m_legacyInventory.length; iTempSlot++ )
	        {
	        	if ( m_legacyInventory[iTempSlot] != null && m_legacyInventory[iTempSlot].stackSize > 0 )
	        	{
	        		FCUtilsItem.EjectStackFromBlockTowardsFacing( worldObj, xCoord, yCoord, zCoord, m_legacyInventory[iTempSlot], iFacing );
	        		
	        		m_legacyInventory[iTempSlot] = null;
	        	}
	        }
	        
	        m_legacyInventory = null;
    	}        
    	
    	if ( m_stackMilling != null )
    	{
    		FCUtilsItem.EjectStackFromBlockTowardsFacing( worldObj, xCoord, yCoord, zCoord, m_stackMilling, iFacing );
    		
    		m_stackMilling = null;
    		
	        onInventoryChanged();
    	}
    }
    
    public void AttemptToAddSingleItemFromStack( ItemStack stack )
    {
    	if ( m_stackMilling == null )
    	{
    		m_stackMilling = stack.copy();
    		
    		m_stackMilling.stackSize = 1;
    		
    		stack.stackSize--;
    		
	        onInventoryChanged();
    	}
    }
}