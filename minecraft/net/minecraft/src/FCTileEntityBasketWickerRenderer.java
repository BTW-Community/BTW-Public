// FCMOD (client only)

package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FCTileEntityBasketWickerRenderer extends FCTileEntityBasketRenderer
{
    private final float m_fStorageItemMaxHeight = 0.25F;
    
    public void renderTileEntityAt( TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	super.renderTileEntityAt( tileEntity, xCoord, yCoord, zCoord, fPartialTickCount );
    	
    	FCTileEntityBasketWicker basket = (FCTileEntityBasketWicker)tileEntity;
    	
		RenderStorageStack( basket, xCoord, yCoord, zCoord, fPartialTickCount );
    }
    
	//------------- Class Specific Methods ------------//
	
    private void RenderStorageStack( FCTileEntityBasketWicker basket, double xCoord, double yCoord, double zCoord, float fPartialTickCount )
    {
    	ItemStack stack = basket.GetStorageStack();
    	
    	if ( stack != null && basket.m_fLidOpenRatio > 0.01F )
    	{
	    	int iMetadata = basket.worldObj.getBlockMetadata( basket.xCoord, basket.yCoord, basket.zCoord );
	    	
	        EntityItem entity = new EntityItem( basket.worldObj, 0.0D, 0.0D, 0.0D, stack );
	        
	        entity.hoverStart = 0.0F;
	    	
	        GL11.glPushMatrix();
	        
	        float fCurrentItemHeight = m_fStorageItemMaxHeight * GetCurrentOpenRatio( basket, fPartialTickCount );
	        	
	        GL11.glTranslatef( (float)xCoord + 0.5F, (float)yCoord + fCurrentItemHeight, (float)zCoord + 0.5F );        
	        
            // don't rotate items rendered as billboards (fancyGraphics test)
            
	        if ( RenderManager.instance.options.fancyGraphics )
	        {        	
	        	float fYaw = ConvertFacingToYaw( FCBetterThanWolves.fcBlockBasketWicker.GetFacing( iMetadata ) );
	        	
	        	GL11.glRotatef( fYaw, 0.0F, 1.0F, 0.0F);
	        }
	
            
	        if ( DoesStackRenderAsBlock( stack ) && stack.stackSize > 4 )
        	{
	        	// move large block stacks towards the front to avoid overlapping with lid
	        	
	        	GL11.glTranslatef( 0F, 0F, -0.15F );
        	}
	        
	        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
	
	        GL11.glPopMatrix();
    	}
    }
    
    private float ConvertFacingToYaw( int iFacing )
    {
    	float fYaw = 0;
    	
    	if ( iFacing == 3 )
    	{
    		fYaw = 180F;
    	}
    	else if ( iFacing == 4 )
    	{
    		fYaw = 90F;
    	}
    	else if ( iFacing == 5 )
    	{
    		fYaw = 270F;
    	}
    	
    	return fYaw;
    }
    
    private boolean DoesStackRenderAsBlock( ItemStack stack )
    {
        return stack.getItemSpriteNumber() == 0 && Block.blocksList[stack.itemID] != null && 
        	Block.blocksList[stack.itemID].DoesItemRenderAsBlock( stack.getItemDamage() );
    }
}
