//FCMOD

package net.minecraft.src;

public class FCItemWaterWheel extends Item
{
    public FCItemWaterWheel( int iItemID )
    {
        super( iItemID );
        
        maxStackSize = 1;
        
        SetBuoyant();
        
        setUnlocalizedName( "fcItemWaterWheel" );        
		
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

            	FCEntityWaterWheel waterWheel = new FCEntityWaterWheel( world, 
            		(float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, bIAligned );

        		if ( waterWheel.ValidateAreaAroundDevice() )
        		{
        			if ( waterWheel.IsClearOfBlockingEntities() )
        			{
	                    if( !world.isRemote )
	                    {
	                        waterWheel.setRotationSpeed( waterWheel.ComputeRotation() );
	
			                world.spawnEntityInWorld( waterWheel );
	                    }
		                
		                itemStack.stackSize--;
		                
		                return true;
        			}
        			else
        			{
                        if( world.isRemote )
                        {
                        	player.addChatMessage( "Water Wheel placement is obstructed by something, or by you" );
                        }
        			}
        		}
        		else
        		{
                    if( world.isRemote )
                    {
                    	player.addChatMessage( "Not enough room to place Water Wheel" );
                    }
        		}
        	}
        }        
        
        return false;
    }    
}
    
