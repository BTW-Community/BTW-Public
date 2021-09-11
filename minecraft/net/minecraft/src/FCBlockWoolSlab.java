// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockWoolSlab extends FCBlockSlab
{
	private boolean m_bIsUpsideDown;
	
    public final static int m_iNumSubtypes = 16;    
    
    public FCBlockWoolSlab( int iBlockID, boolean bIsUpsideDown )
    {
        super( iBlockID, Material.cloth );

        setHardness( 0.8F );
        
        SetBuoyancy( 1F );
        
        m_bIsUpsideDown = bIsUpsideDown;        

        if ( !bIsUpsideDown )
        {        	
            InitBlockBounds( 0F, 0F, 0F, 1F, 0.5F, 1F );
        }
        else
        {
        	InitBlockBounds( 0F, 0.5F, 0F, 1F, 1F, 1F );
        }
        
        setStepSound( soundClothFootstep );
        
        setUnlocalizedName( "fcBlockWoolSlab" );

		setCreativeTab( CreativeTabs.tabBlock );		
    }

	@Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
		// override so that a normal slab is dropped regardless of whether this one is upside down or not
		
        return FCBetterThanWolves.fcWoolSlab.blockID;
    }
	
	@Override
    public int damageDropped( int i )
    {
        return i;
    }
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
    protected boolean canSilkHarvest()
    {
		// to prevent silk touch from overriding and potential harvesting an upside down slab
		
        return false;
    }    
    
    //------------- FCBlockSlab ------------//
    
	@Override
    public boolean GetIsUpsideDown( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return m_bIsUpsideDown;
    }
    
	@Override
    public boolean GetIsUpsideDown( int iMetadata )
    {
    	return m_bIsUpsideDown;
    }
    
	@Override
    public void SetIsUpsideDown( World world, int i, int j, int k, boolean bUpsideDown )
    {
		if ( m_bIsUpsideDown != bUpsideDown )
		{
			int iNewBlockID = FCBetterThanWolves.fcBlockWoolSlabTop.blockID;
			int iMetadata = world.getBlockMetadata( i, j, k );
			
			if ( blockID == FCBetterThanWolves.fcBlockWoolSlabTop.blockID )
			{
				iNewBlockID = FCBetterThanWolves.fcWoolSlab.blockID;
			}
			
			world.setBlockAndMetadataWithNotify( i, j, k, iNewBlockID, iMetadata );
		}
    }
	
	@Override
    public int SetIsUpsideDown( int iMetadata, boolean bUpsideDown )
	{
		// this can't be done given the 2 different block types used here
		
		return iMetadata;
	}
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		return Block.cloth.blockID;
	}
	
	@Override
	public int GetCombinedMetadata( int iMetadata )
	{
		return iMetadata;
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconByColorArray;

	@Override
    public void registerIcons( IconRegister register )
    {
        m_IconByColorArray = new Icon[16];

        for ( int iColor = 0; iColor < this.m_IconByColorArray.length; ++iColor )
        {
            m_IconByColorArray[iColor] = register.registerIcon( "cloth_" + iColor );
        }
    }
    
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return m_IconByColorArray[iMetadata % m_IconByColorArray.length];
    }
    
	@Override
    public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
		if ( !m_bIsUpsideDown )
		{
			for ( int iSubtype = 0; iSubtype < m_iNumSubtypes; iSubtype++ )
			{
				list.add( new ItemStack( iBlockID, 1, iSubtype ) );
			}
		}
    }
	
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }
}