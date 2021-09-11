// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchFiniteBurning extends FCBlockTorchBaseBurning
	implements ITileEntityProvider
{
	private boolean m_bIsBeingCrushed = false;
	
    protected FCBlockTorchFiniteBurning( int iBlockID )
    {
    	super( iBlockID );
    	
        isBlockContainer = true;
    	
        setLightValue( 0.875F );
        
    	setUnlocalizedName( "fcBlockTorchFiniteBurning" );
    	
        setCreativeTab( null );
    }   

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityTorchFinite();
    }
	
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
	{
		// prevents normal drop
	}
    
    @Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
        
        if ( !world.isRemote )
        {
            FCTileEntityTorchFinite tileEntity = (FCTileEntityTorchFinite)world.getBlockTileEntity( i, j, k );

            if ( tileEntity != null )
            {
	            int iBurnCountdown = tileEntity.m_iBurnTimeCountdown;
	            
	            if ( iBurnCountdown > 0 && !m_bIsBeingCrushed )
	            {
	        		int iNewItemDamage = (int)( FCItemBlockTorchFiniteBurning.m_fDamageToBurnTimeRatio * 
	        			(float)( FCTileEntityTorchFinite.m_iMaxBurnTime - tileEntity.m_iBurnTimeCountdown ) ); 
	        		
	        		// the below has a minimum of 1 damage to ensure damage bar is initially displayed
	        		iNewItemDamage = MathHelper.clamp_int( iNewItemDamage, 1, FCItemBlockTorchFiniteBurning.m_iMaxDamage - 1 ); 
	        		
		            ItemStack stack = new ItemStack( blockID, 1, iNewItemDamage );
		            
		            long iExpiryTime = FCUtilsWorld.GetOverworldTimeServerOnly() + (long)iBurnCountdown;
		            
		            stack.setTagCompound( new NBTTagCompound() );
		            stack.getTagCompound().setLong( "outTime", iExpiryTime);
		
		            dropBlockAsItem_do( world, i, j, k, stack );
	            }
            }
        }
        
        m_bIsBeingCrushed = false;
        
        world.removeBlockTileEntity(i, j, k);
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entity, ItemStack stack )
	{
        TileEntity tileEntity = world.getBlockTileEntity( i, j, k );

        if ( tileEntity != null && tileEntity instanceof FCTileEntityTorchFinite )
        {
            if ( stack.hasTagCompound() && stack.getTagCompound().hasKey( "outTime" ) )
            {
            	long lExpiryTime = stack.getTagCompound().getLong( "outTime" );
            	
            	int iCountDown = (int)( lExpiryTime - FCUtilsWorld.GetOverworldTimeServerOnly() );
            	
            	if ( iCountDown < 0 || iCountDown > FCTileEntityTorchFinite.m_iMaxBurnTime )
            	{
            		iCountDown = FCTileEntityTorchFinite.m_iMaxBurnTime;
            	}
            	
            	((FCTileEntityTorchFinite)tileEntity).m_iBurnTimeCountdown = iCountDown;
            	
            	if ( iCountDown < FCTileEntityTorchFinite.m_iSputterTime )
            	{
            		SetIsSputtering( world, i, j, k, true );
            	}
            }
        }
	}

	@Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
	@Override
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
		m_bIsBeingCrushed = true;
    }
    
    @Override
	public void OnFluidFlowIntoBlock( World world, int i, int j, int k, BlockFluid newBlock )
	{
        world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
        
		m_bIsBeingCrushed = true;        
	}

	@Override
    public boolean OnRotatedAroundBlockOnTurntableToFacing( World world, int i, int j, int k, int iFacing  )
    {
		world.setBlockToAir( i, j, k );
		
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
	public void SetIsSputtering( World world, int i, int j, int k, boolean bSputtering )
	{
		int iMetadata = SetIsSputtering( world.getBlockMetadata( i, j, k ), bSputtering );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	static public int SetIsSputtering( int iMetadata, boolean bIsSputtering )
	{
		if ( bIsSputtering )
		{
			iMetadata |= 8;
		}
		else
		{
			iMetadata &= (~8);
		}
		
		return iMetadata;
	}
    
	public boolean GetIsSputtering( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsSputtering( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	static public boolean GetIsSputtering( int iMetadata )
	{
		return ( iMetadata & 8 ) != 0;
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon m_iconSputtering;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_iconSputtering = register.registerIcon( "fcBlockTorchFiniteSputtering" );		
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( GetIsSputtering( iMetadata ) )
		{
			return m_iconSputtering;
		}
		
		return super.getIcon( iSide, iMetadata );
    }
}
    
