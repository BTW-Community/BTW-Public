// FCMOD

package net.minecraft.src;

public class FCClientEntityCindersFX extends EntityFX
{
    private float m_fParticleScale;

    public FCClientEntityCindersFX( World par1World, double fXPos, double fYPos, double fZPos )
    {
        super( par1World, fXPos, fYPos, fZPos, 0D, 0D, 0D );
        
        //renderDistanceWeight = 100D;        
        
        motionX *= 1.5D;
        motionY *= 2D;
        motionZ *= 1.5D;
        
        motionY = rand.nextDouble() * 0.4D + 0.05D;
        
        particleRed = particleGreen = particleBlue = 1F;
        
        particleScale *= rand.nextFloat() * 2F + 0.2F;
        
        m_fParticleScale = particleScale;
        
        particleMaxAge = (int)( 16D / ( Math.random() * 0.8D + 0.2D ) );
        
        noClip = false;
        
        setParticleTextureIndex( 49 );
    }
    
    public int getBrightnessForRender( float fPartialTicks )
    {
        float fAgeFraction = ( (float)particleAge + fPartialTicks ) / (float)particleMaxAge;
        
        MathHelper.clamp_float( fAgeFraction, 0F, 1F );

        int iBrightnessHighBits = super.getBrightnessForRender( fPartialTicks ) >> 16 & 255;
        
        int iBrightnessLowBits = 240;
        
        return iBrightnessLowBits | iBrightnessHighBits << 16;
    }

    public float getBrightness( float fPartialTicks )
    {
        return 1.0F;
    }

    public void renderParticle( Tessellator tessellator, float fPartialTicks, float par3, float par4, float par5, float par6, float par7)
    {
        float fAgeFraction = ( (float)particleAge + fPartialTicks ) / (float)particleMaxAge;
        
        particleScale = m_fParticleScale * ( 1F - fAgeFraction * fAgeFraction );
        
        super.renderParticle( tessellator, fPartialTicks, par3, par4, par5, par6, par7 );
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if ( particleAge++ >= particleMaxAge )
        {
            setDead();
        }

        float fAgeFraction = (float)this.particleAge / (float)this.particleMaxAge;

        if ( rand.nextFloat() > fAgeFraction )
        {
            worldObj.spawnParticle( "smoke", posX, posY, posZ, motionX, motionY, motionZ );
        }

        this.motionY -= 0.03D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9990000128746033D;
        this.motionY *= 0.9990000128746033D;
        this.motionZ *= 0.9990000128746033D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
