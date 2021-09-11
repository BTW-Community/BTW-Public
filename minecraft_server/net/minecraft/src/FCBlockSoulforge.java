// FCMOD

package net.minecraft.src;

public class FCBlockSoulforge  extends BlockContainer
{
    private final FCModelBlock m_blockModel = new FCModelBlockSoulforge();
    
    public FCBlockSoulforge( int iBlockID )
    {
        super( iBlockID, Material.iron );  
        
        setHardness( 3.5F );        
        setStepSound( soundMetalFootstep );        
        
        setUnlocalizedName( "fcBlockAnvil" );
        
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
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityAnvil();
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
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote )
        {
        	if ( player instanceof EntityPlayerMP ) // should always be true
        	{
        		FCContainerSoulforge container = new FCContainerSoulforge( player.inventory, world, i, j, k );
        		
        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcAnvilContainerID );        		
        	}
        }

        return true;
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iFacing = GetFacing( blockAccess, i, j, k );
        
        if ( iFacing == 2 || iFacing == 3 )
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.5F - FCModelBlockSoulforge.m_fAnvilHalfBaseWidth, 0.0F, 0.0F, 
				0.5F + FCModelBlockSoulforge.m_fAnvilHalfBaseWidth, 1.0F, 1.0F );
        }
        else
        {
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 0.0F, 0.5F - FCModelBlockSoulforge.m_fAnvilHalfBaseWidth, 
        		1.0F, 1.0F, 0.5F + FCModelBlockSoulforge.m_fAnvilHalfBaseWidth );
        }    	
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        FCTileEntityAnvil tileEntityAnvil = (FCTileEntityAnvil)world.getBlockTileEntity( i, j, k );
        
        if ( tileEntityAnvil != null )
        {
        	tileEntityAnvil.EjectMoulds(); // legacy for old mould items
        }

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
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
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{
		RotateAroundJAxis( world, i, j, k, bReverse );
		
		return true;
	}
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}