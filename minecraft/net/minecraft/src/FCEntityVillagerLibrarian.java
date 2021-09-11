package net.minecraft.src;

public class FCEntityVillagerLibrarian extends FCEntityVillager {
	public FCEntityVillagerLibrarian(World world) {
		super(world, FCEntityVillager.professionIDLibrarian);
	}
	
	//CLIENT ONLY
	public String getTexture() {
		if (this.GetCurrentTradeLevel() >= 5)
            return "/btwmodtex/fcLibrarianSpecs.png";
        return "/mob/villager/librarian.png";
	}
}