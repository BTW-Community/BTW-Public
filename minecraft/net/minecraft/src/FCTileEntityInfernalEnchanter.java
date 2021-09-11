// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCTileEntityInfernalEnchanter extends TileEntity
{
	private int m_iTimeSinceLastCandleFlame[];
	public boolean m_bPlayerNear;
	
	private static final int m_iMaxTimeBetweenFlameUpdates = 10;
	
	public FCTileEntityInfernalEnchanter()
	{
		super();
		
		m_iTimeSinceLastCandleFlame = new int[4];
		
		for ( int iTemp = 0; iTemp < 4; iTemp++ )
		{
			m_iTimeSinceLastCandleFlame[iTemp] = 0;
		}
		
		m_bPlayerNear = false;
	}
	
    @Override
    public void updateEntity()
    {
        super.updateEntity();
    
        // note that this is done on the client as well, since it's entirely display related
        
        EntityPlayer entityplayer = worldObj.getClosestPlayer((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, 4.5D );
        
        if (entityplayer != null)
        {
        	if ( !m_bPlayerNear )
        	{
        		LightCandles();
        		
        		m_bPlayerNear = true;
        	}
        	else
        	{
        		UpdateCandleFlames();
        	}
        }
        else
        {
        	m_bPlayerNear = false;
        }
    }
    
    //************* Class Specific Methods ************//
    
    private void LightCandles()
    {
    	for ( int iTemp = 0; iTemp < 4; iTemp++ )
    	{
    		DisplayCandleFlameAtIndex( iTemp );
    	}
    	
        worldObj.playSoundEffect( xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "mob.ghast.fireball", 1.0F, worldObj.rand.nextFloat() * 0.4F + 0.8F );
    }
    
    private void UpdateCandleFlames()
    {
    	for ( int iTemp = 0; iTemp < 4; iTemp++ )
    	{
    		m_iTimeSinceLastCandleFlame[iTemp]++;
    		
    		if ( m_iTimeSinceLastCandleFlame[iTemp] > m_iMaxTimeBetweenFlameUpdates || worldObj.rand.nextInt( 5 ) == 0 )
    		{   		    		
    			DisplayCandleFlameAtIndex( iTemp );
    		}
    	}
    }
    
    private void DisplayCandleFlameAtIndex( int iCandleIndex )
    {
        double flameX = xCoord + ( 2.0D / 16.0D );
        double flameY = yCoord + FCBlockInfernalEnchanter.m_fBlockHeight + FCBlockInfernalEnchanter.m_fCandleHeight + 0.175F;
        double flameZ = zCoord  + ( 2.0D / 16.0D );
        
        if ( iCandleIndex == 1 || iCandleIndex == 3 )
        {
        	flameX = xCoord + ( 14.0D / 16.0D );
        }
        
        if ( iCandleIndex == 2 || iCandleIndex == 3 )
        {
        	flameZ = zCoord + ( 14.0D / 16.0D );
        }
        
        DisplayCandleFlameAtLoc( flameX, flameY, flameZ );
        
		m_iTimeSinceLastCandleFlame[iCandleIndex] = 0; 		
    }

	private void DisplayCandleFlameAtLoc( double xCoord, double yCoord, double zCoord )
	{
        worldObj.spawnParticle( "smoke", xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
        worldObj.spawnParticle( "flame", xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
	}
	
}
