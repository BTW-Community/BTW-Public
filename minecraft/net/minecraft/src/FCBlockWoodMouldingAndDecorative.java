// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockWoodMouldingAndDecorative extends FCBlockMouldingAndDecorative
{
    public final static int m_iOakTableTopTextureID = 93;
    public final static int m_iOakTableLegTextureID = 94;
    
	protected FCBlockWoodMouldingAndDecorative( int iBlockID, String sTextureName, String sColumnSideTextureName, int iMatchingCornerBlockID, String name )
	{
		super( iBlockID,  FCBetterThanWolves.fcMaterialPlanks, 
			sTextureName, sColumnSideTextureName, iMatchingCornerBlockID, 
			2F, 5F, Block.soundWoodFootstep, name );		
    	
        SetAxesEffectiveOn( true );        
        
        SetBuoyancy( 1F );
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.PLANKS_OAK.m_iBurnTime / 4 );
        
        SetFireProperties( 5, 20 );
	}
	
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		if ( IsDecorative( iMetadata ) )
		{
			return FCBetterThanWolves.fcBlockWoodMouldingDecorativeItemStubID;
		}
		
		return FCBetterThanWolves.fcBlockWoodMouldingItemStubID;
    }
	
	@Override
    public int damageDropped(int metadata) {
		return this.damageDropped(this.blockID, metadata);
    }
	
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
    	if ( IsDecorative( world, i, j, k ) )
    	{
    		return super.GetItemIDDroppedOnSaw( world, i, j, k );
    	}
    	
    	return FCBetterThanWolves.fcBlockWoodCornerItemStubID;
	}
    
	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
    	if ( IsDecorative( world, i, j, k ) )
    	{
    		return super.GetItemCountDroppedOnSaw( world, i, j, k );
    	}
    	
    	return 2;
    }
    
	@Override
    public int GetItemDamageDroppedOnSaw( World world, int i, int j, int k )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
    	if ( IsDecorative( iMetadata ) )
    	{
    		return super.GetItemDamageDroppedOnSaw( world, i, j, k );
    	}
    	
		return damageDropped( iMetadata );
    }
	
	@Override
    public boolean DoesTableHaveLeg( IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( blockID == FCBetterThanWolves.fcBlockWoodOakMouldingAndDecorative.blockID )
    	{
        	int iBlockBelowID = blockAccess.getBlockId( i, j - 1, k );
        	
    		if ( iBlockBelowID == Block.fence.blockID )
    		{
    			return true;
    		}
    	}
    	
    	return super.DoesTableHaveLeg( blockAccess, i, j, k );
    }
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        world.playAuxSFX( FCBetterThanWolves.m_iWoodBlockDestroyedAuxFXID, i, j, k, 0 );

		int iNumDropped =  GetNumSawDustDroppedForType( iMetadata );
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, iNumDropped, 0, 1F );
    }
		
	@Override
    public boolean canDropFromExplosion( Explosion explosion )
    {
        return false;
    }
    
	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		float fChanceOfPileDrop = 1.0F;
		
		if ( explosion != null )
		{
			fChanceOfPileDrop = 1.0F / explosion.explosionSize;
		}
		
		int iNumDropped =  GetNumSawDustDroppedForType( world.getBlockMetadata( i, j, k ) );
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, iNumDropped, 0, fChanceOfPileDrop );
    }
	
    //------------- Class Specific Methods ------------//
	
	protected int GetWoodTypeFromBlockID(int blockID) {
	    int woodType;
		
		if (blockID == FCBetterThanWolves.fcBlockWoodOakMouldingAndDecorative.blockID) {
			woodType = 0;
		}
		else if (blockID == FCBetterThanWolves.fcBlockWoodSpruceMouldingAndDecorative.blockID) {
			woodType = 1;
		}
		else if (blockID == FCBetterThanWolves.fcBlockWoodBirchMouldingAndDecorative.blockID) {
			woodType = 2;
		}
		else if (blockID == FCBetterThanWolves.fcBlockWoodJungleMouldingAndDecorative.blockID) {
			woodType = 3;
		}
		else { // blood
			woodType = 4;
		}
		
		return woodType;
	}
	
	public int GetNumSawDustDroppedForType( int iMetadata )
	{
    	if ( IsDecorative( iMetadata ) )
    	{
    		return 2;
    	}
    	
		return 1; // moulding
	}
	
	private int damageDropped(int blockID, int metadata) {
		int woodType = this.GetWoodTypeFromBlockID(blockID);
		
    	if (!IsDecorative(metadata)) {
    		return woodType;
    	}
    	
		int iBlockType;
		
	    if (metadata == m_iSubtypeColumn) {
	    	iBlockType =  FCItemBlockWoodMouldingDecorativeStub.m_iTypeColumn;
	    }
	    else if (metadata == m_iSubtypePedestalUp || metadata == m_iSubtypePedestalDown) {
	    	iBlockType = FCItemBlockWoodMouldingDecorativeStub.m_iTypePedestal;
	    }
	    else { // table
	    	iBlockType = FCItemBlockWoodMouldingDecorativeStub.m_iTypeTable;
	    }
	    
	    return FCItemBlockWoodMouldingDecorativeStub.GetItemDamageForType(woodType, iBlockType);
	}
	
	//----------- Client Side Functionality -----------//
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    	
    	if ( iMetadata == m_iSubtypeTable && blockID == FCBetterThanWolves.fcBlockWoodOakMouldingAndDecorative.blockID )
    	{
    		return RenderOakTable( renderBlocks, blockAccess, i, j, k, this );
    	}

    	return super.RenderBlock( renderBlocks, i, j, k );
    }
    
    static public boolean RenderOakTable
    ( 
    	RenderBlocks renderBlocks, 
    	IBlockAccess blockAccess, 
    	int i, int j, int k, 
    	Block block
    )
    {
	   	FCBlockMouldingAndDecorative tableBlock = (FCBlockMouldingAndDecorative)block;
		
	    // render top
	    
	    renderBlocks.setRenderBounds( 0.0F, 1.0F - m_dTableTopHeight, 0.0F,
	    	1.0F, 1.0F, 1.0F );
	
	    FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakTop );
	   
	    if ( tableBlock.DoesTableHaveLeg( blockAccess, i, j, k ) )
	    {
		    // render leg
		   
		    renderBlocks.setRenderBounds( 0.5F - m_dTableLegHalfWidth, 0.0F, 0.5F - m_dTableLegHalfWidth,
			    0.5F + m_dTableLegHalfWidth, m_dTableLegHeight, 0.5F + m_dTableLegHalfWidth );
		   
		    FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, block, i, j, k, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakLeg );		   
	    }	   
	   
		return true;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	if ( blockID == FCBetterThanWolves.fcBlockWoodMouldingDecorativeItemStubID )
    	{
    		Block block = this;
    	
    		int iItemType = FCItemBlockWoodMouldingDecorativeStub.GetBlockType( iItemDamage );
			int iWoodType = FCItemBlockWoodMouldingDecorativeStub.GetWoodType( iItemDamage );		
    		
    		if ( iItemType == FCItemBlockWoodMouldingDecorativeStub.m_iTypeColumn )
    		{
    			iItemDamage = m_iSubtypeColumn;
    		}
    		else if ( iItemType == FCItemBlockWoodMouldingDecorativeStub.m_iTypePedestal )
    		{
    			iItemDamage = m_iSubtypePedestalUp;
    		}
    		else // table
    		{
    			iItemDamage = m_iSubtypeTable;
    		}
    		
    		if ( iWoodType == 0 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodOakMouldingAndDecorative;
    		}
    		else if ( iWoodType == 1 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodSpruceMouldingAndDecorative;
    		}
    		else if ( iWoodType == 2 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodBirchMouldingAndDecorative;
    		}
    		else if ( iWoodType == 3 )
    		{
    			block = FCBetterThanWolves.fcBlockWoodJungleMouldingAndDecorative;
    		}   
    		else // 4 
    		{
    			block = FCBetterThanWolves.fcBlockWoodBloodMouldingAndDecorative;
    		}   
    		
    		if ( iItemDamage == m_iSubtypeTable && block.blockID == FCBetterThanWolves.fcBlockWoodOakMouldingAndDecorative.blockID )
    		{
    			RenderOakTableInvBlock( renderBlocks, block );
    		}
    		else
    		{
    			RenderDecorativeInvBlock( renderBlocks, block, iItemDamage, fBrightness );    			
    		}
    	}
    	else
    	{
        	renderBlocks.setRenderBounds( GetBlockBoundsFromPoolForItemRender( iItemDamage ) );
        	
    		Icon woodTexture;
    		
    		int iWoodType = iItemDamage;
    		
	        switch ( iWoodType )
	        {
	            case 1: // spruce
	            	
	            	woodTexture = FCBetterThanWolves.fcBlockWoodSpruceMouldingAndDecorative.blockIcon;
	            	
	            	break;
	
	            case 2: // birch
	            	
	            	woodTexture = FCBetterThanWolves.fcBlockWoodBirchMouldingAndDecorative.blockIcon;
	                
	            	break;
	
	            case 3: // jungle
	            	
	            	woodTexture = FCBetterThanWolves.fcBlockWoodJungleMouldingAndDecorative.blockIcon;
	                
	            	break;
	
	            case 4: // blood
	            	
	            	woodTexture = FCBetterThanWolves.fcBlockWoodBloodMouldingAndDecorative.blockIcon;
	                
	            	break;
	
	            default: // oak
	            	
	            	woodTexture = FCBetterThanWolves.fcBlockWoodOakMouldingAndDecorative.blockIcon;
	        }
	        
    		FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, this, -0.5F, -0.5F, -0.5F, woodTexture );
    	}
    }
    
    static public void RenderOakTableInvBlock
    ( 
		RenderBlocks renderBlocks, 
		Block block
	)
    {
    	// render top
        
 	   	renderBlocks.setRenderBounds( 0.0F, 1.0F - m_dTableTopHeight, 0.0F,
 	   		1.0F, 1.0F, 1.0F );
    
 	   	FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakTop );
 	   
 	   	// render leg
	   
 	   	renderBlocks.setRenderBounds( 0.5F - m_dTableLegHalfWidth, 0.0F, 0.5F - m_dTableLegHalfWidth,
 	   		0.5F + m_dTableLegHalfWidth, m_dTableLegHeight, 0.5F + m_dTableLegHalfWidth );
    
 	   	FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, block, -0.5F, -0.5F, -0.5F, ( (FCBlockAestheticNonOpaque)FCBetterThanWolves.fcAestheticNonOpaque).m_IconTableWoodOakLeg );
    }

	@Override
    public int getDamageValue(World world, int x, int y, int z) {
		int blockID = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		int damageDropped = this.damageDropped(blockID, metadata);
		
		return damageDropped;
    }
}
