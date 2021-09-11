// FCMOD

package net.minecraft.src;

public class FCItemSign extends ItemSign
{
    public FCItemSign( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();
        SetIncineratedInCrucible();
        
        setUnlocalizedName( "sign" );
    }

    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        if ( iFacing == 0 || itemStack.stackSize == 0 ||
        	( player != null && !player.canPlayerEdit( i, j, k, iFacing, itemStack ) ) ||
        	!world.getBlockMaterial( i, j, k ).isSolid() )
        {
            return false;
        }
        
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		targetPos.AddFacingAsOffset( iFacing );
		
        if ( Block.signPost.canPlaceBlockAt( world, targetPos.i, targetPos.j, targetPos.k ) )
        {
            if ( iFacing == 1 )
            {
                int iYaw = MathHelper.floor_double( ( ( player.rotationYaw + 180F ) * 16F / 360F ) + 0.5D ) & 15;
                
                world.setBlock( targetPos.i, targetPos.j, targetPos.k, Block.signPost.blockID, iYaw, 3 );
            }
            else
            {
            	world.setBlock( targetPos.i, targetPos.j, targetPos.k, Block.signWall.blockID, iFacing, 3 );
            }

            itemStack.stackSize--;
            
            TileEntitySign tileEnt = (TileEntitySign)world.getBlockTileEntity( targetPos.i, targetPos.j, targetPos.k );

            if ( tileEnt != null )
            {
                player.displayGUIEditSign( tileEnt );
            }

            return true;
        }
        
        return false;
    }
}
