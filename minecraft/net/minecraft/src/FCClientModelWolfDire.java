// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientModelWolfDire extends ModelBase
{
    public ModelRenderer m_modelHeadMain;
    public ModelRenderer m_modelBody;
    public ModelRenderer m_modelLeg1;
    public ModelRenderer m_modelLeg2;
    public ModelRenderer m_modelLeg3;
    public ModelRenderer m_modelLeg4;
    ModelRenderer m_modelTail;
    ModelRenderer m_modelMane;

    private float m_fHeadRotation;
    
    public FCClientModelWolfDire()
    {
        float var1 = 0.0F;
        float var2 = 13.5F;
        
        this.m_modelHeadMain = new ModelRenderer(this, 0, 0);
        this.m_modelHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, var1);
        this.m_modelHeadMain.setRotationPoint(-1.0F, var2, -7.0F);
        this.m_modelBody = new ModelRenderer(this, 18, 14);
        this.m_modelBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, var1);
        this.m_modelBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.m_modelMane = new ModelRenderer(this, 21, 0);
        this.m_modelMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, var1);
        this.m_modelMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        this.m_modelLeg1 = new ModelRenderer(this, 0, 18);
        this.m_modelLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.m_modelLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.m_modelLeg2 = new ModelRenderer(this, 0, 18);
        this.m_modelLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.m_modelLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.m_modelLeg3 = new ModelRenderer(this, 0, 18);
        this.m_modelLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.m_modelLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.m_modelLeg4 = new ModelRenderer(this, 0, 18);
        this.m_modelLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.m_modelLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.m_modelTail = new ModelRenderer(this, 9, 18);
        this.m_modelTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.m_modelTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        
        this.m_modelHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, var1);
        this.m_modelHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, var1);
        this.m_modelHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, var1);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

        this.m_modelHeadMain.renderWithRotation(par7);
        this.m_modelBody.render(par7);
        this.m_modelLeg1.render(par7);
        this.m_modelLeg2.render(par7);
        this.m_modelLeg3.render(par7);
        this.m_modelLeg4.render(par7);
        this.m_modelTail.renderWithRotation(par7);
        this.m_modelMane.render(par7);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        FCEntityWolfDire var5 = (FCEntityWolfDire)par1EntityLiving;

        this.m_modelTail.rotateAngleY = 0.0F;

        this.m_modelBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.m_modelBody.rotateAngleX = ((float)Math.PI / 2F);
        this.m_modelMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
        this.m_modelMane.rotateAngleX = this.m_modelBody.rotateAngleX;
        this.m_modelTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        this.m_modelLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.m_modelLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.m_modelLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.m_modelLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.m_modelLeg1.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;
        this.m_modelLeg2.rotateAngleX = MathHelper.cos(par2 * 0.6662F + (float)Math.PI) * 1.4F * par3;
        this.m_modelLeg3.rotateAngleX = MathHelper.cos(par2 * 0.6662F + (float)Math.PI) * 1.4F * par3;
        this.m_modelLeg4.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;

        m_modelHeadMain.rotationPointY = 13.5F + ((FCEntityWolfDire)par1EntityLiving).GetHeadRotationPointOffset(par4) * 5.0F;
        m_fHeadRotation = ((FCEntityWolfDire)par1EntityLiving).GetHeadRotation(par4);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        this.m_modelHeadMain.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.m_modelHeadMain.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.m_modelTail.rotateAngleX = par3;
        
        m_modelHeadMain.rotateAngleX = m_fHeadRotation;
    }    
}
