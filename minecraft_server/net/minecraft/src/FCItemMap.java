// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemMap extends ItemMap
{
    protected FCItemMap( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
		SetBellowsBlowDistance( 3 );
		SetFilterableProperties( m_iFilterable_Thin );
		
		setUnlocalizedName( "map" );
    }
    
    @Override
    public void updateMapData( World world, Entity entity, MapData mapData )
    {
        if ( world.provider.dimensionId == mapData.dimension && entity instanceof EntityPlayer )
        {
        	// Note: This function only controls adding new data to the map.  It has nothing to do with existing data, and thus is not called if
        	// the map is on a frame.
        	
            if ( mapData.IsEntityLocationVisibleOnMap( entity ) )
            {
            	super.updateMapData( world, entity, mapData );
            }
        }
    }
}