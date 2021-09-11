// FCMOD: Client only

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCTileEntityFurnaceBrickRenderer extends TileEntitySpecialRenderer
{
	private static final double m_dCookingItemVisualOffset = 0.25F;
	
    public void renderTileEntityAt( TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	FCTileEntityFurnaceBrick furnace = (FCTileEntityFurnaceBrick)tileEntity;
    	
    	ItemStack cookStack = furnace.GetCookStack();
    	
    	if ( cookStack != null )
    	{
    		RenderCookStack( furnace, xCoord, yCoord, zCoord, cookStack, fPartialTickCount );
    	}
    }
    
    private void RenderCookStack( FCTileEntityFurnaceBrick furnace, double xCoord, double yCoord, double zCoord, ItemStack stack, float fPartialTickCount )
    {
    	int iMetadata = furnace.worldObj.getBlockMetadata( furnace.xCoord, furnace.yCoord, furnace.zCoord );
    	
        EntityItem entityItem = new EntityItem( furnace.worldObj, 0.0D, 0.0D, 0.0D, stack );
        entityItem.getEntityItem().stackSize = 1;
        entityItem.hoverStart = 0.0F;
    	
        GL11.glPushMatrix();
        GL11.glTranslatef( (float)xCoord + 0.5F, (float)yCoord + ( 6.5F / 16F ), (float)zCoord + 0.5F );        
        
    	GL11.glScalef( 0.7F, 0.7F, 0.7F );
        
    	int iFacing = iMetadata & 7;
		Vec3 vOffset = Vec3.createVectorHelper( 0.0D, 0.0D, 0.0D );
    	float fYaw = 0F;
    	
    	if ( iFacing == 2 )
    	{
    		fYaw = 0F;
    		vOffset.zCoord = -m_dCookingItemVisualOffset;
    	}
    	else if ( iFacing == 3 )
    	{
    		fYaw = 180F;
    		vOffset.zCoord = m_dCookingItemVisualOffset;
    	}
    	else if ( iFacing == 4 )
    	{
    		fYaw = 90F;
    		vOffset.xCoord = -m_dCookingItemVisualOffset;
    	}
    	else if ( iFacing == 5 )
    	{
    		fYaw = 270F;
    		vOffset.xCoord = m_dCookingItemVisualOffset;
    	}
        	
        // move the item towards the front
        
    	GL11.glTranslatef( (float)vOffset.xCoord, (float)vOffset.yCoord, 
    		(float)vOffset.zCoord );
    	
        if ( RenderManager.instance.options.fancyGraphics )
        {        	
            // don't rotate items rendered as billboards
            
        	GL11.glRotatef( fYaw, 0.0F, 1.0F, 0.0F);
        }
        
    	// furnaces get item lighting from block in front
    	
    	int iBrightness = GetItemRenderBrightnessForBlockToFacing( furnace, iFacing );
    	
        int var11 = iBrightness % 65536;
        int var12 = iBrightness / 65536;
        
        OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, (float)var11 / 1F, 
        	(float)var12 / 1F);
        
        GL11.glColor4f( 1F, 1F, 1F, 1F );
        
        RenderManager.instance.renderEntityWithPosYaw(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

        GL11.glPopMatrix();        
    }

    protected int GetItemRenderBrightnessForBlockToFacing( FCTileEntityFurnaceBrick furnace, 
    	int iFacing )
    {
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( 
    		furnace.xCoord, furnace.yCoord, furnace.zCoord, iFacing );
	
	    if ( furnace.worldObj.blockExists( targetPos.i, targetPos.j, targetPos.k ) )
	    {
	        return furnace.worldObj.getLightBrightnessForSkyBlocks(
	        	targetPos.i, targetPos.j, targetPos.k, 0 );
	    }
	    
        return 0;
    }
}
