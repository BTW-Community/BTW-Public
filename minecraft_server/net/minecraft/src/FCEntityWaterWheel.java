// FCMOD

package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FCEntityWaterWheel extends FCEntityMechPowerHorizontal
{
	// constants
	
    static final public float m_fHeight = 4.8F;
    static final public float m_fWidth = 4.8F;
    static final public float m_fDepth = 0.8F;
    
    static final public int m_iMaxDamage = 160;

    static final public float m_fRotationPerTick = 0.25F;
    
    static final public int m_iTicksPerFullUpdate = 20;    
    
    public FCEntityWaterWheel( World world )
    {
        super(world);        
    }
    
    public FCEntityWaterWheel( World world, double x, double y, double z, boolean bIAligned  ) 
    {
    	super( world, x, y, z, bIAligned );
        
    }
    
	@Override
    protected void entityInit()
    {
    	super.entityInit();
    	
    }
    
	@Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    	nbttagcompound.setBoolean( "bWaterWheelIAligned", m_bIAligned );    	
    	nbttagcompound.setFloat( "fRotation", m_fRotation );    	
    	nbttagcompound.setBoolean( "bProvidingPower", m_bProvidingPower );
    }
    	

	@Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    	m_bIAligned = nbttagcompound.getBoolean( "bWaterWheelIAligned" );    	
    	m_fRotation = nbttagcompound.getFloat( "fRotation" );    	
    	m_bProvidingPower = nbttagcompound.getBoolean( "bProvidingPower" );
    	
        InitBoundingBox();
    }
    
    //------------- FCIEntityPacketHandler ------------//

    @Override
    public Packet GetSpawnPacketForThisEntity()
    {    	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );
        
        try
        {
	        byte bIAligned = 0;
	        
	        if ( m_bIAligned )
	        {
	        	bIAligned = 1;
	        }
	        
	        dataStream.writeInt( FCBetterThanWolves.fcCustomSpawnEntityPacketTypeWaterWheel );
	        dataStream.writeInt( entityId );
	        
	        dataStream.writeInt( MathHelper.floor_double( posX * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posY * 32D ) );
	        dataStream.writeInt( MathHelper.floor_double( posZ * 32D ) );
	        
	        dataStream.writeByte( bIAligned );	        
	        dataStream.writeInt( getRotationSpeedScaled() );
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }        
	        
    	return new Packet250CustomPayload( FCBetterThanWolves.fcCustomPacketChannelSpawnCustomEntity, byteStream.toByteArray() ); 
    }
    
    //------------- FCEntityMechPower ------------//
    
	@Override
    public float GetWidth()
	{
		return m_fWidth;
	}
    
	@Override
    public float GetHeight()
	{
		return m_fHeight;
	}
	
	@Override
    public float GetDepth()
	{
		return m_fDepth;
	}
	
	@Override
    public int GetMaxDamage()
	{
		return m_iMaxDamage;
	}
	
	@Override
    public int GetTicksPerFullUpdate()
	{
		return m_iTicksPerFullUpdate;
	}	
    
	@Override
    protected void DestroyWithDrop()
    {
    	if ( !isDead )
    	{
	    	dropItemWithOffset( FCBetterThanWolves.fcItemWaterWheel.itemID, 1, 0.0F );
	    	
			setDead();
    	}
    }
        
	@Override
    public boolean ValidateAreaAroundDevice()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	return WaterWheelValidateAreaAroundBlock( worldObj, iCenterI, iCenterJ, iCenterK, m_bIAligned );
    }
    
	@Override
    protected float ComputeRotation()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	float fRotationAmount = 0.0F;
    	
    	int iFlowJ = iCenterJ - 2;
    	
    	// check for rotation below
    	
    	int iFlowBlockID = worldObj.getBlockId( iCenterI, iFlowJ, iCenterK );
    	
    	if ( iFlowBlockID == Block.waterMoving.blockID || iFlowBlockID == Block.waterStill.blockID )
    	{
    		Vec3 flowVector = getFlowVector( worldObj, iCenterI, iFlowJ, iCenterK );
    		
    		if ( m_bIAligned )
    		{
    			if ( flowVector.zCoord > 0.33F )
    			{
    				fRotationAmount = -m_fRotationPerTick; 
    			}
    			else if ( flowVector.zCoord < -0.33F )
    			{
    				fRotationAmount = m_fRotationPerTick; 
    			}
    		}
    		else
    		{
    			if ( flowVector.xCoord > 0.33F )
    			{
    				fRotationAmount = m_fRotationPerTick; 
    			}
    			else if ( flowVector.xCoord < -0.33F )
    			{
    				fRotationAmount = -m_fRotationPerTick; 
    			}
    		}
    	}
    	
    	// check for rotation on the sides
    	
    	int iOffset;
    	int kOffset;
    	
    	if ( m_bIAligned )
    	{
    		iOffset = 0;    		
    		kOffset = 2;
    	}
    	else
    	{    		
       		iOffset = 2;    		
    		kOffset = 0;
    	}
    	
    	iFlowBlockID = worldObj.getBlockId( iCenterI + iOffset, iCenterJ, iCenterK - kOffset );
    	
    	if ( iFlowBlockID == Block.waterMoving.blockID || iFlowBlockID == Block.waterStill.blockID )
    	{
    		BlockFluid fluidBlock = (BlockFluid)(Block.blocksList[iFlowBlockID]);
    		
			fRotationAmount -= m_fRotationPerTick; 
    	}
    	
    	iFlowBlockID = worldObj.getBlockId( iCenterI - iOffset, iCenterJ, iCenterK + kOffset );
    	
    	if ( iFlowBlockID == Block.waterMoving.blockID || iFlowBlockID == Block.waterStill.blockID )
    	{
    		BlockFluid fluidBlock = (BlockFluid)(Block.blocksList[iFlowBlockID]);
    		
			fRotationAmount += m_fRotationPerTick; 
    	}
    	
    	if ( fRotationAmount > m_fRotationPerTick )
    	{
    		fRotationAmount = m_fRotationPerTick;
    	}
    	else if ( fRotationAmount <= -m_fRotationPerTick )
    	{
    		fRotationAmount = -m_fRotationPerTick;
    	}
    		
    	return fRotationAmount;
    }
    
	//------------- Class Specific Methods ------------//

    static public boolean WaterWheelValidateAreaAroundBlock( World world, int i, int j, int k, boolean bIAligned )
    {
    	int iOffset;
    	int kOffset;
    	
    	if ( bIAligned )
    	{
    		iOffset = 0;    		
    		kOffset = 1;
    	}
    	else
    	{
       		iOffset = 1;    		
    		kOffset = 0;
    	}
    	
    	for ( int iHeightOffset = -2; iHeightOffset <= 2; iHeightOffset++ )
    	{
    		for ( int iWidthOffset = -2; iWidthOffset <= 2; iWidthOffset++ )
    		{
    			if ( iHeightOffset != 0 || iWidthOffset != 0 )
    			{
    				int tempI = i + ( iOffset * iWidthOffset );
    				int tempJ = j + iHeightOffset;
    				int tempK = k + ( kOffset * iWidthOffset );
    				
    				if ( !IsValidBlockForWaterWheelToOccupy( world, tempI, tempJ, tempK ) )
    				{
    					return false;
    				}
    			}
    		}
    	}
    	
    	return true;
    }
    
    static public boolean IsValidBlockForWaterWheelToOccupy( World world, int i, int j, int k )
    {
    	if ( !world.isAirBlock( i, j, k ) )
    	{
    		int iBlockID = world.getBlockId( i, j, k );
    		
    		if ( iBlockID != Block.waterMoving.blockID && iBlockID != Block.waterStill.blockID )
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }    
    
    // ripped from BlockFluid so I don't have to modify base-class
    private Vec3 getFlowVector( IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        Vec3 vec3 = par1IBlockAccess.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 0.0D);
        
        int i = getEffectiveFlowDecay(par1IBlockAccess, par2, par3, par4);

        for (int j = 0; j < 4; j++)
        {
            int k = par2;
            int l = par3;
            int i1 = par4;

            if (j == 0)
            {
                k--;
            }

            if (j == 1)
            {
                i1--;
            }

            if (j == 2)
            {
                k++;
            }

            if (j == 3)
            {
                i1++;
            }

            int j1 = getEffectiveFlowDecay(par1IBlockAccess, k, l, i1);

            if (j1 < 0)
            {
                if (par1IBlockAccess.getBlockMaterial(k, l, i1).blocksMovement())
                {
                    continue;
                }

                j1 = getEffectiveFlowDecay(par1IBlockAccess, k, l - 1, i1);

                if (j1 >= 0)
                {
                    int k1 = j1 - (i - 8);
                    vec3 = vec3.addVector((k - par2) * k1, (l - par3) * k1, (i1 - par4) * k1);
                }

                continue;
            }

            if (j1 >= 0)
            {
                int l1 = j1 - i;
                vec3 = vec3.addVector((k - par2) * l1, (l - par3) * l1, (i1 - par4) * l1);
            }
        }

        if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) >= 8)
        {
            boolean flag = false;

            if (flag || isBlockSolid(par1IBlockAccess, par2, par3, par4 - 1, 2))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2, par3, par4 + 1, 3))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2 - 1, par3, par4, 4))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2 + 1, par3, par4, 5))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2, par3 + 1, par4 - 1, 2))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2, par3 + 1, par4 + 1, 3))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2 - 1, par3 + 1, par4, 4))
            {
                flag = true;
            }

            if (flag || isBlockSolid(par1IBlockAccess, par2 + 1, par3 + 1, par4, 5))
            {
                flag = true;
            }

            if (flag)
            {
                vec3 = vec3.normalize().addVector(0.0D, -6D, 0.0D);
            }
        }

        vec3 = vec3.normalize();
        return vec3;
    }
    
    // ripped from BlockFluid so I don't have to modify base-class
    private boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        Material material = par1IBlockAccess.getBlockMaterial(par2, par3, par4);

        if (material == Block.waterMoving.blockMaterial)
        {
            return false;
        }

        if (par5 == 1)
        {
            return true;
        }

        if (material == Material.ice)
        {
            return false;
        }
        else
        {
        	// changed this line to replace the super call in the original
            return par1IBlockAccess.getBlockMaterial(par2, par3, par4).isSolid();
        }
    }
    
    // ripped from BlockFluid so I don't have to modify base-class
    private int getEffectiveFlowDecay( IBlockAccess iblockaccess, int i, int j, int k)
    {
        if(iblockaccess.getBlockMaterial(i, j, k) != Block.waterMoving.blockMaterial)
        {
            return -1;
        }
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if(l >= 8)
        {
            l = 0;
        }
        return l;
    }    
}