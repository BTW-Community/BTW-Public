package net.minecraft.src;

public class FCBlockComparator extends BlockComparator {
	public FCBlockComparator(int id, boolean powered) {
		super(id, powered);
		this.InitBlockBounds(0, 0, 0, 1, .125, 1);
	}
}