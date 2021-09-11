// FCMOD (client only)

package net.minecraft.src;

public class FCClientModelPig extends ModelPig
{
    private float m_fHeadRotation;
    
    public FCClientModelPig()
    {
        this( 0F );
    }

    public FCClientModelPig( float fScaleFactor )
    {
    	// scale factor appears to be used by saddled render pass model
    	
        super( fScaleFactor );
    }
    
    public void setLivingAnimations( EntityLiving entity, float par2, float par3, 
    	float fPartialTick )
    {
        super.setLivingAnimations( entity, par2, par3, fPartialTick );

        FCEntityPig pig = (FCEntityPig)entity;
        
        if ( !pig.isChild() )
        {
        	head.rotationPointY = 12F + pig.GetGrazeHeadVerticalOffset( fPartialTick ) * 4F;
        }
        else
        {        	
        	head.rotationPointY = 12F + pig.GetGrazeHeadVerticalOffset( fPartialTick ) * 2F;
        }
        	
        
        m_fHeadRotation = pig.GetGrazeHeadRotation( fPartialTick );
    }

    public void setRotationAngles( float par1, float par2, float par3, 
    	float par4, float par5, float par6, Entity entity )
    {
        super.setRotationAngles( par1, par2, par3, par4, par5, par6, entity );
        
        head.rotateAngleX = m_fHeadRotation;
    }
    
	//------------- Class Specific Methods ------------//
}
