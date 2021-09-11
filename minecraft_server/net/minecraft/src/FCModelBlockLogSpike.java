// FCMOD

package net.minecraft.src;

public class FCModelBlockLogSpike extends FCModelBlock
{
    private final static float m_fRimWidth = ( 1F / 16F );
    
    private final static float m_fLayerHeight = ( 2F / 16F );    
    private final static float m_fFirstLayerHeight = ( 3F / 16F );    
    private final static float m_fLayerWidthGap = ( 1F / 16F );
    
    private final static float m_fSelectionHeight = m_fFirstLayerHeight;    
    private final static float m_fSelectionWidthGap = m_fRimWidth;    
    
    public final static AxisAlignedBB m_boxSelection = new AxisAlignedBB( 
    	FCModelBlockLogSpike.m_fSelectionWidthGap, 0F, FCModelBlockLogSpike.m_fSelectionWidthGap, 
    	1F - FCModelBlockLogSpike.m_fSelectionWidthGap, FCModelBlockLogSpike.m_fSelectionHeight, 1F - FCModelBlockLogSpike.m_fSelectionWidthGap );
    
	@Override
    protected void InitModel()
    {
        AddBox( m_fRimWidth, 0, m_fRimWidth, 1F - m_fRimWidth, m_fFirstLayerHeight, 1F - m_fRimWidth );        
        
        for ( int iTempLayer = 1; iTempLayer <= 6; iTempLayer++ )
        {        
	        float fWidthGap = m_fRimWidth + ( m_fLayerWidthGap * iTempLayer );
	        float fHeightGap = m_fFirstLayerHeight + ( m_fLayerHeight * ( iTempLayer - 1 ) );
	        
	        AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
        }        
    }
	
    //------------- Class Specific Methods ------------//
}
