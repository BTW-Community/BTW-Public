// FCMOD

package net.minecraft.src;

public class FCClientModelWaterWheel extends ModelBase
{
    public ModelRenderer waterWheelComponents[];
    
    private final int iNumWaterWheelComponents = 16;
    
    private final float fLocalPi = 3.141593F;
    private final float fStrutDistFromCent = 30.0F;

    public FCClientModelWaterWheel()
    {
    	waterWheelComponents = new ModelRenderer[iNumWaterWheelComponents];
  
    	// blades
    	
    	for ( int i = 0; i < 8; i++ )
    	{
        	waterWheelComponents[i] = new ModelRenderer( this, 0, 0 );
        	waterWheelComponents[i].addBox( 2.50F, -1F, -7F, 36, 2, 14);
        	waterWheelComponents[i].setRotationPoint( 0F, 0F, 0F);
        	
        	waterWheelComponents[i].rotateAngleZ = fLocalPi * (float)i / 4.0F ;
    	}
    	
    	// struts
    	
    	for ( int i = 0; i < 8; i++ )
    	{
        	waterWheelComponents[i + 8] = new ModelRenderer( this, 0, 0 );
        	waterWheelComponents[i + 8].addBox( 0.0F, -1F, -6F, 23, 2, 12);
        	
        	float fRotationAngle = fLocalPi * 0.25F * i;
        	
        	waterWheelComponents[i + 8].setRotationPoint( fStrutDistFromCent * MathHelper.cos( fRotationAngle ), 
        			fStrutDistFromCent * MathHelper.sin( fRotationAngle ), 0F);
        	
        	waterWheelComponents[i + 8].rotateAngleZ = ( fLocalPi * 0.625F ) + ( 0.25F * fLocalPi * i );
    	}    		
    }

    @Override
    public void render( Entity entity, float f, float f1, float f2, float f3, float f4, float f5 )
    {
        for(int i = 0; i < iNumWaterWheelComponents; i++)
        {
        	waterWheelComponents[i].render( f5 );
        }
    }
    
    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    }
}