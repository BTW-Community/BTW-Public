// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientRenderDynamite extends Render
{
    public FCClientRenderDynamite()
    {
    }

    @Override
    public void doRender( Entity entity, double d, double d1, double d2, 
            float f, float f1 )
    {
        Tessellator tessellator = Tessellator.instance;
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        
        loadTexture("/gui/items.png");
        
        FCEntityDynamite entityDynamite = (FCEntityDynamite)entity;
        
        Icon itemIcon = Item.itemsList[entityDynamite.m_iItemShiftedIndex].itemIcon;
        
        double f2 = (double)itemIcon.getMinU();
        double f3 = (double)itemIcon.getMaxU();
        double f4 = (double)itemIcon.getMinV();
        double f5 = (double)itemIcon.getMaxV();
        
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0D, f2, f5);
        tessellator.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0D, f3, f5);
        tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
        tessellator.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0D, f2, f4);
        tessellator.draw();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
    }
}