//FCMOD

package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class FCClientGuiPulley extends GuiContainer
{
	static final int iPulleyGuiHeight = 174;
	static final int iPulleyMachineIconWidth = 14;
	static final int iPulleyMachineIconHeight = 14;

    private FCTileEntityPulley associatedTileEntityPulley;

	public FCClientGuiPulley( InventoryPlayer inventoryplayer, FCTileEntityPulley tileEntityPulley )
    {
        super( new FCContainerPulley( inventoryplayer, tileEntityPulley ) );
        
        ySize = iPulleyGuiHeight;
        
        associatedTileEntityPulley = tileEntityPulley;
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int i, int j )
    {
        fontRenderer.drawString( "Pulley", 75, 6, 0x404040 );
        fontRenderer.drawString( "Inventory", 8, ( ySize - 96 ) + 2, 0x404040 );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
    {
    	// draw the background image
    	
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        
        mc.renderEngine.bindTexture( "/btwmodtex/fcguipulley.png" );
        
        int xPos = ( width - xSize ) / 2;
        int yPos = ( height - ySize ) / 2;
        
        drawTexturedModalRect( xPos, yPos, 0, 0, xSize, ySize );
        
        // draw the machine indicator
        
        if ( associatedTileEntityPulley.m_iMechanicalPowerIndicator > 0 )
        {
            drawTexturedModalRect( xPos + 80,									// screen x pos 
            		yPos + 18, 													// screen y pos
            		176, 														// bitmap source x
            		0, 															// bitmap source y
            		iPulleyMachineIconWidth, 									// width
        			iPulleyMachineIconHeight );									// height
        }        
    }    
}