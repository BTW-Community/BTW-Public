//FCMOD

package net.minecraft.src;

public class FCItemWindMillVertical extends Item
{
    public FCItemWindMillVertical( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();
        
        maxStackSize = 1;
        
        setUnlocalizedName( "fcItemWindMillVertical" );
    	
        setCreativeTab( CreativeTabs.tabRedstone );
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        int iTargetBlockID = world.getBlockId( i, j, k );
        
        if ( iTargetBlockID == FCBetterThanWolves.fcBlockAxle.blockID )
        {
        	int iAxisAlignment = ((FCBlockAxle)(FCBetterThanWolves.fcBlockAxle)).
        		GetAxisAlignment( world, i, j, k );
        	
        	if ( iAxisAlignment == 0 )
        	{
        		// offset the center of the Wind Mill in the direction the player is looking
        		
        		if ( player.rotationPitch <= 0F )
        		{
        			j += 3; // looking upwards
        		}
        		else
        		{
        			j -= 3;
        		}
        		
        		if ( !CheckForSupportingAxles( world, i, j, k ) )
        		{
                    if( world.isRemote )
                    {
                    	player.addChatMessage( "Too few vertical Axles in column to place Wind Mill here" );
                    }
        		}
        		else
        		{
        			FCEntityWindMillVertical windMill = new FCEntityWindMillVertical( world, 
                		(float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F );
    			
	        		if ( windMill.ValidateAreaAroundDevice() )
	        		{
	        			if ( windMill.IsClearOfBlockingEntities() )
	        			{
		                    if ( !world.isRemote )
		                    {
		            			windMill.setRotationSpeed( windMill.ComputeRotation() );
		                        
				                world.spawnEntityInWorld( windMill );
		                    }
			                
			                itemStack.stackSize--;
	        			}
	        			else
	        			{
	                        if( world.isRemote )
	                        {
	                        	player.addChatMessage( "Wind Mill placement is obstructed by something, or by you" );
	                        }
	        			}	        			
	        		}
	        		else
	        		{
	                    if( world.isRemote )
	                    {
	                    	player.addChatMessage( "Not enough room to place Wind Mill (They are friggin HUGE!)" );
	                    }
	        		}
        		}
            }
            
            return true;
        } 
        
        return false;
    }
    
    @Override
    public void OnUsedInCrafting( int iItemDamage, EntityPlayer player, ItemStack outputStack )
    {
    	if ( !player.worldObj.isRemote )
    	{
    		if ( outputStack.itemID == FCBetterThanWolves.fcItemWindMillBlade.itemID )
    		{
    			for (int i = 0; i < 7; i++) {
	    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
	    				new ItemStack( FCBetterThanWolves.fcItemWindMillBlade ) );
    			}
    		}
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean CheckForSupportingAxles( World world, int i, int j, int k )
    {
    	for ( int iTempJ = j - 3; iTempJ <= j + 3; iTempJ++ )
    	{    		
            int iTargetBlockID = world.getBlockId( i, iTempJ, k );
            
            if ( iTargetBlockID == FCBetterThanWolves.fcBlockAxle.blockID )
            {
            	int iAxisAlignment = ((FCBlockAxle)(FCBetterThanWolves.fcBlockAxle)).
            		GetAxisAlignment( world, i, iTempJ, k );
            	
            	if ( iAxisAlignment != 0 )
            	{
            		return false;
            	}
            }
            else
            {
            	return false;
            }
    	}
    	
    	return true;
    }
}