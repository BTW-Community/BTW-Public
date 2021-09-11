package net.minecraft.src;

import java.util.Set;

import net.minecraft.src.FCEntityVillager.WeightedMerchantEntry;
import net.minecraft.src.FCEntityVillager.WeightedMerchantRecipeEntry;

public class FCEntityVillagerFarmer extends FCEntityVillager {
	public FCEntityVillagerFarmer(World world) {
		super(world, FCEntityVillager.professionIDFarmer);
	}

	@Override
	public int GetDirtyPeasant() {
		return dataWatcher.getWatchableObjectInt(m_iDirtyPeasantDataWatcherID);
	}

	@Override
	public void SetDirtyPeasant(int iDirtyPeasant) {
		dataWatcher.updateObject(m_iDirtyPeasantDataWatcherID, iDirtyPeasant);
	}
}