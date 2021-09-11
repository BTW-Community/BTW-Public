// FCMOD

package net.minecraft.src;

public class FCModelBlockHamper extends FCModelBlock
{
	public static final double m_dBaseHeight = ( 13D / 16D );
	public static final double m_dBaseWidth = ( 14D / 16D );
	public static final double m_dBaseDepth = ( 12D / 16D );

	public static final double m_dBaseBottomHeight = ( 2D / 16D );
	public static final double m_dBaseBottomWidthGap = ( 1D / 16D );
	
	public static final double m_dBaseHalfWidth = ( m_dBaseWidth / 2D );
	public static final double m_dBaseHalfDepth = ( m_dBaseDepth / 2D );
	
	public static final double m_dLidHeight = ( 3D / 16D );
	public static final double m_dLidWidth = ( m_dBaseWidth + ( 2D / 16D ) );
	public static final double m_dLidDepth = ( m_dBaseDepth + ( 2D / 16D ) );
	
	public static final double m_dLidHalfWidth = ( m_dLidWidth / 2D );
	public static final double m_dLidHalfDepth = ( m_dLidDepth / 2D );
	
	private static final float m_fLidLayerHeight = ( 1F / 16F );
	private static final float m_fLidLayerWidthGap = ( 1F / 16F );
	
	private static final double m_dLidMinX = 0.5D - m_dLidHalfWidth;
	private static final double m_dLidMaxX = 0.5D + m_dLidHalfWidth;
	
	private static final double m_dLidMinZ = 0.5D - m_dLidHalfDepth;
	private static final double m_dLidMaxZ = 0.5D + m_dLidHalfDepth;
	
	private static final int m_iNumLidLayers = 3;
	
    private static final Vec3 m_lidRotationPoint = Vec3.createVectorHelper( 8F / 16F, 13F / 16F, 14F / 16F );
    
	public FCModelBlock m_lid;
	
	public AxisAlignedBB m_selectionBox;
	public AxisAlignedBB m_selectionBoxOpen;
	
	@Override
    protected void InitModel()
    {
		// base
		
		AddBox( 0.5D - m_dBaseHalfWidth, m_dBaseBottomHeight, 0.5D - m_dBaseHalfDepth,
			0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfDepth );
		
		AddBox( 0.5D - m_dBaseHalfWidth + m_dBaseBottomWidthGap, 0D, 0.5D - m_dBaseHalfDepth + m_dBaseBottomWidthGap ,
			0.5D + m_dBaseHalfWidth - m_dBaseBottomWidthGap, m_dBaseBottomHeight, 0.5D + m_dBaseHalfDepth - m_dBaseBottomWidthGap );
		
		// lid
		
		m_lid = new FCModelBlock();

		for ( double iTempLayer = 0; iTempLayer < m_iNumLidLayers; iTempLayer++ )
		{
			double m_dWidthOffset = iTempLayer * m_fLidLayerWidthGap;
			double m_dHeighOffset = iTempLayer * m_fLidLayerHeight;
			
			m_lid.AddBox( 0.5D - m_dLidHalfWidth + m_dWidthOffset, 
				m_dBaseHeight + m_dHeighOffset, 
				0.5D - m_dLidHalfDepth + m_dWidthOffset,
				0.5D + m_dLidHalfWidth - m_dWidthOffset, 
				m_dBaseHeight + m_fLidLayerHeight + m_dHeighOffset, 
				0.5D + m_dLidHalfDepth - m_dWidthOffset );
		}
		
		// interior
		
		m_selectionBox = new AxisAlignedBB( 0.5D - m_dBaseHalfWidth, m_dBaseBottomHeight, 0.5D - m_dBaseHalfDepth,
				0.5D + m_dBaseHalfWidth, m_dBaseHeight + ( m_fLidLayerHeight * 2F ), 0.5D + m_dBaseHalfDepth );
		
		m_selectionBoxOpen = new AxisAlignedBB( 0.5D - m_dBaseHalfWidth, m_dBaseBottomHeight, 0.5D - m_dBaseHalfDepth,
			0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfDepth );    
	}
	
	public Vec3 GetLidRotationPoint()
	{
    	return m_lidRotationPoint;
	}	
}
