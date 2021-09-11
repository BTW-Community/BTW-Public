//FCMOD

package net.minecraft.src;

public class FCBlockNetherBrickSidingAndCornerAndDecorative extends FCBlockSidingAndCornerAndDecorative
{
	protected FCBlockNetherBrickSidingAndCornerAndDecorative( int iBlockID )
	{
		super( iBlockID, FCBetterThanWolves.fcMaterialNetherRock, 
			"fcBlockDecorativeNetherBrick", 2.0F, 10F, Block.soundStoneFootstep, 
			"fcNetherBrickSiding" );
	}	
}