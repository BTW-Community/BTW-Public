// FCMOD

package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FCClientRenderCanvas extends Render
{
    /** RNG. */
    private Random rand;

    public FCClientRenderCanvas()
    {
        rand = new Random();
    }

    public void func_158_a( FCEntityCanvas canvas, double par2, double par4, double par6, float par8, float par9)
    {
        rand.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        loadTexture("/btwmodtex/btwart01.png");
        FCEnumCanvasArt enumart = canvas.m_art;
        float f = 0.0625F;
        GL11.glScalef(f, f, f);
        func_159_a(canvas, enumart.m_iSizeX, enumart.m_iSizeY, enumart.m_iOffsetX, enumart.m_iOffsetY);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void func_159_a( FCEntityCanvas canvas, int par2, int par3, int par4, int par5)
    {
        float f = (float)(-par2) / 2.0F;
        float f1 = (float)(-par3) / 2.0F;
        float f2 = -0.5F;
        float f3 = 0.5F;

        // uv coordinates for wood backing
        /*
        float f12 = 0.75F;
        float f13 = 0.8125F;
        float f14 = 0.0F;
        float f15 = 0.0625F;
        
        float f16 = 0.75F;
        float f17 = 0.8125F;
        float f18 = 0.001953125F;
        float f19 = 0.001953125F;
        float f20 = 0.7519531F;
        float f21 = 0.7519531F;
        float f22 = 0.0F;
        float f23 = 0.0625F;
        */
        float f12 = 0.0F;
        float f13 = f12 + ( 1F / 16F );
        float f14 = 13F / 16F;
        float f15 = f14 + ( 1F / 16F );
        
        float f16 = f12;
        float f17 = f13;
        float f18 = f14 + 0.001953125F;
        float f19 = f14 + 0.001953125F;
        float f20 = f12 + 0.001953125F;
        float f21 = f12 + 0.001953125F;
        float f22 = f14;
        float f23 = f14 + 0.0625F;
        

        for (int i = 0; i < par2 / 16; i++)
        {
            for (int j = 0; j < par3 / 16; j++)
            {
                float f4 = f + (float)((i + 1) * 16);
                float f5 = f + (float)(i * 16);
                float f6 = f1 + (float)((j + 1) * 16);
                float f7 = f1 + (float)(j * 16);
                
                func_160_a(canvas, (f4 + f5) / 2.0F, (f6 + f7) / 2.0F);
                
                float f8 = (float)((par4 + par2) - i * 16) / 256F;
                float f9 = (float)((par4 + par2) - (i + 1) * 16) / 256F;
                float f10 = (float)((par5 + par3) - j * 16) / 256F;
                float f11 = (float)((par5 + par3) - (j + 1) * 16) / 256F;
                
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                
                // front face
                
                tessellator.setNormal(0.0F, 0.0F, -1F);
                
                tessellator.addVertexWithUV(f4, f7, f2, f9, f10);
                tessellator.addVertexWithUV(f5, f7, f2, f8, f10);
                tessellator.addVertexWithUV(f5, f6, f2, f8, f11);
                tessellator.addVertexWithUV(f4, f6, f2, f9, f11);
                
                // back face
                
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                
                tessellator.addVertexWithUV(f4, f6, f3, f12, f14);
                tessellator.addVertexWithUV(f5, f6, f3, f13, f14);
                tessellator.addVertexWithUV(f5, f7, f3, f13, f15);
                tessellator.addVertexWithUV(f4, f7, f3, f12, f15);
                
                // edges
                
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                
                tessellator.addVertexWithUV(f4, f6, f2, f16, f18);
                tessellator.addVertexWithUV(f5, f6, f2, f17, f18);
                tessellator.addVertexWithUV(f5, f6, f3, f17, f19);
                tessellator.addVertexWithUV(f4, f6, f3, f16, f19);
                
                tessellator.setNormal(0.0F, -1F, 0.0F);
                
                tessellator.addVertexWithUV(f4, f7, f3, f16, f18);
                tessellator.addVertexWithUV(f5, f7, f3, f17, f18);
                tessellator.addVertexWithUV(f5, f7, f2, f17, f19);
                tessellator.addVertexWithUV(f4, f7, f2, f16, f19);
                
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                
                tessellator.addVertexWithUV(f4, f6, f3, f21, f22);
                tessellator.addVertexWithUV(f4, f7, f3, f21, f23);
                tessellator.addVertexWithUV(f4, f7, f2, f20, f23);
                tessellator.addVertexWithUV(f4, f6, f2, f20, f22);
                
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                
                tessellator.addVertexWithUV(f5, f6, f2, f21, f22);
                tessellator.addVertexWithUV(f5, f7, f2, f21, f23);
                tessellator.addVertexWithUV(f5, f7, f3, f20, f23);
                tessellator.addVertexWithUV(f5, f6, f3, f20, f22);
                
                tessellator.draw();
            }
        }
    }

    private void func_160_a( FCEntityCanvas canvas, float par2, float par3)
    {
        int i = MathHelper.floor_double(canvas.posX);
        int j = MathHelper.floor_double(canvas.posY + (double)(par3 / 16F));
        int k = MathHelper.floor_double(canvas.posZ);

        if (canvas.direction == 0)
        {
            i = MathHelper.floor_double(canvas.posX + (double)(par2 / 16F));
        }

        if (canvas.direction == 1)
        {
            k = MathHelper.floor_double(canvas.posZ - (double)(par2 / 16F));
        }

        if (canvas.direction == 2)
        {
            i = MathHelper.floor_double(canvas.posX - (double)(par2 / 16F));
        }

        if (canvas.direction == 3)
        {
            k = MathHelper.floor_double(canvas.posZ + (double)(par2 / 16F));
        }

        int l = renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 0x10000;
        int j1 = l / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        func_158_a((FCEntityCanvas)par1Entity, par2, par4, par6, par8, par9);
    }
}
