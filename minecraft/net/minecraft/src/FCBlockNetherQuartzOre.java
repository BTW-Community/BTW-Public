// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockNetherQuartzOre extends BlockOre
{
    public FCBlockNetherQuartzOre( int iBlockID )
    {
        super( iBlockID );
        
    	SetBlockMaterial( FCBetterThanWolves.fcMaterialNetherRock );
    	
    	setHardness( 1F );
    	setResistance( 5F );
    	
    	setStepSound( soundStoneFootstep );
    	
    	setUnlocalizedName("netherquartz");
    }
    
	//----------- Client Side Functionality -----------//
    
	@Override
    public void registerIcons( IconRegister register )
    {
		blockIcon = register.registerIcon( "fcBlockNetherQuartz" );
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	return renderer.RenderStandardFullBlock( this, i, j, k );
    }
    
    @Override
    public boolean DoesItemRenderAsBlock( int iItemDamage )
    {
    	return true;
    }    
    
    @Override
    public void RenderBlockMovedByPiston( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	renderBlocks.RenderStandardFullBlockMovedByPiston( this, i, j, k );
    }    
}