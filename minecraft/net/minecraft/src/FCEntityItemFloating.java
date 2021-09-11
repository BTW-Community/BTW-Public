// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntityItemFloating extends EntityItem
	implements FCIEntityPacketHandler
{
    public FCEntityItemFloating( World world, double dPosX, double dPosY, double dPosZ, ItemStack itemStack )
    {
    	super( world, dPosX, dPosY, dPosZ, itemStack );
    }

    public FCEntityItemFloating( World world )
    {
        super( world );
    }
    
    // most of the code in the following function is just a copy of that in EntityItem with the ability to float added in
    public void onUpdate()
    {
    	// reproduces what Entity normally calls on update since we don't want to call the EntityItem super version of this
        onEntityUpdate();

        if (delayBeforeCanPickup > 0)
        {
            delayBeforeCanPickup--;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        //motionY -= 0.039999999105930328D;

        // Code out of EntityBoat to handle floating
        int numDepthChecks = 5;
        double d = 0.0D;
        double dBoundingYOffset = 0.10D;

        for (int j = 0; j < numDepthChecks; j++)
        {
            double d2 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 0)) /*/ (double)numDepthChecks */) + dBoundingYOffset;
            double d8 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 1)) /*/ (double)numDepthChecks */) + dBoundingYOffset;
            
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(boundingBox.minX, d2, boundingBox.minZ, boundingBox.maxX, d8, boundingBox.maxZ);

            if (worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d += 1.0D / (double)numDepthChecks;
            }
        }
        // End of code out of EntityBoat
        
        if (worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) == Material.lava)
        {
            motionY = 0.20000000298023221D;
            motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
            motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
            worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
        }
        // more Entity boat floating
        else if (d < 1.0D)
        {
            double d6 = d * 2D - 1.0D;
            motionY += 0.04D * d6;
        }
        else
        {
            if (motionY < 0.0D)
            {
                motionY /= 2D;
            }

            motionY += 0.007D;
        }
        // end EntityBoat

        if ( !worldObj.isRemote )
        {
        	pushOutOfBlocks( posX, ( boundingBox.minY + boundingBox.maxY ) / 2D, posZ );
        }
        
        moveEntity(motionX, motionY, motionZ);
        
        float f = 0.98F;

        if (onGround)
        {
            f = 0.5880001F;
            int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

            if (i > 0)
            {
                f = Block.blocksList[i].slipperiness * 0.98F;
            }
        }

        motionX *= f;
        motionY *= 0.98000001907348633D;
        motionZ *= f;

        if (onGround)
        {
            motionY *= -0.5D;
        }

        age++;

        if (age >= 6000)
        {
            setDead();
        }
    }
    
    //************* FCIEntityPacketHandler ************//

    @Override
    public int GetTrackerViewDistance()
    {
    	return 64;
    }
    
    @Override
    public int GetTrackerUpdateFrequency()
    {
    	return 20;
    }
    
    @Override
    public boolean GetTrackMotion()
    {
    	return true;
    }
    
    @Override
    public boolean ShouldServerTreatAsOversized()
    {
    	return false;
    }
    
    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
    	// FCTODO: Move this up into a parent class with the BloodWood Sapling code
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeItemFloating );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeInt( getEntityItem().itemID );
	        dataStream.writeInt( getEntityItem().stackSize );
	        dataStream.writeInt( getEntityItem().getItemDamage() );
	        
	        dataStream.writeByte( (byte)(int)( motionX * 128D ) );
	        dataStream.writeByte( (byte)(int)( motionY * 128D ) );
	        dataStream.writeByte( (byte)(int)( motionZ * 128D ) );	        		
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }    
}