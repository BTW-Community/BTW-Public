// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTurntable extends BlockContainer
	implements FCIBlockMechanical
{
	private static final int iTurntableTickRate = 10;
	
	protected FCBlockTurntable( int iBlockID )
	{
        super( iBlockID, Material.rock );

        setHardness( 2F );
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockTurntable" );
        
        setCreativeTab( CreativeTabs.tabRedstone );
	}
	
	@Override
    public int tickRate( World world )
    {
    	return iTurntableTickRate;
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityTurntable();
    }

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bReceivingMechanicalPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bMechanicalOn = IsBlockMechanicalOn( world, i, j, k );
    	
    	if ( bMechanicalOn != bReceivingMechanicalPower )
    	{
	        EmitTurntableParticles( world, i, j, k, random );
	        
    		SetBlockMechanicalOn( world, i, j, k, bReceivingMechanicalPower );
    		
	        world.markBlockForUpdate( i, j, k );
    	}
    	
    	boolean bReceivingRedstonePower = world.isBlockGettingPowered(i, j, k);
    	boolean bRedstoneOn = IsBlockRedstoneOn( world, i, j, k );
    	
    	if ( bRedstoneOn != bReceivingRedstonePower )
    	{
    		SetBlockRedstoneOn( world, i, j, k, bReceivingRedstonePower );    		
    	}
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
    	ItemStack playerEquippedItem = player.getCurrentEquippedItem();
    	
    	if ( playerEquippedItem != null )
    	{
    		return false;
    	}
    	
        if ( !world.isRemote )
        {
        	int iSwitchSetting = GetSwitchSetting( world, i, j, k );
        	
        	iSwitchSetting++;
        	
        	if ( iSwitchSetting > 3 )
        	{
        		iSwitchSetting = 0;
        	}
        	
        	SetSwitchSetting( world, i, j, k, iSwitchSetting );
        
	        world.markBlockForUpdate( i, j, k );
	        
	        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );

	        // click sound
	        
            world.playAuxSFX( 1001, i, j, k, 0 );
        }
        
        return true;
    }

	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
    //------------- FCIBlockMechanical -------------//
    
	@Override
    public boolean CanOutputMechanicalPower()
    {
    	return false;
    }

	@Override
    public boolean CanInputMechanicalPower()
    {
    	return true;
    }

	@Override
    public boolean IsInputtingMechanicalPower( World world, int i, int j, int k )
    {
    	// check for powered axles below
    	
    	return FCUtilsMechPower.IsBlockPoweredByAxleToSide( world, i, j, k, 0 );    	
    }    

	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		return iFacing == 0;
	}
	
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakTurntable( world, i, j, k );
	}
	
    //------------- Class Specific Methods ------------//
    
    public boolean IsBlockMechanicalOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 1 ) > 0;    
	}
    
    public void SetBlockMechanicalOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & (~1); // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 1;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
    public boolean IsBlockRedstoneOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 2 ) > 0;    
	}
    
    public void SetBlockRedstoneOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & (~2); // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 2;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
    public int GetSwitchSetting( IBlockAccess iBlockAccess, int i, int j, int k )
    {    	
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 12 ) >> 2;    
    }
    
    public void SetSwitchSetting( World world, int i, int j, int k, int iSetting )
    {
    	if ( iSetting >= 4 || iSetting < 0 )
    	{
    		iSetting = 0;
    	}
    	
    	int iMetadata = world.getBlockMetadata( i, j, k ) & (~12); // filter out old on state
    	
    	iMetadata |= ( iSetting << 2 );
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    void EmitTurntableParticles( World world, int i, int j, int k, Random random )
    {
        for ( int counter = 0; counter < 5; counter++ )
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            
            world.spawnParticle( "smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D );
        }
    }
    
	private void BreakTurntable( World world, int i, int j, int k )
	{
		FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, Item.redstone.itemID, 0 );
		
    	DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStone.itemID, 16, 0, 0.75F );
		
		// drop wood siding
		
		for ( int iTemp = 0; iTemp < 2; iTemp++ )
		{
			FCUtilsItem.EjectSingleItemWithRandomOffset( world, i, j, k, FCBetterThanWolves.fcBlockWoodSidingItemStubID, 0 );			
		}
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconBySideArray = new Icon[6];
    
    private Icon m_IconSwitch;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" ); // for hit effects
        
        m_IconBySideArray[0] = register.registerIcon( "fcBlockTurntable_bottom" );
        m_IconBySideArray[1] = register.registerIcon( "fcBlockTurntable_top" );
        
        Icon sideIcon = register.registerIcon( "fcBlockTurntable_side" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
        
        m_IconSwitch = register.registerIcon( "fcBlockTurntable_switch" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_IconBySideArray[iSide];
    }
	
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
    	if ( IsBlockMechanicalOn( world, i, j, k ) )
    	{
    		EmitTurntableParticles( world, i, j, k, random );	        
    	}
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		return true;
    }
	
    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	super.RenderBlock( renderBlocks, i, j, k );
    	
    	// render the switches
    	
    	int iSwitchSetting = GetSwitchSetting( renderBlocks.blockAccess, i, j, k );
    	
    	float switchOffset = ( 4.0F / 16.0F ) + ( (float)iSwitchSetting * ( 2.0F / 16.0F ) ); 
        
        renderBlocks.setRenderBounds( switchOffset, ( 5.0F / 16.0F ), ( 1.0F / 16.0F ), 
        		switchOffset + ( 2.0F / 16.0F ), ( 7.0F / 16.0F ), 1.0F + ( 1.0F / 16.0F ) );

        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, m_IconSwitch );
        
        renderBlocks.setRenderBounds( 1.0F - ( switchOffset + ( 2.0F / 16.0F ) ), ( 5.0F / 16.0F ), 0.0F - ( 1.0F / 16.0F ), 
    		1.0F - switchOffset, ( 7.0F / 16.0F ), 1.0F - ( 1.0F / 16.0F ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, m_IconSwitch );
        
        renderBlocks.setRenderBounds( ( 1.0F / 16.0F ), ( 5.0F / 16.0F ), 1.0F - ( switchOffset + ( 2.0F / 16.0F ) ), 
    		1.0F + ( 1.0F / 16.0F ), ( 7.0F / 16.0F ), 1.0F - switchOffset );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, m_IconSwitch );
        
        renderBlocks.setRenderBounds( 0.0F - ( 1.0F / 16.0F ), ( 5.0F / 16.0F ), switchOffset, 
        		1.0F - ( 1.0F / 16.0F ), ( 7.0F / 16.0F ), switchOffset + ( 2.0F / 16.0F ) );
        
        FCClientUtilsRender.RenderStandardBlockWithTexture( renderBlocks, this, i, j, k, m_IconSwitch );
        
        return true;
    }    
}