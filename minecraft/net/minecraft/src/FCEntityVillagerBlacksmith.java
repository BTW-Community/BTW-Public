package net.minecraft.src;

public class FCEntityVillagerBlacksmith extends FCEntityVillager {
	public FCEntityVillagerBlacksmith(World world) {
		super(world, FCEntityVillager.professionIDBlacksmith);
	}
	
	//CLIENT ONLY
	public String getTexture() {
        return "/mob/villager/smith.png";
	}
}