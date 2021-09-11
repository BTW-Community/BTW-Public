// FCMOD

package net.minecraft.src;

public class FCBlockHamper extends FCBlockBasket
{
	public static final FCModelBlockHamper m_model = new FCModelBlockHamper();
	
    protected FCBlockHamper( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockHamper" );
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityHamper();
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote)
        {
        	if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, i, j + 1, k, 0, true ) && 
        		!FCUtilsWorld.IsBlockRestingOnThatBelow( world, i, j + 1, k ) )
    		{
	            FCTileEntityHamper tileEntity = (FCTileEntityHamper)world.getBlockTileEntity( i, j, k );
	            
	        	if ( player instanceof EntityPlayerMP ) // should always be true
	        	{
	        		FCContainerHamper container = new FCContainerHamper( player.inventory, tileEntity );
	        		
	        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcHamperContainerID );        		
	        	}
    		}
        }
        
		return true;
    }
	
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing == 0 || ( iFacing == 1 && !GetIsOpen( blockAccess, i, j, k ) );
	}
    
    @Override
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	if ( !world.isRemote )
    	{
    		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemWickerPiece.itemID, 2, 0, 0.75F );
    	}
    }
    
    @Override
	public FCModelBlock GetLidModel( int iMetadata )
    {
		return m_model.m_lid;
    }
	
    @Override
	public Vec3 GetLidRotationPoint()
	{
    	return m_model.GetLidRotationPoint();
	}
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
