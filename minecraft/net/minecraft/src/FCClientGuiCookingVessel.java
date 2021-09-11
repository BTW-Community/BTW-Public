// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientGuiCookingVessel extends GuiContainer
{
	static final int m_iGuiHeight = 193;
	static final int m_iFireIconHeight = 12;

    private FCTileEntityCookingVessel m_AssociatedTileEntity;
    private int m_iContainerID;

	public FCClientGuiCookingVessel( InventoryPlayer inventoryplayer, FCTileEntityCookingVessel tileEntity, int iContainerID )
    {
        super( new FCContainerCookingVessel( inventoryplayer, tileEntity ) );
        
        ySize = m_iGuiHeight;
        
        m_AssociatedTileEntity = tileEntity;
        m_iContainerID = iContainerID;
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int i, int j )
    {
    	if ( m_iContainerID == FCBetterThanWolves.fcCrucibleContainerID )
    	{
            fontRenderer.drawString( "Crucible", 66, 6, 0x404040 );    		
    	}
    	else
    	{
    		fontRenderer.drawString( "Cauldron", 66, 6, 0x404040 );
    	}
    	
        fontRenderer.drawString( "Inventory", 8, ( ySize - 96 ) + 2, 0x404040 );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	// draw the background image
    	
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        
        mc.renderEngine.bindTexture( "/btwmodtex/fccauldron.png" );
        
        int xPos = ( width - xSize ) / 2;
        int yPos = ( height - ySize ) / 2;
        
        drawTexturedModalRect( xPos, yPos, 0, 0, xSize, ySize );
        
        // draw the cooking indicator
        
        if ( m_AssociatedTileEntity.IsCooking() )
        {
            int scaledIconHeight = m_AssociatedTileEntity.getCookProgressScaled( m_iFireIconHeight );
            
            drawTexturedModalRect( xPos + 81,									// screen x pos 
            		yPos + 19 + m_iFireIconHeight - scaledIconHeight, 	// screen y pos
            		176, 														// bitmap source x
            		m_iFireIconHeight - scaledIconHeight, 				// bitmap source y
            		14, 														// width
            		scaledIconHeight + 2);										// height
        }        
    }    
}