// FCMOD

package net.minecraft.src;

public abstract class FCTileEntityBasket extends TileEntity
{
    protected static final float m_fLidOpenRate = 0.1F;
    protected static final float m_fLidCloseRate = 0.2F;
    
	protected static final float m_fMaxKeepOpenRange = 8F;
	
	public float m_fLidOpenRatio = 0F;
	public float m_fPrevLidOpenRatio = 0F;
	
	public boolean m_bClosing = false;
	
	FCBlockBasket m_blockBasket;
	
    public FCTileEntityBasket( FCBlockBasket blockBasket )
    {
    	super();
    	
    	m_blockBasket = blockBasket;
    }
    
    @Override
    public void updateEntity()
    {
    	super.updateEntity();   

		UpdateOpenState();
    }
    
    @Override
    public boolean receiveClientEvent( int iEventType, int iEventParam )
    {
        if ( iEventType == 1)
        {
        	m_bClosing = iEventParam == 1;
        	
            return true;
        }
        
        return super.receiveClientEvent( iEventType, iEventParam );
    }
    
    //------------- Class Specific Methods ------------//
    
    public abstract void EjectContents();
    
    public void StartClosingServerSide()
    {
		m_bClosing = true;
		
        worldObj.addBlockEvent( xCoord, yCoord, zCoord, getBlockType().blockID, 1, 1 );
    }
    
    private void UpdateOpenState()
    {
    	m_fPrevLidOpenRatio = m_fLidOpenRatio;
    	
    	if ( m_blockBasket.GetIsOpen( worldObj, xCoord, yCoord, zCoord ) )
    	{
    		if ( m_bClosing )
    		{
    			m_fLidOpenRatio -= m_fLidCloseRate;
    			
    			if ( m_fLidOpenRatio <= 0F )
    			{
    				m_fLidOpenRatio = 0F;
    				
    				if ( !worldObj.isRemote )
    				{
    					m_blockBasket.SetIsOpen( worldObj, xCoord, yCoord, zCoord, false );
    					
    					OnFinishedClosing();
    				}
    			}
    		}
    		else
    		{
	    		if ( ShouldStartClosingServerSide() )
				{
	    			StartClosingServerSide();
				}
	    		else
	    		{
	    			m_fLidOpenRatio += m_fLidOpenRate;
	    			
	    			if ( m_fLidOpenRatio > 1F )
	    			{
	    				m_fLidOpenRatio = 1F;
	    			}
	    		}
    		}
    	}
    	else
    	{
    		m_bClosing = false;
    		m_fLidOpenRatio = 0F;
    	}
    }
    
    public abstract boolean ShouldStartClosingServerSide();
    
    protected void OnFinishedClosing()
    {
		worldObj.playSoundEffect( xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
			"step.gravel", 0.1F + ( worldObj.rand.nextFloat() * 0.1F ), 
    		1F + ( worldObj.rand.nextFloat() * 0.25F ) );		
    }
}
