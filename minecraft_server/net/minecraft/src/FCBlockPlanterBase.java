// FCMOD

package net.minecraft.src;

public class FCBlockPlanterBase extends Block
{
	protected static final double m_dPlanterWidth = ( 1D - ( 4D / 16D ) );
	protected static final double m_dPlanterHalfWidth = ( m_dPlanterWidth / 2D );
	
	protected static final double m_dPlanterBandHeight = ( 5D / 16D );
	protected static final double m_dPlanterBandHalfHeight = ( m_dPlanterBandHeight / 2D );
	
    protected FCBlockPlanterBase( int iBlockID )
    {
    	super( iBlockID, Material.glass );
    	
        setHardness( 0.6F );
        SetPicksEffectiveOn( true );
        
        setTickRandomly( true );        
        
        setStepSound( soundGlassFootstep );        
        
        setUnlocalizedName( "fcBlockPlanterSoil" );     
        
        setCreativeTab( CreativeTabs.tabDecorations );
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
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
		if ( !world.isRemote && entity.isEntityAlive() && entity instanceof EntityItem )
		{
			EntityItem entityItem = (EntityItem)entity;
			ItemStack stack = entityItem.getEntityItem();			
			
			if ( stack.getItem().itemID == Item.dyePowder.itemID && 
				stack.getItemDamage() == 15 ) // bone meal
			{
				if ( AttemptToApplyFertilizerTo( world, i, j, k ) )
				{
					stack.stackSize--;
					
					if ( stack.stackSize <= 0 )
					{
						entityItem.setDead();
					}

		            world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "random.pop", 0.25F, 
	            		( ( world.rand.nextFloat() - world.rand.nextFloat() ) * 
            			0.7F + 1F ) * 2F );
				}
			}
		}
    }
	
	@Override
	public float GetPlantGrowthOnMultiplier( World world, int i, int j, int k, Block plantBlock )
	{
		if ( GetIsFertilizedForPlantGrowth( world, i, j, k ) )
		{
			return 2F;
		}

		return 1F;
	}
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	// bottom
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0.5F - m_dPlanterHalfWidth, 0F, 0.5F - m_dPlanterHalfWidth, 
    		0.5F + m_dPlanterHalfWidth, 1F - m_dPlanterBandHeight, 0.5F + m_dPlanterHalfWidth );
    
    	// top
    	
		rayTrace.AddBoxWithLocalCoordsToIntersectionList( 0F, 1F - m_dPlanterBandHeight, 0F, 1F, 1F, 1F );
        
        return rayTrace.GetFirstIntersection();    	
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	return 1F;
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	return iFacing == 0 || super.HasCenterHardPointToFacing( blockAccess, i, j, k, iFacing, bIgnoreTransparency );
	}
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing == 1;		
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
