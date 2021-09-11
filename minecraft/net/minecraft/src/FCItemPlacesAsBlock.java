// FCMOD

package net.minecraft.src;

public class FCItemPlacesAsBlock extends Item
{
	protected int m_iBlockID;
	protected int m_iBlockMetadata = 0;
	
	protected boolean m_bRequireNoEntitiesInTargetBlock = false;
	
    public FCItemPlacesAsBlock( int iItemID, int iBlockID )
    {
    	super( iItemID );
    	
    	m_iBlockID = iBlockID;
    }
    
    public FCItemPlacesAsBlock( int iItemID, int iBlockID, int iBlockMetadata )
    {
    	this( iItemID, iBlockID );
    	
    	m_iBlockMetadata = iBlockMetadata;
    }
    
    public FCItemPlacesAsBlock( int iItemID, int iBlockID, int iBlockMetadata, String sItemName )
    {
    	this( iItemID, iBlockID, iBlockMetadata );
    	
    	setUnlocalizedName( sItemName );
    }
    
    /**
     * This constructor should only be called by ItemBlock child class
     */
    protected FCItemPlacesAsBlock( int iItemID )
    {
    	super( iItemID );

    	m_iBlockID = iItemID + 256;        
    	m_iBlockMetadata = 0;
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        int iNewBlockID = GetBlockIDToPlace( itemStack.getItemDamage(), iFacing, fClickX, fClickY, fClickZ );
        
        if ( itemStack.stackSize == 0 ||
        	( player != null && !player.canPlayerEdit( i, j, k, iFacing, itemStack ) ) ||        	
    		( j == 255 && Block.blocksList[iNewBlockID].blockMaterial.isSolid() ) )
        {
            return false;
        }
        
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		
		int iOldBlockID = world.getBlockId( i, j, k );
        Block oldBlock = Block.blocksList[iOldBlockID];
		
        if ( oldBlock != null )
        {
        	if ( oldBlock.IsGroundCover( ) )
        	{
        		iFacing = 1;
        	}
        	else if ( !oldBlock.blockMaterial.isReplaceable() )
        	{
        		targetPos.AddFacingAsOffset( iFacing );
        	}
        }

        if ( ( !m_bRequireNoEntitiesInTargetBlock || IsTargetFreeOfObstructingEntities( world, targetPos.i, targetPos.j, targetPos.k ) ) && 
        	world.canPlaceEntityOnSide( iNewBlockID, targetPos.i, targetPos.j, targetPos.k, false, iFacing, player, itemStack ) )
        {
            Block newBlock = Block.blocksList[iNewBlockID];
            
        	int iNewMetadata = getMetadata( itemStack.getItemDamage() );
        	
        	iNewMetadata = newBlock.onBlockPlaced( world, targetPos.i, targetPos.j, targetPos.k, iFacing, fClickX, fClickY, fClickZ, iNewMetadata );

        	iNewMetadata = newBlock.PreBlockPlacedBy( world, targetPos.i, targetPos.j, targetPos.k, iNewMetadata, player );            
            
            if ( world.setBlockAndMetadataWithNotify( targetPos.i, targetPos.j, 
        		targetPos.k, iNewBlockID, iNewMetadata ) )
            {
                if ( world.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == iNewBlockID )
                {
                    newBlock.onBlockPlacedBy( world, targetPos.i, targetPos.j, 
                		targetPos.k, player, itemStack );
                    
                    newBlock.onPostBlockPlaced( world, targetPos.i, targetPos.j, targetPos.k, iNewMetadata );
                    
                    // Panick animals when blocks are placed near them
                    world.NotifyNearbyAnimalsOfPlayerBlockAddOrRemove( player, newBlock, targetPos.i, targetPos.j, targetPos.k );            
                }
                
                PlayPlaceSound( world, targetPos.i, targetPos.j, targetPos.k, newBlock );
                
                itemStack.stackSize--;
            }
            
        	return true;    	
        }
        
    	return false;    	
    }
    
