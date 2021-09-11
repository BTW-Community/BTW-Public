// FCMOD

package net.minecraft.src;

public class FCBlockGlass extends BlockGlass
{
    public FCBlockGlass( int iBlockID, Material material, boolean bRenderFacesNeighboringOnSameBlock )
    {
        super( iBlockID, material, bRenderFacesNeighboringOnSameBlock );
        
        SetPicksEffectiveOn( true );        
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return bIgnoreTransparency;
	}
    
    @Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}	
	
    @Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    @Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
}
