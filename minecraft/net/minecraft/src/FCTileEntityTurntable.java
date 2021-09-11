// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCTileEntityTurntable extends TileEntity
{
	private final int m_iMaxHeightOfBlocksRotated = 2;
	
	private int m_iRotationTickCount;
	public int m_iCraftingRotationCount;
	
    private static int m_iTicksToRotate[];
    
	private int m_iSwitchOverride; // legacy support for old data value
	
    static 
    {
    	m_iTicksToRotate = (new int[] 
        { 
			10, 
			20, 40, 80, 200, 600,
			1200, 2400, 6000, 12000, 24000
        });
    }
    
    public FCTileEntityTurntable()
    {
    	m_iRotationTickCount = 0;
    	m_iCraftingRotationCount = 0;
    	
    	m_iSwitchOverride = -1;
    }
    
    @Override
    public void readFromNBT( NBTTagCompound nbttagcompound )
    {
        super.readFromNBT(nbttagcompound);
        
    	m_iRotationTickCount = nbttagcompound.getInteger( "m_iRotationCount" ); // legacy name
    	
        if ( nbttagcompound.hasKey( "m_iSwitchSetting" ) )
        {
        	// legacy data format support
        	
        	m_iSwitchOverride = nbttagcompound.getInteger( "m_iSwitchSetting" );
	    	
	    	if ( m_iSwitchOverride > 3 )
	    	{
	    		m_iSwitchOverride = 3;
	    	}
        }
    	
        if( nbttagcompound.hasKey( "m_iPotteryRotationCount" ) ) // legacy name
        {
        	m_iCraftingRotationCount = nbttagcompound.getInteger( "m_iPotteryRotationCount" ); // legacy name
        }
    	
    }
    
    @Override
    public void writeToNBT( NBTTagCompound nbttagcompound )
    {
        super.writeToNBT(nbttagcompound);
        
        nbttagcompound.setInteger( "m_iRotationCount", m_iRotationTickCount ); // legacy name        
        nbttagcompound.setInteger( "m_iPotteryRotationCount", m_iCraftingRotationCount ); // legacy name   
    }
        
    @Override
    public void updateEntity()
    {
    	// Remove the following block for the server
    	if ( worldObj.isRemote )
    	{
        	if ( ( (FCBlockTurntable)FCBetterThanWolves.fcTurntable ).
    			IsBlockMechanicalOn( worldObj, xCoord, yCoord, zCoord ) )
        	{
        		m_iRotationTickCount++;
        		
        		if ( m_iRotationTickCount >= GetTicksToRotate() )
        		{
                    worldObj.playSound( (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, 
                		"random.click", 0.05F, 1.0F );
                        
        			
            		m_iRotationTickCount = 0;    	
        		}
        	}
        	else
        	{
        		m_iRotationTickCount = 0;    	
    		}
        	
    		return;
    	}
    	
    	if ( m_iSwitchOverride >= 0 )
    	{
    		// support for legacy data format
    		
    		((FCBlockTurntable)FCBetterThanWolves.fcTurntable ).SetSwitchSetting( worldObj, xCoord, yCoord, zCoord, m_iSwitchOverride );
				
			m_iSwitchOverride = -1;
    	}
    	
        byte byte0 = 9; // standard block update range + 1 to take into account stuff that may be attached

        if ( !worldObj.checkChunksExist( xCoord - byte0, yCoord - byte0, zCoord - byte0, xCoord + byte0, yCoord + byte0, zCoord + byte0 ) )
        {
        	return;
    	}

    	if ( ( (FCBlockTurntable)FCBetterThanWolves.fcTurntable ).
			IsBlockMechanicalOn( worldObj, xCoord, yCoord, zCoord ) )
    	{
    		m_iRotationTickCount++;
    		
    		if ( m_iRotationTickCount >= GetTicksToRotate() )
    		{
    			RotateTurntable();
    			
        		m_iRotationTickCount = 0;    	
    		}
    	}
    	else
    	{
    		m_iRotationTickCount = 0;    	
		}
    }
    
	//------------- Class Specific Methods ------------//
    
    private int GetTicksToRotate()
    {
    	return m_iTicksToRotate[( (FCBlockTurntable)FCBetterThanWolves.fcTurntable ).GetSwitchSetting( worldObj, xCoord, yCoord, zCoord )];
    }
    
    private void RotateTurntable()
    {        
    	boolean bReverseDirection = ( (FCBlockTurntable)FCBetterThanWolves.fcTurntable ).
			IsBlockRedstoneOn( worldObj, xCoord, yCoord, zCoord );
    	
    	int iTempCraftingCounter = m_iCraftingRotationCount;
    	
    	for ( int iTempJ = yCoord + 1; iTempJ <= yCoord + m_iMaxHeightOfBlocksRotated; iTempJ++ )
    	{
        	Block targetBlock = Block.blocksList[worldObj.getBlockId( xCoord, iTempJ, zCoord )];

        	if ( targetBlock != null && targetBlock.CanRotateOnTurntable( worldObj, xCoord, iTempJ, zCoord ) )
        	{
	    		// have to store the transmission capacity as rotation may affect it (like with crafter blocks)
	    		
	    		boolean bCanTransmitHorizontally = targetBlock.CanTransmitRotationHorizontallyOnTurntable( worldObj, 
	    			xCoord, iTempJ, zCoord );
	    		
	    		boolean bCanTransmitVertically = targetBlock.CanTransmitRotationVerticallyOnTurntable( worldObj, 
	    			xCoord, iTempJ, zCoord );
	    		
	    		iTempCraftingCounter = targetBlock.RotateOnTurntable( worldObj, 
	    			xCoord, iTempJ, zCoord, bReverseDirection, iTempCraftingCounter );
				
		    	if ( bCanTransmitHorizontally )
		    	{
			    	RotateBlocksAttachedToBlock( xCoord, iTempJ, zCoord, bReverseDirection );
		    	}
		    	
		    	if ( !bCanTransmitVertically )
		    	{
		    		break;
		    	}
        	}
        	else
        	{
        		break;
        	}
    	}
    	
    	if ( iTempCraftingCounter > m_iCraftingRotationCount )
    	{
    		m_iCraftingRotationCount = iTempCraftingCounter;
    	}
    	else
    	{
    		m_iCraftingRotationCount = 0;
    	}
    	
    	// notify the neighbours so Buddy can pick up on this change
    	
    	worldObj.notifyBlocksOfNeighborChange( 
			xCoord, yCoord, zCoord, FCBetterThanWolves.fcTurntable.blockID );
    }   
    
	private void RotateBlocksAttachedToBlock( int i, int j, int k, boolean bReverseDirection )
	{
		int newBlockIDs[] = new int[4];
		int newMetadataArray[] = new int[4];
		
		for ( int iTempIndex = 0; iTempIndex < 4; iTempIndex++ )
		{
			newBlockIDs[iTempIndex] = 0;
			newMetadataArray[iTempIndex] = 0;
		}
		
		for ( int iTempFacing = 2; iTempFacing <=5; iTempFacing++ )
		{
			FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
			
			tempPos.AddFacingAsOffset( iTempFacing );
			
			int iTempBlockID = worldObj.getBlockId( tempPos.i, tempPos.j, tempPos.k );
			
			Block tempBlock = Block.blocksList[iTempBlockID];
			
			if ( tempBlock != null )
			{
				int iOppositeFacing = Block.GetOppositeFacing( iTempFacing );
				
				if ( tempBlock.CanRotateAroundBlockOnTurntableToFacing( worldObj, tempPos.i, tempPos.j, tempPos.k, iOppositeFacing ) )
				{
					if ( tempBlock.OnRotatedAroundBlockOnTurntableToFacing( worldObj, tempPos.i, tempPos.j, tempPos.k, iOppositeFacing  ) )
					{
						int iDestFacing = Block.RotateFacingAroundJ( iTempFacing, bReverseDirection );
						
						newBlockIDs[iDestFacing - 2] = iTempBlockID;
						
						newMetadataArray[iDestFacing - 2] = tempBlock.GetNewMetadataRotatedAroundBlockOnTurntableToFacing( 
							worldObj, tempPos.i, tempPos.j, tempPos.k, 
							iOppositeFacing, Block.GetOppositeFacing( iDestFacing ) );
						
						worldObj.setBlockWithNotify( tempPos.i, tempPos.j, tempPos.k, 0 );
					}
				}
			}
		}
		
		for ( int iTempIndex = 0; iTempIndex < 4; iTempIndex++ )
		{
			int iTempBlockID = newBlockIDs[iTempIndex];
			
			if ( iTempBlockID != 0 )
			{
				int iTempFacing = iTempIndex + 2;
				int iTempMetaData = newMetadataArray[iTempIndex];
				
				FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );				
				tempPos.AddFacingAsOffset( iTempFacing );
				
				if ( FCUtilsWorld.IsReplaceableBlock( worldObj, tempPos.i, tempPos.j, tempPos.k ) )
				{
					worldObj.setBlockAndMetadataWithNotify( tempPos.i, tempPos.j, tempPos.k, iTempBlockID, iTempMetaData );
				}
				else
				{
					// target block is occupied.  Eject rotated block as item at old location
					
					Block tempBlock = Block.blocksList[iTempBlockID];
					
					int iOldFacing = Block.RotateFacingAroundJ( iTempFacing, !bReverseDirection );
					FCUtilsBlockPos oldPos = new FCUtilsBlockPos( i, j, k );
					
					oldPos.AddFacingAsOffset( iOldFacing );
					
					tempBlock.dropBlockAsItem( worldObj, oldPos.i, oldPos.j, oldPos.k, iTempBlockID, 0 );
				}
			}
		}
	}	
}