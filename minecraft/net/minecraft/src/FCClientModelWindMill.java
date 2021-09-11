//FCMOD (client only)

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientModelWindMill extends ModelBase
{
    public ModelRenderer windMillComponents[];
    
    private final int iNumWindMillComponents = 8;
    
    private final float fLocalPi = 3.141593F;
    
    private final float fBladeOffsetFromCenter = 15.0f;
    private final int iBladeLength= (int)( ( FCEntityWindMill.m_fHeight * 8.0f ) - fBladeOffsetFromCenter ) - 3;
    private final int iBladeWidth = 16;
    
    private final float fShaftOffsetFromCenter = 2.5f;
    private final int iShaftLength = (int)( ( FCEntityWindMill.m_fHeight * 8.0f ) - fShaftOffsetFromCenter ) - 2;
    private final int iShaftWidth = 4;
    	
    public FCClientModelWindMill()
    {
    	windMillComponents = new ModelRenderer[iNumWindMillComponents];
  
    	// shafts
    	
    	for ( int i = 0; i < 4; i++ )
    	{
    		windMillComponents[i] = new ModelRenderer( this, 0, 0 );
    		
    		windMillComponents[i].addBox( fShaftOffsetFromCenter, -(float)iShaftWidth / 2.0f, -(float)iShaftWidth / 2.0f, 
    				iShaftLength, iShaftWidth, iShaftWidth );
    		
    		windMillComponents[i].setRotationPoint( 0F, 0F, 0F );
        	
    		windMillComponents[i].rotateAngleZ = fLocalPi * (float)( i - 4 ) / 2.0F ;
    	}    	
    	
    	// blades
    	
    	for ( int i = 4; i < 8; i++ )
    	{
    		windMillComponents[i] = new ModelRenderer( this, 0, 15 );
    		windMillComponents[i].addBox( fBladeOffsetFromCenter, 1.75f/*-(float)iBladeWidth / 2.0f*/, 1.0F, iBladeLength, iBladeWidth, 1 );
    		windMillComponents[i].setRotationPoint( 0F, 0F, 0F );
        	
    		windMillComponents[i].rotateAngleX = -fLocalPi / 12.0F ; // 15 degrees
    		windMillComponents[i].rotateAngleZ = fLocalPi * (float)i / 2.0F ;
    	}
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    }
    
	//------------- Class Specific Methods ------------//
    
    public void render( float f, float f1, float f2, float f3, float f4, float f5, FCEntityWindMill windMillEnt )
    {
    	// render shafts
    	
        for ( int i = 0; i < 4; i++ )
        {
        	windMillComponents[i].render( f5 );
        }
        
        // render blades
        
        float fBrightness = 1.0F;//windMillEnt.getBrightness(f);	// render change brought about in 1.0(?)        
        
        for ( int i = 4; i < 8; i++ )
        {
            int iBladeColor = windMillEnt.getBladeColor( i - 4 );
            
            GL11.glColor3f( fBrightness * FCEntitySheep.fleeceColorTable[iBladeColor][0], 
        		fBrightness * FCEntitySheep.fleeceColorTable[iBladeColor][1], 
        		fBrightness * FCEntitySheep.fleeceColorTable[iBladeColor][2]);
            
        	windMillComponents[i].render( f5 );        	
        }
    }    
}