    @Override
    public int getMetadata( int iItemDamage )
    {
    	return m_iBlockMetadata;
    }
    
    @Override
    public boolean CanItemBeUsedByPlayer( World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack )
    {
    	return canPlaceItemBlockOnSide( world, i, j, k, iFacing, player, stack );
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    	int iTargetDirection = GetTargetFacingPlacedByBlockDispenser( iFacing );    	
    	
    	int iBlockID = GetBlockIDToPlace( stack.getItemDamage(), iTargetDirection, 
    		0.5F, 0.25F, 0.5F ); // equivalent to lower half of the middle of the block
    	
    	Block newBlock = Block.blocksList[iBlockID];

    	if ( newBlock != null && world.canPlaceEntityOnSide( iBlockID, 
    		targetPos.i, targetPos.j, targetPos.k, true, iTargetDirection, null, stack ) )
        {
    		int iBlockMetadata = getMetadata( stack.getItemDamage() );
        	
    		iBlockMetadata = newBlock.onBlockPlaced( 
    			world, targetPos.i, targetPos.j, targetPos.k, iTargetDirection,
    			0.5F, 0.25F, 0.5F, // equivalent to lower half of the middle of the block
    			iBlockMetadata );
        	
        	world.setBlockAndMetadataWithNotify( targetPos.i, targetPos.j, targetPos.k, iBlockID, 
        			iBlockMetadata );
        	
            newBlock.onPostBlockPlaced( world, targetPos.i, targetPos.j, targetPos.k, 
            	iBlockMetadata );
            
            world.playAuxSFX( FCBetterThanWolves.m_iBlockPlaceAuxFXID, i, j, k, iBlockID );                            

			return true;
        }       
    	
    	return false;
	}
	
	//------------- Class Specific Methods ------------//
    
    public int getBlockID()
    {
        return m_iBlockID;
    }

    public boolean canPlaceItemBlockOnSide( World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack )
    {
        int iTargetBlockID = world.getBlockId( i, j, k );
        Block iTargetBlock = Block.blocksList[iTargetBlockID];
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );

        if ( iTargetBlock != null )
        {
        	if ( iTargetBlock.IsGroundCover( ) )
        	{
        		iFacing = 1;
        	}
        	else if ( !iTargetBlock.blockMaterial.isReplaceable() )
        	{
        		targetPos.AddFacingAsOffset( iFacing );
        	}
        }        

        // the following places the click spot right in the center, which while technically not correct, shouldn't
        // make much of a difference given vanilla ignores it entirely        
        int iNewBlockID = GetBlockIDToPlace( stack.getItemDamage(), iFacing, 0.5F, 0.5F, 0.5F );
        
        return world.canPlaceEntityOnSide( iNewBlockID, targetPos.i, targetPos.j, targetPos.k, false, iFacing, (Entity)null, stack);
    }
    
    public FCItemPlacesAsBlock SetAssociatedBlockID( int iBlockID )
    {
    	m_iBlockID = iBlockID;
    	
    	return this;
    }
    
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	return getBlockID();
    }
    
    protected boolean IsTargetFreeOfObstructingEntities( World world, int i, int j, int k )
    {
    	AxisAlignedBB blockBounds = AxisAlignedBB.getAABBPool().getAABB(
        	(double)i, (double)j, (double)k, (double)(i + 1), (double)( j + 1 ), (double)(k + 1) );
    	
    	return world.checkNoEntityCollision( blockBounds );
    }
    
    protected void PlayPlaceSound( World world, int i, int j, int k, Block block )
    {
        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, block.stepSound.getPlaceSound(), 
        	( block.stepSound.getVolume() + 1F ) / 2F, block.stepSound.getPitch() * 0.8F );
    }
    
	public int GetTargetFacingPlacedByBlockDispenser( int iDispenserFacing )
    {
    	return Block.GetOppositeFacing( iDispenserFacing );
    }
}
