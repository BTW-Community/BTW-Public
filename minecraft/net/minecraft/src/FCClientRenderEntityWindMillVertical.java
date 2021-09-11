//FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientRenderEntityWindMillVertical extends Render
{
    protected FCClientModelWindMillVerticalShafts m_ModelShafts;
    protected FCClientModelWindMillVerticalSails m_ModelSails;
    protected FCClientModelWindMillVerticalFrame m_ModelFrame;
    
    public FCClientRenderEntityWindMillVertical()
    {
        shadowSize = 0.0F;
        
        m_ModelShafts = new FCClientModelWindMillVerticalShafts();
        m_ModelSails = new FCClientModelWindMillVerticalSails();
        m_ModelFrame = new FCClientModelWindMillVerticalFrame();
    }
    
    @Override
    public void doRender( Entity entity, double x, double y, double z, 
            float fYaw, float renderPartialTicks ) // the last parameter comes from the client's timer
    {
    	// do not render unless the chunks around are loaded
    	
    	int i = MathHelper.floor_double( entity.posX );
    	int j = MathHelper.floor_double( entity.posY );
    	int k = MathHelper.floor_double( entity.posZ );
    	
    	if ( !entity.worldObj.checkChunksExist( i - 16, j - 16, k - 16, i + 16, j + 16, k + 16 ) )
    	{
    		return;
    	}
    	
    	FCEntityWindMillVertical entityWindMill = (FCEntityWindMillVertical)entity;
    	
        // rock the wind mill for damage
        float deltaTime = (float)entityWindMill.m_iTimeSinceHit - renderPartialTicks;
        float deltaDamage = (float)entityWindMill.m_iCurrentDamage - renderPartialTicks;
        float rotateForDamage = 0.0F;
        
        if ( deltaDamage < 0.0F)
        {
            deltaDamage = 0.0F;
        }
        
        if ( deltaDamage > 0.0F )
        {
        	rotateForDamage = ( (MathHelper.sin( deltaTime ) * deltaTime * deltaDamage) / 40F ) * 
    			(float)entityWindMill.m_iRockDirection;        	
        }
        
        GL11.glPushMatrix();
        GL11.glTranslatef( (float)x, (float)y, (float)z );
        
        GL11.glScalef( 1.0F, 1.0F, 1.0F );//(-1F, -1F, 1.0F);
        
        GL11.glRotatef( entityWindMill.m_fRotation, 0.0F, 1.0F, 0.0F);
        
        GL11.glRotatef( rotateForDamage, 0.0F, 1.0F, 0.0F);

        loadTexture("/btwmodtex/fcwindmillwoodver.png");
        
        m_ModelShafts.render( 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityWindMill );
        
        loadTexture("/btwmodtex/fcwindmillwoodhrz.png");
        
        m_ModelFrame.render( 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityWindMill );
        
        loadTexture("/btwmodtex/fcwindmillcloth.png");
        
        m_ModelSails.render( 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityWindMill );
        
        GL11.glPopMatrix();
    }    
}