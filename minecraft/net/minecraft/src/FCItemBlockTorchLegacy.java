// FCMOD

package net.minecraft.src;

public class FCItemBlockTorchLegacy extends FCItemBlockTorchBurning
{	
    public FCItemBlockTorchLegacy( int iItemID )
    {
        super( iItemID );
    }
    
    @Override
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	return FCBetterThanWolves.fcBlockTorchNetherBurning.blockID;
    }
}
