// FCMOD

package net.minecraft.src;

public class FCModelBlockSpike extends FCModelBlock
{
    protected static final double m_dShaftWidth = ( 1D / 16D );
    protected static final double m_dShaftHalfWidth = ( m_dShaftWidth / 2D );
    
    protected static final double m_dBaseWidth = ( 4D / 16D );
    protected static final double m_dBaseHalfWidth = ( m_dBaseWidth / 2D );
    protected static final double m_dBaseHeight = ( 2D / 16D );
    protected static final double m_dBaseHalfHeight = ( m_dBaseHeight / 2D );
    
	protected static final double m_dBallWidth = ( 3D / 16D );
    protected static final double m_dBallHalfWidth = ( m_dBallWidth / 2D );
    protected static final double m_dBallVeticalOffset = ( 10D / 16D );
    
    protected static final double m_dCandleHolderWidth = ( 4D / 16D );
    protected static final double m_dCandleHolderHalfWidth = ( m_dCandleHolderWidth / 2D );
    protected static final double m_dCandleHolderHeight = ( 1D / 16D );
    protected static final double m_dCandleHolderHalfHeight = ( m_dCandleHolderHeight / 2D );
    
    protected static final double m_dCandleHolderVeticalOffset = ( 1D - m_dCandleHolderHeight );
    
    protected static final double m_dCenterBraceWidth = ( 3D / 16D );
    protected static final double m_dCenterBraceHalfWidth = ( m_dCenterBraceWidth / 2D );
    protected static final double m_dCenterBraceHeight = ( 4D / 16D );
    protected static final double m_dCenterBraceHalfHeight = ( m_dCenterBraceHeight / 2D );
    protected static final double m_dCenterBraceVeticalOffset = ( 0.5D - m_dCenterBraceHalfHeight ); 
    
    protected static final double m_dSideSupportLength = ( 8D / 16D );
    protected static final double m_dSideSupportWidth = ( 1D / 16D );
    protected static final double m_dSideSupportHalfWidth = ( m_dSideSupportWidth / 2D );
    protected static final double m_dSideSupportHeight = ( 2D / 16D );
    protected static final double m_dSideSupportHalfHeight = ( m_dSideSupportHeight / 2D );
    protected static final double m_dSideSupportVeticalOffset = ( 0.5D - m_dSideSupportHalfHeight );
    
    protected static final double m_dBoundingBoxWidth = ( 4D / 16D );
    protected static final double m_dBoundingBoxHalfWidth = ( m_dBoundingBoxWidth / 2D );
    
	public FCModelBlock m_modelBase;
	public FCModelBlock m_modelHolder;
	public FCModelBlock m_modelBall;
	public FCModelBlock m_modelCenterBrace;
	public FCModelBlock m_modelSideSupport;
	
	public AxisAlignedBB m_boxCollisionCenter;
	public AxisAlignedBB m_boxCollisionStrut;
	
	@Override
    protected void InitModel()
    {
		// shaft
		
		AddBox( 0.5D - m_dShaftHalfWidth, 0D, 0.5D - m_dShaftHalfWidth,
			0.5D + m_dShaftHalfWidth, 1D, 0.5D + m_dShaftHalfWidth );

		// base
		
		m_modelBase = new FCModelBlock();
		
		m_modelBase.AddBox( 0.5D - m_dBaseHalfWidth, 0D, 0.5D - m_dBaseHalfWidth,
			0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfWidth );
		
		// holder
		
		m_modelHolder = new FCModelBlock();
		
		m_modelHolder.AddBox( 0.5D - m_dCandleHolderHalfWidth, 
			m_dCandleHolderVeticalOffset, 0.5D - m_dCandleHolderHalfWidth,
			0.5D + m_dCandleHolderHalfWidth,  
			m_dCandleHolderVeticalOffset + m_dCandleHolderHeight, 
			0.5D + m_dCandleHolderHalfWidth );
		
		// ball
		
		m_modelBall = new FCModelBlock();
		
		m_modelBall.AddBox( 0.5D - m_dBallHalfWidth, 
			m_dBallVeticalOffset, 0.5D - m_dBallHalfWidth,
			0.5D + m_dBallHalfWidth, m_dBallVeticalOffset + m_dBallWidth, 
			0.5D + m_dBallHalfWidth );
		
		// center brace
		
		m_modelCenterBrace = new FCModelBlock();
		
		m_modelCenterBrace.AddBox( 0.5D - m_dCenterBraceHalfWidth, 
			m_dCenterBraceVeticalOffset, 0.5D - m_dCenterBraceHalfWidth,
			0.5D + m_dCenterBraceHalfWidth,  
			m_dCenterBraceVeticalOffset + m_dCenterBraceHeight, 
			0.5D + m_dCenterBraceHalfWidth );
		
		// side support
		
		m_modelSideSupport = new FCModelBlock();
		
		m_modelSideSupport.AddBox( 0.5D - m_dSideSupportHalfWidth, 
			m_dSideSupportVeticalOffset, 0D,
			0.5D + m_dSideSupportHalfWidth,  
			m_dSideSupportVeticalOffset + m_dSideSupportHeight, 
			m_dSideSupportLength );
		
		// collision volumes 
		
		m_boxCollisionCenter = new AxisAlignedBB( 
			0.5D - m_dBoundingBoxHalfWidth, 0D, 0.5D - m_dBoundingBoxHalfWidth, 
    		0.5D + m_dBoundingBoxHalfWidth, 1D, 0.5D + m_dBoundingBoxHalfWidth );
		
		m_boxCollisionStrut = new AxisAlignedBB( 
			0.5D - m_dBoundingBoxHalfWidth, 0D, 0D, 
    		0.5D + m_dBoundingBoxHalfWidth, 1D, 0.5D + m_dBoundingBoxHalfWidth );
    }
	
	//----------- Client Side Functionality -----------//

    @Override
    public void RenderAsItemBlock( RenderBlocks renderBlocks, Block block, int iItemDamage )
    {
    	super.RenderAsItemBlock( renderBlocks, block, iItemDamage );
    	
    	m_modelBase.RenderAsItemBlock( renderBlocks, block, iItemDamage );
    	m_modelBall.RenderAsItemBlock( renderBlocks, block, iItemDamage );    	
    }
}
