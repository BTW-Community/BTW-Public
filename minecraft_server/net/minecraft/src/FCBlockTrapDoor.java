// FCMOD

package net.minecraft.src;

public class FCBlockTrapDoor extends BlockTrapDoor
{
    protected static final double m_dThickness = ( 3D / 16D );
    protected static final double m_dHalfThickness = ( m_dThickness / 2D );

    protected FCBlockTrapDoor( int iBlockID )
    {
    	super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );
    	
	    setHardness( 1.5F );
    	
        SetAxesEffectiveOn();
        
    	SetBuoyant();
    	
        InitBlockBounds( 0D, 0.5D - m_dHalfThickness, 0D, 
        	1D, 0.5D + m_dHalfThickness, 1D );
        
    	setStepSound( soundWoodFootstep );
    	
    	setUnlocalizedName( "trapdoor" );
    	
    	disableStats();    	
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	// override to prevent trap doors from responding to redstone signal and 
    	// dropping without support
    }
    
    @Override
    public void onPoweredBlockChange(World par1World, int par2, int par3, int par4, boolean par5)
    {
    	// override to prevent trap doors from responding to redstone signal
    }
    
    @Override
    public boolean canPlaceBlockOnSide( World world, int i, int j, int k, int iSide )
    {
    	return true;
    }
    
    @Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -4;
    }

    // Removed this so that mobs can reliably path through trap doors
    /*
    @Override
    public int AdjustPathWeightOnNotBlocked( int iPreviousWeight )
    {
    	return 2;
    }
    */
  
    @Override
    public boolean CanPathThroughBlock( IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder )
    {
    	if ( !pathFinder.CanPathThroughClosedWoodDoor() )
    	{
    		// note: getBlocksMovement() is misnamed and returns if the block *doesn't* block movement
    		
	    	if ( !pathFinder.CanPathThroughOpenWoodDoor() || !getBlocksMovement( blockAccess, i, j, k ) )
	    	{
	    		return false;
	    	}
    	}
    	
    	return true;
    }
    
    @Override
    public boolean IsBreakableBarricade( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }

    @Override
    public boolean IsBreakableBarricadeOpen( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return isTrapdoorOpen( blockAccess.getBlockMetadata( i, j, k ) );
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	// override because vanilla function does the opposite of what's intended
    	
        return isTrapdoorOpen(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
		// override to deprecate parent
    }
	
	@Override
    public void setBlockBoundsForItemRender()
    {
		// override to deprecate parent
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
        if ( isTrapdoorOpen( iMetadata ) )
        {
        	int iDirection = iMetadata & 3;
        	
        	switch ( iDirection )
        	{
        		case 0:
        			
	            	return AxisAlignedBB.getAABBPool().getAABB(         	
	            		0.0F, 0.0F, 1.0F - m_dThickness, 
	            		1.0F, 1.0F, 1.0F );
	
        		case 1:
        			
	            	return AxisAlignedBB.getAABBPool().getAABB(         	
	            		0.0F, 0.0F, 0.0F, 
	            		1.0F, 1.0F, m_dThickness );
	
        		case 2:
        			
	            	return AxisAlignedBB.getAABBPool().getAABB(         	
	            		1.0F - m_dThickness, 0.0F, 0.0F, 
	            		1.0F, 1.0F, 1.0F );
	
        		default: // 3
        			
	            	return AxisAlignedBB.getAABBPool().getAABB(         	
	            		0.0F, 0.0F, 0.0F, 
	            		m_dThickness, 1.0F, 1.0F );
        	}
        }
        
        if ((iMetadata & 8) != 0)
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 1.0F - m_dThickness, 0.0F, 
        		1.0F, 1.0F, 1.0F );
        }
        
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0.0F, 0.0F, 0.0F, 
    		1.0F, m_dThickness, 1.0F );
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, 
    	Vec3 startRay, Vec3 endRay )
    {
    	// copy of method in block base class to override that in parent
    	
    	AxisAlignedBB collisionBox = GetBlockBoundsFromPoolBasedOnState( 
    		world, i, j, k ).offset( i, j, k );
    	
    	MovingObjectPosition collisionPoint = collisionBox.calculateIntercept( startRay, endRay );
    	
    	if ( collisionPoint != null )
    	{
    		collisionPoint.blockX = i;
    		collisionPoint.blockY = j;
    		collisionPoint.blockZ = k;
    	}
    	
    	return collisionPoint;
    }

    @Override
    public boolean CanItemPassIfFilter( ItemStack filteredItem )
    {
    	int iFilterableProperties = filteredItem.getItem().GetFilterableProperties( filteredItem ); 
    		
    	return ( iFilterableProperties & ( Item.m_iFilterable_Small | 
    		Item.m_iFilterable_Narrow | Item.m_iFilterable_Fine ) ) != 0;
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
