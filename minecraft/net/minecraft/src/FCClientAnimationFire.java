// FCMOD

package net.minecraft.src;

import java.nio.ByteBuffer;

public class FCClientAnimationFire 
{
	public static FCClientAnimationFire[] m_InstanceArray = new FCClientAnimationFire[] { null, null };
	
	public int m_iWidth;
	public int m_iHeight;
	
	public int m_iTextureHeight;
	
	public int m_iSize;
	
    protected float m_fPreviousIntensities[];
    protected float m_fCurrentIntensities[];
    
    private float m_fIntensityDecayFactor; 
    private float m_fIntensityDecayFactorTop;
    private double m_dDistanceFromCenterIntensityModifier; 
    private int m_iCenterRow;

    public final float m_fColorShiftSeperatorBlueToWhite = 0.39F;
    public final float m_fColorShiftSeperatorWhiteToRed = 0.66F;

    public final float m_fInvisiblePixelThresholdTop = 0.87F;
    public final float m_fInvisiblePixelThresholdBottom = 0.001F;
    
	public FCClientAnimationFire( int iInstanceIndex, int iTextureWidth, int iTextureHeight )
	{
		m_iWidth = iTextureWidth;
		m_iHeight = iTextureHeight * 2;
		
		m_iTextureHeight = iTextureHeight;
		
        m_iSize = m_iWidth * m_iHeight;
        
        m_fPreviousIntensities = new float[m_iSize];
        m_fCurrentIntensities = new float[m_iSize];
        
        for ( int iTempIndex = 0; iTempIndex < m_iSize; iTempIndex++ )
        {
        	m_fPreviousIntensities[iTempIndex] = 0F;
        	m_fCurrentIntensities[iTempIndex] = 0F;
        }
        
        m_fIntensityDecayFactor = 1F + ( 0.08F * ( 16F / (float)m_iTextureHeight ) );
        m_fIntensityDecayFactorTop = 1F + ( 0.07F * ( 16F / (float)m_iTextureHeight ) );
        m_dDistanceFromCenterIntensityModifier = 0.123D * ( 16D / (double)m_iWidth );
        
        m_iCenterRow = m_iWidth / 2;
        
        m_InstanceArray[iInstanceIndex] = this;
	}
	
    public void Update()
    {
    	DriftFireUpwards();
    	
    	GenerateNewBottomRow();
    	
        m_fPreviousIntensities = m_fCurrentIntensities;
    }
    
    private void GenerateNewBottomRow()
    {
        for ( int i = 0; i < m_iWidth; i++ )
        {
        	double dDistFromCenter = (double)( m_iCenterRow - Math.abs( i - ( m_iCenterRow - 1 ) ) );
        	
        	double dBaseIntensity = ( Math.random() * Math.random() * Math.random() * 4D ) + 
        		( Math.random() * 0.10000000149011612D ) + 0.2D;
        	
        	double dDistanceFromCenterModifier = dDistFromCenter * m_dDistanceFromCenterIntensityModifier;
        	
        	dDistanceFromCenterModifier = ( dDistanceFromCenterModifier * dDistanceFromCenterModifier ); 
        	
            m_fCurrentIntensities[i + ( ( m_iHeight - 1 ) * m_iWidth )] = 
            	(float)( dBaseIntensity + dDistanceFromCenterModifier );
       }        
    }
    
    private void DriftFireUpwards()
    {
    	// copy all rows except the bottom from the one below, and slowly decrease intensity
    	
        for ( int i = 0; i < m_iWidth; i++ )
        {
            for ( int j = 0; j < m_iHeight - 1; j++ )
            {
                int iTotalWeight = 18; // primary pixel weight
                
                float fNewIntensity = m_fPreviousIntensities[i + ( j + 1 ) * m_iWidth] * (float)iTotalWeight;

                // modify the intensity by the intensity of the pixels around this one, using a weighted average
                
                for ( int iTempI = i - 1; iTempI <= i + 1; iTempI++ )
                {
                    for ( int iTempJ = j; iTempJ <= j + 1; iTempJ++ )
                    {
                        if ( iTempI >= 0 && iTempJ >= 0 && iTempI < m_iWidth && iTempJ < m_iHeight )
                        {
                            fNewIntensity += m_fPreviousIntensities[iTempI + iTempJ * m_iWidth];
                        }

                        iTotalWeight++;
                    }
                }

                if ( j < m_iTextureHeight )
                {
                	fNewIntensity /= ( (float)iTotalWeight * m_fIntensityDecayFactorTop );

                }
                else
                {
                	fNewIntensity /= ( (float)iTotalWeight * m_fIntensityDecayFactor );
                }
                
            	m_fCurrentIntensities[i + j * m_iWidth] = fNewIntensity;
            }
        }
    }
    
