// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientRenderMovingAnchor extends Render
{
    private RenderBlocks localRenderBlocks;
    
    public FCClientRenderMovingAnchor()
    {
    	localRenderBlocks = new RenderBlocks();
        shadowSize = 0.0F;
    }
    
    @Override
    public void doRender( Entity entity, double x, double y, double z, 
            float fYaw, float renderPartialTicks ) // the last parameter comes from the client's timer
    {
        World world = entity.worldObj;
        
    	localRenderBlocks.blockAccess = world;
    	
        GL11.glPushMatrix();
        
        GL11.glTranslatef( (float)x, (float)y, (float)z );
        
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        
        int i = MathHelper.floor_double( entity.posX );
        int j = MathHelper.floor_double( entity.posY );
        int k = MathHelper.floor_double( entity.posZ );
        
        this.loadTexture("/terrain.png");
        
        Block block = FCBetterThanWolves.fcAnchor;
  
        // render the base
   
        double fHalfLength = 0.50F;
        double fHalfWidth = 0.50F;
        double dBlockHeight = FCBlockAnchor.m_dAnchorBaseHeight;
        
        localRenderBlocks.setRenderBounds( 0.5F - fHalfWidth, 0, 0.5F - fHalfLength, 
    		0.5F + fHalfWidth, dBlockHeight, 0.5F + fHalfLength );
        
        FCClientUtilsRender.RenderMovingBlockWithMetadata( localRenderBlocks, block, 
    		world, i, j, k, 1 );
  
        // render the loop
        
        fHalfLength = 0.125F;
        fHalfWidth = 0.125F;
        dBlockHeight = 0.25F;
        
        localRenderBlocks.setRenderBounds( 0.5F - fHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight, 0.5F - fHalfLength, 
    		0.5F + fHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight + dBlockHeight, 0.5F + fHalfLength );
        
        FCClientUtilsRender.RenderMovingBlockWithTexture( localRenderBlocks, block, 
    		world, i, j, k,
    		((FCBlockAnchor)FCBetterThanWolves.fcAnchor).m_IconNub );  // Metal texture for the loop)
        
        // render the rope

        if ( world.getBlockId( i, j, k ) != FCBetterThanWolves.fcRopeBlock.blockID )
        {
            
	        fHalfLength = FCBlockRope.m_dRopeWidth * 0.499F;
	        fHalfWidth = FCBlockRope.m_dRopeWidth * 0.499F;
	        
	        double yOffset = 1.0D - ( entity.posY - (double)j );
	        
	        //fBlockHeight = 1.0f;
	        
	        localRenderBlocks.setRenderBounds( 0.5F - fHalfWidth, FCBlockAnchor.m_dAnchorBaseHeight, 0.5F - fHalfLength, 
	    		0.5F + fHalfWidth, (float)( yOffset + 1.0D )/*1.99F*/, 0.5F + fHalfLength );
	        
	        FCClientUtilsRender.RenderMovingBlockWithTexture( localRenderBlocks, block, 
	    		world, i, j, k,
	    		((FCBlockRope)FCBetterThanWolves.fcRopeBlock).blockIcon );	        
        }
        
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        
        GL11.glPopMatrix();
    }
}