// FCMOD

package net.minecraft.src;

public class FCItemBlockSlab extends ItemBlock
{
    public FCItemBlockSlab( int iItemID )
    {
        super( iItemID );
        
        setHasSubtypes( true );        
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        if ( itemStack.stackSize == 0)
        {
            return false;
        }

        if ( !player.canPlayerEdit( i, j, k, iFacing, itemStack ) )
        {
            return false;
        }

        if ( attemptToCombineWithBlock( itemStack, player, world, i, j, k, iFacing, true ) )
        {
        	return true;
        }

    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
    	targetPos.AddFacingAsOffset( iFacing );
    	
        if ( attemptToCombineWithBlock( itemStack, player, world, targetPos.i, targetPos.j, targetPos.k, iFacing, false ) )
        {
            return true;
        }
        else
        {
            return super.onItemUse( itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ );
        }
    }
    
    //------------- Class Specific Methods ------------//
    
    public boolean attemptToCombineWithBlock( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, boolean bFacingTest )
    {
        if ( canCombineWithBlock( world, i, j, k, itemStack.getItemDamage() ) )
        {    		
	        int iTargetBlockID = world.getBlockId( i, j, k );
	        Block targetBlock = Block.blocksList[iTargetBlockID];
	        
	        if ( targetBlock != null && ( targetBlock instanceof FCBlockSlab ) )
	        {        
	        	FCBlockSlab slabBlock = (FCBlockSlab)targetBlock;
	        	
	        	boolean bIsTargetUpsideDown = slabBlock.GetIsUpsideDown( world, i, j, k );
	        	
		        if ( !bFacingTest || ( iFacing == 1 && !bIsTargetUpsideDown ) || ( iFacing == 0 && bIsTargetUpsideDown ) )
		        {
		            if ( world.checkNoEntityCollision( Block.GetFulBlockBoundingBoxFromPool( world, i, j, k ) ) )
		            {
		            	if ( convertToFullBlock( world, i, j, k ) )
		            	{		            		
			                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, slabBlock.stepSound.getStepSound(), 
			                		( slabBlock.stepSound.getVolume() + 1.0F) / 2.0F, slabBlock.stepSound.getPitch() * 0.8F);
			            	
			                itemStack.stackSize--;
			                
			            	Block newBlock = Block.blocksList[world.getBlockId( i, j, k )];
			            	
			            	if ( newBlock != null )
			            	{
			    	            // Panick animals when blocks are placed near them
			    	            world.NotifyNearbyAnimalsOfPlayerBlockAddOrRemove( player, newBlock, i, j, k );
			            	}
		            	}
		            }
		            
		            // if placement fails due to entity collision, still "use" the item without placing the block
		            
		            return true;
		        }
	        }
        }
        
        return false;
    }
    
    public boolean canCombineWithBlock( World world, int i, int j, int k, int iItemDamage )
    {
        int iBlockID = world.getBlockId( i, j, k );
        
        if ( iBlockID == getBlockID() )
        {
        	Block block = Block.blocksList[iBlockID];
        	
        	if ( block instanceof FCBlockSlab )
        	{
	            int iMetadata = ((FCBlockSlab)block).SetIsUpsideDown( world.getBlockMetadata( i, j, k ), false );
	            
	            if ( iMetadata == iItemDamage )
	            {
	            	return true;
	            }
        	}
        }
        
    	return false;
    }
    
    public boolean convertToFullBlock( World world, int i, int j, int k )
    {
        int iBlockID = world.getBlockId( i, j, k );        
    	Block block = Block.blocksList[iBlockID];
    	
    	if ( block instanceof FCBlockSlab )
    	{
        	return ((FCBlockSlab)block).ConvertToFullBlock( world, i, j, k );
        }
        	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
	
    @Override
    public boolean canPlaceItemBlockOnSide( World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack itemStack )
    {
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
        if ( canCombineWithBlock( world, targetPos.i, targetPos.j, targetPos.k, itemStack.getItemDamage() ) )
        {
            int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
            
        	FCBlockSlab slab = (FCBlockSlab)Block.blocksList[iTargetBlockID];
        	
        	boolean bIsUpsideDown = slab.GetIsUpsideDown( world, targetPos.i, targetPos.j, targetPos.k );
        	
            if ( ( iFacing == 1 && !bIsUpsideDown ) || ( iFacing == 0 && bIsUpsideDown ) )
            {
            	return true;
            }
        }
        
        targetPos.AddFacingAsOffset( iFacing );
        
        if ( canCombineWithBlock( world, targetPos.i, targetPos.j, targetPos.k, itemStack.getItemDamage() ) )
        {
        	return true;
        }
        
        return super.canPlaceItemBlockOnSide( world, i, j, k, iFacing, player, itemStack );
    }
}