// FCMOD

package net.minecraft.src;

public interface FCIBlockFluidSource
{
	/*
	 * Returns the height level of the source (0 to 8) if a valid source for the fluid block, -1 otherwise
	 */
	public int IsSourceToFluidBlockAtFacing( World world, int i, int j, int k, int iFacing  );
}