    public void CopyRegularFireFrameToByteBuffer( ByteBuffer m_frameBuffer, int m_iBufferPixelSize )
    {
        for ( int iPixelIndex = 0; iPixelIndex < m_iBufferPixelSize; iPixelIndex++ )
        {
            float iPixelIntensity = m_fCurrentIntensities[iPixelIndex];

            if ( iPixelIntensity > 1.0F )
            {
                iPixelIntensity = 1.0F;
            }
            else if ( iPixelIntensity < 0.0F )
            {
                iPixelIntensity = 0.0F;
            }

            float fColorMultiplier = 1.0F -  iPixelIntensity;
            
            int iRed = 0;
            int iGreen = 0;
            int iBlue = 0;
            
            char cAlpha = '\377';
            
            if ( fColorMultiplier > m_fInvisiblePixelThresholdTop || fColorMultiplier < m_fInvisiblePixelThresholdBottom )
            {
                cAlpha = '\0';
            }
            else if ( fColorMultiplier < m_fColorShiftSeperatorBlueToWhite )
            {
            	float fFactor = fColorMultiplier / m_fColorShiftSeperatorBlueToWhite;
            	
            	float fFactorSquared = fFactor * fFactor;
            	
	            iRed = (int)( fFactorSquared * 255F );
	            iGreen = (int)( fFactorSquared * 255F );	            
	            iBlue = (int)( fFactor * 100F ) + 155;            
            }
            else if ( fColorMultiplier < m_fColorShiftSeperatorWhiteToRed )
            {
            	iRed = 255;
            	iGreen = 255;
            	iBlue = 255;
            }
            else
            {   
	            float fDelta = 1.0F - ( ( fColorMultiplier - m_fColorShiftSeperatorWhiteToRed ) / 
	            	( 1.0F - m_fColorShiftSeperatorWhiteToRed ) );

	            iRed = (int)( fDelta * 120F ) + 135;
	            
	            float fDeltaSquared = fDelta * fDelta;
	            
	            iGreen = (int)( fDeltaSquared * 225F ) + 30;
	            
	            float fBlueMultiplier = fDeltaSquared * fDeltaSquared;
	            fBlueMultiplier *= fBlueMultiplier;
	            
	            iBlue = (int)( fBlueMultiplier * 255F );            
            }
            
            m_frameBuffer.put( iPixelIndex * 4 + 0, (byte)iRed );
            m_frameBuffer.put( iPixelIndex * 4 + 1, (byte)iGreen );
            m_frameBuffer.put( iPixelIndex * 4 + 2, (byte)iBlue );
            m_frameBuffer.put( iPixelIndex * 4 + 3, (byte)cAlpha );
        }
    }
    
    public void CopyStokedFireFrameToByteBuffer( ByteBuffer m_frameBuffer, int m_iBufferPixelSize )
    {
        for ( int iPixelIndex = 0; iPixelIndex < m_iBufferPixelSize; iPixelIndex++ )
        {
            float iPixelIntensity = m_fCurrentIntensities[iPixelIndex + ( m_iTextureHeight * m_iWidth )];

            if ( iPixelIntensity > 1.0F )
            {
                iPixelIntensity = 1.0F;
            }
            else if ( iPixelIntensity < 0.0F )
            {
                iPixelIntensity = 0.0F;
            }

            float fColorMultiplier = 1.0F -  iPixelIntensity;
            
            int iRed = 0;
            int iGreen = 0;
            int iBlue = 0;
            
            char cAlpha = '\377';
            
            if ( fColorMultiplier > m_fInvisiblePixelThresholdTop || fColorMultiplier < m_fInvisiblePixelThresholdBottom )
            {
                cAlpha = '\0';
            }
            else if ( fColorMultiplier < m_fColorShiftSeperatorBlueToWhite )
            {
            	float fFactor = fColorMultiplier / m_fColorShiftSeperatorBlueToWhite;
            	
            	float fFactorSquared = fFactor * fFactor;
            	
	            iRed = (int)( fFactorSquared * 255F );
	            iGreen = (int)( fFactorSquared * 255F );	            
	            iBlue = (int)( fFactor * 100F ) + 155;            
            }
            else if ( fColorMultiplier < m_fColorShiftSeperatorWhiteToRed )
            {
            	iRed = 255;
            	iGreen = 255;
            	iBlue = 255;
            }
            else
            {   
	            float fDelta = 1.0F - ( ( fColorMultiplier - m_fColorShiftSeperatorWhiteToRed ) / 
	            	( 1.0F - m_fColorShiftSeperatorWhiteToRed ) );

	            iRed = (int)( fDelta * 120F ) + 135;
	            
	            float fDeltaSquared = fDelta * fDelta;
	            
	            iGreen = (int)( fDeltaSquared * 225F ) + 30;
	            
	            float fBlueMultiplier = fDeltaSquared * fDeltaSquared;
	            fBlueMultiplier *= fBlueMultiplier;
	            
	            iBlue = (int)( fBlueMultiplier * 255F );            
            }
            
            m_frameBuffer.put( iPixelIndex * 4 + 0, (byte)iRed );
            m_frameBuffer.put( iPixelIndex * 4 + 1, (byte)iGreen );
            m_frameBuffer.put( iPixelIndex * 4 + 2, (byte)iBlue );
            m_frameBuffer.put( iPixelIndex * 4 + 3, (byte)cAlpha );
        }
    }
    
    static public void UpdateInstances()
    {
    	for ( int iTempIndex = 0; iTempIndex < 2; iTempIndex++ )
    	{
    		if ( m_InstanceArray[iTempIndex] != null )
    		{
    			m_InstanceArray[iTempIndex].Update();    			
    		}
    	}
    }    
}