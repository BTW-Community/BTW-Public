// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockNetherrackFalling extends FCBlockFalling
{
    public FCBlockNetherrackFalling( int iBlockID )
    {
	    super( iBlockID, FCBetterThanWolves.fcMaterialNetherRock );
	    
	    setHardness( 0.6F );        
	    setResistance( 0.4F * 5F / 3F ); // 0.4 was original hardness of netherrack.  Equation to preserve its original blast resistance.
	    SetPicksEffectiveOn();
	    
	    setStepSound( soundStoneFootstep );
	    
        setUnlocalizedName( "hellrock" );        
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return Block.netherrack.blockID;
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 1F;
    }    
    
    @Override
    public int GetEfficientToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron and better
    }

	@Override
    protected boolean canSilkHarvest()
    {
		// falling version can't be harvested, as it's dimension dependant
		
        return false;
    }
	
    @Override
    protected void onStartFalling( EntityFallingSand entity ) 
    {	
    	super.onStartFalling( entity );
    	
    	int i = MathHelper.floor_double( entity.posX );
    	int j = (int)entity.posY;
    	int k = MathHelper.floor_double( entity.posZ );
    	
        entity.worldObj.playAuxSFX( FCBetterThanWolves.m_iGhastMoanSoundAuxFXID,            
    		i, j, k, 0 );

        if ( entity.worldObj.getBlockId( i, j + 1, k ) == Block.fire.blockID )
        {
        	entity.metadata = 1; // on fire flag
        	
	        entity.worldObj.playAuxSFX( FCBetterThanWolves.m_iFlamingNetherrackFallAuxFXID,            
	    		MathHelper.floor_double( entity.posX ), 
	    		MathHelper.floor_double( entity.posY ), 
	    		MathHelper.floor_double( entity.posZ ), 0 );
        }        
    }
    
	@Override
    public void onFinishFalling( World world, int i, int j, int k, int iMetadata )
    {
    	super.onFinishFalling( world, i, j, k, iMetadata );
    	
    	if ( iMetadata != 0 )
    	{
    		if ( world.isAirBlock( i, j + 1, k ) )
    		{
        		world.setBlockWithNotify( i, j + 1, k, Block.fire.blockID );

    			// remove on fire flag, but don't renotify neighbors
    			
    			world.SetBlockMetadataWithNotify( i, j, k, 0, 2 );
    		}
    	}
    }
    
	@Override
    protected void OnFallingUpdate( FCEntityFallingBlock entity ) 
    {
    	if ( entity.worldObj.isRemote && entity.metadata != 0 )
    	{
    		EmitSmokeParticles( entity.worldObj, entity.posX, entity.posY, entity.posZ, 
    			entity.worldObj.rand );
    	}
    }
    
	@Override
    public void OnBlockDestroyedLandingFromFall( World world, int i, int j, int k, int iMetadata )
    {
		// drop regular without messing with tool interactions
		
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	@Override
    public boolean DoesInfiniteBurnToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	return iFacing == 1;
    }
	
    //------------- Class Specific Methods ------------//
	
    private void EmitSmokeParticles( World world, double dCenterX, double dCenterY, double dCenterZ, Random rand )
    {
        for ( int iTempCount = 0; iTempCount < 5; ++iTempCount )
        {
            double xPos = dCenterX - 0.60D + rand.nextDouble() * 1.2D;
            double yPos = dCenterY + 0.25D + rand.nextDouble() * 0.25D;
            double zPos = dCenterZ - 0.60D + rand.nextDouble() * 1.2D;
        
        	world.spawnParticle( "largesmoke", xPos, yPos, zPos, 0D, 0D, 0D );
        }
    }
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_iconEmbers;
    
    @Override
    public void registerIcons( IconRegister register )
    {
        super.registerIcons( register );
    
		m_iconEmbers = register.registerIcon( "fcOverlayNetherrackEmbers" );
    }
    
    @Override
    public void RenderFallingBlock( RenderBlocks renderBlocks, int i, int j, int k, int iMetadata )
    {
    	renderBlocks.SetRenderAllFaces( true );
    	
        renderBlocks.setRenderBounds( GetFixedBlockBoundsFromPool() );
        
        renderBlocks.renderStandardBlock( this, i, j, k );
        
        if ( iMetadata != 0 )
        {
            Tessellator tessellator = Tessellator.instance;
            
            // render top face only same as FCClientUtilsRender.RenderBlockFullBrightWithTexture()
            
        	tessellator.setColorOpaque_F( 1F, 1F, 1F );
        	
        	tessellator.setBrightness( renderBlocks.blockAccess.getLightBrightnessForSkyBlocks( 
        		i, j, k, 15 ) );
        	
            renderBlocks.renderFaceYPos( null, i, j, k, m_iconEmbers );
        }
        
    	renderBlocks.SetRenderAllFaces( false );
    }
}
