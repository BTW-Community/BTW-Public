// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockPulley extends BlockContainer
	implements FCIBlockMechanical
{
	private static int iPulleyTickRate = 10;
	
	protected FCBlockPulley( int iBlockID )
	{
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );

        setHardness( 2F );
        
        SetAxesEffectiveOn( true );
        
        SetBuoyancy( 1F );
        
		SetFireProperties( FCEnumFlammability.PLANKS );
		
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockPulley" );
        
        setTickRandomly( true );
        
        setCreativeTab( CreativeTabs.tabRedstone );
	}
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        if ( !world.isRemote )
        {
            FCTileEntityPulley tileEntityPulley = (FCTileEntityPulley)world.getBlockTileEntity( i, j, k );
            
        	if ( player instanceof EntityPlayerMP ) // should always be true
        	{
        		FCContainerPulley container = new FCContainerPulley( player.inventory, tileEntityPulley );
        		
        		FCBetterThanWolves.ServerOpenCustomInterface( (EntityPlayerMP)player, container, FCBetterThanWolves.fcPulleyContainerID );        		
        	}
        }
        
        return true;
    }
    
	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityPulley();
    }

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

    	if ( tileEntity != null )
    	{
    		FCUtilsInventory.EjectInventoryContents( world, i, j, k, (IInventory)tileEntity );
    	}

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }
    
	@Override
    public int tickRate( World world )
    {
    	return iPulleyTickRate;
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = IsBlockOn( world, i, j, k );
    	
    	boolean bStateChanged = false;
    	
    	if ( bOn != bReceivingPower )
    	{
    		/*
	        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	    		"step.gravel", 
	    		2.0F + ( random.nextFloat() * 0.1F ),		// volume 
	    		0.5F + ( random.nextFloat() * 0.1F ) );	// pitch
	        
	        EmitPulleyParticles( world, i, j, k, random );
    		*/
	        
    		SetBlockOn( world, i, j, k, bReceivingPower );
    		
    		bStateChanged = true;
    	}
    	
    	boolean bRedstoneOn = IsRedstoneOn( world, i, j, k );
    	boolean bReceivingRedstone = world.isBlockGettingPowered( i, j, k ) || 
    		world.isBlockGettingPowered( i, j + 1, k );
    	
    	if ( bRedstoneOn != bReceivingRedstone )
    	{
    		/*
	        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	    		"step.gravel", 
	    		2.0F + ( random.nextFloat() * 0.1F ),		// volume 
	    		0.5F + ( random.nextFloat() * 0.1F ) );	// pitch
	        
	        EmitPulleyParticles( world, i, j, k, random );
    		*/
	        
	        SetRedstoneOn( world, i, j, k, bReceivingRedstone );
	        
    		bStateChanged = true;
    	}
    	
    	if ( bStateChanged )
    	{
    		( (FCTileEntityPulley)world.getBlockTileEntity( i, j, k ) ).NotifyPulleyEntityOfBlockStateChange();
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( !IsCurrentStateValid( world, i, j, k ) )
		{
			// verify we have a tick already scheduled to prevent jams on chunk load
			
			if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
			{
		        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );        
			}
		}
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		if ( !IsCurrentStateValid( world, i, j, k ) &&
			!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemGear.itemID, 1, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, Item.goldNugget.itemID, 2, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, Item.ingotIron.itemID, 1, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public boolean IsIncineratedInCrucible()
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
    	return FCUtilsMechPower.IsBlockPoweredByAxle( world, i, j, k, this ) || 
    		FCUtilsMechPower.IsBlockPoweredByHandCrank( world, i, j, k );  	
    }
	
	@Override
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing )
	{
		return iFacing >= 2;
	}
	
	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakPulley( world, i, j, k );
	}

	@Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory(((IInventory) par1World.getBlockTileEntity(par2, par3, par4)));
    }
	
	//------------- Class Specific Methods ------------//
    
    public boolean IsBlockOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 1 ) > 0;    
	}
    
    public void SetBlockOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ); // filter out old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 1;
    	}
    	else
    	{
    		iMetaData &= ~1;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
    public boolean IsRedstoneOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 2 ) > 0;
    }
    
    public void SetRedstoneOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & (~2); // filter out any old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 2;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
	
    void EmitPulleyParticles( World world, int i, int j, int k, Random random )
    {
        for ( int counter = 0; counter < 5; counter++ )
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            
            world.spawnParticle( "smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D );
        }
    }
    
	public void BreakPulley( World world, int i, int j, int k )
	{
		DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	public boolean IsCurrentStateValid( World world, int i, int j, int k )
	{
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = IsBlockOn( world, i, j, k );
    	
    	if ( bReceivingPower != bOn )
    	{
    		return false;
    	}
    	
    	boolean bRedstoneOn = IsRedstoneOn( world, i, j, k );
    	boolean bReceivingRedstone = world.isBlockGettingPowered( i, j, k ) || 
    		world.isBlockGettingPowered( i, j + 1, k );
    	
    	return bRedstoneOn == bReceivingRedstone;    	
	}
}