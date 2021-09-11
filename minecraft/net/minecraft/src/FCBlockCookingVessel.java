// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public abstract class FCBlockCookingVessel extends FCBlockVessel
{
    public FCBlockCookingVessel( int iBlockID, Material material )
    {
        super( iBlockID, material );        
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        FCUtilsInventory.EjectInventoryContents( world, i, j, k, (IInventory)world.getBlockTileEntity(i, j, k) );

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }    

	@Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( world.isRemote )
		{
			// don't collect items on the client, as it's dependent on the state of the inventory
			
			return;
		}
		
        List collisionList = null;
        
        if ( GetMechanicallyPoweredFlag( world, i, j, k ) )
        {
        	// tilted blocks can't collect
        	
        	return;
        }
        
        // check for items within the collection zone       
        
        collisionList = world.getEntitiesWithinAABB( EntityItem.class, 
    		AxisAlignedBB.getAABBPool().getAABB( (float)i, (float)j + m_dCollisionBoxHeight, (float)k, 
				(float)(i + 1), (float)j + m_dCollisionBoxHeight + 0.05F, (float)(k + 1)) );

    	if ( collisionList != null && collisionList.size() > 0 )
    	{
    		TileEntity tileEnt = world.getBlockTileEntity( i, j, k );
    		
    		if ( !( tileEnt instanceof IInventory ) )
    		{
    			return;
    		}
    	
            IInventory inventoryEntity = (IInventory)tileEnt;
            
            for ( int listIndex = 0; listIndex < collisionList.size(); listIndex++ )
            {
	    		EntityItem targetEntityItem = (EntityItem)collisionList.get( listIndex );
	    		
		        if ( !targetEntityItem.isDead )
		        {
        			if ( FCUtilsInventory.AddItemStackToInventory( inventoryEntity, targetEntityItem.getEntityItem() ) )
        			{
			            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
			            		"random.pop", 0.25F, 
			            		((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			            
			            targetEntityItem.setDead();			            
        			}
		        }		        
            }
    	}
    }
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
		ValidateFireUnderState( world, i, j, k );
		
		super.updateTick( world, i, j, k, rand );
		
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		ValidateFireUnderState( world, i, j, k );
		
		super.onNeighborBlockChange( world, i, j, k, iBlockID );
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	if ( !IsOpenSideBlocked( world, i, j, k ) )
    	{
	        if ( !world.isRemote )
	        {
	        	TileEntity tileEnt = world.getBlockTileEntity( i, j, k );
	        	
	        	if ( tileEnt instanceof FCTileEntityCookingVessel )
	        	{        	
	        		FCTileEntityCookingVessel vesselEntity = (FCTileEntityCookingVessel)world.getBlockTileEntity( i, j, k );
		            
		        	if ( player instanceof EntityPlayerMP ) // should always be true
		        	{
		        		FCContainerCookingVessel container = new FCContainerCookingVessel( player.inventory, vesselEntity );
		        		
		        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, GetContainerID() );
		        	}
	        	}
	        }
    	}
                
        return true;
    }

	@Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory(((IInventory) par1World.getBlockTileEntity(par2, par3, par4)));
    }

    //------------- Class Specific Methods -------------//

	abstract protected void ValidateFireUnderState( World world, int i, int j, int k );
	
	abstract protected int GetContainerID();	
}