// FCMOD

package net.minecraft.src;

public class FCItemSnowball extends ItemSnowball
{
    public FCItemSnowball( int iItemID )
    {
        super( iItemID );
        
        SetBuoyant();
        SetIncineratedInCrucible();
        SetFilterableProperties( m_iFilterable_Small );
        
        setUnlocalizedName( "snowball" );
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
    	return FCBetterThanWolves.fcBlockSnowSolid.blockID;
    }
}
