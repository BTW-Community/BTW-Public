// FCMOD

package net.minecraft.src;

public class FCItemWindMill extends Item
{
    public FCItemWindMill( int iItemID )
    {
        super( iItemID );
        
        maxStackSize = 1;
        
        SetBuoyant();
        
        setUnlocalizedName( "fcItemWindMill" );
        
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
        	
        	if ( iAxisAlignment != 0 )
        	{
        		boolean bIAligned = false;
        		
        		if ( iAxisAlignment == 2 )
        		{
	            	bIAligned = true;
        		}

    			FCEntityWindMill windMill = new FCEntityWindMill( world, 
            		(float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, bIAligned );
			
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
    			for (int i = 0; i < 3; i++) {
	    			FCUtilsItem.EjectStackWithRandomVelocity( player.worldObj, player.posX, player.posY, player.posZ, 
	    				new ItemStack( FCBetterThanWolves.fcItemWindMillBlade ) );
    			}
    		}
    	}
    }
} 
