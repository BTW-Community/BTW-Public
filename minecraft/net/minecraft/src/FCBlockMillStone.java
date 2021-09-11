// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockMillStone extends BlockContainer
	implements FCIBlockMechanical 
{
	private static int m_iTickRate = 10;
	
	public static final int m_iContentsNothing = 0;
	public static final int m_iContentsNormalGrinding = 1;
	public static final int m_iContentsNetherrack = 2;
	public static final int m_iContentsCompanionCube = 3;
	public static final int m_iContentsJammed = 4;
	
	public static final FCModelBlockMillStone m_model = new FCModelBlockMillStone();
	
    protected FCBlockMillStone( int iBlockID )
    {
	    super( iBlockID, Material.rock );
	    
        setHardness( 3.5F );
        
        setStepSound( soundStoneFootstep );
        setUnlocalizedName( "fcBlockMillStone" );
        
        setTickRandomly( true );
        
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
    public int tickRate( World world )
    {
    	return m_iTickRate;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {		
		int iState = this.GetCurrentGrindingType( world, i, j, k );
		
        FCTileEntityMillStone tileEntity = (FCTileEntityMillStone)world.getBlockTileEntity( i, j, k );
        
		if ( iState == m_iContentsNothing )
		{
	    	ItemStack heldStack = player.getCurrentEquippedItem();

	    	if ( heldStack != null )
	    	{
				if ( !world.isRemote )
				{
			        world.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, i, j, k, 0 );
			        
					tileEntity.AttemptToAddSingleItemFromStack( heldStack );
				}
				else
				{				
					heldStack.stackSize--;
				}
	    	}
		}
		else if ( !world.isRemote )
    	{
	        world.playAuxSFX( FCBetterThanWolves.m_iItemCollectionPopSoundAuxFXID, i, j, k, 0 );
	        
			tileEntity.EjectContents( iFacing );
    	}
		
		return true;		
    }

	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        FCUtilsInventory.EjectInventoryContents( world, i, j, k, (IInventory)world.getBlockTileEntity( i, j, k ) );

        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }

	@Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityMillStone();
    }

	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = GetIsMechanicalOn( world, i, j, k );
    	
    	if ( bOn != bReceivingPower )
    	{
    		SetIsMechanicalOn( world, i, j, k, bReceivingPower );
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
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	FCUtilsRayTraceVsComplexBlock rayTrace = new FCUtilsRayTraceVsComplexBlock( world, i, j, k, startRay, endRay );
    	
    	m_model.AddToRayTrace( rayTrace );
    	
    	rayTrace.AddBoxWithLocalCoordsToIntersectionList( m_model.m_boxBase );    	
		
        return rayTrace.GetFirstIntersection();
    }

    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return true;
	}
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing == 0 || bIgnoreTransparency;
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
		return iFacing < 2;
	}
	@Override
    public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
	public void Overpower( World world, int i, int j, int k )
	{
		BreakMillStone( world, i, j, k );
	}
	
	@Override
	public boolean hasComparatorInputOverride()
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return ((FCTileEntityMillStone) par1World.getBlockTileEntity(par2, par3, par4)).m_stackMilling == null ? 0 : 15;
    }
	
    //------------- Class Specific Methods ------------//
    
    public boolean GetIsMechanicalOn( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsMechanicalOn( blockAccess.getBlockMetadata( i, j, k ) );    
	}
    
    public boolean GetIsMechanicalOn( int iMetadata )
    {
    	return ( iMetadata & 1 ) > 0;    
    }
    
    public void SetIsMechanicalOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetadata = SetIsMechanicalOn( world.getBlockMetadata( i, j, k ), bOn );
    	
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public int SetIsMechanicalOn( int iMetadata, boolean bOn )
    {
    	if ( bOn )
    	{
            return iMetadata | 1;
    	}
    	
    	return iMetadata & (~1);
    }
    
    public int GetCurrentGrindingType( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetCurrentGrindingType( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetCurrentGrindingType( int iMetadata )
    {
    	return ( iMetadata & 14 ) >> 1;
    }
    
    public void SetCurrentGrindingType( World world, int i, int j, int k, int iGrindingType )
    {
    	int iMetadata = SetCurrentGrindingType( world.getBlockMetadata( i, j, k ), iGrindingType );
    	
		world.setBlockMetadataWithClient( i, j, k, iMetadata );
    }
    
    public int SetCurrentGrindingType( int iMetadata, int iGrindingType )
    {
    	iMetadata &= ~14; // filter out old state
    	
    	return iMetadata | ( iGrindingType << 1 );    	
    }
    
	private void BreakMillStone( World world, int i, int j, int k )
	{
    	DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStone.itemID, 16, 0, 0.75F );
		
        world.playAuxSFX( FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0 );							        
        
		world.setBlockWithNotify( i, j, k, 0 );
	}
	
	public boolean IsCurrentStateValid( World world, int i, int j, int k )
	{
    	boolean bReceivingPower = IsInputtingMechanicalPower( world, i, j, k );
    	boolean bOn = GetIsMechanicalOn( world, i, j, k );
    	
    	return bOn == bReceivingPower;    	
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_iconsBySide = new Icon[6];
    private Icon[] m_iconsBySideFull = new Icon[6];
    private Icon[] m_iconsBySideOn = new Icon[6];
    private Icon[] m_iconsBySideOnFull = new Icon[6];
    
    private boolean m_bRenderingBase = false;

	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" ); // for hit effects
        
        m_iconsBySideFull[0] = m_iconsBySide[0] = register.registerIcon( "fcBlockMillStone_bottom" );
        
        m_iconsBySideFull[1] = m_iconsBySide[1] = register.registerIcon( "fcBlockMillStone_top" );
        
        m_iconsBySideOn[0] = m_iconsBySideOnFull[0] = register.registerIcon( "fcBlockMillStone_bottom_on" );
    	m_iconsBySideOn[1] = m_iconsBySideOnFull[1] = register.registerIcon( "fcBlockMillStone_top_on" );
    		
        Icon sideIcon = register.registerIcon( "fcBlockMillStone_side" );
        Icon sideIconFull = register.registerIcon( "fcBlockMillStone_side_full" );
        
        Icon sideIconOn = register.registerIcon( "fcBlockMillStone_side_on" );
        Icon sideIconOnFull = register.registerIcon( "fcBlockMillStone_side_on_full" );
        
        for ( int iTempSide = 2; iTempSide <= 5; iTempSide++ )
        {
        	m_iconsBySide[iTempSide] = sideIcon;
        	m_iconsBySideFull[iTempSide] = sideIconFull;
        	
        	m_iconsBySideOn[iTempSide] = sideIconOn;
        	m_iconsBySideOnFull[iTempSide] = sideIconOnFull;
        }
    }
    
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( m_bRenderingBase )
		{
			return m_iconsBySide[iSide];
		}
		else if ( GetIsMechanicalOn( iMetadata ) )
		{
			if ( GetCurrentGrindingType( iMetadata ) == m_iContentsNothing )
			{
				return m_iconsBySideOn[iSide];
			}
			else
			{
				return m_iconsBySideOnFull[iSide];
			}
		}
		else
		{
			if ( GetCurrentGrindingType( iMetadata ) == m_iContentsNothing )
			{
				return m_iconsBySide[iSide];
			}
			else
			{
				return m_iconsBySideFull[iSide];
			}
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
    	m_bRenderingBase = true;
    	
        m_model.m_boxBase.RenderAsBlock( renderBlocks, this, i, j, k );
        
    	m_bRenderingBase = false;
    	
    	return m_model.RenderAsBlock( renderBlocks, this, i, j, k );
    }
    
    @Override
    public void RenderBlockAsItem( RenderBlocks renderBlocks, int iItemDamage, float fBrightness )
    {
    	m_bRenderingBase = true;
    	
        m_model.m_boxBase.RenderAsItemBlock( renderBlocks, this, iItemDamage );
        
    	m_bRenderingBase = false;
    	
    	m_model.RenderAsItemBlock( renderBlocks, this, iItemDamage );
    }
    
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random random )
    {
    	if ( GetIsMechanicalOn( world, i, j, k ) )
    	{
	        int iCurrentGrindingType = GetCurrentGrindingType( world, i, j, k );
	        
	    	EmitMillingParticles( world, i, j, k, iCurrentGrindingType, random );
	        
            if ( iCurrentGrindingType == m_iContentsJammed )
            {
		        world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		    		"minecart.base", 
		    		1.0F + ( world.rand.nextFloat() * 0.1F ),	// volume 
		    		1.25F + ( world.rand.nextFloat() * 0.1F ) );	// pitch
            }
            else if ( iCurrentGrindingType != m_iContentsNothing )
            {
		        world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		    		"minecart.base", 
		    		1.0F + ( world.rand.nextFloat() * 0.1F ),	// volume 
		    		0.75F + ( world.rand.nextFloat() * 0.1F ) );	// pitch
            }
            else if ( random.nextInt( 2 ) == 0 )
	        {
		        world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		    		"minecart.base", 
		    		1.5F + ( random.nextFloat() * 0.1F ),	// volume 
		    		0.5F + ( random.nextFloat() * 0.1F ) );	// pitch
	        }
	        
	        if ( iCurrentGrindingType == m_iContentsNetherrack )
	        {
		        if ( random.nextInt( 3 ) <= 1 )
		        {
			        world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
		        		"mob.ghast.scream", 0.75F, random.nextFloat() * 0.4F + 0.8F );
		        }
	        }
	        else if ( iCurrentGrindingType == m_iContentsCompanionCube )
	        {
		        if ( random.nextInt( 3 ) <= 1 )
		        {
		            world.playSound( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 
		        		"mob.wolf.hurt", 2F, ( random.nextFloat() - random.nextFloat() ) * 0.2F + 1F );
		        }
	        }            
    	}
    }
    	
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
    {
		AxisAlignedBB transformedBox = m_model.m_boxSelection.MakeTemporaryCopy();
		
		transformedBox.offset( i, j, k );
		
		return transformedBox;		
    }
	
	@Override
	public void ClientNotificationOfMetadataChange( World world, int i, int j, int k, int iOldMetadata, int iNewMetadata )
	{
		if ( !GetIsMechanicalOn( iOldMetadata ) && GetIsMechanicalOn( iNewMetadata ) )
		{
			// mech power turn on
			
			int iGrindType = GetCurrentGrindingType( iNewMetadata );
			
            if ( iGrindType == m_iContentsCompanionCube )
            {                
                world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
                		"mob.wolf.hurt", 5F, ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.2F + 1F );
            }
            
	        world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	    		"minecart.base", 
	    		1.5F + ( world.rand.nextFloat() * 0.1F ),	// volume 
	    		0.5F + ( world.rand.nextFloat() * 0.1F ) );	// pitch
	        
	        EmitMillingParticles( world, i, j, k, iGrindType, world.rand );
		}
	}
        
    private void EmitMillingParticles( World world, int i, int j, int k, int iGrindType, Random rand )
    {
    	String sParticle;
    	
    	if ( iGrindType == m_iContentsNothing )
    	{
    		sParticle = "smoke";
    	}
    	else if ( iGrindType == m_iContentsJammed )
    	{
    		sParticle = "largesmoke";
    	}
    	else
    	{
    		sParticle = "fcwhitesmoke";
    	}
    	
        for ( int iTempCount = 0; iTempCount < 5; iTempCount++ )
        {
            float smokeX = (float)i + rand.nextFloat();
            float smokeY = (float)j + rand.nextFloat() * 0.5F + 1F;
            float smokeZ = (float)k + rand.nextFloat();
            
            world.spawnParticle( sParticle, smokeX, smokeY, smokeZ, 0D, 0D, 0D );
        }
    }    
}
