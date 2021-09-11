// FCMOD

package net.minecraft.src;

public class FCTileEntityArcaneVessel extends TileEntity
	implements FCITileEntityDataPacketHandler
{
	static final public int m_iMaxContainedExperience = 1000;
	
	static final private int m_iMinTempleExperience = 200;
	static final private int m_iMaxTempleExperience = 256;
	
	static final public int m_iMaxVisualExperienceLevel = 10;
	
	private final int m_iXPEjectUnitSize = 20;	
	
    private int m_iVisualExperienceLevel;
    
    private int m_iContainedRegularExperience;
    private int m_iContainedDragonExperience;
    
    
	public FCTileEntityArcaneVessel()
	{
	    m_iVisualExperienceLevel = 0;
	    
	    m_iContainedRegularExperience = 0;
	    m_iContainedDragonExperience = 0;
	}
	
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        
        nbttagcompound.setInteger( "regXP", m_iContainedRegularExperience );
        nbttagcompound.setInteger( "dragXP", m_iContainedDragonExperience );
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        
    	m_iContainedRegularExperience = nbttagcompound.getInteger( "regXP" );    	
    	m_iContainedDragonExperience = nbttagcompound.getInteger( "dragXP" );
    	
    	int iTotalExperience = m_iContainedRegularExperience + m_iContainedDragonExperience;
    	
    	m_iVisualExperienceLevel = (int)( (float)m_iMaxVisualExperienceLevel * ( (float)iTotalExperience / (float)m_iMaxContainedExperience ) );
    	
    	if ( iTotalExperience > 0 && m_iVisualExperienceLevel == 0 )
    	{
    		m_iVisualExperienceLevel = 1;
    	}
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        nbttagcompound.setByte( "x", (byte)m_iVisualExperienceLevel );
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, nbttagcompound );
    }
    
    @Override
    public void updateEntity()
    {
    	if ( worldObj.isRemote )
    	{
    		return;
    	}
    	
		int iBlockID = worldObj.getBlockId( xCoord, yCoord, zCoord );
		
		Block block = Block.blocksList[iBlockID];
		
		if ( block == null || !( block instanceof FCBlockArcaneVessel ) )
		{
			// shouldn't happen
			
			return;
		}
		
		FCBlockArcaneVessel vesselBlock = (FCBlockArcaneVessel)block; 
		
		if ( vesselBlock.GetMechanicallyPoweredFlag( worldObj, xCoord, yCoord, zCoord ) )
		{
    		int iTiltFacing = vesselBlock.GetTiltFacing( worldObj, xCoord, yCoord, zCoord );
    		
			AttemptToSpillXPFromInv( iTiltFacing );    	
		}
    }
    	
    //************* FCITileEntityDataPacketHandler ************//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound nbttagcompound )
    {
    	m_iVisualExperienceLevel = nbttagcompound.getByte( "x" );
        
        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }    
    
    //************* Class Specific Methods ************//
    
    public int GetVisualExperienceLevel()
    {
    	return m_iVisualExperienceLevel;
    }
    
    public int GetContainedRegularExperience()
    {
    	return m_iContainedRegularExperience;
    }
    
    public void SetContainedRegularExperience( int iExperience )
    {
    	m_iContainedRegularExperience = iExperience;
    	
    	ValidateVisualExperience();    	
    }
    
    public int GetContainedDragonExperience()
    {
    	return m_iContainedDragonExperience;
    }
    
    public void SetContainedDragonExperience( int iExperience )
    {
    	m_iContainedDragonExperience = iExperience;
    	
    	ValidateVisualExperience();    	
    }
    
    public int GetContainedTotalExperience()
    {
    	return m_iContainedDragonExperience + m_iContainedRegularExperience;
    }
    
    public void ValidateVisualExperience()
    {
    	int iTotalExperience = m_iContainedRegularExperience + m_iContainedDragonExperience;
    	
    	int iNewVisualExperience = (int)( (float)m_iMaxVisualExperienceLevel * ( (float)iTotalExperience / (float)m_iMaxContainedExperience ) );
    	
    	if ( iTotalExperience > 0 && iNewVisualExperience == 0 )
    	{
    		iNewVisualExperience = 1;
    	}
    	
    	// mark block to be sent to client
    	
    	if ( iNewVisualExperience != m_iVisualExperienceLevel )
    	{    	
    		m_iVisualExperienceLevel = iNewVisualExperience;
    		
    		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    	}
    }
    
    public void InitTempleExperience()
    {
    	SetContainedRegularExperience( worldObj.rand.nextInt( m_iMaxTempleExperience - m_iMinTempleExperience ) + m_iMinTempleExperience );
    }
    
    public boolean AttemptToSwallowXPOrb( World world, int i, int j, int k, EntityXPOrb entityXPOrb )
    {
    	int iTotalContainedXP = m_iContainedRegularExperience + m_iContainedDragonExperience;
    	int iRemainingSpace = m_iMaxContainedExperience - iTotalContainedXP;
    	
    	if ( iRemainingSpace > 0 )
    	{
        	int iXPToAddToInventory = 0;
        	boolean bIsDragonOrb = entityXPOrb.m_bNotPlayerOwned;
        	
    		if ( entityXPOrb.xpValue <= iRemainingSpace )
    		{
    			iXPToAddToInventory = entityXPOrb.xpValue;
    			
    			entityXPOrb.setDead();
    		}
    		else
    		{
    			iXPToAddToInventory = iRemainingSpace;    			
    		}
    		
    		if ( bIsDragonOrb )
    		{
    			SetContainedDragonExperience( m_iContainedDragonExperience + iXPToAddToInventory );
    		}
    		else
    		{
    			SetContainedRegularExperience( m_iContainedRegularExperience + iXPToAddToInventory );
    		}
			
			return true;    			
    	}    
        
    	return false;
    }
    
    private void AttemptToSpillXPFromInv( int iTiltFacing )
    {
    	int iXPToSpill = 0;
    	boolean bSpillDragonOrb = false;
    	
    	if ( m_iContainedDragonExperience > 0 || m_iContainedRegularExperience > 0 && !IsTiltedOutputBlocked( iTiltFacing ) )
    	{
    		if ( m_iContainedDragonExperience > 0 )
    		{
    			bSpillDragonOrb = true;
    			
    			if ( m_iContainedDragonExperience < m_iXPEjectUnitSize )
    			{
    				iXPToSpill = m_iContainedDragonExperience;
    			}
    			else
    			{
    				iXPToSpill = m_iXPEjectUnitSize;
    			}
    			
    			SetContainedDragonExperience( m_iContainedDragonExperience - iXPToSpill );
    		}
    		else
    		{
    			if ( m_iContainedRegularExperience < m_iXPEjectUnitSize )
    			{
    				iXPToSpill = m_iContainedRegularExperience;
    			}
    			else
    			{
    				iXPToSpill = m_iXPEjectUnitSize;
    			}
    			
    			SetContainedRegularExperience( m_iContainedRegularExperience - iXPToSpill );
    		}
    	}
    	
    	if ( iXPToSpill > 0 )
    	{
    		SpillXPOrb( iXPToSpill, bSpillDragonOrb, iTiltFacing );
    	}
    }
    
    private boolean IsTiltedOutputBlocked( int iTiltFacing )
    {
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( xCoord, yCoord, zCoord );
		
		targetPos.AddFacingAsOffset( iTiltFacing );
		
		if ( !worldObj.isAirBlock( targetPos.i, targetPos.j, targetPos.k ) )
		{
			if ( !FCUtilsWorld.IsReplaceableBlock( worldObj, targetPos.i, targetPos.j, targetPos.k ) )
			{				
				int iTargetBlockID = worldObj.getBlockId( targetPos.i, targetPos.j, targetPos.k );
				
				Block targetBlock = Block.blocksList[iTargetBlockID];
				
				if ( targetBlock.blockMaterial.isSolid() )
				{
					return true;
				}
			}				
		}
		
		return false;
    }
    
    public void EjectContentsOnBlockBreak()
    {
    	while ( m_iContainedRegularExperience > 0 )
    	{
    		int iEjectSize = m_iXPEjectUnitSize;
    		
    		if ( m_iContainedRegularExperience < m_iXPEjectUnitSize )
    		{
    			iEjectSize = m_iContainedRegularExperience;
    		}
    		
    		EjectXPOrbOnBlockBreak( iEjectSize, false );
    		
			m_iContainedRegularExperience -= iEjectSize;
    	}
    	
    	while ( m_iContainedDragonExperience > 0 )
    	{
    		int iEjectSize = m_iXPEjectUnitSize;
    		
    		if ( m_iContainedDragonExperience < m_iXPEjectUnitSize )
    		{
    			iEjectSize = m_iContainedDragonExperience;
    		}
    		
    		EjectXPOrbOnBlockBreak( iEjectSize, true );
    		
			m_iContainedDragonExperience -= iEjectSize;
    	}
    }
    
    private void SpillXPOrb( int iXPValue, boolean bDragonOrb, int iFacing )
    {
		Vec3 itemPos = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );

		itemPos.xCoord *= 0.5F;
		itemPos.yCoord *= 0.5F;
		itemPos.zCoord *= 0.5F;
		
		itemPos.xCoord += ((float)xCoord ) + 0.5F;
		itemPos.yCoord += ((float)yCoord ) + 0.25F;
		itemPos.zCoord += ((float)zCoord ) + 0.5F + worldObj.rand.nextFloat() * 0.3F;
		
		if ( itemPos.xCoord > 0.1F || itemPos.xCoord < -0.1F )
		{
			itemPos.xCoord +=  ( worldObj.rand.nextFloat() * 0.5F ) - 0.25F;
		}
		else
		{
			itemPos.zCoord +=  ( worldObj.rand.nextFloat() * 0.5F ) - 0.25F;
		}
    	
        EntityXPOrb xpOrb = new EntityXPOrb( worldObj, 
			itemPos.xCoord, itemPos.yCoord, itemPos.zCoord, 
    		iXPValue, bDragonOrb );

		Vec3 itemVel = FCUtilsMisc.ConvertBlockFacingToVector( iFacing );
		
		itemVel.xCoord *= 0.1F;
		itemVel.yCoord *= 0.1F;
		itemVel.zCoord *= 0.1F;
		
		xpOrb.motionX = itemVel.xCoord;
		xpOrb.motionY = itemVel.yCoord;
		xpOrb.motionZ = itemVel.zCoord;
        
        worldObj.spawnEntityInWorld( xpOrb );
    }
    
    private void EjectXPOrbOnBlockBreak( int iXPValue, boolean bDragonOrb )
    {
        double xOffset = worldObj.rand.nextDouble() * 0.7D + 0.15D;
        double yOffset = worldObj.rand.nextDouble() * 0.7D + 0.15D;
        double zOffset = worldObj.rand.nextDouble() * 0.7D + 0.15D;
    	
        EntityXPOrb xpOrb = new EntityXPOrb( worldObj, 
    			xCoord + xOffset, yCoord + yOffset, zCoord + zOffset, 
        		iXPValue, bDragonOrb );

        xpOrb.motionX = (float)worldObj.rand.nextGaussian() * 0.05F;
        xpOrb.motionY = (float)worldObj.rand.nextGaussian() * 0.05F + 0.2F;
        xpOrb.motionZ = (float)worldObj.rand.nextGaussian() * 0.05F;
        
        worldObj.spawnEntityInWorld( xpOrb );        
    }    
}