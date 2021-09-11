// FCMOD

package net.minecraft.src;

public class FCItemNetherBrick extends Item
{
    public FCItemNetherBrick( int iItemID )
    {
        super( iItemID );
        
        setUnlocalizedName( "fcItemBrickNether" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
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
    	return FCBetterThanWolves.fcBlockNetherBrickLoose.blockID;
    }
}
