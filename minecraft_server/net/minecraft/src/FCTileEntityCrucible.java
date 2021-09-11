//FCMOD

package net.minecraft.src;

import java.util.List;

public class FCTileEntityCrucible extends FCTileEntityCookingVessel
{
    public FCTileEntityCrucible()
    {
    }

    @Override
    public void readFromNBT( NBTTagCompound nbttagcompound )
    {
        super.readFromNBT(nbttagcompound);
        
    	m_iCookCounter = nbttagcompound.getInteger( "m_iCrucibleCookCounter" );
    	
        if ( nbttagcompound.hasKey( "m_iStokedCooldownCounter" ) )
        {
        	m_iStokedCooldownCounter = nbttagcompound.getInteger( "m_iStokedCooldownCounter" );
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

    	nbttagcompound.setInteger( "m_iCrucibleCookCounter", m_iCookCounter );
    	nbttagcompound.setInteger( "m_iStokedCooldownCounter", m_iStokedCooldownCounter );    	
    }

    //************* IInventory ************//
    
    @Override
    public String getInvName()
    {
        return "Crucible";
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
		m_bContainsValidIngrediantsForState = false;
		
    	if ( m_iFireUnderType == 1 )
    	{
    		// regular fire

    		if ( FCCraftingManagerCrucible.getInstance().GetCraftingResult( this ) != null )
    		{
    			m_bContainsValidIngrediantsForState = true;
    		}
    	}
    	else if ( m_iFireUnderType == 2 )
    	{
    		// stoked fire
    		
    		if ( DoesContainExplosives() )
    		{
				m_bContainsValidIngrediantsForState = true;
    		}
    		else if ( FCCraftingManagerCrucibleStoked.getInstance().GetCraftingResult( this ) != null ) 
			{
	    		m_bContainsValidIngrediantsForState = true;
			}
    		else if ( GetFirstStackThatContainsItemsDestroyedByStokedFire() >= 0 )
    		{    			
	    		m_bContainsValidIngrediantsForState = true;
    		}
    	}
    }
    
    @Override
    protected FCCraftingManagerBulk GetCraftingManager( int iFireType )
    {
    	if ( iFireType == 1 )
    	{
    		return FCCraftingManagerCrucible.getInstance();
    	}
    	else if ( iFireType == 2 )
    	{
    		return FCCraftingManagerCrucibleStoked.getInstance();
    	}    	
    	
    	return null;
    }
    
    @Override
    protected boolean AttemptToCookStoked()
    {
		int iBurnableSlot = GetFirstStackThatContainsItemsDestroyedByStokedFire();
		
		if ( iBurnableSlot >= 0 )
		{
			decrStackSize( iBurnableSlot, 1 );
			
			return true;
		}
		
		return super.AttemptToCookStoked();
    }
    
    //------------- Class Specific Methods ------------//
    
    private int GetFirstStackThatContainsItemsDestroyedByStokedFire()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            if ( getStackInSlot( i ) != null )
            {
            	if ( getStackInSlot( i ).getItem().IsIncineratedInCrucible() )
            	{
            		return i;
            	}
            }
        }
        
    	return -1;
    }
}