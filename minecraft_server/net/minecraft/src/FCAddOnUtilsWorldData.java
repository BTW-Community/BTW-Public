package net.minecraft.src;

public interface FCAddOnUtilsWorldData {
	public void saveWorldDataToNBT(WorldServer world, NBTTagCompound tag);
	
	public void loadWorldDataFromNBT(WorldServer world, NBTTagCompound tag);
	
	public String getFilename();
}
