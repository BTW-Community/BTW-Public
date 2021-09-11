// FCMOD

package net.minecraft.src;

public class FCItemDye extends ItemDye
{
    public FCItemDye( int iItemID )
    {
        super( iItemID );
        
        SetBellowsBlowDistance( 2 );
        
        setUnlocalizedName( "dyePowder" );
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        if ( itemStack.getItemDamage() == 15 ) // bone meal
        {        	
        	if ( ApplyBoneMeal( world, i, j, k ) )
        	{
                itemStack.stackSize--;
                
                return true;
        	}
        }

        return false;
    }

    @Override
    // client
    public boolean itemInteractionForEntity( ItemStack stack, EntityLiving entity )
    // server
    //public boolean useItemOnEntity( ItemStack stack, EntityLiving entity )
    {
        if ( entity instanceof FCEntitySheep )
        {
            FCEntitySheep sheep = (FCEntitySheep)entity;
            int i = BlockCloth.getBlockFromDye(stack.getItemDamage());

            if (!sheep.getSheared() && sheep.getFleeceColor() != i)
            {
            	sheep.setSuperficialFleeceColor(i);
            	
                stack.stackSize--;
            }

            return true;
        }
        
        return false;
    }
    
    @Override
    public int GetFilterableProperties( ItemStack stack )
    {
    	if ( stack.getItemDamage() == 0 )
    	{
    		// ink sack
    		
    		return m_iFilterable_Small;    		
    	}
    	
		return m_iFilterable_Fine;    		
    }
    
    //------------- Class Specific Methods ------------//

    private boolean ApplyBoneMeal( World world, int i, int j, int k )
    {    	
        Block targetBlock = Block.blocksList[world.getBlockId( i, j, k )];
        
        if ( targetBlock != null && 
        	targetBlock.AttemptToApplyFertilizerTo( world, i, j, k ) )
        {
            return true;
        }
        
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
	
	private Icon m_cocoaPowderIcon;
	
    public void registerIcons( IconRegister par1IconRegister )
    {
    	super.registerIcons( par1IconRegister );
    	
    	m_cocoaPowderIcon = par1IconRegister.registerIcon( "fcItemCocoaPowder" );    	
    }
    
    @Override
    public Icon getIconFromDamage( int iDamage )
    {
    	if ( iDamage == 3 ) // cocoa powder
    	{
    		return m_cocoaPowderIcon;
    	}
    	else
    	{
    		return super.getIconFromDamage( iDamage );
    	}
    }    
}