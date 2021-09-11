//FCMOD 

package net.minecraft.src;

public class FCItemBlockCompanionCube extends ItemBlock
{
    public FCItemBlockCompanionCube( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        setUnlocalizedName( "fcCompanionCube" );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
    	if ( getIsSlab( iItemDamage ) )
    	{
    		return 8;
    	}
    	
        return 0;
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	if ( itemstack.getItemDamage() > 0 )
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("slab").toString();
    	}
    	else
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("cube").toString();
    	}
    }
    
    // Much of the slab code in this file is ripped directly from FCItemBlockSlab, modified to handle the subtypes.  From this point forward, most of the code in this file
    // just deals with the slabs
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
    	if ( !getIsSlab( itemStack.getItemDamage() ) )
    	{
            return super.onItemUse( itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ );
            
    	}
    	
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
    
    public boolean getIsSlab( int iItemDamage )
    {
    	return iItemDamage != 0;
    }
    
    public boolean canCombineWithBlock( World world, int i, int j, int k, int iItemDamage )
    {
        int iBlockID = world.getBlockId( i, j, k );

        if ( getIsSlab( iItemDamage ) )
        {
	        if ( iBlockID == FCBetterThanWolves.fcCompanionCube.blockID )
	        {
	        	FCBlockCompanionCube companionCube = (FCBlockCompanionCube)(Block.blocksList[iBlockID]);

	        	if ( companionCube != null )
	        	{
	        		if ( companionCube.GetIsSlab( world, i, j, k ) )
	        		{
		            	return true;
		        	}
	        	}
	        }
        }
        
    	return false;
    }
    
    public boolean convertToFullBlock( EntityPlayer player, World world, int i, int j, int k )
    {
        int iBlockID = world.getBlockId( i, j, k );
        
        if ( iBlockID == FCBetterThanWolves.fcCompanionCube.blockID )
        {
        	FCBlockCompanionCube companionCube = (FCBlockCompanionCube)(Block.blocksList[iBlockID]);

        	if ( companionCube != null )
        	{
    			if ( companionCube.GetIsSlab( world, i, j, k ) )
    			{
                    // Companion cubes love to be reassembled                   

		        	companionCube.SpawnHearts( world, i, j, k );
		    		
	                world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, companionCube.stepSound.getStepSound(), 
                		( companionCube.stepSound.getVolume() + 1.0F) / 2.0F, companionCube.stepSound.getPitch() * 0.8F);
	            	
		        	int iTargetBlockID = FCBetterThanWolves.fcCompanionCube.blockID;
		        	int iTargetMetadata = 0; 
		        	
					int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacingReversed( player );
					
					iTargetMetadata = companionCube.SetFacing( iTargetMetadata, iFacing );
			        
		        	return world.setBlockAndMetadataWithNotify( i, j, k, iTargetBlockID, iTargetMetadata );
    			}
        	}
        }
        	
    	return false;
    }
    
    public boolean isSlabUpsideDown( int iBlockID, int iMetadata )
    {
    	if ( iBlockID == FCBetterThanWolves.fcCompanionCube.blockID )
    	{
        	FCBlockCompanionCube companionCube = (FCBlockCompanionCube)(Block.blocksList[iBlockID]);

        	if ( companionCube != null )
        	{
    			return companionCube.GetIsUpsideDownSlabFromMetadata( iMetadata );
        	}
    	}
    			
		return false;
    }
    
    public boolean attemptToCombineWithBlock( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, boolean bFacingTest )
    {
        if ( canCombineWithBlock( world, i, j, k, itemStack.getItemDamage() ) )
        {    		
	        int iTargetBlockID = world.getBlockId( i, j, k );
	        Block targetBlock = Block.blocksList[iTargetBlockID];
	        
	        if ( targetBlock != null  )
	        {        
	        	int iTargetMetadata = world.getBlockMetadata( i, j, k );
	        	
	        	boolean bIsTargetUpsideDown = isSlabUpsideDown( iTargetBlockID, iTargetMetadata );
	        	
		        if ( !bFacingTest || ( iFacing == 1 && !bIsTargetUpsideDown ) || ( iFacing == 0 && bIsTargetUpsideDown ) )
		        {
		            if ( world.checkNoEntityCollision( targetBlock.getCollisionBoundingBoxFromPool( world, i, j, k ) ) )
		            {
		            	if ( convertToFullBlock( player, world, i, j, k ) )
		            	{		            		
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
    
	//----------- Client Side Functionality -----------//
	
    @Override
    public boolean canPlaceItemBlockOnSide( World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack itemStack )
    {
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
        if ( canCombineWithBlock( world, targetPos.i, targetPos.j, targetPos.k, itemStack.getItemDamage() ) )
        {
            int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
            int iTargetMetadata = world.getBlockMetadata( targetPos.i, targetPos.j, targetPos.k );
            
        	boolean bIsUpsideDown = isSlabUpsideDown( iTargetBlockID, iTargetMetadata );
        	
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