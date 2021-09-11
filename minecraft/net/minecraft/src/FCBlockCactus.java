// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCactus extends BlockCactus
{
    protected FCBlockCactus( int iBlockID )
    {
    	super( iBlockID );
    	
        SetAxesEffectiveOn( true );
    	setHardness( 0.4F );
        
        SetBuoyant();
        
        setStepSound( soundClothFootstep );
        
        setUnlocalizedName( "cactus" );
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	// prevent growth in the end dimension
    	
    	if ( world.provider.dimensionId != 1 )
    	{
    		super.updateTick( world, i, j, k, rand );
    	}
    }
    
    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	// only allow replanting onto planters
    	
    	if ( super.canPlaceBlockAt( world, i, j, k ) )
    	{
    		int iBlockBelowID = world.getBlockId( i, j - 1, k );
    		
    		return iBlockBelowID == FCBetterThanWolves.fcPlanter.blockID ||
    			iBlockBelowID == FCBetterThanWolves.fcBlockPlanterSoil.blockID;
    	}
    	
    	return false;
    }
    
    @Override
    public boolean canBlockStay( World world, int i, int j, int k )
    {
        if ( CanStayNextToBlock( world, i - 1, j, k ) &&
        	CanStayNextToBlock( world, i + 1, j, k ) &&
        	CanStayNextToBlock( world, i, j, k - 1 ) &&
        	CanStayNextToBlock( world, i, j, k + 1 ) )
        {
            int iBlockBelowID = world.getBlockId( i, j - 1, k );
            Block blockBelow = Block.blocksList[iBlockBelowID];
            
            return iBlockBelowID == blockID || ( blockBelow != null && 
            	blockBelow.CanCactusGrowOnBlock( world, i, j - 1, k ) );
        }
        
        return false;
    }
    
    @Override
    public void OnStruckByLightning( World world, int i, int j, int k )
    {
    	world.setBlockToAir( i, j, k );
    	
        world.playAuxSFX( FCBetterThanWolves.m_iCactusExplodeAuxFXID, i, j, k, 0 );
        
        if ( world.getBlockId( i, j - 1, k ) == blockID )
        {
        	// relay the strike downwards to other cactus blocks
        	
        	OnStruckByLightning( world, i, j - 1, k );
        }
    }
    
    //------------- Class Specific Methods ------------//

    protected boolean CanStayNextToBlock( World world, int i, int j, int k )
    {
    	return !world.getBlockMaterial( i, j, k ).isSolid() || 
    		world.getBlockId( i, j, k ) == FCBetterThanWolves.fcBlockWeb.blockID;
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( GetBlockBoundsFromPoolBasedOnState( 
        	renderer.blockAccess, i, j, k ) );
        
    	return renderer.renderBlockCactus( this, i, j, k );
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	if ( iSide == 0 || iSide == 1 )
    	{
    		return !( blockAccess.isBlockOpaqueCube(i, j, k) || blockAccess.getBlockId( i, j, k ) == Block.cactus.blockID );
    	}
    	
    	return true;
    }
}
