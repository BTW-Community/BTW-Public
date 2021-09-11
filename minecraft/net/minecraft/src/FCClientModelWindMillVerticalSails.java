//FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientModelWindMillVerticalSails extends ModelBase
{
    public ModelRenderer m_Components[];
    
    private final int m_iNumBlades = 8;
    private final int m_iNumComponents = m_iNumBlades;
    
    private final float m_fLocalPi = 3.141593F;
    
    private final float m_fSailOffsetFromCenter = ( FCEntityWindMillVertical.m_fWidth / 2F * 16F ) + 0.5F;
    
    private final int m_iSailLength = (int)( FCEntityWindMillVertical.m_fHeight * 16.0f ) - 8;
    private final float m_fHalfSailLength = ( (float)m_iSailLength / 2F );
    
    private final int m_iSailWidth = 20;
    private final float m_fHalfSailWidth = ( (float)m_iSailWidth / 2F );
    
    private final int m_iSailThickness = 1;
    private final float m_fHalfSailThickness = ( (float)m_iSailThickness / 2F );
    
    public FCClientModelWindMillVerticalSails()
    {
    	m_Components = new ModelRenderer[m_iNumComponents];
  
    	
    	for ( int iTempBlade = 0; iTempBlade < m_iNumBlades; iTempBlade++ )
    	{
    		// sail 
    		
    		m_Components[iTempBlade] = new ModelRenderer( this, 0, 0 );
    		
    		m_Components[iTempBlade].setTextureSize( 16, 16 );
    		
    		m_Components[iTempBlade].addBox( m_fSailOffsetFromCenter - m_fHalfSailThickness, -m_fHalfSailLength, -(float)m_iSailWidth,
    			m_iSailThickness, m_iSailLength, m_iSailWidth );
    		
    		m_Components[iTempBlade].setRotationPoint( 0F, 0F, 0F );
        	
    		m_Components[iTempBlade].rotateAngleY = ( m_fLocalPi * 2F ) * (float)iTempBlade / (float)m_iNumBlades;    		
    	}
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    }
    
	//------------- Class Specific Methods ------------//
    
    public void render( float f, float f1, float f2, float f3, float f4, float fScale, FCEntityWindMillVertical windMillEnt )
    {
        // render blades
        
        float fBrightness = 1.0F;        
        
        for ( int i = 0; i < m_iNumComponents; i++ )
        {
            int iBladeColor = windMillEnt.getBladeColor( i );
            
            GL11.glColor3f( fBrightness * FCEntitySheep.fleeceColorTable[iBladeColor][0], 
        		fBrightness * FCEntitySheep.fleeceColorTable[iBladeColor][1], 
        		fBrightness * FCEntitySheep.fleeceColorTable[iBladeColor][2]);
            
        	m_Components[i].render( fScale );        	
        }
    }    
}