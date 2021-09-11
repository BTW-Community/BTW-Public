// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockCauldron extends FCBlockCookingVessel
{
    public FCBlockCauldron( int iBlockID )
    {
        super( iBlockID, Material.iron );
        
        setHardness( 3.5F );
        setResistance( 10F );
        
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "fcBlockCauldron" );        
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityCauldron();
    }
    
    //------------- FCBlockCookingVessel -------------//

	@Override
	protected void ValidateFireUnderState( World world, int i, int j, int k )
	{
		// FCTODO: Move this to parent class
		
		if ( !world.isRemote )
		{
			TileEntity tileEnt = world.getBlockTileEntity( i, j, k );
			
			if ( tileEnt instanceof FCTileEntityCauldron )
			{
				FCTileEntityCauldron tileEntityCauldron = 
	            	(FCTileEntityCauldron)tileEnt;
	            
	            tileEntityCauldron.ValidateFireUnderType();            
			}
		}
	}
	
	@Override
	protected int GetContainerID()
	{
		return FCBetterThanWolves.fcCauldronContainerID;
	}
	
    //------------- Class Specific Methods -------------//
    
	//----------- Client Side Functionality -----------//
	
	private Icon m_IconContents;
	
	@Override
    public void registerIcons( IconRegister register )
    {
        Icon sideIcon = register.registerIcon( "fcBlockCauldron_side" );
        
        blockIcon = sideIcon; // for hit effects
        
        m_IconInteriorBySideArray[0] = m_IconWideBandBySideArray[0] = m_IconCenterColumnBySideArray[0] = register.registerIcon( "fcBlockCauldron_bottom" );
        
        m_IconInteriorBySideArray[1] = m_IconCenterColumnBySideArray[1] = register.registerIcon( "fcBlockCauldron_top" );
        m_IconWideBandBySideArray[1] = register.registerIcon( "fcBlockCauldronWideBand_top" );
        
        m_IconInteriorBySideArray[2] = m_IconWideBandBySideArray[2] = m_IconCenterColumnBySideArray[2] = sideIcon;
        m_IconInteriorBySideArray[3] = m_IconWideBandBySideArray[3] = m_IconCenterColumnBySideArray[3] = sideIcon;
        m_IconInteriorBySideArray[4] = m_IconWideBandBySideArray[4] = m_IconCenterColumnBySideArray[4] = sideIcon;
        m_IconInteriorBySideArray[5] = m_IconWideBandBySideArray[5] = m_IconCenterColumnBySideArray[5] = sideIcon;
        
        m_IconContents = register.registerIcon( "fcBlockCauldron_contents" );
    }
	
	//------------- Custom Renderer ------------//
    
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {    	
    	super.RenderBlock( renderBlocks, i, j, k );
    	
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	if ( GetFacing( blockAccess, i, j, k ) == 1 )
    	{
	        // render contents if upright
    		
    		TileEntity tileEntity = blockAccess.getBlockTileEntity( i, j, k );
    		
    		if ( tileEntity instanceof FCTileEntityCookingVessel )
    		{	        
    	        FCTileEntityCookingVessel vesselEntity = (FCTileEntityCookingVessel)blockAccess.getBlockTileEntity( i, j, k );
    	        
    	        short iItemCount = vesselEntity.m_sStorageSlotsOccupied;
    	        
	        	float fHeightRatio = (float)iItemCount / 27.0F;
	        	
	            float fBottom = 9.0F / 16F;
	            
	            float fTop = fBottom + ( 1.0F / 16F ) + 
	            	( ( ( 1.0F - ( 2.0F / 16.0F ) ) - ( fBottom + ( 1.0F / 16F ) ) ) * fHeightRatio );
	
	            renderBlocks.setRenderBounds( 0.125F, fBottom, 0.125F, 
	    	    		0.875F, fTop, 0.875F );
	            
	            FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, m_IconContents );
    		}
    	}

    	return true;
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	super.RenderBlockAsItem( renderBlocks, iItemDamage, fBrightness );
    	
        // render contents
		
        final float fBottom = 9.0F / 16F;
        
        final float fTop = 10.0F / 16F;

        renderBlocks.setRenderBounds( 0.125F, fBottom, 0.125F, 
	    		0.875F, fTop, 0.875F );
        
        FCClientUtilsRender.RenderInvBlockWithTexture( renderBlocks, this, -0.5F, -0.5F, -0.5F, m_IconContents );
    }
}