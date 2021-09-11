//FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientModelWindMillVerticalFrame extends ModelBase
{
    public ModelRenderer m_Components[];
    
    private final int m_iNumBlades = 8;
    private final int m_iNumComponents = m_iNumBlades * 4;
    
    private final float m_fLocalPi = 3.141593F;
    
    private final float m_fSpokesOffsetFromCenter = 2;
    
    private final int m_iSpokesLength = (int)( FCEntityWindMillVertical.m_fWidth / 2.0F * 16F ) - 3;
    
    private final float m_fHalfSpokesLength = ( (float)m_iSpokesLength / 2F );
    
    private final int m_iSpokesWidth = 2;
    
    private final float m_fHalfSpokesWidth = ( (float)m_iSpokesWidth / 2F );
    
    private final float m_fSpokesVerticalOffset = ( FCEntityWindMillVertical.m_fHeight / 2F * 16F ) - 2.5F;
    
    private final float m_fRimOffsetFromCenter = FCEntityWindMillVertical.m_fWidth / 2.0F * 16F - 4.5F;
    	
    private final int m_iRimSegmentLength = (int)( FCEntityWindMillVertical.m_fWidth / 2.0F * 16F ) - 18;
    
    private final float m_fHalfRimSegmentLength = ( (float)m_iRimSegmentLength / 2F );
    
    public FCClientModelWindMillVerticalFrame()
    {
    	m_Components = new ModelRenderer[m_iNumComponents];  
    	
    	for ( int iTempBlade = 0; iTempBlade < m_iNumBlades; iTempBlade++ )
    	{
        	// bottom spokes
    		
    		m_Components[iTempBlade] = new ModelRenderer( this, 0, 0 );
    		
    		m_Components[iTempBlade].setTextureSize( 16, 16 );
    		
    		m_Components[iTempBlade].addBox( m_fSpokesOffsetFromCenter, -m_fHalfSpokesWidth - m_fSpokesVerticalOffset, -m_fHalfSpokesWidth,
    			m_iSpokesLength, m_iSpokesWidth, m_iSpokesWidth );
    		
    		m_Components[iTempBlade].setRotationPoint( 0F, 0F, 0F );
        	
    		m_Components[iTempBlade].rotateAngleY = ( m_fLocalPi * 2F ) * (float)iTempBlade / (float)m_iNumBlades;
    		
    		// top spokes
    		
    		m_Components[iTempBlade + m_iNumBlades] = new ModelRenderer( this, 0, 0 );
    		
    		m_Components[iTempBlade + m_iNumBlades].setTextureSize( 16, 16 );
    		
    		m_Components[iTempBlade + m_iNumBlades].addBox( m_fSpokesOffsetFromCenter, -m_fHalfSpokesWidth + m_fSpokesVerticalOffset, -m_fHalfSpokesWidth,
    			m_iSpokesLength, m_iSpokesWidth, m_iSpokesWidth );
    		
    		m_Components[iTempBlade + m_iNumBlades].setRotationPoint( 0F, 0F, 0F );
        	
    		m_Components[iTempBlade + m_iNumBlades].rotateAngleY = ( m_fLocalPi * 2F ) * (float)iTempBlade / (float)m_iNumBlades;
    		
    		// bottom rim
    		
    		m_Components[iTempBlade + m_iNumBlades * 2] = new ModelRenderer( this, 0, 0 );
    		
    		m_Components[iTempBlade + m_iNumBlades * 2].setTextureSize( 16, 16 );
    		
    		m_Components[iTempBlade + m_iNumBlades * 2].addBox( m_fRimOffsetFromCenter - m_fHalfSpokesWidth, -m_fHalfSpokesWidth - m_fSpokesVerticalOffset, -m_fHalfRimSegmentLength,
    			m_iSpokesWidth, m_iSpokesWidth, m_iRimSegmentLength );
    		
    		m_Components[iTempBlade + m_iNumBlades * 2].setRotationPoint( 0F, 0F, 0F );
        	
    		m_Components[iTempBlade + m_iNumBlades * 2].rotateAngleY = ( m_fLocalPi * 2F ) * (float)iTempBlade / (float)m_iNumBlades + ( m_fLocalPi * 2F / 16F );
    		
    		// top rim
    		
    		m_Components[iTempBlade + m_iNumBlades * 3] = new ModelRenderer( this, 0, 0 );
    		
    		m_Components[iTempBlade + m_iNumBlades * 3].setTextureSize( 16, 16 );
    		
    		m_Components[iTempBlade + m_iNumBlades * 3].addBox( m_fRimOffsetFromCenter - m_fHalfSpokesWidth, -m_fHalfSpokesWidth + m_fSpokesVerticalOffset, -m_fHalfRimSegmentLength,
    			m_iSpokesWidth, m_iSpokesWidth, m_iRimSegmentLength );
    		
    		m_Components[iTempBlade + m_iNumBlades * 3].setRotationPoint( 0F, 0F, 0F );
        	
    		m_Components[iTempBlade + m_iNumBlades * 3].rotateAngleY = ( m_fLocalPi * 2F ) * (float)iTempBlade / (float)m_iNumBlades + ( m_fLocalPi * 2F / 16F );
    	}
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    }
    
    //************* Class Specific Methods ************//
    
    public void render( float f, float f1, float f2, float f3, float f4, float fScale, FCEntityWindMillVertical windMillEnt )
    {
        for ( int i = 0; i < m_iNumComponents; i++ )
        {
        	m_Components[i].render( fScale );
        }        
    }    
}