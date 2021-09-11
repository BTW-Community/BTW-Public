// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientRenderMiningCharge extends Render
{
    private RenderBlocks blockRenderer;
    
    public FCClientRenderMiningCharge()
    {
        blockRenderer = new RenderBlocks();
        shadowSize = 0.5F;
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
    	RenderMiningCharge( (FCEntityMiningCharge)entity, d, d1, d2, f, f1);
    }

    //************* Class Specific Methods ************//
    
    public void RenderMiningCharge( FCEntityMiningCharge miningCharge, double d, double d1, double d2, 
            float f, float f1 )
    {
    	int iFacing = miningCharge.m_iFacing;
    	
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        
        if(((float)miningCharge.m_iFuse - f1) + 1.0F < 10F)
        {
            float fScaleFactor = 1.0F - (((float)miningCharge.m_iFuse - f1) + 1.0F) / 10F;
            
            if(fScaleFactor < 0.0F)
            {
                fScaleFactor = 0.0F;
            }
            if(fScaleFactor > 1.0F)
            {
                fScaleFactor = 1.0F;
            }
            
            fScaleFactor *= fScaleFactor;
            fScaleFactor *= fScaleFactor;
            
            float fScale = 1.0F + fScaleFactor * 0.3F;
            
            GL11.glScalef( fScale, fScale, fScale );
        }
        
        float f3 = (1.0F - (((float)miningCharge.m_iFuse - f1) + 1.0F) / 100F) * 0.8F;
        
        this.loadTexture("/terrain.png");
        
        RenderMiningChargeBlock( iFacing, miningCharge.getBrightness(f1));
        
        if((miningCharge.m_iFuse / 5) % 2 == 0)
        {
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDisable(2896 /*GL_LIGHTING*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 772);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
            RenderMiningChargeBlock(  iFacing, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glEnable(2896 /*GL_LIGHTING*/);
            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        }
        
        GL11.glPopMatrix();
    }
    
    public void RenderMiningChargeBlock( int iFacing, float fColorMultiplier )
    {
    	FCBlockMiningCharge block = (FCBlockMiningCharge)FCBetterThanWolves.fcBlockMiningCharge;
    	
        Tessellator tessellator = Tessellator.instance;
        
        int iRenderColor = block.getRenderColor( 0 );
        
        float fRed = (float)(iRenderColor >> 16 & 0xff) / 255F;
        float fGreen = (float)(iRenderColor >> 8 & 0xff) / 255F;
        float fBlue = (float)(iRenderColor & 0xff) / 255F;
        
        GL11.glColor4f( fRed * fColorMultiplier, fGreen * fColorMultiplier, fBlue * fColorMultiplier, 1.0F );
        
        SetRenderBoundsBasedOnFacing( iFacing );
        
        int iMetaData = iFacing;
        
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        blockRenderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromMetadataCustom(0, iMetaData));        
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        blockRenderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromMetadataCustom(1, iMetaData));        
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        blockRenderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromMetadataCustom(2, iMetaData));        
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        blockRenderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromMetadataCustom(3, iMetaData));        
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        blockRenderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromMetadataCustom(4, iMetaData));        
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        blockRenderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromMetadataCustom(5, iMetaData));        
        tessellator.draw();
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    
    void SetRenderBoundsBasedOnFacing( int iFacing )
    {
    	FCBlockMiningCharge block = (FCBlockMiningCharge)(FCBetterThanWolves.fcBlockMiningCharge);
    	double dBoundingBoxHeight = FCBlockMiningCharge.m_dBoundingBoxHeight; 

        switch ( iFacing )
        {
	        case 0:
	        	
	        	blockRenderer.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	            		1.0F, dBoundingBoxHeight, 1.0F );
	        	
	        	break;
	        	
	        case 1:
	        	
	        	blockRenderer.setRenderBounds( 0.0F, dBoundingBoxHeight, 0.0F, 
	            		1.0F, 1.0F, 1.0F );
	        	
	        	break;
	        	
	        case 2:
	        	
	        	blockRenderer.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	            		1.0F, 1.0F, dBoundingBoxHeight );
	        	
	        	break;
	        	
	        case 3:
	        	
	        	blockRenderer.setRenderBounds( 0.0F, 0.0F, dBoundingBoxHeight, 
	            		1.0F, 1.0F, 1.0F );
	        	
	        	break;
	        	
	        case 4:
	        	
	        	blockRenderer.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	        			dBoundingBoxHeight, 1.0F, 1.0F );
	        	
	        	break;
	        	
	        default: // 5
	        	
	        	blockRenderer.setRenderBounds( dBoundingBoxHeight, 0.0F, 0.0F, 
	            		1.0F, 1.0F, 1.0F );
	        	
	        	break;
        }
    }
}