// FCMOD

package net.minecraft.src;

public class FCModelBlockCampfire extends FCModelBlock
{
    private static final float m_fLogThickness  = ( 2F / 16F );
    
    private static final float m_fMinorBorderWidth  = ( 1F / 16F );
    private static final float m_fMajorBorderWidth  = ( 0F / 16F );
    
    private static final float m_fLayerWidthOffset = ( 1F / 16F );
    private static final float m_fLayerLengthOffset = ( 1F / 16F );
    
    private static final int m_iNumLayers = 4;
    
    public FCModelBlock m_modelInSnow;
    
	@Override
    protected void InitModel()
    {
		m_modelInSnow = new FCModelBlock();
		
        for ( int iTempLayer = 0; iTempLayer < m_iNumLayers; iTempLayer++ )
        {
        	float fLayerMultiplier = (float)iTempLayer;
        	
        	float fMinY = fLayerMultiplier * m_fLogThickness;
        	float fMaxY = fMinY + m_fLogThickness;
        	
        	float fMinorOffset = 0F;
        	float fMajorOffset = 0F;
        	
        	if ( iTempLayer > 1 )
        	{
        		fMajorOffset = m_fLayerLengthOffset * ( fLayerMultiplier - 1F );
        	}
    		fMinorOffset = m_fLayerWidthOffset * ( fLayerMultiplier );    		
        	
        	float fMinorAxisMin = m_fMinorBorderWidth + fMinorOffset;
        	float fMinorAxisMax = fMinorAxisMin + m_fLogThickness;        	
        	
        	float fMajorAxisMin = m_fMajorBorderWidth + fMajorOffset;
        	float fMajorAxisMax = 1F - fMajorAxisMin;
        	
        	if ( ( iTempLayer & 1 ) == 0 )
        	{
        		AddBox( fMajorAxisMin, fMinY, fMinorAxisMin, fMajorAxisMax, fMaxY, fMinorAxisMax );
        		AddBox( fMajorAxisMin, fMinY, 1F -  fMinorAxisMax, fMajorAxisMax, fMaxY, 1F - fMinorAxisMin );
        		
        		if ( iTempLayer != 0 )
        		{
        			m_modelInSnow.AddBox( fMajorAxisMin, fMinY, fMinorAxisMin, fMajorAxisMax, fMaxY, fMinorAxisMax );
        			m_modelInSnow.AddBox( fMajorAxisMin, fMinY, 1F -  fMinorAxisMax, fMajorAxisMax, fMaxY, 1F - fMinorAxisMin );
        		}
        	}
        	else
        	{
        		AddBox( fMinorAxisMin, fMinY, fMajorAxisMin, fMinorAxisMax, fMaxY, fMajorAxisMax );
        		AddBox( 1F - fMinorAxisMax, fMinY, fMajorAxisMin, 1F - fMinorAxisMin, fMaxY, fMajorAxisMax );
        		
        		m_modelInSnow.AddBox( fMinorAxisMin, fMinY, fMajorAxisMin, fMinorAxisMax, fMaxY, fMajorAxisMax );
        		m_modelInSnow.AddBox( 1F - fMinorAxisMax, fMinY, fMajorAxisMin, 1F - fMinorAxisMin, fMaxY, fMajorAxisMax );
        	}
        }
    }
}
