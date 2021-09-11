//FCMOD 

package net.minecraft.src;

public class FCItemBlockAestheticNonOpaque extends ItemBlock
{
    public FCItemBlockAestheticNonOpaque( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( "fcBlockAestheticNonOpaque" );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
		return iItemDamage;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	switch ( itemstack.getItemDamage() )
    	{
    		case FCBlockAestheticNonOpaque.m_iSubtypeUrn:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("urn").toString();
    			
    		case FCBlockAestheticNonOpaque.m_iSubtypeColumn:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("column").toString();
    			
    		case FCBlockAestheticNonOpaque.m_iSubtypePedestalUp:
    		case FCBlockAestheticNonOpaque.m_iSubtypePedestalDown:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("pedestal").toString();
    			
    		case FCBlockAestheticNonOpaque.m_iSubtypeTable:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("table").toString();
    			
    		case FCBlockAestheticNonOpaque.m_iSubtypeWickerSlab:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("wickerslab").toString();
                
    		case FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlab:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("whitecobbleslab").toString();
                
    		case FCBlockAestheticNonOpaque.m_iSubtypeLightningRod:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("lightningrod").toString();
                
			default:
				
				return super.getUnlocalizedName();
    	}
    }
    
    // Much of the slab code in this file is ripped directly from FCItemBlockSlab, modified to handle the subtypes.  From this point forward, most of the code in this file
    // just deals with the slabs    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
        if ( itemStack.getItemDamage() == FCBlockAestheticNonOpaque.m_iSubtypeLightningRod )
        {
        	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
        	
        	targetPos.AddFacingAsOffset( iFacing );
        	
        	if ( FCBlockAestheticNonOpaque.CanLightningRodStay( world, targetPos.i, targetPos.j, targetPos.k ) )
    		{
                return super.onItemUse( itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ );
    		}
        	
        	return false;
        }
        	
    	if ( itemStack.getItemDamage() != FCBlockAestheticNonOpaque.m_iSubtypeWickerSlab && itemStack.getItemDamage() != FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlab  )
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

    @Override
    public float GetBuoyancy( int iItemDamage )
    {
    	switch ( iItemDamage )
    	{
    		case FCBlockAestheticNonOpaque.m_iSubtypeUrn:
    		case FCBlockAestheticNonOpaque.m_iSubtypeTable:
    		case FCBlockAestheticNonOpaque.m_iSubtypeWickerSlab:
    		case FCBlockAestheticNonOpaque.m_iSubtypeGrate:
    		case FCBlockAestheticNonOpaque.m_iSubtypeWicker:
    		case FCBlockAestheticNonOpaque.m_iSubtypeSlats:
    		case FCBlockAestheticNonOpaque.m_iSubtypeWickerSlabUpsideDown:
    			
    			return 1.0F;
    			
    	}
    	
    	return super.GetBuoyancy( iItemDamage );
    }
    
    //------------- Class Specific Methods ------------//
    
    public boolean canCombineWithBlock( World world, int i, int j, int k, int iItemDamage )
    {
        int iBlockID = world.getBlockId( i, j, k );

        if ( iItemDamage == FCBlockAestheticNonOpaque.m_iSubtypeWickerSlab )
        {
	        if ( iBlockID == FCBetterThanWolves.fcAestheticNonOpaque.blockID )
	        {
	        	int iBlockMetadata = world.getBlockMetadata( i, j, k );
	        	
	        	if ( iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWickerSlab ||
	    			iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWickerSlabUpsideDown )
	        	{
	            	return true;
	        	}
	        }
        }
        else if ( iItemDamage == FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlab )
        {
	        if ( iBlockID == FCBetterThanWolves.fcAestheticNonOpaque.blockID )
	        {
	        	int iBlockMetadata = world.getBlockMetadata( i, j, k );
	        	
	        	if ( iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlab ||
	    			iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlabUpsideDown )
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
        
        if ( iBlockID == FCBetterThanWolves.fcAestheticNonOpaque.blockID )
        {
        	int iBlockMetadata = world.getBlockMetadata( i, j, k );
        	
        	if ( iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWickerSlab ||
    			iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWickerSlabUpsideDown )
        	{
	        	int iTargetBlockID = FCBetterThanWolves.fcAestheticOpaque.blockID;
	        	int iTargetMetadata = FCBlockAestheticOpaque.m_iSubtypeWicker; 
	        	
	        	return world.setBlockAndMetadataWithNotify( i, j, k, iTargetBlockID, iTargetMetadata );
        	}
        	else if ( iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlab ||
    			iBlockMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlabUpsideDown )
        	{
	        	int iTargetBlockID = FCBetterThanWolves.fcAestheticOpaque.blockID;
	        	int iTargetMetadata = FCBlockAestheticOpaque.m_iSubtypeWhiteCobble; 
	        	
	        	return world.setBlockAndMetadataWithNotify( i, j, k, iTargetBlockID, iTargetMetadata );
        	}
        }
        	
    	return false;
    }
    
    public boolean IsSlabUpsideDown( int iBlockID, int iMetadata )
    {
    	if ( iBlockID == FCBetterThanWolves.fcAestheticNonOpaque.blockID )
    	{
    		if ( iMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWickerSlabUpsideDown || 
    			iMetadata == FCBlockAestheticNonOpaque.m_iSubtypeWhiteCobbleSlabUpsideDown )
    		{
    			return true;
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
	        	
	        	boolean bIsTargetUpsideDown = IsSlabUpsideDown( iTargetBlockID, iTargetMetadata );
	        	
		        if ( !bFacingTest || ( iFacing == 1 && !bIsTargetUpsideDown ) || ( iFacing == 0 && bIsTargetUpsideDown ) )
		        {
		            if ( world.checkNoEntityCollision( Block.GetFulBlockBoundingBoxFromPool( world, i, j, k ) ) )
		            {
		            	if ( convertToFullBlock( world, i, j, k ) )
		            	{		            		
			                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, targetBlock.stepSound.getStepSound(), 
		                		( targetBlock.stepSound.getVolume() + 1.0F) / 2.0F, targetBlock.stepSound.getPitch() * 0.8F);
			            	
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
            
        	boolean bIsUpsideDown = IsSlabUpsideDown( iTargetBlockID, iTargetMetadata );
        	
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