// FCMOD

package net.minecraft.src;

public class FCModelBlockFurnaceBrick extends FCModelBlock
{
	public static final float m_fBaseHeight = ( 6F / 16F );
	
	public static final float m_fSideWidth = ( 4F / 16F );
	public static final float m_fHalfSideWidth = ( m_fSideWidth / 2F );
	
	public static final float m_fTopThickness = ( 4F / 16F );
	public static final float m_fHalfTopThickness = ( m_fTopThickness / 2F );
	
	public static final float m_fMindTheGap = 0.001F;
	
	@Override
    protected void InitModel()
    {
    	// interior
    	
		/*
    	AddBox( m_fSideWidth - m_fMindTheGap, m_fBaseHeight - m_fMindTheGap, 0F,
    		1F - ( m_fSideWidth - m_fMindTheGap ), 1F - ( m_fTopThickness - m_fMindTheGap), 1F - m_fSideWidth );
		*/
		
		// inverted interior
		
    	AddBox( 1F - ( m_fSideWidth - m_fMindTheGap ), 1F - ( m_fTopThickness - m_fMindTheGap), 1F - m_fSideWidth, 
    		m_fSideWidth - m_fMindTheGap, m_fBaseHeight - m_fMindTheGap, 0F );
    }    
}
