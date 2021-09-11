// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockShovel extends Block
{
    protected FCModelBlockShovel m_model = new FCModelBlockShovel();
	
    public FCBlockShovel( int iBlockID )
    {
        super( iBlockID, Material.iron );
        
        setHardness( 5F );     
        SetPicksEffectiveOn();
        
    	InitBlockBounds( 0F, 0F, 0F, 1F, 1F, 1F );
    	
        setStepSound( soundMetalFootstep );        
        
        setUnlocalizedName( "fcBlockShovel" );
        
        setCreativeTab( CreativeTabs.tabRedstone );
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
    public void addCollisionBoxesToList( World world, int i, int j, int k, AxisAlignedBB boundingBox, List list, Entity entity )
    {
		FCModelBlock transformedModel = GetTransformedModelForMetadata( m_model.m_collisionModel, 
			world.getBlockMetadata( i, j, k ) );
		
		transformedModel.AddIntersectingBoxesToCollisionList( world, i, j, k, boundingBox, list );
    }
    
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		int iVerticalOrientation = 1;
		int iBlockFacing = 2;
		
		if ( iFacing >= 2 )
		{
			if ( fClickY > 0.5F )
			{
				iVerticalOrientation = 0;
			}
			
			iBlockFacing = iFacing;
		}
		else
		{
			if ( iFacing == 0 )
			{
				iVerticalOrientation = 0; 
			}
		}
			
		iMetadata = SetFacing( iMetadata, iBlockFacing );
		iMetadata = SetVerticalOrientation( iMetadata, iVerticalOrientation );
		
        return iMetadata;        
    }
    
	@Override
    public int PreBlockPlacedBy( World world, int i, int j, int k, int iMetadata, EntityLiving entityBy ) 
	{
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed( entityBy );

		return SetFacing( iMetadata, iFacing );
	}
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
		FCModelBlock transformedModel = GetTransformedModelForMetadata( m_model.m_rayTraceModel, 
			world.getBlockMetadata( i, j, k ) );
		
		transformedModel.AddToRayTrace( rayTrace );
		
        return rayTrace.GetFirstIntersection();
    }
	
	
	@Override
	public int GetFacing( int iMetadata )
	{
		return ( iMetadata & 3 ) + 2;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= ~3; // filter out old facing
		
		iMetadata |= MathHelper.clamp_int( iFacing, 2, 5 ) - 2; // convert to flat facing
		
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}

	@Override
    public int GetPistonShovelEjectDirection( World world, int i, int j, int k, int iToFacing )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( iToFacing >= 2 )
		{
			if ( iToFacing == GetFacing( iMetadata ) )
			{
				return GetVerticalOrientation( iMetadata );
			}
		}
		else if ( iToFacing == GetVerticalOrientation( iMetadata ) )
		{			
			return GetFacing( iMetadata );
		}
		
		return -1;
    }
    
	//------------- Class Specific Methods ------------//
	
	/**
	 * 0 = down, 1 = up, 2 = side
	 */
	public int GetVerticalOrientation( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetVerticalOrientation( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public int GetVerticalOrientation( int iMetadata )
	{
		return ( iMetadata & 12 ) >> 2;
	}
	
	public void SetVerticalOrientation( World world, int i, int j, int k, int iLevel )
	{
		int iMetadata = SetVerticalOrientation( world.getBlockMetadata( i, j, k ), iLevel );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetVerticalOrientation( int iMetadata, int iLevel )
	{
		iMetadata &= ~12; // filter out old facing
		
		iMetadata |= iLevel << 2;
		
		return iMetadata;
	}
	
    private FCModelBlock GetTransformedModelForMetadata( FCModelBlock model, int iMetadata )
    {
		FCModelBlock transformedModel = model.MakeTemporaryCopy();
		
    	if ( GetVerticalOrientation( iMetadata ) == 0 )
    	{
    		transformedModel.TiltToFacingAlongJ( 0 );
    	}
    		 
		transformedModel.RotateAroundJToFacing( GetFacing( iMetadata ) );
		
		return transformedModel;
    }
    
	//----------- Client Side Functionality -----------//
}