// FCMOD (client only)

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientGuiCraftingAnvil extends FCClientGuiCraftingWorkbench
{
    public FCClientGuiCraftingAnvil( InventoryPlayer invPlayer, World world, int i, int j, int k )
    {
    	super( invPlayer, world, i, j, k );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float par1, int par2, int par3 )
    {
        GL11.glColor4f( 1F, 1F, 1F, 1F);
        
        mc.renderEngine.bindTexture( "/btwmodtex/fcGuiAnvilVanilla.png" );
        
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        
        drawTexturedModalRect( xPos, yPos, 0, 0, xSize, ySize);
    }
}
