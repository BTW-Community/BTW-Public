// FCMOD: Client Only

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCTileEntityToolPlacedRenderer extends TileEntitySpecialRenderer
{
    public void renderTileEntityAt( TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	FCTileEntityToolPlaced tileEntityTool = (FCTileEntityToolPlaced)tileEntity;
    	
    	ItemStack toolStack = tileEntityTool.GetToolStack();
    	
    	if ( toolStack != null )
    	{
    		RenderToolStack( tileEntityTool, toolStack, xCoord, yCoord, zCoord );
    	}
    }
    
    private void RenderToolStack( FCTileEntityToolPlaced tileEntityTool, ItemStack toolStack, double xCoord, double yCoord, double zCoord )
    {
    	int iMetadata = tileEntityTool.worldObj.getBlockMetadata( tileEntityTool.xCoord, tileEntityTool.yCoord, tileEntityTool.zCoord );
    	
    	int iFacing = FCBetterThanWolves.fcBlockToolPlaced.GetFacing( iMetadata );
    	int iFacingLevel = FCBetterThanWolves.fcBlockToolPlaced.GetVerticalOrientation( iMetadata );
    	
        EntityItem entityTool = new EntityItem( tileEntityTool.worldObj, 0.0D, 0.0D, 0.0D, toolStack );
        FCItemTool toolItem = (FCItemTool)toolStack.getItem();
        
        entityTool.getEntityItem().stackSize = 1;
        entityTool.hoverStart = 0F;
    	
        GL11.glPushMatrix();
        
        float fVerticalOffset = toolItem.GetVisualVerticalOffsetAsBlock();
        float fHorizontalOffset = toolItem.GetVisualHorizontalOffsetAsBlock();
        
        float fXOffset = 0.5F;
        float fZOffset = 0.5F;
        float fYOffset = 0.5F;
        
        if ( iFacingLevel == 2 )
        {
        	if ( iFacing == 2 )
        	{
        		fZOffset = fVerticalOffset;
        	}
        	else if ( iFacing == 3 )
        	{
        		fZOffset = 1 - fVerticalOffset;
        	}
        	else if ( iFacing == 4 )
        	{
        		fXOffset = fVerticalOffset;
        	}
        	else if ( iFacing == 5 )
        	{
        		fXOffset = 1 - fVerticalOffset;
        	}
        	
        	fYOffset = fHorizontalOffset;
        }
        else if ( iFacingLevel == 1 )
        {
        	fYOffset = 1F - fVerticalOffset;
        	
        	if ( iFacing == 2 )
        	{
        		fZOffset = fHorizontalOffset;
        	}
        	else if ( iFacing == 3 )
        	{
        		fZOffset = 1 - fHorizontalOffset;
        	}
        	else if ( iFacing == 4 )
        	{
        		fXOffset = fHorizontalOffset;
        	}
        	else if ( iFacing == 5 )
        	{
        		fXOffset = 1 - fHorizontalOffset;
        	}
        }
        else // iFacingLevel == 0
        {
        	fYOffset = fVerticalOffset;
        	
        	if ( iFacing == 2 )
        	{
        		fZOffset = 1 - fHorizontalOffset;
        	}
        	else if ( iFacing == 3 )
        	{
        		fZOffset = fHorizontalOffset;
        	}
        	else if ( iFacing == 4 )
        	{
        		fXOffset = 1 - fHorizontalOffset;
        	}
        	else if ( iFacing == 5 )
        	{
        		fXOffset = fHorizontalOffset;
        	}
        }	        
        
        GL11.glTranslatef( (float)xCoord + fXOffset, (float)yCoord + fYOffset, (float)zCoord + fZOffset );        
        GL11.glScalef( 2F, 2F, 2F);
        
        float fPitch = 0F;
        float fRoll = 0F;

        if ( iFacing == 2 )
        {
        	fPitch = 90F;
        }
        if ( iFacing == 3 )
        {
        	fPitch = 270F;
        }
        else if ( iFacing == 4 )
        {
        	fPitch = 180F;
        }
        else if ( iFacing == 5 )
        {
        	fPitch = 0F;
        }

        if ( iFacingLevel == 0 )
        {
        	fRoll = 180F;
        }
        else if ( iFacingLevel == 2 )
        {
        	fRoll = 270F;
        }
        
        fRoll += toolItem.GetVisualRollOffsetAsBlock();
        
    	GL11.glRotatef( fPitch, 0F, 1F, 0F );
    	GL11.glRotatef( fRoll, 0F, 0F, 1F );
    	
        RenderItem.m_bForceFancyItemRender = true;
        
        RenderManager.instance.renderEntityWithPosYaw( entityTool, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F );
        
        RenderItem.m_bForceFancyItemRender = false;

        GL11.glPopMatrix();        
    }
}
