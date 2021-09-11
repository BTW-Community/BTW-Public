// FCMOD

package net.minecraft.src;

public class FCItemBlockSnow extends ItemBlockWithMetadata
{
	// FCMOD: This class exists to remove the onItemUse override of ItemSnow so that snow blocks can't be combined/stacked.
	
    public FCItemBlockSnow( int iItemID, Block block )
    {
        super( iItemID, block );
    }    
}
