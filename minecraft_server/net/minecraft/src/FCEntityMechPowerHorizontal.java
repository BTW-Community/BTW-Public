// FCMOD

package net.minecraft.src;

public abstract class FCEntityMechPowerHorizontal extends FCEntityMechPower
{
	// local vars
	
    public boolean m_bIAligned;
    
    public FCEntityMechPowerHorizontal( World world )
    {
        super( world );
        
        m_bIAligned = true;
    }
    
    public FCEntityMechPowerHorizontal( World world, double x, double y, double z, boolean bIAligned  ) 
    {
    	super( world, x, y, z );
        
        m_bIAligned = bIAligned;
        
        InitBoundingBox();
    }
    
	@Override
    public void setDead()
    {
    	if ( m_bProvidingPower )
    	{        	
        	int iCenterI = MathHelper.floor_double( posX );
        	int iCenterJ = MathHelper.floor_double( posY );
        	int iCenterK = MathHelper.floor_double( posZ );
        	
        	int iCenterBlockID = worldObj.getBlockId( iCenterI, iCenterJ, iCenterK );

        	if ( iCenterBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
        	{
        		// temporarily remove the center axle to prevent power-transfer problems if we're powering
        		// in both directions
        		
        		int iAxisAlignment = ( (FCBlockAxle)FCBetterThanWolves.fcBlockAxlePowerSource ).GetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK );
        		
                worldObj.setBlockWithNotify( iCenterI, iCenterJ, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID );
                
                ( (FCBlockAxle)FCBetterThanWolves.fcBlockAxle).SetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment );
        	}
    	}
    	
        super.setDead();
    }
    
    //------------- FCEntityMechPower ------------//
	
    @Override
    protected boolean ValidateConnectedAxles()
    {
    	// verify we still have an axle at our center
    	
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	int iCenterBlockID = worldObj.getBlockId( iCenterI, iCenterJ, iCenterK );

    	if ( !FCUtilsMechPower.IsBlockIDAxle( iCenterBlockID ) )
    	{
    		return false;
    	}
    	
    	FCBlockAxle centerAxleBlock = (FCBlockAxle)Block.blocksList[iCenterBlockID];
    	
		// make sure the center block is still properly oriented
		
    	int iAxisAlignment = centerAxleBlock.GetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK );
    	
    	if ( iAxisAlignment == 0 || ( iAxisAlignment == 1 && m_bIAligned ) || ( iAxisAlignment == 2 && !m_bIAligned ) )
		{
    		return false;
		}            		
    	
    	if ( !m_bProvidingPower )
    	{
    		if ( iCenterBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
    		{
    			worldObj.setBlockWithNotify( iCenterI, iCenterJ, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID );
    			
    			((FCBlockAxle)FCBetterThanWolves.fcBlockAxle).SetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment );
    		}
    		else if ( centerAxleBlock.GetPowerLevel( worldObj, iCenterI, iCenterJ, iCenterK ) > 0 )
    		{
    			// we have an unpowered device on a powered axle
    			
        		return false;
    		}
    	}
    	else
    	{
    		if ( iCenterBlockID == FCBetterThanWolves.fcBlockAxle.blockID )
    		{
    			// we have powered device on a unpowered axle.  Restore power (this is likely the result of a player-rotated axle or Gear Box).
    			
    			worldObj.setBlockWithNotify( iCenterI, iCenterJ, iCenterK, FCBetterThanWolves.fcBlockAxlePowerSource.blockID );
    			
    			((FCBlockAxle)FCBetterThanWolves.fcBlockAxlePowerSource).SetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment );
    		}    		
    	}
    	
    	return true;    	
    }
    
    @Override
    protected void TransferPowerStateToConnectedAxles()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );
    	
    	int iCenterBlockID = worldObj.getBlockId( iCenterI, iCenterJ, iCenterK );
    	
    	FCBlockAxle centerAxleBlock = (FCBlockAxle)Block.blocksList[iCenterBlockID];
    	
    	int iAxisAlignment = centerAxleBlock.GetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK );
    	
    	if ( m_bProvidingPower )
    	{
    		if ( iCenterBlockID == FCBetterThanWolves.fcBlockAxle.blockID )
    		{
    			worldObj.setBlockWithNotify( iCenterI, iCenterJ, iCenterK, FCBetterThanWolves.fcBlockAxlePowerSource.blockID );
    			
    			((FCBlockAxle)FCBetterThanWolves.fcBlockAxlePowerSource).SetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment );
    		}
    	}
    	else
    	{
    		if ( iCenterBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID )
    		{
    			worldObj.setBlockWithNotify( iCenterI, iCenterJ, iCenterK, FCBetterThanWolves.fcBlockAxle.blockID );
    			
    			((FCBlockAxle)FCBetterThanWolves.fcBlockAxle).SetAxisAlignment( worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment );
    		}
    	}    	
    }
    
    //------------- Class Specific Methods ------------//

    protected void InitBoundingBox()
    {
        if ( m_bIAligned )
        {
        	boundingBox.setBounds( posX - ( GetDepth() * 0.5F ), posY - ( GetHeight() * 0.5F ), posZ - ( GetWidth() * 0.5F ),
    			posX + ( GetDepth() * 0.5F ), posY + ( GetHeight() * 0.5F ), posZ + ( GetWidth() * 0.5F ) );
        			
        }
        else
        {
        	boundingBox.setBounds( posX - ( GetWidth() * 0.5F ), posY - ( GetHeight() * 0.5F ), posZ - ( GetDepth() * 0.5F ),
    			posX + ( GetWidth() * 0.5F ), posY + ( GetHeight() * 0.5F ), posZ + ( GetDepth() * 0.5F ) );
        }
    }
    
    protected AxisAlignedBB GetDeviceBounds()
    {	
        if ( m_bIAligned )
        {
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		posX - ( GetDepth() * 0.5F ), posY - ( GetHeight() * 0.5F ), posZ - ( GetWidth() * 0.5F ),
    			posX + ( GetDepth() * 0.5F ), posY + ( GetHeight() * 0.5F ), posZ + ( GetWidth() * 0.5F ) );
        			
        }
        else
        {
        	return AxisAlignedBB.getAABBPool().getAABB( 
        		posX - ( GetWidth() * 0.5F ), posY - ( GetHeight() * 0.5F ), posZ - ( GetDepth() * 0.5F ),
    			posX + ( GetWidth() * 0.5F ), posY + ( GetHeight() * 0.5F ), posZ + ( GetDepth() * 0.5F ) );
        }
    }
}
