// FCMOD

package net.minecraft.src;

public class FCBlockSandstoneMouldingAndDecorative extends FCBlockMouldingAndDecorative
{
	protected FCBlockSandstoneMouldingAndDecorative( int iBlockID, int iMatchingCornerBlockID )
	{
		super( iBlockID, Material.rock, "fcBlockDecorativeSandstone_top", "fcBlockColumnSandstone_side", iMatchingCornerBlockID, 
			0.8F, 1.34F, Block.soundStoneFootstep, "fcSandstoneMoulding" );
	}
	
}