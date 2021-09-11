package net.minecraft.src;

public class FCBlockComparator extends BlockComparator {
	public FCBlockComparator(int id, boolean powered) {
		super(id, powered);
		this.InitBlockBounds(0, 0, 0, 1, .125, 1);
	}
	
	//CLIENT ONLY
	public boolean RenderBlock(RenderBlocks render, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
        var5.setBrightness(this.getMixedBrightnessForBlock(render.blockAccess, par2, par3, par4));
        var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        int var6 = render.blockAccess.getBlockMetadata(par2, par3, par4);
        int var7 = var6 & 3;
        double var8 = 0.0D;
        double var10 = -0.1875D;
        double var12 = 0.0D;
        double var14 = 0.0D;
        double var16 = 0.0D;
        Icon var18;

        if (this.func_94490_c(var6))
        {
            var18 = Block.torchRedstoneActive.getBlockTextureFromSide(0);
        }
        else
        {
            var10 -= 0.1875D;
            var18 = Block.torchRedstoneIdle.getBlockTextureFromSide(0);
        }

        switch (var7)
        {
            case 0:
                var12 = -0.3125D;
                var16 = 1.0D;
                break;

            case 1:
                var8 = 0.3125D;
                var14 = -1.0D;
                break;

            case 2:
                var12 = 0.3125D;
                var16 = -1.0D;
                break;

            case 3:
                var8 = -0.3125D;
                var14 = 1.0D;
        }

        render.renderTorchAtAngle(this, (double)par2 + 0.25D * var14 + 0.1875D * var16, (double)((float)par3 - 0.1875F), (double)par4 + 0.25D * var16 + 0.1875D * var14, 0.0D, 0.0D, var6);
        render.renderTorchAtAngle(this, (double)par2 + 0.25D * var14 + -0.1875D * var16, (double)((float)par3 - 0.1875F), (double)par4 + 0.25D * var16 + -0.1875D * var14, 0.0D, 0.0D, var6);
        render.setOverrideBlockTexture(var18);
        render.renderTorchAtAngle(this, (double)par2 + var8, (double)par3 + var10, (double)par4 + var12, 0.0D, 0.0D, var6);
        render.clearOverrideBlockTexture();
        render.setRenderBounds(this.GetFixedBlockBoundsFromPool());
        render.renderBlockRedstoneLogicMetadata(this, par2, par3, par4, var7);
        return true;
	}
}