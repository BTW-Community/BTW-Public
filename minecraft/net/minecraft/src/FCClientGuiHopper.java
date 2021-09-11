//FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientGuiHopper extends GuiContainer
{
	static final int iHopperGuiHeight = 193;
	static final int iHopperMachineIconHeight = 14;

    private FCTileEntityHopper associatedTileEntityHopper;

	public FCClientGuiHopper( InventoryPlayer inventoryplayer, FCTileEntityHopper tileentityHopper )
    {
        super( new FCContainerHopper( inventoryplayer, tileentityHopper ) );
        
        ySize = iHopperGuiHeight;
        
        associatedTileEntityHopper = tileentityHopper;
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int i, int j )
    {
        fontRenderer.drawString( "Hopper", 70, 6, 0x404040 );
        fontRenderer.drawString( "Inventory", 8, ( ySize - 96 ) + 2, 0x404040 );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
    {
    	// draw the background image
    	
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        
        mc.renderEngine.bindTexture( "/btwmodtex/fcHopper.png" );
        
        int xPos = ( width - xSize ) / 2;
        int yPos = ( height - ySize ) / 2;
        
        drawTexturedModalRect( xPos, yPos, 0, 0, xSize, ySize );
        
        // draw the machine indicator

        if ( associatedTileEntityHopper.m_iMechanicalPowerIndicator > 0 )
        {
            drawTexturedModalRect( xPos + 80,									// screen x pos 
        		yPos + 18, 														// screen y pos
        		176, 															// bitmap source x
        		0, 																// bitmap source y
        		14, 															// width
    			iHopperMachineIconHeight );										// height
        } 
    }    
}