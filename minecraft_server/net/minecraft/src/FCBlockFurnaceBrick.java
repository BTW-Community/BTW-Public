// FCMOD

// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFurnaceBrick extends FCBlockFurnace
{
    protected final FCModelBlock m_modelBlockInterior = new FCModelBlockFurnaceBrick();
    
    protected final float m_fClickYTopPortion = ( 6F / 16F ); 
    protected final float m_fClickYBottomPortion = ( 6F / 16F ); 
    	
    protected FCBlockFurnaceBrick( int iBlockID, boolean bIsLit )
    {
        super( iBlockID, bIsLit );
        
        SetPicksEffectiveOn();
        
        setHardness( 2F );
        setResistance( 3.33F ); // need to override resistance set in parent
        
        setUnlocalizedName( "fcBlockFurnaceBrick" );        
    }
    
    @Override
    public TileEntity createNewTileEntity( World world )
    {
        return new FCTileEntityFurnaceBrick();
    }

	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        int iMetadata = world.getBlockMetadata( i, j, k );
		int iBlockFacing = iMetadata & 7;
		
		if ( iBlockFacing != iFacing )
		{
			// block is only accessible from front
			
			return false;
		}

    	ItemStack heldStack = player.getCurrentEquippedItem();
        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)world.getBlockTileEntity( i, j, k );        
        ItemStack cookStack = tileEntity.GetCookStack();        
    	
        if ( fYClick > m_fClickYTopPortion )
        {
        	if ( cookStack != null )
        	{
				tileEntity.GivePlayerCookStack( player, iFacing );
				
				return true;
        	}
        	else
        	{
				if ( heldStack != null && IsValidCookItem( heldStack ) )
				{
					if ( !world.isRemote )
					{
						tileEntity.AddCookStack( new ItemStack( heldStack.itemID, 1, heldStack.getItemDamage() ) );
					}
    				
	    			heldStack.stackSize--;
	    			
	    			return true;
				}
        	}
        }
        else if ( fYClick < m_fClickYBottomPortion && heldStack != null )
        {
        	// handle fuel here
        	
    		Item item = heldStack.getItem();    		
			int iItemDamage = heldStack.getItemDamage();
			
    		if ( item.GetCanBeFedDirectlyIntoBrickOven( iItemDamage ) ) 
    		{
    			if ( !world.isRemote )
    			{
    				int iItemsConsumed = tileEntity.AttemptToAddFuel( heldStack );
    				
	                if ( iItemsConsumed > 0 )
	                {
	                	if ( isActive )
	                	{
			                world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
			                	"mob.ghast.fireball", 0.2F + world.rand.nextFloat() * 0.1F, world.rand.nextFloat() * 0.25F + 1.25F );
	                	}
	                	else
	                	{
	        	            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	                    		"random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
	                	}
		                
	        			heldStack.stackSize -= iItemsConsumed;	        			
	                }
    			}
                
    			return true;
    		}
        }
        
		return false;
    }
	
    @Override
    public int quantityDropped( Random rand )
    {
        return 4 + rand.nextInt( 6 );
    }

    @Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return Item.brick.itemID;
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	@Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
		if ( !FCUtilsWorld.DoesBlockHaveSolidTopSurface( world, i, j - 1, k ) )
		{
			return false;
		}
		
        return super.canPlaceBlockAt( world, i, j, k );
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {    	
        if ( !FCUtilsWorld.DoesBlockHaveSolidTopSurface( world, i, j - 1, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify( i, j, k, 0 );
        }
    }
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	int iBlockFacing = blockAccess.getBlockMetadata( i, j, k ) & 7;
    	
    	return iBlockFacing != iFacing;
	}

    @Override
    public void updateFurnaceBlockState( boolean bBurning, World world, int i, int j, int k, boolean bHasContents )
    {
        int iMetadata = world.getBlockMetadata( i, j, k );
        TileEntity tileEntity = world.getBlockTileEntity( i, j, k );
        
        keepFurnaceInventory = true;

        if ( bBurning )
        {
            world.setBlock( i, j, k, FCBetterThanWolves.fcBlockFurnaceBrickBurning.blockID );
        }
        else
        {
            world.setBlock( i, j, k, FCBetterThanWolves.fcBlockFurnaceBrickIdle.blockID );
        }

        keepFurnaceInventory = false;
        
        if ( !bHasContents )
        {
        	iMetadata = iMetadata & 7;
        }
        else
        {
        	iMetadata = iMetadata | 8;
        }
        
        world.SetBlockMetadataWithNotify( i, j, k, iMetadata, 2 );

        if ( tileEntity != null )
        {
            tileEntity.validate();
            world.setBlockTileEntity( i, j, k, tileEntity );
        }
    }

	@Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( !isActive )
		{
	        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)blockAccess.getBlockTileEntity( i, j, k );
	        
	        // uses the visual fuel level rather than the actualy fuel level so this will work on the client
	        
	        if ( tileEntity.GetVisualFuelLevel() > 0 )
	        {
	        	return true;
	        }
		}
		
    	return false;
    }
    
	@Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		if ( !isActive )
		{
	        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)world.getBlockTileEntity( i, j, k );
	        
	        if ( tileEntity.AttemptToLight() )
	        {
	            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
	            	"mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F );
	            
	            return true;
	        }	            
		}
		
		return false;
    }
    
	@Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( !isActive )
		{
	        FCTileEntityFurnaceBrick tileEntity = (FCTileEntityFurnaceBrick)blockAccess.getBlockTileEntity( i, j, k );
	        
	        if ( tileEntity.HasValidFuel() )
	        {
				return 60; // same chance as leaves and other highly flammable objects
	        }
		}
		
		return 0;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
    @Override
	protected int IDDroppedSilkTouch()
	{
		return FCBetterThanWolves.fcBlockFurnaceBrickIdle.blockID;
	}
	
    @Override
    public boolean GetIsBlockWarm( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return isActive;
    }
    
    @Override
    public boolean DoesBlockHopperInsert( World world, int i, int j, int k )
    {
    	return true;
    }
    
	//------------- Class Specific Methods ------------//

	public boolean IsValidCookItem( ItemStack stack )
	{
		if ( FurnaceRecipes.smelting().getSmeltingResult( stack.getItem().itemID ) != null )
		{
			return true;
		}
		
		return false;
	}
	
	//----------- Client Side Functionality -----------//
}
