// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockFlowerPot extends BlockFlowerPot
{
	protected static final double m_dHeight = ( 6 / 16D );
	protected static final double m_dWidth = ( 6 / 16D );
	protected static final double m_dHalfWidth = ( m_dWidth / 2D );
	
    public FCBlockFlowerPot( int iBlockID )
    {
        super( iBlockID );
        
        InitBlockBounds( 0.5F - m_dHalfWidth, 0.0F, 0.5F - m_dHalfWidth, 
        	0.5F + m_dHalfWidth, m_dHeight, 0.5F + m_dHalfWidth );
    }

    @Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iSide, float fClickX, float fClickY, float fClickZ )
    {
        ItemStack playerStack = player.inventory.getCurrentItem();

        if ( playerStack != null && world.getBlockMetadata(i, j, k) == 0 )
        {
            int iMetadataForStack = GetMetadataForItemStack( playerStack );

            if ( iMetadataForStack > 0 )
            {
                world.SetBlockMetadataWithNotify( i, j, k, iMetadataForStack, 2 );

                if ( !player.capabilities.isCreativeMode )
                {
                	playerStack.stackSize--;
                	
                	if ( playerStack.stackSize <= 0 )
                	{                	
                		player.inventory.setInventorySlotContents( player.inventory.currentItem, null );
                	}
                }

                return true;
            }
        }
        
        return false;
    }

    @Override
    public int getDamageValue( World world, int i, int j, int k )
    {
        ItemStack stack = GetPlantStackForMetadata( world.getBlockMetadata(i, j, k ) );
        
        if ( stack == null )
        {
        	return Item.flowerPot.itemID; 
        }
        
        return stack.getItemDamage();
    }

    @Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
    	// this portion copied from Block class since we can't call super.super
    	
        if ( !world.isRemote )
        {
            int iQuantityDropped = quantityDroppedWithBonus( iFortuneModifier, world.rand );

            for ( int iDropCount = 0; iDropCount < iQuantityDropped; ++iDropCount )
            {
                if ( world.rand.nextFloat() <= fChance )
                {
                    int itemID = idDropped( iMetadata, world.rand, iFortuneModifier );

                    if (itemID > 0)
                    {
                        dropBlockAsItem_do( world, i, j, k, new ItemStack( itemID, 1, damageDropped(iMetadata ) ) );
                    }
                }
            }
        }

        if ( iMetadata > 0 )
        {
            ItemStack stack = GetPlantStackForMetadata( iMetadata );

            if ( stack != null )
            {
                dropBlockAsItem_do( world, i, j, k, stack );
            }
        }
    }

    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
    //------------- Class Specific Methods ------------//

    private ItemStack GetPlantStackForMetadata( int iMetadata )
    {
    	if ( iMetadata == 7 )
    	{
            return new ItemStack( FCBetterThanWolves.fcItemMushroomRed );
    	}
    	else if ( iMetadata == 8 )
    	{
            return new ItemStack( FCBetterThanWolves.fcItemMushroomBrown );
    	}
    	else
    	{
    		return getPlantForMeta( iMetadata );
    	}
    }

    private int GetMetadataForItemStack( ItemStack stack )
    {
        int itemID = stack.getItem().itemID;
        
        if ( itemID == FCBetterThanWolves.fcItemMushroomRed.itemID )
        {
        	return 7;
        }
        else if ( itemID == FCBetterThanWolves.fcItemMushroomBrown.itemID )
        {
        	return 8;
        }
        else
        {
        	return getMetaForPlant( stack );
        }
    }
    
	//----------- Client Side Functionality -----------//
}
