// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCTileEntityUnfiredBrick extends TileEntity
	implements FCITileEntityDataPacketHandler
{
    private final int m_iTimeToCook = ( 10 * 60 * 20 );
    private final int m_iRainCookDecay = 10;
    
	private int m_iCookCounter = 0;
	
	private boolean m_bIsCooking = false;
	
    public FCTileEntityUnfiredBrick()
    {
    	super();
    }
    
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );
        
        if( tag.hasKey( "fcCookCounter" ) )
        {
        	m_iCookCounter = tag.getInteger( "fcCookCounter" );
        }
    }
    
    @Override
    public void writeToNBT( NBTTagCompound tag)
    {
        super.writeToNBT( tag );
        
        tag.setInteger( "fcCookCounter", m_iCookCounter );
    }
        
    @Override
    public void updateEntity()
    {
    	super.updateEntity();   

    	if ( !worldObj.isRemote )
    	{
    		UpdateCooking();
    	}
    	else 
    	{    
    		if ( m_bIsCooking )
    		{
				if ( worldObj.rand.nextInt( 20 ) == 0 )
				{
	                double xPos = xCoord + 0.25F + worldObj.rand.nextFloat() * 0.5F;
	                double yPos = yCoord + 0.5F + worldObj.rand.nextFloat() * 0.25F;
	                double zPos = zCoord + 0.25F + worldObj.rand.nextFloat() * 0.5F;
	                
	                worldObj.spawnParticle( "fcwhitesmoke", xPos, yPos, zPos, 0.0D, 0.0D, 0.0D );
	            }
    		}
    	}
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        
        tag.setBoolean( "x", m_bIsCooking );
        
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, tag );
    }
    
    //------------- FCITileEntityDataPacketHandler ------------//
    
    @Override
    public void readNBTFromPacket( NBTTagCompound tag )
    {
        m_bIsCooking = tag.getBoolean( "x" );

        // force a visual update for the block since the above variables affect it
        
        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );        
    }
    
    //------------- Class Specific Methods ------------//
    
    public void UpdateCooking()
    {
		boolean bNewCooking;
    	int iBlockMaxNaturalLight = worldObj.GetBlockNaturalLightValueMaximum( xCoord, yCoord, zCoord );
    	int iBlockCurrentNaturalLight = iBlockMaxNaturalLight - worldObj.skylightSubtracted;
    	
    	bNewCooking = iBlockCurrentNaturalLight >= 15;
    	
    	int iBlockAboveID = worldObj.getBlockId( xCoord, yCoord + 1, zCoord );
    	Block blockAbove = Block.blocksList[iBlockAboveID];
    	
    	if ( blockAbove != null && blockAbove.IsGroundCover( ) )
    	{
    		bNewCooking = false;
    	}
    	
    	if ( bNewCooking != m_bIsCooking )
    	{			
    		m_bIsCooking = bNewCooking;
		
    		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    	}
    	
    	FCBlockUnfiredBrick brickBlock = FCBetterThanWolves.fcBlockUnfiredBrick;
    	
    	if ( m_bIsCooking )
    	{
    		m_iCookCounter++;
    		
    		if ( m_iCookCounter >= m_iTimeToCook )
    		{
    			brickBlock.OnFinishedCooking( worldObj, xCoord, yCoord, zCoord );
    			
    			return;
    		}
    	}
    	else
    	{
    		if ( IsRainingOnBrick( worldObj, xCoord, yCoord, zCoord ) )
    		{
    			m_iCookCounter -= m_iRainCookDecay;
    			
    			if ( m_iCookCounter < 0 )
    			{
    				m_iCookCounter = 0;
    			}
    		}    		
    	}
    	
    	int iDisplayedCookLevel = brickBlock.GetCookLevel( worldObj, xCoord, yCoord, zCoord );
    	int iCurrentCookLevel = ComputeCookLevel();;
		
    	if ( iDisplayedCookLevel != iCurrentCookLevel )
    	{
    		brickBlock.SetCookLevel( worldObj, xCoord, yCoord, zCoord, iCurrentCookLevel );
    	}
    }
    
    public boolean IsRainingOnBrick( World world, int i, int j, int k )
    {
    	return world.isRaining() && world.IsRainingAtPos( i, j, k );
    }
    
    private int ComputeCookLevel()
    {
    	if ( m_iCookCounter > 0 )
		{
			int iCookLevel= (int)( ( (float)m_iCookCounter / (float)m_iTimeToCook ) * 7F ) + 1;
			
			return MathHelper.clamp_int( iCookLevel, 0, 7 );
		}
    	
    	return 0;
    }
    
    //----------- Client Side Functionality -----------//
}
