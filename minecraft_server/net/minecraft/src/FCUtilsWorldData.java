package net.minecraft.src;

public class FCUtilsWorldData implements FCAddOnUtilsWorldData {
	@Override
	public void saveWorldDataToNBT(WorldServer world, NBTTagCompound tag) {
		if ( world.GetMagneticPointList() != null )
	    {
	    	tag.setTag( "FCMagneticPoints", world.GetMagneticPointList().saveToNBT() );
	    }
	    
	    if ( world.GetLocalEnderChestInventory() != null )
	    {
	    	tag.setTag( "FCEnderItems", world.GetLocalEnderChestInventory().saveInventoryToNBT() );
	    }
	    
	    if ( world.GetLocalLowPowerEnderChestInventory() != null )
	    {
	    	tag.setTag( "FCLPEnderItems", world.GetLocalLowPowerEnderChestInventory().saveInventoryToNBT() );
	    }
	    
	    if ( world.GetLootingBeaconLocationList() != null )
	    {
	    	tag.setTag( "FCLootingBeacons", world.GetLootingBeaconLocationList().saveToNBT() );
	    }
	    
	    if ( world.GetSpawnLocationList() != null )
	    {
	    	tag.setTag( "FCSpawnLocations", world.GetSpawnLocationList().saveToNBT() );
	    }
	}

	@Override
	public void loadWorldDataFromNBT(WorldServer world, NBTTagCompound tag) {
		if ( tag.hasKey( "FCMagneticPoints" ) )
	    {
	        NBTTagList nbttaglist1 = tag.getTagList( "FCMagneticPoints" );
	        
	    	world.GetMagneticPointList().loadFromNBT( nbttaglist1 );
	    }
	    
	    if ( tag.hasKey( "FCEnderItems" ) )
	    {
	        NBTTagList itemList = tag.getTagList( "FCEnderItems" );
	        
	    	world.GetLocalEnderChestInventory().loadInventoryFromNBT( itemList );
	    }
	    
	    if ( tag.hasKey( "FCLPEnderItems" ) )
	    {
	        NBTTagList itemList = tag.getTagList( "FCLPEnderItems" );
	        
	    	world.GetLocalLowPowerEnderChestInventory().loadInventoryFromNBT( itemList );
	    }
	    
	    if ( tag.hasKey( "FCLootingBeacons" ) )
	    {
	        NBTTagList nbttaglist1 = tag.getTagList( "FCLootingBeacons" );
	        
	    	world.GetLootingBeaconLocationList().loadFromNBT( nbttaglist1 );
	    }
	    
	    if ( tag.hasKey( "FCSpawnLocations" ) )
	    {
	        NBTTagList nbttaglist1 = tag.getTagList( "FCSpawnLocations" );
	        
	    	world.GetSpawnLocationList().loadFromNBT( nbttaglist1 );
	    }
	}

	@Override
	public String getFilename() {
		return "FCWorld";
	}
}