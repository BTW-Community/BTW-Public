// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockVanillaHopper extends BlockHopper
{
    public FCBlockVanillaHopper( int iBlockID )
    {
        super( iBlockID );
        
        setHardness( 3F );
        setResistance( 8F );
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "hopper" );        
        
        setCreativeTab( null );
    }

	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
        getCollisionBoundingBoxFromPool( world, i, j, k ).
        	AddToListIfIntersects( intersectingBox, list );
    }
    
    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
    	return par9;
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return null;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	return false;
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    }

    @Override
    public int getRenderType()
    {
        return 0;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return false;
    }

    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return 0;
    }

	//----------- Client Side Functionality -----------//
    
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon( "fcBlockStub" );
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return blockIcon;
    }
    
    @Override
    public String getItemIconName()
    {
        return null;
    }
}
