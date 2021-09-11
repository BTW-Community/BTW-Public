// FCMOD

package net.minecraft.src;

public class FCTileEntityChest extends TileEntityChest
{
	@Override
    public void openChest()
    {
		// the possibility that this is actually a Block.chest is fine, as neighbors rarely test for block type changed
    	worldObj.notifyBlockChange( xCoord, yCoord, zCoord, FCBetterThanWolves.fcBlockChest.blockID );
    	
    	super.openChest();
    }

	@Override
    public void closeChest()
    {
		// the possibility that this is actually a Block.chest is fine, as neighbors rarely test for block type changed
    	worldObj.notifyBlockChange( xCoord, yCoord, zCoord, FCBetterThanWolves.fcBlockChest.blockID );
    	
    	super.closeChest();
    }
	
	@Override
    public void checkForAdjacentChests()
    {
        if ( !adjacentChestChecked )
        {
            adjacentChestChecked = true;
            adjacentChestZNeg = null;
            adjacentChestXPos = null;
            adjacentChestXNeg = null;
            adjacentChestZPosition = null;

        	int iBlockID = worldObj.getBlockId( xCoord, yCoord, zCoord );
        	
            if ( IsAdjacentChest( iBlockID, xCoord - 1, yCoord, zCoord ) )
            {
                adjacentChestXNeg = (TileEntityChest)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);
            }

            if ( IsAdjacentChest( iBlockID, xCoord + 1, yCoord, zCoord ) )
            {
                adjacentChestXPos = (TileEntityChest)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);
            }

            if ( IsAdjacentChest( iBlockID, xCoord, yCoord, zCoord - 1 ) )
            {
                adjacentChestZNeg = (TileEntityChest)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);
            }

            if ( IsAdjacentChest( iBlockID, xCoord, yCoord, zCoord + 1 ) )
            {
                adjacentChestZPosition = (TileEntityChest)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);
            }

            if (adjacentChestZNeg != null)
            {
                ((FCTileEntityChest)adjacentChestZNeg).ValidateNeighborConnection(this, 0);
            }

            if (adjacentChestZPosition != null)
            {
            	((FCTileEntityChest)adjacentChestZPosition).ValidateNeighborConnection(this, 2);
            }

            if (adjacentChestXPos != null)
            {
            	((FCTileEntityChest)adjacentChestXPos).ValidateNeighborConnection(this, 1);
            }

            if (adjacentChestXNeg != null)
            {
            	((FCTileEntityChest)adjacentChestXNeg).ValidateNeighborConnection(this, 3);
            }
        }
    }
    
    //------------- Class Specific Methods ------------//
	
    protected boolean IsAdjacentChest( int iBlockID, int i, int j, int k )
    {
    	int iAdjacentBlockID = worldObj.getBlockId( i, j, k );
    	
    	// weird instanceof is necessary because this is sometimes called after the block is destroyed
    	// and the neighboring block won't update correctly unless the connection is recognized   
    	if ( iBlockID == iAdjacentBlockID || !( Block.blocksList[iBlockID] instanceof BlockChest ) )
    	{
	        Block adjacentBlock = Block.blocksList[iAdjacentBlockID];
	        
	        return adjacentBlock != null && adjacentBlock instanceof BlockChest ? 
	        	((BlockChest)adjacentBlock).isTrapped == func_98041_l() : false;
    	}
    	
    	return false;
    }
    
    protected void ValidateNeighborConnection( TileEntityChest neighborEntity, int iDirection )
    {
        if ( neighborEntity.isInvalid() )
        {
            adjacentChestChecked = false;
        }
        else if ( adjacentChestChecked )
        {
            switch ( iDirection )
            {
                case 0:
                	
                    if (adjacentChestZPosition != neighborEntity)
                    {
                        adjacentChestChecked = false;
                    }

                    break;

                case 1:
                	
                    if (adjacentChestXNeg != neighborEntity)
                    {
                        adjacentChestChecked = false;
                    }

                    break;

                case 2:
                	
                    if (adjacentChestZNeg != neighborEntity)
                    {
                        adjacentChestChecked = false;
                    }

                    break;

                case 3:
                	
                    if (adjacentChestXPos != neighborEntity)
                    {
                        adjacentChestChecked = false;
                    }
            }
        }
    }
    
    public void ClearContents()
    {
        for ( int iSlot = 0; iSlot < getSizeInventory(); iSlot++ )
        {
            if ( getStackInSlot( iSlot ) != null )
            {
        		setInventorySlotContents( iSlot, null );
            }
        }
    }
    
	//------------ Client Side Functionality ----------//    
}
