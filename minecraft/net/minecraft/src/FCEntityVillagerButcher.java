package net.minecraft.src;

public class FCEntityVillagerButcher extends FCEntityVillager {
	public FCEntityVillagerButcher(World world) {
		super(world, FCEntityVillager.professionIDButcher);
	}
	
	//CLIENT ONLY
	public String getTexture() {
		if (this.GetCurrentTradeLevel() >= 4)
            return "/btwmodtex/fcButcherLvl.png";
        return "/mob/villager/butcher.png";
	}
}