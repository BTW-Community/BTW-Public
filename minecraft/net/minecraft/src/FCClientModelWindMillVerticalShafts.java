//FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientModelWindMillVerticalShafts extends ModelBase
{
    public ModelRenderer m_Components[];
    
    private final int m_iNumBlades = 8;
    private final int m_iNumComponents = m_iNumBlades;
    
    private final float m_fLocalPi = 3.141593F;
    
    private final float m_fShaftOffsetFromCenter = ( FCEntityWindMillVertical.m_fWidth / 2F * 16F );
    
    private final int m_iShaftLength = (int)( FCEntityWindMillVertical.m_fHeight * 16F );
    
    private final float m_fShaftHalfLength = ( (float)m_iShaftLength / 2F );
    
    private final int m_iShaftWidth = 4;
    
    private final float m_fHalfShaftWidth = ( (float)m_iShaftWidth / 2F );
    	
    public FCClientModelWindMillVerticalShafts()
    {
    	m_Components = new ModelRenderer[m_iNumComponents];
  
    	
    	for ( int iTempBlade = 0; iTempBlade < m_iNumBlades; iTempBlade++ )
    	{
        	// shaft
    		
    		m_Components[iTempBlade] = new ModelRenderer( this, 0, 0 );
    		
    		m_Components[iTempBlade].setTextureSize( 16, 16 );
    		
    		m_Components[iTempBlade].addBox( m_fShaftOffsetFromCenter - m_fHalfShaftWidth, -m_fShaftHalfLength, -m_fHalfShaftWidth,
    			m_iShaftWidth, m_iShaftLength, m_iShaftWidth );
    		
    		m_Components[iTempBlade].setRotationPoint( 0F, 0F, 0F );
        	
    		m_Components[iTempBlade].rotateAngleY = ( m_fLocalPi * 2F ) * (float)iTempBlade / (float)m_iNumBlades;    		
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