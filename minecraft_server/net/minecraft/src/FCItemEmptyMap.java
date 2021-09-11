// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemEmptyMap extends ItemEmptyMap
{
    protected FCItemEmptyMap( int iItemID )
    {
    	super( iItemID );
    	
        setHasSubtypes( true );    	
    	SetBuoyant();
		SetFilterableProperties( m_iFilterable_Thin );
		
		setUnlocalizedName( "emptyMap" );
    }
    
    @Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	// modified version of parent method that sets the map scale based on the item damage
    	
        ItemStack newStack = new ItemStack( Item.map, 1, world.getUniqueDataId( "map" ) );
        
        String sMapName = "map_" + newStack.getItemDamage();
        MapData newMapData = new MapData( sMapName );
        world.setItemData( sMapName, newMapData );
        
        newMapData.scale = (byte)stack.getItemDamage();
        
        int var7 = 128 * (1 << newMapData.scale);
        
        newMapData.xCenter = (int)(Math.round( player.posX / (double)var7 ) * (long)var7 );
        newMapData.zCenter = (int)(Math.round( player.posZ / (double)var7 ) * (long)var7 );
        
        newMapData.dimension = (byte)world.provider.dimensionId;
        newMapData.markDirty();
        --stack.stackSize;

        if (stack.stackSize <= 0)
        {
            return newStack;
        }
        else
        {
            if (!player.inventory.addItemStackToInventory(newStack.copy()))
            {
                player.dropPlayerItem(newStack);
            }

            return stack;
        }
    }
    
	//----------- Client Side Functionality -----------//
}