// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFurnace extends BlockFurnace
{
    protected FCBlockFurnace( int iBlockID, boolean bIsLit )
    {
        super( iBlockID, bIsLit );
        
        setStepSound( soundStoneFootstep );
        
        setHardness( 3F );
        setResistance( 5.83F ); // odd value to match vanilla hardness of 3.5F
        
        if ( !bIsLit )
        {
        	setCreativeTab( CreativeTabs.tabDecorations );
        }
        else
        {
        	setLightValue( 0.875F );        
    	}
        
        setUnlocalizedName( "furnace" );        
    }
    
    @Override
    public int quantityDropped( Random rand )
    {
        return 12 + rand.nextInt( 5 );
    }

    @Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcItemStone.itemID;
    }
    
	@Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
		// override to handle funkiness of silk-touching a container block
		
        player.addStat( StatList.mineBlockStatArray[this.blockID], 1 );
        player.addExhaustion( 0.025F );

        if ( EnchantmentHelper.getSilkTouchModifier( player ) )
        {
            ItemStack dropStack = new ItemStack( IDDroppedSilkTouch(), 1, 0 );

            if ( dropStack != null )
            {
                dropBlockAsItem_do( world, i, j, k, dropStack );
            }
        }
        else
        {
            int iFortuneModifier = EnchantmentHelper.getFortuneModifier( player );
            
            dropBlockAsItem( world, i, j, k, iMetadata, iFortuneModifier );
        }
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
		/*
		if ( isActive )
		{
	    	ItemStack stack = player.getCurrentEquippedItem();
	    	
    		if ( stack != null && stack.getItem().GetCanItemBeSetOnFireOnUse( stack.getItemDamage() ) )
    		{
    			return false;
    		}
    	}
    	*/
    	
    	return super.onBlockActivated( world, i, j, k, player, iFacing, fXClick, fYClick, fZClick );
    }
	
    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return isActive;
    }
    
    @Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
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
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		int iFacing = iMetadata & 7;
		
		iFacing = Block.RotateFacingAroundJ( iFacing, bReverse );
		
		return ( iMetadata & (~7) ) | iFacing;
	}
	
	//------------- Class Specific Methods ------------//
	
	protected int IDDroppedSilkTouch()
	{
		return Block.furnaceIdle.blockID;
	}
	
	//----------- Client Side Functionality -----------//

    protected Icon m_IconFullFront;
    protected Icon m_IconFullFrontLit;
    
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		int iMetadataStripped = iMetadata & 7;
		boolean bHasContents = ( iMetadata & 8 ) != 0;
		
		if ( iMetadataStripped == iSide && bHasContents )
		{
			if ( isActive )
			{
				return m_IconFullFrontLit;
			}
			else
			{
				return m_IconFullFront;
			}
		}
		
		return super.getIcon( iSide, iMetadataStripped );
    }
	
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons(  register );

		m_IconFullFront = register.registerIcon( "fcBlockFurnaceFullFront" );
		m_IconFullFrontLit = register.registerIcon( "fcBlockFurnaceFullFrontLit" );
    }
	
    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        if ( isActive && rand.nextInt( 3 ) == 0 )
        {
            world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.fire", 
            	0.25F + rand.nextFloat() * 0.25F, 
            	0.5F + rand.nextFloat() * 0.25F, false );
        }
        
        super.randomDisplayTick( world, i, j, k, rand );
    }
}