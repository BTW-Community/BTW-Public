// FCMOD

package net.minecraft.src;

public abstract class FCItemDoor extends Item
{
    public FCItemDoor( int iITemID )
    {
        super( iITemID );
        
        maxStackSize = 1;
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }

    @Override
    public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
        if ( iFacing == 1 )
        {
            ++j;

            if ( player.canPlayerEdit( i, j, k, iFacing, stack ) && player.canPlayerEdit( i, j + 1, k, iFacing, stack ) )
            {
                if ( GetDoorBlock().canPlaceBlockAt( world, i, j, k ) )
                {
                    int iDirection = MathHelper.floor_double( ( ( player.rotationYaw + 180.0F ) * 4.0F / 360.0F ) - 0.5D ) & 3;
                    
                    ItemDoor.placeDoorBlock( world, i, j, k, iDirection, GetDoorBlock() );
                    
                    --stack.stackSize;
                    
                    return true;
                }
            }
        }
        
        return false;
    }
	
    //------------- Class Specific Methods ------------//
    
    public abstract Block GetDoorBlock();
	
	//----------- Client Side Functionality -----------//
}
