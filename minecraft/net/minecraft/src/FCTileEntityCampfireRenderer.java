// FCMOD (client only)

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCTileEntityCampfireRenderer extends TileEntitySpecialRenderer
{
    public void renderTileEntityAt( TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	FCTileEntityCampfire campfire = (FCTileEntityCampfire)tileEntity;
    	
		RenderCookStack( campfire, xCoord, yCoord, zCoord );
    }
    
    private void RenderCookStack( FCTileEntityCampfire campfire, double xCoord, double yCoord, double zCoord )
    {
    	ItemStack stack = campfire.GetCookStack();
    	
    	if ( stack != null )
    	{
	    	int iMetadata = campfire.worldObj.getBlockMetadata( campfire.xCoord, campfire.yCoord, campfire.zCoord );
	    	boolean bIAligned = FCBetterThanWolves.fcBlockCampfireUnlit.GetIsIAligned( iMetadata );
	    	
	        EntityItem entity = new EntityItem( campfire.worldObj, 0.0D, 0.0D, 0.0D, stack );
	        
	        entity.getEntityItem().stackSize = 1;
	        entity.hoverStart = 0.0F;
	    	
	        GL11.glPushMatrix();
	        GL11.glTranslatef( (float)xCoord + 0.5F, (float)yCoord + ( 9F / 16F ), (float)zCoord + 0.5F );        
	        
	        if ( !bIAligned && RenderManager.instance.options.fancyGraphics )
	        {        	
	            // don't rotate items rendered as billboards (fancyGraphics test)
	            
	        	GL11.glRotatef( 90F, 0.0F, 1.0F, 0.0F);
	        }
	
	        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
	
	        GL11.glPopMatrix();
    	}
    }
}
