// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockOre extends BlockOre
{
    public FCBlockOre( int iBlockID )
    {
        super( iBlockID );
        
        SetPicksEffectiveOn();
    }

    @Override
    public boolean HasStrata()
    {
    	return true;
    }
    
    @Override
    public int GetMetadataConversionForStrataLevel( int iLevel, int iMetadata )
    {
    	return iLevel;
    }
    
    @Override
    public float getBlockHardness( World world, int i, int j, int k )
    {
    	int iStrata = GetStrata( world, i, j, k );
    	
    	if ( iStrata != 0 )
    	{
    		// normal ore has a hardness of 3
    		
	    	if ( iStrata == 1 )
	    	{
	    		return 4.0F;
	    	}
	    	else
	    	{
	    		return 6.0F; 
	    	}
    	}
    	
        return super.getBlockHardness( world, i, j, k );
    }
    
    @Override
    public float getExplosionResistance( Entity entity, World world, int i, int j, int k )
    {
    	int iStrata = GetStrata( world, i, j, k );
    	
    	if ( iStrata != 0 )
    	{
    		// normal ore has a resistance of 5
    		
	    	if ( iStrata == 1 )
	    	{
	    		return 7F * ( 3.0F / 5.0F );
	    	}
	    	else
	    	{
	    		return  10F * ( 3.0F / 5.0F );
	    	}
    	}
    	
        return super.getExplosionResistance( entity, world, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
	
    public int GetStrata( IBlockAccess blockAccess, int i, int j, int k )
    {
		return GetStrata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetStrata( int iMetadata )
    {
    	return iMetadata & 3;
    }
    
    public int GetRequiredToolLevelForStrata( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iStrata = GetStrata( blockAccess, i, j, k );
    	
    	if ( iStrata > 1 )
    	{
    		return iStrata + 1;
    	}
    	
    	return 2;
    }
    
    @Override
    public boolean IsNaturalStone( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
	//----------- Client Side Functionality -----------//
    
    private Icon m_IconByMetadataArray[] = new Icon[16];

	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_IconByMetadataArray[0] = blockIcon;		
		m_IconByMetadataArray[1] = register.registerIcon( "fcBlock" + getUnlocalizedName2() + "Strata_1" );				
		m_IconByMetadataArray[2] = register.registerIcon( "fcBlock" + getUnlocalizedName2() + "Strata_2" );
		
		for ( int iTempIndex = 3; iTempIndex < 16; iTempIndex++ )
		{
			m_IconByMetadataArray[iTempIndex] = blockIcon;
		}
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return m_IconByMetadataArray[iMetadata];    	
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	return renderer.RenderStandardFullBlock( this, i, j, k );
    }    

    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
    {
        RenderCookingByKilnOverlay( renderBlocks, i, j, k, bFirstPassResult );
    }
    
    @Override
    public boolean DoesItemRenderAsBlock( int iItemDamage )
    {
    	return true;
    }
    
    @Override
	public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
        list.add( new ItemStack( iBlockID, 1, 0 ) );
        //list.add( new ItemStack( iBlockID, 1, 1 ) );
        //list.add( new ItemStack( iBlockID, 1, 2 ) );
    }
    
    @Override
    public void RenderBlockMovedByPiston( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	renderBlocks.RenderStandardFullBlockMovedByPiston( this, i, j, k );
    }    
}