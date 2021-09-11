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
}