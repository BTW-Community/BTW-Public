// FCMOD

package net.minecraft.src;

public class FCBlockDetectorLogicGlowing extends FCBlockDetectorLogic
{
    private final static float m_fLitFaceThickness = 0.01F;
	
    public FCBlockDetectorLogicGlowing( int iBlockID )
    {
        super( iBlockID );  
        
        setLightValue( 1F );     
        
        setUnlocalizedName( "fcBlockDetectorLogicGlowing" );
    }
    
    //------------- FCBlockDetectorLogic ------------//

	@Override
	protected void RemoveSelf( World world, int i, int j, int k )
	{
		// this function exists as the regular block shouldn't notify the client when it is removed, but the glowing variety should 
		
    	world.setBlock( i, j, k, 0, 0, 2 );        	
	}
    
    //------------- Class Specific Methods ------------//

	//----------- Client Side Functionality -----------//
	
	@Override
    public int getMixedBrightnessForBlock(IBlockAccess iblockaccess, int i, int j, int k)
	{
		return 0xF00F0;
	}
	
	@Override
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return m_currentBlockRenderer.ShouldSideBeRenderedBasedOnCurrentBounds( 
			iNeighborI, iNeighborJ, iNeighborK, iSide );
    }
    
	public void SetRenderBoundsToRenderLitFace( RenderBlocks renderBlocks, int iFacing )
	{
		switch ( iFacing )
		{
			case 0:
				
				renderBlocks.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	    			1.0F, m_fLitFaceThickness, 1.0F );
		    	
		    	break;
		    	
			case 1:
				
				renderBlocks.setRenderBounds( 0.0F, 1.0F - m_fLitFaceThickness, 0.0F, 
	    			1.0F, 1.0F, 1.0F );
		    	
		    	break;
		    	
			case 2:
				
				renderBlocks.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	    			1.0F, 1.0F, m_fLitFaceThickness );
		    	
		    	break;
		    	
			case 3:
				
				renderBlocks.setRenderBounds( 0.0F, 0.0F, 1.0F - m_fLitFaceThickness, 
	    			1.0F, 1.0F, 1.0F );
		    	
		    	break;
		    	
			case 4:
				
				renderBlocks.setRenderBounds( 0.0F, 0.0F, 0.0F, 
	    			m_fLitFaceThickness, 1.0F, 1.0F );
		    	
		    	break;
		    	
			default: // 5
				
				renderBlocks.setRenderBounds( 1.0F - m_fLitFaceThickness, 0.0F, 0.0F, 
	    			1.0F, 1.0F, 1.0F );
		    	
		    	break;
		    	
		}
    	
	}
	public boolean ShouldVisiblyProjectToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{	
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k, iFacing );
    	
    	if ( iFacing == 0 )
    	{
    		return FCUtilsWorld.DoesBlockHaveSolidTopSurface( blockAccess, targetPos.i, targetPos.j, targetPos.k );    		
    	}
    	else
    	{
    		return blockAccess.isBlockNormalCube( targetPos.i, targetPos.j, targetPos.k );
    	}    	
	}
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderer.blockAccess;
    	
		for ( int iTempFacing = 0; iTempFacing <= 5; iTempFacing++ )
		{
			if ( ShouldVisiblyProjectToFacing( blockAccess, i, j, k, iTempFacing ) )
			{
				SetRenderBoundsToRenderLitFace( renderer, iTempFacing );
				
		        FCClientUtilsRender.RenderBlockFullBrightWithTexture( renderer, blockAccess, i, j, k, blockIcon );
			}				
		}
		
    	if ( bLogicDebugDisplay )
    	{
    		return RenderDetectorLogicDebug( renderer, blockAccess, i, j, k, this );
    	}
    	else
    	{    	
    		return true;
    	}
    }    
}