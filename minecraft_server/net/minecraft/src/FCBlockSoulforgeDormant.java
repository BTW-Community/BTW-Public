// FCMOD

package net.minecraft.src;

public class FCBlockSoulforgeDormant extends Block
{
    private final FCModelBlock m_blockModel = new FCModelBlockSoulforge();
    
    public FCBlockSoulforgeDormant( int iBlockID )
    {
        super( iBlockID, Material.iron );  
        
        setHardness( 3F ); // same as gold storage
        
        setStepSound( soundMetalFootstep );        
        
        setUnlocalizedName( "fcBlockSoulforgeDormant" );
        
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
    	// flat facing only
    	
    	if ( iFacing < 2 )
    	{
    		iFacing = 2;
    	}
    	else
    	{
        	iFacing = Block.GetOppositeFacing( iFacing );        	
    	}
    	
        return SetFacing( iMetadata, iFacing );        
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack )
	{
		int iFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed( entityLiving );
		
		SetFacing( world, i, j, k, iFacing );
	}
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        if ( iFacing == 2 || iFacing == 3 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5D - FCModelBlockSoulforge.m_fAnvilHalfBaseWidth, 0D, 0D, 
				0.5D + FCModelBlockSoulforge.m_fAnvilHalfBaseWidth, 1D, 1D );
        }
        else
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0D, 0D, 0.5D - FCModelBlockSoulforge.m_fAnvilHalfBaseWidth, 
        		1D, 1D, 0.5D + FCModelBlockSoulforge.m_fAnvilHalfBaseWidth );
        }    	
    }
	
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	int iFacing = GetFacing( world, i, j, k );
    	
    	FCModelBlock transformedModel = m_blockModel.MakeTemporaryCopy();
    	
    	transformedModel.RotateAroundJToFacing( iFacing );
    	
    	return transformedModel.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{
		return iMetadata;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		return iFacing;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return true;
	}
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}