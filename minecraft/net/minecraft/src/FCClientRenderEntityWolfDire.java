// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientRenderEntityWolfDire extends RenderLiving
{
	private static final float m_fShadowSize = 0.5F;
	
    public FCClientRenderEntityWolfDire( ModelBase model, ModelBase renderPassModel )
    {
        super( model, m_fShadowSize );
        
        setRenderPassModel( model );
    }

    @Override
    protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2)
    {
        return this.getTailRotation( (FCEntityWolfDire)par1EntityLiving, par2 );
    }
    
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    	super.doRender( par1Entity, par2, par4, par6, par8, par9 );
    }
    
    @Override
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
    {
        GL11.glScalef(1.5F, 1.5F, 1.5F);
    }
    
    @Override
    protected int shouldRenderPass( EntityLiving entity, int iRenderPass, float par3)
    {
        return renderEyes( (FCEntityWolfDire)entity, iRenderPass );
    }
    
    //------------- Class Specific Methods ------------//
    
    protected float getTailRotation( FCEntityWolfDire wolf, float par2 )
    {
        return wolf.getTailRotation();
    }
    
    protected int renderEyes( FCEntityWolfDire wolf, int iRenderPass )
    {
        if (iRenderPass != 0)
        {
            return -1;
        }
        else
        {
            this.loadTexture("/btwmodtex/fcWolfDireEyes.png");
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_LIGHTING);

            if ( wolf.isInvisible() )
            {
                GL11.glDepthMask( false );
            }
            else
            {
                GL11.glDepthMask( true );
            }

            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            return 1;
        }
    }
}
