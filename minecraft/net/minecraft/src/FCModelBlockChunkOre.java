// FCMOD

package net.minecraft.src;

public class FCModelBlockChunkOre extends FCModelBlock
{
    protected static final double m_dBaseWidth = ( 3D / 16D );
    protected static final double m_dBaseHalfWidth = ( m_dBaseWidth / 2D );
    protected static final double m_dBaseHeight = ( 0.5D / 16D );
    
    protected static final double m_dCenterWidth = ( 4D / 16D );
    protected static final double m_dCenterHalfWidth = ( m_dCenterWidth / 2D );
    protected static final double m_dCenterHeight = ( 3D / 16D );
    
    protected static final double m_dTopWidth = ( 2D / 16D );
    protected static final double m_dTopHalfWidth = ( m_dTopWidth / 2D );
    protected static final double m_dTopHeight = ( 0.5D / 16D );
    protected static final double m_dTopVerticalOffset = ( m_dBaseHeight + m_dCenterHeight );
    protected static final double m_dTopXOffset = ( 0D );
    protected static final double m_dTopZOffset = ( 0.5D / 16D );
    
    protected static final double m_dFringeXWidth = ( 5D / 16D );
    protected static final double m_dFringeXHalfWidth = ( m_dFringeXWidth / 2D );
    protected static final double m_dFringeXDepth = ( 3D / 16D );
    protected static final double m_dFringeXHalfDepth = ( m_dFringeXDepth / 2D );
    protected static final double m_dFringeXHeight = ( 1.5D / 16D );
    protected static final double m_dFringeXVerticalOffset = ( m_dBaseHeight + ( 0.5D / 16D ) );
    
    protected static final double m_dFringeZWidth = ( 3D / 16D );
    protected static final double m_dFringeZHalfWidth = ( m_dFringeZWidth / 2D );
    protected static final double m_dFringeZDepth = ( 5D / 16D );
    protected static final double m_dFringeZHalfDepth = ( m_dFringeZDepth / 2D );
    protected static final double m_dFringeZHeight = ( 1.5D / 16D );
    protected static final double m_dFringeZVerticalOffset = ( m_dBaseHeight + ( 1D / 16D ) );
    
    public static final double m_dBoundingBoxWidth = ( m_dCenterWidth );
    public static final double m_dBoundingBoxHalfWidth = ( m_dBoundingBoxWidth / 2D );
    public static final double m_dBoundingBoxHeight = ( m_dCenterHeight );
    public static final double m_dBoundingBoxVerticalOffset = ( m_dBaseHeight );
    
	@Override
    protected void InitModel()
    {
		super.InitModel();
		
		// base
		
		AddBox( 0.5D - m_dBaseHalfWidth, 0D, 0.5D - m_dBaseHalfWidth,
			0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfWidth );
		
		// center
		
		AddBox( 0.5D - m_dCenterHalfWidth, m_dBaseHeight, 0.5D - m_dCenterHalfWidth,
			0.5D + m_dCenterHalfWidth, m_dBaseHeight + m_dCenterHeight, 0.5D + m_dCenterHalfWidth );
		
		// top
		
		AddBox( 0.5D - m_dTopHalfWidth + m_dTopXOffset, m_dTopVerticalOffset, 
			0.5D - m_dTopHalfWidth + m_dTopZOffset,
			0.5D + m_dTopHalfWidth + m_dTopXOffset, m_dTopVerticalOffset + m_dTopHeight, 
			0.5D + m_dTopHalfWidth + m_dTopZOffset );
		
		// fringe
		
		AddBox( 0.5D - m_dFringeXHalfWidth, m_dFringeXVerticalOffset, 0.5D - m_dFringeXHalfDepth,
			0.5D + m_dFringeXHalfWidth, m_dFringeXVerticalOffset + m_dFringeXHeight, 
			0.5D + m_dFringeXHalfDepth );
		
		AddBox( 0.5D - m_dFringeZHalfWidth, m_dFringeZVerticalOffset, 0.5D - m_dFringeZHalfDepth,
			0.5D + m_dFringeZHalfWidth, m_dFringeZVerticalOffset + m_dFringeZHeight, 
			0.5D + m_dFringeZHalfDepth );		
    }
}