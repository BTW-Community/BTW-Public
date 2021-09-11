// FCMOD

package net.minecraft.src;

public class FCItemBrick extends FCItemPlacesAsBlock
{
    public FCItemBrick( int iItemID )
    {
    	super( iItemID );
    	
    	setUnlocalizedName( "brick" );

        setCreativeTab( CreativeTabs.tabMaterials );    	
    }
    
    @Override
    public int getBlockID()
    {
        return FCBetterThanWolves.fcBlockCookedBrick.blockID;
    }

    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 8;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockBrickLoose.blockID;
    }
}
