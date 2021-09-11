// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;

public class FCEntityCanvas extends Entity
	implements FCIEntityPacketHandler
{
    private int tickCounter1;

    /** the direction the painting faces */
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public FCEnumCanvasArt m_art;

    public FCEntityCanvas(World par1World)
    {
        super(par1World);
        tickCounter1 = 0;
        direction = 0;
        yOffset = 0.0F;
        setSize(0.5F, 0.5F);
    }

    public FCEntityCanvas(World par1World, int i, int j, int k, int iFacing )
    {
        this(par1World);
        xPosition = i;
        yPosition = j;
        zPosition = k;
        ArrayList arraylist = new ArrayList();
        FCEnumCanvasArt aenumart[] = FCEnumCanvasArt.values();
        int iNameLength = aenumart.length;

        for (int iTemp = 0; iTemp < iNameLength; iTemp++)
        {
            FCEnumCanvasArt enumart = aenumart[iTemp];
            m_art = enumart;
            func_412_b(iFacing);

            if (onValidSurface())
            {
                arraylist.add(enumart);
            }
        }

        if (arraylist.size() > 0)
        {
            m_art = (FCEnumCanvasArt)arraylist.get(rand.nextInt(arraylist.size()));
        }

        func_412_b(iFacing);
    }

    /*
     * Constructor using art ordinal specificall for use by the client
     */
    public FCEntityCanvas( World par1World, int i, int j, int k, int iFacing, int iArtOrdinal )
    {
        this(par1World);
        xPosition = i;
        yPosition = j;
        zPosition = k;
        FCEnumCanvasArt aenumart[] = FCEnumCanvasArt.values();

        m_art = aenumart[iArtOrdinal];

        func_412_b(iFacing);
    }
    
    public FCEntityCanvas(World par1World, int par2, int par3, int par4, int par5, String par6Str)
    {
        this(par1World);
        xPosition = par2;
        yPosition = par3;
        zPosition = par4;
        FCEnumCanvasArt aenumart[] = FCEnumCanvasArt.values();
        int i = aenumart.length;
        int j = 0;

        do
        {
            if (j >= i)
            {
                break;
            }

            FCEnumCanvasArt enumart = aenumart[j];

            if (enumart.m_sTitle.equals(par6Str))
            {
                m_art = enumart;
                break;
            }

            j++;
        }
        while (true);

        func_412_b(par5);
    }

    protected void entityInit()
    {
    }

    public void func_412_b(int iFacing)
    {
        direction = iFacing;
        prevRotationYaw = rotationYaw = iFacing * 90;
        float f = m_art.m_iSizeX;
        float f1 = m_art.m_iSizeY;
        float f2 = m_art.m_iSizeX;

        if (iFacing == 0 || iFacing == 2)
        {
            f2 = 0.5F;
        }
        else
        {
            f = 0.5F;
        }

        f /= 32F;
        f1 /= 32F;
        f2 /= 32F;
        float f3 = (float)xPosition + 0.5F;
        float f4 = (float)yPosition + 0.5F;
        float f5 = (float)zPosition + 0.5F;
        float f6 = 0.5625F;

        if (iFacing == 0)
        {
            f5 -= f6;
            f3 -= ComputeBlockOffset(m_art.m_iSizeX);
        }
        else if (iFacing == 1)
        {
            f3 -= f6;
            f5 += ComputeBlockOffset(m_art.m_iSizeX);
        }
        else if (iFacing == 2)
        {
            f5 += f6;
            f3 += ComputeBlockOffset(m_art.m_iSizeX);
        }
        else if (iFacing == 3)
        {
            f3 += f6;
            f5 -= ComputeBlockOffset(m_art.m_iSizeX);
        }
        
        f4 += ComputeBlockOffset(m_art.m_iSizeY);
        
        setPosition(f3, f4, f5);
        
        float f7 = -0.00625F;
        boundingBox.setBounds(f3 - f - f7, f4 - f1 - f7, f5 - f2 - f7, f3 + f + f7, f4 + f1 + f7, f5 + f2 + f7);
    }

    private float ComputeBlockOffset( int iEdgeSize )
    {
    	if ( iEdgeSize % 32 == 0 )
    	{
    		return 0.5F;
    	}
    	
    	return 0.0F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (tickCounter1++ == 100 && !worldObj.isRemote)
        {
            tickCounter1 = 0;

            if (!isDead && !onValidSurface())
            {
                setDead();
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(FCBetterThanWolves.fcItemCanvas)));
            }
        }
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface()
    {
        if (worldObj.getCollidingBoundingBoxes(this, boundingBox).size() > 0)
        {
            return false;
        }

        int i = m_art.m_iSizeX / 16;
        int j = m_art.m_iSizeY / 16;
        int k = xPosition;
        int l = yPosition;
        int i1 = zPosition;

        if (direction == 0)
        {
            k = MathHelper.floor_double(posX - (double)((float)m_art.m_iSizeX / 32F));
        }

        if (direction == 1)
        {
            i1 = MathHelper.floor_double(posZ - (double)((float)m_art.m_iSizeX / 32F));
        }

        if (direction == 2)
        {
            k = MathHelper.floor_double(posX - (double)((float)m_art.m_iSizeX / 32F));
        }

        if (direction == 3)
        {
            i1 = MathHelper.floor_double(posZ - (double)((float)m_art.m_iSizeX / 32F));
        }

        l = MathHelper.floor_double(posY - (double)((float)m_art.m_iSizeY / 32F));

        for (int j1 = 0; j1 < i; j1++)
        {
            for (int k1 = 0; k1 < j; k1++)
            {
                Material material;

                if (direction == 0 || direction == 2)
                {
                    material = worldObj.getBlockMaterial(k + j1, l + k1, zPosition);
                }
                else
                {
                    material = worldObj.getBlockMaterial(xPosition, l + k1, i1 + j1);
                }

                if (!material.isSolid())
                {
                    return false;
                }
            }
        }

        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);

        for (int l1 = 0; l1 < list.size(); l1++)
        {
            if (list.get(l1) instanceof EntityPainting || list.get(l1) instanceof FCEntityCanvas )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (!isDead && !worldObj.isRemote)
        {
            setDead();
            setBeenAttacked();
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(FCBetterThanWolves.fcItemCanvas)));
        }

        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setByte("Dir", (byte)direction);
        par1NBTTagCompound.setString("Motive", m_art.m_sTitle);
        par1NBTTagCompound.setInteger("TileX", xPosition);
        par1NBTTagCompound.setInteger("TileY", yPosition);
        par1NBTTagCompound.setInteger("TileZ", zPosition);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        direction = par1NBTTagCompound.getByte("Dir");
        xPosition = par1NBTTagCompound.getInteger("TileX");
        yPosition = par1NBTTagCompound.getInteger("TileY");
        zPosition = par1NBTTagCompound.getInteger("TileZ");
        String s = par1NBTTagCompound.getString("Motive");
        FCEnumCanvasArt aenumart[] = FCEnumCanvasArt.values();
        int i = aenumart.length;

        for (int j = 0; j < i; j++)
        {
        	FCEnumCanvasArt enumart = aenumart[j];

            if (enumart.m_sTitle.equals(s))
            {
                m_art = enumart;
            }
        }

        if (m_art == null)
        {
            m_art = FCEnumCanvasArt.Icarus;
        }

        func_412_b(direction);
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double par1, double par3, double par5)
    {
        if (!worldObj.isRemote && !isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            setDead();
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(FCBetterThanWolves.fcItemCanvas)));
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double par1, double par3, double par5)
    {
        if (!worldObj.isRemote && !isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            setDead();
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(FCBetterThanWolves.fcItemCanvas)));
        }
    }
    
    /*
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    	// empty override to prevent network entity teleport packets from fucking up position and bounding box
    }
    */
    
    //************* FCIEntityPacketHandler ************//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeCanvas );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( xPosition );
	        dataStream.writeInt( yPosition );
	        dataStream.writeInt( zPosition );
	        dataStream.writeInt( direction );
	        dataStream.writeInt( m_art.ordinal() );
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    @Override
    public int GetTrackerViewDistance()
    {
    	return 160;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 0x7fffffff;
    }    
    
    @Override
    public boolean GetTrackMotion()
    {
    	return false;
    }
    
    @Override
    public boolean ShouldServerTreatAsOversized()
    {
    	return true;
    }
    
    @Override
    protected boolean ShouldSetPositionOnLoad()
    {
    	return false;
    }
}
