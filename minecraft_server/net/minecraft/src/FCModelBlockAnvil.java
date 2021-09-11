// FCMOD

package net.minecraft.src;

public class FCModelBlockAnvil extends FCModelBlock
{
	private static final double m_dBaseWidth = 0.75D;
	private static final double m_dBaseHalfWidth = ( m_dBaseWidth / 2D );
	
	private static final double m_dBaseHeight = 0.25D;
	
	private static final double m_dBaseTopWidth = 0.5D;
	private static final double m_dBaseTopHalfWidth = ( m_dBaseTopWidth / 2D );
	
	private static final double m_dBaseTopDepth = ( 10D / 16D );
	private static final double m_dBaseTopHalfDepth = ( m_dBaseTopDepth / 2D );
	
	private static final double m_dBaseTopHeight = ( 1D / 16D );
	
	private static final double m_dShaftWidth = 0.25D;
	private static final double m_dShaftHalfWidth = ( m_dShaftWidth / 2D );
	
	private static final double m_dShaftDepth = 0.5D;
	private static final double m_dShaftHalfDepth = ( m_dShaftDepth / 2D );
	
	private static final double m_dShaftHeight = ( 5D / 16D );
	
	private static final double m_dTopWidth = ( 10D / 16D );
	private static final double m_dTopHalfWidth = ( m_dTopWidth / 2D );
	
	private static final double m_dTopDepth = 1D;
	private static final double m_dTopHalfDepth = ( m_dTopDepth / 2D );
	
	private static final double m_dTopHeight = ( 6D / 16D );

	public AxisAlignedBB m_boxSelection;
	
	@Override
    protected void InitModel()
    {
		AddBox( 0.5D - m_dBaseHalfWidth, 0D, 0.5D - m_dBaseHalfWidth,
			0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfWidth );
		
		double dRunningHeight = m_dBaseHeight;
		
		AddBox( 0.5D - m_dBaseTopHalfWidth, dRunningHeight, 0.5D - m_dBaseTopHalfDepth,
			0.5D + m_dBaseTopHalfWidth, dRunningHeight + m_dBaseTopHeight, 0.5D + m_dBaseTopHalfDepth );
		
		dRunningHeight += m_dBaseTopHeight;
		
		AddBox( 0.5D - m_dShaftHalfWidth, dRunningHeight, 0.5D - m_dShaftHalfDepth,
			0.5D + m_dShaftHalfWidth, dRunningHeight + m_dShaftHeight, 0.5D + m_dShaftHalfDepth );
		
		dRunningHeight += m_dShaftHeight;
		
		AddBox( 0.5D - m_dTopHalfWidth, dRunningHeight, 0.5D - m_dTopHalfDepth,
			0.5D + m_dTopHalfWidth, dRunningHeight + m_dTopHeight, 0.5D + m_dTopHalfDepth );
		
		m_boxSelection = new AxisAlignedBB( 0.5D - m_dTopHalfWidth, 1D - m_dTopHeight, 0.5D - m_dTopHalfDepth, 
			0.5D + m_dTopHalfWidth, 1D, 0.5D + m_dTopHalfDepth ); // same as vanilla
    }
}
