// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFireStoked extends FCBlockFire
{
	private final int m_iTickRate = 42;
	
	protected FCBlockFireStoked( int iBlockID )
	{
        super( iBlockID );

		setHardness( 0F );
		setLightValue( 1F );		
		
		SetFireProperties( 60, 0 );		
		
		setStepSound( soundWoodFootstep );		
		
		setUnlocalizedName( "fcBlockStokedFire" );
		
		disableStats();		
	}

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
		// override to make burn time more consistent
		
        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }
    
	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
    }

	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		if ( ValidateState( world, i, j, k ) )
		{
	        if ( world.getBlockId( i, j + 1, k ) == Block.brick.blockID )
	        {
	        	world.setBlockWithNotify( i, j + 1, k, FCBetterThanWolves.fcKiln.blockID );
	        }
	        
	        int iMetaData = world.getBlockMetadata( i, j, k );
	        
	        if( iMetaData < 15 )
	        {
	        	iMetaData += 1;
	        	
	            world.setBlockMetadata( i, j, k, iMetaData );
	        }
	        
	        TryToDestroyBlockWithFire( world, i + 1, j, k, 300, random, 0 );
	        TryToDestroyBlockWithFire( world, i - 1, j, k, 300, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j - 1, k, 250, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j + 1, k, 250, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j, k - 1, 300, random, 0 );
	        TryToDestroyBlockWithFire( world, i, j, k + 1, 300, random, 0 );
	        
	        CheckForFireSpreadFromLocation( world, i, j, k, random, 0 );
	        
	        if ( iMetaData >= 3 )
	        {
	        	// revert to regular fire if we've exceeded our life-span
	        	
	            world.setBlockAndMetadataWithNotify( i, j, k, Block.fire.blockID, 0 );
	        }
	        else
	        {	
	        	world.scheduleBlockUpdate( i, j, k, blockID, 
	        		tickRate( world ) + world.rand.nextInt( 10 ) );
	        }
		}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		// verify we have a tick already scheduled to prevent jams on chunk load
		
		if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
		{
			// reset the countdown and schedule an extra long tick
			// to give any nearby bellows a chance to catch up too
			
			world.setBlockMetadata( i, j, k, 0 );  
			
	        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) * 4 );        
		}
    }
    
	@Override
    public boolean DoesInfiniteBurnToFacing( IBlockAccess blockAccess, int i, int j, int k, 
    	int iFacing )
    {
    	return iFacing == 1;
    }
	
    //------------- Class Specific Methods ------------//
    
    /**
     * Return true if the Stoked fire block still remains
     */
    public boolean ValidateState( World world, int i, int j, int k )
    {
        if ( !canPlaceBlockAt( world, i, j, k ) )
        {
            world.setBlockWithNotify( i, j, k, 0 );
            
            return false;
        }        
        else if ( world.getBlockId( i, j - 1, k ) == FCBetterThanWolves.fcBBQ.blockID )
        {
        	if ( !FCBetterThanWolves.fcBBQ.IsLit( world, i, j - 1, k ) )
        	{
        		// we are above an unlit BBQ, extinguish
        		
                world.setBlockWithNotify( i, j, k, 0 );
                
                return false;
        	}
        }
        else
        {
        	// stoked fire can only exist over a Hibachi.  Revert to regular fire if we're
        	// not over one
        	
            world.setBlockAndMetadataWithNotify( i, j, k, Block.fire.blockID, 0 );
            
            return false;
        }
        
    	return true;
    }
    
	//----------- Client Side Functionality -----------//
    
    private Icon[] fireTextureArray;
    
    @Override
    public void registerIcons( IconRegister register )
    {
        fireTextureArray = new Icon[] { 
        	register.registerIcon( "fcBlockFireStokedStub_0", new FCClientTextureFireStoked( "fcBlockFireStokedStub_0", 0 ) ), 
        	register.registerIcon( "fcBlockFireStokedStub_1", new FCClientTextureFireStoked( "fcBlockFireStokedStub_1", 1 ) ) 
    	};
    }

    @Override
    public Icon func_94438_c(int par1)
    {
		return fireTextureArray[par1];
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return fireTextureArray[0];
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	// portion of RenderBlocks.RenderBlockFire() heavily modified

    	IBlockAccess blockAccess = renderBlocks.blockAccess;
        Tessellator tessellator = Tessellator.instance;
        Icon texture1 = func_94438_c( 0 );
        Icon texture2 = func_94438_c( 1 );
        
        if ( ( ( i + k ) & 1 ) != 0 )
        {
	        texture1 = func_94438_c( 1 );
	        texture2 = func_94438_c( 0 );
        }
        
        Icon currentTexture = texture1;

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        
        tessellator.setBrightness( getMixedBrightnessForBlock(blockAccess, i, j, k ) );
        
        double dMinU = (double)currentTexture.getMinU();
        double dMinV = (double)currentTexture.getMinV();
        double dMaxU = (double)currentTexture.getMaxU();
        double dMaxV = (double)currentTexture.getMaxV();
        
        float fRenderHeight = 1.0F;

        double var18 = (double)i + 0.5D + 0.2D;
        double var20 = (double)i + 0.5D - 0.2D;
        double var22 = (double)k + 0.5D + 0.2D;
        double var24 = (double)k + 0.5D - 0.2D;
        double var26 = (double)i + 0.5D - 0.3D;
        double var28 = (double)i + 0.5D + 0.3D;
        double var30 = (double)k + 0.5D - 0.3D;
        double var32 = (double)k + 0.5D + 0.3D;
        
        tessellator.addVertexWithUV(var26, (double)((float)j + fRenderHeight), (double)(k + 1), dMaxU, dMinV);
        tessellator.addVertexWithUV(var18, (double)(j + 0), (double)(k + 1), dMaxU, dMaxV);
        tessellator.addVertexWithUV(var18, (double)(j + 0), (double)(k + 0), dMinU, dMaxV);
        tessellator.addVertexWithUV(var26, (double)((float)j + fRenderHeight), (double)(k + 0), dMinU, dMinV);
        tessellator.addVertexWithUV(var28, (double)((float)j + fRenderHeight), (double)(k + 0), dMaxU, dMinV);
        tessellator.addVertexWithUV(var20, (double)(j + 0), (double)(k + 0), dMaxU, dMaxV);
        tessellator.addVertexWithUV(var20, (double)(j + 0), (double)(k + 1), dMinU, dMaxV);
        tessellator.addVertexWithUV(var28, (double)((float)j + fRenderHeight), (double)(k + 1), dMinU, dMinV);
        
        dMinU = (double)texture2.getMinU();
        dMinV = (double)texture2.getMinV();
        dMaxU = (double)texture2.getMaxU();
        dMaxV = (double)texture2.getMaxV();
        
        tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + fRenderHeight), var32, dMaxU, dMinV);
        tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), var24, dMaxU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), var24, dMinU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + fRenderHeight), var32, dMinU, dMinV);
        tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + fRenderHeight), var30, dMaxU, dMinV);
        tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), var22, dMaxU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), var22, dMinU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + fRenderHeight), var30, dMinU, dMinV);
        
        var18 = (double)i + 0.5D - 0.5D;
        var20 = (double)i + 0.5D + 0.5D;
        var22 = (double)k + 0.5D - 0.5D;
        var24 = (double)k + 0.5D + 0.5D;
        var26 = (double)i + 0.5D - 0.5D;
        var28 = (double)i + 0.5D + 0.5D;
        var30 = (double)k + 0.5D - 0.5D;
        var32 = (double)k + 0.5D + 0.5D;
        
        tessellator.addVertexWithUV(var26, (double)((float)j + fRenderHeight), (double)(k + 0), dMinU, dMinV);
        tessellator.addVertexWithUV(var18, (double)(j + 0), (double)(k + 0), dMinU, dMaxV);
        tessellator.addVertexWithUV(var18, (double)(j + 0), (double)(k + 1), dMaxU, dMaxV);
        tessellator.addVertexWithUV(var26, (double)((float)j + fRenderHeight), (double)(k + 1), dMaxU, dMinV);
        tessellator.addVertexWithUV(var28, (double)((float)j + fRenderHeight), (double)(k + 1), dMinU, dMinV);
        tessellator.addVertexWithUV(var20, (double)(j + 0), (double)(k + 1), dMinU, dMaxV);
        tessellator.addVertexWithUV(var20, (double)(j + 0), (double)(k + 0), dMaxU, dMaxV);
        tessellator.addVertexWithUV(var28, (double)((float)j + fRenderHeight), (double)(k + 0), dMaxU, dMinV);
        
        dMinU = (double)texture1.getMinU();
        dMinV = (double)texture1.getMinV();
        dMaxU = (double)texture1.getMaxU();
        dMaxV = (double)texture1.getMaxV();
        
        tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + fRenderHeight), var32, dMinU, dMinV);
        tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), var24, dMinU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), var24, dMaxU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + fRenderHeight), var32, dMaxU, dMinV);
        tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + fRenderHeight), var30, dMinU, dMinV);
        tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), var22, dMinU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), var22, dMaxU, dMaxV);
        tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + fRenderHeight), var30, dMaxU, dMinV);

    	return true;
    }
}