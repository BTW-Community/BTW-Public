// FCMOD

package net.minecraft.src;

public class FCModelBlockBucketFull extends FCModelBlockBucket
{
	public static final double m_dContentsHeight = ( m_dHeight - m_dBaseHeight - ( 1.5D / 16D ) );
	public static final double m_dContentsVerticalOffset = ( m_dBaseHeight + ( 1D / 16D ) );
	public static final double m_dContentsWidth = m_dInteriorWidth;
	public static final double m_dContentsHalfWidth = ( m_dContentsWidth / 2D );
	
	public FCModelBlockBucketFull()
	{
		super();
	}
	
	@Override
    protected void InitModel()
    {
		super.InitModel();
		
		FCUtilsPrimitiveAABBWithBenefits tempBox = new FCUtilsPrimitiveAABBWithBenefits( 
			0.5D - m_dContentsHalfWidth, m_dContentsVerticalOffset, 0.5D - m_dContentsHalfWidth, 
			0.5D + m_dContentsHalfWidth, m_dContentsVerticalOffset + m_dContentsHeight, 
			0.5D + m_dContentsHalfWidth, 
			m_iAssemblyIDContents );
		
		AddPrimitive( tempBox );
    }
}