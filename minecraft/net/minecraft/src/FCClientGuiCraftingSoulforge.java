// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientGuiCraftingSoulforge extends GuiContainer
{
	private FCContainerSoulforge m_container;
	
	static final int m_iGuiHeight = 184;
	
    /*
     * world, i, j, & k are only relevant on the server
     */
    public FCClientGuiCraftingSoulforge( InventoryPlayer inventoryplayer, World world, int i, int j, int k )
    {
        super( new FCContainerSoulforge( inventoryplayer, world, i, j, k ) );
        
        m_container = (FCContainerSoulforge)inventorySlots;
        
        ySize = m_iGuiHeight;
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int i, int j )
    {
        fontRenderer.drawString( "Soulforge", 22, 6, 0x404040 );
        fontRenderer.drawString( "Inventory", 8, (ySize - 96) + 2, 0x404040 );

        DrawSecondaryOutputIndicator();        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture( "/btwmodtex/fcguianvil.png" );
        
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        
        drawTexturedModalRect( xPos, yPos, 0, 0, xSize, ySize);
    }
    
    //------------- Class Specific Methods ------------//
    
    private void DrawSecondaryOutputIndicator()
    {
        IRecipe recipe = CraftingManager.getInstance().FindMatchingRecipe(
        	m_container.craftMatrix, mc.theWorld );
        
        if ( recipe != null && recipe.HasSecondaryOutput() )
        {
	        Slot outputSlot = (Slot)m_container.inventorySlots.get( 0 );
	        
	        int iDisplayX = outputSlot.xDisplayPosition + 26;
	        int iDisplayY = outputSlot.yDisplayPosition + 5;
	        
	        FCClientUtilsRender.DrawSecondaryCraftingOutputIndicator( mc, iDisplayX, iDisplayY );
        }
    }    
}