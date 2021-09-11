// FCMOD

package net.minecraft.src;

public class FCClientEntityWhiteSmokeFX extends EntityFX
{
    protected float m_fParticleScale;

    public FCClientEntityWhiteSmokeFX( World world, double fXPos, double fYPos, double fZPos, double fXVel, double fYVel, double fZVel )
    {
        super( world, fXPos, fYPos, fZPos, 0.0D, 0.0D, 0.0D );
        
        float f = 2.5F;
        
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;
        
        motionX += fXVel;
        motionY += fYVel;
        motionZ += fZVel;
        
        particleRed = particleGreen = particleBlue = 1.0F - (float)(Math.random() * 0.30000001192092896D);
        particleScale *= 0.75F;
        particleScale *= f;
        m_fParticleScale = particleScale;
        particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.29999999999999999D));
        particleMaxAge *= f;
        noClip = false;
    }

    public void renderParticle( Tessellator tessellator, float fPartialTicks, float par3, float par4, float par5, float par6, float par7)
    {
        float f = (((float)particleAge + fPartialTicks) / (float)particleMaxAge) * 32F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        particleScale = m_fParticleScale * f;
        super.renderParticle(tessellator, fPartialTicks, par3, par4, par5, par6, par7);
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }

        setParticleTextureIndex(7 - (particleAge * 8) / particleMaxAge);
        motionY += 0.0040000000000000001D;
        moveEntity(motionX, motionY, motionZ);

        if (posY == prevPosY)
        {
            motionX *= 1.1000000000000001D;
            motionZ *= 1.1000000000000001D;
        }

        motionX *= 0.95999997854232788D;
        motionY *= 0.95999997854232788D;
        motionZ *= 0.95999997854232788D;

        if (onGround)
        {
            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
        }
    }
}
