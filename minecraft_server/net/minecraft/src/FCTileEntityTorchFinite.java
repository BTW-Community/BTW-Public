// FCMOD

package net.minecraft.src;

public class FCTileEntityTorchFinite extends TileEntity
{
    static private final float m_fChanceOfFireSpread = 0.01F;
    static private final float m_fChanceOfGoingOutFromRain = 0.01F;
    
    static public final int m_iMaxBurnTime = 24000; // full day
	static public final int m_iSputterTime = 30 * 20; // 30 seconds
		
	public int m_iBurnTimeCountdown = 0;
	
    public FCTileEntityTorchFinite()
    {
    	super();
    	
    	m_iBurnTimeCountdown = m_iMaxBurnTime; 
    }
    
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        if( tag.hasKey( "fcBurnCounter" ) )
        {
        	m_iBurnTimeCountdown = tag.getInteger( "fcBurnCounter" );
        }
    }
    
    @Override
    public void writeToNBT( NBTTagCompound tag)
    {
        super.writeToNBT( tag );
        
        tag.setInteger( "fcBurnCounter", m_iBurnTimeCountdown );
    }
        
    @Override
    public void updateEntity()
    {
    	super.updateEntity();   

        if ( !worldObj.isRemote )
        {
			m_iBurnTimeCountdown--;
			
			if ( m_iBurnTimeCountdown <= 0 || ( worldObj.rand.nextFloat() <= m_fChanceOfGoingOutFromRain && IsRainingOnTorch() ) )
			{				
				// wait until all chunks in vicinity are loaded to avoid lighting glitches
				
				if ( worldObj.doChunksNearChunkExist( xCoord, yCoord, zCoord, 32 ) )
				{
					ExtinguishTorch();
				}
				else
				{
					// ensure it goes out once fully loaded
					
					m_iBurnTimeCountdown = 0;
				}				
			}
			else 
			{
				if ( m_iBurnTimeCountdown < m_iSputterTime )
				{
			    	int iMetadata = worldObj.getBlockMetadata( xCoord, yCoord, zCoord );
			    	
			    	if ( !FCBlockTorchFiniteBurning.GetIsSputtering( iMetadata ) )
			    	{
			    		FCBlockTorchFiniteBurning block = (FCBlockTorchFiniteBurning)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
			    		
			    		block.SetIsSputtering( worldObj, xCoord, yCoord, zCoord, true );
			    	}
				}
				
	    		if ( worldObj.rand.nextFloat() <= m_fChanceOfFireSpread )
	    		{
    				FCBlockFire.CheckForFireSpreadAndDestructionToOneBlockLocation( worldObj, xCoord, yCoord + 1, zCoord );
	    		}
			}
    	}
    	else
    	{
	    	int iMetadata = worldObj.getBlockMetadata( xCoord, yCoord, zCoord );
	    	
	    	if ( FCBlockTorchFiniteBurning.GetIsSputtering( iMetadata ) )
	    	{ 
	    		Sputter();
	    	}
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean IsRainingOnTorch()
    {
    	FCBlockTorchFiniteBurning block = (FCBlockTorchFiniteBurning)(Block.blocksList[worldObj.getBlockId( xCoord, yCoord, zCoord )]);
    	
    	return block.IsRainingOnTorch( worldObj, xCoord, yCoord, zCoord );
    }
    
    private void ExtinguishTorch()
    {
    	m_iBurnTimeCountdown = 0;
    	
    	int iOldMetadata = worldObj.getBlockMetadata( xCoord, yCoord, zCoord );
    	int iOrientation = FCBlockTorchBase.GetOrientation( iOldMetadata );
    	
    	int iNewMetadata = FCBlockTorchBase.SetOrientation( 0, iOrientation );
    	iNewMetadata = FCBlockTorchFiniteUnlit.SetIsBurnedOut( iNewMetadata, true );
    	
		worldObj.playAuxSFX( 1004, xCoord, yCoord, zCoord, 0 );
		
    	worldObj.setBlockAndMetadataWithNotify( xCoord, yCoord, zCoord, FCBetterThanWolves.fcBlockTorchFiniteUnlit.blockID, iNewMetadata );
    }
    
    private void Sputter()
    {
    	int iMetadata = worldObj.getBlockMetadata( xCoord, yCoord, zCoord );
    	
        int iOrientation = FCBlockTorchBase.GetOrientation( iMetadata );
        
        double dHorizontalOffset = 0.27D;

        double xPos = xCoord + 0.5D + ( worldObj.rand.nextDouble() * 0.1D - 0.05D );
        double yPos = yCoord + 0.92D + ( worldObj.rand.nextDouble() * 0.1D - 0.05D );
        double zPos = zCoord + 0.5D + ( worldObj.rand.nextDouble() * 0.1D - 0.05D );
        
        if ( iOrientation == 1 )
        {
        	xPos -= dHorizontalOffset;
        }
        else if ( iOrientation == 2 )
        {
        	xPos += dHorizontalOffset;
        }
        else if ( iOrientation == 3 )
        {
        	zPos -= dHorizontalOffset;        	
        }
        else if ( iOrientation == 4 )
        {
        	zPos += dHorizontalOffset;
        }
        else
        {
        	yPos -= 0.22D;
        }
        
        worldObj.spawnParticle("smoke", xPos, yPos, zPos, 0.0D, 0.0D, 0.0D);
    }
    
    //----------- Client Side Functionality -----------//
}