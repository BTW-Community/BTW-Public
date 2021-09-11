// FCMOD

package net.minecraft.src;

public class FCBlockNetherBrickMouldingAndDecorative extends FCBlockMouldingAndDecorative
{
	protected FCBlockNetherBrickMouldingAndDecorative( int iBlockID, int iMatchingCornerBlockID )
	{
		super( iBlockID, FCBetterThanWolves.fcMaterialNetherRock, 
			"fcBlockDecorativeNetherBrick", "fcBlockColumnNetherBrick_side", 
			iMatchingCornerBlockID, 2.0F, 10F, Block.soundStoneFootstep, "fcNetherBrickMoulding" );		
	}
}