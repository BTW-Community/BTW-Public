// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockDaylightDetector extends BlockDaylightDetector
{
    public FCBlockDaylightDetector(int par1)
    {
        super( par1 );
		
        setCreativeTab( null );
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return 0;
    }

    @Override
    public void updateLightLevel(World par1World, int par2, int par3, int par4)
    {
    }

    @Override
    public boolean canProvidePower()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World par1World)
    {
        return null;
    }

}
