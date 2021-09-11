// FCMOD (client only)

package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FCTileEntityBasketRenderer extends TileEntitySpecialRenderer
{
    protected RenderBlocks m_localRenderBlocks = new RenderBlocks();

    public void renderTileEntityAt( TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	FCTileEntityBasket basket = (FCTileEntityBasket)tileEntity;
    	
    	Block block = Block.blocksList[basket.worldObj.getBlockId( 
    		basket.xCoord, basket.yCoord, basket.zCoord )];
    	
    	// it looks like it's possible for the block and tile entity to get out of sync during
    	// rendering, hence the instanceof test
    	
    	if ( block instanceof FCBlockBasket )
    	{
    		RenderBasketLidAsBlock( basket, (FCBlockBasket)block, xCoord, yCoord, zCoord, 
    			fPartialTickCount );
    	}
    }
    
	//------------- Class Specific Methods ------------//
	
    private void RenderBasketLidAsBlock( FCTileEntityBasket tileEntity, FCBlockBasket block, 
    	double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	int iMetadata = tileEntity.worldObj.getBlockMetadata( tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord );
    	
    	// it looks like it's possible for the block and tile entity to get out of sync during
    	// rendering, hene the instanceof test
    	
    	if ( block.GetIsOpen( iMetadata ) )
    	{
	        int iFacing = block.GetFacing( iMetadata );
	
	        GL11.glPushMatrix();	
	        GL11.glTranslatef( (float)xCoord, (float)yCoord, (float)zCoord );	        
            GL11.glDisable( GL11.GL_LIGHTING );
	        
	        bindTextureByName( "/terrain.png" );
	        
            m_localRenderBlocks.blockAccess = tileEntity.worldObj;
            
            Tessellator.instance.startDrawingQuads();
            
            Tessellator.instance.setTranslation( -(double)tileEntity.xCoord, -(double)tileEntity.yCoord, -(double)tileEntity.zCoord );
            
            FCModelBlock  transformedModel = block.GetLidModel( iMetadata ).MakeTemporaryCopy();
            
	    	// all rotation needs to be done through the block model so that the block renderer will know which face is
	    	// oriented towards which direction for lighting
	    	
			transformedModel.RotateAroundJToFacing( iFacing );
            
			m_localRenderBlocks.SetUvRotateTop( block.ConvertFacingToTopTextureRotation( iFacing ) );
			m_localRenderBlocks.SetUvRotateBottom( block.ConvertFacingToBottomTextureRotation( iFacing ) );
			
            block.m_iOpenLidBrightness = block.getMixedBrightnessForBlock( 
            		tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord );
            
            block.m_bRenderingOpenLid = true;

			transformedModel.RenderAsBlockWithColorMultiplier( m_localRenderBlocks, 
				block, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord );
	        
            block.m_bRenderingOpenLid = false;
            
	        Tessellator.instance.setTranslation( 0D, 0D, 0D );
	        
	        float fOpenRatio = GetCurrentOpenRatio( tileEntity, fPartialTickCount );
	        float fLidAngle = fOpenRatio * 45F;
	        
	        Vec3 lidRotationPoint = Vec3.createVectorHelper( block.GetLidRotationPoint() );
	        
	        lidRotationPoint.RotateAsBlockPosAroundJToFacing( iFacing );
	        
	        GL11.glTranslatef( (float)lidRotationPoint.xCoord, (float)lidRotationPoint.yCoord, (float)lidRotationPoint.zCoord );
	        
	        if ( iFacing == 2 )
	        {
		        GL11.glRotatef( fLidAngle, 1F, 0F, 0F );
	        }
	        else if ( iFacing == 3 )
	        {
		        GL11.glRotatef( -fLidAngle, 1F, 0F, 0F );
	        }
	        else if ( iFacing == 4 )
	        {
		        GL11.glRotatef( -fLidAngle, 0F, 0F, 1F );
	        }
	        else if ( iFacing == 5 )
	        {
		        GL11.glRotatef( fLidAngle, 0F, 0F, 1F );
	        }
	        
	        GL11.glTranslatef( -(float)lidRotationPoint.xCoord, -(float)lidRotationPoint.yCoord, -(float)lidRotationPoint.zCoord );	        
	        
	        Tessellator.instance.draw();
    
	        m_localRenderBlocks.ClearUvRotation();
			
            GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glPopMatrix();
    	}
    }
    
    protected float GetCurrentOpenRatio( FCTileEntityBasket basket, float fPartialTickCount )
    {
        float fOpenRatio = basket.m_fPrevLidOpenRatio + ( basket.m_fLidOpenRatio - basket.m_fPrevLidOpenRatio ) * fPartialTickCount;
        
        fOpenRatio = 1.0F - fOpenRatio;
        fOpenRatio = 1.0F - fOpenRatio * fOpenRatio * fOpenRatio;
        
        return fOpenRatio;        
    }
}
