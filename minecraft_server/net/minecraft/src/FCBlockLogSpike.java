// FCMOD

package net.minecraft.src;

public class FCBlockLogSpike extends Block
{
    public final static float m_fHardness = 2F;
    
    private FCModelBlock m_modelBlock = new FCModelBlockLogSpike();
    
    public FCBlockLogSpike( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );
        
        setHardness( m_fHardness );
		SetAxesEffectiveOn();
		SetChiselsEffectiveOn();
        
        SetBuoyant();
        
        SetFireProperties( 5, 5 );
        
        Block.useNeighborBrightness[iBlockID] = true;        
        setLightOpacity( 4 );
        
        setStepSound( soundWoodFootstep );        
        
        setUnlocalizedName( "fcBlockLogSpike" );
    }    
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		iMetadata = SetFacing( iMetadata, iFacing );
		
		return iMetadata;
    }
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	int iFacing = GetFacing( world, i, j, k );
    	
    	FCModelBlock transformedModel = m_modelBlock.MakeTemporaryCopy();
    	
    	transformedModel.TiltToFacingAlongJ( iFacing );
    	
    	return transformedModel.CollisionRayTrace( world, i, j, k, startRay, endRay );
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	return iFacing == Block.GetOppositeFacing( iFacing );
	}
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
		// last item dropped is always saw dust to encourage player to keep some chewed logs around decoratively 
    	
        dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust ) );
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
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 1, 0, fChanceOfPileDrop );
    }
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 1000; // always convert, never harvest
    }
    
    @Override
    public int GetFacing( int iMetadata )
    {
    	int iFacing = ( iMetadata ) & 7;
    	
    	return iFacing;
    }
    
    @Override
    public int SetFacing( int iMetadata, int iFacing )
    {
    	iMetadata &= (~7);    	
    	
    	return iMetadata | iFacing; 
    }

    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
