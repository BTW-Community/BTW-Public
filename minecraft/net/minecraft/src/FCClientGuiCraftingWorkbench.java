// FCMOD (client only)

package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FCClientGuiCraftingWorkbench extends GuiContainer
{
	private FCContainerWorkbench m_container;
	
    public FCClientGuiCraftingWorkbench( InventoryPlayer inventory, World world, int i, int j, int k )
    {
        super( new FCContainerWorkbench( inventory, world, i, j, k ) );
        
        m_container = (FCContainerWorkbench)inventorySlots;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        DrawSecondaryOutputIndicator();        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/gui/crafting.png");
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
    }
    
    //------------- Class Specific Methods ------------//
    
    private void DrawSecondaryOutputIndicator()
    {
        IRecipe recipe = CraftingManager.getInstance().FindMatchingRecipe(
        	m_container.craftMatrix, mc.theWorld );
        
        if ( recipe != null && recipe.HasSecondaryOutput() )
        {
	        Slot outputSlot = (Slot)m_container.inventorySlots.get( 0 );
	        
	        int iDisplayX = outputSlot.xDisplayPosition + 24;
	        int iDisplayY = outputSlot.yDisplayPosition + 5;
	        
	        FCClientUtilsRender.DrawSecondaryCraftingOutputIndicator( mc, iDisplayX, iDisplayY );
        }
    }    
}
