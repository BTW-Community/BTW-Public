// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockStairs extends FCBlockStairsBase
{
    protected final Block m_referenceBlock;
    protected final int m_iReferenceBlockMetadata;
    
    protected FCBlockStairs( int iBlockID, Block referenceBlock, int iReferenceBlockMetadata )
    {
        super( iBlockID, referenceBlock.blockMaterial);
        
        m_referenceBlock = referenceBlock;
        m_iReferenceBlockMetadata = iReferenceBlockMetadata;
        
        setHardness( referenceBlock.blockHardness );
        setResistance( referenceBlock.blockResistance / 3.0F );
        
        setStepSound( referenceBlock.stepSound );        
    }

    @Override
    public void onBlockClicked( World world, int i, int j, int k, EntityPlayer player )
    {
        m_referenceBlock.onBlockClicked( world, i, j, k, player );
    }

    @Override
    public void onBlockDestroyedByPlayer( World world, int i, int j, int k, int iMetadata )
    {
        m_referenceBlock.onBlockDestroyedByPlayer( world, i, j, k, iMetadata );
    }

    @Override
    public float getExplosionResistance( Entity entity )
    {
        return m_referenceBlock.getExplosionResistance( entity );
    }

    @Override
    public int tickRate( World world )
    {
        return m_referenceBlock.tickRate( world );
    }

    @Override
    public void velocityToAddToEntity( World world, int i, int j, int k, Entity entity, Vec3 velocityVec )
    {
        m_referenceBlock.velocityToAddToEntity( world, i, j, k, entity, velocityVec );
    }

    @Override
    public boolean isCollidable()
    {
        return m_referenceBlock.isCollidable();
    }

    @Override
    public boolean canCollideCheck( int iMetadata, boolean flag )
    {
        return m_referenceBlock.canCollideCheck( iMetadata, flag );
    }

    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        return this.m_referenceBlock.canPlaceBlockAt(world, i, j, k);
    }

    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
    	super.onBlockAdded( world, i, j, k );
        
        m_referenceBlock.onBlockAdded( world, i, j, k );
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    	
        m_referenceBlock.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }

    @Override
    public void onEntityWalking( World world, int i, int j, int k, Entity entity )
    {
        m_referenceBlock.onEntityWalking( world, i, j, k, entity );
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        m_referenceBlock.updateTick( world, i, j, k, rand );
    }

    @Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        return m_referenceBlock.onBlockActivated( world, i, j, k, player, 0, 0.0F, 0.0F, 0.0F );
    }

    @Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
        m_referenceBlock.onBlockDestroyedByExplosion( world, i, j, k, explosion );
    }

    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        m_referenceBlock.randomDisplayTick( world, i, j, k, rand );
    }
    
    @Override
    public int getMixedBrightnessForBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
        return m_referenceBlock.getMixedBrightnessForBlock( blockAccess, i, j, k );
    }

    @Override
    public float getBlockBrightness( IBlockAccess blockAccess, int i, int j, int k )
    {
        return m_referenceBlock.getBlockBrightness( blockAccess, i, j, k );
    }
    
    @Override
    public int getRenderBlockPass()
    {
        return m_referenceBlock.getRenderBlockPass();
    }

    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return m_referenceBlock.getIcon( iSide, this.m_iReferenceBlockMetadata );
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
    {
        return m_referenceBlock.getSelectedBoundingBoxFromPool( world, i, j, k );
    }
    
    @Override
    public void registerIcons( IconRegister register ) 
    {    	
    }
}