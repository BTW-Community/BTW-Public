// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntityItemBloodWoodSapling extends EntityItem
	implements FCIEntityPacketHandler
{
    public FCEntityItemBloodWoodSapling( World world, double dPosX, double dPosY, double dPosZ, ItemStack itemStack )
    {
    	super( world, dPosX, dPosY, dPosZ, itemStack );
    	
        isImmuneToFire = true;
    }

    public FCEntityItemBloodWoodSapling( World world )
    {
        super( world );
        
        isImmuneToFire = true;
    }
    
    public void onUpdate()
    {
    	super.onUpdate();
    	
    	if ( !isDead && !worldObj.isRemote )
    	{
            if ( onGround )
            {
            	int i = MathHelper.floor_double( posX );
            	int iBlockBelowJ = MathHelper.floor_double( boundingBox.minY - 0.1F );
            	int k = MathHelper.floor_double( posZ );
            	
            	int iBlockBelowID = worldObj.getBlockId( i, iBlockBelowJ, k );            	

                CheckForBloodWoodPlant( i, iBlockBelowJ, k); 
            }
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
    	// FCTODO: Move this up into a parent class with the Floating Item code
    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeItemBloodWoodSapling );
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
    
    //************* Class Specific Methods ************//

    public void CheckForBloodWoodPlant( int i, int j, int k )
    {
    	Block blockAbove = Block.blocksList[worldObj.getBlockId( i, j + 1, k )];
    	
        if ( blockAbove == null || blockAbove.IsAirBlock() || blockAbove.IsGroundCover() )
    	{
        	int iBlockID = worldObj.getBlockId( i, j, k );

        	if ( iBlockID == Block.slowSand.blockID || ( iBlockID == FCBetterThanWolves.fcPlanter.blockID && 
				((FCBlockPlanter)(FCBetterThanWolves.fcPlanter)).GetPlanterType( worldObj, i, j, k ) == FCBlockPlanter.m_iTypeSoulSand ) )
        	{        		
    			worldObj.setBlockAndMetadataWithNotify( i, j + 1, k, 
					FCBetterThanWolves.fcAestheticVegetation.blockID,
					FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling );
    			
    			getEntityItem().stackSize--;
    			
    			if ( getEntityItem().stackSize <= 0 )
    			{
    				setDead();
    			}
        	}
    	}
    }
}