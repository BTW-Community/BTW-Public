package net.minecraft.src;

import net.minecraft.src.FCEntityVillager.TradeEffect;

public class FCEntityVillagerPriest extends FCEntityVillager {
	public FCEntityVillagerPriest(World world) {
		super(world, FCEntityVillager.professionIDPriest);
		
		registerEffectForLevelUp(FCEntityVillager.professionIDPriest, 5, new TradeEffect() {
				@Override
				public void playEffect(FCEntityVillager villager) {
					villager.worldObj.playSoundAtEntity(villager, "mob.enderdragon.growl", 1.0F, 0.5F);
					villager.worldObj.playSoundAtEntity(villager, "ambient.weather.thunder", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
					villager.worldObj.playSoundAtEntity(villager, "random.levelup", 0.75F + (rand.nextFloat() * 0.25F), 0.5F);
				}
			});
	}
	
	@Override
	public int getCurrentMaxNumTrades() {
		int tradeLevel = this.GetCurrentTradeLevel();
		
		if (tradeLevel >= 4) {
			return tradeLevel + 1;
		}
		else {
			return tradeLevel;
		}
	}
	
	@Override
	protected void spawnCustomParticles() {
		if (GetCurrentTradeLevel() >= 5) { // top level priest
			// enderman particles

			worldObj.spawnParticle("portal", 
					posX + (rand.nextDouble() - 0.5D) * width, 
					posY + rand.nextDouble() * height - 0.25D, 
					posZ + (rand.nextDouble() - 0.5D) * width, 
					(rand.nextDouble() - 0.5D) * 2D, 
					-rand.nextDouble(), 
					(rand.nextDouble() - 0.5D) * 2D);
		}
	}
}