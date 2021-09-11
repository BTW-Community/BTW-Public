// FCMOD

package net.minecraft.src;

public class FCBlockLog extends BlockLog
{
    protected FCBlockLog( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );

	    setHardness( 1.25F ); // vanilla 2
	    setResistance( 3.33F );  // odd value to match vanilla resistance set through hardness of 2
        
		SetAxesEffectiveOn();
		SetChiselsEffectiveOn();
		
        SetBuoyant();
        
		SetCanBeCookedByKiln( true );
		SetItemIndexDroppedWhenCookedByKiln( 256 + 7 ); // Item.coal.itemID not initialized yet
		SetItemDamageDroppedWhenCookedByKiln( 1 ); // charcoal
		
		SetFireProperties( FCEnumFlammability.LOGS );
		
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "log" );        
    }    
    
    @Override
    public float getBlockHardness( World world, int i, int j, int k )
    {
    	float fHardness = super.getBlockHardness( world, i, j, k );
    	
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( GetIsStump( iMetadata ) )
    	{   
    		fHardness *= 3;    		
    	}
    	
        return fHardness; 
    }
    
    @Override
    public boolean GetIsProblemToRemove( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsStump( blockAccess, i, j, k );
    }
    
    @Override
    public boolean GetDoesStumpRemoverWorkOnBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsStump( blockAccess, i, j, k );
    }
    
    @Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	int iOldMetadata = world.getBlockMetadata( i, j, k );
    	int iNewMetadata = 0;
    	
    	if ( GetIsStump( iOldMetadata ) )
    	{
    		if ( IsWorkStumpItemConversionTool( stack, world, i, j, k ) )
			{
    	        world.playAuxSFX( FCBetterThanWolves.m_iShaftRippedOffLogAuxFXID, i, j, k, 0 );
    	        
    	    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockWorkStump.blockID, iOldMetadata & 3 );
    	    	
    	    	return true;    	    	
			}
    		else
    		{
    			iNewMetadata = FCBetterThanWolves.fcBlockLogDamaged.SetIsStump( iNewMetadata );
    		}
    	}
    	else
    	{
    		int iOrientation = ( iOldMetadata >> 2 ) & 3;
    		
        	iNewMetadata = FCBetterThanWolves.fcBlockLogDamaged.SetOrientation( iNewMetadata, iOrientation );
    	}    	
    		
    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockLogDamaged.blockID, iNewMetadata );
    	
    	if ( !world.isRemote )
    	{
            FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemBark, 1, iOldMetadata & 3 ), iFromSide );
    	}
    	
    	return true;
    }
    
    @Override
    public boolean GetCanBlockBeIncinerated( World world, int i, int j, int k )
    {
    	return !GetIsStump( world, i, j, k );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 6, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemBark.itemID, 1, iMetadata & 3, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
		ConvertToSmouldering( world, i, j, k );
    }
    
	@Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
        int iAxisAlignment = iMetadata & 0xc;
        
        if ( iAxisAlignment != 0 )
        {
	        if ( iAxisAlignment == 4 )
	        {
	        	iAxisAlignment = 8;
	        }
	        else if ( iAxisAlignment == 8 )
	        {
	        	iAxisAlignment = 4;
	        }
	        
	        iMetadata = ( iMetadata & (~0xc) ) | iAxisAlignment;
        }
        
        return iMetadata;
	}

    @Override
    public int GetCookTimeMultiplierInKiln( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 8;
    }
    
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
    	return FCBlockPlanks.GetFurnaceBurnTimeByWoodType( iItemDamage ) * 4;
    }
    
    //------------- Class Specific Methods ------------//
	
	public void ConvertToSmouldering( World world, int i, int j, int k )
	{
		int iNewMetadata = FCBetterThanWolves.fcBlockLogSmouldering.SetIsStump( 0, GetIsStump( world, i, j, k ) );
		
		world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockLogSmouldering.blockID, iNewMetadata );
	}
    
    public boolean GetIsStump( int iMetadata )
    {
    	return ( iMetadata & 12 ) == 12;
    }    

    public boolean GetIsStump( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iBlockID = blockAccess.getBlockId( i, j, k );
    	
    	if ( iBlockID == Block.wood.blockID )
    	{
    		int iMetadata = blockAccess.getBlockMetadata( i, j, k );
    		
    		if ( GetIsStump( iMetadata ) )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean IsDeadStump( World world, int i, int j, int k )
    {
    	if ( GetIsStump( world, i, j, k ) )
		{
    		int iBlockAboveID = world.getBlockMetadata( i, j + 1, k );
    		
        	if ( iBlockAboveID != Block.wood.blockID )
        	{
        		return true;
        	}
		}
    	
    	return false;    	
    }

    public boolean IsWorkStumpItemConversionTool( ItemStack stack, World world, int i, int j, int k )
    {
    	if ( stack != null && stack.getItem() instanceof FCItemChisel )
    	{
			int iToolLevel = ((FCItemChisel)stack.getItem()).toolMaterial.getHarvestLevel();
			
			return iToolLevel >= 2;
    	}  
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
    
    public static final String[] trunkTextureTypes = new String[] {"fcBlockTrunkOak", "fcBlockTrunkSpruce", "fcBlockTrunkBirch", "fcBlockTrunkJungle"};
    private Icon[] trunkIconArray;
    private Icon trunkTopIcon;
    
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {    	
    	if ( ( iMetadata & 12 ) == 12 )
    	{    		
    		if ( iSide > 1 )
    		{
    			return trunkIconArray[iMetadata & 3];
    		}
    		else
    		{
    			return trunkTopIcon;
    		}
    	}
    	
		return super.getIcon( iSide, iMetadata );
    }    
    
    @Override
    public void registerIcons( IconRegister iconRegister )
    {
    	trunkTopIcon = iconRegister.registerIcon( "fcBlockTrunkTop" );
    	
    	trunkIconArray = new Icon[trunkTextureTypes.length];

        for (int iTextureID = 0; iTextureID < trunkIconArray.length; iTextureID++ )
        {
        	trunkIconArray[iTextureID] = iconRegister.registerIcon(trunkTextureTypes[iTextureID]);
        }
        
        super.registerIcons( iconRegister );
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
        renderer.setRenderBounds( 0D, 0D, 0D, 1D, 1D, 1D );
        
    	return renderer.renderBlockLog( this, i, j, k );
    }    

    @Override
    public void RenderBlockSecondPass( RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult )
    {
        RenderCookingByKilnOverlay( renderBlocks, i, j, k, bFirstPassResult );
    }
}