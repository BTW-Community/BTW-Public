// FCMOD

package net.minecraft.src;

public class FCBlockBlackStone extends BlockQuartz
{
    public FCBlockBlackStone( int iBlockID )
    {
        super( iBlockID );
        
        setHardness( 2F );
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "quartzBlock" );
    }
    
	//----------- Client Side Functionality -----------//
	
    private Icon m_IconChiseled;
    private Icon m_IconLinesSide;
    private Icon m_IconLinesTop;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "fcBlockBlackStone" );
        
        m_IconChiseled = register.registerIcon( "fcBlockBlackStone_chiseled" );
        m_IconLinesSide = register.registerIcon( "fcBlockBlackStone_lines" );
        m_IconLinesTop = register.registerIcon( "fcBlockBlackStone_lines_top" );
    }
        
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iMetadata == 1 ) // chiseled
		{
			return m_IconChiseled;
		}
		else if ( iMetadata == 2 ) // lines
		{
			if ( iSide <= 1 )
			{
				return m_IconLinesTop;
			}
			
			return m_IconLinesSide;
		}
		else if ( iMetadata == 3 ) // lines sideways
		{
			if ( iSide >= 4 )
			{
				return m_IconLinesTop;
			}
			
			return m_IconLinesSide;
		}
		else if ( iMetadata == 4 ) // lines sideways
		{
			if ( iSide == 2 || iSide == 3 )
			{
				return m_IconLinesTop;
			}
			
			return m_IconLinesSide;
		}
		
		return blockIcon;
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderBlockQuartz( this, i, j, k );
    }    
}
