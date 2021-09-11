// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCocoa extends BlockCocoa
{
    public FCBlockCocoa( int iBlockID )
    {
        super( iBlockID );
        
        SetAxesEffectiveOn( true );        
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	// override to prevent growth in the end and reduce overall growth rate
    	
        if ( !canBlockStay( world, i, j, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            
            world.setBlockWithNotify(i, j, k, 0);
        }        
        else if ( world.provider.dimensionId != 1 )
    	{
            if ( world.rand.nextInt( 20 ) == 0 )
            {
                int iMetadata = world.getBlockMetadata( i, j, k );
                int iGrowthLevel= func_72219_c( iMetadata );

                if ( iGrowthLevel < 2 )
                {
                    iGrowthLevel++;
                    
                    world.setBlockMetadataWithNotify( i, j, k, iGrowthLevel << 2 | getDirection( iMetadata ) );
                }
            }
    	}    	
    }    
    
    @Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
        int iGrowthState = func_72219_c( iMetadata );
        
        int iNumDropped = 0;

        if ( iGrowthState >= 2 )
        {
            iNumDropped = 1;

        	if ( world.rand.nextInt( 4 ) - iFortuneModifier <= 0 )
        	{
        		iNumDropped = 2;
        	}
        }

        for ( int iTempCount = 0; iTempCount < iNumDropped; ++iTempCount )
        {
            dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemCocoaBeans, 1, 0 ) );
        }
    }

    @Override
    public int getDamageValue(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
		// need to special case as this block doesn't use IDDropped, and damageDropped
		
		return new ItemStack( FCBetterThanWolves.fcItemCocoaBeans.itemID, 1, 0 );			
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iMetadata = blockAccess.getBlockMetadata(i, j, k);
        
        int var6 = getDirection( iMetadata );
        int var7 = func_72219_c( iMetadata );
        
        int var8 = 4 + var7 * 2;
        int var9 = 5 + var7 * 2;
        
        float var10 = (float)var8 / 2F;

        switch (var6)
        {
            case 0:
            	
            	return AxisAlignedBB.getAABBPool().getAABB( 
            		(8F - var10) / 16F, (12F - (float)var9) / 16F, (15F - (float)var8) / 16F, 
            		(8F + var10) / 16F, 0.75F, 0.9375F );

            case 1:
            	
            	return AxisAlignedBB.getAABBPool().getAABB( 
            		0.0625F, (12F - (float)var9) / 16F, (8F - var10) / 16F, 
            		(1F + (float)var8) / 16F, 0.75F, (8F + var10) / 16F );

            case 2:
            	
            	return AxisAlignedBB.getAABBPool().getAABB( 
            		(8F - var10) / 16F, (12F - (float)var9) / 16F, 0.0625F, 
            		(8F + var10) / 16F, 0.75F, (1F + (float)var8) / 16F );

            default: // 3
            	
            	return AxisAlignedBB.getAABBPool().getAABB( 
            		(15F - (float)var8) / 16F, (12F - (float)var9) / 16F, (8F - var10) / 16F, 
            		0.9375F, 0.75F, (8F + var10) / 16F );
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }
    
	//----------- Client Side Functionality -----------//
}